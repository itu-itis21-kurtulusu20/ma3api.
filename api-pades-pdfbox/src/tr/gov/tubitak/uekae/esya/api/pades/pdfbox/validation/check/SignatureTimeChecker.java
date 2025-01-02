package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.check;

import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.PadesChecker;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.ValidationResultDetailImpl;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType;

import java.util.Calendar;

/**
 * @author ayetgin
 */
public class SignatureTimeChecker implements PadesChecker {

    public static final int MILLIS_IN_SECONDS = 1000;

    public ValidationResultDetail check(PAdESSignature signature){

        Calendar timeStampTime = signature.getTimeStampTime();
        Calendar declaredTimeOfPdf = signature.getSigningTimeAttrFromM();
        Calendar declaredTimeOfCMS = signature.getSigningTimeAttrFromCMS();
        long signingTimeTolerance = signature.getContainer().getContext().getConfig().getCertificateValidationConfig().getSigningTimeToleranceInSeconds();

        boolean resultOfPdf = true;
        boolean resultOfCms = true;

        if(timeStampTime != null){
            if(declaredTimeOfPdf != null)
              resultOfPdf = checkDeclaredTimeIsBeforeTimeStampTime(timeStampTime, declaredTimeOfPdf, signingTimeTolerance);
            if(declaredTimeOfCMS != null )
              resultOfCms = checkDeclaredTimeIsBeforeTimeStampTime(timeStampTime, declaredTimeOfCMS, signingTimeTolerance);
        }

        if (resultOfPdf == false || resultOfCms == false )
            return new ValidationResultDetailImpl(SignatureTimeChecker.class, CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMP_TIME_CHECKER), CMSSignatureI18n.getMsg(E_KEYS.SIGNING_TIME_CHECKER_UNSUCCESSFUL), ValidationResultType.INVALID);


        //this part tries to validate signing time via comparing the time of the signature and the time of a previous signature that has time value.
        //Of course the time of previous signature must be earlier than the time of the signature.
        int index = signature.getContainer().getSignatures().indexOf(signature);
        if (index==0)
            return null;
        Calendar time = signature.getSigningTime();
        if (time==null)
            return null;
        Calendar prevTime = getPreviousSigningTime(signature, index-1);
        if (prevTime==null)
            return null;
        
        time.add(Calendar.MILLISECOND,(int)(signingTimeTolerance * MILLIS_IN_SECONDS));
        
        if (time.before(prevTime)){
            return new ValidationResultDetailImpl(SignatureTimeChecker.class, CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMP_TIME_CHECKER), CMSSignatureI18n.getMsg(E_KEYS.SIGNING_TIME_CHECKER_UNSUCCESSFUL), ValidationResultType.INVALID);
        }

        return new ValidationResultDetailImpl(SignatureTimeChecker.class, CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMP_TIME_CHECKER), CMSSignatureI18n.getMsg(E_KEYS.SIGNING_TIME_CHECKER_SUCCESSFUL), ValidationResultType.VALID);
    }
    
    public Calendar  getPreviousSigningTime(PAdESSignature signature, int index) {
    	Calendar prevTime = signature.getContainer().getSignatures().get(index).getSigningTime();
    	while(prevTime == null && index != 0){
    		index--;
    		prevTime = signature.getContainer().getSignatures().get(index).getSigningTime();
    	}
        return prevTime;
    }

    public boolean checkDeclaredTimeIsBeforeTimeStampTime(Calendar timeStampTime, Calendar declaredTime, long signingTimeTolerance){

        long stsTimeInLong = timeStampTime.getTimeInMillis();
        long signingTimeInLong = declaredTime.getTimeInMillis();
        long signingTimeToleranceInMilliSeconds = signingTimeTolerance*MILLIS_IN_SECONDS;
        long signingTimeFlex = signingTimeInLong - signingTimeToleranceInMilliSeconds;

        return stsTimeInLong >= signingTimeFlex;
    }
}
