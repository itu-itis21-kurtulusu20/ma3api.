using System;
using System.Collections.Generic;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops
{
    class KeyCorpOps : PKCS11Ops
    {
        public KeyCorpOps()
            : base(CardType.KEYCORP)
        {
        }

        /*
        override public void createRSAKeyPair(long aSessionID, String aKeyLabel, int aModulusBits, bool aIsSign, bool aIsEncrypt)
        {
            CK_MECHANISM mech = new CK_MECHANISM();
            mech.mechanism = PKCS11Constants_Fields.CKM_RSA_PKCS_KEY_PAIR_GEN;
            mech.pParameter = null;

            CK_ATTRIBUTE_NET[] pubKeyTemplate = msCardInfoMap[CardType.KEYCORP].getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits, aIsSign, aIsEncrypt).ToArray();
            CK_ATTRIBUTE_NET[] priKeyTemplate = msCardInfoMap[CardType.KEYCORP].getRSAPrivateKeyCreateTemplate(aKeyLabel, aIsSign, aIsEncrypt).ToArray();

            mPKCS11.PKCS11Module.C_GenerateKeyPair(aSessionID, mech, pubKeyTemplate, priKeyTemplate);


            String label = aKeyLabel;
            if (aIsSign)
                label = aKeyLabel + "-S";
            if (aIsEncrypt)
                label = aKeyLabel + "-X";

            CK_ATTRIBUTE_NET[] labelTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, label) };

            long[] keyIDs = objeAra(aSessionID, labelTemplate);

            CK_ATTRIBUTE_NET[] modulusTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS) };

            mPKCS11.PKCS11Module.C_GetAttributeValue(aSessionID, keyIDs[0], modulusTemplate);

            byte[] modulus = (byte[])modulusTemplate[0].pValue;
            //byte[] id = HafizadaTumKripto.Instance.ozetAl(modulus, Ozellikler.OZET_SHA1);
            byte[] id = DigestUtil.digest(DigestAlg.SHA1, modulus);

            CK_ATTRIBUTE_NET[] idTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID, id) };
            mPKCS11.PKCS11Module.C_SetAttributeValue(aSessionID, keyIDs[0], idTemplate);
            mPKCS11.PKCS11Module.C_SetAttributeValue(aSessionID, keyIDs[1], idTemplate);

        }
        */

        public override void importCertificateAndKey(long aSessionID, String aCertLabel, String aKeyLabel, /*RSAPrivateCrtKey*//*RsaPrivateCrtKeyParameters*/EPrivateKeyInfo aPrivKey, ECertificate aSertifika)
        {

            String pubKeyLabel = "";
            bool isSign = false;
            bool isEncrypt = false;
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
                        isSign = true;
                        pubKeyLabel = aKeyLabel + "-S";
                    }
                    else if (ku.isKeyEncipherment() || ku.isDataEncipherment())
                    {
                        pubKeyLabel = aKeyLabel + "-X";
                        isEncrypt = true;
                    }
                }
            }

            List<CK_ATTRIBUTE_NET> priKeyTemplate = CardType.KEYCORP.getCardTemplate().getRSAPrivateKeyImportTemplate(pubKeyLabel, aPrivKey, aSertifika, isSign, isEncrypt);
            List<CK_ATTRIBUTE_NET> pubKeyTemplate = CardType.KEYCORP.getCardTemplate().getRSAPublicKeyImportTemplate(pubKeyLabel, aPrivKey, aSertifika, isSign, isEncrypt);

            ePkcs11Library.C_CreateObject(aSessionID, priKeyTemplate.ToArray());
            ePkcs11Library.C_CreateObject(aSessionID, pubKeyTemplate.ToArray());

            String dataLabel = "SPM-CTR-001";

            CK_ATTRIBUTE_NET[] searchTemplate =
            {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_DATA),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,"SPM-CTR-001"),
            };

            long[] objectList = objeAra(aSessionID, searchTemplate);

            if (objectList.Length == 0)
            {
                CK_ATTRIBUTE_NET[] objectTemplate =
                {
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE,pubKeyLabel),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_DATA),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,false),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,dataLabel)
                };

                ePkcs11Library.C_CreateObject(aSessionID, objectTemplate);
            }

            List<CK_ATTRIBUTE_NET> certTemplate = CardType.KEYCORP.getCardTemplate().getCertificateTemplate(aCertLabel, aSertifika);
            ePkcs11Library.C_CreateObject(aSessionID, certTemplate.ToArray());
        }
    }
}
