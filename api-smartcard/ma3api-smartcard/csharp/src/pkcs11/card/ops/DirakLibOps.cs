using System;
using System.Runtime.InteropServices;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.src.pkcs11.card.ops
{
    public class DirakLibOps
    {
        public static void userLogin(String username, String password)
        {
            DirakLibDLLConnector.Dirak_UserLogin("srpP", username, password);
        }

        public static void userLogin(String username, String password, String hsmIP)
        {
            DirakLibDLLConnector.Dirak_UserLoginIp("srpP", username, password, hsmIP);
        }

        public static void userLogout(String username)
        {
            DirakLibDLLConnector.Dirak_UserLogout(username);
        }

        public static void userLogout(String username, String hsmIp)
        {
            DirakLibDLLConnector.Dirak_UserLogoutIp(username, hsmIp);
        }

        protected static class DirakLibDLLConnector
        {
            const String dllName = "dirakp11-64.dll";

            [DllImport(dllName)]
            [return: MarshalAs(UnmanagedType.SysInt)]
            public static extern int Dirak_ResetSrpUserPin(String newPass, String newPass2);

            [DllImport(dllName)]
            [return: MarshalAs(UnmanagedType.SysInt)]
            public static extern int Dirak_ResetSrpUserPinIp(String oldPass, String newPass, String newPass2, String hsmIP);

            [DllImport(dllName)]
            [return: MarshalAs(UnmanagedType.SysInt)]
            public static extern int Dirak_GetLoggedInUsers(byte[] username);

            [DllImport(dllName)]
            [return: MarshalAs(UnmanagedType.SysInt)]
            public static extern int Dirak_SetPinPermission(long slotId, int permission);

            [DllImport(dllName)]
            [return: MarshalAs(UnmanagedType.SysInt)]
            public static extern int Dirak_GetPinPermission(long slotId);

            [DllImport(dllName)]
            [return: MarshalAs(UnmanagedType.SysInt)]
            public static extern int Dirak_SetTokenName(long slotId, String tokenName);

            [DllImport(dllName)]
            [return: MarshalAs(UnmanagedType.I8)]
            public static extern long Dirak_UserLogin(String userType, String username, String password);

            [DllImport(dllName)]
            [return: MarshalAs(UnmanagedType.I8)]
            public static extern long Dirak_UserLoginIp(String userType, String username, String password, String hsmIP);

            [DllImport(dllName)]
            [return: MarshalAs(UnmanagedType.I8)]
            public static extern long Dirak_UserLogout(String username);

            [DllImport(dllName)]
            [return: MarshalAs(UnmanagedType.I8)]
            public static extern long Dirak_UserLogoutIp(String username, String hsmIp);
        }
    }
}
