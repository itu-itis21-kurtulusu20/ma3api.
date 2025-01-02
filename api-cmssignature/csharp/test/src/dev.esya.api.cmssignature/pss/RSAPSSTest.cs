using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using NUnit.Framework;
using test.esya.api.cmssignature;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace dev.esya.api.cmssignature
{
    

    [TestFixture]
    class RSAPSSTest: CMSSignatureTest
    {
        public static int TEST_COUNT = 100;

        [Test]
        public void test_Crypto_WithPSS_WithPfx()
        {
            PrivateKey privKey = (PrivateKey)getSignerPrivateKey();
            PublicKey pubKey = new PublicKey(getSignerCertificate().getSubjectPublicKeyInfo());

            RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);

            byte[] data;
            byte[] signature;
            bool result;

            StreamWriter sw = new StreamWriter("T:\\Temp\\Pss\\SignatureDotNet1.txt", false, Encoding.ASCII);

            for (int i = 0; i < TEST_COUNT; i++)
            {
                int randomDataLen = new Random().Next(200) + 1;
                data = RandomUtil.generateRandom(randomDataLen);

                sw.WriteLine("D:" + StringUtil.ToHexString(data));

                signature = SignUtil.sign(SignatureAlg.RSA_PSS, rsapssParams, data, privKey);
                result = SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, data, signature, pubKey);
                Assert.True(result);

                sw.WriteLine("S:" + StringUtil.ToHexString(signature));
            }
            sw.Close();
        }

        [Test]
        public void test_Crypto_WithPSS_WithSmartCard()
        {
            SmartCard sc = new SmartCard(CardType.AKIS);
            long slotNo = sc.getSlotList()[0];
            long sid = sc.openSession(slotNo);
            sc.login(sid, "12345");

            byte[] signature;
            bool result;
            byte[] data;

            RSAPSSParams pssParams = new RSAPSSParams(DigestAlg.SHA256);

            byte[] certBytes = sc.getSignatureCertificates(sid)[0];
            ECertificate cert = new ECertificate(certBytes);
            PublicKey pubKey = new PublicKey(cert.getSubjectPublicKeyInfo());

            StreamWriter sw = new StreamWriter("T:\\Temp\\Pss\\SignatureDotNet2.txt", false, Encoding.ASCII);

            for (int i = 0; i < TEST_COUNT; i++)
            {
                int randomDataLen = new Random().Next(200) + 1;
                data = RandomUtil.generateRandom(randomDataLen);

                sw.WriteLine("D:" + StringUtil.ToHexString(data));

                signature = SmartOp.sign(sc, sid, slotNo, cert.getSerialNumber().GetData(), data, SignatureAlg.RSA_PSS.getName(), pssParams);
                result = SignUtil.verify(SignatureAlg.RSA_PSS, pssParams, data, signature, pubKey);
                Assert.True(result);

                sw.WriteLine("S:" + StringUtil.ToHexString(signature));
            }

            sw.Close();
            sc.logout(sid);
            sc.closeSession(sid);
        }


        [Test]
        public void test_CMS_WithPSS_WithPfx()
        {
            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            params_[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;
          
            SignedDataValidationResult sdvr;
            RSAPSSParams pssParams = new RSAPSSParams(DigestAlg.SHA256);

            StreamWriter sw = new StreamWriter("T:\\Temp\\Pss\\SignatureDotNet3.txt", false, Encoding.ASCII);
            
            for (int i = 0; i < TEST_COUNT; i++)
            {
                int randomDataLen = new Random().Next(200) + 1;
                byte [] data = RandomUtil.generateRandom(randomDataLen);

                BaseSignedData bs = new BaseSignedData();   
                bs.addContent(new SignableByteArray(data));
                bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_PSS, pssParams), null, params_);

                sw.WriteLine("D:" + StringUtil.ToHexString(data));
                    
                sdvr = ValidationUtil.validate(bs.getEncoded(), null);            
                Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

                sw.WriteLine("S:" + StringUtil.ToHexString(bs.getEncoded()));
               
            }
            sw.Close();   
         }

       

        [Test]
        public void test_CMS_WithPSS_WithSmartCard()
        {
            P11SmartCard p11SmartCard = new P11SmartCard(CardType.AKIS);
            long slotNo = 1;
            p11SmartCard.openSession(slotNo);
            p11SmartCard.login("12345");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            params_[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            RSAPSSParams pssParams = new RSAPSSParams(DigestAlg.SHA256);

            byte[] certBytes = p11SmartCard.getSignatureCertificates()[0];
            ECertificate cert = new ECertificate(certBytes);
            BaseSigner signer = p11SmartCard.getSigner(cert, Algorithms.SIGNATURE_RSA_PSS, pssParams);

            StreamWriter sw = new StreamWriter("T:\\Temp\\Pss\\SignatureDotNet4.txt", false, Encoding.ASCII);

            for (int i = 0; i < TEST_COUNT; i++)
            {
                int randomDataLen = new Random().Next(200) + 1;
                byte []data = RandomUtil.generateRandom(randomDataLen);


                sw.WriteLine("D:" + StringUtil.ToHexString(data));

                BaseSignedData bs = new BaseSignedData();
                bs.addContent(new SignableByteArray(data));

                bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, params_);

                SignedDataValidationResult sdvr = ValidationUtil.validate(bs.getEncoded(), null);
                Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

                sw.WriteLine("S:" + StringUtil.ToHexString(bs.getEncoded()));
            }

            sw.Close();
            p11SmartCard.logout();
            p11SmartCard.closeSession();
                    
        }


        
    }
}
