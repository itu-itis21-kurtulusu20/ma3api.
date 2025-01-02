package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

/**
 * Created by orcun.ertugrul on 25-Oct-17.
 */

/*
*  sun.security.pkcs11.wrapper.PKCS11Constants sınıfı bütün constant'ları kapsamıyor. Olmayan sabit tanımları bu sınıfı ekleyebiliriz.
* **/

public class PKCS11ConstantsExtended {
    public static final long  CKG_MGF1_SHA224               = 0x00000005L;
    public static final long  CKM_SHA224                    = 0x00000255L;
    public static final long  CKM_SHA224_RSA_PKCS_PSS       = 0x00000047L;

    public static final long  CKM_SHA256_RSA_PKCS_PSS       = 0x00000043L;
    public static final long  CKG_MGF1_SHA256               = 0x00000002L;

    public static final long  CKM_SHA384_RSA_PKCS_PSS       = 0x00000044L;
    public static final long  CKG_MGF1_SHA384               = 0x00000003L;

    public static final long  CKM_SHA512_RSA_PKCS_PSS       = 0x00000045L;
    public static final long  CKG_MGF1_SHA512               = 0x00000004L;

    public static final long  CKF_ARRAY_ATTRIBUTE           = 0x40000000L;
    public static final long  CKA_ALLOWED_MECHANISMS        = (CKF_ARRAY_ATTRIBUTE|0x00000600L);

    public static final long  CKM_AES_CMAC                  = 0x0000108AL;
    public static final long  CKM_AES_KEY_WRAP              = 0x00002109L;
    public static final long  CKM_AES_KEY_WRAP_PAD          = 0x0000210AL;

    public static final long CKD_SHA224_KDF                 = 0x00000005L;
    public static final long CKD_SHA256_KDF                 = 0x00000006L;
    public static final long CKD_SHA384_KDF                 = 0x00000007L;
    public static final long CKD_SHA512_KDF                 = 0x00000008L;

    public static final long CKK_EC_EDWARDS                 = 0x00000040L;
    public static final long CKM_EDDSA                      = 0x00001057L;
}
