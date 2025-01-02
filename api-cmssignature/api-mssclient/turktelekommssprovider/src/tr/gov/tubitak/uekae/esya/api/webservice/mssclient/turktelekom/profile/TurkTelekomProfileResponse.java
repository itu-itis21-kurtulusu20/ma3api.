package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.profile;

import org.etsi.uri.ts102204.v1_1.turktelekom.MSSProfileRespType;
import org.etsi.uri.ts102204.v1_1.turktelekom.MssURIType;
import org.etsi.uri.ts102204.v1_1.turktelekom.StatusCodeType;
import org.etsi.uri.ts102204.v1_1.turktelekom.StatusType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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
public class TurkTelekomProfileResponse implements IProfileResponse {

    protected MSSProfileRespType mssProfileRespType;

    public TurkTelekomProfileResponse(MSSProfileRespType mssProfileRespType) {
        this.mssProfileRespType = mssProfileRespType;
    }

    public Status getStatus() {
        StatusType status = mssProfileRespType.getStatus();
        if(status == null){
            return  null;
        }
        StatusCodeType statusCode = status.getStatusCode();
        return new Status(statusCode.getValue().toString(10), status.getStatusMessage());
    }

    public String getMSSUri() {
        List<MssURIType> signatureProfile = mssProfileRespType.getSignatureProfile();
        return signatureProfile.get(0).getMssURI();
    }

    public String getUserCertSerial(){
        String certSerial=null;
        List<MssURIType> signatureProfile = mssProfileRespType.getSignatureProfile();
        if(signatureProfile!=null){
            for (MssURIType mssURIType : signatureProfile) {
                /*
                List<Object> any = mssURIType.getAny();
                MessageElement[] any = mssURIType.get_any();
                if(any!=null)
                {
                    for (MessageElement messageElement : any) {
                        if(messageElement.getName().equals("CertSerialNumber"))
                        {
                            certSerial = messageElement.getValue();
                            BigInteger bg= new BigInteger(certSerial,10);
                            certSerial = bg.toString(16);
                            return certSerial.toUpperCase();
                        }
                    }
                }  */
            }
        }
        return certSerial;
    }

    public ProfileInfo getProfileInfo() {
        String certSerial=null;
        String issuerName=null;
        String issuerDNDer=null;
        String hash=null;
        String digestAlg = null;
        List<MssURIType> signatureProfile = mssProfileRespType.getSignatureProfile();
        if(signatureProfile!=null){
            for (MssURIType mssURIType : signatureProfile) {
                List<Object> any = mssURIType.getAny();
                if(any!=null)
                {
                    for (Object element : any) {
                        Element messageElement = (Element) element;

                        String elementName = messageElement.getLocalName();
                        if(elementName.equals("CertSerialNumber"))
                        {
                            certSerial = messageElement.getTextContent();
                            BigInteger bg= new BigInteger(certSerial,10);
                            byte[] bytes = bg.toByteArray();
                            certSerial = StringUtil.toString(bytes);
                        }
                        else  if(elementName.equals("CertIssuerDN"))
                        {
                            issuerName = messageElement.getTextContent();
                        }
                        else if(elementName.equals("certIssuerDN-DER"))
                        {
                            issuerDNDer = messageElement.getTextContent();
                        }
                        else if(elementName.equals("CertHash"))
                        {
							NodeList ms = messageElement.getChildNodes();
							hash = ms.item(1).getTextContent();							
							digestAlg = ms.item(0).getTextContent();  //getAttribute("Algorithm");
                        }
                    }
                }
            }
        }

        if(certSerial == null){
            return  null;
        }

        ProfileInfo retProfileInfo = new ProfileInfo();
        retProfileInfo.setSerialNumber(certSerial);
        retProfileInfo.setIssuerName(issuerName);
        retProfileInfo.setCertIssuerDN(issuerDNDer);
        retProfileInfo.setCertHash(hash);
        retProfileInfo.setDigestAlg(digestAlg);
        return retProfileInfo;
    }

    public ECertificate getCertificate() {
        return null;
    }

    public MSSProfileRespType getMssProfileRespType() {
        return mssProfileRespType;
    }
}
