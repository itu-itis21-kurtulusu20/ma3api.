using System;
using System.Reflection;
using System.Xml;
using Org.BouncyCastle.Math;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.stub.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.profile
{
    /**
 * Turk Telekom mobile signature profile response implementation
 * @see IProfileResponse
 */

    public class TurkTelekomProfileResponse : IProfileResponse
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static String RESPONSE_XML_FIELD_NAME_CERT_SERIAL_NUMBER = "CertSerialNumber";
        private static String RESPONSE_XML_FIELD_NAME_CERT_ISSUER_DN = "CertIssuerDN";
        private static String RESPONSE_XML_FIELD_NAME_CERT_ISSUER_DN_DER = "certIssuerDN-DER";
        private static String RESPONSE_XML_FIELD_NAME_CERT_HASH = "CertHash";
        private readonly MSS_ProfileRespType _profileResponse;

        public TurkTelekomProfileResponse(MSS_ProfileRespType aStatusResponse)
        {
            _profileResponse = aStatusResponse;
        }

        public Status getStatus()
        {
            StatusType statusType = _profileResponse.Status;
            if(statusType == null)
            {
                return null;
            }
            return new Status(statusType.StatusCode.Value, _profileResponse.Status.StatusMessage);
        }

        public String getMSSUri()
        {
            return _profileResponse.SignatureProfile[0].mssURI;
        }

       public ProfileInfo getProfileInfo() {
        logger.Debug("Profile response dan seri numarası ve issuer alınacak.");
        String certSerial=null;
        String issuerName=null;
        String issuerDNDer=null;
        String hash = null;
        String digestAlg = null;

        mssURIType[] signatureProfile = _profileResponse.SignatureProfile;
        if(signatureProfile == null)
        {
             return null;
        }
        if(signatureProfile!=null){
            foreach (mssURIType mssUriType in signatureProfile)
            {
                XmlElement[] xmlElements = mssUriType.Any;
                if(xmlElements!=null)
                {
                    foreach (XmlElement xmlElement in xmlElements)
                    {
                        string elementName = xmlElement.Name;
                        if(elementName.Contains(RESPONSE_XML_FIELD_NAME_CERT_SERIAL_NUMBER))
                        {
                            certSerial = xmlElement.InnerText;
                            BigInteger bg= new BigInteger(certSerial,10);
                            byte[] bytes = bg.ToByteArray();
                            certSerial = StringUtil.ToString(bytes);
                        }
                        else if (elementName.Contains(RESPONSE_XML_FIELD_NAME_CERT_ISSUER_DN))
                        {
                            issuerName = xmlElement.InnerText;
                        }
                        else if (elementName.Contains(RESPONSE_XML_FIELD_NAME_CERT_ISSUER_DN_DER))
                        {
                            issuerDNDer = xmlElement.InnerText;
                        }
                        else if (elementName.Contains(RESPONSE_XML_FIELD_NAME_CERT_HASH))
                        {
                            hash = xmlElement.ChildNodes.Item(1).InnerText;
                            digestAlg = xmlElement.ChildNodes.Item(0).InnerText;  //getAttribute("Algorithm");
                        }
                    }
                }
            }
        }
        ProfileInfo retProfileInfo = new ProfileInfo();
        retProfileInfo.SetSerialNumber(certSerial);
        retProfileInfo.SetIssuerName(issuerName);
        retProfileInfo.SetCertIssuerDN(issuerDNDer);
        retProfileInfo.SetCertHash(hash);
        retProfileInfo.setDigestAlg(digestAlg);
        logger.Debug("Profile response dan seri numarası ve issuer alındı.Serial ="+certSerial+",issuer="+issuerName);
        return retProfileInfo;
    }

       public ECertificate getCertificate()
       {
           return null;
       }
    }
}