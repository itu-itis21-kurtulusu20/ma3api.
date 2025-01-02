package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.smartcard.bundle.SmartCardI18n;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ExceptionFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure.CK_ATTRIBUTE_STRUCTURE;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure.CK_MECHANISM_STRUCTURE;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DirakLibOps {

    public enum CryptoOperation {
        SIGN,
        ENCRYPT,
        DECRYPT
    }

    public static int UNWRAPANDOP_MAXRESULTLEN = 1024;

    public static void userLogin(String username, String password) {
        DirakLibJNAConnector.dirakP11lib.Dirak_UserLogin("srpP", username, password);
    }

    public static void userLogin(String username, String password, String hsmIP) {
        DirakLibJNAConnector.dirakP11lib.Dirak_UserLoginIp("srpP", username, password, hsmIP);
    }

    public static void userLogout(String username) {
        DirakLibJNAConnector.dirakP11lib.Dirak_UserLogout(username);
    }

    public static void userLogout(String username, String hsmIp) {
        DirakLibJNAConnector.dirakP11lib.Dirak_UserLogoutIp(username, hsmIp);
    }

    public static int OpHistoryLen = 32768;
    public static String getOperationHistory() throws SmartCardException {
        IntByReference opHistoryLen = new IntByReference(OpHistoryLen);
        byte [] history = new byte[OpHistoryLen];
        int result = DirakLibJNAConnector.dirakP11lib.Dirak_GetOperationInfoHistory(history, opHistoryLen);

        if(result == PKCS11Constants.CKR_OK) {
            return new String(history, 0, opHistoryLen.getValue(), java.nio.charset.StandardCharsets.US_ASCII);
        } else if(result == PKCS11Constants.CKR_BUFFER_TOO_SMALL) {
            history = new byte[opHistoryLen.getValue()];
            result = DirakLibJNAConnector.dirakP11lib.Dirak_GetOperationInfoHistory(history, opHistoryLen);
            if (result == PKCS11Constants.CKR_OK) {
                OpHistoryLen = OpHistoryLen + 1024;
                return new String(history, 0, opHistoryLen.getValue(), java.nio.charset.StandardCharsets.US_ASCII);
            }
        }

        throw new SmartCardException("Can not get operation history. Error Code: 0x" + Integer.toHexString(result));
    }

    public static byte[] unwrapAndOP(
        long sessionID,
        CK_MECHANISM unwrapMechanism,
        long unwrapperKeyID,
        byte[] wrappedKey,
        CK_ATTRIBUTE[] unwrapTemplate,
        CryptoOperation operation,
        CK_MECHANISM operationMechanism,
        byte[] operationData
    ) throws SmartCardException {
        CK_ATTRIBUTE_STRUCTURE[] unwrapTemplateStructure = CK_ATTRIBUTE_STRUCTURE.newArrayFilled(unwrapTemplate);

        return DirakLibOps.unwrapAndOP(
            new NativeLong(sessionID, true),
            new CK_MECHANISM_STRUCTURE(unwrapMechanism),
            new NativeLong(unwrapperKeyID, true),
            wrappedKey,
            new NativeLong(wrappedKey.length, true),
            unwrapTemplateStructure,
            new NativeLong(unwrapTemplate.length, true),
            new NativeLong(operation.ordinal(), true),
            new CK_MECHANISM_STRUCTURE(operationMechanism),
            operationData,
            new NativeLong(operationData.length, true)
        );
    }

    public static String sendPCICommand(String message) {
        byte[] response = new byte[2048];
        IntByReference responseLen = new IntByReference();

        int retVal = DirakLibJNAConnector.dirakP11lib.sendPCICommand(
            message,
            message.length(),
            response,
            responseLen
        );

        return new String(response, 0, responseLen.getValue(), StandardCharsets.US_ASCII);
    }

    protected static byte[] unwrapAndOP(
        NativeLong hSession,
        CK_MECHANISM_STRUCTURE pUnwrappedMech,
        NativeLong hUnwrappedKeyId,
        byte[] pWrappedKey,
        NativeLong ulWrappedKeyLen,
        Structure[] pUnwrapTemplate,
        NativeLong ulAttributeCount,
        NativeLong ulOperation,
        CK_MECHANISM_STRUCTURE pOperationMech,
        byte[] pOperationData,
        NativeLong ulOperationDataLen
    ) throws SmartCardException {
        int maxResultLen = DirakLibOps.UNWRAPANDOP_MAXRESULTLEN;
        byte[] pResultData = new byte[maxResultLen];
        int[] pResultDataLen = new int[]{maxResultLen};

        long returnCode = DirakLibJNAConnector.dirakP11lib.C_UnwrapAndOP(
            hSession,
            pUnwrappedMech,
            hUnwrappedKeyId,
            pWrappedKey,
            ulWrappedKeyLen,
            pUnwrapTemplate,
            ulAttributeCount,
            ulOperation,
            pOperationMech,
            pOperationData,
            ulOperationDataLen,
            pResultData,
            pResultDataLen
        );

        if (returnCode != PKCS11Constants.CKR_OK) {
            throw new SmartCardException("Dirak C_UnwrapAndOP Error", PKCS11ExceptionFactory.getPKCS11Exception(returnCode));
        }

        if (pResultDataLen[0] < maxResultLen) {
            return Arrays.copyOf(pResultData, pResultDataLen[0]);
        } else if (pResultDataLen[0] == maxResultLen) {
            return pResultData;
        } else {
            throw new SmartCardException(SmartCardI18n.getMsg(E_KEYS.EXCEEDING_DATA_LENGTH));
        }
    }

    public interface DirakLibJNAConnector extends Library {

        DirakLibJNAConnector dirakP11lib = (DirakLibJNAConnector) Native.loadLibrary((Platform.isWindows() ? "dirakp11-64.dll" : "libdirakp11-64.so"), DirakLibJNAConnector.class);

        int Dirak_ResetSrpUserPin(String newPass, String newPass2);

        int Dirak_ResetSrpUserPinIp(String oldPass, String newPass, String newPass2, String hsmIP);

        int Dirak_GetLoggedInUsers(byte[] username);

        int Dirak_SetPinPermission(long slotId, int permission);

        int Dirak_GetPinPermission(long slotId);

        int Dirak_SetTokenName(long slotId, String tokenName);

        long Dirak_UserLogin(String userType, String username, String password);

        long Dirak_UserLoginIp(String userType, String username, String password, String hsmIP);

        long Dirak_UserLogout(String username);

        long Dirak_UserLogoutIp(String username, String hsmIp);

        int Dirak_GetOperationInfoHistory(byte [] log, IntByReference logLen);

        int C_UnwrapAndOP(
            NativeLong hSession,
            CK_MECHANISM_STRUCTURE pUnwrappedMech,
            NativeLong hUnwrappedKeyId,
            byte[] pWrappedKey,
            NativeLong ulWrappedKeyLen,
            Structure[] pUnwrapTemplate,
            NativeLong ulAttributeCount,
            NativeLong ulOperation,
            CK_MECHANISM_STRUCTURE pOperationMech,
            byte[] pOperationData,
            NativeLong ulOperationDataLen,
            byte[] pResultData,
            int[] pResultDataLen
        );

        int C_SignInit(
            long hSession,
            CK_MECHANISM_STRUCTURE pMechanism,
            long hKey
        );

        int C_VerifyInit(
            long hSession,
            CK_MECHANISM_STRUCTURE pMechanism,
            long hKey
        );

        int sendPCICommand(
            String message,
            long messageLen,
            byte[] response,
            IntByReference responseLen
        );
    }
}
