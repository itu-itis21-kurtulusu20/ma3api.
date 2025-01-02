package tr.gov.tubitak.uekae.esya.api.infra.directory;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;


/**
 * <p>Title: MA3 Dizin islemleri</p>
 * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
 * classlar bu pakette bulunmaktadir. Bu class dizine yeni bir CDP
 * entrysi eklemek icin kullanilir.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class AddCDPAIA
    extends AddEntry
{

     public AddCDPAIA(DirectoryInfo aBB)
     {
          super(aBB);
     }


    public boolean writeCDP(String aTKA, String aCN, byte[] aCrl)
     {

          //dizine bagli olmayan attribute'lari ekle.
          _kontrolluAttributeEkle(ATTR_CN, aCN);
          _kontrolluAttributeEkle(ATTR_CRL, aCrl);

          //Eklenmesi zorunlu olan, dizine gore degiskenlik gosteren  ve daha once
          //eklenmemis attribute'lari ekle.
          _kontrolluAttributeEkle(ATTR_OBJECTCLASS, "cRLDistributionPoint");
          if (mDizinTipi.equals(ACTIVE_DIRECTORY))
          {
               _kontrolluAttributeEkle(ATTR_INSTANCETYPE, "4");
          }
          else if (mDizinTipi.equals(NETSCAPE))
          {    //NOPMD
               //do nothing
          }
          else if (mDizinTipi.equals(CRITICAL_PATH))
          {    //NOPMD
               //do nothing
          }
          else
          {
               _hataBildirCiddi(GenelDil.LDAP_TIP_HATALI);
               return false;
          }

          //Entry'yi olustur
          return super.write("cn=" + aCN + "," + aTKA);
     }


     public boolean writeAIA(String aTKA, String aCN, byte[] aCer)
     {

          //dizine bagli olmayan attribute'lari ekle.
          _kontrolluAttributeEkle(ATTR_CN, aCN);
          _kontrolluAttributeEkle(ATTR_SMSERTIFIKASI, aCer);

          //Eklenmesi zorunlu olan, dizine gore degiskenlik gosteren  ve daha once
          //eklenmemis attribute'lari ekle.
          if (mDizinTipi.equals(ACTIVE_DIRECTORY))
          {
               _kontrolluAttributeEkle(ATTR_OBJECTCLASS, "certificationAuthority");
               _kontrolluAttributeEkle(ATTR_INSTANCETYPE, "4");
               _kontrolluAttributeEkle(ATTR_ARL, new byte[]
                                      {0});
               _kontrolluAttributeEkle(ATTR_CRL, new byte[]
                                      {0});
          }
          else
          {
               _hataBildirCiddi(GenelDil.LDAP_TIP_HATALI);
               return false;
          }

          //Entry'yi olustur
          return super.write("cn=" + aCN + "," + aTKA);
     }


     public boolean write(String aTKA)
     {
          _hataBildirCiddi(GenelDil.CALISTIRILMAMASI_GEREKEN_FONKSIYON);
          return false;
     }
     
     public static void main(String[] args)
     {
          StaticDirectoryInfo x= new StaticDirectoryInfo("20.1.5.1",3060,NETSCAPE,"cn=_esyaadmin,cn=Users,dc=eturkiye,dc=net","123456");
          
          AddCDPAIA cdp = new AddCDPAIA(x);
          
          System.out.println("Baglanti:"+cdp.isConnected());
          
          System.out.println(cdp.writeCDP("o=SIL,o=ARSIV,c=TR","dene1",new byte[]{1,2,3,4}));
          System.out.println(cdp.getMessage());
     }
}