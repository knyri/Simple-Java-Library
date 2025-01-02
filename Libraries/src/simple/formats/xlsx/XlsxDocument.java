package simple.formats.xlsx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import simple.io.FileUtil;

public abstract class XlsxDocument{
	final protected Path file;
	final protected Document doc;
	protected XlsxRelationships relationships;
	final protected XlsxDocumentType type;
	final protected XlsxPackage parent;
	public XlsxDocument(XlsxPackage parent, Path f, XlsxDocumentType type) throws IOException, SAXException {
		this.type= type;
		this.parent= parent;
		file= FileUtil.toRealPath(f);
		doc= XlsxUtil.open(f);
	}
	public XlsxDocumentType getType() {
		return type;
	}
	public Path getPath() {
		return file;
	}
	final void afterInit() throws IOException, SAXException {
		Path rels= this.file.resolveSibling("_rels/" + this.file.getFileName() + ".rels");
		if(Files.exists(rels, new LinkOption[] {})) {
//			System.out.append("parsing rels ").println(rels);
			relationships= new XlsxRelationships(parent, rels);
			parent.addDocument(rels, relationships);
		}else {
			relationships= null;
		}
	}
	abstract void open() throws IOException, SAXException;
	public abstract void packageFullyLoaded() throws IOException, SAXException;
	public void save() throws IOException, TransformerException{
		XlsxUtil.save(doc, file);
	}
	/**
	 * Don't depend on the data to be correct if you use the other methods after modifying the document.
	 * @return
	 */
	public Document getDocumentNode() {
		return doc;
	}
}
