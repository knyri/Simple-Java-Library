package simple.parser.ml.impl;

import static simple.parser.ml.Tag.CDATA;
import static simple.parser.ml.Tag.HTMLCOMM;
import static simple.parser.ml.Tag.META;
import static simple.parser.ml.Tag.SGMLCDATA;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import simple.CIString;
import simple.parser.ml.PageFormatter;
import simple.parser.ml.Tag;

/**
 * @author Kenneth Pierce
 *
 */
public final class DefaultPageFormatter extends PageFormatter {
	private static final DefaultPageFormatter instance= new DefaultPageFormatter();
	public static final DefaultPageFormatter getInstance(){
		return instance;
	}
	private final String indent;
	public DefaultPageFormatter(){
		this("\t");
	}
	public DefaultPageFormatter(String indent) {
		this.indent= indent;
	}

	@Override
	public void writeTo(Tag tag, Writer out) throws IOException {
		format(out, tag, "");
	}
	private void format(Writer buf, Tag tag, final String indent) throws IOException{
		if (tag.getName().equals(CDATA)){
			buf
				.append(tag.getContent())
				.append(EOL);
			return;
		}
		if (tag.getName().equals(SGMLCDATA)){
			buf
				.append("<!CDATA[[")
				.append(tag.getContent())
				.append("]]>")
				.append(EOL);
			return;
		}
		if (tag.getName().equals(HTMLCOMM) || tag.getName().equals(META)){
			buf
				.append(tag.getContent())
				.append(EOL);
			return;
		}
		buf
			.append(indent)
			.append('<')
			.append(tag.getName());
		Map<CIString, String> properties= tag.getProperties();
		for (final CIString key : properties.keySet()) {
			buf
				.append(" ")
				.append(key)
				.append("=\"")
				.append(properties.get(key))
				.append("\"");
		}
		if (tag.isSelfClosing()) {
			buf
				.append("/>")
				.append(EOL);
			return;
		}
		buf
			.append('>')
			.append(EOL);
		if(tag.hasChild()){
			for(Tag t: tag){
				format(buf, t, indent + this.indent);
			}
			buf.append(indent);
		}
		buf
			.append("</")
			.append(tag.getName())
			.append(">")
			.append(EOL);
	}

}
