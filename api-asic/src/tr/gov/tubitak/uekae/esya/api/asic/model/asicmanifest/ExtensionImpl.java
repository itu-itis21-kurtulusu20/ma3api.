package tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest;


import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asic.core.ASiCConstants;

/**
 * @author ayetgin
 */
public class ExtensionImpl implements Extension
{
    Element extensionElm;

    public ExtensionImpl(Element aExtensionElm)
    {
        extensionElm = aExtensionElm;
    }

    public boolean isCritical()
    {
        String critical = extensionElm.getAttribute(ASiCConstants.ATTR_CRITICAL);
        return critical.equalsIgnoreCase("true");
    }
}
