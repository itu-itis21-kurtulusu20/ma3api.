using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common;

namespace smartcard.src.tr.gov.tubitak.uekae.esya.api.smartcard.util
{
    public static class TLV
    {

        public static byte[] yanyanaKoy(byte[] tlv1, byte[] tlv2)
        {
            byte[] r = new byte[tlv1.Length + tlv2.Length];
            Array.Copy(tlv1, 0, r, 0, tlv1.Length);
            Array.Copy(tlv2, 0, r, tlv1.Length, tlv2.Length);
            return r;
        }

        /**
         *
         * @param x byte []
         * @param startIndex Tag index.
         * @return startIndex and endIndex
         */
        public static int[] getIcerik(byte[] x, int startIndex)
        {
            //startIndex: Tag index
            int lenghtStartIndex = startIndex + 1;
            if ((x[lenghtStartIndex] & 0x00ff) == 0x80)
                throw new ESYARuntimeException("BER desteklenmiyor");
            if ((x[lenghtStartIndex] & 0x00ff) < 0x80)
                return new int[] { lenghtStartIndex + 1, lenghtStartIndex + (byte)x[lenghtStartIndex] };
            int l_son = lenghtStartIndex + (((byte)x[lenghtStartIndex]) - ((byte)0x80));
            int i;
            int uzunluk = 0;// = ((byte)x[l_son]&0x00ff);
            for (i = lenghtStartIndex + 1; i <= l_son; i++)
            {
                uzunluk = (uzunluk << 8) + ((byte)x[i] & 0x00ff);
            }
            return new int[] { l_son + 1, l_son + uzunluk };
        }

        public static byte[] makeTLV(byte tag, byte[] x)
        {
            //uzunlugu encode et...
            byte[] tlv_l_x = TLV_L(x);
            //gerekli yeri al.
            byte[] tlv_x = new byte[1 + tlv_l_x.Length + x.Length];
            //ilk byte'i tag olarak set et
            tlv_x[0] = tag;
            //uzunlugu ve veriyi ekle
            Array.Copy(tlv_l_x, 0, tlv_x, 1, tlv_l_x.Length);
            Array.Copy(x, 0, tlv_x, 1 + tlv_l_x.Length, x.Length);
            //oluturulan yapiyi don.
            return tlv_x;
        }

        public static byte[] TLV_L(byte[] x)
        {
            if (x.Length < 0x80)
                return new byte[] { (byte)x.Length };
            else
            {
                int b = x.Length;
                int a = 1;
                int k = 0x0100;
                //kac byte olacagini bul...
                while (b >= k)
                {
                    a++;
                    k = k << 8;
                }
                byte[] r = new byte[a + 1];
                //ilk byte'i DER'e uygun olarak set et...
                r[0] = (byte)(((byte)0x80) + ((byte)a));
                //hex olarak array'a cevir...
                for (int i = a; i > 0; i--)
                {
                    //en sagdaki byte'i al..
                    r[i] = (byte)(b);
                    //en sagdaki byte'i at...
                    b = b >> 8;
                }
                return r;
            }
        }
    }
}
