using System;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.formats
{

	using TSSettings = tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using SignatureType = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using TimestampConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
	using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
	using ArchiveTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.ArchiveTimeStamp;


	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class A : XL
	{
		public A(Context aContext, XMLSignature aSignature) : base(aContext, aSignature)
		{
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToT() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToT()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_A, SignatureType.XAdES_T);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToC() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToC()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_A, SignatureType.XAdES_C);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToX1() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToX1()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_A, SignatureType.XAdES_X);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToX2() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToX2()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_A, SignatureType.XAdES_X);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToXL() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToXL()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_A, SignatureType.XAdES_X_L);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToA() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat evolveToA()
		{
			throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_A);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat addArchiveTimeStamp() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public override SignatureFormat addArchiveTimeStamp()
		{
			addTimestampValidationDataForLastArchiveTS();

			TimestampConfig tsConfig = mContext.Config.TimestampConfig;
			TSSettings settings = tsConfig.Settings;
			ArchiveTimeStamp archiveTimeStamp = new ArchiveTimeStamp(mContext, mSignature, tsConfig.DigestMethod, settings);

			UnsignedSignatureProperties usp = mSignature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties;
			usp.addArchiveTimeStamp(archiveTimeStamp);
			return this;
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: protected void addTimestampValidationDataForLastArchiveTS() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		protected internal virtual void addTimestampValidationDataForLastArchiveTS()
		{
			UnsignedSignatureProperties usp = mSignature.QualifyingProperties.UnsignedSignatureProperties;
			if (usp != null && usp.ArchiveTimeStampCount > 0)
			{				
					ArchiveTimeStamp ats = usp.getArchiveTimeStamp(usp.ArchiveTimeStampCount - 1);
					addTimestampValidationData(ats, DateTime.Now);				
			}

		}
	}

}