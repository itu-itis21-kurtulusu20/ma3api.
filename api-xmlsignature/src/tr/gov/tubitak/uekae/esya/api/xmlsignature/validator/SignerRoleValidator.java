package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

/**
 * Should this property contain claimed roles, the specific rules governing the
 * acceptance of the XAdES signature as valid or not in the view of the contents
 * of this property are out of the scope of the present document.
 *
 * If this property contains some certified role, the verifier should verify the
 * validity of the attribute certificates present.
 *
 * Additional rules the governing the acceptance of the XAdES signature as valid
 * or not in the view of the contents of this property, are out of the scope of
 * the present document.
 *
 * @author ayetgin
 */
public class SignerRoleValidator implements Validator
{
    public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate) throws XMLSignatureException
    {
        return null;  // todo
    }

    public String getName()
    {
        return null;  // todo
    }
}
