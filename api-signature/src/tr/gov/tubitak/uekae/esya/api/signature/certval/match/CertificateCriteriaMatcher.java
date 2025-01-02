package tr.gov.tubitak.uekae.esya.api.signature.certval.match;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author ayetgin
 */
public class CertificateCriteriaMatcher
{
    public boolean match(CertificateSearchCriteria aCriteria, ECertificate aCertificate)
    {
        // issuer
        String issuer = aCriteria.getIssuer();
        if (issuer != null) {
            if (!issuer.equals(aCertificate.getIssuer().stringValue()))
                return false;
        }

        // serial
        BigInteger serial = aCriteria.getSerial();
        if (serial != null) {
            if (!serial.equals(aCertificate.getSerialNumber()))
                return false;
        }

        // subject
        String subject = aCriteria.getSubject();
        if (subject != null) {
            if (!subject.equals(aCertificate.getSubject().stringValue()))
                return false;
        }

        // ski
        byte[] ski = aCriteria.getSubjectKeyIdentifier();
        if (ski != null) {
            EExtensions extensions = aCertificate.getExtensions();
            if (extensions == null)
                return false;
            ESubjectKeyIdentifier certSki = extensions.getSubjectKeyIdentifier();

            if ((certSki == null) || (!Arrays.equals(ski, certSki.getValue())))
                return false;
        }


        // digest alg & value
        if (aCriteria.getDigestAlg() != null) {
            try {
                byte[] digest = DigestUtil.digest(aCriteria.getDigestAlg(), aCertificate.getEncoded());
                if (!Arrays.equals(digest, aCriteria.getDigestValue()))
                    return false;
            }
            catch (Exception x) {
                throw new ESYARuntimeException("Cant check digest value of certificate", x);
            }
        }

        // if nothing failed
        return true;
    }
}
