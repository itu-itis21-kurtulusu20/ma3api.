using log4net.Repository.Hierarchy;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.formats
{

	using Logger = log4net.ILog;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using SignatureType = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class X : C
	{

		public X(Context aContext, XMLSignature aSignature) : base(aContext, aSignature)
		{
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToT() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToT()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X, SignatureType.XAdES_T);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToC() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToC()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X, SignatureType.XAdES_C);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToX1() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToX1()
		{
			throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_X);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToX2() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToX2()
		{
			throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_X);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToXL() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToXL()
		{
			addValidationData();
			return new XL(mContext, mSignature);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToA() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToA()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_X, SignatureType.XAdES_A, SignatureType.XAdES_X_L);
		}
	}

}