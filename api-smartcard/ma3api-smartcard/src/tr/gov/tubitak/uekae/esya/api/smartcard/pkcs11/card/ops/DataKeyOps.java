package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;


public class DataKeyOps extends PKCS11Ops
{
	public DataKeyOps()
    {
	    super(CardType.DATAKEY);
    }
}
