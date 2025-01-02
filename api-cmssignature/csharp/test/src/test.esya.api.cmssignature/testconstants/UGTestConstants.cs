using System;
using System.IO;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;

namespace test.esya.api.cmssignature.testconstants
{
    public class UGTestConstants : TestConstants
    {
        //Revocation control from OCSP
        private readonly String PFX1_PATH = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_2.p12";
	    //Revocation control from CRL
        private readonly String PFX2_PATH = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_2.p12";
        // EC certificate for sign
        private readonly String PFX3_PATH = "T:\\api-parent\\resources\\ug\\pfx\\esya-test-system\\test@test.gov.tr_724328.pfx";

        private readonly String PFX_PASSWORD = "123456";
        private readonly String PFX3_PASSWORD = "724328";


        private readonly String ZD_ADD = "http://zdsA1.test3.kamusm.gov.tr";
        private readonly int ZD_USER_ID = 1;
        private readonly String ZD_PASSWORD = "12345678";

        private static readonly String DIRECTORY = "T:\\api-cmssignature\\testdata\\cmssignature\\imza\\";

        

        private static readonly String POLICY_DIRECTORY = "T:\\api-parent\\resources\\ug\\config\\";
        private readonly String POLICY_FILE = POLICY_DIRECTORY + "certval-policy-test.xml";
        private readonly String POLICY_FILE_CRL = POLICY_DIRECTORY + "certval-crl-policy-test.xml";
        
        private readonly String POLICY_FILE_OCSP = POLICY_DIRECTORY + "certval-ocsp-policy-test.xml";
        private readonly String POLICY_FILE_NoOCSP_NoCRL = POLICY_DIRECTORY + "certval-no-ocsp-no-crl-finder-policy-test.xml";

        private readonly String HUGE_FILE = "D:\\share\\film\\Life\\Life S01E01 Challenges of Life.mkv";

        private readonly ECertificate cert;
        private readonly ECertificate secondCert;
        private readonly ECertificate ecCert;

        private readonly IPrivateKey privKey;
        private readonly IPrivateKey secondPrivKey;
        private readonly IPrivateKey ecPrivKey;


        public UGTestConstants()
        {
            IPfxParser pfxParser = Crypto.getPfxParser();
            pfxParser.loadPfx(PFX1_PATH, PFX_PASSWORD);
            privKey = pfxParser.getFirstPrivateKey();
            cert = pfxParser.getFirstCertificate();

            IPfxParser pfxParser2 = Crypto.getPfxParser();
            pfxParser2.loadPfx(PFX2_PATH, PFX_PASSWORD);
            secondPrivKey = pfxParser2.getFirstPrivateKey();
            secondCert = pfxParser2.getFirstCertificate();
            
            IPfxParser pfxParser3 = Crypto.getPfxParser();
            pfxParser3.loadPfx(PFX3_PATH, PFX3_PASSWORD);
            ecPrivKey = pfxParser3.getFirstPrivateKey();
            ecCert = pfxParser3.getFirstCertificate();
        }


        #region TestConstants Members

        public String getDirectory()
        {
            return DIRECTORY;
        }

        public BaseSigner getSignerInterface(SignatureAlg aAlg)
        {
            Signer signer = Crypto.getSigner(aAlg);
            signer.init(privKey);
            return signer;
        }

        public BaseSigner getSignerInterface(SignatureAlg aAlg, IAlgorithmParams algorithmParams)
        {
            Signer signer = Crypto.getSigner(aAlg);
            signer.init(privKey, algorithmParams);
            return signer;
        }

        public BaseSigner getSecondSignerInterface(SignatureAlg aAlg)
        {
            Signer signer = Crypto.getSigner(aAlg);
            signer.init(secondPrivKey);
            return signer;
        }

        public BaseSigner getSecondSignerInterface(SignatureAlg aAlg, IAlgorithmParams algorithmParams)
        {
            Signer signer = Crypto.getSigner(aAlg);
            signer.init(secondPrivKey, algorithmParams);
            return signer;
        }

        public BaseSigner getECSignerInterface(SignatureAlg aAlg) {
            Signer signer = Crypto.getSigner(aAlg);
            signer.init(ecPrivKey);
            return signer;
        }

        //Revocation control from OCSP
        public ECertificate getSignerCertificate()
        {
            return cert;
        }

        public IPrivateKey getSignerPrivateKey()
        {
            return privKey;
        }

        //Revocation control from CRL 
        public ECertificate getSecondSignerCertificate()
        {
            return secondCert;
        }

        public ECertificate getECSignerCertificate()
        {
            return ecCert;
        }

        public ValidationPolicy getPolicy()
        {
            return PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE, FileMode.Open, FileAccess.Read));
        }

        public TSSettings getTSSettings()
        {
            return new TSSettings(ZD_ADD, ZD_USER_ID, ZD_PASSWORD);
        }

        //@Override
        public ISignable getSimpleContent()
        {
            try
            {
                return new SignableByteArray(Encoding.ASCII.GetBytes("test"));
            }
            catch (Exception e)
            {
                throw new SystemException(e.Message, e);
            }
        }

        public ISignable getHugeContent()
        {
            return new SignableFile(new FileInfo(HUGE_FILE));
        }

        //@Override
        public ValidationPolicy getPolicyCRL()
        {
            return PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE_CRL, FileMode.Open, FileAccess.Read));
        }

        //@Override
        public ValidationPolicy getPolicyOCSP()
        {
            return PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE_OCSP, FileMode.Open, FileAccess.Read));
        }

        //@Override
        public ValidationPolicy getPolicyNoOCSPNoCRL()
        {
            return
                PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE_NoOCSP_NoCRL, FileMode.Open,
                                                                 FileAccess.Read));
        }

        #endregion
    }
}