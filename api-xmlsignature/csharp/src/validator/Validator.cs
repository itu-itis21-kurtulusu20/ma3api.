using tr.gov.tubitak.uekae.esya.api.xmlsignature;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;

	/// <summary>
	/// @author ahmety
	/// date: Oct 1, 2009
	/// </summary>
	public interface Validator
	{

		/// <param name="aSignature"> to be validated </param>
		/// <param name="aCertificate"> used for signature </param>
		/// <returns> null if this validator is not related to signature </returns>
		/// <exception cref="XMLSignatureException"> if unexpected errors occur on IO, or
		///          crypto operations etc. </exception>
		
        ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate);
		string Name {get;}

	}

}