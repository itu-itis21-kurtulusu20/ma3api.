using System;
using System.IO;
using NUnit.Framework;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;
using SignaturePolicyId = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyId;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class CheckPolicyDigestValues
    {
        private static String BASEDIR;
        private static String SIGNATUREFILENAME;
        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            BASEDIR = bpg.getBaseDir();
        }


        [SetUp]
        public void setUp()
        {
        }

        private bool checkPolicyDigestValue(String baseDir, String fileName) //throws XMLSignatureException, IOException, NoSuchAlgorithmException
        {
            try
            {
                Context context = new Context(baseDir);
                XMLSignature signature = XMLSignature.parse(new FileDocument(new FileInfo(baseDir+fileName)), context);

                SignaturePolicyId policyID = signature.QualifyingProperties.SignedSignatureProperties.SignaturePolicyIdentifier.SignaturePolicyId;

                DigestAlg digestAlg = policyID.DigestMethod.Algorithm;
                byte [] digestValue = policyID.DigestValue;

                String policyFilePath = policyID.PolicyQualifiers[0].URI;
                Document resolvedDoc = Resolver.resolve(policyFilePath, signature.Context);
                Transforms transformer = policyID.Transforms;
                if(transformer!=null)
                {
                    resolvedDoc = transformer.apply(resolvedDoc);
                }
                byte[] digested = DigestUtil.digest(digestAlg, resolvedDoc.Bytes);

                bool testResult = ArrayUtil.Equals(digestValue, digested);

                return testResult;
            }
            catch(Exception e)
            {
                throw e;
            }
        }

        [Test]
         public void a_checkPolicyDigestValueForP2Enveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE2_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void b_checkPolicyDigestValueForP2Enveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE2_ENVELOPED_SIG_FILE_NAME;
            try {
                bool testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void c_checkPolicyDigestValueForP2Detached()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE2_DETACHED_SIG_FILE_NAME;
            try {
                bool testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void d_checkPolicyDigestValueForP3Enveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE3_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void e_checkPolicyDigestValueForP3Enveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE3_ENVELOPED_SIG_FILE_NAME;
            try {
                bool testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void f_checkPolicyDigestValueForP3Detached()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE3_DETACHED_SIG_FILE_NAME;
            try {
                bool testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void g_checkPolicyDigestValueForP4Enveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE4_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void h_checkPolicyDigestValueForP4Enveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE4_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void i_checkPolicyDigestValueForP4Detached()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE4_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
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

        }
    }
}
