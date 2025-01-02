using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtests2010.negative
{
    [TestFixture]
    public class ESXLong_N : CMSSignatureTest
    {
        public static List<ECRL> crltest1 = new List<ECRL>();
        public static List<ECRL> crltest2 = new List<ECRL>();
        public static List<ECRL> crltest3 = new List<ECRL>();

        public static List<EOCSPResponse> ocsptest4 = new List<EOCSPResponse>();
        public static List<EOCSPResponse> ocsptest5 = new List<EOCSPResponse>();
        public static List<EOCSPResponse> ocsptest6 = new List<EOCSPResponse>();
        private readonly String INPUT_PATH;

        public ESXLong_N()
        {
            INPUT_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\";
        }

        static ESXLong_N()
        {
            try
            {
                ECRL crlA1 =
                    new ECRL(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-1.CERT-SIG-LevelACAOK.crl"));
                ECRL crlA2 =
                    new ECRL(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-2.CERT-SIG-LevelACAOK.crl"));
                ECRL crlA3 =
                    new ECRL(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-3.CERT-SIG-LevelACAOK.crl"));

                ECRL crlB1 =
                    new ECRL(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-1.CERT-SIG-LevelBCAOK.crl"));
                ECRL crlB2 =
                    new ECRL(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-2.CERT-SIG-LevelBCAOK.crl"));
                ECRL crlB3 =
                    new ECRL(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-3.CERT-SIG-LevelBCAOK.crl"));

                ECRL crlRoot1 =
                    new ECRL(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-1.CERT-SIG-RootCAOK.crl"));
                ECRL crlRoot2 =
                    new ECRL(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-2.CERT-SIG-RootCAOK.crl"));
                ECRL crlRoot3 =
                    new ECRL(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-3.CERT-SIG-RootCAOK.crl"));


                crltest1.Add(crlA1);
                crltest1.Add(crlB1);
                crltest1.Add(crlRoot1);

                crltest2.Add(crlA2);
                crltest2.Add(crlB2);
                crltest2.Add(crlRoot2);

                crltest3.Add(crlA3);
                crltest3.Add(crlB3);
                crltest3.Add(crlRoot3);

                EBasicOCSPResponse ocsp4 =
                    new EBasicOCSPResponse(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-4.CERT-SIG-EE.bor.der"));
                EBasicOCSPResponse ocsp5 =
                    new EBasicOCSPResponse(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-5.CERT-SIG-EE.bor.der"));
                EBasicOCSPResponse ocsp6 =
                    new EBasicOCSPResponse(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-6.CERT-SIG-EE.bor.der"));

                EBasicOCSPResponse ocspA4 =
                    new EBasicOCSPResponse(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-4.CERT-SIG-LevelACAOK.bor.der"));
                EBasicOCSPResponse ocspA5 =
                    new EBasicOCSPResponse(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-5.CERT-SIG-LevelACAOK.bor.der"));
                EBasicOCSPResponse ocspA6 =
                    new EBasicOCSPResponse(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-6.CERT-SIG-LevelACAOK.bor.der"));

                EBasicOCSPResponse ocspB4 =
                    new EBasicOCSPResponse(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-4.CERT-SIG-LevelBCAOK.bor.der"));
                EBasicOCSPResponse ocspB5 =
                    new EBasicOCSPResponse(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-5.CERT-SIG-LevelBCAOK.bor.der"));
                EBasicOCSPResponse ocspB6 =
                    new EBasicOCSPResponse(
                        AsnIO.dosyadanOKU(
                            "D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-6.CERT-SIG-LevelBCAOK.bor.der"));


                EOCSPResponse eocsp4 =
                    new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(),
                                                       new ResponseBytes(new[] {1, 2}, ocsp4.getBytes())));
                EOCSPResponse eocsp5 =
                    new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(),
                                                       new ResponseBytes(new[] {1, 2}, ocsp5.getBytes())));
                EOCSPResponse eocsp6 =
                    new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(),
                                                       new ResponseBytes(new[] {1, 2}, ocsp6.getBytes())));

                EOCSPResponse eocspA4 =
                    new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(),
                                                       new ResponseBytes(new[] {1, 2}, ocspA4.getBytes())));
                EOCSPResponse eocspA5 =
                    new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(),
                                                       new ResponseBytes(new[] {1, 2}, ocspA5.getBytes())));
                EOCSPResponse eocspA6 =
                    new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(),
                                                       new ResponseBytes(new[] {1, 2}, ocspA6.getBytes())));

                EOCSPResponse eocspB4 =
                    new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(),
                                                       new ResponseBytes(new[] {1, 2}, ocspB4.getBytes())));
                EOCSPResponse eocspB5 =
                    new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(),
                                                       new ResponseBytes(new[] {1, 2}, ocspB5.getBytes())));
                EOCSPResponse eocspB6 =
                    new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(),
                                                       new ResponseBytes(new[] {1, 2}, ocspB6.getBytes())));


                ocsptest4.Add(eocsp4);
                ocsptest4.Add(eocspA4);
                ocsptest4.Add(eocspB4);

                ocsptest5.Add(eocsp5);
                ocsptest5.Add(eocspA5);
                ocsptest5.Add(eocspB5);

                ocsptest5.Add(eocsp6);
                ocsptest5.Add(eocspA6);
                ocsptest5.Add(eocspB6);
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                Console.WriteLine(e.StackTrace);
            }
        }

        private SignedDataValidationResult testVerifingESXLong(String file, Dictionary<String, Object> params_)
        {
            byte[] input = AsnIO.dosyadanOKU(INPUT_PATH + file);


            SignedDataValidation sdv = new SignedDataValidation();
            SignedDataValidationResult sdvr = sdv.verify(input, params_);

            Console.WriteLine(sdvr.ToString());

            return sdvr;
        }


        [Test]
        public void testVerifingESXLong1()
        {
            Console.WriteLine("----------------------testNegativeESX1-----------------------");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            params_[EParameters.P_INITIAL_CRLS] = crltest1;

            SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-1.p7s", params_);

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESXLong2()
        {
            Console.WriteLine("----------------------testNegativeESX2-----------------------");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            params_[EParameters.P_INITIAL_CRLS] = crltest2;

            SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-2.p7s", params_);

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESXLong3()
            //throws Exception
        {
            Console.WriteLine("----------------------testNegativeESX3-----------------------");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            params_[EParameters.P_INITIAL_CRLS] = crltest3;

            SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-3.p7s", params_);

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESXLong4()
        {
            Console.WriteLine("----------------------testNegativeESX4-----------------------");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            params_[EParameters.P_INITIAL_OCSP_RESPONSES] = ocsptest4;

            SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-4.p7s", params_);

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESXLong5()
        {
            Console.WriteLine("----------------------testNegativeESX5-----------------------");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            params_[EParameters.P_INITIAL_OCSP_RESPONSES] = ocsptest5;

            SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-5.p7s", params_);

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }

        [Test]
        public void testVerifingESXLong6()
        {
            Console.WriteLine("----------------------testNegativeESX6-----------------------");

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            params_[EParameters.P_INITIAL_OCSP_RESPONSES] = ocsptest6;

            SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-6.p7s", params_);

            Assert.AreEqual(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
        }
    }
}