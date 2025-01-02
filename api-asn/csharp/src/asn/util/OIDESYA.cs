using Com.Objsys.Asn1.Runtime;
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class OIDESYA
    {
        public static readonly int[] id_esya = { 1, 3, 6, 1, 4, 1, 11311, 10 };
        public static readonly int[] id_servisler = { 1, 3, 6, 1, 4, 1, 11311, 10, 1 };
        public static readonly int[] id_refno = { 1, 3, 6, 1, 4, 1, 11311, 10, 1, 1 };
        public static readonly int[] id_kartSeriNo = { 1, 3, 6, 1, 4, 1, 11311, 10, 1, 2 };
        public static readonly int[] id_kartUreticiNo = { 1, 3, 6, 1, 4, 1, 11311, 10, 1, 3 };
        public static readonly int[] id_sablonNo = { 1, 3, 6, 1, 4, 1, 11311, 10, 1, 4 };
        public static readonly int[] id_sertTalepNo = { 1, 3, 6, 1, 4, 1, 11311, 10, 1, 5 };
        public static readonly int[] id_siparisDetayNo = { 1, 3, 6, 1, 4, 1, 11311, 10, 1, 6 };

        public static readonly int[] id_ESYAyonetici = { 1, 3, 6, 1, 4, 1, 11311, 10, 2, 1 };
        public static readonly int[] id_ESYAkayitci = { 1, 3, 6, 1, 4, 1, 11311, 10, 2, 2 };
        public static readonly int[] id_ESYAdenetci = { 1, 3, 6, 1, 4, 1, 11311, 10, 2, 3 };

        public static readonly int[] id_TK_nesoid = { 2, 16, 792, 1, 61, 0, 1, 5070, 1, 1 };
        public static readonly int[] id_TK_nesoid_2 = { 2, 16, 792, 1, 61, 0, 1, 5070, 2, 1 };

        public static readonly Asn1ObjectIdentifier oid_refno = new Asn1ObjectIdentifier(id_refno);
        public static readonly Asn1ObjectIdentifier oid_kartSeriNo = new Asn1ObjectIdentifier(id_kartSeriNo);
        public static readonly Asn1ObjectIdentifier oid_kartUreticiNo = new Asn1ObjectIdentifier(id_kartUreticiNo);
        public static readonly Asn1ObjectIdentifier oid_sablonNo = new Asn1ObjectIdentifier(id_sablonNo);
        public static readonly Asn1ObjectIdentifier oid_sertTalepNo = new Asn1ObjectIdentifier(id_sertTalepNo);
        public static readonly Asn1ObjectIdentifier oid_siparisDetayNo = new Asn1ObjectIdentifier(id_siparisDetayNo);

        public static readonly Asn1ObjectIdentifier oid_ESYAyonetici = new Asn1ObjectIdentifier(id_ESYAyonetici);
        public static readonly Asn1ObjectIdentifier oid_ESYAkayitci = new Asn1ObjectIdentifier(id_ESYAkayitci);
        public static readonly Asn1ObjectIdentifier oid_ESYAdenetci = new Asn1ObjectIdentifier(id_ESYAdenetci);

    }
}
