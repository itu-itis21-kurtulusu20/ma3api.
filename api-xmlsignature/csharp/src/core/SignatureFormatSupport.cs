namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.core
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using SignatureType = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureRuntimeException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature.formats;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

	/// <summary>
	/// Support for determining signature format,
	/// and format creation according to type.
	/// 
	/// @author ahmety
	/// date: Jun 5, 2009
	/// </summary>
	public static class SignatureFormatSupport
	{

		public static SignatureType resolve(XMLSignature aSignature)
		{
			// todo
			QualifyingProperties qp = aSignature.QualifyingProperties;

			if (qp != null)
			{
				UnsignedProperties up = qp.UnsignedProperties;
				if (up != null)
				{
					UnsignedSignatureProperties usp = up.UnsignedSignatureProperties;
					if (usp != null)
					{
						// xadesv141:ArchiveTimeStamp
						// then a
						if (usp.ArchiveTimeStampCount > 0)
						{
							return SignatureType.XAdES_A;
						}

						// certificatevalues & revocationvalues
						// then xl

						if (usp.CertificateValues != null || usp.RevocationValues != null)
						{
							return SignatureType.XAdES_X_L;
						}

						// sigandrefstimestamp | refsonlytimestamp
						// then x
						if (usp.SigAndRefsTimeStampCount > 0 || usp.RefsOnlyTimeStampCount > 0)
						{
							return SignatureType.XAdES_X;
						}


						if (usp.CompleteCertificateRefs != null)
						{
							return SignatureType.XAdES_C;
						}

						if (usp.SignatureTimeStampCount > 0)
						{
							return SignatureType.XAdES_T;
						}
					}
				}

				SignedProperties sp = qp.SignedProperties;
				if (sp != null)
				{

					SignedSignatureProperties ssp = sp.SignedSignatureProperties;
					if (ssp != null && ssp.SignaturePolicyIdentifier != null)
					{						
							// it has signature policy identifier
							return SignatureType.XAdES_EPES;						
					}
				}

				// it has qualifying properties
				return SignatureType.XAdES_BES;
				// todo check if BES type B
			}
			return SignatureType.XMLDSig;
		}

		public static SignatureFormat construct(SignatureType aType, Context aContext, XMLSignature aSignature)
		{
			switch (aType)
			{
				case SignatureType.XMLDSig:
					return new XMLDSig(aContext, aSignature);
				case SignatureType.XAdES_BES:
					return new BES(aContext, aSignature);
				case SignatureType.XAdES_EPES:
					return new EPES(aContext, aSignature);
				case SignatureType.XAdES_T:
					return new T(aContext, aSignature);
				case SignatureType.XAdES_C:
					return new C(aContext, aSignature);
				case SignatureType.XAdES_X:
					return new X(aContext, aSignature);
				case SignatureType.XAdES_X_L:
					return new XL(aContext, aSignature);
				case SignatureType.XAdES_A:
					return new A(aContext, aSignature);
				// should not happen so no i18n!
				default :
					throw new XMLSignatureRuntimeException("Signature format " + aType.ToString() + " is not supported yet.");
			}
		}


	}

}