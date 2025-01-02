using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation
{
    /**
     * Map structure of revocation check
     */
    public class RevocationInfoMap
    {
        public Dictionary<ECertificate, RevocationInfoItems> mMap = new Dictionary<ECertificate, RevocationInfoItems>();
        public enum RevocationInfoStatus { RIS_VALID, RIS_INVALID };

        public Dictionary<ECertificate, RevocationInfoItems> getMap()
        {
            return mMap;
        }

        public void appendCRL(ECertificate iCert, ECRL iCRL)
        {
            if (!mMap.ContainsKey(iCert))
            {
                mMap.Add(iCert, new RevocationInfoItems());
            }
            mMap[iCert].appendCRL(iCRL);
        }

        public void appendOCSPResponse(ECertificate iCert, EOCSPResponse iOCSPResponse)
        {
            if (!mMap.ContainsKey(iCert))
            {
                mMap.Add(iCert, new RevocationInfoItems());
            }
            mMap[iCert].appendOCSPResponse(iOCSPResponse);
        }

        public bool contains(ECertificate iCert)
        {
            return mMap.ContainsKey(iCert);
        }

        public class RevocationInfoItems
        {
            readonly List<ECRL> mCRLList = new List<ECRL>(0);
            readonly List<EOCSPResponse> mOCSPResponseList = new List<EOCSPResponse>(0);

            public List<ECRL> getCRLList()
            {
                return mCRLList;
            }

            public List<EOCSPResponse> getOCSPResponseList()
            {
                return mOCSPResponseList;
            }

            public void appendCRL(ECRL iCRL)
            {
                mCRLList.Add(iCRL);
            }

            public void appendOCSPResponse(EOCSPResponse iOCSPResponse)
            {
                mOCSPResponseList.Add(iOCSPResponse);
            }
        }
    }
}
