package simple.forms.old;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

/**
 * Form object for first names.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @deprecated
 */
public class FirstName extends JPanel implements FormObject {

	private static final long serialVersionUID = 1L;
	private JTextField data = new JTextField(10);
	public FirstName() {
		JPanel temp = new JPanel();
		temp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),"First Name"));
		temp.add(data);
		add(temp);
	}
	public String getValue() {
		return data.getText().trim();
	}
	public void setValue(String v) {
		data.setText(v);
	}
	public boolean isValid() {
		if (getValue().length()>0) {return true;}
		return false;
	}
	public void reset() {
		data.setText("");
	}
	public String getName() {
		return "First Name";
	}
	public JPanel getJPanel() {
		return (JPanel)this;
	}
}