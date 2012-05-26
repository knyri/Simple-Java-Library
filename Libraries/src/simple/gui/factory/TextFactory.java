/**
 * 
 */
package simple.gui.factory;

import java.awt.Font;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/** Creates Fonts and sets up StyledDocuments.
 * <br>Created: Mar 6, 2009
 * @author Kenneth Pierce
 */
public final class TextFactory {
	protected TextFactory() {}
	/**
	 * Font style
	 */
	public static final String
		STYLE_PLAIN = "plain",
		STYLE_BOLD = "bold",
		STYLE_ITALIC = "italic",
		STYLE_LARGE = "large",
		STYLE_SMALL = "small";
	public static final String
		FONT_SANSSERIF = "SansSerif",
		FONT_SERIF = "Serif",
		FONT_MONOSPACE = "Monospace";
    /**
     * Size 12 Courier New font.
     */
    public static final Font MONOSPACE = new Font("Courier New", Font.PLAIN, 12);
    /**
     * Creates a plain font.
     * @param name Name of the font wanted.
     * @param size Point size of the font.
     * @return A new java.awt.Font that represents a font with
     * 			point size <var>size</var> that has the name <var>name</var>.
     */
    public static Font makeFont(String name, int size) {
    	return new Font(name, Font.PLAIN, size);
    }
    /**
     * Returns {@link #MONOSPACE}.
     * @return {@link #MONOSPACE}
     */
    public static Font getMonoFont() {
    	return MONOSPACE;
    }
    /**
     * Creates a font of point size <var>size</var> derived from {@link #MONOSPACE}.
     * @param size Point size the font should be.
     * @return <code>{@link #MONOSPACE}.deriveFont(<var>size</var>)</code>
     */
    public static Font getMonoFont(int size) {
    	return MONOSPACE.deriveFont(size);
    }
    /**
     * Adds a plain, bold, italic, small, and large style to the document.
     * Small is 2 points smaller than the base size.
     * Large is 2 points larger then the base size.
     * @param doc The document that these will be added to.
     * @param font The font family. null defaults to SanSerif.
     * @param size The font size. 0 defaults to 12.
     */
    public static void addStylesToDocument(StyledDocument doc, String font, int size) {
    	if (doc == null) return;
    	if (font == null || font.isEmpty()) font = FONT_SANSSERIF;
    	if (size <= 0) size = 12;
    	else if (size < 3) size = 3;
    	Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
    	
    	Style plain = doc.addStyle(STYLE_PLAIN, def);
    	StyleConstants.setFontFamily(plain, font);
    	StyleConstants.setFontSize(plain, size);
    	
    	Style s = doc.addStyle(STYLE_BOLD, plain);
    	StyleConstants.setBold(s, true);
    	
    	s = doc.addStyle(STYLE_ITALIC, plain);
    	StyleConstants.setItalic(s, true);
    	
    	s = doc.addStyle(STYLE_LARGE, plain);
    	StyleConstants.setFontSize(s, size+2);
    	
    	s = doc.addStyle(STYLE_SMALL, plain);
    	StyleConstants.setFontSize(s, size-2);
    }
    /**
     * Adds a plain, bold, italic, small, and large style to the document.
     * Small is 2 points smaller than the base size.
     * Large is 2 points larger then the base size.
     * @param doc The document that these will be added to.
     * @param font The font family. null defaults to SanSerif.
     */
    public static void addStylesToDocument(StyledDocument doc, String font) {
    	addStylesToDocument(doc, font, 0);
    }
    /**
     * Adds a plain, bold, italic, small, and large style to the document.
     * Small is 2 points smaller than the base size.
     * Large is 2 points larger then the base size.
     * @param doc The document that these will be added to.
     * @param size The font size. 0 defaults to 12.
     */
    public static void addStylesToDocument(StyledDocument doc, int size) {
    	addStylesToDocument(doc, null, size);
    }
    /**
     * Adds a plain, bold, italic, small, and large style to the document.
     * Small is 2 points smaller than the base size.
     * Large is 2 points larger then the base size.
     * @param doc The document that these will be added to.
     */
    public static void addStylesToDocument(StyledDocument doc) {
    	addStylesToDocument(doc, null, 0);
    }
}
