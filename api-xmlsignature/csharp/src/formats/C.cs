namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.formats
{

	using Logger = log4net.ILog;
	using TSSettings = tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using SignatureType = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using TimestampConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
	using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
	using RefsOnlyTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RefsOnlyTimeStamp;
	using SigAndRefsTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.SigAndRefsTimeStamp;

	/// <summary>
	/// XML Advanced Electronic Signature with Complete validation data references
	/// (XAdES-C) in accordance with the present document adds to the XAdES-T the
	/// <code>CompleteCertificateRefs</code> and <code>CompleteRevocationRefs</code>
	/// unsigned properties as defined by the present document. If attribute
	/// certificates appear in the signature, then XAdES-C also incorporates the
	/// <code>AttributeCertificateRefs</code> and the
	/// <code>AttributeRevocationRefs</code> elements.
	/// 
	/// <p><code>CompleteCertificateRefs</code> element contains a sequence of
	/// references to the full set of CA certificates that have been used to
	/// validate the electronic signature up to (but not including) the signing
	/// certificate.
	/// 
	/// <p><code>CompleteRevocationRefs</code> element contains a full set of
	/// references to the revocation data that have been used in the validation of
	/// the signer and CA certificates.
	/// 
	/// <p><code>AttributeCertificateRefs</code> and
	/// <code>AttributeRevocationRefs</code> elements contain references to the full
	/// set of Attribute Authorities certificates and references to the full set of
	/// revocation data that have been used in the validation of the attribute
	/// certificates present in the signature, respectively.
	/// 
	/// <p>Storing the references allows the values of the certification path and
	/// revocation data to be stored elsewhere, reducing the size of a stored
	/// electronic signature format.
	/// 
	/// <p>Below follows the structure for XAdES-C built by direct incorporation of
	/// properties on a XAdES-T containing the <code>SignatureTimeStamp</code>
	/// signed property. A XAdES-C form based on time-marks MAY exist without such
	/// element.
	/// 
	/// @author ahmety
	/// date: Nov 9, 2009
	/// </summary>
	public class C : T
	{
		public C(Context aContext, XMLSignature aSignature) : base(aContext, aSignature)
		{
		}

		public override SignatureFormat evolveToT()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_C, SignatureType.XAdES_T);
		}

		public override SignatureFormat evolveToC()
		{
			throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_C);
		}

		public override SignatureFormat evolveToX1()
		{
			TimestampConfig tsConfig = mContext.Config.TimestampConfig;
			TSSettings settings = tsConfig.Settings;
			SigAndRefsTimeStamp sarts = new SigAndRefsTimeStamp(mContext, mSignature, tsConfig.DigestMethod, settings);
			UnsignedSignatureProperties usp = mSignature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties;
			usp.addSigAndRefsTimeStamp(sarts);

			// add TS validation data for previous timestamps
			addTimestampValidationDataForSignatureTS();
			addTimestampValidationDataForAllDataObjectsTS();
			addTimestampValidationDataForIndividualDataObjectsTS();

			return new X(mContext, mSignature);
		}

		public override SignatureFormat evolveToX2()
		{
			TimestampConfig tsConfig = mContext.Config.TimestampConfig;
			TSSettings settings = tsConfig.Settings;
			RefsOnlyTimeStamp refsOnlyTimeStamp = new RefsOnlyTimeStamp(mContext, mSignature, tsConfig.DigestMethod, settings);
			UnsignedSignatureProperties usp = mSignature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties;
			usp.addRefsOnlyTimeStamp(refsOnlyTimeStamp);

			// add TS validation data for previous timestamps
			addTimestampValidationDataForSignatureTS();
			addTimestampValidationDataForAllDataObjectsTS();
			addTimestampValidationDataForIndividualDataObjectsTS();

			return new X(mContext, mSignature);
		}

		public override SignatureFormat evolveToXL()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_C, SignatureType.XAdES_X_L, SignatureType.XAdES_X);
		}

		public override SignatureFormat evolveToA()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_C, SignatureType.XAdES_A, SignatureType.XAdES_X);
		}
	}

}