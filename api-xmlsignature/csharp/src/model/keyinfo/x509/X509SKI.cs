using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509
{

    using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;


	/// <summary>
	/// X509Data child element, which contains the base64 encoded plain
	/// (i.e. non-DER-encoded) value of a X509 V.3 SubjectKeyIdentifier extension
	/// </summary>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data </seealso>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
	/// @author ahmety
	/// date: Jun 16, 2009 </seealso>
	public class X509SKI : X509DataElement
	{

		private readonly byte[] mSKIBytes;

		public X509SKI(Context aContext, ECertificate aCertificate) : base(aContext)
		{
			mSKIBytes = getSKI(aCertificate);
            XmlCommonUtil.setBase64EncodedText(mElement, mSKIBytes);
		}

		/// <summary>
		///  Construct X509SKI from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public X509SKI(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public X509SKI(Element aElement, Context aContext) : base(aElement, aContext)
		{
			mSKIBytes = XmlCommonUtil.getBase64DecodedText(aElement);
		}

		public virtual byte[] SKIBytes
		{
			get
			{
				return mSKIBytes;
			}
		}

		// base element
		public override string LocalName
		{
			get
			{
				return Constants.TAG_X509SKI;
			}
		}

		public virtual byte[] getSKI(ECertificate aCertificate)
		{
		    return aCertificate.getSubjectPublicKeyInfo().getEncoded();
		}
	}

}