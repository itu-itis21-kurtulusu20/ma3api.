/**
 * @author yavuz.kahveci
 */

using System;
using tr.gov.tubitak.uekae.esya.api.asic.core;

namespace tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest
{
    using Element = System.Xml.XmlElement;

    public class ExtensionImpl : Extension
    {
        readonly Element extensionElm;

        public ExtensionImpl(Element aExtensionElm)
        {
            extensionElm = aExtensionElm;
        }

        public bool isCritical()
        {
            string critical = extensionElm.GetAttribute(ASiCConstants.ATTR_CRITICAL);
            return critical.Equals("true", StringComparison.OrdinalIgnoreCase); //equalsIgnoreCase("true");
        }
    }
}
