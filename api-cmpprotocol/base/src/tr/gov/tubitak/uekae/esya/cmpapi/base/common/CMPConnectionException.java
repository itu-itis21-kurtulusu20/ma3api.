package tr.gov.tubitak.uekae.esya.cmpapi.base.common;


/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 2, 2010 - 11:22:25 AM <p>
 * <b>Description</b>: <br>
 *     CMPConnectionException exception for CMP Connection Layer
 */

public class CMPConnectionException extends RuntimeException {
    public CMPConnectionException(String message) {
        super(message);
    }

    public CMPConnectionException(String message, Exception ex) {
        super(message,ex);
    }
}
