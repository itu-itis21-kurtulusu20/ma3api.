using System;
using System.IO;
using System.Runtime.InteropServices;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{
    public static class EMimeUtil
    {
        public static string GetMimeType(string fileName)
        {
            string mimeType = "application/unknown";
            if (fileName == null)
            {
                return mimeType;
            }
            try
            {
                string ext = System.IO.Path.GetExtension(fileName).ToLower();
                Microsoft.Win32.RegistryKey regKey = Microsoft.Win32.Registry.ClassesRoot.OpenSubKey(ext);
                if (regKey != null && regKey.GetValue("Content Type") != null)
                    mimeType = regKey.GetValue("Content Type").ToString();
                return mimeType;
            }
            catch (NullReferenceException exc)
            {
                System.Console.WriteLine(exc.StackTrace);
            }
            return mimeType;
        }

        public static readonly int MimeSampleSize = 256;

        public static readonly string DefaultMimeType = "application/octet-stream";

        [DllImport(@"urlmon.dll", CharSet = CharSet.Auto)]
        private extern static uint FindMimeFromData(
            uint pBC,
            [MarshalAs(UnmanagedType.LPStr)] string pwzUrl,
            [MarshalAs(UnmanagedType.LPArray)] byte[] pBuffer,
            uint cbSize,
            [MarshalAs(UnmanagedType.LPStr)] string pwzMimeProposed,
            uint dwMimeFlags,
            out uint ppwzMimeOut,
            uint dwReserverd
        );

        public static string GetMimeFromFile(FileStream fs,string fileName)
        {
            byte[] buff;
            long bufSize = MimeSampleSize;
            if (fileName != null)
            {
              long numBytes =  new FileInfo (fileName).Length;
              if (numBytes < bufSize)
              {
                  bufSize = numBytes;
              }
            }
            BinaryReader br = new BinaryReader(fs);
            buff = br.ReadBytes((int) MimeSampleSize);
            return GetMimeFromBytes(buff);
        }

        public static string GetMimeFromBytes(byte[] data)
        {
            try
            {
                uint mimeType;
                FindMimeFromData(0, null, data, (uint)MimeSampleSize, null, 0, out mimeType, 0);

                var mimePointer = new IntPtr(mimeType);
                var mime = Marshal.PtrToStringUni(mimePointer);
                Marshal.FreeCoTaskMem(mimePointer);

                return mime ?? DefaultMimeType;
            }
            catch
            {
                return DefaultMimeType;
            }
        }
    }
}
