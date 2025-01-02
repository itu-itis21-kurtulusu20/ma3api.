

using System;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.certval;

namespace test.esya.api.signature.settings
{
    public class TestConfig
    {
        private static readonly String POLICY_CRL_ONLY = "certval-ug-policy-crlOnly.xml";
        private static readonly String POLICY_OCSP_ONLY = "certval-ug-policy-ocspOnly.xml";

        SignatureFormat format;
        String baseDir;

        CertValidationPolicies crlPolicies = new CertValidationPolicies();
        CertValidationPolicies ocspPolicies = new CertValidationPolicies();

        public TestConfig(SignatureFormat aFormat, String aBaseDir)
        {
            format = aFormat;
            baseDir = aBaseDir;

            crlPolicies.register(null, PolicyReader.readValidationPolicy(aBaseDir + "/../../testresources/" + POLICY_CRL_ONLY));
            ocspPolicies.register(null, PolicyReader.readValidationPolicy(aBaseDir + "/../../testresources/" + POLICY_OCSP_ONLY));
        }

        public SignatureFormat getFormat()
        {
            return format;
        }

        public void setFormat(SignatureFormat aFormat)
        {
            format = aFormat;
        }

        public String getPath(String fileName)
        {
            String ext = "";
            switch (format)
            {
                case SignatureFormat.XAdES: ext = ".xml"; break;
                case SignatureFormat.CAdES: ext = ".p7s"; break;
                default: throw new SystemException();
            }

            return baseDir + "/" + fileName + ext;
        }

        public CertValidationPolicies getCRLOnlyPolicies()
        {
            return crlPolicies;
        }

        public CertValidationPolicies getOCSPOnlyPolicies()
        {
            return ocspPolicies;
        }
    }
}
