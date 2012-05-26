package simple.forms.old;

import javax.swing.JPanel;

/**
 * Interface for making form objects for simple.forms.old.Form.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @deprecated
 */
public interface FormObject {
	/**
	 * @return Name of the object.
	 */
	public String getName();
	/**
	 * @return String representation of the value.
	 */
	public String getValue();
	public void setValue(String value);
	/**
	 * @return True if the value is valid.
	 */
	public boolean isValid();
	/**
	 * Reset the value to it's default.
	 */
	public void reset();
	/**
	 * @return Yourself in a JPanel.
	 */
	public JPanel getJPanel();
}