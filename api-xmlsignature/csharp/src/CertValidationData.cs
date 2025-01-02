using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

	using EOCSPResponse = tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
	using ECRL = tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	//using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	//using TimeStampValidationData = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.TimeStampValidationData;


	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class CertValidationData
	{

        private readonly List<ECertificate> mCertificates = new List<ECertificate>();
        private readonly List<ECRL> mCrls = new List<ECRL>();
        private readonly List<EOCSPResponse> mOcspResponses = new List<EOCSPResponse>();

		private IDictionary<XAdESTimeStamp, TimeStampValidationData> mTSValidationData = new Dictionary<XAdESTimeStamp, TimeStampValidationData>(0);


		public virtual void addCertificate(ECertificate aCertificate)
		{
			mCertificates.Add(aCertificate);
		}

		public virtual void addCRL(ECRL aCRL)
		{
			mCrls.Add(aCRL);
		}

		public virtual void addOCSPResponse(EOCSPResponse aResponse)
		{
			mOcspResponses.Add(aResponse);
		}

		public virtual List<ECertificate> Certificates
		{
			get
			{
				return mCertificates;
			}
		}

        public virtual List<ECRL> Crls
		{
			get
			{
				return mCrls;
			}
		}

        public virtual List<EOCSPResponse> OcspResponses
		{
			get
			{
				return mOcspResponses;
			}
		}

		public virtual IDictionary<XAdESTimeStamp, TimeStampValidationData> TSValidationData
		{
			get
			{
				return mTSValidationData;
			}
			set
			{
				mTSValidationData = value;
			}
		}


		public virtual void addTSValidationData(XAdESTimeStamp aXAdESTimeStamp, TimeStampValidationData aTSValidationData)
		{
			mTSValidationData[aXAdESTimeStamp] = aTSValidationData;
		}


		public virtual TimeStampValidationData getTSValidationDataForTS(XAdESTimeStamp aTimeStamp)
		{
            if(mTSValidationData.ContainsKey(aTimeStamp))
                return mTSValidationData[aTimeStamp];
		    return null;
		}

	}

}