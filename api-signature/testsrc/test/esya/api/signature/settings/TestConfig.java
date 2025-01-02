package test.esya.api.signature.settings;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFormat;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertValidationPolicies;

/**
 * @author ayetgin
 */
public class TestConfig
{
    private static final String POLICY_CRL_ONLY = "certval-ug-policy-crlOnly.xml";
    private static final String POLICY_OCSP_ONLY = "certval-ug-policy-ocspOnly.xml";

    SignatureFormat format;
    String baseDir;

    CertValidationPolicies crlPolicies = new CertValidationPolicies();
    CertValidationPolicies ocspPolicies = new CertValidationPolicies();

    public TestConfig(SignatureFormat aFormat, String aBaseDir) throws Exception
    {
        format = aFormat;
        baseDir = aBaseDir;

        crlPolicies.register(null, PolicyReader.readValidationPolicy(aBaseDir+"/../../testresources/"+POLICY_CRL_ONLY));
        ocspPolicies.register(null, PolicyReader.readValidationPolicy(aBaseDir+"/../../testresources/"+POLICY_OCSP_ONLY));
    }

    public SignatureFormat getFormat()
    {
        return format;
    }

    public void setFormat(SignatureFormat aFormat)
    {
        format = aFormat;
    }

    public String getPath(String fileName){
        String ext = "";
        switch (format){
            case XAdES      : ext = ".xml"; break;
            case CAdES      : ext = ".p7s"; break;
            default: throw new RuntimeException();
        }

        return baseDir+"/"+ fileName+ext;
    }

    public CertValidationPolicies getCRLOnlyPolicies(){
        return crlPolicies;
    }

    public CertValidationPolicies getOCSPOnlyPolicies(){
        return ocspPolicies;
    }
}
