package simple.w3c;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Not multi-thread safe.
 * @author piercekc
 *
 */
public class ElementIterator implements Iterable<Node>{
	private NodeList list= null;
	public ElementIterator(){}
	public ElementIterator(NodeList list){
		setNodeList(list);
	}
	public void setNodeList(NodeList list){
		this.list= list;
	}
	@Override
	public Iterator<Node> iterator(){
		if(list == null){
			throw new IllegalStateException("NodeList hasn't been set yet.");
		}
		Iterator<Node> ret= new Iterator<Node>(){
			private Node next= list.item(0);
			boolean shouldInit= true;
			@Override
			public boolean hasNext(){
				return next != null;
			}

			@Override
			public Node next(){
				if(shouldInit){
					shouldInit= false;
					init();
					return null;
				}
				Node ret= next;
				init();
				return ret;
			}
			void init(){
				while((null != (next= next.getNextSibling())) && next.getNodeType() != Node.ELEMENT_NODE);
			}
		};
		ret.next();
		return ret;
	}

}
