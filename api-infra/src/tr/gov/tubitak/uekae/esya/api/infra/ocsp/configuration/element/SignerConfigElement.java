package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/21/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 * The structure is as follows:
 * <ul>
 * <li>keylabel</li>
 * <li>slotno</li>
 * <li>cardtype</li>
 * <li>pin</li>
 * <li>sessionpool</li>
 * </ul>
 */
public class SignerConfigElement extends AbstractConfigElement implements ISmartCardConfigElement {

	private OcspConfigTags[] tags = new OcspConfigTags[]{OcspConfigTags.KEYLABEL, OcspConfigTags.SLOTNO, OcspConfigTags.CARDTYPE, OcspConfigTags.PIN, OcspConfigTags.SESSIONPOOL};

	public SignerConfigElement(){

	}
	public SignerConfigElement(String keyLabel, long slotNo, String cardType, String password, int sessionPool) {
		map.put(OcspConfigTags.KEYLABEL, keyLabel);
		map.put(OcspConfigTags.SLOTNO, Long.toString(slotNo));
		map.put(OcspConfigTags.CARDTYPE, cardType);
		map.put(OcspConfigTags.PIN, password);
		map.put(OcspConfigTags.SESSIONPOOL, Integer.toString(sessionPool));
	}

	public OcspConfigTags getRootTag() {
		return OcspConfigTags.SIGNER;
	}

	public OcspConfigTags[] getElementNames() {
		return tags;
	}

	public boolean isMultiple() {
		return true;
	}

	/*public void wrapSensitiveData(BufferedCipher cipher) throws Exception {
		String pin = map.get(OcspConfigTags.PIN);
		if (pin!=null && !pin.trim().equals("")){
			cipher.reset();
			pin = Base64.encode(cipher.doFinal(pin.getBytes("UTF-8")));
			map.put(OcspConfigTags.PIN, pin);
		}
	}*/

	public String getKeyLabel() {
		return map.get(OcspConfigTags.KEYLABEL);
	}

	public long getSlotNo() {
		return Long.parseLong(map.get(OcspConfigTags.SLOTNO));
	}

	public String getPin() {
		return map.get(OcspConfigTags.PIN);
	}

	public String getCardType() {
		return map.get(OcspConfigTags.CARDTYPE);
	}

	public int getSessionPool() {
		return Integer.parseInt(map.get(OcspConfigTags.SESSIONPOOL));
	}
}
