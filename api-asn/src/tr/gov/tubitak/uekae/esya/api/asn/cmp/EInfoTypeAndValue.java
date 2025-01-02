package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OpenType;
import com.objsys.asn1j.runtime.Asn1Type;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.InfoTypeAndValue;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2013 <br>
 * <b>Date</b>: 5/20/13 - 9:58 AM <p>
 * <b>Description</b>: <br>
 */
public class EInfoTypeAndValue extends BaseASNWrapper<InfoTypeAndValue> {

    public EInfoTypeAndValue(InfoTypeAndValue aObject) {
        super(aObject);
    }

    public EInfoTypeAndValue(byte[] aBytes) throws ESYAException {
        super(aBytes, new InfoTypeAndValue());
    }

    public EInfoTypeAndValue(Asn1ObjectIdentifier infoOid, Asn1Type infoValue) throws ESYAException {
        super(new InfoTypeAndValue(infoOid));
        try {
            Asn1DerEncodeBuffer enbuf = new Asn1DerEncodeBuffer();
            infoValue.encode(enbuf);
            this.mObject.infoValue = new Asn1OpenType(enbuf.getMsgCopy());
        } catch (Asn1Exception e) {
            throw new ESYAException("Invalid Asn1Type for InfoValue:" + e.getMessage(), e);
        }
    }

    public <T extends Asn1Type> T getGeneralInfo(Class<T> aClass) throws ESYAException {
        if (mObject == null)
            return null;
        try {
            Asn1Type asn1Type = aClass.getConstructor().newInstance();
            asn1Type = AsnIO.arraydenOku(asn1Type, mObject.infoValue.value);
            return (T) asn1Type;
        } catch (Exception e) {
            throw new ESYAException("Cannot build Asn1Type for " + aClass, e);
        }
    }
}
