package simple.formats.xlsx;

import java.io.IOException;
import java.nio.file.Path;

import org.xml.sax.SAXException;

public class XlsxDocumentCustomProperties extends XlsxDocument{


	public XlsxDocumentCustomProperties(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.PropertiesCustom);
	}

	@Override
	protected void open() throws IOException{
		// TODO Auto-generated method stub

	}

	@Override
	public void packageFullyLoaded() throws IOException, SAXException{
		// TODO Auto-generated method stub

	}

}
