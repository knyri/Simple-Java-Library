package simple.gui;

import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Creates a slideshow of JPanels that can advance forward and backward and/or skip
 * to the end or beginning.
 * <br>Created: 2005
 * @author Kenneth Pierce
 */
public class Flipper extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private CardLayout CL = new CardLayout();
	private JPanel cards = new JPanel(CL);
	public Flipper (Component[] x) {
		for (int i=0;i<x.length;i++) {
			cards.add(x[i],String.valueOf(i));
		}
		setLayout(new BorderLayout());
		JPanel pTemp = new JPanel(new GridLayout(1,0,2,2));
		JButton bTemp = new JButton("<<");
		bTemp.setActionCommand("first");
		bTemp.addActionListener(this);
		pTemp.add(bTemp);

		bTemp = new JButton("<");
		bTemp.setActionCommand("previous");
		bTemp.addActionListener(this);
		pTemp.add(bTemp);
		add(pTemp, BorderLayout.EAST);

		add(cards);

		pTemp = new JPanel(new GridLayout(1,0,2,2));
		bTemp = new JButton(">");
		bTemp.setActionCommand("next");
		bTemp.addActionListener(this);
		pTemp.add(bTemp);

		bTemp = new JButton(">>");
		bTemp.setActionCommand("last");
		bTemp.addActionListener(this);
		pTemp.add(bTemp);
		add(pTemp, BorderLayout.WEST);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("next")) {
			CL.next(cards);
		} else if (e.getActionCommand().equals("previous")) {
			CL.previous(cards);
		} else if (e.getActionCommand().equals("first")) {
			CL.first(cards);
		} else if (e.getActionCommand().equals("last")) {
			CL.last(cards);
		}
	}
}
