/* µCOMP:javac -classpath ".." %sµ */
/* µEXEC:cmd /K java -classpath .. XML.XMLDocµ */
package simple.xml;

import java.io.*;
import java.net.*;

/**
 * <p>Title: XMLDoc</p>
 * <p>Description: Simple Generic XML Parser</p>
 * <br>Created: 2004
 * @author Kenneth Pierce
 * @version 2.1
 * Note that this version only supports tag attributes self-closing tags and tags with sub-tags.
 * Next release will support tags with text contents.
 * <b>Does not validate!!!</b>
 * @deprecated 11/2010 Replaced by {@link simple.ml.InlineLooseParser}
 */
final public class XMLDoc {
	URL Tfile;
	Root doc = new Root("");
	boolean busy = false;
	protected XMLDoc() {}
	/**
	 * 
	 * @param file
	 * @throws MalformedURLException
	 */
	public XMLDoc(String file) throws MalformedURLException {
		this(new File(file));
	}
	/**
	 * 
	 * @param file
	 * @throws MalformedURLException
	 */
	public XMLDoc(File file) throws MalformedURLException {
		this(file.toURI().toURL());
	}
	/**
	 * 
	 * @param file
	 */
	public XMLDoc(URL file) {
		if (file == null)
			throw new IllegalArgumentException("File cannot be null.");
		Tfile = file;
	}

//Function that does it all... with plenty of help o.O
	/**
	 * This is the big function that parses the set XML file.
	 */
	public void parse() throws IOException {
		busy = true;
		StringBuffer tmp = new StringBuffer();
		String temp = null;
		try {
			BufferedInputStream in = new BufferedInputStream(Tfile.openStream());
			int b = 0;
			while((b=in.read())!=-1) {
				tmp.append((char)b);
			}
			in.close();
			//System.out.println(tmp);
			temp = new String(tmp);
			tmp = null;
				if (temp.indexOf("?>")!=-1) {temp=temp.substring(temp.lastIndexOf("?>"),temp.length());}
				int off = 0;
//				int lev = levelNum(temp,1);
				off = temp.indexOf("<", off);
				int off2 = temp.indexOf(">",off);
				doc.addSub(temp.substring(off,off2));
				if (temp.charAt(off2-1)=='/') {
					//doc.addSub(temp.substring(off,off2));
				} else {
					doc.getSub(0).addSub(parse(temp,temp.substring(off2+1,temp.indexOf("</"+getTag(temp,off,off2)+">")),2));
				}
		} catch(IOException e) {
			busy = false;
			throw e;
		}
		busy = false;
	}

//Recursive function to get the subtags of subtags... recursive, BLECH >.<
	/**
	 * Recursive function that parses subtags of subtags. recursive, BLECH >.<
	 * @param temp The full unparsed tag with attributes
	 * @param tag Name of the tag being parsed
	 * @param level The current depth
	 * @return An array sub the found sub elements
	 */
	private Sub[] parse(String temp, String tag,int level) {
		int off = 0, off2 = 0;
		int lev = levelNum(tag, 1);
		Sub[] sub = new Sub[lev];
		for (int i = 0;i < lev; i++) {
			off = tag.indexOf("<",off);
			if (off==-1) {return sub;}
			if (tag.charAt(off+1)=='!') return sub;
			off2 = tag.indexOf(">",off);
			sub[i] = new Sub(tag.substring(off,off2));
			if (tag.charAt(off2-1)=='/') {
				sub[i].addSub(tag.substring(off,off2));
			} else {
				if (tag.indexOf("</"+getTag(tag,off,off2)+">")<0)//added
					sub[i].addSub(tag.substring(off,off2));//added
				else//added
					sub[i].addSub(parse(temp,tag.substring(off2+1,tag.indexOf("</"+getTag(tag,off,off2)+">")),level+1));
			}
			if (tag.indexOf("</"+getTag(tag,off,off2)+">")==-1) {
				off = off2;
			} else {
				off = tag.indexOf("</"+getTag(tag,off,off2)+">")+("</"+getTag(tag,off,off2)+">").length();
			}
		}
		return sub;
	}

//Retrieve the tag's name only for finding the end tag
	/**
	 * Used by parse() function.
	 * @param temp The whole tag(all the ugly contents).
	 * @param off start index
	 * @param off2 end index
	 * @return Tag's name
	 */
	private String getTag(String temp, int off, int off2) {
		char[] p = temp.toCharArray();
		temp = new String("");
		for (int i = off;i < off2; i++) {
			switch (p[i]) {
				case '>':
				case '/':
				case ' ':
					return new String(temp);
				case '\t':
				case '\n':
				case '\r':
				case '<':
				case '=':
				case '\"':
				case '\'':
				break;
				default:
					temp += p[i];
			}
		}
		return new String(temp);
	}

//used to count the number of 'level' elements there are
	/**
	 * Used by parse() function
	 * @param tag raw tag(from source file)
	 * @param level
	 * @return number of tags in <var>tag</tag> at depth <var>level</var>
	 */
	private int levelNum(String tag, int level) {
		char[] p = tag.toCharArray();
		int count = 0;
		int lev = 1;
		boolean open = false;
		for (int i = 0;i < p.length;i++) {
			switch(p[i]) {
				case '/':
					if ((p[i+1]=='>')){lev--;open=false;}
				break;
				case '<':
					if (p[i+1]=='/') {lev--;}else if(!open) {
						if (lev==level){count++;}
						lev++;
						open=true;
					}
				break;
				case '>':
					open=false;
				break;
			}		}
		return count;
	}

//Don't want people to retrieve information while we are still parsing a large file!
	/**
	 * @return True if parse() function is still working, false otherwise.
	 */
	public boolean isParsing() {return busy;}
	/**
	 * Blocks until parse() function is done working.
	 */
	public void waitFor() {
		while (isParsing()) ;
	}

//debugging purposes only
	/**
	 * Prints the current document in an easy to read format.<br>
	 * For debuging purposes only.
	 */
	public String print() {
		waitFor();
		busy = true;
		String temp = new String();
		temp = doc.getTag() + "\n";
		Root root = doc;
		temp += tagTree(root,1);
		System.out.print(temp);
		busy = false;
		return temp;
	}

//recursive function to print the subtags of the subtags. again, BLECH X.X
	/**
	 * Recursive fuction used by print().
	 * @param sub Current node
	 * @param nest Depth level
	 * @return String containing tag attributes and subtags
	 */
	private String tagTree(Root sub, int nest) {
		String temp = "";
		for (int i = 0;i<sub.getSubCount();i++) {
			for (int j = 0;j<nest;j++) {temp += "| ";}
			temp += "+" + sub.getSub(i).getTag() + " : " + sub.getSubCount() + "\n";
			for (int j = 0;j<nest;j++) {temp += "| ";}
			temp += " +Attrs: "+sub.getSub(i).getParamCount();
			for (int j = 0; j < sub.getSub(i).getParamCount(); j++) {temp += " " + sub.getSub(i).getParamName(j) + "=" + sub.getSub(i).getParamValue(j);}
			temp += "\n" + tagTree(sub.getSub(i),nest+1);
		}
		return temp;
	}
	/**
	 * Recursively searches <var>sub</var> for <var>tag</var> skipping <var>offset</var> similar tags.
	 * @param tag Tag to find.
	 * @param sub Tag to start search
	 * @param offset Number of similar tags to skip
	 * @return The tag if found, null otherwise.
	 */
	private Root findSub(String tag, Root sub, int offset) {
		if (tag == null)
			System.err.println("NULL tag!");
		if (sub == null)
			System.err.println("NULL sub!");
		for (int i = 0;i<sub.getSubCount();i++) {
			Sub x;
			if (sub.getSub(i).getTag().equals(tag)) {
//				for (
				if (offset==0) {
					return sub.getSub(i);
				} else {
					offset--;
				}
			}
			x = (Sub)findSub(tag,(Root)sub.getSub(i), offset);
			if (x==null) {continue;}
			if (x.getTag().equals(tag)) {
					return x;
			}
		}
		return null;
	}
	/**
	 * Follows the tree specified in <var>tree</var> and gets <var>param</var>.<br>
	 * Format of tree:<br>
	 * tagName;index/subtagName;index<br>
	 * where tagName is non-null and index&gt;=0.<br>
	 * Example:<br>
	 * <code>getParam("mime;0/text;3/type;0", "mimeType");</code><br>
	 * The above can also be written as:<br>
	 * <code>getParam("mime/text;3/type", "mimeType");</code>
	 * @param tree Tree to follow
	 * @param param Parameter to get
	 * @return Value of <var>param</var> if found, null otherwise
	 */
	public String getParam(String tree, String param) {
		//format: tag;#/tag;#/ect...
		waitFor();
		String[] tre = tree.split("/");
		String[] tmp = null;
		int index = 0;
		Sub sub = doc.getSub(0);
		for (int i = 0;i<tre.length;i++) {
			tmp = tre[i].split(";");
			if (tmp.length==0||(tmp.length==1&&tmp[1]=="")) {
				index = 0;
			} else {
				index = Integer.parseInt(tmp[1]);
			}
			sub = (Sub)findSub(tmp[0], sub, index);
		}
		if (sub==null)
			return null;
		else
			return sub.getParam(param);
	}
	/**
	 * Recursively searches the main document for <var>tag</var> to get
	 * the value <var>param</var> skipping <var>offset</var> <var>tag</var>s.
	 * @param tag Tag to find.
	 * @param param Attribute of tag to get.
	 * @param offset Number of <var>tag</var>s to skip.
	 * @return The value of <var>param</var> or null if <var>tag</var> or <var>param</var> isn't found.
	 */
	public String getParam(String tag, String param, int offset) {
		//new format!(for tag string) #.#/sub.#/sub.#/sub.etc
		waitFor();
		Sub b = (Sub)findSub(tag,doc,offset);
		if (b==null) return "";
		/*String tags[] = tag.split(".");
		for (int i = 0;i<tags.length;i++) {
use above format to finish this
		}*/
//new debug("getParam","b=" + b);
/*		if (offset<0||offset>b.getSubCount()) {
			return "";
		} else {
			return b.getSub(tag, offset).getParam(param);
		}*/
		return b.getParam(param);
		//This will be expanded in future versions to better suit my needs
	}
	/**
	 * Recursively searches the document for <var>tag</var> skipping <var>offset</var> similar tags.
	 * @param tag
	 * @param offset
	 * @return The number of subtags <var>tag</var> has. -1 if <var>tag</var> isn't found.
	 */
	public int getSubNum(String tag,int offset) {
		waitFor();
		/*
			doc.getSubCount();//old
		*/
		Root sub = findSub(tag, doc, offset);//doc.getSub(tag,offset);
		if (sub==null)
			return -1;
		return sub.getSubCount();
	}
}

//Classes to handle XML nodes and such
/**
 * Provides basics.
 * @author KP
 *
 */
class Root {
	private int subCount = 0;
	private String tag;
	private String[][] attr;
	private Sub[] sub = null;
	private String innards;
	private boolean selfClose = false;
	protected Root() {}
	/**
	 * Parses tag name and attributes.
	 * @param tag Raw tag data.
	 */
	public Root(String tag) {
//Get only what we want(tag name and params). I told ya this was a simple parser :)
//it can't even handle innerdata yet, and probably never will unless someone finally
//decides that donations make it possible!
		tag = tag.trim();
//new debug("Root Con","tag="+tag);
		this.tag = tag.substring((tag.startsWith("<"))?1:0,tag.length());
		if (tag.equals("")) {return;}
		if (tag.endsWith("/>")||tag.endsWith("/")) {
			this.tag = this.tag.substring(0,this.tag.indexOf(' '));
		} else {
			this.tag = this.tag.substring(0,(this.tag.indexOf(' ')==-1)?this.tag.length():this.tag.indexOf(' '));
		}
		this.tag = this.tag.trim();
		{
			char[] x = tag.toCharArray();
			boolean open = false;
			for (int i = 0;i < x.length; i++) {
				if (x[i] == '\"') {open = (open)?false:true;}
				if ((x[i] == ' ')&&!(open)) {x[i]='ÿ';}
			}
			tag = new String(x);
		}
		String[] temp = tag.split("ÿ");
		if (temp.length>1) {
			attr = new String[temp[temp.length-1].equals("/")?temp.length-2:temp.length-1][2];
			for(int i = 0;i<attr.length;i++) {
				attr[i] = temp[i+1].split("=");
//new debug("Root Con","i="+i);
//new debug("Root Con ATTRs","max="+max);
//new debug("Root Con ATTRs","attr[i][0]="+attr[i][0]);
			//if (attr[i-1][1]==null) {continue;}
				attr[i][1]=attr[i][1].substring(1,(attr[i][1].endsWith("/"))?attr[i][1].length()-2:attr[i][1].length()-1);
			}
		} else {attr=new String[0][0];}
		if (((temp[temp.length-1].equals("/>"))||(temp[temp.length-1].equals("/")))) {selfClose = true;}
/*		for (int i = 0;i<attr.length;i++) {
new debug("ATTR Test",attr[i][0]+"="+attr[i][1]);
		}*/
	}

	/**
	 * @return true if the tag is self closing. i.e. &lt;tag param="125" /&gt;
	 */
	public boolean isSelfClosed() {return selfClose;}//used to avoid a bug that I cannot seem to fix any other way
//adds a sub to the parent node
	/**
	 * Adds <var>sub</var> to child list.<br>
	 * Does nothing if isSelfClosed is true.
	 * @param sub
	 */
	public void addSub(Sub sub) {
		if (isSelfClosed()) {return;}
		if (sub==null) {return;}
		sub.setRoot(this);
		if (this.sub==null) {
			this.sub = new Sub[1];
			this.sub[0] = sub;
		} else {
			Sub[] temp = new Sub[this.sub.length+1];
			System.arraycopy(this.sub,0,temp,0,this.sub.length);
			this.sub = new Sub[this.sub.length+1];
			temp[temp.length-1] = sub;
			System.arraycopy(temp,0,this.sub,0,this.sub.length);
		}
		subCount++;
	}
	/**
	 * Calls <code>addSub(new Sub(<var>tag</var>, this))</code>.<br>
	 * Does nothing if isSelfClosed is true.
	 * @param tag
	 */
	public void addSub(String tag) {
		if (isSelfClosed()) {return;}
		addSub(new Sub(tag, this));
	}
	public void addSub(Sub[] sub) {
		if (isSelfClosed()) {return;}
		for (int i = 0;i<sub.length;i++) {
			this.addSub(sub[i]);
		}
	}
//get/set
	public int getSubCount() {
		return subCount;
	}
	public String getTag() {
		return tag;
	}
	/**
	 * Searches the Root's subs for <var>tag</var>.
	 * @param tag
	 * @param offset
	 * @return Found Sub element
	 */
	public Sub getSub(String tag,int offset) {
		boolean found = false;
		for (;offset<sub.length-1;offset++) {
			if (sub[offset].getTag().equals(tag)){found = true;break;}
		}
		if (!found) {
			for (;offset<sub.length-1;offset++) {
				if (sub[offset].getSub(tag, offset).equals(tag)){found = true;break;}
			}
		}
		return sub[offset];
	}
	public Sub getSub(int index) {
		if (index<0||index>=sub.length) {
			throw new IndexOutOfBoundsException("index "+index+" out of range. Max: "+(sub.length-1));
		} else {
			return sub[index];
		}
	}
	public void setContent(String s) {
		innards = s;
	}
	public String getContent() {
		return innards;
	}
	public String getParam(String param) {
		for (int i = 0; i < attr.length; i++) {
			if (attr[i][0].equals(param)) {return attr[i][1];}
		}
		return "";
	}
	public String getParamValue(int index) {
		if (index<0||index>attr.length-1) {
			return "";
		} else {
			return attr[index][1];
		}
	}
	public String getParamName(int index) {
		if (index<0||index>attr.length-1) {
			return "";
		} else {
			return attr[index][0];
		}
	}
	public int getParamCount() {
		return attr.length;
	}
}
//Extension of the Root class to handle subtags that can also be nodes
final class Sub extends Root {
	private Root root = null;
	public Sub(String tag, Root root) {
		super(tag);
		this.root = root;
	}
	public Sub(String tag) {
		super(tag);
	}
	/**
	 * Takes a Root object and creates a Sub object from it.
	 * @param cast
	 * @return Root as a Sub
	 */
	public static Sub cast(Root cast) {
		if (cast instanceof Sub) return (Sub)cast;
		String x = "<"+cast.getTag();
		for (int i = 0;i<cast.getParamCount();i++) {
			x += " " + cast.getParamName(i) + "=\"" + cast.getParamValue(i) + "\"";
		}
		if (cast.isSelfClosed()) {x += " /";}
		Sub b = new Sub(x);
		for (int i = 0;i<cast.getSubCount();i++) {
			b.addSub(cast.getSub(i));
		}
		return b;
	}
	public Root getRoot() {
		return root;
	}
	public void setRoot(Root root) {
		this.root = root;
	}
}