package tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author ahmety
 * date: May 14, 2009
 */
public class IdResolver implements IResolver
{
    private static Logger logger = LoggerFactory.getLogger(IdResolver.class);

    public boolean isResolvable(String aURI, Context aContext)
    {
        if (aURI==null || aURI.length()==0)
            return false;

        return (aURI.startsWith("#"))
                    && (!aURI.startsWith("#xpointer(")
                    && aContext.getIdRegistry().get(aURI.substring(1))!=null);
    }

    public Document resolve(String aURI, Context aContext) throws IOException
    {
        try {
            Element element = aContext.getIdRegistry().get(aURI.substring(1));

            if (element==null){
                throw new IOException(I18n.translate("resolver.cantResolveUri", aURI));
            }

            return new DOMDocument(element, aURI);
        }
        catch (Exception x){
            logger.error(I18n.translate("resolver.cantResolveUri", aURI), x);
            throw new IOException(I18n.translate("resolver.cantResolveUri", aURI));
        }
    }

}
