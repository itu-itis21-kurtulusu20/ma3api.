using System;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using DOMDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
	using XAdESBaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
	using SignedInfo = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
	using Reference = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	using Element = XmlElement;

	/// <summary>
	/// <code>Include</code> elements explicitly identify data objects that are
	/// time-stamped. Their order of appearance indicates how the data objects
	/// contribute in the generation of the input to the digest computation.
	/// 
	/// <p>The URI attribute in <code>Include</code> element identifies one
	/// time-stamped data object. Its value MUST follow the rules indicated below:
	/// <ul>
	/// <li>It MUST have an empty not-fragment part and a bare-name XPointer fragment
	/// when the Include and the time-stamped data object are in the same document.
	/// <li>It MUST have a not empty not-fragment part and a bare-name XPointer
	/// fragment when the Include and the time-stamped data object are not in the
	/// same document.
	/// <li>When not empty, its not-fragment part MUST be equal to:
	/// <ul>
	/// <li>the not-fragment part of the Target attribute of the QualifyingProperties
	/// enclosing the Include element if the time-stamped data object is enveloped by
	/// the XAdES signature; or
	/// <li>the not-fragment part of the URI attribute of the QualifyingPropertiesReference
	/// element referencing the QualifyingProperties element enveloping the
	/// time-stamped data object if this QualifyingProperties element is not
	/// enveloped by the XAdES signature.
	/// </ul>
	/// </ul>
	/// 
	/// Applications aligned with the present document MUST parse the retrieved
	/// resource, and then process the bare-name XPointer as explained below to get
	/// a XPath node-set suitable for use by Canonical XML. For processing the
	/// bare-name XPointer applications MUST use as XPointer evaluation context the
	/// root node of the XML document that contains the element referenced by the
	/// not-fragment part of URI. Applications MUST derive an XPath node-set from
	/// the resultant location-set as indicated below:
	/// <ol>
	/// <li>Replace the element node E retrieved by the bare-name XPointer with E
	/// plus all descendants of E (text, comments, PIs, elements) and all namespace
	/// and attribute nodes of E and its descendant elements.
	/// <li>Delete all the comment nodes.
	/// </ul>
	/// 
	/// <p>In time-stamps that cover <code>ds:Reference</code> elements, the
	/// attribute referencedData MAY be present. If present with value set to "true",
	/// the time-stamp is computed on the result of processing the corresponding
	/// <code>ds:Reference</code> element according to the XMLDSIG processing model.
	/// If the attribute is not present or is present with value "false", the
	/// time-stamp is computed on the ds:Reference element itself. When appearing in
	/// a time-stamp container property, each Include element MUST be processed in
	/// order as detailed below:
	/// <ol>
	/// <li>Retrieve the data object referenced in the URI attribute following the
	/// referencing mechanism indicated above.
	/// <li>If the retrieved data is a ds:Reference element and the referencedData
	/// attribute is set to the value "true", take the result of processing the
	/// retrieved ds:Reference element according to the reference processing model of
	/// XMLDSIG; otherwise take the ds:Reference element itself.
	/// <li>If the resulting data is an XML node set, canonicalize it. If
	/// ds:Canonicalization is present, the algorithm indicated by this element is
	/// used. If not, the standard canonicalization method specified by XMLDSIG is used.
	/// <li>Concatenate the resulting octets to those resulting from previous
	/// processing as indicated in the corresponding time-stamp container property.
	/// </ol>
	/// 
	/// <p>Below follows the schema definition for the data type.
	/// 
	/// <pre>
	/// &lt;xsd:complexType name="IncludeType">
	///   &lt;xsd:attribute name="URI" type="xsd:anyURI" use="required"/>
	///   &lt;xsd:attribute name="referencedData" type="xsd:boolean" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Sep 28, 2009
	/// </summary>
	public class Include : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private string mURI;
		private bool ?mReferencedData;

		public Include(Context aContext, string aURI, bool ?aReferencedData) : base(aContext)
		{
			mURI = aURI;
			mReferencedData = aReferencedData;

			mElement.SetAttribute(Constants.ATTR_URI,null, mURI);
			if (aReferencedData != null)
			{
                mElement.SetAttribute(Constants.ATTR_REFERENCEDDATA, null,aReferencedData.ToString());
			}
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public Include(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public Include(Element aElement, Context aContext) : base(aElement, aContext)
		{
            mURI = mElement.GetAttribute(Constants.ATTR_URI);
            string isReferenced = mElement.GetAttribute(Constants.ATTR_REFERENCEDDATA);
			if (isReferenced != null)
			{
				mReferencedData = Convert.ToBoolean(isReferenced);
			}
		}

		public virtual string URI
		{
			get
			{
				return mURI;
			}
			set
			{
				mURI = value;
                mElement.SetAttribute(Constants.ATTR_URI, null,mURI);
			}
		}


		public virtual bool? ReferencedData
		{
			get
			{
				return mReferencedData;
			}
			set
			{
				mReferencedData = value;
				if (value == null)
				{
                    mElement.RemoveAttribute(Constants.ATTR_REFERENCEDDATA,null);
				}
				else
				{
                    mElement.SetAttribute(Constants.ATTR_REFERENCEDDATA, null,value.ToString());
				}
			}
		}


		public override string LocalName
		{
			get
			{
                return Constants.TAGX_INCLUDE;
			}
		}
	}

}