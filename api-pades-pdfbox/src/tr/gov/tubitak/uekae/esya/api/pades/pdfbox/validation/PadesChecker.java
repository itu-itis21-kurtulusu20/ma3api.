package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation;


import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;

/**
 * @author ayetgin
 */
public interface PadesChecker {

    ValidationResultDetail check(PAdESSignature signature);
}
