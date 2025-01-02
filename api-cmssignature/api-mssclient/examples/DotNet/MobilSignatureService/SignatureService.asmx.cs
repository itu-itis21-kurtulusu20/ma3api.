using System;
using System.IO;
using System.Linq;
using System.Collections.Generic;
using System.Net;
using System.Web.Services;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;


namespace MobilSignatureService
{
    /// <summary>
    /// Summary description for Service1
    /// </summary>
    [WebService(Namespace = "http://localhost/webservices")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class Service1 : System.Web.Services.WebService
    {

        static EMSSPRequestHandler msspRequestHandler;

        static Service1()
        {
            //Türk Telekom mobil imza için eklendi.
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
        }

        private void loadLicense()
        {
            //write license path below
            FileStream fileStream = new FileStream(@"C:\ma3api-dotnet\lisans\lisans.xml", FileMode.Open, FileAccess.Read);
            LicenseUtil.setLicenseXml(fileStream);
        }

        [WebMethod]
        public void setCertificateInitials(String phoneNumber, int iOperator)
        {
            loadLicense();
            Operator mobileOperator = (Operator) iOperator;
            PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator(phoneNumber, mobileOperator);
            MSSParams mobilParams = null;

            if (mobileOperator == Operator.TURKCELL)
            {
                mobilParams = new MSSParams("********", "********", "www.turkcelltech.com");
                mobilParams.SetMsspSignatureQueryUrl("https://msign.turkcell.com.tr/MSSP2/services/MSS_Signature");
                mobilParams.SetMsspProfileQueryUrl("https://msign.turkcell.com.tr/MSSP2/services/MSS_ProfileQueryPort");

            }
            else if (mobileOperator == Operator.TURKTELEKOM)
            {
                mobilParams = new MSSParams("********", "********", "");
                mobilParams.SetMsspSignatureQueryUrl("https://mobilimza.turktelekom.com.tr/EGAMsspWSAP2/MSS_SignatureService");
                mobilParams.SetMsspProfileQueryUrl("https://mobilimza.turktelekom.com.tr/EGAMsspWSAP2/MSS_ProfileQueryService");
            }
            else if (mobileOperator == Operator.VODAFONE)
            {
                mobilParams = new MSSParams("********", "********", "mobilimza.vodafone.com.tr");
                mobilParams.SetMsspSignatureQueryUrl("https://mobilimza.vodafone.com.tr:443/Dianta2/MSS_SignatureService");
            }

            mobilParams.QueryTimeOutInSeconds = 120;
            mobilParams.ConnectionTimeOutMs = 120000;
            msspRequestHandler = new EMSSPRequestHandler(mobilParams);
            msspRequestHandler.setCertificateInitials(phoneNumberAndOperator);
        }

        [WebMethod]
        public string SignHash(String hashForSign64, String displayText,String phoneNumber, int iOperator, String signingAlg, bool isMultiSignature)
        {
            loadLicense();
            Operator mobileOperator = (Operator)iOperator;
            PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator(phoneNumber, mobileOperator);
            if (isMultiSignature)
            {

                String[] eachDataToBeSigned = hashForSign64.Split(';');
                List<byte[]> dataToBeSigned = getBase64DecodedData(eachDataToBeSigned);

                String[] eachInformativeText = displayText.Split(';');
                List<String> informativeTexts = new List<String>(eachInformativeText.ToList());

                try
                {
                    return msspRequestHandler.sign(dataToBeSigned, SigningMode.SIGNHASH, phoneNumberAndOperator, informativeTexts, signingAlg, null);
                }
                catch (Exception ex)
                {
                    throw new ESYAException("Error in multi signing", ex);
                }

            }
            else
            {
                byte[] dataForSign = Convert.FromBase64String(hashForSign64);
                byte[] signedData;
                try
                {
                    signedData = msspRequestHandler.Sign(dataForSign, SigningMode.SIGNHASH, phoneNumberAndOperator, displayText, signingAlg);
                } catch(Exception ex)
                {
                    throw new ESYAException("Error in single signing", ex);
                }
                return Convert.ToBase64String(signedData);
            }
        }

        private List<byte[]> getBase64DecodedData(string[] eachDataToBeSigned)
        {
            List<byte[]> base64DecodedData = new List<byte[]>();
            for (int i = 0; i < eachDataToBeSigned.Length; i++)
                base64DecodedData.Add(Convert.FromBase64String(eachDataToBeSigned[i]));

            return base64DecodedData;
        }

        [WebMethod]
        public string getSigningCert()
        {
            loadLicense();
            return Convert.ToBase64String(msspRequestHandler.getSigningCert().getEncoded());
        }

        [WebMethod]
        public string getSigningCertAttr()
        {
            loadLicense();
            ESigningCertificate sc = new ESigningCertificate(msspRequestHandler.getSigningCertAttr());
            return Convert.ToBase64String(sc.getEncoded());
        }

        [WebMethod]
        public string getSigningCertAttrv2()
        {
            loadLicense();
            return Convert.ToBase64String(msspRequestHandler.getSigningCertAttrv2().getEncoded());
        }

        [WebMethod]
        public string getSignerIdentifier()
        {
            loadLicense();
            return Convert.ToBase64String(msspRequestHandler.getSignerIdentifier().getEncoded());
        }

        [WebMethod]
        public string getDigestAlg()
        {
            loadLicense();
            return msspRequestHandler.getDigestAlg().getName();
        }
    }
}