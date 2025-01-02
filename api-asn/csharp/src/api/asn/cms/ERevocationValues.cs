using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ERevocationValues : BaseASNWrapper<RevocationValues>

    {
        public ERevocationValues(RevocationValues aObject)
            : base(aObject)
        {
        }

        public ERevocationValues(byte[] aBytes)
            : base(aBytes, new RevocationValues())
        {
        }
        
        public ERevocationValues(List<ECRL> aCRLs, List<EBasicOCSPResponse> aOCSPResponses)
               :base(new RevocationValues())
        {
            if (aCRLs != null && aCRLs.Count > 0)
            {
                CertificateList[] crlArr = unwrapArray<CertificateList,ECRL>(aCRLs.ToArray());
                mObject.crlVals = new _SeqOfCertificateList(crlArr);
            }
            if (aOCSPResponses != null && aOCSPResponses.Count > 0)
            {
                BasicOCSPResponse[] ocspArr = unwrapArray<BasicOCSPResponse, EBasicOCSPResponse>(aOCSPResponses.ToArray());
                mObject.ocspVals = new _SeqOfBasicOCSPResponse(ocspArr);
            }
        }
        
        public List<ECRL> getCRLs()
        {
            if (mObject.crlVals == null || mObject.crlVals.elements == null)
                return null;

            List<ECRL> crls = new List<ECRL>();
            foreach (CertificateList crl in mObject.crlVals.elements)
            {
                crls.Add(new ECRL(crl));
            }

            return crls;
        }

        public List<EBasicOCSPResponse> getBasicOCSPResponses()
        {
            if (mObject.ocspVals == null || mObject.ocspVals.elements == null)
                return null;

            List<EBasicOCSPResponse> ocsps = new List<EBasicOCSPResponse>();
            foreach (BasicOCSPResponse ocsp in mObject.ocspVals.elements)
            {
                ocsps.Add(new EBasicOCSPResponse(ocsp));
            }

            return ocsps;
        }
        public int getBasicOCSPResponseCount()
        {
            if (mObject.ocspVals == null || mObject.ocspVals.elements == null)
                return 0;
            return mObject.ocspVals.elements.Length;
        }
        public int getCRLCount()
        {
            if (mObject.crlVals == null || mObject.crlVals.elements == null)
                return 0;
            return mObject.crlVals.elements.Length;
        }
    }
}
