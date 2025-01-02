using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

	using CertificateStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class SignatureValidationResult : ValidationResult, tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult
	{
		private CertificateStatusInfo mStatusInfo;

		public SignatureValidationResult() : base(typeof(XMLSignature))
		{
		    setCheckMessage(I18n.translate("validation.check.xmlSignature"));
		}

		/*public SignatureValidationResult(tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType aResultType, string aMessage) : base(aResultType, aMessage)
		{
		}*/

		public SignatureValidationResult(tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType aResultType, string aMessage, string aInfo)
            : base(aResultType, I18n.translate("validation.check.xmlSignature"), aMessage, aInfo, typeof(XMLSignature))
		{
		}

        public void setStatus(tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType aType, String aMessage)
        {
            base.setStatus(aType, getCheckMessage(), aMessage, null);
        }

        public void setStatus(tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType aType, String aMessage, String aInfo)
        {
            base.setStatus(aType, getCheckMessage(), aMessage, aInfo);
        }
               
        public void setCertificateStatusInfo(CertificateStatusInfo aMStatusInfo)
        {
            mStatusInfo = aMStatusInfo;
        }

        public CertificateStatusInfo getCertificateStatusInfo()
        {
            return mStatusInfo;
        }

	    public List<ValidationResultDetail> getDetails()
	    {
	        return base.getDetails();
	    }
        public List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult> getCounterSignatureValidationResults()
        {
            List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult> results = new List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult>();
            foreach (ValidationResult vr in items){
                if (vr is SignatureValidationResult){
                    results.Add((tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult)vr);
                }
            }
            return results;
        }
	}

}