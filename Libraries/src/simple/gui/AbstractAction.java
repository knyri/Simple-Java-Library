/**
 * 
 */
package simple.gui;

import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Action;

/**
 * <hr>
 * <br>Created: May 21, 2011
 * @author Kenneth Pierce
 */
public abstract class AbstractAction implements Action {
	private final Vector<PropertyChangeListener>pcls=new Vector<PropertyChangeListener>(3);
	private final Hashtable<String,Object>properties=new Hashtable<String,Object>();
	private boolean enabled=true;
	public void addPropertyChangeListener(final PropertyChangeListener pcl){pcls.add(pcl);}
	public Object getValue(final String key) {return properties.get(key);}
	public boolean isEnabled(){return enabled;}
	public void putValue(final String key,final Object value){properties.remove(key);properties.put(key, value);}
	public void removePropertyChangeListener(final PropertyChangeListener pcl){pcls.remove(pcl);}
	public void setEnabled(final boolean b){enabled=b;}
}
