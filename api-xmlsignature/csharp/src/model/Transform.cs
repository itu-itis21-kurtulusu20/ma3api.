using System.Collections.Generic;
using System.Text;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model
{
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using TransformType = tr.gov.tubitak.uekae.esya.api.xmlsignature.TransformType;
	using XMLSignatureRuntimeException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using Constants =tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


	/// <summary>
	/// <p>Each Transform consists of an Algorithm attribute and content parameters,
	/// if any, appropriate for the given algorithm. The Algorithm  attribute value
	/// specifies the name of the algorithm to be performed, and the Transform
	/// content provides additional data to govern the algorithm's processing of the
	/// transform input.</p>
	/// <p/>
	/// <p>Each <code>Transform</code> consists of an <code>Algorithm</code>
	/// attribute and content parameters, if any, appropriate for the given
	/// algorithm. The <code>Algorithm</code> attribute value specifies the name of
	/// the algorithm to be performed, and the <code>Transform</code> content
	/// provides additional data to govern the algorithm's processing of the
	/// transform input. </p>
	/// <p/>
	/// <p>Examples of transforms include but are not limited to base64 decoding
	/// [MIME], canonicalization [XML-C14N], XPath filtering [XPath], and XSLT
	/// [XSLT]. The generic definition of the Transform element also allows
	/// application-specific transform algorithms.</p>
	/// <p/>
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// <p/>
	/// <pre>
	/// &lt;complexType name="TransformType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;choice maxOccurs="unbounded" minOccurs="0">
	///         &lt;any processContents='lax' namespace='##other'/>
	///         &lt;element name="XPath" type="{http://www.w3.org/2001/XMLSchema}string"/>
	///       &lt;/choice>
	///       &lt;attribute name="Algorithm" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// @author ahmety
	///         date: Jun 12, 2009
	/// </summary>
	public class Transform : BaseElement
	{

		private string mAlgorithm;

		/*
		    first element xpath expression if any
		    second element params xml element
		 */
		private object[] mParameters;

		// used for xpath
		private XmlNode mParameterNode;
		private readonly bool mXPath;

		/// <summary>
		/// Transform constructor for given algorithm </summary>
		/// <param name="aContext">   of the signature containing transform </param>
		/// <param name="aAlgorithm"> transform algorithm </param>
		/// <exception cref="XMLSignatureException"> if there is a problem about algorithm </exception>
		public Transform(Context aContext, string aAlgorithm) : base(aContext)
		{
			mXPath = false;
			construct(aAlgorithm, null);
		}

		/// <summary>
		/// Transform constructor for XPath </summary>
		/// <param name="aContext">         of the signature containing transform </param>
		/// <param name="aAlgorithm">       transform algorithm </param>
		/// <param name="aXPathExpression"> for xpath transform </param>
		/// <param name="aPrefixToNamespaceMap"> prefix-namespace pairs Xpath element </param>
		/// <exception cref="XMLSignatureException"> if there is a problem about algorithm </exception>
		public Transform(Context aContext, string aAlgorithm, string aXPathExpression, IDictionary<string, string> aPrefixToNamespaceMap) : base(aContext)
		{

			if (!TransformType.XPATH.Url.Equals(aAlgorithm))
			{
				throw new XMLSignatureRuntimeException("Illegal use of xpath transform constructor");
			}
			mXPath = true;

            XmlElement xpath = createElement(Constants.NS_XMLDSIG, Constants.TAG_XPATH);
			xpath.InnerText = aXPathExpression;

			if (aPrefixToNamespaceMap != null)
			{
				foreach (string prefix in aPrefixToNamespaceMap.Keys)
				{
                    XmlCommonUtil.addNSAttribute(xpath, prefix, aPrefixToNamespaceMap[prefix]);
				}
			}

			construct(aAlgorithm, xpath);
		}

		/// <summary>
		/// Generic constructor for transformation with unknown transform parameter
		/// object. If you use this constructor you should most probably register
		/// your own <code>
		/// <seealso cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.Transformer"/>
		/// </code> to TransformEngine to execute transformation when necessary. </summary>
		/// <param name="aContext">       of the signature containing transform </param>
		/// <param name="aAlgorithm">     transform algorithm </param>
		/// <param name="aParamsElement"> xml element of transform parameters if any </param>
		/// <exception cref="XMLSignatureException">  if there is a problem about algorithm </exception>
        public Transform(Context aContext, string aAlgorithm, XmlElement aParamsElement)
            : base(aContext)
		{
			construct(aAlgorithm, aParamsElement);
		}

		// internal construction method
		private void construct(string aAlgorithm, XmlElement aParamsElement)
		{
			mAlgorithm = aAlgorithm;

			if ((mAlgorithm == null) || (mAlgorithm.Length == 0))
			{
                throw new XMLSignatureRuntimeException("xml.WrongContent", Constants.ATTR_ALGORITHM, Constants.TAG_TRANSFORM);
			}
		    mElement.SetAttribute(Constants.ATTR_ALGORITHM, null, aAlgorithm);

			if (aParamsElement != null)
			{
				addLineBreak();
				mElement.AppendChild(aParamsElement);
				addLineBreak();
			}
			mParameterNode = aParamsElement;
			mParameters = new object[] {(mXPath ? XPathExpression : null), aParamsElement};

		}

		/// <summary>
		/// Construct Transform from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///                               resolved appropriately </exception>
		public Transform(XmlElement aElement, Context aContext) : base(aElement, aContext)
		{

			mAlgorithm = getAttribute(aElement, Constants.ATTR_ALGORITHM);

			if ((mAlgorithm == null) || (mAlgorithm.Length == 0))
			{
                throw new XMLSignatureException("xml.WrongContent", Constants.ATTR_ALGORITHM, Constants.TAG_TRANSFORM);
			}

			if (TransformType.XPATH.Url.Equals(mAlgorithm))
			{
				mParameters = new object[] {XPathExpression, mParameterNode};
			}

		}

		public virtual string Algorithm
		{
			get
			{
				return mAlgorithm;
			}
			set
			{
			    mElement.SetAttribute(Constants.ATTR_ALGORITHM, null, value);
				mAlgorithm = value;
			}
		}


		public virtual object[] Parameters
		{
			get
			{
				return mParameters;
			}
		}

		private string XPathExpression
		{
			get
			{
				/*
				The XPath expression to be evaluated appears as the character content of
				a transform parameter child element named XPath.
				*/
                XmlElement paramElement = selectChildElement(Constants.NS_XMLDSIG, Constants.TAG_XPATH);
    
				mParameterNode = paramElement;
    
				if (paramElement == null)
				{
					return null;
				}
    
				return getXpathExpressionFromNode(paramElement.FirstChild);
			}
		}

		/// <summary>
		/// Method getStrFromNode </summary>
		/// <param name="xpathnode"> node to extract xpath expression </param>
		/// <returns> the string for the node. </returns>
		private string getXpathExpressionFromNode(XmlNode xpathnode)
		{
			if (xpathnode.NodeType == XmlNodeType.Text)
			{

				// iterate over all siblings of the context node because
				// eventually, the text is "polluted" with pi's or comments
				StringBuilder sb = new StringBuilder();

                for (XmlNode currentSibling = xpathnode.ParentNode.FirstChild; currentSibling != null; currentSibling = currentSibling.NextSibling)
				{
                    if (currentSibling.NodeType == XmlNodeType.Text)
                    {//TODO Doðru mu acaba
                        sb.Append(currentSibling.InnerText);
                    }
				}

				return sb.ToString();
			}
			else if (xpathnode.NodeType == XmlNodeType.Attribute || xpathnode.NodeType == XmlNodeType.ProcessingInstruction)
			{
				return xpathnode.Value;
			}           
			return null;
		}


		// base element methods
		public override string LocalName
		{
			get
			{
				return Constants.TAG_TRANSFORM;
			}
		}
	}

}