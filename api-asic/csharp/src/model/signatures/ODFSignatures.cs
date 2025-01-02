/**
 * @author yavuz.kahveci
 */

using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.asic.model.signatures
{
    public class ODFSignatures : BaseSignatures
    {

        public ODFSignatures(Context context, PackageContents contents) : base(context,contents)
        {
        }

        // todo
        //<signatures xmlns="urn:oasis:names:tc:opendocument:xmlns:container">
        //<document-signatures xmlns="urn:oasis:names:tc:opendocument:xmlns:digitalsignature:1.0">

        protected override string getRootElementPrefix()
        {
            return null;
        }

        protected override string getRootElementNamespace()
        {
            return "urn:oasis:names:tc:opendocument:xmlns:digitalsignature:1.0";
        }

        protected override string getRootElementName()
        {
            return "document-signatures";
        }
    }
}