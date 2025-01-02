package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS;
import sun.security.pkcs11.wrapper.CK_RSA_PKCS_PSS_PARAMS;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.util.ArrayList;
import java.util.List;

public class CK_MECHANISM_STRUCTURE extends Structure {

    public NativeLong mechanism = new NativeLong(0L, true);
    public Pointer pParameter = Pointer.NULL;
    public NativeLong ulParameterLen = new NativeLong(0L, true);

    public CK_MECHANISM_STRUCTURE() {
        setAlignType(ALIGN_NONE);
    }

    public CK_MECHANISM_STRUCTURE(long mech, CK_PARAMS_STRUCTURE paramsStructure) {
        this();
        this.mechanism = new NativeLong(mech, true);
        this.pParameter = paramsStructure.getPointer();
        this.ulParameterLen = new NativeLong(paramsStructure.size(), true);

        write();
    }

    public CK_MECHANISM_STRUCTURE(CK_MECHANISM ckMechanism) throws SmartCardException {
        this();

        this.mechanism = new NativeLong(ckMechanism.mechanism, true);

        if (ckMechanism.pParameter == null) {
            this.pParameter = Pointer.NULL;
            this.ulParameterLen = new NativeLong(0L, true);
        } else if (ckMechanism.pParameter instanceof CK_RSA_PKCS_PSS_PARAMS) {
            CK_RSA_PKCS_PSS_PARAMS_STRUCTURE pssParams = new CK_RSA_PKCS_PSS_PARAMS_STRUCTURE((CK_RSA_PKCS_PSS_PARAMS) ckMechanism.pParameter);
            this.pParameter = pssParams.getPointer();
            this.ulParameterLen = new NativeLong(pssParams.size(), true);
        } else if (ckMechanism.pParameter instanceof CK_RSA_PKCS_OAEP_PARAMS) {
            CK_RSA_PKCS_OAEP_PARAMS_STRUCTURE oaepParams = new CK_RSA_PKCS_OAEP_PARAMS_STRUCTURE((CK_RSA_PKCS_OAEP_PARAMS) ckMechanism.pParameter);
            this.pParameter = oaepParams.getPointer();
            this.ulParameterLen = new NativeLong(oaepParams.size(), true);
        } else if (ckMechanism.pParameter instanceof byte[]) {
            byte[] pParameterData = (byte[]) ckMechanism.pParameter;
            this.pParameter = new Memory(pParameterData.length);
            this.pParameter.write(0L, pParameterData, 0, pParameterData.length);

            this.ulParameterLen = new NativeLong(pParameterData.length, true);
        } else {
            throw new SmartCardException("Unknown parameter type: " + ckMechanism.pParameter.getClass());
        }

        write();
    }

    @Override
    protected List<String> getFieldOrder() {
        List<String> fieldOrder = new ArrayList<>();

        fieldOrder.add("mechanism");
        fieldOrder.add("pParameter");
        fieldOrder.add("ulParameterLen");

        return fieldOrder;
    }

}
