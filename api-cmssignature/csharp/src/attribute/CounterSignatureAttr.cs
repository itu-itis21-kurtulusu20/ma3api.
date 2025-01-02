using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.asn.cms;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    public class CounterSignatureAttr : AttributeValue
    {
        /**
      * only attributes that are allowed in a
      * counterSignature attribute are
      * counterSignature, messageDigest,
      signingTime, and signingCertificate.
      */
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_countersignature;
        private static readonly int DEFAULT_SIGNERINFO_VERSION = 1;

        public CounterSignatureAttr()
            : base()
        {
        }

        public CounterSignatureAttr(SignerInfo aCounter)
            : base()
        {
            _setValue(aCounter);
        }

        public override void setValue()
        {
            Object sertifika = null;
            mAttParams.TryGetValue(AllEParameters.P_COUNTER_SIGNATURE_CERTIFICATE, out sertifika);
            if (sertifika == null)
                throw new NullParameterException("P_COUNTER_SIGNATURE_CERTIFICATE parametre degeri null");

            Object imzaci = null;
            mAttParams.TryGetValue(AllEParameters.P_COUNTER_SIGNATURE_SIGNER_INTERFACE, out imzaci);
            if (imzaci == null)
                throw new NullParameterException("P_COUNTER_SIGNATURE_SIGNER_INTERFACE parametre degeri null");

            Object signature = null;
            mAttParams.TryGetValue(AllEParameters.P_SIGNATURE, out signature);
            if (signature == null)
                throw new NullParameterException("P_SIGNATURE parametre degeri null");

            ECertificate cer = null;
            try
            {
                cer = (ECertificate)sertifika;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_COUNTER_SIGNATURE_CERTIFICATE parametresi ECertificate tipinde degil", aEx);
            }

            BaseSigner imzaciI = null;
            try
            {
                imzaciI = (BaseSigner)imzaci;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_COUNTER_SIGNATURE_SIGNER_INTERFACE parametre degeri AY_BasitImzaci tipinde degil", aEx);
            }

            byte[] signatureBA = null;
            try
            {
                signatureBA = (byte[])signature;
            }
            catch (InvalidCastException aEx)
            {
                throw new CMSSignatureException("P_SIGNATURE parametre degeri byte[] tipinde degil", aEx);
            }

            ESignerInfo signerInfo = null;
            try
            {
                signerInfo = _signerInfoOlustur(cer, imzaciI, signatureBA);
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Verilen parametrelerle CounterSignature icin imzaci olusturulamadi.", aEx);
            }

            _setValue(signerInfo.getObject());
        }

        private ESignerInfo _signerInfoOlustur(ECertificate aCer, BaseSigner aImzaci, byte[] aContent)
        {
            //SignatureAlg signatureAlg = SignatureAlg.fromName(aImzaci.getSignatureAsymAlgorithm(), aImzaci.getDigestAlgorithm());
            SignatureAlg signatureAlg = SignatureAlg.fromName(aImzaci.getSignatureAlgorithmStr());

            DigestAlg digestAlg = signatureAlg.getDigestAlg();

            ESignerInfo si = new ESignerInfo(new SignerInfo());

            si.setVersion(DEFAULT_SIGNERINFO_VERSION);

            ESignerIdentifier sid = new ESignerIdentifier(new SignerIdentifier());
            sid.setIssuerAndSerialNumber(new EIssuerAndSerialNumber(aCer));
            
            si.setSignerIdentifier(sid);
            si.setDigestAlgorithm(digestAlg.toAlgorithmIdentifier());
            si.setSignatureAlgorithm(signatureAlg.toAlgorithmIdentifier(null));
		    
            byte[] imza;
            try
            {
                imza = aImzaci.sign(aContent);
            }
            catch (ESYAException e)
            {
                throw new CryptoException("Can not sign data", e);
            }

            si.setSignature(imza);

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[AllEParameters.P_CONTENT] = new SignableByteArray(aContent);
            params_[AllEParameters.P_DIGEST_ALGORITHM] = digestAlg;
            params_[AllEParameters.P_SIGNING_CERTIFICATE] = aCer;

            MessageDigestAttr md = new MessageDigestAttr();
            md.setParameters(params_);
            md.setValue();

            IAttribute sc = null;
            if (digestAlg.Equals(DigestAlg.SHA1))
            {
                sc = new SigningCertificateAttr();
            }
            else
            {
                sc = new SigningCertificateV2Attr();
            }
            sc.setParameters(params_);
            sc.setValue();

            si.addSignedAttribute(md.getAttribute());
            si.addSignedAttribute(sc.getAttribute());

            return si;
        }
        /**
         * Checks whether attribute is signed or not.
         * @return false 
         */ 
        public override bool isSigned()
        {
            return false;
        }
        /**
         * Returns AttributeOID of CounterSignatureAttr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
    }
}
