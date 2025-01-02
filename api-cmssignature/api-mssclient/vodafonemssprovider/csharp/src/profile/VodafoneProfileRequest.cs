using System;
using System.Net;
using System.Net.Security;
using System.ServiceModel;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;
using VodafoneMobilImza.VodafoneServis;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.profile
{
    class VodafoneProfileRequest : IProfileRequest
    {
        private MSSParams _params;

        public VodafoneProfileRequest(MSSParams aParams)
        {
            setMSSParams(aParams);
        }

        public void setMSSParams(MSSParams aParams)
        {
            _params = aParams;
        }

        public void setServiceUrl(string aServiceUrl)
        {
            _params.SetMsspProfileQueryUrl(aServiceUrl);
        }

        public IProfileResponse sendRequest(string aMSISDN, string aApTransId)
        {
            ECertificate certificate = getSignerCertificate(aMSISDN);
            return new VodafoneProfileResponse(certificate, _params);
        }

        public ECertificate getSignerCertificate(string aMSISDN)
        {
            string MSISDN = string.Copy(aMSISDN);
            // telefon numarasının (MSISDN) başındaki '0'ın atılması
            if (MSISDN.StartsWith("0"))
                MSISDN = MSISDN.Substring(1);

            ECertificate cert = getSignerCertificateFromService(MSISDN, _params.AP_ID, _params.PWD);
            return cert;
        }



        /*
         * VodafoneMobilImza.dll içerisinden decompile edilerek alındı. 
         * Sadece sertifika çekme fonksiyonu bulunmuyordu, bizim sertifika çekmek yeterli idi.
         * Address bilgisini parametre olarak almıyordu.
         */
        private ECertificate getSignerCertificateFromService(string MSISDN, string AP_ID, string PWD)
        {
            Uri address = new Uri(_params.GetMsspSignatureQueryUrl());
            
            EndpointAddress endpointAddress = new EndpointAddress(address);


            BasicHttpBinding httpBinding = new BasicHttpBinding();
            httpBinding.OpenTimeout = TimeSpan.FromMilliseconds(_params.ConnectionTimeOutMs);
            httpBinding.Security.Mode = BasicHttpSecurityMode.Transport;
            

            ServicePointManager.ServerCertificateValidationCallback =
                (RemoteCertificateValidationCallback) ((sender, certificate, chain, sslPolicyErrors) => true);
            MSS_RegistrationReqType MSS_RegistrationReq = new MSS_RegistrationReqType();
            MSS_SignaturePortType signaturePortType =
                (MSS_SignaturePortType) new MSS_SignaturePortTypeClient(httpBinding, endpointAddress);
            MSS_RegistrationReq.MajorVersion = "1";
            MSS_RegistrationReq.MinorVersion = "1";
            MSS_RegistrationReq.AP_Info = new MessageAbstractTypeAP_Info();
            MSS_RegistrationReq.AP_Info.AP_ID = AP_ID;
            MSS_RegistrationReq.AP_Info.AP_PWD = PWD;
            MSS_RegistrationReq.AP_Info.AP_TransID = "1110";
            MSS_RegistrationReq.AP_Info.Instant = DateTime.Now;
            MSS_RegistrationReq.MobileUser = new MobileUserType();
            MSS_RegistrationReq.MobileUser.MSISDN = MSISDN;
            MSS_RegistrationReq.MSSP_Info = new MessageAbstractTypeMSSP_Info();
            MSS_RegistrationReq.MSSP_Info = new MessageAbstractTypeMSSP_Info()
            {
                MSSP_ID = new MeshMemberType()
                {
                    URI = "http://dianta2.vodafone.com.tr"
                }
            };

            MSS_RegistrationRespType registrationRespType = signaturePortType.MSS_Registration(MSS_RegistrationReq);
            if (registrationRespType.Status.StatusCode.Value.Equals("408"))
            {
                byte [] certBytes = registrationRespType.X509Certificate;
                return new ECertificate(certBytes);
            }
            else
            {
                throw new ESYAException("Can not get certificate from Vodafone. Status: " + registrationRespType.Status);
            }
        }
    }
}