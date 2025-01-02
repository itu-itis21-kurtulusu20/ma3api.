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
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class AA2SignParallel
    {
        private static ECertificate CERTIFICATE;
        private static ECertificate CERTIFICATE2;
        //private static BaseSigner BASESIGNER;
        //private static BaseSigner BASESIGNER2;
        //private static PrivateKey PRIVATEKEY;
        //private static PrivateKey PRIVATEKEY2;
        private static String BASEDIR;
        private static String SIGNATUREFILENAME;
        private static String PLAINFILENAME;
        private static String PLAINFILEMIMETYPE;

        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            CERTIFICATE = bpg.getCertificate();
            CERTIFICATE2 = bpg.getCertificate2();
            //BASESIGNER = bpg.getBaseSigner();
            //BASESIGNER2 = bpg.getBaseSigner2();
            //PRIVATEKEY = bpg.getPrivateKey();
            //PRIVATEKEY2 = bpg.getPrivateKey2();
            BASEDIR = bpg.getBaseDir();
            PLAINFILENAME =UnitTestParameters.PLAINFILENAME;
            PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;
        }

        [SetUp]
        public void setUp()
        {
            //SIGNATUREFILENAME = UnitTestParameters.PARALLEL_ENVELOPED_SIG_FILE_NAME;
        }

        [Test]
        public void a_createParallel()
        {
            SIGNATUREFILENAME = UnitTestParameters.PARALLEL_ENVELOPED_SIG_FILE_NAME;
            try
            {
                String plainFilePath = BASEDIR + PLAINFILENAME;
                {
                    FileStream fileStream = File.Open(plainFilePath, FileMode.Open, FileAccess.Read);
                    fileStream.Close();
                }

                Context context = new Context(BASEDIR);
                SignedDocument signatures = new SignedDocument(context);
                Document doc = Resolver.resolve(plainFilePath, context);
                String fragment = signatures.addDocument(doc);

                XMLSignature signature1 = signatures.createSignature();
                signature1.addDocument("#" + fragment, PLAINFILEMIMETYPE, false);
                signature1.addKeyInfo(CERTIFICATE);
                //signature1.sign(BASESIGNER);
                bpg.signWithBaseSigner(signature1);

                XMLSignature signature2 = signatures.createSignature();
                signature2.addDocument("#" + fragment, PLAINFILEMIMETYPE, false);
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
