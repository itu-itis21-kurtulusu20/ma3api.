using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;

namespace tr.gov.tubitak.uekae.esya.api.signature.impl
{
    public class TimestampInfoImp : TimestampInfo
    {
        private readonly TimestampType type;
        private readonly ESignedData signedData;
        private readonly ETSTInfo tstInfo;


        public TimestampInfoImp(TimestampType type, ESignedData signedData, ETSTInfo tstInfo)
        {
            this.type = type;
            this.signedData = signedData;
            this.tstInfo = tstInfo;
        }

        public TimestampType getType()
        {
            return type;
        }

        public ESignedData getSignedData()
        {
            return signedData;
        }

        public ETSTInfo getTSTInfo()
        {
            return tstInfo;
        }
    }
}
