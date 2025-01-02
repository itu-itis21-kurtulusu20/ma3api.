package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.crypto.IDTBSRetrieverSigner;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_NAMESPACESPEC;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG;

/**
 * Enveloped Document support with multiple signatures serial and/or parallel.
 *
 * @author ayetgin
 * date May 6, 2009
 */
public class SignedDocument {

    private static Logger logger = LoggerFactory.getLogger(SignedDocument.class);

    private org.w3c.dom.Document mDocument;
    private Context mContext;

    private Element mRootElement, mSignaturesElement, mDocumentsElement;

    private List<XMLSignature> mSignatures = new ArrayList<XMLSignature>();

    public SignedDocument(Document aDocument, Context aContext)
        throws XMLSignatureException
    {
        mContext = aContext;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            mDocument = db.parse(new ByteArrayInputStream(aDocument.getBytes()));
            construct(mDocument, aContext);
        }
        catch (Exception e){
            logger.error("Can't parse XML signature: "+ aDocument.getURI(), e);
            throw new XMLSignatureException(e, "errors.cantConstructSignature", aDocument.getURI());
        }

    }
    public SignedDocument(org.w3c.dom.Document aDocument, Context aContext)
        throws XMLSignatureException
    {
        construct(aDocument, aContext);
    }

    public void construct(org.w3c.dom.Document aDocument, Context aContext)
        throws XMLSignatureException
    {
        mContext = aContext;
        mDocument = aDocument;
        mContext.setDocument(mDocument);
    	try
		{
			LV.getInstance().checkLD(LV.Urunler.XMLIMZA);
		}
		catch(LE e)
		{
			logger.error("Lisans kontrolu basarisiz.", e);
			throw new ESYARuntimeException("Lisans kontrolu basarisiz.", e);
		}
        try {
            Element nscontext = XmlUtil.createDSctx(mDocument, "ds", NS_XMLDSIG);

            //XmlUtil.addNSAttribute(nscontext, NS_MA3_PREFIX, NS_MA3);

            mRootElement = mDocument.getDocumentElement();
            mDocumentsElement = XmlUtil.selectFirstElementNoNS(mRootElement.getFirstChild(), "data");
            mSignaturesElement = XmlUtil.selectFirstElementNoNS(mRootElement.getFirstChild(), "signatures");

            //XPathFactory factory = XPathFactory.newInstance();
            //XPath xpath = factory.newXPath();
            //xpath.setNamespaceContext(new NodeNamespaceContext(nscontext));
            //XPathExpression expression = xpath.compile("/envelope/signatures/ds:Signature");

            //NodeList signaturesList = (NodeList)expression.evaluate(mDocument, XPathConstants.NODESET);
            Element[] signatureElements = XmlUtil.selectNodes(mSignaturesElement.getFirstChild(), Constants.NS_XMLDSIG, "Signature");
            //NodeList signaturesList = mSignaturesElement.getElementsByTagNameNS("*", "Signature");

            for (Element element : signatureElements){
                XMLSignature s = new XMLSignature(element, mContext);
                mSignatures.add(s);
            }
        }
        catch (Exception e){
          logger.error("Can't parse XML signature ", e);
          throw new XMLSignatureException(e, "errors.cantConstructSignature", aDocument);
        }

    }


    public SignedDocument(Context aContext)
    {
        mContext = aContext;
        mDocument = aContext.getDocument();

        mRootElement = mDocument.createElementNS(null, "envelope");
        mDocument.appendChild(mRootElement);

        //mRootElement.setAttributeNS(NS_NAMESPACESPEC, "ma3", NS_MA3);
        mRootElement.setAttributeNS(NS_NAMESPACESPEC, "xmlns:ds", NS_XMLDSIG);

        mSignaturesElement = mDocument.createElementNS(null, "signatures");
        mDocumentsElement = mDocument.createElementNS(null, "data");
        XmlUtil.addLineBreak(mSignaturesElement);
        XmlUtil.addLineBreak(mDocumentsElement);

        XmlUtil.addLineBreak(mRootElement);
        mRootElement.appendChild(mDocumentsElement);
        XmlUtil.addLineBreak(mRootElement);
        mRootElement.appendChild(mSignaturesElement);
        XmlUtil.addLineBreak(mRootElement);
    }

    /**
     * @param aDocument to be added
     * @return identifier to be used by references
     * @throws XMLSignatureException if problem occurs while
     *  dereferencing/accessing the document
     */
    public String addDocument(Document aDocument)
        throws XMLSignatureException
    {

        Element dataElement = mDocument.createElementNS(null, "data-item");

        String id = mContext.getIdGenerator().uret(IdGenerator.TYPE_DATA);
        dataElement.setAttributeNS(null, "Id", id);
        mContext.getIdRegistry().put(id, dataElement);

        dataElement.appendChild(
                        dataElement.getOwnerDocument().createTextNode(
                                            new String(aDocument.getBytes())));
        mDocumentsElement.appendChild(dataElement);
        XmlUtil.addLineBreak(mDocumentsElement);

        return id;
    }

    public String addXMLDocument(Document aDocument)
        throws XMLSignatureException
    {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document xmldoc =
                      db.parse(new ByteArrayInputStream(aDocument.getBytes()));
            Element element = xmldoc.getDocumentElement();

            return addXMLNode(element);
        } catch (Exception e){
            throw new XMLSignatureException(e, "errors.cantAddDocument", aDocument.getURI());
        }
    }

    public String addXMLNode(Node aNode){
        Element dataElement = mDocument.createElementNS(null, "data-item");

        String id = mContext.getIdGenerator().uret(IdGenerator.TYPE_DATA);
        dataElement.setAttributeNS(null, "Id", id);
        mContext.getIdRegistry().put(id, dataElement);

        Node importing = mDocument.importNode(aNode, true);
        dataElement.appendChild(importing);

        mDocumentsElement.appendChild(dataElement);
        XmlUtil.addLineBreak(mDocumentsElement);

        return id;
    }

    public Context getContext()
    {
        return mContext;
    }

    public XMLSignature createSignature(){
        XMLSignature s = new XMLSignature(mContext, false);
        mSignaturesElement.appendChild(s.getElement());
        XmlUtil.addLineBreak(mSignaturesElement);

        mSignatures.add(s);
        return s;
    }

    public XMLSignature addSignature(XMLSignature signature){

        XMLSignature cloned;
        try {
            Node importing = mDocument.importNode(signature.getElement(), true);
            cloned = new XMLSignature((Element)importing, mContext);
            mSignatures.add(cloned);
            mSignaturesElement.appendChild(cloned.getElement());
            XmlUtil.addLineBreak(mSignaturesElement);
        } catch (Exception e){
            throw new XMLSignatureRuntimeException("Error clonining signature when attaching to SignedDoc.", e);
        }
        return cloned;
    }

    public void removeSignature(XMLSignature signature){
        mSignatures.remove(signature);
        mSignaturesElement.removeChild(signature.getElement());
    }

    public int getRootSignatureCount(){
        return mSignatures.size();
    }

    public XMLSignature getSignature(int aIndex){
        return mSignatures.get(aIndex);
    }

    public List<XMLSignature> getRootSignatures(){
        return new ArrayList<XMLSignature>(mSignatures);
    }

    public ValidationResult verify() throws XMLSignatureException {
        ValidationResult vr = new ValidationResult(getClass());
        for (XMLSignature signature : mSignatures) {
            ValidationResult verified = signature.verify();
            vr.addItem(verified);
            if (verified.getType()!= ValidationResultType.VALID){
                vr.setStatus(verified.getType(), I18n.translate("validation.check.signedDoc"),
                             verified.getMessage(), null);
                return verified;
            }
        }
        vr.setStatus(ValidationResultType.VALID, I18n.translate("validation.check.signedDoc"),
                     I18n.translate("core.signedDocumentVerified"), null);
        return vr;
    }

    // output methods
    public byte[] write() throws XMLSignatureException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        write(new StreamResult(baos));
        return baos.toByteArray();
    }

    public void write(OutputStream aStream) throws XMLSignatureException
    {
        write(new StreamResult(aStream));
    }

    public void write(Result aResult) throws XMLSignatureException
    {
        try {
            Source s = new DOMSource(mDocument.getDocumentElement());
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(s, aResult);
        } catch (Exception e){
            logger.error("Error in SignedDocument", e);
            throw new XMLSignatureException(e, "errors.cantOutputXml");
        }
    }

    public void finishAddingSignature(byte [] signature) throws XMLSignatureException {
        byte[] tempSignature = IDTBSRetrieverSigner.getTempSignatureBytes();

        List<XMLSignature> unFinishedSignatures = new ArrayList<>();
        List<XMLSignature> allSignatures = new ArrayList<>();

        for (XMLSignature xmlSignature : getRootSignatures()){
            allSignatures.add(xmlSignature);
            allSignatures.addAll(xmlSignature.getAllCounterSignatures());
        }

        for(XMLSignature xmlSignature : allSignatures) {
            byte[] signatureOfSigner = xmlSignature.getSignatureValue();
            if (Arrays.equals(signatureOfSigner, tempSignature)) {
                unFinishedSignatures.add(xmlSignature);
            }
        }

        if(unFinishedSignatures.size() == 0)
            throw new XMLSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NO_UNFINISHED_SIGNATURE));

        //Normalde bitmemiş durumda olması gereken 1 adet imza olması lazım. Aynı anda imzalamaya başlamış kişilerden dolayı veya işlemini bitirmemiş
        //kişilerden dolayı yarım kalmış imza olabilir.
        if(unFinishedSignatures.size() > 1)
            logger.info("Unfinished Signer Count: " + unFinishedSignatures.size());

        boolean valid = false;
        for(XMLSignature xmlSignature : unFinishedSignatures)	{
            xmlSignature.setSignatureValue(signature);

            ValidationResult result = xmlSignature.validateSignatureValue();
            if(result.mResultType.equals(ValidationResultType.VALID)) {
                valid = true;
                break;
            } else {
                xmlSignature.setSignatureValue(tempSignature);
            }
        }

        if(!valid)
            throw new XMLSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE));
    }
}
