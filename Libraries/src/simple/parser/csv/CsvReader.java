package simple.parser.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import simple.io.ParseException;

public class CsvReader implements Closeable{
	private int row=0;
	private final BufferedReader in;
	private final char colSep, escape, quote;

	/**
	 * @param file The CSV file
	 * @param colSep The column character. Default: ','
	 * @param quote The quote character. Default: '"'
	 * @param escape The quote escape character. Default: '"'
	 * @param newline Not the line separator. This is what to put for quoted values that span multiple lines. Default: "\r\n"
	 * @throws FileNotFoundException
	 */
	public CsvReader(File file, char colSep, char quote, char escape, String newline) throws FileNotFoundException {
		this.colSep= colSep;
		this.escape= escape;
		this.quote= quote;
		in= new BufferedReader(new FileReader(file));
	}
	/**
	 * @param file the file to open
	 * @throws FileNotFoundException
	 */
	public CsvReader(File file) throws FileNotFoundException{
		this(file, ',', '"', '"', "\r\n");
	}
	public int getRow(){
		return row;
	}
	public List<String> readRow() throws IOException, ParseException{
		row++;
		LinkedList<String> ret= new LinkedList<>();
		boolean hasescape= false,
				inquote= false,
				quoted= false;
		String line;
		StringBuilder value= new StringBuilder(40);
		int idx;
		char ch;
		while ((line= in.readLine()) != null){
			if(inquote){
				value.append("\r\n");
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
						if((idx + 1) < line.length() && line.charAt(idx+1) == quote){
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
		return ret;
	}
	@Override
	public void close() throws IOException{
		in.close();
	}
}
