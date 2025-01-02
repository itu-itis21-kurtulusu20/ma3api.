using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using System.Text;
using System.Threading;
using iaik.pkcs.pkcs11.wrapper;
using log4net;
using smartcard.src.tr.gov.tubitak.uekae.esya.api.smartcard.util;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.cardobject;
using tr.gov.tubitak.uekae.esya.src.api.asn.algorithms;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11
{
    public class SmartCard
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly CardType mCardType;
        private readonly CardType.Application mApplication;
        /**
        * Create smart card with a card type
        * @param aCardType
        * @throws PKCS11Exception
        * @throws IOException
        */
        private SmartCard()
        {
            try
            {
                //ECertificate cert;
                LV.getInstance().checkLicenceDates(LV.Products.AKILLIKART);
            }
            catch (LE ex)
            {
                throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

        /**
         * Create smart card with a card type
         * @param aCardType
         * @throws PKCS11Exception
         * @throws IOException
         */
        public SmartCard(CardType aCardType) : this(aCardType, CardType.Application.ESIGNATURE) { }

        /**
	     * Create smart card with a card type and application
	     * @param aCardType
	     * @param aApplication
	     * @throws PKCS11Exception
	     * @throws IOException
	     */
        public SmartCard(CardType aCardType, CardType.Application aApplication) : this()
        {

            logger.Debug("CardType: " + aCardType.getName());

            mCardType = aCardType;
            mCardType.getCardTemplate().getPKCS11Ops().initialize();
            mApplication = aApplication;
        }

        /**
         * getTokenPresentSlotList returns list of slot handles that has token present
         * 
         * @return list of slot handles that has token present
         * @throws PKCS11Exception
         */
        public long[] getTokenPresentSlotList()
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getTokenPresentSlotList();
        }

        /**
         * getSlotList returns list of slot handles
         * 
         * @return list of slot handles
         * @throws PKCS11Exception
         */
        public long[] getSlotList()
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getSlotList();
        }

        /**
	 * getSlotInfo returns information about the slot with the given id
	 * 
	 * @param aSlotID slot id
	 * @return slot information (slot description, manufacturer id, flags, hardware version, firmware version...) 	 
	 */
        public CK_SLOT_INFO getSlotInfo(long aSlotID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getSlotInfo(aSlotID);
        }

        /**
	 * isTokenPresent checks if token is present in the slot
	 * 
	 * @param aSlotID slot id
	 * @return true if token is present, false otherwise
	 */
        public bool isTokenPresent(long aSlotID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().isTokenPresent(aSlotID);
        }

        /**
	 * getTokenInfo returns information about the token present in the given slot
	 * 
	 * @param aSlotID slot id
	 * @return token information (label, manufacturer id, model...)	 
	 */
        public CK_TOKEN_INFO getTokenInfo(long aSlotID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getTokenInfo(aSlotID);
        }

        /**
	 * getSessionInfo returns information about the session with the given id 
	 * 
	 * @param aSessionID session id
	 * @return session information (slot, state, flags...)	 
	 */
        public CK_SESSION_INFO getSessionInfo(long aSessionID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getSessionInfo(aSessionID);
        }

        /**
	 * getMechanismList returns list of mechanism types supported by the token present in the given slot
	 * 
	 * @param aSlotID slot id
	 * @return list of mechanism types supported
	 */
        public long[] getMechanismList(long aSlotID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getMechanismList(aSlotID);
        }

        public static ArrayList getAttributeNames()
        {
            ArrayList fieldsPKCS11;
            {
                  FieldInfo[] fieldsPKCS11Constants = typeof(PKCS11Constants).GetFields();
                  FieldInfo[] fieldsPKCS11Constants_Fields = typeof(PKCS11Constants_Fields).GetFields();

                  fieldsPKCS11 = new ArrayList(fieldsPKCS11Constants);
                  fieldsPKCS11.AddRange(fieldsPKCS11Constants_Fields);
            }
            ArrayList attributeNames = new ArrayList();

            foreach (FieldInfo field in fieldsPKCS11)
            {
                string fieldName = field.Name;
                if (fieldName.StartsWith("CKA_"))
                {
                    attributeNames.Add(fieldName);
                }
            }

            return attributeNames;
        }

        /**
	 * openSession opens a session between the application and the token present in the given slot.
	 * 
	 * @param aSlotID slot id of the token
	 * @return session handle
	 */
        public long openSession(long aSlotID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().openSession(aSlotID);
        }

        /**
	 * closeSession closes the session between the application and the token
	 * 
	 * @param aSessionID	 
	 */
        public void closeSession(long aSessionID)
        {
            mCardType.getCardTemplate().getPKCS11Ops().closeSession(aSessionID);
        }

        /**
	 * login logs user to the session
	 * 
	 * @param aSessionID session handle
	 * @param aCardPIN  pin of the token	 
	 */
        public void login(long aSessionID, String aCardPIN)
        {
            mCardType.getCardTemplate().getPKCS11Ops().login(aSessionID, aCardPIN);
        }

        /**
	 * logout logs a user out from a token.
	 * 
	 * @param aSessionID session handle	 
	 */
        public void logout(long aSessionID)
        {
            mCardType.getCardTemplate().getPKCS11Ops().logout(aSessionID);
        }

        /**
	 * isAnyObjectExist searches for any type of object and return true if found. For searching private area,login is required. 
	 * If user does't login, search is done in public area of the token.
	 *  
	 * @param aSessionID session handle
	 * @return true if any object is found, false otherwise.	 
	 */
        public bool isAnyObjectExist(long aSessionID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().isAnyObjectExist(aSessionID);
        }

        /**
	 * importCertificate imports the given certificate to the token with the given label.
	 * 
	 * @param aSessionID session handle
	 * @param aCertLabel certificate is imported to the token with this label
	 * @param aCertificate certificate	 
	 */
        public void importCertificate(long aSessionID, String aCertLabel, ECertificate aCertificate)
        {
            mCardType.getCardTemplate().getPKCS11Ops().importCertificate(aSessionID, aCertLabel, aCertificate);
        }
        /**
         * createRSAKeyPair generates RSA public/private key pair according to the given parameters. 
         * If keys with the given label already exist, SmartCardException is thrown.
         * 
         * @param aSessionID session handle
         * @param aKeyLabel keys are generated with this label
         * @param aModulusBits   
         * @param aIsSign true if keys are for signing, false otherwise
         * @param aIsEncrypt true if keys are for encryption, false otherwise
         * @throws PKCS11Exception
         * @throws SmartCardException
         * @throws IOException
         */
        public long[] createRSAKeyPair(long aSessionID, string aKeyLabel, int aModulusBits, bool aIsSign, bool aIsEncrypt)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().createRSAKeyPair(aSessionID, aKeyLabel, aModulusBits, aIsSign, aIsEncrypt);
        }
        /**
         * generateRSAPrivateKey generates RSA Private Key in the Asn1 format.
         * 
         * @param aSessionID session handle
         * @param keySize key size
         * @return generated key
         * @throws ESYAException
         */
        public byte[] generateRSAPrivateKey(int keySize)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().generateRSAPrivateKey(keySize);
        }

        /**
	 * Sign byte array with given parameter
	 * @param aSessionID
	 * @param aKeyID
	 * @param aMechanism
	 * @param aToBeSigned
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
        public byte[] signDataWithKeyID(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, byte[] aToBeSigned)
        {
            checkLicense();

            byte[] signed = mCardType.getCardTemplate().getPKCS11Ops().signDataWithKeyID(aSessionID, aKeyID, aMechanism, aToBeSigned);

            signed = makeTLV(aMechanism, signed);

            return signed;
        }

        public ESubjectPublicKeyInfo createECKeyPair(long aSessionID, String aKeyLabel, EECParameters ecParameters, bool aIsSign, bool aIsEncrypt)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().createECKeyPair(aSessionID, aKeyLabel, ecParameters, aIsSign, aIsEncrypt);
        }
        /**
	 * signDataWithCertSerialNo finds the private key that has the same CKA_ID value with the certificate having the given serial number
	 * and signs the given data.
	 * 
	 * @param aSessionID session handle
	 * @param aSerialNumber certificate serial number
	 * @param aMechanism mechanism for signing 
	 * @param aToBeSigned Data to be signed
	 * @return signature	 
	 */
        [Obsolete]
        public byte[] signDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, long aMechanism, byte[] aToBeSigned)
        {
            checkLicense();

            CK_MECHANISM mech = new CK_MECHANISM();
            mech.mechanism = aMechanism;

            byte [] signed = mCardType.getCardTemplate().getPKCS11Ops().signDataWithCertSerialNo(aSessionID, aSerialNumber, mech, aToBeSigned);

            signed = makeTLV(mech, signed);

            return signed;
        }

        /**
         * signDataWithCertSerialNo finds the private key that has the same CKA_ID value with the certificate having the given serial number
         * and signs the given data.
         * 
         * @param aSessionID session handle
         * @param aSerialNumber certificate serial number
         * @param aMechanism mechanism for signing 
         * @param aToBeSigned Data to be signed
         * @return signature	 
         */
        public byte[] signDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, CK_MECHANISM aMechanism, byte[] aToBeSigned)
        {
            checkLicense();

            byte[] signed = mCardType.getCardTemplate().getPKCS11Ops().signDataWithCertSerialNo(aSessionID, aSerialNumber, aMechanism, aToBeSigned);

            signed = makeTLV(aMechanism, signed);

            return signed;
        }

        /**
	 * decryptDataWithCertSerialNo finds the private key that has the same CKA_ID value with the certificate having the given serial number
	 * and decrypts the given encrypted data.
	 * 
	 * @param aSessionID session handle
	 * @param aSerialNumber certificate serial number
	 * @param aMechanism mechanism for decryption
	 * @param aData encrypted data
	 * @return decrypted data	 
	 */
        [Obsolete]
        public byte[] decryptDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, long aMechanism, byte[] aData)
        {
            checkLicense();
            iaik.pkcs.pkcs11.wrapper.CK_MECHANISM mech = new iaik.pkcs.pkcs11.wrapper.CK_MECHANISM();
            mech.mechanism = aMechanism;
            return mCardType.getCardTemplate().getPKCS11Ops().decryptDataWithCertSerialNo(aSessionID, aSerialNumber, mech, aData);
        }

        public byte[] decryptDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, CK_MECHANISM aMechanism, byte[] aData)
        {
            checkLicense();
            return mCardType.getCardTemplate().getPKCS11Ops().decryptDataWithCertSerialNo(aSessionID, aSerialNumber, aMechanism, aData);
        }


        //Permanent objects, Token true
        public long[] getAllObjectIds(long aSessionID)
        {
            CK_ATTRIBUTE[] tokenAttr = new CK_ATTRIBUTE[1];
            tokenAttr[0] = new CK_ATTRIBUTE();
            tokenAttr[0].type = PKCS11Constants_Fields.CKA_TOKEN;
            tokenAttr[0].pValue = true;

            long[] objIds = this.objeAra(aSessionID, tokenAttr);

            return objIds;
        }

        public P11Object[] getAllObjectInfos(long aSessionID)
        {
            long[] objIds = getAllObjectIds(aSessionID);

            P11Object [] objs = new P11Object[objIds.Length];

            for (int i=0; i < objIds.Length; i++)
            {
                CK_ATTRIBUTE[] attrs = P11Object.getAttributesToFilled();

                this.getAttributeValue(aSessionID, objIds[i], attrs);

                objs[i] = new P11Object(attrs);
            }

            return objs;
        }

        public List<byte[]> getCertificates(long aSessionID)
        {
            checkLicense();
            return mCardType.getCardTemplate().getPKCS11Ops().getCertificates(aSessionID);
        }

        /**
	 * getSignatureCertificates returns signing certificates.(Searches for keyusage with digitalSignature bit set)
	 * 
	 * @param aSessionID session handle
	 * @return list of signing certificates. If no signing certificate is found,returns empty list.	 
	 */
        public List<byte[]> getSignatureCertificates(long aSessionID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getSignatureCertificates(aSessionID);
        }

        public byte[] getModulusOfKey(long aSessionID, long aObjID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getModulusOfKey(aSessionID, aObjID);
        }

        /**
         * return Private Key Object ID from Certificate Serial
         * @param aSessionID session handle
         * @param aCertSerialNo
         * @return
         * @throws PKCS11Exception
         * @throws SmartCardException
         */
        public long getPrivateKeyObjIDFromCertificateSerial(long aSessionID, byte[] aCertSerialNo)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getPrivateKeyObjIDFromCertificateSerial(aSessionID, aCertSerialNo);
        }


        /**
	 * return Private Key Object ID from Private Key label
	 * @param aSessionID session handle
	 * @param aLabel
	 * @return
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
        public long getPrivateKeyObjIDFromPrivateKeyLabel(long aSessionID, String aLabel)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getObjIDFromPrivateKeyLabel(aSessionID, aLabel);
        }


        /**
     * getEncryptionCertificates returns encrytion certificates. (Searches for keyusage with dataEncipherment bit set or keyEncipherment bit set)
     * @param aSessionID session handle
     * @return list of encryption certificates. If no encryption certificate is found,returns empty list.	 
     */
        public List<byte[]> getEncryptionCertificates(long aSessionID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getEncryptionCertificates(aSessionID);
        }

        /**
	 * getSignatureKeyLabels returns labels of signing keys.
	 *  
	 * @param aSessionID session handle
	 * @return list of labels	
	 */
        public String[] getSignatureKeyLabels(long aSessionID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getSignatureKeyLabels(aSessionID);
        }

        /**
	 * getEncryptionKeyLabels returns labels of encryption keys.
	 *  
	 * @param aSessionID session handle
	 * @return list of labels	 
	 */
        public String[] getEncryptionKeyLabels(long aSessionID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getEncryptionKeyLabels(aSessionID);
        }

        /**
	 * isObjectExist searches for any type of object with the given label and returns true if finds anything. For
	 * searching in private area, login is required. 
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of the searched object
	 * @return true if found,false otherwise	
	 */
        public bool isObjectExist(long aSessionID, String aLabel)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().isObjectExist(aSessionID, aLabel);
        }

        /**
	 * writePrivateData writes(as CKO_DATA) the given data to the token's private area with the given label. Login must be done before calling this method
	 * in order to write to private area.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label to be given to data
	 * @param aData data to be written	 
	 */
        public void writePrivateData(long aSessionID, String aLabel, byte[] aData)
        {
            mCardType.getCardTemplate().getPKCS11Ops().writePrivateData(aSessionID, aLabel, aData);
        }

        /**
	 * writePublicData writes(as CKO_DATA) the given data to the public area of the card with the given label.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label to be given to the data
	 * @param aData data to be written
	 */
        public void writePublicData(long aSessionID, String aLabel, byte[] aData)
        {
            mCardType.getCardTemplate().getPKCS11Ops().writePublicData(aSessionID, aLabel, aData);
        }

        /**
	 * readPrivateData reads data(type CKO_DATA) from the private area of the card. Must be logged in before calling 
	 * this method in order to reach private area.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of the data
	 * @return list of data with the given label	
	 */
        public List<byte[]> readPrivateData(long aSessionID, String aLabel)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().readPrivateData(aSessionID, aLabel);
        }

        /**
	 * readPublicData reads data(type CKO_DATA) from the public area of the card.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of the data
	 * @return list of data with the given label	 
	 */
        public List<byte[]> readPublicData(long aSessionID, String aLabel)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().readPublicData(aSessionID, aLabel);
        }

        /**
	 * isPublicKeyExist searches for public key with the given label and returns true if finds.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of the public key
	 * @return true if public key with the given label exists, false otherwise.	 
	 */
        public bool isPublicKeyExist(long aSessionID, String aLabel)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().isPublicKeyExist(aSessionID, aLabel);
        }

        /**
	 * isPrivateKeyExist searches for the private key with the given label. Must be logged in before calling 
	 * this method in order to reach private area.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of the private key
	 * @return true if private key with the given label is found, false otherwise.	 
	 */
        public bool isPrivateKeyExist(long aSessionID, String aLabel)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().isPrivateKeyExist(aSessionID, aLabel);
        }

        /**
	 * isCertificateExist searches for certificate with the given label.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of the certificate
	 * @return true if certificate is found,false otherwise.	 
	 */
        public bool isCertificateExist(long aSessionID, String aLabel)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().isCertificateExist(aSessionID, aLabel);
        }

        /**
	 * readCertificate returns certificates that has the given label as list.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel certificate label
	 * @return certificate list	 
	 */
        public List<byte[]> readCertificate(long aSessionID, String aLabel)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().readCertificate(aSessionID, aLabel);
        }

        /**
	 * readCertificate returns certificates that has the given serial number as list.
	 * 
	 * @param aSessionID session handle
	 * @param aCertSerialNo certificate serial number
	 * @return	 
	 */
        public byte[] readCertificate(long aSessionID, byte[] aCertSerialNo)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().readCertificate(aSessionID, aCertSerialNo);
        }

        public /*PublicKey*//*ESubjectPublicKeyInfo*/ERSAPublicKey readRSAPublicKey(long aSessionID, String aLabel)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().readRSAPublicKey(aSessionID, aLabel);
        }

        //public /*PublicKey*/ESubjectPublicKeyInfo readECPublicKey(long aSessionID, String aLabel)
        //{
        //    return msCardInfoMap[mCardType].getCardOps().readECPublicKey(aSessionID, aLabel);
        //}

        /**
	 * updatePrivateData finds the data (type CKO_DATA) with the given label and updates it with the given value.
	 * Must be logged in before calling this method in order to reach private area.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of data to be updated
	 * @param aValue new value for data	 
	 */
        public void updatePrivateData(long aSessionID, String aLabel, byte[] aValue)
        {
            mCardType.getCardTemplate().getPKCS11Ops().updatePrivateData(aSessionID, aLabel, aValue);
        }

        /**
	 * updatePublicData finds the data (type CKO_DATA) with the given label and updates it with the given value.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of data to be updated
	 * @param aValue new value for data	 
	 */
        public void updatePublicData(long aSessionID, String aLabel, byte[] aValue)
        {
            mCardType.getCardTemplate().getPKCS11Ops().updatePublicData(aSessionID, aLabel, aValue);
        }


        /**
         * Delete certificate with given key label
         * @param aSessionID session id
         * @param aKeyLabel label of the certificate that will be deleted
         * @return number of certificates deleted
         * @throws PKCS11Exception
         */
        public int deleteCertificate(long aSessionID, String aKeyLabel)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().deleteCertificate(aSessionID, aKeyLabel);
        }


        /**
	 * deletePrivateObject finds the object/s (type can be anything) with the given label and deletes it/them.
	 * Must be logged in before calling this method in order to reach private area.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of the object that will be deleted	 
	 */
        public void deletePrivateObject(long aSessionID, String aLabel)
        {
            mCardType.getCardTemplate().getPKCS11Ops().deletePrivateObject(aSessionID, aLabel);
        }

        /**
	 * deletePublicObject finds the object/s (type can be anything) with the given label and deletes it/them.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of the object that will be deleted	
	 */
        public void deletePublicObject(long aSessionID, String aLabel)
        {
            mCardType.getCardTemplate().getPKCS11Ops().deletePublicObject(aSessionID, aLabel);
        }

        /**
	 * deletePrivateData finds the object/s (type CKO_DATA) with the given label and deletes it/them. 
	 * Must be logged in before calling this method in order to reach private area.
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of the data that will be deleted	 
	 */
        public void deletePrivateData(long aSessionID, String aLabel)
        {
            mCardType.getCardTemplate().getPKCS11Ops().deletePrivateData(aSessionID, aLabel);
        }

        /**
	 * deletePublicData finds the object/s (type CKO_DATA) with the given label and deletes it/them. 
	 * 
	 * @param aSessionID session handle
	 * @param aLabel label of the data that will be deleted	
	 */
        public void deletePublicData(long aSessionID, String aLabel)
        {
            mCardType.getCardTemplate().getPKCS11Ops().deletePublicData(aSessionID, aLabel);
        }

        /**
	 * getRandomData generates random data
	 * 
	 * @param aSessionID session handle
	 * @param aDataLength length of random data to be generated	 
     * @return random data
	 */
        public byte[] getRandomData(long aSessionID, int aDataLength)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getRandomData(aSessionID, aDataLength);
        }

        /**
	 * getTokenSerialNo returns token's serial number.
	 * @param aSlotID id of slot that token is present
	 * @return token's serial number	 
	 */
        public byte[] getTokenSerialNo(long aSlotID)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().getTokenSerialNo(aSlotID);
        }

        /**
	 * signData finds the private key with the given label and signs the given data.
	 * Must be logged in before calling this method.
	 * 
	 * @param aSessionID session handle
	 * @param aKeyLabel label of the private key
	 * @param aToBeSigned Data to be signed
	 * @param aMechanism signature mechanism. Values can be obtained from PKCS11Constants with values starting CKM_
	 * @return Signature	 
	 */
        [Obsolete]
        public byte[] signData(long aSessionID, String aKeyLabel, byte[] aToBeSigned, long aMechanism)
        {
            checkLicense();

            CK_MECHANISM mech = new CK_MECHANISM();
            mech.mechanism = aMechanism;

            byte[] signed = mCardType.getCardTemplate().getPKCS11Ops().signData(aSessionID, aKeyLabel, aToBeSigned, mech);

            signed = makeTLV(mech, signed);

            return signed;

        }

        /**
          * signData finds the private key with the given label and signs the given data.
          * Must be logged in before calling this method.
          * 
          * @param aSessionID session handle
          * @param aKeyLabel label of the private key
          * @param aToBeSigned Data to be signed
          * @param aMechanism signature mechanism. Values can be obtained from PKCS11Constants with values starting CKM_
          * @return Signature	 
  */
        public byte[] signData(long aSessionID, String aKeyLabel, byte[] aToBeSigned, CK_MECHANISM aMechanism)
        {
            checkLicense();

            byte[] signed = mCardType.getCardTemplate().getPKCS11Ops().signData(aSessionID, aKeyLabel, aToBeSigned, aMechanism);

            signed = makeTLV(aMechanism, signed);

            return signed;
        }
        /**
	 * verifyData finds the public key and verifies the given signature.
	 * 
	 * @param aSessionID session handle
	 * @param aKeyLabel label of the public key
	 * @param aData Data that is signed
	 * @param aSignature Signature
	 * @param aMechanism signature mechanism. Values can be obtained from PKCS11Constants with values starting CKM_	 
	 */
        public void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aSignature, long aMechanism)
        {
            CK_MECHANISM mechanism = new CK_MECHANISM();
            mechanism.mechanism = aMechanism;

            mCardType.getCardTemplate().getPKCS11Ops().verifyData(aSessionID, aKeyLabel, aData, aSignature, mechanism);
        }

        public void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aSignature, CK_MECHANISM mechanism)
        {
            mCardType.getCardTemplate().getPKCS11Ops().verifyData(aSessionID, aKeyLabel, aData, aSignature, mechanism);
        }

        /**
	 * encryptData finds the public key with the given label and encrypts the given data.
	 * 
	 * @param aSessionID session handle
	 * @param aKeyLabel label of the public key
	 * @param aData data to be encrypted
	 * @param aMechanism encryption mechanism. Values can be obtained from PKCS11Constants with values starting CKM_
	 * @return encrypted data	 
	 */
        public byte[] encryptData(long aSessionID, String aKeyLabel, byte[] aData, long aMechanism)
        {
            CK_MECHANISM mech = new CK_MECHANISM();
            mech.mechanism = aMechanism;

            return mCardType.getCardTemplate().getPKCS11Ops().encryptData(aSessionID, aKeyLabel, aData, mech);
        }

        public byte[] encryptData(long aSessionID, String aKeyLabel, byte[] aData, CK_MECHANISM mechanism)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().encryptData(aSessionID, aKeyLabel, aData, mechanism);
        }


        // EC algorithms were tested with
        // AKIS v.2.5.2 Smartcard --> Signature didn't come in TLV format
        // UTIMACO HSM            --> Signature come in TLV format
        // NCIPHER HSM            --> Signature didn't come in TLV format
        // DIRAK HSM              --> Signature didn't come in TLV format
        byte[] makeTLV(CK_MECHANISM mech, byte[] signed)
        {
            if (mApplication == CardType.Application.EPASSPORT)
            {
                //Raw EC Signature. Do NOT apply TLV format.
                //Plain Signature Format (TR-03110-part-3-2016 Section A.7.4.1)
                return signed;
            }
            else if (mApplication == CardType.Application.ESIGNATURE)
            {
                // X509 Certificate And CRL EC Signature Format (RFC 3279 - Section 2.2.2)
                // CMS EC Signature Format (RFC 5453 - Section 7.2)
                if (mech.mechanism == PKCS11Constants_Fields.CKM_ECDSA
                    || mech.mechanism == PKCS11Constants_Fields.CKM_ECDSA_SHA1)
                {
                    byte[] signatureInTLVFormat;
                    if (mCardType == CardType.AKIS)
                    {
                        signatureInTLVFormat = ECSignatureTLVUtil.addTLVToSignature(signed);
                    }
                    else if (mCardType == CardType.UTIMACO)
                    {
                        signatureInTLVFormat = signed;
                    }
                    else if (mCardType == CardType.NCIPHER)
                    {
                        signatureInTLVFormat = ECSignatureTLVUtil.addTLVToSignature(signed);
                    }
                    else if (mCardType == CardType.DIRAKHSM)
                    {
                        signatureInTLVFormat = ECSignatureTLVUtil.addTLVToSignature(signed);
                    }
                    else
                    {
                        if (ECSignatureTLVUtil.isSignatureInTLVFormat(signed) == false)
                            signatureInTLVFormat = ECSignatureTLVUtil.addTLVToSignature(signed);
                        else
                            signatureInTLVFormat = signed;
                    }

                    return signatureInTLVFormat;
                }
                else
                    return signed;
            }
            else
            {
                throw new SmartCardException("Unknown Application for TLV format");
            }
        }

        /**
	 * decryptData finds the private key with given label and decrypts data. 
	 * Must be logged in before calling this method.
	 * 
	 * @param aSessionID session handle
	 * @param aKeyLabel label of the private key
	 * @param aData encrypted data
	 * @param aMechanism encryption mechanism. Values can be obtained from PKCS11Constants with values starting CKM_
	 * @return decrypted data	 
	 */
        public byte[] decryptData(long aSessionID, String aKeyLabel, byte[] aData, long aMechanism)
        {
            CK_MECHANISM mech = new CK_MECHANISM();
            mech.mechanism = aMechanism;

            checkLicense();
            return mCardType.getCardTemplate().getPKCS11Ops().decryptData(aSessionID, aKeyLabel, aData, mech);
        }
        public byte[] decryptData(long aSessionID, String aKeyLabel, byte[] aData, CK_MECHANISM mechanism)
        {
            checkLicense();
            return mCardType.getCardTemplate().getPKCS11Ops().decryptData(aSessionID, aKeyLabel, aData, mechanism);
        }

        /**
	 * importCertificateAndKey imports private key, certificate and public key extracted from certificate to the card. 
	 * Must be logged in before calling this method.
	 * 
	 * @param aSessionID session handle
	 * @param aCertLabel label of certificate 
	 * @param aKeyLabel label of keys 
	 * @param aPrivKey private key.It must be java.security.interfaces.RSAPrivateCrtKey or java.security.interfaces.ECPrivateKey. Otherwise,
	 * SmartCardException is thrown.
	 * @param aCert certificate
	 */
        public void importCertificateAndKey(long aSessionID, String aCertLabel, String aKeyLabel, /*PrivateKey*//*AsymmetricKeyParameter*/ EPrivateKeyInfo aPrivKey, ECertificate aCert)
        {
            mCardType.getCardTemplate().getPKCS11Ops().importCertificateAndKey(aSessionID, aCertLabel, aKeyLabel, aPrivKey, aCert);
        }

        public long[] importKeyPair(long aSessionID, string aLabel, ESubjectPublicKeyInfo aPubKeyInfo, EPrivateKeyInfo aPrivKeyInfo, EECParameters ecParameters, byte[] aSubject, bool aIsSign, bool aIsEncrypt)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().importKeyPair(aSessionID, aLabel, aPubKeyInfo, aPrivKeyInfo, ecParameters, aSubject, aIsSign, aIsEncrypt);
        }

        public long[] importRSAKeyPair(long aSessionID, string aLabel, EPrivateKeyInfo aPrivKey, byte[] aSubject, bool aIsSign, bool aIsEncrypt)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().importRSAKeyPair(aSessionID, aLabel, aPrivKey, aSubject, aIsSign, aIsEncrypt);
        }

        public long[] importECKeyPair(long aSessionID, string aLabel, ESubjectPublicKeyInfo aPubKeyInfo, EPrivateKeyInfo aPrivKeyInfo, EECParameters ecParameters, byte[] aSubject, bool aIsSign, bool aIsEncrypt)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().importECKeyPair(aSessionID, aLabel, aPubKeyInfo, aPrivKeyInfo, ecParameters, aSubject, aIsSign, aIsEncrypt);
        }

        /**
	 * changePassword changes user's pin.
	 * 
	 * @param aOldPass old pin
	 * @param aNewPass new pin
	 * @param aSessionID session handle
	 */
        public void changePassword(String aOldPass, String aNewPass, long aSessionID)
        {
            mCardType.getCardTemplate().getPKCS11Ops().changePassword(aOldPass, aNewPass, aSessionID);
        }

        /**
	 * formatToken changes user's pin and formats the token.
	 * 
	 * @param aSOpin SO's(Security Officer) pin
	 * @param aNewPIN User's new pin
	 * @param aLabel label to be given to the token
	 * @param slotID slot id that the token is present	
	 */
        public void formatToken(String aSOpin, String aNewPIN, String aLabel, int slotID)
        {
            mCardType.getCardTemplate().getPKCS11Ops().formatToken(aSOpin, aNewPIN, aLabel, slotID);
        }

        /**
	 * setSOPin changes SO's(Security Officer) pin
	 * 
	 * @param aSOPin SO's old pin
	 * @param aNewSOPin SO's new pin
	 * @param aSessionHandle session handle	
	 */
        public void setSOPin(byte[] aSOPin, byte[] aNewSOPin, long aSessionHandle)
        {
            mCardType.getCardTemplate().getPKCS11Ops().setSOPin(aSOPin, aNewSOPin, aSessionHandle);
        }

        /**
	 * changeUserPin changes user's pin.
	 * 
	 * @param aSOPin SO's pin
	 * @param aUserPin new pin for user
	 * @param aSessionHandle session handle	
	 */
        public void changeUserPin(byte[] aSOPin, byte[] aUserPin, long aSessionHandle)
        {
            mCardType.getCardTemplate().getPKCS11Ops().changeUserPin(aSOPin, aUserPin, aSessionHandle);
        }

        public bool setContainer(byte[] aContainerLabel, long aSessionHandle)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().setContainer(aContainerLabel, aSessionHandle);
        }

        public bool importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName, byte[] aPbCertData, int aSignOrEnc)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().importCertificateAndKeyWithCSP(aAnahtarCifti, aAnahtarLen, aScfname, aContextName, aPbCertData, aSignOrEnc);
        }

        public bool importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName, ECertificate aPbCertificate, int aSignOrEnc)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().importCertificateAndKeyWithCSP(aAnahtarCifti, aAnahtarLen, aScfname, aContextName, aPbCertificate, aSignOrEnc);
        }
        /**
         * Return card type
         * @return
         */
        public CardType getCardType()
        {
            return mCardType;
        }

        public long[] objeAra(long aSessionID, CK_ATTRIBUTE[] aTemplate)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().objeAra(aSessionID, aTemplate);
        }

        public void getAttributeValue(long aSessionID, long aObjectID, CK_ATTRIBUTE[] aTemplate)
        {
            mCardType.getCardTemplate().getPKCS11Ops().getAttributeValue(aSessionID, aObjectID, aTemplate);
        }

        private static void checkLicense()
        {
            try
            {
                bool isTest = LV.getInstance().isTestLicense(LV.Products.AKILLIKART);
                if (isTest)
                {
                    Thread.Sleep(2000);
                }
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

        public byte[] wrapKey(long sessionID, CK_MECHANISM mechanism, String wrapperKeyLabel, String labelOfKeyToWrap)
        {
            return mCardType.getCardTemplate().getPKCS11Ops()
                .wrapKey(sessionID, mechanism, wrapperKeyLabel, labelOfKeyToWrap);
        }

        public long importSecretKey(long aSessionID, SecretKeyTemplate aKeyTemplate)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().importSecretKey(aSessionID, aKeyTemplate);
        }
        public long createSecretKey(long sessionID, SecretKeyTemplate template)
        {
            return mCardType.getCardTemplate().getPKCS11Ops().createSecretKey(sessionID, template);
        }
    }
}
