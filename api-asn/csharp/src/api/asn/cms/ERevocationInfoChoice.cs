using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ERevocationInfoChoice : BaseASNWrapper<RevocationInfoChoice>
    {
        public ERevocationInfoChoice(RevocationInfoChoice aRevocationInfoChoice)
            : base(aRevocationInfoChoice)
        {
        }

        public ERevocationInfoChoice(ECRL aCRL)
            : base(new RevocationInfoChoice())
        {
            setCRL(aCRL);
        }
        public ERevocationInfoChoice(EOCSPResponse aOCSP)
            : base(new RevocationInfoChoice())
        {
            setOCSP(aOCSP);
        }
        public int getType()
        {
            return mObject.ChoiceID;
        }

        public void setCRL(ECRL aCRL)
        {
            mObject.SetElement(RevocationInfoChoice._CRL, aCRL.getObject());
        }
        //TODO basicOCSPResponse?
        public void setOCSP(EOCSPResponse aOCSP)
        {
            OtherRevocationInfoFormat other = new OtherRevocationInfoFormat(_cmsValues.id_ri_ocsp_response, new Asn1OpenType(aOCSP.getEncoded()));
            mObject.SetElement(RevocationInfoChoice._OTHER, other);
        }
        public void setOtherRevocationInfo(OtherRevocationInfoFormat aOther)
        {
            mObject.SetElement(RevocationInfoChoice._OTHER, aOther);
        }

        public ECRL getCRL()
        {
            if (mObject.ChoiceID == RevocationInfoChoice._CRL)
                return new ECRL((CertificateList)mObject.GetElement());

            return null;
        }


        public OtherRevocationInfoFormat getOtherRevocationInfo()
        {
            if (mObject.ChoiceID == RevocationInfoChoice._OTHER)
                return (OtherRevocationInfoFormat)mObject.GetElement();

            return null;
        }

    }
}
