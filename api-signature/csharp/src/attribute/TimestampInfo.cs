using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;

namespace tr.gov.tubitak.uekae.esya.api.signature.attribute
{
    public interface TimestampInfo
    {
        TimestampType getType();
        ESignedData getSignedData();
        ETSTInfo getTSTInfo();
    }
}
