using System;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

    using Logger = log4net.ILog;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
	using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
	using SignedProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedProperties;
	using SignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedSignatureProperties;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;



	/// <summary>
	/// <p>Should a signature policy (implicit or explicit) be in place,
	/// applications SHOULD follow its rules for checking this signed property.
	/// Otherwise, the present document considers the validation of this signed
	/// property an application dependant issue.
	/// 
	/// @author ahmety
	/// date: Oct 1, 2009
	/// </summary>
	public class SigningTimeValidator : Validator
	{

		private static Logger logger = log4net.LogManager.GetLogger(typeof(SigningTimeValidator));

		public virtual ValidationResult validate(XMLSignature aSignature, ECertificate certificate)
		{
			QualifyingProperties qp = aSignature.QualifyingProperties;
			if (qp != null)
			{
				SignedProperties sp = qp.SignedProperties;
				if (sp != null)
				{
					SignedSignatureProperties ssp = sp.SignedSignatureProperties;
					// check if signing time (if exist any) is in validity
					// period of the signing certificate
					if (ssp.SigningTime != null)
					{
					    DateTime bas = certificate.getNotBefore().Value;
						DateTime son = certificate.getNotAfter().Value;
					    DateTime signingTime = (DateTime) ssp.SigningTime;
						if (signingTime<bas || signingTime>son)
						{
							string failMessage = I18n.translate("validation.signingTime.notWithinCertificatePeriod");
							logger.Warn(failMessage);
							return new ValidationResult(ValidationResultType.WARNING,
                                                        I18n.translate("validation.check.signingTime"),
                                                        failMessage, null, GetType());
						}
					}
					logger.Debug("Signing Certificate is matching Signed Property.");
					return new ValidationResult(ValidationResultType.VALID,
                                                I18n.translate("validation.check.signingTime"),
                                                I18n.translate("validation.signingTime.valid"),
                                                null, GetType());

				}
			}
			return new ValidationResult(ValidationResultType.VALID,
                                        I18n.translate("validation.check.signingTime"),
                                        I18n.translate("validation.signingTime.notFound"),
                                        null, GetType());
		}

        internal XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature)
        {
            UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
        if (usp!=null){

            if (usp.SignatureTimeStampCount>0){
                return usp.getSigAndRefsTimeStamp(0);
            }
            else if (usp.RefsOnlyTimeStampCount>0){
                return usp.getRefsOnlyTimeStamp(0);
            }
            else if (usp.ArchiveTimeStampCount>0){
                return usp.getArchiveTimeStamp(0);
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