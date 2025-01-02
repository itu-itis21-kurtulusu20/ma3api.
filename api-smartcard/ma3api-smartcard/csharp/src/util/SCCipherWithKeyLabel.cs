using System;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.util
{
    public class SCCipherWithKeyLabel : BaseCipher
    {
        private readonly CardType mKartTipi = null;
        private readonly String mParola = "";
        private readonly String mAnahtarAdi = "";
        private readonly long mSlot = -1;
        private readonly long mSessionId = -1;
        private readonly SmartCard mAkilliKart;
        private readonly String mAlgorithm = "";

        /**
	    * 
	    * @param aSmartCard
	    * @param aSessionId
	    * @param aSlotID
	    * @param aKeyLabel
	    */
        public SCCipherWithKeyLabel(SmartCard aSmartCard, long aSessionId, long aSlotID, String aKeyLabel)
        {
            mAkilliKart = aSmartCard;
            mSessionId = aSessionId;
            mSlot = aSlotID;
            mAnahtarAdi = aKeyLabel;
            mAlgorithm = Algorithms.CIPHER_RSA_PKCS1;
        }

        public SCCipherWithKeyLabel(CardType aKartTipi, String aParola, String aAnahtarAdi, long aSlot)
        {
            mKartTipi = aKartTipi;
            mParola = aParola;
            mAnahtarAdi = aAnahtarAdi;
            mSlot = aSlot;
            mAlgorithm = Algorithms.CIPHER_RSA_PKCS1;
        }

        public byte[] doFinal(byte[] aData)
        {
            try
            {
                if (mAkilliKart != null)
                {
                    return SmartOp.decrypt(mAkilliKart, mSessionId, mAnahtarAdi, aData);
                }
                else
                {
                    SmartOp so = new SmartOp(mSlot, mKartTipi, mParola);
                    return so.decrypt(mAnahtarAdi, aData);
                }
            }
            catch (Exception aEx)
            {
                throw new ESYAException("Kartta sifre cozulurken hata olustu", aEx);
            }
        }

        public String getCipherAlgorithmStr()
        {
            return mAlgorithm;
        }
    }
}
