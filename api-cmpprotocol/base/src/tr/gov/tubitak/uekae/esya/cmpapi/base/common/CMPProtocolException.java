package tr.gov.tubitak.uekae.esya.cmpapi.base.common;

import tr.gov.tubitak.uekae.esya.asn.cmp.*;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 2, 2010 - 9:09:10 AM <p>
 * <b>Description</b>: <br>
 *     Exceptions related with Protocol Layer. so it tracks ExceptionInfo
 *     @see ExceptionInfo
 */

public class CMPProtocolException extends Exception {
    private ExceptionInfo exceptionInfo;

/*    public CMPProtocolException(String message) {
        super(message);
    }

    public CMPProtocolException(String message, Throwable cause) {
        super(message,cause);
    }*/

    public CMPProtocolException(int failInfoID, String errorInfo) {
        super(errorInfo);
        this.exceptionInfo = new ExceptionInfo(failInfoID, errorInfo);
    }
    
    public CMPProtocolException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo.getErrorInfo());
        this.exceptionInfo = exceptionInfo;
    }

    public CMPProtocolException(Throwable cause, ExceptionInfo exceptionInfo) {
        super(exceptionInfo.getErrorInfo(),cause);
        this.exceptionInfo = exceptionInfo;
    }

    public CMPProtocolException(String message, ExceptionInfo exceptionInfo) {
        super(message);
        this.exceptionInfo = exceptionInfo;
    }

    public CMPProtocolException(String message, Throwable cause, ExceptionInfo exceptionInfo) {
        super(message,cause);
        this.exceptionInfo = exceptionInfo;
    }

    public ExceptionInfo getExceptionInfo() {
        if( exceptionInfo == null )
            exceptionInfo = new ExceptionInfo(PKIFailureInfo.addInfoNotAvailable,"" );

        return exceptionInfo;
    }

    @Override
    public String getMessage() {
        return super.getMessage()+" "+ getExceptionInfo();  
    }
}
