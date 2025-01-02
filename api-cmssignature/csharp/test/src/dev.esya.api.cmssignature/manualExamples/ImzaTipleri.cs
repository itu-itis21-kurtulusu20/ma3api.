using System;
using System.Collections.Generic;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;

namespace dev.esya.api.cmssignature.manualExamples
{
    [TestFixture]
    public class ImzaTipleri
    {
        private static readonly Object VALIDATION_POLICY;
        private String CONTENT_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\test.dat";

        [Test]
        public void testBES()
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(new SignableByteArray(Encoding.ASCII.GetBytes("test")));

            //Since SigningTime attribute is optional,add it to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = VALIDATION_POLICY;

            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, params_);
        }

        private readonly EContentInfo input;

        [Test]
        public void testBeyanTarihi()
        {
            BaseSignedData bs = new BaseSignedData(input);
            List<EAttribute> attrs = bs.getSignerList()[0].getSignedAttribute(SigningTimeAttr.OID);
            DateTime? time = SigningTimeAttr.toTime(attrs[0]);
            Console.WriteLine(time.Value.ToString());
        }

        [Test]
        public void testESTTarihi()
        {
            BaseSignedData bs = new BaseSignedData(input);
            EST estSign = (EST) bs.getSignerList()[0];
            DateTime? time = estSign.getTime();
            Console.WriteLine(time.Value.ToString());
        }

        [Test]
        public void testEST()
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(new SignableByteArray(Encoding.ASCII.GetBytes("test")));

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            //ilerli for getting signaturetimestamp
            TSSettings tsSettings = new TSSettings("http://zd.ug.net", 21, "12345678");
            params_[EParameters.P_TSS_INFO] = tsSettings;
            params_[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            params_[EParameters.P_CERT_VALIDATION_POLICY] = TestConstants.getPolicy();

            //add signer
            bs.addSigner(ESignatureType.TYPE_EST, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         null, params_);
        }

        private BaseSigner getSignerInterface(SignatureAlg rsaSha1)
        {
            // TODO Auto-generated method stub
            return null;
        }

        private ECertificate getSignerCertificate()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }
}