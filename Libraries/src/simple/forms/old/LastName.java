package simple.forms.old;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Creates a form object that accepts last names.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @deprecated
 */
@Deprecated
public class LastName extends JPanel implements FormObject {

	private static final long serialVersionUID = 1L;
	private JTextField data = new JTextField(10);
	public LastName() {
		JPanel temp = new JPanel();
		temp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),"Last Name"));
		temp.add(data);
		add(temp);
	}
	@Override
	public String getValue() {
		return data.getText().trim();
	}
	@Override
	public void setValue(String v) {
		data.setText(v);
	}
	@Override
	public boolean isValid() {
		if (getValue().length()>0) {return true;}
		return false;
	}
	@Override
	public void reset() {
		data.setText("");
	}
	@Override
	public String getName() {
		return "Last Name";
	}
	@Override
	public JPanel getJPanel() {
		return this;
	}
}