using System;
using System.Collections.Generic;
using System.Reflection;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using iaik.pkcs.pkcs11.wrapper;
using log4net;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
using tr.gov.tubitak.uekae.esya.asn.algorithms;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    public abstract class CardTemplate : ICardTemplate
    {
        protected CardType cardType;
        protected IPKCS11Ops mIslem;
        protected Object PKCS11GetLock = new Object();


        protected ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);


        public CardTemplate(CardType aCardType)
        {
            cardType = aCardType;
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.AKILLIKART);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

        public abstract string[] getATRHashes();
        public abstract IPKCS11Ops getPKCS11Ops();
        public CardType getCardType()
        {
            return cardType;
        }



        public virtual List<List<CK_ATTRIBUTE_NET>> getCertSerialNumberTemplates(byte[] aSerialNumber)
        {
            List<List<CK_ATTRIBUTE_NET>> bigList = new List<List<CK_ATTRIBUTE_NET>>();

            CK_ATTRIBUTE_NET classAttr = new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_CERTIFICATE);
            CK_ATTRIBUTE_NET tokenAttr = new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true);


            List<CK_ATTRIBUTE_NET> list1 = new List<CK_ATTRIBUTE_NET>();
            list1.Add(classAttr);
            list1.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER, aSerialNumber));
            list1.Add(tokenAttr);
            bigList.Add(list1);

            //TCard hatası, en baştaki byte sıfırı tek karakter ile temsil etmiş
            //Küçük eşit ve küçük farkı var.

            String hexSerial2 = getHexString(aSerialNumber);
            for (int i = 0; i < aSerialNumber.Length * 2 - hexSerial2.Length; i++)
            {
                hexSerial2 = '0' + hexSerial2;
            }

            byte[] asciiSerial2 = null;
            try
            {
                //asciiSerial2 = hexSerial2.getBytes("ASCII");
                asciiSerial2 = ASCIIEncoding.ASCII.GetBytes(hexSerial2);
            }
            catch (Exception e)
            {
                throw new SystemException(e.Message, e);
            }

            byte[] asciiTLV2 = new byte[asciiSerial2.Length + 4];
            asciiTLV2[0] = 0x02; //Tag
            asciiTLV2[1] = (byte)(asciiSerial2.Length + 2); // length
            asciiTLV2[2] = Convert.ToByte('0');
            asciiTLV2[3] = Convert.ToByte('x');
            Array.Copy(asciiSerial2, 0, asciiTLV2, 4, asciiSerial2.Length);

            List<CK_ATTRIBUTE_NET> list2 = new List<CK_ATTRIBUTE_NET>();
            list2.Add(classAttr);
            list2.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER, asciiTLV2));
            list2.Add(tokenAttr);
            bigList.Add(list2);

            //Dogrusu
            String hexSerial = getHexString(aSerialNumber);

            for (int i = 0; i <= aSerialNumber.Length * 2 - hexSerial.Length; i++)
            {
                hexSerial = '0' + hexSerial;
            }

            byte[] asciiSerial = null;
            try
            {
                //asciiSerial = hexSerial.getBytes("ASCII");
                asciiSerial = ASCIIEncoding.ASCII.GetBytes(hexSerial);
            }
            catch (Exception e)
            {
                throw new SystemException("TKART icin getCertSerialNumberTemplates() serial conversion problem", e);
            }

            byte[] asciiTLV = new byte[asciiSerial.Length + 4];
            asciiTLV[0] = 0x02; //Tag
            asciiTLV[1] = (byte)(asciiSerial.Length + 2); // length
            asciiTLV[2] = Convert.ToByte('0');
            asciiTLV[3] = Convert.ToByte('x');
            Array.Copy(asciiSerial, 0, asciiTLV, 4, asciiSerial.Length);

            List<CK_ATTRIBUTE_NET> list3 = new List<CK_ATTRIBUTE_NET>();
            list3.Add(classAttr);
            list3.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER, asciiTLV));
            list3.Add(tokenAttr);
            bigList.Add(list3);


            //E-Güven Aladdin Kart
            byte[] TLV = new byte[aSerialNumber.Length + 2];
            TLV[0] = 2;
            TLV[1] = (byte)aSerialNumber.Length;
            Array.Copy(aSerialNumber, 0, TLV, 2, aSerialNumber.Length);

            List<CK_ATTRIBUTE_NET> list4 = new List<CK_ATTRIBUTE_NET>();
            list4.Add(classAttr);
            list4.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER, TLV));
            list4.Add(tokenAttr);
            bigList.Add(list4);


            //NCipher'ın kendi uygulaması ile yüklenen sertifikaların en başında işaret byte'ı 00 olmuyor.
            if (aSerialNumber[0] == 0x00)
            {
                byte[] serialWithoutZero = new byte[aSerialNumber.Length - 1];
                Array.Copy(aSerialNumber, 1, serialWithoutZero, 0, serialWithoutZero.Length);

                List<CK_ATTRIBUTE_NET> list5 = new List<CK_ATTRIBUTE_NET>();
                list5.Add(classAttr);
                list5.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER, serialWithoutZero));
                list5.Add(tokenAttr);

                bigList.Add(list5);
            }


            return bigList;
        }
        private String getHexString(byte[] aSerialNumber)
        {
            BigInteger bigInt1 = new BigInteger();
            bigInt1.SetData(aSerialNumber);
            String hexSerial = bigInt1.ToString(16);
            while (hexSerial.StartsWith("0")) //for java compliance
            {
                hexSerial = hexSerial.Remove(0, 1);
            }
            return hexSerial;
        }

        public virtual List<CK_ATTRIBUTE_NET> getRSAPrivateKeyCreateTemplate(String aKeyLabel, bool aIsSign,
                                                                             bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> attributeList = new List<CK_ATTRIBUTE_NET>();

            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,
                                                   PKCS11Constants_Fields.CKO_PRIVATE_KEY));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_KEY_TYPE, PKCS11Constants_Fields.CKK_RSA));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aKeyLabel));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, true));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_DECRYPT, aIsEncrypt));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SIGN, aIsSign));

            return attributeList;
        }


        public virtual List<CK_ATTRIBUTE_NET> getRSAPublicKeyCreateTemplate(String aKeyLabel, int aModulusBits,
                                                                            bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> attributeList = new List<CK_ATTRIBUTE_NET>();

            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,
                                                   PKCS11Constants_Fields.CKO_PUBLIC_KEY));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_KEY_TYPE, PKCS11Constants_Fields.CKK_RSA));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aKeyLabel));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, false));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PUBLIC_EXPONENT,
                                                   new byte[] { 0x01, 0x00, 0x01 }));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ENCRYPT, aIsEncrypt));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VERIFY, aIsSign));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS_BITS, aModulusBits));
            return attributeList;
        }

        public virtual List<CK_ATTRIBUTE_NET> getRSAPrivateKeyImportTemplate(String aLabel, EPrivateKeyInfo aPrivKeyInfo, ECertificate aSertifika, bool aIsSign, bool aIsEncrypt)
        {
            AsymmetricKeyParameter privKey = PrivateKeyFactory.CreateKey(BouncyProviderUtil.ToBouncy(aPrivKeyInfo));

            if (privKey is RsaPrivateCrtKeyParameters)
            {
                RsaPrivateCrtKeyParameters privCrtKey = (RsaPrivateCrtKeyParameters)privKey;

                List<CK_ATTRIBUTE_NET> attributeList = new List<CK_ATTRIBUTE_NET>();

                byte[] modBytes = privCrtKey.Modulus.ToByteArrayUnsigned();
                byte[] RSA_Public_Exponent = privCrtKey.PublicExponent.ToByteArrayUnsigned();
                byte[] RSA_Private_Exponent = privCrtKey.Exponent.ToByteArrayUnsigned();
                byte[] RSA_Prime_1 = privCrtKey.P.ToByteArrayUnsigned(); //p
                byte[] RSA_Prime_2 = privCrtKey.Q.ToByteArrayUnsigned(); //q
                byte[] RSA_Exponent_1 = privCrtKey.DP.ToByteArrayUnsigned(); //dp
                byte[] RSA_Exponent_2 = privCrtKey.DQ.ToByteArrayUnsigned(); //dq
                byte[] RSA_Coefficient = privCrtKey.QInv.ToByteArrayUnsigned(); //qInv

                byte[] id = DigestUtil.digest(DigestAlg.SHA1, modBytes);

                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_PRIVATE_KEY));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_KEY_TYPE, PKCS11Constants_Fields.CKK_RSA));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, true));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SENSITIVE, true));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS, modBytes));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PUBLIC_EXPONENT, RSA_Public_Exponent));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE_EXPONENT, RSA_Private_Exponent));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIME_1, RSA_Prime_1));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIME_2, RSA_Prime_2));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EXPONENT_1, RSA_Exponent_1));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EXPONENT_2, RSA_Exponent_2));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_COEFFICIENT, RSA_Coefficient));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID, id));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SIGN, aIsSign));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_DECRYPT, aIsEncrypt));
                if (aSertifika != null)
                {
                    byte[] subject = aSertifika.getSubject().getBytes();
                    attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SUBJECT, subject));
                }

                return attributeList;
            }
            else
            {
                throw new Exception("Metoda verilen ozel anahtar RsaPrivateCrtKey tipinde degil!, Tipi:  " + privKey.GetType());
            }
        }

        public virtual List<CK_ATTRIBUTE_NET> getRSAPublicKeyImportTemplate(String aLabel, EPrivateKeyInfo aPrivKeyInfo, ECertificate aSertifika, bool aIsSign, bool aIsEncrypt)
        {
            AsymmetricKeyParameter privKey = PrivateKeyFactory.CreateKey(BouncyProviderUtil.ToBouncy(aPrivKeyInfo));

            if (privKey is RsaPrivateCrtKeyParameters)
            {
                RsaPrivateCrtKeyParameters privCrtKey = (RsaPrivateCrtKeyParameters)privKey;
                List<CK_ATTRIBUTE_NET> attributeList = new List<CK_ATTRIBUTE_NET>();

                byte[] modBytes = privCrtKey.Modulus.ToByteArrayUnsigned();
                byte[] RSA_Public_Exponent = privCrtKey.PublicExponent.ToByteArrayUnsigned();

                byte[] id = DigestUtil.digest(DigestAlg.SHA1, modBytes);

                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_PUBLIC_KEY));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_KEY_TYPE, PKCS11Constants_Fields.CKK_RSA));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, false));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS, modBytes));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PUBLIC_EXPONENT, RSA_Public_Exponent));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID, id));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VERIFY, aIsSign));
                attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ENCRYPT, aIsEncrypt));

                return attributeList;
            }
            else
            {
                throw new Exception("Metoda verilen ozel anahtar RsaPrivateCrtKey tipinde degil!, Tipi:  " + privKey.GetType());
            }
        }


        public List<CK_ATTRIBUTE_NET> getCertificateTemplate(String aLabel, ECertificate aCert)
        {
            //Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer(); 
            //aCert.Encode(enc);
            byte[] value = aCert.getBytes(); //enc.MsgCopy;

            List<CK_ATTRIBUTE_NET> attributeList = new List<CK_ATTRIBUTE_NET>();

            byte[] serialNo = aCert.getSerialNumber().GetData(); //aCert.tbsCertificate.serialNumber.mValue.GetData();
            byte[] issuer = aCert.getIssuer().getBytes(); //UtilName.name2byte(aCert.tbsCertificate.issuer);
            byte[] subject = aCert.getSubject().getBytes(); //UtilName.name2byte(aCert.tbsCertificate.subject);

            //CKA_ID degeri olarak,eger rsa anahtari ise public key in modulus unun, digerleri icin tbsCertificate.subjectPublicKeyInfo.subjectPublicKey.value nun sha1
            //hashi alinir
            byte[] idVeri = null;

            if (aCert.getPublicKeyAlgorithm().Equals(new EAlgorithmIdentifier(_algorithmsValues.rsaEncryption)))
            {
                ERSAPublicKey rsaPublicKey = new ERSAPublicKey(aCert.getSubjectPublicKeyInfo().getSubjectPublicKey());
                Org.BouncyCastle.Math.BigInteger modulusBigInteger =
                    new Org.BouncyCastle.Math.BigInteger(rsaPublicKey.getModulus().mValue.GetData());
                idVeri = modulusBigInteger.ToByteArrayUnsigned();
            }
            else
            {
                idVeri = aCert.getSubjectPublicKeyInfo().getSubjectPublicKey();
            }

            //byte[] id = HafizadaTumKripto.Instance.ozetAl(idVeri, "SHA1");
            byte[] id = DigestUtil.digest(DigestAlg.SHA1, idVeri);

            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,
                                                   PKCS11Constants_Fields.CKO_CERTIFICATE));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CERTIFICATE_TYPE,
                                                   PKCS11Constants_Fields.CKC_X_509));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, false));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ISSUER, issuer));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER, serialNo));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE, value));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID, id));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SUBJECT, subject));
            attributeList.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODIFIABLE, true));

            return attributeList;
        }
       

        public virtual void applyTemplate(SecretKeyTemplate template)
        {

        }

        /*  //BU METODA GEREK YOK!?
            protected byte[] toByteArray(BigInteger aX)
            {
                 byte[] xx = aX.toByteArray();
                 if (xx[0] == 0)
                 {
                      byte[] temp = new byte[xx.length - 1];
                      System.arraycopy(xx,
                                       1,
                                       temp,
                                       0,
                                       temp.length);
                      xx = temp;
                 }
                 return xx;
            }
               */
    }
}