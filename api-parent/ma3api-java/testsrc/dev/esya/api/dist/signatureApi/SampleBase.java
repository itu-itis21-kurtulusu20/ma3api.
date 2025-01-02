package dev.esya.api.dist.signatureApi;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertValidationPolicies;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignableBytes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SampleBase {

    public static SignatureFormat signatureFormat = SignatureFormat.CAdES;
    public static String ROOT = "";
    private static final String PIN = "12345";

    //Comment tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker for POLICY_CRL_ONLY.
    private static final String POLICY_CRL_ONLY = "./config/certval-policy-test.xml";
    private static final String POLICY_OCSP_ONLY = "./config/certval-policy-test.xml";
    private static CertValidationPolicies crlPolicies = new CertValidationPolicies();
    private static CertValidationPolicies ocspPolicies = new CertValidationPolicies();

    public static Context createContext() {
        Context c = new Context(new File("./testdata").toURI());
        System.out.println("Base URI of context: " + c.getBaseURI().toString());
        c.setConfig(new Config("./config/esya-signature-config.xml"));
        c.setData(getContent()); //for detached CAdES signatures validation
        return c;
    }

    public static Signable getContent() {
        return new SignableBytes("test data".getBytes(), "data.txt", "text/plain");
    }

    public static boolean getCheckQCStatement() {
        //return false;   //Unqualified
        return true;   //Qualified
    }

    //Get PIN from user
    public static String getPIN() {
        return PIN;
    }

    public static String getPath(String fileName) {
        String ext = "";
        switch (signatureFormat) {
            case XAdES:
                ext = ".xml";
                break;
            case CAdES:
                ext = ".p7s";
                break;
            case PAdES:
                ext = ".pdf";
                break;
            default:
                throw new RuntimeException();
        }

        return "./testdata/" + fileName + ext;
    }

    public static void dosyaYaz(SignatureContainer sc, String fileName) throws Exception {

        FileOutputStream fis = new FileOutputStream(getPath(fileName));
        sc.write(fis);
        fis.close();
    }

    public static SignatureContainer readSignatureContainer(String fileName) throws Exception {
        return readSignatureContainer(fileName, createContext());
    }

    public static SignatureContainer readSignatureContainer(String fileName, Context c) throws Exception {
        FileInputStream fis = new FileInputStream(getPath(fileName));
        SignatureContainer container = SignatureFactory.readContainer(fis, c);
        fis.close();
        return container;
    }

    public static CertValidationPolicies getCRLOnlyPolicy() {
        try {
            crlPolicies.register(null, PolicyReader.readValidationPolicy(POLICY_CRL_ONLY));
        } catch (ESYAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return crlPolicies;
    }

    public static CertValidationPolicies getOCSPOnlyPolicy() {
        try {
            ocspPolicies.register(null, PolicyReader.readValidationPolicy(POLICY_OCSP_ONLY));
        } catch (ESYAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ocspPolicies;
    }
}
