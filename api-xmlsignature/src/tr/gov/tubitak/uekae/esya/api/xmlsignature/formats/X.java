package tr.gov.tubitak.uekae.esya.api.xmlsignature.formats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


/**
 * @author ayetgin
 */
public class X extends C
{
    private static Logger logger = LoggerFactory.getLogger(X.class);

    public X(Context aContext, XMLSignature aSignature)
    {
        super(aContext, aSignature);
    }

    @Override
    public SignatureFormat evolveToT() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X, SignatureType.XAdES_T);
    }

    @Override
    public SignatureFormat evolveToC() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_X, SignatureType.XAdES_C);
    }

    @Override
    public SignatureFormat evolveToX1() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_X);
    }

    @Override
    public SignatureFormat evolveToX2() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatAlreadyEvolved", SignatureType.XAdES_X);
    }

    @Override
    public SignatureFormat evolveToXL() throws XMLSignatureException
    {
    	addValidationData();
        return new XL(mContext, mSignature);
    }

    @Override
    public SignatureFormat evolveToA() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_X, SignatureType.XAdES_A, SignatureType.XAdES_X_L);
    }
}
