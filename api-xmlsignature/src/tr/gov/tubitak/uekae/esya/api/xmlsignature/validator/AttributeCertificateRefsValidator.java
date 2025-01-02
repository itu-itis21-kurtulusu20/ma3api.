package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.AttributeCertificateRefs;

/**
 * @author ayetgin
 */
public class AttributeCertificateRefsValidator implements Validator
{
    public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate) throws XMLSignatureException
    {
        QualifyingProperties qp = aSignature.getQualifyingProperties();

        if (qp!=null){
            UnsignedSignatureProperties usp = qp.getUnsignedSignatureProperties();
            if (usp!=null){
                AttributeCertificateRefs certRefs = usp.getAttributeCertificateRefs();
                if (certRefs!=null){
                    for (int i=0; i<certRefs.getCertificateReferenceCount(); i++){
                        CertID certId = certRefs.getCertificateReference(i);

                        // todo check if certId exist in cert validation path
                    }
                }
            }
        }
        return null;

    }

    public String getName()
    {
        return getClass().getName();   
    }
}
