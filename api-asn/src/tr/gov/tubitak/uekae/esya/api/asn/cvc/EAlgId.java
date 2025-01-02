package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1Tag;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cvc.AlgId;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/5/11
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class EAlgId extends BaseASNWrapper<AlgId> {
    public EAlgId(byte[] aEncoded) throws ESYAException {
        super(aEncoded, new AlgId());
    }

    public EAlgId() {
        super(new AlgId());
    }

    public void setAlgId(byte[] aAldIdValue) {
        getObject().value = aAldIdValue;
    }

    public byte[] getByteValues() {
        return getObject().value;
    }

    public int getLength() {
        return getByteValues().length;
    }

    public static EAlgId fromValue(byte[] aAlgIdValue) {
        EAlgId algId = new EAlgId();
        algId.setAlgId(aAlgIdValue);
        return algId;
    }

    public byte[] getTagLen() {
       return getTagLen(getLength());
    }

    public static byte[] getTagLen(int aLen) {
        Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
        encodeBuffer.encodeTagAndLength(Asn1Tag.UNIV, Asn1Tag.PRIM, 6, aLen);
        return encodeBuffer.getMsgCopy();
    }

    public int[] toIntArray() throws ESYAException {
        Asn1ObjectIdentifier objectIdentifier = new Asn1ObjectIdentifier();
        Asn1BerDecodeBuffer decodeBuffer = new Asn1BerDecodeBuffer(getEncoded());
        try {
            objectIdentifier.decode(decodeBuffer);
        } catch (Exception e) {
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new ESYAException("Decode Error!", e);
        }
        return objectIdentifier.value;
    }
}
