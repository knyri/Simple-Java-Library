package simple.w3c;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class NodeUtil{
	private NodeUtil(){}

	public static Element firstElement(NodeList list){
		Node tmp= list.item(0);
		if(tmp.getNodeType() == Node.ELEMENT_NODE){
			return (Element)tmp;
		}
		for(int idx= 1, end= list.getLength(); idx< end; idx++){
			tmp= list.item(idx);
			if(tmp.getNodeType() == Node.ELEMENT_NODE){
				break;
			}
		}
		return (Element)tmp;
	}
	public static Element nextElement(Node from){
		while((null != (from= from.getNextSibling())) && from.getNodeType() != Node.ELEMENT_NODE);
		return (Element)from;
	}
	public static Element previousElement(Node from){
		while((null != (from= from.getPreviousSibling())) && from.getNodeType() != Node.ELEMENT_NODE);
		return (Element)from;
	}
	public static Element firstChildElement(Node parent){
		Node tmp= parent.getFirstChild();
		if(tmp.getNodeType() != Node.ELEMENT_NODE){
			tmp= nextElement(tmp);
		}
		return (Element)tmp;
	}
	/**
	 * Finds the element named [name]. Considers the [start] node
	 * @param start
	 * @param name
	 * @return
	 */
	public static Element findElement(Node start, String name){
		Node tmp= start;
		while(tmp != null && !name.equals(tmp.getNodeName())){
			tmp= nextElement(tmp);
		}
		return (Element)tmp;
	}
	/**
	 * Finds the next element named [name]. Does not consider the [start] node
	 * @param start
	 * @param name
	 * @return
	 */
	public static Element findNextElement(Node start, String name){
		Element tmp= nextElement(start);
		while(tmp != null && !name.equals(tmp.getNodeName())){
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
