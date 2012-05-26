package simple.gui;

import javax.swing.JPanel;
import javax.swing.JMenu;

/**
 * <br>Created: ??
 * @author Kenneth Pierce
 */
public abstract class AbstractProgram extends JPanel {
	private static final long serialVersionUID = 1L;
	protected abstract JPanel buildGUI(String[] args);
	protected abstract JMenu buildMenu();
}
