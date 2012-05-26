package simple.util;

/*
do_gui's purpose is simply to make my life easier
by doing repetative gui things for me.
*/
import javax.swing.*;
import javax.swing.border.*;
import java.util.EventObject;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
//import simple.debug;

/**
 * This class does repetitive things for GUI creation.<br>
 * Methods here are for convenience and for reducing creation time.
 * Depends on nothing else.
 * This class has grown too big and has been split into 5 classes:
 * <ol>
 * <li>{@link simple.gui.factory.SJFrame}
 * <li>{@link simple.gui.factory.SJOptionPane}
 * <li>{@link simple.gui.factory.SJPanel}
 * <li>{@link simple.gui.factory.SwingFactory}
 * </ol>
 * <br>Created: 2004
 * @deprecated See description for replacement classes
 * @author Kenneth Pierce
 * @version 1.3 
 */
public final class do_gui {
	protected do_gui() {}
	/**
	 * These are the message types and determines what icon will be displayed.
	 * <ul>
	 * <li>mINF = Information Message</li>
	 * <li>mGEN = General(Plain) Message</li>
	 * <li>mASK = Question Message.</li>
	 * <li>mWARN = Warning Message</li>
	 * <li>mERR = Error Message</li>
	 * </ul>
	 * Values taken from {@link javax.swing.JOptionpane}.
	 * @see javax.swing.JOptionPane
	 * @see #showMessage(String, String, int)
	 * @see #showQuestionMessage(String, String, int)
	 */
	public static final int mINF = JOptionPane.INFORMATION_MESSAGE,
		mGEN = JOptionPane.PLAIN_MESSAGE,
		mASK = JOptionPane.QUESTION_MESSAGE,
		mWARN = JOptionPane.WARNING_MESSAGE,
		mERR = JOptionPane.ERROR_MESSAGE;
	/**
	 * These define the options and return values for {@link #showQuestionMessage(String, String, int)}.<br>
	 * These values are taken from JOptionPane.
	 * <ul>
	 * <li>moYN = Shows the buttons Yes and No.</li>
	 * <li>moOC = Shows the buttons OK and Cancel</li>
	 * <li>moYNC = Shows the buttons Yes, No, and Cancel.</li>
	 * <li>moOK = Value returned if OK was selected.</li>
	 * <li>moCANCEL = Value returned if Cancel was selected.</li>
	 * <li>moYES = Value returned if Yes was selected.</li>
	 * <li>moNO = Value returned if No was selected.</li>
	 * <li>moCLOSED = Value returned if the window was closed without the user selecting something</li>
	 * </ul>
	 * @see javax.swing.JOptionPane
	 * @see #showQuestionMessage(String, String, int)
	 */
	public static final int moYN = JOptionPane.YES_NO_OPTION,
		moOC = JOptionPane.OK_CANCEL_OPTION,
		moYNC = JOptionPane.YES_NO_CANCEL_OPTION,
		moOK = JOptionPane.OK_OPTION,
		moNO = JOptionPane.NO_OPTION,
		moCANCEL = JOptionPane.CANCEL_OPTION,
		moYES = JOptionPane.YES_OPTION,
		moCLOSED = JOptionPane.CLOSED_OPTION;
	/**
	 * Defines preset border types.
	 */
	public final static int EmptyBorder = 0,
							BlackBorder = 1,
							WhiteBorder = 2;
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
	//Simple Y/N/C
	/**
	 * Displays a JOptionPane dialog box that asks a question and returns the user's selection.
	 * 
	 * @param mess Question to be displayed.
	 * @param title Title of the Dialog box.
	 * @param mOptions One of {@link #moYN}, {@link #moOC}, {@link #moYNC}.
	 * @return Result from {@link javax.swing.JOptionPane#showConfirmDialog(java.awt.Component, java.lang.Object, java.lang.String, int, int) JOptionPane.showConfirmationMessage(...)}.
	 */
	public static int showQuestionMessage(String mess, String title, int mOptions) {
		return JOptionPane.showConfirmDialog(null, mess, title, mOptions, mASK);
		}
	//Simple inform
	/**
	 * Displays a JOptionPane dialog box that displays a message.
	 * Calls {@link javax.swing.JOptionPane#showMessageDialog(java.awt.Component, java.lang.Object, java.lang.String, int)}.
	 * 
	 * @param mess Message to be displayed.
	 * @param title Title of the dialog box.
	 * @param mType One of {@link simple.util.do_gui#mINF mINF},
	 *  {@link simple.util.do_gui#mGEN mGEN},
	 *  {@link simple.util.do_gui#mASK mASK},
	 *  {@link simple.util.do_gui#mWARN mWARN}, or
	 *  {@link simple.util.do_gui#mERR mERR}.
	 */
	public static void showMessage(String mess, String title, int mType) {
		JOptionPane.showMessageDialog(null, mess, title, mType);
	}
	/**
	 * Prompts the user for text input.
	 * Calls (@link javax.swing.JOptionPane#showInputDialog(java.lang.Object, java.lang.Object)}.
	 * 
	 * @param message Message to be displayed.
	 * @param initValue Initial value of the text box.
	 * @return Contents of the input box.
	 */
	public static String prompt(String message, String initValue) {
		return JOptionPane.showInputDialog(message, initValue);
	}
	/**
	 * Prompts the used for text input.
	 * Calls {@link javax.swing.JOptionPane#showInputDialog(java.lang.Object)}
	 * 
	 * @param message Message to be displayed.
	 * @return Contents of the input box.
	 */
	public static String prompt(String message) {
		return JOptionPane.showInputDialog(message);
	}
	//add titled borders
	/**
	 * Calls <code>JComponent.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),<var>title</var>))</code>.
	 * 
	 * @param c JComponent to add the TitledBorder to.
	 * @param title The title to be set.
	 * @return The JComponent after setting a TitledBorder using an EmptyBorder and giving it title <var>title</var>.
	 * @see javax.swing.JComponent
	 * @see javax.swing.BorderFactory
	 */
	public static JComponent addTitledBorder (JComponent c, String title) {
		c.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return c;
	}
	/**
	 * @param c JComponent to add the border to.
	 * @param title Title to be set.
	 * @param borderType Type of border to use.
	 * @deprecated See {@link javax.swing.BorderFactory}
	 * @return The JComponent after setting a TitledBorder using the desired border type.
	 * @see javax.swing.border.TitledBorder
	 * @see javax.swing.BorderFactory
	 */
	public static JComponent addTitledBorder (JComponent c, String title, byte borderType) {
		Border b = null;
		switch (borderType) {
		case EmptyBorder:
			b = new EmptyBorder(0,0,0,0);
			break;
		case BlackBorder:
			b = LineBorder.createBlackLineBorder();
			break;
		case WhiteBorder:
			b = new LineBorder(Color.WHITE);
			break;
		}
		c.setBorder(new TitledBorder(b, title));
		return c;
	}
	/**
	 * Adds a border to any JComponent. It is recommended to wrap the JComponent
	 * in a JPanel first.
	 * 
	 * @param c JComponent to add the border to.
	 * @param borderType One of {@link #EmptyBorder}, {@link #WhiteBorder}, {@link #BlackBorder}
	 * @return <var>c</var> with a border.
	 * @deprecated See {@link javax.swing.BorderFactory}
	 * @see javax.swing.BorderFactory
	 */
	public static JComponent addBorder (JComponent c, byte borderType) {
		switch (borderType) {
		case EmptyBorder:
			c.setBorder(new EmptyBorder(0,0,0,0));
			break;
		case WhiteBorder:
			c.setBorder(new LineBorder(Color.WHITE));
			break;
		case BlackBorder:
			c.setBorder(LineBorder.createBlackLineBorder());
			break;
		}
		return c;
	}
	//create labeled panels
	
	/**
	 * Used to create a preformatted JPanel with a JLabel bound to the left using a BorderLayout. Used by
	 * {@link simple.forms.Form Form} to create the labeled text boxes.
	 * 
	 * @param title Text to be displayed on the left.
	 * @return A new JPanel with a JLabel on the left with <var>title</var> set as its text.
	 */
	public static JPanel makeLabeledPanel(String title) {
		JPanel tmp = new JPanel(new BorderLayout());
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
	public static JPanel makeLabeledPanel(String title, String orient) {
		JPanel tmp = new JPanel(new BorderLayout());
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
	public static JPanel makeLabeledPanel(JComponent c, String title) {
		JPanel tmp = new JPanel(new BorderLayout());
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
	public static JPanel makeLabeledPanel2(JComponent c, String title) {
		JPanel tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
	public static JPanel makeLabeledPanel(JComponent c, String title, String orient) {
		JPanel tmp = new JPanel(new BorderLayout());
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
	public static JPanel makeLabeledPanel2(JComponent c, String title, String orient) {
		JPanel tmp = new JPanel(new FlowLayout());
		if (orient==RIGHT) {
			tmp.add(c);
			tmp.add(new JLabel(title));
		} else {
			return makeLabeledPanel2(c, title);
		}
		
		return tmp;
	}
	//create titled panels
	/**
	 * Creates an empty JPanel with a TitledBorder.
	 * 
	 * @param title Title to be displayed.
	 * @return An empty JPanel with a TitledBorder.
	 */
	public static JPanel makeTitledPanel(String title) {
		JPanel tmp = new JPanel();//new BorderLayout());
		tmp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return tmp;
	}
	/**
	 * Variant of {@link #addTitledBorder(JComponent, String)}.
	 * 
	 * @param c JComponent to be added.
	 * @param title Title to be displayed.
	 * @return A JPanel with a TitledBorder and <var>c</var> added to it.
	 */
	public static JPanel makeTitledPanel(JComponent c, String title) {
		JPanel tmp = new JPanel();//new BorderLayout());
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
	public static JPanel makeTitledPanel(JComponent[] c, String title) {
		JPanel tmp = new JPanel();
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
	public static JPanel makeTitledPanel(String title, LayoutManager m) {
		JPanel tmp = new JPanel(m);//new BorderLayout());
		tmp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return tmp;
	}
	public static JPanel makeTitledPanel(JComponent c, String title, LayoutManager m) {
		JPanel tmp = new JPanel(m);//new BorderLayout());
		tmp.add(c);
		tmp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return tmp;
	}
	public static JPanel makeTitledPanel(JComponent[] c, String title, LayoutManager m) {
		JPanel tmp = new JPanel(m);//new BorderLayout());
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
	public static JPanel makeTitledPanel(JComponent[] c, String title, int axis) {
		JPanel tmp = new JPanel();
		BoxLayout m = new BoxLayout(tmp, axis);
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
	public static JPanel makeBoxLayoutPanelX(JComponent[] c) {
		JPanel tmp = makeBoxLayoutPanelX();
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
	public static JPanel makeBoxLayoutPanelY(JComponent[] c) {
		JPanel tmp = makeBoxLayoutPanelY();
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
	
	public static JPanel makeBoxLayoutPanel(int axis) {
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
	public static JPanel makeBoxLayoutPanelY(JPanel panel) {
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
	public static JPanel makeBoxLayoutPanelX(JPanel panel) {
		return makeBoxLayoutPanel(panel, X_AXIS);
	}
	
	public static JPanel makeBoxLayoutPanel(JPanel panel, int axis) {
		if (panel == null) {
			panel = new JPanel();
		}
		BoxLayout m = new BoxLayout(panel, axis);
		panel.setLayout(m);
		return panel;
	}
	/**
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @return JMenuItem with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 */
	public static JMenuItem makeJMenuItem(String text, String command) {
		JMenuItem tmp = new JMenuItem(text);
		tmp.setActionCommand(command);
		return tmp;
	}
	/**
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @return JMenuItem with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 */
	public static JMenu makeJMenu(String text, String command) {
		JMenu tmp = new JMenu(text);
		tmp.setActionCommand(command);
		return tmp;
	}
	/**
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @param al ActionListener to set for the returned JMenuItem
	 * @return JMenuItem with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 */
	public static JMenuItem makeJMenuItem(String text, String command, ActionListener al) {
		JMenuItem tmp = new JMenuItem(text);
		tmp.setActionCommand(command);
		tmp.addActionListener(al);
		return tmp;
	}
	/**
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @param al ActionListener to set for the returned JMenu
	 * @return JMenuItem with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 */
	public static JMenu makeJMenu(String text, String command, ActionListener al) {
		JMenu tmp = new JMenu(text);
		tmp.setActionCommand(command);
		tmp.addActionListener(al);
		return tmp;
	}
	//simple JPanel wrappers
	/**
	 * Useful to avoid the nasty effects of BoxLayout and GridLayout on preferred sizes.
	 * 
	 * @param c
	 * @return <var>c</var> in a JPanel.
	 */
	public static JPanel wrapInJPanel(JComponent c) {
		JPanel tmp = new JPanel();
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
	public static JPanel wrapInJPanel(JComponent c, LayoutManager m) {
		JPanel tmp = new JPanel(m);
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
	public static JPanel wrapInJPanel(JComponent c[], GridLayout m) {
		JPanel tmp = new JPanel(m);
		for (int i = 0;i<c.length;i++) {
			tmp.add(c[i]);
		}
		return tmp;
	}
	/**
	 * Creates a JPanel with a LayoutManager of GridLayout with the specified number of columns and rows
	 * and adds the items of c to the JPanel.
	 * @param c Array of JComponents to add to the JPanel 
	 * @param cols Number of columns
	 * @param rows Number of rows.
	 * @return A JPanel with a GridLayout.
	 */
	public static JPanel wrapInJPanel(JComponent c[], int cols, int rows) {
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
	public static JPanel wrapInJPanel(JComponent c[], LayoutManager m, Object[] args) {
		if (c==null||args==null||m==null||c.length!=args.length) {return null;}
		JPanel tmp = new JPanel(m);
		for (int i = 0;i<args.length;i++) {
			tmp.add(c[i], args[i]);
		}
		return tmp;
	}
	//JButton creation
	/**
	 * Creates a JButton with common properties already set.
	 * 
	 * @param display The text to be displayed on the button.
	 * @param actionCommand The action command to be set with JButton.setActionCommand(String)
	 * @return JButton with text <var>display</var> and actionCommand <var>actionCommand</var>
	 * @see javax.swing.AbstractButton
	 */
	public static JButton makeJButton(String display, String actionCommand) {
		JButton bTmp = new JButton(display);
		bTmp.setActionCommand(actionCommand);
		return bTmp;
	}
	/**
	 * Creates a JButton with common properties already set.
	 * 
	 * @param display The text to be displayed on the button.
	 * @param actionCommand The action command to be set with JButton.setActionCommand(String)
	 * @param al ActionListener to be added to the resulting JButton.
	 * @return JButton with text <var>display</var> and actionCommand <var>actionCommand</var>
	 * @see javax.swing.AbstractButton
	 */
	public static JButton makeJButton(String display, String actionCommand, ActionListener al) {
		JButton bTmp = new JButton(display);
		bTmp.setActionCommand(actionCommand);
		bTmp.addActionListener(al);
		return bTmp;
	}
	/**
	 * Returns a JLabel for each element in <var>vals</var>.
	 * @param vals Text to be displayed.
	 * @return An array of JLabels with text from <var>vals</var>.<br>
	 * 		null if vals==null
	 */
	public static JLabel[] makeJLabels(String[] vals) {
		if (vals==null) {return null;}
		JLabel[] lTmp = new JLabel[vals.length];
		for (int i = 0;i<vals.length;i++) {
			lTmp[i] = new JLabel(vals[i]);
		}
		return lTmp;
	}
	/**
	 * Retrieves the set action command from an EventObject if it is an AbstractButton or subclass.
	 * 
	 * @param e EventObject trigger.
	 * @return if e.getSource() instanceof AbstractButton then AbstractButton.getActionCommand()<br>
	 * 		null otherwise.
	 * @see javax.swing.AbstractButton
	 */
	public static String getActionCommand(EventObject e) {
		return ((e.getSource() instanceof AbstractButton)?((AbstractButton)e.getSource()).getActionCommand():null);
	}
	//Jframes
	/**
	 * Creates a default JFrame of size 300x300 that exits when closed.
	 * 
	 * @return An empty JFrame of size 300x300 that exits when closed.
	 */
	public static JFrame makeDefaultJFrame() {
		JFrame fTmp = new JFrame();
		fTmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fTmp.setSize(300,300);
		return fTmp;
	}
	/**
	 * Calls {@link #makeDefaultJFrame()} and sets the title.
	 * 
	 * @param title Title for the frame.
	 * @return An empty JFrame with a set title that exits when closed.
	 */
	public static JFrame makeDefaultJFrame(String title) {
		JFrame fTmp = makeDefaultJFrame();
		fTmp.setTitle(title);
		return fTmp;
	}
	/**
	 * Sets up a frame and displays it.
	 * 
	 * Calls the following methods on <var>frame</var>:<br>
	 * <code>
	 * frame.pack();<br>
	 * frame.setLocationRelativeTo(null);<br>
	 * frame.setVisible(true);<br>
	 * </code>
	 * @param frame JFrame to be setup and shown.
	 */
	public static void showFrame(JFrame frame) {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public static JFrame makeJFrameAndShow(String title, JPanel content) {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content.setOpaque(true);
		frame.setContentPane(content);
		showFrame(frame);
		return frame;
	}
//------------------------------------------------------------------------------------------------
	private static JFileChooser mDirChoose   = null;    
    private static JFileChooser mFileChoose  = null;
    
    private final static HashMap<String, ImageIcon> ICON_CACHE = new HashMap<String, ImageIcon>();
    
    /**
     * Creates an image icon. Sucessful image loads are cached in a HaspMap.
     * <br>author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     * @param imgResource Resource for the wanted image
     * @return An ImageIcon of the image or null if it was not found.
     */
    public static ImageIcon createImageIcon(String imgResource) {
        
        synchronized(ICON_CACHE) {
            
            // already cached
            ImageIcon icon = (ImageIcon)ICON_CACHE.get(imgResource);
            if(icon != null) {
                return icon;
            }
            
            // load image
            URL imgUrl = do_gui.class.getClassLoader().getResource(imgResource);
            if(imgUrl != null) {
                try {
                    icon = new ImageIcon(imgUrl);
                }
                catch(Exception ex) {
                }
            }
            
            // cache the loaded image
            if(icon != null) {
                ICON_CACHE.put(imgResource, icon);
            }
            
            return icon;
        }
    }


    /**
     * Create splash window. Returns null if image not found.
     * <br>author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     * @param imgResource Resource of the image to be loaded.
     * @return A JWindow containin the image.
     */
    public static JWindow createSplashWindow(String imgResource) {

        ImageIcon icon = createImageIcon(imgResource);
        if (icon == null) {
            return null;
        }

        JLabel lab = new JLabel();
        lab.setIcon(icon);

        Dimension iDim = new Dimension(icon.getIconWidth(), icon.getIconHeight());

        JWindow splashWin = new JWindow();
        splashWin.getContentPane().add(lab);
        splashWin.setSize(iDim);
        setLocation(splashWin);
        return splashWin;
    }
    /**
     * Get top frame. May return null, if parent frame not found.
     * <br>author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     * @param comp Component to start with.
     * @return Component that has no parent.
     */    
    public static Component getTopFrame(Component comp) {
        Component frameComp = comp;
        while( (frameComp != null) && !(frameComp instanceof java.awt.Frame) ) {
            frameComp = frameComp.getParent();
        }        
        return frameComp;
    }
    /**
     * Predefined error message box.
     * @param parent Component to center on. Can be null.
     * @param str Message to display.
     */
    public static void showErrorMessage(Component parent, String str) {
        JOptionPane.showMessageDialog(parent, str, "Error!",
                                      JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Predefined error message box.
     * @param parent Component to center on. Can be null.
     * @param obj Message to display.
     */
    public static void showErrorMessage(Component parent, Object obj) {
        JOptionPane.showMessageDialog(parent, obj, "Error!",
                                      JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Predefined error message box that shows the error stack trace as the message.
     * @param parent Component to center on. Can be null.
     * @param th Error to show stack trace for.
     */
    public static void showErrorMessage(Component parent, Throwable th) {
        String errStr = Utils.getStackTrace(th);
        showErrorMessage(parent, errStr);
    }
    /**
     * Predefined warning message box.
     * @param parent Component to center on. Can be null.
     * @param str Message to display.
     */
    public static void showWarningMessage(Component parent, String str) {
        JOptionPane.showMessageDialog(parent, str, "Warning!",
                                      JOptionPane.WARNING_MESSAGE);
    }
    /**
     * Predefined warning message box.
     * @param parent Component to center on. Can be null.
     * @param obj Message to display.
     */
    public static void showWarningMessage(Component parent, Object obj) {
        JOptionPane.showMessageDialog(parent, obj, "Warning!",
                                      JOptionPane.WARNING_MESSAGE);
    }
    /**
     * Predefined warning message box that shows the stack trace as the message.
     * @param parent Component to center on. Can be null.
     * @param th Error to show the stack trace for.
     */
    public static void showWarningMessage(Component parent, Throwable th) {
        String errStr = Utils.getStackTrace(th);
        showWarningMessage(parent, errStr);
    }
    /**
     * Predefined information message box.
     * @param parent Component to center on. Can be null.
     * @param str Message to display.
     */
    public static void showInformationMessage(Component parent, String str) {
        JOptionPane.showMessageDialog(parent, str, "Information!",
                                      JOptionPane.INFORMATION_MESSAGE );
    }
    /**
     * Predefined information message box.
     * @param parent Component to center on. Can be null.
     * @param obj Message to display.
     */
    public static void showInformationMessage(Component parent, Object obj) {
        JOptionPane.showMessageDialog(parent, obj, "Information!",
                                      JOptionPane.INFORMATION_MESSAGE );
    }
    /**
     * Predefined confirmation message box.
     * @param parent Component to center on. Can be null.
     * @param str Message to display.
     * @return True is yes, false if no or closed with no selection.
     */
    public static boolean getConfirmation(Component parent, String str) {
        int res = JOptionPane.showConfirmDialog(parent, 
                                                str,
                                                "Confirmation",
                                                JOptionPane.YES_NO_OPTION, 
                                                JOptionPane.QUESTION_MESSAGE 
                                               );
        return(res == JOptionPane.YES_OPTION); 
    }
    /**
     * Opens a JFileChooser and returns the result.
     * <br>author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     * @param parent Component to center on.
     * @return String of the file's path or null if no file was selected.
     */
    public static String getFileName(Component parent) {

        if (mFileChoose == null) {
            mFileChoose = new JFileChooser();
            mFileChoose.setFileSelectionMode(JFileChooser.FILES_ONLY);
            mFileChoose.setCurrentDirectory(new File(System.getProperty("user.dir")));
        }
        mFileChoose.setMultiSelectionEnabled(false);
        int returnVal = mFileChoose.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return mFileChoose.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }
    /**
     * @param parent Component to center on.
     * @param filter Filter to use for filtering displayed files.
     * @return Resulting file or null if none were selected or box was closed by means other than the Open button.
     */
    public static File getFileName(Component parent, FileFilter filter) {

        if (mFileChoose == null) {
            mFileChoose = new JFileChooser();
            mFileChoose.setFileSelectionMode(JFileChooser.FILES_ONLY);
            mFileChoose.setCurrentDirectory(new File(System.getProperty("user.dir", ".")));
        }
        mFileChoose.setMultiSelectionEnabled(false);
        mFileChoose.setFileFilter(filter);
        int returnVal = mFileChoose.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return mFileChoose.getSelectedFile();
        } else {
            return null;
        }
    }
    /**
     * Opens a JFileChooser and returns the result.<br>
     * Adapted from code by <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     * @param parent Component to center on.
     * @return String of the file's path or null if no file was selected.
     */
    public static File[] getFileNames(Component parent) {

        if (mFileChoose == null) {
            mFileChoose = new JFileChooser();
            mFileChoose.setFileSelectionMode(JFileChooser.FILES_ONLY);
            mFileChoose.setCurrentDirectory(new File(System.getProperty("user.dir", ".")));
        }
        mFileChoose.setMultiSelectionEnabled(true);
        int returnVal = mFileChoose.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return mFileChoose.getSelectedFiles();
        } else {
            return null;
        }
    }
    /**
     * Opens a JFileChooser and returns the result.<br>
     * Adapted from code by <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     * @param parent Component to center on.
     * @return String of the directory path or null if nothing was selected or box closed abnormally.
     */  
    public static File getDirName(Component parent) {

        if (mDirChoose == null) {
            mDirChoose = new JFileChooser();
            mDirChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            mDirChoose.setCurrentDirectory(new File(System.getProperty("user.dir",".")));
        }
        mDirChoose.setMultiSelectionEnabled(false);
        int returnVal = mDirChoose.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return mDirChoose.getSelectedFile();
        } else {
            return null;
        }
    }
    /**
     * Opens a JFileChooser and returns the result.
     * <br>author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     * @param parent Component to center on.
     * @return String of the directory path or null if nothing was selected.
     */  
    public static File[] getDirNames(Component parent) {

        if (mDirChoose == null) {
            mDirChoose = new JFileChooser();
            mDirChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            mDirChoose.setCurrentDirectory(new File(System.getProperty("user.dir",".")));
        }
        mDirChoose.setMultiSelectionEnabled(true);
        int returnVal = mDirChoose.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return mDirChoose.getSelectedFiles();
        } else {
            return null;
        }
    }
    /**
     * Update component look & feel.<br>
     * Updates the look and feel of the JFileChoosers only.
     * <br>author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     */
    public static void updateLnF() {

        if (mDirChoose != null) {
            SwingUtilities.updateComponentTreeUI(mDirChoose);
        }
        if (mFileChoose != null) {
            SwingUtilities.updateComponentTreeUI(mFileChoose);
        }
    }
    /**
     * Position properly - center.
     * <br>author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     * @param comp Component to center on the screen.
     */
    public static void setLocation(Component comp) {
        Dimension cDim = comp.getSize();
        Dimension wDim = Toolkit.getDefaultToolkit().getScreenSize();
        comp.setLocation((wDim.width - cDim.width)/2, (wDim.height - cDim.height)/2);
    }
    /**
     * Position with respect to the parent component.
     * <br>author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     * @param comp Component to be centered
     * @param parent Component to center on.
     */
    public static void setLocation(Component comp, Component parent)  {
        Dimension cDim = comp.getSize();
        Rectangle pRect = parent.getBounds();
        int x = pRect.x + (pRect.width - cDim.width)/2;
        int y = pRect.y + (pRect.height - cDim.height)/2;
        comp.setLocation(x, y);
    }
    /**
     * Display a new panel. First removes all children, then add.
     * <br>author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
     * @param parent Container to clear
     * @param child Component to add when parent is cleared
     */
    public static void showNewPanel(Container parent, Component child) {
        parent.removeAll();
        parent.add(child);
        parent.validate();
        parent.repaint();
    }
    /**
     * Size 12 Courier New font.
     */
    public static final Font MONOSPACE = new Font("Courier New", Font.PLAIN, 12);
    /**
     * Creates a plain font.
     * @param name Name of the font wanted.
     * @param size Point size of the font.
     * @return A new java.awt.Font that represents a font with
     * 			point size <var>size</var> that has the name <var>name</var>.
     */
    public static Font makeFont(String name, int size) {
    	return new Font(name, Font.PLAIN, size);
    }
    /**
     * Returns {@link #MONOSPACE}.
     * @return {@link #MONOSPACE}
     */
    public static Font getMonoFont() {
    	return MONOSPACE;
    }
    /**
     * Creates a font of point size <var>size</var> derived from {@link #MONOSPACE}.
     * @param size Point size the font should be.
     * @return <code>{@link #MONOSPACE}.deriveFont(<var>size</var>)</code>
     */
    public static Font getMonoFont(int size) {
    	return MONOSPACE.deriveFont(size);
    }
}