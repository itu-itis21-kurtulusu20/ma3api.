package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

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
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.ArchiveTimeStamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author ayetgin
 */
public class ArchiveTimestampValidator extends BaseTimeStampValidator
{

    @Override
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
        int atsCount = usp.getArchiveTimeStampCount();
        if (atsCount==0)
            return null;
        List<ArchiveTimeStamp> list = new ArrayList<ArchiveTimeStamp>(atsCount);
        for (int i=0; i<atsCount; i++){
            list.add(usp.getArchiveTimeStamp(i));
        }
        return list;
    }

    @Override
    XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature) throws XMLSignatureException
    {
        UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp!=null){
            if (usp.getArchiveTimeStampCount()>0){
                for (int i=0; i<usp.getArchiveTimeStampCount(); i++){
                    ArchiveTimeStamp current = usp.getArchiveTimeStamp(i);
                    if (current.equals(timestamp)){ // here we stop
                        if (i<usp.getArchiveTimeStampCount()-1) {
                            return usp.getArchiveTimeStamp(i+1);
                        }
                    }
                }
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
        boolean result = checkTimestampVsSigningTime(aTime, aTimeStampIndex, aSignature, aResult);
        result &= checkTimestampVsDataObjectTimestamps(aTime, aTimeStampIndex, aSignature, aResult);
        result &= checkTimestampVsSignatureTimestamp(aTime, aTimeStampIndex, aSignature, aResult);
        result &= checkTimestampVsRefTimestamps(aTime, aTimeStampIndex, aSignature, aResult);

        // check versus Previous Archive Timestamps
        UnsignedSignatureProperties usp = aSignature.getQualifyingProperties().getUnsignedSignatureProperties();
        for (int i=0; i<usp.getArchiveTimeStampCount(); i++){
            ArchiveTimeStamp current = usp.getArchiveTimeStamp(i);
            if (current.equals(aContainerTimeStamp)){ // here we stop
                break;
            }
            for (int j=0; j<current.getEncapsulatedTimeStampCount();j++){
                EncapsulatedTimeStamp ets = current.getEncapsulatedTimeStamp(j);
                result &= checkTimestampVsPreviousTimestamp(aTime, aTimeStampIndex, ets, current, aResult);
            }
        }

        return result;
    }

    @Override
    String getTSNodeName()
    {
        return Constants.TAGX_ARCHIVETIMESTAMP;  
    }

}
