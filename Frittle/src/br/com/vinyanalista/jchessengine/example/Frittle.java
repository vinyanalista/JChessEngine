package br.com.vinyanalista.jchessengine.example;

import br.com.vinyanalista.jchessengine.*;
import net.sourceforge.frittle.*;
import net.sourceforge.frittle.ai.AI;

public class Frittle implements JChessEngine {
	/** The current version **/
    public static final String VERSION = "1.0";

	/** The Game object that contains GameStates and Game Logic. */
	private Game game;

	/** The brain of the engine. Artificial Intelligence. */
	private AI ai;
	
	/** Debug mode flag. True if debugMode mode is on */
    private boolean debugMode;
    
    public Frittle() {
    	debugMode = true; // TODO Verificar
    	// Create new game and restart AI engine
		game = new Game();
		ai = new AI(this);
		debugMode = false; // TODO Verificar
	}
	
	public void black() {
	}

	public void computer() {
	}

	public void debug(String message) {
		if (debugMode)
			XBoard.send("# " + message);
	}

	public void force() {
		debug("Frittle is inactive");
        ai.destroyThreads();
		ai.forceMode = true;
	}
	
	/**
	 * Get a reference to the current AI.
	 *
	 * @return		the AI object
	 */
	public AI getAI()
	{
		return ai;
	}
	
	/**
     * Get a reference to the current Game.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }
    
	public void go() {
		// First check if the game is still on
        if(game.isGameOver())
        	XBoard.tellUserError("Game Over");
        // If so, ask the AI to make a move
        else
        {
            debug("Frittle is playing " + (game.getCurrentState().getActivePlayer()));
            ai.go();
        }
	}
	
	public void moveNow() {
		ai.moveNow();
	}

	public void newGame() {
		// Start a new game
        ai.destroyThreads();
        ai.resetModes();
        game = new Game();
        debug("New game started");
	}
	
	public void opponentMove(String opponentMove) {
		if(game.doMove(opponentMove))
        {
            // The move was made, success!
            // If not in forceMode mode and the game is still on, let the AI play the next move
            if(ai.forceMode == false && game.isGameOver() == false)
                ai.go();
        }
        else
        {
            // Illegal move
        	XBoard.illegalMove();
        }
	}
	
	public void performanceTest(int depth) {
		Perft p = new Perft(game.getCurrentState());
		p.test(depth);
	}

	public void quit() {
	}

	public void random() {
	}
	
	public void remove() {
		game.undo();
		game.undo();
        debug("Reversed two moves");
	}
	
	public void resign() {
		game.resign();
	}
	
	public void setBoard(String fen) {
        try
        {
            ai.destroyThreads();
            GameState state = new GameState(fen);
            game.setState(state);
            debug("OK");
        }
        catch(InvalidFENException e)
        {
        	XBoard.tellUserError(e.getMessage());
        }
	}
	
	public void setSearchDepth(int depth) {
        ai.searchDepth = depth;
        debug("OK");
	}

	public void setDebug(boolean debug) {
		debugMode = debug;
	}

	public void setOpponentTime(int time) {
	}

	public void setPondering(boolean pondering) {
		ai.ponderMode = pondering;
		debug("Pondering " + (pondering ? "on" : "off"));
	}

	public void setProtocolVersion(int protocolVersion) {
		XBoard.feature("myname=\"Frittle " + VERSION + "\" setboard=1 analyze=0 variants=\"normal\" colors=0 debug=1 done=1");
	}

	public void setShowThinking(boolean showThinking) {
		ai.showThinking = showThinking;
		debug("Thinking " + (showThinking ? "on" : "off"));
	}

	public void setTime(long time) {
		ai.clock.set(time);
	}

	public void setTimeControls(int time) {
	}

	public void setTimeControls(int movesPerSession, long baseTime, long increment) {
		// Set clock format on game
        if (movesPerSession > 0) // Tournament format
            game.setClockFormat(new ClockFormat(movesPerSession, baseTime));
        else // Fischer format
            game.setClockFormat(new ClockFormat(baseTime, increment));
        // Reset AI clock
        ai.clock.set(baseTime);
        debug("OK");
	}

	public void undo() {
		game.undo();
        debug("Reversed one move");
	}
	
	public void unrecognizedCommand(String input) {
		// Do nothing if erroneous command was made in XBoard mode
	}

	public void white() {
	}
	
	/**
	 * This is the method that is invoked when the program is
	 * run from the commandline via the JVM.
	 *
	 * @param args	the command line arguments, if any
	 */
	public static void main(String[] args) {
		Frittle engine = new Frittle();
		XBoard.setEngine(engine);
		engine.setDebug(true);
		
		// Parse arguments
        for(int i=0; i < args.length; i++)
        {
            if(args[i].equals("-debug"))
            {
                engine.setDebug(true);
            }
        }
        // Welcome message
        XBoard.send("Welcome to Frittle " + VERSION);
		engine.debug("Frittle is "	+ (engine.getAI().forceMode?"inactive":"playing BLACK"));
		engine.debug("Pondering "	+ (engine.getAI().ponderMode?"on":"off"));
		engine.debug("Thinking "	+ (engine.getAI().showThinking?"on":"off"));
		XBoard.send("Type 'help' for a list of commands.");
		
		try {
			engine.debug("Frittle started listening to xboard commands");
			XBoard.listen();
		} catch (Exception e) {
			engine.debug("Frittle exited unexpectedly");
			engine.debug(e.getMessage());
		}
		
		engine.getAI().destroyThreads();
	}

}
