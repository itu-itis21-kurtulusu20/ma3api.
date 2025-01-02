package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.Extension;

/**
 * @author ahmety
 *         date: Jan 29, 2010
 */
public class EExtension extends BaseASNWrapper<Extension> {

    public EExtension(byte[] aBytes) throws ESYAException {
        super(aBytes, new Extension());
    }

    public EExtension(Extension aObject) {
        super(aObject);
    }

    public EExtension(Asn1ObjectIdentifier aExtID, boolean aCritic, BaseASNWrapper aValue) throws ESYAException {
        super(new Extension(aExtID.value, aCritic, aValue.getEncoded()));
    }

    public EExtension(Asn1ObjectIdentifier aExtID, boolean aCritic, Asn1Type asn1Type) throws ESYAException {
        super(new Extension(aExtID.value, aCritic, null));
        Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();
        try {
            asn1Type.encode(safeEncBuf);
        } catch (Exception ex) {
            throw new ESYAException("Extension islenirken hata: ExtID:" + aExtID, ex);
        }
        mObject.extnValue = new Asn1OctetString(safeEncBuf.getMsgCopy());
    }

    public Asn1ObjectIdentifier getIdentifier() {
        return mObject.extnID;
    }

    public byte[] getValue() {
        return mObject.extnValue.value;
    }

    public Asn1DerDecodeBuffer getValueAsDecodeBuffer() {
        return new Asn1DerDecodeBuffer(mObject.extnValue.value);
    }

    public boolean isCritical() {
        return mObject.critical.value;
    }
}
