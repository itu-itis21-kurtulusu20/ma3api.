namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

	/// <summary>
	/// Should this property contain claimed roles, the specific rules governing the
	/// acceptance of the XAdES signature as valid or not in the view of the contents
	/// of this property are out of the scope of the present document.
	/// 
	/// If this property contains some certified role, the verifier should verify the
	/// validity of the attribute certificates present.
	/// 
	/// Additional rules the governing the acceptance of the XAdES signature as valid
	/// or not in the view of the contents of this property, are out of the scope of
	/// the present document.
	/// 
	/// @author ayetgin
	/// </summary>
	public class SignerRoleValidator : Validator
	{
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult validate(tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature, tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate aCertificate) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
		{
			return null; // todo
		}

		public virtual string Name
		{
			get
			{
				return null; // todo
			}
		}
	}

}