using System;
using System.Collections.Generic;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using System.Xml;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using NUnit;
using NUnit.Framework;
using NUnit.Framework.Constraints;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Utilities;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;
using Base64 = Org.BouncyCastle.Utilities.Encoders.Base64;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class CheckSignedObjectDigestValues
    {
        //private static ECertificate CERTIFICATE;
        //private static BaseSigner BASESIGNER;
        //private static PrivateKey PRIVATEKEY;
        private static String BASEDIR;
        private static String SIGNATUREFILENAME;

        private static int caseNum;
        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            //CERTIFICATE = bpg.getCertificate();
            //BASESIGNER = bpg.getBaseSigner();
            //PRIVATEKEY = bpg.getPrivateKey();
            BASEDIR = bpg.getBaseDir();

            caseNum=0;
        }

        [SetUp]
        public void setUp()
        {
            /*switch (caseNum)
            {
                case 0 : SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME; break;
                case 1 : SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME; break;
                case 2 : SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME; break;
                case 3 : SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME; break;
                case 4 : SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME; break;
                case 5 : SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_SIG_FILE_NAME; break;
                case 6 : SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME; break;
                case 7 : SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME; break;
                case 8 : SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME; break;
                case 9 : SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME; break;
                case 10 : SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME; break;
                case 11 : SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME; break;
                case 12 : SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME; break;
                case 13 : SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME; break;
                case 14 : SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME; break;
                case 15 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME; break;
                case 16 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME; break;
                case 17 : SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME; break;
                case 18: SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME; break;
                default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
            }
            caseNum++;*/
        }

        private String getDigestMethodString(DigestAlg digestAlg)
        {
            String rtr;
            if(digestAlg == DigestAlg.SHA1)
            {
                rtr = "SHA-1";
            }
            else if(digestAlg == DigestAlg.SHA224)
            {
                rtr = "SHA-224";
            }
            else if(digestAlg == DigestAlg.SHA256)
            {
                rtr = "SHA-256";
            }
            else if(digestAlg == DigestAlg.SHA384)
            {
                rtr = "SHA-384";
            }
            else if(digestAlg == DigestAlg.SHA512)
            {
                rtr = "SHA-512";
            }
            else
            {
                rtr = "Unknown";
            }
            return rtr;
        }

        private bool checkEnvelopingFileDigest(String baseDir, String fileName) //throws XMLSignatureException, CryptoException, NoSuchAlgorithmException, IOException 
        {
            try 
	        {	        
		        XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new FileInfo(baseDir+fileName)),
                    new Context(baseDir)) ;

                int referenceCount = signature.SignedInfo.ReferenceCount;
                for(int i=1;i<referenceCount;i++)
                {
                    DigestAlg digestAlg = signature.SignedInfo.getReference(i).DigestMethod.Algorithm;
                    byte [] digestValue = signature.SignedInfo.getReference(i).DigestValue;

                    XMLObject obj = signature.getObject(i);
                    String base64icerik = obj.Element.FirstChild.Value;

                    DOMDocument domDoc = new DOMDocument(obj.Element, null);
                    Document transformedDocument = domDoc.getTransformedDocument(signature.SignedInfo.CanonicalizationMethod);
                    byte[] c14ned = transformedDocument.Bytes;
                    String c14nedstr = c14ned.ToString();//new String(c14ned);

                    byte[] digested = DigestUtil.digest(digestAlg, c14ned);

                    String dig64c = Base64.Encode(digested).ToString();

                    bool testResult = ArrayUtil.Equals(digestValue, digested);

                    if(!testResult)
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

        private bool checkEnvelopedFileDigest(String baseDir, String fileName) //throws XMLSignatureException, CryptoException, NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException 
        {
            try 
	        {	        
		        XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new FileInfo(baseDir+fileName)),
                    new Context(baseDir)) ;

                XmlDocument document = new XmlDocument();
                document.Load(baseDir+fileName);
                document.DocumentElement.Normalize();

                XmlNode rootNode = document.DocumentElement;

                int referenceCount = signature.SignedInfo.ReferenceCount;
                for(int i=1;i<referenceCount;i++)
                {
                    Reference reference = signature.SignedInfo.getReference(i);
                    DigestAlg digestAlg = reference.DigestMethod.Algorithm;
                    byte[] digestValue = reference.DigestValue;

                    String digestStr = Convert.ToBase64String(digestValue);

                    DOMDocument domDoc = (DOMDocument)reference.getReferencedDocument();
                    /*
                    XmlElement signatureElement = signature.Element;
                    XmlNode referencedNode = rootNode.ChildNodes.Item(i);
                    DOMDocument domDoc = new DOMDocument(referencedNode, null);                     
                     * */
                    Document transformedDocument = reference.getTransformedDocument(signature.SignedInfo.CanonicalizationMethod);
                   // Document transformedDocument = domDoc.getTransformedDocument(signature.SignedInfo.CanonicalizationMethod);
                    String docstr = transformedDocument.ToString();
                    byte[] c14ned = transformedDocument.Bytes;
                    String str123 = Convert.ToBase64String(c14ned);
                    byte[] digested = DigestUtil.digest(digestAlg, c14ned);

                    bool testResult = ArrayUtil.Equals(digestValue, digested);

                    if(!testResult)
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

        private bool checkDetachedFileDigest(String baseDir, String fileName) //throws XMLSignatureException, CryptoException, NoSuchAlgorithmException, IOException 
        {
            try
            {
                XMLSignature signature = XMLSignature.parse(
                            new FileDocument(new FileInfo(baseDir + fileName)),
                            new Context(baseDir));

                int referenceCount = signature.SignedInfo.ReferenceCount;
                for (int i = 1; i < referenceCount; i++)
                {
                    DigestAlg digestAlg = signature.SignedInfo.getReference(i).DigestMethod.Algorithm;
                    byte[] digestValue = signature.SignedInfo.getReference(i).DigestValue;

                    String fileURI= signature.SignedInfo.getReference(i).URI;
                    String filePath;
                    if(fileURI.Contains(":\\")) //full path
                    {
                        filePath = fileURI;
                    }
                    else   //relative path
                    {
                        filePath = baseDir + signature.SignedInfo.getReference(i).URI.Substring(1);
                    }

                    StreamReader reader = new StreamReader(filePath);
                    String fileContentStr = reader.ReadToEnd();
                    byte[] fileContent = Encoding.ASCII.GetBytes(fileContentStr);
                    byte[] digested = DigestUtil.digest(digestAlg, fileContent);
                    reader.Close();

                    bool testResult = ArrayUtil.Equals(digestValue, digested);

                    if (!testResult)
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
        public void a_checkSignedObjectForBESEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkEnvelopingFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (CryptoException e) {
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
        public void b_checkSignedObjectForBESEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void c_checkSignedObjectForBESDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkDetachedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void d_checkSignedObjectForESTEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopingFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void e_checkSignedObjectForESTEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void f_checkSignedObjectForESTDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkDetachedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void g_checkSignedObjectForESCEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopingFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void h_checkSignedObjectForESCEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void i_checkSignedObjectForESCDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkDetachedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void j_checkSignedObjectForESXEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopingFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void k_checkSignedObjectForESXEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void l_checkSignedObjectForESXDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkDetachedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void m_checkSignedObjectForESXLEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopingFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void n_checkSignedObjectForESXLEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void o_checkSignedObjectForESXLDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkDetachedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void p_checkSignedObjectForESAEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopingFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void r_checkSignedObjectForESAEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void s_checkSignedObjectForESADetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkDetachedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
        public void t_checkSignedObjectForBESEDefterEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkEnvelopedFileDigest(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignedPropertiesDigestValueCheck");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
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
            //bpg.logoutFromSmartCard();
        }
    }
}
