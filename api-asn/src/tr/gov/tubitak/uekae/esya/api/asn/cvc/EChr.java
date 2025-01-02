package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Tag;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.cvc.Chr;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/5/11
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class EChr extends BaseASNWrapper<Chr> {

    public EChr(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new Chr());
    }

    public EChr() {
        super(new Chr());
    }

    public EChr(String aChrStr) throws ESYAException {
        super(new Chr());
        byte[] chr = StringUtil.toByteArray(aChrStr);
        if (chr.length < 12) {
            byte[] paddedChr = new byte[12];
            System.arraycopy(chr, 0, paddedChr, 12 - chr.length, chr.length);
            setChr(paddedChr);
        } else {
            setChr(chr);
        }

    }


    public void setChr(byte[] aChrValue) throws ESYAException {
        if (aChrValue.length != 12)
            throw new ESYAException("Chr field must be 12 bytes. Found: " + aChrValue.length);
        getObject().value = aChrValue;
    }

    public int getLength() {
        return getByteValues().length;
    }

    public byte[] getByteValues() {
        return getObject().value;
    }

    public static EChr fromValue(byte[] aChrValue) throws ESYAException {
        EChr chr = new EChr();
        chr.setChr(aChrValue);
        return chr;
    }

    public byte[] getTagLen() {
        return getTagLen(getLength());
    }

    public static byte[] getTagLen(int aLen) {
        Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
        encodeBuffer.encodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 32, aLen);
        return encodeBuffer.getMsgCopy();
    }
}
