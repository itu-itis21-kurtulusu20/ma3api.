namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using CertID = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
	using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
	using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
	using AttributeCertificateRefs = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.AttributeCertificateRefs;

	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class AttributeCertificateRefsValidator : Validator
	{
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult validate(tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature, tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate aCertificate) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
		{
			QualifyingProperties qp = aSignature.QualifyingProperties;

			if (qp != null)
			{
				UnsignedSignatureProperties usp = qp.UnsignedSignatureProperties;
				if (usp != null)
				{
					AttributeCertificateRefs certRefs = usp.AttributeCertificateRefs;
					if (certRefs != null)
					{
						for (int i = 0; i < certRefs.CertificateReferenceCount; i++)
						{
						    certRefs.getCertificateReference(i);
							// todo check if certId exist in cert validation path
						}
					}
				}
			}
			return null;

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