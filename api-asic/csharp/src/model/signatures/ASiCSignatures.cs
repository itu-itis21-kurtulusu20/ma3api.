/**
 * @author yavuz.kahveci
 */
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.asic.model.signatures
{
    public class ASiCSignatures : BaseSignatures
    {

        public ASiCSignatures(Context context, PackageContents contents)
            : base(context, contents)
        {
        }


        protected override string getRootElementPrefix()
        {
            return "asic";
        }

        protected override string getRootElementNamespace()
        {
            return "http://uri.etsi.org/02918/v1.2.1#";
        }

        protected override string getRootElementName()
        {
            return "XAdESSignatures";
        }
    }
}