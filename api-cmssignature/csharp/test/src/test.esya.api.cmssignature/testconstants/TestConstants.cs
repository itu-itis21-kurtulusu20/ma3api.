using System;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;

namespace test.esya.api.cmssignature.testconstants
{
    public interface TestConstants
    {
        BaseSigner getSignerInterface(SignatureAlg aAlg);
        BaseSigner getSignerInterface(SignatureAlg aAlg, IAlgorithmParams algorithmParams);
        BaseSigner getSecondSignerInterface(SignatureAlg aAlg);
        BaseSigner getSecondSignerInterface(SignatureAlg aAlg, IAlgorithmParams algorithmParams);
        BaseSigner getECSignerInterface(SignatureAlg aAlg);

        ECertificate getSignerCertificate();
    
        IPrivateKey getSignerPrivateKey();

        ECertificate getSecondSignerCertificate();
        
        ECertificate getECSignerCertificate();

        ValidationPolicy getPolicy();

        ValidationPolicy getPolicyCRL();

        ValidationPolicy getPolicyOCSP();

        ValidationPolicy getPolicyNoOCSPNoCRL();

        TSSettings getTSSettings();

        String getDirectory();

        ISignable getSimpleContent();

        ISignable getHugeContent();
    }
}