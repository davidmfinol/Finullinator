public class Sandrock extends Actor {
	protected static final String [] actions = {"Rest", "Attack", "Crouch", "Fall", "Jump", "MeleeAttack", "Run"};
	int xDistance, yDistance;
		
	public Sandrock(int x, int y) {
		super(x, y);
	}
	
	public int getAnimationLength(String name, int actionNumber) {
		int length;
		switch (actionNumber) {
			case 0 : length = 3; break;
			case 1 : length = 4; break;
			case 2 : length = 1; break;
			case 3 : length = 1; break;
			case 4 : length = 2; break;
			case 5 : length = 6; break;
			case 6 : length = 1; break;
			default : System.err.println("Error in choosing animation length for "+getName()); length = 1; break;
		}
		return length;
	}
	public String[] getActionNames() {
		return actions;
	}
	
	public String getName() {
		return "Sandrock";
	}
	public String getFileType() {
		return "png";
	}
	
	public int attacking() {
		return 5;
	}
	public int falling() {
		return 3;
	}
	public int jumping() {
		return 4;
	}
	public int moving() {
		return 6;
	}
	public boolean actionLoops() {
		if(getAction()==jumping())
			return true;
		return false;
	}

	public int getMaxHealth() {
		return 100;
	}
	public int getAtk() {
		return 25;
	}
	
	public void update(long frameElapsedTime) {
		Actor player = Game.getPlayer();
		yDistance = player.getYLocation()+player.sprite().getHeight(null)-getYLocation();
		if(isFacingRight())
			xDistance = player.getXLocation()-(getXLocation()+sprite().getWidth(null));
		else
			xDistance = getXLocation()-(player.getXLocation()+player.sprite().getWidth(null));
		super.update(frameElapsedTime);
	}
	public int nextAction() {
		int action = 0; 
		if(xDistance>-300 && xDistance<300) {
			if( (getGravity()==0&&yDistance>sprite().getHeight(null)) ^ (getGravity()!=0&&isMidAir()&&yDistance>=0) )
				action = falling(); 
			else {
				if(yDistance<0) 
					action = jumping();
				else if(xDistance > -300 && xDistance < -50) {
					changeDirection();
					action = 1;
				}
				else if(xDistance < -30) {
					changeDirection();
					action = moving();
				}
				else if(xDistance < 0) {
					changeDirection();
					action = attacking();
				}
				else if(xDistance < 30)
					action = attacking();
				else if(xDistance < 50)
					action = moving();
				else if(xDistance < 300)
					action = 1;
			}
		}
		return action;
	}
	public void performAction(int animationNumber) {
		switch (getAction()) {
			case 1 : if(animationNumber==3)
				shootBullet(20); break;
			case 3 : fall(); 
				if(xDistance<-1*sprite().getWidth(null))
					changeDirection();
				if(xDistance>10 && yDistance<100)
					move(); break;
			case 4 :if(animationNumber==1)
				jump();
				if(xDistance<-1*sprite().getWidth(null))
					changeDirection();
				if(xDistance>10 && yDistance<100)
					move();  break;
			case 5 : attack(); break;
			case 6 : move(); break;
		}
		
	}	
	public void determineAirMovement(long frameElapsedTime, int animationNumber) {
		if(getGravity() != 0) {
			if(getAction()==jumping())
				setFallSpeed(getXSpeed()/2);
			if(getAction()==falling() && getFallSpeed()<getXSpeed()*3){
				double speedChange = getGravity()*frameElapsedTime;
				setFallSpeed(getFallSpeed()+speedChange);
			}
			if(!isMidAir()){
				setJumpSpeed(getXSpeed());
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
}
