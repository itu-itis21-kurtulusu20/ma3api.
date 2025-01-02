package tr.gov.tubitak.uekae.esya.api.asic.core;

import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.provider.DocumentSignable;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IResolver;

import java.io.IOException;

/**
 * @author ayetgin
 */
public class PackageContentResolver implements IResolver
{
    private PackageContents contents;

    public PackageContentResolver(PackageContents aContents)
    {
        contents = aContents;
    }

    public boolean isResolvable(String aURI, Context aBaglam)
    {
        for (Signable signable : contents.getDatas()){
            String path = signable.getURI();
            String absPath = "/"+path;
            if (path.equals(aURI) || absPath.equals(aURI))
                return true;
        }
        return false;
    }

    public Document resolve(String aURI, Context aBaglam) throws IOException
    {
        for (Signable signable : contents.getDatas()){
            String path = signable.getURI();
            String absPath = "/"+path;
            if (path.equals(aURI) || absPath.equals(aURI))
                return new DocumentSignable(signable);
        }
        return null;
    }
}
