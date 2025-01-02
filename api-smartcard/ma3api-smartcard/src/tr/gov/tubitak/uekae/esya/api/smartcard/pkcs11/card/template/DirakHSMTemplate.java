package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.DirakHSMOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.IPKCS11Ops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by orcun.ertugrul on 11-Oct-17.
 */
public class DirakHSMTemplate extends CardTemplate
{
    // yeni bir değişken eklendiğinde getVendorSpecificAttributes() fonksiyonunu güncellemek gerekiyor.
    public static final long CKA_SENSITIVE_EC_PARAMS = 0x80000001;
    public static final long CKA_SENSITIVE_EC_POINT = 0x80000002;
    public static final long CKA_KCV_NON_PCI = 0x80000003;
    public static final long CKA_XMSS_HASH_ID = 0x80000004;
    public static final long CKA_STATUS = 0x80000005;
    public static final long CKA_CREATE_DATE = 0x80000006;
    // public static final long CKA_BIP32_CHAIN_CODE = 0x80000007;
    public static final long CKA_BIP32_VERSION_BYTES = 0x80000008;
    public static final long CKA_BIP32_CHILD_INDEX = 0x80000009;
    public static final long CKA_BIP32_CHILD_DEPTH = 0x8000000A;
    public static final long CKA_BIP32_ID = 0x8000000B;
    public static final long CKA_BIP32_FINGERPRINT = 0x8000000C;
    public static final long CKA_BIP32_PARENT_FINGERPRINT = 0x8000000D;

    public static final long CKM_EQPROOF = 0x80000007;
    public static final long CKM_ELGAMAL_EC = 0x80000008;

    public static final long CKK_BIP32 = 0x80000003;

    private static List<String> ATR_HASHES = new ArrayList<String>();

    public DirakHSMTemplate()
    {
        super(CardType.DIRAKHSM);
    }

    public synchronized IPKCS11Ops getPKCS11Ops()
    {
        if(mIslem == null)
            mIslem = new DirakHSMOps();
        return mIslem;
    }

    public void applyTemplate(SecretKeyTemplate template) throws SmartCardException
    {
        if(template.isWrapperOrUnWrapper() == false) {
            template.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true));
            template.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, true));
        }
    }

    public void applyTemplate(KeyPairTemplate template) throws SmartCardException {
        if (template instanceof ECKeyPairTemplate) {
            ECKeyTemplate ecPublicKeyTemplate = (ECKeyTemplate) template.getPublicKeyTemplate();
            ECKeyTemplate ecPrivateKeyTemplate = (ECKeyTemplate) template.getPrivateKeyTemplate();

            if (ecPublicKeyTemplate != null) {
                if (ecPublicKeyTemplate.isSecretECCurve()) {
                    ecPublicKeyTemplate.add(new CK_ATTRIBUTE(CKA_SENSITIVE_EC_PARAMS, true));
                    ecPublicKeyTemplate.add(new CK_ATTRIBUTE(CKA_SENSITIVE_EC_POINT, true));
                }
            }

            if (ecPrivateKeyTemplate != null) {
                if (ecPrivateKeyTemplate.isSecretECCurve()) {
                    ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(CKA_SENSITIVE_EC_PARAMS, true));
                }
            }
        }
    }

    public String[] getATRHashes()
    {
        return ATR_HASHES.toArray(new String[0]);
    }

    public static void addATR(String aATR)
    {
        ATR_HASHES.add(aATR);
    }

    public Map<Long,String> getVendorSpecificAttributeTypesWithNames(){
        Map<Long,String> attributeTypesWithNames = new HashMap<>();
        attributeTypesWithNames.put(CKA_SENSITIVE_EC_PARAMS, "CKA_SENSITIVE_EC_PARAMS");
        attributeTypesWithNames.put(CKA_SENSITIVE_EC_POINT, "CKA_SENSITIVE_EC_POINT");
        attributeTypesWithNames.put(CKA_KCV_NON_PCI, "CKA_KCV_NON_PCI");
        attributeTypesWithNames.put(CKA_XMSS_HASH_ID, "CKA_XMSS_HASH_ID");
        attributeTypesWithNames.put(CKA_STATUS, "CKA_STATUS");
        attributeTypesWithNames.put(CKA_CREATE_DATE, "CKA_CREATE_DATE");
        // attributeTypesWithNames.put(CKA_BIP32_CHAIN_CODE, "CKA_BIP32_CHAIN_CODE");
        attributeTypesWithNames.put(CKA_BIP32_VERSION_BYTES, "CKA_BIP32_VERSION_BYTES");
        attributeTypesWithNames.put(CKA_BIP32_CHILD_INDEX, "CKA_BIP32_CHILD_INDEX");
        attributeTypesWithNames.put(CKA_BIP32_CHILD_DEPTH, "CKA_BIP32_CHILD_DEPTH");
        attributeTypesWithNames.put(CKA_BIP32_ID, "CKA_BIP32_ID");
        attributeTypesWithNames.put(CKA_BIP32_FINGERPRINT, "CKA_BIP32_FINGERPRINT");
        attributeTypesWithNames.put(CKA_BIP32_PARENT_FINGERPRINT, "CKA_BIP32_PARENT_FINGERPRINT");

        return attributeTypesWithNames;
    }

    public Map<Long,Class> getVendorSpecificAttributeValuesType(){
        Map<Long,Class> attributeValuesType = new HashMap<>();
        attributeValuesType.put(CKA_SENSITIVE_EC_PARAMS, boolean.class);
        attributeValuesType.put(CKA_SENSITIVE_EC_POINT, boolean.class);
        attributeValuesType.put(CKA_KCV_NON_PCI, byte[].class);
        attributeValuesType.put(CKA_XMSS_HASH_ID, long.class);
        attributeValuesType.put(CKA_STATUS, boolean.class);
        attributeValuesType.put(CKA_CREATE_DATE, char[].class);
        // attributeValuesType.put(CKA_BIP32_CHAIN_CODE, byte[].class);
        attributeValuesType.put(CKA_BIP32_VERSION_BYTES, byte[].class);
        attributeValuesType.put(CKA_BIP32_CHILD_INDEX, byte[].class);
        attributeValuesType.put(CKA_BIP32_CHILD_DEPTH, byte[].class);
        attributeValuesType.put(CKA_BIP32_ID, byte[].class);
        attributeValuesType.put(CKA_BIP32_FINGERPRINT, byte[].class);
        attributeValuesType.put(CKA_BIP32_PARENT_FINGERPRINT, byte[].class);

        return attributeValuesType;
    }
}
