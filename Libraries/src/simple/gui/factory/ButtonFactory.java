/**
 * 
 */
package simple.gui.factory;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/** Makes buttons!<br>
 * Slimmer alternative to {@link simple.gui.factory.SwingFactory}
 * <br>Created: May 14, 2009
 * @author Kenneth Pierce
 */
public final class ButtonFactory {

	protected ButtonFactory() {}
	/**
	 * Creates a JButton with common properties already set.
	 * 
	 * @param display The text to be displayed on the button.
	 * @param actionCommand The action command to be set with JButton.setActionCommand(String)
	 * @return JButton with text <var>display</var> and actionCommand <var>actionCommand</var>
	 * @see javax.swing.AbstractButton
	 */
	public static JButton makeJButton(String display, String actionCommand) {
		JButton bTmp = new JButton(display);
		bTmp.setActionCommand(actionCommand);
		return bTmp;
	}
	/**
	 * Creates a JButton with common properties already set.
	 * 
	 * @param display The text to be displayed on the button.
	 * @param actionCommand The action command to be set with JButton.setActionCommand(String)
	 * @param al ActionListener to be added to the resulting JButton.
	 * @return JButton with text <var>display</var> and actionCommand <var>actionCommand</var>
	 * @see javax.swing.AbstractButton
	 */
	public static JButton makeJButton(String display, String actionCommand, ActionListener al) {
		JButton bTmp = new JButton(display);
		bTmp.setActionCommand(actionCommand);
		bTmp.addActionListener(al);
		return bTmp;
	}
}
