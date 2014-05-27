import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class OptionsMenu extends JPanel{
	
	public OptionsMenu (Dimension size, boolean isPaused) {
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
			
		JLabel SELabel = new JLabel("Sound Effect Volume", JLabel.CENTER);
		JSlider SEVolume = new JSlider(JSlider.HORIZONTAL, 0, 100, Game.getSEVolume());
		SEVolume.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if(!source.getValueIsAdjusting())
					Game.setSEVolume((int)source.getValue());
			}
		});
		
		JLabel BGMLabel = new JLabel("Background Music Volume", JLabel.CENTER);
		JSlider BGMVolume = new JSlider(JSlider.HORIZONTAL, 0, 100, Game.getBGMVolume());
		BGMVolume.addChangeListener(new ChangeListener() {
			public void stateChanged (ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if(!source.getValueIsAdjusting())
					Game.setBGMVolume((int)source.getValue());
			}
		});
		
		JCheckBox sound = new JCheckBox("Sound");
		sound.setSelected(Game.getSound());
		sound.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.DESELECTED)
					Game.setSound(false);
				else
					Game.setSound(true);
			}
		});
		JComboBox res = new JComboBox(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes());
		
		Dimension dm = new Dimension((int)size.getWidth()/24, (int)size.getHeight()/24);
		add(goBack);
		add(new Box.Filler(dm, dm, dm));
		add(SELabel);
		add(new Box.Filler(dm, dm, dm));
		add(SEVolume);
		add(new Box.Filler(dm, dm, dm));
		add(BGMLabel);
		add(new Box.Filler(dm, dm, dm));
		add(BGMVolume);
		add(new Box.Filler(dm, dm, dm));
		add(sound);
		add(new Box.Filler(dm, dm, dm));
		add(res);
		validate();
	}
}
