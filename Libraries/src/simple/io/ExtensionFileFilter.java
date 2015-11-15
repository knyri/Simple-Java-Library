package simple.io;

import java.io.File;
import java.io.FileFilter;

/**
 * Filters files by their extension.
 * @author Ken Pierce
 *
 */
public class ExtensionFileFilter implements FileFilter{
	private final String[] extensions;
	public ExtensionFileFilter(String... extensions){
		this.extensions= new String[extensions.length];
		for(int i=0; i<extensions.length; i++){
			this.extensions[i]= extensions[i].toLowerCase();
		}
	}

	@Override
	public boolean accept(File file){
		String ext= file.getName();
		ext= ext.substring(ext.lastIndexOf('.')+1).toLowerCase();
		for(String s: extensions){
			if(s.equals(ext))
				return true;
		}
		return false;
	}

}
