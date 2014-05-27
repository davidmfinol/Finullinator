/**
 *Anything that can move on the game screen is an actor.
*/

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;
import java.awt.geom.Rectangle2D;

//Slanted wall movement?
//Make all floors in one treeset, then find slopes for inclination

public abstract class Actor implements Renderable {
	public static final long AVERAGE_ANIMATION_TIME = 500000000; //how long each animation should take in nanoseconds
	public static final double DEFAULT_GRAVITY = .000000000000001;//how much acceleration (pixels/nanosecond squared) there is due to gravity
	protected static HashMap<String, Image[][]> spriteMap = new HashMap<String, Image[][]>(); // all the sprites for every character in the game
	private Image[][] spriteSheet;//all the sprites for this character
	private Image [] animationImages;//the sequence of sprites for the current animation
	private Image previous;//the previous sprite that was displayed by this character
	
	private int sprite = 0; //the number corresponding to how far along the animation is
		//this number is placed in animationImages (animationImages[sprite]) to get the correct sprite
		
	private int actionNumber = 0; //the number corresponding to the action in getActionNames[actionNumber]
		//an actionNumber of 0 should always be Rest
		
	private int health, //how much health the actor has left
		XLoc, //the x-location of this actor
		YLoc, //the y-location of this actor
		frameMovement, //how many pixels the actor will move (x-axis) this frame
		fallMovement, //how many pixels the actor will fall this frame
		jumpMovement; // how many pixels the actor will jump this frame
		
	private boolean facingRight, //whether the character is facing to the right
		spriteChangedThisFrame, //whether sprite changed this frame
		animationComplete, //whether the animation is complete so the action can change
		actionChanged, //whether the action changed this frame
		hurt = false, //whether the actor was hurt this frame and should flash white
		dead = false; //whether the actor is dead
		
	private double jumpSpeed = getXSpeed(), //how fast (pixels/ns) the character jumps
		fallSpeed = getXSpeed()/2, // how fast (pixels/ns) the character falls due to gravity
		gravity; //how much acceleration (pixels/nanosecond squared) there is due to gravity
	
	private long timePassed = 0, //how long the current animation has been happening
		
		 //how long you have to wait before you change the current sprite
		timeForNextSprite;
		
	/**
	 *Each individual action of an actor should have a set number of sprite images
	 *This affects the length of animationImages[] for actionNumber
	 *@param actionNumber The number for the action that you want to find out about
	 *@return How many images are in the animation indicated by actionNumber
	 */
	public abstract int getAnimationLength(String name, int actionNumber);
	public abstract String[] getActionNames();
	public abstract String getName();
	public abstract String getFileType();
	public abstract int nextAction();
	// return the actionNumber for the following basic actions
	public abstract int attacking();
	public abstract int falling();
	public abstract int jumping();
	public abstract int moving();
	
	public abstract int getMaxHealth();
	public abstract int getAtk();
	public abstract void performAction(int animationNumber);
	
	
	public Actor(int x, int y) {
		XLoc = x;
		YLoc = y;
		health = getMaxHealth();
		spriteSheet = getSprites(getName(), getFileType());
		animationImages = spriteSheet[getAction()];
		facingRight = true;
		gravity = DEFAULT_GRAVITY;
		timeForNextSprite = getTimeForNextSprite(0);
	}
	public Actor(GameSaveData data) {
		XLoc = data.xLoc;
		YLoc = data.yLoc;
		health = data.health;
		spriteSheet = getSprites(getName(), getFileType());
		animationImages = spriteSheet[getAction()];
		facingRight = data.facingRight;
		gravity = DEFAULT_GRAVITY;
		timeForNextSprite = getTimeForNextSprite(0);
	}
	public Actor(int x, int y, String name, String fileType, boolean facingRight) {
		XLoc = x;
		YLoc = y;
		health = getMaxHealth();
		setGravity(0);
		spriteSheet = getSprites(name, fileType);
		animationImages = spriteSheet[getAction()];
		this.facingRight = facingRight;
		timeForNextSprite = getAnimationTime(0)/getAnimationLength(name, 0);
	}
	
	public long getAnimationTime(int actionNumber) {
		return AVERAGE_ANIMATION_TIME;
	}
	public long getTimeForNextSprite(int spriteNumber) {
		return getAnimationTime(getAction())/getAnimationLength(getName(), getAction());
	}
	public String getDirectory() {
		return "images"+File.separator+getName()+File.separator;
	}
	
	public Image[][] getSprites(String name, String fileType) {
		Image[][] temp = spriteMap.get(name);
		if(temp == null)
			temp = loadSprites(name, fileType);
		return temp;
	}
	public Image[][] loadSprites(String name, String fileType) {
		Image[][] temp = new Image[getActionNames().length][0];
		for(int x=0;x<getActionNames().length;x++) {
			Image [] images = new Image[getAnimationLength(name, x)];
			int loaded = 0;
			
			for(int y=0;y<images.length;y++) {
				try {
			    	images[y] = ImageIO.read(new File(getDirectory()+getActionNames()[x]+(y+1)+"."+fileType));
			    	loaded++;
				} catch (IOException e) {
					try{
						if(loaded > 0)
							images[y] = images[y-loaded];
						else
							images[y] = temp[0][0];
					}catch (Exception err){
						Game.exitWithError("Character sprites for "+name+" failed to load: "+err);
					}
				}
			}
			temp[x] = images;
		}
		spriteMap.put(name, temp);
		return temp;
	}
	
	public void setXLocation(int x) {
		XLoc = x;
	}
	public void setYLocation(int y) {
		YLoc = y;
	}
	public void setLocation(int x, int y) {
		XLoc = x;
		YLoc = y;
	}
	public int getXLocation() {
		return XLoc;
	}
	public int getYLocation() {
		return YLoc;
	}
	
	public int getHealth() {
		return health;
	}
	public void setHealth(int h) {
		health = h;
	}
	
	public double getXSpeed() {
		return .00000025;
	}
	public void setFrameMovement(int f) {
		frameMovement = f;
	}
	public int getFrameMovement() {
		return frameMovement;
	}
	
	public void setFallMovement(int g) {
		fallMovement = g;
	}
	public void setJumpMovement(int a) {
		jumpMovement = a;
	}
	public int getFallMovement() {
		return fallMovement;
	}
	public int getJumpMovement() {
		return jumpMovement;
	}
	
	public void setFallSpeed(double f) {
		fallSpeed = f;
	}
	public double getFallSpeed() {
		return fallSpeed;
	}
	public void setJumpSpeed(double j) {
		jumpSpeed = j;
	}
	public double getJumpSpeed() {
		return jumpSpeed;
	}
	
	public boolean isFacingRight() {
		return facingRight;
	}
	public void changeDirection() {
		facingRight = !facingRight;
	}
	
	public void render(Graphics g) {
		Image img = sprite();
		GameFrame frame = Game.getFrame();
		GameLevel level = Game.getLevel();
		int w = img.getWidth(frame);
		int h = img.getHeight(frame);
		if(hurt) {
			BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_USHORT_555_RGB );// this type of image ignores transparency
			Graphics g2 = bi.getGraphics();
			g2.drawImage(img, 0, 0, frame);
			//whiten the image
			RescaleOp rop = new RescaleOp(3.0f, 0, null);
			img = rop.filter(bi, null);
			//make transparent again
			final Color color = new Color(0).black;//the color that will be made transparent
			ImageFilter filter = new RGBImageFilter() {
				public int markerRGB = color.getRGB() | 0xFF000000; // the color we are looking for... Alpha bits are set to opaque
				public final int filterRGB(int x, int y, int rgb) {
					if ( ( rgb | 0xFF000000 ) == markerRGB ) {
						return 0x00FFFFFF & rgb; // Mark the alpha bits as zero - transparent
					}
					else {
						return rgb;// nothing to do
					}
				}
			};
			ImageProducer ip = new FilteredImageSource(img.getSource(), filter);
			img = Toolkit.getDefaultToolkit().createImage(ip);
		}
		if(facingRight)
			g.drawImage(img, XLoc-level.getXLocation(), YLoc-level.getYLocation(), w, h, frame);
		else
			g.drawImage(img, XLoc-level.getXLocation()+w, YLoc-level.getYLocation(), -w, h, frame);
	}
	
	public void update(long frameElapsedTime) {
		timePassed+= frameElapsedTime;
		setFrameMovement((int)(getXSpeed()*frameElapsedTime));
		
		spriteChangedThisFrame = (timePassed>=timeForNextSprite) || newInput();
		animationComplete = sprite==getAnimationLength(getName(), getAction())-1 && spriteChangedThisFrame;
		if(spriteChangedThisFrame){
			chooseAction();
			chooseSprite();
		}
		determineAirMovement(frameElapsedTime, sprite);
		performAction(sprite);
	}
	public void chooseAction() {
		actionChanged = true;
		if(getGravity()!=0 && isMidAir() && (getAction()==moving()||getAction()==attacking()) )
			setAction(falling());
		else if(animationComplete || newInput())
			setAction(nextAction());
		else
			actionChanged = false;
	}
	public void chooseSprite() {
		GameLevel level = Game.getLevel();
		GameFrame frame = Game.getFrame();
		previous = sprite();
		timePassed = 0;
		if(actionChanged) {
			hurt = false;
			if(!newInput() && actionLoops())
				sprite = 1;
			else
				sprite = 0;
			animationImages = spriteSheet[getAction()];
		}
		else
			sprite++;
		
		if(sprite()!=previous) {
			YLoc -= sprite().getHeight(frame)-previous.getHeight(frame);
			if(!isFacingRight())
				XLoc -= sprite().getWidth(frame)-previous.getWidth(frame);
			if(level.conflictsWalls(getArea())) {
				if(!isFacingRight())
					XLoc += sprite().getWidth(frame)-previous.getWidth(frame);
				else
					XLoc -= sprite().getWidth(frame)-previous.getWidth(frame);
			}
		}
		timeForNextSprite = getTimeForNextSprite(sprite);
	}
	public void determineAirMovement(long frameElapsedTime, int animationNumber) {
		if(gravity != 0) {
			double speedChange = gravity*frameElapsedTime;
			if(getAction()==jumping())
				jumpSpeed-=speedChange;
			else if(getAction()==falling() && fallSpeed<getXSpeed()*3)
				fallSpeed+=speedChange;
			if(!isMidAir()){
				jumpSpeed = getXSpeed();
				fallSpeed = getXSpeed()/2;
			}
			int jumped = (int)(jumpSpeed*frameElapsedTime);
			if(jumped<=0)
				setAction(falling());
			setJumpMovement(jumped);
			setFallMovement((int)(fallSpeed*frameElapsedTime));
		}
		else {
			setJumpMovement(getFrameMovement());
			setFallMovement(getFrameMovement());
		}
	}
	public boolean newInput() {
		boolean landed = getAction()==falling()&&!isMidAir();
		return landed || (getGravity()!=0 && isMidAir() && getAction()!=jumping());
	}
	
	public void setAction(int a) {
		actionNumber = a;
	}
	public int getAction() {
		return actionNumber;
	}
	public boolean actionLoops() {
		return false;
	}
	
	public void fall() {
		YLoc+= fallMovement;
		while(Game.getLevel().conflictsFloors(getArea()))
			YLoc-= 1;
	}
	public void jump() {
		YLoc-= jumpMovement;
		while(Game.getLevel().conflictsFloors(getArea()))
			YLoc+= 1;
	}
	public void move() {
		if(facingRight) {
			XLoc+= frameMovement;
			if(Game.getLevel().conflictsWalls(getArea())) {
				XLoc-= frameMovement;
				if(this instanceof Bullet)
					die();
			}
		}
		else {
			XLoc-= frameMovement;
			if(Game.getLevel().conflictsWalls(getArea())) {
				XLoc+= frameMovement;
				if(this instanceof Bullet)
					die();
			}
		}
	}
	public void attack() {
		if(spriteChangedThisFrame) {
			Actor player = Game.getPlayer();
			if(this.isInSameArea(player))
				player.hurt(getAtk()/getAnimationLength(getName(), getAction()));
		}
	}
	public void shootBullet(int strength) {
		if(spriteChangedThisFrame) {
			int x;
			if(isFacingRight())
				x = getXLocation()+sprite().getWidth(null);
			else
				x = getXLocation();
			Game.getActors().add(
				new Bullet(x, getYLocation()+sprite().getHeight(null)/2, getName(), getFileType(), isFacingRight(), strength));
		}
	}
	public void hurt(int dmg) {
		hurt = true;
		health-=dmg;
		if(health<=0)
			die();
	}
	public void die() {
		dead = true;
	}
	public boolean isDead() {
		return dead;
	}
	
	public boolean isMidAir() {
		 return !Game.getLevel().conflictsFloors(getBelowArea());
	}
	public void setGravity(double grav) {
		gravity = grav;
	}
	public double getGravity() {
		return gravity;
	}
	
	public Rectangle2D getArea() {
		return new Rectangle2D.Double(XLoc, YLoc, sprite().getWidth(null), sprite().getHeight(null));
	}
	public Rectangle2D getBelowArea() {
		return new Rectangle2D.Double(XLoc, YLoc+1, sprite().getWidth(null), sprite().getHeight(null)+1);
	}
	public boolean isInSameArea(Actor other) {
		return getArea().intersects(other.getArea());
	}
	
	public Image sprite() {
		return animationImages[sprite];
	}
	public boolean spriteChangedThisFrame() {
		return spriteChangedThisFrame;
	}
}

