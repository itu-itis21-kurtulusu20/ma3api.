package tr.gov.tubitak.uekae.esya.cmpapi.esya.caControl;

import com.objsys.asn1j.runtime.Asn1InvalidLengthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader_pvno;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionController;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionTrustProvider;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 04.07.2013
 * Time: 08:34
 * To change this template use File | Settings | File Templates.
 */
public class EMessageVerifier {
    IProtectionTrustProvider protectionTrustProvider;
    public EMessageVerifier(IProtectionTrustProvider protectionTrustProvider) {
        this.protectionTrustProvider = protectionTrustProvider;
    }

    private static final Logger logger = LoggerFactory.getLogger(EMessageVerifier.class);
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
     * extract Directory Name from GeneralName..
     * ... ops wait What is it doing in here? use new BaseAsn Classes and remove it.
     * @param generalName
     * @return
     * @throws tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException
     */
    protected EName getDirectoryName(GeneralName generalName) throws CMPProtocolException {
        if (generalName.getChoiceID() == GeneralName._DIRECTORYNAME)
            return new EName( (Name) generalName.getElement());
        else
            throw new CMPProtocolException(EPKIFailureInfo.badDataFormat, "GeneralName must be _DIRECTORYNAME, GeneralName ChoiceID:" + generalName.getChoiceID());
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

    public void verifyMessage(EPKIMessage previousMessage, EPKIMessage incomingMessage) throws CMPProtocolException {
        verifyHeader(previousMessage.getHeader(),incomingMessage.getHeader());
        verifyProtection(incomingMessage);
    }
}
