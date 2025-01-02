package tr.gov.tubitak.uekae.esya.api.infra.directory;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;

/**
 * <p>Title: MA3 Dizin islemleri</p>
 * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
 * classlar bu pakette bulunmaktadir. Bu class yeni bir kullanici ekler. Dizin tipine
 * gore degisen attribute'larla ilgilenir ve duzenlemeleri yapar.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class AddUser
    extends AddEntry
{

     /**
      * Baglantinin saglandigi constructor.
      * @param aBB Baglanti bilgilerinin alinacagi yer
      */
     public AddUser(DirectoryInfo aBB)
     {
          super(aBB);
     }


    /**
      * Yeni bir kullanicinin dizinde olusturulmasi icin cagrilir. Eger standart
      * bir kullanici olusturulacak bu fonksiyonun argumanlari ile cagrilmasi
      * yeterlidir. Fakat extra attribute'lar set edilmek isteniyorsa, bu fonksiyon
      * cagrilmadan once, DizinEntryEkleme class'ina ait AttributeEkle fonksiyonu
      * kullanilarak extra attribute'lar eklenebilir. DizinEntryEkleme class'indan
      * farkli olarak bu classda dizineYaz yeni olusturulacak kisinin tam TKA'sini
      * degil, olusturulacagi yerin TKA'sini almakta vebasina cn=XXX eklemektedir.
      * @param aTKA Entry'nin olusturulacagi yer
      * @param aCN Kullanicinin adi. TKA'nin basina cn=XXX seklinde eklenecektir.
      * @param aSN Kullanicinin soyadi
      * @param aEmail Kullanicinin email adresi. Daha once bu email adresine sahip
      * bir kullanici varsa basarisiz olacaktir.
      * @return Eger basarili bir sekilde kullanici eklenirse true, bassarili
      * olmazsa false doner.
      */
     public boolean writeToDirectory(String aTKA, String aCN, String aSN, String aEmail)
     {
          //Eklenmesi zorunlu olan, dizine gore degiskenlik gosteren  ve daha once
          //eklenmemis attribute'lari ekle.
          if (mDizinTipi.equals(ACTIVE_DIRECTORY))
          {
               _kontrolluAttributeEkle(ATTR_OBJECTCLASS, "user");
               _kontrolluAttributeEkle(ATTR_CN, aCN);
               _kontrolluAttributeEkle(ATTR_SN, aSN);
               _kontrolluAttributeEkle(ATTR_INSTANCETYPE, "4");
               _kontrolluAttributeEkle(ATTR_SAMACCOUNTNAME, aEmail);
               _kontrolluAttributeEkle(ATTR_MAIL, aEmail);
          }
          else if (mDizinTipi.equals(NETSCAPE))
          {
               _kontrolluAttributeEkle(ATTR_CN, aCN);
               _kontrolluAttributeEkle(ATTR_SN, aSN);
               _kontrolluAttributeEkle(ATTR_OBJECTCLASS, "inetorgperson");
               _kontrolluAttributeEkle(ATTR_GIVENNAME, aCN);
               _kontrolluAttributeEkle(ATTR_MAIL, aEmail);
          }
          else if (mDizinTipi.equals(CRITICAL_PATH))
          {
               _kontrolluAttributeEkle(ATTR_CN, aCN);
               _kontrolluAttributeEkle(ATTR_SN, aSN);
               _kontrolluAttributeEkle(ATTR_OBJECTCLASS, "top");
               addAttribute(ATTR_OBJECTCLASS, "person");
               addAttribute(ATTR_OBJECTCLASS, "organizationalPerson");
               addAttribute(ATTR_OBJECTCLASS, "inetOrgPerson");
               _kontrolluAttributeEkle(ATTR_MAIL, aEmail);
          }

          //Entry'yi olustur
          return super.write("CN=" + aCN + "," + aTKA);
     }


     public boolean write(String aTKA)
     {
          _hataBildirCiddi(GenelDil.CALISTIRILMAMASI_GEREKEN_FONKSIYON);
          return false;
     }


     public static void main (String[] args)
     {
          AddUser da = new AddUser(new StaticDirectoryInfo(
              "192.168.0.128",
              389,
              DirectoryBase.ACTIVE_DIRECTORY,
              "GIRESUN/Administrator",
              "123456"
              )
              );

          System.out.println("Baglanti :" + da.isConnected());

          System.out.println("Yazdim mi :" +
                             da.writeToDirectory("dc=asya,dc=net", "ali", "soran", "ali@asya.net"));

     }
}