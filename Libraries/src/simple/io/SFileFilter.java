package simple.io;

import java.util.Vector;
import java.util.regex.Pattern;
import java.util.Iterator;
import simple.util.ArrayIterator;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.filechooser.FileFilter;

/**Simple version of a mask to filter files.
 * <br>depends on simple.util.ArrayIterator
 * <br>Created: ??
 * @author Kenneth Pierce
 */
public class SFileFilter extends FileFilter implements FilenameFilter {
	private final Vector<Pattern> accepts = new Vector<Pattern>();
	private String desc = null;
	
	public SFileFilter() {}
	public SFileFilter(String[] accept) {
		ArrayIterator<String> iter = new ArrayIterator<String>(accept);
		while (iter.hasNext()) 
			add(Pattern.compile("[a-z0-9]"+iter.next()));
	}
	public SFileFilter(Pattern[] accept) {
		ArrayIterator<Pattern> iter = new ArrayIterator<Pattern>(accept);
		while(iter.hasNext())
			add(iter.next());
	}
	
	public boolean accept(File arg0) {
		if (arg0.isDirectory())
			return true;
		String tmp = arg0.getName().toLowerCase();
		Iterator<Pattern> accept = accepts.iterator();
		while (accept.hasNext())
			if (accept.next().matcher(tmp).matches())
				return true;
		return false;
	}

	public String getDescription() {
		StringBuffer buf = new StringBuffer(accepts.size()*10);
		if (desc!=null)
			buf.append(desc);
		Iterator<Pattern> pIter = accepts.iterator();
		while(pIter.hasNext())
			buf.append(" | "+pIter.next().pattern()+" |");
		return buf.toString();
	}
	public void setDescription(String descr) {
		desc = descr;
	}

	public boolean accept(File dir, String fName) {
		Iterator<Pattern> accept = accepts.iterator();
		while (accept.hasNext())
			if (accept.next().matcher(fName).matches())
				return true;
		return false;
	}
	
	public void add(String accept) {
		accepts.addElement(Pattern.compile(accept));
	}
	public void add(Pattern accept) {
		accepts.addElement(accept);
	}

}
