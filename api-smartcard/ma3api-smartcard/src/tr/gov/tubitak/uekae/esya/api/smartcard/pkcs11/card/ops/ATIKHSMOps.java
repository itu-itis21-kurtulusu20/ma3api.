package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;

public class ATIKHSMOps extends PKCS11Ops{

    public ATIKHSMOps()
    {
	super(CardType.ATIKHSM);
    }
}
