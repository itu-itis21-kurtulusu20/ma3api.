package tr.gov.tubitak.uekae.esya.api.infra.directory;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;

/**
 * <p>Title: MA3 Dizin islemleri</p>
 * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
 * classlar bu pakette bulunmaktadir. Bu class dizine yeni bir organizationalUnit
 * entrysi eklemek icin kullanilir.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class AddOrgUnit
    extends AddEntry
{

     public AddOrgUnit(DirectoryInfo aBB)
     {
          super(aBB);
     }


     public boolean writeToDirectory(String aTKA, String aOU, byte[] aCaCertificate, byte[] aCrl)
     {

          //dizine bagli olmayan attribute'lari ekle.
          _kontrolluAttributeEkle(ATTR_OU, aOU);
          _kontrolluAttributeEkle(ATTR_SMSERTIFIKASI, aCaCertificate);
          _kontrolluAttributeEkle(ATTR_CRL, aCrl);

          //Eklenmesi zorunlu olan, dizine gore degiskenlik gosteren  ve daha once
          //eklenmemis attribute'lari ekle.
          if (mDizinTipi.equals(ACTIVE_DIRECTORY))
          {
               _kontrolluAttributeEkle(ATTR_OBJECTCLASS, "organizationalUnit");
               _kontrolluAttributeEkle(ATTR_INSTANCETYPE, "4");
          }
          else if (mDizinTipi.equals(NETSCAPE))
          {
               addAttribute(ATTR_OBJECTCLASS, "top");
               addAttribute(ATTR_OBJECTCLASS, "organizationalunit");
               addAttribute(ATTR_OBJECTCLASS, "extensibleObject");
          }
          else if (mDizinTipi.equals(CRITICAL_PATH))
          {
               addAttribute(ATTR_OBJECTCLASS, "top");
               addAttribute(ATTR_OBJECTCLASS, "organizationalUnit");
          }

          //Entry'yi olustur
          return super.write("ou=" + aOU + "," + aTKA);
     }


     public boolean write(String aTKA)
     {
          _hataBildirCiddi(GenelDil.CALISTIRILMAMASI_GEREKEN_FONKSIYON);
          return false;
     }
}