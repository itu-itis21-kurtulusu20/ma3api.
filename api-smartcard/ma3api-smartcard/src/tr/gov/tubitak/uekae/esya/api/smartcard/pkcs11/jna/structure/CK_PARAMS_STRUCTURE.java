package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure;

import com.sun.jna.Structure;

public abstract class CK_PARAMS_STRUCTURE extends Structure {

    protected CK_PARAMS_STRUCTURE() {
        setAlignType(ALIGN_NONE);
    }

}
