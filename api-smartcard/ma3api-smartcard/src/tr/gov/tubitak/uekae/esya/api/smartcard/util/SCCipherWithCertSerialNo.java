package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;

import java.security.spec.AlgorithmParameterSpec;


public class SCCipherWithCertSerialNo implements BaseCipher
{
	
	private ISmartCard mAkilliKart = null;
	private long mSessionID;
	private byte[] mSertifikaSeriNo = null;
	private String mAlgorithm = null;
	private AlgorithmParameterSpec mParams = null;
	
	/**
	 * 
	 * @param aSmartCard
	 * @param aSessionID
	 * @param aCertSerialNo
	 */
    public SCCipherWithCertSerialNo(ISmartCard aSmartCard, long aSessionID, byte[] aCertSerialNo)
    {
    	mAkilliKart = aSmartCard;
    	mSessionID = aSessionID;
		mSertifikaSeriNo = aCertSerialNo;
		mAlgorithm = Algorithms.CIPHER_RSA_PKCS1;
	}
    
    
    public SCCipherWithCertSerialNo(ISmartCard aSmartCard, long aSessionID, byte[] aCertSerialNo,String aAlgorithm,AlgorithmParameterSpec aParams)
    {
    	mAkilliKart = aSmartCard;
    	mSessionID = aSessionID;
		mSertifikaSeriNo = aCertSerialNo;
		mAlgorithm = aAlgorithm;
		mParams = aParams;
	}
     

    public byte[] doFinal(byte[] aData)
    throws ESYAException
    {
    	try
		{
    		long slot = mAkilliKart.getSessionInfo(mSessionID).slotID;
			return SmartOp.decrypt(mAkilliKart, mSessionID, slot, mSertifikaSeriNo, aData,mAlgorithm,mParams);
		}
    	catch(Exception aEx)
		{
			throw new ESYAException("Kartta şifre çözülürken hata oluştu", aEx);
		}
    }

	public String getCipherAlgorithmStr() 
	{
		return mAlgorithm;
	}

	
}
