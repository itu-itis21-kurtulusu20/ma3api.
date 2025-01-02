package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.*;

import java.io.InputStream;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: 12/23/11 - 8:53 AM <p>
 * <b>Description</b>: <br>
 */
public class EOCSPRequest extends BaseASNWrapper<OCSPRequest>{
    public EOCSPRequest(OCSPRequest aObject) {
        super(aObject);
    }

    public EOCSPRequest(byte[] aBytes) throws ESYAException {
        super(aBytes, new OCSPRequest());
    }

    public EOCSPRequest(InputStream aStream) throws ESYAException {
        super(aStream, new OCSPRequest());
    }

    public EOCSPRequest(      Version version,
                              EGeneralName requestorName,
                              ERequest[] requests,
                              EExtensions extensions,
                              ESignature signature) throws ESYAException {
        super(new OCSPRequest(
                new TBSRequest(
                        version,
                        requestorName.getObject(),
                        new _SeqOfRequest(unwrapArray(requests)),
                        extensions.getObject()
                        ),
                signature.getObject()
        ));
    }

    public Version getVersion(){
        if(mObject.tbsRequest == null)
            return null;
        else
            return mObject.tbsRequest.version;
    }

    public EGeneralName getRequestorName(){
        if(mObject.tbsRequest == null || mObject.tbsRequest.requestorName == null)
            return null;
        else
            return new EGeneralName(mObject.tbsRequest.requestorName);
    }

    public ERequest[] getRequests(){
        if(mObject.tbsRequest == null || mObject.tbsRequest.requestList == null)
            return null;
        else
            return wrapArray(mObject.tbsRequest.requestList.elements,ERequest.class );
    }

    public EExtensions getExtensions(){
        if(mObject.tbsRequest == null || mObject.tbsRequest.requestExtensions == null)
            return null;
        else
            return new EExtensions(mObject.tbsRequest.requestExtensions);
    }

    public ESignature getSignature(){
        if(mObject.optionalSignature == null)
            return null;
        else
            return new ESignature(mObject.optionalSignature);
    }
}
