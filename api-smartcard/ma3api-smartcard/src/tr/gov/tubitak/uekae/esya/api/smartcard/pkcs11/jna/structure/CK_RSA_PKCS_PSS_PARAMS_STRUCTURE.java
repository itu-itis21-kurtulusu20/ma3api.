package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure;

import com.sun.jna.NativeLong;
import sun.security.pkcs11.wrapper.CK_RSA_PKCS_PSS_PARAMS;
import sun.security.pkcs11.wrapper.Constants;

import java.util.ArrayList;
import java.util.List;

public class CK_RSA_PKCS_PSS_PARAMS_STRUCTURE extends CK_PARAMS_STRUCTURE {

    public NativeLong hashAlg = new NativeLong(0, true);
    public NativeLong mgf = new NativeLong(0, true);
    public NativeLong sLen = new NativeLong(0, true);

    public CK_RSA_PKCS_PSS_PARAMS_STRUCTURE(CK_RSA_PKCS_PSS_PARAMS params) {
        super();

        String pssParamsStr = params.toString();

        int hashAlgStart = pssParamsStr.indexOf(":") + 1;
        int hashAlgEnd = pssParamsStr.indexOf(Constants.NEWLINE, hashAlgStart);
        String hashAlgHexStr = pssParamsStr.substring(hashAlgStart, hashAlgEnd).trim();
        this.hashAlg = new NativeLong(Long.parseLong(hashAlgHexStr, 16));

        int mgfStart = pssParamsStr.indexOf(":", hashAlgEnd) + 1;
        int mgfEnd = pssParamsStr.indexOf(Constants.NEWLINE, mgfStart);
        String mgfHexStr = pssParamsStr.substring(mgfStart, mgfEnd).trim();
        this.mgf = new NativeLong(Long.parseLong(mgfHexStr, 16));

        int sLenStart = pssParamsStr.indexOf(":", mgfEnd) + 1;
        String sLenStr = pssParamsStr.substring(sLenStart).trim();
        this.sLen = new NativeLong(Long.parseLong(sLenStr, 10));

        write();
    }

    @Override
    protected List<String> getFieldOrder() {
        List<String> fieldOrder = new ArrayList<>();

        fieldOrder.add("hashAlg");
        fieldOrder.add("mgf");
        fieldOrder.add("sLen");

        return fieldOrder;
    }
}
