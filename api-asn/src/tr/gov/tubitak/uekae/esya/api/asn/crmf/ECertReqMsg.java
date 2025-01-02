package tr.gov.tubitak.uekae.esya.api.asn.crmf;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;

/**
 * User: zeldal.ozdemir
 * Date: 2/1/11
 * Time: 4:34 PM
 */
public class ECertReqMsg extends BaseASNWrapper<CertReqMsg>{

    public ECertReqMsg(ECertRequest certRequest) throws ESYAException {
        super(new CertReqMsg(certRequest.getObject()));
    }

    public ECertReqMsg(CertReqMsg aCertReqMsg) throws ESYAException {
        super(aCertReqMsg);
    }
    
    public ECertReqMsg(byte[] aBytes) throws ESYAException {
        super(aBytes, new CertReqMsg());
    }
    public ECertRequest getCertRequest(){
        if(mObject == null || mObject.certReq == null)
            return null;
        return new ECertRequest(mObject.certReq);
    }

    public EProofOfPossession getProofOfPossession(){
        return new EProofOfPossession( mObject.pop );
    }
}
