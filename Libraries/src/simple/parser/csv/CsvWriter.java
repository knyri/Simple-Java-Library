package simple.parser.csv;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

public class CsvWriter implements Closeable {
	private int row=0;
	private final Writer out;
	private final char colSep;
	private final String newLine, escaped, quote;

	/**
	 * Start of line
	 */
	private boolean SOL= true;

	/**
	 * @param file The CSV file
	 * @param colSep The column character. Default: ','
	 * @param quote The quote character. Default: '"'
	 * @param escape The quote escape character. Default: '"'
	 * @param newline The line separator
	 * @throws IOException
	 */
	public CsvWriter(File file, char colSep, char quote, char escape, String newline) throws IOException {
		this.colSep= colSep;
		this.escaped= new String(new char[]{escape,quote});
		this.quote= ""+quote;
		this.newLine= newline;
		out= new FileWriter(file);
	}
	/**
	 * @param file the file to open
	 * @throws IOException
	 */
	public CsvWriter(File file) throws IOException{
		this(file, ',', '"', '"', "\r\n");
	}
	public int getRow(){
		return row;
	}
	@Override
	public void close() throws IOException{
		out.close();
	}
	private void writeValue(String string) throws IOException{
		if(!SOL){
			out.write(colSep);
		}else{
			SOL= false;
		}
		out.write(quote.charAt(0));
		out.write(string.replace(quote, escaped));
		out.write(quote.charAt(0));
	}
	public void writeln() throws IOException{
		out.write(newLine);
		SOL= true;
		row++;
	}
	public void writeln(String...strings) throws IOException{
		write(strings);
		writeln();
	}
	public void write(String...strings) throws IOException{
		for(String string: strings){
			writeValue(string);
		}
	}
	public void write(Collection<String> strings) throws IOException{
		for(String string: strings){
			write(string);
		}
	}
	public void writeln(Collection<String> strings) throws IOException{
		write(strings);
		writeln();
	}
}
