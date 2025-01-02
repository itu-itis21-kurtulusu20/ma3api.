using System;
using System.Collections.Generic;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using NUnit;
using NUnit.Framework;
using NUnit.Framework.Constraints;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Utilities;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;
using Base64 = Org.BouncyCastle.Utilities.Encoders.Base64;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class CheckSignatureValue
    {
        private static ECertificate CERTIFICATE;
        private static ECertificate CERTIFICATE2;
        private static ECertificate CERTIFICATE3;
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
            CERTIFICATE = bpg.getCertificate();
            CERTIFICATE2 = bpg.getCertificate2();
            CERTIFICATE3 = bpg.getCertificate3();
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
                case 18 : SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME; break;
                default : SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
            }
            caseNum++;*/
        }

        private String getDigestMethodString(DigestAlg digestAlg)
        {
            String rtr;
            if(digestAlg == DigestAlg.SHA1)
            {
                rtr = "SHA1";
            }
            else if(digestAlg == DigestAlg.SHA224)
            {
                rtr = "SHA224";
            }
            else if(digestAlg == DigestAlg.SHA256)
            {
                rtr = "SHA256";
            }
            else if(digestAlg == DigestAlg.SHA384)
            {
                rtr = "SHA384";
            }
            else if(digestAlg == DigestAlg.SHA512)
            {
                rtr = "SHA512";
            }
            else
            {
                rtr = "Unknown";
            }
            return rtr;
        }

        private String getSignatureAlgorithmString(IAlgorithm algorithm, DigestAlg digestAlg)
        {
            String algorithmString = getDigestMethodString(digestAlg);
            algorithmString+="with";


            if(algorithm.getName().Contains("RSA"))
            {
                algorithmString+="RSA";
            }
            else if(algorithm.getName().Contains("ECDSA"))
            {
                algorithmString+="ECDSA";
            }
            else if(algorithm.getName().Contains("HMAC"))
            {
                algorithmString+="HMAC";
            }

            return algorithmString;
        }

        private BaseSigner getBaseSigner(ECertificate certificate)
        {
            String serialNumber = certificate.getSerialNumber().ToString();

            if (serialNumber.Equals("2772") || serialNumber.Equals("2529")) //yavuz 1st for pfx 2nd for smart card
            {
                return bpg._getSigner(UnitTestParameters.SMARTCARD_SLOTNO0, UnitTestParameters.SMARTCARD_SLOTNO0);
            }
            else if (serialNumber.Equals("2831"))    //suleyman
            {
                return bpg._getSigner(UnitTestParameters.SMARTCARD_SLOTNO1, UnitTestParameters.SMARTCARD_SLOTNO1);
            }
            else if (serialNumber.Equals("2829"))    //beytullah
            {
                return bpg._getSigner(UnitTestParameters.SMARTCARD_SLOTNO2, UnitTestParameters.SMARTCARD_SLOTNO2);
            }
            else if(serialNumber.Equals("2770"))    //yavuz revoked
            {
                return bpg._getSigner(UnitTestParameters.SMARTCARD_SLOTNO_REVOKED, UnitTestParameters.SMARTCARD_SLOTNO_REVOKED);
            }
            else
            {
                return null;
            }
        }

        /*private byte [] toByteArray(InputStream is) throws IOException {
            int nRead;
            byte [] data = new byte[1024];
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            while((nRead =is.read(data,0,data.length))!=-1)
            {
                buffer.write(data,0,nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        }

        private byte [] getPlainFileContentForEnveloping(XMLSignature signature, String baseDir) throws IOException {
            int referenceCount = signature.getSignedInfo().getReferenceCount();
            byte [] fileContent = null;
            for(int i=1;i<referenceCount;i++)
            {
                XMLObject obj = signature.getObject(i);
                String base64icerik = obj.getElement().getFirstChild().getNodeValue();
                fileContent = Base64.decode(base64icerik);
            }
            return toByteArray(new ByteArrayInputStream(fileContent));
        }

        private byte [] getPlainFileContentForEnveloped(XMLSignature signature, String baseDir) throws FileNotFoundException {
            return null;
        }

        private byte [] getPlainFileContentForDetached(XMLSignature signature, String baseDir) throws IOException {
            int referenceCount = signature.getSignedInfo().getReferenceCount();
            String filePath = null;
            for(int i=1;i<referenceCount;i++)
            {
                filePath = baseDir+signature.getSignedInfo().getReference(i).getURI().substring(2);
                break;
            }
            return toByteArray(new FileInputStream(filePath));
        }*/

        private bool checkSignatureValue(String baseDir, String fileName) //throws ESYAException, NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException 
        {
            try
            {
                XMLSignature signature = XMLSignature.parse(
                            new FileDocument(new FileInfo(baseDir + fileName)),
                            new Context(baseDir));

                ECertificate certInSignature = null;
                byte[] certBytes = null;
                KeyInfo keyInfo = signature.KeyInfo;
                for (int i = 0; i < keyInfo.ElementCount; i++)
                {
                    KeyInfoElement keyInfoElement = keyInfo.get(i);
                    if(keyInfoElement.GetType().IsAssignableFrom(typeof(X509Data)))
                    {
                        X509Data data = (X509Data)keyInfo.get(i);
                        for (int j = 0; j < data.ElementCount; j++)
                        {
                            X509DataElement xde = data.get(j);
                            if (xde.GetType() == typeof(X509Certificate))
                            {
                                X509Certificate crt = (X509Certificate)xde;
                                certInSignature = new ECertificate(crt.CertificateBytes);
                                break;
                            }
                        }
                    }
                }

                if(certInSignature == null)
                {
                    Assert.False(false,"İmzadan sertifika alınamadı.");
                }

                byte[] signatureValue = signature.SignatureValue;
                String strSig = Convert.ToBase64String(signatureValue);
                IAlgorithm signatureAlgorithm = signature.SignatureMethod.MAlgorithm;
                DigestAlg signatureDigestAlg = signature.SignatureMethod.MDigestAlg;

                String algorithmString = getSignatureAlgorithmString(signatureAlgorithm, signatureDigestAlg);

                SignedInfo signedInfo = signature.SignedInfo;
                String c14nName = signedInfo.CanonicalizationMethod.URL;

                DOMDocument domDoc = new DOMDocument(signature.SignedInfo.Element, null);
                Document transformedDocument = domDoc.getTransformedDocument(signature.SignedInfo.CanonicalizationMethod);
                byte[] c14nedSignedInfo = transformedDocument.Bytes;

                //byte[] c14nedSignedInfo = XmlUtil.outputDOM(signature.SignedInfo.Element, signedInfo.CanonicalizationMethod);

                String str = c14nedSignedInfo.ToString();
                String str2 = Convert.ToBase64String(c14nedSignedInfo);

                BaseSigner baseSigner = getBaseSigner(certInSignature);
                byte[] netSignatureValue = baseSigner.sign(c14nedSignedInfo);
                String strnet = netSignatureValue.ToString();
                String strnetbase64 = Base64.Encode(netSignatureValue).ToString();



                bool testResult = ArrayUtil.Equals(netSignatureValue, signatureValue);

                return testResult;
            }
               catch (Exception e)
               {
                   throw e;
               }
        }

        [Test]
        public void a_checkSignatureValueForBESEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
        public void b_checkSignatureValueForBESEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void c_checkSignatureValueForBESDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void d_checkSignatureValueForESTEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void e_checkSignatureValueForESTEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void f_checkSignatureValueForESTDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void g_checkSignatureValueForESCEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void h_checkSignatureValueForESCEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void i_checkSignatureValueForESCDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void j_checkSignatureValueForESXEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void k_checkSignatureValueForESXEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void l_checkSignatureValueForESXDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void m_checkSignatureValueForESXLEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void n_checkSignatureValueForESXLEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void o_checkSignatureValueForESXLDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void p_checkSignatureValueForESAEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void r_checkSignatureValueForESAEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void s_checkSignatureValueForESADetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
        public void t_checkDignatureValueForBESEDefterEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSignatureValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SignatureValueCheckResult");
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
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
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
