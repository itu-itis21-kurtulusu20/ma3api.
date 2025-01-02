package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.SelfDescCVC;

/**
 * <b>Author</b>    : bilen.ogretmen <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: 5/13/11 - 10:31 AM <p>
 * <b>Description</b>: <br>
 */
public class ESelfDescCVC extends BaseASNWrapper<SelfDescCVC>{

    public ESelfDescCVC(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new SelfDescCVC());
    }

    public ESelfDescCVC(SelfDescCVC aObject) {
        super(aObject);
    }
}
