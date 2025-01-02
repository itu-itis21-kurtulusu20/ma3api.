using System;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval.match
{
    public class CertificateCriteriaMatcher
    {
        public bool match(CertificateSearchCriteria aCriteria, ECertificate aCertificate)
        {
            // issuer
            String issuer = aCriteria.getIssuer();
            if (issuer != null)
            {
                if (!issuer.Equals(aCertificate.getIssuer().stringValue()))
                    return false;
            }

            // serial
            BigInteger serial = aCriteria.getSerial();
            if (serial != null)
            {
                if (!serial.Equals(aCertificate.getSerialNumber()))
                    return false;
            }

            // subject
            String subject = aCriteria.getSubject();
            if (subject != null)
            {
                if (!subject.Equals(aCertificate.getSubject().stringValue()))
                    return false;
            }

            // ski
            byte[] ski = aCriteria.getSubjectKeyIdentifier();
            if (ski != null)
            {
                EExtensions extensions = aCertificate.getExtensions();
                if (extensions == null)
                    return false;
                ESubjectKeyIdentifier certSki = extensions.getSubjectKeyIdentifier();

                //if ((certSki == null) || (!Arrays.equals(ski, certSki.getValue())))
                if ((certSki == null) || (!ski.SequenceEqual(certSki.getValue())))
                    return false;
            }


            // digest alg & value
            if (aCriteria.getDigestAlg() != null)
            {
                try
                {
                    byte[] digest = DigestUtil.digest(aCriteria.getDigestAlg(), aCertificate.getEncoded());
                    if (!digest.SequenceEqual(aCriteria.getDigestValue()))
                        return false;
                }
                catch (Exception x)
                {
                    throw new ESYARuntimeException("Cant check digest value of certificate", x);
                }
            }

            // if nothing failed
            return true;
        }
    }
}
