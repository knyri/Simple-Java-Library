package simple.formats.xlsx;

public enum XlsxRelationshipType{
	Table("http://schemas.openxmlformats.org/officeDocument/2006/relationships/table"),
	QueryTable("http://schemas.openxmlformats.org/officeDocument/2006/relationships/queryTable"),
	PropertiesExtended("http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties"),
	PropertiesCore("http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties"),
	PropertiesCustom("http://schemas.openxmlformats.org/officeDocument/2006/relationships/custom-properties"),
	OfficeDocument("http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument");
	final String typeString;
	XlsxRelationshipType(String typeString){
		this.typeString= typeString;
	}
	public static XlsxRelationshipType get(String contentType) {
		for(XlsxRelationshipType t: XlsxRelationshipType.values()) {
			if(t.typeString.equals(contentType)) {
				return t;
			}
		}
		return null;
	}
}
