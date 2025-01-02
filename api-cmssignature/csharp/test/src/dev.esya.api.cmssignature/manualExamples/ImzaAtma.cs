using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.manualExamples
{
    [TestFixture]
    public class ImzaAtma
    {
        private readonly String SIGNING_CERTIFICATE_PATH = TestConstants.getDirectory() +
                                                           "testdata\\support\\yasemin_sign.cer";

        private readonly String POLICY_FILE = TestConstants.getDirectory() + "testdata\\support\\policy.xml";

        [Test]
        public void testImzasizBirVerininİmzalanmasi()
        {
            
            String SIGNATURE_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\1.p7s";
            //--------------------------------------------------------------------------------//
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(new SignableByteArray(Encoding.ASCII.GetBytes("test")));

            //create parameters necessary for signature creation 
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();


            TestConstants.setLicence();
            ValidationPolicy policy;
            using (FileStream fs = new FileStream(@"C:\policy.xml", FileMode.Open,FileAccess.Read))
            {
                policy = PolicyReader.readValidationPolicy(fs);
            } 
            //PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE, FileMode.Open, FileAccess.Read));
            /*necessary for certificate validation.By default,certificate validation is done.But if the user does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false*/
             parameters[EParameters.P_CERT_VALIDATION_POLICY] = policy; 
            /*By default, QC statement is checked,and signature wont be created if it
            is not a qualified certificate. By setting this parameter to false,user
            can use test certificates*/ 
            ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);  
            SmartCard sc = new SmartCard(CardType.AKIS); 
            long[] slots = sc.getSlotList(); 
            long session = sc.openSession(slots[0]); 
            sc.login(session, "12345");  
            Pair<SignatureAlg,IAlgorithmParams> algParams = SignatureAlg.
            fromAlgorithmIdentifier(cert.getSignatureAlgorithm());
                        BaseSigner signer = new SCSignerWithCertSerialNo(sc, session,
            slots[0], cert.getSerialNumber().GetData(), algParams.first().getName()); 
 
            //add signer. Since the specified attributes are mandatory for bes,null is given as parameter for optional attributes 
            bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, parameters); 
 
            //write the contentinfo to file 
            AsnIO.dosyayaz(bs.getEncoded(), SIGNATURE_FILE); 
            sc.logout(session); 
            sc.closeSession(session);   
        }

        [Test]
        public void testParalelImzaEklenmesi()
        {
            String SIGNATURE_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\1.p7s";
            String NEW_SIGNATURE_ADDED_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\2.p7s";
            //--------------------------------------------------------------------------------------------//
            byte[] signature = AsnIO.dosyadanOKU(SIGNATURE_FILE);
            BaseSignedData bs = new BaseSignedData(signature);

            //create parameters necessary for signature creation 
            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            ValidationPolicy policy =
                PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE, FileMode.Open, FileAccess.Read));
            /*necessary for certificate validation.By default,certificate validation is done.But if the user does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false*/
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;
          

            ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);

            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = SmartOp.findSlotNumber(CardType.AKIS,null);
            long session = sc.openSession(slot);
            sc.login(session, "12345");

            BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slot, cert.getSerialNumber().GetData(),
                                                             SignatureAlg.RSA_SHA1.getName());

            //add signer. Since the specified attributes are mandatory for bes,null is given as parameter for 
            //optional attributes 
            bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, params_);

            //write the contentinfo to file 
            AsnIO.dosyayaz(bs.getEncoded(), NEW_SIGNATURE_ADDED_FILE);
            sc.logout(session);
            sc.closeSession(session);
        }


        [Test]
        public void testSeriImzaEklenmesi()
        {
            String SIGNATURE_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\1.p7s";
            String NEW_SIGNATURE_ADDED_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\3.p7s";
            //------------------------------------------------------------------------------------------//
            byte[] signature = AsnIO.dosyadanOKU(SIGNATURE_FILE);
            BaseSignedData bs = new BaseSignedData(signature);

            //create parameters necessary for signature creation 
            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            ValidationPolicy policy =
                PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE, FileMode.Open, FileAccess.Read));
            /*necessary for certificate validation.By default,certificate validation is done.But if the user does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false*/
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;
          

            ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);

            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = SmartOp.findSlotNumber(CardType.AKIS, null);
            long session = sc.openSession(slot);
            sc.login(session, "12345");

            BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slot, cert.getSerialNumber().GetData(),
                                                             SignatureAlg.RSA_SHA1.getName());
            Signer firstSigner = bs.getSignerList()[0];

            firstSigner.addCounterSigner(ESignatureType.TYPE_BES, cert, signer, null, params_);

            //write the contentinfo to file 
            AsnIO.dosyayaz(bs.getEncoded(), NEW_SIGNATURE_ADDED_FILE);
            sc.logout(session);
            sc.closeSession(session);
        }

        [Test]
        public void testAyrikImzaAtilmasi()
        {
            String MOVIE_FILE = "D:\\share\\film\\Life\\Life S01E01 Challenges of Life.mkv";
            String SIGNATURE_FILE = TestConstants.getDirectory() + "\\testdata\\support\\bes\\HugeExternalContent.p7s";

            //-------------------------------------------------------------------------//
            BaseSignedData bs = new BaseSignedData();

            FileInfo file = new FileInfo(MOVIE_FILE);
            ISignable signable = new SignableFile(file, 2048);
            bs.addContent(signable, false);

            //create parameters necessary for signature creation
            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            ValidationPolicy policy =
                PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE, FileMode.Open, FileAccess.Read));
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;
           
            ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);

            SmartCard sc = new SmartCard(CardType.AKIS);
            long[] slots = sc.getSlotList();
            long session = sc.openSession(slots[0]);
            sc.login(session, "12345");

            BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slots[0], cert.getSerialNumber().GetData(),
                                                             SignatureAlg.RSA_SHA1.getName());


            bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, params_);

            AsnIO.dosyayaz(bs.getEncoded(), SIGNATURE_FILE);

            sc.logout(session);
            sc.closeSession(session);
        }
    }
}