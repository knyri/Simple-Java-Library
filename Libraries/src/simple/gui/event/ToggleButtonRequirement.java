/**
 * 
 */
package simple.gui.event;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

/**Actually takes any AbstractButton. The requirement is met if
 * {@link javax.swing.AbstractButton#isSelected()} equals the supplied value.
 * <hr>
 * <br>Created: Jan 12, 2011
 * @author Kenneth Pierce
 */
public class ToggleButtonRequirement implements Requirement {
	private final AbstractButton req;
	private final boolean value;
	public ToggleButtonRequirement(AbstractButton req, boolean value) {
		this.req = req;
		this.value = value;
	}
	/**Gets whether the requirement is met.
	 * @return True if the requirement isSelected() value equals the value supplied.
	 * @see simple.gui.event.Requirement#isMet()
	 */
	public boolean isMet() {
		return req.isSelected() == value;
	}

	/**
	 * @param al
	 * @see simple.gui.event.Requirement#addActionListener(java.awt.event.ActionListener)
	 */
	public void addActionListener(ActionListener al) {
		req.addActionListener(al);
	}

}
