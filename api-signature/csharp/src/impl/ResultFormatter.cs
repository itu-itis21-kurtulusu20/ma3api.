using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;

namespace tr.gov.tubitak.uekae.esya.api.signature.impl
{
    /**
     * Utils for validation result implementers
     *
     * @see tr.gov.tubitak.uekae.esya.api.signature.ContainerValidationResult
     * @see SignatureValidationResult
     * @see tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail
     *
     * @author ayetgin
     */
    public class ResultFormatter
    {
        private static readonly String EOL = "\n";

        public String prettyPrint(ContainerValidationResult cvr)
        {
            StringBuilder buffer = new StringBuilder();
            buffer.Append("Container Validation Result : ").Append(cvr.getResultType()).Append(EOL);
            int index = 0;
            foreach (Signature signature in cvr.getSignatureValidationResults().Keys)
            {
                SignatureValidationResult svr = cvr.getSignatureValidationResults()[signature];
               // buffer.Append(mark(svr)).Append("Signature ").Append(index++).Append(EOL);
                buffer.Append(prettyPrint(svr,0));
            }
            return buffer.ToString();
        }
        public String prettyPrint(SignatureValidationResult svr, int indent)
        {
            StringBuilder buffer = new StringBuilder();
            CertificateStatusInfo csi = svr.getCertificateStatusInfo();
            buffer.Append(tabs(indent + 1)).Append("--------------------------").Append(EOL);
            if (csi != null)
            {
                ECertificate certificate = csi.getCertificate();
                buffer.Append(tabs(indent + 1)).Append(indent == 0 ? "Signer : " : "Countersigner : ").Append(certificate.getSubject().stringValue()).Append(EOL);
            }
            buffer.Append(tabs(indent + 0)).Append(mark(svr)).Append(svr.getCheckMessage().Trim()).Append(EOL);
            buffer.Append(tabs(indent + 1)).Append(svr.getCheckResult().Replace("\n", "\n" + tabs(indent + 2)).Trim()).Append(EOL);
            foreach (ValidationResultDetail detail in svr.getDetails()){
                if (!(detail is SignatureValidationResult))
                buffer.Append(prettyPrint(detail, indent+1));
            }
            foreach (SignatureValidationResult counter in svr.getCounterSignatureValidationResults()){
                buffer.Append(prettyPrint(counter, indent+1));
            }
            return buffer.ToString();
        }

        public String prettyPrint(ValidationResultDetail vrd, int level)
        {
            StringBuilder buffer = new StringBuilder();
            buffer.Append(tabs(level)).Append(mark(vrd)).Append(vrd.getCheckMessage().Trim()).Append(EOL);
            buffer.Append(tabs(level + 1)).Append(vrd.getCheckResult().Replace("\n", "\n" + tabs(level + 2)).Trim()).Append(EOL);
            List<ValidationResultDetail> details = vrd.getDetails<ValidationResultDetail>();
            if (details != null)
                foreach (ValidationResultDetail detail in details)
                {
                    buffer.Append(prettyPrint(detail, level + 1));
                }
            return buffer.ToString();
        }

        private String mark(ContainerValidationResult cvr)
        {
            switch (cvr.getResultType())
            {
                case ContainerValidationResultType.ALL_VALID: return "(+) ";
                case ContainerValidationResultType.CONTAINS_INCOMPLETE: return "(?) ";
                default: return "(-) ";
            }
        }
        private String mark(SignatureValidationResult svr)
        {
            switch (svr.getResultType())
            {
                case ValidationResultType.VALID: return "(+) ";
                case ValidationResultType.INCOMPLETE: return "(?) ";
                default: return "(-) ";
            }
        }
        private String mark(ValidationResultDetail vrd)
        {
            switch (vrd.getResultType())
            {
                case ValidationResultType.VALID: return "(+) ";
                case ValidationResultType.INCOMPLETE: return "(?) ";
                default: return "(-) ";
            }
        }
        private String tabs(int level)
        {
            String result = "";
            for (int i = 0; i < level; i++)
            {
                result += "    "; //4 space instead of tab for consistent formatting
            }
            return result;
        }
    }
}
