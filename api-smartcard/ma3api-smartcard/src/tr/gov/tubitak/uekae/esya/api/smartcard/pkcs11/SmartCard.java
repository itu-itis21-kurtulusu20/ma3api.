package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.infra.cache.FixedSizedCache;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.UnwrapObjectsResults;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.WrappedObjectsWithAttributes;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.algorithm.PKCS11AlgorithmUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.DirakLibOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.SecretKey;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.cardobject.P11Object;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure.CK_MECHANISM_STRUCTURE;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECSignatureTLVUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.util.*;


public class SmartCard implements ISmartCard
{
	private static Logger logger = LoggerFactory.getLogger(SmartCard.class);

	protected CardType mCardType;
	protected Application mApplication;
    protected boolean isFipsEnabled = false;

	protected FixedSizedCache<Long, long[]> slotAndMechanismListCache = new FixedSizedCache<>(256, Duration.ofSeconds(5));

	protected long mSlotID = -1;
	protected long mSessionID = -1;

	protected long [] mechsToBeRemoved = null;

	private SmartCard(){
		try
		{
            //ECertificate cert;
			LV.getInstance().checkLD(Urunler.AKILLIKART);
			mApplication = Application.ESIGNATURE;
		}
		catch(LE e)
		{
			throw new ESYARuntimeException("Lisans kontrolu basarisiz. "+ e.getMessage(), e);
		}
	}

	/**
	 * Create smart card with a card type
	 * @param aCardType
	 * @throws PKCS11Exception
	 * @throws IOException
	 */
	public SmartCard(CardType aCardType)
			throws PKCS11Exception,IOException
	{
		this(aCardType, Application.ESIGNATURE);
	}



	/**
	 * Create smart card with a card type and application
	 * @param aCardType
	 * @param aApplication
	 * @throws PKCS11Exception
	 * @throws IOException
	 */
	public SmartCard(CardType aCardType, Application aApplication)
			throws PKCS11Exception,IOException
	{
		this();
		logger.debug("CardType: " + aCardType.getName());

		mCardType = aCardType;
		mCardType.getCardTemplate().getPKCS11Ops().initialize();
		mApplication = aApplication;
	}


	/**
	 * Create smart card with a card type
	 * @param aCardTypeName
	 * @param smartCardDllName
	 * @throws PKCS11Exception
	 * @throws IOException
	 */
	public SmartCard(String aCardTypeName,String smartCardDllName) throws PKCS11Exception, IOException {
		this();
		mCardType = CardType.getCardTypeFromName(aCardTypeName);
		if(mCardType == CardType.UNKNOWN){
			mCardType = CardType.getCardType(smartCardDllName);
		}
		mCardType.getCardTemplate().getPKCS11Ops().initialize();
	}

	/**
	 * Create smart card with a card type name
	 * @param aCardType
	 * @throws PKCS11Exception
	 * @throws IOException
	 */
	public SmartCard(String aCardType) throws PKCS11Exception, IOException {
		this(CardType.getCardType(aCardType));
	}



    /**
     * Sets fips mode.
     * @param mode
     */
	public void setFipsMode(boolean mode)
    {
        isFipsEnabled = mode;
    }


	/**
	 *
	 * @return FipsMode Status. true if fips mode enabled
	 */
	public boolean isFipsModeEnable()
	{
		return isFipsEnabled;
	}


	/**
	 * Sets application in order to change behavior smartcard operations
	 * @param aApplication
	 */
	public void setApplication(Application aApplication)
	{
		mApplication = aApplication;
	}


	/**
	 *
	 * @return Application info
	 */
	public Application getApplication()
	{
		return mApplication;
	}

	/**
	 * getTokenPresentSlotList returns list of slot handles that has token present
	 *
	 * @return list of slot handles that has token present
	 * @throws PKCS11Exception
	 */
	public long[] getTokenPresentSlotList()
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getTokenPresentSlotList();
	}


	public List<Pair<Long,String>> getTokenPresentSlotListWithDescription() throws PKCS11Exception {
		return mCardType.getCardTemplate().getPKCS11Ops().getTokenPresentSlotListWithDescription();
	}

	/**
	 * getSlotList returns list of slot handles
	 *
	 * @return list of slot handles
	 * @throws PKCS11Exception
	 */
	public long[] getSlotList()
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getSlotList();
	}

	public List<SlotInfo> getSlotInfoList() throws PKCS11Exception{
		long[] slotList = getSlotList();
		List<SlotInfo> slotInfos = new ArrayList<SlotInfo>();

		for(int i=0; i < slotList.length; i++){
			long slotId = slotList[i];
			CK_TOKEN_INFO tokenInfo = getTokenInfo(slotId);

			SlotInfo slotInfo = new SlotInfo();
			slotInfo.setSlotId(slotId);
			slotInfo.setSlotLabel(new String(tokenInfo.label).trim());

			slotInfos.add(slotInfo);
		}
		return  slotInfos;
	}

	/**
	 *
	 * @return slot id latest successful opensession function
	 */
	public long getLatestSlotID()
	{
		return mSlotID;
	}

	/**
	 *
	 * @return session id latest successful opensession function
	 */
	public long getLatestSessionID()
	{
		return mSessionID;
	}

	/**
	 * getSlotInfo returns information about the slot with the given id
	 *
	 * @param aSlotID slot id
	 * @return slot information (slot description, manufacturer id, flags, hardware version, firmware version...) 
	 * @throws PKCS11Exception
	 */
	public CK_SLOT_INFO getSlotInfo(long aSlotID)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getSlotInfo(aSlotID);
	}

	/**
	 * getSessionInfo returns information about the session with the given id 
	 *
	 * @param aSessionID session id
	 * @return session information (slot, state, flags...)
	 * @throws PKCS11Exception
	 */
	public CK_SESSION_INFO getSessionInfo(long aSessionID)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getSessionInfo(aSessionID);
	}

	/**
	 * isTokenPresent checks if token is present in the slot
	 *
	 * @param aSlotID slot id
	 * @return true if token is present, false otherwise
	 * @throws PKCS11Exception
	 */
	public boolean isTokenPresent(long aSlotID)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().isTokenPresent(aSlotID);
	}


	/**
	 * getTokenInfo returns information about the token present in the given slot
	 *
	 * @param aSlotID slot id
	 * @return token information (label, manufacturer id, model...)
	 * @throws PKCS11Exception
	 */
	public CK_TOKEN_INFO getTokenInfo(long aSlotID)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getTokenInfo(aSlotID);
	}


	/**
	 * getMechanismList returns list of mechanism types supported by the token present in the given slot
	 *
	 * @param aSlotID slot id
	 * @return list of mechanism types supported
	 * @throws PKCS11Exception
	 */
	public long[] getMechanismList(long aSlotID)
			throws PKCS11Exception {
		long[] mechanisms = slotAndMechanismListCache.getItem(aSlotID);
		if (mechanisms == null) {
			mechanisms = mCardType.getCardTemplate().getPKCS11Ops().getMechanismList(aSlotID);
			mechanisms = removeUnWantedMechanisms(mechanisms, mechsToBeRemoved);
			slotAndMechanismListCache.put(aSlotID, mechanisms);
		}
		return mechanisms;
	}

	public void setMechanismsToBeRemoved(long [] mechsToRemove) {
		this.mechsToBeRemoved = mechsToRemove;
	}

	private long[] removeUnWantedMechanisms(long[] mechanisms, long[] mechsToRemove) {
		if(mechsToRemove == null || mechsToRemove.length == 0)
			return mechanisms;

		Set<Long> set = new HashSet<>();
		for (long value : mechsToRemove) {
			set.add(value);
		}

		// Use a temporary list to store the filtered elements
		List<Long> tempList = new ArrayList<>();
		for (long value : mechanisms) {
			if(!set.contains(value))
				tempList.add(value);
		}

		// Convert the List back to a long[]
		long[] result = new long[tempList.size()];
		for (int i = 0; i < tempList.size(); i++) {
			result[i] = tempList.get(i);
		}

		return result;
	}

	/**
	 * Translates provided mechanisms from long values to String names.
	 * @param mechanisms Mechanism values to get the names of.
	 * @return Names of the mechanisms provided.
	 */
	public static String[] getMechanismNames(long[] mechanisms) throws IllegalAccessException {
        ArrayList<Field> fieldsPKCS11;
        {
    		Field[] fieldsPKCS11Constants = PKCS11Constants.class.getFields();
    		Field[] fieldsPKCS11ConstantsExtended = PKCS11ConstantsExtended.class.getFields();

		    fieldsPKCS11 = new ArrayList<>(Arrays.asList(fieldsPKCS11Constants));
    		fieldsPKCS11.addAll(Arrays.asList(fieldsPKCS11ConstantsExtended));
        }

		String[] mechanismStrings = new String[mechanisms.length];

		for (int c = 0; c < mechanisms.length; c++) {
			boolean isFound = false;
			long mechanism = mechanisms[c];

			for (Field field : fieldsPKCS11) {
				if (field.getName().startsWith("CKM")) {
					if (field.getLong(null) == mechanism) {
						mechanismStrings[c] = field.getName();

						isFound = true;
						break;
					}
				}
			}

			// if field is not found in CKMs: use the hex value
			if (!isFound) {
				mechanismStrings[c] = StringUtil.toFourByteHex((int) mechanism);;
			}
		}

		return mechanismStrings;
	}

	/**
	 * Gets names of mechanisms supported by the smart card slot.
	 * @param aSlotID The slot's ID.
	 * @return Names of mechanisms supported by the SC slot.
	 */
	public String[] getMechanismNames(long aSlotID) throws PKCS11Exception, IllegalAccessException {
		return getMechanismNames(getMechanismList(aSlotID));
	}

	/**
	 * openSession opens a session between the application and the token present in the given slot.
	 *
	 * @param aSlotID slot id of the token
	 * @return session handle
	 * @throws PKCS11Exception
	 */
	public long openSession(long aSlotID)
			throws PKCS11Exception
	{
		long sessionID = mCardType.getCardTemplate().getPKCS11Ops().openSession(aSlotID);

		mSessionID = sessionID;
		mSlotID = aSlotID;

		return sessionID;
	}

	/**
	 * closeSession closes the session between the application and the token
	 *
	 * @param aSessionID
	 * @throws PKCS11Exception
	 */
	public void closeSession(long aSessionID)
			throws PKCS11Exception
	{
		mCardType.getCardTemplate().getPKCS11Ops().closeSession(aSessionID);
	}


	/**
	 * login logs user to the session
	 *
	 * @param aSessionID session handle
	 * @param aCardPIN  pin of the token
	 * @throws PKCS11Exception
	 */
	public void login(long aSessionID,String aCardPIN)
			throws PKCS11Exception
	{
		mCardType.getCardTemplate().getPKCS11Ops().login(aSessionID, aCardPIN);
	}


	/**
	 * logout logs a user out from a token.
	 *
	 * @param aSessionID session handle
	 * @throws PKCS11Exception
	 */
	public void logout(long aSessionID)
			throws PKCS11Exception
	{
		mCardType.getCardTemplate().getPKCS11Ops().logout(aSessionID);
	}

	/**
	 * isAnyObjectExist searches for any type of object and return true if found. For searching private area,login is required. 
	 * If user does't login, search is done in public area of the token.
	 *
	 * @param aSessionID session handle
	 * @return true if any object is found, false otherwise.
	 * @throws PKCS11Exception
	 */
	public boolean isAnyObjectExist(long aSessionID)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().isAnyObjectExist( aSessionID);
	}


	/**
	 * importCertificate imports the given certificate to the token with the given label.
	 *
	 * @param aSessionID session handle
	 * @param aCertLabel certificate is imported to the token with this label
	 * @param aSertifika certificate
	 * @throws PKCS11Exception
	 */
	public void importCertificate(long aSessionID,String aCertLabel,X509Certificate aSertifika)
			throws PKCS11Exception
	{
		mCardType.getCardTemplate().getPKCS11Ops().importCertificate( aSessionID, aCertLabel, aSertifika);
	}


	/**
	 * createKeyPair generates public/private key pair according to the given parameters. If keys with the given label already exist, SmartCardException is thrown.
	 *
	 * @param aSessionID session handle
	 * @param aKeyLabel keys are generated with this label
	 * @param aParamSpec parameters used for key generation. For RSA, java.security.spec.RSAKeyGenParameterSpec;  
	 * for ECDSA java.security.spec.ECParameterSpec or java.security.spec.ECGenParameterSpec type parameters must be supplied.
	 * @param aIsSign true if keys are for signing, false otherwise
	 * @param aIsEncrypt true if keys are for encryption, false otherwise
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 * @throws IOException
	 */
	public long[] createKeyPair(long aSessionID, String aKeyLabel, AlgorithmParameterSpec aParamSpec, boolean aIsSign, boolean aIsEncrypt)
			throws PKCS11Exception,SmartCardException,IOException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().createKeyPair(aSessionID, aKeyLabel, aParamSpec, aIsSign, aIsEncrypt);
	}
	/**
	 * createKeyPair generates public/private key pair according to the given KeyPairTemplate.
	 *
	 *
	 * @param aSessionID session handle (login reqires)
	 * @param template Key Pair template to generate keys
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 * @throws IOException
	 * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate
	 * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate
	 */
	public KeySpec createKeyPair(long aSessionID, KeyPairTemplate template) throws PKCS11Exception,SmartCardException,IOException {
		return mCardType.getCardTemplate().getPKCS11Ops().createKeyPair(aSessionID, template);
	}

	/**
	 * generateRSAPrivateKey generates RSA Private Key in the Asn1 format.
	 *
	 * @param aSessionID session handle
	 * @param keySize key size
	 * @return generated key
	 * @throws ESYAException
	 */
	public byte [] generateRSAPrivateKey(long aSessionID, int keySize) throws ESYAException{
		return mCardType.getCardTemplate().getPKCS11Ops().generateRSAPrivateKey(aSessionID, keySize);
	}

	public KeyPair generateECKeyPair(long aSessionID, ECParameterSpec ecParameterSpec) throws ESYAException{
		return mCardType.getCardTemplate().getPKCS11Ops().generateECKeyPair(aSessionID, ecParameterSpec);
	}

	// EC algorithms were tested with
	// AKIS v.2.5.2 Smartcard --> Signature didn't come in TLV format
	// UTIMACO HSM            --> Signature come in TLV format
	// NCIPHER HSM            --> Signature didn't come in TLV format
	// DIRAK HSM              --> Signature didn't come in TLV format
	byte[] makeTLV(CK_MECHANISM mech,byte[] signed) throws  SmartCardException
	{
		if(mApplication == Application.EPASSPORT)
		{
			//Raw EC Signature. Do NOT apply TLV format.
			//Plain Signature Format (TR-03110-part-3-2016 Section A.7.4.1)
			return signed;
		}
		else if(mApplication == Application.ESIGNATURE)
		{
			// X509 Certificate And CRL EC Signature Format (RFC 3279 - Section 2.2.2)
			// CMS EC Signature Format (RFC 5753 - Section 7.2)
			if (PKCS11AlgorithmUtil.isECMechanism(mech.mechanism))
			{
				byte[] signatureInTLVFormat;
				if(mCardType == CardType.AKIS) {
					signatureInTLVFormat = ECSignatureTLVUtil.addTLVToSignature(signed);
				}else if(mCardType == CardType.UTIMACO){
					signatureInTLVFormat = signed;
				}else if(mCardType == CardType.NCIPHER){
					signatureInTLVFormat = ECSignatureTLVUtil.addTLVToSignature(signed);
				}else if(mCardType == CardType.DIRAKHSM){
					signatureInTLVFormat = ECSignatureTLVUtil.addTLVToSignature(signed);
				}else{
					if(ECSignatureTLVUtil.isSignatureInTLVFormat(signed) == false)
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
	 * signDataWithCertSerialNo finds the private key that has the same CKA_ID value with the certificate having the given serial number
	 * and signs the given data.
	 *
	 * @param aSessionID session handle
	 * @param aSerialNumber certificate serial number
	 * @param aMechanism mechanism for signing 
	 * @param aToBeSigned Data to be signed
	 * @return signature
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	@Deprecated
	public byte[] signDataWithCertSerialNo(long aSessionID,byte[] aSerialNumber,long aMechanism,byte[] aToBeSigned)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();

		CK_MECHANISM mech = new CK_MECHANISM(0L);
		mech.mechanism = aMechanism;

		byte[] signed =  mCardType.getCardTemplate().getPKCS11Ops().signDataWithCertSerialNo( aSessionID, aSerialNumber, mech, aToBeSigned);
		signed = makeTLV(mech,signed);
		return signed;
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
	public byte[] signDataWithKeyID(long aSessionID,long aKeyID,CK_MECHANISM aMechanism,byte[] aToBeSigned)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();

		byte[] signed =  mCardType.getCardTemplate().getPKCS11Ops().signDataWithKeyID( aSessionID, aKeyID, aMechanism, aToBeSigned);

		signed = makeTLV(aMechanism,signed);
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
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public byte[] signDataWithCertSerialNo(long aSessionID,byte[] aSerialNumber,CK_MECHANISM aMechanism,byte[] aToBeSigned)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();
		byte[] signed =  mCardType.getCardTemplate().getPKCS11Ops().signDataWithCertSerialNo( aSessionID, aSerialNumber, aMechanism, aToBeSigned);
		signed = makeTLV(aMechanism,signed);
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
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	@Deprecated
	public byte[] decryptDataWithCertSerialNo(long aSessionID,byte[] aSerialNumber,long aMechanism,byte[] aData)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();

		CK_MECHANISM mech = new CK_MECHANISM(0L);
		mech.mechanism = aMechanism;

		return mCardType.getCardTemplate().getPKCS11Ops().decryptDataWithCertSerialNo( aSessionID, aSerialNumber, mech, aData);
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
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */

	public byte[] decryptDataWithCertSerialNo(long aSessionID,byte[] aSerialNumber,CK_MECHANISM aMechanism,byte[] aData)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();
		return mCardType.getCardTemplate().getPKCS11Ops().decryptDataWithCertSerialNo(aSessionID, aSerialNumber, aMechanism, aData);
	}

	//Permanent objects, Token true
	public long[] getAllObjectIds(long aSessionID) throws PKCS11Exception {
		CK_ATTRIBUTE[] tokenAttr = new CK_ATTRIBUTE[1];
		tokenAttr[0] = new CK_ATTRIBUTE();
		tokenAttr[0].type = PKCS11Constants.CKA_TOKEN;
		tokenAttr[0].pValue = true;

		long[] objIds = this.objeAra(aSessionID, tokenAttr);

		return objIds;
	}

	public P11Object[] getAllObjectInfos(long aSessionID) throws PKCS11Exception {
		long[] objIds = getAllObjectIds(aSessionID);
		P11Object[] objs = new P11Object[objIds.length];

		for (int i = 0; i < objIds.length; i++) {
			CK_ATTRIBUTE[] attrs = P11Object.getAttributesToFilled();

			this.getAttributeValue(aSessionID, objIds[i], attrs);

			objs[i] = new P11Object(objIds[i], attrs);
		}

		return objs;
	}

	public List<byte[]> getCertificates(long aSessionID) throws PKCS11Exception,SmartCardException
	{
		checkLicense();
		return mCardType.getCardTemplate().getPKCS11Ops().getCertificates(aSessionID);
	}

	/**
	 * getSignatureCertificates returns signing certificates.(Searches for keyusage with digitalSignature bit set)
	 *
	 * @param aSessionID session handle
	 * @return list of signing certificates. If no signing certificate is found,returns empty list.
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public List<byte[]> getSignatureCertificates(long aSessionID)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getSignatureCertificates(aSessionID);
	}

	/**
	 * getEncryptionCertificates returns encryption certificates. (Searches for keyusage with dataEncipherment bit set or keyEncipherment bit set)
	 * @param aSessionID session handle
	 * @return list of encryption certificates. If no encryption certificate is found,returns empty list.
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public List<byte[]> getEncryptionCertificates(long aSessionID)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getEncryptionCertificates(aSessionID);
	}


	/**
	 * getSignatureKeyLabels returns labels of signing keys.
	 *
	 * @param aSessionID session handle
	 * @return list of labels
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public String [] getSignatureKeyLabels(long aSessionID)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getSignatureKeyLabels(aSessionID);
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
			throws SmartCardException,PKCS11Exception
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
			throws SmartCardException,PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getObjIDFromPrivateKeyLabel(aSessionID, aLabel);
	}

	/**
	 * return Secret Key Object ID from Secret Key label
	 * @param aSessionID session handle
	 * @param aLabel
	 * @return
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public long getSecretKeyObjIDFromSecretKeyLabel(long aSessionID, String aLabel) throws SmartCardException,PKCS11Exception {
		return mCardType.getCardTemplate().getPKCS11Ops().getObjIDFromSecretKeyLabel(aSessionID, aLabel);
	}


	public long getPublicKeyObjIDFromPublicKeyLabel(long aSessionID, String aLabel)
			throws SmartCardException,PKCS11Exception{
		return mCardType.getCardTemplate().getPKCS11Ops().getObjIDFromPublicKeyLabel(aSessionID, aLabel);
	}


	/**
	 * getEncryptionKeyLabels returns labels of encryption keys.
	 *
	 * @param aSessionID session handle
	 * @return list of labels
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public String [] getEncryptionKeyLabels(long aSessionID)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getEncryptionKeyLabels(aSessionID);
	}

	/**
	 * getWrapperKeyLabels returns labels of wrapper keys.
	 *
	 * @param aSessionID session handle
	 * @return list of labels
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public String [] getWrapperKeyLabels(long aSessionID)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getWrapperKeyLabels(aSessionID);
	}

	/**
	 * getUnwrapperKeyLabels returns labels of unwrapper keys.
	 *
	 * @param aSessionID session handle
	 * @return list of labels
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public String [] getUnwrapperKeyLabels(long aSessionID)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getUnwrapperKeyLabels(aSessionID);
	}

	@Override
	public CK_ATTRIBUTE[] getAllAttributes(long sessionID, long objectID) throws PKCS11Exception {
		return mCardType.getCardTemplate().getPKCS11Ops().getAllAttributes(sessionID, objectID);
	}


	@Override
	public Map<Long, String> getAttributeNames() {
		Map<Long,String> attributeNames = new HashMap<>();

		Map<Long,String> pkcs11AttributeNames = PKCS11Names.getAttributeNames();

		attributeNames.putAll(pkcs11AttributeNames);

		Map<Long,String> vendorSpecificAttributeTypesWithNames = mCardType.getCardTemplate().getVendorSpecificAttributeTypesWithNames();

		attributeNames.putAll(vendorSpecificAttributeTypesWithNames);

		return attributeNames;
	}

	/**
	 * isObjectExist searches for any type of object with the given label and returns true if finds anything. For
	 * searching in private area, login is required.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label of the searched object
	 * @return true if found,false otherwise
	 * @throws PKCS11Exception
	 */
	public boolean isObjectExist(long aSessionID,String aLabel)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().isObjectExist( aSessionID, aLabel);
	}

	/**
	 * writePrivateData writes(as CKO_DATA) the given data to the token's private area with the given label. Login must be done before calling this method
	 * in order to write to private area.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label to be given to data
	 * @param aData data to be written
	 * @throws PKCS11Exception
	 */
	public void writePrivateData(long aSessionID,String aLabel,byte[] aData)
			throws PKCS11Exception
	{
		mCardType.getCardTemplate().getPKCS11Ops().writePrivateData(aSessionID, aLabel, aData);
	}

	/**
	 * writePublicData writes(as CKO_DATA) the given data to the public area of the card with the given label.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label to be given to the data
	 * @param aData data to be written
	 * @throws PKCS11Exception
	 */
	public void writePublicData(long aSessionID, String aLabel,byte[] aData)
			throws PKCS11Exception
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
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no data in private area with the given label exists.
	 */
	public List<byte[]> readPrivateData(long aSessionID,String aLabel)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().readPrivateData(aSessionID, aLabel);
	}


	/**
	 * readPublicData reads data(type CKO_DATA) from the public area of the card.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label of the data
	 * @return list of data with the given label
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no data with the given label in public area exists.
	 */
	public List<byte[]> readPublicData(long aSessionID,String aLabel)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().readPublicData(aSessionID, aLabel);
	}


	/**
	 * isPublicKeyExist searches for public key with the given label and returns true if finds.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label of the public key
	 * @return true if public key with the given label exists, false otherwise.
	 * @throws PKCS11Exception
	 */
	public boolean isPublicKeyExist(long aSessionID,String aLabel)
			throws PKCS11Exception
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
	 * @throws PKCS11Exception
	 */
	public boolean isPrivateKeyExist(long aSessionID,String aLabel)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().isPrivateKeyExist(aSessionID, aLabel);
	}


	/**
	 * isCertificateExist searches for certificate with the given label.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label of the certificate
	 * @return true if certificate is found,false otherwise.
	 * @throws PKCS11Exception
	 */
	public boolean isCertificateExist(long aSessionID,String aLabel)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().isCertificateExist(aSessionID, aLabel);
	}


	/**
	 * readCertificate returns certificates that has the given label as list.
	 *
	 * @param aSessionID session handle
	 * @param aLabel certificate label
	 * @return certificate list
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no certificate with given label is found.
	 */
	public List<byte[]> readCertificate(long aSessionID,String aLabel)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().readCertificate(aSessionID, aLabel);
	}


	/**
	 * readCertificate returns certificates that has the given serial number as list.
	 *
	 * @param aSessionID session handle
	 * @param aCertSerialNo certificate serial number
	 * @return
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no certificate with the given serial number is found.
	 */
	public byte[] readCertificate(long aSessionID,byte[] aCertSerialNo)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().readCertificate(aSessionID, aCertSerialNo);
	}


	/**
	 * readPublicKeySpec returns specification of public key with the given label.For now, supported algorithms
	 * are RSA and ECDSA
	 *
	 * @param aSessionID session handle
	 * @param aLabel public key label
	 * @return For RSA,java.security.spec.RSAPublicKeySpec; for ECDSA java.security.spec.ECPublicKeySpec type is returned
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no public key with the given label is found.
	 */
	public KeySpec readPublicKeySpec(long aSessionID,String aLabel)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().readPublicKeySpec( aSessionID, aLabel);
	}


	/**
	 * readPublicKeySpec returns specification of public key that has the same CKA_ID with certificate having the given serial number.
	 * For now, supported algorithms are RSA and ECDSA
	 *
	 * @param aSessionID session handle
	 * @param aCertSerialNo certificate serial number
	 * @return For RSA,java.security.spec.RSAPublicKeySpec; for ECDSA java.security.spec.ECPublicKeySpec type is returned
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public KeySpec readPublicKeySpec(long aSessionID,byte[] aCertSerialNo)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().readPublicKeySpec( aSessionID, aCertSerialNo);
	}


	/**
	 * updatePrivateData finds the data (type CKO_DATA) with the given label and updates it with the given value.
	 * Must be logged in before calling this method in order to reach private area.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label of data to be updated
	 * @param aValue new value for data
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no data with given label in private area is found.
	 */
	public void updatePrivateData(long aSessionID,String aLabel,byte[] aValue)
			throws PKCS11Exception,SmartCardException
	{
		mCardType.getCardTemplate().getPKCS11Ops().updatePrivateData(aSessionID, aLabel, aValue);
	}


	/**
	 * updatePublicData finds the data (type CKO_DATA) with the given label and updates it with the given value.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label of data to be updated
	 * @param aValue new value for data
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no data with given label is found.
	 */
	public void updatePublicData(long aSessionID,String aLabel,byte[] aValue)
			throws PKCS11Exception,SmartCardException
	{
		mCardType.getCardTemplate().getPKCS11Ops().updatePublicData(aSessionID, aLabel, aValue);
	}


	/**
	 * deletePrivateObject finds the object/s (type can be anything) with the given label and deletes it/them.
	 * Must be logged in before calling this method in order to reach private area.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label of the object that will be deleted
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no object with given label in private area is found.
	 */
	public void deletePrivateObject(long aSessionID,String aLabel)
			throws PKCS11Exception,SmartCardException
	{
		mCardType.getCardTemplate().getPKCS11Ops().deletePrivateObject(aSessionID, aLabel);
	}

	public void deleteObject(long sessionId, long objectHandle) throws PKCS11Exception
	{
		mCardType.getCardTemplate().getPKCS11Ops().deleteObject(sessionId, objectHandle);
	}

	/**
	 * deletePublicObject finds the object/s (type can be anything) with the given label and deletes it/them.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label of the object that will be deleted
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no object with given label is found.
	 */
	public void deletePublicObject(long aSessionID,String aLabel)
			throws PKCS11Exception,SmartCardException
	{
		mCardType.getCardTemplate().getPKCS11Ops().deletePublicObject(aSessionID, aLabel);
	}


	/**
	 * deletePrivateData finds the object/s (type CKO_DATA) with the given label and deletes it/them.
	 * Must be logged in before calling this method in order to reach private area.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label of the data that will be deleted
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no data with given label is found.
	 */
	public void deletePrivateData(long aSessionID,String aLabel)
			throws PKCS11Exception,SmartCardException
	{
		mCardType.getCardTemplate().getPKCS11Ops().deletePrivateData(aSessionID, aLabel);
	}


	/**
	 * deletePublicData finds the object/s (type CKO_DATA) with the given label and deletes it/them.
	 *
	 * @param aSessionID session handle
	 * @param aLabel label of the data that will be deleted
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no data with given label is found.
	 */
	public void deletePublicData(long aSessionID,String aLabel)
			throws PKCS11Exception,SmartCardException
	{
		mCardType.getCardTemplate().getPKCS11Ops().deletePublicData( aSessionID, aLabel);
	}

	/**
	 * getRandomData generates random data
	 *
	 * @param aSessionID session handle
	 * @param aDataLength length of random data to be generated
	 * @return random data
	 * @throws PKCS11Exception
	 */
	public byte[] getRandomData(long aSessionID,int aDataLength)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getRandomData(aSessionID, aDataLength);
	}

	/**
	 * getTokenSerialNo returns token's serial number.
	 * @param aSlotID id of slot that token is present
	 * @return token's serial number
	 * @throws PKCS11Exception
	 */
	public byte[] getTokenSerialNo(long aSlotID)
			throws PKCS11Exception
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
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no private key with given label is found.
	 */
	@Deprecated
	public byte[] signData(long aSessionID,String aKeyLabel,byte[] aToBeSigned,long aMechanism)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();

		CK_MECHANISM mech = new CK_MECHANISM(0L);
		mech.mechanism = aMechanism;

		byte[] signed = mCardType.getCardTemplate().getPKCS11Ops().signData(aSessionID, aKeyLabel, aToBeSigned, mech);
		signed = makeTLV(mech,signed);
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
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no private key with given label is found.
	 */
	public byte[] signData(long aSessionID,String aKeyLabel,byte[] aToBeSigned,CK_MECHANISM aMechanism)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();

		byte[] signed = mCardType.getCardTemplate().getPKCS11Ops().signData(aSessionID, aKeyLabel, aToBeSigned, aMechanism);

		signed = makeTLV(aMechanism,signed);

		return signed;
	}


	public byte[] signAndRecoverData(long aSessionID,String aKeyLabel,byte[] aToBeSigned,CK_MECHANISM aMechanism)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();

		byte[] signed = mCardType.getCardTemplate().getPKCS11Ops().signAndRecoverData(aSessionID, aKeyLabel, aToBeSigned, aMechanism);
		signed = makeTLV(aMechanism,signed);
		return signed;
	}

	public byte[] verifyAndRecoverData(long aSessionID,String aKeyLabel,byte[] aSignedData,CK_MECHANISM aMechanism)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();

		byte[] signed = mCardType.getCardTemplate().getPKCS11Ops().verifyAndRecoverData(aSessionID, aKeyLabel, aSignedData, aMechanism);
		signed = makeTLV(aMechanism,signed);
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
	 * @throws PKCS11Exception  If signature is invalid,PKCS11Exception with CKR_SIGNATURE_INVALID value is thrown. Other types of PKCS11Exception
	 * can be thrown because of other reasons.
	 * @throws SmartCardException If no public key with given label is found.
	 */
	public void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aSignature, long aMechanism) throws PKCS11Exception, SmartCardException {
		CK_MECHANISM mechanism = new CK_MECHANISM(0L);
		mechanism.mechanism = aMechanism;

		verifyData(aSessionID, aKeyLabel, aData, aSignature, mechanism);
	}

	@Override
	public void verifyData(long aSessionID, long aKeyID, byte[] aData, byte[] aSignature, long aMechanism) throws PKCS11Exception, SmartCardException {
		CK_MECHANISM mechanism = new CK_MECHANISM(0L);
		mechanism.mechanism = aMechanism;

		verifyData(aSessionID, aKeyID, aData, aSignature, mechanism);
	}

	/**
	 * verifyData finds the public key and verifies the given signature.
	 *
	 * @param aSessionID session handle
	 * @param aKeyLabel label of the public key
	 * @param aData Data that is signed
	 * @param aSignature Signature
	 * @param aMechanism signature mechanism. Values can be obtained from PKCS11Constants with values starting CKM_
	 * @throws PKCS11Exception  If signature is invalid,PKCS11Exception with CKR_SIGNATURE_INVALID value is thrown. Other types of PKCS11Exception
	 * can be thrown because of other reasons.
	 * @throws SmartCardException If no public key with given label is found.
	 */
	@Override
	public void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aSignature, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
		//decodes TLV if it ECDSA.
		byte[] decodedSignature = decodeSignatureIfItIsEC(aMechanism, aSignature);

		mCardType.getCardTemplate().getPKCS11Ops().verifyData(aSessionID, aKeyLabel, aData, decodedSignature, aMechanism);
	}

	@Override
	public void verifyData(long aSessionID, long aKeyID, byte[] aData, byte[] aSignature, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
		//decodes TLV if it ECDSA.
		byte[] decodedSignature = decodeSignatureIfItIsEC(aMechanism, aSignature);

		mCardType.getCardTemplate().getPKCS11Ops().verifyData(aSessionID, aKeyID, aData, decodedSignature, aMechanism);
	}


	private byte[] decodeSignatureIfItIsEC(CK_MECHANISM mech, byte[] aSignature) {
		if (PKCS11AlgorithmUtil.isECMechanism(mech.mechanism)) {
			try {
				if (ECSignatureTLVUtil.isSignatureInTLVFormat(aSignature)) {
					return ECSignatureTLVUtil.removeTLVFromSignature(aSignature);
				}
			} catch (Exception e) {
				logger.error(e.toString(), e);
				return aSignature;
			}
		}

		return aSignature;
	}

	/**
	 * encryptData finds the public key with the given label and encrypts the given data.
	 *
	 * @param aSessionID session handle
	 * @param aKeyLabel label of the public key
	 * @param aData data to be encrypted
	 * @param aMechanism encryption mechanism. Values can be obtained from PKCS11Constants with values starting CKM_
	 * @return encrypted data
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no public key with given label is found.
	 */
	@Deprecated
	public byte[] encryptData(long aSessionID,String aKeyLabel,byte[] aData,long aMechanism)
			throws PKCS11Exception,SmartCardException
	{
		CK_MECHANISM mech= new CK_MECHANISM(0L);
		mech.mechanism = aMechanism;

		return mCardType.getCardTemplate().getPKCS11Ops().encryptData( aSessionID, aKeyLabel, aData, mech);
	}


	/**
	 * encryptData finds the public key with the given label and encrypts the given data.
	 *
	 * @param aSessionID session handle
	 * @param aKeyLabel label of the public key
	 * @param aData data to be encrypted
	 * @param aMechanism encryption mechanism. Values can be obtained from PKCS11Constants with values starting CKM_
	 * @return encrypted data
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no public key with given label is found.
	 */
	public byte[] encryptData(long aSessionID,String aKeyLabel,byte[] aData,CK_MECHANISM aMechanism)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().encryptData(aSessionID, aKeyLabel, aData, aMechanism);
	}

	/**
	 * encryptData uses the public key with the given ID and encrypts the given data.
	 *
	 * @param aSessionID session handle
	 * @param aKeyID     ID of the public key
	 * @param aData      data to be encrypted
	 * @param aMechanism encryption mechanism. Values can be obtained from PKCS11Constants with values starting CKM_
	 * @return encrypted data
	 * @throws SmartCardException If no public key with given label is found.
	 */
	@Override
	public byte[] encryptData(long aSessionID, long aKeyID, byte[] aData, CK_MECHANISM aMechanism)
		throws PKCS11Exception, SmartCardException {
		return mCardType.getCardTemplate().getPKCS11Ops().encryptData(aSessionID, aKeyID, aData, aMechanism);
	}

	@Override
	public void encryptData(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, InputStream inputStream, OutputStream outputStream) throws PKCS11Exception, SmartCardException, IOException {
		checkLicense();
		mCardType.getCardTemplate().getPKCS11Ops().encryptData(aSessionID, aKeyID, aMechanism, inputStream, outputStream);
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
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no private key with given label is found.
	 */
	@Deprecated
	public byte[] decryptData(long aSessionID,String aKeyLabel,byte[] aData,long aMechanism)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();

		CK_MECHANISM mech= new CK_MECHANISM(0L);
		mech.mechanism = aMechanism;

		return mCardType.getCardTemplate().getPKCS11Ops().decryptData(aSessionID, aKeyLabel, aData, mech);
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
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no private key with given label is found.
	 */
	public byte[] decryptData(long aSessionID,String aKeyLabel,byte[] aData,CK_MECHANISM aMechanism)
			throws PKCS11Exception,SmartCardException
	{
		checkLicense();

		return mCardType.getCardTemplate().getPKCS11Ops().decryptData(aSessionID, aKeyLabel, aData, aMechanism);
	}

	/**
	 * decryptData uses the private key with given ID and decrypts data.
	 * Must be logged in before calling this method.
	 *
	 * @param aSessionID session handle
	 * @param aKeyID     ID of the private key
	 * @param aData      encrypted data
	 * @param aMechanism encryption mechanism. Values can be obtained from PKCS11Constants with values starting CKM_
	 * @return decrypted data
	 * @throws SmartCardException If no private key with given label is found.
	 */
	@Override
	public byte[] decryptData(long aSessionID, long aKeyID, byte[] aData, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {

		checkLicense();

		return mCardType.getCardTemplate().getPKCS11Ops().decryptData(aSessionID, aKeyID, aData, aMechanism);
	}

	@Override
	public void decryptData(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, InputStream inputStream, OutputStream outputStream) throws PKCS11Exception, SmartCardException, IOException {
		checkLicense();
		mCardType.getCardTemplate().getPKCS11Ops().decryptData(aSessionID, aKeyID, aMechanism, inputStream, outputStream);
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
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 * @throws IOException
	 */
	public long[] importCertificateAndKey(long aSessionID, String aCertLabel, String aKeyLabel, PrivateKey aPrivKey, X509Certificate aCert)
			throws PKCS11Exception,SmartCardException,IOException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().importCertificateAndKey( aSessionID, aCertLabel, aKeyLabel, aPrivKey, aCert);
	}

	/*public void importRSAKeyPair(long aSessionID,String aLabel, RSAPrivateCrtKey aPrivKey, byte[] aSubject,boolean aIsSign,boolean aIsEncrypt)
	throws PKCS11Exception,SmartCardException
	{
		mCardType.getCardTemplate().getPKCS11Ops().importRSAKeyPair( aSessionID, aLabel, aPrivKey, aSubject,aIsSign,aIsEncrypt);
	}*/


	/**
	 * importKeyPair imports the given key pair to the token.
	 * @deprecated Use importKeyPair(SessionID, KeyPairTemplate)
	 * @param aSessionID session handle.
	 * @param aLabel Label of the keys.
	 * @param aKeyPair Key pair that will be imported.For now, RSA and ECDSA algorithms are supported.
	 * For RSA, key types must be java.security.interfaces.RSAPublicKey and java.security.interfaces.RSAPrivateCrtKey;
	 * for ECDSA key types must be java.security.interfaces.ECPublicKey and java.security.interfaces.ECPrivateKey.
	 * @param aSubject Value for CKA_SUBJECT field of private key. It may be null.
	 * @param aIsSign True if keys are for signature, false otherwise.
	 * @param aIsEncrypt True if keys are for encryption, false otherwise.
	 * @throws PKCS11Exception
	 * @throws SmartCardException If given key types are not supported.
	 * @throws IOException
	 */
	@Deprecated
	public long[] importKeyPair(long aSessionID, String aLabel, KeyPair aKeyPair, byte[] aSubject, boolean aIsSign, boolean aIsEncrypt)
			throws PKCS11Exception,SmartCardException,IOException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().importKeyPair(aSessionID, aLabel, aKeyPair, aSubject, aIsSign, aIsEncrypt);
	}

	/**
	 * changePassword changes user's pin.
	 *
	 * @param aOldPass old pin
	 * @param aNewPass new pin
	 * @param aSessionID session handle
	 * @throws PKCS11Exception
	 */
	public void changePassword(String aOldPass,String aNewPass,long aSessionID)
			throws PKCS11Exception
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
	 * @throws PKCS11Exception
	 */
	public void formatToken(String aSOpin, String aNewPIN, String aLabel, int slotID)
			throws PKCS11Exception
	{
		mCardType.getCardTemplate().getPKCS11Ops().formatToken(aSOpin, aNewPIN, aLabel, slotID);
	}

	/**
	 * setSOPin changes SO's(Security Officer) pin
	 *
	 * @param aSOPin SO's old pin
	 * @param aNewSOPin SO's new pin
	 * @param aSessionHandle session handle
	 * @throws PKCS11Exception
	 */
	public void setSOPin (byte[] aSOPin,byte[] aNewSOPin, long aSessionHandle)
			throws PKCS11Exception
	{
		mCardType.getCardTemplate().getPKCS11Ops().setSOPin(aSOPin, aNewSOPin, aSessionHandle);
	}


	/**
	 * changeUserPin changes user's pin.
	 *
	 * @param aSOPin SO's pin
	 * @param aUserPin new pin for user
	 * @param aSessionHandle session handle
	 * @throws PKCS11Exception
	 */
	public void changeUserPin (byte[] aSOPin, byte[] aUserPin, long aSessionHandle)
			throws PKCS11Exception
	{
		mCardType.getCardTemplate().getPKCS11Ops().changeUserPin(aSOPin, aUserPin, aSessionHandle);
	}



	public boolean setContainer (byte[] aContainerLabel, long aSessionHandle)
	{
		return mCardType.getCardTemplate().getPKCS11Ops().setContainer(aContainerLabel, aSessionHandle);
	}


	public boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName,X509Certificate aPbCertificate, int aSignOrEnc)
	{
		return mCardType.getCardTemplate().getPKCS11Ops().importCertificateAndKeyWithCSP(aAnahtarCifti,aAnahtarLen, aScfname,  aContextName,aPbCertificate,  aSignOrEnc);
	}

	public boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName,byte[] aPbCertificate, int aSignOrEnc)
	{
		return mCardType.getCardTemplate().getPKCS11Ops().importCertificateAndKeyWithCSP(aAnahtarCifti,aAnahtarLen, aScfname,  aContextName,aPbCertificate,  aSignOrEnc);
	}


	/**
	 * createSecretKey creates secret key
	 *
	 * @param aSessionID session handle
	 * @param aKey Key type can be AESSecretKey,DES3SecretKey or HMACSecretKey.
	 * @throws PKCS11Exception
	 */
	public long createSecretKey(long aSessionID, SecretKey aKey)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().createSecretKey(aSessionID, aKey);
	}

	/**
	 * importSecretKey imports the given secret key to the token
	 * @param aSessionID session handle
	 * @param aKey Key type can be AESSecretKey,DES3SecretKey or HMACSecretKey.
	 * @throws PKCS11Exception
	 */
	public long importSecretKey(long aSessionID, SecretKey aKey)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().importSecretKey(aSessionID, aKey);
	}

	public long importSecretKey(long aSessionID, SecretKeyTemplate aKeyTemplate)
            throws PKCS11Exception, SmartCardException
    {
		return mCardType.getCardTemplate().getPKCS11Ops().importSecretKey(aSessionID, aKeyTemplate);
    }

    private void deleteWrapperKeys(long sid, List<String> deleteLabelList) throws PKCS11Exception, SmartCardException
    {
		deletePrivateObject(sid, deleteLabelList.get(0));
        //deletePublicObject(sid, deleteLabelList.get(0));

        deletePrivateObject(sid, deleteLabelList.get(1));
    }



    /**
	 * Find and return card type from ATR
	 * @param aATRHex ATR value as hexadecimal
	 * @param aApp
	 * @return
	 */
	protected static CardType findCardType(String aATRHex, Application aApp)
	{
		return CardType.getCardTypeFromATR(aATRHex, aApp);
	}

	public long[] findObjects(long aSessionID,CK_ATTRIBUTE[] aTemplate)
			throws PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().objeAra(aSessionID, aTemplate);
	}

	/**
	 * use find Objects
	 * */
	@Deprecated
	public long[] objeAra(long aSessionID,CK_ATTRIBUTE[] aTemplate)
			throws PKCS11Exception
	{
		return findObjects(aSessionID, aTemplate);
	}
	public void getAttributeValue(long aSessionID,long aObjectID,CK_ATTRIBUTE[] aTemplate)
			throws PKCS11Exception
	{
		mCardType.getCardTemplate().getPKCS11Ops().getAttributeValue(aSessionID, aObjectID, aTemplate);
	}

	/**
	 * changeLabel changes all the objects' labels that have aOldLabel with the aNewLabel
	 * @param aSessionID
	 * @param aOldLabel Old label
	 * @param aNewLabel New label
	 * @throws PKCS11Exception
	 * @throws SmartCardException If no object is found with the aOldLabel.
	 */
	public void changeLabel(long aSessionID,String aOldLabel,String aNewLabel)
			throws PKCS11Exception,SmartCardException
	{
		mCardType.getCardTemplate().getPKCS11Ops().changeLabel(aSessionID, aOldLabel, aNewLabel);
	}

	public static void checkLicense()
	{
		try
		{
			boolean isTest = LV.getInstance().isTL(Urunler.AKILLIKART);
			if(isTest)
			{
				logger.debug("Test lisansı, akıllı kart işlemlerinde gecikme yaşanacak.");
				Thread.sleep(2000);
			}
		}
		catch(LE ex)
		{
			throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage(), ex);
		}
		catch (InterruptedException e)
		{
			throw new ESYARuntimeException("Lisans kontrolu sirasinda interrup alindi.", e);
		}

	}

	public byte [] getModulusOfKey(long aSessionID, long aObjID) throws SmartCardException, PKCS11Exception
	{
		return mCardType.getCardTemplate().getPKCS11Ops().getModulusOfKey(aSessionID, aObjID);
	}

	/**
	 * Wrap keys in smartcard with specified wrapping key in smartcard.
	 * @param sessionID session id (login requires)
	 * @param mechanism describe how to wrap key see CKM_RSA_PKCS, CKM_AES_CBC_PAD
	 * @param wrapperKeyLabel wrapper key label
	 * @param labelOfKeyToWrap key to wrap
	 * @return
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public byte[] wrapKey(long sessionID, CK_MECHANISM mechanism, String wrapperKeyLabel, String labelOfKeyToWrap)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().wrapKey(sessionID, mechanism, wrapperKeyLabel, labelOfKeyToWrap);
	}

	public byte[] wrapKey(long sessionID, CK_MECHANISM mechanism, long wrapperKeyID, long keyID)
		throws PKCS11Exception, SmartCardException {
		return ((PKCS11Ops) mCardType.getCardTemplate().getPKCS11Ops()).wrapKey(sessionID, mechanism, wrapperKeyID, keyID);
	}

	public WrappedObjectsWithAttributes wrapObjectsWithAttributes(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, long[] objectIDs)
			throws PKCS11Exception, SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().wrapObjectsWithAttributes(aSessionID, aMechanism, wrapperKeyLabel, objectIDs);
	}

	/**
	 * Wrap keys in smartcard with specified wrapping key in smartcard.
	 * @param sessionID session id (login requires)
	 * @param mechanism describe how to wrap key see CKM_RSA_PKCS, CKM_AES_CBC_PAD
	 * @param wrapperKeyTemplate wrapper key
	 * @param wrappingKeyTemplate key to wrap
	 * @return
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public byte[] wrapKey(long sessionID, CK_MECHANISM mechanism, KeyTemplate wrapperKeyTemplate, KeyTemplate wrappingKeyTemplate)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().wrapKey(sessionID, mechanism, wrapperKeyTemplate, wrappingKeyTemplate);
	}

	/**
	 * Unrap(wrappedKey) keys with unwrapper key(unwrappingKeyLabel) in smartcard with specified mechanism(mechanism), creating key with KeyTemplate(unwrappedKeyTemplate)
	 *
	 * @param sessionID  session id (login requires)
	 * @param mechanism describe how to unwrap key see CKM_RSA_PKCS, CKM_AES_CBC_PAD
	 * @param unwrapperKeyLabel key to unwrap key into smartcard. unwrapping key must be in smartcard, and unwrap flag must be set.
	 * @param wrappedKey wrapped key
	 * @param unwrappedKeyTemplate KeyTemplate to create new unwrapped key in smartcard
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public long unwrapKey(long sessionID, CK_MECHANISM mechanism, String unwrapperKeyLabel, byte[] wrappedKey, KeyTemplate unwrappedKeyTemplate)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().unwrapKey(sessionID, mechanism, unwrapperKeyLabel, wrappedKey, unwrappedKeyTemplate);
	}

	public long unwrapKey(long sid, CK_MECHANISM mechanism, long wrapperKeyID, byte[] wrappedKey, KeyTemplate unwrappedKeyTemplate)
			throws PKCS11Exception, SmartCardException
	{
		return ((PKCS11Ops) mCardType.getCardTemplate().getPKCS11Ops()).unwrapKey(sid, mechanism, wrapperKeyID, wrappedKey, unwrappedKeyTemplate);
	}

	/**
	 * Unrap(wrappedKey) keys with unwrapper key(unwrappingKeyLabel) in smartcard with specified mechanism(mechanism), creating key with KeyTemplate(unwrappedKeyTemplate)
	 *
	 * @param sessionID  session id (login requires)
	 * @param mechanism describe how to unwrap key see CKM_RSA_PKCS, CKM_AES_CBC_PAD
	 * @param unwrapperKeyTemplate key to unwrap key into smartcard. unwrapping key must be in smartcard, and unwrap flag must be set.
	 * @param wrappedKey wrapped key
	 * @param unwrappedKeyTemplate KeyTemplate to create new unwrapped key in smartcard
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public long unwrapKey(long sessionID, CK_MECHANISM mechanism, KeyTemplate unwrapperKeyTemplate, byte[] wrappedKey, KeyTemplate unwrappedKeyTemplate)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().unwrapKey(sessionID, mechanism, unwrapperKeyTemplate, wrappedKey, unwrappedKeyTemplate);
	}

	/**
	 * Unrap(wrappedKey) keys with unwrapper key(unwrappingKeyLabel) in smartcard with specified mechanism(mechanism), creating key with KeyTemplate(unwrappedKeyTemplate)
	 * @param sessionID  session id (login requires)
	 * @param mechanism describe how to unwrap key see CKM_RSA_PKCS, CKM_AES_CBC_PAD
	 * @param certSerialNumber Certificate Serial Number of Private key to unwrap key into smartcard. unwrapping key must be in smartcard, and unwrap flag must be set.
	 * @param wrappedKey wrapped key
	 * @param unwrappedKeyTemplate KeyTemplate to create new unwrapped key in smartcard
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public long unwrapKey(long sessionID, CK_MECHANISM mechanism, byte[] certSerialNumber, byte[] wrappedKey, KeyTemplate unwrappedKeyTemplate)
			throws PKCS11Exception,SmartCardException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().unwrapKey(sessionID, mechanism, certSerialNumber, wrappedKey, unwrappedKeyTemplate);
	}

	public UnwrapObjectsResults unwrapObjectsWithAttributes(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, byte[] wrappedBytes)
			throws PKCS11Exception, ESYAException
	{
		return mCardType.getCardTemplate().getPKCS11Ops().unwrapObjectsWithAttributes(aSessionID, aMechanism, wrapperKeyLabel, wrappedBytes);
	}

	/**
	 * Return card type
	 * @return
	 */
	public CardType getCardType()
	{
		return mCardType;
	}

	/**
	 * Create KeyPair specified with KeyPairTemplate
	 * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate
	 * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate
	 * @param sessionID  session id login requires
	 * @param template   template to create KeyPair
	 */
	public long[] importKeyPair(long sessionID, KeyPairTemplate template) throws PKCS11Exception,SmartCardException {
		return mCardType.getCardTemplate().getPKCS11Ops().importKeyPair(sessionID, template);
	}

	/**
	 * Create SecretKey specified with SecretKeyTemplate
	 * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate
	 * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.DES3KeyTemplate
	 * @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.HMACKeyTemplate
	 * @param sessionID session id login requires
	 * @param template  template to create SecretKey
	 */
	public long createSecretKey(long sessionID, SecretKeyTemplate template) throws PKCS11Exception, SmartCardException {
		return mCardType.getCardTemplate().getPKCS11Ops().createSecretKey(sessionID, template);
	}

	/**
	 * Delete certificate with given key label
	 * @param aSessionID session id
	 * @param aKeyLabel label of the certificate that will be deleted
	 * @return number of certificates deleted
	 * @throws PKCS11Exception
	 */
	public int deleteCertificate(long aSessionID,String aKeyLabel)
			throws PKCS11Exception{
		return mCardType.getCardTemplate().getPKCS11Ops().deleteCertificate(aSessionID, aKeyLabel);
	}

	public boolean isSupportsWrapUnwrap(long sessionId){
		boolean isSupport=true;
		PKCS11Ops pkcs11Ops = (PKCS11Ops) getCardType().getCardTemplate().getPKCS11Ops();
		try {
			pkcs11Ops.getmPKCS11().C_WrapKey(sessionId,new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS),-1,-1);
		} catch (PKCS11Exception e) {
			logger.warn("Warning in SmartCard", e);
			if(e.getErrorCode() == PKCS11Constants.CKR_FUNCTION_NOT_SUPPORTED){
				isSupport = false;
			}
		}

		if(isSupport){
			try {
				pkcs11Ops.getmPKCS11().C_UnwrapKey(sessionId,new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS),-1,"test".getBytes(),null);
			} catch (PKCS11Exception e) {
				logger.warn("Warning in SmartCard", e);
				if(e.getErrorCode() == PKCS11Constants.CKR_FUNCTION_NOT_SUPPORTED){
					isSupport = false;
				}
			}
		}
		return isSupport;
    }


	public byte[] unwrapAndOP(
		long sessionID,
		CK_MECHANISM unwrapMechanism,
		long unwrapperKeyID,
		byte[] wrappedKey,
		CK_ATTRIBUTE[] unwrapTemplate,
		DirakLibOps.CryptoOperation operation,
		CK_MECHANISM operationMechanism,
		byte[] operationData
	) throws SmartCardException, PKCS11Exception {
		return ((PKCS11Ops) mCardType.getCardTemplate().getPKCS11Ops()).unwrapAndOP(
			sessionID,
			unwrapMechanism,
			unwrapperKeyID,
			wrappedKey,
			unwrapTemplate,
			operation,
			operationMechanism,
			operationData
		);
	}

    public PKCS11 getPKCS11() {
        return ((PKCS11Ops) getCardType().getCardTemplate().getPKCS11Ops()).getmPKCS11();
    }

	public PKCS11Ops getPKCS11Ops() {
		return ((PKCS11Ops) getCardType().getCardTemplate().getPKCS11Ops());
	}

	public long deriveKey(long sessionId, CK_MECHANISM derive_mechanism, long privateKeyHandle, KeyTemplate unwrappedKeyTemplate) throws PKCS11Exception {
		return mCardType.getCardTemplate().getPKCS11Ops().deriveKey(sessionId, derive_mechanism, privateKeyHandle, unwrappedKeyTemplate);
	}

	public byte[] signDataWithDIRAK(long sessionID, long privKeyID, CK_MECHANISM_STRUCTURE mechanism, byte[] aToBeSigned) throws PKCS11Exception, SmartCardException {

		checkLicense();

		if(getCardType() != CardType.DIRAKHSM) {
            throw new SmartCardException("Only Dirak HSM is supported with CK_MECHANISM_STRUCTURE");
        }

        // sign init
        int retVal = DirakLibOps.DirakLibJNAConnector.dirakP11lib.C_SignInit(
                sessionID,
                mechanism,
                privKeyID
        );
		// if result is not OK, throw exception
		if (retVal != PKCS11Constants.CKR_OK) {
			throw new PKCS11Exception(retVal);
		}

		// sign & return
		return getPKCS11().C_Sign(sessionID, aToBeSigned);
	}

	public void verifyDataWithDIRAK(long aSessionID, long aKeyID, byte[] aData, byte[] aSignature, CK_MECHANISM_STRUCTURE aMechanism) throws PKCS11Exception, SmartCardException {

		if (getCardType() != CardType.DIRAKHSM) {
			throw new SmartCardException("Only Dirak HSM is supported with CK_MECHANISM_STRUCTURE");
		}

		//decodes TLV if it ECDSA.
		byte[] decodedSignature = decodeSignatureIfItIsEC(new CK_MECHANISM(aMechanism.mechanism.longValue()), aSignature);

		int retVal = DirakLibOps.DirakLibJNAConnector.dirakP11lib.C_VerifyInit(
				aSessionID,
				aMechanism,
				aKeyID
		);
		if (retVal != PKCS11Constants.CKR_OK) {
			throw new PKCS11Exception(retVal);
		}

		getPKCS11().C_Verify(aSessionID, aData, decodedSignature);
	}
}
