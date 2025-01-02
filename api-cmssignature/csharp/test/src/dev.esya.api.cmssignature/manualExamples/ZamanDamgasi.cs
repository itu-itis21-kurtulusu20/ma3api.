using System;
using System.Collections.Generic;
using NUnit.Framework;
using Org.BouncyCastle.Utilities;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;

namespace dev.esya.api.cmssignature.manualExamples
{
    [TestFixture]
    public class ZamanDamgasi
    {
        [Test]
        public void testZamanDamgasi()
        {
            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            TSSettings tsSettings = new TSSettings("http://zd.ug.net", 21, "12345678");
            params_[EParameters.P_TSS_INFO] = tsSettings;
            params_[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
        }
        [Test]
        public void testZamanDamgasiAlma()
        {
            byte[] data = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
            byte[] dataTbs = DigestUtil.digest(DigestAlg.SHA1, data);
            TSSettings settings = new TSSettings("http://zd.ug.net", 21, "12345678");
            TSClient tsClient = new TSClient();
            ETimeStampResponse response = tsClient.timestamp(dataTbs, settings);
            byte[] tsBytes = response.getContentInfo().getEncoded();
           

            EContentInfo ci = new EContentInfo(tsBytes);
            ESignedData sd = new ESignedData(ci.getContent());
            ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
            
            byte[] digestInTS = tstInfo.getHashedMessage();
            DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(tstInfo.getHashAlgorithm());

            byte[] digest = DigestUtil.digest(digestAlg, data);

            if (!Arrays.AreEqual(digest, digestInTS))
                throw new Exception("Özetler uyuşmuyor. Zaman damgası bu dosyanın değil.");
            
            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = TestConstants.getPolicy();

            SignedDataValidation sdv = new SignedDataValidation(); 
            SignedDataValidationResult sdvr = sdv.verify(tsBytes, params_);
            if (sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
                throw new Exception("Zaman damgası doğrulanamadı."); 
            //Zaman damgasını veren sertifikanın zaman damgası yetkisi kontrol ediliyor. 
            
            BaseSignedData bs = new BaseSignedData(tsBytes); 
            ECertificate tsCert = bs.getSignerList()[0].getSignerCertificate(); 
            if(!tsCert.isTimeStampingCertificate()) 
              throw new Exception("Zaman damgası veren sertifika zaman damgası vermeye yetkili değil."); 
 
            Console.WriteLine("Zaman Damgası Doğrulandı.");
            DateTime tsTime = tstInfo.getTime(); 
            Console.WriteLine("Zaman Damgası Alınma Tarihi: " + tsTime.ToLocalTime()); 

        }

    }
}