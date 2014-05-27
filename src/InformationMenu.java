import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InformationMenu extends JPanel {
	Color write = new Color(128,0,0);
	public InformationMenu (Dimension size, boolean isPaused) {
		super(true);
		ActionListener listener = new MenuListener(this);
		setBackground(Color.BLACK);
		setOpaque(true);
		if(isPaused)
			setBounds((int)size.getWidth()/6, (int)size.getHeight()/6, (int)size.getWidth()*2/3, (int)size.getHeight()*2/3);
		else
			setSize(size);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JButton goBack = new JButton("Return");
		goBack.setAlignmentX(Component.CENTER_ALIGNMENT);
		goBack.setActionCommand("return");
		goBack.addActionListener(listener);
		add(goBack);
		
		validate();
	}
	
	public void paint (Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setFont(new Font("Times New Roman", Font.BOLD, getHeight()/20));
		g.setColor(write);
		g.drawString("This game was made by David Finol and Christiaan Cleary.", 0 , getHeight()/10);
		g.drawString("It's more of a piece of a game engine than anything. ", 0, getHeight()/10*2);
		g.drawString("Move: Arrow Keys. Jump: Space. Attack: Control.", 0, getHeight()/10*3);
		paintComponents(g);
	}
}
