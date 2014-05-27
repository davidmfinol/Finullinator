import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Image;

public class Blast extends Actor {
	protected static final String [] actions = {"start", "expand", "finish"};
	private String name, fileType;
	private long timeLived;
	private int atk;
	
	public Blast(int x, int y, String name, String fileType, boolean facingRight, int atk) {
		super(x, y, name+"Blast", fileType, facingRight);
		this.name = name+"Blast";
		this.fileType = fileType;
		this.atk = atk;
	}
	public Image[][] loadSprites(String name, String fileType) {
		Image[][] temp = new Image[1][getAnimationLength(name, 0)];
		
		try {
			for(int x=0;x<temp[0].length;x++)
	    		temp[0][x] = ImageIO.read(new File(getDirectory()+name+(x+1)+"."+fileType));
		} catch (Exception e) {
			Game.exitWithError(name+" failed to load: "+e);
		}
		
		spriteMap.put(name, temp);
		return temp;
	}
	public int getAnimationLength(String name, int actionNumber) {
		if(name.equals("WilyBlast"))
			return 2;
		return 3;
	}
	public String[] getActionNames() {
		return actions;
	}
	public String getDirectory() {
		return "images"+File.separator+"Blasts"+File.separator;
	}
	
	public double getXSpeed() {
		return .000001;
	}
	public String getName() {
		return name;
	}
	public String getFileType() {
		return fileType;
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
