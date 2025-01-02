package tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

import java.io.File;
import java.io.IOException;

/**
 * @author ahmety
 * date: Jul 1, 2009
 */
public class AnonymousResolver implements IResolver
{
    FileDocument mFileDocument;

    public AnonymousResolver(File aFile, String aMIMEType)
            throws XMLSignatureException
    {
        mFileDocument = new FileDocument(aFile, aMIMEType);
    }

    public boolean isResolvable(String aURI, Context aBaglam)
    {
        return aURI == null;
    }

    public Document resolve(String uri, Context aBaglam)
            throws IOException
    {
        return mFileDocument;
    }
}
