package test.esya.api.xmlsignature.validation;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertValidationPolicies;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.InMemoryDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.OfflineResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLValidationUtil {
    public static String POLICY_FILE = "T:\\api-parent\\resources\\ug\\config\\certval-policy-test.xml";
    public static String ROOT_DIR = "T:\\api-parent";
    public static String CONFIG_FILE = "T:\\api-parent\\resources\\ug\\config\\xmlsignature-config.xml";
    public static OfflineResolver POLICY_RESOLVER;

    static
    {

        POLICY_RESOLVER = new OfflineResolver();
        POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.1.1", ROOT_DIR + "/resources/profiller/Elektronik_Imza_Kullanim_Profilleri_Rehberi.pdf", "text/plain");
        POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.2.1", ROOT_DIR + "/resources/profiller/Elektronik_Imza_Kullanim_Profilleri_Rehberi.pdf", "text/plain");
        POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.3.1", ROOT_DIR + "/resources/profiller/Elektronik_Imza_Kullanim_Profilleri_Rehberi.pdf", "text/plain");
    }


    public static void checkSignatureIsValid(String directory, String signatureFileName) throws  Exception {

        List<ValidationResult> validationResultList = validate(directory,  new FileDocument(new File(signatureFileName)));

        for (ValidationResult validationResult : validationResultList) {
            if(validationResult.getType() != ValidationResultType.VALID){
                throw new Exception(validationResult.toString());
            }
        }

    }

    public static void checkSignatureIsValid(String directory, Document xmlDocument) throws  Exception {

        List<ValidationResult> validationResultList = validate(directory,  xmlDocument);

        for (ValidationResult validationResult : validationResultList) {
            if(validationResult.getType() != ValidationResultType.VALID){
                throw new Exception(validationResult.toString());
            }
        }

    }

    public static void checkSignatureIsValid(String directory, byte [] xmlDocumentBytes) throws  Exception {

        InMemoryDocument xmlDocument = new InMemoryDocument(xmlDocumentBytes, directory,
                "application/xml", null);

        List<ValidationResult> validationResultList = validate(directory,  xmlDocument);

        for (ValidationResult validationResult : validationResultList) {
            if(validationResult.getType() != ValidationResultType.VALID){
                throw new Exception(validationResult.toString());
            }
        }

    }


    public static List<ValidationResult> validate(String directory, Document xmlDocument) throws  Exception {
        List<ValidationResult> validationResultList = new ArrayList<ValidationResult>();

        Context context = new Context(directory);
        context.setConfig(new Config(CONFIG_FILE));

        ValidationPolicy policy = PolicyReader.readValidationPolicy(POLICY_FILE);

        CertValidationPolicies policies = new CertValidationPolicies();
        // null means default
        policies.register(null,policy);

        context.getConfig().getValidationConfig().setCertValidationPolicies(policies);

        // add external resolver to resolve policies
        context.addExternalResolver(POLICY_RESOLVER);

        XMLSignature signature = XMLSignature.parse(xmlDocument, context);

        ValidationResult result = signature.verify();

        validationResultList.add(result);

        UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp!=null){
            List<XMLSignature> counterSignatures = usp.getAllCounterSignatures();
            for (XMLSignature counterSignature : counterSignatures){
                ValidationResult counterResult = counterSignature.verify();

                validationResultList.add(counterResult);
           }
        }

        return validationResultList;
    }


}
