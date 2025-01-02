package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;

import java.security.spec.AlgorithmParameterSpec;


public class SCSignerWithCertSerialNo implements BaseSigner
{

	protected ISmartCard mAkilliKart = null;
	protected long mSessionID;
	protected long mSlotID;
    protected byte[] mSertifikaSeriNo = null;
    protected String mSigningAlg = Algorithms.SIGNATURE_RSA_SHA1;
    protected AlgorithmParameterSpec mParams = null;

    /**
     * Implements BaseSigner interface to sign.
     * @param aSmartCard
     * @param aSessionID
     * @param aSlotID
     * @param aCertSerialNo
     * @param aSigningAlg
     */
    public SCSignerWithCertSerialNo(ISmartCard aSmartCard, long aSessionID, long aSlotID, byte[] aCertSerialNo,String aSigningAlg)
    {
    	mAkilliKart = aSmartCard;
    	mSessionID = aSessionID;
        mSlotID = aSlotID;
        mSertifikaSeriNo = aCertSerialNo;
        mSigningAlg = aSigningAlg; 
    }
    
    /**
     * Implements BaseSigner interface to sign.
     * @param aSmartCard
     * @param aSessionID
     * @param aSlotID
     * @param aCertSerialNo
     * @param aSigningAlg
     * @param aParams
     */
    public SCSignerWithCertSerialNo(ISmartCard aSmartCard, long aSessionID, long aSlotID, byte[] aCertSerialNo,String aSigningAlg,AlgorithmParameterSpec aParams)
    {
    	mAkilliKart = aSmartCard;
    	mSessionID = aSessionID;
        mSlotID = aSlotID;
        mSertifikaSeriNo = aCertSerialNo;
        mSigningAlg = aSigningAlg; 
        mParams = aParams;
    }
  
  
    /**
     * Sign given byte array
     * @param aImzalanacak byte[]
     */
	public byte[] sign(byte[] aImzalanacak)
    throws ESYAException
    {
        try
        {
            return SmartOp.sign(mAkilliKart,mSessionID,mSlotID,mSertifikaSeriNo,aImzalanacak,mSigningAlg,mParams);
        }
        catch(Exception aEx)
        {
			throw new ESYAException("Kartta Imzalama yapılırken hata oluştu. " + aEx.getMessage() , aEx);
		}
    }
	/**
	 * Returns signature algorithm as string
	 * @return
	 */
	public String getSignatureAlgorithmStr() 
	{
		return mSigningAlg;
	}
	/**
	 * Returns algorithm parameter spec
	 * @return
	 */
    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return mParams;
    }

}
