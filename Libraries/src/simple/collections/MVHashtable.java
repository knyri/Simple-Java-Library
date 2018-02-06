/**
 *
 */
package simple.collections;

import java.util.HashMap;
import java.util.HashSet;

/**Allows multiple values to be stored for each key. Identical values can be
 * stored for a key if, and only if, the hash is different.
 * <br>Created: Nov 3, 2010
 * @author Kenneth Pierce
 * @param <K> Type of the key.
 * @param <V> Type of the stored values.
 */
public final class MVHashtable<K, V> extends HashMap<K, HashSet<V>> {
	private static final long serialVersionUID = -6236840157697527805L;
	public synchronized void add(K key, V value) {
		HashSet<V> tmp = get(key);
		if (tmp==null) {
			tmp = new HashSet<V>(5,5);
			put(key, tmp);
		}
		tmp.add(value);
	}
	public boolean contains(K key, V value) {
		HashSet<V> tmp = get(key);
		if (tmp==null) return false;
		return tmp.contains(value);
	}
}
