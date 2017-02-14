package simple.parser.csv;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

public class CsvWriter implements Closeable {
	private int row= 0;
	private final Writer out;
	private final char colSep, quote;
	private final String newLine, escaped, quoteStr;

	/**
	 * Start of line
	 */
	private boolean SOL= true;

	public CsvWriter(Writer file, char colSep, char quote, char escape, String newline) throws IOException {
		this.colSep= colSep;
		this.escaped= new String(new char[]{escape,quote});
		this.quoteStr= ""+quote;
		this.quote= quote;
		this.newLine= newline;
		out= file;
	}

	/**
	 * @param file The CSV file
	 * @param colSep The column character. Default: ','
	 * @param quote The quote character. Default: '"'
	 * @param escape The quote escape character. Default: '"'
	 * @param newline The line separator
	 * @throws IOException
	 */
	public CsvWriter(File file, char colSep, char quote, char escape, String newline) throws IOException {
		this(new BufferedWriter(new FileWriter(file), 4096), colSep, quote, escape, newline);
	}
	/**
	 * @param file the file to open
	 * @throws IOException
	 */
	public CsvWriter(File file) throws IOException{
		this(file, ',', '"', '"', "\r\n");
	}
	public CsvWriter(Writer file) throws IOException{
		this(file, ',', '"', '"', "\r\n");
	}
	/**
	 * The current row. Starts at 0
	 * @return Current row
	 */
	public int getRow(){
		return row;
	}
	@Override
	public void close() throws IOException{
		out.close();
	}

	private void writeValue(String string) throws IOException{
		out.write(quote);
		if(string != null){
			out.write(string.replace(quoteStr, escaped));
		}
		out.write(quote);
	}
	/**
	 * Writes the string
	 * @param string String to write
	 * @return this
	 * @throws IOException
	 */
	public CsvWriter write(String string) throws IOException{
		if(SOL){
			SOL= false;
		}else{
			out.write(colSep);
		}
		writeValue(string);
		return this;
	}
	/**
	 * Writes the strings
	 * @param strings Strings to write
	 * @return this
	 * @throws IOException
	 */
	public CsvWriter write(String...strings) throws IOException{
		int pos= 0;
		final int end= strings.length;
		if(end == 0){
			return this;
		}
		if(SOL){
			writeValue(strings[0]);
			pos= 1;
			SOL= false;
		}
		for(; pos < end; pos++ ){
			out.write(colSep);
			writeValue(strings[pos]);
		}
		return this;
	}
	/**
	 * Writes the string
	 * @param strings Strings to write
	 * @return this
	 * @throws IOException
	 */
	public CsvWriter write(Collection<String> strings) throws IOException{
		if(strings.isEmpty()){
			return this;
		}
		Iterator<String> iter= strings.iterator();
		if(SOL){
			writeValue(iter.next());
			SOL= false;
		}
		while(iter.hasNext()){
			out.write(colSep);
			writeValue(iter.next());
		}
		return this;
	}
	/**
	 * Writes the string and ends the line
	 * @param string String to write
	 * @return this
	 * @throws IOException
	 */
	public CsvWriter writeln(String string) throws IOException{
		write(string);
		return writeln();
	}
	/**
	 * Ends the line
	 * @return this
	 * @throws IOException
	 */
	public CsvWriter writeln() throws IOException{
		out.write(newLine);
		SOL= true;
		row++;
		return this;
	}

	/**
	 * Writes the strings and ends the line
	 * @param strings String to write
	 * @return this
	 * @throws IOException
	 */
	public CsvWriter writeln(String...strings) throws IOException{
		write(strings);
		return writeln();
	}

	/**
	 * Writes the strings and ends the line
	 * @param strings Strings to write
	 * @return this
	 * @throws IOException
	 */
	public CsvWriter writeln(Collection<String> strings) throws IOException{
		write(strings);
		return writeln();
	}
}
