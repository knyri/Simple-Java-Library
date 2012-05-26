/**
 * 
 */
package simple.gui.event;

import java.awt.event.ActionListener;

/**
 * <hr>
 * <br>Created: Jan 12, 2011
 * @author Kenneth Pierce
 */
public interface Requirement {
	public boolean isMet();
	/**Adds the action listener to the component being monitored.
	 * @param al
	 */
	public void addActionListener(ActionListener al);
}
