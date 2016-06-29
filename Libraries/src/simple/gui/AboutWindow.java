/**
 *
 */
package simple.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.LayoutManager;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;


/** Creates a dialog for outputting styled text.
 * <hr>
 * Requires {@link simple.gui.SDialog}
 * <br>Created: Mar 8, 2009
 * @author Kenneth Pierce
 */
public class AboutWindow extends SDialog {
	public AboutWindow(Frame frame, String title, boolean modal, LayoutManager topm, LayoutManager leftm,
			LayoutManager rightm, LayoutManager bottomm, LayoutManager centerm) {
		super(frame, title, modal, topm, leftm, rightm, bottomm, centerm);
		// TODO Auto-generated constructor stub
	}

	public AboutWindow(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		// TODO Auto-generated constructor stub
	}
	private static final long serialVersionUID = -4982202238513409876L;
	private final JTextPane help = new JTextPane();
	private final StyledDocument doc = help.getStyledDocument();
	private final JScrollPane HelpScroller = new JScrollPane(help);
	private final Style plain, bold, italic;
	{
		//TextFactory.addStylesToDocument(doc);
		final Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		plain = doc.addStyle("plain", def);
		StyleConstants.setFontFamily(plain, "SansSerif");
		StyleConstants.setFontSize(plain, 12);

		bold = doc.addStyle("bold", plain);
		StyleConstants.setBold(bold, true);

		italic = doc.addStyle("italic", plain);
		StyleConstants.setItalic(italic, true);
		help.setEditable(false);
		addCenter(HelpScroller);
		setButtons(OK_BUTTON);
	}
	public AboutWindow(final JFrame frame, final String title, final boolean modal) {
		super(frame, title, modal, null, null, null, new FlowLayout(FlowLayout.CENTER), new BorderLayout());
	}
	public AboutWindow(final JDialog frame, final String title, final boolean modal) {
		super(frame, title, modal, null, null, null, new FlowLayout(FlowLayout.CENTER), new BorderLayout());
	}

	/* *****************************
	 * APPEND
	 * *****************************/
	public boolean append(final String txt) {
		try {
			doc.insertString(doc.getLength(), txt, plain);
			return true;
		} catch (final BadLocationException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean appendBold(final String txt) {
		try {
			doc.insertString(doc.getLength(), txt, bold);
			return true;
		} catch (final BadLocationException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean appendItalic(final String txt) {
		try {
			doc.insertString(doc.getLength(), txt, italic);
			return true;
		} catch (final BadLocationException e) {
			e.printStackTrace();
			return false;
		}
	}
	/* *****************************
	 * APPEND LINE
	 * *****************************/
	public boolean appendLine(final String txt) {
		try {
			doc.insertString(doc.getLength(), txt+"\n", plain);
			return true;
		} catch (final BadLocationException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean appendLineBold(final String txt) {
		try {
			doc.insertString(doc.getLength(), txt+"\n", bold);
			return true;
		} catch (final BadLocationException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean appendLineItalic(final String txt) {
		try {
			doc.insertString(doc.getLength(), txt+"\n", italic);
			return true;
		} catch (final BadLocationException e) {
			e.printStackTrace();
			return false;
		}
	}
	/* *****************************
	 * INSERT
	 * *****************************/
	public void insertPlain(final String txt, final int offset) throws BadLocationException {
		doc.insertString(offset, txt, plain);
	}
	public void insertBold(final String txt, final int offset) throws BadLocationException {
		doc.insertString(offset, txt, bold);
	}
	public void insertItalic(final String txt, final int offset) throws BadLocationException {
		doc.insertString(offset, txt, italic);
	}
}
