/**
 * 
 */
package simple.gui.container;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import simple.gui.event.ButtonPanelEvent;
import simple.gui.event.ButtonPanelListener;

/** A simple panel for laying out buttons in a row or column. Will forward the
 * action event to the subscribed listeners.
 * <hr>
 * Requires:
 * <ul><li>{@link simple.gui.event.ButtonPanelEvent}<li>{@link simple.gui.event.ButtonPanelListener}</ul>
 * <br>Created: Sep 1, 2008
 * @author Kenneth Pierce
 */
public class ButtonGrid extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final Vector<ButtonPanelListener> listeners = new Vector<ButtonPanelListener>();
	public static final int X_AXIS = BoxLayout.X_AXIS, Y_AXIS = BoxLayout.Y_AXIS;
	/** Lays buttons out on the specified axis.<br>
	 * uses a BoxLayout
	 * @param alignment {@link #X_AXIS} or {@link #Y_AXIS}
	 * @see javax.swing.BoxLayout
	 */
	public ButtonGrid(int alignment) {
		BoxLayout bl = new BoxLayout(this, alignment);
		this.setLayout(bl);
	}
	/** Lays the buttons out in a grid.<br>
	 * Uses a GridLayout.
	 * @param cols Number of columns
	 * @param rows Number of rows
	 * @see java.awt.GridLayout
	 */
	public ButtonGrid(int cols, int rows) {
		this.setLayout(new GridLayout(cols, rows));
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
