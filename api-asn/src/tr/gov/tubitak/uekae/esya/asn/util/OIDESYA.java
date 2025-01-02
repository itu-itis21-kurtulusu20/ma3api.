package tr.gov.tubitak.uekae.esya.asn.util;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.asn.esya._esyaValues;

/**
 * <p>Title: ESYA</p>
 * <p>Description: </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 *
 * @author Muhammed Serdar SORAN
 * @version 1.0
 * @deprecated will be removed in later versions.  use EESYAOID
 * @see tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAOID
 */
@Deprecated
public class OIDESYA
{
    private OIDESYA() {
        throw new IllegalStateException("OIDESYA class is deprecated");
    }

    protected static final int[] id_esya = _esyaValues.id_esya;
    protected static final int[] id_servisler = _esyaValues.id_servisler;
    protected static final int[] id_refno = _esyaValues.id_refno;
    protected static final int[] id_kartSeriNo = _esyaValues.id_kartSeriNo;
    protected static final int[] id_kartUreticiNo = _esyaValues.id_kartUreticiNo;
    protected static final int[] id_sablonNo = _esyaValues.id_sablonNo;
    protected static final int[] id_sertTalepNo = _esyaValues.id_sertTalepNo;
    protected static final int[] id_siparisDetayNo = _esyaValues.id_siparisDetayNo;

    protected static final int[] id_ESYAyonetici = _esyaValues.id_ESYAyonetici;
    protected static final int[] id_ESYAkayitci = _esyaValues.id_ESYAkayitci;
    protected static final int[] id_ESYAdenetci = _esyaValues.id_ESYAdenetci;

    protected static final int[] id_TK_nesoid = _esyaValues.id_TK_nesoid;


    public static final Asn1ObjectIdentifier oid_refno = new Asn1ObjectIdentifier(id_refno);
    public static final Asn1ObjectIdentifier oid_kartSeriNo = new Asn1ObjectIdentifier(id_kartSeriNo);
    public static final Asn1ObjectIdentifier oid_kartUreticiNo = new Asn1ObjectIdentifier(id_kartUreticiNo);
    public static final Asn1ObjectIdentifier oid_sablonNo = new Asn1ObjectIdentifier(id_sablonNo);
    public static final Asn1ObjectIdentifier oid_sertTalepNo = new Asn1ObjectIdentifier(id_sertTalepNo);
    public static final Asn1ObjectIdentifier oid_siparisDetayNo = new Asn1ObjectIdentifier(id_siparisDetayNo);

    public static final Asn1ObjectIdentifier oid_ESYAyonetici = new Asn1ObjectIdentifier(id_ESYAyonetici);
    public static final Asn1ObjectIdentifier oid_ESYAkayitci = new Asn1ObjectIdentifier(id_ESYAkayitci);
    public static final Asn1ObjectIdentifier oid_ESYAdenetci = new Asn1ObjectIdentifier(id_ESYAdenetci);
}
