package simple.forms.old;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Font;

/**
 * Creates a form object that validates E-mail addresses.
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @deprecated
 */
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
	public String getValue() {
		return p1.getText().trim()+"@"+p2.getText().trim()+"."+p3.getText().trim();
	}
	public void setValue(String v) {
		p1.setText(v.substring(0,v.indexOf("@")));
		p2.setText(v.substring(v.indexOf("@")+1,v.indexOf(".")));
		p3.setText(v.substring(v.indexOf(".")+1,v.length()));
	}
	public boolean isValid() {
		if ((p1.getText().trim().length()>0)&&(p2.getText().trim().length()>0)&&(p3.getText().trim().length()>0)) {return true;}
		return false;
	}
	public void reset() {
		p1.setText("");
		p2.setText("");
		p3.setText("");
	}
	public String getName() {
		return "Email";
	}
	public JPanel getJPanel() {
		return (JPanel)this;
	}
}