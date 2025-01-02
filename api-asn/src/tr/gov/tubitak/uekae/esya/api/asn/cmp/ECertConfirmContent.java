package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertConfirmContent;

/**
 * User: zeldal.ozdemir
 * Date: 2/4/11
 * Time: 3:46 PM
 */
public class ECertConfirmContent extends BaseASNWrapper<CertConfirmContent>{
    public ECertConfirmContent(CertConfirmContent aObject) {
        super(aObject);
    }

    public ECertConfirmContent(byte[] aBytes) throws ESYAException {
        super(aBytes, new CertConfirmContent());
    }

    public ECertConfirmContent(ECertStatus[] certStatuses) {
        super(new CertConfirmContent());
        mObject.elements = BaseASNWrapper.unwrapArray(certStatuses);
    }

    public ECertStatus[] getCertStatuses(){
        return BaseASNWrapper.wrapArray(mObject.elements,ECertStatus.class);
    }


}
