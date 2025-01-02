package tr.gov.tubitak.uekae.esya.api.xmlsignature.formats;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;

import java.security.Key;

/**
 * @author ahmety
 * date: May 8, 2009
 */
public class XMLDSig extends BaseSignatureFormat
{

    public XMLDSig(Context aBaglam, XMLSignature aSignature) 
    {
        super(aBaglam, aSignature);
    }


    public XMLSignature createCounterSignature() throws XMLSignatureException
    {
        XMLSignature signature = new XMLSignature(mContext, false);
        SignatureMethod sm = mSignature.getSignatureMethod();
        signature.setSignatureMethod(sm);

        /*
        countersignature property is a XMLDSIG or XAdES signature whose
        ds:SignedInfo MUST contain one ds:Reference element referencing the
        ds:SignatureValue element of the embedding and countersigned XAdES
        signature. The content of the ds:DigestValue in the ds:Reference
        element of the countersignature MUST be the base-64 encoded digest of
        the complete (and canonicalized) ds:SignatureValue element (i.e.
        including the starting and closing tags) of the embedding and
        countersigned XAdES signature.
        */
        String sviURI = "#"+ mSignature.getSignatureValueId();
        signature.addDocument(sviURI, null, null, null, Constants.REFERENCE_TYPE_COUNTER_SIGNATURE, false);

        QualifyingProperties qp = mSignature.createOrGetQualifyingProperties();
        qp.createOrGetUnsignedProperties().getUnsignedSignatureProperties().addCounterSignature(signature);
        return signature;
    }

    public XMLSignature sign(Key aKey) throws XMLSignatureException
    {
        // digest references first, otherwise signature value changes!
        digestReferences(mSignature.getSignedInfo());
        // signature value
        fillSignatureValue(aKey);

        return mSignature;
    }

    public SignatureFormat evolveToA() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_A);
    }

    public SignatureFormat evolveToXL() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_X_L);
    }

    public SignatureFormat evolveToX2() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_X);
    }

    public SignatureFormat evolveToX1() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_X);
    }

    public SignatureFormat evolveToC() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_C);
    }

    public SignatureFormat evolveToT() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolve", SignatureType.XAdES_BES, SignatureType.XAdES_T);
    }

    public SignatureFormat addArchiveTimeStamp() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantAddArchiveTS");
    }
}
