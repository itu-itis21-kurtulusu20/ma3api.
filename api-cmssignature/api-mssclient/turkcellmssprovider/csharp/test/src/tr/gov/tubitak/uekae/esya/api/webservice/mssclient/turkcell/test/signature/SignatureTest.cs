using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.test.signature
{
    [TestFixture]
    public class SignatureTest
    {
        MSSParams mobilParams = new MSSParams("http://MImzaTubitakBilgem", "zR4*9a2+78", "www.turkcelltech.com");
        [Test]
        public void testBinarySignature()
        {
            EMSSPRequestHandler msspRequestHandler = new EMSSPRequestHandler(mobilParams);
            
            PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator("05380892868",Operator.TURKCELL);
            msspRequestHandler.setCertificateInitials(phoneNumberAndOperator);

            byte[] dataForSign = new byte[1];
            byte[] signedData = msspRequestHandler.Sign(dataForSign, SigningMode.SIGNHASH, phoneNumberAndOperator, "Ekranda görünecek.",null);
            
        }


        public bool ByteArrayToFile(string _FileName, byte[] _ByteArray)
        {
            try
            {
                // Open file for reading
                System.IO.FileStream _FileStream = new System.IO.FileStream(_FileName, System.IO.FileMode.Create, System.IO.FileAccess.Write);

                // Writes a block of bytes to this stream using data from a byte array.
                _FileStream.Write(_ByteArray, 0, _ByteArray.Length);

                // close file stream
                _FileStream.Close();

                return true;
            }
            catch (Exception _Exception)
            {
                // Error
                Console.WriteLine("Exception caught in process: {0}", _Exception.ToString());
            }

            // error occurred, return false
            return false;
        }

        [Test]
        public void testTextSignature()
        {
          
            EMSSPRequestHandler msspRequestHandler = new EMSSPRequestHandler(mobilParams);

            PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator("05380892868", Operator.TURKCELL);
             msspRequestHandler.setCertificateInitials(phoneNumberAndOperator);

            String imzalanacakMetin = "Bu metin imzalanacak.";
            byte[] signedData = msspRequestHandler.SignText(imzalanacakMetin, phoneNumberAndOperator,
                                                            SignatureType.PKCS7);

            ByteArrayToFile("RmZMobilImzaliPKCS7.dat", signedData);


        }

        [Test]
        public void testXMLTextSignature()
        {

            EMSSPRequestHandler msspRequestHandler = new EMSSPRequestHandler(mobilParams);

            PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator("05380892868", Operator.TURKCELL);
            msspRequestHandler.setCertificateInitials(phoneNumberAndOperator);

            String imzalanacakMetin = "Bu metin imzalanacak.";
            byte[] signedData = msspRequestHandler.SignText(imzalanacakMetin, phoneNumberAndOperator,
                                                            SignatureType.XML);

            ByteArrayToFile("RmZMobilImzaliXML.dat", signedData);


        }
    }
}
