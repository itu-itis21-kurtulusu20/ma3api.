/**
 * @author yavuz.kahveci
 */
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.asic.model.signatures
{
    public class OCFSignatures : BaseSignatures
    {

        public OCFSignatures(Context context, PackageContents contents) : base(context, contents)
        {
        }

        protected override string getRootElementPrefix()
        {
            return null;  // todo
        }

        protected override string getRootElementNamespace()
        {
            return null;  // todo
        }

        protected override string getRootElementName()
        {
            return null;  // todo
        }
    }
}