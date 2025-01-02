using System;
using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public static class StreamUtil
    {
	    public static byte [] readAll(Stream aIs) 
	    {
		    byte [] buffer = new byte[16384];
            MemoryStream baos = new MemoryStream();
		    int len;
		    while ((len = aIs.Read(buffer, 0, buffer.Length)) > 0)
			    baos.Write(buffer, 0, len);
		    aIs.Close();
		    return baos.ToArray();
	    }

        public static void copy(Stream from, Stream to)
        {
            byte[] buffer = new byte[16384];
            int len;
            while((len = from.Read(buffer, 0, buffer.Length)) > 0)
                to.Write(buffer, 0, len);
            from.Close();
        }

        public static byte[] ReadToEnd(Stream stream)
        {
            long originalPosition = 0;

            if (stream.CanSeek)
            {
                originalPosition = stream.Position;
                stream.Position = 0;
            }

            try
            {
                byte[] readBuffer = new byte[4096];

                int totalBytesRead = 0;
                int bytesRead;

                while ((bytesRead = stream.Read(readBuffer, totalBytesRead, readBuffer.Length - totalBytesRead)) > 0)
                {
                    totalBytesRead += bytesRead;

                    if (totalBytesRead != readBuffer.Length) continue;
                    int nextByte = stream.ReadByte();
                    if (nextByte == -1) continue;
                    byte[] temp = new byte[readBuffer.Length * 2];
                    Buffer.BlockCopy(readBuffer, 0, temp, 0, readBuffer.Length);
                    Buffer.SetByte(temp, totalBytesRead, (byte)nextByte);
                    readBuffer = temp;
                    totalBytesRead++;
                }

                byte[] buffer = readBuffer;
                if (readBuffer.Length == totalBytesRead) return buffer;
                buffer = new byte[totalBytesRead];
                Buffer.BlockCopy(readBuffer, 0, buffer, 0, totalBytesRead);
                return buffer;
            }
            finally
            {
                if (stream.CanSeek)
                {
                    stream.Position = originalPosition;
                }
            }
        }
    }

}

