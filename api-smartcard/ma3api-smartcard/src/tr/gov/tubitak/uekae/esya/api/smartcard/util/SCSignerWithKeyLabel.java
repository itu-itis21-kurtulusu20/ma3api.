package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import java.security.spec.AlgorithmParameterSpec;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;


public class SCSignerWithKeyLabel implements BaseSigner
{
	
	protected ISmartCard mAkilliKart = null;
	protected long mSessionID;
	protected long mSlotID;
    protected String mAnahtarAdi = "";
	protected String mSigningAlg = Algorithms.SIGNATURE_RSA_SHA1;
	protected AlgorithmParameterSpec mParams = null;

	/**
	 * Implements BaseSigner interface to sign.
	 * @param aSmartCard 
	 * @param aSessionID
	 * @param aSlotID
	 * @param aKeyLabel
	 * @param aSigningAlg
	 * @param aParams
	 */
	public SCSignerWithKeyLabel(ISmartCard aSmartCard, long aSessionID, long aSlotID, String aKeyLabel,  String aSigningAlg, AlgorithmParameterSpec aParams )
	{
		mAkilliKart = aSmartCard;
		mSessionID = aSessionID;
		mSlotID = aSlotID;
		mAnahtarAdi = aKeyLabel;
		mSigningAlg = aSigningAlg;
		mParams = aParams;
	}

    public SCSignerWithKeyLabel(ISmartCard mAkilliKart, long mSessionID, long mSlotID, String mAnahtarAdi, String mSigningAlg) {
        this.mAkilliKart = mAkilliKart;
        this.mSessionID = mSessionID;
        this.mSlotID = mSlotID;
        this.mAnahtarAdi = mAnahtarAdi;
        this.mSigningAlg = mSigningAlg;
    }

    public byte[] sign(byte[] aImzalanacak)
	throws ESYAException
	{
		try
		{
			return SmartOp.sign(mAkilliKart, mSessionID, mSlotID, mAnahtarAdi, aImzalanacak, mSigningAlg, mParams);
		} 
		catch(Exception aEx)
		{
			throw new ESYAException("Kartta imzalama yapılırken hata oluştu. " + aEx.getMessage(), aEx);
		}
	}


	public String getSignatureAlgorithmStr() 
	{
		return mSigningAlg;
	}

    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return mParams;
    }

}
