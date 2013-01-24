/**
 *
 */
package simple.net.http;

import simple.net.Uri;

/** Simply holds an immutable copy of the site Uri and the referrer Uri.
 * <br>Created: Jul 18, 2008
 * @author Kenneth Pierce
 */
public class Site {
	private final Uri referrer;
	private final Uri site;
	public Site(Uri referrer, Uri site) {
		this.referrer = referrer;
		this.site = site;
	}
	@Override
	public String toString() {
		return referrer.toString();
	}
	public Uri getReferrer() {
		return referrer;
	}
	public Uri getSite() {
		return site;
	}
	@Override
	public int hashCode() {
		//Why am I only using the referrer?
		if(referrer==null)return 0;
		return referrer.hashCode();
	}
}
