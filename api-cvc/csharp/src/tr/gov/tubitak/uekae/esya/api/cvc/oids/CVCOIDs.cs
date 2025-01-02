using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cvc;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace tr.gov.tubitak.uekae.esya.api.cvc.oids
{
    public class CVCOIDs
    {
        public static readonly CVCOIDs sigs_iso9796_2withsha1 = new CVCOIDs(ECVCValues.sigs_iso9796_2withsha1, "sigs_iso9796_2withsha1",
                                                       SignatureAlg.RSA_ISO9796_2_SHA1);

        public static readonly CVCOIDs sigs_iso9796_2withsha224 = new CVCOIDs(ECVCValues.sigs_iso9796_2withsha224,
                                                                       "sigs_iso9796_2withsha224",
                                                                       SignatureAlg.RSA_ISO9796_2_SHA224);

        public static readonly CVCOIDs sigs_iso9796_2withsha256 = new CVCOIDs(ECVCValues.sigs_iso9796_2withsha256,
                                                         "sigs_iso9796_2withsha256", SignatureAlg.RSA_ISO9796_2_SHA256);

        public static readonly CVCOIDs sigs_iso9796_2withsha384 = new CVCOIDs(ECVCValues.sigs_iso9796_2withsha384,
                                                         "sigs_iso9796_2withsha384", SignatureAlg.RSA_ISO9796_2_SHA384);

        public static readonly CVCOIDs sigs_iso9796_2withsha512 = new CVCOIDs(ECVCValues.sigs_iso9796_2withsha512,
                                                         "sigs_iso9796_2withsha512", SignatureAlg.RSA_ISO9796_2_SHA512);

        public static readonly CVCOIDs enc_iso9796_2withrsasha1 = new CVCOIDs(ECVCValues.enc_iso9796_2withrsasha1,
                                                         "enc_iso9796_2withrsasha1", SignatureAlg.RSA_ISO9796_2_SHA1);

        public static readonly CVCOIDs enc_iso9796_2withrsasha224 = new CVCOIDs(ECVCValues.enc_iso9796_2withrsasha224,
                                                           "enc_iso9796_2withrsasha224",
                                                           SignatureAlg.RSA_ISO9796_2_SHA224);

        public static readonly CVCOIDs enc_iso9796_2withrsasha256 = new CVCOIDs(ECVCValues.enc_iso9796_2withrsasha256,
                                                           "enc_iso9796_2withrsasha256",
                                                           SignatureAlg.RSA_ISO9796_2_SHA256);

        public static readonly CVCOIDs enc_iso9796_2withrsasha384 = new CVCOIDs(ECVCValues.enc_iso9796_2withrsasha384,
                                                           "enc_iso9796_2withrsasha384",
                                                           SignatureAlg.RSA_ISO9796_2_SHA384);

        public static readonly CVCOIDs enc_iso9796_2withrsasha512 = new CVCOIDs(ECVCValues.enc_iso9796_2withrsasha512,
                                                           "enc_iso9796_2withrsasha512",
                                                           SignatureAlg.RSA_ISO9796_2_SHA512);

        public static readonly CVCOIDs oid_dev_auth_privacy_SHA_1 = new CVCOIDs(ECVCValues.oid_dev_auth_privacy_SHA_1,
                                                           "oid_dev_auth_privacy_SHA_1",
                                                           SignatureAlg.RSA_ISO9796_2_SHA1);

        public static readonly CVCOIDs oid_dev_auth_privacy_SHA_224 = new CVCOIDs(ECVCValues.oid_dev_auth_privacy_SHA_224,
                                                             "oid_dev_auth_privacy_SHA_224",
                                                             SignatureAlg.RSA_ISO9796_2_SHA224);

        public static readonly CVCOIDs oid_dev_auth_privacy_SHA_256 = new CVCOIDs(ECVCValues.oid_dev_auth_privacy_SHA_256,
                                                             "oid_dev_auth_privacy_SHA_256",
                                                             SignatureAlg.RSA_ISO9796_2_SHA256);

        public static readonly CVCOIDs oid_dev_auth_privacy_SHA_384 = new CVCOIDs(ECVCValues.oid_dev_auth_privacy_SHA_384,
                                                             "oid_dev_auth_privacy_SHA_384",
                                                             SignatureAlg.RSA_ISO9796_2_SHA384);

        public static readonly CVCOIDs oid_dev_auth_privacy_SHA_512 = new CVCOIDs(ECVCValues.oid_dev_auth_privacy_SHA_512,
                                                             "oid_dev_auth_privacy_SHA_512",
                                                             SignatureAlg.RSA_ISO9796_2_SHA512);

        public static readonly CVCOIDs oid_dsi_ecdsa_SHA_1 = new CVCOIDs(ECVCValues.oid_dsi_ecdsa_SHA_1, "oid_dsi_ecdsa_SHA_1",
                                                    SignatureAlg.ECDSA_SHA1);

        public static readonly CVCOIDs oid_dsi_ecdsa_SHA_224 = new CVCOIDs(ECVCValues.oid_dsi_ecdsa_SHA_224, "oid_dsi_ecdsa_SHA_224",
                                                      SignatureAlg.ECDSA_SHA224);

        public static readonly CVCOIDs oid_dsi_ecdsa_SHA_256 = new CVCOIDs(ECVCValues.oid_dsi_ecdsa_SHA_256, "oid_dsi_ecdsa_SHA_256",
                                                      SignatureAlg.ECDSA_SHA256);

        public static readonly CVCOIDs oid_TA_ECDSA_SHA_1 = new CVCOIDs(ECVCValues.oid_TA_ECDSA_SHA_1, "oid_TA_ECDSA_SHA_1",
                                                   SignatureAlg.ECDSA_SHA1);

        public static readonly CVCOIDs oid_TA_ECDSA_SHA_224 = new CVCOIDs(ECVCValues.oid_TA_ECDSA_SHA_224, "oid_TA_ECDSA_SHA_224",
                                                     SignatureAlg.ECDSA_SHA224);

        public static readonly CVCOIDs oid_TA_ECDSA_SHA_256 = new CVCOIDs(ECVCValues.oid_TA_ECDSA_SHA_256, "oid_TA_ECDSA_SHA_256",
                                                     SignatureAlg.ECDSA_SHA256);

        public static readonly CVCOIDs meac = new CVCOIDs(ECVCValues.meac, "meac", null);


        private Asn1ObjectIdentifier mOid;
        private String mName;
        private SignatureAlg mSignatureAlg;


        CVCOIDs(Asn1ObjectIdentifier aAlgId, String aAlgName, SignatureAlg aSignatureAlg)
        {
            mOid = aAlgId;
            mName = aAlgName;
            mSignatureAlg = aSignatureAlg;
        }

        public String getName()
        {
            return mName;
        }

        public int[] getOID()
        {
            return mOid.mValue;
        }

        public SignatureAlg getSignatureAlg()
        {
            return mSignatureAlg;
        }

        public static CVCOIDs fromOID(Asn1ObjectIdentifier aOID)
        {
            foreach (CVCOIDs alg in CVCOIDs.values())
            {
                if (aOID.mValue.SequenceEqual(alg.getOID()))
                    return alg;
            }
            return null;
        }

        public static CVCOIDs fromName(String algName)
        {
            foreach (CVCOIDs alg in values())
            {
                if (alg.getName().Equals(algName, StringComparison.InvariantCultureIgnoreCase))
                    return alg;
            }
            return null;
        }

        static CVCOIDs[] values()
        {
            Type declaringType = MethodBase.GetCurrentMethod().DeclaringType;
            FieldInfo[] fInfos = declaringType.GetFields();
            List<CVCOIDs> values = new List<CVCOIDs>();
            foreach (FieldInfo fInfo in fInfos)
            {
                if (fInfo.FieldType == declaringType)
                {
                    values.Add((CVCOIDs)fInfo.GetValue(null));
                }
            }
            return values.ToArray();
        }
    }
}
