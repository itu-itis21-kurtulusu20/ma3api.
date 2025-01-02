package tr.gov.tubitak.uekae.esya.cmpapi.base.protection;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 3, 2010
 * Time: 10:53:43 AM
 * To change this template use File | Settings | File Templates.
 */

public class SingleTrustedCertificateFinder implements ITrustedCertificateFinder {
    private ECertificate trustedCertificate;
    private ECertificate cACertificate;

    public SingleTrustedCertificateFinder(ECertificate cACertificate) {

        this.cACertificate = cACertificate;
    }

    public List<ECertificate> findTrustedCertificates(EPKIMessage incomingPkiMessage) {
        ArrayList<ECertificate> list = new ArrayList<ECertificate>();
        list.add(cACertificate);
        return list;
    }


    public void setProtectionCertificate(ECertificate trustedCertificate) {
        this.trustedCertificate = trustedCertificate;
    }
}
