using System;
using System.Collections.Generic;
using System.Reflection;
using tr.gov.tubitak.uekae.esya.api.smartcard.winscard;
using iaik.pkcs.pkcs11.wrapper;
using log4net;
using smartcard.src.tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;


namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11
{
    public class SmartOp
    {
        private long mSlotID;
        private CardType mCardType;
        private String mPassword;

        private static readonly ILog LOGGER = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Create SmartOp with slotId,card type and password
         * @param aSlotID
         * @param aCardType
         * @param aPassword
         */
        public SmartOp(long aSlotID, CardType aCardType, String aPassword)
            : this()
        {
            initialize(aSlotID, aCardType, aPassword);
        }

        public SmartOp()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.AKILLIKART);
            }
            catch (LE ex)
            {
                throw new ESYAException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

        public void initialize(long aSlotID, CardType aCardType, String aPassword)
        {
            mSlotID = aSlotID;
            mCardType = aCardType;
            mPassword = aPassword;
        }

        /**
         * getCardSerialNumber returns token's serial number 
         *
         * @return the token's serial number
         * @throws PKCS11Exception
         * @throws IOException
         */
        public String getCardSerialNumber()
        {
            SmartCard sc = new SmartCard(mCardType);
            return new String(sc.getTokenInfo(mSlotID).serialNumber).Trim();
        }

        /**
	 * importCertificateAndKey imports private key, certificate and public key extracted from certificate to the card. 
	 * PIN must be supplied with constructor since login is needed.
	 *
	 * @param aCertLabel Label of the certificate
	 * @param aKeyLabel Label of the keys
	 * @param aPrivKeyInfo Private key that will be imported
	 * @param aCert Certificate that will be imported 	
	 *
	 */
        public void importCertificateAndKey(String aCertLabel, String aKeyLabel, EPrivateKeyInfo aPrivKeyInfo,
                                            ECertificate aCert)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);
                sc.importCertificateAndKey(sessionID, aCertLabel, aKeyLabel, aPrivKeyInfo, aCert);
                sc.logout(sessionID);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
	 * importCertificateAndKeyWithCSP imports the given key pair to the token with csp.
	 * 
	 * @param aKeyPair Key pair that will be imported
	 * @param aKeyLength Length of the key that will be imported
	 * @param aScfname
	 * @param aContextName
	 * @param aPbCertData
	 * @param aSignOrEnc 
	 * @return
	 */

        public bool importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname,
                                                   String aContextName, byte[] aPbCertData, int aSignOrEnc)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                return sc.importCertificateAndKeyWithCSP(aAnahtarCifti, aAnahtarLen, aScfname, aContextName, aPbCertData,
                                                         aSignOrEnc);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
         * Verilen ozel anahtari ve bu ozel anahtardan olusturulan acik anahtari karta import eder.
         * Method icinde karta login islemi gerceklestiginden constructor da kart parolasi verilmelidir.
         * @param aLabel
         *        Karta anahtarlarin hangi label ile import edilecegidir.   
         * @param aPrivKey
         *        Karta import edilecek ozel anahtardir. Acik anahtar da, bu ozel anahtardan olusturulur.   
         * @param aSubject
         *        Ozel anahtarin subject alanina set edilecek degerdir. 
         * @throws PKCS11Exception 
         * @throws IOException
         * @throws PKCS11Exception,IOException,KriptoException
         * @throws SmartCardException
         */

        public long[] importRSAKeyPair(String aLabel, EPrivateKeyInfo aPrivKeyInfo, byte[] aSubject, bool aIsSign,
                                     bool aIsEncrypt)
        {
            SmartCard sc = null;
            long sessionID = -1;
            long[] objectHandles = null;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);

                objectHandles = sc.importRSAKeyPair(sessionID, aLabel, aPrivKeyInfo, aSubject, aIsSign, aIsEncrypt);

                sc.logout(sessionID);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }

            return objectHandles;
        }


        /**
         * readCertificate returns certificate values read from token.
         *
         * @param aLabel Label of the certificate
         * @throws IOException
         * @throws PKCS11Exception 
         * @throws SmartCardException If no certificate exists in token with the given label.
         */

        public List<byte[]> readCertificate(String aLabel)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                return sc.readCertificate(sessionID, aLabel);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
      * getSignCertificates returns signing certificates.(Searches for keyusage with digitalSignature bit set)
      * 
      * @return list of signing certificates. If no signing certificate is found,returns empty list.      
      *
      */

        public List<byte[]> getSignCertificates()
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                return sc.getSignatureCertificates(sessionID);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
      * getEncryptCertificates returns encrytion certificates. (Searches for keyusage with dataEncipherment bit set or keyEncipherment bit set)
      * 
      * @return list of encryption certificates. If no encryption certificate is found,returns empty list.
      */

        public List<byte[]> getEncryptCertificates()
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                return sc.getEncryptionCertificates(sessionID);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
	 * importCertificate imports the given certificate to the token with the given label.
	 *
	 * @param aCertLabel Label of the certificate.
	 * @param aCert      Certificate.	
	 *
	 */

        public void importCertificate(String aCertLabel, ECertificate aCert)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.importCertificate(sessionID, aCertLabel, aCert);
            }
            catch (PKCS11Exception aEx)
            {
                if (aEx.Message.Equals("CKR_USER_NOT_LOGGED_IN"))
                {
                    sc.login(sessionID, mPassword);
                    sc.importCertificate(sessionID, aCertLabel, aCert);
                    sc.logout(sessionID);
                }
                else
                    throw aEx;
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }


        /**
	 * readPrivateData searches for CKO_DATA type object in private area of the token and returns if found.
	 *
	 * @param aLabel Label of the data.
	 * @return list of data. If no data is found, SmartCardException is thrown.	 
	 */

        public List<byte[]> readPrivateData(String aLabel)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);
                List<byte[]> value = sc.readPrivateData(sessionID, aLabel);
                sc.logout(sessionID);
                return value;
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }


        /**
	 * readPublicData searches for CKO_DATA type object in public area of the token and returns if found.
	 *
	 * @param aLabel Label of the data.
	 * @return list of data. If no data is found, SmartCardException is thrown.
	 */

        public List<byte[]> readPublicData(String aLabel)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                List<byte[]> value = sc.readPublicData(sessionID, aLabel);
                return value;
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }


        /**
     * writePrivateData writes the given data with the given label to the private area of the token.
     *
     * @param aLabel  Label of the data.
     * @param aData  Data that will be imported.    
     */

        public void writePrivateData(String aLabel, byte[] aData)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);
                sc.writePrivateData(sessionID, aLabel, aData);
                sc.logout(sessionID);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }


        /**
	 * writePublicData writes the given data with the given label to the public area of the token.
	 *
	 * @param aLabel Label of the data.
	 * @param aData  Data that will be imported.	
	 */

        public void writePublicData(String aLabel, byte[] aData)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);
                sc.writePublicData(sessionID, aLabel, aData);
                sc.logout(sessionID);
            }
            catch (PKCS11Exception aEx)
            {
                if (aEx.Message.Equals("CKR_USER_NOT_LOGGED_IN"))
                {
                    sc.login(sessionID, mPassword);
                    sc.writePublicData(sessionID, aLabel, aData);
                    sc.logout(sessionID);
                }
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
	 * deletePrivateData searches for data with the given label and type CKO_DATA in the private area and deletes if finds.
	 * If more than one is found,all will be deleted. If none is found, SmartCardException is thrown.
	 * 
	 * @param aLabel Label of data. 	
	 */

        public void deletePrivateData(String aLabel)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);
                sc.deletePrivateData(sessionID, aLabel);
                sc.logout(sessionID);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
	 * deletePublicData searches for data with the given label and type CKO_DATA in the public area and deletes if finds.
	 * If more than one is found,all will be deleted. If none is found, SmartCardException is thrown.
	 * 
	 * @param aLabel Label of data. 	 
	 */

        public void deletePublicData(String aLabel)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.deletePublicData(sessionID, aLabel);
            }
            catch (PKCS11Exception aEx)
            {
                if (aEx.Message.Equals("CKR_USER_NOT_LOGGED_IN"))
                {
                    sc.login(sessionID, mPassword);
                    sc.deletePublicData(sessionID, aLabel);
                    sc.logout(sessionID);
                }
                else
                    throw aEx;
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }


        /**
	 * isObjectExist searches for objects with the given label(type can be CKO_DATA,CKO_PRIVATEKEY,CKO_PUBLICKEY or CKO_CERTIFICATE) and returns true if one or more is found.
	 * 
	 * @param aLabel Label of object that is searched for.
	 * @param aIsPrivate True if search will be done in private area of the card,false otherwise.
	 * @return True if any object with given label is found,false otherwise	 
	 */

        public bool isObjectExist(String aLabel, bool aIsPrivate)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                bool sonuc;
                if (aIsPrivate)
                {
                    sc.login(sessionID, mPassword);
                    sonuc = sc.isObjectExist(sessionID, aLabel);
                    sc.logout(sessionID);
                }
                else
                {
                    sonuc = sc.isObjectExist(sessionID, aLabel);
                }
                return sonuc;
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
	 * isCardEmpty searches for objects(type can be CKO_DATA,CKO_PRIVATEKEY,CKO_PUBLICKEY or CKO_CERTIFICATE) and returns true if none is found.
	 * 
	 * @return True if no object is found,false otherwise.	 
	 */

        public bool isCardEmpty()
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                return sc.isAnyObjectExist(sessionID);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
     * generateKeyPair generates public/private key pair according to the given parameters. If keys with the given label already exist, SmartCardException is thrown.
     *
     * @param aKeyLabel  Label of the generated keys.
     * @param aAlg  Algorithm type.
     * @param aLength   key length.
     * @param aIsSign    True if keys are for signing,false otherwise.
     * @param aIsEncrypt True if keys are for encryption,false otherwise.	
     */

        public long[] generateKeyPair(string aKeyLabel, string aAlg, int aLength, bool aIsSign, bool aIsEncrypt)
        {
            SmartCard smartCard = null;
            long sessionID = -1;
            long[] objectHandles;
            try
            {
                if (mCardType == null)
                {
                    throw new SmartCardException("CardType is null.");
                }
                else if (mSlotID == -1)
                {
                    mSlotID = SmartOp.findSlotNumber(mCardType, null);
                }

                smartCard = new SmartCard(mCardType);
                sessionID = smartCard.openSession(mSlotID);
                smartCard.login(sessionID, mPassword);

                bool sonuc = smartCard.isObjectExist(sessionID, aKeyLabel);
                if (sonuc)
                {
                    throw new SmartCardException(aKeyLabel + " exists in card.");
                }

                if (aAlg.Equals(Algorithms.ASYM_ALGO_RSA))
                {
                    objectHandles = smartCard.createRSAKeyPair(sessionID, aKeyLabel, aLength, aIsSign, aIsEncrypt);
                }
                else
                {
                    throw new SmartCardException(aAlg + " algorithm is not supported.");
                }

                smartCard.logout(sessionID);
            }
            finally
            {
                if (smartCard != null && sessionID != -1)
                    smartCard.closeSession(sessionID);
            }

            return objectHandles;
        }

        /**
         * Karttan acik anahtar okunur.
         * @param aKeyLabel
         * 		  Karttan okunacak acik anahtarin label degeridir. 
         * @param aAlg
         * 		  Karttan okunacak acik anahtarin hangi algoritma icin kullanildigidir.
         * @return
         * @throws PKCS11Exception
         * @throws IOException
         * @throws SmartCardException
         */

        public /*RSAPublicKey*/ /*ERSAPublicKey*/ ESubjectPublicKeyInfo getPublicKey(String aKeyLabel, String aAlg)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                if (mCardType == null)
                {
                    throw new SmartCardException("CardType is null.");
                }
                else if (mSlotID == -1)
                {
                    mSlotID = SmartOp.findSlotNumber(mCardType, null);
                }

                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);

                if (aAlg.Equals(Algorithms.ASYM_ALGO_RSA))
                {
                    ERSAPublicKey rsaKey = sc.readRSAPublicKey(sessionID, aKeyLabel);
                    //TODO aşağıdaki işlem doğru mu yapılmış kontrol et?? (PublicKey'den SubjectPublicKeyInfo üretme işlemi)                    
                    return
                        ESubjectPublicKeyInfo.createESubjectPublicKeyInfo(
                            new AlgorithmIdentifier(_algorithmsValues.rsaEncryption), rsaKey);
                }

                throw new SmartCardException(aAlg + " algorithm is unknown.");
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }


        /**
        * sign finds the private key that has the same CKA_ID value with the certificate having the given serial number
        * and signs the given data.
        * @param aSC Created smart card object
        * @param aSessionID session handle ( must be already logged in)
        * @param aSlotID token present slot handle
        * @param aCertSerialNo certificate serial number
        * @param aToBeSigned Data to be signed
        * @param aSigningAlg Signing algorithm
        * @return signature	
        */

        public static byte[] sign(SmartCard aSC, long aSessionID, long aSlotID, byte[] aCertSerialNo, byte[] aToBeSigned,
                                  String aSigningAlg, IAlgorithmParameterSpec aParamSpec)
        {
            long? objID = null;
            try
            {
                objID = aSC.getPrivateKeyObjIDFromCertificateSerial(aSessionID, aCertSerialNo);
                KeyFinder kf = new ModulusFinderFromObjectId(aSC, aSessionID, (long) objID);

                long[] mechs = aSC.getMechanismList(aSlotID);

                ISignatureScheme signatureScheme =
                    SignatureSchemeFactory.getSignatureScheme(true, aSigningAlg, aParamSpec, mechs, kf);
                byte[] imzalanacak = signatureScheme.getSignatureInput(aToBeSigned);
                CK_MECHANISM mechanism = signatureScheme.getMechanism();

                byte[] imzali = aSC.signDataWithKeyID(aSessionID, (long) objID, mechanism, imzalanacak);
                return imzali;
            }
            catch (PKCS11Exception e)
            {
                LOGGER.Debug($"Hata! KartTipi: {aSC.getCardType()}, Slot: {aSlotID}, CertSerial: {StringUtil.ToHexString(aCertSerialNo)}, Algorithm: {aSigningAlg}", e);

                if (objID != null && e.ErrorCode == PKCS11Constants_Fields.CKR_KEY_TYPE_INCONSISTENT) {
                    checkKeyAndSigningAlgConsistency(aSC, aSessionID, aSigningAlg, (long) objID);
                }

                throw;
            }
        }

        protected static void checkKeyAndSigningAlgConsistency(SmartCard aSC, long aSessionID, String aSigningAlg, long objID)
        {
            CK_ATTRIBUTE[] template;
            {
                CK_ATTRIBUTE keyTypeAttr = new CK_ATTRIBUTE {type = PKCS11Constants_Fields.CKA_KEY_TYPE};
                template = new[]
                {
                    keyTypeAttr
                };
            }

            aSC.getAttributeValue(aSessionID, objID, template);

            AsymmetricAlg alg = SignatureAlg.fromName(aSigningAlg).getAsymmetricAlg();

            if (!(alg.Equals(AsymmetricAlg.RSA) && (long) template[0].pValue == PKCS11Constants_Fields.CKK_RSA ||
                  alg.Equals(AsymmetricAlg.ECDSA) && (long) template[0].pValue == PKCS11Constants_Fields.CKK_ECDSA)) {
                throw new SmartCardException(Resource.message(Resource.IMZA_ANAHTAR_ALGORITMA_UYUMSUZLUGU));
            }
        }

        /**
        * sign finds the private key that has the same CKA_ID value with the certificate having the given serial number
        * and signs the given data.
        *
        * @param aCertSerialNo Certificate serial number
        * @param aToBeSigned  Data to be signed
        * @param aSigningAlg   Signing algorithm
        * @return signature	
        */
        public byte[] sign(byte[] aCertSerialNo, byte[] aToBeSigned, String aSigningAlg, IAlgorithmParameterSpec aParams)
        {
            if (mCardType == null)
            {
                throw new SmartCardException("CardType is null.");
            }
            else if (mSlotID == -1)
            {
                mSlotID = SmartOp.findSlotNumber(mCardType, null);
            }
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);
                byte[] imzali = sign(sc, sessionID, mSlotID, aCertSerialNo, aToBeSigned, aSigningAlg, aParams);
                sc.logout(sessionID);
                return imzali;
            }
            finally
            {
                if (sc != null && sessionID != -1) sc.closeSession(sessionID);
            }
        }

        /**
        * sign finds the private key with the given label and signs the given data.	 
        * @param aKeyLabel Label of the private key
        * @param aToBeSigned Data to be signed
        * @param aSigningAlg Signing algorithm  
        * @return signature 	 
        */

        public byte[] sign(String aAnahtarAdi, byte[] aToBeSigned, String aSigningAlg, IAlgorithmParameterSpec aParamSpec)
        {
            SmartCard sc = null;
            long sessionID = -1;
            if (mCardType == null)
            {
                throw new SmartCardException("CardType is null.");
            }
            else if (mSlotID == -1)
            {
                mSlotID = SmartOp.findSlotNumber(mCardType, null);
            }

            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);
                byte[] imzali = sign(sc, sessionID, mSlotID, aAnahtarAdi, aToBeSigned, aSigningAlg, aParamSpec);
                sc.logout(sessionID);
                return imzali;
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
        * sign finds the private key with the given label and signs the given data.	 
        * @param aSC Created smart card object
        * @param aSessionID Session handle ( must be already logged in)
        * @param aSlotID Token present slot handle
        * @param aKeyLabel Label of the private key
        * @param aToBeSigned Data to be signed
        * @param aSigningAlg Signing algorithm
        * @return signature	 
        */

        public static byte[] sign(SmartCard aSC, long aSessionID, long aSlotID, String aKeyLabel, byte[] aToBeSigned,
                                  String aSigningAlg, IAlgorithmParameterSpec aParamSpec)
        {
            bool sonuc = aSC.isObjectExist(aSessionID, aKeyLabel);
            if (!sonuc)
            {
                throw new SmartCardException(aKeyLabel + " named key does not exist in card.");
            }

            long? aObjID = null;
            try
            {
                aObjID = aSC.getPrivateKeyObjIDFromPrivateKeyLabel(aSessionID, aKeyLabel);
                KeyFinder kf = new ModulusFinderFromObjectId(aSC, aSessionID, (long) aObjID);

                long[] mechs = aSC.getMechanismList(aSlotID);

                ISignatureScheme signatureScheme =
                    SignatureSchemeFactory.getSignatureScheme(true, aSigningAlg, aParamSpec, mechs, kf);
                byte[] imzalanacak = signatureScheme.getSignatureInput(aToBeSigned);
                CK_MECHANISM mechanism = signatureScheme.getMechanism();
                byte[] imzali = aSC.signData(aSessionID, aKeyLabel, imzalanacak, mechanism);
                return imzali;
            }
            catch (PKCS11Exception e)
            {
                LOGGER.Debug(
                    $"Hata! KartTipi: {aSC.getCardType()}, Slot: {aSlotID}, Anahtar Adı: {aKeyLabel}, Algorithm: {aSigningAlg}",
                    e);

                if (aObjID != null && e.ErrorCode == PKCS11Constants_Fields.CKR_KEY_TYPE_INCONSISTENT)
                {
                    checkKeyAndSigningAlgConsistency(aSC, aSessionID, aSigningAlg, (long) aObjID);
                }

                throw;
            }
        }

        /**
         * verify finds the public key with the given label and verifies the given signature
         *
         * @param aKeyLabel   Label of the public key.
         * @param aData    Data that is signed.
         * @param aSignature Signature.
         * @param aSigningAlg   Signing algorithm.
         * @return true if verification successful,false otherwise.     
         */
        //gemplus da hata veriyo
        public bool verify(String aKeyLabel, byte[] aData, byte[] aSignature, String aSigningAlg)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                if (mCardType == null)
                {
                    throw new SmartCardException("CardType is null.");
                }
                else if (mSlotID == -1)
                {
                    mSlotID = SmartOp.findSlotNumber(mCardType, null);
                }

                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);
                bool imzaDogru = _verify(sc, sessionID, mSlotID, aKeyLabel, aData, aSignature, aSigningAlg);
                sc.logout(sessionID);
                return imzaDogru;
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        //gemplus da hata veriyor
        private bool _verify(SmartCard aSC, long aSessionID, long aSlotID, String aKeyLabel, byte[] aImzalanan,
                             byte[] aDogrulanacak, String aSigningAlg)
        {

            PublicKeyFinderWithLabel kf = new PublicKeyFinderWithLabel(aSC, aSessionID, aKeyLabel);

            long[] mechs = aSC.getMechanismList(aSlotID);

            ISignatureScheme signatureScheme = SignatureSchemeFactory.getSignatureScheme(false, aSigningAlg, null, mechs, kf);
            CK_MECHANISM mechanism = signatureScheme.getMechanism();
            byte[] imzalanan = signatureScheme.getSignatureInput(aImzalanan);


            bool imzaDogru = false;
            try
            {
                aSC.verifyData(aSessionID, aKeyLabel, imzalanan, aDogrulanacak, mechanism.mechanism);
                imzaDogru = true;
            }
            catch (PKCS11Exception aEx)
            {
                Console.WriteLine(aEx.StackTrace);
            }
            return imzaDogru;



        }

        /**
	 * encrypt finds the public key with the given label and encrypts the given data.	 
	 * @param aKeyLabel   Label of the public key.
	 * @param aData Data to be encrypted.	 
	 * @return Encrypted data	 
	 */

        public byte[] encrypt(String aKeyLabel, byte[] aData)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                if (mCardType == null)
                {
                    throw new SmartCardException("CardType is null.");
                }
                else if (mSlotID == -1)
                {
                    mSlotID = SmartOp.findSlotNumber(mCardType, null);
                }

                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);

                byte[] sifreli = sc.encryptData(sessionID, aKeyLabel, aData, PKCS11Constants_Fields.CKM_RSA_PKCS);

                sc.logout(sessionID);

                return sifreli;
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
	 * encrypt finds the public key with the given label and encrypts the given data.	 
	 * @param aSC Created smart card object
	 * @param aSessionID Session handle (must be already logged in)
	 * @param aKeyLabel Label of the public key
	 * @param aData Data to be encrypted	 
	 * @return Encrypted data	
	 */

        public static byte[] encrypt(SmartCard aSC, long aSessionID, String aKeyLabel, byte[] aData)
        {
            byte[] sifreli = aSC.encryptData(aSessionID, aKeyLabel, aData, PKCS11Constants_Fields.CKM_RSA_PKCS);
            return sifreli;
        }

        /**
     * decrypt finds the private key with the given label and decrypts the given encrypted data. RSA/NONE/PKCS algorithm is used during decryption.	 
     * @param aKeyLabel Label of the private key.
     * @param aEncryptedData  Encrypted data. 
     * @return Decrypted data	
     */
        //@Deprecated
        [Obsolete]
        public byte[] decrypt(String aKeyLabel, byte[] aEncryptedData)
        {
            if (mCardType == null)
            {
                throw new SmartCardException("CardType is null.");
            }
            else if (mSlotID == -1)
            {
                mSlotID = SmartOp.findSlotNumber(mCardType, null);
            }

            SmartCard sc = null;
            long sessionID = -1;

            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);

                byte[] cozulmus = decrypt(sc, sessionID, aKeyLabel, aEncryptedData);

                sc.logout(sessionID);
                return cozulmus;
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }


        /**
	 * decrypt finds the private key that has the same CKA_ID value with the certificate having the given serial number
	 * and decrypts given data. RSA/NONE/PKCS is used for decryption.	 
	 * @param aCertSerialNo Certificate serial number
	 * @param aEncryptedData    Encrypted data.
	 * @return Decrypted data	 
	 */
        //@Deprecated
        [Obsolete]
        public byte[] decrypt(byte[] aCertSerialNo, byte[] aEncryptedData)
        {
            if (mCardType == null)
            {
                throw new SmartCardException("CardType is null.");
            }
            else if (mSlotID == -1)
            {
                mSlotID = SmartOp.findSlotNumber(mCardType, null);
            }

            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.login(sessionID, mPassword);
                byte[] cozulmus = decrypt(sc, sessionID, aCertSerialNo, aEncryptedData);

                sc.logout(sessionID);
                return cozulmus;
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        /**
	 * decrypt finds the private key that has the same CKA_ID value with the certificate having the given serial number
	 * and decrypts given data. RSA/NONE/PKCS is used for decryption.	 
	 * @param aSC Created smart card object
	 * @param aSessionID Session handle (must be logged in)
	 * @param aCertSerialNo Certificate serial number
	 * @param aEncryptedData Encrypted data.
	 * @return Decrypted data	 
	 */
        //@Deprecated
        [Obsolete]
        public static byte[] decrypt(SmartCard aSC, long aSessionID, byte[] aCertSerialNo, byte[] aCozulecek)
        {
            return aSC.decryptDataWithCertSerialNo(aSessionID, aCertSerialNo, PKCS11Constants_Fields.CKM_RSA_PKCS,
                                                   aCozulecek);
        }

        /**
	 * decrypt finds the private key with the given label and decrypts the given data. RSA/NONE/PKCS is used for decryption.	
	 * @param aSC Created smart card object
	 * @param aSessionID Session handle (must be logged in)
	 * @param aKeyLabel Label of the private key.  
	 * @param aEncryptedData Encrypted data.
	 * @return Decrypted data	
	 */
        //@Deprecated
        [Obsolete]
        public static byte[] decrypt(SmartCard aSC, long aSessionID, String aKeyLabel, byte[] aEncryptedData)
        {
            return aSC.decryptData(aSessionID, aKeyLabel, aEncryptedData, PKCS11Constants_Fields.CKM_RSA_PKCS);
        }
        /**
         * Returns password
         * @return
         */
        public String getPassword()
        {
            return mPassword;
        }
        /**
         * Returns slot
         * @return
         */
        public long getSlot()
        {
            return mSlotID;
        }
        /**
         * Returns card type
         * @return
         */
        public CardType getCardType()
        {
            return mCardType;
        }
        /**
         * Format smart card token
         * @param aSOpin
         * @param aNewPIN
         * @param aLabel 
         */
        public void formatToken(String aSOpin, String aNewPIN, String aLabel)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.formatToken(aSOpin, aNewPIN, aLabel, (int)mSlotID);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }
        /**
         * Change Password
         * @param aOldPass Old password
         * @param aNewPass new password
         */
        public void changePassword(String aOldPass, String aNewPass)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.changePassword(aOldPass, aNewPass, sessionID);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }
        /**
         * Change PUK
         * @param aSOpin
         * @param aNewSOPin 
         */
        public void changePuk(byte[] aSOPin, byte[] aNewSOPin)
        {
            SmartCard sc = null;
            long sessionID = -1;
            try
            {
                sc = new SmartCard(mCardType);
                sessionID = sc.openSession(mSlotID);
                sc.setSOPin(aSOPin, aNewSOPin, sessionID);
            }
            finally
            {
                if (sc != null && sessionID != -1)
                    sc.closeSession(sessionID);
            }
        }

        private static byte[][] msBastakiBytler = new[]
                                                      {
                                                          //sha1
                                                          new[]
                                                              {
                                                                  (byte) 0x30, (byte) 0x21, (byte) 0x30, (byte) 0x09,
                                                                  (byte) 0x06, (byte) 0x05, (byte) 0x2B,
                                                                  (byte) 0x0E, (byte) 0x03, (byte) 0x02, (byte) 0x1A,
                                                                  (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x14
                                                              },
                                                          //sha256
                                                          new[]
                                                              {
                                                                  (byte) 0x30, (byte) 0x31, (byte) 0x30, (byte) 0x0d,
                                                                  (byte) 0x06, (byte) 0x09, (byte) 0x60,
                                                                  (byte) 0x86, (byte) 0x48, (byte) 0x01, (byte) 0x65,
                                                                  (byte) 0x03, (byte) 0x04, (byte) 0x02, (byte) 0x01,
                                                                  (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x20
                                                              },
                                                          //sha384
                                                          new[]
                                                              {
                                                                  (byte) 0x30, (byte) 0x41, (byte) 0x30, (byte) 0x0d,
                                                                  (byte) 0x06, (byte) 0x09, (byte) 0x60,
                                                                  (byte) 0x86, (byte) 0x48, (byte) 0x01, (byte) 0x65,
                                                                  (byte) 0x03, (byte) 0x04, (byte) 0x02, (byte) 0x02,
                                                                  (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x30
                                                              },
                                                          //sha512
                                                          new[]
                                                              {
                                                                  (byte) 0x30, (byte) 0x51, (byte) 0x30, (byte) 0x0d,
                                                                  (byte) 0x06, (byte) 0x09, (byte) 0x60,
                                                                  (byte) 0x86, (byte) 0x48, (byte) 0x01, (byte) 0x65,
                                                                  (byte) 0x03, (byte) 0x04, (byte) 0x02, (byte) 0x03,
                                                                  (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x40
                                                              },
                                                      };

        /*
        private static int _ozetNo(String aOzetAlg)
        {
            if (aOzetAlg.Equals(Constants.DIGEST_SHA1))
                return 0;
            if (aOzetAlg.Equals(Constants.DIGEST_SHA256))
                return 1;
            if (aOzetAlg.Equals(Constants.DIGEST_SHA384))
                return 2;
            if (aOzetAlg.Equals(Constants.DIGEST_SHA512))
                return 3;

            throw new SmartCardException(aOzetAlg + " algorithm is not supported.");
        }*/

        public static bool _in(long aElement, long[] aList)
        {
            if (aList == null)
                return false;
            for (int i = 0; i < aList.Length; i++)
            {
                if (aList[i] == aElement)
                    return true;
            }
            return false;
        }


        /**
         * ATR degerinden yararlanarak, kart tipini ve slotu bulur. Eger birden fazla kart takili ise
         * kullaniciya secim yaptirilir.
         * @return birinci elemani slotID,ikinci elemani kart tipi olan Ikili nesnesi doner.
         * @throws KriptoException 
         */
        /*  public static Ikili<Long,CardType> kartTipiSlotBul() throws KriptoException
          {
              try
              {
                  TerminalFactory tf = TerminalFactory.getDefault();
                  List<CardTerminal> tList = tf.terminals().list(CardTerminals.State.CARD_PRESENT);
                  int length = tList.size();
                  String tName = "";
                  CardTerminal ct = null;

                  if(length == 0)
                  {
                      throw new KriptoException("Kart takili okuyucu bulunamadi.");
                  }
                  else if(length == 1)
                  {
                      tName = tList.get(0).getName();
                      ct = tList.get(0);
                  }
                  else
                  {
                      int cevap;
                      while(true)
                      {
                          cevap = _terminalSectir(tList);
                          if(cevap != -1) break;
                          tList = tf.terminals().list(CardTerminals.State.CARD_PRESENT);
                      }
                      tName = tList.get(cevap).getName();
                      ct = tf.terminals().getTerminal(tName);
                  }
                  Card card = ct.connect("*");
                  String atrHash = _convertToHex(card.getATR().getBytes());
                  card.disconnect(false);
                  CardType kartTipi = SmartCard.findCardType(atrHash);
                  long slotID = _terminaldenSlotBul(kartTipi, tName);
                  return new Ikili<Long,CardType>(slotID,kartTipi);
              }
              catch(Exception aEx)
              {
                  throw new KriptoException("ATR den kart tipi bulma isleminde hata olustu.", aEx);
              }
          }*/

        /* public static List<Ikili<Long,CardType>> kartTipiSlotListBul()
         throws KriptoException
         {
             try
             {
                 TerminalFactory tf = TerminalFactory.getDefault();
                 List<CardTerminal> tList = tf.terminals().list(CardTerminals.State.CARD_PRESENT);
                 int length = tList.size();
                 if(length == 0)
                 {
                     throw new KriptoException("Kart takili okuyucu bulunamadi.");
                 }
    		
                 List<Ikili<Long,CardType>> list = new ArrayList<Ikili<Long,CardType>>();
                 for(CardTerminal ct:tList)
                 {
                     String tName = ct.getName();
                     Card card = ct.connect("*");
                     String atrHash = _convertToHex(card.getATR().getBytes());
                     card.disconnect(false);
                     CardType kartTipi = SmartCard.findCardType(atrHash);
                     long slotID = _terminaldenSlotBul(kartTipi, tName);
                     list.add(new Ikili<Long,CardType>(slotID,kartTipi));
                 }
    		
                 return list;
             }
             catch(Exception aEx)
             {
                 throw new KriptoException("ATR den kart tipi bulma isleminde hata olustu.", aEx);
             }
         }*/

        /*  public static Card kartaBaglan() throws KriptoException
          {
              try
              {
                  TerminalFactory tf = TerminalFactory.getDefault();
                  List<CardTerminal> tList = tf.terminals().list(CardTerminals.State.CARD_PRESENT);
                  int length = tList.size();
                  String tName = "";
                  CardTerminal ct = null;

                  if(length == 0)
                  {
                      throw new KriptoException("Kart takili okuyucu bulunamadi.");
                  }
                  else if(length == 1)
                  {
                      tName = tList.get(0).getName();
                      ct = tList.get(0);
                  }
                  else
                  {
                      int cevap;
                      while(true)
                      {
                          cevap = _terminalSectir(tList);
                          if(cevap != -1) break;
                          tList = tf.terminals().list(CardTerminals.State.CARD_PRESENT);
                      }
                      tName = tList.get(cevap).getName();
                      ct = tf.terminals().getTerminal(tName);
                  }
                  Card card = ct.connect("*");
                  return card;
              }
              catch(Exception aEx)
              {
                  throw new KriptoException("Karta baglanma isleminde hata olustu.", aEx);
              }
          }*/


        /**
         * Finds slot number for the given card type. If there are more than one card in given card type,
         * it wants user to select one of the card.
         * @param aCardType
         *        Slot numarasi bulunmak istenen kartin tipidir.    
         * @return
         * @throws PKCS11Exception
         * @throws IOException
         * @throws SmartCardException
         */

        public static long findSlotNumber(CardType aCardType, ISelector selector)
        {
            SmartCard sc = new SmartCard(aCardType);
            long[] slotIDList = sc.getTokenPresentSlotList();
            int length = slotIDList.Length;
            if (length == 0)
            {
                throw new SmartCardException("No card present");
            }
            else if (length == 1)
            {
                return slotIDList[0];
            }
            else
            {
                int cevap = _slotSectir(sc, slotIDList, selector);
                if (cevap == -1)
                    return -1;
                return slotIDList[cevap];
            }
        }

        private static int _slotSectir(SmartCard aSC, long[] aSlotIDList, ISelector selector)
        {
            int length = aSlotIDList.Length;
            string[] slotDescList = new string[length];
            for (int i = 0; i < length; i++)
            {
                CK_SLOT_INFO slot = aSC.getSlotInfo(aSlotIDList[i]);
                slotDescList[i] = new string(slot.slotDescription).Trim() + " : Slot " + aSlotIDList[i];
            }

            return selectSC(selector, slotDescList);
        }

        private static int selectSC(ISelector selector, string[] scList)
        {
            if (selector == null)
                                
                throw new ESYAException(Resource.message(Resource.SC_SELECTOR_NOT_GIVEN));
            int cevap = selector.Select(Resource.message(Resource.SELECT_SC), scList);
            
            return cevap;
        }


        /**
      * Finds cardtype and slot number. If there are more than one smart card reader, a gui appears to 
      * allow user to select the card reader. If you don'e want to any gui apperance, you can use getCardTerminals and
      * getSlotAndCardType functions. 
      * @aApplication application
      * @return The first object of Pair is the slot number of card and the second object is card type.
      * If card type is unkown, slot number is null. If slot number can not be defined, it is assigned to
      * null.
      * @throws SmartCardException
      */

        public static Pair<long, CardType> findCardTypeAndSlot(CardType.Application aApplication, ISelector selector)
        {
            String[] cardTerminals = getCardTerminals();

            if (cardTerminals.Length == 0)
            {
                throw new SmartCardException("Kart takili okuyucu bulunamadi.");
            }
            else if (cardTerminals.Length == 1)
            {
                return getSlotAndCardType(cardTerminals[0], aApplication);
            }
            else
            {
                int index = selectSC(selector, cardTerminals);
                if (index == -1)
                    return null;
                return getSlotAndCardType(cardTerminals[index], aApplication);
            }
        }

        /**
	 * Finds cardtype and slot number. If there are more than one smart card reader, a gui appears to 
	 * allow user to select the card reader. If you don't want to any gui apperance, you can use getCardTerminals and
	 * getSlotAndCardType functions. 
	 *
	 * @return The first object of Pair is the slot number of card and the second object is card type.
	 * If card type is unkown, slot number is null. If slot number can not be defined, it is assigned to
	 * null.
	 * @throws SmartCardException
	 */

        public static Pair<long, CardType> findCardTypeAndSlot(ISelector selector)
        {
            return findCardTypeAndSlot(CardType.Application.ESIGNATURE, selector);
        }

        /**
         * Finds cardtype and slot number of connected cards.
         * @return List of slots. The first object of Pair is the slot number of card and the second object is card type.
         * If card type is unkown, slot number is null. If slot number can not be defined, it is assigned to
         * null. 
         * @throws SmartCardException 
         */

        public static List<Pair<long, CardType>> findCardTypesAndSlots()
        {
            return findCardTypesAndSlots(CardType.Application.ESIGNATURE);
        }

        /**
         * Finds cardtype and slot number of connected cards.
         * @param aApplication application
         * @return List of slots. The first object of Pair is the slot number of card and the second object is card type.
         * If card type is unkown, slot number is null. If slot number can not be defined, it is assigned to
         * null.
         * @throws SmartCardException 
         */

        public static List<Pair<long, CardType>> findCardTypesAndSlots(CardType.Application aApplication)
        {
            List<Pair<long, CardType>> list = new List<Pair<long, CardType>>();
            String[] cardTerminals = getCardTerminals();
            foreach (string cardTerminal in cardTerminals)
            {
                list.Add(getSlotAndCardType(cardTerminal, aApplication));
            }
            return list;
        }

        /**
         * Gets list of terminals.
         * @return list of terminals
         */

        public static String[] getCardTerminals()
        {
            return WinsCard.getTerminalList();
        }



        public static String[] getCardATRs()
        {
            String[] terminals = WinsCard.getTerminalList();
            String[] atrs = new String[terminals.Length];

            for (int i = 0; i < atrs.Length; i++)
            {
                atrs[i] = StringUtil.ToHexString(WinsCard.getAtr(terminals[i]));
            }

            return atrs;
        }

        /**
         * Gets slot and card type of requested terminal.
         * @param terminal
         * @return The first object of Pair is the slot number of card and the second object is card type.
         * If card type is unkown, slot number is null. If slot number can not be defined, it is assigned to
         * null.
         * @throws SmartCardException
         */

        public static Pair<long, CardType> getSlotAndCardType(String terminal)
        {
            return getSlotAndCardType(terminal, CardType.Application.ESIGNATURE);
        }

        /**
        * Gets slot and card type of requested terminal and application.
        * @param terminal
        * @param aApplication
        * @return The first object of Pair is the slot number of card and the second object is card type.
        * If card type is unkown, slot number is null. If slot number can not be defined, it is assigned to
        * null.
        * @throws SmartCardException
        */
        public static Pair<long, CardType> getSlotAndCardType(String terminal, CardType.Application aApplication)
        {
            byte[] atr = WinsCard.getAtr(terminal);
            CardType cardType = CardType.getCardTypeFromATR(StringUtil.ToString(atr), aApplication);
            long slot = _terminaldenSlotBul(cardType, terminal);
            return new Pair<long, CardType>(slot, cardType);
        }

        private static long _terminaldenSlotBul(CardType aCardType, string aTerminalName)
        {
            SmartCard sc = new SmartCard(aCardType);
            long[] slotList = sc.getTokenPresentSlotList();
            long slotID = 0;
            foreach (long slot in slotList)
            {
                String slotDesc = new String(sc.getSlotInfo(slot).slotDescription).Trim();
                int nullIndex = slotDesc.IndexOf(WinsCard.NULL_CHAR);
                if (nullIndex > 0)
                    slotDesc = slotDesc.Substring(0, slotDesc.IndexOf(WinsCard.NULL_CHAR));
                if (aTerminalName.Contains(slotDesc))
                {
                    slotID = slot;
                    break;
                }
            }

            return slotID;
        }
    }
}