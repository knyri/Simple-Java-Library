package simple.formats.xlsx;

import static simple.w3c.NodeUtil.firstChildElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

public class XlsxCustomXml extends XlsxDocument{

	private static Pattern fileNumber= Pattern.compile("itemProps([0-9]+)\\.xml");
	private String contentType;
	private XlsxDocument item= null;

	public XlsxCustomXml(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.CustomXml);
	}

	public XlsxDocument getItem() throws IOException, SAXException {
		if(null == item) {
			Matcher m= fileNumber.matcher(file.toString());
			System.out.println(file);
			if(!m.find()) {
				System.out.println("eh, not found doc");
			}
			item= new XlsxUnknownType(parent, file.resolveSibling("item" + m.group(1) + ".xml"));
		}
		return item;
	}
	public void setItem(Path item) throws IOException, SAXException {
		Files.copy(item, getItem().file, StandardCopyOption.REPLACE_EXISTING);
		this.item= null;
	}
	public String getContentType() {
		return contentType;
	}
	@Override
	protected void open() throws IOException, SAXException{
		contentType= firstChildElement(firstChildElement(firstChildElement(doc))).getAttribute("ds:uri");
	}

	@Override
	public void packageFullyLoaded() throws IOException, SAXException{
	}


}
