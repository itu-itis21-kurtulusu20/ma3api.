using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.attrcert;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ECertificateChoices : BaseASNWrapper<CertificateChoices>
    {
        public ECertificateChoices(CertificateChoices aObject)
            : base(aObject)
        {
        }

        public ECertificateChoices(byte[] aBytes)
            : base(aBytes, new CertificateChoices())
        {
        }

        public ECertificateChoices(ECertificate aCertificate)
            : base(new CertificateChoices())
        {

            setCertificate(aCertificate);
        }

        public int getType()
        {
            return mObject.ChoiceID;
        }

        public void setCertificate(ECertificate aValue)
        {
            mObject.SetElement(CertificateChoices._CERTIFICATE, aValue.getObject());
        }

        public void setExtendedCertificate(ExtendedCertificate aValue)
        {
            mObject.SetElement(CertificateChoices._EXTENDEDCERTIFICATE, aValue);
        }

        public void setAttributeCertificateV1(AttributeCertificateV1 aValue)
        {
            mObject.SetElement(CertificateChoices._V1ATTRCERT, aValue);
        }

        public void setAttributeCertificateV2(AttributeCertificate aValue)
        {
            mObject.SetElement(CertificateChoices._V2ATTRCERT, aValue);
        }

        public void setOtherCertificateFormat(OtherCertificateFormat aValue)
        {
            mObject.SetElement(CertificateChoices._OTHER, aValue);
        }

        public ECertificate getCertificate()
        {
            if (mObject.ChoiceID == CertificateChoices._CERTIFICATE)
                return new ECertificate((Certificate)mObject.GetElement());

            return null;
        }

        public ExtendedCertificate getExtendedCertificate()
        {
            if (mObject.ChoiceID == CertificateChoices._EXTENDEDCERTIFICATE)
                return (ExtendedCertificate)mObject.GetElement();

            return null;
        }

        public AttributeCertificateV1 getAttributeCertificateV1()
        {
            if (mObject.ChoiceID == CertificateChoices._V1ATTRCERT)
                return (AttributeCertificateV1)mObject.GetElement();

            return null;
        }

        public AttributeCertificate getAttributeCertificateV2()
        {
            if (mObject.ChoiceID == CertificateChoices._V2ATTRCERT)
                return (AttributeCertificate)mObject.GetElement();

            return null;
        }


        public OtherCertificateFormat getOtherCertificateFormat()
        {
            if (mObject.ChoiceID == CertificateChoices._OTHER)
                return (OtherCertificateFormat)mObject.GetElement();

            return null;
        }
    }
}
