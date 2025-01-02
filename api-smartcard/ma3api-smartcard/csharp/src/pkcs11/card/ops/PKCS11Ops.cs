using System;
using System.Collections.Generic;
using System.Text;
using Com.Objsys.Asn1.Runtime;

using iaik.pkcs.pkcs11.wrapper;

using Net.Pkcs11Interop.Common;
using Net.Pkcs11Interop.LowLevelAPI41;


using Org.BouncyCastle.Asn1.X9;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.X509;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
//asnWrappers
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.ec;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;
using tr.gov.tubitak.uekae.esya.api.smartcard.src.util;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.src.api.asn.algorithms;
using Asn1OctetString = Com.Objsys.Asn1.Runtime.Asn1OctetString;
using CK_ATTRIBUTE = Net.Pkcs11Interop.LowLevelAPI41.CK_ATTRIBUTE;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops
{
    public class PKCS11Ops : IPKCS11Ops
    {
        protected EPkcs11Library ePkcs11Library;

        private readonly CardType mCardType;

        static PKCS11Ops()
        { }


        public PKCS11Ops(CardType aKartTip)
        {
            mCardType = aKartTip;
        }

        public void initialize()
        {
            ePkcs11Library = new EPkcs11Library(mCardType.getLibName());

            ePkcs11Library.C_Initialize(null);
        }

        public EPkcs11Library getPkcs11Library()
        {
            return ePkcs11Library;
        }

        public long[] getTokenPresentSlotList()
        {
            return ePkcs11Library.C_GetSlotList(true);
        }

        public long[] getSlotList()
        {
            return ePkcs11Library.C_GetSlotList(false);
        }

        public iaik.pkcs.pkcs11.wrapper.CK_SLOT_INFO getSlotInfo(long aSlotID)
        {
            return ePkcs11Library.C_GetSlotInfo(aSlotID);
        }

        public bool isTokenPresent(long aSlotID)
        {
            return ((getSlotInfo(aSlotID).flags & CKF.CKF_TOKEN_PRESENT) != 0);
        }

        public iaik.pkcs.pkcs11.wrapper.CK_TOKEN_INFO getTokenInfo(long aSlotID)
        {
            return ePkcs11Library.C_GetTokenInfo(aSlotID);
        }

        public iaik.pkcs.pkcs11.wrapper.CK_SESSION_INFO getSessionInfo(long aSessionID)
        {
            return ePkcs11Library.C_GetSessionInfo(aSessionID);
        }

        public long[] getMechanismList(long aSlotID)
        {
            return ePkcs11Library.C_GetMechanismList(aSlotID);
        }

        public long openSession(long aSlotID)
        {
            return ePkcs11Library.C_OpenSession(aSlotID, PKCS11Constants_Fields.CKF_SERIAL_SESSION | PKCS11Constants_Fields.CKF_RW_SESSION);
        }

        public void closeSession(long aSessionID)
        {
            ePkcs11Library.C_CloseSession(aSessionID);
        }

        public void login(long aSessionID, String aCardPIN)
        {
            ePkcs11Library.C_Login(aSessionID, (long)CKU.CKU_USER, aCardPIN);
        }

        public void logout(long aSessionID)
        {
            ePkcs11Library.C_Logout(aSessionID);
        }

        public bool isAnyObjectExist(long aSessionID)
        {
            CK_ATTRIBUTE_NET[] template = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true) };

            long[] bulunanObjeler = objeAra(aSessionID, template);
            if (bulunanObjeler.Length == 0)
                return false;
            return true;
        }

        public void importCertificate(long aSessionID, String aCertLabel, ECertificate aSertifika)
        {
            List<CK_ATTRIBUTE_NET> template = mCardType.getCardTemplate().getCertificateTemplate(aCertLabel, aSertifika);
            ePkcs11Library.C_CreateObject(aSessionID, template.ToArray());

        }

        public virtual long[] createRSAKeyPair(long aSessionID, String aKeyLabel, int aModulusBits, bool aIsSign, bool aIsEncrypt)
        {
            iaik.pkcs.pkcs11.wrapper.CK_MECHANISM mech = new iaik.pkcs.pkcs11.wrapper.CK_MECHANISM();
            mech.mechanism = PKCS11Constants_Fields.CKM_RSA_PKCS_KEY_PAIR_GEN;
            mech.pParameter = null;

            ICardTemplate kartBilgi = mCardType.getCardTemplate();

            CK_ATTRIBUTE_NET[] pubKeyTemplate = kartBilgi.getRSAPublicKeyCreateTemplate(aKeyLabel, aModulusBits, aIsSign, aIsEncrypt).ToArray();
            CK_ATTRIBUTE_NET[] priKeyTemplate = kartBilgi.getRSAPrivateKeyCreateTemplate(aKeyLabel, aIsSign, aIsEncrypt).ToArray();

            long[] objectHandles = ePkcs11Library.C_GenerateKeyPair(aSessionID, mech, pubKeyTemplate, priKeyTemplate);

            CK_ATTRIBUTE_NET[] labelTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aKeyLabel) };

            long[] keyIDs = objeAra(aSessionID, labelTemplate);
            iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] modulusTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS) };

            ePkcs11Library.C_GetAttributeValue(aSessionID, keyIDs[0], modulusTemplate);

            byte[] modulus = (byte[])modulusTemplate[0].pValue;
            byte[] id = DigestUtil.digest(DigestAlg.SHA1, modulus);

            CK_ATTRIBUTE_NET[] idTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID, id) };

            ePkcs11Library.C_SetAttributeValue((uint)aSessionID, (uint)keyIDs[0], idTemplate);
            ePkcs11Library.C_SetAttributeValue((uint)aSessionID, (uint)keyIDs[1], idTemplate);

            return objectHandles;
        }

        public ESubjectPublicKeyInfo createECKeyPair(long aSessionID, String aKeyLabel, EECParameters ecParameters, bool aIsSign, bool aIsEncrypt)
        {
            iaik.pkcs.pkcs11.wrapper.CK_MECHANISM mech = new iaik.pkcs.pkcs11.wrapper.CK_MECHANISM();
            mech.mechanism = PKCS11Constants_Fields.CKM_ECDSA_KEY_PAIR_GEN;
            mech.pParameter = null;

            List<CK_ATTRIBUTE> pubKeyTemplate = new List<CK_ATTRIBUTE>();

            pubKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_TOKEN, true));
            pubKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_PRIVATE, false));
            pubKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_CLASS, CKO.CKO_PUBLIC_KEY));
            pubKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_DERIVE, aIsEncrypt));
            pubKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_VERIFY, aIsSign));
            pubKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_EC_PARAMS, ecParameters.getEncoded()));
            pubKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_LABEL, aKeyLabel));

            List<CK_ATTRIBUTE> priKeyTemplate = new List<CK_ATTRIBUTE>();

            priKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_TOKEN, true));
            priKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_PRIVATE, true));
            priKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_CLASS, CKO.CKO_PRIVATE_KEY));
            priKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_DERIVE, aIsEncrypt));
            priKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_SIGN, aIsSign));
            priKeyTemplate.Add(CkaUtils.CreateAttribute(CKA.CKA_LABEL, aKeyLabel));

            uint pubKeyId = 0, priKeyId = 0;
            ePkcs11Library.C_GenerateKeyPair((uint)aSessionID, mech, pubKeyTemplate.ToArray(), priKeyTemplate.ToArray());

            updateECKeyIDs(aSessionID, aKeyLabel);

            return readECPublicKey(aSessionID, aKeyLabel);
        }

        //todo dönüş tipi belirlenmeli, readRSAPublicKey gibi mi olacak??
        virtual public ESubjectPublicKeyInfo readECPublicKey(long aSessionID, String aLabel)
        {
            CK_ATTRIBUTE_NET[] template =
            {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PUBLIC_KEY),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,false),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel)
           };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                throw new SmartCardException(aLabel + " isimli anahtar kartta yok.");
            }

            CK_ATTRIBUTE_NET[] publicKeyTemplate =
            {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EC_PARAMS),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EC_POINT)
            };

            ePkcs11Library.C_GetAttributeValue(aSessionID, objectList[0], publicKeyTemplate);

            byte[] params_ = (byte[])publicKeyTemplate[0].pValue;
            byte[] point = (byte[])publicKeyTemplate[1].pValue;

            EcpkParameters ecpk = new EcpkParameters();
            try
            {
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(params_);
                ecpk.Decode(decBuf);
            }
            catch (Exception aEx)
            {
                throw new SmartCardException("Okunan EC_PARAMS degeri decode edilirken hata olustu", aEx);
            }

            int c = ecpk.ChoiceID;
            ECDomainParameters parameters = null;

            if (c == EcpkParameters._NAMEDCURVE)
            {
                X9ECParameters x9ecParameters = BouncyProviderUtil.GetX9ECParameters((Asn1ObjectIdentifier)ecpk.GetElement());
                parameters = new ECDomainParameters(x9ecParameters.Curve, x9ecParameters.G, x9ecParameters.N, x9ecParameters.H, x9ecParameters.GetSeed());
            }
            else if (c == EcpkParameters._ECPARAMETERS)
            {
                EECParameters ecParameters = new EECParameters(params_);
                parameters = BouncyKeyPairGenerator.toBouncyECParameters(ecParameters.getObject());
            }
            else
            {
                throw new SmartCardException("Yalnızca NAMEDCURVE ve ECPARAMETERS desteklenmektedir..");
            }

            Asn1DerDecodeBuffer decodeBuf = new Asn1DerDecodeBuffer(point);
            Asn1OctetString s = new Asn1OctetString();
            try
            {
                s.Decode(decodeBuf);
            }
            catch (Exception aEx)
            {
                throw new Asn1Exception("Public Key in okunan encoded point degeri decode edilirken hata olustu", aEx);
            }

            ECPublicKeyParameters pubKey = new ECPublicKeyParameters(
                parameters.Curve.DecodePoint(s.mValue), // Q
                parameters);

            return BouncyProviderUtil.ToAsn1(SubjectPublicKeyInfoFactory.CreateSubjectPublicKeyInfo(pubKey));
        }

        private void updateECKeyIDs(long aSessionID, string aKeyLabel)
        {
            CK_ATTRIBUTE_NET[] pubTemplate =
            { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aKeyLabel),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PUBLIC_KEY)
            };

            long[] keyID = objeAra(aSessionID, pubTemplate);

            CK_ATTRIBUTE_NET[] pointTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EC_POINT) };

            ePkcs11Library.C_GetAttributeValue(aSessionID, keyID[0], pointTemplate);

            byte[] point = (byte[])pointTemplate[0].pValue;
            byte[] spkey = getPointValue(point);


            CK_ATTRIBUTE_NET[] labelTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aKeyLabel) };

            long[] keyIDs = objeAra(aSessionID, labelTemplate);

            byte[] id = DigestUtil.digest(DigestAlg.SHA1, spkey);

            CK_ATTRIBUTE_NET[] idTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID, id) };

            ePkcs11Library.C_SetAttributeValue(aSessionID, keyIDs[0], idTemplate);
            ePkcs11Library.C_SetAttributeValue(aSessionID, keyIDs[1], idTemplate);
        }

        public byte[] getPointValue(byte[] aDerEncodedPoint)
        {
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aDerEncodedPoint);
            Asn1OctetString octet = new Asn1OctetString();
            try
            {
                octet.Decode(decBuf);
            }
            catch (Exception aEx)
            {
                throw new SmartCardException("ID hesaplamasi icin point decode edilirken hata olustu", aEx);
            }

            return new Asn1BitString(octet.mValue.Length << 3, octet.mValue).mValue;
        }

        /*
         * akisde serial number ile ilgili sorun var(serial number in BigInteger halinin String radix 16 a cevrilmis halini buluyor).
         * datakey de dkck232 ile calismiyo,dkck201 ile calisiyo
         */
        /*
         * login olunmasi gerekir.
         */
        /*
         * esyajni ile ayni. //TODO Seri numaralari arasinda TLV li yok
         */
        public virtual byte[] signDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM aMechanism, byte[] aImzalanacak)
        {
            byte[] id = _getCertificateId(aSessionID, aSerialNumber);

            CK_ATTRIBUTE_NET[] privateKeyTemplate =
            {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID,id),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true)
            };

            long[] keyList = objeAra(aSessionID, privateKeyTemplate);

            if (keyList.Length == 0)
            {
                throw new SmartCardException("Verilen seri numarasina sahip sertifikayla ayni ID ye sahip ozel anahtar kartta bulunamadi.");
            }

            ePkcs11Library.C_SignInit(aSessionID, aMechanism, keyList[0]);

            byte[] imzali = ePkcs11Library.C_Sign(aSessionID, aImzalanacak);
            return imzali;
        }

        public byte[] signDataWithKeyID(long aSessionID, long aKeyID, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM aMechanism, byte[] aImzalanacak)
        {
            ePkcs11Library.C_SignInit(aSessionID, aMechanism, aKeyID);

            byte[] imzali = ePkcs11Library.C_Sign(aSessionID, aImzalanacak);
            return imzali;
        }

        public byte[] decryptDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM mech, byte[] aData)
        {
            byte[] cozulecek = _fixLength(aData);

            List<List<CK_ATTRIBUTE_NET>> list = mCardType.getCardTemplate().getCertSerialNumberTemplates(aSerialNumber);

            long[] objectList = null;

            foreach (List<CK_ATTRIBUTE_NET> tList in list)
            {
                objectList = objeAra(aSessionID, tList.ToArray());
                if (objectList.Length > 0) break;
            }

            if (objectList == null || objectList.Length == 0)
            {
                throw new SmartCardException("Verilen seri numarali sertifika kartta bulunamadi.");
            }

            CK_ATTRIBUTE_NET[] idTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID) };

            ePkcs11Library.C_GetAttributeValue(aSessionID, objectList[0], idTemplate);
            byte[] id = (byte[])idTemplate[0].pValue;

            CK_ATTRIBUTE_NET[] privateKeyTemplate =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID,id),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true)
        };

            long[] keyList = objeAra(aSessionID, privateKeyTemplate);

            if (keyList.Length == 0)
            {
                throw new SmartCardException("Verilen seri numarasina sahip sertifikayla ayni ID ye sahip ozel anahtar kartta bulunamadi.");
            }

            ePkcs11Library.C_DecryptInit(aSessionID, mech, keyList[0]);
            byte[] sonuc = ePkcs11Library.C_Decrypt(aSessionID, cozulecek, 1024);
            return sonuc;
        }


        /**
         * Akıllı kart içerisindeki sertifikaları getirir.
         */
        public List<byte[]> getCertificates(long aSessionID)
        {
            CK_ATTRIBUTE_NET[] template = 
            {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_CERTIFICATE)
            };

            long[] objectList = objeAra(aSessionID, template);

            List<byte[]> certs = new List<byte[]>();

            foreach (long handle in objectList)
            {
                CK_ATTRIBUTE_NET[] values = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE) };
                ePkcs11Library.C_GetAttributeValue(aSessionID, handle, values);
                byte[] value = (byte[])values[0].pValue;
                certs.Add(value);
            }

            return certs;
        }

        /**
         * keyusage da digitalSignature alaninin set edilmis olmasina bakar.
         * @param aSlotID
         * @param aSessionID
         * @return
         * @throws PKCS11Exception
         * @throws IOException
         * @throws Asn1Exception
         */
        public List<byte[]> getSignatureCertificates(long aSessionID)
        {
            return getSignatureEncryptionCertificates(aSessionID, true);
        }


        /**
         * keyusage da keyEncipherment alaninin set edilmis olmasina bakar.
         * @param aSlotID
         * @param aSessionID
         * @return
         * @throws PKCS11Exception
         * @throws IOException
         * @throws Asn1Exception
         */
        public List<byte[]> getEncryptionCertificates(long aSessionID)
        {
            return getSignatureEncryptionCertificates(aSessionID, false);
        }


        private List<byte[]> getSignatureEncryptionCertificates(long aSessionID, bool aSign)
        {
            List<byte[]> certificates = getCertificates(aSessionID);
            List<byte[]> filteredCerts = new List<byte[]>();

            foreach (byte[] value in certificates)
            {

                //ESYASertifika sertifika = new ESYASertifika(value);
                ECertificate sertifika = new ECertificate(value);
                //Asn1BitString ku = sertifika.anahtarKullanimiAl();
                EKeyUsage ku = sertifika.getExtensions().getKeyUsage();
                if (ku != null)
                {
                    if (aSign)
                    {
                        bool isSign = ku.isDigitalSignature();
                        if (isSign)
                            filteredCerts.Add(value);
                    }
                    else
                    {
                        bool isEncrypt = (ku.isKeyEncipherment() || ku.isDataEncipherment());
                        if (isEncrypt)
                            filteredCerts.Add(value);
                    }
                }
            }
            return filteredCerts;
        }

        //@Override
        public String[] getSignatureKeyLabels(long aSessionID)
        {
            CK_ATTRIBUTE_NET[] template =
        {
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PRIVATE_KEY),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SIGN,true)
        };

            return getLabels(aSessionID, template);
        }

        //@Override
        public String[] getEncryptionKeyLabels(long aSessionID)
        {
            CK_ATTRIBUTE_NET[] template =
        {
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PRIVATE_KEY),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ENCRYPT,true)
        };

            return getLabels(aSessionID, template);
        }

        private String[] getLabels(long aSessionID, iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] aTemplate)
        {
            long[] objectList = objeAra(aSessionID, aTemplate);

            List<String> labels = new List<String>();

            CK_ATTRIBUTE_NET[] values = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL) };

            foreach (long handle in objectList)
            {
                ePkcs11Library.C_GetAttributeValue(aSessionID, handle, values);
                String label = AttributeUtil.getStringValue(values[0].pValue);
                labels.Add(label);
            }
            return labels.ToArray();
        }


        /*
         * kartta private alanda arama icin, login olunmasi gerekir
         */
        public bool isObjectExist(long aSessionID, String aLabel)
        {
            CK_ATTRIBUTE_NET[] template =
        {
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel)
        };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length > 0)
                return true;
            return false;
        }


        private bool _isObjectExist(long aSessionID, String aLabel, long aDataType)
        {
            CK_ATTRIBUTE_NET[] template =
        {
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,aDataType)
        };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length > 0)
                return true;
            return false;
        }



        private void _writeData(long aSessionID, String aLabel, byte[] aData, bool aIsPrivate)
        {
            CK_ATTRIBUTE_NET[] template =
        {
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_DATA),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE,aData),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,aIsPrivate),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODIFIABLE,true)
        };

            ePkcs11Library.C_CreateObject(aSessionID, template);
        }


        /*
         * login olunmasi gerekir. CKO_DATA tipinde nesne yazar.
         */
        public void writePrivateData(long aSessionID, String aLabel, byte[] aData)
        {
            _writeData(aSessionID, aLabel, aData, true);
        }

        /*
         * akisde login olunmasi gerekir. CKO_DATA tipinde nesne yazar.
         */
        public void writePublicData(long aSessionID, String aLabel, byte[] aData)
        {
            _writeData(aSessionID, aLabel, aData, false);
        }

        private List<byte[]> _readData(long aSessionID, String aLabel, bool aIsPrivate, long aDataType)
        {
            CK_ATTRIBUTE_NET[] template =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,aDataType),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,aIsPrivate),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel),
        };

            long[] objectList = objeAra(aSessionID, template);
            int objectCount = objectList.Length;

            if (objectCount == 0)
            {
                throw new SmartCardException(aLabel + " isimli nesne kartta bulunamadi.");
            }

            CK_ATTRIBUTE_NET[] valueTemplate =
        {
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE)
        };

            List<byte[]> objectValues = new List<byte[]>();

            for (int i = 0; i < objectCount; i++)
            {
                ePkcs11Library.C_GetAttributeValue(aSessionID, objectList[i], valueTemplate);
                byte[] data = (byte[])valueTemplate[0].pValue;
                objectValues.Add(data);
            }

            return objectValues;
        }


        /*
         * login olunmasi gerekir.CKO_DATA tipindeki nesneleri okur.
         */
        public List<byte[]> readPrivateData(long aSessionID, String aLabel)
        {
            return _readData(aSessionID, aLabel, true, PKCS11Constants_Fields.CKO_DATA);
        }


        /*
         * CKO_DATA tipindeki nesneleri okur.
         */
        public List<byte[]> readPublicData(long aSessionID, String aLabel)
        {
            return _readData(aSessionID, aLabel, false, PKCS11Constants_Fields.CKO_DATA);
        }



        public bool isPrivateKeyExist(long aSessionID, String aLabel)
        {
            return _isObjectExist(aSessionID, aLabel, PKCS11Constants_Fields.CKO_PRIVATE_KEY);
        }

        public bool isPublicKeyExist(long aSessionID, String aLabel)
        {
            return _isObjectExist(aSessionID, aLabel, PKCS11Constants_Fields.CKO_PUBLIC_KEY);
        }

        public bool isCertificateExist(long aSessionID, String aLabel)
        {
            return _isObjectExist(aSessionID, aLabel, PKCS11Constants_Fields.CKO_CERTIFICATE);
        }

        public List<byte[]> readCertificate(long aSessionID, String aLabel)
        {
            return _readData(aSessionID, aLabel, false, PKCS11Constants_Fields.CKO_CERTIFICATE);
        }

        public byte[] readCertificate(long aSessionID, byte[] aCertSerialNo)
        {
            CK_ATTRIBUTE_NET[] template =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_CERTIFICATE),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,false),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER,aCertSerialNo)
        };

            long[] objectList = objeAra(aSessionID, template);
            if (objectList.Length == 0)
            {
                throw new SmartCardException("Kartta bu seri numarali sertifika bulunamadi");
            }

            CK_ATTRIBUTE_NET[] valueTemplate =
        {
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE)
        };

            ePkcs11Library.C_GetAttributeValue(aSessionID, objectList[0], valueTemplate);

            return (byte[])valueTemplate[0].pValue;
        }

        /*
         * sonuc olarak ilk elemani modulus,ikinci elemani exponent olan Object[] doner.
         * 
         */
        public ERSAPublicKey readRSAPublicKey(long aSessionID, String aLabel)
        {
            //todo geri dönen değer SubjectPublicKeyInfo olabilir mi? Java tarafı bunu nasıl halletti incele??
            CK_ATTRIBUTE_NET[] template =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PUBLIC_KEY),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel)
        };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                throw new SmartCardException(aLabel + " isimli anahtar kartta yok.");
            }
            iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] publicKeyTemplate =
            {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PUBLIC_EXPONENT),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS)
        };

            ePkcs11Library.C_GetAttributeValue(aSessionID, objectList[0], publicKeyTemplate);

            byte[] publicExponent = (byte[])publicKeyTemplate[0].pValue;
            byte[] modulus = (byte[])publicKeyTemplate[1].pValue;

            Asn1BigInteger exp = new Asn1BigInteger(new BigInteger(publicExponent, 1));
            Asn1BigInteger mod = new Asn1BigInteger(new BigInteger(modulus, 1));

            return new ERSAPublicKey(mod, exp);

        }

        public virtual void updatePrivateData(long aSessionID, String aLabel, byte[] aValue)
        {
            _updateData(aSessionID, aLabel, aValue, true);
        }

        public virtual void updatePublicData(long aSessionID, String aLabel, byte[] aValue)
        {
            _updateData(aSessionID, aLabel, aValue, false);
        }

        private void _updateData(long aSessionID, String aLabel, byte[] aValue, bool aIsPrivate)
        {
            CK_ATTRIBUTE_NET[] template =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_DATA),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,aIsPrivate),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel)
        };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                throw new SmartCardException(aLabel + " isimli nesne kartta bulunamadi.");
            }


            CK_ATTRIBUTE_NET[] valueTemplate =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE,aValue)
        };

            ePkcs11Library.C_SetAttributeValue(aSessionID, objectList[0], valueTemplate);

        }

        private void _deleteObject(long aSessionID, String aLabel, bool aIsPrivate)
        {
            CK_ATTRIBUTE_NET[] template =
            {
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,aIsPrivate),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel)
            };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                throw new SmartCardException(aLabel + " isimli nesne kartta bulunamadi.");
            }

            for (int i = 0; i < objectList.Length; i++)
            {
                ePkcs11Library.C_DestroyObject(aSessionID, objectList[i]);
            }
        }

        public int deleteCertificate(long aSessionID, String aKeyLabel)
        {
            iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[]
            certTemplate = {
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aKeyLabel),
            new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_CERTIFICATE)
            };

            long[] certids = objeAra(aSessionID, certTemplate);
            int deletedCount = 0;
            if (certids.Length > 0)
            {
                for (int i = 0; i < certids.Length; i++)
                {
                    ePkcs11Library.C_DestroyObject(aSessionID, certids[i]);
                    deletedCount++;
                }
            }
            return deletedCount;
        }

        /**
         * login olunmasi gerekir.
         * 
         * @param aSlotID
         * @param aSessionID
         * @param aLabel Karttan silinecek nesnenin ismidir.Bu isimdeki nesnenin tipi CKO_DATA,CKO_CERTIFICATE,
         * CKO_PUBLIC_KEY,CKO_PRIVATE_KEY,CKO_SECRET_KEY olabilir.
         * Kartta bu isimde birden fazla nesne varsa,hepsi silinir.
         * @throws PKCS11Exception
         * @throws KriptoException
         */
        public void deletePrivateObject(long aSessionID, String aLabel)
        {
            _deleteObject(aSessionID, aLabel, true);
        }

        /**
         * akis de login olunmasi gerekir.
         * 
         * @param aSlotID
         * @param aSessionID
         * @param aLabel Karttan silinecek nesnenin ismidir.Bu isimdeki nesnenin tipi CKO_DATA,CKO_CERTIFICATE,
         * CKO_PUBLIC_KEY,CKO_PRIVATE_KEY,CKO_SECRET_KEY olabilir.
         * Kartta bu isimde birden fazla nesne varsa,hepsi silinir.
         * @throws PKCS11Exception
         * @throws KriptoException
         */
        public void deletePublicObject(long aSessionID, String aLabel)
        {
            _deleteObject(aSessionID, aLabel, false);
        }


        private void _deleteData(long aSessionID, String aLabel, bool aIsPrivate)
        {
            CK_ATTRIBUTE_NET[] template =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_DATA),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE,aIsPrivate),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aLabel)
        };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                throw new SmartCardException(aLabel + " isimli nesne kartta bulunamadi.");
            }

            for (int i = 0; i < objectList.Length; i++)
            {
                ePkcs11Library.C_DestroyObject(aSessionID, objectList[i]);
            }
        }


        /**
         * login olunmasi gerekir.
         * 
         * @param aSlotID
         * @param aSessionID
         * @param aLabel Karttan silinecek CKO_DATA tipindeki nesnenin ismidir.
         * Kartta bu isimde birden fazla nesne varsa,hepsi silinir.
         * @throws PKCS11Exception
         * @throws KriptoException
         */
        public void deletePrivateData(long aSessionID, String aLabel)
        {
            _deleteData(aSessionID, aLabel, true);
        }

        /**
         * akis de login olunmasi gerekir.
         * 
         * @param aSlotID
         * @param aSessionID
         * @param aLabel Karttan silinecek CKO_DATA tipindeki nesnenin ismidir.
         * Kartta bu isimde birden fazla nesne varsa,hepsi silinir.
         * @throws PKCS11Exception
         * @throws KriptoException
         */
        public void deletePublicData(long aSessionID, String aLabel)
        {
            _deleteData(aSessionID, aLabel, false);
        }


        /*
         * gemplusda login olunmasi gerekiyor.
         */
        public byte[] getRandomData(long aSessionID, int aDataLength)
        {
            byte[] randomData = new byte[aDataLength];
            ePkcs11Library.C_GenerateRandom(aSessionID, randomData);

            return randomData;
        }


        public byte[] getTokenSerialNo(long aSlotID)
        {
            char[] serialNo = ePkcs11Library.C_GetTokenInfo(aSlotID).serialNumber;
            String serialNoS = new String(serialNo);
            return UnicodeEncoding.Unicode.GetBytes(serialNoS.Trim().ToCharArray());
        }



        /*
         * login gerekiyor.
         */
        /*
         * datakey de dkck232 de calismiyor,dkck201 de calisiyor.
         */
        public virtual byte[] signData(long aSessionID, String aKeyLabel, byte[] aImzalanacak, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM aMechanism)
        {
            iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aKeyLabel),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PRIVATE_KEY)
            };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                throw new SmartCardException(aKeyLabel + " isimli anahtar kartta yok");
            }

            ePkcs11Library.C_SignInit(aSessionID, aMechanism, objectList[0]);
            byte[] imzali = ePkcs11Library.C_Sign(aSessionID, aImzalanacak);

            return imzali;

        }

        /*
         * gemplus ve akisde login gerekiyor.
         */
        public virtual void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aImza, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM mechanism)
        {
            CK_ATTRIBUTE_NET[] template =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aKeyLabel),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PUBLIC_KEY)
        };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                template[2] = new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_SECRET_KEY);
                objectList = objeAra(aSessionID, template);
                if (objectList.Length == 0)
                    throw new SmartCardException(aKeyLabel + " isimli anahtar kartta yok.");
            }

            ePkcs11Library.C_VerifyInit(aSessionID, mechanism, objectList[0]);
            ePkcs11Library.C_Verify(aSessionID, aData, aImza);
        }

        /*
         * gemplus ve akisde login gerekiyor.
         */
        public byte[] encryptData(long aSessionID, String aKeyLabel, byte[] aData, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM mechanism)
        {
            CK_ATTRIBUTE_NET[] template =
        {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aKeyLabel),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PUBLIC_KEY)
        };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                template[2] = new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_SECRET_KEY);
                objectList = objeAra(aSessionID, template);
                if (objectList.Length == 0)
                    throw new SmartCardException(aKeyLabel + " isimli anahtar kartta yok.");
            }

            ePkcs11Library.C_EncryptInit(aSessionID, mechanism, objectList[0]);
            byte[] sonuc = ePkcs11Library.C_Encrypt(aSessionID, aData);

            return sonuc;
        }

        /*
         * login gerekiyor.
         */
        public byte[] decryptData(long aSessionID, String aKeyLabel, byte[] aData, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM mechanism)
        {
            byte[] cozulecek = _fixLength(aData);

            iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE/*_NET*/[] template =
            {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL,aKeyLabel),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PRIVATE_KEY)
            };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                template[2] = new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_SECRET_KEY);
                objectList = objeAra(aSessionID, template);
                if (objectList.Length == 0)
                    throw new SmartCardException(aKeyLabel + " isimli anahtar kartta yok.");
            }

            ePkcs11Library.C_DecryptInit(aSessionID, mechanism, objectList[0]);
            byte[] sonuc = ePkcs11Library.C_Decrypt(aSessionID, cozulecek);

            return sonuc;
        }

        private byte[] _fixLength(byte[] aData)
        {
            int mod = aData.Length % 8;

            if (mod == 1 && aData[0] == 0)
            {
                byte[] trimmed = new byte[aData.Length - 1];
                Array.Copy(aData, 1, trimmed, 0, aData.Length - 1);
                return trimmed;
            }

            if (mod == 7)
            {
                byte[] zeroPadded = new byte[aData.Length + 1];
                zeroPadded[0] = 0;
                Array.Copy(aData, 0, zeroPadded, 1, aData.Length);
                return zeroPadded;
            }

            return aData;
        }


        public virtual void importCertificateAndKey(long aSessionID, String aCertLabel, String aKeyLabel, EPrivateKeyInfo aPrivKeyInfo, ECertificate aCert)
        {

            AsymmetricKeyParameter aPrivKey = PrivateKeyFactory.CreateKey(/*KriptoUtils*/BouncyProviderUtil.ToBouncy(aPrivKeyInfo));  //asn1 to bouncy donusumu yapiliyor...
            bool isSign = false;
            bool isEncrypt = false;
            if (aCert != null)
            {
                EKeyUsage ku = aCert.getExtensions().getKeyUsage();
                if (ku != null)
                {
                    if (ku.isDigitalSignature())
                        isSign = true;
                    if (ku.isKeyEncipherment() || ku.isDataEncipherment())
                        isEncrypt = true;
                }
            }

            if (aPrivKey is ECPrivateKeyParameters)
            {
                importCertificate(aSessionID, aCertLabel, aCert);
                byte[] subject = aCert.getSubject().getBytes();

                //importECKeyPair(aSessionID, aKeyLabel, aCert.getSubjectPublicKeyInfo(), aPrivKeyInfo, subject, isSign, isEncrypt);
            }
            else if (aPrivKey is /*RSAPrivateCrtKey*/RsaPrivateCrtKeyParameters)
            {
                /*RSAPrivateCrtKey*/
                List<CK_ATTRIBUTE_NET> certTemplate = mCardType.getCardTemplate().getCertificateTemplate(aCertLabel, aCert);
                List<CK_ATTRIBUTE_NET> priKeyTemplate = mCardType.getCardTemplate().getRSAPrivateKeyImportTemplate(aKeyLabel, aPrivKeyInfo, aCert, isSign, isEncrypt);
                List<CK_ATTRIBUTE_NET> pubKeyTemplate = mCardType.getCardTemplate().getRSAPublicKeyImportTemplate(aKeyLabel, aPrivKeyInfo, aCert, isSign, isEncrypt);

                ePkcs11Library.C_CreateObject(aSessionID, priKeyTemplate.ToArray());
                ePkcs11Library.C_CreateObject(aSessionID, pubKeyTemplate.ToArray());
                ePkcs11Library.C_CreateObject(aSessionID, certTemplate.ToArray());
            }
            else
            {
                throw new SmartCardException("Verilen ozel anahtar tipi desteklenmiyor");
            }
        }

        public virtual long[] importKeyPair(long aSessionID, String aLabel, ESubjectPublicKeyInfo aPubKeyInfo, EPrivateKeyInfo aPrivKeyInfo, EECParameters ecParameters, byte[] aSubject, bool aIsSign, bool aIsEncrypt)
        {
            int[] oid = aPubKeyInfo.getAlgorithm().getAlgorithm().mValue;
            AsymmetricAlg asymmetricAlg = AsymmetricAlg.fromOID(oid);

            if (asymmetricAlg.Equals(AsymmetricAlg.RSA))
                return importRSAKeyPair(aSessionID, aLabel, aPrivKeyInfo, aSubject, aIsSign, aIsEncrypt);
            else if (asymmetricAlg.Equals(AsymmetricAlg.ECDSA))
                return importECKeyPair(aSessionID, aLabel, aPubKeyInfo, aPrivKeyInfo, ecParameters, aSubject, aIsSign, aIsEncrypt);
            else
                throw new SmartCardException("Unsupported algorithm: " + asymmetricAlg);

        }

        public virtual long[] importRSAKeyPair(long aSessionID, string aLabel, EPrivateKeyInfo aPrivKeyInfo, byte[] aSubject, bool aIsSign, bool aIsEncrypt)
        {
            List<CK_ATTRIBUTE_NET> priKeyTemplate = mCardType.getCardTemplate().getRSAPrivateKeyImportTemplate(aLabel, aPrivKeyInfo, null, aIsSign, aIsEncrypt);
            List<CK_ATTRIBUTE_NET> pubKeyTemplate = mCardType.getCardTemplate().getRSAPublicKeyImportTemplate(aLabel, aPrivKeyInfo, null, aIsSign, aIsEncrypt);
            if (aSubject != null)
            {
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SUBJECT, aSubject));
            }

            long[] objectHandles = new long[2];

            objectHandles[0] = ePkcs11Library.C_CreateObject(aSessionID, priKeyTemplate.ToArray());
            objectHandles[1] = ePkcs11Library.C_CreateObject(aSessionID, pubKeyTemplate.ToArray());

            return objectHandles;
        }

        public virtual long[] importECKeyPair(long aSessionID, String aLabel, ESubjectPublicKeyInfo aPubKeyInfo, EPrivateKeyInfo aPrivKeyInfo, EECParameters ecParameters, byte[] aSubject, bool aIsSign, bool aIsEncrypt)
        {
            AsymmetricKeyParameter privKey = PrivateKeyFactory.CreateKey(BouncyProviderUtil.ToBouncy(aPrivKeyInfo));
            AsymmetricKeyParameter pubKey = PublicKeyFactory.CreateKey(BouncyProviderUtil.ToBouncy(aPubKeyInfo));

            if (pubKey is ECPublicKeyParameters && privKey is ECPrivateKeyParameters)
            {
                ECPublicKeyParameters ecPubKey = (ECPublicKeyParameters)pubKey;
                ECPrivateKeyParameters ecPrivKey = (ECPrivateKeyParameters)privKey;

                if (ecParameters == null)
                {
                    int[] oid = OIDUtil.parse(ecPrivKey.PublicKeyParamSet.Id);
                    Asn1ObjectIdentifier curveOid = new Asn1ObjectIdentifier(oid);

                    NamedCurve namedCurve = NamedCurve.getCurveParametersFromOid(curveOid);
                    ecParameters = namedCurve.getECParameters();
                }

                byte[] point = ecPubKey.Q.GetEncoded();
                byte[] idVeri = new Asn1BitString(point.Length << 3, point).mValue;
                byte[] id = DigestUtil.digest(DigestAlg.SHA1, idVeri);

                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                Asn1OctetString s = new Asn1OctetString(point);
                s.Encode(encBuf);
                byte[] pkecpoint = encBuf.MsgCopy;

                List<CK_ATTRIBUTE_NET> pubKeyTemplate = new List<CK_ATTRIBUTE_NET>();
                pubKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));
                pubKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_PUBLIC_KEY));
                pubKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_KEY_TYPE, PKCS11Constants_Fields.CKK_EC));
                pubKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EC_PARAMS, ecParameters.getEncoded()));
                pubKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EC_POINT, pkecpoint));
                pubKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel));
                pubKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, false));
                pubKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID, id));

                if (aIsEncrypt)
                    pubKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_DERIVE, aIsEncrypt));
                if (aIsSign)
                    pubKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VERIFY, aIsSign));

                List<CK_ATTRIBUTE_NET> priKeyTemplate = new List<CK_ATTRIBUTE_NET>();
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_KEY_TYPE, PKCS11Constants_Fields.CKK_EC));
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_PRIVATE_KEY));
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, true));
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel));
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EC_PARAMS, ecParameters.getEncoded()));
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_VALUE, ecPrivKey.D.ToByteArrayUnsigned()));
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID, id));
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SENSITIVE, true));
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_EXTRACTABLE, false));
                priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true));

                if (aIsEncrypt)
                    priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_DERIVE, aIsEncrypt));
                if (aIsSign)
                    priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SIGN, aIsSign));
                if (aSubject != null)
                    priKeyTemplate.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SUBJECT, aSubject));

                long[] objectHandles = new long[2];

                objectHandles[0] = ePkcs11Library.C_CreateObject(aSessionID, pubKeyTemplate.ToArray());
                objectHandles[1] = ePkcs11Library.C_CreateObject(aSessionID, priKeyTemplate.ToArray());

                return objectHandles;
            }
            else
            {

                throw new SmartCardException("Expected public key type is ECPublicKey but the actual one is " + pubKey.GetType() + "\n" +
                                             "Expected private key type is ECPrivateKey but the actual one is " + privKey.GetType());
            }
        }

        public void changePassword(String aOldPass, String aNewPass, long aSessionID)
        {
            ePkcs11Library.C_SetPIN(aSessionID, aOldPass.ToCharArray(), aNewPass.ToCharArray());
        }

        public void formatToken(String aSOpin, String aNewPIN, String aLabel, int slotID)
        {
            ePkcs11Library.C_CloseAllSessions(slotID);
            ePkcs11Library.C_InitToken(slotID, aSOpin.ToCharArray(), aLabel.ToCharArray());
            long hSession = ePkcs11Library.C_OpenSession(slotID, PKCS11Constants_Fields.CKF_SERIAL_SESSION | PKCS11Constants_Fields.CKF_RW_SESSION);
            ePkcs11Library.C_Login(hSession, PKCS11Constants_Fields.CKU_SO, aSOpin);
            ePkcs11Library.C_InitPIN(hSession, aNewPIN.ToCharArray());
            ePkcs11Library.C_Logout(hSession);
            ePkcs11Library.C_CloseSession(hSession);
        }

        public virtual void setSOPin(byte[] aSOPin, byte[] aNewSOPin, long aSessionID)
        {
            ePkcs11Library.C_Login(aSessionID, PKCS11Constants_Fields.CKU_SO, Encoding.ASCII.GetString(aSOPin));
            ePkcs11Library.C_SetPIN(aSessionID, Encoding.ASCII.GetString(aSOPin).ToCharArray(), Encoding.ASCII.GetString(aNewSOPin).ToCharArray());
            ePkcs11Library.C_Logout(aSessionID);
        }

        public virtual void changeUserPin(byte[] aSOPin, byte[] aUserPin, long aSessionID)
        {
            ePkcs11Library.C_Login(aSessionID, PKCS11Constants_Fields.CKU_SO, Encoding.ASCII.GetString(aSOPin));
            ePkcs11Library.C_InitPIN(aSessionID, Encoding.ASCII.GetString(aUserPin).ToCharArray());
            ePkcs11Library.C_Logout(aSessionID);
        }

        public bool setContainer(byte[] aContainerLabel, long aSessionID)
        {
            throw new NotSupportedException("PKCS11Islemler.setContainer metodu implement edilmedi");
        }


        public bool importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName, byte[] aPbCertData, int aSignOrEnc)
        {
            return importCertificateAndKeyWithCSP(aAnahtarCifti, aAnahtarLen, aScfname, aContextName, new ECertificate(aPbCertData), aSignOrEnc);
        }

        public bool importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName, ECertificate aPbCertificate, int aSignOrEnc)
        {
            throw new NotSupportedException("PKCS11Islemler.importCertificateAndKeyWithCSP metodu implement edilmedi");
        }

        protected void changePUK(byte[] aOldPUK, byte[] aNewPUK, long aSessionID)
        {

            throw new NotSupportedException("PKCS11Islemler.changePUK metodu implement edilmedi");
        }

        protected void unBlockPIN(byte[] aPUK, byte[] aUserPIN, long aSessionID)
        {
            throw new NotSupportedException("PKCS11Islemler.unBlockPIN metodu implement edilmedi");
        }


        public long[] objeAra(long aSessionID, iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] aTemplate)
        {
            uint maxCount = 200;
            uint objCount = 0;

            CK_ATTRIBUTE[] bTemplate = EPkcs11Library.fromIaikAttributeArrayToInteropAttributeArray(aTemplate);

            CKR rv = ePkcs11Library.C_FindObjectsInit((uint)aSessionID, bTemplate, (uint)bTemplate.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            EPkcs11Library.freeCKAttributeMemory(bTemplate);

            uint[] objList = new uint[maxCount];
            rv = ePkcs11Library.C_FindObjects((uint)aSessionID, objList, maxCount, ref objCount);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            ePkcs11Library.C_FindObjectsFinal((uint)aSessionID);

            Array.Resize(ref objList, (int)objCount);

            return EPkcs11Library.uintArrayToLongArray(objList);
        }

        public void getAttributeValue(long aSessionID, long aObjectID, iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] aTemplate)
        {
            ePkcs11Library.C_GetAttributeValue(aSessionID, aObjectID, aTemplate);
        }

        public byte[] generateRSAPrivateKey(int keySize)
        {
            throw new SystemException("ERROR Operation:Operation is not supported in PKCS11Ops. Use creteRSAKeyPair");
        }


        public byte[] getModulusOfKey(long aSessionID, long aObjID)
        {

            CK_ATTRIBUTE_NET[] values = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS) };

            getAttributeValue(aSessionID, aObjID, values);

            return (byte[])values[0].pValue;
        }

        private byte[] _getCertificateId(long aSessionID, byte[] aSerialNumber)
        {
            List<List<CK_ATTRIBUTE_NET>> list = mCardType.getCardTemplate().getCertSerialNumberTemplates(aSerialNumber);

            long[] objectList = null;

            foreach (List<CK_ATTRIBUTE_NET> tList in list)
            {
                objectList = objeAra(aSessionID, tList.ToArray());
                if (objectList.Length > 0) break;
            }

            if (objectList == null || objectList.Length == 0)
            {
                throw new SmartCardException("Verilen seri numarali sertifika kartta bulunamadi.");
            }

            CK_ATTRIBUTE_NET[] idTemplate = { new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID) };

            ePkcs11Library.C_GetAttributeValue(aSessionID, objectList[0], idTemplate);
            byte[] id = (byte[])idTemplate[0].pValue;

            return id;
        }

        public long getPrivateKeyObjIDFromCertificateSerial(long aSessionID, byte[] aCertSerialNo)
        {
            byte[] ID = _getCertificateId(aSessionID, aCertSerialNo);


            CK_ATTRIBUTE_NET[] template =
            {
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PRIVATE_KEY),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_ID,ID)
            };

            long[] objectList = objeAra(aSessionID, template);

            return objectList[0];
        }

        public long getObjIDFromPrivateKeyLabel(long aSessionID, string aLabel)
        {
            CK_ATTRIBUTE_NET[] template =
            {
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS,PKCS11Constants_Fields.CKO_PRIVATE_KEY),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN,true),
                    new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aLabel)
            };

            long[] objectList = objeAra(aSessionID, template);

            if (objectList.Length == 0)
            {
                template[0] = new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_SECRET_KEY);
                objectList = objeAra(aSessionID, template);
                if (objectList.Length == 0)
                    throw new SmartCardException(aLabel + " isimli anahtar kartta yok");
            }

            return objectList[0];
        }
        public byte[] wrapKey(long aSessionID, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM aMechanism, String wrapperKeyLabel, String aKeyLabel)
        {
            CK_ATTRIBUTE_NET[] wrappingKeyTemplate = {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, wrapperKeyLabel),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_PUBLIC_KEY)
            };

            long[] wrappingkeys = objeAra(aSessionID, wrappingKeyTemplate);
            if (wrappingkeys.Length == 0)
            {
                wrappingKeyTemplate[1].pValue = PKCS11Constants_Fields.CKO_SECRET_KEY;
                wrappingkeys = objeAra(aSessionID, wrappingKeyTemplate);
                if (wrappingkeys.Length == 0)
                    throw new SmartCardException(wrapperKeyLabel + " isimli anahtar kartta bulunamadi");
            }

            CK_ATTRIBUTE_NET[] keyTemplate = {
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, aKeyLabel),
                new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, true)
            };
            long[] keys = objeAra(aSessionID, keyTemplate);
            if (keys.Length == 0)
                throw new SmartCardException(aKeyLabel + " isimli anahtar kartta bulunamadi");

            return ePkcs11Library.C_WrapKey(aSessionID, aMechanism, wrappingkeys[0], keys[0]);
        }

        public long importSecretKey(long aSessionId, SecretKeyTemplate aKeyTemplate)
        {
            mCardType.getCardTemplate().applyTemplate(aKeyTemplate);
            return ePkcs11Library.C_CreateObject(aSessionId, aKeyTemplate.getAttributesAsArr());
        }

        public long createSecretKey(long sessionID, SecretKeyTemplate template)
        {
            mCardType.getCardTemplate().applyTemplate(template);
            iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] templateAttr = template.getAttributesAsArr();

            iaik.pkcs.pkcs11.wrapper.CK_MECHANISM mech = new iaik.pkcs.pkcs11.wrapper.CK_MECHANISM();
            mech.mechanism = template.getGenerationMechanism();
            long keyID = ePkcs11Library.C_GenerateKey(sessionID, mech, templateAttr);
            template.setKeyId(keyID);

            return keyID;
        }
    }
}
