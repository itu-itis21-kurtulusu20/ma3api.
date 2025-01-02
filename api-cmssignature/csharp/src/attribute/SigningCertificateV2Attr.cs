using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    public class SigningCertificateV2Attr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_signingCertificateV2;
        private DigestAlg mDigestAlg = DigestAlg.SHA256;


        public SigningCertificateV2Attr()
            : base()
        {
        }

        public override void setValue()
        {
            //tek sertifika olduğunu varsayıyorum

            Object sertifika = null;
            mAttParams.TryGetValue(AllEParameters.P_SIGNING_CERTIFICATE, out sertifika);

            if (sertifika == null)
            {
                Object objSC = null;
                mAttParams.TryGetValue(AllEParameters.P_MOBILE_SIGNER_SIGNING_CERT_ATTRv2, out objSC);
                if (objSC == null)
                    throw new NullParameterException("SIGNING_CERTIFICATE parametre değeri null");
                else
                {
                    SigningCertificateV2 sc = ((ESigningCertificateV2)objSC).getObject();
                    _setValue(sc);
                    return;
                }
            }
            ECertificate cert = null;
            try
            {
                cert = (ECertificate)sertifika;
            }
            catch (InvalidCastException ex)
            {
                throw new CMSSignatureException("SIGNING_CERTIFICATE parametresi ECertificate tipinde değil",
                                       ex);
            }

            Object digestAlgO = null;
            mAttParams.TryGetValue(AllEParameters.P_REFERENCE_DIGEST_ALG, out digestAlgO);
            if (digestAlgO != null)
            {
                try
                {
                    mDigestAlg = (DigestAlg)digestAlgO;
                }
                catch (InvalidCastException ex)
                {
                    throw new CMSSignatureException("P_REFERENCE_DIGEST_ALG parametresi DigestAlg tipinde değil", ex);
                }
            }

            _setValue(_signCertOlustur(new ECertificate[] { cert }, new DigestAlg[] { mDigestAlg }, null));
        }

        private SigningCertificateV2 _signCertOlustur(ECertificate[] aCertificates, DigestAlg[] aDigestAlgs, PolicyInformation[] aPolicies)
        {
            _SeqOfESSCertIDv2 certs = _certSeqOlustur(aCertificates, aDigestAlgs);
            _SeqOfPolicyInformation policies = null;

            if (aPolicies != null && aPolicies.Length != 0)
            {
                policies = new _SeqOfPolicyInformation(aPolicies);
            }

            SigningCertificateV2 sc = new SigningCertificateV2(certs, policies);
            return sc;
        }

        private _SeqOfESSCertIDv2 _certSeqOlustur(ECertificate[] aCertificates, DigestAlg[] aDigestAlgs)
        {
            ESSCertIDv2[] ids = new ESSCertIDv2[aCertificates.Length];
            for (int i = 0; i < aCertificates.Length; i++)
            {
                ECertificate certificate = aCertificates[i];

                Asn1OctetString certHash = new Asn1OctetString(AttributeUtil.createOtherHashValue(certificate.getBytes(), mDigestAlg));

                AlgorithmIdentifier aid = new AlgorithmIdentifier(aDigestAlgs[i].getOID());
                if (aDigestAlgs[i] == DigestAlg.SHA256)
                {
                    aid = null;
                }
                IssuerSerial is_ = AttributeUtil.createIssuerSerial(certificate);

                ids[i] = new ESSCertIDv2(aid, certHash, is_);


            }
            _SeqOfESSCertIDv2 certs = new _SeqOfESSCertIDv2(ids);
            return certs;

        }

        /**
         * Checks whether attribute is signed or not.
         * @return true 
         */ 
        public override bool isSigned()
        {
            return true;
        }
        /**
         * Returns AttributeOID of SigningCertificateV2Attr attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }

    }
}
