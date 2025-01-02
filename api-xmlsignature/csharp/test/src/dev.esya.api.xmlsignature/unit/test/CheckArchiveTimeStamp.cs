using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using NUnit;
using NUnit.Framework;
using NUnit.Framework.Constraints;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class CheckArchiveTimeStamp
    {
        private static ECertificate CERTIFICATE;
        //private static PrivateKey PRIVATEKEY;
        //private static BaseSigner BASESIGNER;
        private static String BASEDIR;
        private static String SIGNATUREFILENAME;

        private static int caseNum;
        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            //CERTIFICATE = bpg.getCertificate();
            //PRIVATEKEY = bpg.getPrivateKey();
            //BASESIGNER = bpg.getBaseSigner();
            BASEDIR = bpg.getBaseDir();

            caseNum=0;
        }

        [SetUp]
        public void setUp()
        {
            /*switch (caseNum)
            {
                case 0 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME; break;
                case 1 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME; break;
                case 2 : SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME; break;
                default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
            }
            caseNum++;*/
        }

        private static bool checkEncapsulatedTS(EncapsulatedTimeStamp encapsulatedTS)
        {
            try
            {
                if(!encapsulatedTS.TimeStamp)
            {
                return false;
            }

            String digestAlg = encapsulatedTS.DigestAlgorithm.getName();
            byte [] digestValue = encapsulatedTS.DigestValue;

            byte [] contentByteArry = encapsulatedTS.SignedData.getEncapsulatedContentInfo().getContent();

    //        encapsulatedTS.getElement().getNodeValue()
    //        String str = encapsulatedTS.getElement().getFirstChild().getNodeValue();
    //        byte [] decodedTSByteArry = Base64.decode(new String(contentByteArry));

    //        MessageDigest digester = MessageDigest.getInstance(digestAlg);
    //        byte [] tsDigestValue = digester.digest(contentByteArry);
    //
    //        if(!Arrays.equals(digestValue,tsDigestValue))
    //        {
    //            return false;
    //        }

            return true;
            }
            catch(XMLSignatureException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                throw e;
            }
            
        }

        private bool checkArchiveTS(ArchiveTimeStamp archiveTS)
        {
            try
            {
                int encapsulatedTS = archiveTS.EncapsulatedTimeStampCount;
            for(int i=0;i<encapsulatedTS;i++)
            {
                if(!checkEncapsulatedTS(archiveTS.getEncapsulatedTimeStamp(i)))
                {
                    return false;
                }
            }
            return true;
            }
            catch(XMLSignatureException e)
            {
                throw e;
            }
            
        }

        public bool checkArchiveTimeStamp(String baseDir, String fileName)
        {
            try
            {
                XMLSignature signature = XMLSignature.parse(
                                    new FileDocument(new FileInfo(baseDir + fileName)),
                                    new Context(baseDir));

                int archiveTSCount = signature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties.ArchiveTimeStampCount;

                for (int i = 0; i < archiveTSCount; i++)
                {
                    if (!checkArchiveTS(signature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties.getArchiveTimeStamp(i)))
                    {
                        return false;
                    }
                }

                return true;
            }
            catch (Exception e)
            {
                
                throw e;
            }
        }

        [Test]
        public void a_checkSignatureValueForESAEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkArchiveTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "ArchiveTimeStampCheckResult");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (CryptoException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (ESYAException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (InvalidKeyException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (SignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void b_checkSignatureValueForESAEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
            try {
                bool testResult = checkArchiveTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "ArchiveTimeStampCheckResult");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (CryptoException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (ESYAException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (InvalidKeyException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (SignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void c_checkSignatureValueForESADetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
            try {
                bool testResult = checkArchiveTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "ArchiveTimeStampCheckResult");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (CryptoException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (ESYAException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (InvalidKeyException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (SignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
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
