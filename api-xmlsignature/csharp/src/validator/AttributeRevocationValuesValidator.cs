namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class AttributeRevocationValuesValidator : Validator
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
				return this.GetType().Name;
			}
		}
	}

}