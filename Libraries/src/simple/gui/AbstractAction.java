/**
 *
 */
package simple.gui;

import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;

/**
 * <hr>
 * <br>Created: May 21, 2011
 * @author Kenneth Pierce
 */
public abstract class AbstractAction implements Action {
	private final List<PropertyChangeListener>pcls=new LinkedList<PropertyChangeListener>();
	private final Hashtable<String,Object>properties=new Hashtable<String,Object>();
	private boolean enabled=true;
	@Override
	public void addPropertyChangeListener(final PropertyChangeListener pcl){pcls.add(pcl);}
	@Override
	public Object getValue(final String key) {return properties.get(key);}
	@Override
	public boolean isEnabled(){return enabled;}
	@Override
	public void putValue(final String key,final Object value){properties.remove(key);properties.put(key, value);}
	@Override
	public void removePropertyChangeListener(final PropertyChangeListener pcl){pcls.remove(pcl);}
	@Override
	public void setEnabled(final boolean b){enabled=b;}
}
