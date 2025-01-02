package tr.gov.tubitak.uekae.esya.api.signature.impl;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampInfo;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;

/**
 * @author ayetgin
 */
public class TimestampInfoImp extends TimestampInfo {

    private TimestampType type;
    private ESignedData signedData;
    private ETSTInfo tstInfo;


    public TimestampInfoImp(TimestampType type, ESignedData signedData, ETSTInfo tstInfo) {
        this.type = type;
        this.signedData = signedData;
        this.tstInfo = tstInfo;
    }

    public TimestampType getType() {
        return type;
    }

    public ESignedData getSignedData() {
        return signedData;
    }

    public ETSTInfo getTSTInfo() {
        return tstInfo;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("TimestampInfo {").append(type).append(" @ ").append(getTSTInfo().getTime().getTime()).append("}");
        return buffer.toString();
    }
}
