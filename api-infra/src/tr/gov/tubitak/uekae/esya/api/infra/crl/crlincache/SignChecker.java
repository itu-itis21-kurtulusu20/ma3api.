package tr.gov.tubitak.uekae.esya.api.infra.crl.crlincache;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Verifier;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import java.security.PublicKey;

/**
 * @author zeldalo
 * Date: 12.May.2009
 */
class SignChecker implements ISignChecker {
    private static final Logger logger = LoggerFactory.getLogger(SignChecker.class);
    //private AY_HafizadaTumKripto imzaci;
    private Verifier imzaci;
    private byte[] signature;

    SignChecker(AlgorithmIdentifier algID, PublicKey publicKey, byte[] signature) throws ESYAException {
        this.signature = signature;
        //imzaci = HafizadaTumKripto.getInstance();
        Pair<SignatureAlg, AlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(algID));
        imzaci = Crypto.getVerifier(pair.first());
		imzaci.init(publicKey,pair.second());
    }

    public void collect(Asn1Type aAsn) {
		if(aAsn != null)
		{
            try {
                byte[] encoded = _encode(aAsn);
                imzaci.update(encoded);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void collect(byte[] data) {
        if(data != null)
		{
            try {
                imzaci.update(data);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public boolean verify() {
        try {
            return imzaci.verifySignature(signature);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    private byte[] _encode(Asn1Type aAsn) throws Asn1Exception{
		Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
		aAsn.encode(enc);
		byte[] encoded = enc.getMsgCopy();
		return encoded;
	}

}
