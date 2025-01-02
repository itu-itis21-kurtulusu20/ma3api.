using System;
using System.IO;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.ms;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;
using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;


namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using C14nMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
	using SignatureMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureMethod;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    /// <summary>
    /// <p>The structure of <code>SignedInfo</code> includes the canonicalization
    /// algorithm, a signature algorithm, and one or more references. The
    /// <code>SignedInfo</code> element may contain an optional ID attribute that
    /// will allow it to be referenced by other signatures and objects.</p>
    /// <p/>
    /// <p><code>SignedInfo</code> does not include explicit signature or digest
    /// properties (such as calculation time, cryptographic device serial number,
    /// etc.). If an application needs to associate properties with the signature or
    /// digest, it may include such information in a <code>SignatureProperties</code>
    /// element within an <code>Object</code> element.</p>
    /// <p/>
    /// <p>The following schema fragment specifies the expected content contained
    /// within this class.
    /// <p/>
    /// <pre>
    /// &lt;complexType name="SignedInfoType">
    ///   &lt;complexContent>
    ///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
    ///       &lt;sequence>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}CanonicalizationMethod"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}SignatureMethod"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Reference" maxOccurs="unbounded"/>
    ///       &lt;/sequence>
    ///       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
    ///     &lt;/restriction>
    ///   &lt;/complexContent>
    /// &lt;/complexType>
    /// </pre>
    /// @author ahmety
    /// date: Jun 10, 2009
    /// </summary>
    public class SignedInfo : Manifest
	{

		/*
		CanonicalizationMethod is a required element that specifies the
		canonicalization algorithm applied to the SignedInfo element prior to
		performing signature calculations.    
		 */
		private C14nMethod mCanonicalizationMethod;

		/*
		SignatureMethod is a required element that specifies the algorithm used for
		signature generation and validation. This algorithm identifies all
		cryptographic functions involved in the signature operation (e.g. hashing,
		public key algorithms, MACs, padding, etc.).    
		 */
		private SignatureMethod mSignatureMethod;

		// for internal
		private readonly Element mC14nElement, mSignMethodElement;
		private readonly int? mHMACOutputLength;


		public SignedInfo(Context aBaglam) : base(aBaglam)
		{

			mC14nElement = insertElement(Constants.NS_XMLDSIG, Constants.TAG_C14NMETHOD);
			mSignMethodElement = insertElement(Constants.NS_XMLDSIG, Constants.TAG_SIGNATUREMETHOD);

			CanonicalizationMethod = Constants.DEFAULT_REFERENCE_C14N;
		    SignatureAlg sa = Context.Config.AlgorithmsConfig.getSignatureAlg();
		    SignatureMethod sm = SignatureMethod.fromAlgorithmName(sa.getName());
		    SignatureMethod = sm;// Constants.DEFAULT_SIGNATURE_METHOD;
		}

		/// <summary>
		/// Construct SignedInfo from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///                               resolved appropriately </exception>
		public SignedInfo(Element aElement, Context aContext) : base(aElement, aContext)
		{

			mC14nElement = XmlCommonUtil.getNextElement(aElement.FirstChild);
			string c14nAlg = getAttribute(mC14nElement, Constants.ATTR_ALGORITHM);
			mCanonicalizationMethod = C14nMethod.resolve(c14nAlg);

			mSignMethodElement = XmlCommonUtil.getNextElement(mC14nElement.NextSibling);
			string signMethodAlg = getAttribute(mSignMethodElement, Constants.ATTR_ALGORITHM);
			mSignatureMethod = SignatureMethod.resolve(signMethodAlg);

			if (mSignatureMethod.MAlgorithm.getName().ToLower().StartsWith("hmac"))
			{
				Element hMACOutputLengthElm = XmlCommonUtil.getNextElement(mSignMethodElement.FirstChild);
				if (hMACOutputLengthElm != null)
				{
					mHMACOutputLength = Convert.ToInt32(XmlCommonUtil.getText(hMACOutputLengthElm));
				}
			}
		}

		public virtual IAlgorithmParams SignatureAlgorithmParameters
		{
			get
			{
			    IAlgorithmParams algorithmParams = null;
			    if (mSignatureMethod.MAlgorithm.getName().ToLower().StartsWith("hmac") && mHMACOutputLength != null)
			    {
			        algorithmParams = new ParamsWithLength(mHMACOutputLength.Value);
			    }
			    else if (mSignatureMethod.MSignatureAlg.getName().Equals("RSAPSS"))
			    {
			        algorithmParams = mSignatureMethod.getAlgorithmParams();
			    }

			    return algorithmParams;
            }
		}

		public virtual C14nMethod CanonicalizationMethod
		{
			get
			{
				return mCanonicalizationMethod;
			}
			set
			{
                mC14nElement.RemoveAttribute(Constants.ATTR_ALGORITHM);
			    mC14nElement.SetAttribute(Constants.ATTR_ALGORITHM,null,value.URL);
				mCanonicalizationMethod = value;
			}
		}


		public virtual SignatureMethod SignatureMethod
		{
			get
			{
				return mSignatureMethod;
			}
			set
			{
                mSignMethodElement.RemoveAttribute(Constants.ATTR_ALGORITHM);
			    mSignMethodElement.SetAttribute(Constants.ATTR_ALGORITHM,null,value.Url);
				mSignatureMethod = value;
			}
		}

        public virtual byte[] CanonicalizedBytes
		{
			get
			{
                XmlDocument ownerDocument = mElement.OwnerDocument;
			    string baseUri = ownerDocument.BaseURI;
                XmlResolver resolver = new XmlSecureResolver(new XmlUrlResolver(), baseUri);
                XmlDocument doc = MsUtils.PreProcessElementInput(mElement, resolver, baseUri);
                // Add non default namespaces in scope
                CanonicalXmlNodeList namespaces = MsUtils.GetPropagatedAttributes(mElement);
                MsUtils.AddNamespaces(doc.DocumentElement, namespaces);

                C14nMethod c14N = mCanonicalizationMethod ?? Constants.DEFAULT_REFERENCE_C14N;
                System.Security.Cryptography.Xml.Transform transform = TransformFactory.Instance.CreateTransform(c14N.URL);
			    transform.Resolver = resolver;
               // c14nMethodTransform.Resolver = resolver;
                //transform. = baseUri;
                transform.LoadInput(doc);
                 MemoryStream outputStream = (MemoryStream)transform.GetOutput(typeof(Stream));
                return outputStream.ToArray();
			}
		}

		// baseElement metodlari
		public override string Namespace
		{
			get
			{
				return Constants.NS_XMLDSIG;
			}
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAG_SIGNEDINFO;
			}
		}
	}

}