using System;
using System.Collections.Generic;
using System.IO;
using iaik.pkcs.pkcs11.wrapper;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops
{
    class CardosOps : PKCS11Ops
    {
        public CardosOps()
            : base(CardType.CARDOS)
        {
        }
        //todo bu metodun imzasında bir parametre eksik gibi kontrol et!(PKCS11Ops ve onun implement ettigi interface de fazladan bir parametre daha var)
        public override void importCertificateAndKey(long aSessionID, String aCertLabel, String aKeyLabel, /*RSAPrivateCrtKey*//*RsaPrivateCrtKeyParameters*/EPrivateKeyInfo aPrivKeyInfo, ECertificate aCert)
        {
            AsymmetricKeyParameter privKey = PrivateKeyFactory.CreateKey(BouncyProviderUtil.ToBouncy(aPrivKeyInfo));  //asn1 to bouncy donusumu yapiliyor...
            if (privKey is RsaPrivateCrtKeyParameters)
            {
                RsaPrivateCrtKeyParameters rsaPrivCrtKey = (RsaPrivateCrtKeyParameters)privKey;
                byte[] modBytes = rsaPrivCrtKey.Modulus.ToByteArray();
                //byte[] id = HafizadaTumKripto.Instance.ozetAl(modBytes, Ozellikler.OZET_SHA1);
                byte[] id = DigestUtil.digest(DigestAlg.SHA1, modBytes);
                bool isSign = false;
                bool isEncrypt = false;
                if (aCert != null)
                {
                    //ESYASertifika sertifika = new ESYASertifika(aCert);
                    //ECertificate sertifika = new ECertificate(aCert);
                    //Asn1BitString ku = sertifika.anahtarKullanimiAl();
                    EKeyUsage ku = aCert.getExtensions().getKeyUsage();

                    if (ku != null)
                    {
                        if (ku.isDigitalSignature())
                            isSign = true;
                        if (ku.isKeyEncipherment() || ku.isDataEncipherment())
                            isEncrypt = true;
                    }
                }
                //Bu kart tipinde privatekey yazilinca,default olarak public keyi kendi yaratiyor.
                //List<CK_ATTRIBUTE_NET> priKeyTemplate = msCardInfoMap[CardType.CARDOS].getRSAPrivateKeyImportTemplate(aKeyLabel, aPrivKeyInfo, aCert, isSign, isEncrypt);
                List<CK_ATTRIBUTE_NET> priKeyTemplate = CardType.CARDOS.getCardTemplate().getRSAPrivateKeyImportTemplate(aKeyLabel, aPrivKeyInfo, aCert, isSign, isEncrypt);
                ePkcs11Library.C_CreateObject(aSessionID, priKeyTemplate.ToArray());

                CK_ATTRIBUTE_NET[] template =
                {
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PUBLIC_KEY),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID,id),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS,modBytes)
                };

                ePkcs11Library.C_FindObjectsInit(aSessionID, template);
                long[] objectList = ePkcs11Library.C_FindObjects(aSessionID, 1);
                ePkcs11Library.C_FindObjectsFinal(aSessionID);
                if (objectList.Length == 0)
                {
                    throw new IOException("Karta public key yazilmamis");
                }

                CK_ATTRIBUTE_NET[] labelTemplate =
                {
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aKeyLabel)
                };


                ePkcs11Library.C_SetAttributeValue(aSessionID, objectList[0], labelTemplate);


                List<CK_ATTRIBUTE_NET> certTemplate = CardType.CARDOS.getCardTemplate().getCertificateTemplate(aCertLabel, aCert);
                ePkcs11Library.C_CreateObject(aSessionID, certTemplate.ToArray());
            }
        }


        public override long[] importRSAKeyPair(long aSessionID, String aLabel, /*RSAPrivateCrtKey*//*RsaPrivateCrtKeyParameters*/EPrivateKeyInfo aPrivKeyInfo, byte[] aSubject, bool aIsSign, bool aIsEncrypt)
        {
            AsymmetricKeyParameter privKey = PrivateKeyFactory.CreateKey(/*KriptoUtils.ToBouncy(aPrivKeyInfo)*/BouncyProviderUtil.ToBouncy(aPrivKeyInfo));  //asn1 to bouncy donusumu yapiliyor...

            long[] objectHandles = new long[2];

            if (privKey is RsaPrivateCrtKeyParameters)
            {
                RsaPrivateCrtKeyParameters rsaPrivCrtKey = (RsaPrivateCrtKeyParameters)privKey;
                List<CK_ATTRIBUTE_NET> priKeyTemplate = CardType.CARDOS.getCardTemplate().getRSAPrivateKeyImportTemplate(aLabel, aPrivKeyInfo, null, aIsSign, aIsEncrypt);
                objectHandles[1] = ePkcs11Library.C_CreateObject(aSessionID, priKeyTemplate.ToArray());

                byte[] modBytes = rsaPrivCrtKey.Modulus.ToByteArray();
                //byte[] id = HafizadaTumKripto.Instance.ozetAl(modBytes, Ozellikler.OZET_SHA1);
                byte[] id = DigestUtil.digest(DigestAlg.SHA1, modBytes);
                byte[] RSA_Public_Exponent = rsaPrivCrtKey.PublicExponent.ToByteArray();

                CK_ATTRIBUTE_NET[] template =
                {
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PUBLIC_KEY),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID,id),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS,modBytes),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PUBLIC_EXPONENT,RSA_Public_Exponent)
                };

                long[] objectList = objeAra(aSessionID, template);
                if (objectList.Length == 0)
                {
                    throw new SmartCardException("Karta public key yazilmamis");
                }

                CK_ATTRIBUTE_NET[] labelTemplate =
                {
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel)
                };

                ePkcs11Library.C_SetAttributeValue(aSessionID, objectList[0], labelTemplate);
            }

            return objectHandles;
        }
    }
}
