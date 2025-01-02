package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;

/**
 * @author ahmety
 * date: Oct 1, 2009
 */
public interface Validator
{

    /**
     * @param aSignature to be validated
     * @param aCertificate used for signature
     * @return null if this validator is not related to signature
     * @throws XMLSignatureException if unexpected errors occur on IO, or
     *          crypto operations etc.
     */
    ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
            throws XMLSignatureException;

    String getName();

}
