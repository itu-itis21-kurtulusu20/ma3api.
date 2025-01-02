/**
 * @author yavuz.kahveci
 */
using tr.gov.tubitak.uekae.esya.api.asic.model;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.provider;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

namespace tr.gov.tubitak.uekae.esya.api.asic.core
{
    
    public class PackageContentResolver : IResolver
    {
        private readonly PackageContents contents;

        public PackageContentResolver(PackageContents aContents)
        {
            contents = aContents;
        }

        public bool isResolvable(string aURI, xmlsignature.Context aBaglam)
        {
            foreach (Signable signable in contents.getDatas())
            {
                string path = signable.getURI();
                string absPath = "/" + path;
                if (path.Equals(aURI) || absPath.Equals(aURI))
                    return true;
            }
            return false;
        }

        public Document resolve(string aURI, xmlsignature.Context aBaglam)
        {
            foreach (Signable signable in contents.getDatas())
            {
                string path = signable.getURI();
                string absPath = "/" + path;
                if (path.Equals(aURI) || absPath.Equals(aURI))
                    return new DocumentSignable(signable);
            }
            return null;
        }
    }
}
