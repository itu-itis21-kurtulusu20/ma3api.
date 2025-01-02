using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope
{
    public class EnvelopeConfig
    {
        private CipherAlg rsaKeyTransAlg;
        private KeyAgreementAlg ecKeyAgreementAlg;
        private ValidationSystem mCertificateValidationSystem;
        private bool mValidateCertificates;


        public EnvelopeConfig()
        {
          rsaKeyTransAlg = CipherAlg.RSA_OAEP_SHA256;
          ecKeyAgreementAlg = KeyAgreementAlg.ECDH_SHA256KDF;
          mValidateCertificates = true;
        }

        public CipherAlg getRsaKeyTransAlg()
        {
            return rsaKeyTransAlg;
        }

        public EnvelopeConfig setRsaKeyTransAlg(CipherAlg rsaKeyTransAlg)
        {
            this.rsaKeyTransAlg = rsaKeyTransAlg;
            return this;
        }

        public KeyAgreementAlg getEcKeyAgreementAlg()
        {
            return ecKeyAgreementAlg;
        }

        public EnvelopeConfig setEcKeyAgreementAlg(KeyAgreementAlg ecKeyAgreementAlg)
        {
            this.ecKeyAgreementAlg = ecKeyAgreementAlg;
            return this;
        }

        public void setPolicy(String policyFile)
        {
            ValidationPolicy policy = PolicyReader.readValidationPolicy(policyFile);
            mCertificateValidationSystem = CertificateValidation.createValidationSystem(policy);
        }

        public void setPolicy(Stream policyFile) 
        {
            ValidationPolicy policy = PolicyReader.readValidationPolicy(policyFile);
            mCertificateValidationSystem =  CertificateValidation.createValidationSystem(policy);
        }

        public ValidationSystem getValidationSystem()
        {
            return mCertificateValidationSystem;
        }

        public static void checkSymmetricAlgorithmIsSafe(CipherAlg aCipherAlg)
        {
            if (aCipherAlg.Equals(CipherAlg.DES_EDE3_CBC) || aCipherAlg.Equals(CipherAlg.RC2_CBC))
                throw new CMSException("Algorithm is not safe enough");
            if (aCipherAlg.getMod().Equals(Mod.ECB))
                throw new CMSException("Mode is not safe enough");
        }

        public void skipCertificateValidation()
        {
            mValidateCertificates = false;
        }

        public void activateCertificateValidation()
        {
            mValidateCertificates = true;
        }

        public bool isCertificateValidationActive()
        {
            return mValidateCertificates;
        }
    }
}
