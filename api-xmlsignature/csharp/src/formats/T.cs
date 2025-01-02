using System;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.formats
{

	using Logger = log4net.ILog;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using CertificateStatus = tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
	using CertificateStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using ValidationDataCollector = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.ValidationDataCollector;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
	using XmlSignUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlSignUtil;


	/// <summary>
	/// @author ahmety
	/// date: Nov 4, 2009
	/// </summary>
	public class T : BES
	{
		private static Logger logger = log4net.LogManager.GetLogger(typeof(T));

		public T(Context aContext, XMLSignature aSignature) : base(aContext, aSignature)
		{
		}

		public override SignatureFormat evolveToT()
		{
			throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_T);
		}

		public override SignatureFormat evolveToC()
		{
			CertificateStatusInfo csi = mContext.getValidationResult(mSignature);

			if (csi == null)
			{
				ECertificate certificate = extractCertificate();
				if (certificate == null)
				{
					throw new XMLSignatureException("errors.cantFindCertificate");
				}


				ValidationResult r1 = (new ValidationDataCollector()).collect(mSignature, true);

				csi = _validateCertificate(certificate, mContext.GetValidationTime(mSignature),true);
			    CertificateStatus certificateStatus = csi.getCertificateStatus();
			    /*string textAl = certificateStatus.textAl();
			    string checkResultsToString = csi.checkResultsToString();*/
			    mContext.setValidationResult(mSignature, csi);
			}
			if (csi.getCertificateStatus() == CertificateStatus.VALID)
			{
				addReferences(csi);
			}
			else
			{
				logger.Warn(XmlSignUtil.verificationInfo(csi));
				throw new XMLSignatureException("validation.certificate.error");
			}
			return new C(mContext, mSignature);
		}


		public override SignatureFormat evolveToX1()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_T, SignatureType.XAdES_X, SignatureType.XAdES_C);
		}

		public override SignatureFormat evolveToX2()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_T, SignatureType.XAdES_X, SignatureType.XAdES_C);
		}

		public override SignatureFormat evolveToXL()
		{
			addTimestampValidationDataForSignatureTS();
			addTimestampValidationDataForAllDataObjectsTS();
			addTimestampValidationDataForIndividualDataObjectsTS();
			addValidationData();
			return new XL(mContext, mSignature);
		}

		public override SignatureFormat evolveToA()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_T, SignatureType.XAdES_A, SignatureType.XAdES_C);
		}

		protected internal virtual void addTimestampValidationDataForAllDataObjectsTS()
		{
			SignedDataObjectProperties sdop = mSignature.QualifyingProperties.SignedDataObjectProperties;
			if (sdop != null && sdop.AllDataObjectsTimeStampCount > 0)
			{
					for (int i = 0; i < sdop.AllDataObjectsTimeStampCount;i++)
					{
						AllDataObjectsTimeStamp adots = sdop.getAllDataObjectsTimeStamp(i);
						addTimestampValidationData(adots,DateTime.Now);
					}				
			}
		}

		protected internal virtual void addTimestampValidationDataForIndividualDataObjectsTS()
		{
			SignedDataObjectProperties sdop = mSignature.QualifyingProperties.SignedDataObjectProperties;
			if (sdop != null && sdop.IndividualDataObjectsTimeStampCount > 0)
			{	
					for (int i = 0; i < sdop.IndividualDataObjectsTimeStampCount;i++)
					{
                        IndividualDataObjectsTimeStamp idots = sdop.getIndividualDataObjectsTimeStamp(i);
                        addTimestampValidationData(idots, DateTime.Now);
                    }                
            }
        }

        protected internal virtual void addTimestampValidationDataForSignatureTS()
        {
            UnsignedSignatureProperties usp = mSignature.QualifyingProperties.UnsignedSignatureProperties;
            if (usp != null && usp.SignatureTimeStampCount > 0)
            {
                    for (int i = 0; i < usp.SignatureTimeStampCount; i++)
                    {
                        SignatureTimeStamp sts = usp.getSignatureTimeStamp(i);
                        addTimestampValidationData(sts, DateTime.Now);
					}				
			}
		}

	}

}