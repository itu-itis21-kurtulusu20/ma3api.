package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertTemplate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRLReason;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.RevDetails;
import tr.gov.tubitak.uekae.esya.asn.x509._ImplicitValues;

/**
 * User: zeldal.ozdemir
 * Date: 2/2/11
 * Time: 8:56 AM
 */
public class ERevDetails extends BaseASNWrapper<RevDetails> {
    public ERevDetails(RevDetails aObject) {
        super(aObject);
    }

    public ERevDetails(byte[] aBytes) throws ESYAException {
        super(aBytes, new RevDetails());
    }

    public ECertTemplate getCertDetails() {
        return new ECertTemplate(mObject.certDetails);
    }

    public ECRLReason getCrlReason() throws ESYAException {
        EExtensions eExtensions = new EExtensions(mObject.crlEntryDetails);
        EExtension extension = eExtensions.getExtension(new Asn1ObjectIdentifier(_ImplicitValues.id_ce_cRLReasons));
        return new ECRLReason(extension.getValue());
    }
}
