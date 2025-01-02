package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/22/14
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ISmartCardConfigElement extends ISensitiveConfigElement {
	String getKeyLabel();
	long getSlotNo();
	String getPin();
	String getCardType();
	int getSessionPool();
}
