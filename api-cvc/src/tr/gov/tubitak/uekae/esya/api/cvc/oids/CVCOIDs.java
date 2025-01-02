package tr.gov.tubitak.uekae.esya.api.cvc.oids;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cvc.ECVCValues;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 6/15/11
 * Time: 4:27 PM
 */
public enum CVCOIDs {

    sigs_iso9796_2withsha1(ECVCValues.sigs_iso9796_2withsha1, "sigs_iso9796_2withsha1", SignatureAlg.RSA_ISO9796_2_SHA1),
    sigs_iso9796_2withsha224(ECVCValues.sigs_iso9796_2withsha224, "sigs_iso9796_2withsha224", SignatureAlg.RSA_ISO9796_2_SHA224),
    sigs_iso9796_2withsha256(ECVCValues.sigs_iso9796_2withsha256, "sigs_iso9796_2withsha256", SignatureAlg.RSA_ISO9796_2_SHA256),
    sigs_iso9796_2withsha384(ECVCValues.sigs_iso9796_2withsha384, "sigs_iso9796_2withsha384", SignatureAlg.RSA_ISO9796_2_SHA384),
    sigs_iso9796_2withsha512(ECVCValues.sigs_iso9796_2withsha512, "sigs_iso9796_2withsha512", SignatureAlg.RSA_ISO9796_2_SHA512),

    enc_iso9796_2withrsasha1(ECVCValues.enc_iso9796_2withrsasha1, "enc_iso9796_2withrsasha1", SignatureAlg.RSA_ISO9796_2_SHA1),
    enc_iso9796_2withrsasha224(ECVCValues.enc_iso9796_2withrsasha224, "enc_iso9796_2withrsasha224", SignatureAlg.RSA_ISO9796_2_SHA224),
    enc_iso9796_2withrsasha256(ECVCValues.enc_iso9796_2withrsasha256, "enc_iso9796_2withrsasha256", SignatureAlg.RSA_ISO9796_2_SHA256),
    enc_iso9796_2withrsasha384(ECVCValues.enc_iso9796_2withrsasha384, "enc_iso9796_2withrsasha384", SignatureAlg.RSA_ISO9796_2_SHA384),
    enc_iso9796_2withrsasha512(ECVCValues.enc_iso9796_2withrsasha512, "enc_iso9796_2withrsasha512", SignatureAlg.RSA_ISO9796_2_SHA512),

    oid_dev_auth_privacy_SHA_1(ECVCValues.oid_dev_auth_privacy_SHA_1, "oid_dev_auth_privacy_SHA_1", SignatureAlg.RSA_ISO9796_2_SHA1),
    oid_dev_auth_privacy_SHA_224(ECVCValues.oid_dev_auth_privacy_SHA_224, "oid_dev_auth_privacy_SHA_224", SignatureAlg.RSA_ISO9796_2_SHA224),
    oid_dev_auth_privacy_SHA_256(ECVCValues.oid_dev_auth_privacy_SHA_256, "oid_dev_auth_privacy_SHA_256", SignatureAlg.RSA_ISO9796_2_SHA256),
    oid_dev_auth_privacy_SHA_384(ECVCValues.oid_dev_auth_privacy_SHA_384, "oid_dev_auth_privacy_SHA_384", SignatureAlg.RSA_ISO9796_2_SHA384),
    oid_dev_auth_privacy_SHA_512(ECVCValues.oid_dev_auth_privacy_SHA_512, "oid_dev_auth_privacy_SHA_512", SignatureAlg.RSA_ISO9796_2_SHA512),

    oid_dsi_ecdsa_SHA_1(ECVCValues.oid_dsi_ecdsa_SHA_1, "oid_dsi_ecdsa_SHA_1", SignatureAlg.ECDSA_SHA1),
    oid_dsi_ecdsa_SHA_224(ECVCValues.oid_dsi_ecdsa_SHA_224, "oid_dsi_ecdsa_SHA_224", SignatureAlg.ECDSA_SHA224),
    oid_dsi_ecdsa_SHA_256(ECVCValues.oid_dsi_ecdsa_SHA_256, "oid_dsi_ecdsa_SHA_256", SignatureAlg.ECDSA_SHA256),

    oid_TA_ECDSA_SHA_1(ECVCValues.oid_TA_ECDSA_SHA_1, "oid_TA_ECDSA_SHA_1", SignatureAlg.ECDSA_SHA1),
    oid_TA_ECDSA_SHA_224(ECVCValues.oid_TA_ECDSA_SHA_224, "oid_TA_ECDSA_SHA_224", SignatureAlg.ECDSA_SHA224),
    oid_TA_ECDSA_SHA_256(ECVCValues.oid_TA_ECDSA_SHA_256, "oid_TA_ECDSA_SHA_256", SignatureAlg.ECDSA_SHA256),

    meac(ECVCValues.meac, "meac", null);


    private Asn1ObjectIdentifier mOid;
    private String mName;
    private SignatureAlg mSignatureAlg;


    CVCOIDs(Asn1ObjectIdentifier aAlgId, String aAlgName, SignatureAlg aSignatureAlg) {
        mOid = aAlgId;
        mName = aAlgName;
        mSignatureAlg = aSignatureAlg;
    }

    public String getName() {
        return mName;
    }

    public int[] getOID() {
        return mOid.value;
    }

    public SignatureAlg getSignatureAlg() {
        return mSignatureAlg;
    }

    public static CVCOIDs fromOID(Asn1ObjectIdentifier aOID) {
        for (CVCOIDs alg : CVCOIDs.values()) {
            if (Arrays.equals(aOID.value, alg.getOID()))
                return alg;
        }
        return null;
    }

    public static CVCOIDs fromName(String algName) {
        for (CVCOIDs alg : values()) {
            if (alg.getName().equalsIgnoreCase(algName))
                return alg;
        }
        return null;
    }

}
