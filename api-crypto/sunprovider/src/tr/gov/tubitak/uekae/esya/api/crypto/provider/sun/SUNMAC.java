package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.crypto.MAC;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithLength;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Provider;

/**
 * @author ayetgin
 */
public class SUNMAC implements MAC
{
    private MACAlg mMACAlg;
    private Mac mMac;
    private int mLength = 0;

    public SUNMAC(MACAlg aMACAlg) throws CryptoException{
        this(aMACAlg, null);
    }

    public SUNMAC(MACAlg aMACAlg, Provider aProvider) throws CryptoException {
        mMACAlg = aMACAlg;
        try {
            if(aProvider != null)
                mMac = Mac.getInstance(SUNProviderUtil.getMACName(aMACAlg),aProvider);
            else
                mMac = Mac.getInstance(SUNProviderUtil.getMACName(aMACAlg));
        } catch (Exception x){
            throw new CryptoException("Cant init MAC "+aMACAlg, x);
        }
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException
    {
        if (aParams!=null && !(aParams instanceof ParamsWithLength)){
            throw new CryptoException("Invalid algorithm parameter, expected ParamWithLength, found "+aParams.getClass());
        }
        mLength = (aParams==null) ? 0 : ((ParamsWithLength)aParams).getLength();

        try {
            mMac.init(aKey);
        }
        catch (Exception e) {
            throw new CryptoException("Cant init MAC", e);
        }
    }

    public void init(byte[] aKey, AlgorithmParams aParams) throws CryptoException
    {
        init(new SecretKeySpec(aKey, mMACAlg.getName()), aParams);
    }

    public void process(byte[] aData) throws CryptoException
    {
        try {
            mMac.update(aData);
        }
        catch (Exception x){
            throw new CryptoException("Error updating MAC", x);
        }
    }

    public byte[] doFinal(byte[] aData) throws CryptoException
    {
        try {
            byte[] result;
            if (aData!=null)
                result = mMac.doFinal(aData);
            else
                result = mMac.doFinal();
            if (mLength>0){
                byte[] resultTrimmed = new byte[mLength];
                System.arraycopy(result, 0, resultTrimmed, 0, mLength);
                return resultTrimmed;
            }
            return result;
        }
        catch (Exception x){
            throw new CryptoException("Error updating MAC", x);
        }
    }

    public MACAlg getMACAlgorithm()
    {
        return mMACAlg;
    }

    public int getMACSize()
    {
        return mMac.getMacLength();
    }

}
