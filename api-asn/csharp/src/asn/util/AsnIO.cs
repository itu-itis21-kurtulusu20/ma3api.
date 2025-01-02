using System;
using System.IO;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.asn.util
{
    //import tr.gov.tubitak.uekae.esya.annotations.ApiClass;
    //import tr.gov.tubitak.uekae.esya.api.common.util.Base64;

    /**
     * <p>Title: ESYA</p>
     * <p>Description: </p>
     * <p>Copyright: TUBITAK Copyright (c) 2004</p>
     * <p>Company: TUBITAK UEKAE</p>
     * @author Muhammed Serdar SORAN
     * @version 1.0
     */
    //todo Annotation!
    //@ApiClass

    public class AsnIO
    {
        public AsnIO()
        {
            //do nothing
        }

        /**
         * Write byte array to the file
         * @param aSonuc Byte array that will be written
         * @param aFileName File that byte array will be written on it
         * @throws IOException
         */
        //Todo: remove this function, use FileUtil class.
        public static void dosyayaz(byte[] fileBytes, String filePath)
        {
            FileUtil.writeBytes(filePath, fileBytes);
        }

        /**
         * Write Asn1Type to the file
         * @param aAsn1 Asn1Type that will be written
         * @param aFileName File that byte array will be written on it
         * @throws IOException
         * @throws Asn1Exception
         */
        public static void dosyayaz(Asn1Type aAsn1, String aFile)
        {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            aAsn1.Encode(encBuf);
            dosyayaz(encBuf.MsgCopy, aFile);
        }
        /**
         * Write Asn1Type to the file as Base64
         * @param aAsn1 Asn1Type that will be written
         * @param aFileName File that byte array will be written on it
         * @throws IOException
         * @throws Asn1Exception
         */
        public static void dosyaBase64yaz(Asn1Type aAsn1, String aFile)
        {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            aAsn1.Encode(encBuf);
            dosyayaz(Encoding.ASCII.GetBytes(Convert.ToBase64String(encBuf.MsgCopy)), aFile);
        }

        /**
         * Read data from file as byte array
         * @param aFile File that will be read
         * @return byte[]
         * @throws IOException
         */
        //Todo: remove this function, use FileUtil class.
        public static byte[] dosyadanOKU(String filePath)
        {
            return FileUtil.readBytes(filePath);
        }

        private static bool besTire(byte[] aVeri, int aIndex)
        {
            int i;
            for (i = 0; i < 5; i++)
                if ((aVeri.Length <= (aIndex + i)) || (aVeri[aIndex + i] != ((byte)'-')))
                    return false;
            return true;
        }
        private static byte[] base64deTemizlikYap(byte[] aKirliBase64)
        {
            byte[] temizBase64 = new byte[aKirliBase64.Length];
            int len = 0;
            for (int i = 0; i < aKirliBase64.Length; i++)
            {
                if (besTire(aKirliBase64, i))
                {
                    //sonraki besTire'ye kadar atlayalim
                    i += 5;
                    while ((!besTire(aKirliBase64, i)) && (i < aKirliBase64.Length))
                    {
                        i++;
                    }
                    i += 4;
                }
                else
                    temizBase64[len++] = aKirliBase64[i];
            }

            byte[] terTemizBase64 = new byte[len];
            Array.Copy(temizBase64, 0, terTemizBase64, 0, len);
            return terTemizBase64;
        }
        /**
         * Read Asn1Type from a byte array
         * @param aAsn1 Asn1Type that will be read
         * @param aDer Byte array which is Der Encoded
         * @throws IOException
         * @throws Asn1Exception
         */
        public static void derOku(Asn1Type aAsn1, byte[] aDer)
        {
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aDer);
            aAsn1.Decode(decBuf);
        }
        /**
         * Read Asn1Type from a byte array
         * @param aAsn1 Asn1Type that will be read
         * @param aDer Byte array which is Der or Base64 Encoded
         * @throws IOException
         * @throws Asn1Exception
         */
        public static void arraydenOku(Asn1Type aAsn1, byte[] aVeri)
        {
            try
            {
                //Once direct der encoded oldugunu varsayip sansimizi deneyelim...
                derOku(aAsn1, aVeri);
            }
            catch (Exception ex)
            {
                //Bir de base64 oldugunu varsayip sansimizi deneyelim.
                byte[] yeniVeri = base64deTemizlikYap(aVeri);
                byte[] derVeri = Convert.FromBase64String(Encoding.ASCII.GetString(yeniVeri));
                //byte[] derVeri = Base64.decode(yeniVeri,0,yeniVeri.Length);
                derOku(aAsn1, derVeri);
            }

        }
        /**
         * Read Asn1Type from a byte array
         * @param aAsn1 Asn1Type that will be read
         * @param aDer Byte array which is Base64 Encoded
         * @throws IOException
         * @throws Asn1Exception
         */
        public static void arraydenBase64Oku(Asn1Type aAsn1, byte[] aVeri)
        {
            byte[] yeniVeri = base64deTemizlikYap(aVeri);
            byte[] derVeri = Convert.FromBase64String(Encoding.ASCII.GetString(yeniVeri));
            derOku(aAsn1, derVeri);
        }
        /**
         * Read  from an input stream
         * @param is InputStream that will be read
         * @return byte[]
         * @throws IOException
         */
        public static byte[] streamOku(Stream aInputStream)
        {
            using (MemoryStream ba = new MemoryStream())
            {
                byte[] block = new byte[4096];
                while (true)
                {
                    int lenght = aInputStream.Read(block, 0, block.Length);                    
                    if (lenght == 0)
                        break;
                    ba.Write(block, 0, lenght);
                }
                return ba.ToArray();
            }
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
        public static byte[] dosyadanOKU(Asn1Type aAsn1, String aFile)
        {
            byte[] b = dosyadanOKU(aFile);
            arraydenOku(aAsn1, b);
            return b;
        }
        /**
         * Get Asn1Type as String
         * @param aAsn1 Asn1Type that will be converted to string
         * @return String
         */
        public static String getFormattedAsn(Asn1Type asn1Type)
        {
            using (StringWriter baos = new StringWriter())
            {
                asn1Type.Print(baos, "Asn1Type:" + asn1Type.GetType().ToString(), 0);
                return baos.ToString();
            }
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
}
