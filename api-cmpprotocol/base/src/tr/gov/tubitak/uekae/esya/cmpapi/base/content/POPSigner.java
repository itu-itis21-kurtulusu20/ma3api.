package tr.gov.tubitak.uekae.esya.cmpapi.base.content;

import com.objsys.asn1j.runtime.Asn1BitString;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertRequest;
import tr.gov.tubitak.uekae.esya.asn.crmf.POPOSigningKey;
import tr.gov.tubitak.uekae.esya.asn.crmf.ProofOfPossession;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

/**
 * RFC 2510
 * On the other hand, if the certification request is for an encryption
 * key pair (i.e., a request for an encryption certificate), then the
 * proof of possession of the private decryption key may be demonstrated
 * in one of three ways.
 * 1) By the inclusion of the private key (encrypted) in the
 * CertRequest (in the PKIArchiveOptions control structure).
 * Mesaja eklemek uzere pop olusturur.
 */

public class POPSigner implements IPOPSigner {
    private static final Logger logger = LoggerFactory.getLogger(POPSigner.class);
    BaseSigner mPOPImzaci;

    public POPSigner(BaseSigner aPOPImzaci) {
        mPOPImzaci = aPOPImzaci;
    }

    public ProofOfPossession popOlustur(CertRequest certRequest) {
        Asn1DerEncodeBuffer buf = new Asn1DerEncodeBuffer();
        try {
            certRequest.encode(buf);
        } catch (Asn1Exception e) {
            logger.error("Error While encoding CertRequest:"+e.getMessage(),e);
            throw new ESYARuntimeException("Error While encoding CertRequest:"+e.getMessage(),e);
        }
        byte[] b = buf.getMsgCopy();
        byte[] imza = null;
        try {
            imza = mPOPImzaci.sign(b);
        } catch (ESYAException e) {
            logger.error("Error while signing POP:"+e.getMessage(),e);
            throw new ESYARuntimeException("Error while signing POP:"+e.getMessage(),e);
        } 
        if (imza == null) {
            logger.error("Signed POP result is null");
            throw new ESYARuntimeException("Signed POP result is null");
        }
        POPOSigningKey signature = null;
        try {
        	SignatureAlg alg = SignatureAlg.fromName(mPOPImzaci.getSignatureAlgorithmStr());
            signature = new POPOSigningKey(
                    new AlgorithmIdentifier(alg.getOID()),
                    new Asn1BitString(imza.length << 3, imza)
            );
        } catch (Exception e) {
            logger.error("Error while creating POPOSigningKey: "+e.getMessage(), e);
            throw new ESYARuntimeException("Error while creating POPOSigningKey: "+e.getMessage(), e);
        }
        return new ProofOfPossession(ProofOfPossession._SIGNATURE, signature);
    }
}
