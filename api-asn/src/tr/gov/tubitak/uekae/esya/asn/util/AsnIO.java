package tr.gov.tubitak.uekae.esya.asn.util;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Enumerated;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1Tag;
import com.objsys.asn1j.runtime.Asn1Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p>Title: ESYA</p>
 * <p>Description: </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */

public class AsnIO
{
	protected static Logger logger = LoggerFactory.getLogger(AsnIO.class);

	public AsnIO ()
	{
		//do nothing
	}

	/**
	 * Write byte array to the file
	 * @param aSonuc Byte array that will be written
	 * @param aFileName File that byte array will be written on it
	 * @throws IOException
	 * @deprecated use FileUtil writeBytes
	 */
	public static void dosyayaz (byte[] aSonuc, String aFileName)
	throws IOException
	{
		FileUtil.writeBytes(aFileName, aSonuc);
	}

	/**
	 * Write Asn1Type to the file
	 * @param aAsn1 Asn1Type that will be written
	 * @param aFile File that byte array will be written on it
	 * @throws IOException
	 * @throws Asn1Exception
	 */
	public static void dosyayaz (Asn1Type aAsn1, String aFile)
	throws Asn1Exception, IOException
	{
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		aAsn1.encode(encBuf);
		dosyayaz(encBuf.getMsgCopy(), aFile);
	}
	/**
	 * Write Asn1Type to the file as Base64
	 * @param aAsn1 Asn1Type that will be written
	 * @param aFile File that byte array will be written on it
	 * @throws IOException
	 * @throws Asn1Exception
	 */
	public static void dosyaBase64yaz (Asn1Type aAsn1, String aFile)
	throws Asn1Exception, IOException
	{
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		aAsn1.encode(encBuf);
		dosyayaz(Base64.encode(encBuf.getMsgCopy()).getBytes(), aFile);
	}

	/**
	 * Read data from file as byte array
	 * @param filePath File that will be read
	 * @return byte[]
	 * @throws IOException
	 * @deprecated use FileUtil readBytes
	 */
	public static byte[] dosyadanOKU (String filePath) throws IOException {
		return FileUtil.readBytes(filePath);
	}

	//Beş adet "-" var mı kontrolü
	private static boolean besTire(byte[] aVeri,int aIndex)
	{
		int i;
		for(i=0;i<5;i++)
			if((aVeri.length <= (aIndex+i)) || (aVeri[aIndex+i]!=((byte)'-')))
				return false;
		return true;
	}

	private static byte[] base64deTemizlikYap(byte[] aKirliBase64)
	{
		byte[] temizBase64 = new byte[aKirliBase64.length];
		int len = 0;
		for(int i=0;i<aKirliBase64.length;i++)
		{
			if(besTire(aKirliBase64,i))
			{
				//sonraki besTire'ye kadar atlayalim
				i+=5;
				while ((!besTire(aKirliBase64,i)) && (i<aKirliBase64.length))
				{
					i++;
				}
				i+=4;
			}
			else
				temizBase64[len++] = aKirliBase64[i];
		}

		byte[] terTemizBase64 = new byte[len];
		System.arraycopy(temizBase64,0,terTemizBase64,0,len);
		return terTemizBase64;
	}
	
	/**
	 * Read Asn1Type from a byte array
	 * @param aAsn1 Asn1Type that will be read
	 * @param aDer Byte array which is Der Encoded
	 * @throws IOException
	 * @throws Asn1Exception
	 */
	public static Asn1Type derOku(Asn1Type aAsn1,byte[] aDer)
	throws Asn1Exception, IOException
	{
		Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aDer);

		return derOku(aAsn1, decBuf);
	}
	/**
	 * Read Asn1Type from a byte array
	 * @param aAsn1 Asn1Type that will be read
	 * @param decBuf Byte array which is Der Encoded
	 * @throws IOException
	 * @throws Asn1Exception
	 */
	public static Asn1Type derOku(Asn1Type aAsn1,Asn1DerDecodeBuffer decBuf)
	throws Asn1Exception, IOException
	{
		if (aAsn1 instanceof Asn1Enumerated) {
			Class clazz = aAsn1.getClass();
			try {
				Asn1Tag tag = (Asn1Tag) clazz.getField("TAG").get(null);
				int decodevalue = decBuf.decodeEnumValue(tag, Asn1Tag.EXPL, 0);
				aAsn1 = (Asn1Type) clazz.getMethod("valueOf", Integer.TYPE).invoke(null, decodevalue);
			} catch (Exception e) {
				logger.error("File could not be read.", e);
				throw new Asn1Exception("Enumarated type can not be decoded.");
			}
		} else
			aAsn1.decode(decBuf);
		return aAsn1;
	}
	/**
	 * Read Asn1Type from a byte array
	 * @param aAsn1 Asn1Type that will be read
	 * @param aVeri Byte array which is Der or Base64 Encoded
	 * @throws IOException
	 * @throws Asn1Exception
	 */
	public static Asn1Type arraydenOku(Asn1Type aAsn1,byte[] aVeri)
	throws Asn1Exception, IOException
	{
		try
		{
			//Once direct der encoded oldugunu varsayip sansimizi deneyelim...
			aAsn1 = derOku(aAsn1,aVeri);
		}
		catch (Asn1Exception ex)
		{
			logger.debug("Data could not be read. Trying Base64 encoded. " + ex.getMessage());
			try {
				//Bir de base64 oldugunu varsayip sansimizi deneyelim.
				aAsn1 = arraydenBase64Oku(aAsn1, aVeri);
			}catch (Exception base64Ex) {
				throw ex; // Throw first ex.
			}
		}
		return aAsn1;
	}


	/**
	 * Read Asn1Type from a byte array
	 * @param aAsn1 Asn1Type that will be read
	 * @param aVeri Byte array which is Base64 Encoded
	 * @throws IOException
	 * @throws Asn1Exception
	 */
	public static Asn1Type arraydenBase64Oku(Asn1Type aAsn1,byte[] aVeri)
	throws Asn1Exception, IOException
	{
		byte[] yeniVeri =  base64deTemizlikYap(aVeri);
		byte[] derVeri = Base64.decode(yeniVeri,0,yeniVeri.length);
		aAsn1 = derOku(aAsn1,derVeri);
		return aAsn1;
	}
	/**
	 * Read  from an input stream
	 * @param is InputStream that will be read
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte [] streamOku(InputStream is) throws IOException
	{
		ByteArrayOutputStream ba = null;
		ba = new ByteArrayOutputStream();
		byte [] block = new byte[4096];
		while(true)
		{
			int lenght = is.read(block);
			if(lenght == -1)
				break;
			ba.write(block, 0, lenght);
		}
		return ba.toByteArray();
	}

	/*
     public static byte[] dosyadanOKU (Asn1Type aAsn1,String aFile)
         throws Asn1Exception, IOException
     {
          byte[] b = dosyadanOKU(aFile);
          Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(b);
          aAsn1.decode(decBuf);
          return b;
     }*/
	/**
	 * Read  from file as Asn1Type
	 * @param aAsn1 Asn1Type that will be decoded
	 * @param aFile File that will be read
	 * @return byte[]
	 * @throws IOException
	 * @throws Asn1Exception
	 */
	public static Asn1Type dosyadanOKU (Asn1Type aAsn1,String aFile)
	throws Asn1Exception, IOException
	{
		byte[] b = dosyadanOKU(aFile);
		aAsn1 = arraydenOku(aAsn1,b);
		return aAsn1;
	}
	/**
	 * Get Asn1Type as String
	 * @param asn1Type Asn1Type that will be converted to string
	 * @return String
	 */
	public static String getFormattedAsn(Asn1Type asn1Type) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		asn1Type.print(new PrintStream(baos), "Asn1Type:" + asn1Type.getClass().toString(), 0);
		return baos.toString();
	}


	/**
	 * Encodes the given object
	 * @param asn1Type Asn1Type that will be encoded
	 * @return encoded byte array
	 */
	public static byte [] derEncode(Asn1Type asn1Type)
	{
		Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
		asn1Type.encode(buff);
		return buff.getMsgCopy();
	}

	/*
     public static byte[] dosyadanBase64OKU (Asn1Type aAsn1,String aFile)
         throws Asn1Exception, IOException
     {
          byte[] base64 = dosyadanOKU(aFile);
          byte[] dBase64 = Base64.decode(base64,0,base64.length);

          Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(dBase64);
          aAsn1.decode(decBuf);

          return dBase64;
     }
	 */
}