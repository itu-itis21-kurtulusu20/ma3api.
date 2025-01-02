package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;


import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * The time-stamp token contained within <code>AllDataObjectsTimeStamp</code>
 * property does not cover any unsigned property and the regular elements
 * within the signature that are mandated to be time-stamped are easily
 * determined by inspecting the <code>ds:SignedInfo</code>contents. That is why
 * this container will exclusively use the implicit mechanism.
 *
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.AllDataObjectsTimeStamp
 *
 * @author ahmety
 * date: Oct 8, 2009
 */
public class AllDataObjectsTimeStampValidator extends BaseTimeStampValidator
{

    List<XAdESTimeStamp> getTimeStampNodes(XMLSignature aSignature)
    {
        QualifyingProperties qp = aSignature.getQualifyingProperties();
        if (qp==null)
            return null;
        SignedDataObjectProperties sdop = qp.getSignedDataObjectProperties();
        if (sdop==null)
            return null;

        int tsCount=sdop.getAllDataObjectsTimeStampCount();
        if (tsCount>0){
            List<XAdESTimeStamp> tsList = new ArrayList<XAdESTimeStamp>(tsCount);
            for (int i=0; i<tsCount; i++){
                tsList.add(sdop.getAllDataObjectsTimeStamp(i));
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
        return Constants.TAGX_ALLDATAOBJECTSTIMESTAMP;
    }
}
