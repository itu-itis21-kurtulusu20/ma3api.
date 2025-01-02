using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using ValidationResultType = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
	using SignedDataObjectProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedDataObjectProperties;
	using SignedProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedProperties;
	using SignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedSignatureProperties;
	using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
	using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;




	/// <summary>
	/// The time-stamp token contained within <code>AllDataObjectsTimeStamp</code>
	/// property does not cover any unsigned property and the regular elements
	/// within the signature that are mandated to be time-stamped are easily
	/// determined by inspecting the <code>ds:SignedInfo</code>contents. That is why
	/// this container will exclusively use the implicit mechanism.
	/// </summary>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.AllDataObjectsTimeStamp
	/// 
	/// @author ahmety
	/// date: Oct 8, 2009 </seealso>
	public class AllDataObjectsTimeStampValidator : BaseTimeStampValidator
	{

		internal override List<XAdESTimeStamp> getTimeStampNodes(XMLSignature aSignature)
		{
			QualifyingProperties qp = aSignature.QualifyingProperties;
			if (qp == null)
			{
				return null;
			}
			SignedDataObjectProperties sdop = qp.SignedDataObjectProperties;
			if (sdop == null)
			{
				return null;
			}

			int tsCount = sdop.AllDataObjectsTimeStampCount;
			if (tsCount > 0)
			{
				List<XAdESTimeStamp> tsList = new List<XAdESTimeStamp>(tsCount);
				for (int i = 0; i < tsCount; i++)
				{
					tsList.Add(sdop.getAllDataObjectsTimeStamp(i));
				}
				return tsList;
			}
			return null;
		}
          
        override internal XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature)
        {
            UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
            if (usp!=null){
                if (usp.SigAndRefsTimeStampCount> 0)
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
			result &= checkTimestampVsSigningTime(aTime, aTimeStampIndex, aSignature, aResult);
			return result;
		}

		internal override string TSNodeName
		{
			get
			{
				return Constants.TAGX_ALLDATAOBJECTSTIMESTAMP;
			}
		}
	}

}