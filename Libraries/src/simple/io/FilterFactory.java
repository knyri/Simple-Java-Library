/**
 *
 */
package simple.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * @author Ken
 *
 */
public final class FilterFactory {
	public static File[] listFiles(String dir, String... ext) {
		File file = new File(dir);
		return file.listFiles(createFilenameFilter(ext));
	}
	/**
	 * Case insensitive extension filename filter.
	 * @param list List of wanted extensions
	 * @return
	 */
	public static FilenameFilter createFilenameFilter(String... list) {
		final List<String> exts= new LinkedList<>();
		for(String ext: list){
			exts.add(ext.toLowerCase());
		}
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir,String file) {
				String ext = file.substring(file.lastIndexOf('.')+1).toLowerCase();
				for(String s: exts){
					if(s.equals(ext)){
						return true;
					}
				}
				return false;
			}
		};
	}
	/**
	 * Case insensitive file filter. Creates the description from
	 * the list of extensions.
	 * @param list List of wanted extensions.
	 * @return
	 */
	public static FileFilter createFileFilter(String... list) {
		final List<String> exts= new LinkedList<>();
		StringBuilder descb= new StringBuilder(list.length*5);
		for(String ext: list){
			exts.add(ext.toLowerCase());
			descb.append(ext).append(',');
		}
		descb.deleteCharAt(descb.length()-1);
		final String desc= descb.toString();
		return new FileFilter() {
			@Override
			public boolean accept(File file) {
				String ext= file.getName();
				ext= ext.substring(ext.lastIndexOf('.')+1).toLowerCase();
				for(String s: exts){
					if(s.equals(ext)){
						return true;
					}
				}
				return false;
			}

			@Override
			public String getDescription() {
				return desc;
			}
		};
	}
	/**
	 * Case insensitive extension filter.
	 * @param ext
	 * @param desc
	 * @return
	 */
	public static FileFilter createFileFilter(final String ext, final String desc) {
		final String extl= ext.toLowerCase();
		return new javax.swing.filechooser.FileFilter() {
			@Override
			public boolean accept(File file) {return file.isDirectory() || file.getName().toLowerCase().endsWith(extl);}
			@Override
			public String getDescription() {return desc;}
		};
	}
	/**
	 * case insensitive extension filter
	 * @param directory nullable
	 * @param ext
	 * @return
	 */
	public static FilenameFilter createFilenameFilter(final File directory, final String ext) {
		final String extl= ext.toLowerCase();
		if(directory != null){
			return new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return directory.equals(dir) && name.toLowerCase().endsWith(extl);
				}
			};
		} else {
			return new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(extl);
				}
			};
		}
	}
}
