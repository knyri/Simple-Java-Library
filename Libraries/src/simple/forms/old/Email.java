package simple.forms.old;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Creates a form object that validates E-mail addresses.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @deprecated
 */
@Deprecated
public class Email extends JPanel implements FormObject {

	private static final long serialVersionUID = 1L;
	private JTextField p1 = new JTextField(10);
	private JTextField p2 = new JTextField(10);
	private JTextField p3 = new JTextField(5);
	public Email() {
		Font f = new Font("Courier",Font.PLAIN, 12);
		p1.setFont(f);
		p2.setFont(f);
		p3.setFont(f);
		JPanel temp = new JPanel();
		temp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),"Email"));
		temp.add(p1);
		temp.add(new JLabel("@"));
		temp.add(p2);
		temp.add(new JLabel("."));
		temp.add(p3);
		add(temp);
	}
	@Override
	public String getValue() {
		return p1.getText().trim()+"@"+p2.getText().trim()+"."+p3.getText().trim();
	}
	@Override
	public void setValue(String v) {
		p1.setText(v.substring(0,v.indexOf("@")));
		p2.setText(v.substring(v.indexOf("@")+1,v.indexOf(".")));
		p3.setText(v.substring(v.indexOf(".")+1,v.length()));
	}
	@Override
	public boolean isValid() {
		if ((p1.getText().trim().length()>0)&&(p2.getText().trim().length()>0)&&(p3.getText().trim().length()>0)) {return true;}
		return false;
	}
	@Override
	public void reset() {
		p1.setText("");
		p2.setText("");
		p3.setText("");
	}
	@Override
	public String getName() {
		return "Email";
	}
	@Override
	public JPanel getJPanel() {
		return this;
	}
}