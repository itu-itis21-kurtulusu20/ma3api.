package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1ValueParseException;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertResponse;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatus;

/**
 * User: zeldal.ozdemir
 * Date: 2/1/11
 * Time: 4:36 PM
 */
public class ECertResponse extends BaseASNWrapper<CertResponse> {

    public ECertResponse(byte[] aBytes) throws ESYAException {
        super(aBytes, new CertResponse());
    }

    public ECertResponse(CertResponse aObject) {
        super(aObject);
    }

    public ECertResponse(long certReqId, EPKIStatusInfo status,
                         ECertifiedKeyPair certifiedKeyPair) {
        this(certReqId, status);
        mObject.certifiedKeyPair = certifiedKeyPair.getObject();
    }

    public ECertResponse(long certReqId, EPKIStatusInfo status) {
        super(new CertResponse());
        mObject.certReqId = new Asn1Integer(certReqId);
        mObject.status = status.getObject();
    }

    public ECertResponse(long certReqId, EPKIStatusInfo status,
                         ECertifiedKeyPair certifiedKeyPair, String rspInfo) throws Asn1ValueParseException {
        this(certReqId, status, certifiedKeyPair);
        mObject.rspInfo = new Asn1OctetString(rspInfo);
    }

    public ECertifiedKeyPair getCertifiedKeyPair(){
        if(mObject.certifiedKeyPair == null)
            return null;
        return new ECertifiedKeyPair(mObject.certifiedKeyPair);
    }

    public boolean isAccepted() {
        if(mObject == null){
            return false;
        }
        return  mObject.status.status.value == PKIStatus.accepted ||
                mObject.status.status.value == PKIStatus.grantedWithMods;
    }

    public long getCertReqID(){
        return mObject.certReqId.value;
    }

    public EPKIStatusInfo getPkiStatusInfo(){
        return new EPKIStatusInfo(mObject.status);
    }
}
