package simple.formats.xlsx;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import simple.w3c.NodeBuilder;
import simple.w3c.NodeUtil;

public class XlsxQueryTable extends XlsxDocument{

	public XlsxQueryTable(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.QueryTable);
	}
	private Node nextIdN, columnCountN, nameN, connectionIdN, fields;
	private int nextId, columnCount;
	private XlsxTable table= null;
	private List<Node> columnNames= new ArrayList<>();

	public String getConnectionId() {
		return connectionIdN.getNodeValue();
	}
	public String getName() {
		return nameN.getNodeValue();
	}

	@Override
	protected void open() throws IOException, SAXException{
//		System.out.println(file);
		// table
		Element qtable= (Element) doc.getFirstChild();
		nameN= qtable.getAttributeNode("name");
		connectionIdN= qtable.getAttributeNode("connectionId");
		// queryTableRefresh
		Element tmp= NodeUtil.firstChildElement(qtable);
//		System.out.println(tmp.getTagName());
		nextIdN= tmp.getAttributeNode("nextId");
		nextId= Integer.parseInt(nextIdN.getNodeValue(), 10);
		// queryTableFields
		tmp= NodeUtil.firstChildElement(tmp);
//		System.out.println(tmp.getTagName());
		columnCountN= tmp.getAttributeNode("count");
		columnCount= Integer.parseInt(columnCountN.getNodeValue(), 10);
		fields= tmp;
		for(Element col= NodeUtil.firstChildElement(fields); col != null; col= NodeUtil.nextElement(col)) {
			columnNames.add(col.getAttributeNode("name"));
		}
	}
	public String getColumnName(int idx) {
		return columnNames.get(idx).getNodeValue();
	}
	public void setColumnName(int idx, String name) {
		columnNames.get(idx).setNodeValue(name);
	}
	public int columnCount() {
		return columnNames.size();
	}
	void xSetTable(XlsxTable t) {
//		System.out.append("Table set ").println(file);
		table= t;
	}
	/**
	 * Adds field to this query table and any related tables/sheets
	 * @param name
	 * @return
	 */
	public XlsxQueryTable addField(String name) {
		return addField(name, null);
	}
	public XlsxQueryTable addField(String name, String styleId) {
		String id= Integer.toString(nextId++, 10);
		NodeBuilder n= new NodeBuilder(doc.createElement("queryTableField"));
		n
			.addAttr("id", id)
			.addAttr("name", name)
			.addAttr("tableColumnId", Integer.toString(++columnCount, 10))
		;
		if(styleId != null) {
			n.addAttr("dataDxfId", styleId);
		}
		fields.appendChild(n.getNode());
		nextIdN.setNodeValue(Integer.toString(nextId, 10));
		columnCountN.setNodeValue(Integer.toString(columnCount, 10));
		table.xAddColumn(name, id);
		return this;
	}
	@Override
	public void packageFullyLoaded() throws IOException, SAXException{
	}
}
