package gnu.crypto.key.dss;

import gnu.crypto.key.IKeyPairCodec;

import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.DSAParams;
import gnu.crypto.util.TLV;
import java.security.PrivateKey;
import java.math.BigInteger;

/**
 * <p>Title: CC</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class DSSKeyPairX509Codec implements IKeyPairCodec
{

  public static final byte[] DSA_OIDarray = new byte[] {
                                          (byte) 0x06, (byte) 0x07, (byte) 0x2A,
                                          (byte) 0x86, (byte) 0x48, (byte) 0xCE,
                                          (byte) 0x38, (byte) 0x04, (byte) 0x01};


  public int getFormatID()
  {
    return X509_FORMAT;
  }


  public byte[] encodePublicKey(PublicKey key)
  {
    if(! (key instanceof DSAPublicKey)) {
      throw new IllegalArgumentException("key");
    }

    DSAPublicKey dsakey = (DSAPublicKey)key;

    DSAParams params = dsakey.getParams();
    byte[] p_array = params.getP().toByteArray();
    byte[] q_array = params.getQ().toByteArray();
    byte[] g_array = params.getG().toByteArray();
    byte[] y_array = dsakey.getY().toByteArray();

    //n ve e'yi ASN1 integer olarak DER encode et.
    byte[] tlv_p = TLV.makeTLV( (byte) 02, p_array);
    byte[] tlv_q = TLV.makeTLV( (byte) 02, q_array);
    byte[] tlv_g = TLV.makeTLV( (byte) 02, g_array);
    byte[] tlv_y = TLV.makeTLV( (byte) 02, y_array);

    //uc integer'i yan yan koy...
    byte[] tlv_pqg = TLV.yanyanaKoy(tlv_p,tlv_q,tlv_g);

    //uc integerdan parametreleri olustur...
    byte[] tlv_params = TLV.makeTLV((byte)0x30,tlv_pqg);
    //parametrelerle objectId'yi yanyana koy...
    byte[] algo = TLV.yanyanaKoy(DSA_OIDarray,tlv_params);
    //ve bunu tlv yap..
    byte[] tlv_algo = TLV.makeTLV((byte)0x30,algo);

    //y'nin basina 0 koyup tlv yap...
    byte[] os = new byte[tlv_y.length+1];
    System.arraycopy(tlv_y,0,os,1,tlv_y.length);
    os[0] = 0;
    byte[] tlv_os = TLV.makeTLV((byte)0x03,os);
    //algo ile y'yi yanyana koyup tlv yap...
    return TLV.makeTLV((byte)0x30,TLV.yanyanaKoy(tlv_algo,tlv_os));
  }

  public byte[] encodePrivateKey(PrivateKey key)
  {
    throw new RuntimeException("not supported...");
  }

  public PublicKey decodePublicKey(byte[] input)
  {
    //SEQUENCE olmali...
    if(input[0] != 0x30)
      throw new IllegalArgumentException("Not Sequence");
    int[] sinir = TLV.getIcerik(input, 0);
    if(sinir[1] != input.length - 1)
      throw new IllegalArgumentException("DER decode error");
    //Algoritmayi gosteren sequence olmali...
    int i;
    i = sinir[0];
    if(input[i] != 0x30)
      throw new IllegalArgumentException("Not Sequence");
    sinir = TLV.getIcerik(input, i);
    //basta DSA OId olmali...
    int j = 0;
    for(i = sinir[0];j < DSA_OIDarray.length;i++)
      if(input[i] != DSA_OIDarray[j++])
        throw new IllegalArgumentException("Object ID not found");

    //DSA parametreler olmali...
    j = sinir[1]+1;
    sinir = TLV.getIcerik(input, i);
    i = sinir[0];
    BigInteger p = TLV.decodeINT(input,i,sinir);
    i = sinir[1]+1;
    BigInteger q = TLV.decodeINT(input,i,sinir);
    i = sinir[1]+1;
    BigInteger g = TLV.decodeINT(input,i,sinir);
    i = sinir[1]+1;
    if(i!=j)
      throw new IllegalArgumentException("DER decode error");

    //i anahtarin bulundugu BITSTRING yapisina ulasti...
    if(input[i] != 0x03)
      throw new IllegalArgumentException("DER decode error");

    //BITSTRING icerigini alalim...
    sinir = TLV.getIcerik(input, i);
    if(sinir[1] != input.length - 1)
      throw new IllegalArgumentException("DER decode error");
    //ilk byte 0 olmali
    i = sinir[0] + 1;

    BigInteger y = TLV.decodeINT(input,i,sinir);

    return new DSSPublicKey(p,q,g,y);

  }

  public PrivateKey decodePrivateKey(byte[] input)
  {
    throw new RuntimeException("not supported...");
  }


}