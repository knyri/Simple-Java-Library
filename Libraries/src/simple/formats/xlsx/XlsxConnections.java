package simple.formats.xlsx;

import java.io.IOException;
import java.nio.file.Path;

import org.xml.sax.SAXException;

public class XlsxConnections extends XlsxDocument{

	public XlsxConnections(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.Connections);
	}

	@Override
	protected void open() throws IOException, SAXException{
		// TODO Auto-generated method stub

	}

	@Override
	public void packageFullyLoaded() throws IOException, SAXException{
		// TODO Auto-generated method stub

	}

}
