using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.formats
{

	using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;


	/// <summary>
	/// @author ahmety
	/// date: May 8, 2009
	/// </summary>
	public class XMLDSig : BaseSignatureFormat
	{

		public XMLDSig(Context aBaglam, XMLSignature aSignature) : base(aBaglam, aSignature)
		{
		}


//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public XMLSignature createCounterSignature() throws XMLSignatureException
		public override XMLSignature createCounterSignature()
		{
			XMLSignature signature = new XMLSignature(mContext, false);
			SignatureMethod sm = mSignature.SignatureMethod;
			signature.SignatureMethod = sm;

			/*
			countersignature property is a XMLDSIG or XAdES signature whose
			ds:SignedInfo MUST contain one ds:Reference element referencing the
			ds:SignatureValue element of the embedding and countersigned XAdES
			signature. The content of the ds:DigestValue in the ds:Reference
			element of the countersignature MUST be the base-64 encoded digest of
			the complete (and canonicalized) ds:SignatureValue element (i.e.
			including the starting and closing tags) of the embedding and
			countersigned XAdES signature.
			*/
			string sviURI = "#" + mSignature.SignatureValueId;
			signature.addDocument(sviURI, null, null, null, Constants.REFERENCE_TYPE_COUNTER_SIGNATURE, false);

			QualifyingProperties qp = mSignature.createOrGetQualifyingProperties();
			qp.createOrGetUnsignedProperties().UnsignedSignatureProperties.addCounterSignature(signature);
			return signature;
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public XMLSignature sign(java.security.Key aKey) throws XMLSignatureException
		public override XMLSignature sign(IPrivateKey aKey)
		{
			// digest references first, otherwise signature value changes!
			digestReferences(mSignature.SignedInfo);
			// signature value
			fillSignatureValue(aKey);

			return mSignature;
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToA() throws XMLSignatureException
		public override SignatureFormat evolveToA()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_A);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToXL() throws XMLSignatureException
		public override SignatureFormat evolveToXL()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_X_L);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToX2() throws XMLSignatureException
		public override SignatureFormat evolveToX2()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_X);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToX1() throws XMLSignatureException
		public override SignatureFormat evolveToX1()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_X);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToC() throws XMLSignatureException
		public override SignatureFormat evolveToC()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_C);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat evolveToT() throws XMLSignatureException
		public override SignatureFormat evolveToT()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_T);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignatureFormat addArchiveTimeStamp() throws XMLSignatureException
		public override SignatureFormat addArchiveTimeStamp()
		{
			throw new XMLSignatureException("error.formatCantAddArchiveTS");
		}
	}

}