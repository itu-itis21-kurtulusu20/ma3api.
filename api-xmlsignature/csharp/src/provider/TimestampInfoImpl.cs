using System;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.provider
{
    /**
     * @uathor ayetgin
     */
    public class TimestampInfoImpl : TimestampInfo {

        private readonly TimestampType type;
        private readonly EncapsulatedTimeStamp timestamp;

        public TimestampInfoImpl(TimestampType type, EncapsulatedTimeStamp timestamp) {
            this.type = type;
            this.timestamp = timestamp;
        }

        public TimestampType getType() {
            return type;
        }

        public ESignedData getSignedData() {
            try {
                return timestamp.SignedData;
            } catch (Exception x){
                throw new SignatureRuntimeException(x);
            }
        }

        public ETSTInfo getTSTInfo() {
            try  {
                return timestamp.TSTInfo;
            } catch (Exception x){
                throw new SignatureRuntimeException(x);
            }
        }
    }
}
