package tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

import java.io.InputStream;

/**
 * @author ahmety
 * date: May 8, 2009
 */
public class KriptoUtil
{

    private static Logger logger = LoggerFactory.getLogger(KriptoUtil.class);

    public static byte[] digest(byte[] aBytes, DigestMethod aDigestMethod) throws XMLSignatureException
    {
        return digest(aBytes, aDigestMethod.getAlgorithm());
    }

    public static byte[] digest(byte[] aBytes, DigestAlg aAlgorithm) throws XMLSignatureException
    {
        try {
            return  DigestUtil.digest(aAlgorithm, aBytes);
        } catch (Exception x){
            logger.error("Problem in getting hash", x);
            throw new XMLSignatureException(x, "errors.kripto.hash", aAlgorithm);
        }
    }

    public static byte[] digest(InputStream aStream, DigestMethod aDigestMethod) throws XMLSignatureException
    {
        return digest(aStream, aDigestMethod.getAlgorithm());
    }

    public static byte[] digest(InputStream aStream, DigestAlg aAlgorithm) throws XMLSignatureException
    {
        try {
             return DigestUtil.digestStream(aAlgorithm, aStream);
        } catch (Exception x){
            logger.error("Problem in getting hash", x);
            throw new XMLSignatureException(x, "errors.kripto.hash", aAlgorithm);
        }
    }


}
