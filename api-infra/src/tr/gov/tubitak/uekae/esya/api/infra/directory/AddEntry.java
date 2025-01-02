package tr.gov.tubitak.uekae.esya.api.infra.directory;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;
import javax.naming.directory.SchemaViolationException;


/**
 * <p>Title: MA3 Dizin islemleri</p>
 * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
 * classlar bu pakette bulunmaktadir. Bu class yeni bir entry eklemek icin
 * kullanilacaktir. Daha ozellesmis DizinKullaniciEkleme classina da bakiniz.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @see AddUser
 * @see AddUser
 * @see AddOrg
 * @see AddOrgUnit
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class AddEntry
    extends DirectoryBase
{

     protected Attributes mAttrs = null;

     /**
      * Baglantinin saglandigi constructor.
      * @param aBB Baglanti bilgilerinin alinacagi yer
      */
     public AddEntry(DirectoryInfo aBB)
     {
          super(aBB);
          mAttrs = new BasicAttributes(true);
     }


    /**
      * Yeni bir entry eklemek icin attributelar belirlenmeli. Attributelar bu
      * fonksiyon ile belirlenir. Aldigi ilk arguman attribute adi, ikinci de
      * bu attribute'a eklenecek degerdir. Ayni attribute'a birden fazla deger
      * eklenecekse, bu fonksiyon birden fazla kere ayni attribute ismi ve farkli
      * degerlerle cagrilabilir.
      * @param aAttrAdi Attribute adi. String olmalidir. Ikinci bir secenek de bu
      * argumanin String array olarak gonderilmesidir. Bu durumda Bu array ile ayni
      * uzunlukta bir object arrayi ikinci arguman olarak verilmelidir. Iki arrayin
      * elemanlari sira ile birbirine eslenir ve her esleme (String,Object eslemesi
      * olacaktir) yeni bir deger olarak eklenir.
      * @param aAttrDegeri Attribute'a gore degisebilir. String veya byte[] olabilir.
      * Ayrica Object[] olmasi durumunda nasil davranacagina yukaridan bakabilirsiniz.
      * @return bir hata cikmazsa true doner. Herhangi bir hata cikarsa false doner.
      * Eger arguman olarak iki array gonderilmisse, ilk hata cikan yerde false
      * doner. Kac attribute'un dogru olarak aktarildigi, kacinin aktarilmadigi
      * bilinemez.
      */
     public boolean addAttribute(Object aAttrAdi, Object aAttrDegeri)
     {
          if ( (aAttrAdi instanceof String) &&
              ( ( (aAttrDegeri instanceof String)) || (aAttrDegeri instanceof byte[]))
              )
          {
               //attrs icinde bu isimde bir attribute var mi diye bakalim
               Attribute temp = mAttrs.get( (String) aAttrAdi);
               if (temp != null)
               { //varsa
                    return temp.add(aAttrDegeri);
               }
               else
               { //yoksa
                    mAttrs.put(normalizeAttrName( (String) aAttrAdi), aAttrDegeri);
                    return true;
               }
          }
          else if ( (aAttrAdi instanceof String[]) && (aAttrDegeri instanceof Object[]))
          {
               String [] attrs = (String[]) aAttrAdi; 
               if ( attrs.length != ( (Object[]) aAttrDegeri).length)
               {
                    return false;
               }
               for (int i = 0; i < attrs.length; i++)
               {
                    if (!addAttribute( attrs[i], ( (Object[]) aAttrDegeri)[i])) //NOPMD
                    {
                         return false;
                    }
               }
          }
          return true;
     }


     // @todo comment yaz...
     protected boolean _kontrolluAttributeEkle (String aAttrAdi, Object aAttrDegeri)
     {
          if (mAttrs.get(aAttrAdi) == null)
          {
               return addAttribute(aAttrAdi, aAttrDegeri);
          }
          return true;
     }


     /**
      * Attribute'lar belirlendikten sonra bu fonksiyon cagrilarak entry dizinde
      * olusturulur. Eger yazarken bir problem cikarsa daha onceden eklenmis
      * attribute'lar degismeden korunacaktir. Problem giderildikten sonra bu
      * fonksiyon tekrar cagrilarak dizine yazma islemi gerceklestirilebilir. Eger
      * dizine yazilirsa daha onceki tum attribute'lar silinecektir.
      * @param aTKA Entry'nin yazilacagi yer
      * @return Bir hata cikarsa false, aksi takdirde true doner.
      */
     public boolean write(String aTKA)
     {
          try
          {
               mConnection.createSubcontext(aTKA, mAttrs);
          } catch (NameAlreadyBoundException ex)
          {
               _hataBildir(ex, GenelDil.EKLERKEN_AYNI_TKA);
               return false;
          } catch (InvalidAttributesException ex)
          {
               _hataBildir(ex, GenelDil.EKLERKEN_EKSIK_ATTRIBUTE); //Eksik attribute var
               return false;
          } catch (SchemaViolationException ex)
          {
               _hataBildir(ex, GenelDil.SCHEMA_HATASI); //schema'ya ayki bir is yapilmaya calisiliyor
               return false;
          } catch (InvalidAttributeValueException ex)
          {
               _hataBildir(ex, GenelDil.ATTRIBUTEA_VERILMEK_ISTENEN_DEGER_HATALI); //attributea verilmek istenen deger attributeun schema tanimiyla celisiyor.
               return false;
          } catch (NameNotFoundException ex)
          {
               _hataBildir(ex, GenelDil.EKLENECEK_NOKTA_BULUNAMADI); //Yeni entry'nin eklenecegi nokta bulunamadi
               return false;
          } catch (NamingException ex)
          {
               _hataBildir(ex, GenelDil.EKLERKEN_BILINMEYEN_HATA); //bilinmeyen bir hata
               return false;
          } catch (NullPointerException ex)
          {
               _hataBildir(ex, GenelDil.LDAPA_BAGLANTI_BULUNAMADI); //LDAP'a baglanti bulunamadi
               return false;
          }

          mAttrs = new BasicAttributes(true);
          return true;
     }


     public static void main (String[] args)
     {

          SampleInterface oi = new SampleInterface();
          AddEntry de = new AddEntry(oi);

          /*
              String[] xx = {"cn","sn","objectClass","givenName","uid"};
              Object[] yy = new Object[5];
              yy[0]="sdasdSDFsadf";
              yy[1]="soran";
              yy[2]="inetorgperson";
              yy[3]="hebele";
              yy[4]="emrah";
              //yy[2]=new String[] {"inetorgperson","organizationalPerson","person","top"};
              de.AttributeEkle(xx,yy);
              de.dizineYaz("cn=emrah,cn=hihigrup,ou=hoho,c=TR");
           */

          /*
              Attributes xyx;
                  try {
                    xyx = de.baglanti.getAttributes("cn=ahbirzenginolsam,ou=serdarousiliniz,o=serdarldaptestsiliniz,c=tr,dc=asya,dc=net");
                    System.out.println("\n\n\n\n\nsize = "+xyx.size());
                    System.out.println("is case ignored = "+xyx.isCaseIgnored());
                    System.out.println(" sn = "+xyx.get("sn"));
                    System.out.println(" hepsi:\n "+xyx);
                  }
                  catch (NamingException ex) {
                    ex.printStackTrace();
              }
           */

          //String[] xx = {"cn","objectClass","instanceType","objectCategory","objectSid","sAMAccountName"};
          String[] xx =
              {"objectClass", "instanceType", "sAMAccountName"};
          Object[] yy = new Object[3];
          yy[0] = "user";
          yy[1] = "4";
          yy[2] = "emra2@asya.net";
          de.addAttribute(xx, yy);
          de.write(
              "cn=tirmik,ou=ddd,ou=serdarousiliniz,o=serdarldaptestsiliniz,c=tr,dc=asya,dc=net");

          /*
              gerekirse burayi acabiliriz...
              Attributes x;
              try {
                x = de.baglanti.getAttributes("uid=gsd,cn=hihigrup,ou=hoho,c=TR");
                System.out.println("\n\n\n\n\nsize = "+x.size());
                System.out.println("is case ignored = "+x.isCaseIgnored());
                System.out.println(" sn = "+x.get("sn"));
                System.out.println(" hepsi:\n "+x);
              }
              catch (NamingException ex) {
                ex.printStackTrace();
              }
           */
     }
}
