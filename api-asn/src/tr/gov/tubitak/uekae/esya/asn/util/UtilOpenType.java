package tr.gov.tubitak.uekae.esya.asn.util;

import java.io.IOException;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1OpenType;
import com.objsys.asn1j.runtime.Asn1Type;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;


public class UtilOpenType {
    public static final Asn1OpenType Asn1NULL = new Asn1OpenType(new byte[]{5, 0});

    public UtilOpenType() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static synchronized Asn1OpenType toOpenType(BaseASNWrapper asnWrapper)
            throws IOException, Asn1Exception {
        return toOpenType(asnWrapper.getObject());
    }

    public static synchronized Asn1OpenType toOpenType(Asn1Type aVal)
            throws IOException, Asn1Exception {
        Asn1OpenType openT = null;
        Asn1DerEncodeBuffer enbuf = new Asn1DerEncodeBuffer();
        aVal.encode(enbuf);
        openT = new Asn1OpenType();

        openT = (Asn1OpenType) AsnIO.derOku(openT,new Asn1DerDecodeBuffer(enbuf.getInputStream()));

        return openT;
    }

    public static synchronized void fromOpenType(Asn1OpenType aOpenType, Asn1Type aToType)
            throws IOException, Asn1Exception {
        if (aToType == null)
            throw new IOException("null");
        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aOpenType.value);
        aToType = AsnIO.derOku(aToType, decBuf);
    }

}
