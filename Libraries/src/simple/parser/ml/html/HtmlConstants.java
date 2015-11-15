/**
 *
 */
package simple.parser.ml.html;

import simple.CIString;
import simple.parser.ml.ParserConstants;

/**
 * 10/10/2012
 * @author Ken
 *
 */
public final class HtmlConstants{
	public static final CIString
		PARAM_ID=new CIString("id").intern(),
		PARAM_HREF=new CIString("href").intern(),
		PARAM_NAME=new CIString("name").intern(),
		PARAM_STYLE=new CIString("style").intern(),
		PARAM_SRC=new CIString("src").intern(),
		PARAM_REL=new CIString("rel").intern(),
		PARAM_TYPE=new CIString("type").intern(),
		PARAM_CLASS=new CIString("class").intern(),
		PARAM_ONCLICK=new CIString("onclick").intern(),
		PARAM_ONMOUSEOUT=new CIString("onmouseout").intern(),
		PARAM_ONMOUSEOVER=new CIString("onmouseover").intern(),
		PARAM_CONTENT=new CIString("content").intern(),
		PARAM_VALUE=new CIString("value").intern(),
		//PARAM_=new CIString("").intern(),
		//TAG_=new CIString("").intern(),
		TAG_SCRIPT=new CIString("script").intern(),
		TAG_LINK=new CIString("link").intern(),
		TAG_META=new CIString("meta").intern(),
		TAG_STYLE= new CIString("style").intern(),
		TAG_BASE=new CIString("base").intern(),

		TAG_B=new CIString("b").intern(),
		TAG_I=new CIString("i").intern(),
		TAG_EM=new CIString("em").intern(),
		TAG_STRONG=new CIString("strong").intern(),

		TAG_FORM=new CIString("form").intern(),
		TAG_INPUT=new CIString("input").intern(),
		TAG_SELECT=new CIString("select").intern(),
		TAG_TEXTAREA=new CIString("textarea").intern(),

		TAG_LI=new CIString("li").intern(),
		TAG_OL=new CIString("ol").intern(),
		TAG_UL=new CIString("ul").intern(),

		TAG_DL=new CIString("dl").intern(),
		TAG_DD=new CIString("dd").intern(),
		TAG_DT=new CIString("dt").intern(),

		TAG_A=new CIString("a").intern(),
		TAG_DIV=new CIString("div").intern(),

		TAG_TABLE=new CIString("table").intern(),
		TAG_THEAD=new CIString("thead").intern(),
		TAG_TBODY=new CIString("tbody").intern(),
		TAG_TFOOT=new CIString("tfoot").intern(),
		TAG_TR=new CIString("tr").intern(),
		TAG_TD=new CIString("td").intern(),
		TAG_TH=new CIString("th").intern(),

		TAG_IMG=new CIString("img").intern(),
		TAG_SPAN=new CIString("span").intern(),
		TAG_P=new CIString("p").intern(),
		TAG_HR=new CIString("hr").intern(),
		TAG_BR=new CIString("br").intern();

	public static final ParserConstants PARSER_CONSTANTS= new ParserConstants();
	static {
		PARSER_CONSTANTS.addPcdataTag(TAG_SCRIPT);
		PARSER_CONSTANTS.addPcdataTag(TAG_STYLE);
		PARSER_CONSTANTS.addPcdataTag(TAG_TEXTAREA);
		//PARSER_CONSTANTS.addPcdataTag(TAG_);
		PARSER_CONSTANTS.addSelfCloser(TAG_BASE);
		PARSER_CONSTANTS.addSelfCloser(TAG_BR);
		PARSER_CONSTANTS.addSelfCloser(TAG_HR);
		PARSER_CONSTANTS.addSelfCloser(TAG_IMG);
		PARSER_CONSTANTS.addSelfCloser(TAG_INPUT);
		PARSER_CONSTANTS.addSelfCloser(TAG_META);
		PARSER_CONSTANTS.addSelfCloser(TAG_LINK);
		//PARSER_CONSTANTS.addSelfCloser(TAG_);

		PARSER_CONSTANTS.addOptionalEnder(TAG_DD, TAG_DD, TAG_DT);
		PARSER_CONSTANTS.addOptionalEnder(TAG_DT, TAG_DD, TAG_DT);
		PARSER_CONSTANTS.addOptionalEnder(TAG_LI, TAG_LI);
		PARSER_CONSTANTS.addOptionalEnder(TAG_P, TAG_P);
		PARSER_CONSTANTS.addOptionalEnder(TAG_TD, TAG_TD, TAG_TH, TAG_TR);
		PARSER_CONSTANTS.addOptionalEnder(TAG_TH, TAG_TD, TAG_TH, TAG_TR);
		PARSER_CONSTANTS.addOptionalEnder(TAG_TR, TAG_TR);
		PARSER_CONSTANTS.addOptionalEnder(TAG_THEAD, TAG_THEAD, TAG_TBODY, TAG_TFOOT);
		PARSER_CONSTANTS.addOptionalEnder(TAG_TBODY, TAG_THEAD, TAG_TBODY, TAG_TFOOT);
		PARSER_CONSTANTS.addOptionalEnder(TAG_TFOOT, TAG_THEAD, TAG_TBODY, TAG_TFOOT);
		//PARSER_CONSTANTS.addOptionalEnder(TAG_, TAG_);
	}
}
