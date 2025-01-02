package tr.gov.tubitak.uekae.esya.api.infra.directory;


import java.net.URI;

/**
 * <p>Title: MA3 Dizin islemleri</p>
 * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
 * classlar bu pakette bulunmaktadir. dizinbilgileri interface'i de dizine
 * baglanmak icin gerekli bilgileri sunacak fonksiyonlari icerir.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 *
 */

public interface DirectoryInfo
{

     //The URI of the directory server to be connected. The port should be also added
     //examples ==> ldap://deneme.net:389 or ldaps://deneme.net:636 or ldap://10.0.2.1:389

     public URI getURI();

     /**
      * Baglanilacak dizinin IP'si. Genel olarak bu interface'deki tum fonksiyonlar su
      * sorunun cevabidir: "Simdi dizine baglanacagim. Dizine baglanmam icin gerekli
      * bilgiler neler?" Baglanilmasi gereken dogru dizin hakkindaki bilgileri bulup
      * buna gore dogru degerleri donmek interface'i implement eden classlarin
      * gorevidir.
      * @return Dizin IP adresi.
      */
     @Deprecated
     public String getIP();


     /**
      * Dizinin dinledigi port numarasi.
      * @return Dizinin dinledigi port numarasi.
      * @see #getIP()
      *
      */
     @Deprecated
     public int getPort();


     /**
      * Dizine baglanacak kullanicinin adi.
      * @return Kullanici adi
      * @see #getIP()
      *
      */
     public String getUserName();


     /**
      * Dizine baglanacak kullacinin parolasi
      * @return Dizin parolasi
      * @see #getIP()
      *
      */
     public String getUserPassword();


     /**
      * Dizin tipi. Tanimli dizin tipleri DizinBase classi icinde sabitlerde
      * bulunabilir.
      * @return Dizin tipini ifade eden string. Bu string tanimli bir dizin
      * tipi olmalidir. Tanimli dizin tipleri icin DizinBase classina bakiniz.
      * @see #getIP()
      *
      */
     public String getType();
}