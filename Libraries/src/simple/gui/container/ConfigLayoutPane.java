package simple.gui.container;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JComponent;
import javax.swing.JPanel;

/** A JPanel that more easily allows for labels one side and fields on the other.
 * <hr>
 * <br>Created: 2008
 * @author Kenneth Pierce
 */
public class ConfigLayoutPane extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int CENTER = FlowLayout.RIGHT, LEFT = FlowLayout.LEFT; 
	private final int align;
	private int rowLeft = 0;
	private int rowRight = 0;
	GridBagConstraints con = new GridBagConstraints();
	public ConfigLayoutPane(int align) {
		super(new GridBagLayout());
		this.align = align;
		con.fill = GridBagConstraints.HORIZONTAL;
	}
	/** Adds the component to the left grid.
	 * @param c component to be added
	 */
	public void addLeft(JComponent c) {
		JPanel tmp = new JPanel(new FlowLayout(align));
		con.gridx = 0;
		con.gridy = rowLeft++;
		tmp.add(c);
		add(tmp, con);
	}
	/** Adds the component to the right grid.
	 * @param c component to be added
	 */
	public void addRight(JComponent c) {
		JPanel tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		con.gridx = 1;
		con.gridy = rowRight++;
		tmp.add(c);
		add(tmp, con);
	}
	/** Adds the components to the specified grids.
	 * @param left the left component
	 * @param right the right component
	 */
	public void add(JComponent left, JComponent right) {
		addLeft(left);
		addRight(right);
	}
	/** Adds the component in the next available grid.
	 * @param c component to be added
	 */
	public void add(JComponent c) {
		if (con.gridx == 1) {
			addLeft(c);
		} else {
			addRight(c);
		}
	}
}
