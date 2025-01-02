package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignatureTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedDataObjectProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;


import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The time-stamp token contained within IndividualDataObjectsTimeStamp
 * property does not cover any unsigned property, but now there is need for
 * explicit information of what of the signed data-objects are actually
 * time-stamped. The consequences is that it will exclusively use the explicit
 * mechanism.
 *
 * @author ahmety
 * date: Oct 9, 2009
 */
public class IndividualDataObjectsTimeStampValidator extends BaseTimeStampValidator
{
    /*
    1) Verify the signature present within the time-stamp token. Rules for acceptance of the validity of the signature
    within the time-stamp, involving trust decisions, are out of the scope of the present document.
    2) Take the first Include element.
    3) Check the coherence of the value of the not-fragment part of the URI within its URI attribute according to the
    rules stated in clause 7.1.4.3.1.
    4) De-reference the URI according to the rules stated in clause 7.1.4.3.1.
    5) Check that the retrieved element is actually a ds:Reference element of the ds:SignedInfo of the
    qualified signature and that its Type attribute (if present) does not have the value
    "http://uri.etsi.org/01903#SignedProperties".
    6) Process it according to the reference processing model of XMLDSIG.
    7) If the result is a node-set, canonicalize it using the algorithm indicated in CanonicalizationMethod
    element of the property, if present. Otherwise use the standard canonicalization method as specified by
    XMLDSIG.
    8) Concatenate the resulting bytes in an octet stream.
    9) Repeat steps 2) to 4) for all the subsequent Include elements (in their order of appearance) within the
    time-stamp token container.
    10) For each time-stamp token encapsulated by the property, compute the digest of the resulting byte stream using
    the algorithm indicated in the time-stamp token and check if it is the same as the digest present there.
    11) Check for coherence in the value of the times indicated in the time-stamp tokens. All the time instants have to
    be previous to the time when the validation is being made, and to the times indicated within the time-stamp
    tokens enclosed within all the rest of time-stamp container properties except AllDataObjectsTimeStamp.
    */

    List<XAdESTimeStamp> getTimeStampNodes(XMLSignature aSignature)
    {
        QualifyingProperties qp = aSignature.getQualifyingProperties();
        if (qp==null)
            return null;
        SignedDataObjectProperties sdop = qp.getSignedDataObjectProperties();
        if (sdop==null)
            return null;

        int tsCount=sdop.getIndividualDataObjectsTimeStampCount();
        if (tsCount>0){
            List<XAdESTimeStamp> tsList = new ArrayList<XAdESTimeStamp>(tsCount);
            for (int i=0; i<tsCount; i++){
                tsList.add(sdop.getIndividualDataObjectsTimeStamp(i));
            }
            return tsList;
        }
        return null;
    }

    @Override
    XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature) throws XMLSignatureException
    {
        UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp!=null){

            if (usp.getSigAndRefsTimeStampCount()>0){
                return usp.getSigAndRefsTimeStamp(0);
            }
            else if (usp.getRefsOnlyTimeStampCount()>0){
                return usp.getRefsOnlyTimeStamp(0);
            }
            else if (usp.getArchiveTimeStampCount()>0){
                return usp.getArchiveTimeStamp(0);
            }
        }
        return null;
    }

    @Override
    boolean checkCoherence(ValidationResult aResult,
                           Calendar aTime,
                           EncapsulatedTimeStamp aTimeStampToCheck, int aTimeStampIndex,
                           XAdESTimeStamp aContainerTimeStamp,
                           XMLSignature aSignature, ECertificate aCertificate) throws XMLSignatureException
    {
        boolean result = checkTimestampVsSigningCertificate(aTime, aTimeStampIndex, aCertificate, aResult);
        result &= checkTimestampVsSigningTime(aTime, aTimeStampIndex, aSignature, aResult);
        return result;
    }

    String getTSNodeName()
    {
        return "IndividualDataObjectsTimeStamp";
    }
}
