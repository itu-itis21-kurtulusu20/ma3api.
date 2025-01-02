package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration;

import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element.*;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/22/14
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ElementFactory {
	public static IOcspConfigElement createConfigElement(OcspConfigTags tags){
		switch (tags){
			case SIGNER:
				return new SignerConfigElement();
			case RESPONSIBLECA:
				return new ResponsibleCaElement();
			case CRLPROVIDER:
				return new CrlProviderConfigElement();
			case DBPROVIDER:
				return new DbProviderConfigElement();
			case SUPPORTEDSIGNALG:
				return new SupportedSignAlgElement();
			case PREFERREDSIGNALG:
				return new PreferredSignAlgElement();
			case OCSPCONFIGNAME:
				return new ConfigNameElement();
			case OCSPSIGNCERT:
				return new OcspSignCertificateConfigElement();
			case URL:
				return new URLConfigElement();
			case OCSPENCRCERT:
				return new OcspEncCertificateConfigElement();
			case DECRYPTOR:
				return new DecryptorConfigElement();
			case NONCECONTROL:
				return new NonceControlConfigElement();
			default:
				return null;
		}
	}
}
