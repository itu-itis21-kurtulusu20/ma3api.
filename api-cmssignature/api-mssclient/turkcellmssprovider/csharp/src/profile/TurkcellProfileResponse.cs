using System;
using System.Reflection;
using System.Xml;
using Org.BouncyCastle.Math;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.stub;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.profile
{
    /**
 * Turkcell mobile signature profile response implementation
 * @see IProfileResponse
 */

    public class TurkcellProfileResponse : IProfileResponse
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static String RESPONSE_XML_FIELD_NAME_CERT_SERIAL_NUMBER = "CertSerialNumber";
        private static String RESPONSE_XML_FIELD_NAME_CERT_ISSUER_DN = "CertIssuerDN";
        private static String RESPONSE_XML_FIELD_NAME_CERT_ISSUER_DN_DER = "certIssuerDN-DER";
        private static String RESPONSE_XML_FIELD_NAME_CERT_HASH = "CertHash";
        private readonly MSS_ProfileRespType _trcellProfileResp;

        public TurkcellProfileResponse(MSS_ProfileRespType aStatusResponse)
        {
            _trcellProfileResp = aStatusResponse;
        }

        public Status getStatus()
        {
            return new Status(_trcellProfileResp.Status.StatusCode.Value, _trcellProfileResp.Status.StatusMessage);
        }

        public String getMSSUri()
        {
            return _trcellProfileResp.SignatureProfile[0].mssURI;
        }

        public ProfileInfo getProfileInfo()
        {
            logger.Debug("Profile response dan seri numarası ve issuer alınacak.");
            String certSerial = null;
            String issuerName = null;
            String hash = null;
            String issuerDNDer = null;
            String mssProfileURI = null;
            String digestAlg = null;

            mssURIType[] signatureProfile = _trcellProfileResp.SignatureProfile;
            if (signatureProfile != null)
            {
                foreach (mssURIType mssUriType in signatureProfile)
                {
                    mssProfileURI = mssUriType.mssURI;
                    XmlElement[] xmlElements = mssUriType.Any;
                    if (xmlElements != null)
                    {
                        foreach (XmlElement xmlElement in xmlElements)
                        {
                            string elementName = xmlElement.Name;
                            if (elementName.Contains(RESPONSE_XML_FIELD_NAME_CERT_SERIAL_NUMBER))
                            {
                                certSerial = xmlElement.InnerText;
                                BigInteger bg = new BigInteger(certSerial, 10);
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
                                digestAlg = ((XmlElement)xmlElement.ChildNodes.Item(0)).GetAttribute("Algorithm");
                            }
                        }
                    }
                }
            }
            ProfileInfo retProfileInfo = new ProfileInfo();
            retProfileInfo.SetSerialNumber(certSerial);
            retProfileInfo.SetIssuerName(issuerName);
            retProfileInfo.SetCertIssuerDN(issuerDNDer);
            retProfileInfo.setMssProfileURI(mssProfileURI);
            retProfileInfo.SetCertHash(hash);
            retProfileInfo.setDigestAlg(digestAlg);
            logger.Debug("Profile response dan seri numarası ve issuer alındı.Serial =" + certSerial + ",issuer=" + issuerName);
            return retProfileInfo;
        }

        public ECertificate getCertificate()
        {
            return null;
        }
    }
}