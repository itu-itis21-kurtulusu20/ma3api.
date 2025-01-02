package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.NonSelfDescCVCwithHeader;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/24/11
 * Time: 10:22 AM
 */
public class ENonSelfDescCVCwithHeader extends BaseASNWrapper<NonSelfDescCVCwithHeader>{
    public ENonSelfDescCVCwithHeader(NonSelfDescCVCwithHeader aObject) {
        super(aObject);
    }

    public ENonSelfDescCVCwithHeader(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new NonSelfDescCVCwithHeader());
    }

    public ENonSelfDescCVCwithHeader(ENonSelfDescCVC aCvc, EHeaderList aHeaderList)
    {
        super(new NonSelfDescCVCwithHeader());
        setCVC(aCvc);
        setHeaderList(aHeaderList);
    }

    public void setCVC(ENonSelfDescCVC aCVC) {
        //getObject().signature = new Asn1OctetString(aSignature);
        getObject().cvc = aCVC.getObject();
    }

    public void setHeaderList(EHeaderList aHeaderList) {
        //getObject().puKRemainder = new Asn1OctetString(aPuKRemainder);
        getObject().header = new Asn1OctetString(aHeaderList.getObject().value);
    }

    public ENonSelfDescCVC getNonSelfDescCVC(){
        return new ENonSelfDescCVC(getObject().cvc);
    }

    public EHeaderList getHeaderList() throws ESYAException {
        return EHeaderList.fromValue(getObject().header.value);
    }
}
