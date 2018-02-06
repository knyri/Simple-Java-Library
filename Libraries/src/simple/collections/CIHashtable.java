/**
 *
 */
package simple.collections;

import java.util.HashMap;

import simple.CIString;

/**HashMap that uses case insensitive keys.
 * <hr>
 * Depends on {@link simple.CIString}
 * <br>Created: Dec 5, 2010
 * @author Kenneth Pierce
 * @param <V> Type for the stored value
 */
public final class CIHashtable<V> extends HashMap<CIString, V> {
	private static final long serialVersionUID = -6151304735728618108L;
	/**Convenience method for {@link #get(Object)}.
	 * @param key
	 * @return The value associated with the key
	 */
	public V get(String key) {
		return get(new CIString(key));
	}
	/**Convenience method for {@link #put(Object, Object)}.
	 * @param key
	 * @param value
	 * @return The previously associated value or null.
	 */
	public V put(String key, V value) {
		return put(new CIString(key), value);
	}
	/**Convenience method for {@link #containsKey(Object)}.
	 * @param key
	 * @return true if the key is found, false otherwise
	 */
	public boolean containsKey(String key) {
		return containsKey(new CIString(key));
	}
}
