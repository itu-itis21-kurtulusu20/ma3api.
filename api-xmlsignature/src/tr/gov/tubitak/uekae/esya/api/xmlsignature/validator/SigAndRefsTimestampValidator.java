package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.SigAndRefsTimeStamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author ayetgin
 */
public class SigAndRefsTimestampValidator extends BaseTimeStampValidator
{


    @Override
    List<? extends XAdESTimeStamp> getTimeStampNodes(XMLSignature aSignature)
    {
        QualifyingProperties qp = aSignature.getQualifyingProperties();
        if (qp!=null){
            UnsignedSignatureProperties usp = qp.getUnsignedSignatureProperties();
            if (usp!=null){
                if (usp.getSigAndRefsTimeStampCount()>0){
                    List<SigAndRefsTimeStamp> list = new ArrayList<SigAndRefsTimeStamp>(usp.getSigAndRefsTimeStampCount());
                    for (int i=0; i<usp.getSigAndRefsTimeStampCount(); i++){
                        list.add(usp.getSigAndRefsTimeStamp(i));
                    }
                    return list;
                }

            }
        }
        return null;
    }

    @Override
    XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature) throws XMLSignatureException
    {
        UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp!=null){
            if (usp.getArchiveTimeStampCount()>0){
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
        result &= checkTimestampVsDataObjectTimestamps(aTime, aTimeStampIndex, aSignature, aResult);
        result &= checkTimestampVsSignatureTimestamp(aTime, aTimeStampIndex, aSignature, aResult);
        return result;
    }
    
    @Override
    String getTSNodeName()
    {
        return Constants.TAGX_SIGANDREFSTIMESTAMP;  
    }
}
