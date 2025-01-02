package simple.formats.xlsx;

import java.io.IOException;
import java.nio.file.Path;

import org.xml.sax.SAXException;

public class XlsxDocumentExtendedProperties extends XlsxDocument{


	public XlsxDocumentExtendedProperties(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.PropertiesExtended);
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
