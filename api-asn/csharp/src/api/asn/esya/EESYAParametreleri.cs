
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYAParametreleri : BaseASNWrapper<ESYAParametreleri>
    {
        public EESYAParametreleri(ESYAParametreleri aObject):base(aObject){}

        public EESYAParametreleri(byte[] aBytes): base(aBytes, new ESYAParametreleri()){}

        public EESYAParametreleri(EESYAParametre[] esyaParametres):base(new ESYAParametreleri())
        {
            mObject.elements = unwrapArray<ESYAParametre, EESYAParametre>(esyaParametres);            
        }
        public EESYAParametre[] getEsyaParametres()
        {
            return wrapArray<EESYAParametre, ESYAParametre>(mObject.elements, typeof(EESYAParametre));
        }
    }
}
