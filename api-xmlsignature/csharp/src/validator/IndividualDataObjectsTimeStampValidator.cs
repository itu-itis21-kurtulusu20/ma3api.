using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
	using SignedDataObjectProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedDataObjectProperties;
	using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
	using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;



	/// <summary>
	/// The time-stamp token contained within IndividualDataObjectsTimeStamp
	/// property does not cover any unsigned property, but now there is need for
	/// explicit information of what of the signed data-objects are actually
	/// time-stamped. The consequences is that it will exclusively use the explicit
	/// mechanism.
	/// 
	/// @author ahmety
	/// date: Oct 9, 2009
	/// </summary>
	public class IndividualDataObjectsTimeStampValidator : BaseTimeStampValidator
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

      internal  override XAdESTimeStamp getNextTimeStamp(XAdESTimeStamp timestamp, XMLSignature signature)
    {
        UnsignedSignatureProperties usp = signature.QualifyingProperties.UnsignedSignatureProperties;
        if (usp!=null){
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

			int tsCount = sdop.IndividualDataObjectsTimeStampCount;
			if (tsCount > 0)
			{
				List<XAdESTimeStamp> tsList = new List<XAdESTimeStamp>(tsCount);
				for (int i = 0; i < tsCount; i++)
				{
					tsList.Add(sdop.getIndividualDataObjectsTimeStamp(i));
				}
				return tsList;
			}
			return null;
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: boolean checkCoherence(tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult aResult, java.util.Calendar aTime, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp aTimeStampToCheck, int aTimeStampIndex, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp aContainerTimeStamp, tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature aSignature, tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate aCertificate) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
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
				return "IndividualDataObjectsTimeStamp";
			}
		}
	}

}