package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PollReqContent_element;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2013 <br>
 * <b>Date</b>: 5/24/13 - 4:41 PM <p>
 * <b>Description</b>: <br>
 */
public class EPollReq extends BaseASNWrapper<PollReqContent_element> {
    public EPollReq(PollReqContent_element aObject) {
        super(aObject);
    }

    public EPollReq(byte[] aBytes) throws ESYAException {
        super(aBytes, new PollReqContent_element());
    }

    public EPollReq(long certId) throws ESYAException {
        super(new PollReqContent_element(certId));
    }

    public long getCertId(){
        if(mObject == null)
            throw new ESYARuntimeException("Null Asn Object in PollReqContent_element");
        return mObject.certReqId.value;
    }
}
