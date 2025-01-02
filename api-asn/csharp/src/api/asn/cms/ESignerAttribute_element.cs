using tr.gov.tubitak.uekae.esya.api.asn.attrcert;
using tr.gov.tubitak.uekae.esya.asn.attrcert;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESignerAttribute_element : BaseASNWrapper<SignerAttribute_element>
    {
        public ESignerAttribute_element(byte[] aBytes)
            : base(aBytes, new SignerAttribute_element())
        {
        }

        public ESignerAttribute_element(SignerAttribute_element aObject)
            : base(aObject)
        {
        }

        public EClaimedAttributes getClaimedAttributes()
        {
            if (mObject.ChoiceID == SignerAttribute_element._CLAIMEDATTRIBUTES)
                return new EClaimedAttributes((ClaimedAttributes)mObject.GetElement());

            return null;
        }

        public EAttributeCertificate getAttributeCertificate()
        {
            if (mObject.ChoiceID == SignerAttribute_element._CERTIFIEDATTRIBUTES)
                return new EAttributeCertificate((AttributeCertificate)mObject.GetElement());

            return null;
        }
    }
}
