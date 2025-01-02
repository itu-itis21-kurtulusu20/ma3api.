package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;


public class TKartOps extends PKCS11Ops
{
	public TKartOps()
	{
		super(CardType.TKART);
	}
}