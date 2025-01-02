package simple.formats.xlsx;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import simple.io.FileUtil;
import simple.w3c.NodeUtil;

public class XlsxRelationships extends XlsxDocument implements Iterable<simple.formats.xlsx.XlsxRelationships.XlsxRelationshipEntry>{
	private final List<XlsxRelationshipEntry> rels= new ArrayList<>(10);

	public XlsxRelationships(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.Relationship);
	}

	public static class XlsxRelationshipEntry{
		public final XlsxRelationshipType type;
		public final Path path;
		XlsxRelationshipEntry(XlsxRelationshipType type, Path path) throws IOException{
			this.path= FileUtil.toRealPath(path);
			this.type= type;
		}
	}
	public XlsxRelationshipEntry get(int idx) {
		return rels.get(idx);
	}
	public int getRelationshipCount() {
		return rels.size();
	}

	@Override
	protected void open() throws IOException{
		Element relse= (Element) doc.getFirstChild();
		for(Element e= NodeUtil.firstChildElement(relse); e != null; e= NodeUtil.nextElement(e)) {
//			System.out.append("rel ").println(e.getAttribute("Target"));
			rels.add(
				new XlsxRelationshipEntry(
					XlsxRelationshipType.get(e.getAttribute("Type")),
					file.resolveSibling("../" + e.getAttribute("Target"))
				)
			);
		}
	}
	@Override
	public void packageFullyLoaded() throws IOException, SAXException{
		// TODO Auto-generated method stub

	}
	@Override
	public Iterator<XlsxRelationshipEntry> iterator(){
		return rels.iterator();
	}

}
