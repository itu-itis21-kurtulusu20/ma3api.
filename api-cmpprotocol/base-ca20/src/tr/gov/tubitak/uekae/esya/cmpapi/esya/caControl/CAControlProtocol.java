package tr.gov.tubitak.uekae.esya.cmpapi.esya.caControl;

import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYACAControlRepMsg;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYACAControlReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAParametreleri;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.asn.esya.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPConnectionException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.PKIMessageType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.CmpHttpLayer;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionTrustProvider;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 28.06.2013
 * Time: 12:39
 * To change this template use File | Settings | File Templates.
 */
public class CAControlProtocol {

    private static final Logger logger = LoggerFactory.getLogger(CAControlProtocol.class);
    EPKIMessage lastRequestMessage;
    EPKIMessage lastResponseMessage;

    CmpHttpLayer connection;
    EName sender;
    EName recipient;
    IProtectionTrustProvider protectionTrustProvider;
    Asn1OctetString transactionID;

    public CAControlProtocol(CmpHttpLayer connection,EName sender,EName recipient,IProtectionTrustProvider protectionTrustProvider){
        this.connection=connection;
        this.sender = sender;
        this.recipient = recipient;
        this.protectionTrustProvider = protectionTrustProvider;
        initTransactionID();
    }

    void initTransactionID(){
        byte[] transactionIDByte = new byte[16];
        //generate 128 bits pseudorandom number
        try {
            java.security.SecureRandom.getInstance("SHA1PRNG").nextBytes(transactionIDByte);
        } catch (NoSuchAlgorithmException ex) {
            throw new ESYARuntimeException("Error while generating transactionID-senderNonce" + ex.getMessage(), ex);
        }
        transactionID = new Asn1OctetString(transactionIDByte);
    }

    void
    checkCMPRespose(EPKIMessage request,EPKIMessage response) throws ESYAException, CMPProtocolException {
        EMessageVerifier messageVerifier = new EMessageVerifier(protectionTrustProvider);
        messageVerifier.verifyMessage(request, response);
        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(response.getChoiceID());
        if(pkiMessageType instanceof PKIMessageType.GenPType){
            //Gelen mesaj CAControlResp mesajı olmalı.
            EESYACAControlRepMsg caControlRepMsg = response.getBody().getCAControlRepMsg();
            ESYACAControlRepMsg caControlRepMsgObject = caControlRepMsg.getObject();
            ESYACAControlRepMsg_donuskodu donuskodu = caControlRepMsgObject.donuskodu;
            if(donuskodu == ESYACAControlRepMsg_donuskodu.failure()){
                String errorStr = caControlRepMsgObject.errorStr.value;
                throw new ESYAException("CA Returned Error. Message = "+errorStr);
            }
            return;
        }
        else if(pkiMessageType instanceof PKIMessageType.ErrorMsgType){
            throw new ESYAException("Gelen mesaj tipi hata mesajı.");
        }
        throw new ESYAException("Beklenmeyen mesaj tipi. Mesaj Tipi :"+pkiMessageType);
    }

    Map<String,byte[]> getParams(EPKIMessage message){
        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(message.getChoiceID());
        if(pkiMessageType instanceof PKIMessageType.GenPType){
            EESYACAControlRepMsg caControlRepMsg = message.getBody().getCAControlRepMsg();
            ESYAParametreleri params = caControlRepMsg.getObject().params;
            if(params==null)
                return null;
            EESYAParametreleri eParams = new EESYAParametreleri(params);
            return eParams.getAsMap();
        }
        else if(pkiMessageType instanceof PKIMessageType.GenMsgType){
            EESYACAControlReqMsg caControlReqMsg = message.getBody().getCAControlReqMsg();
            ESYAParametreleri params = caControlReqMsg.getObject().params;
            if(params==null)
                return null;
            EESYAParametreleri eParams = new EESYAParametreleri(params);
            return eParams.getAsMap();
        }
        return null;
    }

    public CAServiceStatus getServiceStatus() throws ESYAException {
        EPKIHeader epkiHeader = CAControlUtil.createHeader(sender, recipient);

        //Replay attack'tan korunmak olarak gönderiyoruz.
        epkiHeader = sendInitMessageAndGetResponse(epkiHeader);

        EPKIMessage request = CAControlUtil.createCAControlRequestMessage(epkiHeader, ESYACAControlReqMsg_islemtipi.getStatus(), null);
        try {
            EPKIMessage response = sendRequestAndGetResponse(request);
            checkCMPRespose(request,response);
            EESYACAControlRepMsg caControlRepMsg = response.getBody().getCAControlRepMsg();
            ESYACAControlRepMsg caControlRepMsgObject = caControlRepMsg.getObject();
            ESYACAStatus status = caControlRepMsgObject.status;
            if(status == null)
                throw new ESYAException("Gelen cevaptaki status null");


            CAServiceStatus serviceStatus = new CAServiceStatus();

            ESYACAStatus_initializationStatus initializationStatus = status.initializationStatus;
            if(initializationStatus == ESYACAStatus_initializationStatus.initialized()){
                serviceStatus.setInitialized(true);
            }
            else
                serviceStatus.setInitialized(false);


            ESYACAStatus_certificationServiceStatus certServiceStatus = status.certificationServiceStatus;
            if(certServiceStatus ==ESYACAStatus_certificationServiceStatus.notstarted())
                serviceStatus.setCertServiceState(CACertServiceState.NOTSTARTED);
            else if(certServiceStatus ==ESYACAStatus_certificationServiceStatus.started())
                serviceStatus.setCertServiceState(CACertServiceState.STARTED);
            else if(certServiceStatus ==ESYACAStatus_certificationServiceStatus.stopped())
                serviceStatus.setCertServiceState(CACertServiceState.STOPPED);

            ESYACAStatus_crlServiceStatus crlServiceStatus = status.crlServiceStatus;
            if(crlServiceStatus ==ESYACAStatus_crlServiceStatus.notstarted())
                serviceStatus.setCacrlServiceState(CACRLServiceState.NOTSTARTED);
            else if(crlServiceStatus ==ESYACAStatus_crlServiceStatus.started())
                serviceStatus.setCacrlServiceState(CACRLServiceState.STARTED);
            else if(crlServiceStatus ==ESYACAStatus_crlServiceStatus.stopped())
                serviceStatus.setCacrlServiceState(CACRLServiceState.STOPPED);
            return serviceStatus;
        }
        catch (CMPConnectionException e) {
            throw new ESYAException("Bağlantı hatası oluştu",e);
        } catch (CMPProtocolException e) {
            throw new ESYAException("CMP protokol hatası oluştu",e);
        }
    }

    private EPKIHeader sendInitMessageAndGetResponse(EPKIHeader epkiHeader) throws ESYAException {
        EPKIMessage response = sendFullInitMessageAndGetResponse(epkiHeader);
        epkiHeader = CAControlUtil.createHeader(response.getHeader());
        return epkiHeader;
    }

    private EPKIMessage sendFullInitMessageAndGetResponse(EPKIHeader epkiHeader) throws ESYAException {
        EPKIMessage request=null;
        EPKIMessage response=null;
        //İletişimi başlatmak için ilk olarak nonce oluşturmak için nonce isteğine benzer bir mesaj oluşturuyoruz.
        request = CAControlUtil.createCAControlRequestMessage(epkiHeader, ESYACAControlReqMsg_islemtipi.initCA(), null);
        try {
            response = sendRequestAndGetResponse(request);
            checkCMPRespose(request,response);
        } catch (CMPConnectionException e) {
            throw new ESYAException("Bağlantı hatası oluştu",e);
        } catch (CMPProtocolException e) {
            throw new ESYAException("CMP protokol hatası oluştu",e);
        }
        return response;
    }

    public void initializeCA(String hsmPin) throws ESYAException {
        EPKIMessage request=null;
        EPKIMessage response=null;
        EPKIHeader epkiHeader = CAControlUtil.createHeader(sender, recipient);
        //Replay attack'tan korunmak olarak gönderiyoruz.
        response = sendFullInitMessageAndGetResponse(epkiHeader);

        Map<String, byte[]> params = getParams(response);
        byte[] encCertValue = params.get(CAControlUtil.CA_CONTROL_PARAM_NAME_INITIALIZED_CA_ENC_CERT);
        ECertificate encCACert = new ECertificate(encCertValue);
        PublicKey publicKey = KeyUtil.decodePublicKey(encCACert.getSubjectPublicKeyInfo());
        byte[] hsmPinByte = StringUtil.toByteArray(hsmPin);
        byte[] encryptedPin = CipherUtil.encrypt(CipherAlg.RSA_ECB_PKCS1, null, hsmPinByte, publicKey);
        epkiHeader = CAControlUtil.createHeader(response.getHeader());
        List<Pair<String,byte[]>> paramList = new ArrayList<Pair<String, byte[]>>();
        paramList.add(new Pair<String, byte[]>(CAControlUtil.CA_CONTROL_PARAM_NAME_ENCRYPTED_PIN,encryptedPin));
        request = CAControlUtil.createCAControlRequestMessage(epkiHeader, ESYACAControlReqMsg_islemtipi.initCA(),paramList);
        try {
            response = sendRequestAndGetResponse(request);
            checkCMPRespose(request,response);
        } catch (CMPConnectionException e) {
            throw new ESYAException("Bağlantı hatası oluştu",e);
        } catch (CMPProtocolException e) {
            throw new ESYAException("CMP protokol hatası oluştu",e);
        }
    }

    public void initializeCA(byte[] systemEncryptedHsmPin) throws ESYAException {
        EPKIMessage request=null;
        EPKIMessage response=null;
        EPKIHeader epkiHeader = CAControlUtil.createHeader(sender, recipient);
        //Replay attack'tan korunmak olarak gönderiyoruz.
        response = sendFullInitMessageAndGetResponse(epkiHeader);

        Map<String, byte[]> params = getParams(response);
        byte[] encCertValue = params.get(CAControlUtil.CA_CONTROL_PARAM_NAME_INITIALIZED_CA_ENC_CERT);
        ECertificate encCACert = new ECertificate(encCertValue);
        epkiHeader = CAControlUtil.createHeader(response.getHeader());
        List<Pair<String,byte[]>> paramList = new ArrayList<Pair<String, byte[]>>();
        paramList.add(new Pair<String, byte[]>(CAControlUtil.CA_CONTROL_PARAM_NAME_SYSTEM_ENCRYPTED_PIN,systemEncryptedHsmPin));
        request = CAControlUtil.createCAControlRequestMessage(epkiHeader, ESYACAControlReqMsg_islemtipi.initCA(),paramList);
        try {
            response = sendRequestAndGetResponse(request);
            checkCMPRespose(request,response);
        } catch (CMPConnectionException e) {
            throw new ESYAException("Bağlantı hatası oluştu",e);
        } catch (CMPProtocolException e) {
            throw new ESYAException("CMP protokol hatası oluştu",e);
        }
    }

    public void startCertificationService() throws ESYAException {
        EPKIMessage request=null;
        EPKIMessage response=null;
        EPKIHeader epkiHeader = CAControlUtil.createHeader(sender, recipient);
        //Replay attack'tan korunmak olarak gönderiyoruz.
        epkiHeader = sendInitMessageAndGetResponse(epkiHeader);
        request = CAControlUtil.createCAControlRequestMessage(epkiHeader, ESYACAControlReqMsg_islemtipi.startCA(),null);
        try {
            response = sendRequestAndGetResponse(request);
            checkCMPRespose(request,response);
        } catch (CMPConnectionException e) {
            throw new ESYAException("Bağlantı hatası oluştu",e);
        } catch (CMPProtocolException e) {
            throw new ESYAException("CMP protokol hatası oluştu",e);
        }
    }

    public void startCRLService() throws ESYAException {
        EPKIMessage request=null;
        EPKIMessage response=null;
        EPKIHeader epkiHeader = CAControlUtil.createHeader(sender, recipient);
        //Replay attack'tan korunmak olarak gönderiyoruz.
        epkiHeader = sendInitMessageAndGetResponse(epkiHeader);

        ArrayList<Pair<String, byte[]>> sendParamList = new ArrayList<Pair<String, byte[]>>();
        request = CAControlUtil.createCAControlRequestMessage(epkiHeader, ESYACAControlReqMsg_islemtipi.startCRLService(),null);
        try {
            response = sendRequestAndGetResponse(request);
            checkCMPRespose(request,response);
        } catch (CMPConnectionException e) {
            throw new ESYAException("Bağlantı hatası oluştu",e);
        } catch (CMPProtocolException e) {
            throw new ESYAException("CMP protokol hatası oluştu", e);
        }
    }

    public void stopCRLService() throws ESYAException {
        EPKIMessage request=null;
        EPKIMessage response=null;
        EPKIHeader epkiHeader = CAControlUtil.createHeader(sender, recipient);
        //Replay attack'tan korunmak olarak gönderiyoruz.
        epkiHeader = sendInitMessageAndGetResponse(epkiHeader);
        //Yetkilendirme başlangıç mesajı gönderiliyor.
        ArrayList<Pair<String, byte[]>> sendParamList = new ArrayList<Pair<String, byte[]>>();
        request = CAControlUtil.createCAControlRequestMessage(epkiHeader, ESYACAControlReqMsg_islemtipi.stopCRLService(),null);
        try {
            response = sendRequestAndGetResponse(request);
            checkCMPRespose(request,response);
        } catch (CMPConnectionException e) {
            throw new ESYAException("Bağlantı hatası oluştu",e);
        } catch (CMPProtocolException e) {
            throw new ESYAException("CMP protokol hatası oluştu",e);
        }
    }

    public void stopCertificationService() throws ESYAException {
        EPKIMessage request=null;
        EPKIMessage response=null;
        EPKIHeader epkiHeader = CAControlUtil.createHeader(sender, recipient);
        //Replay attack'tan korunmak olarak gönderiyoruz.
        epkiHeader = sendInitMessageAndGetResponse(epkiHeader);
        //Yetkilendirme başlangıç mesajı gönderiliyor.
        ArrayList<Pair<String, byte[]>> sendParamList = new ArrayList<Pair<String, byte[]>>();
        request = CAControlUtil.createCAControlRequestMessage(epkiHeader, ESYACAControlReqMsg_islemtipi.stopCA(),null);
        try {
            response = sendRequestAndGetResponse(request);
            checkCMPRespose(request,response);
        } catch (CMPConnectionException e) {
            throw new ESYAException("Bağlantı hatası oluştu",e);
        } catch (CMPProtocolException e) {
            throw new ESYAException("CMP protokol hatası oluştu",e);
        }
    }

    void addProtection(EPKIMessage epkiMessage){
        epkiMessage.getHeader().setProtectionAlg(protectionTrustProvider.getProtectionGenerator().getProtectionAlg());
        protectionTrustProvider.getProtectionGenerator().addProtection(epkiMessage);
    }

    EPKIMessage sendRequestAndGetResponse(EPKIMessage request) throws CMPConnectionException, CMPProtocolException {
        EPKIHeader requestHeader = request.getHeader();
        requestHeader.setTransactionID(transactionID);
        if(lastResponseMessage!=null){
            EPKIHeader lastResponseMessageHeader = lastResponseMessage.getHeader();
            requestHeader.setTransactionID(lastResponseMessageHeader.getTransactionID());
            requestHeader.setRecipNonce(lastResponseMessageHeader.getSenderNonce());
        }

        addProtection(request);
        lastRequestMessage=request;
        lastResponseMessage =  connection.sendPKIMessageAndReceiveResponse(request);
        return lastResponseMessage;
    }
}

