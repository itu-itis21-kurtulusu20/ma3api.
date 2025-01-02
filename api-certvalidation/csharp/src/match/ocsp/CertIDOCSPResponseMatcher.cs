using System;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.ocsp
{
    /**
     * @author ayetgin
     */
    public class CertIDOCSPResponseMatcher : OCSPResponseMatcher
    {
        protected override bool _matchOCSPResponse(ECertificate aCertificate, ECertificate aIssuer, EOCSPResponse aOCSPResponse)
        {
            if (aOCSPResponse.getResponseStatus() != OCSPResponseStatus.successful().mValue)
                return false;

            for (int i = 0; i < aOCSPResponse.getSingleResponseCount(); i++)
            {
                ESingleResponse singleResponse = aOCSPResponse.getSingleResponse(i);
                ECertID certID = singleResponse.getCertID();
                if (_matches(aCertificate, aIssuer, certID))
                    return true;
            }
            return false;

        }

        private bool _matches(ECertificate aCertificate, ECertificate aIssuer, ECertID aCertId)
        {
            if (!aCertId.getSerialNumber().Equals(aCertificate.getSerialNumber()))
                return false;

            try
            {
                DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(aCertId.getHashAlgorithm());
                byte[] issuerName = aCertificate.getIssuer().getBytes();
                byte[] issuerNameHash = DigestUtil.digest(digestAlg, issuerName);

                if (!aCertId.getIssuerNameHash().SequenceEqual(issuerNameHash))
                    return false;

                byte[] issuerKey = aIssuer.getSubjectPublicKeyInfo().getSubjectPublicKey();
                byte[] issuerKeyHash = DigestUtil.digest(digestAlg, issuerKey);

                if (!aCertId.getIssuerKeyHash().SequenceEqual(issuerKeyHash))
                    return false;

            }
            catch (Exception x)
            {
                Console.WriteLine(x.StackTrace);
                return false;
            }

            return true;
        }
    }
}
