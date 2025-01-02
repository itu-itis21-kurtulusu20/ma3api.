package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

/**
 * @author ayetgin
 */
public class AttributeRevocationValuesValidator implements Validator
{
    public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate) throws XMLSignatureException
    {
        return null;  // todo
    }

    public String getName()
    {
        return getClass().getName();   
    }
}
