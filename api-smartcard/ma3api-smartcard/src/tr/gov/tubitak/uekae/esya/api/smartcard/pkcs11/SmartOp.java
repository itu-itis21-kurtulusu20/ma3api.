package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.MA3APIEnvironment;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.smartcard.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.smartcard.bundle.SmartCardI18n;
import tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder.KeyFinder;
import tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder.ModulusFinderFromObjectID;
import tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder.PublicKeyFinderWithLabel;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.EncryptionSchemeFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.IEncryptionScheme;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.ISignatureScheme;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.P11SignParameters;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.SignatureSchemeFactory;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CardTerminals.State;
import javax.smartcardio.TerminalFactory;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class SmartOp
{

	private long mSlotID;
	private CardType mCardType;
	private String mPassword;
	private static Logger logger = LoggerFactory.getLogger(SmartOp.class);


	static {
		String os = System.getProperty("os.name").toLowerCase();

		if(os.indexOf("mac") >=0){
			if(MA3APIEnvironment.SET_MAC_OS_PCSC_PATH)
				System.setProperty("sun.security.smartcardio.library", "/System/Library/Frameworks/PCSC.framework/Versions/Current/PCSC");
		}
	}


	/**
	 * Create SmartOp with slotId,card type and password
	 * @param aSlotID
	 * @param aCardType
	 * @param aPassword
	 */
	public SmartOp(long aSlotID, CardType aCardType, String aPassword) {
		this();
		initialize(aSlotID, aCardType, aPassword);
	}


	public SmartOp()
	{
		try
		{
			LV.getInstance().checkLD(LV.Urunler.AKILLIKART);
		}
		catch(LE e)
		{
			throw new RuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
		}
	}


	public void initialize(long aSlotID, CardType aCardType,String aPassword)
	{
		mSlotID = aSlotID;
		mCardType = aCardType;
		mPassword = aPassword;
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


	public static Pair<Long, CardType> findCardTypeAndSlot(Application aApp) throws SmartCardException
	{
		 try
	        {
	            TerminalFactory tf = TerminalFactory.getDefault();
	            List<CardTerminal> tList = listCardTerminals(tf, CardTerminals.State.CARD_PRESENT);
	    		int length = tList.size();
	            String tName = "";
	            CardTerminal ct = null;
	            
	            while(true)
	            {
		            if(length == 0)
		            {
		                throw new SmartCardException("Kart takili okuyucu bulunamadi.");
		            }
		            else if(length == 1)
		            {
		                tName = tList.get(0).getName();
		                ct = tList.get(0);
		                break;
		            }
					else
					{
		            	int cevap = 0;
		            	cevap = _terminalSectir(tList);
	                    if(cevap != -1) 
	                    {
			                tName = tList.get(cevap).getName();
			                ct = tf.terminals().getTerminal(tName);
			                break;
	                    }
	                    tList = listCardTerminals(tf, CardTerminals.State.CARD_PRESENT);
		                length = tList.size();
		            }
	            }
	            
	            Card card = ct.connect("*");
	            String atrHash = _convertToHex(card.getATR().getBytes());
	            card.disconnect(false);
	            CardType kartTipi = CardType.getCardTypeFromATR(atrHash, aApp);
	            
	            if(kartTipi == CardType.UNKNOWN)
	            	return new Pair<Long, CardType>(null, kartTipi);
	            else
	            {
	                Long slotID = null;
	                try
	                {
	                	slotID = _terminaldenSlotBul(kartTipi, tName);
	                }
	                catch(Exception e)
	                {
						logger.warn("Warning in SmartOp", e);
	                	slotID = null;
	                }
	                return new Pair<Long, CardType>(slotID, kartTipi);
	            }
	            
	        }
	        catch(SmartCardException e)
	        {
	        	throw e;
	        }
	        catch(Exception e)
	        {
	            throw new SmartCardException("ATR den kart tipi bulma isleminde hata olustu.", e);
	        }
	}

	public static List<Pair<Long, CardType>> findCardTypesAndSlots(Application aApp) throws SmartCardException
	{
		try
    	{
    		TerminalFactory tf = TerminalFactory.getDefault();
    		List<CardTerminal> tList = listCardTerminals(tf, CardTerminals.State.CARD_PRESENT);
    		int length = tList.size();
    		if(length == 0)
            {
                throw new SmartCardException("Kart takili okuyucu bulunamadi.");
            }
    		
    		List< Pair<Long, CardType>> list = new ArrayList< Pair<Long, CardType>>();
    		for(CardTerminal ct:tList)
    		{
    			String tName = ct.getName();
    			Card card = ct.connect("*");
    			String atrHash = _convertToHex(card.getATR().getBytes());
                card.disconnect(false);
                CardType kartTipi = SmartCard.findCardType(atrHash, aApp);
                
                if(kartTipi == CardType.UNKNOWN)
                	list.add(new Pair<Long, CardType>(null, kartTipi));
                else
                {
	                Long slotID = null;
	                try
	                {
	                	slotID = _terminaldenSlotBul(kartTipi, tName);
	                }
	                catch(Exception e)
	                {
						logger.warn("Warning in SmartOp", e);
	                	slotID = null;
	                }
	                list.add(new Pair<Long, CardType>(slotID, kartTipi));
                }
    		}
    		
    		return list;
    	}
    	catch(Exception aEx)
    	{
    		throw new SmartCardException("ATR den kart tipi bulma isleminde hata olustu.", aEx);
    	}
	}
	
	/***
	 * Finds card type and slot number. If there are more than one cards, it wants user to select
	 * one of them
	 * @return slotID and card type of selected card 
	 * @throws SmartCardException 
	 */
    public static Pair<Long, CardType> findCardTypeAndSlot() throws SmartCardException
    {
       return findCardTypeAndSlot(Application.ESIGNATURE);
    }
    
    /***
     * returns all connected card's slot numbers and card types of slots.
     * @return slotID and card type of selected card 
	 * @throws SmartCardException 
     */
    public static List<Pair<Long, CardType>> findCardTypesAndSlots()
    throws SmartCardException
    {
    	return findCardTypesAndSlots(Application.ESIGNATURE);
    }
    
    /**
     * List card terminals
     * @param aTf
     * @param aState
     * @return list card terminal. If there is no card terminal returns empty list
     * @throws CardException
     */
    private static List<CardTerminal> listCardTerminals(TerminalFactory aTf,State aState) throws CardException
    {
    	return listCardTerminals(aTf, aState, true);
    }


    private static  List<CardTerminal> listCardTerminals(TerminalFactory aTf, State aState, boolean firstTime) throws CardException
	{
		List<CardTerminal> tList = new ArrayList<CardTerminal>();
		try
		{
			tList = aTf.terminals().list(aState);
		}
		catch(CardException ex)
		{
			if(firstTime == true && ex.getCause() != null && "SCARD_E_SERVICE_STOPPED".equals(ex.getCause().getMessage())) {
				refreshCardTerminalContext();
				return listCardTerminals(aTf, aState, false);
			}

			if(!ex.getCause().getMessage().contains("SCARD_E_NO_READERS_AVAILABLE"))
				throw ex;
		}
		return tList;
	}

	private static void refreshCardTerminalContext() {
		try {
			logger.warn("Smart card terminal retrieve failed. Terminal may be removed. Trying to refresh card terminal context");
			Class pcsterminal = Class.forName("sun.security.smartcardio.PCSCTerminals");
			Field contextId = pcsterminal.getDeclaredField("contextId");
			contextId.setAccessible(true);

			if (contextId.getLong(pcsterminal) != 0L) {
				// first get a new context value
				Class pcsc = Class.forName("sun.security.smartcardio.PCSC");
				Method SCardEstablishContext = pcsc.getDeclaredMethod("SCardEstablishContext", new Class[]{Integer.TYPE});
				SCardEstablishContext.setAccessible(true);
				Field SCARD_SCOPE_USER = pcsc.getDeclaredField("SCARD_SCOPE_USER");
				SCARD_SCOPE_USER.setAccessible(true);

				long newId = (Long) SCardEstablishContext.invoke(pcsc, new Object[]{SCARD_SCOPE_USER.getInt(pcsc)});
				contextId.setLong(pcsterminal, newId);

				// then clear terminals in cache
				TerminalFactory factory = TerminalFactory.getDefault();
				CardTerminals terminals = factory.terminals();
				Field fieldTerminals = pcsterminal.getDeclaredField("terminals");
				fieldTerminals.setAccessible(true);
				Class classMap = Class.forName("java.util.Map");
				Method clearMap = classMap.getDeclaredMethod("clear");
				clearMap.invoke(fieldTerminals.get(terminals));

				Field fieldStateMap = pcsterminal.getDeclaredField("terminals");
				fieldStateMap.setAccessible(true);
				clearMap.invoke(fieldStateMap.get(terminals));

				logger.info("Successfully refreshed card terminal context");
			}
		} catch (Exception e1) {
			logger.warn("Unable to refresh card terminals", e1);
		}
	}

	private static int _terminalSectir(List<CardTerminal> aTList)
    {
        
        int length = aTList.size();
        String[] tNames = new String[length];
        for(int i=0;i<length;i++)
        {
            tNames[i] = aTList.get(i).getName();
        }
        
        return secenekSor(null, null, tNames,"Okuyucu Listesi",new String[]{SmartCardI18n.getMsg(E_KEYS.OK, null)});
        
    }
    
    private static long _terminaldenSlotBul(CardType aKartTipi,String aTerminalName)
    throws PKCS11Exception,IOException
    {
        SmartCard sc = new SmartCard(aKartTipi);
        long[] slotList = sc.getTokenPresentSlotList();
        long slotID = 0;
        for(long id : slotList)
        {
            String slotDesc = new String(sc.getSlotInfo(id).slotDescription).trim();
            if(aTerminalName.contains(slotDesc))
            {
                slotID = id;
                break;
            }
        }
        
        return slotID;
    }
    
    
    private static String _convertToHex(byte[] aATR)
    {
         final char hexChars[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E','F' };
    	
    	if (aATR == null) return "null";
		if (aATR.length == 0) return "empty array";
		byte strBuffer[] = new byte[aATR.length * 2];
		int j = 0;
		try
		{
			for (byte element : aATR)
			{
				strBuffer[j++] = (byte) hexChars[(element & 0xF0) >> 4];
				strBuffer[j++] = (byte) hexChars[(element & 0x0F)];
			}
		} catch (RuntimeException e)
		{
			throw e;
		}
		return new String(strBuffer);
    }

    /***
     * Returns name of card present card readers.
     * @throws SmartCardException 
     */

	public static String[] getCardTerminals() throws SmartCardException
	{
		try
		{
			TerminalFactory tf = TerminalFactory.getDefault();
			List<CardTerminal> tList = listCardTerminals(tf, CardTerminals.State.CARD_PRESENT);
			String [] terminals = new String[tList.size()];
			for(int i=0; i < tList.size(); i++)
				terminals[i] = tList.get(i).getName();
			return terminals;
		}
		catch(CardException ex)
		{
			throw new SmartCardException("Can not access terminals", ex);
		}
	}

	public static String [] getCardATRs() throws SmartCardException{
		String [] atrs = null;
		try
		{
			TerminalFactory tf = TerminalFactory.getDefault();
			List<CardTerminal> tList = listCardTerminals(tf, CardTerminals.State.CARD_PRESENT);
			atrs = new String[tList.size()];

			for(int i=0; i < tList.size(); i++){
				CardTerminal ct = tList.get(i);
				Card card = ct.connect("*");
				String atrHex = StringUtil.toHexString(card.getATR().getBytes());
				atrs[i] = atrHex;
			}
		}
		catch(CardException ex)
		{
			throw new SmartCardException("Can not access terminals", ex);
		}

		return atrs;
	}
	

	public static Pair<Long, CardType> getSlotAndCardType(String terminal,
			Application aApp) throws SmartCardException 
	{
		try
		{
			TerminalFactory tf = TerminalFactory.getDefault();
			CardTerminal ct = tf.terminals().getTerminal(terminal);
	        Card card = ct.connect("*");
	        String atrHash = _convertToHex(card.getATR().getBytes());
	        card.disconnect(false);
	        CardType kartTipi = CardType.getCardTypeFromATR(atrHash, aApp);
	        Long slotID = null;
	        try
	        {
	        	if(kartTipi != CardType.UNKNOWN )
	        		slotID = _terminaldenSlotBul(kartTipi, terminal);
	        }
	        catch(Exception e)
	        {
				logger.warn("Warning in SmartOp", e);
	        	slotID = null;
	        }
	        Pair<Long, CardType> slot = new Pair<Long, CardType>(slotID, kartTipi);
	        return slot;
		}
		catch(Exception ex)
		{
			throw new SmartCardException("Can not access terminal", ex);
		}
	}

	/**
	 * Finds slot number and card type given terminal name
	 * @throws SmartCardException 
	 */

	public static Pair<Long, CardType> getSlotAndCardType(String terminal) throws SmartCardException
	{
		return getSlotAndCardType(terminal, Application.ESIGNATURE);
	}


	/**
	 * Kartin seri numarasini doner.
	 * @return
	 * @throws PKCS11Exception
	 * @throws IOException
	 */
	public String getCardSerialNumber()
			throws PKCS11Exception,IOException
	{
		SmartCard sc = new SmartCard(mCardType);
		return new String(sc.getTokenInfo(mSlotID).serialNumber).trim();

	}


	/**
	 * Verilen sertifika,ozel anahtar ve ozel anahtardan olusturulan acik anahtari karta yazar.
	 * Method icinde karta login islemi gerceklestiginden constructor da kart parolasi verilmelidir.
	 * @param aCertLabel
	 *        Karta sertifika ve anahtarlarin import edilecegi labeldir.
	 * @param aPrivKey
	 *        Karta import edilecek ozel anahtardir. Acik anahtar da,ozel anahtardan olusturulur.
	 * @param aCert
	 *        Karta import edilecek sertifikadir.
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	public void importCertificateAndKey(String aCertLabel, String aKeyLabel, PrivateKey aPrivKey, X509Certificate aCert)
			throws PKCS11Exception,IOException,SmartCardException
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.login(sessionID, mPassword);
			sc.importCertificateAndKey(sessionID, aCertLabel,aKeyLabel, aPrivKey, aCert);
			sc.logout(sessionID);
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);

		}

	}

	public boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName,byte[] aPbCertData, int aSignOrEnc)
			throws PKCS11Exception,IOException
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			return sc.importCertificateAndKeyWithCSP(aAnahtarCifti, aAnahtarLen, aScfname, aContextName, aPbCertData, aSignOrEnc);

		}
		finally
		{
			if(sc != null && sessionID != -1)
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
	 * @throws PKCS11Exception,IOException,SmartCardException
	 * @throws SmartCardException
	 */
	/*public void importRSAKeyPair (String aLabel,RSAPrivateCrtKey aPrivKey,byte[] aSubject,boolean aIsSign,boolean aIsEncrypt)
	throws PKCS11Exception,IOException,SmartCardException
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.login(sessionID, mPassword);
			sc.importRSAKeyPair(sessionID, aLabel, aPrivKey, aSubject,aIsSign,aIsEncrypt);
			sc.logout(sessionID);
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}*/


	/**
	 * Verilen anahtar ciftini karta import eder.
	 * Method icinde karta login islemi gerceklestiginden constructor da kart parolasi verilmelidir.
	 * @param aLabel
	 *        Karta anahtarlarin hangi label ile import edilecegidir.
	 * @param aKeyPair
	 *        Karta import edilecek anahtar ciftidir.
	 * @param aSubject
	 *        Ozel anahtarin subject alanina set edilecek degerdir.
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	public void importKeyPair (String aLabel, KeyPair aKeyPair, byte[] aSubject, boolean aIsSign, boolean aIsEncrypt)
			throws PKCS11Exception,IOException,SmartCardException
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.login(sessionID, mPassword);
			sc.importKeyPair(sessionID, aLabel, aKeyPair, aSubject,aIsSign,aIsEncrypt);
			sc.logout(sessionID);
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}


	/**
	 * Verilen label degerine sahip sertifika karttan okunur.
	 * @param aLabel
	 *        Karttan okunacak sertifikanin labelidir.Kartta bu isimli CKO_CERTIFICATE tipinde nesne
	 *        bulunamazsa KriptoException atar.
	 * @return
	 * @throws IOException
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public List<byte[]> readCertificate(String aLabel)
			throws IOException,PKCS11Exception,SmartCardException
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			return sc.readCertificate( sessionID, aLabel);
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}

	}


	/**
	 * Kartta bulunan imzalama sertifikalarini (digitalSignature biti set edilmis) listeler.
	 * @return
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	public List<byte[]> getSignCertificates()
			throws PKCS11Exception,IOException,SmartCardException
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
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}


	/**
	 * Kartta bulunan sifreleme sertifikalarini(keyEncipherment yada dataEncipherment biti set edilmis) listeler.
	 * @return
	 * @throws IOException
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public List<byte[]> getEncryptCertificates()
			throws IOException,PKCS11Exception,SmartCardException
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
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}

	/**
	 * Verilen sertifikayi karta import eder.
	 * Bazi card bu islem icin login gerektirdiginden,CKR_USER_NOT_LOGGED_IN istisnasi atilmasi
	 * durumunda method icinde login islemi gerceklesir.
	 * @param aCertLabel
	 *        Karta sertifikanin hangi label ile import edilecegini belirtir.
	 * @param aCert
	 *        Karta import edilecek sertifika degeridir.
	 * @throws IOException
	 * @throws PKCS11Exception
	 */
	public void importCertificate (String aCertLabel, X509Certificate aCert)
			throws PKCS11Exception,IOException
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.importCertificate(sessionID, aCertLabel, aCert);
		}
		catch(PKCS11Exception aEx)
		{
			if(aEx.getMessage().equals("CKR_USER_NOT_LOGGED_IN"))
			{
				sc.login(sessionID, mPassword);
				sc.importCertificate( sessionID, aCertLabel, aCert);
				sc.logout(sessionID);
			}
			else
				throw aEx;
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}


	/**
	 * Labeli verilen CKO_DATA tipindeki nesneyi kartin ozel alanindan okur.
	 * Method icinde karta login islemi gerceklesir.
	 * @param aLabel
	 *        Karttan okunacak nesnenin ismidir. Kartin ozel alaninda bu isimde nesne bulunmamasi durumunda KriptoException
	 *        atilir. Kartta bu isimde birden fazla nesne varsa, bulunan ilk nesne dondurulur.
	 *        CKO_DATA tipindeki nesneleri okur.
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	public List<byte[]> readPrivateData(String aLabel)
			throws PKCS11Exception,IOException,SmartCardException
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
			if(sc !=null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}


	/**
	 * Labeli verilen CKO_DATA tipindeki nesneyi kartin acik alanindan okur.
	 * @param aLabel
	 *        Karttan okunacak nesnenin ismidir. Kartin acik alaninda bu isimde nesne bulunmamasi durumunda KriptoException
	 *        atilir. Kartta bu isimde birden fazla nesne varsa, bulunan ilk nesne dondurulur.CKO_DATA tipindeki nesneleri okur.
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	public List<byte[]> readPublicData(String aLabel)
			throws PKCS11Exception,IOException,SmartCardException
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			List<byte[]> value = sc.readPublicData( sessionID, aLabel);
			return value;
		}
		finally
		{
			if(sc !=null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}

	/**
	 * Verilen veriyi,verilen label ile CKO_DATA tipinde kartin ozel alanina yazar.
	 * Method icinde karta login islemi gerceklesir.
	 * @param aLabel
	 *        Karta verinin hangi label ile yazilacagini belirtir.
	 * @param aData
	 *        Karta yazilacak veridir.
	 * @throws PKCS11Exception
	 * @throws IOException
	 */
	public void writePrivateData(String aLabel, byte[] aData)
			throws PKCS11Exception,IOException
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.login(sessionID, mPassword);
			sc.writePrivateData( sessionID, aLabel, aData);
			sc.logout(sessionID);
		}
		finally
		{
			if(sc !=null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}

	/**
	 * Verilen veriyi,verilen label ile CKO_DATA tipinde kartin acik alanina yazar.
	 * Method icinde bazi card icin login islemi gerceklesir(CKR_USER_NOT_LOGGED_IN istisnasi atilirsa).
	 * @param aLabel
	 *        Karta verinin hangi label ile yazilacagini belirtir.
	 * @param aData
	 *        Karta yazilacak veridir.
	 * @throws PKCS11Exception
	 * @throws IOException
	 */
	public void writePublicData (String aLabel, byte[] aData)
			throws PKCS11Exception,IOException
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
		catch(PKCS11Exception e)
		{
			logger.warn("Warning in SmartOp", e);
			if(e.getMessage().equals("CKR_USER_NOT_LOGGED_IN"))
			{
				sc.login(sessionID, mPassword);
				sc.writePublicData( sessionID, aLabel, aData);
				sc.logout(sessionID);
			}
		}
		finally
		{
			if(sc !=null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}

	/**
	 * Labeli verilen CKO_DATA,CKO_PRIVATEKEY,CKO_PUBLICKEY yada CKO_CERTIFICATE tipinde olan nesneyi kartin
	 * ozel alanindan siler. Method icinde login islemi gerceklesir.
	 * @param aLabel
	 *        Karttan silinecek nesnenin ismidir. Kartta bu isimde birden fazla nesne olmasi durumunda,hepsi silinir.
	 *        Kartta bu isimde bir nesne yoksa,KriptoException atilir.
	 * @throws IOException
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public void deletePrivateData(String aLabel)
			throws IOException,PKCS11Exception,SmartCardException
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
			if(sc !=null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}

	/**
	 * Labeli verilen CKO_DATA,CKO_PRIVATEKEY,CKO_PUBLICKEY yada CKO_CERTIFICATE tipinde olan nesneyi kartin
	 * acik alanindan siler. Method icinde bazi kart tipleri icin login islemi gerceklesir(
	 * CKR_USER_NOT_LOGGED_IN istisnasi atilirsa).
	 * @param aLabel
	 *        Kartin acik alanindan silinecek nesnenin ismidir. Kartta bu isimde birden fazla nesne olmasi
	 *        durumunda,hepsi silinir. Kartta bu isimde bir nesne yoksa,KriptoException atilir.
	 * @throws IOException
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public void deletePublicData(String aLabel)
			throws IOException,PKCS11Exception,SmartCardException
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.deletePublicData(sessionID, aLabel);
		}
		catch(PKCS11Exception aEx)
		{
			if(aEx.getMessage().equals("CKR_USER_NOT_LOGGED_IN"))
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
			if(sc !=null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}


	/**
	 * Kartta verilen label a sahip,CKO_DATA,CKO_PRIVATEKEY,CKO_PUBLICKEY yada CKO_CERTIFICATE
	 * tipinde nesne olup olmadigina bakar.
	 * @param aLabel
	 *        Kartta aranacak nesnenin labelidir.
	 * @param aIsPrivate
	 *        Kartta aramanin ozel yada acik alanda yapilmasini belirler. Arama ozel alanda ise
	 *        method icinde karta login islemi gerceklesir.
	 * @return
	 * @throws IOException
	 * @throws PKCS11Exception
	 */
	public boolean isObjectExist(String aLabel,boolean aIsPrivate)
			throws IOException,PKCS11Exception
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			boolean sonuc;
			if(aIsPrivate)
			{
				sc.login(sessionID, mPassword);
				sonuc = sc.isObjectExist( sessionID, aLabel);
				sc.logout(sessionID);
			}
			else
			{
				sonuc = sc.isObjectExist( sessionID, aLabel);
			}
			return sonuc;
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}

	}

	public boolean isCardEmpty()
			throws PKCS11Exception,IOException
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
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}

	/**
	 * Kartta bir anahtar cifti uretir. Uretilecek anahtarin ozellikleri verilen parametrelerle belirlenir.
	 * @param aKeyLabel
	 * 		  Kartta uretilecek anahtarlara verilecek olan labeldir.
	 * 		  Anahtarla ilgili islemlerde anahtara bu isimle ulasilabilir.
	 * @param aParamSpec
	 * @param aIsSign
	 * 		  Uretilecek ozel anahtarin imzalama icin kullanim durumunu belirler.
	 * @param aIsEncrypt
	 * 		  Uretilecek acik anahtarin sifreleme icin kullanim durumunu belirler.
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	public void generateKeyPair(String aKeyLabel, AlgorithmParameterSpec aParamSpec, boolean aIsSign, boolean aIsEncrypt)
			throws PKCS11Exception,IOException,SmartCardException
	{
		SmartCard smartCard = null;
		long sessionID = -1;
		try
		{


			if(mCardType == null)
			{
				throw new SmartCardException("CardType is null.");
			}
			else if(mSlotID == -1)
			{
				mSlotID = findSlotNumber(mCardType);
			}

			smartCard = new SmartCard(mCardType);
			sessionID = smartCard.openSession(mSlotID);
			smartCard.login(sessionID, mPassword);

			boolean sonuc = smartCard.isObjectExist(sessionID, aKeyLabel);
			if(sonuc)
			{
				throw new SmartCardException(aKeyLabel+" exists in card.");
			}

			smartCard.createKeyPair(sessionID, aKeyLabel, aParamSpec, aIsSign, aIsEncrypt);

			smartCard.logout(sessionID);

		}
		finally
		{
			if(smartCard != null && sessionID != -1)
				smartCard.closeSession(sessionID);
		}

	}

	/**
	 * Karttan acik anahtar okunur.
	 * @param aKeyLabel
	 * 		  Karttan okunacak acik anahtarin label degeridir.
	 * @return
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */

	public KeySpec getPublicKeySpec(String aKeyLabel)
			throws PKCS11Exception,IOException,SmartCardException
	{
		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			if(mCardType == null)
			{
				throw new SmartCardException("CardType is null.");
			}
			else if(mSlotID == -1)
			{
				mSlotID = findSlotNumber(mCardType);
			}

			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			KeySpec spec = sc.readPublicKeySpec(sessionID, aKeyLabel);

			return spec;

		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
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
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	public byte[] sign(byte[] aCertSerialNo, byte[] aToBeSigned, String aSigningAlg)
			throws PKCS11Exception, IOException, SmartCardException {
		return sign(aCertSerialNo, aToBeSigned, aSigningAlg,null);
	}

	/**
	 * sign finds the private key that has the same CKA_ID value with the certificate having the given serial number
	 * and signs the given data.
	 *
	 * @param aSC Created smart card object
	 * @param aSessionID session handle ( must be already logged in)
	 * @param aSlotID token present slot handle
	 * @param aCertSerialNo certificate serial number
	 * @param aToBeSigned Data to be signed
	 * @param aSigningAlg Signing algorithm
	 * @return signature
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public static byte[] sign(ISmartCard aSC, long aSessionID, long aSlotID, byte[] aCertSerialNo, byte[] aToBeSigned, String aSigningAlg)
			throws PKCS11Exception, SmartCardException {
		return sign(aSC, aSessionID, aSlotID, aCertSerialNo, aToBeSigned, aSigningAlg,null);
	}

	/**
	 * sign finds the private key with the given label and signs the given data.
	 *
	 * @param aKeyLabel Label of the private key
	 * @param aToBeSigned Data to be signed
	 * @param aSigningAlg Signing algorithm
	 * @return signature
	 * @throws IOException
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public byte[] sign(String aKeyLabel, byte[] aToBeSigned, String aSigningAlg)
			throws IOException, PKCS11Exception, SmartCardException {
		return sign(aKeyLabel, aToBeSigned, aSigningAlg,null);
	}

	/**
	 * sign finds the private key with the given label and signs the given data.
	 *
	 * @param aSC Created smart card object
	 * @param aSessionID Session handle ( must be already logged in)
	 * @param aSlotID Token present slot handle
	 * @param aKeyLabel Label of the private key
	 * @param aToBeSigned Data to be signed
	 * @param aSigningAlg Signing algorithm
	 * @return signature
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public static byte[] sign(ISmartCard aSC, long aSessionID, long aSlotID, String aKeyLabel, byte[] aToBeSigned, String aSigningAlg)
			throws PKCS11Exception, SmartCardException {
		return sign(aSC, aSessionID, aSlotID, aKeyLabel, aToBeSigned, aSigningAlg,null);
	}

	/**
	 *
	 * Seri numarasi verilen sertifikayi kartta bulur. Bulunan sertifika ile ayni ID ye
	 * sahip ozel anahtari kartta bularak, veriyi imzalar.
	 * @param aCertSerialNo
	 * 		  Imzalama icin kullanilacak ozel anahtarin bagli oldugu sertifikanin seri numarasidir.
	 * @param aToBeSigned
	 * 		  Imzalanacak veridir.
	 * @param aSigningAlg
	 * 		  Imzalamada kullanilacak imzalama algoritmasidir.
	 * @return
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	public byte[] sign(byte[] aCertSerialNo,byte[] aToBeSigned,String aSigningAlg,AlgorithmParameterSpec aParams)
			throws PKCS11Exception,IOException,SmartCardException
	{
		if(mCardType == null)
		{
			throw new SmartCardException("CardType is null.");
		}
		else if(mSlotID == -1)
		{
			mSlotID = findSlotNumber(mCardType);
		}

		SmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.login(sessionID, mPassword);
			byte[] imzali = sign(sc,sessionID,mSlotID,aCertSerialNo, aToBeSigned, aSigningAlg,aParams);
			sc.logout(sessionID);
			return imzali;
		}
		finally
		{
			if(sc != null && sessionID != -1) sc.closeSession(sessionID);
		}

	}



	/*public byte[] sign(byte[] aCertSerialNo,byte[] aToBeSigned,String aSigningAlg)
	throws PKCS11Exception,IOException,SmartCardException
	{
		return sign(aCertSerialNo, aToBeSigned, aSigningAlg,null);
	}*/



	/**
	 * Verilen SmartCard objesi uzerinde imzalama islemini gerceklestirir
	 * @param aSC
	 * @param aSessionID
	 * @param aSlotID
	 * @param aCertSerialNo
	 * @param aImzalanacak
	 * @param aSigningAlg
	 * @return
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public static byte[] sign(ISmartCard aSC,long aSessionID,long aSlotID,byte[] aCertSerialNo,byte[] aImzalanacak, String aSigningAlg, AlgorithmParameterSpec aParamSpec)
			throws PKCS11Exception,SmartCardException
	{
		Long objID = null;
		try {
			objID = aSC.getPrivateKeyObjIDFromCertificateSerial(aSessionID, aCertSerialNo);
			KeyFinder kf = new ModulusFinderFromObjectID(aSC, aSessionID, objID);

			long[] mechs = aSC.getMechanismList(aSlotID);

			ISignatureScheme signatureScheme = SignatureSchemeFactory.getSignatureScheme(true, aSigningAlg, aParamSpec, mechs, kf);
			P11SignParameters signParameters = signatureScheme.getSignParameters(aImzalanacak);
			byte[] imzali = aSC.signDataWithKeyID(aSessionID, objID, signParameters.getMech(), signParameters.getSignatureInput());
			return imzali;
		}
		catch (SmartCardException e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, CertSerial: {2}, Algorithm: {3}", aSC.getCardType(), aSlotID, StringUtil.toHexString(aCertSerialNo), aSigningAlg),e);
			throw  e;
		}
		catch (PKCS11Exception e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, CertSerial: {2}, Algorithm: {3}", aSC.getCardType(), aSlotID, StringUtil.toHexString(aCertSerialNo), aSigningAlg), e);

			if (objID != null && e.getErrorCode() == PKCS11Constants.CKR_KEY_TYPE_INCONSISTENT) {
				checkKeyAndSigningAlgConsistency(aSC, aSessionID, aSigningAlg, objID);
			}

			throw  e;
		}
	}

	protected static void checkKeyAndSigningAlgConsistency(ISmartCard aSC, long aSessionID, String aSigningAlg, long objID) throws PKCS11Exception, SmartCardException {
		CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
				new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE)
		};

		aSC.getAttributeValue(aSessionID, objID, template);

		AsymmetricAlg alg = SignatureAlg.fromName(aSigningAlg).getAsymmetricAlg();

		if (!(alg.equals(AsymmetricAlg.RSA) && (Long) template[0].pValue == PKCS11Constants.CKK_RSA ||
				alg.equals(AsymmetricAlg.ECDSA) && (Long) template[0].pValue == PKCS11Constants.CKK_ECDSA)) {
			throw new SmartCardException(GenelDil.mesaj(GenelDil.IMZA_ANAHTAR_ALGORITMA_UYUMSUZLUGU));
		}
	}

	/**
	 * Labeli verilen ozel anahtari bularak veriyi imzalar.
	 * @param aAnahtarAdi
	 * 		  Imzalama icin kullanilacak ozel anahtarin labelidir.
	 * @param aImzalanacak
	 * 		  Imzalanacak veridir.
	 * @param aSigningAlg
	 * 		  Imzalamada kullanilacak imzalama algoritmasidir.
	 * @param aParams
	 * @return
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	public byte[] sign(String aAnahtarAdi,byte[] aImzalanacak,String aSigningAlg,AlgorithmParameterSpec aParams)
			throws IOException,PKCS11Exception,SmartCardException
	{
		ISmartCard sc = null;
		long sessionID = -1;
		if(mCardType == null)
		{
			throw new SmartCardException("CardType is null.");
		}
		else if(mSlotID == -1)
		{
			mSlotID = findSlotNumber(mCardType);
		}

		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.login(sessionID, mPassword);
			byte[] imzali = sign(sc,sessionID,mSlotID,aAnahtarAdi, aImzalanacak, aSigningAlg,aParams);
			sc.logout(sessionID);
			return imzali;
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}

	}


	/*public byte[] sign(String aAnahtarAdi,byte[] aImzalanacak,String aSigningAlg)
    throws IOException,PKCS11Exception,SmartCardException
    {
		return sign(aAnahtarAdi, aImzalanacak, aSigningAlg,null);
    }*/

	public static byte[] sign(ISmartCard aSC, long aSessionID, long aSlotID, Long aKeyID, byte[] aImzalanacak, String aSigningAlg, AlgorithmParameterSpec aParamSpec)
			throws PKCS11Exception, SmartCardException {
		try {
			KeyFinder kf = new ModulusFinderFromObjectID(aSC, aSessionID, aKeyID);

			long[] mechs = aSC.getMechanismList(aSlotID);

			ISignatureScheme signatureScheme = SignatureSchemeFactory.getSignatureScheme(true, aSigningAlg, aParamSpec, mechs, kf);
			P11SignParameters signParameters = signatureScheme.getSignParameters(aImzalanacak);
			CK_MECHANISM mechanism = signParameters.getMech();
			byte[] imzali = aSC.signDataWithKeyID(aSessionID, aKeyID, mechanism, signParameters.getSignatureInput());
			return imzali;
		} catch (SmartCardException e) {
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, Anahtar Adı: {2}, Algorithm: {3}", aSC.getCardType(), aSlotID, aKeyID, aSigningAlg), e);
			throw e;
		} catch (PKCS11Exception e) {
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, Anahtar Adı: {2}, Algorithm: {3}", aSC.getCardType(), aSlotID, aKeyID, aSigningAlg), e);

			if (aKeyID != null && e.getErrorCode() == PKCS11Constants.CKR_KEY_TYPE_INCONSISTENT) {
				checkKeyAndSigningAlgConsistency(aSC, aSessionID, aSigningAlg, aKeyID);
			}

			throw e;
		}
	}

	public static byte[] sign(ISmartCard aSC, long aSessionID, long aSlotID, String aAnahtarAdi, byte[] aImzalanacak, String aSigningAlg, AlgorithmParameterSpec aParamSpec)
			throws PKCS11Exception, SmartCardException {
		Long aObjID = aSC.getPrivateKeyObjIDFromPrivateKeyLabel(aSessionID, aAnahtarAdi);
		return sign(aSC, aSessionID, aSlotID, aObjID, aImzalanacak, aSigningAlg, aParamSpec);
	}

	/**
	 * Labeli verilen acik anahtari bularak imza dogrulama islemini
	 * gerceklestirir.
	 * @param aAnahtarAdi
	 *            Dogrulama icin kullanilacak acik anahtarin labelidir.
	 * @param aImzalanan
	 *            Imzalanan veridir.
	 * @param aDogrulanacak
	 *            Imzalama sonucu ortaya cikmis veridir.
	 * @param aSigningAlg
	 *            Imzalamada kullanilmis imzalama algoritmasidir.
	 * @return
	 * @throws IOException
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	//gemplus da hata veriyo
	public boolean verify(String aAnahtarAdi,byte[] aImzalanan,byte[] aDogrulanacak,String aSigningAlg)
			throws IOException,PKCS11Exception,SmartCardException
	{
		ISmartCard sc = null;
		long sessionID = -1;
		try
		{
			if(mCardType == null)
			{
				throw new SmartCardException("CardType is null.");
			}
			else if(mSlotID == -1)
			{
				mSlotID = findSlotNumber(mCardType);
			}

			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.login(sessionID, mPassword);
			boolean imzaDogru = verify(sc, sessionID, mSlotID, aAnahtarAdi, aImzalanan, aDogrulanacak, aSigningAlg);
			sc.logout(sessionID);
			return imzaDogru;
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}

	}

	public static boolean verify(ISmartCard aSC,long aSessionID,long aSlotID,String aKeyLabel,byte[] aImzalanan,byte[] aDogrulanacak,String aSigningAlg)
			throws PKCS11Exception,SmartCardException{
		return verify(aSC, aSessionID, aSlotID, aKeyLabel, aImzalanan, aDogrulanacak, aSigningAlg, null);
	}

	public static boolean verify(ISmartCard aSC, long aSessionID, long aSlotID, String aKeyLabel, byte[] aImzalanan, byte[] aDogrulanacak, String aSigningAlg, AlgorithmParams aAlgParams)
		throws PKCS11Exception, SmartCardException {
		try {
			PublicKeyFinderWithLabel kf = new PublicKeyFinderWithLabel(aSC, aSessionID, aKeyLabel);

			long[] mechs = aSC.getMechanismList(aSlotID);

			ISignatureScheme signatureScheme = SignatureSchemeFactory.getSignatureScheme(false, aSigningAlg, aAlgParams, mechs, kf);
			P11SignParameters signParameters = signatureScheme.getSignParameters(aImzalanan);
			CK_MECHANISM mechanism = signParameters.getMech();
			byte[] imzalanan = signParameters.getSignatureInput();

			boolean imzaDogru = false;
			try {
				aSC.verifyData(aSessionID, aKeyLabel, imzalanan, aDogrulanacak, mechanism);
				imzaDogru = true;
			} catch (PKCS11Exception e) {
				logger.error("Error in SmartOp", e);
			}
			return imzaDogru;
		}
		catch (SmartCardException e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, Anahtar Adı: {2}, Algorithm: {3}", aSC.getCardType(), aSlotID, aKeyLabel, aSigningAlg),e);
			throw  e;
		}
		catch (PKCS11Exception e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, Anahtar Adı: {2}, Algorithm: {3}", aSC.getCardType(), aSlotID, aKeyLabel, aSigningAlg),e);
			throw  e;
		}
	}

	/**
	 * Labeli verilen acik anahtari kullanarak, verilen veriyi sifreler.
	 * @param aAnahtarAdi
	 * 	   Sifreleme yapilacak acik anahtarin labelidir.
	 * @param aSifrelenecek
	 * 	   Sifrelenecek veridir.
	 * @return
	 * @throws IOException
	 * @throws PKCS11Exception
	 * @throws SmartCardException
	 */
	public byte[] encrypt(String aAnahtarAdi,byte[] aSifrelenecek,String aAlgoritma,AlgorithmParameterSpec aParametreler)
			throws IOException,PKCS11Exception,SmartCardException
	{
		ISmartCard sc = null;
		long sessionID = -1;
		try
		{
			if(mCardType == null)
			{
				throw new SmartCardException("CardType is null.");
			}
			else if(mSlotID == -1)
			{
				mSlotID = findSlotNumber(mCardType);
			}

			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.login(sessionID, mPassword);

			byte[] sifreli = encrypt(sc, sessionID, aAnahtarAdi, aSifrelenecek, aAlgoritma, aParametreler);

			sc.logout(sessionID);

			return sifreli;
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}

	public static byte[] encrypt(ISmartCard aSC,long aSessionID,String aAnahtarAdi,byte[] aSifrelenecek,String aAlgoritma,AlgorithmParameterSpec aParametreler)
			throws PKCS11Exception,SmartCardException
	{
		try{
			long objID = aSC.getPrivateKeyObjIDFromPrivateKeyLabel(aSessionID, aAnahtarAdi);
			ModulusFinderFromObjectID kf = new ModulusFinderFromObjectID(aSC, aSessionID, objID);

			IEncryptionScheme scheme = EncryptionSchemeFactory.getEncryptionScheme(true, aAlgoritma, aParametreler, aSC, aSessionID, kf);
			byte[] sifreli = aSC.encryptData(aSessionID, aAnahtarAdi, scheme.getResult(aSifrelenecek),scheme.getMechanism());
			return sifreli;
		}
		catch (SmartCardException e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Anahtar Adı: {1}, Algorithm: {2}", aSC.getCardType(), aAnahtarAdi, aAlgoritma),e);
			throw  e;
		}
		catch (PKCS11Exception e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Anahtar Adı: {1}, Algorithm: {2}", aSC.getCardType(),  aAnahtarAdi, aAlgoritma), e);
			throw  e;
		}
	}





	/**
	 * Labeli verilen ozel anahtari kullanarak, sifrelenmis veriyi cozer.
	 * @param aAnahtarAdi
	 * 	   Sifre cozmede kullanilacak ozel anahtarin labelidir.
	 * @param aCozulecek
	 * 	   Cozulecek sifreli veridir.
	 * @return
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	/*public byte[] decrypt(String aAnahtarAdi,byte[] aCozulecek)
	throws PKCS11Exception,IOException,SmartCardException
	{
		return decrypt(aAnahtarAdi,aCozulecek,Algorithms.CIPHER_RSA_PKCS1,null);
	}*/

	public byte[] decrypt(String aAnahtarAdi,byte[] aCozulecek,String aAlgoritma,AlgorithmParameterSpec aParam)
			throws PKCS11Exception,IOException,SmartCardException
	{
		if(mCardType == null)
		{
			throw new SmartCardException("CardType is null.");
		}
		else if(mSlotID == -1)
		{
			mSlotID = findSlotNumber(mCardType);
		}

		ISmartCard sc = null;
		long sessionID = -1;

		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.login(sessionID, mPassword);

			byte[] cozulmus = decrypt(sc,sessionID, mSlotID, aAnahtarAdi, aCozulecek,aAlgoritma,aParam);

			sc.logout(sessionID);
			return cozulmus;
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}


	/**
	 * Sertifika seri nosu verilen sertifikayi kullanarak ayni ID ye sahip
	 * private key i bulur, sifrelenmis veriyi cozer.
	 * @param aCertSerialNo
	 * 	   Sifre cozmede kullanilacak ozel anahtarla ayni ID ye sahip sertifikanin seri nosudur.
	 * @param aCozulecek
	 * 	   Cozulecek sifreli veridir.
	 * @return
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	/*public byte[] decrypt(byte[] aCertSerialNo,byte[] aCozulecek)
	throws PKCS11Exception,IOException,SmartCardException
	{
		return decrypt(aCertSerialNo,aCozulecek,Algorithms.CIPHER_RSA_PKCS1,null);
	}*/


	/**
	 * decrypt finds the private key with the given label and decrypts the given encrypted data. RSA/NONE/PKCS algorithm is used during decryption.
	 *
	 * @param aKeyLabel Label of the private key.
	 * @param aEncryptedData  Encrypted data.
	 * @return Decrypted data
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	@Deprecated
	public byte[] decrypt(String aKeyLabel, byte[] aEncryptedData)
			throws PKCS11Exception, IOException, SmartCardException {
		return decrypt(aKeyLabel, aEncryptedData, Algorithms.CIPHER_RSA_PKCS1,null);
	}

	/**
	 * decrypt finds the private key that has the same CKA_ID value with the certificate having the given serial number
	 * and decrypts given data. RSA/NONE/PKCS is used for decryption.
	 *
	 * @param aCertSerialNo Certificate serial number
	 * @param aEncryptedData    Encrypted data.
	 * @return Decrypted data
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	@Deprecated
	public byte[] decrypt(byte[] aCertSerialNo, byte[] aEncryptedData)
			throws PKCS11Exception, IOException, SmartCardException {
		return decrypt(aCertSerialNo, aEncryptedData,Algorithms.CIPHER_RSA_PKCS1,null);
	}


	/**
	 * decrypt finds the private key that has the same CKA_ID value with the certificate having the given serial number
	 * and decrypts given data. RSA/NONE/PKCS is used for decryption.
	 *
	 * @param aSC Created smart card object
	 * @param aSessionID Session handle (must be logged in)
	 * @param aCertSerialNo Certificate serial number
	 * @param aEncryptedData Encrypted data.
	 * @return Decrypted data
	 * @throws SmartCardException
	 * @throws PKCS11Exception
	 */
	@Deprecated
	public static byte[] decrypt(ISmartCard aSC, long aSessionID, byte[] aCertSerialNo, byte[] aEncryptedData)
			throws SmartCardException, PKCS11Exception {
		long slotID = aSC.getSessionInfo(aSessionID).slotID;
		return SmartOp.decrypt(aSC, aSessionID, slotID, aCertSerialNo, aEncryptedData,Algorithms.CIPHER_RSA_PKCS1,null);
	}

	/**
	 * decrypt finds the private key with the given label and decrypts the given data. RSA/NONE/PKCS is used for decryption.
	 *
	 * @param aSC Created smart card object
	 * @param aSessionID Session handle (must be logged in)
	 * @param aKeyLabel Label of the private key.
	 * @param aEncryptedData Encrypted data.
	 * @return Decrypted data
	 * @throws SmartCardException
	 * @throws PKCS11Exception
	 */
	@Deprecated
	public static byte[] decrypt(ISmartCard aSC, long aSessionID, String aKeyLabel, byte[] aEncryptedData)
			throws SmartCardException, PKCS11Exception {
		long slotID = aSC.getSessionInfo(aSessionID).slotID;
		return SmartOp.decrypt(aSC, aSessionID, slotID, aKeyLabel, aEncryptedData, Algorithms.CIPHER_RSA_PKCS1, null);
	}


	public byte[] decrypt(byte[] aCertSerialNo,byte[] aCozulecek,String aAlgoritma,AlgorithmParameterSpec aParam)
			throws PKCS11Exception,IOException,SmartCardException
	{
		if(mCardType == null)
		{
			throw new SmartCardException("CardType is null.");
		}
		else if(mSlotID == -1)
		{
			mSlotID = findSlotNumber(mCardType);
		}

		ISmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.login(sessionID, mPassword);
			byte[] cozulmus = decrypt(sc,sessionID, mSlotID, aCertSerialNo, aCozulecek,aAlgoritma,aParam);

			sc.logout(sessionID);
			return cozulmus;
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}




	/*public static byte[] decrypt(ISmartCard aSC,long aSessionID,byte[] aCertSerialNo,byte[] aCozulecek)
	throws SmartCardException,PKCS11Exception
	{
		return aSC.decryptDataWithCertSerialNo(aSessionID, aCertSerialNo, PKCS11Constants.CKM_RSA_PKCS, aCozulecek);
	}*/

	public static byte[] decrypt(ISmartCard aSC,long aSessionID, long aSlot, byte[] aCertSerialNo,byte[] aCozulecek,String aAlgorithm,AlgorithmParameterSpec aParams)
			throws SmartCardException,PKCS11Exception
	{
		try{
			long objID = aSC.getPrivateKeyObjIDFromCertificateSerial(aSessionID, aCertSerialNo);
			ModulusFinderFromObjectID kf = new ModulusFinderFromObjectID(aSC, aSessionID, objID);

			IEncryptionScheme scheme = EncryptionSchemeFactory.getEncryptionScheme(false, aAlgorithm, aParams, aSC, aSlot, kf);
			byte[] encodedMessage = aSC.decryptDataWithCertSerialNo(aSessionID, aCertSerialNo,  scheme.getMechanism(), aCozulecek);
			return scheme.getResult(encodedMessage);
		}
		catch (SmartCardException e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, CertSerial: {2}, Algorithm: {3}", aSC.getCardType(), aSlot, StringUtil.toHexString(aCertSerialNo), aAlgorithm),e);
			throw  e;
		}
		catch (PKCS11Exception e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, CertSerial: {2}, Algorithm: {3}", aSC.getCardType(), aSlot, StringUtil.toHexString(aCertSerialNo), aAlgorithm), e);
			throw  e;
		}
	}



	/*public static byte[] decrypt(ISmartCard aSC,long aSessionID,String aAnahtarAdi,byte[] aCozulecek)
	throws SmartCardException,PKCS11Exception
	{
		return aSC.decryptData(aSessionID, aAnahtarAdi, aCozulecek, PKCS11Constants.CKM_RSA_PKCS);
	}*/


	public static byte[] decrypt(ISmartCard aSC,long aSessionID, long aSlot, String aAnahtarAdi,byte[] aCozulecek,String aAlgorithm,AlgorithmParameterSpec aParams)
			throws SmartCardException,PKCS11Exception
	{
		try{
			long objID = aSC.getPrivateKeyObjIDFromPrivateKeyLabel(aSessionID, aAnahtarAdi);
			ModulusFinderFromObjectID kf = new ModulusFinderFromObjectID(aSC, aSessionID, objID);

			IEncryptionScheme scheme = EncryptionSchemeFactory.getEncryptionScheme(false, aAlgorithm, aParams, aSC, aSlot, kf);
			byte[] encodedMessage = aSC.decryptData(aSessionID, aAnahtarAdi, aCozulecek, scheme.getMechanism());
			return scheme.getResult(encodedMessage);
		}
		catch (SmartCardException e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, Anahtar Adı: {2}, Algorithm: {3}", aSC.getCardType(), aSlot, aAnahtarAdi, aAlgorithm),e);
			throw  e;
		}
		catch (PKCS11Exception e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, Anahtar Adı: {2}, Algorithm: {3}", aSC.getCardType(), aSlot, aAnahtarAdi, aAlgorithm),e);
			throw  e;
		}
	}


	public static byte[] wrap(ISmartCard aSC,long aSessionID, long aSlot,String aAlgorithm,String wrapperKeyLabel, String labelOfKeyToWrap,AlgorithmParameterSpec aParams)
			throws PKCS11Exception, SmartCardException {
		try{
			long objID = aSC.getPublicKeyObjIDFromPublicKeyLabel(aSessionID, wrapperKeyLabel);
			ModulusFinderFromObjectID kf = new ModulusFinderFromObjectID(aSC, aSessionID, objID);
			IEncryptionScheme scheme = EncryptionSchemeFactory.getEncryptionScheme(false, aAlgorithm, aParams, aSC, aSlot, kf);
			byte[] encodedMessage = aSC.wrapKey(aSessionID, scheme.getMechanism(), wrapperKeyLabel, labelOfKeyToWrap);
			return scheme.getResult(encodedMessage);
		}
		catch (SmartCardException e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, Wrapper Label: {2}, ToWrap Label: {3}, Algorithm: {4}", aSC.getCardType(), aSlot, wrapperKeyLabel, labelOfKeyToWrap, aAlgorithm),e);
			throw  e;
		}
		catch (PKCS11Exception e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, Wrapper Label: {2}, ToWrap Label: {3}, Algorithm: {4}", aSC.getCardType(), aSlot, wrapperKeyLabel, labelOfKeyToWrap, aAlgorithm),e);
			throw  e;
		}
	}

	public static void unwrap(ISmartCard aSC, long aSessionID, long aSlot, String aAlgorithm, String unwrapperKeyLabel, byte[] wrappedKey, KeyTemplate unwrappedKeyTemplate, AlgorithmParameterSpec aParams)
			throws PKCS11Exception, SmartCardException {
		try {
			long objID = aSC.getPrivateKeyObjIDFromPrivateKeyLabel(aSessionID, unwrapperKeyLabel);
			ModulusFinderFromObjectID kf = new ModulusFinderFromObjectID(aSC, aSessionID, objID);

			IEncryptionScheme scheme = EncryptionSchemeFactory.getEncryptionScheme(false, aAlgorithm, aParams, aSC, aSlot, kf);
			aSC.unwrapKey(aSessionID, scheme.getMechanism(), unwrapperKeyLabel, wrappedKey, unwrappedKeyTemplate);
		}
		catch (SmartCardException e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, UnWrapper Label: {2}", aSC.getCardType(), aSlot, unwrapperKeyLabel),e);
			throw  e;
		}
		catch (PKCS11Exception e){
			logger.debug(MessageFormat.format("Hata! KartTipi: {0}, Slot: {1}, UnWrapper Label: {2}", aSC.getCardType(), aSlot, unwrapperKeyLabel), e);
			throw  e;
		}
	}

	public long getSlot()
	{
		return mSlotID;
	}

	public CardType getCardType()
	{
		return mCardType;
	}

	public void formatToken(String aSOpin, String aNewPIN, String aLabel)
			throws PKCS11Exception,IOException
	{
		ISmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.formatToken(aSOpin, aNewPIN, aLabel, (int) mSlotID);

		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}

	public void changePassword(String aOldPass,String aNewPass)
			throws PKCS11Exception,IOException
	{
		ISmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.changePassword(aOldPass, aNewPass, sessionID);

		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}

	public void changePuk(byte[] aSOPin,byte[] aNewSOPin)
			throws PKCS11Exception,IOException
	{
		ISmartCard sc = null;
		long sessionID = -1;
		try
		{
			sc = new SmartCard(mCardType);
			sessionID = sc.openSession(mSlotID);
			sc.setSOPin(aSOPin, aNewSOPin, sessionID);
		}
		finally
		{
			if(sc != null && sessionID != -1)
				sc.closeSession(sessionID);
		}
	}

	/**
	 * CardType verilen kartin takili oldugu slot numarasini bulur.Verilen kart tipinden birden fazla
	 * kart takili ise kullaniciya secim yaptirilir.
	 * @param aCardType
	 *        Slot numarasi bulunmak istenen kartin tipidir.
	 * @return
	 * @throws PKCS11Exception
	 * @throws IOException
	 * @throws SmartCardException
	 */
	public static long findSlotNumber(CardType aCardType)throws PKCS11Exception,IOException,SmartCardException  {
		return findSlotNumber(aCardType,"Slot List");
	}

	public static long findSlotNumber(CardType aCardType, String message) throws PKCS11Exception, IOException, SmartCardException {
		int cevap = 1; // default for refresh
		do {
			ISmartCard sc = new SmartCard(aCardType);
			long[] slotIDList = sc.getTokenPresentSlotList();
			int length = slotIDList.length;
			if (length == 0) {
				throw new SmartCardException("No card present");
			} else if (length == 1) {
				return slotIDList[0];
			} else {
				String[] slotDescList = new String[length];
				for (int i = 0, slotListArrLength = slotIDList.length; i < slotListArrLength; i++) {
					long slotId = slotIDList[i];
					String label = "";
					try {
						label += " " + new String(sc.getSlotInfo(slotId).slotDescription).trim();
					} catch (Exception e) {
						logger.warn("Warning in SmartOp", e);
						// ignore
					}
					try {
						label += " " + new String(sc.getTokenInfo(slotId).label).trim().split("\u0000")[0];
					} catch (Exception e) {
						logger.warn("Warning in SmartOp", e);
						// ignore
					}
					slotDescList[i] = label;
				}

				String[] aOptions = {"OK", "Refresh", "Cancel"};

				JComboBox combo = new JComboBox(slotDescList);

				cevap = JOptionPane.showOptionDialog(
						null, combo, message,
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE,
						null, aOptions, aOptions[0]);
				if (cevap == 0)
					return slotIDList[combo.getSelectedIndex()];
				if (cevap == 2) // Cancel
					throw new SmartCardException("User Did Not Choose Any Slot ");
				if (cevap == 1)
					continue;
				throw new ESYARuntimeException("Unknown Answer:"+cevap);
			}
		} while (true);
	}

	public static int secenekSor(Component aParent, Icon aIcon, String[] aSecenekList, String aBaslik, String[] aOptions)
	{
		JComboBox combo = new JComboBox(aSecenekList);

		int cevap = JOptionPane.showOptionDialog(aParent, combo,
				aBaslik,
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null, aOptions, aOptions[0]);

		if(cevap == 1)
			return -1;
		return combo.getSelectedIndex();
	}
}
