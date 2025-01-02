using System;
using System.Collections.Generic;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    class GemPlusTemplate : CardTemplate
    {
        protected static readonly List<String> ATR_HASHES = new List<String>();

        static GemPlusTemplate()
	    {
	        ATR_HASHES.Add("3B7D94000080318065B08301029083009000");
	        ATR_HASHES.Add("3B6D000080318065B08301029083009000");
	        ATR_HASHES.Add("3B6D00008065B08301019083009000");
	    }

        public GemPlusTemplate()
            : base(CardType.GEMPLUS)
        {
        }

       

        public override IPKCS11Ops getPKCS11Ops()
        {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new GemPlusOps();
                return mIslem;
            }
        }


        public override List<CK_ATTRIBUTE_NET> getRSAPrivateKeyCreateTemplate(String aKeyLabel, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> list = base.getRSAPrivateKeyCreateTemplate(aKeyLabel, aIsSign, aIsEncrypt);
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_UNWRAP, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SENSITIVE, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SIGN_RECOVER, aIsSign));
            return list;
        }

        public override List<CK_ATTRIBUTE_NET> getRSAPublicKeyCreateTemplate(String aKeyLabel, int aModulusBits, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> list = base.getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits, aIsSign, aIsEncrypt);
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_WRAP, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VERIFY_RECOVER, aIsSign));
            return list;
        }

        public override String[] getATRHashes()
        {
            return ATR_HASHES.ToArray();
        }

        public void addATR(String aATR)
        {
            ATR_HASHES.Add(aATR);
        }

       

    }
}
