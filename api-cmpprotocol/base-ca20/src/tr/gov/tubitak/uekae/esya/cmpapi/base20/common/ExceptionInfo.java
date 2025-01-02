package tr.gov.tubitak.uekae.esya.cmpapi.base20.common;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIStatus;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIStatusInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatus;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatusInfo;

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
    private EPKIFailureInfo pkiFailInfo;
    private String errorInfo;
    private int errorID;

    public ExceptionInfo(EPKIFailureInfo pkiFailInfo, String errorInfo) {
        this.pkiFailInfo = pkiFailInfo;
        this.errorInfo = errorInfo;
        errorID = (int) (Math.random()*Integer.MAX_VALUE);
    }

    public ExceptionInfo(PKIFailureInfo pkiFailInfo, String errorInfo) {
        this.pkiFailInfo = new EPKIFailureInfo(pkiFailInfo);
        this.errorInfo = errorInfo;
        errorID = (int) (Math.random()*Integer.MAX_VALUE);
    }


    
    public EPKIStatusInfo getPKIStatusInfo(){
        EPKIStatus status = new EPKIStatus(PKIStatus.rejection);
        return new EPKIStatusInfo(status, " errorID:"+errorID, pkiFailInfo);
    }

    public EPKIFailureInfo getPkiFailInfo() {
        return pkiFailInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    @Override
    public String toString() {
        return "ExceptionInfo{" +
                "failInfoID=" + pkiFailInfo +
                ", errorInfo='" + errorInfo + '\'' +
                ", errorID=" + errorID +
                '}';
    }
}
