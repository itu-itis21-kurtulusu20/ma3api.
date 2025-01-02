package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import java.util.ArrayList;
import java.util.List;

public class CK_EDDSA_PARAMS_STRUCTURE extends CK_PARAMS_STRUCTURE {

    public byte phFlag = (byte) 0;
    public Pointer pContextData = Pointer.NULL;
    public NativeLong ulContextDataLen = new NativeLong(0L, true);

    public CK_EDDSA_PARAMS_STRUCTURE(
            boolean phFlag,
            byte[] pContextData,
            long ulContextDataLen
    ) {
        super();

        this.phFlag = (byte) (phFlag ? 1 : 0);
        if (pContextData != null) {
            this.pContextData = new Memory(pContextData.length);
            this.pContextData.write(0L, pContextData, 0, pContextData.length);
        } else {
            this.pContextData = Pointer.NULL;
        }
        this.ulContextDataLen = new NativeLong(ulContextDataLen);

        write();
    }

    public CK_EDDSA_PARAMS_STRUCTURE(
            boolean phFlag,
            byte[] pContextData
    ) {
        new CK_EDDSA_PARAMS_STRUCTURE(phFlag, pContextData, pContextData.length);
    }

    @Override
    protected List<String> getFieldOrder() {
        List<String> fieldOrder = new ArrayList<>();

        fieldOrder.add("phFlag");
        fieldOrder.add("pContextData");
        fieldOrder.add("ulContextDataLen");

        return fieldOrder;
    }
}
