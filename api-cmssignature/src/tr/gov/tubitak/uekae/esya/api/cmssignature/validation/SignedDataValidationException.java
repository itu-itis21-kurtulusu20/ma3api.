package tr.gov.tubitak.uekae.esya.api.cmssignature.validation;

public class SignedDataValidationException extends Exception{

    SignedDataValidationResult mSignedDataValidationResult;

    public SignedDataValidationException(String message, SignedDataValidationResult signedDataValidationResult){
        super(message);
        mSignedDataValidationResult = signedDataValidationResult;
    }

    public SignedDataValidationResult getSignedDataValidationResult(){
        return mSignedDataValidationResult;
    }

    @Override
    public String toString() {
        return getMessage() + " Details: \n" + mSignedDataValidationResult.toString();
    }
}
