package tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer;


import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPConnectionException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 3:00:16 PM <p>
 * <b>Description</b>: <br>
 * Connection Layer abstraction for CMP protocol, it might be used by all EE, RA, CA sides
 * @see tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.CmpHttpLayer for HTTP implementation of this protocol
 */

public interface IConnection {

    void sendPKIMessage(EPKIMessage message) throws CMPConnectionException;

    EPKIMessage sendPKIMessageAndReceiveResponse(EPKIMessage message) throws CMPConnectionException, CMPProtocolException;

    void finish();
}
