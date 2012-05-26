/**
 * 
 */
package simple.gui.factory;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/** Makes menus!<br>
 * Slimmer alternative to {@link simple.gui.factory.SwingFactory}
 * <br>Created: May 14, 2009
 * @author Kenneth Pierce
 */
public final class MenuFactory {
	protected MenuFactory() {}
	/**
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @return JMenuItem with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 */
	public static JMenuItem makeJMenuItem(String text, String command) {
		JMenuItem tmp = new JMenuItem(text);
		tmp.setActionCommand(command);
		return tmp;
	}
	/**
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @return JMenuItem with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 */
	public static JMenu makeJMenu(String text, String command) {
		JMenu tmp = new JMenu(text);
		tmp.setActionCommand(command);
		return tmp;
	}
	/**
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @param al ActionListener to set for the returned JMenuItem
	 * @return JMenuItem with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 */
	public static JMenuItem makeJMenuItem(String text, String command, ActionListener al) {
		JMenuItem tmp = new JMenuItem(text);
		tmp.setActionCommand(command);
		tmp.addActionListener(al);
		return tmp;
	}
	/**
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @param al ActionListener to set for the returned JMenu
	 * @return JMenuItem with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 */
	public static JMenu makeJMenu(String text, String command, ActionListener al) {
		JMenu tmp = new JMenu(text);
		tmp.setActionCommand(command);
		tmp.addActionListener(al);
		return tmp;
	}
}
