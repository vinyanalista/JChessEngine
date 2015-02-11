// Commands from the engine to xboard
// http://www.gnu.org/software/xboard/engine-intf.html#9

package br.com.vinyanalista.jchessengine;

import java.io.*;
import java.util.StringTokenizer;
import java.util.regex.*;

public class XBoard {
	/**
     * A regular expression for a move in coordinate notation (eg. e2e4 or g7g8q)
     * Groups:
     * 1 => file of source square
     * 2 => rank of source square
     * 3 => file of target square
     * 4 => rank of target square
     * 5 => character code of promotional piece, if any
     */
	protected static final Pattern COORDINATE_MOVE_PATTERN = Pattern
			.compile("([a-h][1-8])([a-h][1-8])([qrnbQRNB])?");
	
	/**
     * A regular expression for specifying time formats
     * Groups:
     * 1 => Moves per session
     * 2 => Base time minutes
     * 3 => Base time seconds, if any
     * 4 => Increment time in seconds
     */
	protected static Pattern TIME_CONTROLS_PATTERN = Pattern
			.compile("(\\d+) (\\d+)(?:\\:(\\d{2}))? (\\d+)");
	
	protected static JChessEngine engine;
	protected static final BufferedReader input;
	
	static {
		input = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/**
	 * <b>askuser REPTAG MESSAGE</b> 
	 *
	 * <p>Here REPTAG is a string containing no whitespace,
	 * and MESSAGE consists of any characters, including whitespace, to the end
	 * of the line. xboard pops up a question dialog that says MESSAGE and has a
	 * typein box. If the user types in "bar", xboard sends "REPTAG bar" to the
	 * engine. The user can cancel the dialog and send nothing.</p>
	 */
	public static String askUser(String message) {
		send("askuser ANSWER " + message);
		try {
			String answer = readFromInput();
			return answer.substring(answer.indexOf(' '));
		} catch (IOException e) {
			// e.printStackTrace();
			engine.debug(e.getMessage());
			return null;
		}
	}
	
	public static boolean feature(String features) {
		send("feature " + features);
		try {
			String response = readFromInput();
			return response.contains("accepted");
		} catch (IOException e) {
			// e.printStackTrace();
			engine.debug(e.getMessage());
			return false;
		}
	}

	private static void handle(String input) {
		if (COORDINATE_MOVE_PATTERN.matcher(input).matches()) {
			engine.opponentMove(input);
			return;
		}
		StringTokenizer st = new StringTokenizer(input);
		String command = st.nextToken();
		if (command.equals("?")) {
			engine.moveNow();
		} else if (command.equals("black")) {
			engine.black();
		} else if (command.equals("computer")) {
			engine.computer();
		} else if (command.equals("debug")) {
			boolean on = st.nextToken().equals("on");
			engine.setDebug(on);
		} else if (command.equals("easy")) {
			engine.setPondering(false);
		} else if (command.equals("force")) {
			engine.force();
		} else if (command.equals("go")) {
			engine.go();
		} else if (command.equals("hard")) {
			engine.setPondering(true);
		} else if (command.equals("level")) {
			Matcher matcher = TIME_CONTROLS_PATTERN.matcher(input.substring(6));
			if (matcher.matches()) {
				try {
                    // Get moves per session (0 if not tournament mode)
                    int movesPerSession = Integer.parseInt(matcher.group(1));
                    // Get base time in minutes
                    long baseTime = Long.parseLong(matcher.group(2)) * 60 * 1000;
                    // Add seconds component, if any
                    if(matcher.group(3) != null)
                    {
                        baseTime += Long.parseLong(matcher.group(3)) * 1000;
                    }
                    // Get move increment from seconds
                    long increment = Long.parseLong(matcher.group(4)) * 1000;
                    engine.setTimeControls(movesPerSession, baseTime, increment);
				} catch (NumberFormatException e) {
                }
			}
		} else if (command.equals("new")) {
			engine.newGame();
		} else if (command.equals("nopost")) {
			engine.setShowThinking(false);
		} else if (command.equals("otim")) {
			int time = Integer.parseInt(st.nextToken());
			engine.setOpponentTime(time);
		} else if (command.equals("perft")) {
			int depth = Integer.parseInt(input.substring(6));
			engine.performanceTest(depth);
		} else if (command.equals("post")) {
			engine.setShowThinking(true);
		} else if (command.equals("protover")) {
			int protocolVersion = Integer.parseInt(st.nextToken());
			engine.setProtocolVersion(protocolVersion);
		} else if (command.equals("quit")) {
			engine.quit();
		} else if (command.equals("random")) {
			engine.random();
		} else if (command.equals("remove")) {
			engine.remove();
		} else if (command.equals("resign")) {
			engine.resign();
		} else if (command.equals("sd")) {
			int depth = Integer.parseInt(input.substring(3)); // After "sd "
			engine.setSearchDepth(depth);
		} else if (command.equals("setboard")) {
			String fen = input.substring(9); // After "setboard "
			engine.setBoard(fen);
		} else if (command.equals("st")) {
			int time = Integer.parseInt(st.nextToken());
			engine.setTimeControls(time);
		} else if (command.equals("time")) {
			try {
				// the time X commands sends X centiseconds
	            long time = Long.parseLong(st.nextToken()) * 10;
				engine.setTime(time);
			} catch (NumberFormatException e) {
            }
		} else if (command.equals("undo")) {
			engine.undo();
		} else if (command.equals("xboard")) {
			return;
		} else if (command.equals("white")) {
			engine.white();
		} else {
			engine.unrecognizedCommand(input);
		}
	}
	
	public static void illegalMove() {
		send("Illegal Move");
	}

	public static void listen() {
		do {
			try {
				String command = readFromInput();
				if (command == null)
					continue;
				handle(command);
				if (command.equals("quit")) {
					break;
				}
			} catch (IOException e) {
				// e.printStackTrace();
				engine.debug(e.getMessage());
			}
		} while (true);
	}
	
	public static void move(String move) {
		send("move " + move);
	}
	
	private static String readFromInput() throws IOException {
		return input.readLine();
	}

	public static void send(String command) {
		System.out.println(command);
		System.out.flush();
	}
	
	public static void setEngine(JChessEngine engine) {
		XBoard.engine = engine;
	}
	
	/**
     * Show AI thoughts to the user
     *
     * @param   depth       the deeepest ply that the normal search reached
     * @param   sdepth      the deeepest ply that the selective search reached
     * @param   score       the score of evaluation of the principal variation
     * @param   nodes       the number of nodes searched
     * @param   evals       the number of nodes evaluated
     * @param   msec        the time required, in milliseconds
     * @param   pv          the principal variation of the search
     */
    public static void showThinking(int depth, int sdepth, int score, int msec, int nodes, int evals, String pv) {
        send(depth + " " + score + " " + (msec/10) + " " + nodes + " " + pv);
    }
	
	/**
	 * <b>telluser MESSAGE</b>
	 * 
	 * <p>xboard pops up an information dialog containing the
	 * message. MESSAGE consists of any characters, including whitespace, to the
	 * end of the line.</p>
	 */
	public static void tellUser(String message) {
		send("telluser " + message);
	}
	
	/**
	 * <b>tellusererror MESSAGE</b>
	 * 
	 * <p>xboard pops up an error dialog containing the
	 * message. MESSAGE consists of any characters, including whitespace, to the
	 * end of the line.</p>
	 */
	public static void tellUserError(String message) {
		send("tellusererror " + message);
	}

}