package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.mac.HMac;
import gnu.crypto.mac.IMac;
import gnu.crypto.mac.MacFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.crypto.MAC;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithLength;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */
public class GNUMAC implements MAC
{
    private static Logger logger = LoggerFactory.getLogger(GNUMAC.class);

    private IMac mMAC;
    private MACAlg mMACAlg;

    public GNUMAC(MACAlg aMACAlg)
    {
        mMACAlg = aMACAlg;
        mMAC = MacFactory.getInstance(GNUProviderUtil.resolveMacName(mMACAlg));
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException
    {
        init(aKey.getEncoded(), aParams);
    }

    public void init(byte[] aKey, AlgorithmParams aParams) throws CryptoException
    {
        if (aParams!=null && !(aParams instanceof ParamsWithLength)){
            throw new CryptoException("Invalid algorithm parameter, expected ParamWithLength, found "+aParams.getClass());
        }
        int len = (aParams==null) ? 0 : ((ParamsWithLength)aParams).getLength();

        _init(aKey, len);

    }

    private void _init(byte[] aKey, int aLength) throws CryptoException {


        if (logger.isDebugEnabled())
            logger.debug("hmac init: "+aKey +", "+aLength);

        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(HMac.USE_WITH_PKCS5_V2, Boolean.TRUE);
        attr.put(IMac.MAC_KEY_MATERIAL, aKey);
        attr.put(IMac.TRUNCATED_SIZE, aLength);


        try {
            mMAC.init(attr);
        }
        catch (Exception e) {
            throw new CryptoException("Cant init MAC", e);
        }

    }

    public void process(byte[] aData) throws CryptoException
    {
        try {
            mMAC.update(aData, 0, aData.length);
        } catch (Exception x){
            throw new CryptoException("Error updating MAC", x);
        }
    }

    public byte[] doFinal(byte[] aData) throws CryptoException
    {
        if (aData!=null)
            process(aData);
        return mMAC.digest();
    }

    public MACAlg getMACAlgorithm()
    {
        return mMACAlg;
    }

    public int getMACSize()
    {
        return mMAC.macSize();
    }
}
