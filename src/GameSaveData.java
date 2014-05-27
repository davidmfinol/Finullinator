import java.io.Serializable;
/**
 *Where all character data is saved for later gameplay
*/
public class GameSaveData implements Serializable{
	protected GameLevel level;
	protected int health, maxHealth, atk, xLoc, yLoc, bulletStrength, blastStrength, blastsLeft;
	protected boolean facingRight;
	
	public GameSaveData() {
		health = 100;
		maxHealth = 1500;
		atk = 12;
		xLoc = 100;
		yLoc = 100;
		level = new LevelOne(0, 0);
		facingRight = true;
		bulletStrength = 10;
		blastStrength = 100;
		blastsLeft = 2;
	}
}