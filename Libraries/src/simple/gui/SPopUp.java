package simple.gui;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

/**
 * How to use: Create the menu. Create this with the menu. Add this to the components (via {@link Component#addMouseListener(MouseListener)} you want the menu
 * to appear for. To get the component that triggered the menu, use {@link #getTarget()}.
 * This is only for easing the showing of pop up menus and figuring out what component triggered the menu.
 * @author Ken
 *
 */
public class SPopUp extends AbstractMouseListener {

	private final JPopupMenu menu;
	private Component target;
	public SPopUp(JPopupMenu m){
		menu=m;
	}
	@Override
	public void mousePressed(MouseEvent me){if(me.isPopupTrigger()){popUp(me);}}
	@Override
	public void mouseReleased(MouseEvent me){if(me.isPopupTrigger()){popUp(me);}}
	private void popUp(MouseEvent me){
		menu.show(me.getComponent(), me.getX(), me.getY());
		target=me.getComponent();
	}
	/**Gets the component that the menu appeared for.
	 * @return The component that the menu appeared for.
	 */
	public Component getTarget(){
		return target;
	}
	/**The menu that is displayed
	 * @return The menu to be displayed
	 */
	public JPopupMenu getMenu(){
		return menu;
	}
}
