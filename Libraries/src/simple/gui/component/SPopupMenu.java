/**
 *
 */
package simple.gui.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 * <hr>
 * <br>Created: May 21, 2011
 * @author Kenneth Pierce
 */
public final class SPopupMenu extends JPopupMenu implements ActionListener{
	private static final long serialVersionUID = -2981738666417782557L;
	public static final int COPY=1,PASTE=2,CUT=4;
	private Component target;

	public SPopupMenu() {super();}
	/**
	 * @param label
	 */
	public SPopupMenu(final String label) {
		super(label);
	}
	public static void createListener(final JPopupMenu menu){

	}
	public static final Action createCopyAction(final JTextComponent c){
		return new simple.gui.AbstractAction(){
			public void actionPerformed(final ActionEvent e) {
				c.copy();
			}};
	}
	public static final Action createCutAction(final JTextComponent c){
		return new simple.gui.AbstractAction(){
			public void actionPerformed(final ActionEvent e) {
				c.cut();
			}};
	}
	public static final Action createPasteAction(final JTextComponent c){
		return new simple.gui.AbstractAction(){
			public void actionPerformed(final ActionEvent e) {
				c.paste();
			}};
	}
	public void addOption(final int option){
		switch(option){
		case COPY:
			final Action c = new simple.gui.AbstractAction(){
				public void actionPerformed(final ActionEvent e) {
					final SPopupMenu menu=(SPopupMenu)e.getSource();
					if (menu.target instanceof JTextComponent){
						((JTextComponent)menu.target).paste();
					}
				}};
				c.putValue(Action.NAME, "Copy");
				add(c);
				break;
		case PASTE:
			final Action p = new simple.gui.AbstractAction(){
				public void actionPerformed(final ActionEvent e) {
					final SPopupMenu menu=(SPopupMenu)e.getSource();
					if (menu.target instanceof JTextComponent){
						((JTextComponent)menu.target).paste();
					}
				}};
				p.putValue(Action.NAME, "Paste");
				add(p);
				break;
		case CUT:
			final Action u = new simple.gui.AbstractAction(){
				public void actionPerformed(final ActionEvent e) {
					final SPopupMenu menu=(SPopupMenu)e.getSource();
					if (menu.target instanceof JTextComponent){
						((JTextComponent)menu.target).cut();
					}
				}};
				u.putValue(Action.NAME, "Cut");
				add(u);
				break;
		}
	}
	public void addItem(final String name,final ActionListener al){
		final Action a=new simple.gui.AbstractAction(){
			public void actionPerformed(final ActionEvent e){al.actionPerformed(e);}
		};
		a.putValue(Action.NAME, name);
		add(a);
	}
	public void actionPerformed(final ActionEvent ae) {
		if (ae.getSource()!=null && ae.getSource() instanceof Component)
			this.target=(Component)ae.getSource();
	}
}
