/**
 *
 */
package simple.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import simple.gui.factory.SwingFactory;


/**A progress window for a SwingWorker thread.
 * <br>Requires SDialog and simple.gui.factory.SwingFactory
 * <br>Created: Feb 22, 2009
 * @author Kenneth Pierce
 */
public class SwingWorkerProgressWindow extends SDialog implements PropertyChangeListener, ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 5580570673877593545L;
	private final JProgressBar pbar = new JProgressBar(0,100);
	private SwingWorker<?, ?> thread;
	public SwingWorkerProgressWindow(JFrame frame, String title, SwingWorker<?, ?> thread) {
		super(frame, title, false);
		setAlwaysOnTop(true);
		this.setResizable(false);
		this.thread = thread;
		addCenter(pbar);
		setSize(150,100);
		addBottom(SwingFactory.makeJButton("Cancel", "st", this));
		if (thread != null)
			thread.addPropertyChangeListener(this);
	}
	public SwingWorkerProgressWindow(JFrame frame, String title) {
		this(frame, title, null);
	}
	public void setWorker(SwingWorker<?,?> newWorker) {
		if (thread != null)
			thread.removePropertyChangeListener(this);
		thread = newWorker;
		thread.addPropertyChangeListener(this);
		pbar.setValue(0);
	}
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if ("progress".equals(pce.getPropertyName()))
            pbar.setValue((Integer)pce.getNewValue());
		else	if ("display".equals(pce.getPropertyName()))
			pbar.setString((String)pce.getNewValue());
		if (pbar.getValue()==100)
			setVisible(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if ("st".equals(ae.getActionCommand()))
			thread.cancel(true);
	}

}
