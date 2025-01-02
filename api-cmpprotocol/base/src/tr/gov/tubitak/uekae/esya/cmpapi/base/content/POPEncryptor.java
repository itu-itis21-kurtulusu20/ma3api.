package tr.gov.tubitak.uekae.esya.cmpapi.base.content;

import tr.gov.tubitak.uekae.esya.asn.crmf.CertRequest;
import tr.gov.tubitak.uekae.esya.asn.crmf.POPOPrivKey;
import tr.gov.tubitak.uekae.esya.asn.crmf.ProofOfPossession;
import tr.gov.tubitak.uekae.esya.asn.crmf.SubsequentMessage;

/**
* Created by IntelliJ IDEA.
* User: zeldal.ozdemir
* Date: Nov 11, 2010
* Time: 10:41:03 AM
* To change this template use File | Settings | File Templates.
*/

public class POPEncryptor implements IPOPSigner {
    public POPEncryptor() {

    }

    public ProofOfPossession popOlustur(CertRequest aIstek) {
        POPOPrivKey priKey = new POPOPrivKey();
        SubsequentMessage message = new SubsequentMessage(SubsequentMessage.encrCert);
        priKey.set_subsequentMessage(message);
        ProofOfPossession pop = new ProofOfPossession(ProofOfPossession._KEYENCIPHERMENT, priKey);
        return pop;
    }
}
