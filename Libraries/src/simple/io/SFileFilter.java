package simple.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileFilter;

/**Simple version of a mask to filter files.
 * <br>depends on simple.util.ArrayIterator
 * <br>Created: ??
 * @author Kenneth Pierce
 */
public class SFileFilter extends FileFilter implements FilenameFilter {
	private final List<Pattern> accepts = Collections.synchronizedList(new LinkedList<Pattern>());
	private String desc = null;

	public SFileFilter() {}
	public SFileFilter(String[] accept) {
		for(String item : accept)
			add(Pattern.compile("[a-z0-9]"+item));
	}
	public SFileFilter(Pattern[] accept) {
		for(Pattern pat : accept)
			add(pat);
	}

	@Override
	public boolean accept(File arg0) {
		if (arg0.isDirectory())
			return true;
		String tmp = arg0.getName().toLowerCase();
		synchronized(accepts){
			Iterator<Pattern> accept = accepts.iterator();
			while (accept.hasNext())
				if (accept.next().matcher(tmp).matches())
					return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		StringBuffer buf = new StringBuffer(accepts.size()*10);
		if (desc!=null)
			buf.append(desc);
		synchronized(accepts){
			Iterator<Pattern> pIter = accepts.iterator();
			while(pIter.hasNext())
				buf.append(" | "+pIter.next().pattern()+" |");
		}
		return buf.toString();
	}
	public void setDescription(String descr) {
		desc = descr;
	}

	@Override
	public boolean accept(File dir, String fName) {
		synchronized(accepts){
			Iterator<Pattern> accept = accepts.iterator();
			while (accept.hasNext())
				if (accept.next().matcher(fName).matches())
					return true;
		}
		return false;
	}

	public void add(String accept) {
		accepts.add(Pattern.compile(accept));
	}
	public void add(Pattern accept) {
		accepts.add(accept);
	}

}
