package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Tag;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.Cpi;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/5/11
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ECpi extends BaseASNWrapper<Cpi> {
    public ECpi(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new Cpi());
    }

    public ECpi(byte aCpiValue) {
        super(new Cpi());
        setCpi(aCpiValue);

    }

    public void setCpi(byte aCpiValue) {
        getObject().value = new byte[]{aCpiValue};
    }

    public int getLength() {
        return getByteValues().length;
    }

    public byte[] getByteValues() {
        return getObject().value;
    }

    public byte[] getTagLen() {
       return getTagLen(getLength());
    }

    public static byte[] getTagLen(int aLen) {
        Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
        encodeBuffer.encodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 41, aLen);
        return encodeBuffer.getMsgCopy();
    }


}
