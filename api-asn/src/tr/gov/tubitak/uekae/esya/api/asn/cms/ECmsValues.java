package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.asn.cms._cmsValues;

/**
 * User: zeldal.ozdemir
 * Date: 1/25/11
 * Time: 2:15 PM
 */
public class ECmsValues {
    public static final Asn1ObjectIdentifier OID_CT_CONTENTINFO = new Asn1ObjectIdentifier(_cmsValues.id_ct_contentInfo);
    public static final Asn1ObjectIdentifier OID_DATA = new Asn1ObjectIdentifier(_cmsValues.id_data);
    public static final Asn1ObjectIdentifier OID_SIGNEDDATA = new Asn1ObjectIdentifier(_cmsValues.id_signedData);
    public static final Asn1ObjectIdentifier OID_ENVELOPEDDATA = new Asn1ObjectIdentifier(_cmsValues.id_envelopedData);
    public static final Asn1ObjectIdentifier OID_DIGESTEDDATA = new Asn1ObjectIdentifier(_cmsValues.id_digestedData);
    public static final Asn1ObjectIdentifier OID_ENCRYPTEDDATA = new Asn1ObjectIdentifier(_cmsValues.id_encryptedData);
    public static final Asn1ObjectIdentifier OID_CT_AUTHDATA = new Asn1ObjectIdentifier(_cmsValues.id_ct_authData);
    public static final Asn1ObjectIdentifier OID_CONTENTTYPE = new Asn1ObjectIdentifier(_cmsValues.id_contentType);
    public static final Asn1ObjectIdentifier OID_MESSAGEDIGEST = new Asn1ObjectIdentifier(_cmsValues.id_messageDigest);
    public static final Asn1ObjectIdentifier OID_SIGNINGTIME = new Asn1ObjectIdentifier(_cmsValues.id_signingTime);
    public static final Asn1ObjectIdentifier OID_COUNTERSIGNATURE = new Asn1ObjectIdentifier(_cmsValues.id_countersignature);
    public static final Asn1ObjectIdentifier OID_TIMESTAMP = new Asn1ObjectIdentifier(_cmsValues.id_timeStamp);
    public static final Asn1ObjectIdentifier OID_AA = new Asn1ObjectIdentifier(_cmsValues.id_aa);
    public static final Asn1ObjectIdentifier OSMIMECAPABILITIES = new Asn1ObjectIdentifier(_cmsValues.smimeCapabilities);
    public static final Asn1ObjectIdentifier OID_AA_ENCRYPKEYPREF = new Asn1ObjectIdentifier(_cmsValues.id_aa_encrypKeyPref);
    public static final Asn1ObjectIdentifier OID_AA_SIGNINGCERTIFICATE = new Asn1ObjectIdentifier(_cmsValues.id_aa_signingCertificate);
    public static final Asn1ObjectIdentifier OID_AA_SIGNINGCERTIFICATEV2 = new Asn1ObjectIdentifier(_cmsValues.id_aa_signingCertificateV2);
    public static final Asn1ObjectIdentifier OID_SHA256 = new Asn1ObjectIdentifier(_cmsValues.id_sha256);
}
