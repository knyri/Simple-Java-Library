package simple.formats.xlsx;

public enum XlsxDocumentType{
	Relationship("http://schemas.openxmlformats.org/package/2006/relationships"),
	Table("application/vnd.openxmlformats-officedocument.spreadsheetml.table+xml"),
	QueryTable("application/vnd.openxmlformats-officedocument.spreadsheetml.queryTable+xml"),
	CustomXml("application/vnd.openxmlformats-officedocument.customXmlProperties+xml"),
	Connections("application/vnd.openxmlformats-officedocument.spreadsheetml.connections+xml"),
	Theme("application/vnd.openxmlformats-officedocument.theme+xml"),
	Workbook("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"),
	Worksheet("application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"),
	SharedStrings("application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml"),
	Styles("application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"),
	PropertiesCore("application/vnd.openxmlformats-package.core-properties+xml"),
	PropertiesExtended("application/vnd.openxmlformats-officedocument.extended-properties+xml"),
	PropertiesCustom("application/vnd.openxmlformats-officedocument.custom-properties+xml");
	final String typeString;
	XlsxDocumentType(String typeString){
		this.typeString= typeString;
	}
	public static XlsxDocumentType get(String contentType) {
		for(XlsxDocumentType t: XlsxDocumentType.values()) {
			if(t.typeString.equals(contentType)) {
				return t;
			}
		}
		return null;
	}

}
