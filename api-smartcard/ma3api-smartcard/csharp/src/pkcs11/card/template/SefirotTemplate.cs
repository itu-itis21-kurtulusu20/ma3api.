using System;
using System.Collections.Generic;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    class SefirotTemplate : CardTemplate
    {
        protected static readonly List<String> ATR_HASHES = new List<String>();

        public SefirotTemplate()
            : base(CardType.SEFIROT)
        {
        }
        public override IPKCS11Ops getPKCS11Ops()
        {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new SefirotOps();
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
