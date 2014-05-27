public class GameStatus
{
    public static enum GameState
    { 
    	Intro,			// the game is displaying the intro cutscene
		CutScene,   	// the game is displaying a midgame cutscene
		Ending,			// the game is displaying the ending cutscene
		GameInProgress, // the game is in progress
		GamePaused,     // the game is paused
		GameOver,       // the player has died
		GameExited      // the user is at the main menu
    }
    
    private static GameState gameState;
    private static GameState previousState;
	
    public synchronized static boolean isInIntro() {
		return (gameState == GameState.Intro);
    }
    public synchronized static boolean isInCutScene() {
		return (gameState == GameState.CutScene);
    }
    public synchronized static boolean isInEnding() {
		return (gameState == GameState.Ending);
    }
    public synchronized static boolean isGameInProgress() {
		return (gameState == GameState.GameInProgress);
    }
    public synchronized static boolean isGamePaused() {
		return (gameState == GameState.GamePaused);
    }
    public synchronized static boolean isGameOver() {
		return (gameState == GameState.GameOver);
    }
    public synchronized static boolean isGameExited() {
		return (gameState == GameState.GameExited);
    }
    
    public synchronized static GameState getGameState() {
		return gameState;
    }
    
    public synchronized static void revert() {
    	gameState = previousState;
    }
    
    public synchronized static void startNewGame() {
    	previousState = gameState;
		gameState = GameState.Intro;
    }
    public synchronized static void goToCutScene() {
    	previousState = gameState;
		gameState = GameState.CutScene;
    }
    public synchronized static void endGame() {
    	previousState = gameState;
		gameState = GameState.Ending;
    }
    public synchronized static void pauseGame() {
    	previousState = gameState;
	    gameState = GameState.GamePaused;
    }
    public synchronized static void resumeGame() {
    	previousState = gameState;
	    gameState = GameState.GameInProgress;
    }
    public synchronized static void gameOver() {
    	previousState = gameState;
		gameState = GameState.GameOver;
    }
    public synchronized static void exitGame() {
    	previousState = gameState;
		gameState = GameState.GameExited;
    }
    
    public synchronized static boolean wasInIntro() {
		return (previousState == GameState.Intro);
    }
    public synchronized static boolean wasInCutScene() {
		return (previousState == GameState.CutScene);
    }
    public synchronized static boolean wasInEnding() {
		return (previousState == GameState.Ending);
    }
    public synchronized static boolean wasGameInProgress() {
		return (previousState == GameState.GameInProgress);
    }
    public synchronized static boolean wasGamePaused() {
		return (previousState == GameState.GamePaused);
    }
    public synchronized static boolean wasGameOver() {
		return (previousState == GameState.GameOver);
    }
    public synchronized static boolean wasGameExited() {
		return (previousState == GameState.GameExited);
    }
}
