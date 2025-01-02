package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import com.objsys.asn1j.runtime.Asn1BitString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.BasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.ocsp.SingleResponse;
import tr.gov.tubitak.uekae.esya.asn.ocsp._SeqOfCertificate;

import java.util.Calendar;

/**
 * @author ahmety
 * date: Feb 8, 2010
 */
public class EBasicOCSPResponse extends BaseASNWrapper<BasicOCSPResponse> {

    protected static Logger logger = LoggerFactory.getLogger(EBasicOCSPResponse.class);

    public EBasicOCSPResponse(BasicOCSPResponse aObject) {
        super(aObject);
    }

    public EBasicOCSPResponse(byte[] aBytes) throws ESYAException {
        super(aBytes, new BasicOCSPResponse());
    }

    public EBasicOCSPResponse(EResponseData responseData,
                              EAlgorithmIdentifier algorithmIdentifier,
                              byte[] signature,
                              ECertificate[] certificates) throws ESYAException {
        super(new BasicOCSPResponse(
                responseData.getObject(),
                algorithmIdentifier.getObject(),
                new Asn1BitString(signature.length << 3, signature),
                new _SeqOfCertificate(unwrapArray(certificates))
        ));
    }

    public EResponseData getTbsResponseData(){
        return new EResponseData(mObject.tbsResponseData);
    }

    public byte[] getSignature(){
        return mObject.signature.value;
    }

    public EAlgorithmIdentifier getSignatureAlgorithm(){
        return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
    }

    public int getCertificateCount(){
        return mObject.certs.getLength();
    }

    public ECertificate getCertificate(int aIndex){
        return new ECertificate(mObject.certs.elements[aIndex]);
    }

    public EExtensions getResponseExtensions(){
        if (mObject.tbsResponseData.responseExtensions!=null)
            return new EExtensions(mObject.tbsResponseData.responseExtensions);
        return null;
    }

    public Calendar getProducedAt(){
        try {
            return mObject.tbsResponseData.producedAt.getTime();
        } catch (Exception x){
            logger.warn("Warning in EBasicOCSPResponse", x);
            return null;
        }
    }
   
    public ESingleResponse getSingleResponse(ECertID aCertID) throws ESYAException {
    	SingleResponse [] responses = mObject.tbsResponseData.responses.elements;
    	for(int i=0; i < responses.length; i++) {
    		ECertID certID = new ECertID(responses[i].certID);
    		if(certID.equals(aCertID))
    			return new ESingleResponse(responses[i]);
    	}
    	throw new ESYAException("ECertID can not be found in the response");
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SingleResponse[] responses = mObject.tbsResponseData.responses.elements;
        for (int i = 0; i < responses.length; i++) {
            ESingleResponse singleResponse = new ESingleResponse(responses[i]);
            sb.append(singleResponse.toString());
            sb.append(System.getProperty("line.separator"));
            sb.append("---------------------------------------------------------");
        }
        return sb.toString();
    }
}
