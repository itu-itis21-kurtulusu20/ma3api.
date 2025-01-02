package tr.gov.tubitak.uekae.esya.api.tsl.model.core;

import java.util.HashMap;
import java.util.Map;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;

public class NamespacePrefixMap {
	private Map<String, String> uRI2prefix = new HashMap<String, String>();

	public NamespacePrefixMap() {
		init();
	}

	private void init() {
		uRI2prefix.put(Constants.NS_XMLDSIG, "ds");
		uRI2prefix.put(Constants.NS_TSL, "tsl");
		uRI2prefix.put(Constants.NS_TSLX, "tslx");
		uRI2prefix.put(Constants.NS_ECC, "ecc");
		uRI2prefix.put(Constants.NS_XADES, "xades");
	}

	public String getPrefix(String aNamespaceURI) {
		if (uRI2prefix.containsKey(aNamespaceURI)) {
			return uRI2prefix.get(aNamespaceURI);
		}

		return null;
	}

	public void setPrefix(String aNamespaceURI, String prefix) {
		uRI2prefix.put(aNamespaceURI, prefix);
	}
}
