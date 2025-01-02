using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.asn.algorithms;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.algorithms;


/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class SignatureAlg : IAlgorithm
    {
        public static readonly byte[] REQUIRED = new byte[]{};

        private static Dictionary<OID, HashSet<SignatureAlg>> msOidRegistry = new Dictionary<OID, HashSet<SignatureAlg>>();
        private static Dictionary<String, SignatureAlg> msNameRegistry = new Dictionary<String, SignatureAlg>();

        public static readonly SignatureAlg ECDSA = new SignatureAlg(_algorithmsValues.id_ecPublicKey, "ECDSA", AsymmetricAlg.ECDSA, null, null);
        public static readonly SignatureAlg ECDSA_SHA1 = new SignatureAlg(_algorithmsValues.ecdsa_with_SHA1, "ECDSA-with-SHA1", AsymmetricAlg.ECDSA, DigestAlg.SHA1, null);
        public static readonly SignatureAlg ECDSA_SHA224 = new SignatureAlg(_algorithmsValues.ecdsa_with_Sha224, "ECDSA-with-SHA224", AsymmetricAlg.ECDSA, DigestAlg.SHA224, null);
        public static readonly SignatureAlg ECDSA_SHA256 = new SignatureAlg(_algorithmsValues.ecdsa_with_Sha256, "ECDSA-with-SHA256", AsymmetricAlg.ECDSA, DigestAlg.SHA256, null);
        public static readonly SignatureAlg ECDSA_SHA384 = new SignatureAlg(_algorithmsValues.ecdsa_with_Sha384, "ECDSA-with-SHA384", AsymmetricAlg.ECDSA, DigestAlg.SHA384, null);
        public static readonly SignatureAlg ECDSA_SHA512 = new SignatureAlg(_algorithmsValues.ecdsa_with_Sha512, "ECDSA-with-SHA512", AsymmetricAlg.ECDSA, DigestAlg.SHA512, null);

        public static readonly SignatureAlg DSA = new SignatureAlg(_algorithmsValues.id_dsa, "DSA", AsymmetricAlg.DSA, null, null);
        public static readonly SignatureAlg DSA_SHA1 = new SignatureAlg(_algorithmsValues.id_dsa_with_sha1, "DSA-with-SHA1", AsymmetricAlg.DSA, DigestAlg.SHA1, null);
        public static readonly SignatureAlg DSA_SHA256 = new SignatureAlg(null, "DSA-with-SHA256", AsymmetricAlg.DSA, DigestAlg.SHA256, null);
     
        public static readonly SignatureAlg RSA_NONE = new SignatureAlg(_algorithmsValues.rsaEncryption, "RSA-with-NONE", AsymmetricAlg.RSA, null, EAlgorithmIdentifier.ASN_NULL);
        public static readonly SignatureAlg RSA_RAW = new SignatureAlg(_algorithmsValues.rsaEncryption, Algorithms.SIGNATURE_RSA_RAW, AsymmetricAlg.RSA, null, EAlgorithmIdentifier.ASN_NULL);
        public static readonly SignatureAlg RSA_MD5 = new SignatureAlg(_algorithmsValues.md5WithRSAEncryption, "RSA-with-MD5", AsymmetricAlg.RSA, DigestAlg.MD5, EAlgorithmIdentifier.ASN_NULL);
        public static readonly SignatureAlg RSA_SHA1 = new SignatureAlg(_algorithmsValues.sha1WithRSAEncryption, "RSA-with-SHA1", AsymmetricAlg.RSA, DigestAlg.SHA1, EAlgorithmIdentifier.ASN_NULL);
        public static readonly SignatureAlg RSA_SHA224 = new SignatureAlg(_algorithmsValues.sha224WithRSAEncryption, "RSA-with-SHA224", AsymmetricAlg.RSA, DigestAlg.SHA224, EAlgorithmIdentifier.ASN_NULL);
        public static readonly SignatureAlg RSA_SHA256 = new SignatureAlg(_algorithmsValues.sha256WithRSAEncryption, "RSA-with-SHA256", AsymmetricAlg.RSA, DigestAlg.SHA256, EAlgorithmIdentifier.ASN_NULL);
        public static readonly SignatureAlg RSA_SHA384 = new SignatureAlg(_algorithmsValues.sha384WithRSAEncryption, "RSA-with-SHA384", AsymmetricAlg.RSA, DigestAlg.SHA384, EAlgorithmIdentifier.ASN_NULL);
        public static readonly SignatureAlg RSA_SHA512 = new SignatureAlg(_algorithmsValues.sha512WithRSAEncryption, "RSA-with-SHA512", AsymmetricAlg.RSA, DigestAlg.SHA512, EAlgorithmIdentifier.ASN_NULL);

        // use RSAPSSParams for RSAPSS variants
        public static readonly SignatureAlg RSA_PSS = new SignatureAlg(_algorithmsValues.id_RSASSA_PSS, "RSAPSS", AsymmetricAlg.RSA, null, REQUIRED);
        //public static final SignatureAlg RSA_PSS_SHA256 = new SignatureAlg(_algorithmsValues.id_RSASSA_PSS, "RSAPSS-with-SHA256", AsymmetricAlg.RSA, DigestAlg.SHA256);
        //public static final SignatureAlg RSA_PSS_SHA512 = new SignatureAlg(_algorithmsValues.id_RSASSA_PSS, "RSAPSS-with-SHA512", AsymmetricAlg.RSA, DigestAlg.SHA512);

        public static readonly SignatureAlg RSA_ISO9796_2_SHA1 = new SignatureAlg(_algorithmsValues.id_RSASS_ISO9796d2, "RSA-ISO9796-2-with-SHA1", AsymmetricAlg.RSA, DigestAlg.SHA1, null);
        public static readonly SignatureAlg RSA_ISO9796_2_SHA224 = new SignatureAlg(null, "RSA-ISO9796-2-with-SHA224", AsymmetricAlg.RSA, DigestAlg.SHA224, null);
        public static readonly SignatureAlg RSA_ISO9796_2_SHA256 = new SignatureAlg(null, "RSA-ISO9796-2-with-SHA256", AsymmetricAlg.RSA, DigestAlg.SHA256, null);
        public static readonly SignatureAlg RSA_ISO9796_2_SHA384 = new SignatureAlg(null, "RSA-ISO9796-2-with-SHA384", AsymmetricAlg.RSA, DigestAlg.SHA384, null);
        public static readonly SignatureAlg RSA_ISO9796_2_SHA512 = new SignatureAlg(null, "RSA-ISO9796-2-with-SHA512", AsymmetricAlg.RSA, DigestAlg.SHA512, null);
        

        private readonly int[] mOid;
        private readonly String mName;
        private readonly AsymmetricAlg mAsymmetricAlg;
        private readonly DigestAlg mDigestAlg;
        private readonly byte[] mDefaultParams;

        private SignatureAlg(int[] aOid, String aName, AsymmetricAlg aAsymmetricAlg, DigestAlg aDigestAlg, byte[] aDefaultParams)
        {
            mOid = aOid;
            mName = aName;
            mAsymmetricAlg = aAsymmetricAlg;
            mDigestAlg = aDigestAlg;
            mDefaultParams = aDefaultParams;
            register(this);
        }

        private void register(SignatureAlg aSignatureAlg)
        {
            if (aSignatureAlg.getOID() != null)
            {
                OID oid = new OID(aSignatureAlg.getOID());
                HashSet<SignatureAlg> algs = null;
                msOidRegistry.TryGetValue(oid, out algs);
                if (algs == null)
                {
                    msOidRegistry[oid] = new HashSet<SignatureAlg>(new SignatureAlg[] { aSignatureAlg });
                }
                else
                {
                    algs.Add(aSignatureAlg);
                }

            }
            String algName = aSignatureAlg.getName();
            // kullanicinin turettigi bir algortimanin tanimli olani override etmemesi
            // icin kontrol yaparak ekiyoruz..
            if (!msNameRegistry.ContainsKey(algName))
                msNameRegistry[aSignatureAlg.getName()] = this;
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
            SignatureAlg signatureAlg = null;
            msNameRegistry.TryGetValue(aSignatureAlgName, out signatureAlg);
            return signatureAlg;
        }


        /**
         * 
         * returns list of Signature Algorithms
         */
        public static SignatureAlg[] getAlgorithms()
        {
            return msNameRegistry.Values.ToArray();
        }

        /*
        public static SignatureAlg fromName(String aAsymmetricAlgName, String aDigestAlgName)
        {
            AsymmetricAlg asymmetricAlg = AsymmetricAlg.fromName(aAsymmetricAlgName);
            if (asymmetricAlg == null)
                throw new ArgErrorException("Unknown asymetric alg : " + aAsymmetricAlgName);

            DigestAlg digestAlg = aDigestAlgName == null ? null : DigestAlg.fromName(aDigestAlgName);

            foreach (SignatureAlg alg in msNameRegistry.Values)
            {
                if (alg.getAsymmetricAlg().Equals(asymmetricAlg) && (alg.getDigestAlg() == digestAlg))
                {
                    return alg;
                }
            }

            String name = asymmetricAlg.getName();
            if (digestAlg != null)
                name += "-with-" + digestAlg.getName();
            return new SignatureAlg(null, name, asymmetricAlg, digestAlg);
        }*/

        public EAlgorithmIdentifier toAlgorithmIdentifier(IAlgorithmParams aParams)
        {
            // todo i18n
            if (mOid ==null)
            {
                throw new CryptoException("OID not known!");
            }

             byte[] parameters;

            if (aParams==null)
            {
                if (ArrayUtil.Equals(mDefaultParams,REQUIRED))                                            
                {
                    throw new CryptoException("Algorithm Identifier conversion requires parameters for "+mName);
                }
                parameters = mDefaultParams;
            }
            else 
            {
                parameters = aParams.getEncoded();
            }
            return new EAlgorithmIdentifier(getOID(), parameters);
        }

        public EAlgorithmIdentifier ToAlgorithmIdentifierFromSpec(IAlgorithmParameterSpec spec)
        {
     
        if (mOid ==null){
                throw new CryptoException("OID not known!");
            }

        if(spec != null)
        {
                if (spec is IAlgorithmParams)           
                  return toAlgorithmIdentifier((IAlgorithmParams)spec);
                
                else
                  return new EAlgorithmIdentifier(mOid, null);
        }
        else
        {
            return toAlgorithmIdentifier(null);
        }
    }

        private static SignatureAlg fromOID(int[] aOID)
        {
            OID oid = new OID(aOID);
            HashSet<SignatureAlg> algs = null;
            msOidRegistry.TryGetValue(oid, out algs);
            if (algs != null)
            {
                if (algs.Count == 1)
                {
                    return algs.ElementAt(0);
                }
                else
                {
                    throw new ESYARuntimeException("Multiple signature algorithms for same OID " + oid);
                }
            }
            return null;
        }


        public static Pair<SignatureAlg, IAlgorithmParams> fromAlgorithmIdentifier(EAlgorithmIdentifier aAlgoId)
        {
            if (aAlgoId == null)
                return null;
            OID oid = new OID(aAlgoId.getAlgorithm().mValue);
            if (oid.Equals(new OID(_algorithmsValues.id_RSASSA_PSS)))
            {
                //throw new CryptoException("PSS not supported");
                if (aAlgoId.hasParameters())
                {

                    try
                    {
                        RSASSA_PSS_params params_ = new RSASSA_PSS_params();
                        Asn1DerDecodeBuffer decodeBuffer = new Asn1DerDecodeBuffer(aAlgoId.getParameters().mValue);
                        params_.Decode(decodeBuffer);

                        int[] hashAlg = params_.hashAlgorithm.algorithm.mValue;
                        DigestAlg digestAlg = DigestAlg.fromOID(hashAlg);

                        MGF mgf = MGF.fromOID(params_.maskGenAlgorithm.algorithm.mValue);

                        RSAPSSParams pssParams = new RSAPSSParams(digestAlg, mgf, (int)params_.saltLength.mValue, (int)params_.trailerField.mValue);
                        return new Pair<SignatureAlg, IAlgorithmParams>(RSA_PSS, pssParams);
                    }
                    catch (Exception x)
                    {
                        throw new CryptoException("Error decoding algorithm Identifier params");
                    }
                } // params not null
                return new Pair<SignatureAlg, IAlgorithmParams>(RSA_PSS, null);
            }
            //todo RSASS_ISO9796d2
            return new Pair<SignatureAlg, IAlgorithmParams>(fromOID(aAlgoId.getAlgorithm().mValue), null);
        }


        public override int GetHashCode()
        {
            //return mOid.GetHashCode() ^ mName.GetHashCode() ^ mAsymmetricAlg.GetHashCode() ^ mDigestAlg.GetHashCode();
            int result = mAsymmetricAlg != null ? mAsymmetricAlg.GetHashCode() : 0;
            result = 31 * result + (mDigestAlg != null ? mDigestAlg.GetHashCode() : 0);
            return result;
        }

        //@Override
        public override String ToString()
        {
            return mName;
        }

        public override bool Equals(object obj)
        {
            if (this == obj) return true;

            // If parameter is null return false.
            if (obj == null)
            {
                return false;
            }

            // If parameter cannot be cast to Point return false.
            SignatureAlg that = obj as SignatureAlg;
            return Equals(that);
        }

        public bool Equals(SignatureAlg p)
        {
            // If parameter is null return false:
            if (p == null)
            {
                return false;
            }

            // Return true if the fields match:
            if (mAsymmetricAlg != p.mAsymmetricAlg) return false;
            if (mDigestAlg != p.mDigestAlg) return false;
            return mOid.SequenceEqual(p.getOID());
        }


    }
}
