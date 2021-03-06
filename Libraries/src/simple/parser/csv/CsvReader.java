package simple.parser.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import simple.io.ParseException;

/**
 * @author Ken Pierce
 *
 */
public class CsvReader implements Closeable, Iterable<List<String>>{
	private int row=0;
	private final BufferedReader in;
	private final char colSep, escape, quote;
	private final String newLine;

	/**
	 * @param file The CSV file
	 * @param colSep The column character. Default: ','
	 * @param quote The quote character. Default: '"'
	 * @param escape The quote escape character. Default: '"'
	 * @param newline Not the line separator. This is what to put for quoted values that span multiple lines. Default: "\r\n"
	 * @throws FileNotFoundException
	 */
	public CsvReader(File file, char colSep, char quote, char escape, String newline) throws FileNotFoundException {
		this(new BufferedReader(new FileReader(file)), colSep, quote, escape, newline);
	}
	public CsvReader(BufferedReader reader, char colSep, char quote, char escape, String newline) {
		this.colSep= colSep;
		this.escape= escape;
		this.quote= quote;
		this.newLine= newline;
		in= reader;
	}
	/**
	 * Uses a comma as the separator, a double quote as the quote and escape, and the CRLF as the line separator
	 * @param file the file to open
	 * @throws FileNotFoundException
	 */
	public CsvReader(File file) throws FileNotFoundException{
		this(file, ',', '"', '"', "\r\n");
	}
	public CsvReader(BufferedReader reader){
		this(reader, ',', '"', '"', "\r\n");
	}
	public int getRow(){
		return row;
	}
	/**
	 * @return A list containing the values or null if EOF
	 * @throws IOException, ParseException
	 */
	public List<String> readRow() throws IOException, ParseException{
		row++;
		ArrayList<String> ret= new ArrayList<String>();
		boolean hasescape= false,
				inquote= false,
				quoted= false;
		String line;
		StringBuilder value= new StringBuilder(40);
		int idx;
		char ch;
		while ((line= in.readLine()) != null){
			if(inquote){
				value.append(newLine);
			}
			for(idx= 0; idx < line.length(); idx++){
				ch= line.charAt(idx);
				if(ch == quote){
					if(inquote){
						if(quote == escape){
							if((idx + 1) < line.length() && line.charAt(idx+1) == quote){
								value.append(quote);
								idx++;
							}else{
								inquote= false;
							}
						}else if(hasescape){
							value.append(quote);
							hasescape= false;
						}else{
							inquote= false;
						}
					}else if(quote == escape){
						if(value.length() == 0){
							if((idx + 2) < line.length()){
								if(line.charAt(idx+1) == quote){
									if(line.charAt(idx+2) == colSep){
										//empty cell
										idx++;
									}else{
										value.append(quote);
										idx++;
									}
								}else{
									inquote= true;
									quoted= true;
								}
							}else if((idx + 1) < line.length() && line.charAt(idx+1) == quote){
								//empty cell
								idx++;
							}else{
								inquote= true;
								quoted= true;
							}
						}else if((idx + 1) < line.length() && line.charAt(idx+1) == quote){
							value.append(quote);
							idx++;
						}else{
							inquote= true;
							quoted= true;
						}
					}else if(quoted){
						throw new ParseException("Column "+(ret.size()+1)+" on row "+row+" has multiple quoted sections: "+line);
					}else{
						quoted= true;
						inquote= true;
					}
				}else if(ch == escape){
					if(hasescape){
						value.append(escape);
						hasescape= false;
					}else{
						hasescape= true;
					}
				}else if(!inquote && ch == colSep){
					ret.add(value.toString());
					quoted= false;
					value.setLength(0);
				}else if(value.length() > 0 || !Character.isWhitespace(ch)){
					if(hasescape){
						// should I just discard it?
						value.append(escape);
						hasescape= false;
					}
					if(quoted && !inquote && Character.isWhitespace(ch)){
						// has been quoted, not in a quote, and is whitespace.
						// skip it.
						continue;
					}
					value.append(ch);
				}
			}
			if(!inquote){
				ret.add(value.toString());
				break;
			}
		}
		if(line == null && ret.isEmpty()){
			return null;
		}
		return ret;
	}
	@Override
	public void close() throws IOException{
		in.close();
	}
	@SuppressWarnings("resource")
	@Override
	public Iterator<List<String>> iterator() {
		final CsvReader reader= this;
		return new Iterator<List<String>>(){
			List<String> next;
			{
				try {
					next= reader.readRow();
				} catch (Exception e) {
					next= null;
				}
			}
			@Override
			public boolean hasNext() {
				return next != null;
			}
			@Override
			public List<String> next() {
				List<String> ret= next;
				try {
					next= reader.readRow();
				} catch (Exception e) {
					next= null;
				}
				return ret;
			}
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
