package simple.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class ZipUtil{
	private ZipUtil(){}
	/**
	 * @param zip The ZIP archive
	 * @param destDir The base directory to export it's contents
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void unzip(File zip, File destDir) throws FileNotFoundException, IOException{
		File tmp;
		try(ZipFile zf= new ZipFile(zip)){
			byte[] copyBuf= new byte[8192];
			Enumeration<? extends ZipEntry> zes= zf.entries();
			while(zes.hasMoreElements()){
				ZipEntry ze= zes.nextElement();
				if(ze.isDirectory()){
					tmp= new File(destDir, ze.getName());
					if(!tmp.exists() && !tmp.mkdirs()){
						throw new IOException("Failed to create directory " + tmp.getPath());
					}
				}else{
					tmp= new File(destDir, ze.getName());
					if(!tmp.exists() && !tmp.getParentFile().mkdirs() && !tmp.createNewFile()){
						throw new IOException("Failed to create file or folders " + tmp.getPath());
					}
					try(
						OutputStream out= new FileOutputStream(tmp);
						InputStream zis= zf.getInputStream(ze);
					){
						FileUtil.copy(zis, out, copyBuf);
					}
				}
			}
		}
	}
	/**
	 * Does not include the base directory entry.
	 * To include the base directory call {@link #zip(File, File...)}
	 *
	 * @param sourceFolder
	 * @param destFile
	 * @throws IOException
	 * @throws ZipException
	 */
	public static void zipFolder(File sourceFolder, File destFile) throws ZipException, IOException{
		zip(destFile, sourceFolder.listFiles());
	}
	/**
	 * Zips several files
	 *
	 * @param dest Output file
	 * @param input Input files
	 * @throws ZipException
	 * @throws IOException
	 */
	public static void zip(File dest, File... input) throws ZipException, IOException{
		try(ZipOutputStream zip= new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(dest), 8192))){
			for(File f: input){
				if(f.isDirectory()){
					zip(zip, f.getName() + '/', f);
					continue;
				}
				zip.putNextEntry(new ZipEntry(f.getName()));
				try(FileInputStream fis= new FileInputStream(f)){
					FileUtil.copy(fis, zip, 4096);
				}
				zip.closeEntry();
			}
		}
	}
	/**
	 * Adds a directory to a ZIP file
	 *
	 * @param zip The ZIP output stream
	 * @param basePath Base path to use for the entry name
	 * @param dir Directory to add
	 * @throws IOException
	 */
	private static void zip(ZipOutputStream zip, String basePath, File dir) throws IOException{
		for(File f: dir.listFiles()){
			if(f.isDirectory()){
				zip(zip, basePath + f.getName() + '/', f);
				continue;
			}
			zip.putNextEntry(new ZipEntry(basePath + f.getName()));
			try(FileInputStream fis= new FileInputStream(f)){
				FileUtil.copy(fis, zip, 4096);
			}
			zip.closeEntry();
		}
	}
}
