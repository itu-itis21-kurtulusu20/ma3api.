using System;
using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.config;


namespace test.esya.api.cmssignature.validation
{
    public class ValidationUtil : CMSSignatureTest
    {
        protected static String BASE_DIR = "T:\\api-cmssignature\\testdata\\cmssignature\\";
        protected static String CONFIG_FILE = "T:\\api-parent\\resources\\ug\\config\\esya-signature-config.xml";

        public static void checkByCommonAPI(byte [] signatureBytes) {

            Context context = new Context(new Uri(BASE_DIR));
            context.setConfig(new Config(CONFIG_FILE));

            MemoryStream inputStream = new MemoryStream(signatureBytes);
            SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.CAdES, inputStream, context);

            ContainerValidationResult validationResult = signatureContainer.verifyAll();

            if (validationResult.getResultType() != ContainerValidationResultType.ALL_VALID) {
                throw new Exception(validationResult.ToString());
            }
        }

        public static void checkSignatureIsValid(byte[] signature, ISignable externalContent)
        {
            SignedDataValidationResult sdvr = validate(signature, externalContent);

		    if(sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
			    throw new Exception(sdvr.ToString());
	    }



        public static SignedDataValidationResult validate(byte[] aSignature, ISignable aExternalContent)
        {
            CMSSignatureTest testContants = new CMSSignatureTest();

            //ValidationPolicy policy = PolicyReader.readValidationPolicy("T:\\api-cmssignature\\testdata\\cmssignature\\imza\\policyNoRevoc.xml");
            ValidationPolicy policy = testContants.getPolicy();


            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;
            if (aExternalContent != null)
                params_[EParameters.P_EXTERNAL_CONTENT] = aExternalContent;

            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(aSignature, params_);

            if (sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
                Console.WriteLine("İmza doğrulamadı");

            //assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

            return sdvr;
        }       
        
    }
}