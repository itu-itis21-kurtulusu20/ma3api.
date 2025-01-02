package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.profile;

import org.apache.axis.message.MessageElement;
import org.etsi.uri.TS102204.v1_1_2.MSS_ProfileRespType;
import org.etsi.uri.TS102204.v1_1_2.MssURIType;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.ProfileInfo;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.StringUtil;

import java.math.BigInteger;
import java.util.List;


/**
 * Turkcell mobile signature profile response implementation
 * @see IProfileResponse
 */
public class TurkcellProfileResponse implements IProfileResponse {

    private static String RESPONSE_XML_FIELD_NAME_CERT_SERIAL_NUMBER = "CertSerialNumber";
    private static String RESPONSE_XML_FIELD_NAME_CERT_ISSUER_DN = "CertIssuerDN";
    private static String RESPONSE_XML_FIELD_NAME_CERT_ISSUER_DN_DER = "certIssuerDN-DER";
    private static String RESPONSE_XML_FIELD_NAME_CERT_HASH = "CertHash";
    
    protected MSS_ProfileRespType trcellProfileResp = null;

    public TurkcellProfileResponse(MSS_ProfileRespType aStatusResponse) {
        trcellProfileResp = aStatusResponse;
    }

    public Status getStatus() {
        return new Status(trcellProfileResp.getStatus().getStatusCode().getValue().toString(), trcellProfileResp.getStatus().getStatusMessage());
    }

    public String getMSSUri() {
        return trcellProfileResp.getSignatureProfile(0).getMssURI().toString();
    }

    public ProfileInfo getProfileInfo() {
        String certSerial=null;
        String issuerName=null;
        String hash=null;
        String issuerDNDer=null;
        String mssProfileURI = null;
        String digestAlg = null;

        MssURIType[] signatureProfile = trcellProfileResp.getSignatureProfile();
        if(signatureProfile!=null){
            for (MssURIType mssURIType : signatureProfile) {
                mssProfileURI = mssURIType.getMssURI().toString();
                MessageElement[] any = mssURIType.get_any();
                if(any!=null)
                {
                    for (MessageElement messageElement : any) {
                        String elementName = messageElement.getName();
                        if(elementName.equals(RESPONSE_XML_FIELD_NAME_CERT_SERIAL_NUMBER))
                        {
                            certSerial = messageElement.getValue();
                            BigInteger bg= new BigInteger(certSerial,10);
                            byte[] bytes = bg.toByteArray();
                            certSerial = StringUtil.toString(bytes);
                        }
                        else  if(elementName.equals(RESPONSE_XML_FIELD_NAME_CERT_ISSUER_DN))
                        {
                            issuerName = messageElement.getValue();
                        }
                        else if(elementName.equals(RESPONSE_XML_FIELD_NAME_CERT_ISSUER_DN_DER))
                        {
                            issuerDNDer = messageElement.getValue();
                        }
                        else if(elementName.equals(RESPONSE_XML_FIELD_NAME_CERT_HASH))
                        {
							List<MessageElement> ms = messageElement.getChildren();
							hash = ms.get(1).getValue();
							digestAlg = ms.get(0).getAttribute("Algorithm");
                        }
                    }
                }
            }
        }
        ProfileInfo retProfileInfo = new ProfileInfo();
        retProfileInfo.setSerialNumber(certSerial);
        retProfileInfo.setIssuerName(issuerName);
        retProfileInfo.setCertIssuerDN(issuerDNDer);
        retProfileInfo.setMssProfileURI(mssProfileURI);
        retProfileInfo.setCertHash(hash);
        retProfileInfo.setDigestAlg(digestAlg);
        return retProfileInfo;
    }

    public ECertificate getCertificate() {
        return null;
    }

    public MSS_ProfileRespType getTrcellProfileResp() {
        return trcellProfileResp;
    }
}
