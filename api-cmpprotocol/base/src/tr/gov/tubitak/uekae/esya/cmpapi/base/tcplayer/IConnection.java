package tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer;


/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 3:00:16 PM <p>
 * <b>Description</b>: <br>
 * Connection Layer abstraction for CMP protocol, it might be used by all EE, RA, CA sides
 * @see CmpTcpLayer for TCP implementation of this protocol
 */

public interface IConnection {
    void connect() ;

    void sendErrorMessage(PKIMessageTCPException e) ;

    void sendMessage(PKIMessagePacket pkiMessagePacket) ;

    PKIMessagePacket readPKIMessagePacket() throws PKIMessageTCPException;

    void finish();

    void close();

    void setTimeOutInSec(int timeOutInSec);
}
