using System;
using System.Collections.Generic;
using iaik.pkcs.pkcs11.wrapper;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    class SafeSignTemplate : CardTemplate
    {
        protected static readonly List<String> ATR_HASHES = new List<String>();


        static SafeSignTemplate()
	    {
            ATR_HASHES.Add("3BBB1800C01031FE4580670412B00303000081053C");
            ATR_HASHES.Add("3BFA1800FF8131FE454A434F5032315632333165");
            ATR_HASHES.Add("3BB79400C03E31FE6553504B32339000AE");
            ATR_HASHES.Add("3BF81800FF8131FE454A434F507632343143");
	    }

        public SafeSignTemplate()
            : base(CardType.SAFESIGN)
        {
        }
        public override IPKCS11Ops getPKCS11Ops()
        {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new SafeSignOps();
                return mIslem;
            }
        }


        public override List<CK_ATTRIBUTE_NET> getRSAPrivateKeyCreateTemplate(String aKeyLabel, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> list = base.getRSAPrivateKeyCreateTemplate(aKeyLabel, aIsSign, aIsEncrypt);
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_UNWRAP, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SIGN_RECOVER, aIsSign));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SENSITIVE, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LOCAL, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ALWAYS_SENSITIVE, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_NEVER_EXTRACTABLE, true));
            return list;
        }

        public override List<CK_ATTRIBUTE_NET> getRSAPublicKeyCreateTemplate(String aKeyLabel, int aModulusBits, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> list = base.getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits, aIsSign, aIsEncrypt);
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_WRAP, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VERIFY_RECOVER, aIsSign));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LOCAL, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ALWAYS_SENSITIVE, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_NEVER_EXTRACTABLE, true));
            return list;
        }


        public override List<CK_ATTRIBUTE_NET> getRSAPublicKeyImportTemplate(String aLabel, /*RSAPrivateCrtKey*//*RsaPrivateCrtKeyParameters*/EPrivateKeyInfo aPrivKeyInfo, ECertificate aCert, bool aIsSign, bool aIsEncrypt)
        {
            AsymmetricKeyParameter privKey = PrivateKeyFactory.CreateKey(/*KriptoUtils.ToBouncy(aPrivKeyInfo)*/BouncyProviderUtil.ToBouncy(aPrivKeyInfo));  //asn1 to bouncy donusumu yapiliyor...
            if (privKey is RsaPrivateCrtKeyParameters)
            {
                RsaPrivateCrtKeyParameters rsaPrivCrtKey = (RsaPrivateCrtKeyParameters)privKey;
                byte[] modBytes = rsaPrivCrtKey.Modulus.ToByteArray();

                List<CK_ATTRIBUTE_NET> list = base.getRSAPublicKeyImportTemplate(aLabel, aPrivKeyInfo, aCert, aIsSign, aIsEncrypt);
                list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LOCAL, true));
                list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ALWAYS_SENSITIVE, true));
                list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_NEVER_EXTRACTABLE, true));
                list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS_BITS, modBytes.Length * 8));
                return list;
            }
            else
            {
                throw new /*Kripto*/Exception("Metoda verilen ozel anahtar RsaPrivateCrtKey tipinde degil!, Tipi:  " + privKey.GetType().ToString());
            }
        }

        public override List<CK_ATTRIBUTE_NET> getRSAPrivateKeyImportTemplate(String aLabel,/*RSAPrivateCrtKey*//*RsaPrivateCrtKeyParameters*/EPrivateKeyInfo aPrivKeyInfo, ECertificate aCert, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> list = base.getRSAPublicKeyImportTemplate(aLabel, aPrivKeyInfo, aCert, aIsSign, aIsEncrypt);
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LOCAL, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ALWAYS_SENSITIVE, true));
            list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_NEVER_EXTRACTABLE, true));

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
