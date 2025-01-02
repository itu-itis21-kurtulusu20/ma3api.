package tr.gov.tubitak.uekae.esya.api.asic.model.signatures;

import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.signature.Context;

/**
 * @author ayetgin
 */
public class ASiCSignatures extends BaseSignatures
{

    public ASiCSignatures(Context context, PackageContents contents)
    {
        super(context, contents);
    }


    protected String getRootElementPrefix()
    {
        return "asic";
    }

    protected String getRootElementNamespace()
    {
        return "http://uri.etsi.org/02918/v1.2.1#";
    }

    protected String getRootElementName()
    {
        return "XAdESSignatures";
    }
}
