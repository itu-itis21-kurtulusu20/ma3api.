package tr.gov.tubitak.uekae.esya.api.infra.directory;

import java.util.Vector;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.naming.directory.Attribute;
import javax.naming.directory.AttributeInUseException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.NoSuchAttributeException;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;

/**
 * <p>Title: MA3 Dizin islemleri </p>
 * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
 * classlar bu pakette bulunmaktadir. Bu class dizindeki bir entry'nin alanlari
 * uzerinde degisiklik yapar. </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 *
 */

public class Update
    extends DirectoryBase
{

     protected Vector<ModificationItem> mDegisiklikler = null;

     /**
      * Attribute'un eski degerlerine dokunmadan, bu attribute'a yeni bir deger eklemek
      * icin kullanilir.
      */
     public static final int DEGER_EKLE = DirContext.ADD_ATTRIBUTE;
     /**
      * Attribute'taki degerlerden, verilen degeri siler.
      */
     public static final int DEGER_CIKAR = DirContext.REMOVE_ATTRIBUTE;
     /**
      * Attribute'tun tum degerlerini siler ve yerine verilen degeri yazar.
      */
     public static final int DEGER_TUMDENDEGISIR = DirContext.REPLACE_ATTRIBUTE;

     /**
      * Baglantinin saglandigi constructor.
      * @param aBB Baglanti bilgilerinin alinacagi yer
      */
     public Update(DirectoryInfo aBB)
     {
          super(aBB);
          mDegisiklikler = new Vector<ModificationItem>();
     }


    /**
      * Yeni bir guncelleme ekler. Guncellemeler dizinde sirayla yapilacaktir.
      * Bazi oneriler:
      * <ul>
      *     <li>Eger tum degerleri silip, yeni degerler eklemek isteniyorsa, once
      *     DEGER_TUMDENDEGISTIR kullanilarak tek bir deger eklenir. Sonra, eklenecek
      *     degerler DEGER_EKLE ile eklenir.</li>
      *     <li>Eger tek bir degeri, yeni bir degerle degistirmek istenirse, once
      *     DEGER_CIKAR ile degistirilecek deger verilip, sonra da DEGER_EKLE
      *      ile yeni deger eklenir.</li>
      *     <li>Eger tum degerler silinmek isteniyorsa, DEGER_CIKAR ile birlikte
      *     eklenecek deger olarak null verilir.</li>
      * </ul>
      * <br>
      * Eger var olan bir deger eklenmek istenirse, olmayan bir deger
      * cikarilmak istenirse hata verecektir.
      * @param aYapilacakIs DizinGuncelle.DEGER_EKLE, DizinGuncelle.DEGER_CIKAR,
      * DizinGuncelle.DEGER_TUMDENDEGISTIR uclusunden biri olmalidir. Ne anlama
      * geldikleri icin bu sabitlere bakiniz.
      * @param aAttrAdi Degisikligin hangi attribute uzerinde yapilacagidir.
      * @param aDeger Degisiklik yapilirken kullanilacak deger.
      * @see #DEGER_EKLE
      * @see #DEGER_CIKAR
      * @see #DEGER_TUMDENDEGISIR
      *
      */
     public void guncellemeEkle (int aYapilacakIs, String aAttrAdi, Object aDeger)
     {
          ModificationItem m;
          Attribute attr = new BasicAttribute(
              normalizeAttrName(aAttrAdi),
              aDeger);
          
          
          if ((aYapilacakIs != DEGER_CIKAR) && (aYapilacakIs != DEGER_EKLE) &&
              (aYapilacakIs != DEGER_TUMDENDEGISIR))
          {
               _hataBildirCiddi(GenelDil.GUNCELLEME_IS_HATALI);
               aYapilacakIs = DEGER_EKLE;
          }
          //yapilacak guncellemeyi olustur.
          m = new ModificationItem(aYapilacakIs, attr);
          //Guncellemeyi listeye al.
          mDegisiklikler.add(m);
     }


     /**
      * Eklenen guncellemeleri dizinde de gerceklestirir. Eger eklenen
      * guncellemelerden birinde hata olusursa, hic biri dizine aktarilmaz.
      * Hata olmasi durumunda false doner, loga gerekli mesaji yazar, eklenen
      * guncellemeleri degistirmez. Dogru bir sekilde dizinde guncelleme yapilirsa,
      * true doner ve eklenen guncellemelerin hepsini siler. Boylece yeni
      * guncellemeler ekleyip yeniden dizindeGuncelle cagrilabilir.
      * @param aTKA Guncellemenin hangi TKA uzerinde yapilacagi
      * @return Hata cikmazsa true, cikarsa false doner. True donme durumunda
      * eklenen tum guncellemeler temizlenecektir.
      */
     public boolean dizindeGuncelle (String aTKA)
     {
          //elimizdeki vactor'u array'e cevir.
          int size = mDegisiklikler.size();
          aTKA = aTKA.replaceAll("/", "\\\\/");
          ModificationItem[] degisiklik = new ModificationItem[size];
          for (int i = 0; i < size; i++)
          {
               degisiklik[i] = (ModificationItem) mDegisiklikler.get(i);
          }

          try
          {
               mConnection.modifyAttributes(aTKA, degisiklik);
          } catch (AttributeInUseException ex)
          {
               _hataBildir(ex, GenelDil.GUNCELLERKEN_DEGER_EKLENEMIYOR); //eklenmek istenen deger eklenemiyor
               return false;
          } catch (NoSuchAttributeException ex)
          {
               _hataBildir(ex, GenelDil.GUNCELLERKEN_CIKACAK_DEGER_BULUNAMADI); //Cikarilmak istenen deger bulunamadi
               return false;
          } catch (NameNotFoundException ex)
          {
               _hataBildir(ex, GenelDil.GUNCELLENECEK_TKA_0_BULUNAMADI, new String[]
                          {aTKA}); //Guncellenmek istenen TKA "%s" bulunamadi!
               return false;
          } catch (NoPermissionException ex)
          {
               _hataBildir(ex, GenelDil._0_ISLEMI_ICIN_YETERLI_YETKINIZ_YOK,
                          new String[]
                          {msRB.getString(GenelDil.GUNCELLEME)}); //%s islemini gerceklestirmek icin yeterli yetkiye sahip degilsiniz!
               return false;
          } catch (NamingException ex)
          {
               _hataBildir(ex, GenelDil.GUNCELLEMEDE_HATA); //Guncelleme isleminde hata olustu
               return false;
          } catch (NullPointerException ex)
          {
               _hataBildir(ex, GenelDil.LDAPA_BAGLANTI_BULUNAMADI); //LDAP'a baglanti bulunamadi
               return false;
          } catch (Exception ex)
          {
               _hataBildir(ex, GenelDil.GUNCELLEMEDE_HATA); //Guncelleme isleminde hata olustu
               return false;
          }

          mDegisiklikler = new Vector<ModificationItem>();
          return true;
     }


//     public static void main (String[] args)
//     {
//
//          Ornekinterface oi = new Ornekinterface(NETSCAPE);
//          DizinGuncelle dg = new DizinGuncelle(oi);
//
//          //dg.guncellemeEkle(DEGER_EKLE,"o","kacamak");
//          //dg.guncellemeEkle(DEGER_EKLE,"o","serdar");
//          //dg.guncellemeEkle(DEGER_EKLE,"o","dme");
//          //dg.guncellemeEkle(DEGER_CIKAR,"o","serdar");
//          //dg.guncellemeEkle(DEGER_CIKAR,"o","kacamak");
//          //dg.guncellemeEkle(DEGER_CIKAR,"o","se");
//
//          byte[] bb =
//              {2, 4, 3, 4, 5, 6, 7, 8, 9};
//          dg.guncellemeEkle(DEGER_TUMDENDEGISIR, ATTR_CAPRAZSERTIFIKA, null);
//
//          System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
//                             dg.dizindeGuncelle("o=CaprazKSM1,c=tr")
//                             );
//
////    System.out.println(m);
//     }
//     
}