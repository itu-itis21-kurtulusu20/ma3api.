package test.esya.api.signature.settings;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFormat;

/**
 * @author ayetgin
 */
public interface TestSettings
{

    BaseSigner getSigner();

    ECertificate getSignersCertificate();

    Signable getContent();

}
