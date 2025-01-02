package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.Registry;
import gnu.crypto.agreement.BaseAgreement;
import gnu.crypto.agreement.ECCofactorDHAgreement;
import gnu.crypto.agreement.ECDHAgreement;
import gnu.crypto.derivationFunctions.DerivationFunction;
import gnu.crypto.derivationFunctions.MGF1;
import gnu.crypto.derivationFunctions.X9_63KeyDerivation;
import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.sig.ISignature;
import gnu.crypto.sig.dss.DSSSignature;
import gnu.crypto.sig.ecdsa.ECDSASignature;
import gnu.crypto.sig.rsa.RSAISO9796d2Signature;
import gnu.crypto.sig.rsa.RSAPKCS1V1_5Signature;
import gnu.crypto.sig.rsa.RSAPSSSignature;
import gnu.crypto.sig.rsa.RSA_RAW;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoRuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.UnknownElement;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */
class GNUProviderUtil
{
    private static Map<String, String> mEsya2GnuCipherNames = new HashMap<String, String>();


    static {
        mEsya2GnuCipherNames.put("AES128",  Registry.AES_CIPHER);
        mEsya2GnuCipherNames.put("AES192",  Registry.AES_CIPHER);
        mEsya2GnuCipherNames.put("AES256",  Registry.AES_CIPHER);
        mEsya2GnuCipherNames.put("DES",     Registry.DES_CIPHER);
        mEsya2GnuCipherNames.put("3DES",    Registry.DESEDE_CIPHER);
        mEsya2GnuCipherNames.put("RSA",     Registry.RSA_KPG);
        mEsya2GnuCipherNames.put("RC2",     Registry.RC2_CIPHER);
    }

    static String namePadding (Padding aPadding) throws CryptoException
    {
        if (aPadding==Padding.PKCS7){
            return Registry.PKCS7_PAD;
        }else if(aPadding == Padding.NONE)
            return Registry.NONE_PAD;

        throw new UnknownElement(GenelDil.mesaj(GenelDil.PADDING_BILINMIYOR));
    }


    static String nameAlgorithm(CipherAlg aAlg) throws CryptoException
    {
        String name = aAlg.getName();
        int index  = name.indexOf('/');
        if (index>0)
            name = aAlg.getName().substring(0, index);
        return mEsya2GnuCipherNames.get(name);
    }


    static String nameMod(Mod aMod) throws CryptoException
    {
        switch (aMod){
            case CBC : return Registry.CBC_MODE;
            case CFB : return Registry.CFB_MODE;
            case ECB : return Registry.ECB_MODE;
            case OFB : return Registry.OFB_MODE;
            case GCM : return Registry.GCM_MODE;
        }
        throw new UnknownElement(GenelDil.mesaj(GenelDil.SIMMOD_BILINMIYOR));
    }

    static String resolveMacName(MACAlg aMACAlg){

        String rawName = aMACAlg.getName().toLowerCase();
        String macType = rawName.substring(0, rawName.indexOf('-'));
        String digestAlg = resolveDigestName(aMACAlg.getDigestAlg());
        return macType+'-'+digestAlg;
    }

    static String resolveDigestName(DigestAlg aOzetAlg)
    {
        if (aOzetAlg==null)
            return Registry.NONE_HASH;
        switch (aOzetAlg){
            case SHA1   :   return Registry.SHA_1_HASH;
            case SHA224 :   return Registry.SHA224_HASH;
            case SHA256 :   return Registry.SHA256_HASH;
            case SHA384 :   return Registry.SHA384_HASH;
            case SHA512 :   return Registry.SHA512_HASH;
            case MD5    :   return Registry.MD5_HASH;
            case RIPEMD :   return Registry.RIPEMD160_HASH;
        }
        throw new CryptoRuntimeException(GenelDil.mesaj(GenelDil.OZETALG_0_BILINMIYOR, new String[]{aOzetAlg.getName()}));
    }


    static ISignature resolveSignature(SignatureAlg aSignatureAlg, AlgorithmParams aParams) throws CryptoException
    {
        String messageDigestName = resolveDigestName(aSignatureAlg.getDigestAlg());
        AsymmetricAlg asymmetricAlg =  aSignatureAlg.getAsymmetricAlg();
        String signAlgName = aSignatureAlg.getName();
        if(aSignatureAlg == SignatureAlg.RSA_RAW)
        {
            return new RSA_RAW();
        }

        if (asymmetricAlg==AsymmetricAlg.ECDSA){
            return new ECDSASignature(messageDigestName);
        }
        else if (asymmetricAlg==AsymmetricAlg.DSA){
            return new DSSSignature();
        }
        else if (signAlgName.startsWith("RSA-ISO9796")){
            return new RSAISO9796d2Signature(messageDigestName,true);
        }
        else if (signAlgName.startsWith("RSAPSS")){
            if(signAlgName.equals(SignatureAlg.RSA_PSS_RAW.getName())) {
                throw new CryptoException("GNUProvider does not support RSA_PSS_RAW");
            }

            if (aParams!=null && !(aParams instanceof RSAPSSParams))
                throw new ArgErrorException("Invalid algorithm params. Expected "+RSAPSSParams.class.getSimpleName()+", found: "+aParams);
            // todo tum parametreleri goz onune al
            RSAPSSParams params = (aParams==null) ? new RSAPSSParams() : (RSAPSSParams)aParams;

            if (params.getMGF() != MGF.MGF1)
                throw new CryptoException("For GNUProvider, RSAPSS parameter MGF type must be MGF1!");

            String digestName = GNUProviderUtil.resolveDigestName(params.getDigestAlg());
            return new RSAPSSSignature(digestName, params.getSaltLength());
        }
        if (asymmetricAlg==AsymmetricAlg.RSA) {
            return new RSAPKCS1V1_5Signature(messageDigestName);
        }

        throw new UnknownElement(GenelDil.mesaj(GenelDil.OZETALG_0_BILINMIYOR, new String[]{aSignatureAlg.getName()}));
    }

    static BaseAgreement resolveAgreement(AgreementAlg aAgreementAlg) throws CryptoException
    {
        if(aAgreementAlg == AgreementAlg.ECDH)
            return new ECDHAgreement();
        else if(aAgreementAlg == AgreementAlg.ECCDH)
            return new ECCofactorDHAgreement();
        throw new UnknownElement(GenelDil.mesaj(GenelDil.AGREEMENTALG_BILINMIYOR));
    }

    static DerivationFunction resolveDerivationFunction(DerivationFunctionAlg aDerivationFunctionAlg) throws CryptoException
    {
        String digestName = resolveDigestName(aDerivationFunctionAlg.getDigestAlg());
        IMessageDigest digest = HashFactory.getInstance(digestName);
        if(digest == null)
            throw new UnknownElement(GenelDil.mesaj(GenelDil.OZETALG_0_BILINMIYOR));

        if(aDerivationFunctionAlg.getFunctionType().equals(DerivationFunctionType.ECDHKEK))
            return new X9_63KeyDerivation(digest);
        if(aDerivationFunctionAlg.getFunctionType().equals(DerivationFunctionType.MGF1))
            return new MGF1(digest);

        throw new UnknownElement(GenelDil.mesaj(GenelDil.DERIVATIONALG_BILINMIYOR));
    }

    public static PrivateKey resolvePrivateKey(PrivateKey aPrivateKey) throws CryptoException
    {
        if(aPrivateKey.getClass().getName().equals("sun.security.ec.ECPrivateKeyImpl"))
        {
            return new GNUKeyFactory().decodePrivateKey(AsymmetricAlg.ECDSA, aPrivateKey.getEncoded());
        }

        return aPrivateKey;
    }

    public static void main(String[] args) throws Exception
    {
        System.out.println(""+nameAlgorithm(CipherAlg.AES128_CBC));
        System.out.println(""+nameAlgorithm(CipherAlg.RSA_OAEP));
        System.out.println(""+nameAlgorithm(CipherAlg.RC2_CBC));
    }
}
