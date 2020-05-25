package simple.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import simple.util.logging.Log;
import simple.util.logging.LogFactory;

public class CharBuffer implements CharSequence{
	protected static final Log log = LogFactory.getLogFor(CharBuffer.class);
	private final Reader input;
	private final StringBuilder buf= new StringBuilder();
	private int markStart= 0, markEnd= 0, position= 0, fill= 2048;
	public CharBuffer(Reader input){
		this.input= input;
	}
	public void clear(){
		log.debug("Clearing. Unread: ", this.unreadToString());
		buf.setLength(0);
		markStart= 0;
		markEnd= 0;
		position= 0;
	}
	public void clearRead(){
		if(position > 0){
			log.debug("Clearing read: ", this.readToString());
			if(position == buf.length()){
				buf.setLength(0);
			}else{
				buf.delete(0, position);
			}
		}
		position= 0;
	}
	public void apend(char c){
		buf.append(c);
	}
	public void append(String s){
		buf.append(s);
	}
	public int position(){
		return position;
	}
	/**
	 * Number of unread characters left
	 * @return
	 */
	public int remaining(){
		return buf.length() - position;
	}
	/**
	 * Number of characters in the buffer
	 * @return
	 */
	@Override
	public int length(){
		return buf.length();
	}
	public int find(char str){
		return find(str, 0);
	}
	public int find(String str){
		return find(str, 0);
	}
	public int findAny(String str){
		return findAny(str, 0);
	}
	public int find(char str, int offset){
		int idx= buf.indexOf("" + str, position + offset);
		if(idx == -1){
			if(!fill(str)){
				return -1;
			}
			idx= buf.indexOf("" + str, position + offset);
		}
		return idx;
	}
	/**
	 * Searches for the string. Filling the buffer and searching again if not found
	 * @param str
	 * @return
	 */
	public int find(String str, int offset){
		int idx= buf.indexOf(str, position + offset);
		if(idx == -1){
			if(!fill(str, false)){
				return -1;
			}
			idx= buf.indexOf(str, position + offset);
		}
		return idx;
	}
	/**
	 * Searches for any character in the string. Filling the buffer and searching again if not found
	 * @param str
	 * @return
	 */
	public int findAny(String str, int offset){
		int idx= -1;
		for(char c : str.toCharArray()){
			idx= buf.indexOf(""+c, position + offset);
			if(idx > -1){
				break;
			}
		}
		if(idx == -1){
			if(!fill(str, true)){
				return -1;
			}
			for(char c : str.toCharArray()){
				idx= buf.indexOf(""+c, position + offset);
				if(idx > -1){
					break;
				}
			}
		}
		return idx;
	}
	public boolean endsWith(String str){
		return buf.indexOf(str, buf.length() - str.length()) != -1;
	}
	public boolean startsWith(String str){
		return buf.lastIndexOf(str, position) == position;
	}
	public int getMarkStart(){
		return markStart;
	}
	public int getMarkEnd(){
		return markEnd;
	}
	public void markStart(){
		markStart= position;
	}
	public void markEnd(){
		markEnd= position;
	}
	public void markStart(int position){
		markStart= position;
	}
	public void markEnd(int position){
		markEnd= position;
	}
	public String getMarked(){
		return buf.substring(markStart, markEnd);
	}
	public boolean isEmpty(){
		return position == buf.length();
	}
	@Override
	public char charAt(int position){
		return buf.charAt(position);
	}
	public char lastChar(){
		return buf.charAt(buf.length() - 1);
	}
	public char next(){
		return buf.charAt(position + 1);
	}
	public char next(int offset){
		return buf.charAt(position + offset);
	}
	public char prev(){
		return buf.charAt(position - 1);
	}
	public char prev(int offset){
		return buf.charAt(position - offset);
	}
	/**
	 * character at the current position
	 * @return
	 */
	public char current(){
		return buf.charAt(position);
	}
	/**
	 * Returns the current character and advances the position
	 * Fills the buffer if needed
	 * @return
	 * @throws EOFException
	 */
	public char read() throws EOFException{
		if(isEmpty()){
			if(!fill()){
				throw new EOFException();
			}
		}
		return buf.charAt(position++);
	}
	/**
	 * rewinds and sets the current character
	 * @param c
	 * @return false if it can't rewind
	 */
	public boolean unread(char c){
		if(position == 0){
			return false;
		}
		buf.setCharAt(--position, c);
		return true;
	}
	public void push(char c){
		buf.insert(position, c);
	}
	/**
	 * Moves to the position
	 * @param position
	 * @return false if out of range
	 */
	public boolean moveTo(int position){
		if(position < 0 || position > buf.length()){
			return false;
		}
		this.position= position;
		return true;
	}
	/**
	 * Increment position by one
	 * @return
	 */
	public boolean skip(){
		if(isEmpty()){
			if(!fill()){
				return false;
			}
		}
		position+= 1;
		return true;
	}
	/**
	 * Increment position by <var>amt</var>
	 * @param amt
	 * @return
	 */
	public boolean skip(int amt){
		while(position + amt == buf.length()){
			if(!fill()){
				return false;
			}
		}
		position+= amt;
		return true;
	}
	/**
	 * Decrement position by <var>amt</var>
	 * @param amt
	 * @return
	 */
	public boolean rewind(int amt){
		if(amt > position){
			return false;
		}
		position-= amt;
		return true;
	}
	/**
	 * Decrement position by 1
	 * @return
	 */
	public boolean rewind(){
		if(position == 0){
			return false;
		}
		position-= 1;
		return true;
	}
	public boolean fillUntil(String str){
		return fill(str, false);
	}
	public boolean fillUntilAny(String str){
		return fill(str, true);
	}
	public boolean fillUntil(char str){
		return fill(str);
	}
	private boolean fill(String until, boolean any){
		try{
			String read= any ? RWUtil.readUntilAny(input, until) : RWUtil.readUntil(input, until);
			if(read.isEmpty()){
				log.debug("EOF on fill");
				return false;
			}
			log.debug("fill until" + (any ? " any" : "") + " " + until, read);
			buf.append(read);
		}catch(IOException e){
			log.error(e);
			return false;
		}
		return true;
	}
	private boolean fill(char until){
		try{
			String read= RWUtil.readUntil(input, until);
			if(read.isEmpty()){
				log.debug("EOF on fill");
				return false;
			}
			log.debug("fill until " + until, read);
			buf.append(read);
		}catch(IOException e){
			log.error(e);
			return false;
		}
		return true;
	}
	private boolean fill(){
		try{
			String read= RWUtil.readUntil(input, fill);
			if(read.isEmpty()){
				log.debug("EOF on fill");
				return false;
			}
			log.debug("fill until length " + fill, read);
			buf.append(read);
		}catch(IOException e){
			log.error(e);
			return false;
		}
		return true;
	}
	public String readToString(){
		return buf.substring(0, position);
	}
	public String unreadToString(){
		return buf.substring(position);
	}
	@Override
	public String toString(){
		return buf.toString();
	}
	@Override
	public CharSequence subSequence(int start,int end){
		return buf.subSequence(start, end);
	}



}
