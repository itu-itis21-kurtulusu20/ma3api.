/**
 * Created by orcun.ertugrul on 27-Nov-17.
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;

import java.io.IOException;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.List;

public class ESYASmartCard extends SmartCard
{
    private static Logger logger = LoggerFactory.getLogger(ESYASmartCard.class);

    public ESYASmartCard(CardType aCardType) throws PKCS11Exception, IOException
    {
        super(aCardType);
    }

    public ESYASmartCard(String aCardTypeName, String smartCardDllName) throws PKCS11Exception, IOException
    {
        super(aCardTypeName, smartCardDllName);
    }

    public ESYASmartCard(String aCardType) throws PKCS11Exception, IOException
    {
        super(aCardType);
    }

    /**
     *
     * @param aSessionID
     * @param aKeyTemplate
     * @throws PKCS11Exception
     * @throws SmartCardException
     */
    public long importSecretKey(long aSessionID, SecretKeyTemplate aKeyTemplate)
            throws PKCS11Exception, SmartCardException
    {
        if(isFipsEnabled)
        {
            List<String> deleteLabelList = new ArrayList<String>();
            byte [] wrappedHMACKey = null;
            SecretKeyTemplate aesUnwrapperKeyTemplate = null;
            byte[] iv = RandomUtil.generateRandom(16);
            try
            {
                Pair<byte [], SecretKeyTemplate> aesInfo = loadAESWrapperKey(aSessionID, deleteLabelList);
                byte [] aesKey = aesInfo.first();
                aesUnwrapperKeyTemplate = aesInfo.second();
                byte [] hmacKey = (byte []) aKeyTemplate.getAttribute(PKCS11Constants.CKA_VALUE);
                wrappedHMACKey =  CipherUtil.encrypt(CipherAlg.AES256_CBC, new ParamsWithIV(iv),
                        hmacKey, aesKey);
                aKeyTemplate.remove(PKCS11Constants.CKA_VALUE);
            }
            catch (CryptoException e)
            {
                throw new SmartCardException(e);
            }

            mCardType.getCardTemplate().applyTemplate(aKeyTemplate);

            CK_MECHANISM mech = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, iv);

            return unwrapKey(aSessionID, mech, aesUnwrapperKeyTemplate, wrappedHMACKey, aKeyTemplate);
        }
        else
        {
            return mCardType.getCardTemplate().getPKCS11Ops().importSecretKey(aSessionID, aKeyTemplate);
        }
    }


    public long[] importKeyPair(long aSessionID, KeyPairTemplate aKeyTemplate) throws PKCS11Exception, SmartCardException
    {
        if(isFipsEnabled)
        {
            List<String> deleteLabelList = new ArrayList<String>();
            SecretKeyTemplate aesUnwrapperKeyTemplate = null;
            byte[] iv = RandomUtil.generateRandom(16);

            try
            {
                if(aKeyTemplate.getPrivateKeyTemplate() != null)
                {
                    Pair<byte[], SecretKeyTemplate> aesInfo = loadAESWrapperKey(aSessionID, deleteLabelList);
                    byte[] aesKey = aesInfo.first();
                    aesUnwrapperKeyTemplate = aesInfo.second();

                    byte[] privateKeyBytes = aKeyTemplate.getPrivateKeyTemplate().getEncoded();
                    byte[] wrappedPrivateKeyBytes = CipherUtil.encrypt(CipherAlg.AES256_CBC,
                            new ParamsWithIV(iv), privateKeyBytes, aesKey);

                    unwrapKey(aSessionID, new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, iv), aesUnwrapperKeyTemplate, wrappedPrivateKeyBytes, aKeyTemplate.getPrivateKeyTemplate());
                }
                RSAPublicKeyTemplate publicKeyTemplate = (RSAPublicKeyTemplate)aKeyTemplate.getPublicKeyTemplate();
                RSAKeyPairTemplate keyPairTemplate = new RSAKeyPairTemplate(publicKeyTemplate, null);

                return mCardType.getCardTemplate().getPKCS11Ops().importKeyPair(aSessionID, keyPairTemplate);
            }
            catch(CryptoException ex)
            {
                throw new SmartCardException(ex);
            }
        }
        else
        {
            return mCardType.getCardTemplate().getPKCS11Ops().importKeyPair(aSessionID, aKeyTemplate);
        }
    }






    private Pair<byte[],SecretKeyTemplate> loadAESWrapperKey(long sid, List<String> deleteLabelList) throws CryptoException, PKCS11Exception, SmartCardException
    {
        final long  KEYMANAGER_CKA_DECRYPT = 0x00000105L;

        try
        {
            String wrapperSessionKeyPairLabel = "wrapper_rsa_" + System.currentTimeMillis();

            RSAKeyPairTemplate destRSAUnwrapperKeyTemplate = new RSAKeyPairTemplate(wrapperSessionKeyPairLabel, new RSAKeyGenParameterSpec(2048, null));
            destRSAUnwrapperKeyTemplate.getAsWrapperTemplate();
            KeySpec publicKeySpec = createKeyPair(sid, destRSAUnwrapperKeyTemplate);
            deleteLabelList.add(wrapperSessionKeyPairLabel);

            PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);

            javax.crypto.SecretKey aesKey = KeyUtil.generateSecretKey(CipherAlg.AES256_CBC, 256);
            BufferedCipher publicKeyAesWrapper = Crypto.getEncryptor(CipherAlg.RSA_ECB_PKCS1);
            publicKeyAesWrapper.init(publicKey, null);
            byte[] wrappedSymetricKey = publicKeyAesWrapper.doFinal(aesKey.getEncoded());

            String unwrapperSymetricKeyLabel = "wrapper_aes_" + System.currentTimeMillis();
            SecretKeyTemplate unwrappedAesKeyTemplate = new AESKeyTemplate(unwrapperSymetricKeyLabel, 32);
            unwrappedAesKeyTemplate.getAsUnwrapperTemplate();
            if (getCardType() == CardType.NCIPHER) {
                unwrappedAesKeyTemplate.add(new CK_ATTRIBUTE(KEYMANAGER_CKA_DECRYPT, true));
            }

            CK_MECHANISM rsaUnwrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);
            unwrapKey(sid, rsaUnwrapMechanism, destRSAUnwrapperKeyTemplate.getPrivateKeyTemplate(), wrappedSymetricKey, unwrappedAesKeyTemplate);

            deleteLabelList.add(unwrapperSymetricKeyLabel);

            return new Pair<byte[], SecretKeyTemplate>(aesKey.getEncoded(), unwrappedAesKeyTemplate);
        }
        catch(IOException ex)
        {
            throw new SmartCardException(ex);
        }

    }
}
