/**
 *
 */
package simple.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingWorker;

import simple.util.logging.Log;
import simple.util.logging.LogFactory;

/**A SwingWorker to load files in the background.
 * TODO: Probably needs a rewrite
 * <br>Created: Feb 24, 2009
 * @author Kenneth Pierce
 * @see simple.gui.SwingWorkerProgressWindow
 */
public final class FileLoader extends SwingWorker<List<File>, File> {
	private static final Log log = LogFactory.getLogFor(FileLoader.class);
	private final File START;
	private final List<File> LIST;
	private final List<File> FILES = new ArrayList<File>();
	private boolean recursive = false;
	private int fcount = 0;
	private boolean done = false;
	public FileLoader(File base, boolean recursive) {
		START = base;
		LIST = null;
		this.recursive = recursive;
	}
	public FileLoader(List<File> list, boolean recursive) {
		START = null;
		this.LIST = list;
		this.recursive = recursive;
	}
	public FileLoader(File[] list, boolean recursive) {
		START = null;
		this.LIST = Arrays.asList(list);
		this.recursive = recursive;
	}
	/* (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected List<File> doInBackground() throws Exception {
		File[] ftmp;
		if (START != null) {
			if (START.isDirectory()) {
				ftmp = START.listFiles();
				for (File file2 : ftmp) {
					if (file2.isDirectory() && recursive)
						addSubDirs(file2);
					else {
						FILES.add(file2);
						//log.debug("fload inb","f2:"+file2);
						publish(file2);
					}
				}
			} else {
				FILES.add(START);
				//log.debug("fload inb","st:"+START);
				publish(START);
			}
		} else {
			for (File file : LIST) {
				if (file.isDirectory()) {
					ftmp = file.listFiles();
					for (File file2 : ftmp) {
						if (file2.isDirectory() && recursive)
							addSubDirs(file2);
						else {
							FILES.add(file2);
							//log.debug("fload inb","f2:"+file2);
							publish(file2);
						}
					}
				} else {
					FILES.add(file);
					//log.debug("fload inb","f:"+file);
					publish(file);
				}
			}
		}
		done = true;
		log.debug("done");
		return FILES;
	}
	private void addSubDirs(File dir) {
		log.debug("fload add sub:",dir.toString());
		File[] sub = dir.listFiles();
		for(File f : sub) {
			if (f.isDirectory())
				addSubDirs(f);
			else {
				FILES.add(f);
				//log.debug("fload asd",f.toString());
				publish(f);
			}
		}
	}
	@Override
	protected void process(List<File> chunks) {
		log.debug("fload proc",""+chunks.size());
		fcount+=chunks.size();
		if (done) {
			setProgress(100);
		} else {
			setProgress(((fcount-1)*100)/FILES.size());
			log.debug("fload proc",""+((fcount-1)*100)/FILES.size());
		}
	}

}
