using System;
using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

    using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
    using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
    using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
    using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
    using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
    using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
    using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
    using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
    using SigAndRefsTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.SigAndRefsTimeStamp;


    /// <summary>
    /// @author ayetgin
    /// </summary>
    public class SigAndRefsTimestampValidator : BaseTimeStampValidator
    {


        //JAVA TO C# CONVERTER TODO TASK: Java wildcard generics are not converted to .NET:
        //ORIGINAL LINE: java.util.List<? extends tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp> getTimeStampNodes(tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature)
        //JAVA TO C# CONVERTER TODO TASK: Java wildcard generics are not converted to .NET:
        //ORIGINAL LINE: java.util.List<? extends tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp> getTimeStampNodes(tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature)
        internal override List<XAdESTimeStamp> getTimeStampNodes(XMLSignature aSignature)
        {
            QualifyingProperties qp = aSignature.QualifyingProperties;
            if (qp != null)
            {
                UnsignedSignatureProperties usp = qp.UnsignedSignatureProperties;
                if (usp != null && usp.SigAndRefsTimeStampCount > 0)
                {
                        List<XAdESTimeStamp> list = new List<XAdESTimeStamp>(usp.SigAndRefsTimeStampCount);
                        for (int i = 0; i < usp.SigAndRefsTimeStampCount; i++)
                        {
                            list.Add(usp.getSigAndRefsTimeStamp(i));
                        }
                        return list;                   
                }
            }
            return null;
        }

        internal override XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature)
        {
            UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
            if (usp != null && usp.ArchiveTimeStampCount > 0)
            {
               return usp.getArchiveTimeStamp(0);               
            }
            return null;
        }



        internal override bool checkCoherence(ValidationResult aResult, DateTime aTime, EncapsulatedTimeStamp aTimeStampToCheck, int aTimeStampIndex, XAdESTimeStamp aContainerTimeStamp, XMLSignature aSignature, ECertificate aCertificate)
        {
            bool result = checkTimestampVsSigningCertificate(aTime, aTimeStampIndex, aCertificate, aResult);
            result &= checkTimestampVsSigningTime(aTime, aTimeStampIndex, aSignature, aResult);
            result &= checkTimestampVsDataObjectTimestamps(aTime, aTimeStampIndex, aSignature, aResult);
            result &= checkTimestampVsSignatureTimestamp(aTime, aTimeStampIndex, aSignature, aResult);
            return result;
        }

        internal override string TSNodeName
        {
            get
            {
                return Constants.TAGX_SIGANDREFSTIMESTAMP;
            }
        }
    }

}