package tr.gov.tubitak.uekae.esya.api.crypto.alg;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import tr.gov.tubitak.uekae.esya.api.asn.algorithms.ERSASSA_PSS_params;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.OID;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.asn.algorithms.RSASSA_PSS_params;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.*;

/**
 * @author ayetgin
 */

public class SignatureAlg implements Algorithm
{
    private static Map<OID, Set<SignatureAlg>> msOidRegistry = new HashMap<OID, Set<SignatureAlg>>();
    private static Map<String, SignatureAlg> msNameRegistry = new HashMap<String, SignatureAlg>();


    public static final byte[] REQUIRED = new byte[]{};

    public static final SignatureAlg ECDSA = new SignatureAlg(_algorithmsValues.id_ecPublicKey, "ECDSA", AsymmetricAlg.ECDSA, null, null);
    public static final SignatureAlg ECDSA_SHA1 = new SignatureAlg(_algorithmsValues.ecdsa_with_SHA1, "ECDSA-with-SHA1", AsymmetricAlg.ECDSA, DigestAlg.SHA1, null);
    public static final SignatureAlg ECDSA_SHA224 = new SignatureAlg(_algorithmsValues.ecdsa_with_Sha224, "ECDSA-with-SHA224", AsymmetricAlg.ECDSA, DigestAlg.SHA224, null);
    public static final SignatureAlg ECDSA_SHA256 = new SignatureAlg(_algorithmsValues.ecdsa_with_Sha256, "ECDSA-with-SHA256", AsymmetricAlg.ECDSA, DigestAlg.SHA256, null);
    public static final SignatureAlg ECDSA_SHA384 = new SignatureAlg(_algorithmsValues.ecdsa_with_Sha384, "ECDSA-with-SHA384", AsymmetricAlg.ECDSA, DigestAlg.SHA384, null);
    public static final SignatureAlg ECDSA_SHA512 = new SignatureAlg(_algorithmsValues.ecdsa_with_Sha512, "ECDSA-with-SHA512", AsymmetricAlg.ECDSA, DigestAlg.SHA512, null);

    public static final SignatureAlg DSA = new SignatureAlg(_algorithmsValues.id_dsa, "DSA", AsymmetricAlg.DSA, null, null);
    public static final SignatureAlg DSA_SHA1 = new SignatureAlg(_algorithmsValues.id_dsa_with_sha1, "DSA-with-SHA1", AsymmetricAlg.DSA, DigestAlg.SHA1, null);
    public static final SignatureAlg DSA_SHA256 = new SignatureAlg(null, "DSA-with-SHA256", AsymmetricAlg.DSA, DigestAlg.SHA256, null);

    
    public static final SignatureAlg RSA_NONE = new SignatureAlg(_algorithmsValues.rsaEncryption, "RSA-with-NONE", AsymmetricAlg.RSA, null, EAlgorithmIdentifier.ASN_NULL);
    //OID'den algoritma bulurken RSA_NONE, RSA_RAW'dan öncelikli olduğu için üst satırda durmalı.
    public static final SignatureAlg RSA_RAW = new SignatureAlg(_algorithmsValues.rsaEncryption, Algorithms.SIGNATURE_RSA_RAW, AsymmetricAlg.RSA, null, EAlgorithmIdentifier.ASN_NULL);
    public static final SignatureAlg RSA_MD5 = new SignatureAlg(_algorithmsValues.md5WithRSAEncryption, "RSA-with-MD5", AsymmetricAlg.RSA, DigestAlg.MD5, EAlgorithmIdentifier.ASN_NULL);
    public static final SignatureAlg RSA_SHA1 = new SignatureAlg(_algorithmsValues.sha1WithRSAEncryption, "RSA-with-SHA1", AsymmetricAlg.RSA, DigestAlg.SHA1, EAlgorithmIdentifier.ASN_NULL);
    public static final SignatureAlg RSA_SHA224 = new SignatureAlg(_algorithmsValues.sha224WithRSAEncryption, "RSA-with-SHA224", AsymmetricAlg.RSA, DigestAlg.SHA224, EAlgorithmIdentifier.ASN_NULL);
    public static final SignatureAlg RSA_SHA256 = new SignatureAlg(_algorithmsValues.sha256WithRSAEncryption, "RSA-with-SHA256", AsymmetricAlg.RSA, DigestAlg.SHA256, EAlgorithmIdentifier.ASN_NULL);
    public static final SignatureAlg RSA_SHA384 = new SignatureAlg(_algorithmsValues.sha384WithRSAEncryption, "RSA-with-SHA384", AsymmetricAlg.RSA, DigestAlg.SHA384, EAlgorithmIdentifier.ASN_NULL);
    public static final SignatureAlg RSA_SHA512 = new SignatureAlg(_algorithmsValues.sha512WithRSAEncryption, "RSA-with-SHA512", AsymmetricAlg.RSA, DigestAlg.SHA512, EAlgorithmIdentifier.ASN_NULL);

    // use RSAPSSParams for RSAPSS variants
    public static final SignatureAlg RSA_PSS = new SignatureAlg(_algorithmsValues.id_RSASSA_PSS, "RSAPSS", AsymmetricAlg.RSA, null, REQUIRED);
    public static final SignatureAlg RSA_PSS_RAW = new SignatureAlg(_algorithmsValues.id_RSASSA_PSS, "RSAPSS-RAW", AsymmetricAlg.RSA, null, REQUIRED);
    //public static final SignatureAlg RSA_PSS_SHA256 = new SignatureAlg(_algorithmsValues.id_RSASSA_PSS, "RSAPSS-with-SHA256", AsymmetricAlg.RSA, DigestAlg.SHA256);
    //public static final SignatureAlg RSA_PSS_SHA512 = new SignatureAlg(_algorithmsValues.id_RSASSA_PSS, "RSAPSS-with-SHA512", AsymmetricAlg.RSA, DigestAlg.SHA512);

    public static final SignatureAlg RSA_ISO9796_2_SHA1 = new SignatureAlg(_algorithmsValues.id_RSASS_ISO9796d2, "RSA-ISO9796-2-with-SHA1", AsymmetricAlg.RSA, DigestAlg.SHA1, null);
    public static final SignatureAlg RSA_ISO9796_2_SHA224 = new SignatureAlg(_algorithmsValues.id_RSASS_ISO9796d2, "RSA-ISO9796-2-with-SHA224", AsymmetricAlg.RSA, DigestAlg.SHA224, null);
    public static final SignatureAlg RSA_ISO9796_2_SHA256 = new SignatureAlg(_algorithmsValues.id_RSASS_ISO9796d2, "RSA-ISO9796-2-with-SHA256", AsymmetricAlg.RSA, DigestAlg.SHA256, null);
    public static final SignatureAlg RSA_ISO9796_2_SHA384 = new SignatureAlg(_algorithmsValues.id_RSASS_ISO9796d2, "RSA-ISO9796-2-with-SHA384", AsymmetricAlg.RSA, DigestAlg.SHA384, null);
    public static final SignatureAlg RSA_ISO9796_2_SHA512 = new SignatureAlg(_algorithmsValues.id_RSASS_ISO9796d2, "RSA-ISO9796-2-with-SHA512", AsymmetricAlg.RSA, DigestAlg.SHA512, null);

    private int[] mOid;
    private String mName;
    private AsymmetricAlg mAsymmetricAlg;
    private DigestAlg mDigestAlg;
    private byte[] mDefaultParams;

    protected SignatureAlg(int[] aOid, String aName, AsymmetricAlg aAsymmetricAlg, DigestAlg aDigestAlg, byte[] aDefaultParams)
    {
        mOid = aOid;
        mName = aName;
        mAsymmetricAlg = aAsymmetricAlg;
        mDigestAlg = aDigestAlg;
        mDefaultParams = aDefaultParams;
        register(this);
    }

    /*public SignatureAlg(int[] aOid, String aName, AsymmetricAlg aAsymmetricAlg, DigestAlg aDigestAlg, AlgorithmParams aParams) {
        this(aOid, aName, aAsymmetricAlg, aDigestAlg);
        mParams = aParams;
    }*/

    private void register(SignatureAlg aSignatureAlg)
    {
        if (aSignatureAlg.getOID() != null) {
            OID oid = new OID(aSignatureAlg.getOID());
            Set<SignatureAlg> algs = msOidRegistry.get(oid);
            if (algs==null){
                msOidRegistry.put(oid, new HashSet<SignatureAlg>(Arrays.asList(aSignatureAlg)));
            }
            else {
                algs.add(aSignatureAlg);
            }

        }
        String algName = aSignatureAlg.getName();
        // kullanicinin turettigi bir algortimanin tanimli olani override etmemesi
        // icin kontrol yaparak ekiyoruz..
        if (!msNameRegistry.containsKey(algName))
            msNameRegistry.put(aSignatureAlg.getName(), this);
    }

    public int[] getOID()
    {
        return mOid;
    }

    public String getName()
    {
        return mName;
    }

    public AsymmetricAlg getAsymmetricAlg()
    {
        return mAsymmetricAlg;
    }

    public DigestAlg getDigestAlg()
    {
        return mDigestAlg;
    }

    public static SignatureAlg fromName(String aSignatureAlgName)
    {
        return msNameRegistry.get(aSignatureAlgName);
    }

    /*
    public static SignatureAlg fromName(String aAsymmetricAlgName, String aDigestAlgName)
            throws ArgErrorException {
        AsymmetricAlg asymmetricAlg = AsymmetricAlg.fromName(aAsymmetricAlgName);
        if (asymmetricAlg == null)
            throw new ArgErrorException("Unknown asymetric alg : " + aAsymmetricAlgName);

        DigestAlg digestAlg = aDigestAlgName == null ? null : DigestAlg.fromName(aDigestAlgName);

        for (SignatureAlg alg : msNameRegistry.values()) {
            if (alg.getAsymmetricAlg().equals(asymmetricAlg) && (alg.getDigestAlg() == digestAlg)) {
                return alg;
            }
        }

        String name = asymmetricAlg.getName();
        if (digestAlg != null)
            name += "-with-" + digestAlg.getName();
        return new SignatureAlg(null, name, asymmetricAlg, digestAlg);
    } */


    private static SignatureAlg fromOID(int[] aOID)
    {
        OID oid = new OID(aOID);
        Set<SignatureAlg> algs = msOidRegistry.get(oid);
        if (algs!=null){
            if (algs.size()==1){
                return algs.iterator().next();
            } else {
                throw new ESYARuntimeException("Multiple signature algorithms for same OID "+oid);
            }
        }
        return null;
    }

    public static Pair<SignatureAlg, AlgorithmParams> fromAlgorithmIdentifier(EAlgorithmIdentifier aAlgoId)
            throws CryptoException
    {
        if (aAlgoId == null)
            return null;
        OID oid = new OID(aAlgoId.getAlgorithm().value);
        if (oid.equals(new OID(_algorithmsValues.id_RSASSA_PSS))) {
            if (aAlgoId.hasParameters())
            {
                try {
                    RSASSA_PSS_params params = new RSASSA_PSS_params();
                    Asn1DerDecodeBuffer decodeBuffer = new Asn1DerDecodeBuffer(aAlgoId.getParameters().value);
                    params.decode(decodeBuffer);

                    int[] hashAlg = params.hashAlgorithm.algorithm.value;
                    DigestAlg digestAlg = DigestAlg.fromOID(hashAlg);

                    MGF mgf = MGF.fromOID(params.maskGenAlgorithm.algorithm.value);


                    RSAPSSParams pssParams = new RSAPSSParams(digestAlg, mgf, (int)params.saltLength.value, (int)params.trailerField.value);
                    return new Pair<SignatureAlg, AlgorithmParams>(RSA_PSS, pssParams);
                }
                catch (Exception x) {
                    throw new CryptoException("Error decoding algorithm Identifier params");
                }
            } // params not null
            return new Pair<SignatureAlg, AlgorithmParams>(RSA_PSS, null);
        }
        // todo RSASS_ISO9796d2
        return new Pair<SignatureAlg, AlgorithmParams>(fromOID(aAlgoId.getAlgorithm().value), null);
    }

    public EAlgorithmIdentifier toAlgorithmIdentifier(AlgorithmParams aParams)
        throws CryptoException
    {
        // todo i18n
        if (mOid ==null){
            throw new CryptoException("OID not known!");
        }

        byte[] params = null;

        if (aParams==null){
            if (Arrays.equals(mDefaultParams, REQUIRED)){
                throw new CryptoException("Algorithm Identifier conversion requires parameters for "+mName);
            }
            params = mDefaultParams;
        }
        else {
            params = aParams.getEncoded();
        }

        return new EAlgorithmIdentifier(mOid, params);
    }

    public EAlgorithmIdentifier toAlgorithmIdentifierFromSpec(AlgorithmParameterSpec spec) throws ESYAException
    {
        // todo i18n
        if (mOid ==null){
            throw new CryptoException("OID not known!");
        }

        byte [] algoParams = null;
        if(spec != null)
        {
            if(spec instanceof AlgorithmParams)
            {
                return toAlgorithmIdentifier((AlgorithmParams)spec);
            }
            if(spec instanceof PSSParameterSpec)
            {
                PSSParameterSpec pssSpec = (PSSParameterSpec)spec;
                algoParams = new ERSASSA_PSS_params(pssSpec).getEncoded();
            }
            return new EAlgorithmIdentifier(mOid,algoParams);
        }
        else
        {
            return toAlgorithmIdentifier(null);
        }
    }


    /*public static SignatureAlg fromSigner(BaseSigner aSigner) throws ArgErrorException {
        return SignatureAlg.fromName(aSigner.getSignatureAsymAlgorithm(), aSigner.getDigestAlgorithm());
    }*/

    @Override
    public int hashCode()
    {
        int result = mAsymmetricAlg != null ? mAsymmetricAlg.hashCode() : 0;
        result = 31 * result + (mDigestAlg != null ? mDigestAlg.hashCode() : 0);
        return result;
    }
    
    
    /**
     * 
     * @return list of Signature Algorithms.
     */
    public static SignatureAlg [] getAlgorithms()
    {
    	return msNameRegistry.values().toArray(new SignatureAlg[0]);
    }


    @Override
    public String toString()
    {
        return mName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignatureAlg that = (SignatureAlg) o;

        if (mAsymmetricAlg != that.mAsymmetricAlg) return false;
        if (mDigestAlg  != that.mDigestAlg) return false;
        return Arrays.equals(mOid, that.getOID());
    }


}
