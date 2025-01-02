package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.Signature;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: 12/23/11 - 8:57 AM <p>
 * <b>Description</b>: <br>
 */
public class ESignature extends BaseASNWrapper<Signature> {
    public ESignature(Signature aObject) {
        super(aObject);
    }

    public ESignature(byte[] aBytes) throws ESYAException {
        super(aBytes, new Signature());
    }
}
