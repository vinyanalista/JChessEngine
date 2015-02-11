// Commands from xboard to the engine
// http://www.gnu.org/software/xboard/engine-intf.html#8

package br.com.vinyanalista.jchessengine;

public interface JChessEngine {
	/**
	 * <b>black</b>
	 * 
	 * <p>(This command is obsolete as of protocol version 2, but is still sent in
	 * some situations to accommodate older engines unless you disable it with
	 * the feature command.) Set Black on move. Set the engine to play White.
	 * Stop clocks.</p>
	 */
	public void black();

	/**
	 * <b>computer</b>
	 * 
	 * <p>The opponent is also a computer chess engine. Some engines alter
	 * their playing style when they receive this command.</p>
	 */
	public void computer();

	public void debug(String message);

	/**
	 * <b>force</b>
	 * 
	 * <p>Set the engine to play neither color ("force mode"). Stop clocks.
	 * The engine should check that moves received in force mode are legal and
	 * made in the proper turn, but should not think, ponder, or make moves of
	 * its own.</p>
	 */
	public void force();

	/**
	 * <b>go</b>
	 * 
	 * <p>Leave force mode and set the engine to play the color that is on move.
	 * Associate the engine's clock with the color that is on move, the
	 * opponent's clock with the color that is not on move. Start the engine's
	 * clock. Start thinking and eventually make a move.</p>
	 */
	public void go();
	
	/**
	 * <b>?</b>
	 * 
	 * <p>Interrupt search and Move Now!</p>
	 */
	public void moveNow();
	// TODO Verificar ? (não parece ser um comando padrão)

	/**
	 * <b>new</b>
	 * 
	 * <p>Reset the board to the standard chess starting position. Set White on
	 * move. Leave force mode and set the engine to play Black. Associate the
	 * engine's clock with Black and the opponent's clock with White. Reset
	 * clocks and time controls to the start of a new game. Use wall clock for
	 * time measurement. Stop clocks. Do not ponder on this move, even if
	 * pondering is on. Remove any search depth limit previously set by the sd
	 * command.</p>
	 */
	public void newGame();
	
	public void opponentMove(String opponentMove);
	
	/**
	 * <b>perft DEPTH</b>
	 * 
	 * <p>Runs a performance test to depth DEPTH</p>
	 */
	public void performanceTest(int depth);
	// TODO Verificar PERFT (não parece ser um comando padrão)

	/**
	 * <b>quit</b>
	 * 
	 * <p>The chess engine should immediately exit. This command is used when
	 * xboard is itself exiting, and also between games if the -xreuse command
	 * line option is given (or -xreuse2 for the second engine). See also
	 * Signals above.</p>
	 */
	public void quit();

	/**
	 * <b>random</b>
	 * 
	 * <p>This command is specific to GNU Chess 4. You can either ignore it
	 * completely (that is, treat it as a no-op) or implement it as GNU Chess
	 * does. The command toggles "random" mode (that is, it sets random =
	 * !random). In random mode, the engine adds a small random value to its
	 * evaluation function to vary its play. The "new" command sets random mode
	 * off.</p>
	 */
	public void random();
	
	/**
	 * <b>remove</b>
	 * 
	 * <p>If the user asks to retract a move, xboard will send you the
	 * "remove" command. It sends this command only when the user is on move.
	 * Your engine should undo the last two moves (one for each player) and
	 * continue playing the same color.</p>
	 */
	public void remove();
	
	/**
	 * <b>resign</b>
	 * 
	 * <p>If your engine wants to resign, it can send the command "resign".
	 * Alternatively, it can use the "RESULT {comment}" command if the string
	 * "resign" is included in the comment; for example "0-1 {White resigns}".
	 * xboard relays the resignation to the user, the ICS, the other engine in
	 * Two Machines mode, and the PGN save file as required. Note that many
	 * interfaces work more smoothly if you resign before you move.</p>
	 */
	
	public void resign();
	
	/**
	 * <b>setboard FEN</b>
	 * 
	 * <p>The setboard command is the new way to set up positions, beginning in
	 * protocol version 2. It is not used unless it has been selected with the
	 * feature command. Here FEN is a position in Forsythe-Edwards Notation, as
	 * defined in the PGN standard. Note that this PGN standard referred to here
	 * only applies to normal Chess; Obviously in variants that cannot be
	 * described by a FEN for normal Chess, e.g. because the board is not 8x8,
	 * other pieces then PNBRQK participate, there are holdings that need to be
	 * specified, etc., xboard will use a FEN format that is standard or
	 * suitable for that variant. In particular, in FRC or CRC, WinBoard will
	 * use Shredder-FEN or X-FEN standard, i.e. it can use the rook-file
	 * indicator letter to represent a castling right (like HAha) whenever it
	 * wants, but if it uses KQkq, this will always refer to the outermost rook
	 * on the given side.</p>
	 * 
	 * <p><i>Illegal positions:</i> Note that either setboard or edit can be used to send
	 * an illegal position to the engine. The user can create any position with
	 * xboard's Edit Position command (even, say, an empty board, or a board
	 * with 64 white kings and no black ones). If your engine receives a
	 * position that it considers illegal, I suggest that you send the response
	 * "tellusererror Illegal position", and then respond to any attempted move
	 * with "Illegal move" until the next new, edit, or setboard command.</p>
	 */
	
	public void setBoard(String fen);

	public void setDebug(boolean debug);

	/**
	 * <b>otim N</b>
	 * 
	 * <p>Set a clock that always belongs to the opponent. N is a number in
	 * centiseconds (units of 1/100 second). Even if the opponent changes to
	 * playing the opposite color, this clock remains with the opponent.</p>
	 * 
	 * <p>If needed for purposes of board display in force mode (where the engine
	 * is not participating in the game) the time clock should be associated
	 * with the last color that the engine was set to play, the otim clock with
	 * the opposite color.</p>
	 * 
	 * <p>This business of "clocks remaining with the engine" is apparently so
	 * ambiguous that many engines implement it wrong. The clocks in fact always
	 * remain with the color. Which clock reading is relayed with "time", and
	 * which by "otim", is determined by which side the engine plays. Note that
	 * the way the clocks operate and receive extra time (in accordance with the
	 * selected time control) is not affected in any way by which moves are made
	 * by the engine, which by the opponent, and which were forced.</p>
	 * 
	 * <p>Beginning in protocol version 2, if you can't handle the time and otim
	 * commands, you can use the "feature" command to disable them; see below.
	 * The following techniques from older protocol versions also work: You can
	 * ignore the time and otim commands (that is, treat them as no-ops), or
	 * send back "Error (unknown command): time" the first time you see "time".</p>
	 */
	public void setOpponentTime(int time);
	
	/**
	 * <b>hard</b>
	 * 
	 * <p>
	 * Turn on pondering (thinking on the opponent's time, also known as
	 * "permanent brain"). xboard will not make any assumption about what your
	 * default is for pondering or whether "new" affects this setting.
	 * </p>
	 * 
	 * <b>easy</b>
	 * <p>
	 * Turn off pondering.
	 * </p>
	 */
	public void setPondering(boolean pondering);

	/**
	 * <b>protover N</b>
	 * 
	 * <p>
	 * Beginning in protocol version 2 (in which N=2), this command will be sent
	 * immediately after the "xboard" command. If you receive some other command
	 * immediately after "xboard" (such as "new"), you can assume that protocol
	 * version 1 is in use. The "protover" command is the only new command that
	 * xboard always sends in version 2. All other new commands to the engine
	 * are sent only if the engine first enables them with the "feature"
	 * command. Protocol versions will always be simple integers so that they
	 * can easily be compared.
	 * </p>
	 * 
	 * <p>
	 * Your engine should reply to the protover command by sending the "feature"
	 * command (see below) with the list of non-default feature settings that
	 * you require, if any.
	 * </p>
	 * 
	 * <p>
	 * Your engine should never refuse to run due to receiving a higher protocol
	 * version number than it is expecting! New protocol versions will always be
	 * compatible with older ones by default; the larger version number is
	 * simply a hint that additional "feature" command options added in later
	 * protocol versions may be accepted.
	 * </p>
	 */
	public void setProtocolVersion(int protocolVersion);
	
	/**
	 * <b>sd DEPTH</b>
	 * 
	 * <p>The engine should limit its thinking to DEPTH ply. The commands "level"
	 * or "st" and "sd" can be used together in an orthogonal way. If both are
	 * issued, the engine should observe both limitations: In the protocol, the
	 * "sd" command isn't a time control. It doesn't say that your engine has
	 * unlimited time but must search to exactly the given depth. It says that
	 * you should pay attention to the time control as normal, but cut off the
	 * search at the specified depth even if you have time to search deeper. If
	 * you don't have time to search to the specified depth, given your normal
	 * time management algorithm, then you will want to stop sooner than the
	 * given depth.</p>
	 * 
	 * <p>The "new" command should set the search depth back to unlimited. This is
	 * already stated in the spec. The "level" command should not affect the
	 * search depth. As it happens, xboard/WinBoard currently always sends sd
	 * (if needed) right after level, but that isn't part of the spec.</p>
	 */
	public void setSearchDepth(int depth);
	
	/**
	 * <b>post</b>
	 * 
	 * <p>
	 * Turn on thinking/pondering output. See Thinking Output section.
	 * </p>
	 * 
	 * <b>nopost</b>
	 * <p>
	 * Turn off thinking/pondering output.
	 * </p>
	 */
	public void setShowThinking(boolean showThinking);

	/**
	 * <b>time N</b>
	 * 
	 * <p>Set a clock that always belongs to the engine. N is a number in
	 * centiseconds (units of 1/100 second). Even if the engine changes to
	 * playing the opposite color, this clock remains with the engine.</p>
	 */
	public void setTime(long time);

	/**
	 * <b>st TIME</b>
	 * 
	 * <p>Set time controls. See the Time Control section below.</p>
	 */

	public void setTimeControls(int time);

	/**
	 * <b>level MPS BASE INC</b>
	 * 
	 * <p>Set time controls. See the Time Control section below.</p>
	 * */
	public void setTimeControls(int movesPerSession, long baseTime, long increment);
	
	/**
	 * <b>undo</b>
	 * 
	 * <p>If the user asks to back up one move, xboard will send you the
	 * "undo" command. xboard will not send this command without putting you in
	 * "force" mode first, so you don't have to worry about what should happen if
	 * the user asks to undo a move your engine made. (GNU Chess 4 actually
	 * switches to playing the opposite color in this case.)</p>
	 */
	public void undo();
	
	public void unrecognizedCommand(String input);
	
	/**
	 * <b>white</b>
	 * 
	 * <p>(This command is obsolete as of protocol version 2, but is still sent in
	 * some situations to accommodate older engines unless you disable it with
	 * the feature command.) Set White on move. Set the engine to play Black.
	 * Stop clocks.</p>
	 */
	public void white();
	
}