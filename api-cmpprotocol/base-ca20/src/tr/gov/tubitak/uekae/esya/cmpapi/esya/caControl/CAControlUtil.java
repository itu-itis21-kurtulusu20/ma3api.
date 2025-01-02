package tr.gov.tubitak.uekae.esya.cmpapi.esya.caControl;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1UTF8String;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYACAControlRepMsg;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYACAControlReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAParametre;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAParametreleri;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader_pvno;
import tr.gov.tubitak.uekae.esya.asn.esya.*;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 27.06.2013
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class CAControlUtil {

    public static final String CA_CONTROL_PARAM_NAME_DB_USER_NAME = "dbUserName";
    public static final String CA_CONTROL_PARAM_NAME_DB_USER_PASSWORD = "dbUserPassword";
    public static final String CA_CONTROL_PARAM_NAME_REGISTRAR_CERT_SERIAL = "registrarCertSerial";



    public static final String CA_CONTROL_PARAM_NAME_CA_ID = "caId";
    public static final String CA_CONTROL_PARAM_NAME_AUTHORITY_NAME= "authorityName";
    public static final String CA_CONTROL_PARAM_NAME_INITIALIZED_CA_ENC_CERT= "initializedCAEncCert";
    public static final String CA_CONTROL_PARAM_NAME_ENCRYPTED_PIN= "encryptedPin";
    public static final String CA_CONTROL_PARAM_NAME_SYSTEM_ENCRYPTED_PIN= "systemEncryptedPin";



    public static final String CA_CONTROL_PARAM_NAME_DB_SIM_SIGN_KEY = "dbSymSignKey";
    public static final String CA_CONTROL_PARAM_NAME_KEY_ENC_KEY = "keyEncKey";
    public static final String CA_CONTROL_PARAM_NAME_HSM_PWD = "hsmPwd";


    public static final String CA_CONTROL_PARAM_NAME_HSM_SELECT_TITLE_MSG = "hsmSelecteTitleMsg";
    public static final String CA_CONTROL_PARAM_NAME_HSM_SLOT_LIST = "hsmSlotList";
    public static final String HSM_SLOT_INFO_DELIMITER = ":";
    public static final String HSM_SLOT_LIST_DELIMITER = ",";


    private static EPKIHeader createPkiHeaderCAControlRequest(EPKIHeader responseHeader,EName sender,EName receiver) throws ESYAException {
        if(responseHeader!=null){
            return createHeader(responseHeader);
        }
        else
        return createHeader(sender, receiver);
    }

    public static EPKIHeader createHeader(EName sender, EName receiver) throws ESYAException {
        PKIHeader pkiHeader = new PKIHeader();
        pkiHeader.pvno = new PKIHeader_pvno(2);
        pkiHeader.sender =new GeneralName();
        pkiHeader.recipient = new GeneralName();

        pkiHeader.sender.set_directoryName(sender.getObject());
        pkiHeader.recipient.set_directoryName(receiver.getObject());
        //mesaj zamani
        pkiHeader.messageTime = new Asn1GeneralizedTime();
        try {
            pkiHeader.messageTime.setTime(Calendar.getInstance());
        } catch (Asn1Exception aEx) {
            throw new ESYARuntimeException("Message Time Couldnt Created", aEx);
        }

        byte[] senderNonceBytes = new byte[16];
        //generate 128 bits pseudorandom number
        try {
            java.security.SecureRandom.getInstance("SHA1PRNG").nextBytes(senderNonceBytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new ESYARuntimeException("Error while generating transactionID-senderNonce" + ex.getMessage(), ex);
        }
        pkiHeader.senderNonce = new Asn1OctetString(senderNonceBytes);

        byte[] transactionID = new byte[16];
        //generate 128 bits pseudorandom number
        try {
            java.security.SecureRandom.getInstance("SHA1PRNG").nextBytes(transactionID);
        } catch (NoSuchAlgorithmException ex) {
            throw new ESYARuntimeException("Error while generating transactionID-senderNonce" + ex.getMessage(), ex);
        }
        pkiHeader.transactionID = new Asn1OctetString(transactionID);

        EPKIHeader epkiHeader = new EPKIHeader(pkiHeader);
        return epkiHeader;
    }

    public static EPKIMessage createCAControlRequestMessage(EPKIHeader header,ESYACAControlReqMsg_islemtipi islemtipi,List<Pair<String,byte[]>> paramList) throws ESYAException {
        PKIBody pkiBody = new PKIBody();
        EPKIBody epkiBody = new EPKIBody(pkiBody);

        ESYACAControlReqMsg esyacaControlReqMsg = new  ESYACAControlReqMsg();
        esyacaControlReqMsg.islemtipi= islemtipi;

        if(paramList!=null){
            EESYAParametre[] eesyaParametreler=new EESYAParametre[paramList.size()];
            for (int k=0;k<paramList.size();k++) {
                Pair<String, byte[]> paramPair = paramList.get(k);
                eesyaParametreler[k]=new EESYAParametre(paramPair.first(),paramPair.second());
            }
            EESYAParametreleri eParametreler = new EESYAParametreleri(eesyaParametreler);
            esyacaControlReqMsg.params=eParametreler.getObject();
        }
        EESYACAControlReqMsg eesyacaControlReqMsg = new EESYACAControlReqMsg(esyacaControlReqMsg);
        epkiBody.setCAControlReqMessage(eesyacaControlReqMsg);
        EPKIMessage epkiMessage = new EPKIMessage(header,epkiBody);
        return epkiMessage;
    }

    public static EPKIHeader createHeader(EPKIHeader responseHeader) throws ESYAException {
        PKIHeader pkiHeader = new PKIHeader();
        pkiHeader.pvno = new PKIHeader_pvno(2);
        pkiHeader.sender =responseHeader.getRecipient().getObject();
        pkiHeader.recipient = responseHeader.getSender().getObject();

        //mesaj zamani
        pkiHeader.messageTime = new Asn1GeneralizedTime();
        try {
            pkiHeader.messageTime.setTime(Calendar.getInstance());
        } catch (Asn1Exception aEx) {
            throw new ESYARuntimeException("Message Time Couldnt Created", aEx);
        }

        byte[] senderNonceBytes = new byte[16];
        //generate 128 bits pseudorandom number
        try {
            java.security.SecureRandom.getInstance("SHA1PRNG").nextBytes(senderNonceBytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new ESYARuntimeException("Error while generating transactionID-senderNonce" + ex.getMessage(), ex);
        }
        pkiHeader.senderNonce = new Asn1OctetString(senderNonceBytes);
        pkiHeader.recipNonce=responseHeader.getSenderNonce();
        EPKIHeader epkiHeader = new EPKIHeader(pkiHeader);
        return epkiHeader;
    }


    private static EPKIHeader createPkiHeaderCAControlResponse(EPKIHeader responseHeader,EName sender,EName receiver) throws ESYAException {
        if(responseHeader!=null){
            return createHeader(responseHeader);
        }
        else
            return createHeader(sender, receiver);
    }

    public static EPKIMessage createCAControlResponseMessage(EPKIHeader header,ESYACAControlRepMsg_donuskodu donuskodu) throws ESYAException {
        PKIBody pkiBody = new PKIBody();
        EPKIBody epkiBody = new EPKIBody(pkiBody);

        ESYACAControlRepMsg esyacaControlRepMsg = new  ESYACAControlRepMsg();
        esyacaControlRepMsg.donuskodu= donuskodu;
        esyacaControlRepMsg.errorStr=new Asn1UTF8String("");

        EESYACAControlRepMsg eesyacaControlRepMsg = new EESYACAControlRepMsg(esyacaControlRepMsg);
        epkiBody.setCAControlRepMessage(eesyacaControlRepMsg);
        EPKIMessage epkiMessage = new EPKIMessage(header,epkiBody);
        return epkiMessage;
    }

    public static EPKIMessage createCAControlResponseMessage(EPKIHeader header,ESYACAControlRepMsg esyacaControlRepMsg) throws ESYAException {
        PKIBody pkiBody = new PKIBody();
        EPKIBody epkiBody = new EPKIBody(pkiBody);
        EESYACAControlRepMsg eesyacaControlRepMsg = new EESYACAControlRepMsg(esyacaControlRepMsg);
        epkiBody.setCAControlRepMessage(eesyacaControlRepMsg);
        EPKIMessage epkiMessage = new EPKIMessage(header,epkiBody);
        return epkiMessage;
    }
}

