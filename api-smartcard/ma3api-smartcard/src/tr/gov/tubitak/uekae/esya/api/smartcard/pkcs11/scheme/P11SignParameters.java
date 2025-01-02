package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import sun.security.pkcs11.wrapper.CK_MECHANISM;

public class P11SignParameters {

    private CK_MECHANISM mech;
    byte [] signatureInput;

    public P11SignParameters(CK_MECHANISM mech, byte [] signatureInput) {
        this.mech = mech;
        this.signatureInput = signatureInput;
    }

    public CK_MECHANISM getMech() {
        return mech;
    }

    public byte[] getSignatureInput() {
        return signatureInput;
    }
}
