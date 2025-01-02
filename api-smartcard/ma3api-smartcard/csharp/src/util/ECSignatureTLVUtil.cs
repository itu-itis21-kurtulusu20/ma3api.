using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Math;

namespace smartcard.src.tr.gov.tubitak.uekae.esya.api.smartcard.util
{
    public static class ECSignatureTLVUtil
    {
        public static bool isSignatureInTLVFormat(byte[] aSig)
        {
            byte[] x = (byte[])aSig;
            //ilk tag 30 olmali...
            if (x[0] != (byte)0x30)//SEQUENCE
                return false;
            //icerigi alalim
            int[] sinir = TLV.getIcerik(x, 0);
            if (sinir[1] + 1 != x.Length)
                return false;
            //icerikte ardarda iki int olmali
            if (x[sinir[0]] != 0x02) //INTEGER
                return false;
            int[] sinir_r = TLV.getIcerik(x, sinir[0]);
            if (x[sinir_r[1] + 1] != 0x02) //INTEGER
                return false;
            int[] sinir_s = TLV.getIcerik(x, sinir_r[1] + 1);

            if (sinir_s[1] + 1 == aSig.Length)
                return true;
            else
                return false;
        }

        public static byte[] addTLVToSignature(byte[] rAnds)
        {
            byte[] first_half = new byte[rAnds.Length / 2];
            byte[] second_half = new byte[rAnds.Length / 2];
            Array.Copy(rAnds, 0, first_half, 0, first_half.Length);
            Array.Copy(rAnds, first_half.Length, second_half, 0, first_half.Length);

            BigInteger r = new BigInteger(1, first_half);
            BigInteger s = new BigInteger(1, second_half);

            byte[] tlv_f = TLV.makeTLV((byte)02, r.ToByteArray());
            byte[] tlv_s = TLV.makeTLV((byte)02, s.ToByteArray());

            byte[] halfwithhalf = TLV.yanyanaKoy(tlv_f, tlv_s);
            byte[] signature = TLV.makeTLV((byte)0x30, halfwithhalf);
            return signature;
        }
    }
}
