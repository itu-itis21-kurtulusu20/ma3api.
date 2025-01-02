using System;
using System.Collections.Generic;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    class DataKeyTemplate : CardTemplate
    {
        
        protected static readonly List<String> ATR_HASHES = new List<String>();

        static DataKeyTemplate()
	    {
	        ATR_HASHES.Add("3BFF1100008131FE4D8025A00000005657444B3333300600D0");
	    }

        public DataKeyTemplate()
            : base(CardType.DATAKEY)
        {
        }
        

        public override IPKCS11Ops getPKCS11Ops()
        {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new DataKeyOps();
                return mIslem;
            }
        }


        public override List<CK_ATTRIBUTE_NET> getRSAPrivateKeyCreateTemplate(String aKeyLabel, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> list = base.getRSAPrivateKeyCreateTemplate(aKeyLabel, aIsSign, aIsEncrypt);
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_UNWRAP, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SIGN_RECOVER, aIsSign));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SENSITIVE, true));
            return list;
        }

        public override List<CK_ATTRIBUTE_NET> getRSAPublicKeyCreateTemplate(String aKeyLabel, int aModulusBits, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> list = base.getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits, aIsSign, aIsEncrypt);
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_WRAP, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VERIFY_RECOVER, aIsSign));

            return list;
        }

        public override List<CK_ATTRIBUTE_NET> getRSAPublicKeyImportTemplate(String aLabel, /*RSAPrivateCrtKey*//*RsaPrivateCrtKeyParameters*/EPrivateKeyInfo aPrivKeyInfo, ECertificate aSertifika, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> list = base.getRSAPublicKeyImportTemplate(aLabel, aPrivKeyInfo, aSertifika, aIsSign, aIsEncrypt);
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VERIFY_RECOVER, false));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_DERIVE, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODIFIABLE, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_WRAP, false));
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
