package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignatureTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * <code>SignatureTimeStamp</code> envelopes a time-stamp token on the
 * <code>ds:SignatureValue</code> element and exclusively uses the implicit
 * mechanism.
 *
 * <p>The verifier should perform the following steps:
 * <ol>
 * <li>Verify the signature present within the time-stamp token. Rules for
 * acceptance of the validity of the signature within the time-stamp, involving
 * trust decisions, are out of the scope of the present document.
 * <li>Take the <code>ds:SignatureValue</code> element.
 * <li>Canonicalize it using the algorithm indicated in
 * <code>CanonicalizationMethod</code> element of the property, if present.
 * Otherwise use the standard canonicalization method as specified by XMLDSIG.
 * <li>For each time-stamp token encapsulated by the property, compute the
 * digest of the resulting byte stream using the algorithm indicated in the
 * time-stamp token and check if it is the same as the digest present there.
 * <li>Check for coherence in the values of the times indicated in the
 * time-stamp tokens. They have to be posterior to the times indicated in the
 * time-stamp tokens contained within <code>AllDataObjectsTimeStamp</code> or
 * <code>IndividualDataObjectsTimeStamp</code>, if present. Finally they have
 * to be previous to the times indicated in the time-stamp tokens enclosed by
 * any <code>RefsOnlyTimeStamp</code>, <code>SigAndRefsTimeStamp</code> or/and
 * <code>xadesv141:ArchiveTimeStamp</code> present elements.
 * </ol>
 *
 * @author ahmety
 * date: Oct 8, 2009
 */
public class SignatureTimeStampValidator extends BaseTimeStampValidator
{

    private static final Logger logger = LoggerFactory.getLogger(SignatureTimeStampValidator.class);

    List<? extends XAdESTimeStamp> getTimeStampNodes(XMLSignature aSignature)
    {
        QualifyingProperties qp = aSignature.getQualifyingProperties();
        if (qp==null)
            return null;
        UnsignedProperties up = qp.getUnsignedProperties();
        if (up==null)
            return null;
        UnsignedSignatureProperties usp = up.getUnsignedSignatureProperties();
        if (usp==null)
            return null;
        int stsCount = usp.getSignatureTimeStampCount();
        if (stsCount==0)
            return null;
        List<SignatureTimeStamp> list = new ArrayList<SignatureTimeStamp>(stsCount);
        for (int i=0; i<stsCount; i++){
            list.add(usp.getSignatureTimeStamp(i));
        }
        return list;
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
        result &= checkTimestampVsDataObjectTimestamps(aTime, aTimeStampIndex, aSignature, aResult);
        result &= checkTimestampVsSigningTime(aTime, aTimeStampIndex, aSignature, aResult);
        return result;  
    }

    String getTSNodeName()
    {
        return Constants.TAGX_SIGNATURETIMESTAMP;
    }
}
