using System.Security.Cryptography;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

	using Element = XmlElement;
    using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;

	using EConstants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


	//using ECDSAPublicKey = gnu.crypto.key.ecdsa.ECDSAPublicKey;

	/// <summary>
	/// <p>The KeyValue element contains a single public key that may be useful in
	/// validating the signature. Structured formats for defining DSA (REQUIRED) and
	/// RSA (RECOMMENDED) public keys are defined in Signature Algorithms (section
	/// 6.4). The KeyValue element may include externally defined public keys values
	/// represented as PCDATA or element types from an external namespace.
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// 
	/// <pre>
	/// &lt;complexType name="KeyValueType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;choice>
	///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DSAKeyValue"/>
	///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}RSAKeyValue"/>
	///         &lt;any processContents='lax' namespace='##other'/>
	///       &lt;/choice>
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// </summary>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.DSAKeyValue </seealso>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.RSAKeyValue
	/// 
	/// @author ahmety
	/// date: Jun 10, 2009 </seealso>
	public class KeyValue : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, KeyInfoElement
	{
        private readonly KeyValueElement mValue;
        public KeyValue(Context aBaglam, AsymmetricAlgorithm aPublicKey)
            : base(aBaglam)
		{

			addLineBreak();
			if (aPublicKey is DSA)
			{
                DSAKeyValue dsa = new DSAKeyValue(aBaglam, (DSA)aPublicKey);
                this.mElement.AppendChild(dsa.mElement);
				mValue = dsa;
			}
			else if (aPublicKey is RSA)
			{
                RSAKeyValue rsa = new RSAKeyValue(mContext,(RSA)aPublicKey);
                this.mElement.AppendChild(rsa.mElement);
			    mValue = rsa;
			}
            else if (aPublicKey is ECDsa)
			{
                ECKeyValue ec = new ECKeyValue(mContext, (ECDsa)aPublicKey);
                this.mElement.AppendChild(ec.mElement);
				mValue = ec;
			}
			else
			{
				throw new XMLSignatureException("core.invalid.publickey", "KeyValue", aPublicKey);
			}          
			addLineBreak();
		}

		/// <summary>
		///  Construct KeyValue from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///  resolved appropriately </exception>
		public KeyValue(Element aElement, Context aContext) : base(aElement, aContext)
		{
            Element dsa = selectChildElement(EConstants.NS_XMLDSIG, EConstants.TAG_DSAKEYVALUE);
			if (dsa != null)
			{
				//mValueElement = dsa;
				mValue = new DSAKeyValue(dsa, aContext);
			}
			else
			{
                Element rsa = selectChildElement(EConstants.NS_XMLDSIG, EConstants.TAG_RSAKEYVALUE);
				if (rsa != null)
				{
					//mValueElement = rsa;
					mValue = new RSAKeyValue(rsa, mContext);
				}
				else
				{
                    Element ecdsa = selectChildElement(EConstants.NS_XMLDSIG_11, EConstants.TAG_ECKEYVALUE);
					if (ecdsa != null)
					{
						//mValueElement = rsa;
						mValue = new ECKeyValue(ecdsa, mContext);
					}
					else
					{
						throw new XMLSignatureException("unknown.keyValue");
					}
				}
			}
		}

		public virtual KeyValueElement Value
		{
			get
			{
				return mValue;
			}
		}

		// baseelement methods
		public override string LocalName
		{
			get
			{
                return EConstants.TAG_KEYVALUE;
			}
		}
	}

}