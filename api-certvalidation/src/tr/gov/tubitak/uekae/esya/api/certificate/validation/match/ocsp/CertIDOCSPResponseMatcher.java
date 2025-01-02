package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.ocsp;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ECertID;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ESingleResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponseStatus;

import java.util.Arrays;

/**
 * @author ayetgin
 */
public class CertIDOCSPResponseMatcher extends OCSPResponseMatcher
{
    @Override
    protected boolean _matchOCSPResponse(ECertificate aCertificate, ECertificate aIssuer, EOCSPResponse aOCSPResponse)
    {
        if (aOCSPResponse.getResponseStatus() != OCSPResponseStatus._SUCCESSFUL)
            return false;

        for (int i = 0 ; i < aOCSPResponse.getSingleResponseCount(); i++ )
        {
            ESingleResponse singleResponse = aOCSPResponse.getSingleResponse(i);
            ECertID certID = singleResponse.getCertID();
            if (_matches(aCertificate, aIssuer, certID))
                return true;
        }
        return false;

    }

    private boolean _matches(ECertificate aCertificate, ECertificate aIssuer, ECertID aCertId)
    {
        if (!aCertId.getSerialNumber().equals(aCertificate.getSerialNumber()))
            return false;

        try {
            DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(aCertId.getHashAlgorithm());
            byte[] issuerName = aCertificate.getIssuer().getEncoded();
            byte[] issuerNameHash = DigestUtil.digest(digestAlg, issuerName);

            if (!Arrays.equals(aCertId.getIssuerNameHash(), issuerNameHash))
                return false;

            byte[] issuerKey = aIssuer.getSubjectPublicKeyInfo().getSubjectPublicKey();
            byte[] issuerKeyHash = DigestUtil.digest(digestAlg, issuerKey);

            if (!Arrays.equals(aCertId.getIssuerKeyHash(), issuerKeyHash))
                return false;

        } catch (Exception x){
            x.printStackTrace();
            return false;
        }

        return true;
    }
}
