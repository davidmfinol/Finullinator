import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;

public class GameOverMenu extends JPanel {
	Font font;
	Color back;
	public GameOverMenu(Dimension size) {
		super(true);
		back = new Color(128, 0, 0);
		font = new Font("Harlow Solid Italic", Font.BOLD, (int)size.getHeight()/20);
		ActionListener listener = new MenuListener();
		setOpaque(true);
		setSize(size);
		
		JButton again = new JButton("Try Again");
		again.setAlignmentX(Component.CENTER_ALIGNMENT);
		again.setActionCommand("load");
		again.addActionListener(listener);
		
		JButton mainMenu = new JButton("Main Menu");
		mainMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainMenu.setActionCommand("main");
		mainMenu.addActionListener(listener);
		
		add(again);
		add(mainMenu);
		validate();
	}
	public void paint(Graphics g) {
		g.setColor(back);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString("Haha! You have Died!!!", getWidth()/3, getHeight()/2);
		paintComponents(g);
	}
}
