using System;
using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.config;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

namespace test.esya.api.xmlsignature.validation
{
    public class XMLValidationUtil
    {
        public static String CONFIG_FILE = "T:\\api-parent\\resources\\ug\\config\\xmlsignature-config.xml";

        public static void checkSignatureIsValid(String directory, String file)
        {
            byte [] signatureBytes = File.ReadAllBytes(file);
            checkSignatureIsValid(directory, signatureBytes);
        }


        public static void checkSignatureIsValid(String directory, byte[] xmlDocumentBytes) 
        {
            InMemoryDocument xmlDocument = new InMemoryDocument(xmlDocumentBytes, directory,
                    "application/xml", null);

            ValidationResult validationResult = validate(directory, xmlDocument);
            
            if (validationResult.getType() != ValidationResultType.VALID)
            {
                throw new Exception(validationResult.ToString());
            }
            
        }

        private static ValidationResult validate(string directory, Document xmlDocument)
        {
            Context context = new Context(directory);
            context.Config = new Config(CONFIG_FILE);

            XMLSignature signature = XMLSignature.parse(xmlDocument, context);
            ValidationResult result = signature.verify();

            return result;
        }
    }
}
