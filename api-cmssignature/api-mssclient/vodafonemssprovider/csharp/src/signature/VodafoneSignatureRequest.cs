using System;
using System.Net;
using System.Net.Security;
using System.Reflection;
using System.ServiceModel;
using MobilImzaYapilari;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common;
using VodafoneMobilImza.VodafoneServis;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.signature
{
    public class VodafoneSignatureRequest : ISignatureRequest
    {
        private static readonly ILog Logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private MSSParams _params;

        public VodafoneSignatureRequest(MSSParams aParams)
        {
            setMSSParams(aParams);
        }

        public void setMSSParams(MSSParams aParams)
        {
            _params = aParams;
        }

        public void setServiceUrl(string aServiceUrl)
        {
            _params.SetMsspSignatureQueryUrl(aServiceUrl);
        }

        public ISignatureResponse sendRequest(string aTransId, string aMSISDN, ISignable aSignable)
        {
            string MSISDN = aMSISDN;
            if (MSISDN.StartsWith("0"))
                MSISDN = MSISDN.Substring(1);

            string valueToBeDisplayed = aSignable.getValueToBeDisplayed();
            if (valueToBeDisplayed.Length > 7)
            {
                Logger.Warn(
                    "At vodafone mobile signature the displayed info string can be longer than seven characters. It is shortened from " +
                    valueToBeDisplayed.Length + " to 7.");
                valueToBeDisplayed = valueToBeDisplayed.Substring(0, 7);
            }

            byte[] hashOfSignedAttributes = Convert.FromBase64String(aSignable.getValueToBeSigned());

            
            String timeout = _params.QueryTimeOutInSeconds.ToString();

            MImzaResponse resp = senkronMobilImzalama(
                MSISDN,
                _params.AP_ID,
                _params.PWD,
                aTransId,
                hashOfSignedAttributes,
                valueToBeDisplayed,
                timeout
            );

            if (resp.getExceptionValue() != null)
            {
                MImzaException mexc = resp.getExceptionValue();
                throw new ESYAException("Hata Kodu: " + mexc.getErrorCode() + ". " + "Hata İçerik: " +
                                        mexc.getErrorMessage() + ". " + mexc.Message);
            }

            if (resp.getObjectValue() == null)
                throw new ESYAException("Yanıtta veri bulunamadı");

            byte[] signatureBytes = (byte[]) resp.getObjectValue();

            return new VodafoneSignatureResponse(aTransId, MSISDN, signatureBytes, Status.REQUEST_OK);
        }

        /*
         * VodafoneMobilImza.dll içerisinden decompile edilerek alındı. 
         * Address bilgisini parametre olarak alabilmesi sağlandı.
         */
        private MImzaResponse senkronMobilImzalama(
            string msIsdn,
            string ap_id,
            string ap_password,
            string trans_id,
            byte[] dtbsigned,
            string dtbdisplayed,
            string timeout)
        {
            Uri address = new Uri(_params.GetMsspSignatureQueryUrl());
            EndpointAddress endpointAddress = new EndpointAddress(address);

            BasicHttpBinding httpBinding = new BasicHttpBinding();
            httpBinding.OpenTimeout = TimeSpan.FromMilliseconds(_params.ConnectionTimeOutMs);
            httpBinding.Security.Mode = BasicHttpSecurityMode.Transport;

            MImzaResponse mimzaResponse1 = new MImzaResponse();
            try
            {
                if (ap_id == null || ap_password == null || (trans_id == null || msIsdn == null) || dtbsigned == null)
                {
                    MImzaException exVal = new MImzaException(MSS_StatusCodes.getStatusMessage(101), 101);
                    mimzaResponse1.setExceptionValue(exVal);
                    return mimzaResponse1;
                }

                MSS_SignatureReqType MSS_SignatureReq = new MSS_SignatureReqType();
                MSS_SignaturePortType signaturePortType = (MSS_SignaturePortType) new MSS_SignaturePortTypeClient(httpBinding, endpointAddress);
                MSS_SignatureReq.MajorVersion = "1";
                MSS_SignatureReq.MinorVersion = "1";
                MSS_SignatureReq.MessagingMode = MessagingModeType.synch;
                int num = timeout == null ? 1 : (int.Parse(timeout) <= 0 ? 1 : 0);
                MSS_SignatureReq.TimeOut = num != 0 ? "200" : timeout;
                MSS_SignatureReq.AP_Info = new MessageAbstractTypeAP_Info();
                MSS_SignatureReq.AP_Info.AP_ID = ap_id;
                MSS_SignatureReq.AP_Info.AP_PWD = ap_password;
                MSS_SignatureReq.AP_Info.AP_TransID = trans_id;
                MSS_SignatureReq.AP_Info.Instant = DateTime.Now;
                MSS_SignatureReq.MSSP_Info = new MessageAbstractTypeMSSP_Info()
                {
                    MSSP_ID = new MeshMemberType()
                    {
                        URI = "http://dianta2.vodafone.com.tr"
                    }
                };
                MSS_SignatureReq.MobileUser = new MobileUserType();
                MSS_SignatureReq.MobileUser.MSISDN = msIsdn;
                MSS_SignatureReq.DataToBeSigned = new DataType();
                MSS_SignatureReq.DataToBeSigned.Value = Convert.ToBase64String(dtbsigned);
                MSS_SignatureReq.DataToBeSigned.MimeType = "application/octet-stream";
                MSS_SignatureReq.DataToBeSigned.Encoding = "base64";
                if (dtbdisplayed != null && !dtbdisplayed.Equals(""))
                {
                    MSS_SignatureReq.DataToBeDisplayed = new DataType();
                    MSS_SignatureReq.DataToBeDisplayed.Value = dtbdisplayed;
                    MSS_SignatureReq.DataToBeDisplayed.MimeType = "text/plain";
                    MSS_SignatureReq.DataToBeDisplayed.Encoding = "UTF-8";
                }

                AdditionalServiceType additionalServiceType = new AdditionalServiceType();
                additionalServiceType.Description = new mssURIType()
                {
                    mssURI = "http://uri.etsi.org/TS102204/v1.1.2#validate"
                };
                MSS_SignatureReq.SignatureProfile = new mssURIType();
                MSS_SignatureReq.SignatureProfile.mssURI = "http://uri.etsi.org/TS102204/v1.1.2#validate";
                MSS_SignatureReq.AdditionalServices = new AdditionalServiceType[1];
                MSS_SignatureReq.AdditionalServices[0] = additionalServiceType;
                MSS_SignatureReq.MSS_Format = new mssURIType()
                {
                    mssURI = "http://uri.etsi.org/TS102204/v1.1.2#PKCS7"
                };
                MSS_SignatureReq.TimeOut = timeout;
                ServicePointManager.ServerCertificateValidationCallback =
                    (RemoteCertificateValidationCallback) ((sender, certificate, chain, sslPolicyErrors) => true);
                MSS_SignatureRespType signatureRespType = signaturePortType.MSS_Signature(MSS_SignatureReq);
                int int32 = Convert.ToInt32(signatureRespType.Status.StatusCode.Value);
                
                if (int32 == 502)
                {
                    byte[] signatureBytes = (byte[]) signatureRespType.MSS_Signature.Item;

                    MImzaResponse mimzaResponse2 = new MImzaResponse();
                    mimzaResponse2.setObjectValue(signatureBytes);
                    return mimzaResponse2;
                }
            }
            catch (Exception ex)
            {
                bool flag = true;
                MImzaException exVal = new MImzaException(ex.Message, ex);
                string str = ex.Message.IndexOf(" ") <= 0
                    ? ex.Message
                    : ex.Message.Substring(0, ex.Message.IndexOf(" "));
                if (str.Equals("WRONG_DATA_LENGTH"))
                    exVal.setErrorCode(103);
                else if (str.Equals("UNKNOWN_CLIENT"))
                    exVal.setErrorCode(105);
                else if (str.Equals("UNAUTHORIZED_ACCESS"))
                    exVal.setErrorCode(104);
                else if (str.Equals("REQUEST_OK"))
                    exVal.setErrorCode(100);
                else if (str.Equals("USER_SIGN"))
                    exVal.setErrorCode(400);
                else if (str.Equals("REGISTRATION_OK"))
                    exVal.setErrorCode(408);
                else if (str.Equals("REVOKED_CERTIFICATE"))
                    exVal.setErrorCode(501);
                else if (str.Equals("VALID_SIGNATURE"))
                    exVal.setErrorCode(502);
                else if (str.Equals("INVALID_SIGNATURE"))
                    exVal.setErrorCode(503);
                else if (str.Equals("OUTSTANDING_TRANSACTION"))
                    exVal.setErrorCode(504);
                else if (str.Equals("OK_WITH_PUSH_INFO"))
                    exVal.setErrorCode(600);
                else if (str.Equals("OK_WITHOUT_PUSH_INFO"))
                    exVal.setErrorCode(601);
                else if (str.Equals("NOK_WITH_PUSH_INFO"))
                    exVal.setErrorCode(602);
                else if (str.Equals("NOK_WITHOUT_PUSH_INFO"))
                    exVal.setErrorCode(603);
                else
                    flag = false;
                if (flag)
                {
                    exVal.setErrorMessage(MSS_StatusCodes.getStatusMessage(exVal.getErrorCode()));
                }
                else
                {
                    exVal.setErrorMessage(ex.Message);
                    exVal.setErrorCode(-100);
                }

                mimzaResponse1.setExceptionValue(exVal);
            }

            return mimzaResponse1;
        }
    }
}