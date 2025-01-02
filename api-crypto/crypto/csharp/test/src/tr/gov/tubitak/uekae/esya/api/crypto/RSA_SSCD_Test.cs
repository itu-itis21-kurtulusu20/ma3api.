using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace test.src.tr.gov.tubitak.uekae.esya.api.crypto
{
    [TestFixture]
    public class RSA_SSCD_Test
    {
        [Test]
        public void testRSA_SSCD()
        {
            //RSA_SSCD signer = RSA_SSCD.getInstance(DigestAlg.SHA256);

            //byte[] privkey = File.ReadAllBytes("E:/tempInput/privatekey.dat");

            //IPrivateKey eshsPrivKey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, AsnIO.dosyadanOKU("E:/tempInput/CVC_ESHS_Gem_PrivKey2668435.bin"));

           

            //signer.setupForSigning(eshsPrivKey);

            //byte [] K_ESHS = new byte [32];
		  
            //new BouncyRandomGenerator().nextBytes(K_ESHS);

            //String TEST_DATA = "imzalanacak veri";
				
            //byte [] toBeSigned = UtilBytes.concatAll(K_ESHS, ASCIIEncoding.ASCII.GetBytes(TEST_DATA));

            //byte [] sign = signer.generateSignature(toBeSigned, K_ESHS);

            //RSA_SSCD verifier = RSA_SSCD.getInstance(DigestAlg.SHA256);

            //byte [] pubkey = File.ReadAllBytes("E:/tempInput/publickey.dat");
            
            //PublicKey publickey = new PublicKey(pubkey);

            //verifier.setupForVerification(publickey);

            //Boolean verified = verifier.verifySignature(sign, K_ESHS, 32);

        }

    }
}
