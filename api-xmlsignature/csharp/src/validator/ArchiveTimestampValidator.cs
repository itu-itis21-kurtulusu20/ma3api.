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
	using UnsignedProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedProperties;
	using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
	using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
	using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	using ArchiveTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.ArchiveTimeStamp;


	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class ArchiveTimestampValidator : BaseTimeStampValidator
	{

//JAVA TO C# CONVERTER TODO TASK: Java wildcard generics are not converted to .NET:
//ORIGINAL LINE: java.util.List<? extends tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp> getTimeStampNodes(tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature)
//JAVA TO C# CONVERTER TODO TASK: Java wildcard generics are not converted to .NET:
//ORIGINAL LINE: java.util.List<? extends tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp> getTimeStampNodes(tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature)
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
			int atsCount = usp.ArchiveTimeStampCount;
			if (atsCount == 0)
			{
				return null;
			}
            List<XAdESTimeStamp> list = new List<XAdESTimeStamp>(atsCount);
			for (int i = 0; i < atsCount; i++)
			{
				list.Add(usp.getArchiveTimeStamp(i));
			}
			return list;
		}

    internal override  XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature)
    {
        UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
        if (usp!=null && usp.ArchiveTimeStampCount > 0)
            {  
                for (int i = 0; i < usp.ArchiveTimeStampCount; i++)
                {
                    ArchiveTimeStamp current = usp.getArchiveTimeStamp(i);
                    if (current.Equals(timestamp) && i < usp.ArchiveTimeStampCount - 1)
                    { // here we stop
                      return usp.getArchiveTimeStamp(i+1);                        
                    }
                }            
        }
        return null;
    }

		internal override bool checkCoherence(ValidationResult aResult, DateTime aTime, EncapsulatedTimeStamp aTimeStampToCheck, int aTimeStampIndex, XAdESTimeStamp aContainerTimeStamp, XMLSignature aSignature, ECertificate aCertificate)
		{
			bool result = checkTimestampVsSigningTime(aTime, aTimeStampIndex, aSignature, aResult);
			result &= checkTimestampVsDataObjectTimestamps(aTime, aTimeStampIndex, aSignature, aResult);
			result &= checkTimestampVsSignatureTimestamp(aTime, aTimeStampIndex, aSignature, aResult);
			result &= checkTimestampVsRefTimestamps(aTime, aTimeStampIndex, aSignature, aResult);

			// check versus Previous Archive Timestamps
			UnsignedSignatureProperties usp = aSignature.QualifyingProperties.UnsignedSignatureProperties;
			for (int i = 0; i < usp.ArchiveTimeStampCount; i++)
			{
				ArchiveTimeStamp current = usp.getArchiveTimeStamp(i);
				if (current.Equals(aContainerTimeStamp)) // here we stop
				{
					break;
				}
				for (int j = 0; j < current.EncapsulatedTimeStampCount;j++)
				{
					EncapsulatedTimeStamp ets = current.getEncapsulatedTimeStamp(j);
					result &= checkTimestampVsPreviousTimestamp(aTime, aTimeStampIndex, ets, current, aResult);
				}
			}

			return result;
		}

		internal override string TSNodeName
		{
			get
			{
				return Constants.TAGX_ARCHIVETIMESTAMP;
			}
		}

	}

}