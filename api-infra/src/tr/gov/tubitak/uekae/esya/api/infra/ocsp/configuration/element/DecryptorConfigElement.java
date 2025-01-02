package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 7/9/14
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class DecryptorConfigElement extends AbstractConfigElement implements ISmartCardConfigElement {

	private OcspConfigTags[] tags = new OcspConfigTags[]{OcspConfigTags.KEYLABEL, OcspConfigTags.SLOTNO, OcspConfigTags.CARDTYPE};

	public DecryptorConfigElement(){

	}

	public DecryptorConfigElement(String keyLabel, long slotNo, String cardType) {
		map.put(OcspConfigTags.KEYLABEL, keyLabel);
		map.put(OcspConfigTags.SLOTNO, Long.toString(slotNo));
		map.put(OcspConfigTags.CARDTYPE, cardType);
	}

	public String getKeyLabel() {
		return map.get(OcspConfigTags.KEYLABEL);
	}

	public long getSlotNo() {
		return Long.parseLong(map.get(OcspConfigTags.SLOTNO));
	}

	public String getPin() {
		return null;
	}

	public String getCardType() {
		return map.get(OcspConfigTags.CARDTYPE);
	}

	public int getSessionPool() {
		return 1;
	}

	public OcspConfigTags getRootTag() {
		return OcspConfigTags.DECRYPTOR;
	}

	public OcspConfigTags[] getElementNames() {
		return tags ;
	}

	public boolean isMultiple() {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
