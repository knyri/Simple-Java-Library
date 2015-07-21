package simple.forms;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
/**
 * Creates a form for user information like name, address, phone, and e-mail.<br>
 * The form can validate and reset itself.<br>
 * Will not be updated as this was used for my Java 2 class and is no longer needed. <br>
 * Created: 2005
 * @author Kenneth Pierce
 * @version 1.0
 */
public class Form extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Component that can be set.
	 */
	public final static byte FNAME = 1,
							LNAME = 2,
							MNAME = 4,
							ADDRESS = 8,//street, city, zip
							FULLADDRESS = 16,//above and country, state
							PHONE = 32,
							EMAIL = 64;
	private JTextField fName, mName, lName,
						street, city, zip,
						phone1, phone2, phone3,
						eUser, eDomain;
	private final JComboBox<String> state = new JComboBox<String>(new String[] {
											"Alabama",
											"Alaska",
											"Arizona",
											"Arkansas",
											"California",
											"Colorado",
											"Conneticut",
											"Delaware",
											"District of Columbia",
											"Florida",
											"Georgia",
											"Hawaii",
											"Idaho",
											"Illinois",
											"Indiana",
											"Iowa",
											"Kansas",
											"Kentucky",
											"Louisiana",
											"Maine",
											"Maryland",
											"Michigan",
											"Minnesota",
											"Mississippi",
											"Missouri",
											"Montana",
											"Nebraska",
											"Nevada",
											"New Hampshire",
											"New Jersey",
											"New Mexico",
											"New York",
											"North Carolina",
											"North Dakota",
											"Ohio",
											"Oklahoma",
											"Oregon",
											"Pennsylvania",
											"Rhode Island",
											"South Carloina",
											"South Dakota",
											"Tennessee",
											"Texas",
											"Utah",
											"Vermont",
											"Virginia",
											"Washington",
											"West Virginia",
											"Wisconsin",
											"Wyoming",
											"Guam",
											"Puerto Rico",
											"Virgin Islands"});
	private final JComboBox<String> country = new JComboBox<String>(new String[] {"United States"});
	private final byte option;
	/**
	 * To combine components add them together:<br>
	 * Form(FNAME+LNAME)
	 * @param option What components should be shown.
	 */
	public Form(final int option) {
		final Font font = new Font("Courier", Font.PLAIN, 12);
		this.option = (byte)option;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//everything here is pretty well self-explanitory, so
		//comments will be lacking
		JPanel pTemp = borderPanel("Name");
		if (isSet(FNAME)) {
			JPanel temp = borderPanel("First");
			fName = new JTextField(20);
			fName.setFont(font);
			temp.add(fName);
			pTemp.add(temp);
			temp = null;
		}
		if (isSet(MNAME)) {
			JPanel temp = borderPanel("Middle");
			mName = new JTextField(3);
			mName.setFont(font);
			temp.add(mName);
			pTemp.add(temp);
			temp = null;
		}
		if (isSet(LNAME)) {
			JPanel temp = borderPanel("Last");
			lName = new JTextField(20);
			lName.setFont(font);
			temp.add(lName);
			pTemp.add(temp);
			temp = null;
		}
		if (pTemp.getComponentCount()>0) {
			add(pTemp);
		}

		if (isSet(ADDRESS)||isSet(FULLADDRESS)) {
			pTemp = borderPanel("Address");
			pTemp.setLayout(new BoxLayout(pTemp, BoxLayout.Y_AXIS));
			JPanel temp = borderPanel("Street");
			street = new JTextField(50);
			street.setFont(font);
			temp.add(street);
			pTemp.add(temp);

			temp = borderPanel("City");
			city = new JTextField(20);
			city.setFont(font);
			temp.add(city);
			pTemp.add(temp);

			temp = borderPanel("Zip");
			zip = new JTextField(10);//30815 5digits
			zip.setFont(font);
			temp.add(zip);
			pTemp.add(temp);
			temp = null;
			if (isSet(FULLADDRESS)) {
				temp = borderPanel("Country");
				temp.add(country);
				pTemp.add(temp);

				temp = borderPanel("State");
				temp.add(state);
				pTemp.add(temp);
			}
			add(pTemp);
		}

		if (isSet(PHONE)) {
			pTemp = borderPanel("Phone");
			phone1 = new JTextField(4);
			phone2 = new JTextField(4);
			phone3 = new JTextField(5);
			phone1.setFont(font);
			phone2.setFont(font);
			phone3.setFont(font);
			pTemp.add(jLabel("("));
			pTemp.add(phone1);
			pTemp.add(jLabel(")"));
			pTemp.add(phone2);
			pTemp.add(jLabel("-"));
			pTemp.add(phone3);
			add(pTemp);
		}
		if (isSet(EMAIL)) {
			pTemp = borderPanel("E-mail");
			eUser = new JTextField(15);
			eUser.setFont(font);
			eDomain = new JTextField(15);
			eDomain.setFont(font);
			pTemp.add(eUser);
			pTemp.add(new JLabel("@"));
			pTemp.add(eDomain);
			add(pTemp);
		}
	}
	/**
	 * True if the component is set.
	 * @param comp One of the settable components.
	 * @return True if the component is set.
	 */
	private boolean isSet(final int comp) {
		return ((option&comp)!=0);
	}
	private static JLabel jLabel(final String label) {
		final JLabel lTemp = new JLabel(label);
		lTemp.setFont(new Font("Courier", Font.PLAIN, 12));
		return lTemp;
	}
	private static JPanel borderPanel(final String title) {
		final JPanel pTemp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pTemp.setBorder(new TitledBorder(new EmptyBorder(0,0,0,0),title));
		return pTemp;
	}
	/**
	 * @param error The error code supplied by Check().
	 * @return The name of the component that caused the error.
	 */
	public static String getError(final byte error) {
		String msg = null;
		switch (error) {
			case FNAME:
				msg = "First Name";
			break;
			case LNAME:
				msg = "Last Name";
			break;
			case MNAME:
				msg = "Middle Name";
			break;
			case ADDRESS:
				msg = "Address";
			break;
			case PHONE:
				msg = "Phone Number";
			break;
			case EMAIL:
				msg = "Email Address";
		}
		return msg;
	}
	/**
	 * Resets the form to the defaults.
	 *
	 */
	public void reset() {
		if (fName!=null) {fName.setText("");}
		if (mName!=null) {mName.setText("");}
		if (lName!=null) {lName.setText("");}
		if (street!=null) {street.setText("");}
		if (city!=null) {city.setText("");}
		if (zip!=null) {zip.setText("");}
		if (phone1!=null) {phone1.setText("");}
		if (phone2!=null) {phone2.setText("");}
		if (phone3!=null) {phone3.setText("");}
		if (eUser!=null) {eUser.setText("");}
		if (eDomain!=null) {eDomain.setText("");}
		state.setSelectedIndex(0);
		country.setSelectedIndex(0);
	}
	/**
	 * @return The error code of an invalid component value or 0.
	 */
	public byte Check() {
		if (isSet(FNAME)) {
			if (fName.getText().trim().length()==0) {return FNAME;}
		}
		if (isSet(MNAME)) {
			if (mName.getText().trim().length()==0) {return MNAME;}
		}
		if (isSet(LNAME)) {
			if (lName.getText().trim().length()==0) {return LNAME;}
		}
		if (isSet(ADDRESS)) {
			if (street.getText().trim().length()==0) {return ADDRESS;} else
			if (zip.getText().trim().length()!=5) {return ADDRESS;} else
			if (city.getText().trim().length()==0) {return ADDRESS;}
		}
		if (isSet(PHONE)) {
			if (phone1.getText().trim().length()!=3) {return PHONE;} else
			if (phone2.getText().trim().length()!=3) {return PHONE;} else
			if (phone3.getText().trim().length()!=4) {return PHONE;}
		}
		if (isSet(EMAIL)) {
			if (eUser.getText().trim().length()==0) {return EMAIL;} else
			if (eDomain.getText().trim().length()==0||eDomain.getText().indexOf(".")<=0) {return EMAIL;}
		}
		return 0;
	}
	/**
	 * @return A formatted version of all the set components.
	 */
	public String generateReport() {
		final StringBuffer buf = new StringBuffer(255);
		if (isSet(FNAME|MNAME|LNAME)) {
			buf.append("Name: ");
			if (isSet(FNAME)) {
				buf.append(getFirstName()+" ");
			}
			if (isSet(MNAME)) {
				buf.append(getMiddleName()+" ");
			}
			if (isSet(LNAME)) {
				buf.append(getLastName());
			}
			buf.append("\n");
		}
		if (isSet(ADDRESS|FULLADDRESS)) {
			buf.append("Address:\n\t");
			buf.append(getStreet()+"\n\t");
			buf.append(getCity());
			if (isSet(FULLADDRESS)) {
				buf.append(", "+getState());
			}
			buf.append(", "+getZip());
			if (isSet(FULLADDRESS)) {
				buf.append("\n\t"+getCountry());
			}
			buf.append("\n");
		}
		if (isSet(PHONE)) {
			buf.append("Phone: "+getPhoneNumber()+"\n");
		}
		if (isSet(EMAIL)) {
			buf.append("E-mail: "+getEmail()+"\n");
		}
		return buf.toString();
	}
	public String getFullName() {
		final StringBuffer buf = new StringBuffer();
		buf.append(getFirstName()+" ");
		buf.append(getMiddleName()+" ");
		buf.append(getLastName());
		return buf.toString();
	}
	public String getFirstName() {
		if (fName!=null) {
			return fName.getText();
		} else {
			return null;
		}
	}
	public String getMiddleName() {
		if (mName!=null) {
			return mName.getText();
		} else {
			return null;
		}
	}
	public String getLastName() {
		if (lName!=null) {
			return lName.getText();
		} else {
			return null;
		}
	}
	public String getPhoneNumber() {
		if (phone1!=null) {
			return phone1.getText()+phone2.getText()+phone3.getText();
		} else {
			return null;
		}
	}
	public String getStreet() {
		if (street!=null) {
			return street.getText();
		} else {
			return null;
		}
	}
	public String getZip() {
		if (zip!=null) {
			return zip.getText();
		} else {
System.out.println("null Zip");
			return null;
		}
	}
	public String getCity() {
		if (city!=null) {
			return city.getText();
		} else {
			return null;
		}
	}
	public String getState() {
		if (isSet(FULLADDRESS)) {
			return (String)state.getSelectedItem();
		} else {
			return null;
		}
	}
	public String getCountry() {
		if (isSet(FULLADDRESS)) {
			return (String)country.getSelectedItem();
		} else {
			return null;
		}
	}
	public String getEmail() {
		if (eUser!=null) {
			return eUser.getText()+"@"+eDomain.getText();
		} else {
			return null;
		}
	}
}
