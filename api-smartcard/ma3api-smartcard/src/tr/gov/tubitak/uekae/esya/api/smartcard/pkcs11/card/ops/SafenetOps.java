/**
 * 
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;

/**
 * @author aslihan.kubilay
 *
 */
public class SafenetOps extends PKCS11Ops {

	public SafenetOps() 
	{
		super(CardType.SAFENET);
	}

}
