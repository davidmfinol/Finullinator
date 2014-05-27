import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;

public class MenuListener implements ActionListener {
	private JComponent toDelete;
	
	public MenuListener() {
		toDelete = null;
	}
	public MenuListener(JComponent d) {
		toDelete = d;
	}
	
    /*Waits for an action to change the menu that appears on the main menu
     *@param e The name of the menu to bring up
     */
	public void actionPerformed(ActionEvent e) {
		Thread.yield();
			
		if(e.getActionCommand().equals("return")) {
			if(toDelete instanceof PauseMenu)
				GameStatus.revert();
			else if(toDelete instanceof OptionsMenu) {
				Game.saveSettings();
				if(GameStatus.isGameExited()) 
					Game.getFrame().largeMenu(new MainMenu(Game.getFrame().getGameCanvasSize()));
				else
					Game.getFrame().smallMenu(new PauseMenu(Game.getFrame().getGameCanvasSize()));
			}
			else if(toDelete instanceof InformationMenu)
				if(GameStatus.isGameExited()) 
					Game.getFrame().largeMenu(new MainMenu(Game.getFrame().getGameCanvasSize()));
				else
					Game.getFrame().smallMenu(new PauseMenu(Game.getFrame().getGameCanvasSize()));
		}
		else if(e.getActionCommand().equals("main"))
			Game.mainMenu();
		else if(e.getActionCommand().equals("load"))
			Game.load();
		else if(e.getActionCommand().equals("start"))
			Game.startNewGame();
		else if(e.getActionCommand().equals("options"))
			Game.optionsMenu();
		else if(e.getActionCommand().equals("gameinfo"))
			Game.gameInformation();
		else if(e.getActionCommand().equals("exit"))
			Game.exitGame();
			
		if(toDelete!=null) {
			Game.getFrame().getLayeredPane().remove(toDelete);
			if(toDelete instanceof PauseMenu)
				Game.getFrame().requestFocus();
		}
	}
}
