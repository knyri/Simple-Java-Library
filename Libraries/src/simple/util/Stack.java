/**
 *
 */
package simple.util;
import java.util.LinkedList;

/**FILO queue
 * <br>Created: Feb 20, 2008
 * @author Kenneth Pierce
 */
public class Stack<E> {
	private final LinkedList<E> stack = new LinkedList<E>();
	public synchronized void push(E ele) {
		//System.out.println("Push "+ele);
		stack.add(ele);
	}
	public synchronized E pop() {
		//System.out.println("Pop "+stack.lastElement());
		if (stack.isEmpty())
			return null;
		return stack.removeLast();
	}
	public E peek() {
		return stack.peekLast();
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
