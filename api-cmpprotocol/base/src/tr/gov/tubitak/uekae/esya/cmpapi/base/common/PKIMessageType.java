package tr.gov.tubitak.uekae.esya.cmpapi.base.common;

import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.MessageType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 7, 2010 - 9:55:24 AM <p>
 * <b>Description</b>: <br>
 * PKIMessageType for PKIBody in RFC 4210. These subclasses and iterfaces make abstraction
 * for different Messaye Types.
 * <pre>
    PKIBody ::= CHOICE {
        ir [0] CertReqMessages, --Initialization Req
        ip [1] CertRepMessage, --Initialization Resp
        cr [2] CertReqMessages, --Certification Req
        cp [3] CertRepMessage, --Certification Resp
        p10cr [4] CertificationRequest, --PKCS #10 Cert. Req.
        popdecc [5] POPODecKeyChallContent --pop Challenge
        popdecr [6] POPODecKeyRespContent, --pop Response
        kur [7] CertReqMessages, --Key Update Request
        kup [8] CertRepMessage, --Key Update Response
        krr [9] CertReqMessages, --Key Recovery Req
        krp [10] KeyRecRepContent, --Key Recovery Resp
        rr [11] RevReqContent, --Revocation Request
        rp [12] RevRepContent, --Revocation Response
        ccr [13] CertReqMessages, --Cross-Cert. Request
        ccp [14] CertRepMessage, --Cross-Cert. Resp
        ckuann [15] CAKeyUpdAnnContent, --CA Key Update Ann.
        cann [16] CertAnnContent, --Certificate Ann.
        rann [17] RevAnnContent, --Revocation Ann.
        crlann [18] CRLAnnContent, --CRL Announcement
        pkiconf [19] PKIConfirmContent, --Confirmation
        nested [20] NestedMessageContent, --Nested Message
        genm [21] GenMsgContent, --General Message
        genp [22] GenRepContent, --General Response
        error [23] ErrorMsgContent, --Error Message
        certConf [24] CertConfirmContent, --Certificate confirm
        pollReq [25] PollReqContent, --Polling request
        pollRep [26] PollRepContent --Polling response
    }
 * </pre>
 *
 * See static Fields
 */

public abstract class PKIMessageType implements IPKIMessageType {

    protected static Logger logger = LoggerFactory.getLogger(PKIMessageType.class);

    private byte choice;
    private String choiceName;
    private MessageType messageType;

    PKIMessageType(byte choice, MessageType messageType) {
        this.choice = choice;
        this.messageType = messageType;
        this.choiceName = new PKIBody(choice, null).getElemName();
    }

    public byte getChoice() {
        return choice;
    }

    public String getChoiceName() {
        return choiceName;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return "PKIMessageType{" +
                "choice=" + choice +
                ", choiceName='" + choiceName + '\'' +
                ", messageType=" + messageType +
                '}';
    }


    public interface IRequestType extends IPKIMessageType {

    }


    public interface IResponseType extends IPKIMessageType {

    }


    public interface ICertificationType extends IPKIMessageType {

    }


    public static class CertReqType extends PKIMessageType implements IRequestType, ICertificationType {
        CertReqType(byte choice) {
            super(choice, MessageType.pkiReq);
        }
    }


    public static class CertResType extends PKIMessageType implements IResponseType, ICertificationType {
        CertResType(byte choice) {
            super(choice, MessageType.pkiRep);
        }
    }


    public static class CertConfType extends PKIMessageType implements IRequestType {
        CertConfType(byte choice) {
            super(choice, MessageType.pkiReq);
        }
    }


    public static class RevReqType extends PKIMessageType implements IRequestType {
        RevReqType(byte choice) {
            super(choice, MessageType.pkiReq);
        }
    }


    public static class KeyRecReqType extends PKIMessageType implements IRequestType, ICertificationType {
        KeyRecReqType(byte choice) {
            super(choice, MessageType.pkiReq);
        }
    }


    public static class RevResType extends PKIMessageType implements IResponseType {
        RevResType(byte choice) {
            super(choice, MessageType.pkiRep);
        }
    }


    public static class KeyRecResType extends PKIMessageType implements IResponseType {
        KeyRecResType(byte choice) {
            super(choice, MessageType.pkiRep);
        }
    }


    public static class PkiConfType extends PKIMessageType implements IResponseType, IRequestType {
        PkiConfType(byte choice) {
            super(choice, MessageType.pkiRep);
        }
    }


    public static class ErrorMsgType extends PKIMessageType implements IResponseType, IRequestType {
        ErrorMsgType(byte choice) {
            super(choice, MessageType.pkiRep);
        }
    }


    public final static CertReqType IR = new CertReqType(PKIBody._IR);
    public final static CertReqType CR = new CertReqType(PKIBody._CR);
    public final static CertReqType KUR = new CertReqType(PKIBody._KUR);
    public final static CertReqType P10CR = new CertReqType(PKIBody._P10CR);
    public final static CertReqType CVCREQ = new CertReqType(PKIBody._CVCREQ);
    public final static RevReqType RR = new RevReqType(PKIBody._RR);
    public final static KeyRecReqType KRR = new KeyRecReqType(PKIBody._KRR);

    public final static CertConfType CERTCONF = new CertConfType(PKIBody._CERTCONF);

    public final static CertResType IP = new CertResType(PKIBody._IP);
    public final static CertResType CP = new CertResType(PKIBody._CP);
    public final static CertResType KUP = new CertResType(PKIBody._KUP);
    public final static RevResType RP = new RevResType(PKIBody._RP);
    public final static KeyRecResType KRP = new KeyRecResType(PKIBody._KRP);
    public final static PkiConfType PKICONF = new PkiConfType(PKIBody._PKICONF);

    public final static ErrorMsgType ERROR = new ErrorMsgType(PKIBody._ERROR);

    /**
     * creates PKIMessageType from choiceID in PKIBody
     * @param choiceID
     * @return
     */
    public static PKIMessageType getPKIMessageType(int choiceID) {
        Field[] fields = PKIMessageType.class.getFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) &&
                    IPKIMessageType.class.isAssignableFrom(field.getDeclaringClass())) {
                PKIMessageType messageType = null;
                try {
                    messageType = (PKIMessageType) field.get(null);
                } catch (IllegalAccessException e) {
                    logger.error("Error in PKIMessageType", e);
                }
                if (messageType.getChoice() == choiceID)
                    return messageType;
            }
        }
        return null;
    }

/*    public final static ResponseType IP = new ResponseType(PKIBody._IP);
public final static ResponseType CP = new ResponseType(PKIBody._CP);
public final static ResponseType RP = new ResponseType(PKIBody._RP);

public final static RequestType IR = new RequestType(PKIBody._IR);
public final static RequestType CR = new RequestType(PKIBody._CR);
public final static RequestType RR = new RequestType(PKIBody._RR);*/


/*    private byte choice;

private MessageType messageType;

PKIMessageType(byte choice, MessageType messageType) {
    this.choice = choice;
    this.messageType = messageType;
}

public byte getChoice() {
    return choice;
}

public MessageType getMessageType() {
    return messageType;
}*/

    /*
    public static final byte _IR = 1;
    public static final byte _IP = 2;
    public static final byte _CR = 3;
    public static final byte _CP = 4;
    public static final byte _P10CR = 5;
    public static final byte _POPDECC = 6;
    public static final byte _POPDECR = 7;
    public static final byte _KUR = 8;
    public static final byte _KUP = 9;
    public static final byte _KRR = 10;
    public static final byte _KRP = 11;
    public static final byte _RR = 12;
    public static final byte _RP = 13;
    public static final byte _CCR = 14;
    public static final byte _CCP = 15;
    public static final byte _CKUANN = 16;
    public static final byte _CANN = 17;
    public static final byte _RANN = 18;
    public static final byte _CRLANN = 19;
    public static final byte _PKICONF = 20;
    public static final byte _NESTED = 21;
    public static final byte _GENM = 22;
    public static final byte _GENP = 23;
    public static final byte _ERROR = 24;
    public static final byte _CERTCONF = 25;
    public static final byte _POLLREQ = 26;
    public static final byte _POLLREP = 27;
    public static final byte _BCR = 28;*/

/*    *//**
     * Created by IntelliJ IDEA.
     * User: zeldal.ozdemir
     * Date: Nov 1, 2010
     * Time: 2:19:08 PM
     * To change this template use File | Settings | File Templates.
     *//*
    public static class RequestType extends PKIMessageType {
        public RequestType(byte choice) {
            super(choice, MessageType.pkiReq);
        }
    }

    *//**
     * Created by IntelliJ IDEA.
     * User: zeldal.ozdemir
     * Date: Nov 1, 2010
     * Time: 2:22:41 PM
     * To change this template use File | Settings | File Templates.
     *//*
    public static class ResponseType extends PKIMessageType {
        ResponseType(byte choice) {
            super(choice, MessageType.pkiRep);
        }
    }*/


/*    public static class AbstractPKIMessageType implements IPKIMessageType {
  private byte choice;

  private MessageType messageType;

  AbstractPKIMessageType(byte choice, MessageType messageType) {
      this.choice = choice;
      this.messageType = messageType;
  }

  public byte getChoice() {
      return choice;
  }

  public MessageType getMessageType() {
      return messageType;
  }
}  */


}
