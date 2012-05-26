package simple.forms.old;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Creates a form that can be validated, reset, and "submitted."
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @deprecated
 */
public class Form extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	FormObject[] obj;
	FormListener[] list = new FormListener[0];
	int num = 0;
	JPanel inner = new JPanel();
	/*{
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
	}*/
	/**
	 * Creates a form with 10 element slots.
	 */
	public Form() {
		this(10);
	}
	/**
	 * Creates a form with <var>size</var> element slots.
	 * @param size Number of slots needed.
	 */
	public Form(int size){
		obj = new FormObject[size];
		setLayout(new BorderLayout());
		JButton temp = new JButton("Submit");
		temp.setActionCommand("submit");
		temp.addActionListener(this);
		JPanel bottom = new JPanel();
		bottom.add(temp);
		
		temp = new JButton("Reset");
		temp.setActionCommand("reset");
		temp.addActionListener(this);
		bottom.add(temp);
		add(bottom, BorderLayout.SOUTH);
		add(inner);
	}
	protected void fireFormEvent(FormEvent e) {
		for (int i = 0;i<list.length;i++) {
			list[i].formEvent(e);
		}
	}
	public void actionPerformed(ActionEvent e) {
		if (((JButton)e.getSource()).getActionCommand().equals("submit")) {
			String problem = validateForm();
			if (problem!=null) {
				fireFormEvent(new FormEvent(this, FormEvent.ERROR, problem));
			} else {
				fireFormEvent(new FormEvent(this, FormEvent.SUBMIT, null));
			}
		} else {
			fireFormEvent(new FormEvent(this, FormEvent.RESET, null));
		}
		reset();
	}
	public void addFormListener(FormListener fl) {
		FormListener[] temp = list;
		list = new FormListener[list.length+1];
		System.arraycopy(temp,0,list,0,temp.length);
		list[list.length-1]=fl;
	}
	public void add(FormObject o) {
		obj[num++]=o;
		inner.add(o.getJPanel());
		if (num==obj.length) {
			FormObject[] temp = obj;
			obj = new FormObject[num+10];
			System.arraycopy(temp,0,obj,0,temp.length);
		}
	}
	/**
	 * Resets all elements in this form.
	 *
	 */
	public void reset() {
		for (int i = 0;i<num;i++) {
			obj[i].reset();
		}
	}
	/**
	 * Validates all objects on this form.
	 * 
	 * @return Name of form object if it's not valid, null otherwise.
	 */
	public String validateForm() {
		for (int i = 0;i<num;i++) {
			if (!obj[i].isValid()) {
				return obj[i].getName();
			}
		}
		return null;
	}
}