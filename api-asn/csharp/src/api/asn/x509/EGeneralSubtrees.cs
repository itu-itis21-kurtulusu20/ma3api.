using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EGeneralSubtrees : BaseASNWrapper<GeneralSubtrees>
    {
        public EGeneralSubtrees(GeneralSubtrees aObject)
            : base(aObject)
        {
        }

        public EGeneralSubtrees(EGeneralSubtree[] aSubtreeArr)
            : base(new GeneralSubtrees(unwrapArray<GeneralSubtree, EGeneralSubtree>(aSubtreeArr)))
        {
        }

        public EGeneralSubtree[] getElements()
        {
            return wrapArray<EGeneralSubtree, GeneralSubtree>(mObject.elements, typeof(EGeneralSubtree));
        }
    }
}
