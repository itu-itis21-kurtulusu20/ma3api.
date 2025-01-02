package tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;

import java.io.IOException;

/**
 * <p>'#xpointer(/)' MUST be interpreted to identify the root node [XPath] of
 * the document that contains the URI attribute.
 *
 * <p>'#xpointer(id('ID'))' MUST be interpreted to identify the element node
 * identified by '#element(ID)' [XPointer-Element] when evaluated with respect
 * to the document that contains the URI attribute.
 *
 * <p>To retain comments while selecting an element by an identifier ID, use
 * the following scheme-based XPointer: URI='#xpointer(id('ID'))'. To retain
 * comments while selecting the entire document, use the following scheme-based
 * XPointer: URI='#xpointer(/)'
 *
 * @author ahmety
 * date: May 18, 2009
 */
public class XPointerResolver implements IResolver
{

    private static Logger msLogcu = LoggerFactory.getLogger(XPointerResolver.class);

    public boolean isResolvable(String aURI, Context aBaglam)
    {
        if (aURI == null || aURI.equals("")) {
            return false;
        }
        if (isXPointerSlash(aURI) || isXPointerId(aURI)) {
            return true;
        }
        return false;
    }

    public Document resolve(String aUri, Context aContext)
            throws IOException
    {
        String uriStr = aUri;
        if (isXPointerSlash(uriStr)) {
            return new DOMDocument(aContext.getDocument(), aContext.getBaseURIStr(), true);
        }
        else if (isXPointerId(uriStr)) {
            String id = getXPointerId(uriStr);
            Element element = aContext.getIdRegistry().get(id);


            if (element == null) {
                throw new IOException(I18n.translate("resolver.cantResolveUri",aUri));
            }

            String baseURI = aContext.getBaseURIStr();
            String uri;
            if (baseURI != null && baseURI.length() > 0) {
                uri = baseURI.concat(aUri);
            }
            else {
                uri = aUri;
            }

            return new DOMDocument(element, uri, true);
        }
        throw new IOException(I18n.translate("resolver.xpointerSupportLimited", aUri)); 
    }

    /**
     * @param uri check if isXPointerSlash
     * @return true if begins with xpointer
     */
    private static boolean isXPointerSlash(String uri)
    {

        if (uri.equals("#xpointer(/)")) {
            return true;
        }

        return false;
    }


    private static final String XP = "#xpointer(id(";
    private static final int XP_LENGTH = XP.length();

    /**
     * @param uri check value
     * @return it it has an xpointer id
     */
    private static boolean isXPointerId(String uri)
    {


        if (uri.startsWith(XP) && uri.endsWith("))"))
        {
            String idPlusDelim = uri.substring(XP_LENGTH, uri.length() - 2);

            int idLen = idPlusDelim.length() - 1;
            if (((idPlusDelim.charAt(0) == '"')
                        && (idPlusDelim.charAt(idLen) == '"'))
                    || ((idPlusDelim.charAt(0) == '\'')
                        && (idPlusDelim.charAt(idLen) == '\'')))
            {
                if (msLogcu.isDebugEnabled())
                    msLogcu.debug("Id=" + idPlusDelim.substring(1, idLen));

                return true;
            }
        }

        return false;
    }

    /**
     * @param uri to be checked
     * @return xpointerId to search.
     */
    private static String getXPointerId(String uri)
    {
        if (uri.startsWith(XP)&& uri.endsWith("))"))
        {
            String idPlusDelim = uri.substring(XP_LENGTH, uri.length() - 2);
            int idLen = idPlusDelim.length() - 1;
            if (((idPlusDelim.charAt(0) == '"') && (idPlusDelim.charAt(idLen) == '"'))
                    || ((idPlusDelim.charAt(0) == '\'') && (idPlusDelim.charAt(idLen) == '\'')))
            {
                return idPlusDelim.substring(1, idLen);
            }
        }

        return null;
    }

    public static void main(String[] args)
    {
        System.out.println(isXPointerId("#xpointer(id('object-3'))"));
    }

}
