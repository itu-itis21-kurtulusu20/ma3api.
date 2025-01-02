package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.signature.impl.ResultFormatter;

import java.util.Collections;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType.*;

/**
 * @author ayetgin
 */
public class PadesSignatureValidationResult implements SignatureValidationResult {

    CertificateStatusInfo certificateStatusInfo;
    ValidationResultType resultType;
    String checkMessage;
    String checkResult;
    List<ValidationResultDetail> details;

    public PadesSignatureValidationResult(SignatureValidationResult svr) {
        certificateStatusInfo = svr.getCertificateStatusInfo();
        resultType = svr.getResultType();
        checkMessage = svr.getCheckMessage();
        checkResult = svr.getCheckMessage();
        details = svr.getDetails();
    }

    public void addDetail(ValidationResultDetail detail) {
        if (detail==null)
            return;
        details.add(detail);

        ValidationResultType type = detail.getResultType();

        boolean worse = (resultType==VALID && (type==INVALID || type==INCOMPLETE))
                            || (resultType==INCOMPLETE && type==INVALID);
        if (worse){
            resultType = type;
            checkMessage = detail.getCheckMessage();
            checkResult = detail.getCheckResult();
        }

    }

    public CertificateStatusInfo getCertificateStatusInfo() {
        return certificateStatusInfo;
    }

    public ValidationResultType getResultType() {
        return resultType;
    }

    public String getCheckMessage() {
        return checkMessage;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public List<ValidationResultDetail> getDetails() {
        return details;
    }

    public List<SignatureValidationResult> getCounterSignatureValidationResults() {
        return Collections.emptyList();
    }

    private ResultFormatter formatter = new ResultFormatter();
    @Override
    public String toString() {
        return formatter.prettyPrint(this, 0);
    }

}
