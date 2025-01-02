package tr.gov.tubitak.uekae.esya.api.asn.cvc;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 6/15/11
 * Time: 4:55 PM
 */
public class ECVCValues {
    public static final Asn1ObjectIdentifier sigs_iso9796_2withsha1 = new Asn1ObjectIdentifier(new int[]{1, 3, 36, 3, 4, 2, 2, 1});
    public static final Asn1ObjectIdentifier sigs_iso9796_2withsha224 = new Asn1ObjectIdentifier(new int[]{1, 3, 36, 3, 4, 2, 2, 3});
    public static final Asn1ObjectIdentifier sigs_iso9796_2withsha256 = new Asn1ObjectIdentifier(new int[]{1, 3, 36, 3, 4, 2, 2, 4});
    public static final Asn1ObjectIdentifier sigs_iso9796_2withsha384 = new Asn1ObjectIdentifier(new int[]{1, 3, 36, 3, 4, 2, 2, 5});
    public static final Asn1ObjectIdentifier sigs_iso9796_2withsha512 = new Asn1ObjectIdentifier(new int[]{1, 3, 36, 3, 4, 2, 2, 6});
    public static final Asn1ObjectIdentifier enc_iso9796_2withrsasha1 = new Asn1ObjectIdentifier(new int[]{1, 3, 36, 7, 2, 1, 1});
    public static final Asn1ObjectIdentifier enc_iso9796_2withrsasha224 = new Asn1ObjectIdentifier(new int[]{1, 3, 36, 7, 2, 1, 3});
    public static final Asn1ObjectIdentifier enc_iso9796_2withrsasha256 = new Asn1ObjectIdentifier(new int[]{1, 3, 36, 7, 2, 1, 4});
    public static final Asn1ObjectIdentifier enc_iso9796_2withrsasha384 = new Asn1ObjectIdentifier(new int[]{1, 3, 36, 7, 2, 1, 5});
    public static final Asn1ObjectIdentifier enc_iso9796_2withrsasha512 = new Asn1ObjectIdentifier(new int[]{1, 3, 36, 7, 2, 1, 6});
    public static final Asn1ObjectIdentifier oid_dev_auth_privacy_SHA_1 = new Asn1ObjectIdentifier(new int[]{1, 3, 162, 14890, 2, 4, 1, 1});
    public static final Asn1ObjectIdentifier oid_dev_auth_privacy_SHA_224 = new Asn1ObjectIdentifier(new int[]{1, 3, 162, 14890, 2, 4, 1, 3});
    public static final Asn1ObjectIdentifier oid_dev_auth_privacy_SHA_256 = new Asn1ObjectIdentifier(new int[]{1, 3, 162, 14890, 2, 4, 1, 4});
    public static final Asn1ObjectIdentifier oid_dev_auth_privacy_SHA_384 = new Asn1ObjectIdentifier(new int[]{1, 3, 162, 14890, 2, 4, 1, 5});
    public static final Asn1ObjectIdentifier oid_dev_auth_privacy_SHA_512 = new Asn1ObjectIdentifier(new int[]{1, 3, 162, 14890, 2, 4, 1, 6});
    public static final Asn1ObjectIdentifier oid_dsi_ecdsa_SHA_1 = new Asn1ObjectIdentifier(new int[]{1, 3, 162, 14890, 2, 3, 1, 1, 1});
    public static final Asn1ObjectIdentifier oid_dsi_ecdsa_SHA_224 = new Asn1ObjectIdentifier(new int[]{1, 3, 162, 14890, 2, 3, 1, 1, 3});
    public static final Asn1ObjectIdentifier oid_dsi_ecdsa_SHA_256 = new Asn1ObjectIdentifier(new int[]{1, 3, 162, 14890, 2, 3, 1, 1, 4});
    public static final Asn1ObjectIdentifier oid_TA_ECDSA_SHA_1 = new Asn1ObjectIdentifier(new int[]{0, 4, 0, 127, 0, 7, 2, 2, 2, 2, 1});
    public static final Asn1ObjectIdentifier oid_TA_ECDSA_SHA_224 = new Asn1ObjectIdentifier(new int[]{0, 4, 0, 127, 0, 7, 2, 2, 2, 2, 2});
    public static final Asn1ObjectIdentifier oid_TA_ECDSA_SHA_256 = new Asn1ObjectIdentifier(new int[]{0, 4, 0, 127, 0, 7, 2, 2, 2, 2, 3});
    public static final Asn1ObjectIdentifier meac = new Asn1ObjectIdentifier(new int[]{1, 3, 162, 14890, 2, 4, 2});
}
