using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation
{
    public class CertificateValidationException : CMSSignatureException
    {
        private readonly ECertificate certificate;
        private readonly CheckerResult checkerResult;

        public CertificateValidationException(ECertificate cert, CheckerResult aCheckResult)
        {
            certificate = cert;
            checkerResult = aCheckResult;
        }

        public CheckerResult getCheckerResult()
        {
            return checkerResult;
        }

        public CertificateStatusInfo getCertStatusInfo()
        {
            return ((CertificateCheckerResultObject)checkerResult.getResultObject()).getCertStatusInfo();
        }

        //@Override
        public override String ToString()
        {

            if (checkerResult.getResultObject() != null)
            {
                if (checkerResult.getResultStatus() != Types.CheckerResult_Status.SUCCESS)
                {
                    return getCertStatusInfo().getDetailedMessage();
                }
                foreach (CheckerResult result in checkerResult.getCheckerResults())
                {
                    if (result.getResultStatus() != Types.CheckerResult_Status.SUCCESS)
                    {
                        return result.getCheckResult();
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.Append(base.ToString());
            List<IValidationResult> results = checkerResult.getMessages();
            foreach (IValidationResult iValidationResult in results)
            {
                sb.Append(iValidationResult.ToString());
            }
            return sb.ToString();

        }

        //@Override
        public String getMessage()
        {
            return ToString();
        }

        public ECertificate getCertificate()
        {
            return certificate;
        }

        public override string Message
        {
            get
            {
                return getCertStatusInfo().getDetailedMessage() + " (" + Msg.getMsg(Msg.CERTIFICATE_EXPLANATION, new[] { certificate.getSubject().getCommonNameAttribute() }) + ")";
            }
        }
    }
}