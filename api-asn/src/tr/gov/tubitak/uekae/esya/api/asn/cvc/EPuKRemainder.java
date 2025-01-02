package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.PuKRemainder;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/5/11
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class EPuKRemainder extends BaseASNWrapper<PuKRemainder> {
    public  EPuKRemainder(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new PuKRemainder());
    }

    public EPuKRemainder()
    {
        super(new PuKRemainder());
    }

    public void setPuKRemainder(byte[] aPukRemainderValue)
    {
        getObject().value = aPukRemainderValue;
    }

}
