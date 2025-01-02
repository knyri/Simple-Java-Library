package simple.formats.xlsx;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import simple.io.FileUtil;
import simple.io.ZipUtil;
import simple.w3c.NodeUtil;

public class XlsxPackage implements Closeable{
	private final Path dir;
	private final Map<String, XlsxDocument> docs= new HashMap<>();
	private XlsxConnections conns;
	private XlsxSharedStrings strings;
	private final List<XlsxCustomXml> custXml= new ArrayList<>();
	private final List<XlsxQueryTable> queryTables= new ArrayList<>();
	private final List<XlsxTable> tables = new ArrayList<>();
	private final List<XlsxWorksheet> sheets = new ArrayList<>();
	private byte stage= 0;
	final int pathSplit;
	public XlsxPackage(File f) throws IOException, SAXException {
		this(f.toPath());
	}
	String getDocKey(XlsxDocument doc) throws XlsxException {
		for(Entry<String, XlsxDocument> entry: docs.entrySet()) {
			if(entry.getValue() == doc) {
				return entry.getKey();
			}
		}
		throw new XlsxException("The document is not part of this package");
	}
	public int getSharedString(String s) {
		return strings.get(s);
	}
	public void replaceDocument(String docName, Path replacement) throws IOException, SAXException {
		XlsxDocument doc= docs.get(docName);
		Files.copy(replacement, doc.file, StandardCopyOption.REPLACE_EXISTING);
		XlsxDocument d= make(doc.getType(), doc.file);
		docs.replace(docName, d);
		switch(stage) {
		case 1:
			d.afterInit();
			break;
		case 2:
			d.afterInit();
			d.open();
			break;
		case 3:
			d.afterInit();
			d.open();
			d.packageFullyLoaded();
		}
	}
	public void replaceDocument(XlsxDocument doc, Path replacement) throws IOException, SAXException, XlsxException {
		replaceDocument(getDocKey(doc), replacement);
	}
	public void replaceDocument(String docName, Document replacement) throws IOException, SAXException, TransformerException {
		XlsxDocument doc= docs.get(docName);
		XlsxUtil.save(replacement, doc.file);
		XlsxDocument d= make(doc.getType(), doc.file);
		docs.replace(docName, d);
		switch(stage) {
		case 1:
			d.afterInit();
			break;
		case 2:
			d.afterInit();
			d.open();
			break;
		case 3:
			d.afterInit();
			d.open();
			d.packageFullyLoaded();
		}
	}
	public void replaceDocument(XlsxDocument doc, Document replacement) throws IOException, SAXException, TransformerException, XlsxException {
		replaceDocument(getDocKey(doc), replacement);
	}
	public void addDocument(Path fullPath, XlsxDocument doc) throws IOException, SAXException {
		docs.put(FileUtil.toRealPath(fullPath).toString().substring(pathSplit), doc);
		switch(stage) {
		case 1:
			doc.afterInit();
			break;
		case 2:
			doc.afterInit();
			doc.open();
			break;
		case 3:
			doc.afterInit();
			doc.open();
			doc.packageFullyLoaded();
		}
	}
	public List<XlsxCustomXml> getCustomXml() {
		return custXml;
	}
	XlsxDocument make(XlsxDocumentType type, Path path) throws IOException, SAXException {
		switch(type) {
		case Connections:
			return new XlsxConnections(this, path);
		case CustomXml:
			return new XlsxCustomXml(this, path);
		case PropertiesCore:
			return new XlsxDocumentCoreProperties(this, path);
		case PropertiesCustom:
			return new XlsxDocumentCustomProperties(this, path);
		case PropertiesExtended:
			return new XlsxDocumentExtendedProperties(this, path);
		case QueryTable:
			return new XlsxQueryTable(this, path);
		case Relationship:
			return new XlsxRelationships(this, path);
		case SharedStrings:
			return new XlsxSharedStrings(this, path);
		case Styles:
			return new XlsxStyles(this, path);
		case Table:
			return new XlsxTable(this, path);
		case Theme:
			return new XlsxTheme(this, path);
		case Workbook:
			return new XlsxWorkbook(this, path);
		case Worksheet:
			return new XlsxWorksheet(this, path);
		default:
			return new XlsxUnknownType(this, path);
		}
	}
	public XlsxPackage(Path f) throws IOException, SAXException {
		dir= Files.createTempDirectory(f.getFileName().toString().replace('.', '_'), PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxrwx---")));
		System.out.println(dir);
		ZipUtil.unzip(f.toFile(), dir.toFile());
		pathSplit= dir.toString().length();
		//f.toAbsolutePath().toString().substring(parent.getBaseDir().toString().length());
		docs.put("[Content_Types].xml", new XlsxUnknownType(this, dir.resolve("[Content_Types].xml")));
		Document contents= docs.get("[Content_Types].xml").getDocumentNode();
		Element types= (Element)contents.getFirstChild();
		for(Element e= NodeUtil.firstChildElement(types); e != null; e= NodeUtil.nextElement(e)) {
			if(e.getNodeName().equals("Override")) {
				XlsxDocumentType type= XlsxDocumentType.get(e.getAttribute("ContentType"));
				XlsxDocument doc= make(type, dir.resolve(e.getAttribute("PartName").substring(1)));
				docs.put(e.getAttribute("PartName"), doc);
				switch(type) {
				case Connections:
					conns= (XlsxConnections)doc;
					break;
				case CustomXml:
					custXml.add((XlsxCustomXml)doc);
					break;
				case PropertiesCore:
					break;
				case PropertiesCustom:
					break;
				case PropertiesExtended:
					break;
				case QueryTable:
					queryTables.add((XlsxQueryTable)doc);
					break;
				case Relationship:
					break;
				case SharedStrings:
					strings= (XlsxSharedStrings)doc;
					break;
				case Styles:
					break;
				case Table:
					tables.add((XlsxTable)doc);
					break;
				case Theme:
					break;
				case Workbook:
					break;
				case Worksheet:
					sheets.add((XlsxWorksheet)doc);
					break;
				default:
					break;
				}
			}
		}
		stage= 1;
//		System.out.println("After init");
		for(XlsxDocument doc: docs.values().toArray(new XlsxDocument[docs.size()])) {
			doc.afterInit();
		}
//		System.out.println("Open");
		stage= 2;
		for(XlsxDocument doc: docs.values().toArray(new XlsxDocument[docs.size()])) {
			doc.open();
		}
		stage= 3;
//		System.out.println("Fully loaded");
		for(XlsxDocument doc: docs.values()) {
			doc.packageFullyLoaded();
		}
	}
	public void save(File dest) throws IOException, TransformerException{
		for(XlsxDocument doc: docs.values()) {
			doc.save();
		}
		ZipUtil.zipFolder(dir.toFile(), dest);
	}
	public void save(Path dest) throws IOException, TransformerException{
		for(XlsxDocument doc: docs.values()) {
			doc.save();
		}
		ZipUtil.zipFolder(dir.toFile(), dest.toFile());
	}
	/**
	 * Needs to be absolute path
	 * @param fullPath
	 * @return
	 */
	public XlsxDocument getDocument(String fullPath) {
//		System.out.append("get doc ").println(fullPath.substring(pathSplit));
		return docs.get(fullPath.substring(pathSplit));
	}
	/**
	 * Name is generally the path relative to the doc root
	 * @param name
	 * @return
	 */
	public XlsxDocument getDocumentByName(String name) {
		return docs.get(name);
	}
	/**
	 * The base dir
	 * @return
	 */
	public Path getBaseDir() {
		return dir;
	}
	@Override
	public void close() throws IOException{
		FileUtil.deleteDir(dir);
	}
	public XlsxConnections getConnections(){
		return conns;
	}
	public List<XlsxQueryTable> getQueryTables(){
		return queryTables;
	}
	public List<XlsxTable> getTables(){
		return tables;
	}
	public List<XlsxWorksheet> getSheets(){
		return sheets;
	}

}
