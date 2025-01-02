package simple.formats.xlsx;

import java.io.IOException;
import java.nio.file.Path;

import org.xml.sax.SAXException;

public class XlsxUnknownType extends XlsxDocument{

	public XlsxUnknownType(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, null);
	}

	@Override
	protected void open() throws IOException, SAXException{
	}

	@Override
	public void packageFullyLoaded() throws IOException, SAXException{
	}

}
