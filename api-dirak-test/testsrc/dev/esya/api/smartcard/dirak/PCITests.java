package dev.esya.api.smartcard.dirak;

import org.junit.Test;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.DirakLibOps;

import java.io.IOException;

public class PCITests {

    final static String slotID = "75";

    @Test
    public void pinTranslationTest() throws PKCS11Exception, IOException {
        // C_Initialize
        new SmartCard(CardType.DIRAKHSM);

        String message;
        String rvMsg;

        final String msgHeader = "1234";
        final String lmkID = "%" + (slotID.length() < 2 ? "0" : "") + slotID;
        final String PAN = "123456789123";
        final String srcPinBlockFormat = "00";
        final String destPinBlockFormat = "00";
        final String srcZPK;
        final String destZPK;
        final String pinUnderLMK;
        final String pinUnderSrcZPK;
        String pinUnderDestZPK;

        // PIN Generation
        message = msgHeader + "JA" + PAN + "04*0000" + lmkID;
        rvMsg = DirakLibOps.sendPCICommand(message);
        pinUnderLMK = rvMsg.substring(8); // 1234 JB 00 PIN[LMK]
        System.out.println("Send Message.....: " + message);
        System.out.println("Received Message.: " + rvMsg);
        System.out.println("pinUnderLMK......: " + pinUnderLMK);
        System.out.println();

        // Source ZPK Generation
        message = msgHeader + "A00FFFR" + lmkID + "#72T3B00N00";
        rvMsg = DirakLibOps.sendPCICommand(message);
        srcZPK = rvMsg.substring(8, rvMsg.length() - 7); // 1234 A1 00 R ZPK KCV(6)

        System.out.println("Send Message.....: " + message);
        System.out.println("Received Message.: " + rvMsg);
        System.out.println("Source ZPK.......: " + srcZPK);
        System.out.println();

        // Destination ZPK Generation
        message = msgHeader + "A00FFFR" + lmkID + "#72T3B00N00";
        rvMsg = DirakLibOps.sendPCICommand(message);
        destZPK = rvMsg.substring(8, rvMsg.length() - 7); // 1234 A1 00 R ZPK KCV(6)

        System.out.println("Send Message.....: " + message);
        System.out.println("Received Message.: " + rvMsg);
        System.out.println("Destination ZPK..: " + destZPK);
        System.out.println();

        // PIN Translation From LMK to ZPK
        message = msgHeader + "JG" + srcZPK + "00" + PAN + pinUnderLMK + lmkID;
        rvMsg = DirakLibOps.sendPCICommand(message);
        pinUnderSrcZPK = rvMsg.substring(8); // 1234 JH 00 PIN[ZPK]
        System.out.println("Send Message.....: " + message);
        System.out.println("Received Message.: " + rvMsg);
        System.out.println("pinUnderSrcZPK...: " + pinUnderSrcZPK);
        System.out.println();

        for (int i = 0; i < 10; i++) {
            // PIN Translation From soruce ZPK to destination ZPK
            message = msgHeader + "CC" + srcZPK + destZPK + "00" + pinUnderSrcZPK + srcPinBlockFormat + destPinBlockFormat + PAN + lmkID;
            rvMsg = DirakLibOps.sendPCICommand(message);
            pinUnderDestZPK = rvMsg.substring(10, rvMsg.length() - 2); // 1234 CD 00 pinLen pinBlock pinBlockFormat

            System.out.println("Send Message.....: " + message);
            System.out.println("Received Message.: " + rvMsg);
            System.out.println("pinUnderDestZPK..: " + pinUnderDestZPK);
            System.out.println();
        }
    }

    @Test
    public void pinVerificationTest() throws PKCS11Exception, IOException {
        // C_Initialize
        new SmartCard(CardType.DIRAKHSM);

        String message;
        String rvMsg;

        final String msgHeader = "1234";
        final String lmkID = "%" + (slotID.length() < 2 ? "0" : "") + slotID;
        final String PAN = "123456789123";
        final String pinBlockFormat = "00";
        final String ZPK;
        final String pinUnderLMK;
        final String pinUnderZPK;
        String rvPinVerification;

        // ZPK Generation
        message = msgHeader + "A00FFFR" + lmkID + "#72T3B00N00";
        rvMsg = DirakLibOps.sendPCICommand(message);
        ZPK = rvMsg.substring(8, rvMsg.length() - 7); // 1234 A1 00 R ZPK KCV(6)
        System.out.println("Send Message.....: " + message);
        System.out.println("Received Message.: " + rvMsg);
        System.out.println("ZPK..............: " + ZPK);
        System.out.println();

        // PIN Generation
        message = msgHeader + "JA" + PAN + "04*0000" + lmkID;
        rvMsg = DirakLibOps.sendPCICommand(message);
        pinUnderLMK = rvMsg.substring(8); // 1234 JB 00 PIN[LMK]
        System.out.println("Send Message.....: " + message);
        System.out.println("Received Message.: " + rvMsg);
        System.out.println("pinUnderLMK......: " + pinUnderLMK);
        System.out.println();

        // PIN Translation From LMK to ZPK
        message = msgHeader + "JG" + ZPK + pinBlockFormat + PAN + pinUnderLMK + lmkID;
        rvMsg = DirakLibOps.sendPCICommand(message);
        pinUnderZPK = rvMsg.substring(8); // 1234 JH 00 PIN[ZPK]
        System.out.println("Send Message.....: " + message);
        System.out.println("Received Message.: " + rvMsg);
        System.out.println("pinUnderZPK......: " + pinUnderZPK);
        System.out.println();

        for (int i = 0; i < 10; i++) {
            // PIN Verification
            message = msgHeader + "BE" + ZPK + pinUnderZPK + pinBlockFormat + PAN + pinUnderLMK + lmkID;
            rvMsg = DirakLibOps.sendPCICommand(message);
            rvPinVerification = rvMsg.substring(6); // 1234 BF 00

            System.out.println("Send Message.....: " + message);
            System.out.println("Received Message.: " + rvMsg);
            System.out.println("rvPinVerification: " + rvPinVerification);
            System.out.println();
        }
    }
}
