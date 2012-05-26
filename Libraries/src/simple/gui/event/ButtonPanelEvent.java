package simple.gui.event;

import java.awt.event.ActionEvent;
import javax.swing.JButton;

/**
 * <br>Created: 2005
 * @author Kenneth Pierce
 * @see simple.gui.container.ButtonPanel
 */
public class ButtonPanelEvent extends ActionEvent {

	private static final long serialVersionUID = 1L;
	public ButtonPanelEvent(JButton source, int id, String command) {
		super(source, id, command);
	}

	public ButtonPanelEvent(JButton source, int id, String command, int modifiers) {
		super(source, id, command, modifiers);
	}

	public ButtonPanelEvent(JButton source, int id, String command, long when,
			int modifiers) {
		super(source, id, command, when, modifiers);
	}
	public ButtonPanelEvent(ActionEvent e) {
		super(e.getSource(), e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers());
		if (!(e.getSource() instanceof JButton)) {
			throw new IllegalArgumentException("Source object is not a JButton or a subclass of.");
		}
	}
	public JButton getSource() {
		return (JButton)super.getSource();
	}
}
