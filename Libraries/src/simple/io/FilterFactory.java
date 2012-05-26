/**
 * 
 */
package simple.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

/**
 * @author Ken
 *
 */
public final class FilterFactory {
	public static File[] listFiles(String dir, String ext) {
		File file = new File(dir);
		return file.listFiles(createFilenameFilter(ext));
	}
	static Hashtable<String,FileFilter> ffcache = new Hashtable<String,FileFilter>();
	static Hashtable<String,FilenameFilter>fnfcache = new Hashtable<String,FilenameFilter>();
	public static FilenameFilter createFilenameFilter(String list) {
		final String key = list.toLowerCase();
		FilenameFilter ff = fnfcache.get(key);
		if (ff==null)
			ff = new FilenameFilter() {
					final String flist = key;
					public boolean accept(File dir,String file) {
						String ext = file.substring(file.lastIndexOf('.')+1);
						return flist.contains(ext);
					}
				};
			else
				fnfcache.put(key, ff);
		return ff;
	}
	public static FileFilter createFileFilter(String list) {
		final String key = list.toLowerCase();
		FileFilter ff = ffcache.get(key);
		if (ff==null)
			ff = new FileFilter() {
					final String flist = key;
					@Override
					public boolean accept(File file) {
						String ext = file.toString();
						ext = ext.substring(ext.lastIndexOf('.')+1);
						return flist.contains(ext);
					}
		
					@Override
					public String getDescription() {
						return flist;
					}
				};
			else
				ffcache.put(key, ff);
		return ff;
	}
	public static FileFilter createFileFilter(final String ext, final String desc) {
		return new javax.swing.filechooser.FileFilter() {
			final String extention = ext.intern();
			final String description = desc.intern();
			public boolean accept(File file) {return file.getName().endsWith(extention) || file.isDirectory();}
			@Override
			public String getDescription() {return description;}
		};
	}
	public static FilenameFilter createFilenameFilter(final File dir, final String ext) {
		return new FilenameFilter() {
			final File directory = dir;
			final String extention = ext;
			public boolean accept(File dir, String name) {
				if (directory != null) {
					if (dir.equals(directory))
						if (name.endsWith(extention)) return true;
				} else if (name.endsWith(extention)) return true;
				return false;
			}
		};
	}
}
