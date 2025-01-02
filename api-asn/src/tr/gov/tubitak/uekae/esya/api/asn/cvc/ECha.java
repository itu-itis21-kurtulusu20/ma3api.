package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Tag;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.Cha;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/5/11
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class ECha extends BaseASNWrapper<Cha> {

    public ECha(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new Cha());
    }

    public ECha() {
        super(new Cha());
    }

    public void setCha(byte[] aChaValue) throws ESYAException {
//        if (aChaValue.length != 7)
//            throw new ESYAException("Cha field must be 7 bytes. Found: " + aChaValue.length);
        getObject().value = aChaValue;
    }

    public int getLength() {
        return getByteValues().length;
    }

    public byte[] getByteValues() {
        return getObject().value;
    }

    public static ECha fromValue(byte[] aChaValue) throws ESYAException {
        ECha cha = new ECha();
        cha.setCha(aChaValue);
        return cha;
    }

    public byte[] getTagLen() {
        return getTagLen(getLength());
    }

    public static byte[] getTagLen(int aLen) {
        Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
        encodeBuffer.encodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 76, aLen);
        return encodeBuffer.getMsgCopy();
    }
}
