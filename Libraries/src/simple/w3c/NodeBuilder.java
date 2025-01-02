package simple.w3c;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NodeBuilder{
	private Node n;
	private NamedNodeMap attr;
	private Document doc;
	public NodeBuilder(Node n){
		setNode(n);
	}
	public NodeBuilder setNode(Node n) {
		this.n= n;
		attr= n.getAttributes();
		doc= n.getOwnerDocument();
		return this;
	}
	public NodeBuilder addChild(Node child) {
		n.appendChild(child);
		return this;
	}
	public NodeBuilder addAttr(String name, String value) {
		Attr a= doc.createAttribute(name);
		a.setNodeValue(value);
		attr.setNamedItem(a);
		return this;
	}
	public Node getNode() {
		return n;
	}

}
