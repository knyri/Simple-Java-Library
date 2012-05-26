/**
 * 
 */
package simple.gui.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import simple.gui.factory.SwingFactory;

/** Simple tool for selecting a calendar day.<br>
 * Depends on simple.gui.factory.SwingFactory
 * <br>Created: Sep 19, 2010
 * @author Kenneth Pierce
 */
public class CalendarUi extends JPanel implements ActionListener {
	private final JLabel daysl[];
	private final JToggleButton days[];
	private final JLabel header = new JLabel("",JLabel.CENTER);
	private final Calendar date;
	private final Calendar copy;
	private final Locale locale;
	private JToggleButton selected;
	private final JPanel mid;
	private final int slots;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Creates a new CalendarUi with a GregorianCalendar and english locale.
	 */
	public CalendarUi() {
		this(new GregorianCalendar());
	}
	/** Creates a new CalendarUi with cal and english locale.
	 * @param cal The calendar to use for storing the selected date.
	 */
	public CalendarUi(Calendar cal) {
		this(cal, Locale.ENGLISH);
	}
	/** Creates a new CalendarUi with a GregorianCalendar and loc.
	 * @param loc The locale to use.
	 */
	public CalendarUi(Locale loc) {
		this(new GregorianCalendar(), loc);
	}
	/** Creates a new CalendarUi with cal and loc.
	 * @param cal The calendar to use
	 * @param loc The locale to use
	 */
	public CalendarUi(Calendar cal, Locale loc) {
		super(new BorderLayout());
		date = cal;
		copy = (Calendar)cal.clone();
		locale = loc;
		slots = (date.getMaximum(Calendar.DAY_OF_WEEK_IN_MONTH))*date.getMaximum(Calendar.DAY_OF_WEEK);
		mid = new JPanel(new GridLayout(date.getMaximum(Calendar.DAY_OF_WEEK_IN_MONTH),date.getMaximum(Calendar.DAY_OF_WEEK)));
		days = new JToggleButton[date.getMaximum(Calendar.DAY_OF_MONTH)];
		daysl = new JLabel[days.length];
		for (int i = 0; i < days.length; i++) {
			days[i] = new JToggleButton(""+(i+1));
			days[i].addActionListener(this);
			daysl[i] = new JLabel(""+(i+1), JLabel.CENTER);
		}
		selected = days[0];
		selectCalendarDay();
		showDate();
		// === add elements ===
		int diw = date.getActualMaximum(Calendar.DAY_OF_WEEK);
		JPanel top = new JPanel(new BorderLayout());
		JPanel topBottom = new JPanel(new GridLayout(1,diw));
		for (int i = 0; i < diw; i++) {
			copy.set(Calendar.DAY_OF_WEEK, i+1);
			topBottom.add(new JLabel(copy.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, loc), JLabel.CENTER));
		}
		top.add(header);
		top.add(SwingFactory.makeJButton("<<", "pm",this), BorderLayout.WEST);
		top.add(SwingFactory.makeJButton(">>", "nm", this), BorderLayout.EAST);
		top.add(topBottom, BorderLayout.SOUTH);
		
		add(top, BorderLayout.NORTH);
		add(mid);
	}
	/** Returns a {@linkplain java.util.Date} class that represents the selected date.
	 * @return a {@linkplain java.util.Date} class that represents the selected date.
	 */
	public Date getDate() {
		return date.getTime();
	}
	/** Returns a copy of the underlying Calendar with the selected date. 
	 * @return a copy of the underlying Calendar with the selected date.
	 */
	public Calendar getCalendar() {
		return (Calendar)date.clone();
	}
	/**
	 * Updates the UI. Called when instantiated and when the month has been changed.
	 */
	private final void showDate() {
		mid.removeAll();
		header.setText(date.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale)+", "+date.get(Calendar.YEAR));
		copy.setTime(date.getTime());
		copy.set(Calendar.DAY_OF_MONTH, 1);
		int dow = copy.get(Calendar.DAY_OF_WEEK)-1;
		//StaticDebug.log("", copy.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale));
		copy.add(Calendar.MONTH, -1);
		int maxdayspm = copy.getActualMaximum(Calendar.DAY_OF_MONTH);
		int maxdays = date.getActualMaximum(Calendar.DAY_OF_MONTH);
		int slotsremaining = slots - (maxdays);
		//StaticDebug.log("prev days", (daysl.length-dow+1)+"-"+(maxdayspm));
		for (int i = daysl.length-dow-(daysl.length-maxdayspm);i<maxdayspm;i++) {
			mid.add(daysl[i]);
			slotsremaining--;
		}
		for (int i = 0; i < maxdays; i++) {
			mid.add(days[i]);
		}
		//StaticDebug.log("foll days", "1-"+(slotsremaining));
		for(int i = 0; i < slotsremaining; i++) {
			mid.add(daysl[i]);
		}
	}
	private final void select(JToggleButton day) {
		selected.setSelected(false);
		selected = day;
		selected.setSelected(true);
		date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.getText()));
	}
	/**
	 * selects the day stored in the calendar. Called when instantiated and when
	 * the month has been changed.
	 */
	private final void selectCalendarDay() {
		select(days[date.get(Calendar.DAY_OF_MONTH)-1]);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() instanceof JToggleButton) {
			select((JToggleButton)ae.getSource());
		} else if (ae.getSource() instanceof JButton) {
			String cmd = ae.getActionCommand();
			if ("nm".equals(cmd)) {
				date.add(Calendar.MONTH, 1);
				selectCalendarDay();
				showDate();
			} else if ("pm".equals(cmd)) {
				date.add(Calendar.MONTH, -1);
				selectCalendarDay();
				showDate();
			}
		}
	}
}
