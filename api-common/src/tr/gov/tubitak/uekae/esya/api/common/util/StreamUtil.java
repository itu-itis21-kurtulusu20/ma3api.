package tr.gov.tubitak.uekae.esya.api.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil 
{
	public static byte [] readAll(InputStream aIs) throws IOException 
	{
		byte [] buffer = new byte[16384];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len;
		while ((len = aIs.read(buffer, 0, buffer.length)) > 0)
			baos.write(buffer, 0, len);
		aIs.close();
		return baos.toByteArray();
	}

    public static void copy(InputStream from, OutputStream to) throws IOException
   	{
   		byte [] buffer = new byte[16384];
   		ByteArrayOutputStream baos = new ByteArrayOutputStream();
   		int len;
   		while ((len = from.read(buffer, 0, buffer.length)) > 0)
   			to.write(buffer, 0, len);
   		from.close();
   	}

}
