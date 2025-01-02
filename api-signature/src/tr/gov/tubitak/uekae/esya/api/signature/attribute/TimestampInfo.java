package tr.gov.tubitak.uekae.esya.api.signature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * @author ayetgin
 */
public abstract class TimestampInfo {

    public abstract TimestampType getType();
    public abstract ESignedData getSignedData();
    public abstract ETSTInfo getTSTInfo();

    public ECertificate getTSCertificate()
    {
        ESignedData signedData = getSignedData();
        return signedData.getSignerInfo(0).getSignerCertificate(signedData.getCertificates());
    }
}
