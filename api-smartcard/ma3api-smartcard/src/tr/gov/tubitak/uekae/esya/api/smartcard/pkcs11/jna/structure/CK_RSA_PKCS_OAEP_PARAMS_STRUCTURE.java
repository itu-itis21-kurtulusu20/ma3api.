package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import sun.security.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS;

import java.util.ArrayList;
import java.util.List;

public class CK_RSA_PKCS_OAEP_PARAMS_STRUCTURE extends CK_PARAMS_STRUCTURE {

    public NativeLong hashAlg = new NativeLong(0, true);
    public NativeLong mgf = new NativeLong(0, true);
    public NativeLong source = new NativeLong(0, true);
    public Pointer pSourceData = Pointer.NULL;
    public NativeLong ulSourceDataLen = new NativeLong(0, true);

    public CK_RSA_PKCS_OAEP_PARAMS_STRUCTURE(CK_RSA_PKCS_OAEP_PARAMS params) {
        super();

        this.hashAlg = new NativeLong(params.hashAlg, true);
        this.mgf = new NativeLong(params.mgf, true);
        this.source = new NativeLong(params.source, true);

        byte[] pSourceData = params.pSourceData;
        int pSourceDataLen;
        if (pSourceData == null) {
            pSourceDataLen = 0;
            this.pSourceData = Pointer.NULL;
        } else {
            pSourceDataLen = pSourceData.length;
            this.pSourceData = new Memory(pSourceDataLen);
            this.pSourceData.write(0L, pSourceData, 0, pSourceDataLen);
        }

        this.ulSourceDataLen = new NativeLong(NativeLong.SIZE * 4L + pSourceDataLen, true);

        write();
    }

    @Override
    protected List<String> getFieldOrder() {
        List<String> fieldOrder = new ArrayList<>();

        fieldOrder.add("hashAlg");
        fieldOrder.add("mgf");
        fieldOrder.add("source");
        fieldOrder.add("pSourceData");
        fieldOrder.add("ulSourceDataLen");

        return fieldOrder;
    }
}
