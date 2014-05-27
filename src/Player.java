import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.awt.Image;
import java.util.ArrayList;

public class Player extends Actor implements KeyListener {
	protected static final String [] actions = {"Rest", "AirAttack", "AngledAttack", "Attack", "Crouch", "Fall", "Jump", "Land", "Move", "RangedAttack", "SpecialAttack"};
	private boolean [] keysPressed;
	private boolean inputThisAnimation;
	private int maxHealth, atk, bulletStrength, blastStrength, blastsLeft;
	
	public Player(GameSaveData data) {
		super(data);
		atk = data.atk;
		maxHealth = data.maxHealth;
		bulletStrength = data.bulletStrength;
		blastStrength = data.blastStrength;
		blastsLeft = data.blastsLeft;
		keysPressed = new boolean[256];
		inputThisAnimation = false;
	}
	
	public String[] getActionNames() {
		return actions;
	}
	public int getAnimationLength(String name, int actionNumber) {
		int length;
		switch (actionNumber) {
			case 0 : length = 1; break;
			case 1 : length = 2; break;
			case 2 : length = 3; break;
			case 3 : length = 6; break;
			case 4 : length = 1; break;
			case 5 : length = 1; break;
			case 6 : length = 2; break;
			case 7 : length = 1; break;
			case 8 : length = 8; break;
			case 9 : length = 5; break;
			case 10 : length = 7; break;
			default : System.err.println("Error in choosing animation length for "+getName()); length = 1; break;
		}
		return length;
	}
	public long getAnimationTime(int actionNumber) {
		long time = super.getAnimationTime(actionNumber);
		if(actionNumber==7)
			time = 250000000;
		else if(actionNumber==4)
			time = 100000000;
		return time;
	}
	public long getTimeForNextSprite(int spriteNumber) {
		if(getAction()==10 && spriteNumber==6)
			return 500000000;
		return super.getTimeForNextSprite(spriteNumber);
	}
	
	public String getName() {
		return "Player";
	}
	public String getFileType() {
		return "png";
	}
	public boolean newInput() {
		boolean landed = getAction()==falling()&&!isMidAir();
		return landed || (getGravity()!=0&&isMidAir()&&(getAction()!=jumping()&&getAction()!=1)) || (inputThisAnimation  && (getAction()==moving()||getAction()==0));
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	public int getAtk() {
		return atk;
	}
	
	public int nextAction() {
		int previousAction=getAction();
		int nextAction = 0;
		if(keysPressed[KeyEvent.VK_DOWN] && getGravity()==0)
			nextAction = falling();
		else if(isMidAir() && getGravity()!=0 && keysPressed[KeyEvent.VK_CONTROL])
			nextAction = 1;
		else if(isMidAir() && getGravity()!=0)
			nextAction = falling();
		else if(previousAction == falling() && getGravity()!=0)
			nextAction = 7;
		else if(keysPressed[KeyEvent.VK_UP] && keysPressed[KeyEvent.VK_CONTROL])
			nextAction = 2;
		else if(keysPressed[KeyEvent.VK_UP])
			nextAction = jumping();
		else if(keysPressed[KeyEvent.VK_SPACE])
			nextAction = attacking();
		else if(keysPressed[KeyEvent.VK_CONTROL])
			nextAction = 9;
		else if(keysPressed[KeyEvent.VK_ENTER] && blastsLeft>0)
			nextAction = 10;
		else if(keysPressed[KeyEvent.VK_RIGHT] && keysPressed[KeyEvent.VK_LEFT])
		{}
		else if(keysPressed[KeyEvent.VK_RIGHT]) {
			if(!isFacingRight())
				changeDirection();
			nextAction = moving();
		}
		else if(keysPressed[KeyEvent.VK_LEFT]) {
			if(isFacingRight())
				changeDirection();
			nextAction = moving();
		}
		else if(keysPressed[KeyEvent.VK_DOWN] && getGravity()!=0)
			nextAction = 4;
		inputThisAnimation = nextAction!=previousAction;
		return nextAction;
	}
	public void performAction(int animationNumber) {
		switch (getAction()) {
			case 1 : fall(); 
				determineXMovement(); 
				if(animationNumber == 1) 
					shootBullet(bulletStrength); break;
			case 2 : if(animationNumber == 2) 
				shootBullet(bulletStrength); break;
			case 3 : determineXMovement(); 
				attack(); break;
			case 5 : determineXMovement();
				fall(); break;
			case 6 : if(animationNumber!=0 || getGravity()==0) {
				jump(); 
				determineXMovement();} break;
			case 8 : move(); break;
			case 9 : if(animationNumber == 3) 
				shootBullet(bulletStrength); break;
			case 10: if(animationNumber == 6)
				shootBlast(); break;
		}
	}
	public void determineXMovement() {
		if(keysPressed[KeyEvent.VK_RIGHT] && keysPressed[KeyEvent.VK_LEFT])
		{}
		else if(keysPressed[KeyEvent.VK_RIGHT]) {
			if(!isFacingRight())
				changeDirection();
			else
				move();
		}
		else if(keysPressed[KeyEvent.VK_LEFT]) {
			if(isFacingRight())
				changeDirection();
			else
				move();
		}
	}
	public void determineAirMovement(long frameElapsedTime, int animationNumber) {
		if(getGravity()!=0) {
			double speedChange = getGravity()*frameElapsedTime;
			if(getAction()==jumping() && getJumpSpeed()>getXSpeed()/2 && animationNumber!=0)
				setJumpSpeed(getJumpSpeed()-speedChange);
			else if(getAction()==falling() && getFallSpeed()<getXSpeed()*3)
				setFallSpeed(getFallSpeed()+speedChange);
			else if(!isMidAir()){
				setJumpSpeed(getXSpeed()*2);
				setFallSpeed(getXSpeed()/2);
			}
			setJumpMovement((int)(getJumpSpeed()*frameElapsedTime));
			setFallMovement((int)(getFallSpeed()*frameElapsedTime));
		}
		else {
			setJumpMovement(getFrameMovement());
			setFallMovement(getFrameMovement());
		}
	}
	
	public void move() {
		super.move();
		GameLevel level = Game.getLevel();
		int frameMovement = getFrameMovement();
		if(isFacingRight() && getXLocation()+sprite().getWidth(Game.getFrame()) >= level.getRightEdge())
			level.moveRight(frameMovement);
		else if(!isFacingRight() && getXLocation() <= level.getLeftEdge())
			level.moveLeft(frameMovement);
	}
	public void jump() {
		super.jump();
		GameLevel level = Game.getLevel();
		int airMovement = getJumpMovement();
		if(getYLocation() <= level.getTopEdge())
			level.moveUp(airMovement);
	}
	public void fall() {
		super.fall();
		GameLevel level = Game.getLevel();
		int gravityMovement = getFallMovement();
		if(getYLocation()+sprite().getHeight(Game.getFrame()) >= level.getBottomEdge())
			level.moveDown(gravityMovement);
	}
	public void attack() {
		if(spriteChangedThisFrame()) {
			ArrayList<Actor> actors = Game.getActors();
			for(int x=0;x<actors.size();x++) {
				Actor enemy = actors.get(x);
				if(this.isInSameArea(enemy)) {
					enemy.hurt(getAtk()/getAnimationLength(getName(), getAction()));
					return;
				}
			}
		}
	}
	public void shootBlast() {
		if(spriteChangedThisFrame()) {
			int x;
			if(isFacingRight())
				x = getXLocation()+sprite().getWidth(null);
			else
				x = getXLocation();
			Game.getActors().add(
				new PlayerBlast(x, getYLocation()+sprite().getHeight(null)/5, isFacingRight(), blastStrength)); 
			blastsLeft--;
		}
	}
	
	public int attacking() {
		return 3;
	}
	public int falling() {
		return 5;
	}
	public int jumping() {
		return 6;
	}
	public int moving() {
		return 8;
	}
	
	public void keyTyped (KeyEvent typed){}
	public void keyPressed (KeyEvent pressed) {
		int pressedCode = pressed.getKeyCode();
		if(pressedCode<256) {
			if(!keysPressed[pressedCode] && getAction()!=jumping())
				inputThisAnimation = true;
			keysPressed[pressedCode] = true;
		}
	}
	public void keyReleased (KeyEvent released) {
		int releasedCode = released.getKeyCode();
		if(releasedCode<256) {
			keysPressed[releasedCode] = false;
			if(!isMidAir())
				inputThisAnimation = true;
		}
	}
	public void clearInput() {
		keysPressed = new boolean[256];
	}
}
