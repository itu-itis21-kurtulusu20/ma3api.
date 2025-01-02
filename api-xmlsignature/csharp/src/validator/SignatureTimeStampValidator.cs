using System;
using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

    using Logger = log4net.ILog;
    using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
    using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
    using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
    using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
    using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
    using SignatureTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignatureTimeStamp;
    using UnsignedProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedProperties;
    using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
    using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
    using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;



    /// <summary>
    /// <code>SignatureTimeStamp</code> envelopes a time-stamp token on the
    /// <code>ds:SignatureValue</code> element and exclusively uses the implicit
    /// mechanism.
    /// 
    /// <p>The verifier should perform the following steps:
    /// <ol>
    /// <li>Verify the signature present within the time-stamp token. Rules for
    /// acceptance of the validity of the signature within the time-stamp, involving
    /// trust decisions, are out of the scope of the present document.
    /// <li>Take the <code>ds:SignatureValue</code> element.
    /// <li>Canonicalize it using the algorithm indicated in
    /// <code>CanonicalizationMethod</code> element of the property, if present.
    /// Otherwise use the standard canonicalization method as specified by XMLDSIG.
    /// <li>For each time-stamp token encapsulated by the property, compute the
    /// digest of the resulting byte stream using the algorithm indicated in the
    /// time-stamp token and check if it is the same as the digest present there.
    /// <li>Check for coherence in the values of the times indicated in the
    /// time-stamp tokens. They have to be posterior to the times indicated in the
    /// time-stamp tokens contained within <code>AllDataObjectsTimeStamp</code> or
    /// <code>IndividualDataObjectsTimeStamp</code>, if present. Finally they have
    /// to be previous to the times indicated in the time-stamp tokens enclosed by
    /// any <code>RefsOnlyTimeStamp</code>, <code>SigAndRefsTimeStamp</code> or/and
    /// <code>xadesv141:ArchiveTimeStamp</code> present elements.
    /// 
    /// @author ahmety
    /// date: Oct 8, 2009
    /// </summary>
    public class SignatureTimeStampValidator : BaseTimeStampValidator
    {
        internal override List<XAdESTimeStamp> getTimeStampNodes(XMLSignature aSignature)
        {
            QualifyingProperties qp = aSignature.QualifyingProperties;
            if (qp == null)
            {
                return null;
            }
            UnsignedProperties up = qp.UnsignedProperties;
            if (up == null)
            {
                return null;
            }
            UnsignedSignatureProperties usp = up.UnsignedSignatureProperties;
            if (usp == null)
            {
                return null;
            }
            int stsCount = usp.SignatureTimeStampCount;
            if (stsCount == 0)
            {
                return null;
            }
            List<XAdESTimeStamp> list = new List<XAdESTimeStamp>(stsCount);
            for (int i = 0; i < stsCount; i++)
            {
                list.Add(usp.getSignatureTimeStamp(i));
            }
            return list;
        }



        internal override XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature)
        {
            UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
            if (usp != null)
            {

                if (usp.SigAndRefsTimeStampCount > 0)
                {
                    return usp.getSigAndRefsTimeStamp(0);
                }
                else if (usp.RefsOnlyTimeStampCount > 0)
                {
                    return usp.getRefsOnlyTimeStamp(0);
                }
                else if (usp.ArchiveTimeStampCount > 0)
                {
                    return usp.getArchiveTimeStamp(0);
                }
            }
            return null;
        }

        internal override bool checkCoherence(ValidationResult aResult, DateTime aTime, EncapsulatedTimeStamp aTimeStampToCheck, int aTimeStampIndex, XAdESTimeStamp aContainerTimeStamp, XMLSignature aSignature, ECertificate aCertificate)
        {
            bool result = checkTimestampVsSigningCertificate(aTime, aTimeStampIndex, aCertificate, aResult);
            result &= checkTimestampVsDataObjectTimestamps(aTime, aTimeStampIndex, aSignature, aResult);
            result &= checkTimestampVsSigningTime(aTime, aTimeStampIndex, aSignature, aResult);
            return result;
        }

        internal override string TSNodeName
        {
            get
            {
                return Constants.TAGX_SIGNATURETIMESTAMP;
            }
        }
    }

}