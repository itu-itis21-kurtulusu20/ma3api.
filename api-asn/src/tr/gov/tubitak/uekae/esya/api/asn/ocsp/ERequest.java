package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.CertID;
import tr.gov.tubitak.uekae.esya.asn.ocsp.Request;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: 12/23/11 - 8:56 AM <p>
 * <b>Description</b>: <br>
 */
public class ERequest extends BaseASNWrapper<Request> {
    public ERequest(Request aObject) {
        super(aObject);
    }

    public ERequest(byte[] aBytes) throws ESYAException {
        super(aBytes, new Request());
    }

    public ERequest(CertID reqCert, EExtensions extensions) throws ESYAException {
        super(new Request(
                reqCert,
                extensions.getObject()
        ));
    }
    public ERequest(CertID reqCert) throws ESYAException {
        super(new Request( reqCert ));
    }

    public ECertID getCertID(){
        if(mObject == null)
            return null;
        return new ECertID(mObject.reqCert);
    }

    public EExtensions getExtensions(){
        if(mObject.singleRequestExtensions == null)
            return null;
        else
            return new EExtensions(wrapArray(mObject.singleRequestExtensions.elements,EExtension.class));
    }

}
