package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ahmety
 * date: Feb 8, 2010
 */
public class EOCSPResponse extends BaseASNWrapper<OCSPResponse> {

    protected static Logger logger = LoggerFactory.getLogger(EOCSPResponse.class);

    private EBasicOCSPResponse mBasicResponse;
	
	public static EOCSPResponse getEOCSPResponse(EBasicOCSPResponse aResponse) {
    	OCSPResponse response = new OCSPResponse();
    	response.responseStatus =  OCSPResponseStatus.successful();
        response.responseBytes = new ResponseBytes(new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_basic),new Asn1OctetString(aResponse.getEncoded()));
		return new EOCSPResponse(response);
    }

    public static ArrayList<EOCSPResponse> getEOCSPResponseArrayList(List<EBasicOCSPResponse> aResponse){
        ArrayList<EOCSPResponse> ocspResponses = new ArrayList<EOCSPResponse>();
        for (EBasicOCSPResponse basicOCSPResponse : aResponse) {
            ocspResponses.add(getEOCSPResponse(basicOCSPResponse));
        }
        return ocspResponses;
    }

    public static ArrayList<EBasicOCSPResponse> getEBasicOCSPResponseArrayList(List<EOCSPResponse> aResponses){
        ArrayList<EBasicOCSPResponse> ocspBasicResponses = new ArrayList<EBasicOCSPResponse>();
        for (EOCSPResponse aResponse : aResponses) {
            ocspBasicResponses.add(aResponse.getBasicOCSPResponse());
        }
        return ocspBasicResponses;
    }

    public EOCSPResponse(OCSPResponse aObject)
    {
        super(aObject);
    }

    public EOCSPResponse(OCSPResponseStatus responseStatus)  {
        super(new OCSPResponse(responseStatus));
    }

    public EOCSPResponse(OCSPResponseStatus responseStatus,
                         Asn1ObjectIdentifier oid,
                         byte[] responseBytes) throws ESYAException {
        super(new OCSPResponse(
                responseStatus,
                new ResponseBytes(
                        oid,
                        new Asn1OctetString(responseBytes)
                )));
    }

    public EOCSPResponse(byte[] aBytes) throws ESYAException {
    	super(aBytes,new OCSPResponse());
    }

    public EBasicOCSPResponse getBasicOCSPResponse() {
        if (mBasicResponse!=null)
            return mBasicResponse;

        BasicOCSPResponse basicResponse = new BasicOCSPResponse();
        try {
            byte[] value = mObject.responseBytes.response.value;
            Asn1DerDecodeBuffer a = new Asn1DerDecodeBuffer(value);
            basicResponse.decode(a);
        } catch (Exception ex) {
            logger.warn("Warning in EOCSPResponse", ex);
            return null;
        }
        mBasicResponse = new EBasicOCSPResponse(basicResponse);
        return mBasicResponse;
    }


    public ESingleResponse getSingleResponse()
    {
        return getSingleResponse(0);
    }

    public int getSingleResponseCount() {
        EBasicOCSPResponse basicResponse = getBasicOCSPResponse();
        if (basicResponse == null) {
            return -1;
        }
        return basicResponse.getTbsResponseData().getSingleResponseCount();
    }

    public ESingleResponse getSingleResponse(int aSorguSirasi) {
        EBasicOCSPResponse basicResponse = getBasicOCSPResponse();
        if (basicResponse == null) {
            return null;
        }
        return basicResponse.getTbsResponseData().getSingleResponse(aSorguSirasi);
    }


    public byte[] getTbsResponseData() {
        EBasicOCSPResponse basicResponse = getBasicOCSPResponse();
        if(basicResponse == null) {
            return null;
        }
        return basicResponse.getTbsResponseData().getEncoded();
    }

    public byte[] getSignatureValue() {
        EBasicOCSPResponse basicResponse = getBasicOCSPResponse();
        if(basicResponse == null) {
            return null;
        }
        return basicResponse.getObject().signature.value;
    }

    public int getResponseStatus(){
        return mObject.responseStatus.getValue();
    }

    public Asn1ObjectIdentifier getResponseType(){
        if (mObject.responseBytes!=null)
            return mObject.responseBytes.responseType;
        return null;
    }

    public byte[] getResponseBytes(){
        if (mObject.responseBytes!=null)
            return mObject.responseBytes.response.value;
        return null;
    }
}
