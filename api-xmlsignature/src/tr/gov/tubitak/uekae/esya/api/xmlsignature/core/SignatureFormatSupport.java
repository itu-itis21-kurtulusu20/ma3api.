package tr.gov.tubitak.uekae.esya.api.xmlsignature.core;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.formats.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.*;

/**
 * Support for determining signature format,
 * and format creation according to type.
 *
 * @author ahmety
 * date: Jun 5, 2009
 */
public class SignatureFormatSupport
{

    public static SignatureType resolve(XMLSignature aSignature){
        // todo
        QualifyingProperties qp = aSignature.getQualifyingProperties();

        if (qp!=null)
        {
            UnsignedProperties up = qp.getUnsignedProperties();
            if (up != null){
                UnsignedSignatureProperties usp = up.getUnsignedSignatureProperties();
                if (usp != null){
                    // xadesv141:ArchiveTimeStamp
                    // then a
                    if (usp.getArchiveTimeStampCount()>0){
                        return SignatureType.XAdES_A;
                    }

                    // certificatevalues & revocationvalues
                    // then xl

                    if (usp.getCertificateValues()!=null || usp.getRevocationValues()!=null){
                        return SignatureType.XAdES_X_L;
                    }

                    // sigandrefstimestamp | refsonlytimestamp
                    // then x
                    if (usp.getSigAndRefsTimeStampCount()>0 || usp.getRefsOnlyTimeStampCount()>0){
                        return SignatureType.XAdES_X; 
                    }


                    if (usp.getCompleteCertificateRefs()!=null){
                        return SignatureType.XAdES_C;
                    }

                    if (usp.getSignatureTimeStampCount() > 0){
                        return SignatureType.XAdES_T;
                    }
                }
            }

            SignedProperties sp = qp.getSignedProperties();
            if (sp!=null){

                SignedSignatureProperties ssp = sp.getSignedSignatureProperties();
                if (ssp!=null){
                    if (ssp.getSignaturePolicyIdentifier() != null && ssp.getSignaturePolicyIdentifier().getSignaturePolicyId() != null){
                        // it has signature policy identifier
                        return SignatureType.XAdES_EPES;
                    }
                }
            }

            // it has qualifying properties
            return SignatureType.XAdES_BES;
            // todo check if BES type B
        }
        return SignatureType.XMLDSig;
    }

    public static SignatureFormat construct(SignatureType aType, 
                                            Context aContext,
                                            XMLSignature aSignature)
    {
        switch (aType)
        {
            case XMLDSig    : return new XMLDSig(aContext, aSignature);
            case XAdES_BES  : return new BES(aContext, aSignature);
            case XAdES_EPES : return new EPES(aContext, aSignature);
            case XAdES_T    : return new T(aContext, aSignature);
            case XAdES_C    : return new C(aContext, aSignature);
            case XAdES_X    : return new X(aContext, aSignature);
            case XAdES_X_L  : return new XL(aContext, aSignature);
            case XAdES_A    : return new A(aContext, aSignature);
            // should not happen so no i18n!
            default         : throw new XMLSignatureRuntimeException("Signature format "+aType.name()+" is not supported yet.");
        }
    }


}
