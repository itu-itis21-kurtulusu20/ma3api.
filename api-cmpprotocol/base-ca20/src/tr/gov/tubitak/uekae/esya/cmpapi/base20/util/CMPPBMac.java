package tr.gov.tubitak.uekae.esya.cmpapi.base20.util;

import gnu.crypto.mac.IMac;
import gnu.crypto.mac.MacFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPBMParameter;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.asn.cmp.PBMParameter;
import tr.gov.tubitak.uekae.esya.asn.cmp._cmpValues;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>Title: ESYA</p>
 * <p>Description: </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 * it is used to make password base signing
 */

public class CMPPBMac
{
     public static final int MAXSALTLEN = 20;
     public static final int MAXITERATIONLEN = 100;

     private static final Logger LOGCU = LoggerFactory.getLogger(CMPPBMac.class);

     private byte[] mSifre;
     private IMac mMac = MacFactory.getInstance(MacFactory.HMAC_NAME_PREFIX+MacFactory.SHA1_HASH);
     private PBMParameter mParams;

     /**
      * Verilen parola ilekullanilacak, default parametre degerleri ile bir
      * Password based mac objesi olusturur. Salt olarak rastgele bir sayi secer.
      * iteration count olarak da mumkun oldugunca buyuk bir sayi secer.
      * Ozet algoritmasi Sha1 olarak, mac algoritmasi da hmac-sha1 olarak
      * sabittir.
      * @param aPassword Kullanilacak parola.
      * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException init sirasinda cikabilecek exception
      */
     public CMPPBMac(String aPassword)
         throws CryptoException
     {
          byte[] salt = RandomUtil.generateRandom(MAXSALTLEN-1);
          PBMParameter params = new PBMParameter(salt,
                                                 new AlgorithmIdentifier(DigestAlg.SHA1.getOID()),
                                                 MAXITERATIONLEN*2/3,
                                                 new AlgorithmIdentifier(_cmpValues.id_HMAC_SHA1)
                                                 );
          _init(new EPBMParameter(params), aPassword);
     }

     /**
      * Parametreler verilmis ise, bu parametreleri kullanarak bir obje olusturur.
      * Mac algoritmasi hmac-sha1 olmalidir.
      * @param aParam Parametreleri iceren asn1 objesi
      * @param aPassword Kullanilacak parola
      * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException init sirasinda cikabilecek exception
      */
     public CMPPBMac(EPBMParameter aParam,String aPassword)
         throws CryptoException
     {
          _init(aParam,aPassword);
     }

     private void _init(EPBMParameter aParam,String aPassword)
         throws CryptoException
     {
          byte[] salt;
          DigestAlg ozet;
          int iterationCount;

          mParams = aParam.getObject();

          salt = mParams.salt.value;
          if(salt.length > MAXSALTLEN)
               throw new CryptoException("Salt cok uzun : "+salt.length);
          ozet = DigestAlg.fromOID(mParams.owf.algorithm.value);
          iterationCount = (int)mParams.iterationCount.value;
          if(iterationCount > MAXITERATIONLEN)
               throw new CryptoException("Iteration count cok uzun : "+iterationCount);
          if(!Arrays.equals(mParams.mac.algorithm.value,MACAlg.HMAC_SHA1.getOID()))
               throw new CryptoException("Mac algoritmasi bilinmiyor  "+mParams.mac.algorithm);

          _passwordBul(salt,iterationCount,ozet,aPassword);
     }


     private void _passwordBul(byte[] aSalt,int aIterationCount, DigestAlg aOzet,String aPassword)
         throws CryptoException
     {
          byte[] sifre;
          byte[] temp;
          try
          {
               sifre = aPassword.getBytes("US-ASCII");
          } catch (UnsupportedEncodingException ex)
          {
               LOGCU.error("Buraya hic gelmemeli. Charset desteklenmiyor.");
               throw new CryptoException("Buraya hic gelmemeli. Charset desteklenmiyor.");
          }

          //the salt value is appended to the shared secret input
          temp = new byte[sifre.length+aSalt.length];
          System.arraycopy(sifre,0,temp,0,sifre.length);
          System.arraycopy(aSalt,0,temp,sifre.length,aSalt.length);
          sifre = temp;

          // The OWF is then applied iterationCount times, where the
          // salted secret is the input to the first iteration and, for each
          // successive iteration, the input is set to be the output of the
          // previous iteration
          for(int i=0;i<aIterationCount;i++)
          {
               temp = DigestUtil.digest(aOzet, sifre);
               sifre = temp;
          }

          // The output of the final iteration (called
          // "BASEKEY" for ease of reference, with a size of "H") is what is used
          // to form the symmetric key. If the MAC algorithm requires a K-bit key
          // and K <= H, then the most significant K bits of BASEKEY are used. If
          // K > H, then all of BASEKEY is used for the most significant H bits of
          // the key, OWF("1" || BASEKEY) is used for the next most significant H
          // bits of the key, OWF("2" || BASEKEY) is used for the next most
          // significant H bits of the key, and so on, until all K bits have been
          // derived. [Here "N" is the ASCII byte encoding the number N and "||"
          // represents concatenation.]

          int K = mMac.macSize(); //Since hmac is used
          if(K == sifre.length)
               temp = sifre;
          else if (K < sifre.length)
          {
               temp = new byte[K];
               System.arraycopy(sifre,0,temp,0,temp.length);
          }
          else
          {
               LOGCU.error("Bu durum henuz implement edilmedi.");
               throw new CryptoException("Bu durum henuz implement edilmedi.");
          }

          mSifre = temp;

          try
          {
               mMac = MacFactory.getInstance(MacFactory.HMAC_NAME_PREFIX + MacFactory.SHA1_HASH);
               HashMap attr = new HashMap();
               attr.put(IMac.MAC_KEY_MATERIAL, mSifre);
               mMac.init(attr);
          } catch (Exception ex)
          {
               LOGCU.error("Buraya hic gelmemeli. Mac init edilemedi.");
               throw new CryptoException("Buraya hic gelmemeli. Mac init edilemedi.");
          }

     }

     /**
      * PBMac sonucunu hesaplar.
      * @param aVeri Maclenmesi istenen veri.
      * @return PBMac sonucu
      */
     public byte[] pbmacHesapla(byte[] aVeri)
     {
          mMac.reset();
          mMac.update(aVeri,0,aVeri.length);
          return mMac.digest();
     }

     /**
      * Verilen verinin PBMac sonucunun aSonuc olup olmadigini kontrol eder.
      * Imza kontrolunun mac versiyonu.
      * @param aVeri Maclenen veri.
      * @param aSonuc PBMac sonucu oldugu iddia edilen sonuc
      * @return Eger aSonuc gercekten PBMac sonucuysa true, aksi takdirde false
      */
     public boolean pbmacKontrol(byte[] aVeri,byte[] aSonuc)
     {
          byte[] benimSonuc = pbmacHesapla(aVeri);
          return UtilEsitlikler.esitMi(benimSonuc,aSonuc);
     }


     /**
      * Bu instance'da kullanilan parametreleri doner. Ozellikle
      * PBMParameter almayan constructor kullanildiginda faydalidir. Buradan
      * parametreleri alip asn yapiniz icine koyabilirsiniz.
      * @return Kullanilan PBMParameter.
      */
     public PBMParameter getMParams ()
     {
          return mParams;
     }

}