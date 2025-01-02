using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using test.esya.api.cmssignature;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.sign
{
    class ESATest : CMSSignatureTest
    {

        public static Object[] TestCases =
        {
            new object[] {ESignatureType.TYPE_ESA},
            new object[] {ESignatureType.TYPE_ESAv2}
        };

        [Test, TestCaseSource("TestCases")]
        public void _1_testCreateESA_Attached(ESignatureType eSignatureType)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.addSigner(eSignatureType, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test, TestCaseSource("TestCases")]
        public void _2_testCreateESA_Detached(ESignatureType eSignatureType)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent(), false);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.addSigner(eSignatureType, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
        }

        // esa'ya convert için ilk önce xlong imza oluşturmamız gerekiyor
        [Test]
        public void _3_testCreateXLONG_Detached()
        {
            BaseSignedData bs = new BaseSignedData();

            FileInfo fileInfo = new FileInfo("T:\\api-parent\\resources\\testdata\\sample.txt");
            ISignable externalContent = new SignableFile(fileInfo, 32 * 1024);

            bs.addContent(externalContent, false);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), "T:\\api-cmssignature\\test-output\\java\\ma3\\conversion\\XLong_detached.p7s");

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), externalContent);
        }

        // esa'ya convert için ilk önce xlong imza oluşturmamız gerekiyor
        [Test]
        public void _4_testCreateXLONG_Attached()
        {
            BaseSignedData bs = new BaseSignedData();

            FileInfo fileInfo = new FileInfo("T:\\api-parent\\resources\\testdata\\sample.txt");
            ISignable content = new SignableFile(fileInfo, 32 * 1024);

            bs.addContent(content);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), "T:\\api-cmssignature\\test-output\\java\\ma3\\conversion\\XLong_attached.p7s");

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test, TestCaseSource("TestCases")]
        public void _5_testConvertESA_Attached(ESignatureType eSignatureType)
        {
            byte[] signatureFile = AsnIO.dosyadanOKU("T:\\api-cmssignature\\test-output\\java\\ma3\\conversion\\XLong_attached.p7s");
            BaseSignedData bs = new BaseSignedData(signatureFile);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(eSignatureType, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test, TestCaseSource("TestCases")]
        public void _6_testConvertESA_Detached(ESignatureType eSignatureType)
        {
            byte[] signatureFile = AsnIO.dosyadanOKU("T:\\api-cmssignature\\test-output\\java\\ma3\\conversion\\XLong_detached.p7s");
            BaseSignedData bs = new BaseSignedData(signatureFile);

            FileInfo fileInfo = new FileInfo("T:\\api-parent\\resources\\testdata\\sample.txt");
            ISignable externalContent = new SignableFile(fileInfo, 32 * 1024);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_EXTERNAL_CONTENT] = externalContent;

            bs.getSignerList()[0].convert(eSignatureType, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), externalContent);
        }
    }
}
