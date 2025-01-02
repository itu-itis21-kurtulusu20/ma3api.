using System;
using System.Collections.Generic;
using test.esya.api.cmssignature.testconstants;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;

namespace test.esya.api.cmssignature
{
    public class CMSSignatureTest
    {
        public TestConstants msTestConstants = new UGTestConstants();

        public BaseSigner getSignerInterface(SignatureAlg aAlg)
        {
            return msTestConstants.getSignerInterface(aAlg);
        }

        public BaseSigner getSignerInterface(SignatureAlg aAlg, IAlgorithmParams algorithmParams)
        {
            return msTestConstants.getSignerInterface(aAlg, algorithmParams);
        }


        public BaseSigner getSecondSignerInterface(SignatureAlg aAlg)
        {
            return msTestConstants.getSecondSignerInterface(aAlg);
        }

        public BaseSigner getSecondSignerInterface(SignatureAlg aAlg, IAlgorithmParams algorithmParams)
        {
            return msTestConstants.getSecondSignerInterface(aAlg, algorithmParams);
        }

        public BaseSigner getECSignerInterface(SignatureAlg aAlg)
        {
            return msTestConstants.getECSignerInterface(aAlg);
        }

        public ECertificate getSignerCertificate()
        {
            return msTestConstants.getSignerCertificate();
        }

        public IPrivateKey getSignerPrivateKey()
        {
            return msTestConstants.getSignerPrivateKey();
        }

        public ECertificate getSecondSignerCertificate()
        {
            return msTestConstants.getSecondSignerCertificate();
        }

        public ECertificate getECSignerCertificate()
        {
            return msTestConstants.getECSignerCertificate();
        }

        public ValidationPolicy getPolicy()
        {
            return msTestConstants.getPolicy();
        }

        public ValidationPolicy getPolicyCRL()
        {
            return msTestConstants.getPolicyCRL();
        }

        public ValidationPolicy getPolicyOCSP()
        {
            return msTestConstants.getPolicyOCSP();
        }

        public ValidationPolicy getPolicyNoOCSPNoCRL()
        {
            return msTestConstants.getPolicyNoOCSPNoCRL();
        }

        public TSSettings getTSSettings()
        {
            return msTestConstants.getTSSettings();
        }

        public String getDirectory()
        {
            return msTestConstants.getDirectory();
        }

        public ISignable getSimpleContent()
        {
            return msTestConstants.getSimpleContent();
        }

        public ISignable getHugeContent()
        {
            return msTestConstants.getHugeContent();
        }


        public byte[] CreateBasicCMSSignature()
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, parameters);

            return bs.getEncoded();
        }


    }
}
