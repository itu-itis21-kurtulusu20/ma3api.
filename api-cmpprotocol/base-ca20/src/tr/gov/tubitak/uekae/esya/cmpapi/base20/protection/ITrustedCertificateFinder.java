package tr.gov.tubitak.uekae.esya.cmpapi.base20.protection;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 3, 2010
 * Time: 10:53:17 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ITrustedCertificateFinder {
    List<ECertificate> findTrustedCertificates(EPKIMessage incomingPkiMessage);

    void setProtectionCertificate(ECertificate trustedCertificate);
}
