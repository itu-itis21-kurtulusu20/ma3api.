using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public static class FileUtil
    {
        public static void move(FileInfo source, FileInfo dest)
        {
            source.MoveTo(dest.FullName);
        }

        public static void writeBytes(string filePath, byte [] fileBytes)
        {
            using (FileStream fout = new FileStream(filePath, FileMode.Create))
            {
                fout.Write(fileBytes, 0, fileBytes.Length);
            }
        }

        public static byte[] readBytes(string filePath)
        {
            return File.ReadAllBytes(filePath);
        }

    }
}
