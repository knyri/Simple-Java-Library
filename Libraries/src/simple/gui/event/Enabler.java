/**
 *
 */
package simple.gui.event;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**Enables/disables a GUI component based on the requirements.
 * <hr>
 * <br>Created: Jan 12, 2011
 * @author Kenneth Pierce
 */
public class Enabler implements ActionListener {
	private final List<Requirement> reqs = Collections.synchronizedList(new LinkedList<Requirement>());
	private final Component target;
	private boolean enabled = true;

	public Enabler(Component target) {
		this.target = target;
	}
	/**
	 * @param arg0
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		synchronized(reqs){
			for (Requirement req : reqs) {
				if (!req.isMet()) {
					target.setEnabled(false);
					return;
				}
			}
		}
		target.setEnabled(true);
	}
	/**
	 * Forces a re-check of the requirements.
	 * Basically calls <code>actionPerformed(null)</code>
	 */
	public final void forceCheck() {
		actionPerformed(null);
	}
	/**
	 * @return the enabled
	 */
	public final boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public final void addRequirement(Requirement req) {
		reqs.add(req);
	}
	public final void removeRequirement(Requirement req) {
		reqs.remove(req);
	}
}
