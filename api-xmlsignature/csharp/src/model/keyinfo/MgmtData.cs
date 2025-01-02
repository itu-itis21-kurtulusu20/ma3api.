using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

    using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <P>The MgmtData element within KeyInfo  is a string value used to convey
	/// in-band key distribution or agreement data. For example, DH key exchange, RSA
	/// key encryption, etc. Use of this element is <b>NOT RECOMMENDED.</b> It
	/// provides a  syntactic hook where in-band key distribution or agreement
	/// data can be placed. However, superior interoperable child elements of KeyInfo
	/// for the  transmission of encrypted keys and for key agreement are being
	/// specified by the W3C XML Encryption Working Group and they should be used
	/// instead of MgmtData.
	/// 
	/// <p><b>This method is not supported by this API yet!</b>
	/// 
	/// @author ahmety
	/// date: Jun 16, 2009
	/// 
	/// todo decide to implement this class
	/// </summary>
	public class MgmtData : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, KeyInfoElement
	{

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public MgmtData(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aBaglam) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public MgmtData(Element aElement, Context aBaglam) : base(aElement, aBaglam)
		{
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAG_MGMTDATA;
			}
		}
	}

}