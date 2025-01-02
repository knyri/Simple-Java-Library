package simple.formats.xlsx;

import static simple.w3c.NodeUtil.firstChildElement;
import static simple.w3c.NodeUtil.nextElement;

import java.io.IOException;
import java.nio.file.Path;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import simple.formats.xlsx.XlsxRelationships.XlsxRelationshipEntry;
import simple.w3c.NodeBuilder;
public class XlsxWorksheet extends XlsxDocument{

	public XlsxWorksheet(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.Worksheet);
	}

	int lastCol;
	int lastRow;
	Node dimensions, cols, rows;
	XlsxTable table= null;
	@Override
	protected void open() throws IOException, SAXException{
		Element sheet= (Element) doc.getFirstChild();
		dimensions= sheet.getFirstChild().getAttributes().getNamedItem("ref");
		// skip sheetViews and sheetFormatPr
		cols= nextElement(sheet.getFirstChild());
		while(cols != null && !"cols".equals(cols.getNodeName())) {
			cols= nextElement(cols);
		}
		lastCol= Integer.parseInt(cols.getLastChild().getAttributes().getNamedItem("max").getNodeValue(), 10);
//		System.out.append("sheet last col idx ").println(lastCol);
		lastRow= Integer.parseInt(dimensions.getNodeValue().replaceAll("^[A-Z]+[0-9]+:[A-Z]+", ""), 10);
		rows= nextElement(cols);
		while(rows != null && !"sheetData".equals(rows.getNodeName())) {
			rows= nextElement(rows);
		}
	}

	@Override
	public void packageFullyLoaded() throws IOException, SAXException{
		if(relationships != null) {
			for(XlsxRelationshipEntry rel: relationships) {
				if(rel.type == XlsxRelationshipType.Table) {
					table= (XlsxTable) parent.getDocument(rel.path.toString());
					table.xSetWorksheet(this);
				}
			}
		}
	}
	void xAddColumn(String name) {
		NodeBuilder n= new NodeBuilder(doc.createElement("col"));
		String
			col= Integer.toString(++lastCol, 10),
			newDim= "A1:" + XlsxUtil.getColumnRef(lastCol) + lastRow
		;
//		System.out.append("new sheet column ").append(newDim).println(name);
		dimensions.setNodeValue(newDim);

		n
			.addAttr("min", col)
			.addAttr("max", col)
			.addAttr("width", "10")
			.addAttr("bestFit", "1")
			.addAttr("customWidth", "1")
		;
		cols.appendChild(n.getNode());

		// add column to all rows
		Element
			cell= doc.createElement("c"),
			val= doc.createElement("v"),
			row= firstChildElement(rows)
		;
		String
			spans= "1:" + lastCol,
			colRef= XlsxUtil.getColumnRef(lastCol)
		;
		cell.setAttribute("t", "s");
		cell.setAttribute("s", "1");
		cell.setAttribute("r", colRef + row.getAttribute("r"));
		Node
			blankVal= val.cloneNode(false),
			blankCell= cell.cloneNode(false)
		;
		blankCell.appendChild(blankVal);

		row.setAttribute("spans", spans);
		val.setTextContent(Integer.toString(parent.getSharedString(name), 10));
		cell.appendChild(val);
		row.appendChild(cell);
		while(null != (row= nextElement(row))) {
//			System.out.append("Adding col to row ").println(row.getAttribute("r"));
			blankCell= blankCell.cloneNode(true);
			row.setAttribute("spans", spans);
			blankCell.getAttributes().getNamedItem("r").setNodeValue(colRef + row.getAttribute("r"));
			row.appendChild(blankCell);
		}
	}
	public void addColumn(String name) {
		if(table != null) {
			table.addColumn(name);
			return;
		}
		xAddColumn(name);
	}

}
