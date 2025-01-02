package tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;

import java.io.IOException;

/**
 * @author ahmety
 * date: May 14, 2009
 */
public interface IResolver
{

    boolean isResolvable(String aURI, Context aBaglam);

    Document resolve(String aURI, Context aBaglam)
            throws IOException;
}
