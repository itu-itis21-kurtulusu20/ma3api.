package tr.gov.tubitak.uekae.esya.api.xmlsignature.provider;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampInfo;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;

/**
 * @author ayetgin
 */
public class TimestampInfoImpl extends TimestampInfo {

    private TimestampType type;
    private EncapsulatedTimeStamp timestamp;

    public TimestampInfoImpl(TimestampType type, EncapsulatedTimeStamp timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }

    public TimestampType getType() {
        return type;
    }

    public ESignedData getSignedData() {
        try {
            return timestamp.getSignedData();
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }

    public ETSTInfo getTSTInfo() {
        try  {
            return timestamp.getTSTInfo();
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("TimestampInfo {").append(type).append(" @ ").append(getTSTInfo().getTime().getTime()).append("}");
        return buffer.toString();
    }
}
