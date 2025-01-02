package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.NonSelfDescCVC;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/5/11
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class ENonSelfDescCVC extends BaseASNWrapper<NonSelfDescCVC> {

    public ENonSelfDescCVC(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new NonSelfDescCVC());
    }

    public ENonSelfDescCVC(NonSelfDescCVC aObject) {
        super(aObject);
    }

    public ENonSelfDescCVC(byte[] aSignature, byte[] aPuKRemainder, byte[] aCar) {
        super(new NonSelfDescCVC());
        setSignature(aSignature);
        setPuKRemainder(aPuKRemainder);
        setCar(aCar);
    }

    public ENonSelfDescCVC(byte[] aSignature, byte[] aCar) {
        super(new NonSelfDescCVC());
        setSignature(aSignature);
        setCar(aCar);
    }

    public void setSignature(byte[] aSignature) {
        getObject().signature = new Asn1OctetString(aSignature);
    }

    public void setPuKRemainder(byte[] aPuKRemainder) {
        getObject().puKRemainder = new Asn1OctetString(aPuKRemainder);
    }

    public void setCar(byte[] aCar) {
        getObject().car = new Asn1OctetString(aCar);
    }

    public byte[] getSignature() {
        return getObject().signature.value;
    }

    public byte[] getPuKRemainder() {
        if (getObject().puKRemainder != null)
            return getObject().puKRemainder.value;
        return null;
    }

    public byte[] getCar() {
        return getObject().car.value;
    }
}
