package tr.gov.tubitak.uekae.esya.api.asn.crmf;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertRequest;

/**
 * User: zeldal.ozdemir
 * Date: 2/2/11
 * Time: 11:29 AM
 */
public class ECertRequest extends BaseASNWrapper<CertRequest>{
    public ECertRequest(CertRequest aObject) {
        super(aObject);
    }

    public ECertRequest(byte[] aBytes) throws ESYAException {
        super(aBytes, new CertRequest());
    }

    public ECertRequest(long certReqId, ECertTemplate certTemplate) throws ESYAException {
        super(new CertRequest(certReqId, certTemplate.getObject()));
    }

    public long getCertReqId(){
        return mObject.certReqId.value;
    }

    public ECertTemplate getCertTemplate(){
        return new ECertTemplate(mObject.certTemplate);
    }

    public EControls getControls(){
        return new EControls(mObject.controls);
    }
}
