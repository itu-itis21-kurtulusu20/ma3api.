package tr.gov.tubitak.uekae.esya.cmpapi.base20.content;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAOID;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAServiceConfigType;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.ErrorMsgContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader_pvno;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAServiceConfigType;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAServiceConfigType_configType;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionController;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionGenerator;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionTrustProvider;

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
    private IProtectionTrustProvider protectionTrustProvider;
    private long cmpResponseTimeout = -1;
    private final byte[] transactionId;

    Boolean allFailOnSingleFail=false;
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
                break;
            case ONDEMANDCERTIFICATION:
                asnConfigType = ESYAServiceConfigType_configType.ekk();
                break;
            case PRECHECKCERTIFICATION:
                asnConfigType = ESYAServiceConfigType_configType.esya();
                break;
        }
        serviceConfigType = new EESYAServiceConfigType(new ESYAServiceConfigType(asnConfigType));
    }


    public AMsgBuilder(ProtocolType protocolType, EName sender, EName recipient, IProtectionTrustProvider protectionTrustProvider) {
        this.protocolType = protocolType;
        this.sender = sender;
        this.recipient = recipient;
        this.protectionTrustProvider = protectionTrustProvider;
        transactionId = new byte[16];

        //generate 128 bits pseudorandom number
        try {
            java.security.SecureRandom.getInstance("SHA1PRNG").nextBytes(transactionId);
        } catch (NoSuchAlgorithmException ex) {
            throw new ESYARuntimeException("Error while generating transactionID-senderNonce" + ex.getMessage(), ex);
        }
    }

    public AMsgBuilder(ProtocolType protocolType, EName sender, EName recipient, IProtectionTrustProvider protectionTrustProvider,boolean allFailOnSingleFail) {
        this(protocolType,sender,recipient,protectionTrustProvider);
        this.allFailOnSingleFail=allFailOnSingleFail;
    }

    /**
     * Creates PKIHeader with empty templates and fill some required areas for request/response
     * (nonce, messageTime, version, sender, recipient)
     * @return
     */
    private EPKIHeader createPkiHeaderTemplate() throws ESYAException {
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
        if(cmpResponseTimeout > 0){
            epkiHeader.addToGeneralInfo(new EInfoTypeAndValue(EESYAOID.oid_cmpResponseWaitTime,new Asn1Integer(cmpResponseTimeout)));
        }

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
    public EPKIHeader createPkiHeader() throws ESYAException {
        EPKIHeader pkiHeader = createPkiHeaderTemplate();
        pkiHeader.getObject().transactionID = new Asn1OctetString(transactionId);
        return pkiHeader;
    }

    /**
     *  Creates response header against incoming header
     * @param previousHeader
     * @return
     */
    public EPKIHeader createPkiHeader(EPKIHeader previousHeader) throws ESYAException {
        EPKIHeader pkiHeader = createPkiHeaderTemplate();
        pkiHeader.setRecipient( previousHeader.getSender());
        pkiHeader.setSender( previousHeader.getRecipient());
        pkiHeader.setTransactionID( previousHeader.getTransactionID() );
        pkiHeader.setRecipNonce( previousHeader.getSenderNonce() );
        return pkiHeader;
    }

    public void setCmpResponseTimeout(long cmpResponseTimeout) {
        this.cmpResponseTimeout = cmpResponseTimeout;
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
     * @return
     */
    public EPKIMessage createPkiConfMessage(EPKIHeader pkiHeader) {
        PKIBody body = new PKIBody();
        body.set_pkiconf();
        return createCmpMessage(pkiHeader, new EPKIBody(body));
    }

    /**
     * Creates PKIMessage with given Header-Body and adds protection with using IProtectionTrustProvider - IProtectionGenerator
     * @param pkiHeader
     * @param pkiBody
     * @return
     */
    public EPKIMessage createPKIMessage(EPKIHeader pkiHeader, EPKIBody pkiBody) {
        EPKIMessage message = new EPKIMessage(pkiHeader, pkiBody);
        addProtection(message);
        return message;
    }

    /**
     * Verifies Protection of PKIMessage with IProtectionControllers in IProtectionTrustProvider.getAcceptedProtectionContollers()
     * @param message
     * @throws CMPProtocolException if verification doesnt satisfied with any IProtectionController
     */
    public void verifyProtection(EPKIMessage message) throws CMPProtocolException {
        if (message.getHeader().getProtectionAlg() == null || message.getHeader().getProtectionAlg().getAlgorithm() == null)
            throw new CMPProtocolException(EPKIFailureInfo.badAlg, "Protection Alg is Empty!!!");
        List<IProtectionController> protectionGenerators = protectionTrustProvider.getAcceptedProtectionContollers();
        for (IProtectionController protectionController : protectionGenerators) {
            if (protectionController.verifyProtection(message)) {
                logger.info("Protection is successful for: " + protectionController);
                return;
            } else
                logger.warn("Protection operation Failed for:" + protectionController);
        }
        throw new CMPProtocolException(EPKIFailureInfo.badMessageCheck, "Protection is not satisfied for any accepted Strategy");
    }

    /**
     * Apprends protection to PKI Message
     * @param message
     */
    public void addProtection(EPKIMessage message) {
        IProtectionGenerator protectionGenerator = protectionTrustProvider.getProtectionGenerator();
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
            throw new CMPProtocolException(EPKIFailureInfo.unsupportedVersion,
                    "Protocol Cersion must be cmp2000, header.pvno:" + header.getObject().pvno.value);
        if (header.getObject().recipient == null)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "PKIMessage is not for me: Recipient Null!!!");
        //transactionID'yi alalim
        if (header.getObject().transactionID == null) {
            logger.error("TransactionID is Empty !!!.");
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "TransactionID is Empty !!!.");
        }

        if (header.getObject().senderNonce == null)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "SenderNonce is Empty!");
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
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "Error in incomingHeader: Invalid Sender:" + incomingHeader.getObject().sender);

        //recipient            CA name
        //  -- the name of the CA who was asked to produce a certificate
        if (!UtilEsitlikler.esitMi(incomingHeader.getObject().recipient, previousHeader.getObject().sender))
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "Error in incomingHeader: Invalid Recipient:" + incomingHeader.getObject().recipient);

        //transactionID        present
        //  -- value from corresponding ir and ip messages
        if (!incomingHeader.getObject().transactionID.equals(previousHeader.getObject().transactionID))
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "Error in incomingHeader: Invalid transactionID:" + incomingHeader.getObject().transactionID);


        //recipNonce           present
        //  -- value from senderNonce in corresponding ip message
        if ((incomingHeader.getObject().recipNonce == null) || (!incomingHeader.getObject().recipNonce.equals(previousHeader.getObject().senderNonce)))
            throw new CMPProtocolException(EPKIFailureInfo.badSenderNonce, "Error in incomingHeader: Invalid recipNonce:" + incomingHeader.getObject().recipNonce);

    }

    /**
     * Verify request/response PKIMessage pair (header + protection)
     * @param previousMessage
     * @param incomingMessage
     * @throws CMPProtocolException
     */
    public void verifyResponse(EPKIMessage previousMessage, EPKIMessage incomingMessage) throws CMPProtocolException {
        verifyHeader(previousMessage.getHeader(), incomingMessage.getHeader());
        verifyProtection(incomingMessage);
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
            throw new CMPProtocolException(EPKIFailureInfo.badDataFormat, "GeneralName must be _DIRECTORYNAME, GeneralName ChoiceID:" + generalName.getChoiceID());
    }

    /**
     * same as above after creating PKIMessage from header/body
     *
     * @param pkiHeader
     * @param pkiBody
     * @return
     */
    public EPKIMessage createCmpMessage(EPKIHeader pkiHeader, EPKIBody pkiBody) {
        return createPKIMessage(pkiHeader, pkiBody);
    }

    /**
     * creates PKI Conf Body
     * @param pkiBody
     * @throws CMPProtocolException
     */
    public void verifyPkiConfBody(EPKIBody pkiBody) throws CMPProtocolException {
        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(pkiBody.getObject().getChoiceID());
        if (pkiMessageType != PKIMessageType.PKICONF)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "Invalid PKI Body, must be PKICONF, it is:" + pkiBody);
    }

    /**
     * Creates PKIMessagePacket with ErrorMsg Body
     *
     * @param pkiHeader
     * @param e
     * @return
     */
    public EPKIMessage createErrorMessage(EPKIHeader pkiHeader, CMPProtocolException e) {
        PKIBody pkiBody = new PKIBody();
        pkiBody.set_error(new ErrorMsgContent(e.getExceptionInfo().getPKIStatusInfo().getObject()));
        return createCmpMessage(pkiHeader, new EPKIBody(pkiBody));
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

    public void setProtectionTrustProvider(IProtectionTrustProvider protectionTrustProvider) {
        this.protectionTrustProvider = protectionTrustProvider;
    }
}
