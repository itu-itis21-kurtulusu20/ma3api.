package tr.gov.tubitak.uekae.esya.cmpapi.base.common;

import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.MessageType;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 7, 2010 - 9:55:24 AM <p>
 * <b>Description</b>: <br>
 *     Interface for PKI Message Types (PKIBody - content types. )
 *     @see PKIMessageType
*/

interface IPKIMessageType {
    public byte getChoice();

    public MessageType getMessageType();

    String getChoiceName();
}
