import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.util.ArrayList;

public class Bullet extends Actor {
	protected static final String [] actions = {"Be a Bullet"};
	private String name, fileType;
	private long timeLived;
	private int atk;
	
	public Bullet (int x, int y, String name, String fileType, boolean facingRight, int atk) {
		super(x, y, name+"Bullet", fileType, facingRight);
		this.name = name+"Bullet";
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
		if(name.equals("SandrockBullet"))
			return 6;
		else
			return 1;
	}

	public String[] getActionNames() {
		return actions;
	}
	public String getDirectory() {
		return "images"+File.separator+"Bullets"+File.separator;
	}
	public String getName() {
		return name;
	}
	public String getFileType() {
		return fileType;
	}
	
	public double getXSpeed() {
		return .000001;
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
		timeLived+= frameElapsedTime;
		super.update(frameElapsedTime);
		if(timeLived > 500000000)
			die();
	}
	public void performAction(int animationNumber) {
		move();
		attack();
	}
	public void attack() {
		if(!getName().equals("PlayerBullet")) {
			Actor player = Game.getPlayer();
			if(this.isInSameArea(player)) {
				player.hurt(getAtk());
				die();
			}
		}
		else {
			ArrayList<Actor> actors = Game.getActors();
			for(int x=0;x<actors.size();x++) {
				Actor enemy = actors.get(x);
				if(enemy!=this && this.isInSameArea(enemy)) {
					enemy.hurt(getAtk());
					die();
					return;
				}
			}
		}
	}
}
