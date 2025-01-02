package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertStatus;

public class ECertStatus extends BaseASNWrapper<CertStatus>{

	public ECertStatus(CertStatus aObject){
		super(aObject);
	}

	public ECertStatus(byte[] aBytes) throws ESYAException {
		super(aBytes,new CertStatus());
	}

    public ECertStatus( byte[] certHash, long certReqId, EPKIStatusInfo statusInfo) {
        super(new CertStatus(certHash,certReqId, statusInfo.getObject()));
    }

    public byte[] getCertHash() {
        return mObject.certHash.value;
    }

    public void setCertHash(byte[] certHash) {
        mObject.certHash = new Asn1OctetString(certHash);
    }

    public long getCertReqId() {
        return mObject.certReqId.value;
    }

    public void setCertReqId(long certReqId) {
        mObject.certReqId = new Asn1Integer(certReqId);
    }

    public EPKIStatusInfo getStatusInfo() {
	return (mObject.statusInfo == null ? null : new EPKIStatusInfo(mObject.statusInfo));        
    }

    public void setStatusInfo(EPKIStatusInfo statusInfo) {
        mObject.statusInfo = statusInfo.getObject();
    }

}
