package simple.forms.old;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Creates a form object that accepts First and Last names.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @deprecated
 */
@Deprecated
public class Name extends JPanel implements FormObject {

	private static final long serialVersionUID = 1L;
	private JTextField first = new JTextField(10);
	private JTextField last = new JTextField(10);
	public Name() {
		JPanel temp = new JPanel();
		temp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),"First Name"));
		temp.add(first);
		temp = new JPanel();
		temp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),"Last Name"));
		temp.add(last);
		add(temp);
	}
	@Override
	public String getValue() {
		return first.getText().trim()+" "+last.getText().trim();
	}
	@Override
	public void setValue(String v) {
		first.setText(v.split(" ")[0]);
		last.setText(v.split(" ")[1]);
	}
	@Override
	public boolean isValid() {
		if ((first.getText().trim().length()>0)&&(last.getText().trim().length()>0)) {return true;}
		return false;
	}
	@Override
	public void reset() {
		first.setText("");
		last.setText("");
	}
	@Override
	public String getName() {
		return "Name";
	}
	@Override
	public JPanel getJPanel() {
		return this;
	}
}