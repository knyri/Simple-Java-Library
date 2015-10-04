/**
 *
 */
package simple.gui.factory;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import simple.util.Utils;

/**Depends on simple.util.Utils
 * Tames JOptionPane by predefining commonly used values.
 * <br>Created: Mar 6, 2009
 * @author Kenneth Pierce
 */
public final class SJOptionPane {
	private static JFileChooser mDirChoose   = null;
    private static JFileChooser mFileChoose  = null;
	/**
	 * These are the message types and determines what icon will be displayed.
	 * <ul>
	 * <li>mINF = Information Message</li>
	 * <li>mGEN = General(Plain) Message</li>
	 * <li>mASK = Question Message.</li>
	 * <li>mWARN = Warning Message</li>
	 * <li>mERR = Error Message</li>
	 * </ul>
	 * Values taken from {@link javax.swing.JOptionPane}.
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
}
