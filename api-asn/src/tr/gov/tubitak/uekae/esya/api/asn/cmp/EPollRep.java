package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1UTF8String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFreeText;
import tr.gov.tubitak.uekae.esya.asn.cmp.PollRepContent_element;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2013 <br>
 * <b>Date</b>: 5/24/13 - 4:39 PM <p>
 * <b>Description</b>: <br>
 */
public class EPollRep extends BaseASNWrapper<PollRepContent_element> {
    public EPollRep(PollRepContent_element aObject) {
        super(aObject);
    }

    public EPollRep(byte[] aBytes) throws ESYAException {
        super(aBytes, new PollRepContent_element());
    }

    public EPollRep(long certReqId, long checkAfter, String reason) throws ESYAException {
        super(new PollRepContent_element());
        mObject.certReqId = new Asn1Integer(certReqId);
        mObject.checkAfter = new Asn1Integer(checkAfter);
        mObject.reason = new PKIFreeText(new Asn1UTF8String[]{new Asn1UTF8String(reason)});
    }

    public EPollRep(long certReqId, long checkAfter) throws ESYAException {
        super(new PollRepContent_element());
        mObject.certReqId = new Asn1Integer(certReqId);
        mObject.checkAfter = new Asn1Integer(checkAfter);
    }

    public long getCertReqId(){
        return mObject.certReqId.value;
    }

    public long getCheckAfter(){
        return mObject.checkAfter.value;
    }

    public String getReason(){
        if(mObject.reason == null)
            return "";
        String reason = "";
        for (Asn1UTF8String element : mObject.reason.elements) {
            reason += element.value+" \n";
        }
        return reason;
    }

}
