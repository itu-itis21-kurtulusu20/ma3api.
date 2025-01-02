package tr.gov.tubitak.uekae.esya.api.signature.impl;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.signature.ContainerValidationResult;
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;

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
    private static final String EOL = "\n";

    public String prettyPrint(ContainerValidationResult cvr){
        StringBuilder buffer = new StringBuilder();
        buffer.append("Container Validation Result : ").append(cvr.getResultType()).append(EOL);
        int index=0;
        for (Signature signature : cvr.getSignatureValidationResults().keySet()){
            SignatureValidationResult svr = cvr.getSignatureValidationResults().get(signature);
            //buffer.append(mark(svr)).append("Signature ").append(index++).append(EOL);
            buffer.append(prettyPrint(svr, 0));
        }
        return buffer.toString();
    }


    public String prettyPrint(SignatureValidationResult svr, int indent){
        StringBuilder buffer = new StringBuilder();
        CertificateStatusInfo csi = svr.getCertificateStatusInfo();
        buffer.append(tabs(indent + 1)).append("--------------------------").append(EOL);
        if (csi!=null){
            ECertificate certificate = csi.getCertificate();
            buffer.append(tabs(indent+1)).append(indent==0 ? "Signer : " : "Countersigner : ").append(certificate.getSubject().stringValue()).append(EOL);
        }
        buffer.append(tabs(indent+0)).append(mark(svr)).append(svr.getCheckMessage().trim()).append(EOL);
        buffer.append(tabs(indent+1)).append(svr.getCheckResult().replaceAll("\n", "\n" + tabs(indent+2)).trim()).append(EOL);
        for (ValidationResultDetail detail : svr.getDetails()){
            if (!(detail instanceof SignatureValidationResult))
            buffer.append(prettyPrint(detail, indent+1));
        }
        for (SignatureValidationResult counter : svr.getCounterSignatureValidationResults()){
            buffer.append(prettyPrint(counter, indent+1));
        }
        return buffer.toString();
    }

    public String prettyPrint(ValidationResultDetail vrd, int level){
        StringBuilder buffer = new StringBuilder();
        buffer.append(tabs(level)).append(mark(vrd)).append(vrd.getCheckMessage().trim()).append(EOL);
        buffer.append(tabs(level+1)).append(vrd.getCheckResult().replaceAll("\n", "\n" + tabs(level + 2)).trim()).append(EOL);
        if (vrd.getDetails()!=null)
        for (ValidationResultDetail detail : vrd.getDetails()){
            buffer.append(prettyPrint(detail, level+1));
        }
        return  buffer.toString();
    }

    private String mark(ContainerValidationResult cvr){
        switch (cvr.getResultType()){
            case ALL_VALID          : return "(+) ";
            case CONTAINS_INCOMPLETE: return "(?) ";
            default                 : return "(-) ";
        }
    }
    private String mark(SignatureValidationResult svr){
        switch (svr.getResultType()){
            case VALID      : return "(+) ";
            case INCOMPLETE : return "(?) ";
            default         : return "(-) ";
        }
    }
    private String mark(ValidationResultDetail vrd){
        switch (vrd.getResultType()){
            case VALID      : return "(+) ";
            case INCOMPLETE : return "(?) ";
            default         : return "(-) ";
        }
    }
    private String tabs(int level){
        String result = "";
        for (int i=0; i<level;i++){
            result += "    "; //4 space instead of tab for consistent formatting
        }
        return result;
    }

}
