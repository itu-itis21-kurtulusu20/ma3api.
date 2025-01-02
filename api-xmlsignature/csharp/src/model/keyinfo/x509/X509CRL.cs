using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509
{

    using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <code>X509Data</code> child element, which contains a base64-encoded
	/// certificate revocation list (CRL)
	/// </summary>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data </seealso>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
	/// @author ahmety
	/// date: Jun 16, 2009 </seealso>
	public class X509CRL : X509DataElement
	{
		private readonly byte[] mCRLBytes;

		public X509CRL(Context aContext, byte[] aCRLBytes) : base(aContext)
		{
			mCRLBytes = aCRLBytes;
            XmlCommonUtil.setBase64EncodedText(mElement, aCRLBytes);
		}

		/// <summary>
		///  Construct X509CRL from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public X509CRL(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public X509CRL(Element aElement, Context aContext) : base(aElement, aContext)
		{
			mCRLBytes = XmlCommonUtil.getBase64DecodedText(mElement);
		}

		public virtual byte[] CRLBytes
		{
			get
			{
				return mCRLBytes;
			}
		}

		// base element
		public override string LocalName
		{
			get
			{
				return Constants.TAG_X509CRL;
			}
		}


	}

}