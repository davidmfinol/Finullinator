import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

//return player start point method
//enemies held by level?
//more time efficient walls?
//what about slanted walls?
public abstract class GameLevel implements Renderable, Serializable {
	private Image background;
	private final String DIRECTORY = "images"+File.separator+"maps"+File.separator;
	//bufferArea is fraction of screen that the player can't go to
	//when the player reaches it, the map will scroll
	private final double BUFFER_AREA = .1;
	//EdgeAreas are the pixel size of the screen that player can't go to
	private int XLoc, YLoc;
	transient private int XEdgeArea, YEdgeArea;
	private ArrayList<Line2D> floors;
	private ArrayList<Line2D> walls;
	
	public abstract String getMapName(); 
	public abstract String getSongName();
	public abstract ArrayList<Actor> createEnemies();
	public abstract GameLevel getNextLevel();
	
	/**
	 *Constructs a new instance of the level
	 *@param x The XLoc of the level
	 *@param y The YLoc of the level
	 */
	public GameLevel(int x, int y) {
		XLoc = x;
		YLoc = y;
		try {
			background = ImageIO.read(new File(DIRECTORY+getMapName()+".png"));
		} catch (IOException e) {
			Game.exitWithError("Background map for"+getMapName()+"failed to load: "+e);
		}
		GameFrame frame = Game.getFrame();
		XEdgeArea = (int) (BUFFER_AREA*frame.getGameCanvasSize().getWidth());
		YEdgeArea = (int) (BUFFER_AREA*frame.getGameCanvasSize().getHeight());
		walls = createWalls();
		floors = createFloors();
		AudioControl.loopMusic(getSongName());
	}
	/**
	 *Sets the Background image to image
	 *@param image The new picture of the level
	 */
	public void setBackGroundImage(Image image) {
		background = image;
	}
	/**
	 *Returns the image being drawn as the background
	 *@return The image in the background
	 */
	public Image getBackground() {
		return background;
	}
	
	/**
	 *Renders the map on the screen
	 *@param g The graphics object that the map will be drawn on
	 */
	public void render(Graphics g) {
		g.drawImage(background, XLoc, YLoc, null);
	}
	
	/**
	 *Renders the cut-scene for this level
	 *By default, there is no cut-scene, and nothing happens
	 */
	public static void cutScene() {}
	
	/**
	 *Sets the X-location of the map to x
	 *@param x The new X-location
	 */
	public void setXLocation(int x) {
		XLoc = x;
	}
	/**
	 *Sets the Y-location of the map to y
	 *@param y The new Y-location
	 */
	public void setYLocation(int y) {
		YLoc = y;
	}
	/**
	 *Returns the current X-location of the map
	 *@return The X-location of the map
	 */
	public int getXLocation() {
		return -XLoc;
	}
	/**
	 *Returns the current X-location of the map
	 *@return The X-location of the map
	 */
	public int getYLocation() {
		return -YLoc;
	}
	
	/**
	 *Moves the map d pixels to the right
	 *@param d The distance to the right that map should move
	 */
	public void moveRight(int d) {
		XLoc-= d;
		while(XLoc< (-1*getBackground().getWidth(Game.getFrame()) + Game.getFrame().getGameCanvasSize().getWidth()))
			XLoc+= 1;
	}
	/**
	 *Moves the map d pixels to the left
	 *@param d The distance to the left that map should move
	 */
	public void moveLeft(int d) {
		XLoc+= d;
		while(XLoc>0)
			XLoc-= 1;
	}
	/**
	 *Moves the map d pixels up
	 *@param d The distance up that map should move
	 */
	public void moveUp(int d) {
		YLoc+= d;
		while(YLoc>0)
			YLoc-= 1;
	}
	/**
	 *Moves the map d pixels down
	 *@param d The distance down that map should move
	 */
	public void moveDown(int d) {
		YLoc-= d;
		while(YLoc< (-1*getBackground().getHeight(Game.getFrame()) + Game.getFrame().getGameCanvasSize().getHeight()))
			YLoc+= 1;
	}
	
	/**
	 *Returns the X-coordinate where the player must be to cause the map to move to the right
	 *@return The X-coordinate where the player must be to cause the map to move to the right
	 */
	public int getRightEdge() {
		return getXLocation()+(int)Game.getFrame().getGameCanvasSize().getWidth()-XEdgeArea;
	}
	/**
	 *Returns the Y-coordinate where the player must be to cause the map to move down
	 *@return The Y-coordinate where the player must be to cause the map to move down
	 */
	public int getBottomEdge() {
		return getYLocation()+(int)Game.getFrame().getGameCanvasSize().getHeight()-YEdgeArea;
	}
	/**
	 *Returns the X-coordinate where the player must be to cause the map to move to the left
	 *@return The X-coordinate where the player must be to cause the map to move to the left
	 */
	public int getLeftEdge() {
		return getXLocation()+XEdgeArea;
	}
	/**
	 *Returns the Y-coordinate where the player must be to cause the map to move up
	 *@return The Y-coordinate where the player must be to cause the map to move up
	 */
	public int getTopEdge() {
		return getYLocation()+YEdgeArea;
	}
	
	/**
	 *Checks to see if the Actor with area is in contact with a wall
	 *@param area The 2D area of the Actor
	 *@return Whether the Actor is conflicting with a wall on this level
	 */
	public boolean conflictsWalls(Rectangle2D area) {
		boolean conflicts = false;
		for(int x=0;x<walls.size();x++)
			if(walls.get(x).intersects(area))
				conflicts = true;
		return conflicts;
	}
	/**
	 *Checks to see if the Actor with area is in contact with a floor
	 *@param area The 2D area of the Actor
	 *@return Whether the Actor is conflicting with a floor on this level
	 */
	public boolean conflictsFloors(Rectangle2D area) {
		boolean conflicts = false;
		for(int x=0;x<floors.size();x++)
			if(floors.get(x).intersects(area))
				conflicts = true;
		return conflicts;
	} 
	
	/**
	 *Creates the very left and right of the level as walls
	 *@return The arraylist of where all the walls in this level are
	 */
	public ArrayList<Line2D> createWalls() {
		ArrayList<Line2D> temp = new ArrayList<Line2D>();
		GameFrame frame = Game.getFrame();
		temp.add(new Line2D.Double(0, 0, 0, getBackground().getHeight(frame)));
		temp.add(new Line2D.Double(getBackground().getWidth(frame), 0, getBackground().getWidth(frame), getBackground().getHeight(frame)));
		return temp;
	}
	/**
	 *Creates the very top and bottom of the level as floors
	 *@return The arraylist of where all the floors in this level are
	 */
	public ArrayList<Line2D> createFloors() {
		ArrayList<Line2D> temp = new ArrayList<Line2D>();
		GameFrame frame = Game.getFrame();
		temp.add(new Line2D.Double(0, 0, getBackground().getWidth(frame), 0));
		temp.add(new Line2D.Double(0, getBackground().getHeight(frame), getBackground().getWidth(frame), getBackground().getHeight(frame)));
		return temp;
	}
}
