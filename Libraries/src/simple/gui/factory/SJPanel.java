/**
 *
 */
package simple.gui.factory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/** Makes JPanels of all kinds!
 * <br>Created: Mar 6, 2009
 * @author Kenneth Pierce
 */
public final class SJPanel {
	protected SJPanel() {}
	/**
	 * Values from {@link javax.swing.BoxLayout}.
	 */
	public final static int X_AXIS = BoxLayout.X_AXIS,
	Y_AXIS = BoxLayout.Y_AXIS;
	/**
	 * LEFT = BorderLayout.WEST<br>
	 * RIGHT = BorderLayout.EAST
	 * @see java.awt.BorderLayout
	 */
	public final static String LEFT = BorderLayout.WEST,
	RIGHT = BorderLayout.EAST;
	//create labeled panels

	/**
	 * Used to create a preformatted JPanel with a JLabel bound to the left using a BorderLayout. Used by
	 * {@link simple.forms.Form Form} to create the labeled text boxes.
	 *
	 * @param title Text to be displayed on the left.
	 * @return A new JPanel with a JLabel on the left with <var>title</var> set as its text.
	 */
	public static JPanel makeLabeledPanel(final String title) {
		final JPanel tmp = new JPanel(new BorderLayout());
		tmp.add(new JLabel(title), LEFT);
		return tmp;
	}
	/**
	 * Variant of {@link #makeLabeledPanel(String)}. Lets you decide whether
	 * the text is displayed on the left or the right.
	 *
	 * @param title Text to be displayed.
	 * @param orient One of {@link #LEFT} or {@link #RIGHT}.
	 * @return A new JPanel with a JLabel at <var>orient</var> with <var>title</var> set as its text.
	 */
	public static JPanel makeLabeledPanel(final String title, final String orient) {
		final JPanel tmp = new JPanel(new BorderLayout());
		tmp.add(new JLabel(title), orient);
		return tmp;
	}
	/**
	 * Extended version of {@link #makeLabeledPanel(String)}.<br>
	 * Creates new JPanel and adds a JLabel on the left with text <var>title</var> and the JComponent <var>c</var>.
	 *
	 * @param c JComonent to be added.
	 * @param title Text to be displayed on the left.
	 * @return The created JPanel.
	 */
	public static JPanel makeLabeledPanel(final JComponent c, final String title) {
		final JPanel tmp = new JPanel(new BorderLayout());
		tmp.add(new JLabel(title), LEFT);
		tmp.add(c);
		return tmp;
	}
	/**
	 * Creates new JPanel and adds a JLabel on the left with text <var>title</var> and the JComponent <var>c</var>.
	 *
	 * @param c JComponent to be added.
	 * @param title Text to be displayed on the left.
	 * @return The created JPanel.
	 */
	public static JPanel makeLabeledPanel2(final JComponent c, final String title) {
		final JPanel tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tmp.add(new JLabel(title));
		tmp.add(c);
		return tmp;
	}
	/**
	 * Extended version of {@link #makeLabeledPanel(String, String)}.
	 * Variant of {@link #makeLabeledPanel(JComponent, String)}.
	 * Allows you to set the orient of the JLabel.
	 *
	 * @param c JComponent to be added.
	 * @param title Text to be displayed.
	 * @param orient One of {@link #LEFT} or {@link #RIGHT}.
	 * @return The resulting JPanel.
	 */
	public static JPanel makeLabeledPanel(final JComponent c, final String title, final String orient) {
		final JPanel tmp = new JPanel(new BorderLayout());
		tmp.add(new JLabel(title), orient);
		tmp.add(c);
		return tmp;
	}
	/**
	 * Variant of {@link #makeLabeledPanel2(JComponent, String)}.
	 * Extended version of (@link #getLabeledPanel2(JComponent, String)}.
	 *
	 * @param c JComponent to be added.
	 * @param title Text to be displayed.
	 * @param orient
	 * @return The resulting JPanel.
	 */
	public static JPanel makeLabeledPanel2(final JComponent c, final String title, final String orient) {
		if (orient==RIGHT) {
			final JPanel tmp = new JPanel(new FlowLayout());
			tmp.add(c);
			tmp.add(new JLabel(title));
			return tmp;
		} else
			return makeLabeledPanel2(c, title);
	}
	//create titled panels
	/**
	 * Creates an empty JPanel with a TitledBorder.
	 *
	 * @param title Title to be displayed.
	 * @return An empty JPanel with a TitledBorder.
	 */
	public static JPanel makeTitledPanel(final String title) {
		final JPanel tmp = new JPanel();//new BorderLayout());
		tmp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return tmp;
	}
	/**
	 * @param c JComponent to be added.
	 * @param title Title to be displayed.
	 * @return A JPanel with a TitledBorder and <var>c</var> added to it.
	 */
	public static JPanel makeTitledPanel(final JComponent c, final String title) {
		final JPanel tmp = new JPanel();//new BorderLayout());
		tmp.add(c);
		tmp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return tmp;
	}
	/**
	 * Variant of {@link #makeTitledPanel(JComponent, String)}.
	 *
	 * @param c Array of JComponents to be added.
	 * @param title Title to be displayed.
	 * @return The resulting JPanel.
	 */
	public static JPanel makeTitledPanel(final JComponent[] c, final String title) {
		final JPanel tmp = new JPanel();
		for (int i = 0;i<c.length;i++) {
			tmp.add(c[i]);
		}
		tmp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return tmp;
	}
	/**
	 * Creates an empty JPanel with a TitledBorder and sets the LayoutManager to <var>m</var>.
	 *
	 * @param title Title to be displayed.
	 * @param m LayoutManager to use.
	 * @return The resulting JPanel.
	 */
	public static JPanel makeTitledPanel(final String title, final LayoutManager m) {
		final JPanel tmp = new JPanel(m);//new BorderLayout());
		tmp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return tmp;
	}
	public static JPanel makeTitledPanel(final JComponent c, final String title, final LayoutManager m) {
		final JPanel tmp = new JPanel(m);//new BorderLayout());
		tmp.add(c);
		tmp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return tmp;
	}
	public static JPanel makeTitledPanel(final JComponent[] c, final String title, final LayoutManager m) {
		final JPanel tmp = new JPanel(m);//new BorderLayout());
		for (int i = 0;i<c.length;i++) {
			tmp.add(c[i]);
		}
		tmp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return tmp;
	}
	//intended for box layout only
	/**
	 * Creates a JPanel with BoxLayout as its LayoutManager. Adds a
	 * TitledBorder to the JPanel and adds each element of <var>c</var>.
	 *
	 * @param c Array of JComponents to be added.
	 * @param title Title to be displayed.
	 * @param axis One of (@link javax.swing.BoxLayout#PAGE_AXIS}, (@link javax.swing.BoxLayout#X_AXIS}, (@link javax.swing.BoxLayout#Y_AXIS}, (@link javax.swing.BoxLayout#PAGE_AXIS}.
	 * @return The resulting JPanel.
	 * @see javax.swing.BoxLayout
	 */
	public static JPanel makeTitledPanel(final JComponent[] c, final String title, final int axis) {
		final JPanel tmp = new JPanel();
		final BoxLayout m = new BoxLayout(tmp, axis);
		tmp.setLayout(m);
		for (int i = 0;i<c.length;i++) {
			tmp.add(c[i]);
		}
		tmp.setBorder(new TitledBorder(LineBorder.createBlackLineBorder(),title));
		return tmp;
	}
	/**
	 * Creates a JPanel and adds components on the X_AXIS(Horizontal).
	 *
	 * @param c Array of JComponents to be added.
	 * @return BoxLayout JPanel with JComponents on the X_AXIS.
	 * @see javax.swing.BoxLayout
	 */
	public static JPanel makeBoxLayoutPanelX(final JComponent[] c) {
		final JPanel tmp = makeBoxLayoutPanelX();
		for (int i = 0;i<c.length;i++) {
			tmp.add(c[i]);
		}
		return tmp;
	}

	/**
	 * Creates a JPanel and adds components on the Y_AXIS(Vertical).
	 *
	 * @param c Array of JComponents to be added.
	 * @return BoxLayout JPanel with JComponents on the Y_AXIS.
	 * @see javax.swing.BoxLayout
	 */
	public static JPanel makeBoxLayoutPanelY(final JComponent[] c) {
		final JPanel tmp = makeBoxLayoutPanelY();
		for (int i = 0;i<c.length;i++) {
			tmp.add(c[i]);
		}
		return tmp;
	}
	/**
	 * Creates a JPanel with a BoxLayout manager that aligns<br>
	 * added components on the Y_AXIS(Vertical).
	 *
	 * @return BoxLayout JPanel with vertical alignment.
	 * @see javax.swing.BoxLayout
	 */
	public static JPanel makeBoxLayoutPanelY() {
		return makeBoxLayoutPanel(Y_AXIS);
	}
	/**
	 * Creates a JPanel with a BoxLayout manager that aligns<br>
	 * added components on the X_AXIS(Horizontal).
	 *
	 * @return BoxLayout JPanel with horizontal alignment.
	 * @see javax.swing.BoxLayout
	 */
	public static JPanel makeBoxLayoutPanelX() {
		return makeBoxLayoutPanel(X_AXIS);
	}

	public static JPanel makeBoxLayoutPanel(final int axis) {
		return makeBoxLayoutPanel(null, axis);
	}
	/**
	 * Applies a BoxLayout manager that aligns<br>
	 * added components on the Y_AXIS(Vertical).
	 *
	 * @param panel The JPanel to apply this to.
	 * @return BoxLayout JPanel with vertical alignment.
	 * @see javax.swing.BoxLayout
	 */
	public static JPanel makeBoxLayoutPanelY(final JPanel panel) {
		return makeBoxLayoutPanel(panel, Y_AXIS);
	}
	/**
	 * Applies a BoxLayout manager that aligns<br>
	 * added components on the X_AXIS(Horizontal).
	 *
	 * @param panel The JPanel to apply this to.
	 * @return BoxLayout JPanel with horizontal alignment.
	 * @see javax.swing.BoxLayout
	 */
	public static JPanel makeBoxLayoutPanelX(final JPanel panel) {
		return makeBoxLayoutPanel(panel, X_AXIS);
	}

	public static JPanel makeBoxLayoutPanel(JPanel panel, final int axis) {
		if (panel == null) {
			panel = new JPanel();
		}
		panel.setLayout(new BoxLayout(panel, axis));
		return panel;
	}
	public static final JPanel getNewLine(){
		JPanel t=new JPanel();
		t.setLayout(new BoxLayout(t,BoxLayout.LINE_AXIS));
		t.setAlignmentX(0);
		return t;
	}

	public static final JPanel getNewPage(){
		JPanel t=new JPanel();
		t.setLayout(new BoxLayout(t,BoxLayout.PAGE_AXIS));
		return t;
	}
	//simple JPanel wrappers
	/**
	 * Useful to avoid the nasty effects of BoxLayout and GridLayout on preferred sizes.
	 *
	 * @param c
	 * @return <var>c</var> in a JPanel.
	 */
	public static JPanel wrapInJPanel(final JComponent c) {
		final JPanel tmp = new JPanel();
		tmp.add(c);
		return tmp;
	}
	/**
	 * Wraps c in a JPanel with LayoutManager m.
	 * @param c JComponent to add to the JPanel.
	 * @param m LayoutManger for the JPanel.
	 * @return <var>c</var> wrapped in a JPanel with LayoutManager <var>m</var>
	 * @see #wrapInJPanel(JComponent)
	 */
	public static JPanel wrapInJPanel(final JComponent c, final LayoutManager m) {
		final JPanel tmp = new JPanel(m);
		tmp.add(c);
		return tmp;
	}
	/**
	 * Creates a JPanel with GridLayout <var>m</var> and adds the elements of <var>c</var>.
	 *
	 * @param c Array of JComponents to be added.
	 * @param m GridLayout to be used.
	 * @return The resulting JPanel.
	 */
	public static JPanel wrapInJPanel(final JComponent c[], final GridLayout m) {
		final JPanel tmp = new JPanel(m);
		for (int i = 0;i<c.length;i++) {
			tmp.add(c[i]);
		}
		return tmp;
	}
	/**
	 * Creates a JPanel with a GridLayout with the specified number of columns and rows
	 * and adds the items of c to the JPanel.
	 * @param c Array of JComponents to add to the JPanel
	 * @param cols Number of columns
	 * @param rows Number of rows.
	 * @return A JPanel with a GridLayout.
	 */
	public static JPanel wrapInJPanel(final JComponent c[], final int cols, final int rows) {
		return wrapInJPanel(c, new GridLayout(cols, rows));
	}
	/**
	 * Mainly useful for GridBagLayout.<br>
	 * c.length must equal args.length.
	 *
	 * @param c Array of JComponents to be added.
	 * @param m LayoutManager to use.
	 * @param args Arguments to use when adding elements in <var>c</var>.
	 * @return The resulting JPanel.<br>
	 * 		null if <code>c==null or args==null or m==null or c.length!=args.length</code>
	 */
	public static JPanel wrapInJPanel(final JComponent c[], final LayoutManager m, final Object[] args) {
		if (c==null||args==null||m==null||c.length!=args.length)
			return null;
		final JPanel tmp = new JPanel(m);
		for (int i = 0;i<args.length;i++) {
			tmp.add(c[i], args[i]);
		}
		return tmp;
	}
}
