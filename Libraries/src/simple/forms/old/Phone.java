package simple.forms.old;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Creates a form object that accepts phone numbers.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @deprecated
 */
@Deprecated
public class Phone extends JPanel implements FormObject {

	private static final long serialVersionUID = 1L;
	private JTextField p1 = new JTextField(5);
	private JTextField p2 = new JTextField(5);
	private JTextField p3 = new JTextField(6);

	public Phone() {
		Font f = new Font("Courier",Font.PLAIN, 12);
		p1.setFont(f);
		p2.setFont(f);
		p3.setFont(f);
		JPanel temp = new JPanel();
		temp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),"Phone Number"));
		temp.add(new JLabel("("));
		temp.add(p1);
		temp.add(new JLabel(")"));
		temp.add(p2);
		temp.add(new JLabel("-"));
		temp.add(p3);
		add(temp);
	}
	@Override
	public String getValue() {
		return p1.getText().trim()+p2.getText().trim()+p3.getText().trim();
	}
	@Override
	public void setValue(String v) {
		p1.setText(v.substring(0,3));
		p2.setText(v.substring(3,7));
		p1.setText(v.substring(7,v.length()));
	}
	@Override
	public boolean isValid() {
		if (getValue().length()>9) {return true;}
		return false;
	}
	@Override
	public void reset() {
		p1.setText("");
		p2.setText("");
		p3.setText("");
	}
	/**
	 * @return The name of this element: "Phone Number"
	 */
	@Override
	public String getName() {
		return "Phone Number";
	}
	@Override
	public JPanel getJPanel() {
		return this;
	}
}