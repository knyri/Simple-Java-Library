package simple.io;

import java.io.File;

import simple.util.HexUtil;
import simple.util.do_str;

/**Depends on simple.io.ParseException, simple.util.do_str
 * Rename Tokens:
 * <dl>
 * <dt>$N</dt>
 * <dd>File name without extension</dd>
 * <dt>$N(n)</dt>
 * <dd>Characters starting from n of the file name without extension. (inclusive)
 * You can use negative numbers to start from the end of the filename.
 * E.g. 'hello.txt' with  $N(-3) will give 'llo'.</dd>
 * <dt>$N(n,m)</dt>
 * <dd>Characters n to m of the file name without extension. (inclusive, exclusive)
 * You can use negative numbers to start from the end of the filename.
 * E.g. 'hello.txt' with  $N(-2,-1) will give 'l'.</dd>
 * <dt>$F</dt>
 * <dd>File name with extension.</dd>
 * <dt>$F(n,m)</dt>
 * <dd>Characters n to m of the file name with extension. (inclusive, exclusive)
 * You can use negative numbers to start from the end of the filename.
 * E.g. 'hello.txt' with  $F(-2,-1) will give 'x'; $F(0,-3) yields 'hello.'</dd>
 * <dt>$F(n)</dt>
 * <dd>Characters starting from n of the file name with extension. (inclusive)
 * You can use negative numbers to start from the end of the filename.
 * E.g. 'hello.txt' with  $F(-3) will give 'txt'.</dd>
 * <dt>$E</dt>
 * <dd>Extension of file with '.'
 * E.g. 'hello.txt.log' with $E will give '.log'</dd>
 * <dt>$D</dt>
 * <dd>Path without the ending '\'</dd>
 * <dt>$D(n)</dt>
 * <dd>Part n of the directory name from the lowest level.
 * E.g. $D(1) of "C:\program files\games\doom.exe" = "games"</dd>
 * <dt>$M</dt>
 * <dd>Moves the file up one directory. Equivalent to $M(1).</dd>
 * <dt>$M(n)</dt>
 * <dd>Moves the file up the directory tree n times.</dd>
 * <dt>$#</dt>
 * <dd>Number starting from 0. Additional '#' can be added to pad with '0's.
 * (e.g. $## would result in a scheme like 00, 01, 02, etc.)</dd>
 * <dt>$P</dt>
 * <dd>The path with out the drive letter.(includes start and end slash)</dd>
 * </dl>
 * <br>Created: Jun 24, 2008
 * @author Kenneth Pierce
 */
public final class RenameFormat {
	public static final int OK = 0;
	public static final int BADTARGET = 1;
	public static final int BADSOURCE = 2;
	public static final int CANTUNDO = 3;
	public static final int PARSEEXCEPTION = 4;
	public static final int IOERRORMKDIR = 5;
	public static final int IOERRORRENAME = 6;
	//Error messages
	private static final String[] ERR = new String[] {"OK", "Destination already exist", "Source doesn't exist", "Can't Undo: Hasn't been renamed.", "Parse Exception: ", "System error making dirs", "System error renaming"};
	public static boolean fRESOLVEURLESCAPED=false;
	public static boolean fURISAFE=false;
	static private int number = 0;
	private File file;
	private File undo = null;
	private boolean canundo = false;
	private String syn;
	private String destName = null;
	private ParseException pe = null;
	private int errorNum = -1;
	private RenameFormat() {}
	public RenameFormat(final File fil, final String syntax) {
		file = fil;
		syn = syntax;
	}
	public static synchronized void setNumber(final int i) {
		RenameFormat.number = i;
	}
	private static synchronized int increment() {
		return RenameFormat.number++;
	}
	public int rename() {
		if (!file.exists()) return (errorNum=RenameFormat.BADSOURCE);
		if (destName==null) {
			try {
				destName = parse();
			} catch (final ParseException e) {
				pe = e;
				return (errorNum=RenameFormat.PARSEEXCEPTION);
			}
		}
		final int tmp = destName.lastIndexOf('\\');
		if (tmp > 0) {
			undo = new File(destName.substring(0, tmp+1));
			undo.mkdirs();
		}
		undo = new File(destName);
		if (undo.exists()) {return (errorNum=RenameFormat.BADTARGET);}
		if (!file.renameTo(undo)) {return (errorNum=RenameFormat.IOERRORRENAME);}
		canundo = true;
		return (errorNum=RenameFormat.OK);
	}
	public int mockRename() {
		if (!file.exists()) return (errorNum=RenameFormat.BADSOURCE);
		if (destName==null) {
			try {
				destName = parse();
			} catch (final ParseException e) {
				pe = e;
				return (errorNum=RenameFormat.PARSEEXCEPTION);
			}
		}
		undo = new File(destName);
		if (undo.exists()) {return (errorNum=RenameFormat.BADTARGET);}
		return (errorNum=RenameFormat.OK);
	}
	public String parse() throws ParseException {
		final String path = file.getAbsolutePath(), fileName,ext;
		String fullName;
		final StringBuffer buf = new StringBuffer(file.getAbsolutePath().length());
		int begin = path.indexOf(File.separatorChar);
		int end = path.lastIndexOf(File.separatorChar);
		final int lastSlash = end < 0?0:end;
		final int firstSlash = begin < 0?0:begin;
		end = path.lastIndexOf('.');
		final int extDot = (end < lastSlash)?-1:end;
		fullName = path.substring(lastSlash+1);
		if(RenameFormat.fRESOLVEURLESCAPED){
			final StringBuilder resolved=new StringBuilder(fullName.length());
			int lend=0;
			for(int i=0;i<fullName.length();i++){
				if(fullName.charAt(i)!='%'){resolved.append(fullName.charAt(i));continue;}
				lend=i+3;i++;
				if((i+2)>fullName.length())continue;
				if(fullName.charAt(lend)=='%')
					while(fullName.charAt(lend+3)=='%'){
						lend+=3;
						if((lend+3)>fullName.length())break;
					}
				resolved.append(new String(HexUtil.fromHex(fullName.substring(i,lend).replace("%",""))));
				i=lend-1;
			}
			fullName=resolved.toString();
		}
		if (extDot == -1) {
			fileName = fullName;
			ext = "";
		} else {
			fileName = fullName.substring(0, fullName.length()-(path.length()-extDot));
			ext = path.substring(extDot);
		}
		System.out.println(path);
		System.out.println(fullName);
		System.out.println(fileName);
		System.out.println(ext);
		begin = end = 0;
		for (int i = 0;i<syn.length();i++) {
			if (syn.charAt(i)=='$') {
				switch(syn.charAt(++i)) {
				case 'D'://$M$D(1)$N.jpg
				//All or part of the path
					if (syn.charAt(i+1)=='(') {
						begin = i+2;
						end = syn.indexOf(')', i+1);
						if (end==-1)
							throw new ParseException("Missing matching ')' at " + begin);

						if (!do_str.isNaN(syn.substring(begin, end)))
							buf.append(getDir(path, Integer.parseInt(syn.substring(begin, end))));
						else
							throw new ParseException("Expected number in $D(#), got $D(" + syn.substring(begin, end) + ") instead.");
						i = end;
					} else {
						buf.append(path.substring(0,lastSlash));
					}
					break;
				case 'N':
				//fileName
					if ((i+1) < syn.length() && syn.charAt(i+1)=='(') {
						final String[] sTmp = syn.substring(i+2,syn.indexOf(')',i+1)).split(",");
						if (sTmp.length==2) {
							if (!do_str.isNaN(sTmp[0])&&!do_str.isNaN(sTmp[1])) {
								begin = Integer.parseInt(sTmp[0]);
								end = Integer.parseInt(sTmp[1]);
								if (begin < 0)
									begin = fileName.length() + begin;
								if (end < 0)
									end = fileName.length() + end;
								if (end < begin)
									throw new ParseException("End index is before begin index.");
								if (end < 0)
									throw new ParseException("End index comes before the start of the filename by "+(0-end)+".");
								if (begin < 0)
									throw new ParseException("Begin index comes before the start of the filename by "+(0-begin)+".");
								buf.append(fileName.substring(begin, end));
							}
						} else {
							if (!do_str.isNaN(sTmp[0])) {
								begin = Integer.parseInt(sTmp[0]);
								if (begin < 0)
									begin = fileName.length() - 1 + begin;
								if (begin < 0)
									throw new ParseException("Begin index comes before the start of the filename by "+(0-begin)+".");
								buf.append(fileName.substring(begin));
							}
						}
						i=syn.indexOf(')',i+1);
						if (end==-1)
							throw new ParseException("Missing matching ')' at " + begin);
					} else {
						buf.append(fileName);
					}
					break;
				case '#':
				//number format
//$D\akina_$##$E
					final String tmp = do_str.padLeft(syn.lastIndexOf('#')+1-i,'0',Integer.toString(RenameFormat.increment()));
					buf.append(tmp);
					i=syn.lastIndexOf('#');
					break;
				case 'P':
				//Full path with trailing separator
					buf.append(path.substring(firstSlash, lastSlash+1));
					break;
				case 'F':
				//file name with extension
					if ((i+1) < syn.length() && syn.charAt(i+1)=='(') {
						final String[] sTmp = syn.substring(i+2,syn.indexOf(')',i+1)).split(",");
						if (sTmp.length==2) {
							if (!do_str.isNaN(sTmp[0])&&!do_str.isNaN(sTmp[1])) {
								begin = Integer.parseInt(sTmp[0]);
								end = Integer.parseInt(sTmp[1]);
								if (begin < 0)
									begin = fullName.length() - 1 + begin;
								if (end < 0)
									end = fullName.length() + end;
								if (end < begin)
									throw new ParseException("End index is before begin index.");
								if (end < 0)
									throw new ParseException("End index comes before the start of the filename by "+(0-end)+".");
								if (begin < 0)
									throw new ParseException("Begin index comes before the start of the filename by "+(0-begin)+".");
								buf.append(fullName.substring(begin, end));
							}
						} else {
							if (!do_str.isNaN(sTmp[0])) {
								begin = Integer.parseInt(sTmp[0]);
								if (begin < 0)
									begin = fullName.length() - 1 + begin;
								if (begin < 0)
									throw new ParseException("Begin index comes before the start of the filename by "+(0-begin)+".");
								buf.append(fullName.substring(begin));
							}
						}
						i=syn.indexOf(')',i+1);
						if (end==-1)
							throw new ParseException("Missing matching ')' at " + begin);
					} else {
						buf.append(fullName);
					}
					break;
				case 'M':
				//move up directory
					final String[] sTmp = path.split("\\\\");
					end = 1;
					if (syn.charAt(i+1)=='(') {
						if (!do_str.isNaN(syn.substring(i+2,syn.indexOf(')',i+1))))
							end = Integer.parseInt(syn.substring(i+2, syn.indexOf(')',i+1)));
						i=syn.indexOf(')',i+1);
						if (end==-1)
							throw new ParseException("Missing matching ')' at " + begin);
					}
					for(int ind = 0;ind<(sTmp.length-(end+1));ind++) {
						buf.append(sTmp[ind]+"\\");
					}
					break;
				case 'E':
				//file extension
					buf.append(ext);
					break;
				default:
					buf.append(syn.charAt(--i));
				}
				//System.out.println(buf.toString());
			} else {
				buf.append(syn.charAt(i));
			}
		}
		if(RenameFormat.fURISAFE){
			String tmp=buf.toString().toLowerCase();
			tmp=tmp.replace(' ','-');
			String tmp2=tmp.substring(tmp.lastIndexOf('.'));
			tmp=tmp.substring(0,tmp.length()-tmp2.length());
			tmp=tmp.replace('.','-').replace("--","-");
			
			return tmp+tmp2;
		}
		return buf.toString();
	}
	private static String getDir(final String path, final int i) {
		String[]x = null;
		if (path.indexOf('\\')!=-1)
			x = path.split("\\\\");
		else
			x = path.split("/");
		return x[x.length-i-1];
	}
	public void setFormat(final String format) {
		canundo = false;
		destName = null;
		syn = format;
	}
	public void setFile(final File f) {
		canundo = false;
		destName = null;
		file = f;
	}
	public int undo() {
		if (!canundo) return (errorNum=RenameFormat.CANTUNDO);
		if (!undo.exists()) return (errorNum=RenameFormat.BADSOURCE);
		if (file.exists()) return (errorNum=RenameFormat.BADTARGET);
		file.getParentFile().mkdirs();
		undo.renameTo(file);
		canundo = false;
		return (errorNum=RenameFormat.OK);
	}
	public int getErrorNum() {
		return errorNum;
	}
	/**Gets a user friendly description of the error
	 * @return the error string
	 */
	public String getError() {
		if (errorNum>=0&&errorNum<RenameFormat.ERR.length) {
			if (errorNum==RenameFormat.PARSEEXCEPTION) {
				return RenameFormat.ERR[errorNum]+pe.toString();
			} else {
				return RenameFormat.ERR[errorNum];
			}
		} else {
			return "No error.";
		}
	}
	/**Gets a user friendly description of the error
	 * @param errorNum Error number
	 * @return the error string
	 */
	public String getError(final int errorNum) {
		if (errorNum>=0&&errorNum<RenameFormat.ERR.length) {
			if (errorNum==RenameFormat.PARSEEXCEPTION) {
				return RenameFormat.ERR[errorNum]+pe.toString();
			} else {
				return RenameFormat.ERR[errorNum];
			}
		} else {
			return "No error.";
		}
	}
	/** Gets the undo value.
	 * @return The undo value or null if this has not been run.
	 */
	public String toStringTarget() {
		if (undo==null) return null;
		return undo.getAbsolutePath();
	}
	/** Gets the current value.
	 * @return The current value
	 */
	@Override
	public String toString() {
		if (file!=null)
			return file.getAbsolutePath();
		else
			return "";
	}
	public String saveState() {
		final StringBuffer buf = new StringBuffer();
		buf.append(file+";");
		buf.append(undo+";");
		buf.append(syn+";");
		buf.append(pe+";");
		buf.append(errorNum+"\n");
		return buf.toString();
	}
	public static RenameFormat loadState(final String state) throws ParseException {
		final String[] args = state.split(";");
		if (args.length!=5)
			throw new ParseException("Invalid number of parameters. Expected 5.");
		final RenameFormat temp = new RenameFormat();
		temp.setFile(new File(args[0]));
		if (!"null".equals(args[1])) {
			temp.undo = new File(args[1]);
			temp.canundo = true;
		}
		temp.setFormat(args[2]);
		temp.pe = new ParseException(args[3]);
		temp.errorNum = Integer.parseInt(args[4]);
		return null;
	}
	public static void main(final String[] args){
		RenameFormat.fRESOLVEURLESCAPED=true;
		final RenameFormat test=new RenameFormat();
		test.setFormat("$D\\$N$E");
		test.setFile(new File("D:\\Apps\\MediaSnatcher4\\thepiratebay.org\\%5B3DS%5DLEGO_Star_Wars_III_3DS%5BLEEFNDSSTUFF.TK%5D.6663504.TPB.torrent"));
		System.out.println(test.getError(test.mockRename()));
		System.out.println(test.toString());
		System.out.println(test.toStringTarget());
	}
}