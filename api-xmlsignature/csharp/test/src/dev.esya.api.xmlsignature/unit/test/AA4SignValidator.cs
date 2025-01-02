using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Xml;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using NUnit;
using NUnit.Framework;
using NUnit.Framework.Constraints;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class AA4SignValidator
    {
        private static IResolver POLICY_RESOLVER;
        private static List<String> signatureFiles;
        private static String BASEDIR;
        private static ECertificate CERTIFICATE;
        private static ECertificate CERTIFICATEWRONG;

        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            BASEDIR = bpg.getBaseDir();
            POLICY_RESOLVER = bpg.getPolicyResolver();
            signatureFiles = new List<String>();
        }

        [SetUp]
        public void setUp()
        {
            signatureFiles.Clear();

            signatureFiles.Add(UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.BES_DETACHED_SIG_FILE_NAME);

            signatureFiles.Add(UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.EST_DETACHED_SIG_FILE_NAME);

            signatureFiles.Add(UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME);

            signatureFiles.Add(UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME);

            signatureFiles.Add(UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME);

            signatureFiles.Add(UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME);

            signatureFiles.Add(UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.ESA_ARCHIVE_TS_DETACHED_SIG_FILE_NAME);

            //signatureFiles.Add(UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME);

            signatureFiles.Add(UnitTestParameters.PARALLEL_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.PARALLEL_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.COUNTER_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.COUNTER_ON_PARALLEL_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.CREATED_COUNTER_ON_PARALLEL_SIG_FILE_NAME);

            signatureFiles.Add(UnitTestParameters.PROFILE2_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.PROFILE2_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.PROFILE2_DETACHED_SIG_FILE_NAME);

            signatureFiles.Add(UnitTestParameters.PROFILE3_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.PROFILE3_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.PROFILE3_DETACHED_SIG_FILE_NAME);

            signatureFiles.Add(UnitTestParameters.PROFILE4_ENVELOPING_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.PROFILE4_ENVELOPED_SIG_FILE_NAME);
            signatureFiles.Add(UnitTestParameters.PROFILE4_DETACHED_SIG_FILE_NAME);
            
        }


        private bool validate(String fileName) //throws  Exception 
        {

            try
            {
                Context context = new Context(BASEDIR);
                context.addExternalResolver(POLICY_RESOLVER);
		        XMLSignature signature = XMLSignature.parse(new FileDocument(new FileInfo(BASEDIR+fileName)), context);

                // no params, use the certificate in key info
                ValidationResult result = signature.verify();
                System.Console.WriteLine(result.toXml());
                Assert.True(result.getType() == ValidationResultType.VALID,
                            "Cant verify " + fileName);

                if (result.getType() != ValidationResultType.VALID)
                {
                    return false;
                }

                UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
                if (usp!=null){
                    IList<XMLSignature> counterSignatures = usp.AllCounterSignatures;
                    foreach (XMLSignature counterSignature in counterSignatures){
                        ValidationResult counterResult = signature.verify();

                        System.Console.WriteLine(counterResult.toXml());

                        Assert.True(counterResult.getType() == ValidationResultType.VALID,
                                    "Cant verify counter signature" + fileName + " : "+counterSignature.Id);
                        if (result.getType() != ValidationResultType.VALID)
                        {
                            return false;
                        }

                    }
                }
	        }
	        catch (Exception e)
	        {
	            return false;
		        throw e;
	        }
            return true;
        }

        [Test]
        public void validateSignature()
        {
            bool testResult = true;
            foreach (String signatureFile in signatureFiles)
            {
                System.Console.WriteLine("Dosya Adı = "+signatureFile);
                if (File.Exists((BASEDIR + signatureFile)))
                {
                    try
                    {
                        testResult = validate(signatureFile);
                        if (!testResult)
                        {
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        SupportClass.WriteStackTrace(e, Console.Error);
                        continue;
                    }
                }
                else
                {
                    continue;
                }
            }
            Assert.True(testResult);
        }


        private bool validateWithCertificate(String fileName, ECertificate certificate) //throws  Exception 
        {
            try 
	        {
                System.Console.WriteLine("Dosya Adı = " + fileName);
		        XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new FileInfo(BASEDIR+fileName)),
                    new Context(BASEDIR)) ;

                
                ValidationResult result = signature.verify(certificate);
                System.Console.WriteLine(result.toXml());
                Assert.True(result.getType() == ValidationResultType.VALID,
                                  "Cant verify " + fileName);
                if (result.getType() != ValidationResultType.VALID)
                {
                    return false;
                }

                UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
                if (usp!=null){
                    IList<XMLSignature> counterSignatures = usp.AllCounterSignatures;
                    foreach (XMLSignature counterSignature in counterSignatures){
                        ValidationResult counterResult = signature.verify();

                        System.Console.WriteLine(counterResult.toXml());

                        Assert.True(counterResult.getType() == ValidationResultType.VALID,
                                    "Cant verify counter signature" + fileName + " : "+counterSignature.Id);

                        if (counterResult.getType() != ValidationResultType.VALID)
                        {
                            return false;
                        }
                       }
                  }
	          }
	          catch (Exception e)
	          {
	            return false;
		        throw e;
	          }
            return true;
        }

        //[Test]
        public void validateSignaturesWithCertificate()
        {
            bool testResult = true;
            foreach (String signatureFile in signatureFiles)
            {
                if(File.Exists((BASEDIR + signatureFile)))
                {
                    try
                    {
                        testResult = validateWithCertificate(signatureFile,CERTIFICATE);
                        if (!testResult)
                        {
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        SupportClass.WriteStackTrace(e, Console.Error);
                        continue;
                    }
                }
                else
                {
                    continue;
                }
                
            }
            Assert.True(testResult);
        }

        //[Test]
        public void validateSignaturesWithWrongCertificate()
        {
            bool testResult = false;
            foreach (String signatureFile in signatureFiles)
            {
                if (File.Exists((BASEDIR + signatureFile)))
                {
                    try
                    {
                        testResult = validateWithCertificate(signatureFile, CERTIFICATEWRONG);
                        if (testResult)
                        {
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        SupportClass.WriteStackTrace(e, Console.Error);
                        continue;
                    }
                }
            }
            Assert.False(testResult);
        }

        [TearDown]
        public void tearDown()
        {

        }

        [TestFixtureTearDown]
        public static void finish()
        {
            //bpg.logoutFromSmartCard();
        }
    }
}
