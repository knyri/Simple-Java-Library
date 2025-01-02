package simple.formats.xlsx;

import static simple.w3c.NodeUtil.firstChildElement;
import static simple.w3c.NodeUtil.nextElement;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XlsxSharedStrings extends XlsxDocument{

	private final Map<String, Integer> strings= new HashMap<>();
	private final AtomicInteger idx= new AtomicInteger(0);
	public XlsxSharedStrings(XlsxPackage parent, Path f) throws IOException, SAXException{
		super(parent, f, XlsxDocumentType.SharedStrings);
	}

	@Override
	protected void open() throws IOException, SAXException{
		Element string= firstChildElement(firstChildElement(doc));
		do{
			strings.put(firstChildElement(string).getTextContent(), idx.getAndIncrement());
		}while(null != (string= nextElement(string)));
	}

	public int get(String s) {
		Integer i= strings.get(s);
		if(null == i) {
			synchronized(strings) {
				i= strings.get(s);
				if(null == i) {
					i= idx.getAndIncrement();
					strings.put(s, i);
				}
			}
		}
		return i;
	}

	@Override
	public void packageFullyLoaded() throws IOException, SAXException{
		// TODO Auto-generated method stub

	}

}
