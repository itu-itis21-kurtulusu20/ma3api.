package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import gnu.crypto.key.ecdsa.ECDSAKeyPairX509Codec;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.asn.scencryptedpackage.ESCObject;
import tr.gov.tubitak.uekae.esya.api.asn.scencryptedpackage.ESCObjectAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.scencryptedpackage.ESCObjectBag;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.DerValue;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.ArrayUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteConversionUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.ObjectResult;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.UnwrapObjectsResults;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.WrappedObjectsWithAttributes;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.util.SCObjectUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ExceptionFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11Names;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.ECParameters;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.SecretKey;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.util.OpsUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template.ICardTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.AttributeUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECUtil;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;
import static tr.gov.tubitak.uekae.esya.api.smartcard.util.Constants.*;

public class PKCS11Ops implements IPKCS11Ops
{
    private Logger logger = LoggerFactory.getLogger(PKCS11Ops.class);

    protected PKCS11LibOps pkcs11LibOps;
    protected PKCS11 mPKCS11;
    private CardType mCardType;
    protected MessageDigest OZET_ALICI;
    protected ESYAPKCS11 mESYAPKCS11 = null;


    public PKCS11Ops(CardType aKartTip)
    {
        try
        {
            OZET_ALICI = MessageDigest.getInstance("SHA-1");
        }
		catch (NoSuchAlgorithmException e)
        {
            logger.error("Error in PKCS11Ops", e);
            throw new RuntimeException(e);
        }
        mCardType = aKartTip;
    }

    public void initialize()
            throws PKCS11Exception,IOException
    {
        try
        {
            CK_C_INITIALIZE_ARGS args = get_CK_C_INITIALIZE_ARGS();
            String libPath = OpsUtil.getLibPath(mCardType.getLibName());

            mPKCS11 = PKCS11.getInstance(libPath, "C_GetFunctionList", args, false);
        }
        catch(Exception ex)
        {
            throw new IOException("PKCS11 wrapper objesi olusturulamadi", ex);
        }
    }

    CK_C_INITIALIZE_ARGS get_CK_C_INITIALIZE_ARGS() {
        return null;
    }

    private PKCS11LibOps getPKCS11LibOps(){
        if(pkcs11LibOps == null){
            return new PKCS11LibOps(mCardType);
        }
        return pkcs11LibOps;
    }

    public long[] getTokenPresentSlotList()
            throws PKCS11Exception
    {
        return mPKCS11.C_GetSlotList(true);
    }

    /**
     * Returns representable slot and information lists
     */
    public List<Pair<Long, String>> getTokenPresentSlotListWithDescription() throws PKCS11Exception {
        long[] tokenPresentSlotList = getTokenPresentSlotList();
        List<Pair<Long, String>> pairList = new ArrayList<Pair<Long, String>>();
        for (long slotId : tokenPresentSlotList) {
            String label = "";
            try {
                label += " " + new String(getSlotInfo(slotId).slotDescription).trim();
            } catch (Exception e) {
                logger.info("Slot Bilgisi Alırken Hata:" + e.getMessage(), e);
                // ignore
            }
            try {
                label += " " + new String(getTokenInfo(slotId).label).trim().split("\u0000")[0];
            } catch (Exception e) {
                logger.info("Slot Bilgisi Alırken Hata:" + e.getMessage(), e);
                // ignore
            }
            pairList.add(new Pair<Long, String>(slotId, label));
        }
        return pairList;
    }

    public long[] getSlotList()
            throws PKCS11Exception
    {
        return mPKCS11.C_GetSlotList(false);
    }

    public CK_SLOT_INFO getSlotInfo(long aSlotID)
            throws PKCS11Exception
    {
        return mPKCS11.C_GetSlotInfo(aSlotID);
    }

    public CK_SESSION_INFO getSessionInfo(long aSessionID)
            throws PKCS11Exception
    {
        return mPKCS11.C_GetSessionInfo(aSessionID);
    }

    public boolean isTokenPresent(long aSlotID)
            throws PKCS11Exception
    {
        CK_SLOT_INFO slotInfo = mPKCS11.C_GetSlotInfo(aSlotID);
        long flags = slotInfo.flags;
        if((flags & CKF_TOKEN_PRESENT)!= 0)
            return true;
        return false;
    }

    public CK_TOKEN_INFO getTokenInfo(long aSlotID)
            throws PKCS11Exception
    {
        return mPKCS11.C_GetTokenInfo(aSlotID);
    }

    public long[] getMechanismList(long aSlotID)
            throws PKCS11Exception
    {
        return mPKCS11.C_GetMechanismList(aSlotID);
    }

    public long openSession(long aSlotID)
            throws PKCS11Exception
    {
        return mPKCS11.C_OpenSession(aSlotID, CKF_SERIAL_SESSION | CKF_RW_SESSION, null, null);
    }

    public void closeSession(long aSessionID)
            throws PKCS11Exception
    {
        mPKCS11.C_CloseSession(aSessionID);
    }

    /*
     * normal user login islemi
     */
    public void login(long aSessionID, String aCardPIN)
            throws PKCS11Exception
    {
        mPKCS11.C_Login(aSessionID, CKU_USER, aCardPIN.toCharArray());
    }

    public void logout(long aSessionID)
            throws PKCS11Exception
    {
        mPKCS11.C_Logout(aSessionID);
    }

    /*
     * kartta private alanda arama icin, login olunmasi gerekir
     */
    public boolean isAnyObjectExist(long aSessionID)
            throws PKCS11Exception
    {
        CK_ATTRIBUTE[] template = {new CK_ATTRIBUTE(CKA_TOKEN, true)};

        long[] bulunanObjeler = objeAra(aSessionID, template);
        if(bulunanObjeler.length == 0)
            return false;
        return true;
    }

    /*
     * akis de karta sertifika yazimi icin login olunmasi gerekir.
     */
    /*
     * Butun kart tipleri icin ayni sablon.Sablon esyajni ile ayni.
     */
    public void importCertificate(long aSessionID, String aCertLabel, X509Certificate aSertifika)
            throws PKCS11Exception
    {
        List<CK_ATTRIBUTE> template = mCardType.getCardTemplate().getCertificateTemplate(aCertLabel, aSertifika);
        mPKCS11.C_CreateObject(aSessionID, template.toArray(new CK_ATTRIBUTE[0]));
    }

    /*
     * login olunmasi gerekir.
     */
    /*
     * sablonlar esyajni ile ayni yapildi.safesign ve akis icin ayri sablon var.
     */
    private long[] _createRSAKeyPair(long aSessionID, String aKeyLabel, RSAKeyGenParameterSpec aKeySpec, boolean aIsSign, boolean aIsEncrypt)
            throws PKCS11Exception
    {
        CK_MECHANISM mech = new CK_MECHANISM(0L);
        mech.mechanism = CKM_RSA_X9_31_KEY_PAIR_GEN;
        mech.pParameter = null;

        if (this.mCardType == CardType.AKIS || this.mCardType == CardType.ATIKHSM ||
            this.mCardType == CardType.DIRAKHSM || this.mCardType == CardType.OPENDNSSOFTHSM ||
            this.mCardType == CardType.PROCENNEHSM) {
            mech.mechanism = CKM_RSA_PKCS_KEY_PAIR_GEN;
        }

        int modulusBits = aKeySpec.getKeysize();
        BigInteger publicExponent = aKeySpec.getPublicExponent();
        if (publicExponent == null)
            publicExponent = RSAKeyGenParameterSpec.F4;

        ICardTemplate kartBilgi = mCardType.getCardTemplate();

        CK_ATTRIBUTE[] pubKeyTemplate = kartBilgi.getRSAPublicKeyCreateTemplate(aKeyLabel, modulusBits, publicExponent, aIsSign, aIsEncrypt).toArray(new CK_ATTRIBUTE[0]);
        CK_ATTRIBUTE[] priKeyTemplate = kartBilgi.getRSAPrivateKeyCreateTemplate(aKeyLabel, aIsSign, aIsEncrypt).toArray(new CK_ATTRIBUTE[0]);

        long[] objectHandles = mPKCS11.C_GenerateKeyPair(aSessionID, mech, pubKeyTemplate, priKeyTemplate);

        updateRSAKeyIds(aSessionID, aKeyLabel);

        return objectHandles;
    }

    @Deprecated
    public long[] createKeyPair(long aSessionID, String aKeyLabel, AlgorithmParameterSpec aParamSpec, boolean aIsSign, boolean aIsEncrypt)
            throws PKCS11Exception,SmartCardException,IOException
    {
        if(aParamSpec instanceof RSAKeyGenParameterSpec)
        {
            return _createRSAKeyPair(aSessionID, aKeyLabel, (RSAKeyGenParameterSpec) aParamSpec, aIsSign, aIsEncrypt);
        }
        else if(aParamSpec instanceof ECParameterSpec)
        {
            return _createECKeyPair(aSessionID, aKeyLabel, (ECParameterSpec) aParamSpec, aIsSign, aIsEncrypt);
        }
        else if(aParamSpec instanceof ECGenParameterSpec)
        {
            ECGenParameterSpec genspec = (ECGenParameterSpec) aParamSpec;
            ECParameterSpec spec = NamedCurve.getECParameterSpec(genspec.getName());
            if (spec == null)
                throw new SmartCardException("Verilen AlgorithmParameterSpec desteklenmiyor");
            return _createECKeyPair(aSessionID, aKeyLabel, spec, aIsSign, aIsEncrypt);
        }
        else
            throw new SmartCardException("Verilen AlgorithmParameterSpec desteklenmiyor");
    }

    private long[] _createECKeyPair(long aSessionID, String aKeyLabel, ECParameterSpec aECSpec, boolean aIsSign, boolean aIsEncrypt)
        throws PKCS11Exception, IOException {
        CK_MECHANISM mech = new CK_MECHANISM(0L);
        mech.mechanism = CKM_ECDSA_KEY_PAIR_GEN;
        mech.pParameter = null;

        byte[] paramsencoded = ECParameters.encodeParameters(aECSpec);

        CK_ATTRIBUTE[] pubKeyTemplate = new CK_ATTRIBUTE[]{
            new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, false),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, CKO_PUBLIC_KEY),
            new CK_ATTRIBUTE(CKA_ENCRYPT, aIsEncrypt),
            new CK_ATTRIBUTE(CKA_VERIFY, aIsSign),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_PARAMS, paramsencoded),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, aKeyLabel),
        };

        CK_ATTRIBUTE[] priKeyTemplate = new CK_ATTRIBUTE[]{
            new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, CKO_PRIVATE_KEY),
            new CK_ATTRIBUTE(CKA_DECRYPT, aIsEncrypt),
            new CK_ATTRIBUTE(CKA_SIGN, aIsSign),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, aKeyLabel),
        };

        long[] objectHandles = mPKCS11.C_GenerateKeyPair(aSessionID, mech, pubKeyTemplate, priKeyTemplate);

        // find public key and get point value to calculate id
        CK_ATTRIBUTE[] pubTemplate =
            {
                new CK_ATTRIBUTE(CKA_LABEL, aKeyLabel),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, CKO_PUBLIC_KEY)
            };

        long[] pubKeyIDs = objeAra(aSessionID, pubTemplate);

        CK_ATTRIBUTE[] ecPropTemplate =
            {
                new CK_ATTRIBUTE(CKA_EC_POINT)
            };

        mPKCS11.C_GetAttributeValue(aSessionID, pubKeyIDs[0], ecPropTemplate);

        byte[] point = (byte[]) ecPropTemplate[0].pValue;

        byte[] spkey = mCardType.getCardTemplate().getPointValue(point);

        CK_ATTRIBUTE[] labelTemplate = {new CK_ATTRIBUTE(CKA_LABEL, aKeyLabel)};

        long[] keyIDs = objeAra(aSessionID, labelTemplate);

        byte[] id = OZET_ALICI.digest(spkey);

        CK_ATTRIBUTE[] idTemplate = {new CK_ATTRIBUTE(CKA_ID, id)};

        mPKCS11.C_SetAttributeValue(aSessionID, keyIDs[0], idTemplate);
        mPKCS11.C_SetAttributeValue(aSessionID, keyIDs[1], idTemplate);

        return objectHandles;
    }

    public long createSecretKey(long aSessionID, SecretKey aKey)
            throws PKCS11Exception
    {
        List<CK_ATTRIBUTE> template = mCardType.getCardTemplate().getSecretKeyCreateTemplate(aKey);

        CK_MECHANISM mech = new CK_MECHANISM(0L);
        mech.mechanism = aKey.getGenerationMechanism();
        mech.pParameter = null;

        return mPKCS11.C_GenerateKey(aSessionID, mech, template.toArray(new CK_ATTRIBUTE[0]));
    }

    public long importSecretKey(long aSessionId, SecretKey aKey)
            throws PKCS11Exception
    {
        List<CK_ATTRIBUTE> template = mCardType.getCardTemplate().getSecretKeyImportTemplate(aKey);
        return mPKCS11.C_CreateObject(aSessionId, template.toArray(new CK_ATTRIBUTE[0]));
    }

    public long importSecretKey(long aSessionId, SecretKeyTemplate aKeyTemplate)
            throws  PKCS11Exception,SmartCardException
    {
        mCardType.getCardTemplate().applyTemplate(aKeyTemplate);
        return mPKCS11.C_CreateObject(aSessionId, aKeyTemplate.getAttributesAsArr());
    }




    public byte[] signDataWithKeyID(long aSessionID,long aKeyID,CK_MECHANISM aMechanism,byte[] aImzalanacak) throws PKCS11Exception
    {
        mPKCS11.C_SignInit(aSessionID, aMechanism, aKeyID);

        byte[] imzali = mPKCS11.C_Sign(aSessionID, aImzalanacak);
        return imzali;
    }

    /*
     * akisde serial number ile ilgili sorun var(serial number in BigInteger halinin String radix 16 a cevrilmis halini buluyor).
     * datakey de dkck232 ile calismiyo, dkck201 ile calisiyo
     */
    /*
     * login olunmasi gerekir.
     */
    /*
     * esyajni ile ayni.
     */
    public byte[] signDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, CK_MECHANISM aMechanism, byte[] aImzalanacak)
            throws PKCS11Exception,SmartCardException
    {

        byte[] id = _getCertificateId(aSessionID, aSerialNumber);

        CK_ATTRIBUTE[] privateKeyTemplate =
            {
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(CKA_ID, id),
                new CK_ATTRIBUTE(CKA_PRIVATE, true),
                new CK_ATTRIBUTE(CKA_TOKEN, true)
            };

        long[] keyList = objeAra(aSessionID, privateKeyTemplate);

        if(keyList.length == 0)
        {
            throw new SmartCardException("Verilen seri numarasina sahip sertifikayla ayni ID ye sahip ozel anahtar kartta bulunamadi.");
        }

        mPKCS11.C_SignInit(aSessionID, aMechanism, keyList[0]);

        byte[] imzali = mPKCS11.C_Sign(aSessionID, aImzalanacak);
        return imzali;
    }

    private byte[] _getCertificateId(long aSessionID, byte[] aSerialNumber)
            throws PKCS11Exception,SmartCardException
    {
        List<List<CK_ATTRIBUTE>> list = mCardType.getCardTemplate().getCertSerialNumberTemplates(aSerialNumber);

        long[] objectList = null;

        for(List<CK_ATTRIBUTE> tList:list)
        {
            objectList = objeAra(aSessionID, tList.toArray(new CK_ATTRIBUTE[0]));
            if (objectList.length > 0) break;
        }

        if(objectList == null || objectList.length == 0)
        {
            throw new SmartCardException("Verilen seri numarali sertifika kartta bulunamadi.");
        }

        CK_ATTRIBUTE[] idTemplate = {new CK_ATTRIBUTE(CKA_ID)};

        mPKCS11.C_GetAttributeValue(aSessionID, objectList[0], idTemplate);
        byte[] id = (byte[])idTemplate[0].pValue;
        return id;
    }

    /*
     * login olunmasi gerekir.
     */
    /*
     * esyajni ile ayni.
     */

    public byte[] decryptDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, CK_MECHANISM aMechanism, byte[] aData)
            throws PKCS11Exception,SmartCardException
    {
        byte[] cozulecek = fixLengthForDecryption(aMechanism.mechanism, aData);

        byte[] id = getObjectIdWithCertSerialNumber(aSessionID, aSerialNumber);

        CK_ATTRIBUTE[] privateKeyTemplate =
            {
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(CKA_ID, id),
                new CK_ATTRIBUTE(CKA_PRIVATE, true),
                new CK_ATTRIBUTE(CKA_TOKEN, true)
            };

        long[] keyList = objeAra(aSessionID, privateKeyTemplate);

        if(keyList.length == 0)
        {
            throw new SmartCardException("Verilen seri numarasina sahip sertifikayla ayni ID ye sahip ozel anahtar kartta bulunamadi.");
        }

        byte[] output = new byte[1024];

        mPKCS11.C_DecryptInit(aSessionID, aMechanism, keyList[0]);

        int length = C_Decrypt(aSessionID, cozulecek, 0, cozulecek.length, output, 0, output.length);

        byte[] sonuc = new byte[length];

        System.arraycopy(output, 0, sonuc, 0, length);

        return sonuc;
    }

    private int C_Decrypt(long session, byte[] encrypted, int encryptedIndex, int encryptedLen, byte[] decryptedBytes, int decryptedIndex, int decryptedLen)
        throws SmartCardException {
        try {
            return mPKCS11.C_Decrypt(session, 0, encrypted, encryptedIndex, encryptedLen, 0, decryptedBytes, decryptedIndex, decryptedLen);
        }
        catch (PKCS11Exception ex){
            throw new SmartCardException(ex);
        }

    }

    private byte[] getObjectIdWithCertSerialNumber(long aSessionID, byte[] aSerialNumber) throws PKCS11Exception, SmartCardException {
        List<List<CK_ATTRIBUTE>> list = mCardType.getCardTemplate().getCertSerialNumberTemplates(aSerialNumber);

        long[] objectList = null;

        for (List<CK_ATTRIBUTE> tList : list) {
            objectList = objeAra(aSessionID, tList.toArray(new CK_ATTRIBUTE[0]));
            if (objectList.length > 0) break;
        }

        if (objectList == null || objectList.length == 0) {
            throw new SmartCardException("Verilen seri numarali sertifika kartta bulunamadi.");
        }

        CK_ATTRIBUTE[] idTemplate = {new CK_ATTRIBUTE(CKA_ID)};

        mPKCS11.C_GetAttributeValue(aSessionID, objectList[0], idTemplate);
        return (byte[]) idTemplate[0].pValue;
    }

    /**
     * keyusage da digitalSignature alaninin set edilmis olmasina bakar.
     */
    public List<byte[]> getSignatureCertificates(long aSessionID)
            throws PKCS11Exception,SmartCardException
    {
        return getSignatureEncryptionCertificates(aSessionID, true);
    }

    /**
     * keyusage da keyEncipherment alaninin set edilmis olmasina bakar.
     */
    public List<byte[]> getEncryptionCertificates(long aSessionID)
            throws PKCS11Exception,SmartCardException
    {
        return getSignatureEncryptionCertificates(aSessionID, false);
    }

    private List<byte[]> getSignatureEncryptionCertificates(long aSessionID, boolean aSign)
            throws PKCS11Exception,SmartCardException
    {
        List<byte[]> certificates = getCertificates(aSessionID);
        List<byte[]> filteredCerts = new ArrayList<byte[]>();

        for(byte [] certificateBytes : certificates)
        {
            X509Certificate cert = null;
            try
            {
                InputStream inStream = new ByteArrayInputStream(certificateBytes);
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                cert = (X509Certificate) cf.generateCertificate(inStream);
                inStream.close();
            }
            catch(Exception aEx)
            {
                throw new SmartCardException("Sertifika encode edilirken hata olustu. Cert Bytes: " + StringUtil.toHexString(certificateBytes), aEx);
            }

            boolean[] ku = cert.getKeyUsage();
            if(ku != null)
            {
                if(aSign)
                {
                    if (ku[KEYUSAGE_DIGITALSIGNATURE])
                        filteredCerts.add(certificateBytes);
                }
                else
                {
                    if (ku[KEYUSAGE_DATAENCIPHER] || ku[KEYUSAGE_KEYENCIPHER])
                        filteredCerts.add(certificateBytes);
                }
            }
        }
        return filteredCerts;
    }

    public List<byte[]> getCertificates(long aSessionID)
        throws PKCS11Exception {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_CLASS, CKO_CERTIFICATE)
            };

        long[] objectList = objeAra(aSessionID, template);

        List<byte[]> certs = new ArrayList<byte[]>();

        CK_ATTRIBUTE[] values = {new CK_ATTRIBUTE(CKA_VALUE)};

        for (long handle : objectList) {
            mPKCS11.C_GetAttributeValue(aSessionID, handle, values);
            byte[] value = (byte[]) values[0].pValue;
            certs.add(value);
        }

        return certs;
    }

    public String[] getSignatureKeyLabels(long aSessionID)
            throws PKCS11Exception {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(CKA_SIGN, true)
            };

        return getLabels(aSessionID, template);
    }

    public String[] getEncryptionKeyLabels(long aSessionID)
            throws PKCS11Exception {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PUBLIC_KEY),
                new CK_ATTRIBUTE(CKA_ENCRYPT, true)
            };

        return getLabels(aSessionID, template);
    }

    public String[] getWrapperKeyLabels(long aSessionID)
            throws PKCS11Exception {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_WRAP, true)
            };

        return getLabels(aSessionID, template);
    }

    public String[] getUnwrapperKeyLabels(long aSessionID)
            throws PKCS11Exception {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_UNWRAP, true)
            };

        return getLabels(aSessionID, template);
    }

    public String[] getLabels(long aSessionID, CK_ATTRIBUTE[] aTemplate)
        throws PKCS11Exception {
        long[] objectList = objeAra(aSessionID, aTemplate);

        List<String> labels = new ArrayList<String>();

        CK_ATTRIBUTE[] values = {new CK_ATTRIBUTE(CKA_LABEL)};

        for(long handle:objectList)
        {
            mPKCS11.C_GetAttributeValue(aSessionID, handle, values);
            String label = AttributeUtil.getStringValue(values[0].pValue);
            labels.add(label);
        }
        return labels.toArray(new String[0]);
    }

    /*
     * kartta private alanda arama icin, login olunmasi gerekir
     */
    public boolean isObjectExist(long aSessionID, String aLabel)
            throws PKCS11Exception
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_LABEL, aLabel)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length > 0)
            return true;
        return false;
    }

    private boolean _isObjectExist(long aSessionID, String aLabel, long aDataType)
            throws PKCS11Exception
    {
        CK_ATTRIBUTE[] template =
            {
             // new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_LABEL, aLabel),
                new CK_ATTRIBUTE(CKA_CLASS, aDataType)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length > 0)
            return true;
        return false;
    }

    private void _writeData(long aSessionID, String aLabel, byte[] aData, boolean aIsPrivate)
            throws PKCS11Exception
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_CLASS, CKO_DATA),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_LABEL, aLabel),
                new CK_ATTRIBUTE(CKA_VALUE, aData),
                new CK_ATTRIBUTE(CKA_PRIVATE, aIsPrivate)
            };

        mPKCS11.C_CreateObject(aSessionID, template);
    }

    /*
     * login olunmasi gerekir. CKO_DATA tipinde nesne yazar.
     */
    public void writePrivateData(long aSessionID, String aLabel, byte[] aData)
            throws PKCS11Exception
    {
        _writeData(aSessionID, aLabel, aData, true);
    }

    /*
     * akisde login olunmasi gerekir. CKO_DATA tipinde nesne yazar.
     */
    public void writePublicData(long aSessionID, String aLabel, byte[] aData)
            throws PKCS11Exception
    {
        _writeData(aSessionID, aLabel, aData, false);
    }

    protected List<byte[]> _readData(long aSessionID, String aLabel, boolean aIsPrivate, long aDataType)
            throws PKCS11Exception,SmartCardException
    {
        return _readData(aSessionID, aLabel, aIsPrivate, true, aDataType);
    }

    protected List<byte[]> _readData(long aSessionID, String aLabel, boolean aIsPrivate, boolean aIstoken, long aDataType)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_CLASS, aDataType),
                new CK_ATTRIBUTE(CKA_TOKEN, aIstoken),
                new CK_ATTRIBUTE(CKA_PRIVATE, aIsPrivate),
                new CK_ATTRIBUTE(CKA_LABEL, aLabel),
            };

        long[] objectList = objeAra(aSessionID, template);
        int objectCount = objectList.length;

        if(objectCount == 0)
        {
            throw new SmartCardException(aLabel + " isimli nesne kartta bulunamadi.");
        }

        CK_ATTRIBUTE[] valueTemplate =
            {
                new CK_ATTRIBUTE(CKA_VALUE)
            };

        List<byte[]> objectValues = new ArrayList<byte[]>();

        for(int i=0;i<objectCount;i++)
        {
            mPKCS11.C_GetAttributeValue(aSessionID, objectList[i], valueTemplate);
            byte[] data = (byte[]) valueTemplate[0].pValue;
            objectValues.add(data);
        }

        return objectValues;
    }

    /*
     * login olunmasi gerekir.CKO_DATA tipindeki nesneleri okur.
     */
    public List<byte[]> readPrivateData(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException
    {
        return _readData(aSessionID, aLabel, true, CKO_DATA);
    }

    /*
     * CKO_DATA tipindeki nesneleri okur.
     */
    public List<byte[]> readPublicData(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException
    {
        return _readData(aSessionID, aLabel, false, CKO_DATA);
    }

    public boolean isPrivateKeyExist(long aSessionID, String aLabel)
            throws PKCS11Exception
    {
        return _isObjectExist(aSessionID, aLabel, CKO_PRIVATE_KEY);
    }

    public boolean isPublicKeyExist(long aSessionID, String aLabel)
            throws PKCS11Exception
    {
        return _isObjectExist(aSessionID, aLabel, CKO_PUBLIC_KEY);
    }

    public boolean isCertificateExist(long aSessionID, String aLabel)
            throws PKCS11Exception
    {
        return _isObjectExist(aSessionID, aLabel, CKO_CERTIFICATE);
    }

    public List<byte[]> readCertificate(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException
    {
        return _readData(aSessionID, aLabel, false, CKO_CERTIFICATE);
    }

    public byte[] readCertificate(long aSessionID, byte[] aCertSerialNo)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_CLASS, CKO_CERTIFICATE),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_PRIVATE, false),
                new CK_ATTRIBUTE(CKA_SERIAL_NUMBER, aCertSerialNo)
            };

        long[] objectList = objeAra(aSessionID, template);
        if(objectList.length == 0)
        {
            throw new SmartCardException("Kartta bu seri numarali sertifika bulunamadi");
        }

        CK_ATTRIBUTE[] valueTemplate =
            {
                new CK_ATTRIBUTE(CKA_VALUE)
            };

        mPKCS11.C_GetAttributeValue(aSessionID, objectList[0], valueTemplate);

        return (byte[]) valueTemplate[0].pValue;
    }

    public KeySpec readPublicKeySpec(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PUBLIC_KEY),
                // new CK_ATTRIBUTE(CKA_TOKEN, true), // public key might not be token
                new CK_ATTRIBUTE(CKA_LABEL, aLabel)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
        {
            throw new SmartCardException(aLabel + " isimli anahtar kartta yok.");
        }

        return getPublicKeySpec(aSessionID, objectList[0]);
    }

    public KeySpec readPublicKeySpec(long aSessionID, byte[] aCertSerialNo)
            throws SmartCardException,PKCS11Exception
    {
        byte[] ID = _getCertificateId(aSessionID, aCertSerialNo);
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PUBLIC_KEY),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_ID, ID)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
        {
            throw new SmartCardException(StringUtil.toHexString(ID) + " ID'li anahtar kartta yok.");
        }

        return getPublicKeySpec(aSessionID, objectList[0]);
    }

    public long getPrivateKeyObjIDFromCertificateSerial(long aSessionID, byte[] aCertSerialNo)
            throws SmartCardException,PKCS11Exception
    {
        byte[] ID = _getCertificateId(aSessionID, aCertSerialNo);

        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_ID, ID)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
        {
            throw new SmartCardException(StringUtil.toHexString(ID) + " ID'li anahtar kartta yok. Kartta sertifika bulundu; yalnız özel anahtar bulunamadı! CertSerialNo (Hex): " + StringUtil.toHexString(aCertSerialNo));
        }

        return objectList[0];
    }

    public KeySpec getPublicKeySpec(long aSessionID, long aObjectId)
            throws SmartCardException,PKCS11Exception
    {
        CK_ATTRIBUTE[] keyTemplate =
            {
                new CK_ATTRIBUTE(CKA_KEY_TYPE)
            };

        mPKCS11.C_GetAttributeValue(aSessionID, aObjectId, keyTemplate);
        Long keyType = (Long) keyTemplate[0].pValue;


        if(keyType==CKK_RSA)
        {
            return _readRSAPublicKeySpec(aSessionID, aObjectId);
        }
        else if(keyType==CKK_EC || keyType==CKK_ECDSA)
        {
            return _readECPublicKeySpec(aSessionID, aObjectId);
        }
        else
        {
            throw new SmartCardException("Anahtar tipi (%d) desteklenmiyor. Desteklenen algoritmalar RSA ve ECDSA.", keyType);
        }
    }

    private RSAPublicKeySpec _readRSAPublicKeySpec(long aSessionID, long aKeyId)
            throws PKCS11Exception
    {
        CK_ATTRIBUTE[] rsaPublicKeyTemplate =
            {
                new CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT),
                new CK_ATTRIBUTE(CKA_MODULUS)
            };

        mPKCS11.C_GetAttributeValue(aSessionID, aKeyId, rsaPublicKeyTemplate);
        byte[] publicExponent = (byte[]) rsaPublicKeyTemplate[0].pValue;
        byte[] modulus = (byte[]) rsaPublicKeyTemplate[1].pValue;

        BigInteger exp = new BigInteger(1, publicExponent);
        BigInteger mod = new BigInteger(1, modulus);

        return new RSAPublicKeySpec(mod, exp);
    }

    protected ECPublicKeySpec _readECPublicKeySpec(long aSessionID, long objID)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] ecPublicKeyTemplate =
            {
                new CK_ATTRIBUTE(CKA_ECDSA_PARAMS),
                new CK_ATTRIBUTE(CKA_EC_POINT)
            };

        mPKCS11.C_GetAttributeValue(aSessionID, objID, ecPublicKeyTemplate);

        byte[] params = (byte[]) ecPublicKeyTemplate[0].pValue;
        byte[] point = (byte[]) ecPublicKeyTemplate[1].pValue;

        try
        {
            ECParameterSpec ecparamspec = ECParameters.decodeParameters(params);

            byte[] pointValue = mCardType.getCardTemplate().getPointValue(point);

            ECPoint ecpoint = ECParameters.decodePoint(pointValue, ecparamspec.getCurve());
            ECPublicKeySpec ecpubkeyspec = new ECPublicKeySpec(ecpoint, ecparamspec);

            return ecpubkeyspec;
        }
        catch(IOException bEx)
        {
            throw new SmartCardException("EC publickey olusturulurken io hatasi olustu", bEx);
        }
    }

    public void updatePrivateData(long aSessionID, String aLabel, byte[] aValue)
            throws PKCS11Exception,SmartCardException
    {
        _updateData(aSessionID, aLabel, aValue, true);
    }

    public void updatePublicData(long aSessionID, String aLabel, byte[] aValue)
            throws PKCS11Exception,SmartCardException
    {
        _updateData(aSessionID, aLabel, aValue, false);
    }

    private void _updateData(long aSessionID, String aLabel, byte[] aValue, boolean aIsPrivate)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_CLASS, CKO_DATA),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_PRIVATE, aIsPrivate),
                new CK_ATTRIBUTE(CKA_LABEL, aLabel)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
        {
            throw new SmartCardException(aLabel + " isimli nesne kartta bulunamadi.");
        }

        CK_ATTRIBUTE[] valueTemplate =
            {
                new CK_ATTRIBUTE(CKA_VALUE, aValue)
            };

        mPKCS11.C_SetAttributeValue(aSessionID, objectList[0], valueTemplate);
    }

    protected void _deleteObject(long aSessionID, String aLabel, boolean aIsPrivate, boolean aIsToken)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_TOKEN, aIsToken),
                new CK_ATTRIBUTE(CKA_PRIVATE, aIsPrivate),
                new CK_ATTRIBUTE(CKA_LABEL, aLabel)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
        {
            throw new SmartCardException(aLabel + " isimli nesne kartta bulunamadi.");
        }

        for(int i=0 ;i<objectList.length;i++)
        {
            mPKCS11.C_DestroyObject(aSessionID, objectList[i]);
        }
    }

    /**
     * login olunmasi gerekir.
     *
     * @param aLabel     Karttan silinecek nesnenin ismidir.Bu isimdeki nesnenin tipi CKO_DATA, CKO_CERTIFICATE,
     *                   CKO_PUBLIC_KEY, CKO_PRIVATE_KEY, CKO_SECRET_KEY olabilir.
     *                   Kartta bu isimde birden fazla nesne varsa, hepsi silinir.
     */
    public void deletePrivateObject(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException
    {
        _deleteObject(aSessionID, aLabel, true, true);
    }

    public void deleteObject(long sessionId, long objectHandle) throws PKCS11Exception {
        mPKCS11.C_DestroyObject(sessionId, objectHandle);
    }

    /**
     * akis de login olunmasi gerekir.
     *
     * @param aLabel     Karttan silinecek nesnenin ismidir.Bu isimdeki nesnenin tipi CKO_DATA, CKO_CERTIFICATE,
     *                   CKO_PUBLIC_KEY, CKO_PRIVATE_KEY, CKO_SECRET_KEY olabilir.
     *                   Kartta bu isimde birden fazla nesne varsa, hepsi silinir.
     */
    public void deletePublicObject(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException
    {
        _deleteObject(aSessionID, aLabel, false, true);
    }


    private void _deleteData(long aSessionID, String aLabel, boolean aIsPrivate)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_CLASS, CKO_DATA),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_PRIVATE, aIsPrivate),
                new CK_ATTRIBUTE(CKA_LABEL, aLabel)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
        {
            throw new SmartCardException(aLabel + " isimli nesne kartta bulunamadi.");
        }

        for(int i=0 ;i<objectList.length;i++)
        {
            mPKCS11.C_DestroyObject(aSessionID, objectList[i]);
        }
    }

    /**
     * login olunmasi gerekir.
     *
     * @param aLabel     Karttan silinecek CKO_DATA tipindeki nesnenin ismidir.
     *                   Kartta bu isimde birden fazla nesne varsa, hepsi silinir.
     */
    public void deletePrivateData(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException
    {
        _deleteData(aSessionID, aLabel, true);
    }

    /**
     * akis de login olunmasi gerekir.
     *
     * @param aLabel     Karttan silinecek CKO_DATA tipindeki nesnenin ismidir.
     *                   Kartta bu isimde birden fazla nesne varsa, hepsi silinir.
     */
    public void deletePublicData(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException
    {
        _deleteData(aSessionID, aLabel, false);
    }

    /*
     * gemplusda login olunmasi gerekiyor.
     */
    public byte[] getRandomData(long aSessionID, int aDataLength)
            throws PKCS11Exception
    {
        byte[] randomData = new byte[aDataLength];
        mPKCS11.C_GenerateRandom(aSessionID, randomData);

        return randomData;
    }

    public byte[] getTokenSerialNo(long aSlotID)
            throws PKCS11Exception
    {
        char[] serialNo = mPKCS11.C_GetTokenInfo(aSlotID).serialNumber;
        String serialNoS = new String(serialNo);
        return serialNoS.trim().getBytes();
    }

    /*
     * login gerekiyor.
     */
    /*
     * datakey de dkck232 de calismiyor, dkck201 de calisiyor.
     */
    public byte[] signData(long aSessionID, String aKeyLabel, byte[] aImzalanacak, CK_MECHANISM aMechanism)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_LABEL, aKeyLabel),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
        {
            template[2] = new CK_ATTRIBUTE(CKA_CLASS, CKO_SECRET_KEY);
            objectList = objeAra(aSessionID, template);
            if (objectList.length == 0)
                throw new SmartCardException(aKeyLabel + " isimli anahtar kartta yok");
        }

        mPKCS11.C_SignInit(aSessionID, aMechanism, objectList[0]);
        byte[] imzali = mPKCS11.C_Sign(aSessionID, aImzalanacak);

        return imzali;
    }


    public byte[] signAndRecoverData(long aSessionID, String aKeyLabel, byte[] aImzalanacak, CK_MECHANISM aMechanism)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_LABEL, aKeyLabel),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
        {
            template[2] = new CK_ATTRIBUTE(CKA_CLASS, CKO_SECRET_KEY);
            objectList = objeAra(aSessionID, template);
            if (objectList.length == 0)
                throw new SmartCardException(aKeyLabel + " isimli anahtar kartta yok");
        }

        mPKCS11.C_SignRecoverInit(aSessionID, aMechanism, objectList[0]);
        byte[] output = new byte[1024];
        int length = mPKCS11.C_SignRecover(aSessionID, aImzalanacak, 0, aImzalanacak.length, output, 0, output.length);
        byte[] sonuc = new byte[length];
        System.arraycopy(output, 0, sonuc, 0, length);

        return sonuc;
    }

    public byte[] verifyAndRecoverData(long aSessionID, String aKeyLabel, byte[] aSignature, CK_MECHANISM aMechanism)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_LABEL, aKeyLabel),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PUBLIC_KEY)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
        {
            template[2] = new CK_ATTRIBUTE(CKA_CLASS, CKO_SECRET_KEY);
            objectList = objeAra(aSessionID, template);
            if (objectList.length == 0)
                throw new SmartCardException(aKeyLabel + " isimli anahtar kartta yok");
        }

        byte[] sonuc = null;

        mPKCS11.C_VerifyRecoverInit(aSessionID, aMechanism, objectList[0]);
        byte[] output = new byte[1024];
        int length = mPKCS11.C_VerifyRecover(aSessionID, aSignature, 0, aSignature.length, output, 0, output.length);
        sonuc = new byte[length];
        System.arraycopy(output, 0, sonuc, 0, length);

        return sonuc;
    }

    public void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aImza, CK_MECHANISM aMechanism)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_LABEL, aKeyLabel),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PUBLIC_KEY)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
        {
            template[2] = new CK_ATTRIBUTE(CKA_CLASS, CKO_SECRET_KEY);
            objectList = objeAra(aSessionID, template);
            if (objectList.length == 0)
                throw new SmartCardException(aKeyLabel + " isimli anahtar kartta yok.");
        }

        verifyData(aSessionID, objectList[0], aData, aImza, aMechanism);
    }

    @Override
    public void verifyData(long aSessionID, long aKeyID, byte[] aData, byte[] aImza, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
        mPKCS11.C_VerifyInit(aSessionID, aMechanism, aKeyID);
        mPKCS11.C_Verify(aSessionID, aData, aImza);
    }

    /*
     * gemplus ve akis icin login gerekiyor.
     */
    public void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aImza, long aMechanism)
            throws PKCS11Exception,SmartCardException
    {
        CK_MECHANISM mech = new CK_MECHANISM(0L);
        mech.mechanism = aMechanism;

        verifyData(aSessionID, aKeyLabel, aData, aImza, mech);
    }

    /*
     * gemplus ve akisde login gerekiyor.
     */
    public byte[] encryptData(long aSessionID, String aKeyLabel, byte[] aData, CK_MECHANISM aMechanism)
            throws PKCS11Exception,SmartCardException
    {
        long keyID = searchKeyToEncryptData(aSessionID, aKeyLabel);
        return encryptData(aSessionID, keyID, aData, aMechanism);
    }

    public long searchKeyToEncryptData(long aSessionID, String aKeyLabel)
            throws PKCS11Exception, SmartCardException {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_LABEL, aKeyLabel),
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_CLASS, CKO_SECRET_KEY)
            };

        long[] objectList = objeAra(aSessionID, template);

        if (objectList.length == 0)  // public key encryption
        {
            template[2] = new CK_ATTRIBUTE(CKA_CLASS, CKO_PUBLIC_KEY);
            objectList = objeAra(aSessionID, template);
            if (objectList.length == 0)
                throw new SmartCardException(aKeyLabel + " isimli anahtar kartta yok");
        }

        return objectList[0];
    }

    /*
     * gemplus ve akisde login gerekiyor.
     */
    @Override
    public byte[] encryptData(long aSessionID, long keyID, byte[] aData, CK_MECHANISM aMechanism)
        throws PKCS11Exception, SmartCardException {
        int max_chunk_size = getMaxChunkSize();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] output = new byte[max_chunk_size + 128]; // Output, işleme giren data'tan uzun olabiliyor.

        mPKCS11.C_EncryptInit(aSessionID, aMechanism, keyID);

        int length;

        if (aData.length < max_chunk_size) {
            length = C_Encrypt(aSessionID, aData, 0, aData.length, output, 0, output.length);
            outputStream.write(output, 0, length);
        } else {
            int remaining = aData.length;
            int index = 0;
            while (remaining > 0) {
                int chunkSize = 0;
                if (remaining > max_chunk_size)
                    chunkSize = max_chunk_size;
                else
                    chunkSize = remaining;

                length = mPKCS11.C_EncryptUpdate(aSessionID, 0, aData, index, chunkSize, 0,
                    output, 0, output.length);
                outputStream.write(output, 0, length);
                remaining = remaining - chunkSize;
                index = index + chunkSize;
            }

            length = mPKCS11.C_EncryptFinal(aSessionID, 0, output, 0, output.length);
            outputStream.write(output, 0, length);
        }

        return outputStream.toByteArray();
    }

    @Override
    public void encryptData(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, InputStream inputStream, OutputStream outputStream) throws PKCS11Exception, SmartCardException, IOException {
        mPKCS11.C_EncryptInit(aSessionID, aMechanism, aKeyID);

        final int max_chunk_size = getMaxChunkSize();
        byte[] inputBuf = new byte[max_chunk_size];
        byte[] outputBuf = new byte[max_chunk_size + 128]; // Output, işleme giren data'tan uzun olabiliyor.

        int encryptedLen;
        int readLen;

        // encrypt
        do {
            readLen = inputStream.read(inputBuf, 0, max_chunk_size);
            if (readLen == -1)
                break;

            encryptedLen = mPKCS11.C_EncryptUpdate(aSessionID, 0, inputBuf, 0, readLen, 0, outputBuf, 0, outputBuf.length);
            outputStream.write(outputBuf, 0, encryptedLen);
        } while (readLen > 0);

        // finalize
        encryptedLen = mPKCS11.C_EncryptFinal(aSessionID, 0, outputBuf, 0, outputBuf.length);
        outputStream.write(outputBuf, 0, encryptedLen);
    }

    public int getMaxChunkSize() {
        return 1024;
    }

    private int C_Encrypt(long session, byte[] data, int dataIndex, int dataLen, byte[] outputBytes, int outputIndex, int outputLen)
        throws SmartCardException, PKCS11Exception {
        try {
            Class<? extends PKCS11> pkcs11Class = mPKCS11.getClass();

            Method[] pkcs11Methods = pkcs11Class.getMethods();

            Method C_EncryptMethod = null;

            for (int i = 0; i < pkcs11Methods.length; i++) {
                if (pkcs11Methods[i].getName().equals("C_Encrypt")) {
                    C_EncryptMethod = pkcs11Methods[i];
                    break;
                }
            }

            if (C_EncryptMethod == null)
                throw new SmartCardException("C_EncryptMethod could not be found");


            if(C_EncryptMethod.isAccessible() == false)
                C_EncryptMethod.setAccessible(true);

            Class<?>[] parameterTypes = C_EncryptMethod.getParameterTypes();
            if (parameterTypes.length == 7) {
                return (Integer) C_EncryptMethod.invoke(mPKCS11, session, data, dataIndex, dataLen, outputBytes, outputIndex, outputLen);
            } else if (parameterTypes.length == 9) {
                return (Integer) C_EncryptMethod.invoke(mPKCS11, session, 0, data, dataIndex, dataLen, 0, outputBytes, outputIndex, outputLen);
            }

            throw new SmartCardException("Convenient C_EncryptMethod could not be found");

        }
        catch (InvocationTargetException ex){
            if (ex.getTargetException() instanceof PKCS11Exception) {
                throw (PKCS11Exception) ex.getTargetException();
            } else {
                throw new SmartCardException("C_EncryptMethod reflection error", ex);
            }
        }
        catch (IllegalAccessException ex){
            throw new SmartCardException("C_EncryptMethod reflection error", ex);
        }
    }

    /*
     * login gerekiyor.
     */
    public byte[] decryptData(long aSessionID, String aKeyLabel, byte[] aData, CK_MECHANISM aMechanism)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_LABEL, aKeyLabel),
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY)
            };

        long[] objectList = objeAra(aSessionID, template);

        if (objectList.length == 0) {
            template[1] = new CK_ATTRIBUTE(CKA_CLASS, CKO_SECRET_KEY);
            objectList = objeAra(aSessionID, template);
            if (objectList.length == 0)
                throw new SmartCardException(aKeyLabel + " isimli anahtar kartta yok");
        }

        long objectID = objectList[0];

        return decryptData(aSessionID, objectID, aData, aMechanism);
    }

    @Override
    public byte[] decryptData(long aSessionID, long aKeyID, byte[] aData, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        mPKCS11.C_DecryptInit(aSessionID, aMechanism, aKeyID);

        byte[] cozulecek = fixLengthForDecryption(aMechanism.mechanism, aData);
        int max_chunk_size = getMaxChunkSize();
        byte[] output = new byte[max_chunk_size+16]; // Output, işleme giren data'tan uzun olabiliyor.
        int length;

        if (cozulecek.length < max_chunk_size) {
            length = C_Decrypt(aSessionID, cozulecek, 0, cozulecek.length, output, 0, output.length);
            outputStream.write(output, 0, length);
        } else {
            int remaining = aData.length;
            int index = 0;
            while (remaining > 0) {
                int chunkSize = Math.min(remaining, max_chunk_size);

                length = mPKCS11.C_DecryptUpdate(aSessionID, 0, cozulecek, index, chunkSize, 0,
                    output, 0, output.length);
                outputStream.write(output, 0, length);
                remaining = remaining - chunkSize;
                index = index + chunkSize;
            }

            length = mPKCS11.C_DecryptFinal(aSessionID, 0, output, 0, output.length);
            outputStream.write(output, 0, length);
        }

        return outputStream.toByteArray();
    }

    @Override
    public void decryptData(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, InputStream inputStream, OutputStream outputStream) throws PKCS11Exception, SmartCardException, IOException {
        mPKCS11.C_DecryptInit(aSessionID, aMechanism, aKeyID);

        final int max_chunk_size = getMaxChunkSize();
        byte[] inputBuf = new byte[max_chunk_size];
        byte[] outputBuf = new byte[max_chunk_size + 128]; // Output, işleme giren datadan uzun olabiliyor.

        int decryptedLen;
        int readLen;

        // decrypt
        do {
            readLen = inputStream.read(inputBuf, 0, max_chunk_size);
            if (readLen == -1)
                break;

            decryptedLen = mPKCS11.C_DecryptUpdate(aSessionID, 0, inputBuf, 0, readLen, 0, outputBuf, 0, outputBuf.length);
            outputStream.write(outputBuf, 0, decryptedLen);
        } while (readLen > 0);

        // finalize
        decryptedLen = mPKCS11.C_DecryptFinal(aSessionID, 0, outputBuf, 0, outputBuf.length);
        outputStream.write(outputBuf, 0, decryptedLen);
    }

    byte[] fixLengthForDecryption(long mechanism, byte[] aData) {
        if(mechanism == CKM_AES_GCM || mechanism == CKM_AES_CTR)
            return aData;

        int mod = aData.length % 8;

        if(mod == 1 && aData[0] == 0)
        {
            byte[] trimmed = new byte[aData.length - 1];
            System.arraycopy(aData, 1, trimmed, 0, aData.length - 1);
            return trimmed;
        }

        if(mod == 7)
        {
            byte[] zeroPadded = new byte[aData.length + 1];
            zeroPadded[0] = 0;
            System.arraycopy(aData, 0, zeroPadded, 1, aData.length);
            return zeroPadded;
        }

        return aData;
    }

    public long[] importCertificateAndKey(long aSessionID, String aCertLabel, String aKeyLabel, PrivateKey aPrivKey, X509Certificate aCert)
            throws PKCS11Exception,SmartCardException,IOException
    {
        boolean isSign = false;
        boolean isEncrypt = false;

        if(aCert!=null)
        {
            boolean[] ku = aCert.getKeyUsage();
            if(ku!=null)
            {
                isSign = ku[KEYUSAGE_DIGITALSIGNATURE];
                isEncrypt = (ku[KEYUSAGE_KEYENCIPHER] || ku[KEYUSAGE_DATAENCIPHER] || ku[KEYUSAGE_KEYAGREEMENT]);
            }
        }

        long[] objectHandles = new long[3];

        byte[] subject = aCert.getSubjectX500Principal().getEncoded();
        if(aPrivKey instanceof RSAPrivateCrtKey)
        {
            RSAPrivateCrtKey privKey = (RSAPrivateCrtKey) aPrivKey;

            long[] keyHandles = _importRSAKeyPair(aSessionID, aKeyLabel, privKey, subject, isSign, isEncrypt);
            objectHandles[0] = keyHandles[0];
            objectHandles[1] = keyHandles[1];
        }
        else if(aPrivKey instanceof ECPrivateKey)
        {
            ECPrivateKey ecprikey = (ECPrivateKey) aPrivKey;
            ECPublicKey ecpubkey = null;
            try {
                ecpubkey = (ECPublicKey) aCert.getPublicKey();
            }
            catch (ClassCastException exc) {
                try {
                    PublicKey publicKey = aCert.getPublicKey();
                    Class ecPublicKeyImpClass = Class.forName("sun.security.ec.ECPublicKeyImpl");
                    Constructor declaredConstructor = ecPublicKeyImpClass.getConstructor(byte[].class);
                    ecpubkey = (ECPublicKey) declaredConstructor.newInstance(publicKey.getEncoded());
                } catch (Exception e) {
                    logger.warn("Warning in PKCS11Ops", e);
                    throw exc;
                }
            }

            // assign key handles from key pair import
            long[] keyHandles = _importECKeyPair(aSessionID, aKeyLabel, ecprikey, ecpubkey, subject, isSign, isEncrypt);
            objectHandles[0] = keyHandles[0];
            objectHandles[1] = keyHandles[1];
        }
        else
        {
            throw new SmartCardException("Verilen ozel anahtar tipi desteklenmiyor");
        }

        List<CK_ATTRIBUTE> certTemplate = mCardType.getCardTemplate().getCertificateTemplate(aCertLabel, aCert);
        objectHandles[2] = mPKCS11.C_CreateObject(aSessionID, certTemplate.toArray(new CK_ATTRIBUTE[0]));

        return objectHandles;
    }

    private long[] _importRSAKeyPair(long aSessionID, String aLabel, RSAPrivateCrtKey aPrivKey, byte[] aSubject, boolean aIsSign, boolean aIsEncrypt)
            throws PKCS11Exception
    {

        List<CK_ATTRIBUTE> priKeyTemplate = mCardType.getCardTemplate().getRSAPrivateKeyImportTemplate(aLabel, aPrivKey, null, aIsSign, aIsEncrypt);
        List<CK_ATTRIBUTE> pubKeyTemplate = mCardType.getCardTemplate().getRSAPublicKeyImportTemplate(aLabel, aPrivKey, null, aIsSign, aIsEncrypt);
        if (aSubject != null)
            priKeyTemplate.add(new CK_ATTRIBUTE(CKA_SUBJECT, aSubject));

        long[] objectHandles = new long[2];
        objectHandles[0] = mPKCS11.C_CreateObject(aSessionID, priKeyTemplate.toArray(new CK_ATTRIBUTE[0]));
        objectHandles[1] = mPKCS11.C_CreateObject(aSessionID, pubKeyTemplate.toArray(new CK_ATTRIBUTE[0]));

        return objectHandles;
    }

    private long[] _importECKeyPair(long aSessionID, String aLabel, ECPrivateKey aPrivKey, ECPublicKey aPubKey, byte[] aSubject, boolean aIsSign, boolean aIsEncrypt)
            throws PKCS11Exception,IOException
    {
        ECParameterSpec paramspec = aPrivKey.getParams();
        byte[] privateEncodedParams;
        if (mCardType == CardType.AKIS)
            privateEncodedParams = ECParameters.encodeECParameterSpec(paramspec);
        else
            privateEncodedParams = ECParameters.encodeParameters(paramspec);

        ECPoint ecpoint = aPubKey.getW();

        byte[] encodedPoint = ECParameters.encodePoint(ecpoint, paramspec.getCurve());

        DerValue pkecpoint = new DerValue(DerValue.tag_OctetString, encodedPoint);

        byte[] id = OZET_ALICI.digest(encodedPoint);

        List<CK_ATTRIBUTE> pubKeyTemplate = new ArrayList<CK_ATTRIBUTE>();
        pubKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
        pubKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, CKO_PUBLIC_KEY));
        pubKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, CKK_EC));
        pubKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_PARAMS, privateEncodedParams));
        pubKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_POINT, pkecpoint.toByteArray()));
        pubKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, aLabel));
        pubKeyTemplate.add(new CK_ATTRIBUTE(CKA_PRIVATE, false));
        pubKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ID, id));

        if (aIsEncrypt)
            pubKeyTemplate.add(new CK_ATTRIBUTE(CKA_DERIVE,aIsEncrypt));
        if (aIsSign)
            pubKeyTemplate.add(new CK_ATTRIBUTE(CKA_VERIFY,aIsSign));

        boolean isPrivate = false;
        ECParameterSpec publicParams = aPubKey.getParams();
        if (publicParams instanceof NamedCurve) {
            isPrivate = true; // Eğer bilinen bir curve değil ise public key i private yapıyoruz.
            Map<String, NamedCurve> nameCurves = NamedCurve.getNameCurves();
            ObjectIdentifier oid = ((NamedCurve) publicParams).getObjectIdentifier();
            for (NamedCurve c : nameCurves.values()) {
                if (c.getObjectIdentifier().toString().equals(oid.toString())) {
                    isPrivate = false;
                    break;
                }
            }
        }
        pubKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, isPrivate));

        List<CK_ATTRIBUTE> priKeyTemplate = new ArrayList<CK_ATTRIBUTE>();
        priKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
        priKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true));
        priKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, CKO_PRIVATE_KEY));
        priKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_ECDSA));
        priKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, aLabel));
        priKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, aPrivKey.getS()));
        priKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ECDSA_PARAMS, privateEncodedParams));
        priKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ID, id));
     // priKeyTemplate.add(new CK_ATTRIBUTE(CKA_DECRYPT, aIsEncrypt));

        if (aIsEncrypt)
            priKeyTemplate.add(new CK_ATTRIBUTE(CKA_DERIVE,aIsEncrypt));
        if (aIsSign)
            priKeyTemplate.add(new CK_ATTRIBUTE(CKA_SIGN,aIsSign));

        if (aSubject != null)
            priKeyTemplate.add(new CK_ATTRIBUTE(CKA_SUBJECT, aSubject));

        long[] objectHandles = new long[2];
        objectHandles[0] = mPKCS11.C_CreateObject(aSessionID, pubKeyTemplate.toArray(new CK_ATTRIBUTE[0]));
        objectHandles[1] = mPKCS11.C_CreateObject(aSessionID, priKeyTemplate.toArray(new CK_ATTRIBUTE[0]));

        return objectHandles;
    }

    /**
     * @return
     * @deprecated Use importKeyPair(SessionID, KeyPairTemplate)
     */
    @Deprecated
    public long[] importKeyPair(long aSessionID, String aLabel, KeyPair aKeyPair, byte[] aSubject, boolean aIsSign, boolean aIsEncrypt)
            throws PKCS11Exception,SmartCardException,IOException
    {
        PrivateKey prikey = aKeyPair.getPrivate();
        PublicKey pubkey = aKeyPair.getPublic();

        if(prikey instanceof RSAPrivateCrtKey && pubkey instanceof RSAPublicKey)
        {
            return _importRSAKeyPair(aSessionID, aLabel, (RSAPrivateCrtKey) prikey, aSubject, aIsSign, aIsEncrypt);
        }
        else if(prikey instanceof ECPrivateKey && pubkey instanceof ECPublicKey)
        {
            return _importECKeyPair(aSessionID, aLabel, (ECPrivateKey) prikey, (ECPublicKey) pubkey, aSubject, aIsSign, aIsEncrypt);
        }

        else
            throw new SmartCardException("Verilen KeyPair daki anahtar tipleri desteklenmiyor");

    }

    public void changePassword(String aOldPass, String aNewPass, long aSessionID)
            throws PKCS11Exception
    {
        if (mESYAPKCS11 == null)
            mESYAPKCS11 = new ESYAPKCS11(mCardType.getLibName());

        int sonuc = mESYAPKCS11.changePassword(aOldPass, aNewPass, (int) aSessionID);
        if (sonuc != PKCS11Constants.CKR_OK)
            throw PKCS11ExceptionFactory.getPKCS11Exception(sonuc);
    }

    public void formatToken(String aSOpin, String aNewPIN, String aLabel, int slotID)
            throws PKCS11Exception
    {
        if (mESYAPKCS11 == null)
            mESYAPKCS11 = new ESYAPKCS11(mCardType.getLibName());

        int sonuc = mESYAPKCS11.formatToken(aSOpin, aNewPIN, aLabel, slotID);
        if (sonuc != PKCS11Constants.CKR_OK)
            throw PKCS11ExceptionFactory.getPKCS11Exception(sonuc);
    }

    public void setSOPin(byte[] aSOPin, byte[] aNewSOPin, long aSessionID)
            throws PKCS11Exception
    {
        if (mESYAPKCS11 == null)
            mESYAPKCS11 = new ESYAPKCS11(mCardType.getLibName());

        int sonuc = mESYAPKCS11.setSOPin(aSOPin, aSOPin.length, aNewSOPin, aNewSOPin.length, (int) aSessionID);
        if (sonuc != PKCS11Constants.CKR_OK)
            throw PKCS11ExceptionFactory.getPKCS11Exception(sonuc);
    }

    public void changeUserPin(byte[] aSOPin, byte[] aUserPin, long aSessionID)
            throws PKCS11Exception
    {
        if (mESYAPKCS11 == null)
            mESYAPKCS11 = new ESYAPKCS11(mCardType.getLibName());

        int sonuc = mESYAPKCS11.changeUserPin(aSOPin, aSOPin.length, aUserPin, aUserPin.length, (int) aSessionID);
        if (sonuc != PKCS11Constants.CKR_OK)
            throw PKCS11ExceptionFactory.getPKCS11Exception(sonuc);
    }

    public boolean setContainer (byte[] aContainerLabel, long aSessionID)
    {
        String libName = mCardType.getLibName();

        if (mESYAPKCS11 == null)
            mESYAPKCS11 = new ESYAPKCS11(libName);

        int sonuc = mESYAPKCS11.setContainer(aContainerLabel, aContainerLabel.length, (int) aSessionID, libName);
        if(sonuc == PKCS11Constants.CKR_OK)
            return true;
        return false;
    }


    public boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName,byte[] aPbCertData, int aSignOrEnc)
    {
        if (mESYAPKCS11 == null)
            mESYAPKCS11 = new ESYAPKCS11(mCardType.getLibName());

        int sonuc = mESYAPKCS11.importCertificateAndKeyWithCSP(aAnahtarCifti, aAnahtarCifti.length, aAnahtarLen, aScfname, aScfname.length(), aContextName, aContextName.length(), aPbCertData, aSignOrEnc);
        if(sonuc == PKCS11Constants.CKR_OK)
            return true;
        return false;
    }

    public boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName,X509Certificate aPbCertificate, int aSignOrEnc)
    {
        byte[] encoded = null;

        try {
            encoded = aPbCertificate.getEncoded();
        } catch (CertificateEncodingException e) {
            logger.warn("Warning in PKCS11Ops", e);
            return false;
        }
        return importCertificateAndKeyWithCSP(aAnahtarCifti, aAnahtarLen, aScfname, aContextName, encoded, aSignOrEnc);
    }

    protected void changePUK(byte[] aOldPUK, byte[] aNewPUK, long aSessionID)
            throws PKCS11Exception
    {
        String libName = mCardType.getLibName();

        if (mESYAPKCS11 == null)
            mESYAPKCS11 = new ESYAPKCS11(libName);

        int sonuc = mESYAPKCS11.ChangePUK(aOldPUK, aOldPUK.length, aNewPUK, aNewPUK.length, (int) aSessionID, libName);
        if (sonuc != PKCS11Constants.CKR_OK)
            throw PKCS11ExceptionFactory.getPKCS11Exception(sonuc);
    }

    protected void unBlockPIN(byte[] aPUK, byte[] aUserPIN, long aSessionID)
            throws  PKCS11Exception
    {
        String libName = mCardType.getLibName();

        if (mESYAPKCS11 == null)
            mESYAPKCS11 = new ESYAPKCS11(libName);

        int sonuc = mESYAPKCS11.UnBlockPIN(aPUK, aPUK.length, aUserPIN, aUserPIN.length, (int) aSessionID, libName);
        if (sonuc != PKCS11Constants.CKR_OK)
            throw PKCS11ExceptionFactory.getPKCS11Exception(sonuc);
    }

    public long[] objeAra(long aSessionID, CK_ATTRIBUTE[] aTemplate)
            throws PKCS11Exception
    {
        long maxCount = 200;

        mPKCS11.C_FindObjectsInit(aSessionID, aTemplate);
        long[] objectList = mPKCS11.C_FindObjects(aSessionID, maxCount);
        mPKCS11.C_FindObjectsFinal(aSessionID);

        return objectList;
    }

    public void getAttributeValue(long aSessionID, long aObjectID, CK_ATTRIBUTE[] aTemplate)
            throws PKCS11Exception
    {
        mPKCS11.C_GetAttributeValue(aSessionID, aObjectID, aTemplate);
    }

    public void changeLabel(long aSessionID, String aOldLabel, String aNewLabel)
            throws PKCS11Exception,SmartCardException
    {
        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_TOKEN, true),
                new CK_ATTRIBUTE(CKA_LABEL, aOldLabel)
            };

        long[] ids = objeAra(aSessionID, template);
        if(ids.length==0)
        {
            throw new SmartCardException(aOldLabel + " isimli nesne kartta bulunamadi.");
        }

        CK_ATTRIBUTE[] labelTemplate =
            {
                new CK_ATTRIBUTE(CKA_LABEL, aNewLabel)
            };

        for(int i=0;i<ids.length;i++)
        {
            mPKCS11.C_SetAttributeValue(aSessionID, ids[i], labelTemplate);
        }
    }

    public byte[] generateRSAPrivateKey(long aSessionID,int keySize) throws ESYAException
    {
        throw new RuntimeException("ERROR Operation:Operation is not supported in PKCS11Ops. Use creteRSAKeyPair");
    }

    public KeyPair generateECKeyPair(long aSessionID, ECParameterSpec ecParameterSpec) throws ESYAException {
        throw new RuntimeException("ERROR Operation:Operation is not supported in PKCS11Ops.");
    }

    public byte[] getModulusOfKey(long aSessionID, long aObjID)
        throws PKCS11Exception {
        CK_ATTRIBUTE[] values = {new CK_ATTRIBUTE(CKA_MODULUS)};

        getAttributeValue(aSessionID, aObjID, values);

        return (byte[]) values[0].pValue;
    }

    private long getObjIDFromKeyLabel(long aSessionID, String aLabel, long keyClass) throws PKCS11Exception, SmartCardException {

        CK_ATTRIBUTE[] template =
            {
                new CK_ATTRIBUTE(CKA_CLASS, keyClass),
                new CK_ATTRIBUTE(CKA_LABEL, aLabel)
            };

        long[] objectList = objeAra(aSessionID, template);

        if(objectList.length == 0)
            throw new SmartCardException(aLabel + " isimli anahtar kartta yok");

        return objectList[0];
    }

    public long getObjIDFromPublicKeyLabel(long aSessionID, String aLabel) throws PKCS11Exception, SmartCardException {
        return getObjIDFromKeyLabel(aSessionID, aLabel, CKO_PUBLIC_KEY);
    }

    public long getObjIDFromPrivateKeyLabel(long aSessionID, String aLabel) throws PKCS11Exception, SmartCardException {
        return getObjIDFromKeyLabel(aSessionID, aLabel, CKO_PRIVATE_KEY);
    }

    public long getObjIDFromSecretKeyLabel(long aSessionID, String aLabel) throws PKCS11Exception, SmartCardException {
        return getObjIDFromKeyLabel(aSessionID, aLabel, CKO_SECRET_KEY);
    }

    /**
     * wrapKey wraps (i.e., encrypts) a private or secret key.
     * @param aSessionID      session id
     * @param aMechanism      wrapping mechanism
     * @param wrapperKeyLabel label of the wrapping key
     * @param aKeyLabel       label of the key that will be wrapped
     * @throws SmartCardException if keys with given label are not found.
     */
    public byte[] wrapKey(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, String aKeyLabel)
            throws PKCS11Exception,SmartCardException
    {
        long wrappingKeys = getWrapperKey(aSessionID, wrapperKeyLabel);

        CK_ATTRIBUTE[] keyTemplate = {
            new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, aKeyLabel),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true)
        };
        long[] keys = objeAra(aSessionID, keyTemplate);
        if (keys.length == 0)
            throw new SmartCardException(aKeyLabel + " isimli anahtar kartta bulunamadi");

        return mPKCS11.C_WrapKey(aSessionID, aMechanism, wrappingKeys, keys[0]);
    }

    public byte[] wrapKey(long aSessionID, CK_MECHANISM aMechanism, long wrapperKeyID, long keyID)
        throws PKCS11Exception, SmartCardException {
        return mPKCS11.C_WrapKey(aSessionID, aMechanism, wrapperKeyID, keyID);
    }

    /**
     * wrapKey wraps (i.e., encrypts) a private or secret key.
     *
     * @param aSessionID session id
     * @param aMechanism wrapping mechanism
     * @param wrapperKeyTemplate KeyFacade of the wrapping key
     * @param aToBeExportedKeyTemplate KeyFacade of the key that will be wrapped
     * @return wrapped key
     */
    public byte[] wrapKey(long aSessionID, CK_MECHANISM aMechanism, KeyTemplate wrapperKeyTemplate, KeyTemplate aToBeExportedKeyTemplate)
        throws PKCS11Exception {
        findKeyIDs(aSessionID, wrapperKeyTemplate, aToBeExportedKeyTemplate);
        return mPKCS11.C_WrapKey(aSessionID, aMechanism, wrapperKeyTemplate.getKeyId(), aToBeExportedKeyTemplate.getKeyId());
    }

    private void findKeyIDs(long aSessionID, KeyTemplate... keyTemplates) throws PKCS11Exception {
        for (KeyTemplate keyTemplate : keyTemplates) {
            if (keyTemplate.getKeyId() <= 0)
                findKeyID(aSessionID, keyTemplate);
        }
    }

    private void findKeyID(long aSessionID, KeyTemplate keyTemplate) throws PKCS11Exception {
        long[] unwrappingkeys = objeAra(aSessionID, keyTemplate.getAttributesAsArr());
        if (unwrappingkeys == null || unwrappingkeys.length == 0)
            return;

        if (unwrappingkeys.length > 1)
            logger.warn("There are more than 1 Key releates with Template:" + keyTemplate);
        keyTemplate.setKeyId(unwrappingkeys[0]);
    }

    public long unwrapKey(long aSessionID, CK_MECHANISM aMechanism, String unwrapperKeyLabel, byte[] aWrappedKey, KeyTemplate aUnwrappedKeyTemplate)
        throws PKCS11Exception, SmartCardException {

        byte[] fixedWrappedKey = fixLengthForDecryption(aMechanism.mechanism, aWrappedKey);

        long unwrappingKeys = getUnwrapperKey(aSessionID, unwrapperKeyLabel);

        long keyId = mPKCS11.C_UnwrapKey(aSessionID, aMechanism, unwrappingKeys, fixedWrappedKey, aUnwrappedKeyTemplate.getAttributesAsArr());
        aUnwrappedKeyTemplate.setKeyId(keyId);

        String keyLabel = aUnwrappedKeyTemplate.getLabel();
        updateRSAKeyIds(aSessionID, keyLabel);

        return keyId;
    }

    public long unwrapKey(long aSessionID, CK_MECHANISM aMechanism, long unwrapperKeyID, byte[] aWrappedKey, KeyTemplate aUnwrappedKeyTemplate)
        throws PKCS11Exception, SmartCardException {

        byte[] fixedWrappedKey = fixLengthForDecryption(aMechanism.mechanism, aWrappedKey);

        long keyId = mPKCS11.C_UnwrapKey(aSessionID, aMechanism, unwrapperKeyID, fixedWrappedKey, aUnwrappedKeyTemplate.getAttributesAsArr());
        aUnwrappedKeyTemplate.setKeyId(keyId);

        String keyLabel = aUnwrappedKeyTemplate.getLabel();
        updateRSAKeyIds(aSessionID, keyLabel);

        return keyId;
    }

    public long unwrapKey(long aSessionID, CK_MECHANISM aMechanism, KeyTemplate unwrapperKeyTemplate, byte[] aWrappedKey, KeyTemplate aUnwrappedKeyTemplate)
        throws PKCS11Exception, SmartCardException {

        byte[] fixedWrappedKey = fixLengthForDecryption(aMechanism.mechanism, aWrappedKey);

        applyTemplate(aUnwrappedKeyTemplate);

        findKeyIDs(aSessionID, unwrapperKeyTemplate);

        long keyId = mPKCS11.C_UnwrapKey(aSessionID, aMechanism, unwrapperKeyTemplate.getKeyId(), fixedWrappedKey, aUnwrappedKeyTemplate.getAttributesAsArr());

        aUnwrappedKeyTemplate.setKeyId(keyId);

        String keyLabel = unwrapperKeyTemplate.getLabel();
        updateRSAKeyIds(aSessionID, keyLabel);

        return keyId;
    }

    private void applyTemplate(KeyTemplate aUnwrappedKeyTemplate) throws SmartCardException
    {
        ICardTemplate cardTemplate = mCardType.getCardTemplate();

        if (aUnwrappedKeyTemplate instanceof SecretKeyTemplate)
            cardTemplate.applyTemplate((SecretKeyTemplate) aUnwrappedKeyTemplate);
    }

    public long unwrapKey(long aSessionID, CK_MECHANISM aMechanism, byte[] certSerialNumber, byte[] aWrappedKey, KeyTemplate aUnwrappedKeyTemplate)
        throws PKCS11Exception, SmartCardException {
        byte[] fixedWrappedKey = fixLengthForDecryption(aMechanism.mechanism, aWrappedKey);
        byte[] id = getObjectIdWithCertSerialNumber(aSessionID, certSerialNumber);
        CK_ATTRIBUTE[] unwrappingKeyTemplate = {
            new CK_ATTRIBUTE(PKCS11Constants.CKA_ID, id),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY)   // cert -> privatekey, not secretkey
        };
        long[] unwrappingkeys = objeAra(aSessionID, unwrappingKeyTemplate);
        if (unwrappingkeys.length == 0)
            throw new SmartCardException(StringUtil.toString(certSerialNumber) + " Sertifika Seri Nolu Gizli Anahtar Kartta Bulunamadı");

        long keyId = mPKCS11.C_UnwrapKey(aSessionID, aMechanism, unwrappingkeys[0], fixedWrappedKey, aUnwrappedKeyTemplate.getAttributesAsArr());

        aUnwrappedKeyTemplate.setKeyId(keyId);

        return keyId;
    }

    public PKCS11 getmPKCS11() {
        return mPKCS11;
    }

    public KeySpec createKeyPair(long aSessionID, KeyPairTemplate template) throws PKCS11Exception, IOException, SmartCardException {
        if (logger.isDebugEnabled()) {
            printAttributes(template.getPrivateKeyTemplate().getAttributesAsArr());
            printAttributes(template.getPublicKeyTemplate().getAttributesAsArr());
        }

        if (template instanceof RSAKeyPairTemplate)
            return createKeyPair(aSessionID, (RSAKeyPairTemplate) template);
        else if (template instanceof ECKeyPairTemplate)
            return createKeyPair(aSessionID, (ECKeyPairTemplate) template);
        else
            throw new ESYARuntimeException("Not implemented Yet for:" + template);
    }

    public KeySpec createKeyPair(long aSessionID, RSAKeyPairTemplate template) throws PKCS11Exception, SmartCardException {

        long[] newKeyIDs = createKeyPairWithOutRead(aSessionID, template);

        KeySpec spec = _readRSAPublicKeySpec(aSessionID, newKeyIDs[0]);

        String keyLabel = template.getPublicKeyTemplate().getLabel();
        updateRSAKeyIds(aSessionID, keyLabel);

        template.getPublicKeyTemplate().setKeyId(newKeyIDs[0]);
        template.getPrivateKeyTemplate().setKeyId(newKeyIDs[1]);

        return spec;
    }

    // First one public key object id, Second one private object key id (Tested @Dirak)
    protected long[] createKeyPairWithOutRead(long aSessionID, RSAKeyPairTemplate template) throws PKCS11Exception, SmartCardException {

        CK_MECHANISM mech = new CK_MECHANISM(0L);
        mech.mechanism = CKM_RSA_PKCS_KEY_PAIR_GEN;
        mech.pParameter = null;

        ICardTemplate kartBilgi = mCardType.getCardTemplate();
        kartBilgi.applyTemplate(template);

        CK_ATTRIBUTE[] pubKeyTemplate = template.getPublicKeyTemplate().getAttributesAsArr();
        CK_ATTRIBUTE[] priKeyTemplate = template.getPrivateKeyTemplate().getAttributesAsArr();

        if (logger.isDebugEnabled())
            printParameterInfo(mech, pubKeyTemplate, priKeyTemplate);

        long[] keyIDs = mPKCS11.C_GenerateKeyPair(aSessionID, mech, pubKeyTemplate, priKeyTemplate);

        return keyIDs;
    }

    protected long[] createKeyPairWithOutRead(long aSessionID, ECKeyPairTemplate template) throws PKCS11Exception, SmartCardException {

        CK_MECHANISM mech = new CK_MECHANISM(0L);
        mech.mechanism = CKM_ECDSA_KEY_PAIR_GEN;
        mech.pParameter = null;

        ICardTemplate kartBilgi = mCardType.getCardTemplate();
        kartBilgi.applyTemplate(template);

        ECPublicKeyTemplate ecPublicKeyTemplate = (ECPublicKeyTemplate) template.getPublicKeyTemplate();
        CK_ATTRIBUTE[] pubKeyAttrs = ecPublicKeyTemplate.getAttributesAsArr();

        ECPrivateKeyTemplate ecPrivateKeyTemplate = (ECPrivateKeyTemplate) template.getPrivateKeyTemplate();
        CK_ATTRIBUTE[] priKeyAttrs = ecPrivateKeyTemplate.getAttributesAsArr();

        long[] keyIDs = mPKCS11.C_GenerateKeyPair(aSessionID, mech, pubKeyAttrs, priKeyAttrs);

        return keyIDs;
    }

    private byte[] updateRSAKeyIds(long aSessionID, String keyLabel) {
        try {
            CK_ATTRIBUTE[] findTemplate =
                {new CK_ATTRIBUTE(CKA_LABEL, keyLabel),
                    new CK_ATTRIBUTE(CKA_TOKEN, true)};

            long[] keyIDs = objeAra(aSessionID, findTemplate);
            if (keyIDs == null || keyIDs.length < 1)
                return null;

            CK_ATTRIBUTE[] modulusTemplate =
                {new CK_ATTRIBUTE(CKA_MODULUS)};

            mPKCS11.C_GetAttributeValue(aSessionID, keyIDs[0], modulusTemplate);

            byte[] modulus = (byte[]) modulusTemplate[0].pValue;
            OZET_ALICI.update(modulus);
            byte[] id = OZET_ALICI.digest();

            CK_ATTRIBUTE[] idTemplate =
                {new CK_ATTRIBUTE(CKA_ID, id)};
            for (long keyID : keyIDs) {
                mPKCS11.C_SetAttributeValue(aSessionID, keyID, idTemplate);
            }

            return id;
        } catch (Exception e) {
            logger.warn("Can not update key id", e);
        }

        return null;
    }

    private KeySpec createKeyPair(long aSessionID, ECKeyPairTemplate template) throws SmartCardException, PKCS11Exception {

        // create key pair, get returned key IDs
        long[] keyIDs = createKeyPairWithOutRead(aSessionID, template);

        byte[] cka_id;
        ECPublicKeySpec keySpec;

        template.getPublicKeyTemplate().setKeyId(keyIDs[0]);
        template.getPrivateKeyTemplate().setKeyId(keyIDs[1]);

        // get public key ID
        long publicKeyID = keyIDs[0];

        ECPublicKeyTemplate ecPublicKeyTemplate = (ECPublicKeyTemplate) template.getPublicKeyTemplate();

        if (ecPublicKeyTemplate.isSecretECCurve()) {
            ECParameterSpec spec = ecPublicKeyTemplate.getSpec();
            ECDomainParameter domainParameter = ECDomainParameter.getInstance(spec);

            CK_ATTRIBUTE[] ecPointTemplate = {
                    new CK_ATTRIBUTE(CKA_EC_POINT)
            };

            mPKCS11.C_GetAttributeValue(aSessionID, publicKeyID, ecPointTemplate);

            try {
                Asn1DerDecodeBuffer decodeBuffer = new Asn1DerDecodeBuffer((byte[]) ecPointTemplate[0].pValue);
                Asn1OctetString asn1OctetString = new Asn1OctetString();
                asn1OctetString.decode(decodeBuffer);

                byte[] pointX = asn1OctetString.value;

                ECGNUPoint ecPoint = new ECDSAKeyPairX509Codec().getECPoint(domainParameter, pointX);
                keySpec = new ECPublicKeySpec(ecPoint, spec);
                ECPublicKey ecPublicKey = (ECPublicKey) KeyUtil.generatePublicKey(keySpec);
                cka_id = ECUtil.generatePKCS11ID(ecPublicKey);
            } catch (Exception ex) {
                throw new SmartCardException(ex);
            }
        } else {
            // get public key spec. (EC since this method is meant for EC templates)
            keySpec = (ECPublicKeySpec) getPublicKeySpec(aSessionID, publicKeyID);

            try {
                ECPublicKey ecPublicKey = (ECPublicKey) KeyUtil.generatePublicKey(keySpec);
                cka_id = ECUtil.generatePKCS11ID(ecPublicKey);

            } catch (CryptoException e) {
                System.err.println("Invalid key");
                throw new ESYARuntimeException(e);
            }
        }

        updateCKA_IDs(aSessionID, keyIDs, cka_id);

        return keySpec;
    }

    private void updateCKA_IDs(long aSessionID, long[] objectIDs, byte[] newID) throws PKCS11Exception {
        // yeni ID için template.
        CK_ATTRIBUTE[] idTemplate = new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(CKA_ID, newID)};

        for(int i=0; i < objectIDs.length; i++){
            if(objectIDs[i] != -1)
                mPKCS11.C_SetAttributeValue(aSessionID, objectIDs[i], idTemplate);
        }
    }



    public long[] importKeyPair(long sessionID, KeyPairTemplate template) throws PKCS11Exception, SmartCardException {
        mCardType.getCardTemplate().applyTemplate(template);

        if (template instanceof RSAKeyPairTemplate)
            return importKeyPair(sessionID, (RSAKeyPairTemplate) template);
        else if (template instanceof ECKeyPairTemplate)
            return importKeyPair(sessionID, (ECKeyPairTemplate) template);
        else
            throw new ESYARuntimeException("Not implemented Yet for:" + template);
    }

    public long[] importKeyPair(long sessionID, RSAKeyPairTemplate template) throws PKCS11Exception {
        long[] keyHandles = new long[]{-1, -1};

        String keyLabel = null;

        // public key
        {
            RSAPublicKeyTemplate publicKeyTemplate = template.getPublicKeyTemplate();
            if (publicKeyTemplate != null) {
                keyLabel = publicKeyTemplate.getLabel();

                CK_ATTRIBUTE[] pubKeyTemplate = publicKeyTemplate.getAttributesAsArr();
                long pubKeyID = mPKCS11.C_CreateObject(sessionID, pubKeyTemplate);

                publicKeyTemplate.setKeyId(pubKeyID);
                keyHandles[0] = pubKeyID;
            }
        }

        // private key
        {
            RSAPrivateKeyTemplate privateKeyTemplate = template.getPrivateKeyTemplate();
            if (privateKeyTemplate != null) {
                CK_ATTRIBUTE[] priKeyTemplate = privateKeyTemplate.getAttributesAsArr();
                long priKeyID = mPKCS11.C_CreateObject(sessionID, priKeyTemplate);

                privateKeyTemplate.setKeyId(priKeyID);
                keyHandles[1] = priKeyID;
            }
        }

        if (keyLabel != null) {
            updateRSAKeyIds(sessionID, keyLabel);
        }

        return keyHandles;
    }

    public long[] importKeyPair(long sessionID, ECKeyPairTemplate template) throws PKCS11Exception {
        long[] keyHandles = new long[]{-1, -1};

        // public key
        {
            AsymmKeyTemplate publicKeyTemplate = template.getPublicKeyTemplate();
            if (publicKeyTemplate != null) {
                CK_ATTRIBUTE[] pubKeyTemplate = publicKeyTemplate.getAttributesAsArr();
                long pubKeyID = mPKCS11.C_CreateObject(sessionID, pubKeyTemplate);

                publicKeyTemplate.setKeyId(pubKeyID);
                keyHandles[0] = pubKeyID;
            }
        }

        // private key
        {
            AsymmKeyTemplate privateKeyTemplate = template.getPrivateKeyTemplate();
            if (privateKeyTemplate != null) {
                CK_ATTRIBUTE[] priKeyTemplate = privateKeyTemplate.getAttributesAsArr();
                long priKeyID = mPKCS11.C_CreateObject(sessionID, priKeyTemplate);

                privateKeyTemplate.setKeyId(priKeyID);
                keyHandles[1] = priKeyID;
            }
        }

        return keyHandles;
    }

    public long createSecretKey(long sessionID, SecretKeyTemplate template) throws PKCS11Exception, SmartCardException {
        mCardType.getCardTemplate().applyTemplate(template);
        CK_ATTRIBUTE[] templateAttr = template.getAttributesAsArr();

        CK_MECHANISM mech = new CK_MECHANISM(template.getGenerationMechanism());
        long keyID = mPKCS11.C_GenerateKey(sessionID, mech, templateAttr);
        template.setKeyId(keyID);

        return keyID;
    }

    public int deleteCertificate(long aSessionID, String aKeyLabel)
            throws PKCS11Exception
    {
        CK_ATTRIBUTE[] certTemplate = {
            new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, aKeyLabel),
            new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_CERTIFICATE)   // cert -> privatekey, not secretkey
        };
        long[] certids = objeAra(aSessionID, certTemplate);
        int deletedCount = 0;
        if(certids.length >0)
        {
            for(int i=0 ;i<certids.length;i++)
            {
                mPKCS11.C_DestroyObject(aSessionID, certids[i]);
                deletedCount++;
            }
        }
        return deletedCount;
    }

    private void printParameterInfo(CK_MECHANISM mech, CK_ATTRIBUTE[] pubKeyTemplate, CK_ATTRIBUTE[] priKeyTemplate) {
        if (logger.isDebugEnabled()) {
            logger.debug(" ****************** pkcs11 parameters parametreleri ********************");
            logger.debug(" Mechanism: " + mech.mechanism);
            logger.debug(" Mechanism Parameters : ");
            if (mech.pParameter == null)
                logger.debug("null");
            logger.debug("\n");

            logger.debug("--------- Private Key Attributes -------------");
            printAttributes(priKeyTemplate);
            logger.debug("----------------------------------------------");
            logger.debug("--------- Public Key Attributes -------------");
            printAttributes(pubKeyTemplate);
            logger.debug("--------------------------------------------");
        }
    }

    private void printAttributes(CK_ATTRIBUTE[] templateAttributes) {
        for (CK_ATTRIBUTE ck_attribute : templateAttributes) {
            logger.debug(ck_attribute.toString());
            System.out.println(ck_attribute.toString());
        }
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

        // unwrap
        long unwrappedKeyID = mPKCS11.C_UnwrapKey(sessionID, unwrapMechanism, unwrapperKeyID, wrappedKey, unwrapTemplate);

        // operation
        byte[] result = null;
        switch (operation) {
            case SIGN:
                result = signDataWithKeyID(sessionID, unwrappedKeyID, operationMechanism, operationData);
                break;
            case ENCRYPT:
                result = encryptData(sessionID, unwrappedKeyID, operationData, operationMechanism);
                break;
            case DECRYPT:
                result = decryptData(sessionID, unwrappedKeyID, operationData, operationMechanism);
                break;
        }

        // delete
        getmPKCS11().C_DestroyObject(sessionID, unwrappedKeyID);

        return result;
    }

    public long deriveKey(long sessionId, CK_MECHANISM derive_mechanism, long privateKeyHandle, KeyTemplate unwrappedKeyTemplate) throws PKCS11Exception {
        CK_ATTRIBUTE[] ck_attributes = unwrappedKeyTemplate.getAttributesAsArr();
        long objectHandle = mPKCS11.C_DeriveKey(sessionId, derive_mechanism, privateKeyHandle, ck_attributes);
        return objectHandle;
    }

    private long getUnwrapperKey(long aSessionID, String unwrapperKeyLabel) throws SmartCardException, PKCS11Exception {
        CK_ATTRIBUTE[] unwrappingKeyTemplate = {
                new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, unwrapperKeyLabel),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP, true)
        };

        long[] unwrappingKeys = objeAra(aSessionID, unwrappingKeyTemplate);

        if (unwrappingKeys.length == 0)
            throw new SmartCardException(unwrapperKeyLabel + " isimli anahtar kartta bulunamadi");
        if (unwrappingKeys.length > 1)
            throw new SmartCardException(unwrapperKeyLabel + " isimli anahtar kartta birden fazla bulundu.");

        return unwrappingKeys[0];
    }

    private long getWrapperKey(long aSessionID, String wrapperKeyLabel) throws SmartCardException, PKCS11Exception {
        CK_ATTRIBUTE[] wrappingKeyTemplate = {
                new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, wrapperKeyLabel),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP, true)
        };
        long[] wrappingKeys = objeAra(aSessionID, wrappingKeyTemplate);

        if (wrappingKeys.length == 0)
            throw new SmartCardException(wrapperKeyLabel + " isimli anahtar kartta bulunamadi");
        if (wrappingKeys.length > 1)
            throw new SmartCardException(wrapperKeyLabel + " isimli anahtar kartta birden fazla bulundu.");

        return wrappingKeys[0];
    }

    private CK_ATTRIBUTE[] getAttributeValueInTwoSteps(long sessionID, long objectID, long [] types) throws PKCS11Exception {
        CK_ATTRIBUTE[] ckAttributeWithBytesValue = getPKCS11LibOps().getAttributeValueInTwoSteps(sessionID, objectID, types);
        CK_ATTRIBUTE[] ckAttributeWithObjectValue = convertValueOfCKAttribute(ckAttributeWithBytesValue);
        return ckAttributeWithObjectValue;
    }

    private CK_ATTRIBUTE[] convertValueOfCKAttribute(CK_ATTRIBUTE[] ckAttributeWithBytesValue) {
        CK_ATTRIBUTE[] ckAttributeWithObjectValue = new CK_ATTRIBUTE[ckAttributeWithBytesValue.length];

        Map<Long, Class> attributeValueTypes = PKCS11Names.getAttributeValueTypes();
        attributeValueTypes.putAll(mCardType.getCardTemplate().getVendorSpecificAttributeValuesType());

        for (int i = 0; i < ckAttributeWithBytesValue.length; i++) {
            long type = ckAttributeWithBytesValue[i].type;
            CK_ATTRIBUTE ckAttribute = new CK_ATTRIBUTE();
            ckAttribute.type = type;

            Class valueTypes = attributeValueTypes.get(type);
            if(valueTypes.equals(char[].class)){
                byte[] pValue = ckAttributeWithBytesValue[i].getByteArray();
                ckAttribute.pValue = new String(pValue, StandardCharsets.UTF_8).toCharArray();
            }else if(valueTypes.equals(byte[].class)){
                ckAttribute.pValue = ckAttributeWithBytesValue[i].getByteArray();
            }else if(valueTypes.equals(boolean.class)){
                byte[] pValue = ckAttributeWithBytesValue[i].getByteArray();
                ckAttribute.pValue = ByteConversionUtil.bytesToBoolean(pValue);
            }else if(valueTypes.equals(long.class)){
                byte[] pValue = ckAttributeWithBytesValue[i].getByteArray();
                ckAttribute.pValue = ByteConversionUtil.littleEndianBytesToLong(pValue);
            }else if(valueTypes.equals(long[].class)){
                byte[] pValue = ckAttributeWithBytesValue[i].getByteArray();
                ckAttribute.pValue = ByteConversionUtil.littleEndianBytesToLongArray(pValue);
            }else if(valueTypes.equals(CK_ATTRIBUTE.class)){
                //not implemented yet
            }
            ckAttributeWithObjectValue[i] = ckAttribute;
        }

        return ckAttributeWithObjectValue;
    }

    @Override
    public WrappedObjectsWithAttributes wrapObjectsWithAttributes(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, long[] objectIDs)
            throws PKCS11Exception, SmartCardException {

        long wrapperKey = getWrapperKey(aSessionID, wrapperKeyLabel);

        WrappedObjectsWithAttributes wrappedObjectsWithAttributes = new WrappedObjectsWithAttributes();
        wrappedObjectsWithAttributes.setTotalToBeWrappedObject(objectIDs.length);
        List<ObjectResult> objectResults = new ArrayList<>();

        List<ESCObject> escObjects = new ArrayList<>();

        for (int i = 0; i < objectIDs.length; i++) {
            ObjectResult objectResult = new ObjectResult();
            objectResult.setObjectID(objectIDs[i]);

            long[] attributeTypes = new long[]{PKCS11Constants.CKA_CLASS, PKCS11Constants.CKA_EXTRACTABLE};

            CK_ATTRIBUTE[] ckAttributes = getAttributeValueInTwoSteps(aSessionID, objectIDs[i], attributeTypes);

            Long classOfObject =  ByteConversionUtil.littleEndianBytesToLong((byte[])ckAttributes[0].pValue);
            Boolean extractableOfObject = ckAttributes.length == 1 ? null : ByteConversionUtil.bytesToBoolean((byte[])ckAttributes[1].pValue);

            if(extractableOfObject == null && (classOfObject == PKCS11Constants.CKO_PUBLIC_KEY || classOfObject == PKCS11Constants.CKO_CERTIFICATE)){

                long[] allAttributeTypes = getAttributeTypes();

                CK_ATTRIBUTE[] objectsCKAttributes = getAttributeValueInTwoSteps(aSessionID, objectIDs[i], allAttributeTypes);

                ESCObject escObject = null;
                try {
                    escObject = SCObjectUtil.encodeESCObject(objectsCKAttributes);
                } catch (ESYAException e) {
                    objectResult.setSuccess(false);
                    objectResult.setReasonIfFail("ENCODING ERROR");
                    objectResults.add(objectResult);
                    continue;
                }

                escObjects.add(escObject);

                objectResult.setSuccess(true);
            }else if(extractableOfObject == false){
                objectResult.setSuccess(false);
                objectResult.setReasonIfFail("CKA_EXTRACTABLE FALSE");
            }else {
                byte[] wrappedKey;
                try {
                    wrappedKey = mPKCS11.C_WrapKey(aSessionID, aMechanism, wrapperKey, objectIDs[i]);
                }  catch (PKCS11Exception e) {
                    objectResult.setSuccess(false);
                    objectResult.setReasonIfFail(e.getMessage());
                    objectResults.add(objectResult);
                    continue;
                }

                long[] allAttributeTypes = getAttributeTypes();

                CK_ATTRIBUTE[] objectsCKAttributes = getAttributeValueInTwoSteps(aSessionID, objectIDs[i], allAttributeTypes);

                ESCObject escObject = null;
                try {
                    escObject = SCObjectUtil.encodeESCObject(wrappedKey, objectsCKAttributes);
                } catch (ESYAException e) {
                    objectResult.setSuccess(false);
                    objectResult.setReasonIfFail("ENCODING ERROR");
                    objectResults.add(objectResult);
                    continue;
                }

                escObjects.add(escObject);

                objectResult.setSuccess(true);
            }
            objectResults.add(objectResult);
        }
        wrappedObjectsWithAttributes.setObjectResult(objectResults);

        ESCObjectBag escObjectBag = new ESCObjectBag(escObjects.toArray(new ESCObject[0]));
        byte[] escObjectBagEncoded = escObjectBag.getEncoded();

        wrappedObjectsWithAttributes.setWrappedObjects(escObjectBagEncoded);

        return wrappedObjectsWithAttributes;
    }

    @Override
    public UnwrapObjectsResults unwrapObjectsWithAttributes(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, byte[] wrappedBytes)
            throws PKCS11Exception, ESYAException {

        long unwrapperKey = getUnwrapperKey(aSessionID, wrapperKeyLabel);

        UnwrapObjectsResults unwrapObjectsResults = new UnwrapObjectsResults();
        ESCObjectBag scObjectBag = new ESCObjectBag(wrappedBytes);
        ESCObject[] escObjects = scObjectBag.getESCObjects();
        unwrapObjectsResults.setTotalToBeUnwrappedObjects(escObjects.length);

        List<ObjectResult> objectResults = new ArrayList<>();

        for (ESCObject escObject : escObjects) {
            ObjectResult objectResult = new ObjectResult();
            List<CK_ATTRIBUTE> ckAttributes = new ArrayList<>();

            long classOfObject = SCObjectUtil.getClassInESCObject(escObject);

            if(classOfObject == PKCS11Constants.CKO_PRIVATE_KEY || classOfObject == PKCS11Constants.CKO_SECRET_KEY){
                byte[] wrappedObject = null;
                for (ESCObjectAttribute escObjectAttribute : escObject.getESCObjectAttributes()) {
                    long type = ByteConversionUtil.bigEndianBytesToLong(escObjectAttribute.getType());
                    if(type == PKCS11Constants.CKA_VALUE){
                        wrappedObject = escObjectAttribute.getValue();
                    }else {

                        if(type == CKA_KEY_GEN_MECHANISM || type == CKA_LOCAL){
                            continue;
                        }
                        // AES için
                        if(type == CKA_VALUE_LEN){
                            continue;
                        }
                        //RSA private için
                        if(type == CKA_PUBLIC_EXPONENT || type == CKA_MODULUS){
                            continue;
                        }
                        //EC private için
                        if(type == CKA_EC_PARAMS){
                            continue;
                        }

                        byte[] value = escObjectAttribute.getValue();
                        CK_ATTRIBUTE ck_attribute = new CK_ATTRIBUTE(type, value);
                        ckAttributes.add(ck_attribute);
                    }
                }

                long objectID;
                try {
                    byte[] fixedWrappedObject = fixLengthForDecryption(aMechanism.mechanism, wrappedObject);
                    objectID = mPKCS11.C_UnwrapKey(aSessionID, aMechanism, unwrapperKey, fixedWrappedObject, ckAttributes.toArray(new CK_ATTRIBUTE[0]));
                } catch (PKCS11Exception e) {
                    objectResult.setSuccess(false);
                    objectResult.setReasonIfFail(e.getMessage());
                    objectResults.add(objectResult);
                    continue;
                }

                objectResult.setObjectID(objectID);
                objectResult.setSuccess(true);
            }else if(classOfObject == PKCS11Constants.CKO_CERTIFICATE || classOfObject == PKCS11Constants.CKO_PUBLIC_KEY){
                for (ESCObjectAttribute escObjectAttribute : escObject.getESCObjectAttributes()) {
                    long type = ByteConversionUtil.bigEndianBytesToLong(escObjectAttribute.getType());

                    if(type == CKA_KEY_GEN_MECHANISM || type == CKA_LOCAL){
                        continue;
                    }
                    // RSA public key için.
                    if(type == CKA_MODULUS_BITS){
                        continue;
                    }

                    byte[] value = escObjectAttribute.getValue();
                    CK_ATTRIBUTE ck_attribute = new CK_ATTRIBUTE(type, value);
                    ckAttributes.add(ck_attribute);
                }
                CK_ATTRIBUTE[] ckAttributesAsArray = ckAttributes.toArray(new CK_ATTRIBUTE[0]);

                long objectIDs;
                try {
                    objectIDs = mPKCS11.C_CreateObject(aSessionID, ckAttributesAsArray);
                } catch (PKCS11Exception e) {
                    objectResult.setSuccess(false);
                    objectResult.setReasonIfFail(e.getMessage());
                    objectResults.add(objectResult);
                    continue;
                }

                objectResult.setObjectID(objectIDs);
                objectResult.setSuccess(true);
            }else{
                throw new SmartCardException("Unknown object class");
            }
            objectResults.add(objectResult);
        }
        unwrapObjectsResults.setObjectResult(objectResults);
        return unwrapObjectsResults;
    }

    @Override
    public CK_ATTRIBUTE[] getAllAttributes(long sessionID, long objectID) throws PKCS11Exception {
        long [] types = getAttributeTypes();
        return getAttributeValueInTwoSteps(sessionID, objectID, types);
    }

    public long[] getAttributeTypes(){
        long[] pkcs11AttributeTypes = PKCS11Names.getAttributeTypes();

        Map<Long, String> vendorSpecificAttributeTypesWithNames = mCardType.getCardTemplate().getVendorSpecificAttributeTypesWithNames();

        if(vendorSpecificAttributeTypesWithNames==null){
            return pkcs11AttributeTypes;
        }

        long[] vendorSpecificAttributeTypes = vendorSpecificAttributeTypesWithNames.keySet().stream().mapToLong(Long::longValue).toArray();

        long[] allAttributes = ArrayUtil.concatArrays(pkcs11AttributeTypes, vendorSpecificAttributeTypes);

        return allAttributes;
    }
}
