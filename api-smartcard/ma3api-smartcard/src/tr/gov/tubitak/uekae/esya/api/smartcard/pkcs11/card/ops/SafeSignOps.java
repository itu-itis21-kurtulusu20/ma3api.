package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;


public class SafeSignOps extends PKCS11Ops
{
	public SafeSignOps()
	{
		super(CardType.SAFESIGN);
	}
}
