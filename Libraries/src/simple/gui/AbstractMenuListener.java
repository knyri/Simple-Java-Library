package simple.gui;

import javax.swing.event.MenuEvent;

/** Allows one to implement only the events wanted.
 * <br>Created: ??
 * @author Kenneth Pierce
 */
public abstract class AbstractMenuListener implements javax.swing.event.MenuListener {
	public void menuCanceled(MenuEvent e) {}
	public void menuDeselected(MenuEvent e) {}
	public void menuSelected(MenuEvent e) {}
}
