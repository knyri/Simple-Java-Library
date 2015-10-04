package simple.io;

import java.io.File;
import java.util.LinkedList;

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
	public static final int
		OK = 0,
		BADTARGET = 1,
		BADSOURCE = 2,
		CANTUNDO = 3,
		PARSEEXCEPTION = 4,
		IOERRORMKDIR = 5,
		IOERRORRENAME = 6;
	//Error messages
	private static final String[] ERR = new String[] {"OK", "Destination already exist", "Source doesn't exist", "Can't Undo: Hasn't been renamed.", "Parse Exception: ", "System error making dirs", "System error renaming"};
	public static boolean fRESOLVEURLESCAPED=false;
	/**
	 * URI safe filename
	 */
	public static boolean fURISAFEF=false;
	/**
	 * URI safe directory
	 */
	public static boolean fURISAFED=false;
	private File
		undo = null;
	private boolean canundo = false;
	private String
		destName = null;
	private ParseException pe = null;
	private int errorNum = -1;
	private static final int
		TRANS_PATH_UC=1,
		TRANS_PATH_UCF=2,
		TRANS_PATH_LC=4,
		TRANS_FILE_UC=8,
		TRANS_FILE_UCF=16,
		TRANS_FILE_LC=32;
	private final State state= new State();
	private final Formatter format= new Formatter();
	public RenameFormat(final File fil, final String syntax) throws ParseException {
		state.setPath(fil);
		setFormat(syntax);
	}
	public synchronized void setNumber(final int i) {
		state.setNumber(i);
	}
	public int rename() {
		File file= state.getFile();
		if (!file.exists()) return (errorNum=RenameFormat.BADSOURCE);
		if (destName==null) {
			try {
				destName = parse();
			} catch (final ParseException e) {
				pe = e;
				return (errorNum=RenameFormat.PARSEEXCEPTION);
			}
		}
		final int tmp = destName.lastIndexOf(File.separator);
		if (tmp > 0) {
			undo = new File(destName.substring(0, tmp+1));
			undo.mkdirs();
		}
		undo = new File(destName);
		if (undo.exists() && !file.equals(undo)) {return (errorNum=RenameFormat.BADTARGET);}
		if (!file.renameTo(undo)) {return (errorNum=RenameFormat.IOERRORRENAME);}
		canundo = true;
		return (errorNum=RenameFormat.OK);
	}
	public int mockRename() {
		File file= state.getFile();
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
		if (undo.exists() && !file.equals(undo)) {return (errorNum=RenameFormat.BADTARGET);}
		return (errorNum=RenameFormat.OK);
	}
	public String parse() throws ParseException {

		String fullName= format.format(state);
		if(RenameFormat.fRESOLVEURLESCAPED){
			//Resolve escaped URL characters
			final StringBuilder resolved=new StringBuilder(fullName.length());
			int lend=0;
			for(int i=0;i<fullName.length();i++){
				switch(fullName.charAt(i)){
				case '-':
				case '_':
					//dashes and underscores to spaces
					resolved.append(' ');
					break;
				case '%':
					lend=i+3;
					i++;
					if((i+2)>fullName.length()){
						//Last is malformed, skip it
						resolved.append('%');
						continue;
					}
					if(fullName.charAt(lend)=='%')
						while(fullName.charAt(lend+3)=='%'){
							//check for and append back to back encoded characters
							lend+=3;
							if((lend+3)>fullName.length())break;
						}
					//resolve all found
					resolved.append(new String(HexUtil.fromHex(fullName.substring(i,lend).replace("%",""))));
					i=lend-1;
					break;
					default:
						resolved.append(fullName.charAt(i));
				}
			}
			fullName=resolved.toString();
		}

		return fullName;
//System.out.println(dir+" --- "+file);

	}
	public void setFormat(final String f) throws ParseException {
		canundo = false;
		destName = null;
		format.parse(f);
	}
	public void setFile(final File f) {
		canundo = false;
		destName = null;
		state.setPath(f);
	}
	public int undo() {
		if (!canundo) return (errorNum=RenameFormat.CANTUNDO);
		if (!undo.exists()) return (errorNum=RenameFormat.BADSOURCE);
		/*
		 * !file.equals(undo) is a check for case insensitive file systems.
		 * Relying on the fact that file.equals(undo) will be true on case insensitive
		 * systems and false on case sensitive systems if the only change was a
		 * case transformation..
		 */
		File file= state.getFile();
		if (file.exists() && !file.equals(undo)) return (errorNum=RenameFormat.BADTARGET);
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
	 * @return The undo value or null if this has not been run or an exception occured.
	 */
	public String toStringTarget() {
		if (undo==null){
			try{
				return parse();
			}catch(ParseException e){
				return null;
			}
		}
		return undo.getAbsolutePath();
	}
	/** Gets the current value.
	 * @return The current value
	 */
	@Override
	public String toString() {
			return state.getFile().getAbsolutePath();
	}
	private static class State{
		int number=0;
		File file= null;
		String[] path;
		String filename;
		String fullfilename;
		String ext;
		public File getFile(){
			return file;
		}
		public void setNumber(int n){
			number= n;
		}
		/** Returns the current number and increments it
		 * @return
		 */
		public int increment(){
			return number++;
		}
		public void setPath(File path){
			if(path==null)return;
			if(!path.isAbsolute())path=path.getAbsoluteFile();
			file=path;
			if(path.isDirectory()){
				this.path=path.getAbsolutePath().split("\\Q"+File.separator+"\\E");
				filename="";
				fullfilename="";
				ext="";
			}else{
				this.path=path.getParentFile().getAbsolutePath().split("\\Q"+File.separator+"\\E");
				fullfilename=path.getName();
				int dot=fullfilename.lastIndexOf('.');
				if(dot==-1){
					filename=fullfilename;
					ext="";
				}else{
					filename=fullfilename.substring(0,dot);
					ext=fullfilename.substring(dot+1);
				}
			}
		}
		public String getExt(){
			return ext;
		}
		public String getFullFilename(){
			return fullfilename;
		}
		public String getFilename(){
			return filename;
		}
		/** Gets the directory N places from the END of the path
		 * For "C:\Games\Doom\doom.exe"
		 * $D(0) returns Doom
		 * $D(1) returns Games
		 * @param n
		 * @return
		 */
		public String getDirN(int n){
			if(n>=path.length)return null;
			while(n<0)n+=path.length;
			return path[path.length-n-1];
		}
		/** Gets the path to directory N places from the END of the path
		 * For "C:\Games\Doom\doom.exe"
		 * $M(0) returns C:\Games\Doom\
		 * $M(1) returns C:\Games\
		 * @param n
		 * @return
		 */
		public String getPathN(int n){
			if(n>=path.length)return null;
			while(n<0)n+=path.length;
			n=path.length-n;
			StringBuilder b= new StringBuilder(255);
			for(int i=0; i<n; i++)
				b.append(path[i]+File.separator);
			return b.toString();
		}
	}
	private static class Formatter {
		LinkedList<Token> tokens= new LinkedList<Token>();
		int transformations=0;
		public String format(State state) throws ParseException{
			StringBuilder buf= new StringBuilder(255);
			for(Token token: tokens){
				buf.append(token.parse(state));
			}
			String dir, file;
			int i= buf.lastIndexOf(File.separator);
			dir= buf.substring(0,i+1);
			file= buf.substring(i+1);
			if(RenameFormat.fURISAFED || RenameFormat.fURISAFEF){
				if(RenameFormat.fURISAFED)
					dir=dir.toLowerCase().replace(' ','-').replace('.','-');
				if(RenameFormat.fURISAFEF)
					file=file.toLowerCase().replace(' ','-');

				return (dir+file).replace("--","-");
			}else{
				if((transformations&TRANS_PATH_LC)==TRANS_PATH_LC){
					dir=dir.toLowerCase();
				}else if((transformations&TRANS_PATH_UC)==TRANS_PATH_UC){
					dir=dir.toUpperCase();
				}else if((transformations&TRANS_PATH_UCF)==TRANS_PATH_UCF){
					String[] dirp= state.path;
					StringBuilder dirb=new StringBuilder(dir.length());
					for(String dirt : dirp){
						dirb.append(do_str.capitalize(dirt)+File.separator);
					}
					dir=dirb.toString();
				}
				if((transformations&TRANS_FILE_LC)==TRANS_FILE_LC){
					file=file.toLowerCase();
				}else if((transformations&TRANS_FILE_UC)==TRANS_FILE_UC){
					file=file.toUpperCase();
				}else if((transformations&TRANS_FILE_UCF)==TRANS_FILE_UCF){
					file=do_str.capitalize(file);
				}
			}
			return buf.toString();
		}
		public void parse(String format) throws ParseException{
			int i=0,begin=0, end=0;
			transformations=0;
			//check for transformations
			if(format.charAt(0)=='&'){
				out:
				do{
					i++;
					switch(format.charAt(i)){
						case 'D':
							if(do_str.startsWith(format,"DUCF",i)){
								transformations+=TRANS_PATH_UCF;
								i+=4;
							}else if(do_str.startsWith(format,"DUC",i)){
								transformations+=TRANS_PATH_UC;
								i+=3;
							}else if(do_str.startsWith(format,"DLC",i)){
								transformations+=TRANS_PATH_LC;
								i+=3;
							}
						break;
						case 'F':
							if(do_str.startsWith(format,"FUCF",i)){
								transformations+=TRANS_FILE_UCF;
								i+=4;
							}else if(do_str.startsWith(format,"FUC",i)){
								transformations+=TRANS_FILE_UC;
								i+=3;
							}else if(do_str.startsWith(format,"FLC",i)){
								transformations+=TRANS_FILE_LC;
								i+=3;
							}
						break;
						default:
							//no transformations; reset index
							i--;
							break out;
					}
				}while(format.charAt(i)=='&');

			}
			StringBuilder literal= new StringBuilder(20);
			for (;i<format.length();i++) {
				// parse tokens
				if (format.charAt(i)=='$') {
					if(literal.length()>0){
						tokens.add(new Literal(literal.toString()));
						literal.setLength(0);
					}
					switch(format.charAt(++i)) {
					case 'D'://$M$D(1)$N.jpg
					//All or part of the path
						if ((i+1 < format.length()) && format.charAt(i+1)=='(') {
							begin = i+2;
							end = format.indexOf(')', i+1);
							if (end==-1)
								throw new ParseException("Missing matching ')' for D at " + begin);

							if (!do_str.isNaN(format.substring(begin, end)))
								tokens.add(new D(Integer.parseInt(format.substring(begin,end))));
							else
								throw new ParseException("Expected number in $D(#), got $D(" + format.substring(begin, end) + ") instead.");
							i = end;
						} else {
							tokens.add(new P());
						}
						break;
					case 'N':
					//fileName
						if ((i+1) < format.length() && format.charAt(i+1)=='(') {
							final String[] sTmp = format.substring(i+2,format.indexOf(')',i+1)).split(",");
							if (sTmp.length==2) {
								if (!do_str.isNaN(sTmp[0])&&!do_str.isNaN(sTmp[1])) {
									begin = Integer.parseInt(sTmp[0]);
									end = Integer.parseInt(sTmp[1]);
									tokens.add(new N(begin,end));
								}
							} else {
								if (!do_str.isNaN(sTmp[0])) {
									begin = Integer.parseInt(sTmp[0]);
									tokens.add(new N(begin,Integer.MAX_VALUE));
								}
							}
							i=format.indexOf(')',i+1);
							if (end==-1)
								throw new ParseException("Missing matching ')' for N at " + begin);
						} else {
							tokens.add(new N(0,Integer.MAX_VALUE));
						}
						break;
					case '#':
					//number format
						tokens.add(new Numbering(format.lastIndexOf('#')+1-i));
						i=format.lastIndexOf('#');
						break;
					case 'P':
					//Full path with trailing separator
						tokens.add(new P());
						break;
					case 'F':
					//file name with extension
						if ((i+1) < format.length() && format.charAt(i+1)=='(') {
							final String[] sTmp = format.substring(i+2,format.indexOf(')',i+1)).split(",");
							if (sTmp.length==2) {
								if (!do_str.isNaN(sTmp[0])&&!do_str.isNaN(sTmp[1])) {
									begin = Integer.parseInt(sTmp[0]);
									end = Integer.parseInt(sTmp[1]);
									tokens.add(new F(begin,end));
								}
							} else {
								if (!do_str.isNaN(sTmp[0])) {
									begin = Integer.parseInt(sTmp[0]);
									tokens.add(new F(begin,Integer.MAX_VALUE));
								}
							}
							i=format.indexOf(')',i+1);
							if (end==-1)
								throw new ParseException("Missing matching ')' for F at " + begin);
						} else {
							tokens.add(new F(0,Integer.MAX_VALUE));
						}
						break;
					case 'M':
					//move up directory
						end = 1;
						if ((i+1 < format.length()) &&format.charAt(i+1)=='(') {
							if (!do_str.isNaN(format.substring(i+2,format.indexOf(')',i+1))))
								end = Integer.parseInt(format.substring(i+2, format.indexOf(')',i+1)));
							i=format.indexOf(')',i+1);
							if (end==-1)
								throw new ParseException("Missing matching ')' for M at " + begin);
						}
						tokens.add(new M(end));
						break;
					case 'E':
					//file extension
						tokens.add(new E());
						break;
					default:
						literal.append('$'+format.charAt(i));
					}
					//System.out.println(buf.toString());
				}else{
					literal.append(format.charAt(i));
				}
			}//end for
			if(literal.length()>0)
				tokens.add(new Literal(literal.toString()));
		}
	}
	private static interface Token{
		public String parse(State state) throws ParseException;
	}
	private static class E implements Token {
		@Override
		public String parse(State state) throws ParseException{
			return state.getExt();
		}
	}
	private static class Literal implements Token {
		private final String literal;
		public Literal(String string){
			literal= string;
		}
		@Override
		public String parse(State state) throws ParseException{
			return literal;
		}

	}
	private static class P implements Token {
		@Override
		public String parse(State state) throws ParseException{
			return state.getPathN(0);
		}

	}
	private static class Numbering implements Token {
		private final int pad;
		public Numbering(int padding){
			pad= padding;
		}
		@Override
		public String parse(State state) throws ParseException{
			return do_str.padLeft(pad,'0',Integer.toString(state.increment()));
		}

	}
	/**
	 * Returns a substring of the filename, without the extension.
	 * Indices may be negative.
	 */
	private static class N implements Token {
		private final int s, e;
		/**
		 * @param start
		 * @param end if set to Integer.MAX_VALUE, it will be ignored
		 */
		public N(int start, int end){
			s= start;
			e= end;
		}
		@Override
		public String parse(State state) throws ParseException{
			int start= s, end= e;
			String fileName=state.getFilename();
			if(fileName.length()==0)return "";

			// Check for negative indices
			if (start < 0)
				start = fileName.length() + start;
			if (end < 0)
				end = fileName.length() + end;

			// Error checking
			if (end < start)
				throw new ParseException("End index is before start index.");
			if(end == start)
				throw new ParseException("Beginning and End cannot be equal.");
			if (end < 0)
				throw new ParseException("End index comes before the start of the filename by "+(0-end)+".");
			if (start < 0)
				throw new ParseException("Start index comes before the start of the filename by "+(0-start)+".");
			if(start >= fileName.length())
				throw new ParseException("Start index exceeds the length of the file name by "+(start-fileName.length())+".");

			if(end >= fileName.length())
				// be lenient with this error
				return fileName.substring(start);
			else
				return fileName.substring(start, end);
		}

	}
	/**
	 * Returns a substring of the filename, with the extension.
	 * Indices may be negative.
	 */
	private static class F implements Token {
		private final int s, e;
		/**
		 * @param start
		 * @param end if set to Integer.MAX_VALUE, it will be ignored
		 */
		public F(int start, int end){
			s= start;
			e= end;
		}
		@Override
		public String parse(State state) throws ParseException{
			int start= s, end= e;
			String fileName=state.getFullFilename();
			if(fileName.length()==0)return "";

			// Check for negative indices
			if (start < 0)
				start = fileName.length() + start;
			if (end < 0)
				end = fileName.length() + end;

			// Error checking
			if (end < start)
				throw new ParseException("End index is before start index.");
			if(end == start)
				throw new ParseException("Beginning and End cannot be equal.");
			if (end < 0)
				throw new ParseException("End index comes before the start of the filename by "+(0-end)+".");
			if (start < 0)
				throw new ParseException("Start index comes before the start of the filename by "+(0-start)+".");
			if(start >= fileName.length())
				throw new ParseException("Start index exceeds the length of the file name by "+(start-fileName.length())+".");

			if(end >= fileName.length())
				// be lenient with this error
				return fileName.substring(start);
			else
				return fileName.substring(start, end);
		}

	}
	/**
	 * Moves the file up N directories
	 */
	private static class M implements Token{
		private final int i;
		public M(int i){
			this.i=i;
		}
		@Override
		public String parse(State state){
			return state.getPathN(i);
		}
	}
	/**
	 * Returns the Nth directory from the END of the path
	 * For "C:\Games\Doom\doom.exe"
	 * $D(0) returns Doom
	 * $D(1) returns Games
	 */
	private static class D implements Token{
		private final int i;
		public D(int i){
			this.i=i;
		}
		@Override
		public String parse(State state){
			return state.getDirN(i);
		}

	}
}