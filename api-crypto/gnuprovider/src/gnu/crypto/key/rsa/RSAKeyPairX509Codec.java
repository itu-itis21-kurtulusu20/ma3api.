package gnu.crypto.key.rsa;


import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import gnu.crypto.key.IKeyPairCodec;
import gnu.crypto.util.TLV;
import java.security.interfaces.RSAPrivateCrtKey;


/**
 * <p>Title: CC</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class RSAKeyPairX509Codec implements IKeyPairCodec {

  public static final byte[] RSA_OIDarray = new byte[] {
                                            (byte) 0x30, (byte) 0x0D, (byte) 0x06, (byte) 0x09, (byte) 0x2A,
                                            (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01,
                                            (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x00};
  public static final byte[] RSA_OIDarray2 = new byte[] {
                                          (byte) 0x30, (byte) 0x0B, (byte) 0x06, (byte) 0x09, (byte) 0x2A,
                                          (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, (byte) 0x0D, (byte) 0x01,
                                          (byte) 0x01, (byte) 0x01};



  public int getFormatID()
  {
    return X509_FORMAT;
  }



  public byte[] encodePublicKey(PublicKey key)
  {
    if(! (key instanceof RSAPublicKey)) {
      throw new IllegalArgumentException("key");
    }

    RSAPublicKey rsakey = (RSAPublicKey)key;

    byte[] n_array = rsakey.getModulus().toByteArray();
    byte[] e_array = rsakey.getPublicExponent().toByteArray();

    //n ve e'yi ASN1 integer olarak DER encode et.
    byte[] tlv_n = TLV.makeTLV( (byte) 02, n_array);
    byte[] tlv_e = TLV.makeTLV( (byte) 02, e_array);

    //iki integer'i yan yan koy...
    byte[] rawrsakey = new byte[tlv_n.length + tlv_e.length];
    System.arraycopy(tlv_n, 0, rawrsakey, 0, tlv_n.length);
    System.arraycopy(tlv_e, 0, rawrsakey, tlv_n.length, tlv_e.length);
    //iki integerdan rsa key icin bir ASN1 sequence olustur.
    byte[] tlv_rsakey = TLV.makeTLV( (byte) 0x30, rawrsakey);
    //bu sequence'in basina 00 koyarak bitstring olarak hazirla
    byte[] bitrsa = new byte[tlv_rsakey.length + 1];
    bitrsa[0] = 0;
    System.arraycopy(tlv_rsakey, 0, bitrsa, 1, tlv_rsakey.length);
    //bit String'i DER encode et
    byte[] tlv_bitrsa = TLV.makeTLV( (byte) 0x03, bitrsa);
    //RSA algorithm Identifier ile bit string yan yana koy..
    byte[] encode = new byte[RSA_OIDarray.length + tlv_bitrsa.length];
    System.arraycopy(RSA_OIDarray, 0, encode, 0, RSA_OIDarray.length);
    System.arraycopy(tlv_bitrsa, 0, encode, RSA_OIDarray.length, tlv_bitrsa.length);

    //son yapiyi encode et
    return TLV.makeTLV( (byte) 0x30, encode);
  }




  public byte[] encodePrivateKey(PrivateKey key)
  {
    if(! (key instanceof RSAPrivateCrtKey)) {
      throw new IllegalArgumentException("key");
    }
    RSAPrivateCrtKey privK = (RSAPrivateCrtKey)key;

    byte[] n_array = privK.getModulus().toByteArray();
    byte[] e_array = privK.getPublicExponent().toByteArray();
    byte[] d_array = privK.getPrivateExponent().toByteArray();
    byte[] p_array = privK.getPrimeP().toByteArray();
    byte[] q_array = privK.getPrimeQ().toByteArray();
    byte[] dp_array = privK.getPrimeExponentP().toByteArray();
    byte[] dq_array = privK.getPrimeExponentQ().toByteArray();
    byte[] coef_array = privK.getCrtCoefficient().toByteArray();

    //Hepsini ASN1 integer olarak DER encode et.
    byte[] tlv_version = TLV.makeTLV( (byte) 02, new byte[]{0});
    byte[] tlv_n = TLV.makeTLV( (byte) 02, n_array);
    byte[] tlv_e = TLV.makeTLV( (byte) 02, e_array);
    byte[] tlv_d = TLV.makeTLV( (byte) 02, d_array);
    byte[] tlv_p = TLV.makeTLV( (byte) 02, p_array);
    byte[] tlv_q = TLV.makeTLV( (byte) 02, q_array);
    byte[] tlv_dp = TLV.makeTLV( (byte) 02, dp_array);
    byte[] tlv_dq = TLV.makeTLV( (byte) 02, dq_array);
    byte[] tlv_coef = TLV.makeTLV( (byte) 02, coef_array);

    //integer'lari yan yan koy...
    byte[] tlv_version_n = TLV.yanyanaKoy(tlv_version,tlv_n);
    byte[] tlv_e_d = TLV.yanyanaKoy(tlv_e,tlv_d);
    byte[] tlv_p_q = TLV.yanyanaKoy(tlv_p,tlv_q);
    byte[] tlv_dp_dq = TLV.yanyanaKoy(tlv_dp,tlv_dq);
    byte[] tlv_version_n_e_d = TLV.yanyanaKoy(tlv_version_n,tlv_e_d);
    byte[] tlv_p_q_dp_dq = TLV.yanyanaKoy(tlv_p_q,tlv_dp_dq);
    byte[] tlv_p_q_dp_dq_coef = TLV.yanyanaKoy(tlv_p_q_dp_dq,tlv_coef);
    byte[] tlv_version_n_e_d_p_q_dq_dq_coef = TLV.yanyanaKoy(tlv_version_n_e_d,tlv_p_q_dp_dq_coef);

    //iki integerdan rsa key icin bir ASN1 sequence olustur.
    byte[] tlv_privKey = TLV.makeTLV( (byte) 0x30, tlv_version_n_e_d_p_q_dq_dq_coef);
    //OCTETSTRING'i DER encode et
    byte[] tlv_octetrsa = TLV.makeTLV( (byte) 0x04, tlv_privKey);
    //version ve RSA algorithm Identifier ile octet string'i yan yana koy..
    byte[] encode = new byte[3+RSA_OIDarray.length + tlv_octetrsa.length];
    encode[0] = 0x02;
    encode[1] = 0x01;
    encode[2] = 0x00;
    System.arraycopy(RSA_OIDarray, 0, encode, 3, RSA_OIDarray.length);
    System.arraycopy(tlv_octetrsa, 0, encode, RSA_OIDarray.length+3, tlv_octetrsa.length);

    //son yapiyi encode et
    return TLV.makeTLV( (byte) 0x30, encode);
  }

  private int findRSAOID(byte[] input,int bas)
  {
    int i;
    int j = 0;

    byte[] RSA_OID = null;
    if(input[bas+1] == RSA_OIDarray[1]) RSA_OID = RSA_OIDarray;
    else if(input[bas+1] == RSA_OIDarray2[1]) RSA_OID = RSA_OIDarray2;
    else throw new IllegalArgumentException("Object ID not found");

    for(i=bas;j<RSA_OID.length;i++)
      if(input[i] != RSA_OID[j++])
        throw new IllegalArgumentException("Object ID not found");
    return i;
  }

  public PublicKey decodePublicKey(byte[] input)
  {
    //SEQUENCE olmali...
    if(input[0] != 0x30)
      throw new IllegalArgumentException("Not Sequence");
    int[] sinir = TLV.getIcerik(input,0);
    if(sinir[1] != input.length-1)
      throw new IllegalArgumentException("DER decode error");
    //basta RSA OId olmali...
    /*
    int i;
    int j = 0;
    for(i=sinir[0];j<RSA_OIDarray.length;i++)
      if(input[i] != RSA_OIDarray[j++])
        throw new IllegalArgumentException("Object ID not found");
     */
    int i = findRSAOID(input,sinir[0]);
    //i anahtarin bulundugu BITSTRING yapisina ulasti...
    if(input[i] != 0x03)
      throw new IllegalArgumentException("DER decode error");

    //BITSTRING icerigini alalim...
    sinir = TLV.getIcerik(input,i);
    if(sinir[1] != input.length-1)
      throw new IllegalArgumentException("DER decode error");
    //ilk byte 0 olmali
    i = sinir[0]+1;
    if(input[i] != 0x30)
      throw new IllegalArgumentException("DER decode error");
    //SEQUENCE icerigini alalim...
    sinir = TLV.getIcerik(input,i);
    if(sinir[1] != input.length-1)
      throw new IllegalArgumentException("DER decode error");
    i = sinir[0];
    if(input[i] != 0x02)
      throw new IllegalArgumentException("DER decode error");
    //INTEGER icerigini alalim... --> Modulus
    sinir = TLV.getIcerik(input,i);
    byte[] n_bytes = new byte[sinir[1]-sinir[0]+1];
    System.arraycopy(input,sinir[0],n_bytes,0,n_bytes.length);
    BigInteger n = new BigInteger(n_bytes);
    i = sinir[1]+1;
    //INTEGER icerigini alalim... --> public expoenent e
    sinir = TLV.getIcerik(input,i);
    byte[] e_bytes = new byte[sinir[1]-sinir[0]+1];
    System.arraycopy(input,sinir[0],e_bytes,0,e_bytes.length);
    BigInteger e = new BigInteger(e_bytes);

    return new GnuRSAPublicKey(n,e);

  }



  public PrivateKey decodePrivateKey(byte[] input)
  {
    //SEQUENCE olmali...
    if(input[0] != 0x30)
      throw new IllegalArgumentException("DER decode error");
    int[] sinir = TLV.getIcerik(input,0);
    if(sinir[1] != input.length-1)
      throw new IllegalArgumentException("DER decode error");
    //basta version 0 olmali...
    if(input[sinir[0]] != (byte)0x02 ||
       input[sinir[0]+1] != (byte)0x01 ||
       input[sinir[0]+2] != (byte)0x00)
      throw new IllegalArgumentException("Version error");
    //sonra RSA OId olmali...
    /*
         int i;
    int j = 0;
    for(i=sinir[0]+3;j<RSA_OIDarray.length;i++)
      if(input[i] != RSA_OIDarray[j++])
        throw new IllegalArgumentException("Object ID not found");
     */
    int i = findRSAOID(input,sinir[0]+3);

    //i anahtarin bulundugu OCTETSTRING yapisina ulasti...
    if(input[i] != 0x04)
      throw new IllegalArgumentException("DER decode error");

    //OCTETSTRING icerigini alalim...
    sinir = TLV.getIcerik(input,i);

    //OCTETSTRING icinde bir SEQUENCE yapisi olmali...
    i = sinir[0];
    if(input[i] != 0x30)
      throw new IllegalArgumentException("DER decode error");

    //SEQUENCE icerigini alallim.
    sinir = TLV.getIcerik(input,i);

    //Ilk eleman version olmali... yani 0
    i = sinir[0];
    if(input[i] != (byte)0x02 ||
       input[i+1] != (byte)0x01 ||
       input[i+2] != (byte)0x00)
      throw new IllegalArgumentException("Version error");
    i +=3 ;

    //Ikinci eleman modulus n olmali
    BigInteger n = TLV.decodeINT(input,i,sinir);
    //System.out.println("n    ="+gnu.crypto.util.Util.toString(n.toByteArray()));

    //Ucuncu eleman public exponent e olmali
    BigInteger e = TLV.decodeINT(input,sinir[1]+1,sinir);
    //System.out.println("e    ="+gnu.crypto.util.Util.toString(e.toByteArray()));

    //Dorduncu eleman private exponent d olmali
    BigInteger d = TLV.decodeINT(input,sinir[1]+1,sinir);
    //System.out.println("d    ="+gnu.crypto.util.Util.toString(d.toByteArray()));

    //Besinci eleman prime p olmali
    BigInteger p = TLV.decodeINT(input,sinir[1]+1,sinir);
    //System.out.println("p    ="+gnu.crypto.util.Util.toString(p.toByteArray()));

    //Altinci eleman prime q olmali
    BigInteger q = TLV.decodeINT(input,sinir[1]+1,sinir);
    //System.out.println("q    ="+gnu.crypto.util.Util.toString(q.toByteArray()));

    //Yedinci eleman exponent1 dp olmali
    BigInteger dp = TLV.decodeINT(input,sinir[1]+1,sinir);
    //System.out.println("dp   ="+gnu.crypto.util.Util.toString(dp.toByteArray()));

    //Sekizinci eleman exponent2 dq olmali
    BigInteger dq = TLV.decodeINT(input,sinir[1]+1,sinir);
    //System.out.println("dq   ="+gnu.crypto.util.Util.toString(dq.toByteArray()));

    //Dokuzuncu eleman coefficient coef olmali
    BigInteger coef = TLV.decodeINT(input,sinir[1]+1,sinir);
    //System.out.println("coef ="+gnu.crypto.util.Util.toString(coef.toByteArray()));


    return new GnuRSAPrivateKey(p,q,e,d);
  }
}