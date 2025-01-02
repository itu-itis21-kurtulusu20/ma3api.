using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

    using Logger = log4net.ILog;
    using Element = XmlElement;
    using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
    using ESYARuntimeException = tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
    using BaseSigner = tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
    using LE = tr.gov.tubitak.uekae.esya.api.common.license.LE;
    using LV = tr.gov.tubitak.uekae.esya.api.common.license.LV;
    using SignatureFormatSupport = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.SignatureFormatSupport;
    using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
    using BES = tr.gov.tubitak.uekae.esya.api.xmlsignature.formats.BES;
    using SignatureFormat = tr.gov.tubitak.uekae.esya.api.xmlsignature.formats.SignatureFormat;
    using tr.gov.tubitak.uekae.esya.api.xmlsignature.model;
    using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
    using SignaturePolicyIdentifier = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyIdentifier;
    using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
    using Resolver = tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
    using IdGenerator = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

    /// 
    /// <summary>
    /// <p>The <code>Signature</code> element is the root element of an XML
    /// Signature. Implementation MUST generate <a
    /// href="http://www.w3.org/TR/2000/WD-xmlschema-1-20000407/#cvc-elt-lax" >laxly
    /// schema valid</a> [XML-schema] <code>Signature</code> elements as specified by
    /// the following schema:</p>
    /// 
    /// <pre>
    /// &lt;complexType name="SignatureType">
    ///   &lt;complexContent>
    ///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
    ///       &lt;sequence>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}SignedInfo"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}SignatureValue"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}KeyInfo" minOccurs="0"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Object" maxOccurs="unbounded" minOccurs="0"/>
    ///       &lt;/sequence>
    ///       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
    ///     &lt;/restriction>
    ///   &lt;/complexContent>
    /// &lt;/complexType>
    /// </pre>
    /// 
    /// @author ahmety
    /// date: Jun 10, 2009
    /// </summary>
    public class XMLSignature : BaseElement
    {
        private static Logger logger = log4net.LogManager.GetLogger(typeof(XMLSignature));

        private SignedInfo mSignedInfo;
        private SignatureFormat mSignatureFormat;

        /*
        The SignatureValue element contains the actual value of the digital
        signature; it is always encoded using base64 [MIME].
         */

        private byte[] mSignatureValue;
        private string mSignatureValueId;

        private KeyInfo mKeyInfo;
        private readonly IList<XMLObject> mObjects = new List<XMLObject>();

        private SignatureType mSignatureType;

        private bool mRoot = true;

        // XAdES
        private QualifyingProperties mQualifyingProperties;
        private XMLObject mQualifyingPropertiesObject;

        /*
        static {
            PropertyConfigurator.configure("log4j.properties");
        } */

        // internal
        private Element mSignatureValueElement;

        /// <summary>
        /// Construct new Signature according to newly created context. If you will
        /// reference relative resources in xml signature, please use constructors
        /// with Context as Parameter like
        /// <code><seealso cref="XMLSignature#XMLSignature(Context)"/> </context> whose context
        /// contains base URI for relative resources.
        /// 
        /// <p> This constructor will use default signature format which is
        /// <code><seealso cref="SignatureType#XAdES_BES"/></code>.
        /// </summary>
        public XMLSignature()
            : this(new Context())
        {
        }

        /// <summary>
        /// Construct new Signature according to context </summary>
        /// <param name="aContext"> where some signature specific properties reside.
        /// 
        /// <p> This constructor will use default signature format which is
        /// <code><seealso cref="SignatureType#XAdES_BES"/></code>. </param>
        public XMLSignature(Context aContext)
            : this(aContext, true)
        {

        }

        /// <summary>
        /// Create new XMLSignature
        /// </summary>
        /// <param name="aContext"> signature related parameters </param>
        /// <param name="aRoot"> Is the signature defined root element of xml document, which
        ///      isdefined in context. Define false if you will add by hand to
        ///      anywhere in document. </param>
        public XMLSignature(Context aContext, bool aRoot)
            : base(aContext)
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.XMLIMZA);
            }
            catch (LE exc)
            {
                logger.Fatal("Lisans kontrolu basarisiz.");
                throw new Exception("Lisans kontrolu basarisiz.", exc);
            }

            mSignatureType = SignatureType.XAdES_BES;
            mSignatureFormat = SignatureFormatSupport.construct(mSignatureType, aContext, this);
            construct();
            if (aRoot)
            {
                Document.AppendChild(mElement);
            }
            IsRoot = aRoot;
        }

        internal virtual void construct()
        {
            string xmlnsDsPrefix = mContext.Config.NsPrefixMap.getPrefix(Constants.NS_XMLDSIG);
            mElement.SetAttribute("xmlns:" + String.Intern(xmlnsDsPrefix), Constants.NS_XMLDSIG);

            generateAndSetId(IdGenerator.TYPE_SIGNATURE);

            addLineBreak();
            mSignedInfo = new SignedInfo(mContext);
            mElement.AppendChild(mSignedInfo.Element);
            addLineBreak();

            mSignatureValueElement = insertElement(Constants.NS_XMLDSIG, Constants.TAG_SIGNATUREVALUE);

            mSignatureValueId = mContext.IdGenerator.uret(IdGenerator.TYPE_SIGNATURE_VALUE);

            mSignatureValueElement.SetAttribute(Constants.ATTR_ID, null, mSignatureValueId);
            mContext.IdRegistry.put(mSignatureValueId, mSignatureValueElement);

            if (mSignatureType != SignatureType.XMLDSig)
            {
                createOrGetQualifyingProperties();
            }
        }

        /// <summary>
        ///  Construct Signature from existing </summary>
        ///  <param name="aElement"> xml element </param>
        ///  <param name="aContext"> according to context </param>
        ///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
        ///      resolved appropriately </exception>
        public XMLSignature(Element aElement, Context aContext)
            : base(aElement, aContext)
        {
            // check out SignedInfo child
            Element signedInfoElem = XmlCommonUtil.getNextElement(aElement.FirstChild);

            // check to see if it is there
            if (signedInfoElem == null)
            {

                throw new XMLSignatureException("xml.WrongContent", Constants.TAG_SIGNEDINFO, Constants.TAG_SIGNATURE);
            }

            // create a SignedInfo object from that element
            mSignedInfo = new SignedInfo(signedInfoElem, aContext);

            // check out SignatureValue child
            mSignatureValueElement = XmlCommonUtil.getNextElement(signedInfoElem.NextSibling);

            // check to see if it exists
            if (mSignatureValueElement == null)
            {

                throw new XMLSignatureException("xml.WrongContent", Constants.TAG_SIGNATUREVALUE, Constants.TAG_SIGNATURE);
            }
            mSignatureValueId = mSignatureValueElement.GetAttribute(Constants.ATTR_ID);
            if (mSignatureValueId != null)
            {
                mContext.IdRegistry.put(mSignatureValueId, mSignatureValueElement);
                mContext.IdGenerator.update(mSignatureValueId);
            }


            mSignatureValue = XmlCommonUtil.getBase64DecodedText(mSignatureValueElement);

            // <element ref="ds:KeyInfo" minOccurs="0"/>
            Element keyInfoElem = XmlCommonUtil.selectFirstElement(mSignatureValueElement.NextSibling, Constants.NS_XMLDSIG, Constants.TAG_KEYINFO);

            // If it exists use it, but it's not mandatory
            if (keyInfoElem != null)
            {
                this.mKeyInfo = new KeyInfo(keyInfoElem, aContext);
            }

            Element[] objElemArr = XmlCommonUtil.selectNodes(mElement.FirstChild, Constants.NS_XMLDSIG, Constants.TAG_OBJECT);
            int qualifyingPropertiesCount = 0;
            foreach (Element objectElement in objElemArr)
            {
                XMLObject xmlObject = new XMLObject(objectElement, aContext);
                mObjects.Add(xmlObject);

                Element[] qpElmementArr = XmlCommonUtil.selectNodes(objectElement.FirstChild,
                                                              Constants.NS_XADES_1_3_2, Constants.TAGX_QUALIFYINGPROPERTIES);

                if (qpElmementArr != null && qpElmementArr.Length > 0)
                {
                    qualifyingPropertiesCount++;
                    if (qualifyingPropertiesCount > 1 || qpElmementArr.Length > 1)
                    {
                        logger.Error("Invalid amount of qualifying propertes!");
                        throw new XMLSignatureException("errors.duplicate", Constants.TAGX_QUALIFYINGPROPERTIES);
                    }
                    mQualifyingPropertiesObject = xmlObject;
                    mQualifyingProperties = new QualifyingProperties(qpElmementArr[0], mContext, this);

                }
            }
            checkSignatureFormat();
        }

        public virtual SignatureType SignatureType
        {
            get
            {
                return mSignatureType;
            }
        }

        public virtual SignedInfo SignedInfo
        {
            get
            {
                return mSignedInfo;
            }
        }

        /// <summary>
        /// The SignatureValue is the actual value of the digital signature; it is
        /// always encoded using base64 [MIME]. </summary>
        /// <returns> signature value in byte[] form! </returns>
        public virtual byte[] SignatureValue
        {
            get
            {
                return mSignatureValue;
            }
            set
            {
                XmlCommonUtil.setBase64EncodedText(mSignatureValueElement, value);
                mSignatureValue = value;
            }
        }


        public virtual Element SignatureValueElement
        {
            get
            {
                return mSignatureValueElement;
            }
        }

        public virtual string SignatureValueId
        {
            get
            {
                return mSignatureValueId;
            }
        }

        public virtual KeyInfo createOrGetKeyInfo()
        {
            if (mKeyInfo == null)
            {
                mKeyInfo = new KeyInfo(mContext);

                // keyinfoyu varsa objectten once insert et
                Element firstObject = selectChildElement(Constants.NS_XMLDSIG, Constants.TAG_OBJECT);

                if (firstObject == null)
                {
                    mElement.AppendChild(mKeyInfo.Element);
                    addLineBreak();
                }
                else
                {
                    mElement.InsertBefore(mKeyInfo.Element, firstObject);
                    mElement.InsertBefore(Document.CreateTextNode("\n"), firstObject);
                }
            }
            return mKeyInfo;
        }


        public virtual KeyInfo KeyInfo
        {
            get
            {
                return mKeyInfo;
            }
        }

        public virtual void addKeyInfo(ECertificate aCertificate)
        {
            mSignatureFormat.addKeyInfo(aCertificate);
        }

        public virtual void addKeyInfo(AsymmetricAlgorithm aPublicKey)
        {
            mSignatureFormat.addKeyInfo(aPublicKey);
        }



        public virtual int ObjectCount
        {
            get
            {
                return mObjects.Count;
            }
        }

        public virtual XMLObject getObject(int aIndex)
        {
            return mObjects[aIndex];
        }

        public virtual void addXMLObject(XMLObject aObject)
        {
            mElement.AppendChild(aObject.Element);
            addLineBreak();
            mObjects.Add(aObject);
        }


        public virtual QualifyingProperties createOrGetQualifyingProperties()
        {
            // todo
            if (mQualifyingProperties == null)
            {
                mQualifyingProperties = new QualifyingProperties(this, mContext);

                mQualifyingPropertiesObject = new XMLObject(mContext);

                string oid = mContext.IdGenerator.uret(IdGenerator.TYPE_OBJECT);
                mQualifyingPropertiesObject.Id = oid;
                //mContext.getIdRegistry().put(oid, object.getElement());

                mQualifyingPropertiesObject.addContent(mQualifyingProperties);

                SignedProperties signedProps = mQualifyingProperties.SignedProperties;

                string signedPropsURI = "#" + signedProps.Id;

                addXMLObject(mQualifyingPropertiesObject);

                try
                {
                    mSignedInfo.addReference(signedPropsURI, null, null, Constants.REFERENCE_TYPE_SIGNED_PROPS);
                }
                catch (Exception x)
                {
                    // we shouldn't drop here
                    throw new ESYARuntimeException(x);
                }
            }
            return mQualifyingProperties;
        }

        public virtual QualifyingProperties QualifyingProperties
        {
            get
            {
                return mQualifyingProperties;
            }
            set
            {
                mQualifyingProperties = value;
            }
        }

        public virtual XMLObject QualifyingPropertiesObject
        {
            get
            {
                return mQualifyingPropertiesObject;
            }
        }


        public virtual SignatureMethod SignatureMethod
        {
            set
            {
                mSignedInfo.SignatureMethod = value;
            }
            get
            {
                return mSignedInfo.SignatureMethod;
            }
        }


        public DateTime? getSigningTimePropertyTime()
        {
            SignedSignatureProperties ssp = mQualifyingProperties.SignedSignatureProperties;
            if (ssp != null && ssp.SigningTime != null)
            {
                return ssp.SigningTime;
            }
            return null;
        }

        public DateTime? getTimestampTime() 
        {
            UnsignedSignatureProperties usp = mQualifyingProperties.UnsignedSignatureProperties;
            if (usp != null && usp.SignatureTimeStampCount > 0)
            {
                SignatureTimeStamp sts = usp.getSignatureTimeStamp(0);
                return sts.getEncapsulatedTimeStamp(0).Time;
            }
            return null;
        }



        public virtual DateTime? SigningTime
        {
            set
            {
                //TODO XMLGregorianCalendar datetime aras�nda ne fark var ?
                //XMLGregorianCalendar time = XmlUtil.createDate(value);
                if (value != null)
                    createOrGetQualifyingProperties().SignedSignatureProperties.SigningTime = (DateTime)value;
            }
            get
            {

                if (mQualifyingProperties != null)
                {
                    DateTime? timestampTime = getTimestampTime();
                    if (timestampTime != null)
                        return timestampTime;

                    DateTime? signingTimePropertyTime = getSigningTimePropertyTime();
                    if(signingTimePropertyTime != null)
                        return signingTimePropertyTime;
                }
                return null;
            }
        }

        public virtual void setPolicyIdentifier(int[] aOID, string aDescription, string aPolicyURI)
        {
            setPolicyIdentifier(aOID, aDescription, aPolicyURI, null);
        }

        public virtual void setPolicyIdentifier(int[] aOID, string aDescription, string aPolicyURI, String userNotice)
        {
            SignaturePolicyIdentifier policy = new SignaturePolicyIdentifier(mContext, aOID, aDescription, aPolicyURI, userNotice);
            createOrGetQualifyingProperties().SignedSignatureProperties.SignaturePolicyIdentifier = policy;
        }

        /**
         * This method is written to support changing signature id together with target of
         * qualifying properties according to e-fatura
         * @param aId value to set as "Id" attribute. If param is null existing
         */
        public void updateId(String aId)
        {
            base.Id = aId;
            mQualifyingProperties.Target = "#" + aId;
        }

        /// <summary>
        /// Add data to XML Signature either as reference, or including data in a
        /// ds:object tag.
        /// </summary>
        /// <param name="aDocumentURI"> to be added to signature </param>
        /// <param name="aMimeType"> mime info of added document
        ///      this info will be added as DataObjectFormat </param>
        /// <param name="aEmbed"> include in signature or reference only. </param>
        /// <returns> reference id </returns>
        /// <exception cref="XMLSignatureException"> if cant resolve resource to be referenced
        ///  or embedded. </exception>
        public virtual string addDocument(string aDocumentURI, string aMimeType, bool aEmbed)
        {
            return addDocument(aDocumentURI, aMimeType, null, mContext.Config.AlgorithmsConfig.DigestMethod, null, aEmbed);
        }

        public virtual string addDocument(string aDocumentURI, string aMimeType, DigestMethod aDigestMethod, bool aEmbed)
        {
            return addDocument(aDocumentURI, aMimeType, null, aDigestMethod, null, aEmbed);
        }

        public virtual string addDocument(string aDocumentURI, string aMimeType, Transforms aTransforms, DigestMethod aDigestMethod, bool aEmbed)
        {
            return addDocument(aDocumentURI, aMimeType, aTransforms, aDigestMethod, null, aEmbed);
        }

        public virtual string addDocument(string aDocumentURI, string aMimeType, Transforms aTransforms, DigestMethod aDigestMethod, string aType, bool aEmbed)
        {
            string uri = aDocumentURI;
            if (aEmbed)
            {
                Document doc = Resolver.resolve(aDocumentURI, mContext);
                byte[] rawData = doc.Bytes;
                string objId = addObject(rawData, doc.MIMEType, null);
                uri = "#" + objId;
            }

            string referenceId = mSignedInfo.addReference(uri, aTransforms, aDigestMethod, aType);

            if (aMimeType != null && aMimeType.Length > 0)
            {
                DataObjectFormat dof = new DataObjectFormat(mContext, "#" + referenceId, null, null, aMimeType, null);
                SignedDataObjectProperties sdop = createOrGetQualifyingProperties().SignedProperties.createOrGetSignedDataObjectProperties();
                sdop.addDataObjectFormat(dof);
            }

            return referenceId;
        }

        public String addDocument(Document document)
        {
            return addDocument(document, null, null);
        }

        public String addDocument(Document document, Transforms transforms, DigestMethod digestMethod)
        {
            return addDocument(document, transforms, digestMethod, true);
        }


        public String addDocument(Document document, Transforms transforms, DigestMethod digestMethod, bool embed)
        {
            byte[] rawData = document.Bytes;
            String uri = document.URI;
            if (embed)
            {
                String objId = "";
                if (document.GetType().IsAssignableFrom(typeof(DOMDocument)))
                {
                    string rawDataStr = System.Text.Encoding.ASCII.GetString(rawData);
                    objId = addPlainObject(rawDataStr, document.MIMEType, document.Encoding);
                }
                else 
                {
                    objId = addObject(rawData, document.MIMEType, document.Encoding);
                }
                uri = "#" + objId;
            } else 
            {
                //todo document path full path ise context'e gore relative path hesaplama...
            }
            //String uri = "#"+objId;
            String referenceId =  mSignedInfo.addReference(uri, transforms, digestMethod, null);
            String mimeType = document.MIMEType;
            if (!string.IsNullOrEmpty(mimeType))
            {
                DataObjectFormat dof = new DataObjectFormat(mContext, "#"+referenceId, null, null, mimeType, null);
                SignedDataObjectProperties sdop = createOrGetQualifyingProperties().SignedProperties.createOrGetSignedDataObjectProperties();
                sdop.addDataObjectFormat(dof);
            }
            return referenceId;
        }

        public virtual string addObject(byte[] aContent, string aMimeType, string aEncoding)
        {
            XMLObject @object = new XMLObject(mContext);

            string objectId = mContext.IdGenerator.uret(IdGenerator.TYPE_OBJECT);
            @object.Id = objectId;
            //mContext.getIdRegistry().put(objectId, object.getElement());

            if (aEncoding != null)
            {
                @object.Encoding = aEncoding;
            }
            if (aMimeType != null)
            {
                @object.MIMEType = aMimeType;
            }

            @object.addContentBase64(aContent);
            addXMLObject(@object);

            return @object.Id;
        }

        public virtual string addPlainObject(string aContent, string aMimeType, string aEncoding)
        {
            XMLObject @object = new XMLObject(mContext);

            string objectId = mContext.IdGenerator.uret(IdGenerator.TYPE_OBJECT);
            @object.Id = objectId;
            //mContext.getIdRegistry().put(objectId, object.getElement());

            if (aEncoding != null)
            {
                @object.Encoding = aEncoding;
            }
            if (aMimeType != null)
            {
                @object.MIMEType = aMimeType;
            }

            @object.addContent(aContent);
            addXMLObject(@object);

            return @object.Id;
        }

        /// <returns> signing time from
        ///  a) signature timestamp if exists
        ///  b) signing time if exists
        ///  c) if above 2 methods fail, returns null </returns>
        /// <exception cref="XMLSignatureException"> if encapsulated timestamp is problematic </exception>

        /// <summary>
        /// Creates new counter signature over current signature </summary>
        /// <returns> new counter signature </returns>
        /// <exception cref="XMLSignatureException"> </exception>
        public virtual XMLSignature createCounterSignature()
        {
            return mSignatureFormat.createCounterSignature();
        }

        /// <summary>
        /// add Signature TimeStamp according to xml signature config </summary>
        /// <exception cref="XMLSignatureException">
        /// 
        /// public void addSignatureTimeStamp() throws XMLSignatureException
        /// {
        ///    Config config = mContext.getConfig();
        ///    DigestMethod dm = config.getAlgorithmsConfig().getDigestMethod();
        ///    TSSettings settings = config.getTimestampConfig().getSettings();
        ///    SignatureTimeStamp sts = new SignatureTimeStamp(mContext, this, DigestMethod.SHA_1, settings);
        /// 
        ///    UnsignedSignatureProperties usp = createOrGetQualifyingProperties().createOrGetUnsignedProperties().getUnsignedSignatureProperties();
        ///    usp.addSignatureTimeStamp(sts);
        /// 
        ///    if (mContext.getConfig().getParameters().isAddTimestampValidationData()){
        ///    	addTimestampValidationData(sts);
        ///    }
        /// 
        ///    checkSignatureFormat();
        /// } </exception>

        /// <returns> counter signatures in signature </returns>
        public virtual IList<XMLSignature> AllCounterSignatures
        {
            get
            {
                QualifyingProperties qp = QualifyingProperties;
                if ((qp == null) || (qp.UnsignedProperties == null) || (qp.UnsignedProperties.UnsignedSignatureProperties == null))
                {
                    return new List<XMLSignature>();
                    //return Collections.EMPTY_LIST;
                }
                return qp.UnsignedProperties.UnsignedSignatureProperties.AllCounterSignatures;
            }
        }

        // S I G N I N G
        public virtual void sign(BaseSigner aSigner)
        {
            checkBeforeSign();
            mSignatureFormat.sign(aSigner);
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

        public virtual void sign(IPrivateKey aKey)
        {
            checkBeforeSign();
            mSignatureFormat.sign(aKey);
        }

        public virtual void sign(byte[] aHmacSecretKey)
        {
            checkBeforeSign();
            mSignatureFormat.sign((IPrivateKey)createSecretKey(aHmacSecretKey));
        }

        private void checkBeforeSign()
        {

            try
            {
                bool isTest = LV.getInstance().isTestLicense(LV.Products.XMLIMZA);
                if (isTest)
                {

                    KeyInfo keyInfo = KeyInfo;
                    ECertificate certificate = keyInfo.resolveCertificate();

                    if (certificate == null)
                    {
                        throw new XMLSignatureException("Test license requires Signing Certificate in KeyInfo");
                    }

                    if (!certificate.getSubject().getCommonNameAttribute().ToLower().Contains("test"))
                    {
                        //TODO Buraya lisans kontrolunu geri koy
                        //throw new Exception("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
                    }
                }
            }
            catch (LE ex)
            {
                logger.Fatal("Lisans kontrolu basarisiz.");
                throw new Exception("Lisans kontrolu basarisiz.", ex);
            }


        }


        // private methods

        private IKey createSecretKey(byte[] aHmacSecretKey)
        {
            //TODO HMAC Secret key i�in i�lem yap
            //JAVA TO C# CONVERTER TODO TASK: Anonymous inner classes are not converted to C# if the base type is not defined in the code being converted:
            //			return new javax.crypto.SecretKey()
            //		{
            //			public String getAlgorithm()
            //			{
            //				return "HMAC";
            //			}
            //			public String getFormat()
            //			{
            //				return null;
            //			}
            //			public byte[] getEncoded()
            //			{
            //				return aHmacSecretKey;
            //			}
            //		};
            return null;
        }

        private void checkSignatureFormat()
        {
            SignatureType st = SignatureFormatSupport.resolve(this);
            if (mSignatureType != st)
            {
                mSignatureType = st;
                mSignatureFormat = SignatureFormatSupport.construct(mSignatureType, mContext, this);
            }
        }

        public virtual ECertificate SigningCertificate
        {
            get
            {
                try
                {
                    return ((BES)mSignatureFormat).extractCertificate();
                }
                catch (Exception x)
                {
                    Console.WriteLine(x.ToString());
                    Console.Write(x.StackTrace);
                    return null;
                }
            }
        }

        // V E R I F I C A T I O N

        /// <summary>
        /// Validates the signature according to the
        /// <a href="http://www.w3.org/TR/xmldsig-core/#sec-CoreValidation">
        /// core validation processing rules</a>.
        /// 
        /// <p>This method only validates the signature the first time it is
        /// invoked. On subsequent invocations, it returns a cached result.</p>
        /// </summary>
        /// <returns> <code>true</code> if the signature passed core validation,
        ///    otherwise <code>false</code> </returns>
        /// <exception cref="XMLSignatureException"> if an unexpected error occurs during
        ///    validation that prevented the validation operation from completing </exception>
        public virtual ValidationResult verify()
        {
            return mSignatureFormat.validateCore();
        }

        public virtual ValidationResult verify(IPublicKey aKey)
        {
            return mSignatureFormat.validateCore(aKey);
        }

        public virtual ValidationResult verify(ECertificate aCertificate)
        {
            return mSignatureFormat.validateCore(aCertificate);
        }

        public virtual ValidationResult verify(byte[] aSecretKey)
        {
            return verify((IPublicKey)createSecretKey(aSecretKey));
        }


        public virtual void upgradeToXAdES_T()
        {
            checkSignatureFormat();
            mSignatureFormat = mSignatureFormat.evolveToT();
            checkSignatureFormat();
        }
        public virtual void upgradeToXAdES_C()
        {
            checkSignatureFormat();
            mSignatureFormat = mSignatureFormat.evolveToC();
            checkSignatureFormat();
        }
        public virtual void upgradeToXAdES_X1()
        {
            checkSignatureFormat();
            mSignatureFormat = mSignatureFormat.evolveToX1();
            checkSignatureFormat();
        }

        public virtual void upgradeToXAdES_X2()
        {
            checkSignatureFormat();
            mSignatureFormat = mSignatureFormat.evolveToX2();
            checkSignatureFormat();
        }

        public virtual void upgradeToXAdES_XL()
        {
            checkSignatureFormat();
            mSignatureFormat = mSignatureFormat.evolveToXL();
            checkSignatureFormat();

            TurkishESigProfileValidator validator = new TurkishESigProfileValidator();
            ValidationResult vr = validator.validate(this, SigningCertificate);
            if (vr.getType() != ValidationResultType.VALID)
            {
                throw new XMLSignatureException(vr.getMessage());
            }
        }

        public virtual void upgradeToXAdES_A()
        {
            checkSignatureFormat();
            mSignatureFormat = mSignatureFormat.evolveToA();
            checkSignatureFormat();
        }

        public virtual void upgrade(signature.SignatureType type)
        {
            bool evolvedAlready = false;
            SignatureType current = SignatureType;
			switch (type)
			{
                case signature.SignatureType.ES_BES:
					throw new XMLSignatureRuntimeException("Cannot upgrade to BES! (Silly upgrade!)");
                case signature.SignatureType.ES_EPES:
					throw new XMLSignatureRuntimeException("Cannot upgrade to EPES! (Add PolicyID instead!)");
                case signature.SignatureType.ES_T:
                    if(new List<SignatureType>(new []{ SignatureType.XAdES_T, SignatureType.XAdES_C, SignatureType.XAdES_X, SignatureType.XAdES_X_L, SignatureType.XAdES_A}).Contains(current))
                    {
                        evolvedAlready = true;
					}
					break;
                case signature.SignatureType.ES_C:
					if (new List<SignatureType>(new []{ SignatureType.XAdES_C, SignatureType.XAdES_X, SignatureType.XAdES_X_L, SignatureType.XAdES_A}).Contains(current))
					{
						evolvedAlready = true;
					}
					break;
                case signature.SignatureType.ES_X_Type1:
                case signature.SignatureType.ES_X_Type2:
                    if (new List<SignatureType>(new []{SignatureType.XAdES_X, SignatureType.XAdES_X_L, SignatureType.XAdES_A}).Contains(current))
					{
						evolvedAlready = true;
					}
					break;              
                case signature.SignatureType.ES_XL:
                case signature.SignatureType.ES_XL_Type1:
                case signature.SignatureType.ES_XL_Type2:
					if (new List<SignatureType>(new []{SignatureType.XAdES_X_L, SignatureType.XAdES_A}).Contains(current))
					{
						evolvedAlready = true;
					}
					break;
                case signature.SignatureType.ES_A:
					if (SignatureType.XAdES_A == current)
					{
						evolvedAlready = true;
					}
					break;
			}
			if (evolvedAlready)
			{
				throw new XMLSignatureRuntimeException("error.formatCantEvolve", current, type);
			}//*/

            if (type.Equals(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_BES))
            {
                return;
            }

            if (SignatureType == SignatureType.XAdES_BES)
            {
                upgradeToXAdES_T();
            }
            if (type.Equals(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_T))
            {
                return;
            }

            if (SignatureType == SignatureType.XAdES_T)
            {
                upgradeToXAdES_C();
            }
            
			if (type.Equals(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_C))
			{
				return;
			}

            if (SignatureType == SignatureType.XAdES_C && type.Equals(signature.SignatureType.ES_X_Type2))
            {
                upgradeToXAdES_X2();
                return;
            }
            
			/*if (type.Equals(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type1))
			{
				return;
			}*/


            if (SignatureType == SignatureType.XAdES_C)
            {
                upgradeToXAdES_X1();
            }
            
			if (type.Equals(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type1))
			{
				return;
			}

            if (SignatureType == SignatureType.XAdES_X)
            {
                upgradeToXAdES_XL();
            }
            
			if (    type.Equals(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL)
                || type.Equals(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL_Type1)
                || type.Equals(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL_Type2)   )
			{
				return;
			}

            if (SignatureType == SignatureType.XAdES_X_L)
            {
                upgradeToXAdES_A();
            }
        }

        public virtual void addArchiveTimeStamp()
        {
            checkSignatureFormat();
            mSignatureFormat = mSignatureFormat.addArchiveTimeStamp();
        }

        /// <summary>
        /// Upgrade methods automatically adds previous timestamp validation data. Use this method
        /// if you manually add timestamp or if validation data for latest timestamp is required </summary>
        /// <param name="aXAdESTimeStamp"> timestamp that will be verified and validation data will be determined </param>
        /// <param name="aValidationTime"> that validation will be made </param>
        public virtual void addTimeStampValidationData(XAdESTimeStamp aXAdESTimeStamp, DateTime aValidationTime)
        {
            if (mSignatureFormat is BES)
            {
                ((BES)mSignatureFormat).addTimestampValidationData(aXAdESTimeStamp, aValidationTime);
            }
            else
            {
                throw new XMLSignatureException("Validation data can only be added to Signature in BES or more enhanced versions!");
            }
        }


        // O U T P U T

        /// <summary>
        /// Output xml signature to byte array
        /// </summary>
        /// <returns> signature as byte array </returns>
        /// <exception cref="XMLSignatureException"> if xml conversion fails </exception>
        public virtual byte[] write()
        {
            // Create an XML document instance and load the XML data.
            MemoryStream memStream = new MemoryStream();
            BinaryWriter bw = new BinaryWriter(memStream);
            string xmlStr = mElement.OuterXml;
            byte[] bytes = Encoding.UTF8.GetBytes(xmlStr);
            bw.Write(bytes);
            bw.Close();
            return memStream.ToArray();
        }


        /// <summary>
        /// Output xml signature to stream
        /// </summary>
        /// <param name="aStream"> signature to be output </param>
        /// <exception cref="XMLSignatureException"> if xml conversion fails </exception>
        public virtual void write(TextWriter textWriter)
        {
            string xmlStr = mElement.OuterXml;
            byte[] bytes = Encoding.UTF8.GetBytes(xmlStr);
            textWriter.Write(bytes);
            /*XmlDocument ownerDocument = mElement.OwnerDocument;
            ownerDocument.Save(textWriters);*/
        }

        /// <summary>
        /// Output xml signature to stream
        /// </summary>
        /// <param name="aStream"> signature to be output </param>
        /// <exception cref="XMLSignatureException"> if xml conversion fails </exception>
        public virtual void write(Stream aStream)
        {
            XmlDocument ownerDocument = mElement.OwnerDocument;
            if (IsRoot && !ownerDocument.InnerXml.Contains(XmlCommonUtil.XML_PREAMBLE_STR))
            {                
                  byte[] utf8Definition = XmlCommonUtil.XML_PREAMBLE;
                  aStream.Write(utf8Definition, 0, utf8Definition.Length);
                  aStream.Flush();               
            }
           // ownerDocument.Save(aStream);
            string outerXml = mElement.OuterXml;
            byte[] bytes = Encoding.UTF8.GetBytes(outerXml);
            aStream.Write(bytes,0,bytes.Length);
            aStream.Flush();             
        }

        // baseElement metodlari
        public override string LocalName
        {
            get
            {
                return Constants.TAG_SIGNATURE;
            }
        }

        public bool IsRoot
        {
            get { return mRoot; }
            set { mRoot = value; }
        }


        public static XMLSignature parse(XmlDocument xmlDoc, Context aContext)
        {
            XmlNameTable nsTable = xmlDoc.NameTable;
            XmlNamespaceManager nsManager = new XmlNamespaceManager(nsTable);
            nsManager.AddNamespace("ds", Constants.NS_XMLDSIG);
            nsManager.AddNamespace("xades", Constants.NS_XADES_1_3_2);
            XmlNodeList nodeList = xmlDoc.SelectNodes("//ds:Signature[1]", nsManager);
            XmlNode xmlSignatureNode = nodeList[0];
            return new XMLSignature((XmlElement)xmlSignatureNode, aContext);
        }

        // utility
        public static XMLSignature parse(Document aDocument, Context aContext)
        {
            try
            {
                XmlDocument xmlDoc = new XmlDocument();
                xmlDoc.PreserveWhitespace = true;

                MemoryStream ms = new MemoryStream(aDocument.Bytes);

                ////////bu eski baya
                ////xmlDoc.Load(ms);
                
                XmlReader reader = XmlReader.Create(ms);
                xmlDoc.Load(reader);

                // bu deneme icin yazilmisti ama calismadi, sinifi da asagida
                //using (TextReader tr = new StreamReader(ms))
                    //xmlDoc.Load(new LineCleaningTextReader(tr));

                return parse(xmlDoc, aContext);

            }
            catch (Exception x)
            {
                logger.Error("Can't parse XML signature: " + aDocument.URI, x);
                throw new XMLSignatureException(x, "errors.cantConstructSignature", aDocument.URI);
            }

        }

        public byte[] initAddingSignature(SignatureAlg signatureAlg, IAlgorithmParameterSpec algorithmParams)
        {
            DTBSRetrieverSigner dtbsRetrieverSigner = new DTBSRetrieverSigner(signatureAlg, algorithmParams);
            sign(dtbsRetrieverSigner);
            return dtbsRetrieverSigner.getDtbs();
        }

        public void finishAddingSignature(byte[] signature){
            byte[] tempSignature = DTBSRetrieverSigner.getTempSignatureBytes();

            List<XMLSignature> unFinishedSigners = new List<XMLSignature>();
            List<XMLSignature> allSigners = new List<XMLSignature>();
            allSigners.Add(this);
            allSigners.AddRange(AllCounterSignatures);

            foreach (XMLSignature xmlSignature in allSigners) {
                byte[] signatureOfSigner = xmlSignature.SignatureValue;
                if (Enumerable.SequenceEqual(signatureOfSigner, tempSignature)) {
                    unFinishedSigners.Add(xmlSignature);
                }
            }

            if (unFinishedSigners.Count == 0)
                throw new XMLSignatureException(Msg.getMsg(Msg.NO_UNFINISHED_SIGNATURE));

            //Normalde bitmemiş durumda olması gereken 1 adet imza olması lazım. Aynı anda imzalamaya başlamış kişilerden dolayı veya işlemini bitirmemiş
            //kişilerden dolayı yarım kalmış imza olabilir.
            if (unFinishedSigners.Count > 1)
                logger.Info("Unfinished Signer Count: " + unFinishedSigners.Count);

            bool valid = false;
            foreach (XMLSignature xmlSignature in unFinishedSigners)
            {
                xmlSignature.SignatureValue = signature;

                ValidationResult result = xmlSignature.validateSignatureValue();

                if (result.mResultType.Equals(ValidationResultType.VALID))
                {
                    valid = true;
                    break;
                }
                else
                {
                    xmlSignature.SignatureValue = tempSignature;
                }
            }

            if (!valid)
                throw new XMLSignatureException(Msg.getMsg(Msg.NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE));
        }

        internal ValidationResult validateSignatureValue()
        {
            ECertificate signingCertificate = SigningCertificate;
            IPublicKey publicKey;
            try
            {
                publicKey = KeyUtil.decodePublicKey(signingCertificate.getSubjectPublicKeyInfo());
            } catch (tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException ex) {
                throw new XMLSignatureException(ex, "errors.cantDecode", "PublicKey");
            }
            return mSignatureFormat.validateSignatureValue(publicKey);
        }

        /*
        private class LineCleaningTextReader : TextReader
        {
            private readonly TextReader _src;
            public LineCleaningTextReader(TextReader src)
            {
                _src = src;
            }
            public override int Read()
            {
                int r = _src.Read();
                switch (r)
                {
                    case 0xD:// \r
                        switch (_src.Peek())
                        {
                            case 0xA:
                            case 0x85: // \n or NEL char
                                _src.Read();
                                break;
                        }
                        return 0xA;
                    case 0x85://NEL
                        return 0xA;
                    default:
                        return r;
                }
            }
        }
        //*/

    }

}