package tr.gov.tubitak.uekae.esya.api.infra.directory;

import javax.naming.ContextNotEmptyException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;

/**
 * <p>Title: MA3 Dizin islemleri</p>
 * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
 * classlar bu pakette bulunmaktadir. Bu class verilen entry'yi dizinden
 * tamamen siler.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 *
 */

public class DeleteEntry
    extends DirectoryBase
{

     /**
      * Baglantinin saglandigi constructor.
      * @param aBB Baglanti bilgilerinin alinacagi yer
      *
      */
     public DeleteEntry(DirectoryInfo aBB)
     {
          super(aBB);
     }


    /**
      * Verilen TKA'li entry'yi dizinden tamamen siler. Hata olusursa olusan hataya gore
      * dogru mesaji kaydeder. Mesaji getMesaj ile alabilirsiniz.
      * @param aTKA Silinecek entry'nin TKS'si
      * @return Bir hata olusursa false, silme islemi basarili olursa true doner.
      *
      */
     public boolean deleteFromDirectory(String aTKA)
     {
          try
          {
               mConnection.unbind(aTKA);
          } catch (NameNotFoundException ex)
          {
               _hataBildir(ex, GenelDil.SILINECEK_TKA_HATALI);
               return false;
          } catch (ContextNotEmptyException ex)
          {
               _hataBildir(ex, GenelDil.SILINECEK_TKADA_ENTRY_VAR);
               return false;
          } catch (NamingException ex)
          {
               _hataBildir(ex, GenelDil.SILERKEN_BILINMEYEN_HATA);
               return false;
          } catch (NullPointerException ex)
          {
               _hataBildir(ex, GenelDil.LDAPA_BAGLANTI_BULUNAMADI); //LDAP'a baglanti bulunamadi
               return false;
          }

          return true;
     }


     public static void main (String[] args)
     {
          SampleInterface oi = new SampleInterface(DirectoryBase.ACTIVE_DIRECTORY);
          DeleteEntry des = new DeleteEntry(oi);

          des.deleteFromDirectory("ou=ddd,ou=serdarousiliniz,o=serdarldaptestsiliniz,c=tr,dc=asya,dc=net");

     }
}