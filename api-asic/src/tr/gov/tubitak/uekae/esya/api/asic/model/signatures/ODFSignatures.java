package tr.gov.tubitak.uekae.esya.api.asic.model.signatures;


import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.signature.Context;

/**
 * @author ayetgin
 */
public class ODFSignatures extends BaseSignatures
{

    public ODFSignatures(Context context, PackageContents contents)
    {
        super(context, contents);
    }

    // todo
    //<signatures xmlns="urn:oasis:names:tc:opendocument:xmlns:container">
    //<document-signatures xmlns="urn:oasis:names:tc:opendocument:xmlns:digitalsignature:1.0">

    protected String getRootElementPrefix()
    {
        return null;
    }

    protected String getRootElementNamespace()
    {
        return "urn:oasis:names:tc:opendocument:xmlns:digitalsignature:1.0";
    }

    protected String getRootElementName()
    {
        return "document-signatures";
    }
}
