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
	public void append(String s) {
		out.append(s);
	}
	public void appendln(String s) {
		out.append(s+'\n');
	}
	public void clear() {
		out.setText("");
	}
}
