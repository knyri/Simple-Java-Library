package simple.formats.xlsx;

import java.io.IOException;
import java.nio.file.Path;

import org.xml.sax.SAXException;

public class XlsxTheme extends XlsxDocument{

	public XlsxTheme(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.Theme);
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
