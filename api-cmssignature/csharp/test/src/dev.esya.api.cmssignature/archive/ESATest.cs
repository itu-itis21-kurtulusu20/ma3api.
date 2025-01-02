using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace dev.esya.api.cmssignature.archive
{
    public class ESATest : CMSSignatureTest
    {
        private readonly String OUTPUT_FOLDER = "T:\\api-cmssignature\\test-output\\csharp\\ma3\\esa\\";


        [Test]
        public void testCreateXLong() 
        {
            BaseSignedData baseSignedData = new BaseSignedData();
            baseSignedData.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            ECertificate cert = getSignerCertificate();
            BaseSigner signer = getSignerInterface(SignatureAlg.RSA_SHA256);
            baseSignedData.addSigner(ESignatureType.TYPE_ESXLong, cert, signer, null, parameters);

            byte[] encodedSignature = baseSignedData.getEncoded();

            FileUtil.writeBytes(OUTPUT_FOLDER + "Xlong.p7s", encodedSignature);
        
            ValidationUtil.checkSignatureIsValid(encodedSignature, null);
        }

        [Test]
        public void convertXLongToESA()
        {
            byte[] signatureBytes = FileUtil.readBytes(OUTPUT_FOLDER + "Xlong.p7s");

            BaseSignedData bsd = new BaseSignedData(signatureBytes);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_FORCE_STRICT_REFERENCE_USE] = true;

            bsd.getSignerList()[0].convert(ESignatureType.TYPE_ESAv2, parameters);

            FileUtil.writeBytes(OUTPUT_FOLDER + "ESAv2.p7s", bsd.getEncoded());
        }

        [Test]
        public void convertESAv2ToESAv2ESAv2()
        {
            byte[] signatureBytes = FileUtil.readBytes(OUTPUT_FOLDER + "ESAv2.p7s");

            BaseSignedData bsd = new BaseSignedData(signatureBytes);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_FORCE_STRICT_REFERENCE_USE] = true;

            bsd.getSignerList()[0].convert(ESignatureType.TYPE_ESAv2, parameters);

            FileUtil.writeBytes(OUTPUT_FOLDER + "ESAv2ESAv2.p7s", bsd.getEncoded());
        }

        [Test]
        public void convertESAv2ESAv2To_3_ESAv2()
        {
            byte[] signatureBytes = FileUtil.readBytes(OUTPUT_FOLDER + "ESAv2ESAv2.p7s");

            BaseSignedData bsd = new BaseSignedData(signatureBytes);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_FORCE_STRICT_REFERENCE_USE] = true;

            bsd.getSignerList()[0].convert(ESignatureType.TYPE_ESAv2, parameters);

            FileUtil.writeBytes(OUTPUT_FOLDER + "3_ESAv2.p7s", bsd.getEncoded());
        }

        [Test]
        public void convert_ESA101()
        {
            byte[] fileBytes = FileUtil.readBytes("C:\\a\\ESA101\\ESA_101.pdf.p7s");

            BaseSignedData bsd = new BaseSignedData(fileBytes);

            Dictionary < String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_FORCE_STRICT_REFERENCE_USE] = true;

            bsd.getSignerList()[0].convert(ESignatureType.TYPE_ESAv2, parameters);

            FileUtil.writeBytes(OUTPUT_FOLDER + "3_ESAv2.p7s", bsd.getEncoded());
        }

        [Test]
        public void validate_ESA101()
        {
            //BasicConfigurator.configure();
            byte [] fileBytes = FileUtil.readBytes("C:\\a\\ESA101\\ESA_101.pdf.p7s");
            ValidationUtil.checkSignatureIsValid(fileBytes, null);
        }
    }
}
