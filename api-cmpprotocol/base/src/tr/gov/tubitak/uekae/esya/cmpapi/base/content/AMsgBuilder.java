package tr.gov.tubitak.uekae.esya.cmpapi.base.content;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EInfoTypeAndValue;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAOID;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAServiceConfigType;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAServiceConfigType;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAServiceConfigType_configType;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYATekIstekHatadaTumIstekGeriAl;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionController;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionGenerator;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.Flag;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.PKIMessagePacket;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.Version;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 8, 2010 - 9:27:31 AM <p>
 * <b>Description</b>: <br>
 *     Base Message Builder Class, responsible for:
 * <ul>
 *     <li> Creates PKI Headers
 *     <li> Verifies PKI headers with previous headers(or parameters)
 *     <li> Adds Protection
 *     <li> Creates PKI Body builders (see Builder classes)
 *     <li> Creates PKIMessage and PKIMessagePackets.
 * </ul>
 *     @see CertificationBuilder
 *     @see RevocationBuilder
 *     @see PKCS10Builder
 */
public abstract class AMsgBuilder<T extends IConfigType> implements IMsgBuilder<T> {
    private static final Logger logger = LoggerFactory.getLogger(AMsgBuilder.class);
    protected ProtocolType protocolType;
    private EName sender;
    private EName recipient;
    Boolean allFailOnSingleFail=null;
    private EESYAServiceConfigType serviceConfigType;

    public void setServiceConfigType(CmpConfigType cmpConfigType) {
        ESYAServiceConfigType_configType asnConfigType=ESYAServiceConfigType_configType.undefined_();
        switch (cmpConfigType){
            case ESYA:
                asnConfigType = ESYAServiceConfigType_configType.esya();
                break;
            case TURKCELL:
                asnConfigType = ESYAServiceConfigType_configType.mobil();
                break;
            case EKK:
                asnConfigType = ESYAServiceConfigType_configType.ekk();
        }
        serviceConfigType = new EESYAServiceConfigType(new ESYAServiceConfigType(asnConfigType));

    }

    public AMsgBuilder(ProtocolType protocolType, EName sender, EName recipient) {
        this.protocolType = protocolType;
        this.sender = sender;
        this.recipient = recipient;
    }

    public AMsgBuilder(ProtocolType protocolType, EName sender, EName recipient,boolean allFailOnSingleFail) {
        this.protocolType = protocolType;
        this.sender = sender;
        this.recipient = recipient;
        this.allFailOnSingleFail = allFailOnSingleFail;
    }

    /**
     * Creates PKIHeader with empty templates and fill some required areas for request/response
     * (nonce, messageTime, version, sender, recipient)
     * @return
     */
    private EPKIHeader createPkiHeaderTemplate() {
        PKIHeader pkiHeader = new PKIHeader();
        pkiHeader.pvno = new PKIHeader_pvno(2);
        pkiHeader.sender = new GeneralName();
        pkiHeader.sender.set_directoryName(sender.getObject());
        pkiHeader.recipient = new GeneralName();
        pkiHeader.recipient.set_directoryName(recipient.getObject());

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
        EPKIHeader epkiHeader = new EPKIHeader(pkiHeader);
        if(allFailOnSingleFail!=null){
            try {
                epkiHeader.addToGeneralInfo(new EInfoTypeAndValue(EESYAOID.oid_cmpTekIstekHatadaTumIstekGeriAl, new Asn1Boolean(allFailOnSingleFail)));
            } catch (ESYAException exc) {
                throw new ESYARuntimeException("Tek istekte hata alındığında tümününü iptal edilip edilmeyeceği belirlenemedi.", exc);
            }
        }
        //service config type
        if(serviceConfigType!=null){
            try {
                epkiHeader.addToGeneralInfo(new EInfoTypeAndValue(EESYAOID.oid_cmpServiceConfigType, serviceConfigType.getObject()));
            } catch (ESYAException exc) {
                throw new ESYARuntimeException("İstek servis config tipi belirlenemedi.", exc);
            }
        }
        return epkiHeader;
    }

    /**
     * Create PKI Header with Template + TransactionID  for 1st reuqest
     * @return
     */
    public EPKIHeader createPkiHeader() {
        EPKIHeader pkiHeader = createPkiHeaderTemplate();

        byte[] transactionIDBytes = new byte[16];

        //generate 128 bits pseudorandom number
        try {
            java.security.SecureRandom.getInstance("SHA1PRNG").nextBytes(transactionIDBytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new ESYARuntimeException("Error while generating transactionID-senderNonce" + ex.getMessage(), ex);
        }
        pkiHeader.getObject().transactionID = new Asn1OctetString(transactionIDBytes);
        return pkiHeader;
    }

    /**
     *  Creates response header against incoming header
     * @param previousHeader
     * @return
     */
    public EPKIHeader createPkiHeader(EPKIHeader previousHeader) {
        EPKIHeader pkiHeader = createPkiHeaderTemplate();
        pkiHeader.setRecipient( previousHeader.getSender());
        pkiHeader.setSender( previousHeader.getRecipient());
        pkiHeader.setTransactionID( previousHeader.getTransactionID() );
        pkiHeader.setRecipNonce( previousHeader.getSenderNonce() );
        return pkiHeader;
    }

    /**
     * Certification Builder for Vertification beased protocols
     * @return
     * @throws CMPProtocolException
     */
    public CertificationBuilder createCertificationBuilder() throws CMPProtocolException {
        return new CertificationBuilder(protocolType);
    }

    /**
     * PKCS10 builder for PKCS10 based protocols...
     * @return
     * @throws CMPProtocolException
     */
    public PKCS10Builder createPKCS10Builder() throws CMPProtocolException {
        return new PKCS10Builder(protocolType);
    }

    /**
     * Creates PKIConf message, it can be used all transaction based protocols
     * @param pkiHeader
     * @param trustProvider
     * @return
     */
    public PKIMessagePacket createPkiConfMessage(EPKIHeader pkiHeader, IProtectionTrustProvider trustProvider) {
        PKIBody body = new PKIBody();
        body.set_pkiconf();
        return createCmpPacket(pkiHeader, new EPKIBody(body), trustProvider);
    }

    /**
     * Creates PKIMessage with given Header-Body and adds protection with using IProtectionTrustProvider - IProtectionGenerator
     * @param pkiHeader
     * @param pkiBody
     * @param trustProvider
     * @return
     */
    public EPKIMessage createPKIMessage(EPKIHeader pkiHeader, EPKIBody pkiBody, IProtectionTrustProvider trustProvider) {
        EPKIMessage message = new EPKIMessage(pkiHeader, pkiBody);
        addProtection(message, trustProvider);
        return message;
    }

    /**
     * Verifies Protection of PKIMessage with IProtectionControllers in IProtectionTrustProvider.getAcceptedProtectionContollers()
     * @param message
     * @param trustProvider
     * @throws CMPProtocolException if verification doesnt satisfied with any IProtectionController
     */
    public void verifyProtection(EPKIMessage message, IProtectionTrustProvider trustProvider) throws CMPProtocolException {
        if (message.getHeader().getProtectionAlg() == null || message.getHeader().getProtectionAlg().getAlgorithm() == null)
            throw new CMPProtocolException(PKIFailureInfo.badAlg, "Protection Alg is Empty!!!");
        List<IProtectionController> protectionGenerators = trustProvider.getAcceptedProtectionContollers();
        for (IProtectionController protectionController : protectionGenerators) {
            if (protectionController.verifyProtection(message)) {
                logger.info("Protection is successful for: " + protectionController);
                return;
            } else
                logger.warn("Protection operation Failed for:" + protectionController);
        }
        throw new CMPProtocolException(PKIFailureInfo.badMessageCheck, "Protection is not satisfied for any accepted Strategy");
    }

    /**
     * Apprends protection to PKI Message
     * @param message
     * @param trustProvider
     */
    public void addProtection(EPKIMessage message, IProtectionTrustProvider trustProvider) {
        IProtectionGenerator protectionGenerator = trustProvider.getProtectionGenerator();
        message.getHeader().setProtectionAlg( protectionGenerator.getProtectionAlg() ) ;
        protectionGenerator.addProtection(message);

    }

    /**
     * Verify any request or response header
     * @param header
     * @throws CMPProtocolException
     */
    public void verifyHeader(EPKIHeader header) throws CMPProtocolException {
        if (header.getObject().pvno.value != PKIHeader_pvno.cmp2000) //RFC4210'da cmp2000 olarak fixlenmistir.
            throw new CMPProtocolException(PKIFailureInfo.unsupportedVersion,
                    "Protocol Cersion must be cmp2000, header.pvno:" + header.getObject().pvno.value);
        if (header.getObject().recipient == null)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "PKIMessage is not for me: Recipient Null!!!");
        //transactionID'yi alalim
        if (header.getObject().transactionID == null) {
            logger.error("TransactionID is Empty !!!.");
            throw new CMPProtocolException(PKIFailureInfo.incorrectData, "TransactionID is Empty !!!.");
        }

        if (header.getObject().senderNonce == null)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData, "SenderNonce is Empty!");
        try {
            if (header.getObject().senderNonce.getLength() != 16) {
                logger.warn("Gelen mesajdaki senderNonce 128 bit degil. " + (header.getObject().senderNonce.getLength() * 8) + " bit");
            }
        } catch (Asn1InvalidLengthException ex) {
            logger.error("Gelen mesajdaki senderNonce 128 bit degil.", ex); // but continue....
        }
    }

    /**
     * Verify header with expected Recipient(does it come to me or not)
     * @param header
     * @param recipient
     * @throws CMPProtocolException
     */
    public void verifyHeader(EPKIHeader header, EName recipient) throws CMPProtocolException {

        verifyHeader(header);
        EName incomingName = getDirectoryName(header.getObject().recipient);
        if (! recipient.equals(incomingName) ) {

            String errorMsg = "PKIMessage is not for me !!!:";

            errorMsg += "My Name:\n" + recipient.stringValue();
            errorMsg += "\nheader.recipient:\n" + incomingName.stringValue();

            errorMsg += "\n\n *Problem might be caused by UTF-8 Differences.";
            logger.error(errorMsg);
            throw new ESYARuntimeException(errorMsg);
        }
    }

    /**
     * Verify header with according to previous header.
     * it checks sender, recipient match.
     * TransavitonID and Nonce must match etc.
     * @param previousHeader
     * @param incomingHeader
     * @throws CMPProtocolException
     */
    public void verifyHeader(EPKIHeader previousHeader, EPKIHeader incomingHeader) throws CMPProtocolException {
//        try {

        verifyHeader(incomingHeader, getDirectoryName(previousHeader.getObject().sender));

        if (!UtilEsitlikler.esitMi(incomingHeader.getObject().sender, previousHeader.getObject().recipient))
            throw new CMPProtocolException(PKIFailureInfo.incorrectData, "Error in incomingHeader: Invalid Sender:" + incomingHeader.getObject().sender);

        //recipient            CA name
        //  -- the name of the CA who was asked to produce a certificate
        if (!UtilEsitlikler.esitMi(incomingHeader.getObject().recipient, previousHeader.getObject().sender))
            throw new CMPProtocolException(PKIFailureInfo.incorrectData, "Error in incomingHeader: Invalid Recipient:" + incomingHeader.getObject().recipient);

        //transactionID        present
        //  -- value from corresponding ir and ip messages
        if (!incomingHeader.getObject().transactionID.equals(previousHeader.getObject().transactionID))
            throw new CMPProtocolException(PKIFailureInfo.incorrectData, "Error in incomingHeader: Invalid transactionID:" + incomingHeader.getObject().transactionID);


        //recipNonce           present
        //  -- value from senderNonce in corresponding ip message
        if ((incomingHeader.getObject().recipNonce == null) || (!incomingHeader.getObject().recipNonce.equals(previousHeader.getObject().senderNonce)))
            throw new CMPProtocolException(PKIFailureInfo.badSenderNonce, "Error in incomingHeader: Invalid recipNonce:" + incomingHeader.getObject().recipNonce);

    }

    /**
     * Verify request/response PKIMessage pair (header + protection)
     * @param previousMessage
     * @param incomingMessage
     * @param trustProvider
     * @throws CMPProtocolException
     */
    public void verifyResponse(EPKIMessage previousMessage, EPKIMessage incomingMessage, IProtectionTrustProvider trustProvider) throws CMPProtocolException {
        verifyHeader(previousMessage.getHeader(), incomingMessage.getHeader());
        verifyProtection(incomingMessage, trustProvider);
    }

    /**
     * extract Directory Name from GeneralName..
     * ... ops wait What is it doing in here? use new BaseAsn Classes and remove it.
     * @param generalName
     * @return
     * @throws CMPProtocolException
     */
    protected EName getDirectoryName(GeneralName generalName) throws CMPProtocolException {
        if (generalName.getChoiceID() == GeneralName._DIRECTORYNAME)
            return new EName( (Name) generalName.getElement());
        else
            throw new CMPProtocolException(PKIFailureInfo.badDataFormat, "GeneralName must be _DIRECTORYNAME, GeneralName ChoiceID:" + generalName.getChoiceID());
    }

    /**
     * Creates PKIMessagePacket for any non-final message
     * @param pkiRequest
     * @return
     */
    public PKIMessagePacket createCmpPacket(EPKIMessage pkiRequest) {

        PKIMessagePacket messagePacket = new PKIMessagePacket();
        messagePacket.setFlag(Flag.open);
        messagePacket.setPKIMessage(pkiRequest);
        messagePacket.setVersion(Version.RFC4210_Compliant);
        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(pkiRequest.getObject().body.getChoiceID());
        messagePacket.setMessageType(pkiMessageType.getMessageType());

        return messagePacket;
    }

    /**
     * same as above after creating PKIMessage from header/body
     * @param pkiHeader
     * @param pkiBody
     * @param trustProvider
     * @return
     */
    public PKIMessagePacket createCmpPacket(EPKIHeader pkiHeader, EPKIBody pkiBody, IProtectionTrustProvider trustProvider) {
        EPKIMessage pkiMessage = createPKIMessage(pkiHeader, pkiBody, trustProvider);
        return createCmpPacket(pkiMessage);
    }

    /**
     * creates PKI Conf Body
     * @param pkiBody
     * @throws CMPProtocolException
     */
    public void verifyPkiConfBody(EPKIBody pkiBody) throws CMPProtocolException {
        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(pkiBody.getObject().getChoiceID());
        if (pkiMessageType != PKIMessageType.PKICONF)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData, "Invalid PKI Body, must be PKICONF, it is:" + pkiBody);
    }

    /**
     * Creates PKIMessagePacket with ErrorMsg Body
     * @param pkiHeader
     * @param e
     * @param trustProvider
     * @return
     */
    public PKIMessagePacket createErrorMessage(EPKIHeader pkiHeader, CMPProtocolException e, IProtectionTrustProvider trustProvider) {
        PKIBody pkiBody = new PKIBody();
        pkiBody.set_error(new ErrorMsgContent(e.getExceptionInfo().getPKIStatusInfo()));
        return createCmpPacket(pkiHeader, new EPKIBody(pkiBody), trustProvider);
    }

    public boolean isErrorMsg(EPKIBody body) {
        return PKIMessageType.getPKIMessageType(body.getObject().getChoiceID()) == PKIMessageType.ERROR;
    }

    public void reportErrorMessage(ErrorMsgContent errorMsgContent) {
        logger.error("ErrorMsgContent:" + AsnIO.getFormattedAsn(errorMsgContent));
    }

    /**
     * Key Recovery PKIBody builder
     * @return
     */
    public KeyRecoveryBuilder createKeyRecoveryBuilder() {
        return new KeyRecoveryBuilder(protocolType);  
    }

    /**
     * Revocation PKIBody builder..
     * @return
     */

    public RevocationBuilder createRecovationBuilder() {
        return new RevocationBuilder(protocolType);
    }


}
