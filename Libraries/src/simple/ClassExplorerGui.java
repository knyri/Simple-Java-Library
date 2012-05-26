/**
 * 
 */
package simple;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import simple.gui.factory.SwingFactory;
import simple.util.ClassExplorer;

/** a simple UI component that shows everything about a class.
 * It will attempt to load a class.
 * <br>Created: Jul 12, 2008
 * @author Kenneth Pierce
 * @see simple.util.ClassExplorer
 */
public class ClassExplorerGui extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final JTextField in = new JTextField();
	private final JTextArea out = new JTextArea();
	private final JScrollPane outScroll = new JScrollPane(out);
	public ClassExplorerGui() {
		super(new BorderLayout());
		JPanel top = new JPanel(new BorderLayout());
		top.add(in);
		top.add(SwingFactory.makeJButton("Explore", "ex", this), BorderLayout.EAST);
		add(top, BorderLayout.NORTH);
		add(outScroll);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = SwingFactory.makeDefaultJFrame("Class Explorer");
		JPanel back = new ClassExplorerGui();
		back.setOpaque(true);
		frame.setContentPane(back);
		//frame.pack();
		frame.setSize(500, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			Class<?> cl = Class.forName(in.getText());
			out.setText(new ClassExplorer(cl).toString());
		} catch (Exception e) {
			out.setText(e.getLocalizedMessage());
		}
	}

}
