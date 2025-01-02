package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;


public class AEPOps extends PKCS11Ops
{
	public AEPOps()
    {
	    super(CardType.AEPKEYPER);
	}
}
