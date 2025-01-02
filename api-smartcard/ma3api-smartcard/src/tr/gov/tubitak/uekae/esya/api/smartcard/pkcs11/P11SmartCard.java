package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_TOKEN_INFO;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.smartcard.bundle.SmartCardI18n;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithCertSerialNo;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.List;

public class P11SmartCard implements BaseSmartCard
{
	private static Logger logger = LoggerFactory.getLogger(P11SmartCard.class);
	
	protected SmartCard sc;
	protected long slot;
	protected long session;
	/**
     * Create smart card with a card type
     * @param aCardType
     * @throws PKCS11Exception
     * @throws IOException
     */
	public P11SmartCard(CardType aCardType) throws PKCS11Exception, IOException
	{
		sc = new SmartCard(aCardType);
	}
	
	/**
	 * openSession opens a session between the application and the token present in the given slot.
	 * 
	 * @param aSlotID slot id of the token
	 * @throws SmartCardException
	 */
	public void openSession(long aSlotID) throws SmartCardException 
	{
		try 
		{
			slot = aSlotID;
			session = sc.openSession(aSlotID);
		} 
		catch (PKCS11Exception e) 
		{
			throw new SmartCardException(e.getMessage(), e);
		}
	}

	
	/**
	 * getSignatureCertificates returns signing certificates.
	 * @return list of signing certificates. If no signing certificate is found,returns empty list.
	 * @throws SmartCardException
	 */
	public List<byte[]> getSignatureCertificates()
			throws SmartCardException 
	{
		try 
		{
			return sc.getSignatureCertificates(session);
		}
		catch (PKCS11Exception e) 
		{
			throw new SmartCardException(e.getMessage(), e);
		}
	}
	/**
	 * getEncryptionCertificates returns encryption certificates.
	 * @return list of encryption certificates. If no encryption certificate is found,returns empty list.
	 * @throws SmartCardException
	 */
	public List<byte[]> getEncryptionCertificates()
			throws SmartCardException 
	{
		try 
		{
			return sc.getEncryptionCertificates(session);
		}
		catch (PKCS11Exception e) 
		{
			throw new SmartCardException(e.getMessage(), e);
		}
	}
	/**
	 *  logs user to the token
	 * @param aCardPIN  pin of the token
	 * @throws SmartCardException
	 * @throws LoginException
	 */
	public void login(String aCardPIN)
			throws SmartCardException, LoginException 
	{
		try 
		{
			sc.login(session, aCardPIN);
		}
		catch (PKCS11Exception e) 
		{
			
			if(e.getErrorCode() == PKCS11Constants.CKR_PIN_INCORRECT)
			{
				try
				{
					CK_TOKEN_INFO tokenInfo = sc.getTokenInfo(slot);
					
					if((tokenInfo.flags & PKCS11Constants.CKF_USER_PIN_FINAL_TRY) == PKCS11Constants.CKF_USER_PIN_FINAL_TRY)
						throw new LoginException(SmartCardI18n.getMsg(E_KEYS.INCORRECT_PIN_FINAL_TRY), e, true, false);
				} 
				catch (PKCS11Exception e1)
				{
					logger.error("TokenInfo alınamadı", e1);
				}
				
				throw new LoginException(SmartCardI18n.getMsg(E_KEYS.INCORRECT_PIN), e, false, false);
			}
			else if(e.getErrorCode() == PKCS11Constants.CKR_PIN_LOCKED)
				throw new LoginException(SmartCardI18n.getMsg(E_KEYS.PIN_LOCKED), e, false, true);
			
			if(e.getErrorCode() != PKCS11Constants.CKR_USER_ALREADY_LOGGED_IN)
				throw new SmartCardException(e.getMessage(), e);
		}
		
	}
	/**
	 * logs a user out from a token.
	 * @throws SmartCardException
	 */
	public void logout() throws SmartCardException 
	{
		try 
		{
			sc.logout(session);
		}
		catch (PKCS11Exception e) 
		{
			throw new SmartCardException(e.getMessage(), e);
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
            //CK_TOKEN_INFO tokenInfo =sc.getTokenInfo(slot);
           // getSerial(slot);
			getSignatureCertificates();          
			return true;
		} 
		catch (Exception e) {
			logger.warn("Warning in P11SmartCard", e);
            return false;
        }
    }
	/**
	 * return serial number of token
	 * @return 
	 * @throws SmartCardException
	 */
	public String getSerial() throws SmartCardException 
	{
        return getSerial(slot);
	}

	/**
	 * prepare and return signer from X509Certificate and signing algorithm
	 * @return 
	 * @throws SmartCardException
	 */
	public BaseSigner getSigner(X509Certificate aCert, String aSigningAlg)
			throws SmartCardException 
	{
		return new SCSignerWithCertSerialNo(sc, session, slot, aCert.getSerialNumber().toByteArray(), aSigningAlg);
	}
	/**
	 * prepare and return signer from X509Certificate, signing algorithm and AlgorithmParameterSpec
	 * @return 
	 * @throws SmartCardException
	 */
	public BaseSigner getSigner(X509Certificate aCert, String aSigningAlg, 	AlgorithmParameterSpec aParams)
			throws SmartCardException 
	{
		return new SCSignerWithCertSerialNo(sc, session, slot, aCert.getSerialNumber().toByteArray(), aSigningAlg, aParams);
	}
	/**
	 * return smart card
	 * @return
	 */
	public SmartCard getSmartCard()
	{
		return sc;
	}
	/**
	 * closeSession closes the session between the application and the token
	 * @throws SmartCardException
	 */
	public void closeSession() throws SmartCardException 
	{
		try 
		{
			sc.closeSession(session);
		}
		catch (PKCS11Exception e) 
		{
			throw new SmartCardException(e);
		}
	}

	/**
	 * return serial number of token
	 * @param aSlotID
	 * @return 
	 * @throws SmartCardException
	 */
    public String getSerial(long aSlotID) throws SmartCardException
	{
		try 
		{
			String serial = new String(sc.getTokenInfo(aSlotID).serialNumber).trim();
			return serial;
		}
		catch (PKCS11Exception e) 
		{
			throw new SmartCardException(e.getMessage(), e);
		}
	}


	public long getSlotNo(){
		return slot;
	}

	public long getSessionNo()
	{
		return session;
	}


	
	
}
