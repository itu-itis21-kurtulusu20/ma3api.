using System;
using System.Collections.Generic;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    class KeyCorpTemplate : CardTemplate
    {
        protected static readonly List<String> ATR_HASHES = new List<String>();

        static KeyCorpTemplate()
    	{
    	   // ATR_HASHES.Add("3BB79400C03E31FE6553504B32339000AE");
    	}

        public KeyCorpTemplate()
            : base(CardType.KEYCORP)
        {
        }
      
        public override IPKCS11Ops getPKCS11Ops()
        {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new KeyCorpOps();
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

        public override List<CK_ATTRIBUTE_NET> getRSAPrivateKeyImportTemplate(String aLabel, /*RSAPrivateCrtKey*//*RsaPrivateCrtKeyParameters*/EPrivateKeyInfo aPrivKeyInfo, ECertificate aSertifika, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> list = base.getRSAPublicKeyImportTemplate(aLabel, aPrivKeyInfo, aSertifika, aIsSign, aIsEncrypt);

            String label = aLabel;
            if (aSertifika != null)
            {

                //ESYASertifika sertifika = new ESYASertifika(aSertifika);      
                //ECertificate sertifika = new ECertificate(aSertifika);                
                //Asn1BitString ku = sertifika.anahtarKullanimiAl();
                EKeyUsage ku = aSertifika.getExtensions().getKeyUsage();

                if (ku != null)
                {
                    if (ku.isDigitalSignature())
                    {
                        label = label + "-S";
                        list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel + "-S"));
                    }
                    else if (ku.isKeyEncipherment() || ku.isDataEncipherment())
                    {
                        label = label + "-X";
                        list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel + "-X"));
                    }
                }
            }

            foreach (CK_ATTRIBUTE_NET attr in list)
            {
                if (attr.type == PKCS11Constants_Fields.CKA_LABEL)
                    attr.pValue = label;
            }


            return list;
        }


        public override List<CK_ATTRIBUTE_NET> getRSAPublicKeyImportTemplate(String aLabel,/*RSAPrivateCrtKey*//*RsaPrivateCrtKeyParameters*/EPrivateKeyInfo aPrivKeyInfo, ECertificate aSertifika, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> list = base.getRSAPublicKeyImportTemplate(aLabel, aPrivKeyInfo, aSertifika, aIsSign, aIsEncrypt);

            String label = aLabel;
            if (aSertifika != null)
            {
                //ESYASertifika sertifika = new ESYASertifika(aSertifika);
                //ECertificate sertifika = new ECertificate(aSertifika);
                //Asn1BitString ku = sertifika.anahtarKullanimiAl();
                EKeyUsage ku = aSertifika.getExtensions().getKeyUsage();

                if (ku != null)
                {
                    if (ku.isDigitalSignature())
                    {
                        label = label + "-S";
                        list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel + "-S"));
                    }
                    else if (ku.isKeyEncipherment() || ku.isDataEncipherment())
                    {
                        label = label + "-X";
                        list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel + "-X"));
                    }
                }
            }

            foreach (CK_ATTRIBUTE_NET attr in list)
            {
                if (attr.type == PKCS11Constants_Fields.CKA_LABEL)
                    attr.pValue = label;
            }

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
