package br.com.vinyanalista.jchessengine.example;

import java.io.*;

import br.com.vinyanalista.jchessengine.*;

public class DumbEngine implements JChessEngine {
	
	private static final String LOG_FILE_EXTENSION = ".debug";
	private static final String LOG_FILE_NAME = "DumbEngineLog";

	private boolean debug;
	private final BufferedWriter log;

	public DumbEngine() {
		debug = false;
		// http://www.mkyong.com/java/how-to-append-content-to-file-in-java/
		String logFileName = LOG_FILE_NAME + LOG_FILE_EXTENSION;
		File logFile = new File(logFileName);
		int count = 0;
		while (logFile.exists()) {
			count++;
			logFileName = LOG_FILE_NAME + count + LOG_FILE_EXTENSION;
			logFile = new File(logFileName);
		}
		FileWriter fileWritter;
		BufferedWriter bufferedWriter;
		try {
			fileWritter = new FileWriter(logFile.getName(), true);
			bufferedWriter = new BufferedWriter(fileWritter);
		} catch (IOException e) {
			// e.printStackTrace();
			debug(e.getMessage());
			bufferedWriter = null;
		}
		log = bufferedWriter;
	}
	
	public void black() {
	}

	public void computer() {
	}

	public void debug(String message) {
		if (!debug) {
			return;
		}
		System.out.println("# " + message);
		if (log == null) {
			return;
		}
		try {
			log.write(message + "\n");
			log.flush();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("# Exception:\n# " + e.getMessage());
		}
	}

	public void force() {
	}

	public void go() {
		XBoard.move(XBoard.askUser("Please, help me! Give me a move!"));
	}
	
	public void moveNow() {
	}

	public void newGame() {
	}
	
	public void opponentMove(String opponentMove) {
		go();
	}
	
	public void performanceTest(int depth) {
	}

	public void quit() {
	}

	public void random() {
	}
	
	public void remove() {
	}

	public void resign() {
	}

	public void setBoard(String fen) {
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setOpponentTime(int time) {
	}
	
	public void setPondering(boolean pondering) {
	}

	public void setProtocolVersion(int protocolVersion) {
		XBoard.feature("myname=\"DumbEngine\" setboard=1 analyze=0 variants=\"normal\" colors=0 debug=1 done=1");
	}
	
	public void setSearchDepth(int depth) {
	}
	
	public void setShowThinking(boolean showThinking) {
	}

	public void setTime(long time) {
	}

	public void setTimeControls(int time) {
	}

	public void setTimeControls(int movesPerSession, long baseTime, long increment) {
	}
	
	public void undo() {
	}
	
	public void unrecognizedCommand(String input) {
	}

	public void white() {
	}
	
	public static void main(String[] args) {
		DumbEngine engine = new DumbEngine();
		XBoard.setEngine(engine);
		engine.setDebug(true);
		
		engine.debug("DumbEngine ready");
		
		try {
			engine.debug("DumbEngine will start listening to xboard commands");
			XBoard.listen();
		} catch (Exception e) {
			engine.debug("DumbEngine exited unexpectedly");
			engine.debug(e.getMessage());
		}
	}

}