package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import gnu.crypto.key.ecdsa.ECDSAPrivateKey;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8.EPrivateKeyInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Properties;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * Created by orcun.ertugrul on 11-Oct-17.
 */
public class DirakHSMOps extends PKCS11Ops
{
    static final public int MAX_CHUNK_SIZE = 65536;

    private Logger logger = LoggerFactory.getLogger(DirakHSMOps.class);

    public DirakHSMOps()
    {
        super(CardType.DIRAKHSM);
    }


    @Override
    CK_C_INITIALIZE_ARGS get_CK_C_INITIALIZE_ARGS()
    {
        boolean multiThread = isMultiThread();

        if(multiThread == true)
        {
            CK_C_INITIALIZE_ARGS args = new CK_C_INITIALIZE_ARGS();
            args.flags = CKF_OS_LOCKING_OK;
            return args;
        }
        else
        {
            return null;
        }

    }

    private boolean isMultiThread()
    {
        String configFilePath = null;
        try
        {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.indexOf("windows") >= 0)
            {
                String programDataFolder = System.getenv("ProgramData");
                configFilePath = programDataFolder + "\\TUBITAK\\BILGEM\\Dirak\\bilgemHsm.conf";
            }
            else //linux
            {
                configFilePath = "/etc/bilgemHsm/conf/bilgemHsm.conf";
            }

            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(configFilePath);
            properties.load(fis);
            fis.close();
            String multithreadStr = properties.getProperty("multithread");

            if(multithreadStr == null)
                return false;

            boolean multiThread = Boolean.parseBoolean(multithreadStr);

            return multiThread;
        }
        catch (Exception ex)
        {
            logger.error("Error while reading Dirak conf file at " + configFilePath, ex);
            return false;
        }
    }


    //Aşağıda wrap'li fonksiyon var.
    public byte[] generateRSAPrivateKey(long aSessionID, int keySize) throws ESYAException{

        String keyLabel = "temp_rsa_key_for_key_generation_" + StringUtil.toHexString(RandomUtil.generateRandom(8));
        long[] keyIDs;

        try {

            RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, new RSAKeyGenParameterSpec(keySize, null));

            rsaKeyPairTemplate.getAsExtractableTemplate();

            keyIDs = createKeyPairWithOutRead(aSessionID, rsaKeyPairTemplate);

        }catch (Exception ex){
            throw new ESYAException("Error creation temp RSA key at Dirak HSM", ex);
        }

        boolean  exceptionOccured = false;

        try {
            byte [] keyBytes = readRSAPrivateKeyBytes(aSessionID, keyIDs[1]);

            //Eski kullanımda "ERSAPrivateKey" tipinde veri istediği için aşağıdaki gibi bir dönüşüm yapıldı.
            EPrivateKeyInfo privateKeyInfo = new EPrivateKeyInfo(keyBytes);
            return privateKeyInfo.getPrivateKey();
        }catch (Exception ex){
            exceptionOccured = true;
            throw new ESYAException("Error reading temp RSA key at Dirak HSM", ex);
        }
        finally {
            try {
                mPKCS11.C_DestroyObject(aSessionID, keyIDs[0]);
                mPKCS11.C_DestroyObject(aSessionID, keyIDs[1]);
            }
            catch (Exception ex){
                if(exceptionOccured == false)
                    throw new ESYAException("Error deleting temp RSA key at Dirak HSM", ex);
            }
        }
    }

    private byte[] readRSAPrivateKeyBytes(long aSessionID, long keyID) throws PKCS11Exception, SmartCardException, CryptoException {
        String wrapperKeyLabel = "tempWrapperKey_" + System.currentTimeMillis();
        byte[] wrapperKeyBytes = getRandomData(aSessionID, 32);

        AESKeyTemplate wrapperKey = new AESKeyTemplate(wrapperKeyLabel, wrapperKeyBytes);
        wrapperKey.getAsWrapperTemplate().getAsUnwrapperTemplate();

        long wrapperKeyId = 0;
        try {
            wrapperKeyId = importSecretKey(aSessionID, wrapperKey);

            byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM mechanism = new CK_MECHANISM(CKM_AES_CBC_PAD);
            mechanism.pParameter = wrapIV;

            byte[] wrappedBytes = mPKCS11.C_WrapKey(aSessionID, mechanism, wrapperKeyId, keyID);

            byte[] decrypt = CipherUtil.decrypt(CipherAlg.AES256_CBC, new ParamsWithIV(wrapIV), wrappedBytes, wrapperKeyBytes);
            return decrypt;

        } finally {
            if (wrapperKeyId != 0) {
                mPKCS11.C_DestroyObject(aSessionID, wrapperKeyId);
            }
        }
    }

    public KeyPair generateECKeyPair(long aSessionID, ECParameterSpec ecParameterSpec) throws ESYAException {
        //Keys will be deleted after session closed. so isToken false
        String keyLabel = "temp_ec_key_for_key_generation_" + StringUtil.toHexString(RandomUtil.generateRandom(8));
        ECPublicKeySpec ecPublicKeySpec = null;
        long[] keyIDs;
        try {

            ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec);

            ecKeyPairTemplate.getAsExtractableTemplate();

            keyIDs = createKeyPairWithOutRead(aSessionID, ecKeyPairTemplate);

            ecPublicKeySpec = _readECPublicKeySpec(aSessionID, keyIDs[0]);

        }catch (Exception ex){
            throw new ESYAException("Error creation temp EC key at Dirak HSM", ex);
        }


        boolean  exceptionOccured = false;
        try {
            byte [] dValueBytes =  readECPrivateKeyBytes(aSessionID, keyIDs[1]);
            BigInteger dValue = new BigInteger(dValueBytes);

            ECDomainParameter domainParameter = ECDomainParameter.getInstance(ecParameterSpec);
            PublicKey publicKey = KeyUtil.generatePublicKey(ecPublicKeySpec);
            ECDSAPrivateKey privateKey = new ECDSAPrivateKey(domainParameter, dValue);

            KeyPair ecdsaKeyPair = new KeyPair(publicKey, privateKey);

            return ecdsaKeyPair;

        }catch (Exception ex){
            exceptionOccured = true;
            throw new ESYAException("Error reading temp EC key at Dirak HSM", ex);
        }
        finally {
            try {
                mPKCS11.C_DestroyObject(aSessionID, keyIDs[0]);
                mPKCS11.C_DestroyObject(aSessionID, keyIDs[1]);
            }
            catch (Exception ex){
                if(exceptionOccured == false)
                    throw new ESYAException("Error deleting temp EC key at Dirak HSM", ex);
            }
        }
    }

    private byte[] readECPrivateKeyBytes(long aSessionID, long keyID) throws PKCS11Exception, SmartCardException, CryptoException {
        String wrapperKeyLabel = "tempWrapperKey_" + System.currentTimeMillis();
        byte[] wrapperKeyBytes = getRandomData(aSessionID, 32);

        AESKeyTemplate wrapperKey = new AESKeyTemplate(wrapperKeyLabel, wrapperKeyBytes);
        wrapperKey.getAsWrapperTemplate().getAsUnwrapperTemplate();

        long wrapperKeyId = 0;
        try {
            wrapperKeyId = importSecretKey(aSessionID, wrapperKey);

            byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM mechanism = new CK_MECHANISM(CKM_AES_CBC_PAD);
            mechanism.pParameter = wrapIV;

            byte[] wrappedBytes = mPKCS11.C_WrapKey(aSessionID, mechanism, wrapperKeyId, keyID);

            byte[] decrypt = CipherUtil.decrypt(CipherAlg.AES256_CBC, new ParamsWithIV(wrapIV), wrappedBytes, wrapperKeyBytes);
            return decrypt;

        } finally {
            if (wrapperKeyId != 0) {
                mPKCS11.C_DestroyObject(aSessionID, wrapperKeyId);
            }
        }
    }

    @Override
    public byte[] unwrapAndOP(
        long sessionID,
        CK_MECHANISM unwrapMechanism,
        long unwrapperKeyID,
        byte[] wrappedKey,
        CK_ATTRIBUTE[] unwrapTemplate,
        DirakLibOps.CryptoOperation operation,
        CK_MECHANISM operationMechanism,
        byte[] operationData
    ) throws SmartCardException {
        return DirakLibOps.unwrapAndOP(
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

    @Override
    public int getMaxChunkSize() {
        return MAX_CHUNK_SIZE; // 1024 * 64
    }
}
