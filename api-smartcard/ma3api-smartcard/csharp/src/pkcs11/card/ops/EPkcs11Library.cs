using System;
using System.Runtime.InteropServices;
using System.Text;
using iaik.pkcs.pkcs11.wrapper;
using Net.Pkcs11Interop.Common;
using Net.Pkcs11Interop.LowLevelAPI41;
using PKCS11Exception = iaik.pkcs.pkcs11.wrapper.PKCS11Exception;
using CK_ATTRIBUTE = Net.Pkcs11Interop.LowLevelAPI41.CK_ATTRIBUTE;

using CK_RSA_PKCS_PSS_PARAMS = Net.Pkcs11Interop.LowLevelAPI41.MechanismParams.CK_RSA_PKCS_PSS_PARAMS;
using CK_RSA_PKCS_OAEP_PARAMS = Net.Pkcs11Interop.LowLevelAPI41.MechanismParams.CK_RSA_PKCS_OAEP_PARAMS;

using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
using tr.gov.tubitak.uekae.esya.api.smartcard.src.util;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops
{
    public class EPkcs11Library : Pkcs11Library
    {
        public EPkcs11Library(string libraryPath) : base(libraryPath)
        {
        }

        //HELPER FUNCTIONS
        public static long[] uintArrayToLongArray(uint[] arr)
        {
            long[] larr = new long[arr.Length];
            for (int i = 0; i < arr.Length; i++)
                larr[i] = (long)arr[i];

            return larr;
        }

        public static char[] byteArrayToCharArray(byte[] arr)
        {
            char[] c_arr = new char[arr.Length];
            for (int i = 0; i < arr.Length; i++)
            {
                c_arr[i] = (char)arr[i];
            }
            return c_arr;
        }

        public static byte[] charArrayToByteArray(char[] arr)
        {
            byte[] b_arr = new byte[arr.Length];
            for (int i = 0; i < arr.Length; i++)
            {
                b_arr[i] = (byte)arr[i];
            }
            return b_arr;
        }

        public static CK_ATTRIBUTE createAttribute(long type, object value)
        {
            if(value == null)
                return CkaUtils.CreateAttribute((CKA)type);

            if (value.GetType() == typeof(int) || value.GetType() == typeof(long))
                return CkaUtils.CreateAttribute((CKA)type, Convert.ToUInt32(value));
            if (value.GetType() == typeof(bool))
                return CkaUtils.CreateAttribute((CKA)type, (bool)value);
            if (value.GetType() == typeof(string))
                return CkaUtils.CreateAttribute((CKA)type, (string)value);
            if (value.GetType() == typeof(byte[]))
                return CkaUtils.CreateAttribute((CKA)type, (byte[])value);
            if (value.GetType() == typeof(DateTime))
                return CkaUtils.CreateAttribute((CKA)type, (DateTime)value);
            if (value.GetType() == typeof(CK_ATTRIBUTE[]))
                return CkaUtils.CreateAttribute((CKA)type, (CK_ATTRIBUTE[])value);
            if (value.GetType() == typeof(uint[]))
                return CkaUtils.CreateAttribute((CKA)type, (uint[])value);
            if (value.GetType() == typeof(CKM[]))
                return CkaUtils.CreateAttribute((CKA)type, (CKM[])value);
            Console.WriteLine("ATTRIBUTE COULD NOT BE HANDLED PROPERLY");
            return new CK_ATTRIBUTE();
        }

        public static CK_ATTRIBUTE[] fromIaikAttributeArrayToInteropAttributeArray(iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] iaikAttArray)
        {
            CK_ATTRIBUTE[] interopAttArray = new CK_ATTRIBUTE[iaikAttArray.Length];
            for (int i = 0; i < iaikAttArray.Length; i++)
                interopAttArray[i] = createAttribute(iaikAttArray[i].type, iaikAttArray[i].pValue);

            return interopAttArray;
        }

        public static CK_ATTRIBUTE[] fromAttributeNetArrayToInteropAttributeArray(CK_ATTRIBUTE_NET[] AttNetArray)
        {
            CK_ATTRIBUTE[] interopAttArray = new CK_ATTRIBUTE[AttNetArray.Length];
            for (int i = 0; i < AttNetArray.Length; i++)
                interopAttArray[i] = createAttribute(AttNetArray[i].type, AttNetArray[i].pValue);

            return interopAttArray;
        }

        public static void freeCKAttributeMemory(CK_ATTRIBUTE[] arr)
        {
            //Free Memory taken by attributes
            for (int i = 0; i < arr.Length; i++)
            {
                UnmanagedMemory.Free(ref arr[i].value);
                arr[i].valueLen = 0;
            }
        }

        public static Net.Pkcs11Interop.LowLevelAPI41.CK_MECHANISM fromIaikMechanismToInteropMechanism(
            iaik.pkcs.pkcs11.wrapper.CK_MECHANISM iaikMech)
        {
            
            if (iaikMech.pParameter == null)
                return CkmUtils.CreateMechanism((CKM)iaikMech.mechanism);

            if (iaikMech.pParameter.GetType() == typeof(iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_PSS_PARAMS))
            {
                CK_RSA_PKCS_PSS_PARAMS p = new CK_RSA_PKCS_PSS_PARAMS();
                p.HashAlg = (uint)((iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_PSS_PARAMS)iaikMech.pParameter).hashAlg;
                p.Len = (uint)((iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_PSS_PARAMS)iaikMech.pParameter).sLen;
                p.Mgf = (uint)((iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_PSS_PARAMS)iaikMech.pParameter).mgf;
                return CkmUtils.CreateMechanism((CKM)iaikMech.mechanism, p);
            }
            if (iaikMech.pParameter.GetType() == typeof(iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS))
            {
                CK_RSA_PKCS_OAEP_PARAMS p = new CK_RSA_PKCS_OAEP_PARAMS();
                p.HashAlg = (uint)((iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS)iaikMech.pParameter).hashAlg;
                p.Mgf = (uint)((iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS)iaikMech.pParameter).mgf;
                p.Source = (uint)((iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS)iaikMech.pParameter).source;
                p.SourceDataLen = 0;

                if (p.SourceData != IntPtr.Zero)
                {
                    int dataLen = ((iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS)iaikMech.pParameter).pSourceData
                        .Length;
                    p.SourceDataLen = (uint)dataLen;
                    IntPtr ptr = Marshal.AllocHGlobal(dataLen);
                    Marshal.Copy(((iaik.pkcs.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS)iaikMech.pParameter).pSourceData, 0, ptr, dataLen);
                    p.SourceData = ptr;
                }

                return CkmUtils.CreateMechanism((CKM)iaikMech.mechanism, p);
            }

            if (iaikMech.pParameter.GetType() == typeof(byte[]))
                return CkmUtils.CreateMechanism((CKM)iaikMech.mechanism, (byte[])iaikMech.pParameter);

            return CkmUtils.CreateMechanism((CKM)iaikMech.mechanism, iaikMech.pParameter);
        }
        
        //HELPER FUNCTIONS

        public void C_Initialize(CK_C_INITIALIZE_ARGS args)
        {
            CKR rv = base.C_Initialize(args);
            if (rv != CKR.CKR_OK && rv != CKR.CKR_CRYPTOKI_ALREADY_INITIALIZED)
                throw new PKCS11Exception((long)rv);
        }

        public long[] C_GetSlotList(bool tokenPresent)
        {
            uint slotCount = 0;
            CKR rv = C_GetSlotList(tokenPresent, null, ref slotCount);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            uint[] slotList = new uint[slotCount];
            rv = C_GetSlotList(tokenPresent, slotList, ref slotCount);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            Array.Resize(ref slotList, (int)slotCount);

            return uintArrayToLongArray(slotList);
        }

        public iaik.pkcs.pkcs11.wrapper.CK_SLOT_INFO C_GetSlotInfo(long aSlotID)
        {
            Net.Pkcs11Interop.LowLevelAPI41.CK_SLOT_INFO temp = new Net.Pkcs11Interop.LowLevelAPI41.CK_SLOT_INFO();
            CKR rv = C_GetSlotInfo((uint)aSlotID, ref temp);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            iaik.pkcs.pkcs11.wrapper.CK_SLOT_INFO slotInfo = new iaik.pkcs.pkcs11.wrapper.CK_SLOT_INFO();

            slotInfo.flags = (long)temp.Flags;
            slotInfo.firmwareVersion = new iaik.pkcs.pkcs11.wrapper.CK_VERSION();
            slotInfo.firmwareVersion.major = temp.FirmwareVersion.Major[0];
            slotInfo.firmwareVersion.minor = temp.FirmwareVersion.Minor[0];
            slotInfo.hardwareVersion = new iaik.pkcs.pkcs11.wrapper.CK_VERSION();
            slotInfo.hardwareVersion.major = temp.HardwareVersion.Major[0];
            slotInfo.hardwareVersion.minor = temp.HardwareVersion.Minor[0];
            slotInfo.manufacturerID = byteArrayToCharArray(temp.ManufacturerId);
            slotInfo.slotDescription = byteArrayToCharArray(temp.SlotDescription);

            return slotInfo;
        }

        public iaik.pkcs.pkcs11.wrapper.CK_TOKEN_INFO C_GetTokenInfo(long aSlotID)
        {
            Net.Pkcs11Interop.LowLevelAPI41.CK_TOKEN_INFO temp = new Net.Pkcs11Interop.LowLevelAPI41.CK_TOKEN_INFO();
            CKR rv = C_GetTokenInfo((uint)aSlotID, ref temp);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            iaik.pkcs.pkcs11.wrapper.CK_TOKEN_INFO tokenInfo = new iaik.pkcs.pkcs11.wrapper.CK_TOKEN_INFO();
            tokenInfo.flags = (long)temp.Flags;
            tokenInfo.manufacturerID = byteArrayToCharArray(temp.ManufacturerId);
            tokenInfo.hardwareVersion = new iaik.pkcs.pkcs11.wrapper.CK_VERSION();
            tokenInfo.hardwareVersion.major = temp.HardwareVersion.Major[0];
            tokenInfo.hardwareVersion.minor = temp.HardwareVersion.Minor[0];
            tokenInfo.firmwareVersion = new iaik.pkcs.pkcs11.wrapper.CK_VERSION();
            tokenInfo.firmwareVersion.major = temp.FirmwareVersion.Major[0];
            tokenInfo.firmwareVersion.minor = temp.FirmwareVersion.Minor[0];
            tokenInfo.label = byteArrayToCharArray(temp.Label);
            tokenInfo.model = byteArrayToCharArray(temp.Model);
            tokenInfo.serialNumber = byteArrayToCharArray(temp.SerialNumber);
            tokenInfo.ulFreePrivateMemory = temp.FreePrivateMemory;
            tokenInfo.ulFreePublicMemory = temp.FreePublicMemory;
            tokenInfo.ulTotalPrivateMemory = temp.TotalPrivateMemory;
            tokenInfo.ulTotalPublicMemory = temp.TotalPrivateMemory;
            tokenInfo.ulMaxPinLen = temp.MaxPinLen;
            tokenInfo.ulMinPinLen = temp.MinPinLen;
            tokenInfo.ulSessionCount = temp.SessionCount;
            tokenInfo.ulRwSessionCount = temp.RwSessionCount;
            tokenInfo.ulMaxRwSessionCount = temp.MaxRwSessionCount;
            tokenInfo.ulMaxSessionCount = temp.MaxSessionCount;
            tokenInfo.utcTime = byteArrayToCharArray(temp.UtcTime);

            return tokenInfo;
        }

        public iaik.pkcs.pkcs11.wrapper.CK_SESSION_INFO C_GetSessionInfo(long aSessionID)
        {
            Net.Pkcs11Interop.LowLevelAPI41.CK_SESSION_INFO
                temp = new Net.Pkcs11Interop.LowLevelAPI41.CK_SESSION_INFO();
            CKR rv = C_GetSessionInfo((uint)aSessionID, ref temp);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            iaik.pkcs.pkcs11.wrapper.CK_SESSION_INFO sessionInfo = new iaik.pkcs.pkcs11.wrapper.CK_SESSION_INFO();
            sessionInfo.flags = temp.Flags;
            sessionInfo.slotID = temp.SlotId;
            sessionInfo.state = temp.State;
            sessionInfo.ulDeviceError = temp.DeviceError;

            return sessionInfo;
        }

        public long[] C_GetMechanismList(long aSlotID)
        {
            uint count = 0;
            CKR rv = C_GetMechanismList((uint)aSlotID, null, ref count);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            CKM[] temp = new CKM[count];
            rv = C_GetMechanismList((uint)aSlotID, temp, ref count);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            long[] mechList = new long[count];
            for (int i = 0; i < count; i++)
                mechList[i] = (long)temp[i];

            return mechList;
        }

        public long C_OpenSession(long slotID, long flags)
        {
            uint sessID = 0;
            CKR rv = C_OpenSession((uint)slotID, (uint)flags, IntPtr.Zero, IntPtr.Zero, ref sessID);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            return sessID;
        }

        public void C_CloseSession(long aSessionID)
        {
            CKR rv = base.C_CloseSession((uint)aSessionID);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_Login(long aSessionID, long userType, String aCardPIN)
        {
            CKR rv = C_Login((uint)aSessionID, (CKU)userType, Encoding.UTF8.GetBytes(aCardPIN), (uint)aCardPIN.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_Logout(long aSessionID)
        {
            CKR rv = base.C_Logout((uint)aSessionID);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public long C_CreateObject(long aSessionID, iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] template)
        {
            CK_ATTRIBUTE[] bTemplate = fromIaikAttributeArrayToInteropAttributeArray(template);

            uint objID = 0;
            CKR rv = C_CreateObject((uint)aSessionID, bTemplate, (uint)template.Length, ref objID);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            EPkcs11Library.freeCKAttributeMemory(bTemplate);

            return objID;
        }

        public long[] C_GenerateKeyPair(long aSessionID, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM pMechanism,
            iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] pubKeyTemplate, iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] priKeyTemplate)
        {
            CK_ATTRIBUTE[] pubTemplate = fromIaikAttributeArrayToInteropAttributeArray(pubKeyTemplate);
            CK_ATTRIBUTE[] priTemplate = fromIaikAttributeArrayToInteropAttributeArray(priKeyTemplate);

            Net.Pkcs11Interop.LowLevelAPI41.CK_MECHANISM mech = fromIaikMechanismToInteropMechanism(pMechanism);

            uint pubKeyId = 0, priKeyId = 0;
            CKR rv = C_GenerateKeyPair((uint)aSessionID, ref mech, pubTemplate,
                (uint)pubTemplate.Length, priTemplate, (uint)priTemplate.Length, ref pubKeyId, ref priKeyId);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            freeCKAttributeMemory(pubTemplate);
            freeCKAttributeMemory(priTemplate);

            return new long[] {pubKeyId, priKeyId};
        }

        public long[] C_GenerateKeyPair(long aSessionID, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM pMechanism,
            CK_ATTRIBUTE[] pubTemplate, CK_ATTRIBUTE[] priTemplate)
        {
            Net.Pkcs11Interop.LowLevelAPI41.CK_MECHANISM mech = fromIaikMechanismToInteropMechanism(pMechanism);

            uint pubKeyId = 0, priKeyId = 0;
            CKR rv = C_GenerateKeyPair((uint)aSessionID, ref mech, pubTemplate,
                (uint)pubTemplate.Length, priTemplate, (uint)priTemplate.Length, ref pubKeyId, ref priKeyId);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            freeCKAttributeMemory(pubTemplate);
            freeCKAttributeMemory(priTemplate);

            return new long[] {pubKeyId, priKeyId};
        }

        public void C_GetAttributeValue(long aSessionID, long hObject, iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] pTemplate)
        {
            CK_ATTRIBUTE[] toBeReadTemplate = fromIaikAttributeArrayToInteropAttributeArray(pTemplate);

            CKR rv = C_GetAttributeValue((uint)aSessionID, 
                (uint)hObject, 
                toBeReadTemplate,
                (uint)toBeReadTemplate.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            for (int i = 0; i < toBeReadTemplate.Length; i++)
            {
                toBeReadTemplate[i].value =
                    UnmanagedMemory.Allocate((int) toBeReadTemplate[i].valueLen);
            }

            rv = C_GetAttributeValue((uint)aSessionID, (uint)hObject, toBeReadTemplate,
                (uint)toBeReadTemplate.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            for (int i = 0; i < pTemplate.Length; i++)
            {

                byte[] barray = new byte[toBeReadTemplate[i].valueLen];
                Marshal.Copy(toBeReadTemplate[i].value, barray, 0, barray.Length);

                object pValue = getValueForKnownType(toBeReadTemplate[i].type, barray);

                if (pValue != null)
                    pTemplate[i].pValue = pValue;
                else
                    pTemplate[i].pValue = barray;
            }

            EPkcs11Library.freeCKAttributeMemory(toBeReadTemplate);
        }

        private object getValueForKnownType(uint type, byte [] pValue)
        {
            if (type == PKCS11Constants_Fields.CKA_CLASS)
                return AttributeUtil.byteArrayToLong(pValue);

            if (type == PKCS11Constants_Fields.CKA_LABEL)
                return AttributeUtil.getStringValue(pValue);

            if (type == PKCS11Constants_Fields.CKA_KEY_TYPE)
                return AttributeUtil.byteArrayToLong(pValue);

            return null;
        }

        public void C_SetAttributeValue(long aSessionID, long hObject, iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] pTemplate)
        {
            CK_ATTRIBUTE[] template = fromIaikAttributeArrayToInteropAttributeArray(pTemplate);
            CKR rv = C_SetAttributeValue((uint)aSessionID, (uint)hObject, template, (uint)template.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            EPkcs11Library.freeCKAttributeMemory(template);
        }

        public void C_SignInit(long aSessionID, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM pMechanism, long hKey)
        {
            Net.Pkcs11Interop.LowLevelAPI41.CK_MECHANISM mech = fromIaikMechanismToInteropMechanism(pMechanism);
            CKR rv = base.C_SignInit((uint)aSessionID, ref mech, (uint)hKey);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public byte[] C_Sign(long aSessionID, byte[] dataToBeSigned)
        {
            uint signatureLength = 0;
            CKR rv = C_Sign((uint)aSessionID, dataToBeSigned, (uint)dataToBeSigned.Length, null, ref signatureLength);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            byte[] signature = new byte[signatureLength];

            rv = C_Sign((uint)aSessionID, dataToBeSigned, (uint)dataToBeSigned.Length, signature, ref signatureLength);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            Array.Resize(ref signature, (int)signatureLength);

            return signature;
        }

        public void C_SignUpdate(long hSession, byte[] pPart)
        {
            CKR rv = C_SignUpdate((uint)hSession, pPart, (uint)pPart.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public byte[] C_SignFinal(long hSession)
        {
            uint signatureLength = 0;
            CKR rv = C_SignFinal((uint)hSession, null, ref signatureLength);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            byte[] signature = new byte[signatureLength];
            rv = C_SignFinal((uint)hSession, signature, ref signatureLength);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            Array.Resize(ref signature, (int)signatureLength);

            return signature;
        }

        public void C_EncryptInit(long hSession, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM pMechanism, long hKey)
        {
            Net.Pkcs11Interop.LowLevelAPI41.CK_MECHANISM mech = fromIaikMechanismToInteropMechanism(pMechanism);
            CKR rv = C_EncryptInit((uint)hSession, ref mech, (uint)hKey);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public byte[] C_Encrypt(long hSession, byte[] pData)
        {
            uint encryptedLen = 0;
            CKR rv = C_Encrypt((uint)hSession, pData, (uint)pData.Length, null, ref encryptedLen);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            byte[] encryptedData = new byte[encryptedLen];
            rv = C_Encrypt((uint)hSession, pData, (uint)pData.Length, encryptedData, ref encryptedLen);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            Array.Resize(ref encryptedData, (int)encryptedLen);

            return encryptedData;
        }

        public void C_DecryptInit(long hSession, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM pMechanism, long hKey)
        {
            Net.Pkcs11Interop.LowLevelAPI41.CK_MECHANISM mech = fromIaikMechanismToInteropMechanism(pMechanism);
            CKR rv = base.C_DecryptInit((uint)hSession, ref mech, (uint)hKey);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public byte[] C_Decrypt(long hSession, byte[] pEncryptedData, int outLen = 0)
        {
            uint decryptedLen = (uint)outLen;
            CKR rv = CKR.CKR_OK;
            if (decryptedLen == 0)
            {
                rv = C_Decrypt((uint)hSession, pEncryptedData, (uint)pEncryptedData.Length, null,
                    ref decryptedLen);
                if (rv != CKR.CKR_OK)
                    throw new PKCS11Exception((long)rv);
            }

            byte[] decryptedData = new byte[decryptedLen];
            rv = C_Decrypt((uint)hSession, pEncryptedData, (uint)pEncryptedData.Length, decryptedData, ref decryptedLen);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
            
            Array.Resize(ref decryptedData, (int)decryptedLen);

            return decryptedData;
        }

        public void C_DestroyObject(long hSession, long hObject)
        {
            CKR rv = base.C_DestroyObject((uint)hSession, (uint)hObject);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_GenerateRandom(long hSession, byte[] randomData)
        {
            CKR rv = C_GenerateRandom((uint)hSession, randomData, (uint)randomData.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_VerifyInit(long hSession, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM pMechanism, long hKey)
        {
            Net.Pkcs11Interop.LowLevelAPI41.CK_MECHANISM mech = fromIaikMechanismToInteropMechanism(pMechanism);

            CKR rv = base.C_VerifyInit((uint)hSession, ref mech, (uint)hKey);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_Verify(long hSession, byte[] pData, byte[] pSignature)
        {
            CKR rv = base.C_Verify((uint)hSession, pData, (uint)pData.Length, pSignature, (uint)pSignature.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_SetPIN(long hSession, char[] pOldPin, char[] pNewPin)
        {
            CKR rv = base.C_SetPIN((uint)hSession, charArrayToByteArray(pOldPin), (uint)pOldPin.Length,
                charArrayToByteArray(pNewPin), (uint)pNewPin.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_CloseAllSessions(long slotID)
        {
            CKR rv = base.C_CloseAllSessions((uint)slotID);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_InitToken(long slotID, char[] pPin, char[] pLabel)
        {
            CKR rv = base.C_InitToken((uint)slotID, charArrayToByteArray(pPin), (uint)pPin.Length,
                charArrayToByteArray(pLabel));
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_InitPIN(long hSession, char[] pPin)
        {
            CKR rv = base.C_InitPIN((uint)hSession, charArrayToByteArray(pPin), (uint)pPin.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_FindObjectsInit(long hSession, iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] pTemplate)
        {
            CK_ATTRIBUTE[] templateConverted = fromIaikAttributeArrayToInteropAttributeArray(pTemplate);
            CKR rv = base.C_FindObjectsInit((uint)hSession, templateConverted, (uint)templateConverted.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            EPkcs11Library.freeCKAttributeMemory(templateConverted);
        }

        public long[] C_FindObjects(long hSession, long ulMaxObjectCount)
        {
            uint foundCount = 0;

            uint[] foundIDs = new uint[ulMaxObjectCount];
            CKR rv = base.C_FindObjects((uint)hSession, foundIDs, (uint)ulMaxObjectCount, ref foundCount);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            return uintArrayToLongArray(foundIDs);
        }

        public void C_FindObjectsFinal(long hSession)
        {
            CKR rv = base.C_FindObjectsFinal((uint)hSession);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_VerifyUpdate(long hSession, byte[] pPart)
        {
            CKR rv = base.C_VerifyUpdate((uint)hSession, pPart, (uint)pPart.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public void C_VerifyFinal(long hSession, byte[] pSignature)
        {
            CKR rv = base.C_VerifyFinal((uint)hSession, pSignature, (uint)pSignature.Length);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);
        }

        public byte[] C_WrapKey(long hSession, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM pMechanism, long hWrappingKey,
            long hKey)
        {
            Net.Pkcs11Interop.LowLevelAPI41.CK_MECHANISM mech = fromIaikMechanismToInteropMechanism(pMechanism);
            uint wrappedLen = 0;
            CKR rv = base.C_WrapKey((uint)hSession, ref mech, (uint)hWrappingKey, (uint)hKey, null, ref wrappedLen);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            byte[] wrappedKey = new byte[wrappedLen];
            rv = base.C_WrapKey((uint)hSession, ref mech, (uint)hWrappingKey, (uint)hKey, wrappedKey, ref wrappedLen);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            Array.Resize(ref wrappedKey, (int)wrappedLen);

            return wrappedKey;
        }

        public long C_GenerateKey(long hSession, iaik.pkcs.pkcs11.wrapper.CK_MECHANISM pMechanism, iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] pTemplate)
        {
            uint keyID = CK.CK_INVALID_HANDLE;

            Net.Pkcs11Interop.LowLevelAPI41.CK_MECHANISM mech = fromIaikMechanismToInteropMechanism(pMechanism);
            CK_ATTRIBUTE[] attributes = fromIaikAttributeArrayToInteropAttributeArray(pTemplate);

            CKR rv = base.C_GenerateKey((uint)hSession, ref mech, attributes, (uint)attributes.Length, ref keyID);
            if (rv != CKR.CKR_OK)
                throw new PKCS11Exception((long)rv);

            EPkcs11Library.freeCKAttributeMemory(attributes);

            return keyID;
        }
    }
}