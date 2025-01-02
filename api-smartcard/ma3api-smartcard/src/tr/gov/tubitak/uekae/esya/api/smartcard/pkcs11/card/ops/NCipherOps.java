package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;

import static sun.security.pkcs11.wrapper.PKCS11Constants.CKF_OS_LOCKING_OK;


public class NCipherOps extends PKCS11Ops
{
	public NCipherOps()
	{
		super(CardType.NCIPHER);
	}

    @Override
    CK_C_INITIALIZE_ARGS get_CK_C_INITIALIZE_ARGS() {
        CK_C_INITIALIZE_ARGS args = new CK_C_INITIALIZE_ARGS();
        args.flags = CKF_OS_LOCKING_OK;
        return args;
    }
}

