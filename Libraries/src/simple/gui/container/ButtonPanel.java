package simple.gui.container;

import javax.swing.*;

import simple.gui.event.ButtonPanelEvent;
import simple.gui.event.ButtonPanelListener;

import java.awt.event.*;
import java.awt.FlowLayout;
import java.util.Vector;


/**JPanel wrapper for buttons. Will forward the actions performed to the
 * subscribed listeners.
 * <hr>
 * Requires:
 * <ul><li>{@link simple.gui.event.ButtonPanelEvent}<li>{@link simple.gui.event.ButtonPanelListener}</ul>
 * <br>Created: 2005
 * @author Kenneth Pierce
 */
public class ButtonPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final Vector<ButtonPanelListener> listeners = new Vector<ButtonPanelListener>();
	public static final int LEFT = FlowLayout.LEFT,
						RIGHT = FlowLayout.RIGHT,
						CENTER = FlowLayout.CENTER;
	public ButtonPanel(int align) {
		super(new FlowLayout(align));
	}
	public void addButtonListener(ButtonPanelListener ls) {
		listeners.add(ls);
	}
	public synchronized void actionPerformed(ActionEvent e) {
		ButtonPanelEvent event = new ButtonPanelEvent(e);
		for (ButtonPanelListener cur : listeners) {
			cur.actionPerformed(event);
		}
	}
	public void add(JButton button) {
		button.addActionListener(this);
		super.add(button);
	}
	
}
