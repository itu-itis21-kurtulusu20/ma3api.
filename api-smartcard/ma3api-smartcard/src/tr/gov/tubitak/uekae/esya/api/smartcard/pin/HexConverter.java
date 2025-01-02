package tr.gov.tubitak.uekae.esya.api.smartcard.pin;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import java.nio.ByteBuffer;
import java.util.StringTokenizer;


/**
 * İçinde hex sayılar olan bir stringi byte dizisine dönüştürür. Veya byte
 * dizisini içinde hex sayılar olan stringe dönüştürür
 * todo ileride bunu common altından kullanıp alıp sil
 */

public class HexConverter
{
	protected static Logger logger = LoggerFactory.getLogger(HexConverter.class);
	/** Hex karakterler */
	public final static char hexChars[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
	        'F' };
	/** Bu tablo da Hex karakterlerin rakamsal karşılığını tutar */
	public final static byte hexValTable[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13,
	        14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
	/** Bu dizideki elemalar "indis" değerlerinin Hex string karşılığını tutar */
	public final static String byteVal[] = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B",
	        "0C", "0D", "0E", "0F", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D",
	        "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F",
	        "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F", "40", "41",
	        "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53",
	        "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65",
	        "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77",
	        "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
	        "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B",
	        "9C", "9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD",
	        "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF",
	        "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1",
	        "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3",
	        "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5",
	        "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };
	/** prettyprint için parametre */
	public static int PRINT_ASCII_VALUES = 0;

	/**
	 * Bir byte dizisini hex sayılar string'ine dönüştürür. Stringde default olarak boşluk yoktur
	 * @param array Hex string'e dönüştürülecek giriş dizisi
	 * @return : Hexadecimal String
	 */
	public static String byteArrayToHexString(byte array[])
	{
		if (array == null) return "null";
		if (array.length == 0) return "empty array";
		byte strBuffer[] = new byte[array.length * 2];
		int j = 0;
		try
		{
			for (byte element : array)
			{
				strBuffer[j++] = (byte) hexChars[(element & 0xF0) >> 4];
				strBuffer[j++] = (byte) hexChars[(element & 0x0F)];
			}
		} catch (RuntimeException e)
		{
			throw e;
		}
		return new String(strBuffer);
	}

	/**
	 * ascii dizisi hex byte olarak geri doner
	 * @param array Hex string'e dönüstürülecek giris dizisi
	 * @return : Hexadecimal String
	 */
	public static byte[] asciiArrayToHexByteArray(byte array[]) throws Exception
	{
		byte[] hexByteArray = new byte[array.length];
		byte[] hexHataliByteArray = new byte[array.length];
		byte sifir = '0';
		byte dokuz = '9';
		byte aHarfi = 'A';
		byte fHarfi = 'F';
		if (array == null) return hexHataliByteArray;
		if (array.length == 0) return hexHataliByteArray;
		try
		{
			for (int k = 0; k < array.length; k++)
			{
				if (((byte) array[k] <= (byte) dokuz) && ((byte) array[k] >= (byte) sifir))
				{ // 0-9
					hexByteArray[k] = (byte) ((byte) array[k] - (byte) sifir);
				} else if (((byte) array[k] <= (byte) fHarfi) && ((byte) array[k] >= (byte) aHarfi))
				{ // A-F
					hexByteArray[k] = (byte) ((byte) array[k] - (byte) aHarfi + 10); // A-F
				} else
				{
					throw new ESYARuntimeException("Giris degerleri arasinda hex olmayan karakter var.");
				}
			}
		} catch (RuntimeException e)
		{
			throw new ESYARuntimeException("Giris degerleri hex degil", e);
		}
		return hexByteArray;
	}

	/**
	 * Bir byte dizisini hex sayılar string'ine dönüştürür.
	 * Her hex digit arasına default olarak boşluk karakteri koyar
	 *
	 * @param array Hex string'e dönüştürülecek giriş dizisi
	 * @param spacer : hex değerler arasına konacak ayıraç karakteri özel olarak belirlenmek isterse buraya girilmelidir.
	 * @return : Hexadecimal String
	 */
	public static String byteArrayToHexString(byte array[], String spacer)
	{
		if (array == null) return "null";
		if (array.length == 0) return "empty array";
		int spacerLen = 0;
		if (spacer != null) spacerLen = spacer.length();
		StringBuffer str = new StringBuffer(array.length * (2 + spacerLen) + 1);
		for (byte element : array)
		{
			str.append(byteVal[element & 0xFF]);
			if (spacer != null) str.append(spacer);
		}
		if (spacer != null) str.setLength(str.length() - spacerLen);
		return str.toString();
	}

	/**
	 * Verilen  byte dizisini Offset: XX XX XX XX .. şeklinde her satıra 16 adet Hex byte ve ASCII karşılığı ile basar
	 * Aslında byteArrayToPrettyHexString(array, PRINT_ASCII_VALUES, 16) ile aynıdır
	 * @param array
	 * @return
	 */
	public static String hexDump(byte array[])
	{
		return byteArrayToPrettyHexString(array, PRINT_ASCII_VALUES, 16);
	}

	/**
	 * Pretty Print As Hex. Ofset ve ascii değerlerini de basar ve istenilen aralıklarla satırlara böler
	 * @param array : Giriş disizi
	 * @param type  : şimdilik ihmal ediliyor (çeşitli default türler olacak)
	 * @param bytesPerColumn : 80 lik bir text ekranda görülebilmesi için 16 verin, daha büyükler için 25 olabilir.
	 *                         isterseniz kafanıza göre de takılabilirsiniz.
	 * @return şık string :)
	 */
	public static String byteArrayToPrettyHexString(byte array[], int type, int bytesPerColumn)
	{
		if (array == null) return "Array is null.";
		if (array.length == 0) return "empty array";
		int spacerLen = 0;
		String spacer = " ";
		boolean showByteCount = true;
		// Başta yazılan index bilgisinin uzunluğu ne olacak?
		int lengthDigitCount = calculateDigitCount(array.length);
		if (bytesPerColumn < 1) bytesPerColumn = 1;
		if (spacer != null) spacerLen = spacer.length();
		// uygun uzunlukta bufferimizi oluşturalım
		StringBuffer str = new StringBuffer(array.length * (4 + spacerLen) + (array.length / bytesPerColumn)
		        * (lengthDigitCount + 4));
		// kaç segment olacağını hesaplayalım
		int segments = array.length / bytesPerColumn;
		if (array.length % bytesPerColumn != 0) segments++; // eğer sonda fazlalık kalırsa o da bir segment edecek
		// Giriş bilgisini yazalım.
		str.append("\nDizi Boyu: " + array.length + "\n");
		// Her satır için gerekli işlemi tekrarlayacağız
		// Bu dizi indeksi
		int index = 0;
		int expectedLen = bytesPerColumn * 2 + (bytesPerColumn * spacer.length());
		// Her segment için
		for (int i = 0; i < segments; i++)
		{
			// kaçıncı byte'den başlıyoruz onu yazalım.
			if (showByteCount == true)
			{
				str.append(padSpace("" + index, lengthDigitCount) + ": ");
			}
			int startIndex = index;
			int strLen = 0;
			for (int j = 0; j < bytesPerColumn && index < array.length; j++)
			{
				// Hex değerimizi ve spacer'i ekleyelim.
				str.append(byteVal[array[index++] & 0xFF]);
				if (spacer != null) str.append(spacer);
				strLen += spacer.length() + 2; //her byte için 2+spacerlen karakter
			}
			String pad = getSpacesForPadding(strLen, expectedLen);
			str.append(pad);
			str.append(" ");
			if (type == PRINT_ASCII_VALUES) for (int k = startIndex; k < index; k++)
			{
				str.append(getAsciiRep(array[k]));
			}
			str.append("\n");
		}
		return str.toString().trim();
	}

	/**
	 * Verilen sayının stringhalinde kaç dijit buluacağını hesaplar (log10 len) aslında.
	 * @param len
	 * @return
	 */
	private static int calculateDigitCount(int len)
	{
		if (len < 0) return 0;
		if (len < 10) return 1;
		if (len < 100) return 2;
		if (len < 1000) return 3;
		if (len < 10000) return 4;
		if (len < 100000) return 5;
		if (len < 1000000) return 6;
		return 7;
	}

	/**
	 * Verilen String'in boyu len olana dek sağdan boşluk ekler
	 * @param str
	 * @param len
	 * @return boşluk padlenmiş string
	 */
	private static String padSpace(String str, int len)
	{
		int padCount = len - str.length();
		if (padCount <= 0) return str;
		for (int i = 0; i < padCount; i++)
			str += " ";
		return str;
	}

	/**
	 * Bu da len-strlen uzunlukta boşluklarla doldurulmuş bir String döndürür. Pretty print için maymun olduk.
	 * @param strLen
	 * @param len
	 * @return
	 */
	private static String getSpacesForPadding(int strLen, int len)
	{
		int padCount = len - strLen;
		if (padCount <= 0) return "";
		String str = "";
		for (int i = 0; i < padCount; i++)
			str += " ";
		return str;
	}

	/**
	 * verilen byte değerinin ASCII karşılığını String olarak döndürür. byteVal{@literal >}126 için "." döner.
	 * @param byteVal
	 * @return
	 */
	public static String getAsciiRep(byte byteVal)
	{
		int val = (byteVal & 0xFF);
		String str;
		if (val > 126 || val < ' ')
			str = ".";
		else
			str = "" + (char) val;
		return str;
	}

	/**
	 * İçinde hex sayılar olan bir string'i byte dizisine dönüştürür. Default olarak Stringin içinde rakamlar
	 * arasında boşluk olmadığını kabul eder.
	 * @param str : Giriş Hex stringi
	 * @return : byte[] cinsinden, hex stringinin içindeki  sayısal değerler
	 * @throws NumberFormatException
	 */
	public static byte[] hexStringToByteArray(String str) throws NumberFormatException
	{
		if (str == null) return null;
		if (str.length() == 0) return null;
		str.trim();
		int len = str.length();
		int j = 0, k = 0;
		byte[] strBuf = str.getBytes();
		byte[] buf = new byte[len / 2];
		if ((str.length() % 2) != 0)
		{
			throw new NumberFormatException("Hex String uzunluğu çift olmalı. String: " + str);
		}
		try
		{
			for (int i = 0; i < len; i += 2)
			{
				byte firstNibble = hexValTable[strBuf[k++]];
				byte secondNibble = hexValTable[strBuf[k++]];
				if (firstNibble == -1 || secondNibble == -1)
				{
					throw new NumberFormatException("String Illegal Hex karakter taşıyor. String: " + str);
				}
				buf[j++] = (byte) (firstNibble * 16 + secondNibble);
			}
		} catch (RuntimeException e)
		{
			logger.warn("Warning in HexConverter", e);
			throw new NumberFormatException("Hatalı hex string. String: " + str);
		}
		return buf;
	}

	/**
	 * İçinde hex sayılar olan bir string'i byte dizisine dönüştürür. spacer Stringindeki karakterleri ayıraç kabul eder.
	 * @param str : Giriş Hex stringi
	 * @param spacer : hex değerler arasındaki ayıraç karakteri özel olarak belirlenmek isterse buraya girilmelidir.
	 *                 null veya "" ise bitişik Stringler için çalşır (ancak bu iş için tek parametreli versiyon kullanılmalı)
	 * @return : byte[] cinsinden, hex stringinin içindeki  sayısal değerler
	 * @throws NumberFormatException
	 */
	public static byte[] hexStringToByteArray(String str, String spacer) throws NumberFormatException
	{
		int count = 0;
		byte array[];
		if (str == null) return null;
		if (str.length() == 0) return null;
		str.trim();
		try
		{
			if (spacer == null || spacer.equals(""))
			{
				if ((str.length() % 2) != 0)
				{
					str = "0" + str;
				}
				array = new byte[str.length() / 2];
				for (int i = 0; i < str.length(); i += 2)
				{
					String temp = str.substring(i, i + 2);
					array[count++] = (byte) Integer.parseInt(temp, 16);
				}
			} else
			{
				StringTokenizer token = new StringTokenizer(str, " ,");
				count = token.countTokens();
				array = new byte[count];
				for (int i = 0; i < count; i++)
				{
					array[i] = (byte) Integer.parseInt(token.nextToken(), 16);
				}
			}
		} catch (RuntimeException e)
		{
			logger.warn("Warning in HexConverter", e);
			throw new NumberFormatException("Hatalı Hex String: " + str);
		}
		return array;
	}

	// ---------- Diğer Primitif Dönüşümler ----------------------------
	/**
	 * Verilen bir long sayıyı Hex String'e dönüştürür.
	 * @param input hex string'e dönüştürülecek long cinsinden giriş.
	 * @return long girişin Hex String karşılığı
	 */
	public static String longToHexString(long input)
	{
		ByteBuffer buf = ByteBuffer.allocate(8); // Long değer 8 byte
		buf.putLong(0, input);
		buf.flip();
		return byteArrayToHexString(buf.array(), null);
	}

	/**
	 * Verilen bir long sayıyı byte array'e dönüştürür.
	 * @param input long cinsinden veri
	 * @return
	 */
	public static byte[] longToByteArray(long input)
	{
		ByteBuffer buf = ByteBuffer.allocate(8); // Long değer 8 byte
		buf.putLong(0, input);
		buf.flip();
		return buf.array();
	}

	/**
	 * Verilen byte dizinin long değerini verir.
	 * @param input
	 * @return
	 */
	public static long byteArrayToLong(byte[] input)
	{
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.put(input, 0, 8);
		buf.flip();
		return buf.getLong(0);
	}

	/**
	 * Verilen short sayıyı byte array e çevirir.
	 * @param input
	 * @return
	 */
	public static byte[] shortToByteArray(short input)
	{
		ByteBuffer buf = ByteBuffer.allocate(2); // Long değer 8 byte
		buf.putShort(0, input);
		buf.flip();
		return buf.array();
	}

	/**
	 * Verilen byte array i short a çevirir
	 * @param input
	 * @return
	 */
	public static short byteArrayToShort(byte[] input)
	{
		ByteBuffer buf = ByteBuffer.allocate(2);
		buf.put(input, 0, 2);
		buf.flip();
		return buf.getShort(0);
	}

	public static String shortToHexString(short input)
	{
		ByteBuffer buf = ByteBuffer.allocate(2); // Long değer 8 byte
		buf.putShort(0, input);
		buf.flip();
		return byteArrayToHexString(buf.array(), null);
	}

	public static String byteToHexString(byte input)
	{
		return byteVal[input & 0xFF];
	}

	/**
	 * Verilen bir Hex String'in long sayı değerini döndürür.
	 * @param inputStr: Hex String
	 * @return : Hex String'in long karşılığı.
	 */
	public static long hexStringToLong(String inputStr)
	{
		byte[] tempbuff = hexStringToByteArray(inputStr, null);
		ByteBuffer buf = ByteBuffer.allocate(tempbuff.length);
		buf.put(tempbuff);
		buf.flip();
		return buf.getLong();
	}

	/**
	 * Verilen bir int sayıyı Hex String'e dönüştürür.
	 * @param input hex string'e dönüştürülecek int cinsinden giriş.
	 * @return int girişin Hex String karşılığı
	 */
	public static String intToHexString(int input)
	{
		ByteBuffer buf = ByteBuffer.allocate(4); //int değer 4 byte
		buf.putInt(0, input);
		buf.flip();
		return byteArrayToHexString(buf.array(), null);
	}

	/**
	 * Verilen bir int sayıyı byte array'e dönüştürür.
	 * @param input byte arraye dönüştürülecek int sayısı
	 * @return int girişin 4 byte array karşılığı
	 * Aynısı converters classında da var.
	 */
	public static byte[] intToByteArray(int input)
	{
		ByteBuffer buf = ByteBuffer.allocate(4); //int değer 4 byte
		buf.putInt(0, input);
		buf.flip();
		return buf.array();
	}

	/**
	 * Verilen byte dizisinin int değerini verir.
	 * @param input
	 * @return integer derived from the first 4 bytes of the given array.
	 */
	public static int byteArrayToInt(byte[] input)
	{
		ByteBuffer buf = ByteBuffer.allocate(4); //int değer 4 byte
		buf.put(input, 0, 4);
		buf.flip();
		return buf.getInt(0);
	}

	/**
	 * Verilen bir Hex String'in int sayı değerini döndürür.
	 * @param inputStr: Hex String
	 * @return : Hex String'in int karşılığı.
	 */
	public static int hexStringToInt(String inputStr)
	{
		byte[] tempbuff = hexStringToByteArray(inputStr, null);
		ByteBuffer buf = ByteBuffer.allocate(tempbuff.length);
		buf.put(tempbuff);
		buf.flip();
		return buf.getInt();
	}

	/**
	 * Verilen bir Hex String'in byte sayı değerini döndürür.
	 * @param inputStr: Hex String
	 * @return : Hex String'in int karşılığı.
	 */
	public static byte hexStringToByte(String inputStr)
	{
		byte[] tempbuff = hexStringToByteArray(inputStr, null);
		/*        ByteBuffer buf = ByteBuffer.allocate(tempbuff.length);
		        buf.put(tempbuff);
		        buf.flip();*/
		return tempbuff[0];
	}

	/**
	 * Bir byte dizisini aralarında . olan ondalık sayılar stringine  dönüştürür.
	 * Örnek: giriş : 0x011A03A0  çıkış : 1.26.3.160
	 *
	 * @param array noktalı formata 'e dönüştürülecek giriş dizisi
	 * @return : Dotted decimal String
	 */
	public static String byteArrayToDottedString(byte array[])
	{
		String spacer = ".";
		if (spacer == null)
		{
			spacer = "";
		}
		StringBuffer str = new StringBuffer(array.length * 4);
		int hi = 0;
		int lo = 0;
		int mask = 0x0F;
		for (byte element : array)
		{
			hi = (element >> 4) & mask;
			lo = element & mask;
			str.append(hi * 16 + lo);
			str.append(spacer);
		}
		str.setLength(str.length() - spacer.length()); //en son spacer ı silmek için
		return str.toString().trim();
	}

	/**
	 * İçinde noktayla ayrılmış ondalık sayılar olan -her sayı üç basamağa kadar- bir string'i byte dizisine dönüştürür.
	 * DİKKAT Girişler 256'den küçük olmalıdır
	 *  1.2.110.65  {@literal -->} 0x01026E41
	 * @param str : Giriş dotted stringi
	 * @return : byte[] cinsinden, dotted stringinin içindeki  sayısal değerler
	 * @throws NumberFormatException
	 */
	public static byte[] dottedStringToByteArray(String str) throws NumberFormatException
	{
		/** @todo: 127 den buyuk olursa noktalar arası rakamlar ne yapılacağı belirlenmelidir. TD.
		    @done: 255'e kadar çalıştığı görüldü. 255'den büyükleri (mod 255) yapıyor. TD. */
		String spacer = ".";
		int count = 0;
		byte array[];
		StringTokenizer token = new StringTokenizer(str, spacer);
		count = token.countTokens();
		array = new byte[count];
		int i = 0;
		for (i = 0; i < count; i++)
		{
			array[i] = (byte) Integer.parseInt(token.nextToken());
		}
		return array;
	}

	public static byte[] concatByteArrays(byte[] b1, byte[] b2)
	{
		ByteBuffer buf = ByteBuffer.allocate(b1.length + b2.length);
		buf.put(b1);
		buf.put(b2);
		buf.flip();
		return buf.array();
	}

	public static String getUTFString(String source)
	{
		try
		{
			return new String(source.getBytes("UTF-8"), "UTF-8");
		} catch (Exception e)
		{
			logger.warn("Warning in HexConverter", e);
			return source;
		}
	}

	public static String byteArrayToHexString(byte[] array, int start, int length, String spacer)
	{
		if (array == null) return "null";
		if (array.length == 0) return "empty array";

		int strlen = spacer.length();
		byte strBuffer[] = new byte[length * (strlen + 2) - strlen];
		int j = 0;
		char ch[] = spacer.toCharArray();
		try
		{
			int end = start + length;
			for (int i = start; i < end; i++)
			{
				strBuffer[j++] = (byte) hexChars[(array[i] & 0xF0) >> 4];
				strBuffer[j++] = (byte) hexChars[(array[i] & 0x0F)];
				if (j + strlen < end)
				{
					for (char element : ch)
					{
						strBuffer[j++] = (byte) element;
					}
				}
			}
		} catch (RuntimeException e)
		{
			logger.warn("Warning in HexConverter", e);
			return "";
		}
		return new String(strBuffer);
	}

	public static String byteArrayToHexString(byte[] array, int start, int length)
	{
		if (array == null) return "null";
		if (array.length == 0) return "empty array";
		byte strBuffer[] = new byte[length * 2];
		int j = 0;
		try
		{
			int end = start + length;
			for (int i = start; i < end; i++)
			{
				strBuffer[j++] = (byte) hexChars[(array[i] & 0xF0) >> 4];
				strBuffer[j++] = (byte) hexChars[(array[i] & 0x0F)];
			}
		} catch (RuntimeException e)
		{
			logger.warn("Warning in HexConverter", e);
			return "";
		}
		return new String(strBuffer);
	}

	public static String integerToHexString(int i, int numDigits)
	{
		String h = Integer.toHexString(i).toUpperCase();
		while (h.length() < numDigits)
			h = "0" + h;

		return h;
	}
}
