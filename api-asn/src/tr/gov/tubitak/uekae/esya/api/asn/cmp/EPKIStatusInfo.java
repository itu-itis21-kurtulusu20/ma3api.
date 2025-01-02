package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1UTF8String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFreeText;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatusInfo;

/**
 * User: zeldal.ozdemir
 * Date: 2/2/11
 * Time: 9:15 AM
 */
public class EPKIStatusInfo extends BaseASNWrapper<PKIStatusInfo> {
    Asn1UTF8String[] elements;

    public EPKIStatusInfo(PKIStatusInfo aObject) {
        super(aObject);
    }

    public EPKIStatusInfo(byte[] aBytes) throws ESYAException {
        super(aBytes, new PKIStatusInfo());
    }

    public EPKIStatusInfo(EPKIStatus status) {
        super(new PKIStatusInfo(status.getObject()));
    }

    public EPKIStatusInfo(EPKIStatus status, String statusString, EPKIFailureInfo failInfo) {
        super(new PKIStatusInfo(status.getObject(), new PKIFreeText(), failInfo.getObject()));
        mObject.statusString.elements = new Asn1UTF8String[]{new Asn1UTF8String(statusString)};
    }

    public EPKIStatus getStatus() {
        return new EPKIStatus(mObject.status);
    }

    public String getErrorString(String delim){
        String text = "";
        if(mObject.statusString != null )
            for (int i = 0; i < mObject.statusString.elements.length; i++) {
                Asn1UTF8String element = mObject.statusString.elements[i];
                text += element.value + delim;
            }
      return text;
    }

    public String stringValue(String delim) {
        String text = "PKIFreeText{";
        text += getErrorString(delim);
        text += '}';
        if (mObject.failInfo != null) {
            text += "\n  FailInfo{" + StringUtil.toString(mObject.failInfo.value)+"}";
        }
        text += "\n  StatusInfo{" + mObject.status.value+"}";
        return text;
    }

    public EPKIFailureInfo getFailureInfo() {
        if (mObject.failInfo == null)
            return null;
        else
            return new EPKIFailureInfo(mObject.failInfo);
    }
}
