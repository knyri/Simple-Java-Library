package simple.gui.container;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.ItemSelectable;
import java.util.Vector;

/**
 * Simple spinoff of ButtonGroup.<br>
 * Manages JRadioButtons allowing only one to be selected at a time.
 * <br>Created: 2005
 * @author Kenneth Pierce
 * @version 1.0
 * @see javax.swing.ButtonGroup
 */
public class RadioButtonGroup implements ActionListener, ItemSelectable {
//simpler ButtonGroup spinoff
	Vector<JRadioButton> group = new Vector<JRadioButton>();
	Vector<ItemListener> ils = new Vector<ItemListener>();
	ItemEvent ieD = new ItemEvent(this, ItemEvent.ITEM_FIRST, null, ItemEvent.DESELECTED);
	ItemEvent ieS = new ItemEvent(this, ItemEvent.ITEM_FIRST, null, ItemEvent.SELECTED);
	JRadioButton selected;
	public RadioButtonGroup() {}
	/**
	 * Adds <var>rb</var> to the group and checks to see if it is
	 * set as selected. If it is then it is set as the currently selected
	 * radio button.
	 * @param rb
	 */
	public void add(JRadioButton rb) {
		group.add(rb);
		if (rb.isSelected()) {setSelected(rb);}
		rb.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e) {
		setSelected((JRadioButton)e.getSource());
	}
	public void addItemListener(ItemListener il) {
		ils.add(il);
	}
	/**
	 * @return The currently selected radio button.
	 */
	public JRadioButton getSelected() {return selected;}
	/**
	 * Sets the radiobutton at index <var>i</var> as selected. Does nothing if i &lt; 0 or &gt;
	 * the number of radiobuttons. 
	 * 
	 * @param i Index of JRadioButton to be selected.
	 */
	public void setSelected(int i) {
		if (i>group.size()) {
			setSelected(group.get(group.size()));
		} else if (i>-1) {
			setSelected(group.get(i));
		} else {
			setSelected(0);
		}
	}
	/**
	 * Sets <var>rb</var> as the currently selected radio button.
	 * 
	 * @param rb
	 */
	public void setSelected(JRadioButton rb) {
		if (selected!=null) {
			selected.setSelected(false);
			ieD = new ItemEvent(this, ieD.getID()+1, selected, ItemEvent.DESELECTED);
		}
		selected = rb;
		rb.setSelected(true);
		ieS = new ItemEvent(this, ieD.getID()+1, selected, ItemEvent.SELECTED);
		notifyItemListeners(ieD);
		notifyItemListeners(ieS);
	}
	private void notifyItemListeners(ItemEvent ie) {
		for (int i = 0; i < ils.size(); i++) {
			ils.get(i).itemStateChanged(ie);
		}
	}
	public Object[] getSelectedObjects() {
		return new Object[] {selected};
	}
	public void removeItemListener(ItemListener arg0) {
		ils.remove(arg0);
	}
}