using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.config;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test;

namespace ImzaApiTest.src.tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.validation
{
    public class XMLSignatureValidation
    {
        public static void Validate(String directory, String fileName)
        {
            Context context = new Context(directory);
            context.Config = new Config("T:\\api-parent\\resources\\ug\\config\\xmlsignature-config.xml");


            XMLSignature signature = XMLSignature.parse(
                                        new FileDocument(new FileInfo(directory + fileName)),
                                        context);

            // no params, use the certificate in key info
            ValidationResult result = signature.verify();
            //Console.WriteLine(result.toXml());

            if (result.getType() != ValidationResultType.VALID)
            {
                Console.WriteLine(result.toXml());
                Assert.True(result.getType() == ValidationResultType.VALID, "Cant verify " + fileName);
            }

            


            UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
            if (usp != null)
            {
                IList<XMLSignature> counterSignatures = usp.AllCounterSignatures;
                foreach (XMLSignature counterSignature in counterSignatures)
                {
                    ValidationResult counterResult = signature.verify();

                    Console.WriteLine(counterResult.toXml());

                    Assert.True(counterResult.getType() == ValidationResultType.VALID,
                        "Cant verify counter signature" + fileName + " : " + counterSignature.Id);

                }
            }

        }
    }
}
