using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ERevocationInfoChoices : BaseASNWrapper<RevocationInfoChoices>
    {
        public ERevocationInfoChoices(RevocationInfoChoices aObject)
            : base(aObject)
        {
        }

        public int getRevocationInfoChoiceCount()
        {
            if (mObject.elements == null)
                return 0;

            return mObject.elements.Length;
        }

        public ERevocationInfoChoice getRevocationInfoChoice(int aIndex)
        {
            if (mObject.elements == null)
                return null;

            return new ERevocationInfoChoice(mObject.elements[aIndex]);
        }

        public void addRevocationInfoChoice(ERevocationInfoChoice aRevocationInfoChoice)
        {
            mObject.elements = extendArray(mObject.elements, aRevocationInfoChoice.getObject());
        }


        public List<ECRL> getCRLs()
        {
            List<ECRL> crls = new List<ECRL>();
            if (mObject.elements == null)
                return crls;

            for (int i = 0; i < mObject.elements.Length; i++)
            {
                RevocationInfoChoice ric = mObject.elements[i];
                if (ric.ChoiceID == RevocationInfoChoice._CRL)
                    crls.Add(new ECRL((CertificateList)ric.GetElement()));
            }

            return crls;
        }
        public List<EOCSPResponse> getOSCPResponses()
        {
            List<EOCSPResponse> ocsps = new List<EOCSPResponse>();
            if (mObject.elements == null)
                return ocsps;

            try
            {
                for (int i = 0; i < mObject.elements.Length; i++)
                {
                    RevocationInfoChoice ric = mObject.elements[i];
                    if (ric.ChoiceID == RevocationInfoChoice._OTHER)
                    {
                        OtherRevocationInfoFormat format = (OtherRevocationInfoFormat)ric.GetElement();
                        EOCSPResponse ocsp = null;
                        if (format.otherRevInfoFormat.Equals(new Asn1ObjectIdentifier(_cmsValues.id_ri_ocsp_response)))
                        {
                            ocsp = new EOCSPResponse(format.otherRevInfo.mValue);
                        }
                        else if (format.otherRevInfoFormat.Equals(new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_basic)))
                        {
                            EBasicOCSPResponse basicOcsp = new EBasicOCSPResponse(format.otherRevInfo.mValue);
                            ocsp = EOCSPResponse.getEOCSPResponse(basicOcsp);
                        }
                        if (ocsp != null)
                            ocsps.Add(ocsp);
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return new List<EOCSPResponse>();
            }
            return ocsps;
        }
    }
}
