package tr.gov.tubitak.uekae.esya.cmpapi.base20.util;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ESuppLangTagsValue;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.CertificationBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.IMsgBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 10/2/13
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocaleUtility {

	public static  EPKIMessage createLocaleSetRequest(String localeTag, IMsgBuilder msgBuilder) throws ESYAException {
		EPKIHeader requestHeader = msgBuilder.createPkiHeader();
		EPKIBody pkiBody = createLocaleSetBody(localeTag);
		return msgBuilder.createCmpMessage(requestHeader, pkiBody);
	}

	private static EPKIBody createLocaleSetBody(String localeTAG) throws ESYAException {
		EPKIBody epkiBody = new EPKIBody();
		epkiBody.setSuppLangTagsValueRequest(new ESuppLangTagsValue(localeTAG));
		return epkiBody;
	}
}
