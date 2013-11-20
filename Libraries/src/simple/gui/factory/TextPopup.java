package simple.gui.factory;

import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import simple.gui.AbstractAction;
import simple.gui.SPopUp;

/**
 *  Singleton Pop Up for JTextComponents that has the basic
 *  copy, cut, paste, and select all commands.
 * @author Ken
 */
public final class TextPopup extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	static final TextPopup ref=new TextPopup();
	public static TextPopup getMenu(){return ref;}
	private final SPopUp displayer = new SPopUp(this);
	public MouseListener getListener(){return displayer;}
	protected TextPopup(){
		Action c = new AbstractAction(){
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (displayer.getTarget() instanceof JTextComponent){
					((JTextComponent)displayer.getTarget()).copy();
				}
			}};
		c.putValue(Action.NAME, "Copy");
		add(c);
		c = new AbstractAction(){
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (displayer.getTarget() instanceof JTextComponent){
					((JTextComponent)displayer.getTarget()).paste();
				}
			}};
		c.putValue(Action.NAME, "Paste");
		add(c);
		c = new AbstractAction(){
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (displayer.getTarget() instanceof JTextComponent){
					((JTextComponent)displayer.getTarget()).cut();
				}
			}};
		c.putValue(Action.NAME, "Cut");
		add(c);
		c = new AbstractAction(){
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (displayer.getTarget() instanceof JTextComponent){
					((JTextComponent)displayer.getTarget()).selectAll();
				}
			}};
		c.putValue(Action.NAME, "Select All");
		add(c);
	}
}
