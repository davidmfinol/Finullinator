import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.awt.Image;
import java.util.Random;

public class Mossflower extends Actor{
	protected static final String [] actions = {"Rest", "Attack", "Fall", "Jump", "Move"};
	private static Random actionChooser = new Random();
	
	public Mossflower(int x, int y) {
		super(x, y);
	}
	
	public String[] getActionNames() {
		return actions;
	}
	public int getAnimationLength(String name, int actionNumber) {
		int length;
		switch (actionNumber) {
			case 0 : length = 1; break;
			case 1 : length = 2; break;
			case 2 : length = 1; break;
			case 3 : length = 1; break;
			case 4 : length = 2; break;
			default : System.err.println("Error in choosing animation length for "+getName()); length = 1; break;
		}
		return length;
	}
	
	public String getName() {
		return "Mossflower";
	}
	public String getFileType() {
		return "png";
	}
	
	public int getMaxHealth() {
		return 30;
	}
	public int getAtk() {
		return 5;
	}
	
	public int nextAction() {
		int action;
		if(isMidAir())
			action = falling();
		else {
			action = actionChooser.nextInt(6);
			if(action==5) {
				changeDirection();
				action = 4;
			}
		}
		return action;
	}
	public void performAction(int animationNumber) {
		switch (getAction()) {
			case 1 : attack(); break;
			case 2 : fall(); break;
			case 3 : jump(); break;
			case 4 : move(); break;
		}
	}
	
	public int attacking() {
		return 1;
	}
	public int falling() {
		return 2;
	}
	public int jumping() {
		return 3;
	}
	public int moving() {
		return 4;
	}
	
}
