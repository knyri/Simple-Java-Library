package simple.w3c;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class NodeUtil{
	private NodeUtil(){}

	public static Node firstElement(NodeList list){
		Node tmp= list.item(0);
		if(tmp.getNodeType() == Node.ELEMENT_NODE){
			return tmp;
		}
		for(int idx= 1, end= list.getLength(); idx< end; idx++){
			tmp= list.item(idx);
			if(tmp.getNodeType() == Node.ELEMENT_NODE){
				break;
			}
		}
		return tmp;
	}
	public static Node nextElement(Node from){
		while((null != (from= from.getNextSibling())) && from.getNodeType() != Node.ELEMENT_NODE);
		return from;
	}
	public static Node previousElement(Node from){
		while((null != (from= from.getPreviousSibling())) && from.getNodeType() != Node.ELEMENT_NODE);
		return from;
	}
	public static Node firstChildElement(Node parent){
		Node tmp= parent.getFirstChild();
		if(tmp.getNodeType() != Node.ELEMENT_NODE){
			tmp= nextElement(tmp);
		}
		return tmp;
	}
	public static Node findElement(Node start, String name){
		Node tmp= nextElement(start);
		while(tmp != null && !name.equals(tmp.getLocalName())){
			tmp= nextElement(tmp);
		}
		return tmp;
	}
	public static String getAttrValue(Node node, String name){
		return getAttrValue(node.getAttributes(), name);
	}
	public static String getAttrValue(NamedNodeMap attrs, String name){
		Node attr= attrs.getNamedItem(name);
		if(attr == null){
			return null;
		}
		return attr.getNodeValue();
	}
	public static void setAttrValue(Node node, String attr, String value){
		NamedNodeMap attrs= node.getAttributes();
		Node n= attrs.getNamedItem(attr);
		if(n == null){
			n= node.getOwnerDocument().createAttribute(attr);
			n.setNodeValue(value);
			attrs.setNamedItem(n);
		}else{
			n.setNodeValue(value);
		}
	}
}
