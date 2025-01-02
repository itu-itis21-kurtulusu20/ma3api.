package tr.gov.tubitak.uekae.esya.api.asic.model.signatures;


import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.signature.Context;

/**
 * @author ayetgin
 */
public class OCFSignatures extends BaseSignatures
{

    public OCFSignatures(Context context, PackageContents contents)
    {
        super(context, contents);
    }

    protected String getRootElementPrefix()
    {
        return null;  // todo
    }

    protected String getRootElementNamespace()
    {
        return null;  // todo
    }

    protected String getRootElementName()
    {
        return null;  // todo
    }
}
