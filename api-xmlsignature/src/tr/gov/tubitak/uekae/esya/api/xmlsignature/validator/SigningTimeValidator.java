package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.util.Calendar;


/**
 * <p>Should a signature policy (implicit or explicit) be in place,
 * applications SHOULD follow its rules for checking this signed property.
 * Otherwise, the present document considers the validation of this signed
 * property an application dependant issue.
 *
 * @author ahmety
 * date: Oct 1, 2009
 */
public class SigningTimeValidator implements Validator
{

    private static Logger logger = LoggerFactory.getLogger(SigningTimeValidator.class);


    public ValidationResult validate(XMLSignature aSignature, ECertificate certificate) throws XMLSignatureException
    {
        QualifyingProperties qp = aSignature.getQualifyingProperties();
        if (qp!=null)
        {
            SignedProperties sp = qp.getSignedProperties();
            if (sp!=null)
            {
                SignedSignatureProperties ssp = sp.getSignedSignatureProperties();
                // check if signing time (if exist any) is in validity
                // period of the signing certificate
                if (ssp.getSigningTime()!=null)
                {
                    Calendar bas = certificate.getNotBefore();
                    Calendar son = certificate.getNotAfter();
                    Calendar signingTime = ssp.getSigningTime().toGregorianCalendar();
                    if (signingTime.before(bas) || signingTime.after(son))
                    {
                        String failMessage = I18n.translate("validation.signingTime.notWithinCertificatePeriod");
                        logger.warn(failMessage);
                        return new ValidationResult(ValidationResultType.WARNING,
                                                    I18n.translate("validation.check.signingTime"),
                                                    failMessage, null, getClass());
                    }
                }
                logger.debug("Signing Certificate is matching Signed Property.");
                return new ValidationResult(ValidationResultType.VALID,
                                            I18n.translate("validation.check.signingTime"),
                                            I18n.translate("validation.signingTime.valid"),
                                            null, getClass());

            }
        }
        return new ValidationResult(ValidationResultType.VALID,
                                    I18n.translate("validation.check.signingTime"),
                                    I18n.translate("validation.signingTime.notFound"),
                                    null, getClass());
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }
    
}
