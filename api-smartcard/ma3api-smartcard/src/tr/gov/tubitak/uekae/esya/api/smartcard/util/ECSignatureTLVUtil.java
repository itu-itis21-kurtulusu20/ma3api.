package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;

import java.math.BigInteger;

/**
 * Created by orcun.ertugrul on 20-Dec-17.
 */
public class ECSignatureTLVUtil
{
    public static byte[] addTLVToSignature(byte [] rAnds)
    {
        byte[] first_half = new byte[rAnds.length/2];
        byte[] second_half = new byte[rAnds.length/2];
        System.arraycopy(rAnds, 0, first_half, 0, first_half.length);
        System.arraycopy(rAnds, first_half.length, second_half, 0, first_half.length);

        BigInteger r = new BigInteger(1,first_half);
        BigInteger s = new BigInteger(1,second_half);

        byte[] tlv_f = TLV.makeTLV((byte) 02, r.toByteArray());
        byte[] tlv_s = TLV.makeTLV( (byte) 02, s.toByteArray());

        byte[] halfwithhalf = TLV.yanyanaKoy(tlv_f, tlv_s);
        byte[] signature = TLV.makeTLV( (byte) 0x30, halfwithhalf);
        return signature;
    }

    public static byte [] removeTLVFromSignature(byte [] aSig) throws ESYAException
    {
        byte[] x = (byte[])aSig;
        //ilk tag 30 olmali...
        if(x[0] != (byte)0x30)//SEQUENCE
            throw new ESYAException("Signature is not in TLV Format!");
        //icerigi alalim
        int[] sinir = TLV.getIcerik(x, 0);
        if(sinir[1]+1 != x.length)
            throw new ESYAException("Signature is not in TLV Format!");
        //icerikte ardarda iki int olmali
        if(x[sinir[0]] != 0x02) //INTEGER
            throw new ESYAException("Signature is not in TLV Format!");
        int[] sinir_r = TLV.getIcerik(x,sinir[0]);
        if(x[sinir_r[1]+1] != 0x02) //INTEGER
            throw new ESYAException("Signature is not in TLV Format!");
        int[] sinir_s = TLV.getIcerik(x,sinir_r[1]+1);

        byte[] r_array = new byte[sinir_r[1]-sinir_r[0]+1];
        byte[] s_array = new byte[sinir_s[1]-sinir_s[0]+1];

        System.arraycopy(x,sinir_r[0],r_array,0,r_array.length);
        System.arraycopy(x,sinir_s[0],s_array,0,s_array.length);

        r_array = trimZerosFromStart(r_array);
        s_array = trimZerosFromStart(s_array);

        //r ve s uzunluklarını aynı yapılıyor.
        if(r_array.length < s_array.length)
            r_array = padZeroToArray(r_array, s_array.length);

        if(s_array.length < r_array.length)
            s_array = padZeroToArray(s_array, r_array.length);


        return ByteUtil.concatAll(r_array, s_array);
    }

    public static boolean isSignatureInTLVFormat(byte [] aSig)
    {
        try
        {
            byte[] x = (byte[]) aSig;
            //ilk tag 30 olmali...
            if (x[0] != (byte) 0x30)//SEQUENCE
                return false;

            //icerigi alalim
            int[] sinir = TLV.getIcerik(x, 0);
            if (sinir[1] + 1 != x.length)
                return false;

            //icerikte ardarda iki int olmali
            if (x[sinir[0]] != 0x02) //INTEGER
                return false;
            int[] sinir_r = TLV.getIcerik(x, sinir[0]);

            if (x[sinir_r[1] + 1] != 0x02) //INTEGER
                return false;
            int[] sinir_s = TLV.getIcerik(x, sinir_r[1] + 1);

            if (sinir_s[1] + 1 == aSig.length)
                return true;
            else
                return false;
        }
        catch (Exception ex){
            return false;
        }
    }


    public static byte[] trimZerosFromStart(byte[] array)
    {
        int i = 0;
        while(array[i] == 0)
            i++;

        if(i == 0) {
            return array;
        } else {
            byte[] newArray = new byte[array.length - i];
            System.arraycopy(array, i, newArray, 0, array.length - i);
            return newArray;
        }
    }



    private static byte[] padZeroToArray(byte[] array, int length)
    {
        int padLen = length - array.length;
        byte [] pad = new byte [padLen];
        return ByteUtil.concatAll(pad, array);
    }
}
