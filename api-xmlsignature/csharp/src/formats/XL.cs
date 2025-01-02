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
	using RefsOnlyTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RefsOnlyTimeStamp;
	using SigAndRefsTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.SigAndRefsTimeStamp;


	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class XL : X
	{
		public XL(Context aContext, XMLSignature aSignature) : base(aContext, aSignature)
		{
		}

		public override SignatureFormat evolveToT()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X_L, SignatureType.XAdES_T);
		}

		public override SignatureFormat evolveToC()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X_L, SignatureType.XAdES_C);
		}

		public override SignatureFormat evolveToX1()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X_L, SignatureType.XAdES_X);
		}

		public override SignatureFormat evolveToX2()
		{
			throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X_L, SignatureType.XAdES_X);
		}

		public override SignatureFormat evolveToXL()
		{
			throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_X_L);
		}

		public override SignatureFormat evolveToA()
		{
			TimestampConfig tsConfig = mContext.Config.TimestampConfig;
			TSSettings settings = tsConfig.Settings;

			addTimestampValidationDataForRefsOnlyTS();
			addTimestampValidationDataForSigAndRefsTS();

			ArchiveTimeStamp archiveTimeStamp = new ArchiveTimeStamp(mContext, mSignature, tsConfig.DigestMethod, settings);
			UnsignedSignatureProperties usp = mSignature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties;
			usp.addArchiveTimeStamp(archiveTimeStamp);

			return new A(mContext, mSignature);
		}

		protected internal virtual void addTimestampValidationDataForSigAndRefsTS()
		{
			UnsignedSignatureProperties usp = mSignature.QualifyingProperties.UnsignedSignatureProperties;
			if (usp != null && usp.SigAndRefsTimeStampCount > 0)
			{				
					for (int i = 0; i < usp.SigAndRefsTimeStampCount;i++)
					{
						SigAndRefsTimeStamp srts = usp.getSigAndRefsTimeStamp(i);
						addTimestampValidationData(srts, DateTime.Now);
					}				
			}
		}

		protected internal virtual void addTimestampValidationDataForRefsOnlyTS()
		{
			UnsignedSignatureProperties usp = mSignature.QualifyingProperties.UnsignedSignatureProperties;
			if (usp != null && usp.RefsOnlyTimeStampCount > 0)
			{
					for (int i = 0; i < usp.RefsOnlyTimeStampCount;i++)
					{
						RefsOnlyTimeStamp rots = usp.getRefsOnlyTimeStamp(i);
                        addTimestampValidationData(rots, DateTime.Now);
					}				
			}
		}

	}

}