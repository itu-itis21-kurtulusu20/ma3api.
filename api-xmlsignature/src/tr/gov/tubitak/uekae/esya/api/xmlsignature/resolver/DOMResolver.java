package tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;

import java.io.IOException;

/**
 * Resolve resource over node hierarchy by Id attribute.
 *
 * @author ahmety
 * date: Jun 17, 2009
 */
public class DOMResolver implements IResolver
{

    public boolean isResolvable(String uri, Context aBaglam)
    {
        if (uri == null) {
            return false;
        }

        // if (empty uri) or (fragment) and (not xpointer)
        if (uri.equals("") || ((uri.charAt(0)=='#') && !((uri.charAt(1)=='x') && uri.startsWith("#xpointer(")))
            )
        {
            return true;
        }
        return false;

    }

    public Document resolve(String aURI, Context aBaglam)
            throws IOException
    {
        if (aURI.equals(""))
        {
            return new DOMDocument(aBaglam.getDocument(), aBaglam.getBaseURIStr());
        }
        else
        {
            String seek = aURI.substring(1);
            Element found = aBaglam.getDocument().getElementById(seek);
            if (found == null)
            {
                found = XmlUtil.findByIdAttr(aBaglam.getDocument().getDocumentElement(), seek);
            }
            if (found == null)
            {
                throw new IOException(I18n.translate("resolver.cantResolveUri", aURI));
            }

            return new DOMDocument(found, aBaglam.getBaseURIStr());
        }
    }

}
