/**
 * 
 */
package simple.util.system;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import simple.gui.container.SimpleBorderLayoutPane;
import simple.gui.factory.SwingFactory;

/**Does not yet work.
 * <br>Created: Aug 20, 2009
 * @author Kenneth Pierce
 */
public class RegistryEditor extends SimpleBorderLayoutPane implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static Runtime runtime = Runtime.getRuntime();
	private final JTextArea out = new JTextArea();
	public RegistryEditor() {
		addCenter(out);
		addBottom(SwingFactory.makeJButton("Export", "EXP", this));
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		JFrame frame = SwingFactory.makeDefaultJFrame("", 300, 200);
		frame.setContentPane(new RegistryEditor());
		SwingFactory.showFrame(frame);
		/*Console con = System.console();
		LineNumberReader input = null; 
		PrintWriter output = null;
		if (con!=null) {
			input = new LineNumberReader(con.reader());
			output = con.writer();
		} else {
			input = new LineNumberReader(new InputStreamReader(System.in));
			output = new PrintWriter(System.out);
		}
		String line = null;
		String cmd = null;
		int part = 0;
		while (true) {
			line = input.readLine();
			part = line.indexOf(' ');
			if (part==-1) part = line.length();
			cmd = line.substring(0, part);
			output.println(cmd);
			output.println(line);
			output.println(part);
			if (cmd=="list") {
				do_list(output, line.substring(cmd.length()+1));
			} else if (cmd=="del") {
				
			} else if (cmd=="create") {
				
			} else if (cmd=="help" || cmd=="?"){
				printHelp(output);
			} else if (cmd == "x") {
				break;
			}
		}*/
	}
	protected static void do_list(PrintWriter out, String arg) {
		try {
			Process thread = null;
			if (arg==null || arg.trim().length() == 0)
				thread = runtime.exec("regedit /s/e registrytmp.reg");
			else
				thread = runtime.exec("regedit /s/e registrytmp.reg \""+arg+"\"");
			thread.waitFor();
			char buff[] = new char[255];
			FileReader in = new FileReader("registrytmp.reg");
			int read = 0;
			while ((read = in.read(buff)) !=-1) {
				out.print(read);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	protected static void printHelp(PrintWriter out) {
		out.println("list <key>: List keys and subkeys.");
		out.println("del <key> : Deletes the key.");
		out.println("create <key>{=<value>} : Creates the key.");
		out.println("x : exit");
	}
	public static String getList(String key) {
		
		return null;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		if ("EXP".equals(ae.getActionCommand())) {
			StringWriter str = new StringWriter();
			do_list(new PrintWriter(str), null);
			out.append(str.toString());
		}
	}

}
