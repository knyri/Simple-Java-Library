/**
 *
 */
package simple.gui.factory;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.EventObject;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

/**Common Swing creation functions. (No dependents)
 * <br>Created: Mar 6, 2009
 * @author Kenneth Pierce
 */
public final class SwingFactory {
	protected SwingFactory() {}
	private static JFileChooser mDirChoose   = null;
    private static JFileChooser mFileChoose  = null;
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
     * Update component look and feel.<br>
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
	 * Calls {@link #makeDefaultJFrame(String, int, int)} with an empty
	 * title and a width and height of 300.
	 *
	 * @return An empty JFrame of size 300x300 that exits when closed.
	 */
	public static JFrame makeDefaultJFrame() {
		return makeDefaultJFrame("", 300, 300);
	}
	/**
	 * Calls {@link #makeDefaultJFrame(String, int, int)} with the specified
	 * title and a width and height of 300.
	 *
	 * @param title Title for the frame.
	 * @return An empty JFrame with a set title that exits when closed.
	 */
	public static JFrame makeDefaultJFrame(String title) {
		return makeDefaultJFrame(title, 300, 300);
	}
	/**
	 * Creates a JFrame that exits when closed with the
	 * specified title, width, and height.
	 *
	 * @param title Title for the frame.
	 * @param width Width of the frame.
	 * @param height Height of the frame.
	 * @return An empty JFrame with a set title that exits when closed.
	 */
	public static JFrame makeDefaultJFrame(String title, int width, int height) {
		JFrame fTmp = new JFrame();
		fTmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fTmp.setSize(width, height);
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
	/**Creates, sets the content pane, centers, and shows the frame.
	 * @param title Title for the frame.
	 * @param content The content pane.
	 * @return The created frame.
	 */
	public static JFrame makeJFrameAndShow(String title, Container content) {
		JFrame frame = makeDefaultJFrame(title);
		if (content instanceof JComponent)
			((JComponent)content).setOpaque(true);
		frame.setContentPane(content);
		showFrame(frame);
		return frame;
	}
	/**Creates, sets the content pane, centers, and shows the frame.
	 * @param title Title for the frame.
	 * @param width Width for the frame.
	 * @param height Height for the frame.
	 * @param content The content pane.
	 * @return The created frame.
	 */
	public static JFrame makeJFrameAndShow(String title, int width, int height, Container content) {
		JFrame frame = makeDefaultJFrame(title, width, height);
		if (content instanceof JComponent)
			((JComponent)content).setOpaque(true);
		frame.setContentPane(content);
		showFrame(frame);
		return frame;
	}
	/**Creates a JMenuItem with the supplied text and action command.
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @return JMenuItem with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 * @see javax.swing.JMenuItem
	 */
	public static JMenuItem makeJMenuItem(String text, String command) {
		JMenuItem tmp = new JMenuItem(text);
		tmp.setActionCommand(command);
		return tmp;
	}
	/**Creates a JMenu with the supplied text and action command.
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @return JMenu with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 * @see javax.swing.JMenu
	 */
	public static JMenu makeJMenu(String text, String command) {
		JMenu tmp = new JMenu(text);
		tmp.setActionCommand(command);
		return tmp;
	}
	/**Creates a JMenuItem with the supplied text and action command and sets the ActionListener.
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
	public static JMenuItem makeJMenuItem(String text, ActionListener al) {
		JMenuItem tmp = new JMenuItem(text);
		tmp.addActionListener(al);
		return tmp;
	}
	public static JMenuItem makeJMenuItem(String text, String command, ImageIcon icon, ActionListener al) {
		JMenuItem tmp = new JMenuItem(text,icon);
		tmp.setActionCommand(command);
		tmp.addActionListener(al);
		return tmp;
	}
	public static JMenuItem makeJMenuItem(String text, ImageIcon icon, ActionListener al) {
		JMenuItem tmp = new JMenuItem(text,icon);
		tmp.addActionListener(al);
		return tmp;
	}

	/**Creates a JMenu with the supplied text and action command and sets the ActionListener.
	 * @param text Displayed text.
	 * @param command Text to set as its ActionCommand
	 * @param al ActionListener to set for the returned JMenu
	 * @return JMenu with displayed text and set action command
	 * @see javax.swing.AbstractButton
	 */
	public static JMenu makeJMenu(String text, String command, ActionListener al) {
		JMenu tmp = new JMenu(text);
		tmp.setActionCommand(command);
		tmp.addActionListener(al);
		return tmp;
	}
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
	 * Creates a JButton with common properties already set.
	 *
	 * @param display The text to be displayed on the button.
	 * @param actionCommand The action command to be set with JButton.setActionCommand(String)
	 * @param icon icon to show
	 * @param al ActionListener to be added to the resulting JButton.
	 * @return JButton with text <var>display</var> and actionCommand <var>actionCommand</var>
	 * @see javax.swing.AbstractButton
	 */
	public static JButton makeJButton(String display, String actionCommand,Icon icon, ActionListener al) {
		JButton bTmp = new JButton(display,icon);
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
	/**Added for chaining calls on anonymous JComponents.
	 * @param c The component to get the border
	 * @param b The border
	 * @return The passed component
	 */
	public static JComponent addBorder(JComponent c,Border b){
		c.setBorder(b);
		return c;
	}
	/**For chaining calls on anonymous Containers
	 * @param c component to add
	 * @param to container to add to
	 * @return the container
	 */
	public static JComponent add(Component c,JComponent to){
		to.add(c);
		return to;
	}
	/**For chaining calls on anonymous Containers
	 * @param c component to add
	 * @param idx index to add at
	 * @param to container to add to
	 * @return the container
	 */
	public static JComponent add(Component c, int idx,JComponent to){
		to.add(c,idx);
		return to;
	}
	/**For chaining calls on anonymous Containers
	 * @param c component to add
	 * @param con constraints
	 * @param to container to add to
	 * @return the container
	 */
	public static JComponent add(Component c,Object con,JComponent to){
		to.add(c,con);
		return to;
	}
	/**For chaining calls on anonymous Containers
	 * @param c component to add
	 * @param con constraints
	 * @param idx index
	 * @param to container to add to
	 * @return the container
	 */
	public static JComponent add(Component c,Object con,int idx,JComponent to){
		to.add(c,con,idx);
		return to;
	}
	/**For Chaining calls on anonymous Containers
	 * @param name name of the component
	 * @param c the component to add
	 * @param to the container to add to
	 * @return the container
	 */
	public static JComponent add(String name,Component c,JComponent to){
		to.add(name,c);
		return to;
	}
}
