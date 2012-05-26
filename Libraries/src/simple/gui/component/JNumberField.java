package simple.gui.component;

import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * A JTextField that can only accept numbers.
 * <br>Created: 2005
 * @author Kenneth Pierce
 */
public final class JNumberField extends JTextField implements FocusListener{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Determines if the contents are selected when
	 * focus is gained. Off by default.
	 */
	private boolean focusSelect;
	/**
	 * Cache for the value; 
	 */
	private double value = 0;
	/**
	 * Full constructor.
	 * @param v Initial value
	 * @param col Number of columns
	 * @param SoF Select on Focus
	 */
	public JNumberField(String v, int col, boolean SoF) {
		super(v, col);
		setSelectOnFocus(SoF);
		addFocusListener(this);
	}
	/**
	 * Default constructor. Calls this("0", 7, false).
	 */
	public JNumberField() {this("0", 7, false);}
	/**
	 * Calls this(v, v.length(), false).
	 * @param v Initial value
	 */
	public JNumberField(String v) {this(v, v.length(), false);}
	/**
	 * Calls this(v, col, false).
	 * @param v Initial value
	 * @param col Number of columns
	 */
	public JNumberField(String v, int col) {this(v, col, false);}
	/**
	 * Calls this("0", col, false).
	 * @param col Number of columns
	 */
	public JNumberField(int col) {this("0", col, false);}
	/**
	 * Calls this("0", 7, SoF).
	 * @param SoF Select on Focus
	 */
	public JNumberField(boolean SoF) {this("0", 7, SoF);}
	/**
	 * Calls this("0", col, SoF).
	 * @param col The length of the field
	 * @param SoF Select on Focus
	 */
	public JNumberField(int col, boolean SoF) {this("0", col, SoF);}
	/**
	 * Sets whether the contents are to be selected<br>
	 * when focus is gained.
	 * 
	 * @param b Value to be set.
	 */
	public void setSelectOnFocus(boolean b) {
		focusSelect = b;
	}
	public boolean isSelectOnFocus() {
		return focusSelect;
	}
	public void focusGained(FocusEvent e) {
		if (focusSelect) {
			super.selectAll();
		}
	}
	public double getValue() {
		if (!super.getText().equals("")) {
			if (((NumberDocument)this.getDocument()).change) {
				value = Double.parseDouble(super.getText()); 
			}
			return value;
		} else
			return 0;
	}
	public int getValueAsInt() {
		if (!super.getText().equals("")) {
			if (((NumberDocument)this.getDocument()).change) {
				value = Double.parseDouble(super.getText()); 
			}
			return (int)value;
		} else
			return 0;
	}
	public void setValue(double value) {
		setText(""+value);
	}
	public void focusLost(FocusEvent e) {}
	
	protected Document createDefaultModel() {
		return new NumberDocument();
	}
	static class NumberDocument extends PlainDocument {
		
		private static final long serialVersionUID = 1L;
		protected boolean change = false;
		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
				String text = getText(0, this.getLength());
				if (text.equals(str)) {
					super.insertString(offset, str.trim(), a);
					return;
				}
//				This throws an error if str is not a valid number
				if (str.contains(".") && text.contains("."))
					return;
				if (str.indexOf('.') != str.lastIndexOf('.'))
					return;
				if (str.contains("-") && text.contains("-"))
					return;
				if (str.contains("+") && text.contains("+"))
					return;
			try {
				//Integer.parseInt(str.trim());
				Double.parseDouble(text+str.trim());
			} catch (NumberFormatException nfe) {return;}
//				It passes the test so add it to the content
			change = true;
			super.insertString(offset, str.trim(), a);
		}
	}
}