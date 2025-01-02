/* Smart card key transfer program
 *
 * This program copies keys from one smart card to another.
 *
 * Tested smart cards:
 * - from DIRAK to DIRAK
 *   (for key types: AES, RSA public, RSA private)
 * - from DIRAK to UTIMACO
 *   (for key types: AES, RSA public, RSA private)
 * - from UTIMACO to DIRAK
 *   (for key types: AES, RSA public, RSA private)
 */

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11Names;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SlotInfo;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.cardobject.P11Object;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

class Application {
    static BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        System.out.println("You can transfer exportable keys between HSMs with this application.");

        System.out.println("\r\nSource HSM Info: ");
        SmartCard sourceSC = getSmartCard();

        System.out.println("\r\nDestination Card Info: ");
        SmartCard destinationSC = getSmartCard();

        String wrapperLabel = getWrapperLabel(sourceSC, destinationSC);

        transfer(sourceSC, destinationSC, wrapperLabel);
    }

    private static void transfer(SmartCard sourceSC, SmartCard destinationSC, String wrapperLabel) throws Exception {
        while (true) {
            P11Object[] objectInfos = sourceSC.getAllObjectInfos(sourceSC.getLatestSessionID());
            int index = 0;
            System.out.println("\r\nObjects:");
            for (P11Object objectInfo : objectInfos) {
                System.out.println("  " + index + ". " + objectInfo.getLabel() + " " + objectInfo.getType());
                index++;
            }

            try {
                System.out.print("Enter key no (-1 to exit): ");
                String keyNoStr = console.readLine();
                int keyNo = Integer.parseInt(keyNoStr);
                if (keyNo == -1)
                    return;

                transfer(sourceSC, destinationSC, wrapperLabel, objectInfos[keyNo]);
                System.out.println("Transfer successful");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void transfer(SmartCard sourceSC, SmartCard destinationSC, String wrapperLabel, P11Object object) throws Exception {
        if (object.getType() == P11Object.ObjectType.PublicKey) {
            transferPublicKey(sourceSC, destinationSC, object);
        } else {
            transferWrappable(sourceSC, destinationSC, wrapperLabel, object);
        }
    }

    private static void transferWrappable(SmartCard sourceSC, SmartCard destinationSC, String wrapperLabel, P11Object object) throws Exception {
        SecretKeyTemplate wrapperKeyTemplate = new AESKeyTemplate(wrapperLabel);

        byte[] iv = RandomUtil.generateRandom(16);

        CK_MECHANISM wrapMech = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD);
        wrapMech.pParameter = iv;

        // "key to be exported"
        GenericKeyTemplate keyTemplate = new GenericKeyTemplate(object.getLabel());
        keyTemplate.add(object.getAsAttributes());

        // "unwrapped key template"
        GenericKeyTemplate allKeyTemplate = getKeyAttributes(sourceSC, object);

        P11Object.ObjectType objectType = object.getType();
        CardType sourceCardType = sourceSC.getCardType();
        CardType destinationCardType = destinationSC.getCardType();

        if (objectType == P11Object.ObjectType.SecretKey) {
            if (allKeyTemplate.containsAttribute(PKCS11Constants.CKA_DECRYPT) &&
                    allKeyTemplate.getAttribute(PKCS11Constants.CKA_DECRYPT).equals(true) &&
                    allKeyTemplate.containsAttribute(PKCS11Constants.CKA_WRAP) &&
                    allKeyTemplate.getAttribute(PKCS11Constants.CKA_WRAP).equals(true)
            ) {
                System.out.println("CKA_DECRYPT and CKA_WRAP could not be true at the same time because of security vulnerability.");
                System.out.print("Which property do you want to keep? (D)ecrypt, (W)rap: ");

                String choice = console.readLine();

                if (choice.equalsIgnoreCase("D")) {
                    allKeyTemplate.remove(PKCS11Constants.CKA_WRAP);
                } else if (choice.equalsIgnoreCase("W")) {
                    allKeyTemplate.remove(PKCS11Constants.CKA_DECRYPT);
                } else {
                    // todo invalid case handling- do not just throw exception
                    throw new Exception("Key could not transfer!");
                }
            }

            if (allKeyTemplate.containsAttribute(PKCS11Constants.CKA_ENCRYPT) &&
                    allKeyTemplate.getAttribute(PKCS11Constants.CKA_ENCRYPT).equals(true) &&
                    allKeyTemplate.containsAttribute(PKCS11Constants.CKA_UNWRAP) &&
                    allKeyTemplate.getAttribute(PKCS11Constants.CKA_UNWRAP).equals(true)
            ) {
                System.out.println("CKA_ENCRYPT and CKA_UNWRAP could not be true at the same time because of security vulnerability.");
                System.out.print("Which property do you want to keep? (E)ncrypt, (U)nwrap: ");

                String choice = console.readLine();

                if (choice.equalsIgnoreCase("E")) {
                    allKeyTemplate.remove(PKCS11Constants.CKA_UNWRAP);
                } else if (choice.equalsIgnoreCase("U")) {
                    allKeyTemplate.remove(PKCS11Constants.CKA_ENCRYPT);
                } else {
                    // todo invalid case handling- do not just throw exception
                    throw new Exception("Key could not transfer!");
                }
            }

            allKeyTemplate.remove(PKCS11Constants.CKA_KEY_GEN_MECHANISM);
            allKeyTemplate.remove(PKCS11Constants.CKA_VALUE_LEN);

            if (sourceCardType == CardType.OPENDNSSOFTHSM || destinationCardType == CardType.UTIMACO) {
                allKeyTemplate.remove(PKCS11Constants.CKA_LOCAL);
                allKeyTemplate.remove(PKCS11Constants.CKA_ALWAYS_SENSITIVE);
                allKeyTemplate.remove(PKCS11Constants.CKA_NEVER_EXTRACTABLE);
            }
        } else if (objectType == P11Object.ObjectType.PrivateKey) {
            allKeyTemplate.remove(PKCS11Constants.CKA_KEY_GEN_MECHANISM);
            allKeyTemplate.remove(PKCS11Constants.CKA_MODULUS);
            allKeyTemplate.remove(PKCS11Constants.CKA_PUBLIC_EXPONENT);

            if (sourceCardType == CardType.OPENDNSSOFTHSM) {
                allKeyTemplate.remove(PKCS11Constants.CKA_PRIME_1);
                allKeyTemplate.remove(PKCS11Constants.CKA_PRIME_2);
                allKeyTemplate.remove(PKCS11Constants.CKA_LOCAL);
                allKeyTemplate.remove(PKCS11Constants.CKA_COEFFICIENT);
                allKeyTemplate.remove(PKCS11Constants.CKA_ALWAYS_SENSITIVE);
                allKeyTemplate.remove(PKCS11Constants.CKA_NEVER_EXTRACTABLE);
                allKeyTemplate.remove(PKCS11Constants.CKA_EXPONENT_1);
                allKeyTemplate.remove(PKCS11Constants.CKA_EXPONENT_2);
                allKeyTemplate.remove(PKCS11Constants.CKA_PRIVATE_EXPONENT);
            } else if (destinationCardType == CardType.UTIMACO) {
                allKeyTemplate.remove(PKCS11Constants.CKA_LOCAL);
                allKeyTemplate.remove(PKCS11Constants.CKA_MODULUS_BITS);
                allKeyTemplate.remove(PKCS11Constants.CKA_NEVER_EXTRACTABLE);
                allKeyTemplate.remove(PKCS11Constants.CKA_ALWAYS_SENSITIVE);
            }
        }

        byte[] wrappedKey = sourceSC.wrapKey(sourceSC.getLatestSessionID(), wrapMech, wrapperKeyTemplate, keyTemplate);
        destinationSC.unwrapKey(destinationSC.getLatestSessionID(), wrapMech, wrapperLabel, wrappedKey, allKeyTemplate);
    }

    /* RSA is tested. EC is not tested yet. */
    private static void transferPublicKey(SmartCard sourceSC, SmartCard destinationSC, P11Object object) throws Exception {
        GenericKeyTemplate publicRSAKeyAttributes = getKeyAttributes(sourceSC, object);

        // (attributes that commonly cause template exceptions)
        publicRSAKeyAttributes.remove(PKCS11Constants.CKA_MODULUS_BITS);
        publicRSAKeyAttributes.remove(PKCS11Constants.CKA_KEY_GEN_MECHANISM);
        publicRSAKeyAttributes.remove(PKCS11Constants.CKA_LOCAL);

        ((PKCS11Ops) destinationSC.getCardType().getCardTemplate().getPKCS11Ops()).getmPKCS11().C_CreateObject(destinationSC.getLatestSessionID(), publicRSAKeyAttributes.getAttributesAsArr());
    }

    private static long getObjectId(SmartCard sc, P11Object objectInfo) throws PKCS11Exception, ESYAException {
        CK_ATTRIBUTE[] attrs = objectInfo.getAsAttributes();
        long[] objIds = sc.findObjects(sc.getLatestSessionID(), attrs);
        if (objIds.length > 1)
            throw new ESYAException("Birden fazla nesne bulundu. Adet: " + objIds.length);
        return objIds[0];
    }

    private static GenericKeyTemplate getKeyAttributes(SmartCard sourceSC, P11Object objectInfo) throws PKCS11Exception, ESYAException {
        long objId = getObjectId(sourceSC, objectInfo);

        GenericKeyTemplate keyTemplate = new GenericKeyTemplate(objectInfo.getLabel());
        long[] attrTypes = PKCS11Names.getAttributeTypes();
        for (long attrType : attrTypes) {
            try {
                CK_ATTRIBUTE[] attrs = new CK_ATTRIBUTE[1];
                attrs[0] = new CK_ATTRIBUTE(attrType);
                sourceSC.getAttributeValue(sourceSC.getLatestSessionID(), objId, attrs);
                if (attrs[0].pValue != null)
                    keyTemplate.add(attrs[0]);
            } catch (PKCS11Exception ex) {
                // continue;
            }
        }

        return keyTemplate;
    }

    private static String getWrapperLabel(SmartCard sourceSC, SmartCard destinationSC) throws Exception {
        System.out.print("\r\nCreate wrapper/unwrapper key? (Y/N): ");
        String yesno = console.readLine();

        if (yesno.equalsIgnoreCase("Y")) {
            System.out.println("Creating Wrapper Key");

            System.out.print("Enter Key Name: ");
            String wrapperLabel = console.readLine().trim();

            System.out.print("Enter Key Size: ");
            String keySizeStr = console.readLine();
            int keySize = Integer.parseInt(keySizeStr);

            createWrapperKey(sourceSC, destinationSC, wrapperLabel, keySize);

            System.out.println("Wrapper key created");

            return wrapperLabel;
        } else if (yesno.equalsIgnoreCase("N")) {
            System.out.print("Enter existing wrapper name: ");
            String wrapperLabel = console.readLine();
            return wrapperLabel.trim();
        } else {
            System.out.println("Unknown Input");
            return getWrapperLabel(sourceSC, destinationSC);
        }
    }

    private static void createWrapperKey(SmartCard sourceSC, SmartCard destinationSC, String wrapperLabel, int keySize) throws Exception {
        byte[] aesbytes = RandomUtil.generateRandom(keySize / 8);

        SecretKeyTemplate aesKeyTemplate = new AESKeyTemplate(wrapperLabel);
        aesKeyTemplate = aesKeyTemplate.getAsWrapperTemplate().getAsUnwrapperTemplate();
        aesKeyTemplate = aesKeyTemplate.getAsImportTemplate(aesbytes);

        sourceSC.importSecretKey(sourceSC.getLatestSessionID(), aesKeyTemplate);
        destinationSC.importSecretKey(destinationSC.getLatestSessionID(), aesKeyTemplate);
    }

    private static SmartCard getSmartCard() throws Exception {
        System.out.print("- DLL name: ");
        String dllName = console.readLine();
        CardType cardType = CardType.getCardType(dllName);

        SmartCard sc = new SmartCard(cardType);

        List<SlotInfo> slotInfoList = sc.getSlotInfoList();
        System.out.println(slotInfoList.size() + " Slots: ");
        for (SlotInfo slotInfo : slotInfoList) {
            String slotLabel = slotInfo.getSlotLabel();
            System.out.println("  " + slotInfo.getSlotId() + " - " + ((slotLabel == null || slotLabel.length() == 0) ? "<no label>" : slotLabel));
        }

        System.out.print("- Slot Number: ");
        String slotStr = console.readLine();
        long slot = Long.parseLong(slotStr);

        System.out.print("- PIN: ");
        String PIN = console.readLine();

        return getSmartCard(dllName, slot, PIN);
    }

    private static SmartCard getSmartCard(String dllName, long slot, String pin) throws Exception {
        CardType cardType = CardType.getCardType(dllName);

        SmartCard sc = new SmartCard(cardType);
        long session = sc.openSession(slot);

        sc.login(session, pin);

        P11Object[] objectInfos = sc.getAllObjectInfos(session);

        System.out.printf("\r\nObjects (%d): %n", objectInfos.length);
        for (P11Object objectInfo : objectInfos) {
            System.out.println("  " + objectInfo.getLabel() + " (" + objectInfo.getType() + ")");
        }

        return sc;
    }
}
