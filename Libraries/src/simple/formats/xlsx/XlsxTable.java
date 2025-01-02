package simple.formats.xlsx;

import static simple.w3c.NodeUtil.firstChildElement;
import static simple.w3c.NodeUtil.nextElement;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import simple.w3c.NodeBuilder;
import simple.w3c.NodeUtil;

public class XlsxTable extends XlsxDocument implements XlsxNamedItem{
	public XlsxTable(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.Table);
	}

	private Element table;
	private Node name, displayName;
	private Node ref;
	private Element autofilter;
	private Element columns;
	private Attr columnCountN;
	private int columnCount;
	private boolean isQueryTable= false;
	private XlsxQueryTable queryTable;
	private XlsxWorksheet sheet= null;
	private String lastRow;
	private List<Node> columnNames= new ArrayList<>();

	@Override
	protected void open() throws IOException, SAXException{
		table= (Element)doc.getFirstChild();
		name=  table.getAttributeNode("name");
		displayName= table.getAttributeNode("displayName");
		ref= table.getAttributeNode("ref");
		autofilter= firstChildElement(table);
		columns= nextElement(autofilter);
		columnCountN= columns.getAttributeNode("count");
		columnCount= Integer.parseInt(columnCountN.getNodeValue(), 10);
		lastRow= ref.getNodeValue().replaceAll("^[A-Z]+[0-9]+:[A-Z]+", "");
		for(Element col= NodeUtil.firstChildElement(columns); col != null; col= NodeUtil.nextElement(col)) {
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
	void xSetQueryTable(XlsxQueryTable t) {
		queryTable= t;
		isQueryTable= true;
	}
	void xSetWorksheet(XlsxWorksheet sheet) {
		this.sheet= sheet;
	}
	void xAddColumn(String name, String columnId) {
		String cc= Integer.toString(++columnCount, 10);
		NodeBuilder n= new NodeBuilder(doc.createElement("tableColumn"));
		n
			.addAttr("id", cc)
			.addAttr("xr3:uid", "{" + UUID.randomUUID().toString().toUpperCase() + "}")
			.addAttr("uniqueName", cc)
			.addAttr("name", name)
			.addAttr("queryTableFieldId", columnId)
		;
		columns.appendChild(n.getNode());
		columnCountN.setNodeValue(cc);
		String newDim= "A1:" + XlsxUtil.getColumnRef(columnCount) + lastRow;
		ref.setNodeValue(newDim);
		autofilter.getAttributeNode("ref").setNodeValue(newDim);
		if(sheet != null) {
			sheet.xAddColumn(name);
		}
	}
	public void addColumn(String name) {
		if(isQueryTable) {
			queryTable.addField(name);
			return;
		}
		String cc= Integer.toString(++columnCount, 10);
		NodeBuilder n= new NodeBuilder(doc.createElement("tableColumn"));
		n
			.addAttr("id", cc)
			.addAttr("xr3:uid", "{" + UUID.randomUUID().toString().toUpperCase() + "}")
			.addAttr("uniqueName", cc)
			.addAttr("name", name)
		;
		columns.appendChild(n.getNode());
		columnCountN.setNodeValue(cc);
	}
	@Override
	public String getName() {
		return name.getNodeValue();
	}

	@Override
	public void setName(String name){
		this.name.setNodeValue(name);
		this.displayName.setNodeValue(name);
	}

	@Override
	public void packageFullyLoaded() throws IOException, SAXException{
//		System.out.println(table.getAttribute("tableType"));
		if("queryTable".equals(table.getAttribute("tableType"))) {
			for(int i= 0, end= relationships.getRelationshipCount(); i < end; i++) {
				if(relationships.get(i).type == XlsxRelationshipType.QueryTable) {
					XlsxQueryTable t= (XlsxQueryTable) parent.getDocument(relationships.get(i).path.toString());
					t.xSetTable(this);
					xSetQueryTable(t);
					this.isQueryTable= true;
					break;
				}
			}
		}
	}
	@Override
	public XlsxDocument getDocument(){
		return this;
	}

}
