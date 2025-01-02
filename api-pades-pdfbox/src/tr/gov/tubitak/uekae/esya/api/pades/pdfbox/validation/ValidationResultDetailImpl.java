package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation;

import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType;

import java.util.Collections;
import java.util.List;

/**
 * @author ayetgin
 */
public class ValidationResultDetailImpl implements ValidationResultDetail {

    Class validatorClass;
    String checkMessage;
    String checkResult;
    ValidationResultType result;
    List<? extends ValidationResultDetail> details;

    @SuppressWarnings("unchecked")
    public ValidationResultDetailImpl(Class validatorClass, String checkMessage, String checkResult, ValidationResultType result) {
        this(validatorClass, checkMessage, checkResult, result, Collections.EMPTY_LIST);
    }


    public ValidationResultDetailImpl(Class validatorClass, String checkMessage, String checkResult, ValidationResultType result, List<? extends ValidationResultDetail> details) {
        this.validatorClass = validatorClass;
        this.checkMessage = checkMessage;
        this.checkResult = checkResult;
        this.result = result;
        this.details = details;
    }

    public Class getValidatorClass() {
        return validatorClass;
    }

    public String getCheckMessage() {
        return checkMessage;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public ValidationResultType getResultType() {
        return result;
    }

    public List<? extends ValidationResultDetail> getDetails() {
        return details;
    }
}
