package tr.gov.tubitak.uekae.esya.api.smartcard.apdu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECSignatureTLVUtil;
import tubitak.akis.cif.akisExceptions.*;
import tubitak.akis.cif.commands.AbstractAkisCommands;
import tubitak.akis.cif.commands.CIFFactory;
import tubitak.akis.cif.dataStructures.AkisKey;
import tubitak.akis.cif.dataStructures.Algorithm;
import tubitak.akis.cif.dataStructures.DF_EF;
import tubitak.akis.cif.dataStructures.Tags;
import tubitak.akis.cif.dataStructures.Version;
import tubitak.akis.cif.functions.CommandTransmitterPCSC;
import tubitak.akis.cif.functions.ICommandTransmitter;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.smartcard.bundle.SmartCardI18n;
import tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder.KeyFinder;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.BaseSmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.LoginException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.ISignatureScheme;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.RSAPSSSignature;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.SignatureSchemeFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.APDUSigner;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CardTerminals.State;
import javax.smartcardio.TerminalFactory;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class APDUSmartCard implements BaseSmartCard
{
	public static int CERT_FID_START = 0x2F10;
	public static int CERT_FID_END = 0x2F20;

	protected Hashtable<String, Integer> serialCertIndex;
	protected Hashtable<String, Integer> serialKeyID;
	protected List<byte []> signingCerts;
	protected List<byte []> encryptionCerts;


	protected TerminalHandler terminalHandler;
	protected AbstractAkisCommands commands;
	protected ICommandTransmitter pcsc;
	protected Card card;
	protected boolean disableSecureMessaging=false;

	String commandLoggingPath;

	public void setCommandLoggingPath(String commandLoggingPath) {
		this.commandLoggingPath = commandLoggingPath;
	}

	/**
	 * Set disableSecureMessaging flag
	 * @param disableSecureMessaging boolean
	 */
	public void setDisableSecureMessaging(boolean disableSecureMessaging) {
		this.disableSecureMessaging = disableSecureMessaging;
	}

	private static Logger LOGGER = LoggerFactory.getLogger(APDUSmartCard.class);

	private String PIN;
	/**
	 * Checks whether smart card support APDU or not.
	 * @return True if smart card support APDU, false otherwise
	 */
	public static boolean isSupported(String aTerminalName)
	{
		CardTerminal terminal = TerminalFactory.getDefault().terminals().getTerminal(aTerminalName);
		return isSupported(terminal);
	}
	/**
	 * Checks whether smart card support APDU or not.
	 * @return True if smart card support APDU, false otherwise
	 */
	public static boolean isSupported(String aTerminalName, TerminalHandler aTerminalHandler)
	{
		try
		{
			List<CardTerminal> terminals = aTerminalHandler.listCardTerminals(State.CARD_PRESENT);
			for (CardTerminal terminal : terminals)
			{
				if(terminal.getName().equals(aTerminalName))
					return isSupported(terminal, aTerminalHandler);
			}
		}
		catch (CardException e)
		{
			LOGGER.warn("Warning in APDUSmartCard", e);
			return false;
		}
		CardTerminal terminal = TerminalFactory.getDefault().terminals().getTerminal(aTerminalName);
		return isSupported(terminal);
	}

	public static boolean isSupported(CardTerminal aTerminal, TerminalHandler aTerminalHandler)
	{
		try
		{
			ICommandTransmitter transmitter = aTerminalHandler.getTransmitter(aTerminal);
			return isSupported(transmitter);
		}
		catch (CardException ex)
		{
			LOGGER.error("isSoppurted funtion error",ex);
			return false;
		}
		catch (Exception e)
		{
			LOGGER.error("isSoppurted funtion error",e);
			return false;
		}
	}

	/**
	 * Checks whether smart card support APDU or not.
	 * @return True if smart card support APDU, false otherwise
	 */
	public static boolean isSupported(CardTerminal aTerminal)
	{
		try
		{
			ICommandTransmitter pcsc = new CommandTransmitterPCSC(aTerminal, false);
			return isSupported(pcsc);
		}
		catch (NoClassDefFoundError ex)
		{
			LOGGER.error("akiscif.jar bulunamadı",ex);
			return false;
		}
		catch (Exception e)
		{
			LOGGER.error("isSoppurted funtion error",e);
			return false;
		}
	}

	/**
	 * Checks whether smart card support APDU or not.
	 * @return True if smart card support APDU, false otherwise
	 */
	protected static boolean isSupported(ICommandTransmitter transmitter)
	{
		try
		{
			AbstractAkisCommands ac = CIFFactory.getAkisCIFInstance(transmitter);
			if(ac == null)
				return false;
			else
				return true;
		}
		catch(Exception ex)
		{
			LOGGER.error("isSupported funtion error",ex);
			return false;
		}
		catch (NoClassDefFoundError ex)
		{
			LOGGER.error("akiscif.jar bulunamadı",ex);
			return false;
		}
	}
	/**
	 * Create APDUSmartCard with TerminalHandler
	 * @param aTerminalHandler
	 */
	public APDUSmartCard(TerminalHandler aTerminalHandler)
	{
		terminalHandler = aTerminalHandler;
		initialize();
	}
	/**
	 * Default constructor
	 */
	public APDUSmartCard()
	{
		terminalHandler = new PCTerminalHandler();
		initialize();
	}
	protected void initialize()
	{
		//serialCertIndex = new Hashtable<String, Integer>();
		serialKeyID = new Hashtable<String, Integer>();
	}

	/**
	 * List slots
	 * @return
	 * @throws CardException
	 */
	public long [] getSlotList() throws CardException
	{
		int count = listCardTerminals(CardTerminals.State.ALL).size();
		long [] slots = new long[count];
		for(int i=0; i < count; i++)
			slots[i] = i+1;
		return slots;
	}

	/**
	 * List card terminals
	 * @param aState
	 * @return list card terminal. If there is no card terminal returns empty list
	 * @throws CardException
	 */
	private List<CardTerminal> listCardTerminals(State aState) throws CardException
	{
		List<CardTerminal> tList = new ArrayList<CardTerminal>();
		try
		{
			tList = terminalHandler.listCardTerminals(aState);
		}
		catch(CardException ex)
		{

			if(!ex.getCause().getMessage().contains("SCARD_E_NO_READERS_AVAILABLE"))
				throw ex;
		}
		return tList;
	}

	/**
	 * List terminals
	 * @return
	 * @throws CardException
	 */
	public CardTerminal [] getTerminalList() throws CardException
	{
		List<CardTerminal> cts = listCardTerminals(CardTerminals.State.ALL);
		return cts.toArray(new CardTerminal[0]);
	}

	/**
	 * openSession opens a session between the application and the token present in the given slot.
	 *
	 * @param iCommandTransmitter given command transmitter
	 * @throws SmartCardException
	 */
	public void openSession(ICommandTransmitter iCommandTransmitter) throws SmartCardException {
		pcsc = iCommandTransmitter;
		openSession();
	}

	/**
	 * openSession opens a session between the application and the token present in the given slot.
	 *
	 * @param aTerminal terminal of the token
	 * @throws SmartCardException
	 */
	public void openSession(CardTerminal aTerminal) throws SmartCardException
	{
		try {
			pcsc = terminalHandler.getTransmitter(aTerminal);
		} catch (CardException e) {
			throw new SmartCardException(e);
		}
		openSession();
	}

	private void openSession() throws SmartCardException {
		try
		{
			commands = CIFFactory.getAkisCIFInstance(pcsc);
			if(commandLoggingPath!=null)
				commands.activateCommandLogging(commandLoggingPath);

			LOGGER.debug("Session opened. Card Version: " + commands.getVersion());

			//RMZ
			// Burası android altında akis 1.2 ile testlerde sorun olduğu için
			// secure messaging olmadan işlem yapabilmek için yapıldı.
			if(!disableSecureMessaging){
				if(!(commands.getVersion() == Version.V10_UEKAE || isVersion20(commands.getVersion())))
					commands.activateSecureMsging();
			}

			commands.selectMF();
			commands.selectDFByName("PKCS-15".getBytes("ASCII"));
		}
		catch (Exception e)
		{
			throw new SmartCardException("Can not open session.", e);
		}
	}

	/**
	 * Checks whether session is active or not.
	 * @return True if active,false otherwise
	 */
	public boolean isSessionActive()
	{
		try
		{
			commands.getSerial();
			return true;
		}
		catch (AkisException e)
		{
			LOGGER.warn("Warning in APDUSmartCard", e);
			return false;
		}
	}
	/**
	 * Return X509 certificates
	 * @return
	 * @throws SmartCardException
	 */
	protected List<X509Certificate> readCertificates() throws SmartCardException
	{
		serialCertIndex = new Hashtable<String, Integer>();

		CertificateFactory cf = null;
		List<DF_EF>  files = new ArrayList<DF_EF>();
		try
		{
			cf = CertificateFactory.getInstance("X.509");
			if(isVersion20(commands.getVersion()))
			{
				byte [] fids =  new byte [] {0x2f, 0x10};
				for(int i=0; i < 10; i++)
				{
					DF_EF file = new DF_EF();
					file.FID = new byte[2];
					file.FID[0] = fids[0]; file.FID[1] = fids[1];
					files.add(file);
					fids[1]++;
				}

			}
			else
				files = commands.dir();
		}
		catch (CertificateException e)
		{
			throw new SmartCardException("Can not create CertificateFactory", e);
		}
		catch (Exception e)
		{
			throw new SmartCardException(e);
		}

		List<X509Certificate> certs = new ArrayList<X509Certificate>();

		int index = 1;
		for (DF_EF file : files)
		{
			int fid = (file.FID[0] << 8)  + file.FID[1];
			if(fid >= CERT_FID_START && fid <= CERT_FID_END)
			{
				try
				{
					byte [] certBytes = commands.readFileBySelectingUnderActiveDF(file.FID);
					if(certBytes == null)
						throw new AkisSWException(CardErrorCodes.DOSYA_YOK_HATASI);
					X509Certificate cert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(certBytes));
					serialCertIndex.put(StringUtil.toString(cert.getSerialNumber().toByteArray()), index);
					index++;
					certs.add(cert);
				}
				catch (CertificateException e)
				{
					LOGGER.error("Certificate can not decoded.", e);
				}
				catch (AkisSWException e)
				{
					if(e.getErrorCode() != CardErrorCodes.DOSYA_YOK_HATASI)
					{
						throw new SmartCardException(e);
					}
				}
				catch (AkisException e)
				{
					throw new SmartCardException(e);
				}
			}
		}
		return certs;
	}
	/**
	 * returns signing certificates.
	 * @return list of signing certificates. If no signing certificate is found,returns empty list.
	 * @throws SmartCardException
	 */
	public List<byte []> getSignatureCertificates()
			throws SmartCardException
	{
		List<X509Certificate> certs = readCertificates();
		List<byte [] > signatureCerts = new ArrayList<byte[]>();
		for (X509Certificate cert : certs)
		{
			if(cert.getKeyUsage()[0]==true)
			{
				try
				{
					signatureCerts.add(cert.getEncoded());
				}
				catch (CertificateEncodingException e)
				{
					LOGGER.error("Certificate can not decoded", e);
				}
			}
		}

		return signatureCerts;
	}

	private void signInit(){
		if(serialCertIndex == null){
			try {
				readCertificates();
			} catch (SmartCardException e) {
				LOGGER.error("Error in APDUSmartCard", e);
			}
		}
	}
	/**
	 * returns signnature after signing data.
	 * @return signature byte[]
	 * @throws SmartCardException
	 */
	public byte [] sign(byte []aData, X509Certificate aCertificate, String aSigningAlg)
			throws SmartCardException, CardException
	{
		SmartCard.checkLicense();
		signInit();
		return sign(aData, aCertificate, aSigningAlg, null);
	}
	/**
	 * returns signnature after signing data.
	 * @return signature byte[]
	 * @throws SmartCardException
	 */
	//Hesitate to change
	public byte[] sign(byte[] aData, X509Certificate aCertificate, String aSigningAlg, AlgorithmParameterSpec spec)
			throws CardException, SmartCardException
	{
		SmartCard.checkLicense();
		signInit();

		byte[] dataToBeSigned;

		final X509Certificate cert = aCertificate;

		KeyFinder kf = new KeyFinder()
		{
			@Override
			public KeySpec getKeySpec() throws SmartCardException, PKCS11Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getKeyLength() throws SmartCardException, PKCS11Exception
			{
				return ((RSAPublicKey)cert.getPublicKey()).getModulus().toByteArray().length;
			}
		};

		long [] mechs = commands.getMechanisms();
		try
		{
			ISignatureScheme scheme = SignatureSchemeFactory.getSignatureScheme(true, aSigningAlg, spec, mechs, kf);
			dataToBeSigned = scheme.getSignParameters(aData).getSignatureInput();
		}
		catch (PKCS11Exception ex)
		{
			throw new SmartCardException(ex);
		}


		String serialStr = StringUtil.toString(aCertificate.getSerialNumber().toByteArray());
		Integer keyID = serialKeyID.get(serialStr);

		//If it is tested before sign directly
		if(keyID != null)
		{
			try
			{
				return signAndCheck(dataToBeSigned,(byte) keyID.intValue(), aData, serialStr, cert, aSigningAlg, spec);

			}
			catch(CardException ex)
			{
				throw ex;
			}
		}
		else
		{
			Integer certIndex = serialCertIndex.get(serialStr);
			if(certIndex == null)
				return tryAllKeys(dataToBeSigned, aData, serialStr, aCertificate, aSigningAlg, spec);

			//possible KeyID
			keyID = certIndex*2-1;
			try
			{
				//try most possible keyID
				return signAndCheck(dataToBeSigned, keyID.byteValue(), aData, serialStr, aCertificate, aSigningAlg, spec);
			}
			catch(Exception e)
			{
				LOGGER.debug("Warning in APDUSmartCard", e);
				return tryAllKeys(dataToBeSigned, aData, serialStr, aCertificate, aSigningAlg, spec);
			}


		}
	}

	private byte[] tryAllKeys(byte [] veri, byte [] aData, String serialStr, X509Certificate aCertificate,
							  String signingAlg, AlgorithmParameterSpec params) throws CardException
	{
		byte[] keyIDs;

		try
		{
			if(isVersion20(commands.getVersion()))
			{
				keyIDs = new byte[10];
				keyIDs[0] = (byte)1;
				keyIDs[1] = (byte)2;
				keyIDs[2] = (byte)3;
				keyIDs[3] = (byte)4;
				keyIDs[4] = (byte)5;
				keyIDs[5] = (byte)6;
				keyIDs[6] = (byte)7;
				keyIDs[7] = (byte)8;
				keyIDs[8] = (byte)9;
				keyIDs[9] = (byte)10;

			}
			else {
				AkisKey [] keys = commands.getKeyInfos();
				keyIDs = new byte[keys.length];
				for(int i=0 ; i < keys.length ; i++){
					keyIDs[i] = keys[i].getKeyID();
				}
			}
		}
		catch (Exception e) {
			throw new CardException(e);
		}

		for (byte keyID : keyIDs)
		{
			try
			{
				return signAndCheck(veri, keyID, aData, serialStr, aCertificate, signingAlg, params);
			}
			catch(Exception e)
			{
				LOGGER.warn("Warning in APDUSmartCard", e);
			}

		}
		//All possible keys are tried.
		throw new CardException(new AkisSWException(CardErrorCodes.ISTENILEN_ANAHTAR_YUKLU_DEGIL));
	}

	private byte [] signAndCheck(byte[] dataToBeSigned, byte keyID, byte [] aData, String serialStr,
								 X509Certificate cert, String aSigningAlg, AlgorithmParameterSpec aParams)  throws CardException
	{
		byte[] signature = null;
		try
		{
			if(aSigningAlg == Algorithms.SIGNATURE_RSA_PSS)
			{
				if(!_in(PKCS11Constants.CKM_RSA_PKCS_PSS, commands.getMechanisms()))
					throw new CardException("PSS padding is not supported");

				PSSParameterSpec pssParams = (PSSParameterSpec) aParams;
				String digestAlgStr = pssParams.getDigestAlgorithm();
				Algorithm digestAlg = getAkisAlgorithm(digestAlgStr);
				if(queryKeyObject(keyID))
				{
					byte keyIDForSignKey = (byte)((byte)0x80 | keyID);
					commands.verify((byte)1, PIN.getBytes("ASCII"), false);
					signature = commands.sign(Algorithm.PKCS_PSS, digestAlg, keyIDForSignKey, dataToBeSigned);

				}
			} else if(isAnRSAPKCSAlgorithm(aSigningAlg)){
				if(isVersion20(commands.getVersion()))
				{
					if(queryKeyObject(keyID))
					{
						byte keyIDForSignKey = (byte)((byte)0x80 | keyID);
						commands.verify((byte)1, PIN.getBytes("ASCII"), false);
						signature = commands.sign(Algorithm.PKCS_1_5, null, keyIDForSignKey, dataToBeSigned);
					}
				}
				else
					signature = commands.sign(dataToBeSigned, keyID);

			} else if (isAnEclipticCurveAlgorithm(aSigningAlg)) {
				Algorithm digestAlgorithm = findDigestAlgorithmOfEC (aSigningAlg) ;

				if(queryKeyObject(keyID))
				{
					byte keyIDForSignKey = (byte)((byte)0x80 | keyID);
					commands.verify((byte)1, PIN.getBytes("ASCII"), false);
					signature = commands.sign(Algorithm.ECC, digestAlgorithm, keyIDForSignKey, dataToBeSigned);
					signature = ECSignatureTLVUtil.addTLVToSignature(signature);
				}

			} else {
				throw new CardException("Unsupported signing alg: " + aSigningAlg);
			}
		}
		catch (CardException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new CardException(e);
		}


		if(checkSignature(signature, aData,cert, aSigningAlg, aParams) == true)
		{
			serialKeyID.put(serialStr, (int)keyID);
			return signature;
		}
		else
			throw new CardException( new AkisSWException(CardErrorCodes.ANAHTAR_HATALI));

	}

	private Algorithm findDigestAlgorithmOfEC (String aSignatureAlg) throws CardException {

		if(aSignatureAlg.contains("SHA256"))
			return Algorithm.SHA_256;

		if(aSignatureAlg.contains("SHA384"))
			return Algorithm.SHA_384;

		if(aSignatureAlg.contains("SHA512"))
			return Algorithm.SHA_512;

		throw new CardException("Unsupported alg: " + aSignatureAlg);
	}

	private boolean isAnEclipticCurveAlgorithm (String algorithmName) {
		if (algorithmName == Algorithms.SIGNATURE_ECDSA_SHA256 || algorithmName == Algorithms.SIGNATURE_ECDSA_SHA384 || algorithmName == Algorithms.SIGNATURE_ECDSA_SHA512
			|| algorithmName == Algorithms.SIGNATURE_ECDSA_SHA224 || algorithmName == Algorithms.SIGNATURE_ECDSA_SHA1 || algorithmName == Algorithms.SIGNATURE_ECDSA ){
			return true ;
		}
		return false ;
	}

	private boolean isAnRSAPKCSAlgorithm (String algorithmName) {
		if ( algorithmName == Algorithms.SIGNATURE_RSA || algorithmName == Algorithms.SIGNATURE_RSA_MD5
				|| algorithmName == Algorithms.SIGNATURE_RSA_SHA1 || algorithmName == Algorithms.SIGNATURE_RSA_SHA224
				|| algorithmName == Algorithms.SIGNATURE_RSA_SHA256	|| algorithmName == Algorithms.SIGNATURE_RSA_SHA384
				|| algorithmName == Algorithms.SIGNATURE_RSA_SHA512){
			return true ;
		}
		return false ;
	}


	private boolean queryKeyObject(byte keyID) throws CardException {
		byte tmpKeyID = keyID;
		boolean isObjectExists = false;
		try {
			isObjectExists = commands.queryObject(Tags.AsymmPriKey, tmpKeyID);
			if(!isObjectExists){
				tmpKeyID = (byte)((byte)0x80 | tmpKeyID);
				isObjectExists = commands.queryObject(Tags.AsymmPriKey, tmpKeyID);
			}
		} catch (Exception e) {
			throw new CardException(e) ;
		}
		return isObjectExists;
	}

	private Algorithm getAkisAlgorithm(String aAlg) throws CardException
	{
		if(aAlg == Algorithms.DIGEST_SHA1)
			return Algorithm.SHA_1;
		else if(aAlg == Algorithms.DIGEST_SHA256)
			return Algorithm.SHA_256;
		else
			throw new CardException("Unsupported digest alg: " + aAlg);
	}


	private boolean isKeyError(Exception ex)
	{
		if(ex instanceof AkisSWException)
		{
			long errorCode = ((AkisSWException)ex).getErrorCode();
			if(		errorCode == CardErrorCodes.ANAHTAR_HATALI ||
					errorCode == CardErrorCodes.ANAHTAR_YOK ||
					errorCode == CardErrorCodes.ISTENILEN_ANAHTAR_YUKLU_DEGIL)
				return true;

		}
		return false;
	}

	private boolean checkSignature(byte[] signature, byte [] data,X509Certificate cert, String aSigningAlg,
								   AlgorithmParameterSpec aParams)
	{
		try
		{

			if(aSigningAlg.equals(Algorithms.SIGNATURE_RSA_PSS))
			{

				PSSParameterSpec pssSpec = (PSSParameterSpec)aParams;

				RSAPSSSignature pssSignature = new RSAPSSSignature();

				MessageDigest digester = MessageDigest.getInstance(pssSpec.getDigestAlgorithm());
				digester.update(data);
				byte [] hash = digester.digest();

				digester.reset();

				PSSParameterSpec params = (PSSParameterSpec) aParams;

				return pssSignature.verifySignature(signature, cert.getPublicKey(),
						hash, digester, params.getSaltLength());
			}

			else {
				String digestAlgOfSignatureAlg = Algorithms.getDigestAlgOfSignatureAlg(aSigningAlg);
				String javaDigestName;
				if(digestAlgOfSignatureAlg == null)
					javaDigestName = "NONE";
				else
					javaDigestName = digestAlgOfSignatureAlg.replaceAll("-", "");

				String fullAlgorithmName = "" ;
				if (aSigningAlg.contains("RSA")) {
					fullAlgorithmName = javaDigestName + "with" + "RSA";
				} else if (aSigningAlg.contains("ECDSA")) {
					fullAlgorithmName = javaDigestName + "with" + "ECDSA";
				} else {
					throw new CardException("Unsupported signing alg: " + aSigningAlg);
				}

				Signature signatureChecker =  Signature.getInstance(fullAlgorithmName);
				signatureChecker.initVerify(cert);
				signatureChecker.update(data);
				return signatureChecker.verify(signature);
			}
		}
		catch(Exception e)
		{
			LOGGER.warn("Warning in APDUSmartCard", e);
			return false;
		}
	}
	/**
	 * openSession opens a session between the application and the token present in the given slot.
	 *
	 * @param aSlotID slot id of the token
	 * @throws SmartCardException
	 */
	@Override
	public void openSession(long aSlotID) throws SmartCardException
	{
		try
		{
			List<CardTerminal> cts = listCardTerminals(CardTerminals.State.ALL);
			openSession(cts.get((int) (aSlotID -1)));
		}
		catch (CardException e)
		{
			LOGGER.warn("Warning in APDUSmartCard", e);
			throw new SmartCardException("Can not access terminal");
		}
	}
	/**
	 * getEncryptionCertificates returns encryption certificates.
	 * @return list of encryption certificates. If no encryption certificate is found,returns empty list.
	 * @throws SmartCardException
	 */
	@Override
	public List<byte[]> getEncryptionCertificates() throws SmartCardException
	{
		List<X509Certificate> certs = readCertificates();
		List<byte [] > encryptionCerts = new ArrayList<byte[]>();
		for (X509Certificate cert : certs)
		{
			if(cert.getKeyUsage()[2] == true
					|| cert.getKeyUsage()[3] == true)
			{
				try
				{
					encryptionCerts.add(cert.getEncoded());
				}
				catch (CertificateEncodingException e)
				{
					LOGGER.error("Certificate can not decoded", e);
				}
			}
		}

		return encryptionCerts;
	}
	/**
	 *  logs user to the token
	 * @param aCardPIN  pin of the token
	 * @throws SmartCardException
	 * @throws LoginException
	 */
	@Override
	public void login(String aCardPIN) throws SmartCardException, LoginException
	{
		try
		{
			if(isVersion20(commands.getVersion()))
			{
				commands.verify((byte)1, aCardPIN.getBytes("ASCII"), false);
				PIN = aCardPIN;
			}
			else
				commands.verify(aCardPIN.getBytes("ASCII"), false);
		}
		catch(AkisSWException e)
		{
			if(isVersion20(commands.getVersion()))
			{
				if(e.getErrorCode() == 0x6983)
				{
					throw new LoginException(SmartCardI18n.getMsg(E_KEYS.PIN_LOCKED), e, false, true);
				}
			}
			else
			{
				if(e.getErrorCode() == 0x6984)
				{
					throw new LoginException(SmartCardI18n.getMsg(E_KEYS.PIN_LOCKED), e, false, true);
				}
			}

			if((e.getErrorCode() & CardErrorCodes.PIN_HATALI)  == CardErrorCodes.PIN_HATALI)
			{
				if(e.getErrorCode() == CardErrorCodes.PIN_HATALI)
					throw new LoginException(SmartCardI18n.getMsg(E_KEYS.PIN_LOCKED), e, false, true);
				else if(e.getErrorCode() == (CardErrorCodes.PIN_HATALI + 1))
					throw new LoginException(SmartCardI18n.getMsg(E_KEYS.INCORRECT_PIN_FINAL_TRY), e, true, false);
				else
					throw new LoginException(SmartCardI18n.getMsg(E_KEYS.INCORRECT_PIN), e, false, false);
			}
		}
		catch (Exception e)
		{
			throw new SmartCardException(e);
		}
	}
	/**
	 * logs a user out from a token.
	 * @throws SmartCardException
	 */
	@Override
	public void logout() throws SmartCardException
	{
		try
		{
			if(!isVersion20(commands.getVersion()))
				commands.logout();
			else
			{
				commands.selectMF();
				commands.selectDFByName("PKCS-15".getBytes("ASCII"));
			}
		}
		catch (Exception e)
		{
			throw new SmartCardException(e);
		}
	}
	/**
	 * return serial number
	 * @return
	 * @throws SmartCardException
	 */
	@Override
	public String getSerial() throws SmartCardException
	{
		try
		{
            return StringUtil.toHexString(commands.getSerial());
		}
		catch (Exception e)
		{
			throw new SmartCardException("Can not get Serial",e);
		}
	}
	/**
	 * return serial number of token
	 * @param aSlotID
	 * @return
	 * @throws SmartCardException
	 */
	@Override
	public String getSerial(long aSlotID) throws SmartCardException
	{
		try
		{
			List<CardTerminal> cts = listCardTerminals(CardTerminals.State.ALL);
			pcsc = terminalHandler.getTransmitter(cts.get((int) (aSlotID -1)));
			commands = CIFFactory.getAkisCIFInstance(pcsc);
			byte [] serial = new byte[0];
			serial = commands.getSerial();
			pcsc.closeCardTerminal();
            String serialString = StringUtil.toHexString(serial);
			return serialString;
		}
		catch (Exception e) {
			throw new SmartCardException("Can not get Serial",e);
		}
	}

	/**
	 * prepare and return signer from X509Certificate and signing algorithm
	 * @return
	 * @throws SmartCardException
	 */
	@Override
	public BaseSigner getSigner(X509Certificate aCert, String aSigningAlg)
			throws SmartCardException
	{
		return new APDUSigner(this, aCert, aSigningAlg);
	}
	/**
	 * prepare and return signer from X509Certificate, signing algorithm and AlgorithmParameterSpec
	 * @return
	 * @throws SmartCardException
	 */
	@Override
	public BaseSigner getSigner(X509Certificate aCert, String aSigningAlg, AlgorithmParameterSpec aParams)
			throws SmartCardException
	{
		return new APDUSigner(this, aCert, aSigningAlg, aParams);
	}
	/**
	 * closeSession closes the session between the application and the token
	 * @throws SmartCardException
	 */
	@Override
	public void closeSession() throws SmartCardException
	{
		pcsc.closeCardTerminal();
	}
	/**
	 * Checks whether version is 20 or not.
	 * @return True if version is not belong to version 1*, false otherwise
	 */
	public static boolean isVersion20(Version aVersion)
	{
		if(aVersion == Version.V10_UEKAE ||
				aVersion == Version.V11_UEKAE_INF ||
				aVersion == Version.V121_UEKAE_INF ||
				aVersion == Version.V121_UEKAE_NXP ||
				aVersion == Version.V121_UEKAE_UKiS ||
				aVersion == Version.V121_UEKAE_UKIS_HHNEC ||
				aVersion == Version.V121_UEKAE_UKiS_SMIC ||
				aVersion == Version.V122_UEKAE_INF ||
				aVersion == Version.V122_UEKAE_NXP ||
				aVersion == Version.V122_UEKAE_UKiS_HHNEC ||
				aVersion == Version.V122_UEKAE_UKiS_SMIC ||
				aVersion == Version.V12_UEKAE_INF ||
				aVersion == Version.V12_UEKAE_NXP ||
				aVersion == Version.V12_UEKAE_UKiS ||
				aVersion == Version.V13_UEKAE_INF ||
				aVersion == Version.V14_UEKAE_INF ||
				aVersion == Version.V14_UEKAE_NXP)
			return false;
		else
			return true;

	}

	public static boolean _in(long aElement,long[] aList)
	{
		if(aList == null)
			return false;
		for (int i = 0 ; i < aList.length ; i++)
		{
			if (aList[i] == aElement)
				return true;
		}
		return false;
	}

}
