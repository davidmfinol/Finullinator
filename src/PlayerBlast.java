import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Image;

public class PlayerBlast extends Actor {
	protected static final String [] actions = {"start", "expand", "finish"};
	private long timeLived;
	private int atk;
	
	public PlayerBlast(int x, int y, boolean facingRight, int atk) {
		super(x, y, "PlayerBlast", "png", facingRight);
		this.atk = atk;
	}
	public Image[][] loadSprites(String name, String fileType) {
		Image[][] temp = new Image[1][3];
		
		try {
			for(int x=0;x<temp[0].length;x++)
	    		temp[0][x] = ImageIO.read(new File(getDirectory()+"Blast"+(x+1)+"."+getFileType()));
		} catch (Exception e) {
			Game.exitWithError(getName()+" failed to load: "+e);
		}
		
		spriteMap.put(getName(), temp);
		return temp;
	}
	public int getAnimationLength(String name, int actionNumber) {
		return 3;
	}
	public String[] getActionNames() {
		return actions;
	}
	public String getDirectory() {
		return "images"+File.separator+"Player"+File.separator;
	}
	
	public double getXSpeed() {
		return .000001;
	}
	public String getName() {
		return "PlayerBlast";
	}
	public String getFileType() {
		return "png";
	}
	
	public int nextAction() {
		return 0;
	}
	public int attacking() {
		return 0;
	}
	public int falling() {
		return 0;
	}
	public int jumping() {
		return 0;
	}
	public int moving() {
		return 0;
	}
	
	public int getMaxHealth() {
		return -1;
	}
	public int getAtk() {
		return atk;
	}
	
	public void update(long frameElapsedTime) {
		
	}
	public void performAction(int animationNumber) {
		
	}

}
