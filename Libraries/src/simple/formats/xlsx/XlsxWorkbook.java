package simple.formats.xlsx;

import java.io.IOException;
import java.nio.file.Path;

import org.xml.sax.SAXException;

public class XlsxWorkbook extends XlsxDocument{

	public XlsxWorkbook(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.Workbook);
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
