/**
 *This JFrame can be either windowed or FSEM. It uses swing components for all the menus.
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
//Small menu
public class GameFrame extends JFrame{
	GraphicsDevice myDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	DisplayMode oldDisplayMode;
	private JPanel gameCanvas = null;
	private Dimension preferredSize;
	MyWindowListener altTabChecker;

	/**
	 *Creates the Frame that the game will be displayed on
	 *@param fullScreen Whether or not the frame will cover the entire screen or be windowed
	 *@param resolution The resolution requested for the frame
	 */
	public GameFrame(boolean fullScreen, Dimension resolution) {
		super("[Place Name Here~]");
		oldDisplayMode = myDevice.getDisplayMode();
		setIconImage(new ImageIcon("game.jpg").getImage());
		setPreferredSize(resolution);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        altTabChecker = new MyWindowListener();
        setIgnoreRepaint(true);
        myDevice.setFullScreenWindow(null);

		if(fullScreen)
			showFull();
		else
			showWindow();
		requestFocus();
	}
	/**
	 *Makes the frame fullscreen
	 */
	public void showFull() {
		gameCanvas = null;
        setUndecorated(true);

        if(myDevice.isFullScreenSupported()) {
        	myDevice.setFullScreenWindow(this);
	        createBufferStrategy(2);
	    	addWindowListener(altTabChecker);
	    	if (myDevice.isDisplayChangeSupported()) {
				try {
	            	DisplayMode myDM = new DisplayMode( (int)preferredSize.getWidth(), (int)preferredSize.getHeight(), 32, 60);
	                myDevice.setDisplayMode(myDM);
	            } catch (IllegalArgumentException iae) {
    				myDevice.setDisplayMode(oldDisplayMode);
				}
			}
			largeMenu(new MainMenu(preferredSize));
	    	validate();
        }
        else{
        	System.err.println("FullScreen Exclusive mode not supported");
        	showWindow();
        }
	}
	/**
	 *Makes the frame windowed
	 */
	public void showWindow() {
	    gameCanvas = new JPanel(true) {
		    public Dimension getSize() {
		    	return preferredSize;}
		    public boolean isFocusable() {
		    	return false;}
		};

		exitFullScreen();
		removeWindowListener(altTabChecker);
	    setUndecorated(false);
        setIgnoreRepaint(false);
		largeMenu(new MainMenu(preferredSize));
		pack();
		setLocationRelativeTo(null);
	    setVisible(true);
	}

	/**
	 *Puts a menu that covers the entire screen on the frame
	 *@param menu The menu that goes on the frame
	 */
	public void largeMenu(JComponent menu) {
		setContentPane(menu);
		validate();
		requestFocus();
	}
	/**
	 *Puts a menu that covers part of the screen on the frame
	 *@param menu The menu that goes on the frame
	 */
	public void smallMenu(JComponent menu) {
		getLayeredPane().add(menu);
		Thread.yield();
		getLayeredPane().validate();
	}
	/**
	 *Puts the the game onto the frame
	 */
	public void gameMode() {
		if(gameCanvas != null){
			createBufferStrategy(2);
	        setContentPane(gameCanvas);
			gameCanvas.setIgnoreRepaint(true);
		}
		else
			setContentPane(new JPanel());
		requestFocus();
	}

    /**
     *Sets the preferred size
     *@param dim the resolution that is preferred
     */
    public void setPreferredSize(Dimension dim) {
    	preferredSize = dim;
    }
    /**
     *Returns the size of the game screen
     *@return The resolution of the game
     */
    public Dimension getGameCanvasSize() {
        return gameCanvas == null ?
		    getSize() :
		    gameCanvas.getSize();
    }

    /**
     *Exits full screen mode
     */
    public void exitFullScreen() {
    	if(gameCanvas==null){
	    	myDevice.setDisplayMode(oldDisplayMode);
			myDevice.setFullScreenWindow(null);
    	}
    }

    /**
     *This class is responible for checking for alt-tabbing
     */
	private final class MyWindowListener extends WindowAdapter {
		public void windowActivated(WindowEvent e) {
			changeScreen(true);
		}
		public void windowDeactivated(WindowEvent e) {
			changeScreen(false);
		}
	}
	/**
	 *Changes between FSEM and windowed to allow alt-tabbing!
	 *Pass false to make alt-tab out as a window , and true for coming back in as a window
	 *@param full Whether it is going back to fullScreen mode
	 */
	public void changeScreen(boolean full) {
		if (full) {
			myDevice.setFullScreenWindow(this);
	    	if (myDevice.isDisplayChangeSupported()) {
				try {
	            	DisplayMode myDM = new DisplayMode( (int)preferredSize.getWidth(), (int)preferredSize.getHeight(), 32, 60);
	                myDevice.setDisplayMode(myDM);
	            } catch (IllegalArgumentException iae) {
    				myDevice.setDisplayMode(oldDisplayMode);
				}
			}
	    	validate();
		}
		else
			exitFullScreen();
	}
}
