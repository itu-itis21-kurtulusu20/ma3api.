package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.crypto.DTBSRetrieverSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.IDTBSRetrieverSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.SignatureFormatSupport;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml.NodeNamespaceContext;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.formats.BES;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.formats.SignatureFormat;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.XMLObject;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.DataObjectFormat;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignatureTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedDataObjectProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyIdentifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.TurkishESigProfileValidator;

import javax.crypto.SecretKey;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.*;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.ATTR_ID;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_NAMESPACESPEC;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XADES_1_3_2;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.REFERENCE_TYPE_SIGNED_PROPS;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAGX_QUALIFYINGPROPERTIES;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_KEYINFO;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_OBJECT;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_SIGNATURE;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_SIGNATUREVALUE;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAG_SIGNEDINFO;

/**
 *
 * <p>The <code>Signature</code> element is the root element of an XML
 * Signature. Implementation MUST generate <a
 * href="http://www.w3.org/TR/2000/WD-xmlschema-1-20000407/#cvc-elt-lax" >laxly
 * schema valid</a> [XML-schema] <code>Signature</code> elements as specified by
 * the following schema:</p>
 *
 * <pre>
 * &lt;complexType name="SignatureType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}SignedInfo"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}SignatureValue"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}KeyInfo" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Object" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jun 10, 2009
 */
public class XMLSignature extends BaseElement
{
    private static Logger logger = LoggerFactory.getLogger(XMLSignature.class);

    private SignedInfo mSignedInfo;
    private SignatureFormat mSignatureFormat;

    /*
    The SignatureValue element contains the actual value of the digital
    signature; it is always encoded using base64 [MIME].
     */
    private byte[] mSignatureValue;
    private String mSignatureValueId;

    private KeyInfo mKeyInfo;
    private List<XMLObject> mObjects = new ArrayList<XMLObject>();

    private SignatureType mSignatureType;

    // XAdES
    private QualifyingProperties mQualifyingProperties;
    private XMLObject mQualifyingPropertiesObject;

    private Calendar mSigningTimePropertyTime;
    private Calendar mSignatureTimestampTime;

    /*
    static {
        PropertyConfigurator.configure("log4j.properties");
    } */

    // internal
    private Element mSignatureValueElement;

    /**
     * Construct new Signature according to newly created context. If you will
     * reference relative resources in xml signature, please use constructors
     * with Context as Parameter like
     * <code>{@link XMLSignature#XMLSignature(Context)} whose context
     * contains base URI for relative resources.</code>.
     *
     * <p> This constructor will use default signature format which is
     * <code>{@link SignatureType#XAdES_BES}</code>.
     */
    public XMLSignature()
    {
        this(new Context());
    }

    /**
     * Construct new Signature according to context
     * @param aContext where some signature specific properties reside.
     *
     * <p> This constructor will use default signature format which is
     * <code>{@link SignatureType#XAdES_BES}</code>.
     */
    public XMLSignature(Context aContext)
    {
        this(aContext, true);
    }

    /**
     * Create new XMLSignature
     *
     * @param aContext signature related parameters
     * @param aRoot Is the signature defined root element of xml document, which
     *      isdefined in context. Define false if you will add by hand to
     *      anywhere in document.
     */
    public XMLSignature(Context aContext, boolean aRoot)
    {
        super(aContext);
        try
		{
			LV.getInstance().checkLD(LV.Urunler.XMLIMZA);
		}
		catch(LE e)
		{
			logger.error("Lisans kontrolu basarisiz.", e);
			throw new ESYARuntimeException("Lisans kontrolu basarisiz.", e);
		}
        mSignatureType = SignatureType.XAdES_BES;
        mSignatureFormat =  SignatureFormatSupport.construct(mSignatureType, aContext, this);
        construct();
        if (aRoot)
            getDocument().appendChild(mElement);
    }

    void construct()
    {
        String xmlnsDsPrefix = mContext.getConfig().getNsPrefixMap().getPrefix(NS_XMLDSIG);

        mElement.setAttributeNS(NS_NAMESPACESPEC, "xmlns:"+xmlnsDsPrefix.intern(), NS_XMLDSIG);

        generateAndSetId(IdGenerator.TYPE_SIGNATURE);

        addLineBreak();
        mSignedInfo = new SignedInfo(mContext);
        mElement.appendChild(mSignedInfo.getElement());
        addLineBreak();

        mSignatureValueElement = insertElement(NS_XMLDSIG, TAG_SIGNATUREVALUE);

        mSignatureValueId = mContext.getIdGenerator().uret(IdGenerator.TYPE_SIGNATURE_VALUE);

        mSignatureValueElement.setAttributeNS(null, ATTR_ID, mSignatureValueId);
        mContext.getIdRegistry().put(mSignatureValueId, mSignatureValueElement);

        if (mSignatureType!=SignatureType.XMLDSig)
            createOrGetQualifyingProperties();
    }

    /**
     *  Construct Signature from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public XMLSignature(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        // check out SignedInfo child
        Element signedInfoElem=XmlUtil.getNextElement(aElement.getFirstChild());

        // check to see if it is there
        if (signedInfoElem == null) {

           throw new XMLSignatureException("xml.WrongContent", TAG_SIGNEDINFO, TAG_SIGNATURE);
        }

        // create a SignedInfo object from that element
        mSignedInfo = new SignedInfo(signedInfoElem, aContext);

        // check out SignatureValue child
        mSignatureValueElement = XmlUtil.getNextElement(signedInfoElem.getNextSibling());

        // check to see if it exists
        if (mSignatureValueElement == null) {

           throw new XMLSignatureException("xml.WrongContent", TAG_SIGNATUREVALUE, TAG_SIGNATURE);
        }
        mSignatureValueId = mSignatureValueElement.getAttributeNS(null, ATTR_ID);
        if (mSignatureValueId!=null) {
            mContext.getIdRegistry().put(mSignatureValueId, mSignatureValueElement);
            mContext.getIdGenerator().update(mSignatureValueId);
        }


        mSignatureValue = XmlUtil.getBase64DecodedText(mSignatureValueElement);

        // <element ref="ds:KeyInfo" minOccurs="0"/>
        Element keyInfoElem = XmlUtil.selectFirstElement(mSignatureValueElement.getNextSibling(),
                                                         NS_XMLDSIG, TAG_KEYINFO);

        // If it exists use it, but it's not mandatory
        if (keyInfoElem != null) {
           this.mKeyInfo = new KeyInfo(keyInfoElem, aContext);
        }

        Element[] objElemArr = XmlUtil.selectNodes(mElement.getFirstChild(), NS_XMLDSIG, TAG_OBJECT);

        // catch existence of qualifying properties
        int qualifyingPropertiesCount = 0;
        for (Element objectElement : objElemArr) {
            XMLObject xmlObject = new XMLObject(objectElement, aContext);
            mObjects.add(xmlObject);

            Element[] qpElmementArr = XmlUtil.selectNodes(objectElement.getFirstChild(),
                                                          NS_XADES_1_3_2, TAGX_QUALIFYINGPROPERTIES);

            if (qpElmementArr != null && qpElmementArr.length > 0) {
                qualifyingPropertiesCount++;
                if (qualifyingPropertiesCount > 1 || qpElmementArr.length > 1) {
                    logger.error("Invalid amount of qualifying propertes!");
                    throw new XMLSignatureException("errors.duplicate", TAGX_QUALIFYINGPROPERTIES);
                }
                mQualifyingPropertiesObject = xmlObject;
                mQualifyingProperties = new QualifyingProperties(qpElmementArr[0], mContext, this);

            }
        }

        checkSignatureFormat();
    }

    public SignatureType getSignatureType()
    {
        return mSignatureType;
    }

    public SignedInfo getSignedInfo()
    {
        return mSignedInfo;
    }

    /**
     * The SignatureValue is the actual value of the digital signature; it is
     * always encoded using base64 [MIME].
     * @return signature value in byte[] form!
     */
    public byte[] getSignatureValue()
    {
        return mSignatureValue;
    }

    /**
     * Set computed signature value. Value will be converted to BASE64
     * This method normally used by internal signature form implementations.
     * @param aSignatureValue computed signature value over signature structure
     */
    public void setSignatureValue(byte[] aSignatureValue)
    {
        XmlUtil.setBase64EncodedText(mSignatureValueElement, aSignatureValue);
        mSignatureValue = aSignatureValue;
    }

    public Element getSignatureValueElement()
    {
        return mSignatureValueElement;
    }

    public String getSignatureValueId()
    {
        return mSignatureValueId;
    }

    public KeyInfo createOrGetKeyInfo()
    {
        if (mKeyInfo==null){
            mKeyInfo = new KeyInfo(mContext);

            // keyinfoyu varsa objectten once insert et
            Element firstObject = selectChildElement(NS_XMLDSIG, TAG_OBJECT);

            if (firstObject==null){
                mElement.appendChild(mKeyInfo.getElement());
                addLineBreak();
            }
            else {
                mElement.insertBefore(mKeyInfo.getElement(), firstObject);
                mElement.insertBefore(getDocument().createTextNode("\n"), firstObject);
            }
        }
        return mKeyInfo;
    }


    public KeyInfo getKeyInfo()
    {
        return mKeyInfo;
    }

    public void addKeyInfo(ECertificate aCertificate)
    {
        mSignatureFormat.addKeyInfo(aCertificate);
    }

    public void addKeyInfo(PublicKey aPublicKey) throws XMLSignatureException
    {
        mSignatureFormat.addKeyInfo(aPublicKey);
    }

    public int getObjectCount()
    {
        return mObjects.size();
    }

    public XMLObject getObject(int aIndex)
    {
        return mObjects.get(aIndex);
    }

    public void addXMLObject(XMLObject aObject)
    {
        mElement.appendChild(aObject.getElement());
        addLineBreak();
        mObjects.add(aObject);
    }


    public QualifyingProperties createOrGetQualifyingProperties()
    {
        // todo
        if (mQualifyingProperties==null){
            mQualifyingProperties =new QualifyingProperties(this, mContext);

            mQualifyingPropertiesObject = new XMLObject(mContext);

            String oid = mContext.getIdGenerator().uret(IdGenerator.TYPE_OBJECT);
            mQualifyingPropertiesObject.setId(oid);
            //mContext.getIdRegistry().put(oid, object.getElement());

            mQualifyingPropertiesObject.addContent(mQualifyingProperties);

            SignedProperties signedProps = mQualifyingProperties.getSignedProperties();

            String signedPropsURI = "#"+signedProps.getId();

            addXMLObject(mQualifyingPropertiesObject);

            try {
                mSignedInfo.addReference(signedPropsURI, null, null, REFERENCE_TYPE_SIGNED_PROPS);
            } catch (Exception x){
                // we shouldn't drop here
                throw new ESYARuntimeException(x);
            }
        }
        return mQualifyingProperties;
    }

    public QualifyingProperties getQualifyingProperties()
    {
        return mQualifyingProperties;
    }

    public XMLObject getQualifyingPropertiesObject()
    {
        return mQualifyingPropertiesObject;
    }

    public void setQualifyingProperties(QualifyingProperties  aQualifyingProps)
    {
        mQualifyingProperties = aQualifyingProps;
    }

    public void setSignatureMethod(SignatureMethod aMethod){
        mSignedInfo.setSignatureMethod(aMethod);
    }

    public SignatureMethod getSignatureMethod(){
        return mSignedInfo.getSignatureMethod();
    }

    public Calendar getSigningTimePropertyTime(){
        SignedSignatureProperties ssp = mQualifyingProperties.getSignedSignatureProperties();
        if (ssp!=null && ssp.getSigningTime()!=null){
            return ssp.getSigningTime().toGregorianCalendar();
        }
        return null;
    }

    public void setSigningTime(Calendar aSigningTime){
        XMLGregorianCalendar time =  XmlUtil.createDate(aSigningTime);
        createOrGetQualifyingProperties().getSignedSignatureProperties().setSigningTime(time);
    }

    /**
     * This method is written to support changing signature id together with target of
     * qualifying properties according to e-fatura
     * @param aId value to set as "Id" attribute. If param is null existing
     */
    public void updateId(String aId) {
        super.setId(aId);
        mQualifyingProperties.setTarget("#"+aId);
    }

    public void setPolicyIdentifier(int[] aOID, String aDescription, String aPolicyURI)
        throws XMLSignatureException
    {
        setPolicyIdentifier(aOID,aDescription,aPolicyURI,null);
    }

    public void setPolicyIdentifier(int[] aOID, String aDescription, String aPolicyURI, String userNotice)
            throws XMLSignatureException
    {
        SignaturePolicyIdentifier policy = new SignaturePolicyIdentifier(mContext, aOID, aDescription, aPolicyURI, userNotice);
        createOrGetQualifyingProperties().getSignedSignatureProperties().setSignaturePolicyIdentifier(policy);
    }

    /**
     * Add data to XML Signature either as reference, or including data in a
     * ds:object tag.
     *
     * @param aDocumentURI to be added to signature
     * @param aMimeType mime info of added document
     *      this info will be added as DataObjectFormat
     * @param aEmbed include in signature or reference only.
     * @return reference id
     * @throws XMLSignatureException if cant resolve resource to be referenced
     *  or embedded.
     */
    public String addDocument(String aDocumentURI, String aMimeType, boolean aEmbed)
            throws XMLSignatureException
    {
        return addDocument(aDocumentURI, aMimeType, null, mContext.getConfig().getAlgorithmsConfig().getDigestMethod(), null,  aEmbed);
    }

    public String addDocument(String aDocumentURI,
                              String aMimeType,
                              DigestMethod aDigestMethod,
                              boolean aEmbed)
            throws XMLSignatureException
    {
        return addDocument(aDocumentURI, aMimeType, null, aDigestMethod, null,  aEmbed);
    }

    public String addDocument(String aDocumentURI,
                              String aMimeType,
                              Transforms aTransforms,
                              DigestMethod aDigestMethod,
                              boolean aEmbed)
            throws XMLSignatureException
    {
        return addDocument(aDocumentURI, aMimeType, aTransforms, aDigestMethod, null,  aEmbed);
    }

    public String addDocument(String aDocumentURI,
                              String aMimeType,
                              Transforms aTransforms,
                              DigestMethod aDigestMethod,
                              String aType,
                              boolean aEmbed)
            throws XMLSignatureException
    {
        String uri = aDocumentURI;
        if (aEmbed){
            Document doc = Resolver.resolve(aDocumentURI,  mContext);
            byte[] rawData = doc.getBytes();
            String objId = addObject(rawData, doc.getMIMEType(), null);
            uri = "#"+objId;
        }

        String referenceId =  mSignedInfo.addReference(uri, aTransforms, aDigestMethod, aType);

        if (aMimeType!=null && aMimeType.length()>0){
            DataObjectFormat dof = new DataObjectFormat(mContext, "#"+referenceId, null, null, aMimeType, null);
            SignedDataObjectProperties sdop = createOrGetQualifyingProperties().getSignedProperties().createOrGetSignedDataObjectProperties();
            sdop.addDataObjectFormat(dof);
        }

        return referenceId;
    }

    public String addDocument(Document document) throws XMLSignatureException, UnsupportedEncodingException {
        return addDocument(document, null, null);
    }

    public String addDocument(Document document, Transforms transforms, DigestMethod digestMethod) throws XMLSignatureException, UnsupportedEncodingException {
        return addDocument(document, transforms, digestMethod, true);
    }

    public String addDocument(Document document, Transforms transforms, DigestMethod digestMethod, boolean embed) throws XMLSignatureException {
        byte[] rawData = document.getBytes();
        String uri = document.getURI();
        if (embed)
        {
            String objId = "";
            if (document.getClass().isAssignableFrom(DOMDocument.class))
            {
                String rawDataStr = new String(rawData);
                objId = addPlainObject(rawDataStr, document.getMIMEType(), document.getEncoding());
            }
            else
            {
                objId = addObject(rawData, document.getMIMEType(), document.getEncoding());
            }
            uri = "#"+objId;
        } else
        {
            //todo document path full path ise context'e göre relative path hesaplama...
        }
        //String uri = "#"+objId;
        String referenceId =  mSignedInfo.addReference(uri, transforms, digestMethod, null);
        String mimeType = document.getMIMEType();

        if(mimeType!=null && !mimeType.isEmpty())
        {
            DataObjectFormat dof = new DataObjectFormat(mContext, "#"+referenceId, null, null, mimeType, null);
            SignedDataObjectProperties sdop = createOrGetQualifyingProperties().getSignedProperties().createOrGetSignedDataObjectProperties();
            sdop.addDataObjectFormat(dof);
        }
        return referenceId;
    }

    public String addObject(byte[] aContent, String aMimeType, String aEncoding)
    {
        XMLObject object = new XMLObject(mContext);

        String objectId = mContext.getIdGenerator().uret(IdGenerator.TYPE_OBJECT);
        object.setId(objectId);
        //mContext.getIdRegistry().put(objectId, object.getElement());

        if (aEncoding!=null)
            object.setEncoding(aEncoding);
        if (aMimeType!=null)
            object.setMIMEType(aMimeType);

        object.addContentBase64(aContent);
        addXMLObject(object);

        return object.getId();
    }

    public String addPlainObject(String aContent, String aMimeType, String aEncoding)
    {
        XMLObject object = new XMLObject(mContext);

        String objectId = mContext.getIdGenerator().uret(IdGenerator.TYPE_OBJECT);
        object.setId(objectId);
        //mContext.getIdRegistry().put(objectId, object.getElement());

        if (aEncoding!=null)
            object.setEncoding(aEncoding);
        if (aMimeType!=null)
            object.setMIMEType(aMimeType);

        object.addContent(aContent);
        addXMLObject(object);

        return object.getId();
    }

    /**
     * @return signing time from
     *  a) signature timestamp if exists
     *  b) signing time if exists
     *  c) if above 2 methods fail, returns null
     * @throws XMLSignatureException if encapsulated timestamp is problematic
     */
    public Calendar getSigningTime() throws XMLSignatureException {
        if (mQualifyingProperties!=null){
            Calendar timestampTime = getTimestampTime();
            if (timestampTime != null){
                return timestampTime;
            }

            Calendar signingTimePropertyTime = getSigningTimePropertyTime();
            if(signingTimePropertyTime != null)
                return signingTimePropertyTime;
        }
        return null;
    }

    public Calendar getTimestampTime() throws XMLSignatureException {
        UnsignedSignatureProperties usp = mQualifyingProperties.getUnsignedSignatureProperties();
        if (usp!=null){
            if (usp.getSignatureTimeStampCount()>0){
                SignatureTimeStamp sts = usp.getSignatureTimeStamp(0);
                return sts.getEncapsulatedTimeStamp(0).getTime();
            }
        }
        return null;
    }

    /**
     * Creates new counter signature over current signature
     * @return new counter signature
     * @throws XMLSignatureException
     */
    public XMLSignature createCounterSignature() throws XMLSignatureException
    {
        return mSignatureFormat.createCounterSignature();
    }

    /**
     * add Signature TimeStamp according to xml signature config
     * @throws XMLSignatureException
     *
    public void addSignatureTimeStamp() throws XMLSignatureException
    {
        Config config = mContext.getConfig();
        DigestMethod dm = config.getAlgorithmsConfig().getDigestMethod();
        TSSettings settings = config.getTimestampConfig().getSettings();
        SignatureTimeStamp sts = new SignatureTimeStamp(mContext, this, DigestMethod.SHA_1, settings);

        UnsignedSignatureProperties usp = createOrGetQualifyingProperties().createOrGetUnsignedProperties().getUnsignedSignatureProperties();
        usp.addSignatureTimeStamp(sts);

        if (mContext.getConfig().getParameters().isAddTimestampValidationData()){
        	addTimestampValidationData(sts);
        }

        checkSignatureFormat();
    }*/

    /**
     * @return counter signatures in signature
     */
    @SuppressWarnings("unchecked")
    public List<XMLSignature> getAllCounterSignatures()
    {
        QualifyingProperties qp = getQualifyingProperties();
        if ((qp==null) || (qp.getUnsignedProperties() ==null)
            || (qp.getUnsignedProperties().getUnsignedSignatureProperties()==null))
        {
            return Collections.EMPTY_LIST;
        }
        return qp.getUnsignedProperties().getUnsignedSignatureProperties().getAllCounterSignatures();
    }

    // S I G N I N G
    public void sign(BaseSigner aSigner) throws XMLSignatureException {
        checkBeforeSign();
        mSignatureFormat.sign(aSigner);
    }

    public byte [] initAddingSignature(SignatureAlg signatureAlg, AlgorithmParameterSpec algorithmParams) throws XMLSignatureException {
        DTBSRetrieverSigner dtbsRetrieverSigner = new DTBSRetrieverSigner(signatureAlg, algorithmParams);
        sign(dtbsRetrieverSigner);
        return dtbsRetrieverSigner.getDtbs();
    }

    /*
    public void sign(ECertificate aCertificate, CardType aCardType, char[] aPassword)
            throws XMLSignatureException
    {
        mSignatureFormat.sign(aCertificate, aCardType, aPassword);
    }

    public void sign(ECertificate aCertificate) throws XMLSignatureException
    {
        mSignatureFormat.sign(aCertificate);
    } */

    public void sign(Key aKey) throws XMLSignatureException
    {
        checkBeforeSign();
        mSignatureFormat.sign(aKey);
    }

    public void sign(byte[] aHmacSecretKey) throws XMLSignatureException
    {
        checkBeforeSign();
        mSignatureFormat.sign(createSecretKey(aHmacSecretKey));
    }

    private void checkBeforeSign() throws XMLSignatureException {

        try
        {
            boolean isTest = LV.getInstance().isTL(LV.Urunler.XMLIMZA);
            if(isTest){

                KeyInfo keyInfo = getKeyInfo();
                ECertificate certificate = keyInfo.resolveCertificate();

                if (certificate==null)
                    throw new XMLSignatureException("Test license requires Signing Certificate in KeyInfo");

                if(!certificate.getSubject().getCommonNameAttribute().toLowerCase().contains("test"))
                {
                    throw new ESYARuntimeException("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
                }
            }
        }
        catch(LE e)
        {
            logger.error("Lisans kontrolu basarisiz.", e);
            throw new ESYARuntimeException("Lisans kontrolu basarisiz.", e);
        }


    }


    // private methods

    private SecretKey createSecretKey(final byte[] aHmacSecretKey){
        return new SecretKey(){
            public String getAlgorithm(){ return "HMAC";}
            public String getFormat()   { return null; }
            public byte[] getEncoded()  { return aHmacSecretKey; }
        };
    }

    private void checkSignatureFormat(){
        SignatureType st = SignatureFormatSupport.resolve(this);
        if (mSignatureType!=st){
            mSignatureType = st;
            mSignatureFormat = SignatureFormatSupport.construct(mSignatureType, mContext, this);
        }
    }

    public ECertificate getSigningCertificate() {
        try {
            return ((BES)mSignatureFormat).extractCertificate();
        }
        catch (Exception e){
            logger.error("Error in XMLSignature", e);
            return null;
        }
    }

    // V E R I F I C A T I O N

    /**
     * Validates the signature according to the
     * <a href="http://www.w3.org/TR/xmldsig-core/#sec-CoreValidation">
     * core validation processing rules</a>.
     *
     * <p>This method only validates the signature the first time it is
     * invoked. On subsequent invocations, it returns a cached result.</p>
     *
     * @return <code>true</code> if the signature passed core validation,
     *    otherwise <code>false</code>
     * @throws XMLSignatureException if an unexpected error occurs during
     *    validation that prevented the validation operation from completing
     */
    public ValidationResult verify() throws XMLSignatureException
    {
        return mSignatureFormat.validateCore();
    }

    public ValidationResult verify(Key aKey) throws XMLSignatureException
    {
        return mSignatureFormat.validateCore(aKey);
    }

    public ValidationResult verify(Certificate aCertificate) throws XMLSignatureException
    {
        return verify(aCertificate.getPublicKey());
    }

    public ValidationResult verify(ECertificate aCertificate) throws XMLSignatureException
    {
        return mSignatureFormat.validateCore(aCertificate);
    }

    public ValidationResult verify(byte[] aSecretKey) throws XMLSignatureException
    {
        return verify(createSecretKey(aSecretKey));
    }


    // E V O L V E
    public void upgradeToXAdES_T() throws XMLSignatureException {
        checkSignatureFormat();
        mSignatureFormat = mSignatureFormat.evolveToT();
        checkSignatureFormat();
    }
    public void upgradeToXAdES_C() throws XMLSignatureException {
        checkSignatureFormat();
        mSignatureFormat = mSignatureFormat.evolveToC();
        checkSignatureFormat();
    }
    public void upgradeToXAdES_X1() throws XMLSignatureException {
        checkSignatureFormat();
        mSignatureFormat = mSignatureFormat.evolveToX1();
        checkSignatureFormat();
    }
    public void upgradeToXAdES_X2() throws XMLSignatureException {
        checkSignatureFormat();
        mSignatureFormat = mSignatureFormat.evolveToX2();
        checkSignatureFormat();
    }
    public void upgradeToXAdES_XL() throws XMLSignatureException {
        checkSignatureFormat();
        mSignatureFormat = mSignatureFormat.evolveToXL();
        checkSignatureFormat();

        TurkishESigProfileValidator validator = new TurkishESigProfileValidator();
        ValidationResult vr  = validator.validate(this, getSigningCertificate());
        if (vr.getType()!=ValidationResultType.VALID){
            throw new XMLSignatureException(vr.getMessage());
        }
    }
    public void upgradeToXAdES_A() throws XMLSignatureException {
        checkSignatureFormat();
        mSignatureFormat = mSignatureFormat.evolveToA();
        checkSignatureFormat();
    }

    public void upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType type) throws XMLSignatureException {

        boolean evolvedAlready = false;
        SignatureType current = getSignatureType();
        switch (type){
            case ES_BES: throw new XMLSignatureRuntimeException("Cannot upgrade to BES! (Silly upgrade!)");
            case ES_EPES: throw new XMLSignatureRuntimeException("Cannot upgrade to EPES! (Add PolicyID instead!)");
            case ES_T:
                if (Arrays.asList(SignatureType.XAdES_T, SignatureType.XAdES_C, SignatureType.XAdES_X, SignatureType.XAdES_X_L, SignatureType.XAdES_A).contains(current))
                    evolvedAlready = true;
                break;
            case ES_C:
                if (Arrays.asList(SignatureType.XAdES_C, SignatureType.XAdES_X, SignatureType.XAdES_X_L, SignatureType.XAdES_A).contains(current))
                    evolvedAlready = true;
                break;
            case ES_X_Type1:
                if (Arrays.asList(SignatureType.XAdES_X, SignatureType.XAdES_X_L, SignatureType.XAdES_A).contains(current))
                    evolvedAlready = true;
                break;
            case ES_X_Type2:
                if (Arrays.asList(SignatureType.XAdES_X, SignatureType.XAdES_X_L, SignatureType.XAdES_A).contains(current))
                    evolvedAlready = true;
                break;
            case ES_XL:
            case ES_XL_Type1:
            case ES_XL_Type2:
                if (Arrays.asList(SignatureType.XAdES_X_L, SignatureType.XAdES_A).contains(current))
                    evolvedAlready = true;
                break;
            case ES_A:
                if (SignatureType.XAdES_A==current)
                    evolvedAlready = true;
                break;
        }
        if (evolvedAlready)
            throw new XMLSignatureRuntimeException("error.formatCantEvolve", current, type);

        if (type== tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_BES)
            return;

        if (getSignatureType()==SignatureType.XAdES_BES || getSignatureType()==SignatureType.XAdES_EPES){
            upgradeToXAdES_T();
        }
        if (type== tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_T)
            return;

        if (getSignatureType()==SignatureType.XAdES_T){
            upgradeToXAdES_C();
        }

        if (type== tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_C)
            return;

        if (getSignatureType()==SignatureType.XAdES_C && type== tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type2){
            upgradeToXAdES_X2();
        }

        if (type== tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type2)
            return;

        if (getSignatureType()==SignatureType.XAdES_C){
            upgradeToXAdES_X1();
        }

        if (type== tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type1)
            return;

        if (getSignatureType()==SignatureType.XAdES_X){
            upgradeToXAdES_XL();
        }

        if ((type== tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL)
            || (type== tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL_Type1)
            || (type== tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL_Type2))
            return;

        if (getSignatureType()==SignatureType.XAdES_X_L){
            upgradeToXAdES_A();
        }
    }

    public void addArchiveTimeStamp() throws XMLSignatureException {
        checkSignatureFormat();
        mSignatureFormat = mSignatureFormat.addArchiveTimeStamp();
    }

    /**
     * Upgrade methods automatically adds previous timestamp validation data. Use this method
     * if you manually add timestamp or if validation data for latest timestamp is required
     * @param aXAdESTimeStamp timestamp that will be verified and validation data will be determined
     * @param aValidationTime that validation will be made
     */
    public void addTimeStampValidationData(XAdESTimeStamp aXAdESTimeStamp, Calendar aValidationTime)
    	throws XMLSignatureException
    {
    	if (mSignatureFormat instanceof BES){
    		((BES) mSignatureFormat).addTimestampValidationData(aXAdESTimeStamp, aValidationTime);
    	}
    	else {
    		throw new XMLSignatureException("Validation data can only be added to Signature in BES or more enhanced versions!");
    	}
    }


    // O U T P U T

    /**
     * Output xml signature to byte array
     *
     * @return signature as byte array
     * @throws XMLSignatureException if xml conversion fails
     */
    public byte[] write() throws XMLSignatureException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        /*XmlUtil.outputDOM(mElement, C14nMethod.INCLUSIVE_WITH_COMMENTS,
                          baos, true);*/
        write(new StreamResult(baos));
        return baos.toByteArray();
    }

    /**
     * Output xml signature to stream
     *
     * @param aStream signature to be output
     * @throws XMLSignatureException if xml conversion fails
     */
    public void write(OutputStream aStream) throws XMLSignatureException
    {
        write(new StreamResult(aStream));
    }

    /**
     * Output xml signature to Result
     *
     * @param aResult signature to be transformed
     * @throws XMLSignatureException if xml conversion fails
     */
    public void write(Result aResult) throws XMLSignatureException
    {
        try {
            Source s = new DOMSource(mElement);
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(s, aResult);
        }catch (Exception x){
            throw new XMLSignatureException(x, "errors.cantOutputXml");
        }
    }


    // baseElement metodlari
    public String getLocalName()
    {
        return TAG_SIGNATURE;
    }

    // utility
    public static XMLSignature parse(Document aDocument, Context aContext)
            throws XMLSignatureException
    {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();


            org.w3c.dom.Document doc = db.parse(new ByteArrayInputStream(aDocument.getBytes()));
            aContext.setDocument(doc);
            Element nscontext = XmlUtil.createDSctx(doc, "ds", NS_XMLDSIG);

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            xpath.setNamespaceContext(new NodeNamespaceContext(nscontext));
            XPathExpression expression = xpath.compile("//ds:Signature[1]");
            Element signatureElement = (Element)expression.evaluate(doc, XPathConstants.NODE);

            return new XMLSignature(signatureElement, aContext);
        }
        catch (Exception x){
            logger.error("Can't parse XML signature: "+ aDocument.getURI(), x);
            throw new XMLSignatureException(x, "errors.cantConstructSignature", aDocument.getURI());
        }
    }

    public static XMLSignature parse(org.w3c.dom.Document w3cDocument, Context aContext)
            throws XMLSignatureException{
        try
        {
            Element nscontext = XmlUtil.createDSctx(w3cDocument, "ds", NS_XMLDSIG);

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            xpath.setNamespaceContext(new NodeNamespaceContext(nscontext));
            XPathExpression expression = xpath.compile("//ds:Signature[1]");
            Element signatureElement = (Element) expression.evaluate(w3cDocument, XPathConstants.NODE);

            return new XMLSignature(signatureElement, aContext);
        }
         catch (Exception x)
         {
                logger.error("Can't parse XML signature: ", x);
                throw new XMLSignatureException(x, "errors.cantConstructSignature");
         }
    }

    public void finishAddingSignature(byte [] signature) throws XMLSignatureException {
        byte[] tempSignature = IDTBSRetrieverSigner.getTempSignatureBytes();

        List<XMLSignature> unFinishedSigners = new ArrayList<>();
        List<XMLSignature> allSigners = new ArrayList<>();
        allSigners.add(this);
        allSigners.addAll(getAllCounterSignatures());

        for(XMLSignature xmlSignature : allSigners) {
            byte[] signatureOfSigner = xmlSignature.getSignatureValue();
            if (Arrays.equals(signatureOfSigner, tempSignature)) {
                unFinishedSigners.add(xmlSignature);
            }
        }

        if(unFinishedSigners.size() == 0)
            throw new XMLSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NO_UNFINISHED_SIGNATURE));

        //Normalde bitmemiş durumda olması gereken 1 adet imza olması lazım. Aynı anda imzalamaya başlamış kişilerden dolayı veya işlemini bitirmemiş
        //kişilerden dolayı yarım kalmış imza olabilir.
        if(unFinishedSigners.size() > 1)
            logger.info("Unfinished Signer Count: " + unFinishedSigners.size());

        boolean valid = false;
        for(XMLSignature xmlSignature : unFinishedSigners)	{
            xmlSignature.setSignatureValue(signature);

            ValidationResult result = validateSignatureValue();

            if(result.mResultType.equals(ValidationResultType.VALID)) {
                valid = true;
                break;
            }
            else {
                xmlSignature.setSignatureValue(tempSignature);
            }
        }

        if(!valid)
            throw new XMLSignatureException(CMSSignatureI18n.getMsg(E_KEYS.NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE));
    }

    protected ValidationResult validateSignatureValue() throws XMLSignatureException {
        ECertificate signingCertificate = getSigningCertificate();
        PublicKey publicKey = null;
        try {
            publicKey = KeyUtil.decodePublicKey(signingCertificate.getSubjectPublicKeyInfo());
        } catch (CryptoException ex) {
            throw new XMLSignatureException(ex, "errors.cantDecode", "PublicKey");
        }
        return mSignatureFormat.validateSignatureValue(publicKey);
    }
}
