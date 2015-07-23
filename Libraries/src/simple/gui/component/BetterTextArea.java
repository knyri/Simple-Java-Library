/**
 *
 */
package simple.gui.component;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**JTextArea wrapped in a JScrollPane with append(..), appendln(..) and clear() functions.
 * <br>Created: Nov 23, 2010
 * @author Kenneth Pierce
 */
public final class BetterTextArea extends JComponent {
	/**
	 *
	 */
	private static final long serialVersionUID = -6881880883567252576L;
	private final JTextArea out = new JTextArea();
	private final JScrollPane pane = new JScrollPane(out);
	public BetterTextArea() {
		setLayout(new BorderLayout());
		add(pane);
	}
	public JScrollPane getScrollPane(){
		return pane;
	}
	public JTextArea getTextArea(){
		return out;
	}
	public BetterTextArea append(String s) {
		out.append(s);
		return this;
	}
	public BetterTextArea appendln(String s) {
		out.append(s);
		out.append("\n");
		return this;
	}
	public void clear() {
		out.setText("");
	}
	public BetterTextArea appendln(){
		out.append("\n");
		return this;
	}
}
