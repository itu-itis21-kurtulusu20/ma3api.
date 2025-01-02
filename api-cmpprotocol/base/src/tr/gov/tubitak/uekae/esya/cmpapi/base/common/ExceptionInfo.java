package tr.gov.tubitak.uekae.esya.cmpapi.base.common;

import com.objsys.asn1j.runtime.Asn1UTF8String;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFreeText;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatus;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatusInfo;

import java.util.BitSet;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 2, 2010 - 9:16:10 AM <p>
 * <b>Description</b>: <br>
 *     Exception info to cary information and build PKIStatusInfo
 *     @see PKIStatusInfo
 */

public class ExceptionInfo {    
    private int failInfoID ;
    private String errorInfo;
    private int errorID;

    public ExceptionInfo(int failInfoID, String errorInfo) {
        this.failInfoID = failInfoID;
        this.errorInfo = errorInfo;
        errorID = (int) (Math.random()*Integer.MAX_VALUE);
    }
    
    public PKIStatusInfo getPKIStatusInfo(){
        PKIStatus status = new PKIStatus(PKIStatus.rejection);
        PKIFreeText freeText = new PKIFreeText(new Asn1UTF8String[]
                {new Asn1UTF8String(errorInfo),new Asn1UTF8String(" errorID:"+errorID)});
        BitSet bs = new BitSet();
        bs.set(failInfoID);
        PKIFailureInfo failureInfo = new PKIFailureInfo(bs);
        return new PKIStatusInfo(status, freeText, failureInfo);
    }

    public int getFailInfoID() {
        return failInfoID;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    @Override
    public String toString() {
        return "ExceptionInfo{" +
                "failInfoID=" + failInfoID +
                ", errorInfo='" + errorInfo + '\'' +
                ", errorID=" + errorID +
                '}';
    }
}
