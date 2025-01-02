package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.SunPKCS11;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.*;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.sun.*;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import javax.security.auth.login.LoginException;
import java.security.Provider;
/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 12/14/12 - 8:44 PM <p>
 * <b>Description</b>: <br>
 * NSS Crypto provider based on NSS Provider and nss softtoken. currently it runs with only 1 session.
 */
public class NSSCryptoProvider implements CryptoProvider{
    private static Logger logger = LoggerFactory.getLogger(NSSCryptoProvider.class);

    Provider mProvider = null;
    private boolean fipsMode;
    private SmartCard nssSoftToken;
    private Long slotID;

    public Long getSlotID() {
        return slotID;
    }

    public Long getSessionID() {
        return sessionID;
    }

    public SmartCard getNssSoftToken() {
        return nssSoftToken;
    }

    private Long sessionID;

    /**
     * NSS Crypto provider. we need softtoken pkcs11wrapper since SunPKCS11 does not properly handle fipsmode operation and some mechanisms
     * @param provider NSS provider
     * @param fipsMode whether NSS provider runs on fips mode or not
     * @param nssSoftToken softtoken pkcs11 wrapper
     * @param slotID softtoken slot id
     * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException
     * @throws sun.security.pkcs11.wrapper.PKCS11Exception
     */
    NSSCryptoProvider(Provider provider, boolean fipsMode, SmartCard nssSoftToken, Long slotID) throws CryptoException, PKCS11Exception {
        mProvider = provider;
        this.fipsMode = fipsMode;
        this.nssSoftToken = nssSoftToken;
        this.slotID = slotID;
        this.sessionID = nssSoftToken.openSession(slotID);
    }

    public Provider getmProvider() {
        return mProvider;
    }

    public Cipher getEncryptor(CipherAlg aCipherAlg) throws CryptoException {
		return new NSSCipher(aCipherAlg, mProvider,true);
	}

	public Cipher getDecryptor(CipherAlg aCipherAlg) throws CryptoException {
		return new NSSCipher(aCipherAlg, mProvider,false);
	}

	public Digester getDigester(DigestAlg aDigestAlg) throws CryptoException {
		return new SUNDigester(aDigestAlg, mProvider);
	}

	public Signer getSigner(SignatureAlg aSignatureAlg) throws CryptoException {
		return new SUNSigner(aSignatureAlg, mProvider);
	}

	public Verifier getVerifier(SignatureAlg aSignatureAlg)
			throws CryptoException {
		return new SUNVerifier(aSignatureAlg, mProvider);
	}

	public MAC getMAC(MACAlg aMACAlg) throws CryptoException {
		return new NSSMAC(aMACAlg, mProvider);
	}

	public Wrapper getWrapper(WrapAlg aWrapAlg) throws CryptoException {
		return new NSSWrapper(aWrapAlg,true,nssSoftToken, sessionID);
	}

	public Wrapper getUnwrapper(WrapAlg aWrapAlg) throws CryptoException {
        return new NSSWrapper(aWrapAlg,false,nssSoftToken, sessionID);
	}

	public KeyAgreement getKeyAgreement(KeyAgreementAlg aKeyAgreementAlg)  throws CryptoException {
		return new NSSKeyAgreement(aKeyAgreementAlg,nssSoftToken, slotID,sessionID);
	}

	public KeyFactory getKeyFactory() throws CryptoException {
		return new NSSKeyFactory(mProvider);
	}

	public KeyPairGenerator getKeyPairGenerator(AsymmetricAlg aAsymmetricAlg)
			throws CryptoException {
		return new SUNKeyPairGenerator(aAsymmetricAlg,mProvider);
	}

	public RandomGenerator getRandomGenerator() {
		return new NSSRandomGenerator(mProvider);
	}

    public boolean isFipsMode() {
        return fipsMode;
    }

    /**
     * we may want to zeroize provider at application exit
     */
    public void destroyProvider() {
        try {
            mProvider.clear();
            ((SunPKCS11)mProvider).logout();
        } catch (LoginException e) {
            // ignore..
        }
    }


}


