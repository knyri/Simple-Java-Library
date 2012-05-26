/**
 * 
 */
package simple.util;
import java.util.Vector;

/**FILO queue
 * <br>Created: Feb 20, 2008
 * @author Kenneth Pierce
 */
public class Stack<E> {
	private final Vector<E> stack = new Vector<E>();
	public synchronized void push(E ele) {
		//System.out.println("Push "+ele);
		stack.add(ele);
	}
	public synchronized E pop() {
		//System.out.println("Pop "+stack.lastElement());
		if (stack.isEmpty())
			return null;
		return stack.remove(stack.size()-1);
	}
	public E peek() {
		return stack.lastElement();
	}
	public int size() {
		return stack.size();
	}
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	public boolean contains(E o) {
		return stack.contains(o);
	}
}
