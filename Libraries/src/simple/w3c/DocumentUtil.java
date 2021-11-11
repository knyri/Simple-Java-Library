package simple.w3c;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public final class DocumentUtil{
	private static final DocumentBuilderFactory docBuilderFactory= DocumentBuilderFactory.newInstance();
	private static final TransformerFactory docOutputFactory= TransformerFactory.newInstance();
	private DocumentUtil(){}

	public static DocumentBuilder getDefaultDocumentBuilder() throws ParserConfigurationException{
		return docBuilderFactory.newDocumentBuilder();
	}
	public static Transformer getDefaultTransformer() throws TransformerConfigurationException {
		return docOutputFactory.newTransformer();
	}

	public static Document parse(File f) throws SAXException, IOException, ParserConfigurationException{
		return docBuilderFactory.newDocumentBuilder().parse(f);
	}

	public static final void output(Document doc, File dest) throws TransformerConfigurationException, TransformerException{
		docOutputFactory.newTransformer().transform(new DOMSource(doc), new StreamResult(dest));
	}
}
