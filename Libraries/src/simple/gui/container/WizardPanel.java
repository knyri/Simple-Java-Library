package simple.gui.container;

import java.awt.BorderLayout;
import java.util.Properties;


/**Panels to be used in conjunction with a WizardFrame.
 * <br>Created: 2005
 * @author Kenneth Pierce
 */
public abstract class WizardPanel extends ConfigPage {
	private static final long serialVersionUID = 1L;
	protected WizardFrame parent;
	/**
	 * Used to allow access to the frame that this panel belongs to.
	 * @return The WizardFrame that this panel belongs to.
	 */
	public final WizardFrame getWizardFrame() {
		return parent;
	}
	/**
	 * Sets the WizardFrame that this panel belongs to.<br />
	 * It should not be called except by the frame it's being
	 * added to.
	 * @param frame
	 */
	public final void setWizardFrame(WizardFrame frame) {
		parent = frame;
	}
	/**
	 * Should check the current panel to see if it is valid.
	 * @return True if the current panel is valid.
	 */
	public abstract boolean isPanelValid();
	/**
	 * Should return a helpful description of why the panel is invalid
	 * if isPanelValid() returns false.<br />
	 * Should be implemented, but does not need to be.
	 * @return The string representation of why the current form is invalid.
	 */
	public abstract String getErrorCode();
	/**
	 * Lets the WizardFrame know if the user can go to the previous panel.
	 * @return True if the user can go to the previous panel.
	 */
	public abstract boolean canGoBack();
	/**
	 * Resets the current panel to default values. 
	 */
	public abstract void reset();
	
	/**
	 * Overrides setupGUI() in ConfigPage to remove the Save and Cancel
	 * buttons. It is now up to the subclass to call the undo and save
	 * methods.
	 * @see simple.gui.container.ConfigPage#setupGUI(java.util.Properties)
	 */
	protected void setupGUI(Properties props) {
		super.setLayout(new BorderLayout());
		super.add(main);
		super.add(bottom, BorderLayout.SOUTH);
		init(props);
	}
}
