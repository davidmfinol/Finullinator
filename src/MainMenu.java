import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//Design pictures on frame
public class MainMenu extends JPanel {
	
	/*Constructs a new instance of the main menu
	 *@param listener The listener object that chooses what to do when a button is pressed on the menu
	 *@param size The size in pixels of the menu
	 */
	public MainMenu(Dimension size) {
		super(true);
		ActionListener listener = new MenuListener();
		setBackground(Color.BLACK);
		setOpaque(true);
		setPreferredSize(size);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JButton load = new JButton("Continue");
		load.setAlignmentX(Component.CENTER_ALIGNMENT);
		load.setActionCommand("load");
		load.addActionListener(listener);
		
		JButton start = new JButton("New Game");
		start.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.setActionCommand("start");
		start.addActionListener(listener);
		
		JButton options = new JButton("Options");
		options.setAlignmentX(Component.CENTER_ALIGNMENT);
		options.setActionCommand("options");
		options.addActionListener(listener);
		
		JButton gameInformation = new JButton("Game Information");
		gameInformation.setAlignmentX(Component.CENTER_ALIGNMENT);
		gameInformation.setActionCommand("gameinfo");
		gameInformation.addActionListener(listener);
		
		JButton exit = new JButton("Exit");
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);
		exit.setActionCommand("exit");
		exit.addActionListener(listener);
		
		Dimension dm = new Dimension((int)size.getWidth()/4, (int)size.getHeight()/4);
		add(new Box.Filler(dm, dm, dm));
		dm = new Dimension((int)size.getWidth()/24, (int)size.getHeight()/24);
		add(load);
		add(new Box.Filler(dm, dm, dm));
		add(start);
		add(new Box.Filler(dm, dm, dm));
		add(options);
		add(new Box.Filler(dm, dm, dm));
		add(gameInformation);
		add(new Box.Filler(dm, dm, dm));
		add(exit);
		validate();
	}
}
