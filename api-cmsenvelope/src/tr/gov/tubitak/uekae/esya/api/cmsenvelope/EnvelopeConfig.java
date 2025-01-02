package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Mod;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;

import java.io.InputStream;

/**
 * Created by sura.emanet on 17.10.2017.
 */
public class EnvelopeConfig
{
    protected CipherAlg rsaKeyTransAlg = CipherAlg.RSA_OAEP_SHA256;
    protected KeyAgreementAlg ecKeyAgreementAlg =  KeyAgreementAlg.ECDH_SHA256KDF;
    protected ValidationSystem mCertificateValidationSystem;
    protected boolean validateCertificates = true;

    public EnvelopeConfig()
    {

    }


    public CipherAlg getRsaKeyTransAlg()   {
        return rsaKeyTransAlg;
    }

    public EnvelopeConfig setRsaKeyTransAlg(CipherAlg rsaKeyTransAlg) {
        this.rsaKeyTransAlg = rsaKeyTransAlg;
        return this;
    }

    public KeyAgreementAlg getEcKeyAgreementAlg() {
        return ecKeyAgreementAlg;
    }

    public EnvelopeConfig setEcKeyAgreementAlg(KeyAgreementAlg ecKeyAgreementAlg) {
        this.ecKeyAgreementAlg = ecKeyAgreementAlg;
        return this;
    }

    public void setPolicy(String policyFile) throws ESYAException
    {
        ValidationPolicy policy = PolicyReader.readValidationPolicy(policyFile);
        mCertificateValidationSystem =  CertificateValidation.createValidationSystem(policy);
    }

    public void setPolicy(InputStream policyFile) throws ESYAException
    {
        ValidationPolicy policy = PolicyReader.readValidationPolicy(policyFile);
        mCertificateValidationSystem =  CertificateValidation.createValidationSystem(policy);
    }

    public ValidationSystem getValidationSystem() { return mCertificateValidationSystem; }

    public static void checkSymetricAlgoritmIsSafe(CipherAlg aCipherAlg) throws CMSException
    {
        if(aCipherAlg.equals(CipherAlg.DES_EDE3_CBC) || aCipherAlg.equals(CipherAlg.RC2_CBC))
            throw new CMSException("Algorithm is not safe enough");
        if(aCipherAlg.getMod().equals(Mod.ECB))
            throw new CMSException("Mode is not safe enough");
    }

    public void skipCertificateValidation()
    {
        validateCertificates = false;
    }

    public void activateCertificateValidation()
    {
        validateCertificates = true;
    }

    public boolean isCertificateValidationActive()
    {
        return validateCertificates;
    }



}
