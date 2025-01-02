using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using NETAPI_TEST.src.testconstants;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace NETAPI_TEST.src.signature
{
    public class SignatureTest
    {
        private static ITestConstants testconstants = new TestConstants();

        public static void BESImza()
        {
            SignableByteArray CONTENT = new SignableByteArray(Encoding.ASCII.GetBytes("test"));

            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(CONTENT);

            //create parameters necessary for signature creation
            Dictionary<string, object> params_ = new Dictionary<string, object>();
            
            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            params_[EParameters.P_CERT_VALIDATION_POLICY] = testconstants.getPolicy();

            //Serfika Kontrolu iptal edelim
            params_[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            //By default, QC statement is checked,and signature wont be created if it is not a qualified certificate
            //By setting this parameter to false,user can use test certificates
            params_[EParameters.P_CHECK_QC_STATEMENT] = false;

            //add signer
            //Since the specified attributes are mandatory for bes,null is given as parameter for optional attributes
            ECertificate signerCert = testconstants.getSignerCertificate();   //kartınızda buldugu ilk imzalama sertifikasını alır

            SignatureAlg aAlg = SignatureAlg.RSA_SHA256;
            SmartCard sc = new SmartCard(testconstants.getCardType());
            long[] slots = sc.getSlotList();
            long session = sc.openSession(slots[0]);
            sc.login(session, testconstants.getPIN());
            byte[] serial = signerCert.getSerialNumber().GetData();
            BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slots[0], serial, aAlg.getName());
            //BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slots[0], serial, aAlg.getAsymmetricAlg().getName(), aAlg.getDigestAlg().getName());

            try
            {
                bs.addSigner(ESignatureType.TYPE_BES, signerCert, signer, null, params_);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);                    
                Console.WriteLine(ex.InnerException.ToString());
            }
           

            sc.logout(session);
            sc.closeSession(session);
            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), "BesImza.bin");
            
            verifySignature(bs.getEncoded());
            removeSignature(bs.getEncoded());
        }

        public static void BESImzaParalel()
        {
            //first signature
            SignableByteArray CONTENT = new SignableByteArray(Encoding.ASCII.GetBytes("test"));
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(CONTENT);

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = testconstants.getPolicy();
            params_[EParameters.P_CHECK_QC_STATEMENT] = false;

            ECertificate signerCert = testconstants.getSignerCertificate();

            //SignatureAlg aAlg = SignatureAlg.fromAlgorithmIdentifier(signerCert.getSignatureAlgorithm());
            Pair<SignatureAlg, IAlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(signerCert.getSignatureAlgorithm());
            SignatureAlg aAlg = pair.first();
            SmartCard sc = new SmartCard(testconstants.getCardType());
            long[] slots = sc.getSlotList();
            long session = sc.openSession(slots[0]);
            sc.login(session, testconstants.getPIN());
            byte[] serial = signerCert.getSerialNumber().GetData();
            BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slots[0], serial, aAlg.getName());

            bs.addSigner(ESignatureType.TYPE_BES, signerCert,
                         signer, null, params_);

            //second signature
            bs = new BaseSignedData(bs.getEncoded());
            bs.addSigner(ESignatureType.TYPE_BES, signerCert,
                         signer, null, params_);


            sc.logout(session);
            sc.closeSession(session);

            AsnIO.dosyayaz(bs.getEncoded(), "besParalel.bin");

            //imza dogrulama
            verifySignature(bs.getEncoded());
        }

        public static void ESXLongSerialTest()
        {
            //İmzasız dosyayı ancak alttaki constructor ve ile BaseSignedData nesnesini yaratıp
            // ardından bs.addSigner(ESignatureType.TYPE_ESXLong, cert, signer, null, parameters); ile
            //imzalıyabiliyorum
            SignableByteArray CONTENT = new SignableByteArray(Encoding.ASCII.GetBytes("test"));
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(CONTENT);
            //İmzalı dosyayı alttaki constructor ile BaseSignedData nesnesini yaratıp Signer firstSigner = bs.getSignerList()[0];
            //ardından da firstSigner.addCounterSigner(ESignatureType.TYPE_ESXLong, cert, signer, null, parameters); ile
            //imza ekliyebiliyorum.
            //BaseSignedData bs = new BaseSignedData(imzalanacakVeri);
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            ValidationPolicy policy;
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = testconstants.getPolicy(); ;
            parameters[EParameters.P_CHECK_QC_STATEMENT] = false;
            parameters[EParameters.P_TSS_INFO] = testconstants.getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;
            parameters[EParameters.P_TRUST_SIGNINGTIMEATTR] = true;

            ECertificate cert = testconstants.getSignerCertificate();

            SmartCard sc = new SmartCard(testconstants.getCardType());
            long[] slots = sc.getSlotList();
            long slot = sc.getSlotList()[0];
            long session = sc.openSession(slot);
            sc.login(session, "12345");

            //SignatureAlg aAlg = SignatureAlg.fromAlgorithmIdentifier(cert.getSignatureAlgorithm());
            Pair<SignatureAlg, IAlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(cert.getSignatureAlgorithm());
            SignatureAlg aAlg = pair.first();
            BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slots[0],
            cert.getSerialNumber().GetData(), aAlg.getName());


            bs.addSigner(ESignatureType.TYPE_BES, cert,
                         signer, null, parameters);


            AsnIO.dosyayaz(bs.getEncoded(), @"signedFile.imz");

            //imzali bir dosya icin EContentInfo nesnesi düzgün oluşabilmeli ek olarak Content tipi de id_signedData olmalı
            byte[] dosya = File.ReadAllBytes(@"signedFile.imz");
            EContentInfo contentInfo = null;
            try
            {
                contentInfo = new EContentInfo(dosya);
                if (contentInfo.getContentType().mValue.SequenceEqual(_cmsValues.id_signedData))
                    Console.WriteLine("Dosya imzali");
            }
            catch (Exception e)
            {
                Console.WriteLine("Dosya decode edilemedi, imzali değil");
            }


            /*if(!Arrays.equals(contentInfo.getContentType().value, _cmsValues.id_signedData))
                throw new NotSignedDataException("Content type is not a signed data. Its oid is " + Arrays.toString(contentInfo.getContentType().value));
        }*/

            bs = new BaseSignedData(dosya);
            //bs.addContent(new SignableByteArray(imzali));
            List<Signer> list = bs.getSignerList();
            bs.addSigner(ESignatureType.TYPE_BES, cert,
                         signer, null, parameters);
            //2. constructor (bytearray parametreli) ile imzalarsam try da hata vermiyor ve ikinci imzayı atabiliyor.
            //ilk imza atılacak ise hata alıp catch de 1.constructor ile(parametresiz) ile ilk imzayı atabiliyorum.
            //amacaım ise tek bir constructor kullanarak, imzasız dosya ya imza atıp, imzalandıktan sonra da ikinci ve üçüncü imzaları aynı constructor
            //ile hata almadan imzalıyabilmek.
            try
            {
                Signer firstSigner = bs.getSignerList()[0];
                firstSigner.addCounterSigner(ESignatureType.TYPE_ESXLong, cert, signer, null, parameters);
                firstSigner.addCounterSigner(ESignatureType.TYPE_ESXLong, cert, signer, null, parameters);
                firstSigner.addCounterSigner(ESignatureType.TYPE_ESXLong, cert, signer, null, parameters);
            }
            catch (Exception e)
            {
                bs.addSigner(ESignatureType.TYPE_ESXLong, cert, signer, null, parameters);
            }
            AsnIO.dosyayaz(bs.getEncoded(), @"C:\Temp\ImzaDeneme.p7s");
            sc.logout(session);
            sc.closeSession(session);
        }
        public static void ESTImza()
        {
            SignableByteArray CONTENT = new SignableByteArray(Encoding.ASCII.GetBytes("test"));
            BaseSignedData bs = new BaseSignedData();

            bs.addContent(CONTENT);

            Dictionary<string, object> params_ = new Dictionary<string, object>();

            //if you are using test certificates,without QCStatement,you must set P_CHECK_QC_STATEMENT to false.By default,it is true
            params_[EParameters.P_CHECK_QC_STATEMENT] = false;

            //necassary for getting signaturetimestamp
            params_[EParameters.P_TSS_INFO] = testconstants.getTSSettings();
            params_[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;

            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            params_[EParameters.P_CERT_VALIDATION_POLICY] = testconstants.getPolicy();
            params_[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = true;

            //add signer
            ECertificate signerCert = testconstants.getSignerCertificate();


            //SignatureAlg aAlg = SignatureAlg.fromAlgorithmIdentifier(signerCert.getSignatureAlgorithm());
            Pair<SignatureAlg, IAlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(signerCert.getSignatureAlgorithm());
            SignatureAlg aAlg = pair.first();
            SmartCard sc = new SmartCard(testconstants.getCardType());
            long[] slots = sc.getSlotList();
            long session = sc.openSession(slots[0]);
            sc.login(session, testconstants.getPIN());
            byte[] serial = signerCert.getSerialNumber().GetData();
            BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slots[0], serial, aAlg.getName());
            //BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slots[0], serial, aAlg.getDigestAlg().getName(), aAlg.getDigestAlg().getName());

            bs.addSigner(ESignatureType.TYPE_EST, signerCert, signer, null, params_);
            sc.logout(session);
            sc.closeSession(session);

            AsnIO.dosyayaz(bs.getEncoded(), "est.bin");

            //imza dogrulama
            verifySignature(bs.getEncoded());
        }

        public static void convertToESTImza()
        {
            try
            {
                byte[] inputBES = AsnIO.dosyadanOKU("BesImza.bin");
                BaseSignedData sd = new BaseSignedData(inputBES);
                Dictionary<string, object> parameters = new Dictionary<string, object>();
                //necessary for getting signaturetimestamp
                parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
                parameters[EParameters.P_TSS_INFO] = testconstants.getTSSettings();
                //necessary for validating signer certificate according to time of //signaturetimestamp
                parameters[EParameters.P_CERT_VALIDATION_POLICY] = testconstants.getPolicy();
                List<Signer> signerList = null;
                signerList = sd.getSignerList();
                signerList[0].convert(ESignatureType.TYPE_EST, parameters);
                AsnIO.dosyayaz(sd.getEncoded(), "BesToEst.bin");
                verifySignature(sd.getEncoded());
            }
            catch (Exception e)
            {
                throw new Exception(e.Message, e);
            }
        }


        public static void testExlongSign()
        {
            BaseSignedData bs = new BaseSignedData();

            ISignable externalContent = new SignableByteArray(Encoding.ASCII.GetBytes("test"));

            bs.addContent(externalContent);

            Dictionary<String, Object> _params = new Dictionary<String, Object>();

            //if you are using test certificates,without QCStatement,you must set P_CHECK_QC_STATEMENT to false.By default,it is true
            _params[EParameters.P_CHECK_QC_STATEMENT] = false;

            //necassary for getting signaturetimestamp
            _params[EParameters.P_TSS_INFO] = testconstants.getTSSettings();
            _params[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;


            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            _params[EParameters.P_CERT_VALIDATION_POLICY] = testconstants.getPolicy();

            //add signer
            ECertificate cert = testconstants.getSignerCertificate();

            //SignatureAlg aAlg = SignatureAlg.fromAlgorithmIdentifier(cert.getSignatureAlgorithm());
            Pair<SignatureAlg, IAlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(cert.getSignatureAlgorithm());
            SignatureAlg aAlg = pair.first();
            SmartCard sc = new SmartCard(testconstants.getCardType());
            long[] slots = sc.getSlotList();
            long session = sc.openSession(slots[0]);
            sc.login(session, testconstants.getPIN());
            byte[] serial = cert.getSerialNumber().GetData();
            BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slots[0], serial, aAlg.getName());
            //BaseSigner signer = new SCSignerWithCertSerialNo(sc, session, slots[0], serial, aAlg.getAsymmetricAlg().getName(), aAlg.getDigestAlg().getName());
            bs.addSigner(ESignatureType.TYPE_ESXLong_Type2, cert, signer, null, _params);

            sc.logout(session);
            sc.closeSession(session);

            AsnIO.dosyayaz(bs.getEncoded(), "Signature-C-ESXLong.bin");

            verifySignature(bs.getEncoded());
            //SimpleValidation.validate(bs.getEncoded(), externalContent);
        }


        //verilen imzayi dogrular
        public static void verifySignature(byte[] aSignedData)
        {

            Console.WriteLine("IMZA DOGRULAMA BASLIYOR");

            BaseSignedData bs = new BaseSignedData(aSignedData);
            List<Signer> sis = bs.getSignerList();
            foreach (Signer signer in sis)
            {
                Console.WriteLine(signer.getSignerCertificate().getSubject().getCommonNameAttribute());
            }

            Dictionary<string, object> parameters = new Dictionary<string, object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = testconstants.getPolicy();
            parameters[EParameters.P_CURRENT_TIME] = DateTime.UtcNow;
            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(aSignedData, parameters);
            if (sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
                Console.WriteLine("Imzalarin hepsi dogrulanamadi");
            Console.WriteLine(sdvr.ToString());
        }

        public static void removeSignature(byte[] aContent)
        {
            BaseSignedData bs = new BaseSignedData(aContent);
            bs.getSignerList()[0].remove();
            byte[] noSign = bs.getEncoded();

            BaseSignedData removedBsd = new BaseSignedData(noSign);
            if (0 != removedBsd.getAllSigners().Count)
            {
                throw new Exception("removeSignature is failed");
            }
           
        }
    }
}
