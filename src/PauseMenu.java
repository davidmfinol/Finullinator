import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PauseMenu extends JPanel {
	
	public PauseMenu (Dimension size) {
		super(true);
		ActionListener listener = new MenuListener(this);
		setBackground(Color.BLACK);
		setOpaque(true);
		setBounds((int)size.getWidth()/6, (int)size.getHeight()/6, (int)size.getWidth()*2/3, (int)size.getHeight()*2/3);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JButton returnToGame = new JButton("Return to Game");
		returnToGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		returnToGame.setActionCommand("return");
		returnToGame.addActionListener(listener);
			
		JButton mainMenu = new JButton("Main Menu");
		mainMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainMenu.setActionCommand("main");
		mainMenu.addActionListener(listener);
		
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
	
		Dimension dm = new Dimension((int)size.getWidth()/24, (int)size.getHeight()/24);
		add(new Box.Filler(dm, dm, dm));
		add(returnToGame);
		add(new Box.Filler(dm, dm, dm));
		add(mainMenu);
		add(new Box.Filler(dm, dm, dm));
		add(options);
		add(new Box.Filler(dm, dm, dm));
		add(gameInformation);
		add(new Box.Filler(dm, dm, dm));
		add(exit);
		validate();
	}
}
