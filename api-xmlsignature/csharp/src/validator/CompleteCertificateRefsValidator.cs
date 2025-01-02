using System.Collections.Generic;
using log4net;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using PathValidationRecord = tr.gov.tubitak.uekae.esya.api.certificate.validation.PathValidationRecord;
	using CertificateStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
	using CertificateCriteriaMatcher = tr.gov.tubitak.uekae.esya.api.signature.certval.match.CertificateCriteriaMatcher;
	using CertificateSearchCriteria = tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
	using SignatureValidationResult = tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult;
	using SignedDataValidationResult = tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using CertID = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
	using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
	using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
	using CompleteCertificateRefs = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteCertificateRefs;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;



	/// <summary>
	/// If CompleteCertificateRefs is present the verifier should:
	/// <ul><li>
	/// 1) Gain access to all the CA certificates that are part of the certification
	/// path
	/// <li>
	/// 2) Check that for each certificate in the aforementioned set, the property
	/// contains its corresponding reference. For doing this the values of the
	/// IssuerSerial, ds:DigestMethod and ds:DigestValue should be checked
	/// <li>
	/// 3) Check that there are no references to certificates out of those that are
	/// part of the certification path.
	/// </ul>
	/// 
	/// @author ahmety
	/// date: Nov 13, 2009
	/// </summary>
	public class CompleteCertificateRefsValidator : Validator
	{
        private readonly CertRevInfoExtractor extractor = new CertRevInfoExtractor();
		private readonly CertificateCriteriaMatcher mMatcher = new CertificateCriteriaMatcher();
        private static readonly ILog logger = LogManager.GetLogger(typeof(CompleteCertificateRefsValidator));
		/// <param name="aSignature">   to be validated </param>
		/// <param name="aCertificate"> used for signature </param>
		/// <returns> null if this validator is not related to signature </returns>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          if unexpected errors occur on IO, or
		///          crypto operations etc. </exception>

		public virtual ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
		{
			QualifyingProperties qp = aSignature.QualifyingProperties;
			UnsignedSignatureProperties usp = qp.UnsignedSignatureProperties;
			if (usp == null)
			{
				return null;
			}

			CompleteCertificateRefs certRefs = usp.CompleteCertificateRefs;
			if (certRefs == null)
			{
				return null;
			}

			Context context = aSignature.Context;

			CertificateStatusInfo csi = context.getValidationResult(aSignature);
            UniqueCertRevInfo info = extractor.trace(csi);
            List<ECertificate> certs = info.getCertificates();

			
				// referanslar cert validation path'de var mi?
				for (int i = 0; i < certRefs.CertificateReferenceCount;i++)
				{
					CertID certId = certRefs.getCertificateReference(i);
				    // bir de timestamp validasyonunda kullaniilmis mi ona bakalim...
                    if (!contains(certs, certId) && !validationresultsContainsCertId(context.getTimestampValidationResults(aSignature), certId))
					{											
                        logger.Info(I18n.translate("validation.references.certRefs.extraCert"));
                        logger.Info(certId + "");

						return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.certRefs"),
                                                    I18n.translate("validation.references.certRefs.extraCert"),
                                                    certId.ToString(), GetType());						
					}
				 }

				// validation path'deki sertifikalara referans var mi
				// imza sertifikasi completecertrefs de olmak zorunda degil..
				foreach (ECertificate cert in certs)
				{
					if (!contains(certRefs, cert) && !aCertificate.Equals(cert) && !(cert.isCACertificate() && cert.isSelfIssued()))
					{
						return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.certRefs"),
                                                    I18n.translate("validation.references.certRefs.missingCert"),
                                                    cert.ToString(), GetType());
					}
				}
			

			return new ValidationResult(ValidationResultType.VALID,
                                        I18n.translate("validation.check.certRefs"),
                                        I18n.translate("validation.references.certRefs.valid"),
                                        null, GetType());
		}

		protected virtual bool validationresultsContainsCertId(IList<SignedDataValidationResult> aValidationResults, CertID aCertID)
		{
			
			foreach (SignedDataValidationResult validationResult in aValidationResults)
			{
                UniqueCertRevInfo info = extractor.trace(validationResult);
                if (contains(info.getCertificates(), aCertID))
                    return true;
			}
			return false;
		}

		protected virtual bool contains(IList<ECertificate> aCertificates, CertID aCertID)
		{
			CertificateSearchCriteria criteria = aCertID.toSearchCriteria();

			foreach (ECertificate certificate in aCertificates)
			{
				if (mMatcher.match(criteria, certificate))
				{
					return true;
				}
			}
			return false;
		}

		public virtual bool contains(CompleteCertificateRefs aRefs, ECertificate aCertificate)
		{
			for (int i = 0; i < aRefs.CertificateReferenceCount; i++)
			{
				CertID @ref = aRefs.getCertificateReference(i);
				CertificateSearchCriteria criteria = @ref.toSearchCriteria();

				if (mMatcher.match(criteria, aCertificate))
				{
					return true;
				}
			}
			return false;
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