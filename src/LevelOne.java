import java.awt.geom.Line2D;
import java.util.ArrayList;

public class LevelOne extends GameLevel {
	
	public LevelOne (int x, int y) {
		super(x, y);
	}
	
	public String getMapName() {
		return "test";
	}
	
	public String getSongName() {
		return "[filename]";
	}
	
	public ArrayList<Line2D> createWalls() {
		ArrayList<Line2D> temp = super.createWalls();
		temp.add(new Line2D.Double(310, 750, 310, 1024));
		temp.add(new Line2D.Double(375, 750, 375, 1024));
		temp.add(new Line2D.Double(470, 750, 470, 1024));
		temp.add(new Line2D.Double(535, 750, 535, 1024));
		temp.add(new Line2D.Double(630, 750, 630, 1024));
		temp.add(new Line2D.Double(695, 750, 695, 1024));
		temp.add(new Line2D.Double(755, 690, 755, 0));
		temp.add(new Line2D.Double(1590, 690, 1590, 0));
		temp.add(new Line2D.Double(2135, 750, 2135, 230));
		temp.add(new Line2D.Double(2875, 145, 2875, 462));
		temp.add(new Line2D.Double(2800, 235, 2800, 500));
		temp.add(new Line2D.Double(2490, 430, 2490, 595));
		temp.add(new Line2D.Double(2650, 430, 2650, 495));
		temp.add(new Line2D.Double(2650, 560, 2650, 600));
		return temp;
	}
	
	public ArrayList<Line2D> createFloors() {
		ArrayList<Line2D> temp = super.createFloors();
		temp.add(new Line2D.Double(0, 750, 310, 750));
		temp.add(new Line2D.Double(375, 750, 470, 750));
		temp.add(new Line2D.Double(535, 750, 630, 750));
		temp.add(new Line2D.Double(695, 750, 1030, 750));
		temp.add(new Line2D.Double(1030, 750, 1095, 785));
		temp.add(new Line2D.Double(1095, 785, 1285, 785));
		temp.add(new Line2D.Double(1285, 785, 1350, 750));
		temp.add(new Line2D.Double(760, 690, 1590, 690));
		temp.add(new Line2D.Double(1350, 750, 2135, 750));
		temp.add(new Line2D.Double(1590, 160, 2870, 160));
		temp.add(new Line2D.Double(1625, 690, 1910, 690));
		temp.add(new Line2D.Double(1825, 620, 2015, 620));
		temp.add(new Line2D.Double(1625, 495, 1750, 495));
		temp.add(new Line2D.Double(1625, 560, 1845, 560));
		temp.add(new Line2D.Double(1670, 375, 1940, 375));
		temp.add(new Line2D.Double(1590, 440, 1685, 440));
		temp.add(new Line2D.Double(1755, 315, 1835, 315));
		temp.add(new Line2D.Double(1870, 255, 1965, 255));
		temp.add(new Line2D.Double(1990, 230, 2100, 230));
		temp.add(new Line2D.Double(2135, 230, 2805, 230));
		temp.add(new Line2D.Double(1590, 270, 1625, 270));
		temp.add(new Line2D.Double(2100, 685, 2135, 685));
		temp.add(new Line2D.Double(1945, 525, 2100, 525));
		temp.add(new Line2D.Double(2020, 375, 2100, 375));
		temp.add(new Line2D.Double(1850, 435, 2005, 435));
		temp.add(new Line2D.Double(2650, 565, 2965, 565));
		temp.add(new Line2D.Double(2490, 595, 2650, 595));
		temp.add(new Line2D.Double(2490, 430, 2650, 430));
		temp.add(new Line2D.Double(2875, 460, 2970, 460));
		temp.add(new Line2D.Double(2965, 560, 3000, 595));
		temp.add(new Line2D.Double(3000, 595, 3050, 595));
		temp.add(new Line2D.Double(3050, 595, 3690, 915));
		return temp;
	}
	
	public ArrayList<Actor> createEnemies() {
		ArrayList<Actor> temp = new ArrayList<Actor>();
    	temp.add(new Mossflower(150, 150));
    	temp.add(new Sandrock(200, 200));
		return temp;
	}
	public GameLevel getNextLevel() {
		return null;
	}
}