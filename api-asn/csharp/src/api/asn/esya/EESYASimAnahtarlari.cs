
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYASimAnahtarlari : BaseASNWrapper<ESYASimAnahtarlari>
    {
         public EESYASimAnahtarlari(byte[] aBytes) 
             :base(aBytes, new ESYASimAnahtarlari()){}

        public EESYASimAnahtarlari(EESYASimAnahtari[] simAnahtaris)
            :base(new ESYASimAnahtarlari())
        {
            mObject.elements = unwrapArray<ESYASimAnahtari,EESYASimAnahtari>(simAnahtaris);
        }

        public EESYASimAnahtari[] getEsyaSimAnahtarlari() 
        {
            EESYASimAnahtari[] eesyaSimAnahtaris= new EESYASimAnahtari[mObject.elements.Length];
            for (int i = 0; i < mObject.elements.Length; i++) {
                ESYASimAnahtari element = mObject.elements[i];
                eesyaSimAnahtaris[i] = new EESYASimAnahtari(element);

            }
            return eesyaSimAnahtaris;
        }
    }
}
