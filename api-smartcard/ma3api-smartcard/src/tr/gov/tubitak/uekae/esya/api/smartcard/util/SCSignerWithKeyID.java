package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;

import java.security.spec.AlgorithmParameterSpec;

public class SCSignerWithKeyID implements BaseSigner {

    protected ISmartCard mAkilliKart = null;
    protected long mSessionID;
    protected long mSlotID;
    protected long mKeyID;
    protected String mSigningAlg = Algorithms.SIGNATURE_RSA_SHA1;
    protected AlgorithmParameterSpec mParams = null;

    /**
     * Implements BaseSigner interface to sign.
     * @param aSmartCard
     * @param aSessionID
     * @param aSlotID
     * @param aKeyID
     * @param aSigningAlg
     * @param aParams
     */
    public SCSignerWithKeyID(ISmartCard aSmartCard, long aSessionID, long aSlotID, long aKeyID,  String aSigningAlg, AlgorithmParameterSpec aParams )
    {
        mAkilliKart = aSmartCard;
        mSessionID = aSessionID;
        mSlotID = aSlotID;
        mKeyID = aKeyID;
        mSigningAlg = aSigningAlg;
        mParams = aParams;
    }

    public SCSignerWithKeyID(ISmartCard mAkilliKart, long mSessionID, long mSlotID, long aKeyID, String mSigningAlg) {
        this.mAkilliKart = mAkilliKart;
        this.mSessionID = mSessionID;
        this.mSlotID = mSlotID;
        this.mKeyID = aKeyID;
        this.mSigningAlg = mSigningAlg;
    }

    @Override
    public byte[] sign(byte[] aData) throws ESYAException {
        try {
            return SmartOp.sign(mAkilliKart, mSessionID, mSlotID, mKeyID, aData, mSigningAlg, mParams);
        } catch (Exception aEx) {
            throw new ESYAException("Kartta imzalama yapılırken hata oluştu. " + aEx.getMessage(), aEx);
        }
    }

    @Override
    public String getSignatureAlgorithmStr() {
        return mSigningAlg;
    }

    @Override
    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return mParams;
    }
}
