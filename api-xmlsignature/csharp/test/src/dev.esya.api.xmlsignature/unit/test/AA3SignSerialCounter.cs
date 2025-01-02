using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
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
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class AA3SignSerialCounter
    {
        private static ECertificate CERTIFICATE;
        private static ECertificate CERTIFICATE2;
        private static ECertificate CERTIFICATE3;
        //private static BaseSigner BASESIGNER;
        //private static BaseSigner BASESIGNER2;
        //private static BaseSigner BASESIGNER3;
        //private static PrivateKey PRIVATEKEY;
        //private static PrivateKey PRIVATEKEY2;
        //private static PrivateKey PRIVATEKEY3;
        private static String BASEDIR;
        private static String SIGNATUREFILENAME;
        private static String PLAINFILENAME;
        private static String PLAINFILENAME2;
        private static String PLAINFILEMIMETYPE;
        private static String PLAINFILEMIMETYPE2;
        private static String SIGNATUREFILETOBECOUNTERSIGNED;

        private static int caseNum;
        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            CERTIFICATE = bpg.getCertificate();
            CERTIFICATE2 = bpg.getCertificate2();
            CERTIFICATE3 = bpg.getCertificate3();
            //BASESIGNER = bpg.getBaseSigner();
            //BASESIGNER2 = bpg.getBaseSigner2();
            //BASESIGNER3 = bpg.getBaseSigner3();
            //PRIVATEKEY = bpg.getPrivateKey();
            //PRIVATEKEY2 = bpg.getPrivateKey2();
            //PRIVATEKEY3 = bpg.getPrivateKey3();
            BASEDIR = bpg.getBaseDir();
            PLAINFILENAME =UnitTestParameters.PLAINFILENAME;
            PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;
            PLAINFILENAME2 = UnitTestParameters.PLAINFILENAME2;
            PLAINFILEMIMETYPE2 = UnitTestParameters.PLAINFILE2MIMETYPE;

            caseNum = 0;
        }

        [SetUp]
        public void setUp()
        {
            /*switch(caseNum)
            {
                case 0: SIGNATUREFILENAME = UnitTestParameters.PARALLEL_ENVELOPING_SIG_FILE_NAME; break;
                case 1: SIGNATUREFILENAME = UnitTestParameters.COUNTER_ENVELOPING_SIG_FILE_NAME; break;
                case 2: SIGNATUREFILENAME = UnitTestParameters.CREATED_COUNTER_ON_PARALLEL_SIG_FILE_NAME; break;
                case 3: SIGNATUREFILENAME = UnitTestParameters.COUNTER_ON_PARALLEL_SIG_FILE_NAME; break;
                case 4: SIGNATUREFILENAME = UnitTestParameters.PARALLEL_ADDED_SIG_FILE_NAME; break;
                default: break;
            }
            caseNum++;*/
        }

        //private void createSignature()
        //{
        //    SIGNATUREFILETOBECOUNTERSIGNED = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME;
        //    try {
        //        Context context = new Context(BASEDIR);
        //        XMLSignature signature = new XMLSignature(context);
        //        signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
        //        signature.addKeyInfo(CERTIFICATE);
        //        signature.sign(BASESIGNER);
        //        signature.write(new FileStream(BASEDIR+SIGNATUREFILETOBECOUNTERSIGNED, FileMode.Create));
        //    } catch (XMLSignatureException e) {
        //        Assert.True(false);
        //        SupportClass.WriteStackTrace(e, Console.Error);
        //    } catch (FileNotFoundException e) {
        //        Assert.True(false);
        //        SupportClass.WriteStackTrace(e, Console.Error);
        //    }
        //}

        [Test]
        public void a_createParallelSignature()
        {
            SIGNATUREFILENAME = UnitTestParameters.PARALLEL_ENVELOPING_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                SignedDocument signatures = new SignedDocument(context);

                XMLSignature signature1 = signatures.createSignature();
                signature1.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature1.addDocument(BASEDIR + PLAINFILENAME2, PLAINFILEMIMETYPE2, true);
                signature1.addKeyInfo(CERTIFICATE);
                //signature1.sign(BASESIGNER);
                bpg.signWithBaseSigner(signature1);

                XMLSignature signature2 = signatures.createSignature();
                signature2.addDocument(BASEDIR + PLAINFILENAME2, PLAINFILEMIMETYPE2, true);
                signature2.addKeyInfo(CERTIFICATE2);
                //signature2.sign(BASESIGNER2);
                bpg.signWithBaseSigner2(signature2);
                signatures.write(new FileStream(BASEDIR+SIGNATUREFILENAME, FileMode.Create));
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void b_createCounter()
        {
            SIGNATUREFILENAME = UnitTestParameters.COUNTER_ENVELOPING_SIG_FILE_NAME;
            SIGNATUREFILETOBECOUNTERSIGNED = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                Document doc = Resolver.resolve(BASEDIR + SIGNATUREFILETOBECOUNTERSIGNED, context);
                XMLSignature signature = XMLSignature.parse(doc,context);
                XMLSignature counterSignature = signature.createCounterSignature();
                counterSignature.addKeyInfo(CERTIFICATE3);
                //counterSignature.sign(BASESIGNER3);
                bpg.signWithBaseSigner3(counterSignature);
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));

            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void c_createCounterOnParallelSignature()    //sifirdan olusturuyor butun imzalari
        {
            SIGNATUREFILENAME = UnitTestParameters.CREATED_COUNTER_ON_PARALLEL_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                SignedDocument signatures = new SignedDocument(context);

                XMLSignature signature1 = signatures.createSignature();
                signature1.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature1.addDocument(BASEDIR + PLAINFILENAME2, PLAINFILEMIMETYPE2, true);
                signature1.addKeyInfo(CERTIFICATE);
                //signature1.sign(BASESIGNER);
                bpg.signWithBaseSigner(signature1);

                XMLSignature signature2 = signatures.createSignature();
                signature2.addDocument(BASEDIR + PLAINFILENAME2, PLAINFILEMIMETYPE2, true);
                signature2.addKeyInfo(CERTIFICATE2);
                //signature2.sign(BASESIGNER2);
                bpg.signWithBaseSigner2(signature2);

                XMLSignature signatureToBeCounterSigned = signatures.getSignature(0);
                XMLSignature counterSignature = signatureToBeCounterSigned.createCounterSignature();
                counterSignature.addKeyInfo(CERTIFICATE3);
                //counterSignature.sign(BASESIGNER3);
                bpg.signWithBaseSigner3(counterSignature);
                signatures.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        //mevcut bir paralel imza dosyasına counter signature ekleme
        [Test]
        public void d_addCounterSignatureToParallelSignedSignature()
        {
            SIGNATUREFILENAME = UnitTestParameters.COUNTER_ON_PARALLEL_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                Document doc = Resolver.resolve(BASEDIR + UnitTestParameters.PARALLEL_ENVELOPING_SIG_FILE_NAME, context);
                SignedDocument signatures = new SignedDocument(doc,context);

                XMLSignature signature = signatures.getSignature(0);
                XMLSignature counterSignature = signature.createCounterSignature();
                counterSignature.addKeyInfo(CERTIFICATE3);
                //counterSignature.sign(BASESIGNER3);
                bpg.signWithBaseSigner3(counterSignature);
                signatures.write(new FileStream(BASEDIR+SIGNATUREFILENAME,FileMode.Create));
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        //mevcut imzaya paralel imza ekliyor
        //[Test]
        public void e_addParallelSignatureToSignatureFile()
        {
            try
            {
                Context context = new Context(BASEDIR);
                Document doc = Resolver.resolve(BASEDIR + UnitTestParameters.COUNTER_ON_PARALLEL_SIG_FILE_NAME, context);
                SignedDocument signatures = new SignedDocument(doc, context);

                XMLSignature signature = signatures.createSignature();
                signature.addDocument(PLAINFILENAME2, PLAINFILEMIMETYPE2, true);
                signature.addKeyInfo(CERTIFICATE3);
                //signature.sign(BASESIGNER3);
                bpg.signWithBaseSigner3(signature);
                signatures.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
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
            //bpg.logoutFromSmartCard();
        }
    }
}
