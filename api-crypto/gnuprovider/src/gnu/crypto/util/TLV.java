package gnu.crypto.util;

import java.math.BigInteger;


/**
 * <p>Title: CC</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class TLV {

  public TLV()
  {
       //Do nothing
  }

  public static byte[] yanyanaKoy(byte[] tlv1, byte[] tlv2)
  {
    byte[] r = new byte[tlv1.length + tlv2.length];
    System.arraycopy(tlv1, 0, r, 0, tlv1.length);
    System.arraycopy(tlv2, 0, r, tlv1.length, tlv2.length);
    return r;
  }

  public static byte[] yanyanaKoy(byte[] tlv1, byte[] tlv2,byte[] tlv3)
  {
    byte[] r = new byte[tlv1.length + tlv2.length + tlv3.length];
    System.arraycopy(tlv1, 0, r, 0, tlv1.length);
    System.arraycopy(tlv2, 0, r, tlv1.length, tlv2.length);
    System.arraycopy(tlv3, 0, r, tlv1.length+tlv2.length, tlv3.length);
    return r;
  }


  public static BigInteger decodeINT(byte[] input,int i,int[] sinir2)
  {
    //INTEGER olmasi gerekir
    if(input[i] != 0x02)
      throw new IllegalArgumentException("DER decode error");
    int[] sinir = getIcerik(input, i);
    byte[] n_array = new byte[sinir[1] - sinir[0] + 1];
    System.arraycopy(input, sinir[0], n_array, 0, n_array.length);

    sinir2[0] = sinir[0];
    sinir2[1] = sinir[1];
    return new BigInteger(n_array);
  }


  public static byte[] makeTLV(byte tag, byte[] x)
  {
    //uzunlugu encode et...
    byte[] tlv_l_x = TLV_L(x);
    //gerekli yeri al.
    byte[] tlv_x = new byte[1 + tlv_l_x.length + x.length];
    //ilk byte'i tag olarak set et
    tlv_x[0] = tag;
    //uzunlugu ve veriyi ekle
    System.arraycopy(tlv_l_x, 0, tlv_x, 1, tlv_l_x.length);
    System.arraycopy(x, 0, tlv_x, 1 + tlv_l_x.length, x.length);
    //oluturulan yapiyi don.
    return tlv_x;
  }

  public static byte[] TLV_L(byte[] x)
  {
    if(x.length < 0x80)
      return new byte[] { (byte) x.length};
    else {
      int b = x.length;
      int a = 1;
      int k = 0x0100;
      //kac byte olacagini bul...
      while(b >= k) {
        a++;
        k = k << 8;
      }
      byte[] r = new byte[a + 1];
      //ilk byte'i DER'e uygun olarak set et...
      r[0] = (byte)(((byte) 0x80) + ((byte)a));
      //hex olarak array'a cevir...
      for(int i = a;i > 0;i--) {
        //en sagdaki byte'i al..
        r[i] = (byte) (b);
        //en sagdaki byte'i at...
        b = b >> 8;
      }
      return r;
    }
  }

  public static int[] getIcerik(byte[] x,int bas)
  {
    int l_bas = bas+1;
    if((x[l_bas]&0x00ff) == 0x80)
      throw new RuntimeException("BER desteklenmiyor");
    if((x[l_bas]&0x00ff) < 0x80)
      return new int[] {l_bas+1,l_bas+(byte)x[l_bas]};
    int l_son = l_bas +  (  ((byte)x[l_bas]) - ((byte)0x80)  );
    int i;
    int uzunluk=0;// = ((byte)x[l_son]&0x00ff);
    for(i=l_bas+1;i<=l_son;i++)
    {
      uzunluk = (uzunluk << 8) + ((byte)x[i]&0x00ff);
    }
    return new int[] {l_son+1,l_son+uzunluk};
  }

  public static void main(String[] args)
  {

    int[] x = getIcerik(TLV.makeTLV((byte)0x30,new byte[1000]),0);
    System.out.println("iste :" +x[0]+ " " + x[1]);

    x = getIcerik(new byte[]{0x30,2,1,2,0x30,1,10},0);
    System.out.println("iste :" +x[0]+ " " + x[1]);
  }

}