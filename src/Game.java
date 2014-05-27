/**
 * @(#)Game.java
 *
 * This game engine is David Finol and Christiaan Cleary's CS3 Project.
 *
 * @author David Finol
 * @version 1.00 2009/2/11
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.io.*;
import javax.swing.SwingUtilities;
//What to do when paused?
//choose level system?
//load game
public class Game implements Runnable{
    public static final int DESIRED_FPS = 60; // desired FPS (frames per second)
    // how long each frame should take in nanoseconds
    public static final long DESIRED_NS_PER_FRAME = 1000000000 / DESIRED_FPS;

	private static GameFrame frame;
	private static GameLoop game;
	private static boolean fullScreen, sound;
	private static Dimension resolution;
	private static int SEVolume, BGMVolume;

	private static GameLevel level;
	private static Player player;
	private static GameSaveData data;
	private static ArrayList<Actor> actors;

    /**
     *Loads the settings and arguments, and creates a new instance of the game
     *@param args The arguments passed by the user
     */
    public static void main(String[] args) {
    	//Defaults
    	boolean fs = true, snd = true;
    	Dimension res = new Dimension(1024, 768);
    	int se = 100, bgm = 100;

    	//load from settings file
    	File settingsFile = new File("settings.dat");
    	Scanner in = null;
    	PrintWriter out = null;
		try {
			in = new Scanner(settingsFile);
		    fs = in.nextLine().split(" ")[1].equals("on");
		    String[] resline = in.nextLine().split(" ");
		    int d1 = Integer.parseInt(resline[1]), d2 = Integer.parseInt(resline[2]);
		    res = new Dimension(d1, d2);
		    snd = in.nextLine().split(" ")[1].equals("on");
		    se = Integer.parseInt(in.nextLine().split(" ")[1]);
		    bgm = Integer.parseInt(in.nextLine().split(" ")[1]);
		} catch (Exception e) {
			if(in!=null)
				in.close();
			settingsFile.delete();
			System.err.println("Bad settings.dat file. Resetting with defaults.");
			try {
				out = new PrintWriter(new FileWriter(settingsFile, false));
				out.println("fullscreen= on");
				out.println("resolution= 1024 768");
				out.println("sound= on");
				out.println("se= 100");
				out.println("bgm= 100");
			}catch(IOException fail){
				System.err.println("Unable to write settings.dat file. Continuing with defaults.");
			}
		}
		finally {
			if(in!=null)
				in.close();
			if(out!=null)
				out.close();
		}

    	//load the arguments from user
    	int i = 0;
		try {
		    for (; i < args.length; i++) {
		    	if ("-fs".equals(args[i]))
			    	fs = "on".equals(args[++i]);
			    else if ("-res".equals(args[i]))
			    	res = new Dimension(Integer.parseInt(args[++i]), Integer.parseInt(args[++i]));
			    else if ("-snd".equals(args[i]))
			    	snd = "on".equals(args[++i]);
			    else if ("-se".equals(args[i]))
			    	se = Integer.parseInt(args[++i]);
			    else if ("-bgm".equals(args[i]))
			    	bgm = Integer.parseInt(args[++i]);
			    else if ("-h".equals(args[i]))
			    	usage("Help page requested.");
			    else
			    	usage("Invalid argument: "+args[i]);
			}
		} catch (ArrayIndexOutOfBoundsException e){
			usage("Argument at position " + i + " missing.");
		}catch (NumberFormatException e){
			usage("Argument at position " + i + " must be an integer.");
		}
		if(se>100)
			se = 100;
		if(bgm>100)
			bgm = 100;

		//instantiate the game
		SwingUtilities.invokeLater(new Game(fs, res, snd, se, bgm));
    }
    /**
     *Tells the user how to use the command line arguments
     *@param msg The error message that prompted this menu
     */
    public static void usage(String msg)
    {
		System.err.println(msg);
		System.err.println("Usage: java Game [-fs on|off] [-res width height] [-snd on|off] [-se vol] [-bgm vol] [-h]");
		System.err.println(" -fs on|off        : fullscreen(default) or windowed");
		System.err.println(" -res width height : resolution of the game screen(default:1024x768)");
		System.err.println(" -snd on|off       : sound on(default) or muted");
		System.err.println(" -se vol           : the volume of sound effect (default:100, the highest)");
		System.err.println(" -bgm vol          : the volume of background music (default:100, the highest)");
		System.err.println(" -h                : print out this usage information page");
		System.exit(0);
    }
    /**
     *Accepts the settings from main method
     *@param fs Whether the game is fullscreen
     *@param res The resolution of the game screen
     *@param snd Whether sound is on
     *@param se The sound effects volume
     *@param bgm The back ground music volume
     */
    public Game (boolean fs, Dimension res, boolean snd, int se, int bgm) {
    	fullScreen = fs;
    	resolution = res;
    	sound = snd;
    	SEVolume = se;
    	BGMVolume = bgm;
    }
    /**
     *Runs the game
     */
    public void run() {
    	GameStatus.exitGame();
    	frame = new GameFrame(fullScreen, resolution);
    }

    /**
     *Ends the current game by ending the game loop
	 */
	public static void endCurrentGame() {
		GameStatus.exitGame();
		if(game!=null && game.isAlive()) {
			game.interrupt();
			try{
				game.join();
			}catch(Exception e) {}
		}
		game = null;
	}

	/**
	 *Creates new savefile and then starts the game
	 */
	public static void startNewGame() {
		endCurrentGame();
    	data = new GameSaveData();
    	GameStatus.startNewGame();
    	game = new GameLoop();
	}

	/**
	 *Loads a previous save file and starts the game
	 */
	public static void load() {
		endCurrentGame();
    	loadSaveFile();
    	GameStatus.resumeGame();
		game = new GameLoop();
	}
	/**
	 *Loads the gameSaveData from the previous save
	 */
	public static void loadSaveFile() {
		if(data==null)
			data = new GameSaveData();
	}

	public static void saveSettings() {

	}
	public static void saveGame() {

	}

	/**
	 *Goes to the main menu
	 */
	public static void mainMenu() {
		endCurrentGame();
        frame.largeMenu(new MainMenu(frame.getGameCanvasSize()));
	}
	/**
	 *Brings up the options menu
	 */
	public static void optionsMenu() {
	  	if(GameStatus.isGamePaused())
	  		frame.smallMenu(new OptionsMenu(frame.getGameCanvasSize(), true));
	  	else
	  		frame.largeMenu(new OptionsMenu(frame.getGameCanvasSize(), false));
	}
	/**
	 *Brings up the pause menu
	 */
	public static void pauseMenu(){
		GameStatus.pauseGame();
		Thread.yield();
		frame.smallMenu(new PauseMenu(frame.getGameCanvasSize()));
	}
	/**
	 *Brings up the game information page
	 */
	public static void gameInformation() {
	  	if(GameStatus.isGamePaused())
	  		frame.smallMenu(new InformationMenu(frame.getGameCanvasSize(), true));
	  	else
	  		frame.largeMenu(new InformationMenu(frame.getGameCanvasSize(), false));
	}

	/**
	 *Brings up the game over menu
	 */
	public static void gameOver() {
		endCurrentGame();
		frame.largeMenu(new GameOverMenu(frame.getGameCanvasSize()));
	}

	/**
	 *Exits the game
	 */
	public static void exitGame() {
		frame.exitFullScreen();
		System.exit(0);
	}
	/**
	 *Exits the game with an error message output
	 *@param err The error message to be output
	 */
	public static void exitWithError(String err) {
		System.err.println(err);
		frame.exitFullScreen();
		System.exit(1);
	}


    public static int getSEVolume() {
    	return SEVolume;
    }
    public static int getBGMVolume() {
    	return BGMVolume;
    }
    public static boolean getSound() {
    	return sound;
    }
    /**
     *Returns the frame that the game is playing on
     *@return The frame that the the game is playing on
     */
    public static GameFrame getFrame() {
    	return frame;
    }
    /**
     *Returns the level that the player is currently at
     *@return The level that the player is currently at
     */
    public static GameLevel getLevel() {
    	return level;
    }
    /**
     *Returns the actors besides the main player that are currently active
     *@return The actors currently acting
     */
    public static ArrayList<Actor> getActors() {
     	return actors;
     }
    /**
     *Returns the main player that is currently active
     *@return The player that the gamer is controlling
     */
    public static Actor getPlayer() {
     	return player;
     }

    public synchronized static void setSEVolume(int se) {
    	SEVolume = se;
    }
    public synchronized static void setBGMVolume(int bgm) {
    	BGMVolume = bgm;
    }
    public synchronized static void setSound(boolean s) {
    	sound = s;
    }

    /**
	 *This class is responsible for actually looping through the game
	 */
	static class GameLoop extends Thread {
	    /**
	     *Loads the necessary data (level, player, and enemies) and loops through the game
	     */
	    public GameLoop() {
	    	frame.gameMode();
	    	level = data.level;
	    	player = new Player(data);
	    	actors = level.createEnemies();
	    	start();
	     }
		/**
		 *The main game loop. Runs until the player dies, quits, or goes to main menu
	     */
		public void run() {
			KeyListener checkForESC = new KeyListener(){
				public void keyTyped (KeyEvent typed){}
				public void keyReleased (KeyEvent released) {}
				public void keyPressed (KeyEvent pressed) {
					if(pressed.getKeyCode() == KeyEvent.VK_ESCAPE) {
						if(!GameStatus.isGamePaused())
							Game.pauseMenu();
					}
				}
			};
			frame.addKeyListener(checkForESC);
			//can't have frame.hasFocus() to be reliable
			while(GameStatus.isInIntro()) {
				//render intro cutscene
				GameStatus.resumeGame();
			}
			while (GameStatus.isGameInProgress() || GameStatus.isInCutScene() || GameStatus.isGamePaused()) {
				long prevFrameTime = System.nanoTime();
				long startTimeNS = System.nanoTime();
				long endOfFrameTimeNS;

				// This is your basic game loop
	    		frame.addKeyListener(player);
				while(GameStatus.isGameInProgress() && frame.hasFocus()){
				    long currFrameTime = System.nanoTime();
				    long frameElapsedTime = (currFrameTime - prevFrameTime);
				    prevFrameTime = currFrameTime;

		            // note the frame start time and calculate
		            // when it's supposed to end given current desired framerate
				    startTimeNS = System.nanoTime();
				    endOfFrameTimeNS = startTimeNS + DESIRED_NS_PER_FRAME;

					// run game logic: gather and process input
					for(int x=0;x<actors.size();x++)
						actors.get(x).update(frameElapsedTime);
					player.update(frameElapsedTime);
					if(player.isDead())
						GameStatus.gameOver();

					for(int x=0;x<actors.size();x++)
						if(actors.get(x).isDead()) {
							actors.remove(x);
							x--;
						}
			        // and, finally, render
				    renderGame();

				    // if the frame took less than required time,
				    // sleep and give other threads a chance to catch up.
					waitForFrameEnd(endOfFrameTimeNS);
				}
				frame.removeKeyListener(player);
				player.clearInput();

				while(GameStatus.isGamePaused() || GameStatus.isInCutScene() && frame.hasFocus()) {
					//render cutscene
					level.cutScene();
				}
			}
			while(GameStatus.isInEnding() && frame.hasFocus()) {
				//render ending cutscene
				level.cutScene();
			}

			frame.removeKeyListener(checkForESC);
			if(GameStatus.isGameOver())
				SwingUtilities.invokeLater(new Thread(new Runnable() {
					public void run() {
						Game.gameOver();
					}
				}));
		}
	    /**
	     *A typical render loop: render objects from back to front:
	     *background, actors, then the player and hud
	     *then show the back buffer.
	     */
	    public static void renderGame() {
			Dimension dim = frame.getGameCanvasSize();
			BufferStrategy bs = frame.getBufferStrategy();
			Graphics g = null;
			try {
				if (!bs.contentsLost()) {
					g = bs.getDrawGraphics();
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, (int)dim.getWidth(), (int)dim.getHeight());

					//level, actors, player
					level.render(g);
					for(int x=actors.size()-1;x>=0;x--)
						actors.get(x).render(g);
					player.render(g);
					g.setFont(new Font("Times New Roman", Font.BOLD, (int)frame.getGameCanvasSize().getHeight()/50));
					g.drawString(""+player.getHealth(), 0, (int)frame.getGameCanvasSize().getHeight()/50);
					if(!GameStatus.isGamePaused())
						bs.show();
				}
			}catch (Exception e) {
				System.err.println("There was an error rendering the game: "+e);
			}finally {
				if(g!=null)
					g.dispose();
			}
	    }
	    /**
	     *Maintain the requested fps by waiting before going to the next frame
	     */
	    public void waitForFrameEnd(long endOfFrameTimeNS) {
			// have at least one yield() per frame to allow
			// other threads to catch up in case we're pegging
			// the cpu if the requested fps is too high
		        Thread.yield();
			// spin in this loop until the time for this frame
			// has elapsed.
		        while (System.nanoTime() < endOfFrameTimeNS) {
			    // this may not be necessary
		            Thread.yield();
		            try {
				// sleep whatever the minimal amount is
		                Thread.sleep(1);
		            } catch (Exception e) {}
		        }
	    }
	}
}
