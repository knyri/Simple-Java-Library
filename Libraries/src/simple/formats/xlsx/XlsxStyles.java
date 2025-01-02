package simple.formats.xlsx;

import static simple.w3c.NodeUtil.findElement;
import static simple.w3c.NodeUtil.firstChildElement;
import static simple.w3c.NodeUtil.nextElement;

import java.io.IOException;
import java.nio.file.Path;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XlsxStyles extends XlsxDocument{

	private int dxfCount;
	public XlsxStyles(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.Styles);
	}

	@Override
	protected void open() throws IOException, SAXException{
		Element dxfs= findElement(firstChildElement(doc), "dxfs");
		dxfCount= Integer.parseInt(dxfs.getAttribute("count"), 10);
		for(Element dxf= firstChildElement(dxfs); dxf != null; dxf= nextElement(dxf)) {
			//
		}
	}

	@Override
	public void packageFullyLoaded() throws IOException, SAXException{
	}

}
