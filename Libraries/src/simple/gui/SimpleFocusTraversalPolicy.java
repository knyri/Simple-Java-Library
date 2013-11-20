/**
 *
 */
package simple.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.Vector;

/**
 * Basic Focus Traversal Policy using a Vector.
 * @author Ken
 *
 */
public class SimpleFocusTraversalPolicy extends FocusTraversalPolicy {
	private final Vector<Component> order;
	public SimpleFocusTraversalPolicy(Vector<Component> order) {
		this.order = new Vector<Component>(order.size());
		this.order.addAll(order);
	}
	@Override
	public Component getComponentAfter(Container focusCycleRoot,Component aComponent){
		int idx = (order.indexOf(aComponent) + 1) % order.size();
		return order.get(idx);
	}
	@Override
	public Component getComponentBefore(Container focusCycleRoot,Component aComponent){
		int idx = order.indexOf(aComponent) - 1;
		if (idx < 0)
			idx = order.size() - 1;
		return order.get(idx);
	}
	@Override
	public Component getDefaultComponent(Container focusCycleRoot){return order.get(0);}
	@Override
	public Component getLastComponent(Container focusCycleRoot) {return order.lastElement();}
	@Override
	public Component getFirstComponent(Container focusCycleRoot) {return order.get(0);}
}
