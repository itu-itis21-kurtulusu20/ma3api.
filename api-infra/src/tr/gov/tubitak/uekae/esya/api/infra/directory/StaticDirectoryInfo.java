package tr.gov.tubitak.uekae.esya.api.infra.directory;


import java.net.URI;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class StaticDirectoryInfo
    implements DirectoryInfo
{

     private long mDizinNo = 0;
     private String mDizinIP, mDizinTipi, mDizinCalismaNoktasi, mDizinKullaniciAdi, mDizinKullaniciParola;
     private int mDizinPort, mDizinMod;
     private URI mURI;
     private boolean mDizindeMailIleAramaYap = false,
                     mDizindeRdnTekillestir = false,
                     mVarsayilan = false;

    @Deprecated
    public StaticDirectoryInfo(
               String aDizinIP, int aDizinPort, String aDizinTipi,
               String aDizinKullaniciAdi, String aDizinKullaniciParola)
     {
          this.mDizinIP = aDizinIP;
          this.mDizinPort = aDizinPort;
          this.mDizinTipi = aDizinTipi;
          this.mDizinKullaniciAdi = aDizinKullaniciAdi;
          this.mDizinKullaniciParola = aDizinKullaniciParola;

          this.mDizinMod = -1;
     }

    public StaticDirectoryInfo(
            URI aURI, String aDizinTipi,
            String aDizinKullaniciAdi, String aDizinKullaniciParola)
    {
        this.mURI = aURI;
        this.mDizinTipi = aDizinTipi;
        this.mDizinKullaniciAdi = aDizinKullaniciAdi;
        this.mDizinKullaniciParola = aDizinKullaniciParola;

        this.mDizinMod = -1;
    }

    @Deprecated
    public StaticDirectoryInfo(
               long aDizinNo,
               String aDizinIP, int aDizinPort, int aDizinMod, 
               String aDizinTipi,String aDizinCalismaNoktasi, 
               boolean aDizindeMailIleAramaYap, 
               boolean aDizindeRdnTekillestir,
               boolean aVarsayilan,
               String aDizinKullaniciAdi,
               String aDizinKullaniciParola
               )
     {
          this.mDizinNo = aDizinNo;
          this.mDizinIP = aDizinIP;
          this.mDizinPort = aDizinPort;
          this.mDizinMod = aDizinMod;
          this.mDizinTipi = aDizinTipi;
          this.mDizinCalismaNoktasi = aDizinCalismaNoktasi;
          this.mDizindeMailIleAramaYap = aDizindeMailIleAramaYap;
          this.mDizindeRdnTekillestir = aDizindeRdnTekillestir;
          this.mVarsayilan = aVarsayilan;
          this.mDizinKullaniciAdi = aDizinKullaniciAdi;
          this.mDizinKullaniciParola = aDizinKullaniciParola;

     }

    public StaticDirectoryInfo(
            long aDizinNo,
            URI aURI, int aDizinMod,
            String aDizinTipi,String aDizinCalismaNoktasi,
            boolean aDizindeMailIleAramaYap,
            boolean aDizindeRdnTekillestir,
            boolean aVarsayilan,
            String aDizinKullaniciAdi,
            String aDizinKullaniciParola
    )
    {
        this.mDizinNo = aDizinNo;
        this.mURI = aURI;
        this.mDizinMod = aDizinMod;
        this.mDizinTipi = aDizinTipi;
        this.mDizinCalismaNoktasi = aDizinCalismaNoktasi;
        this.mDizindeMailIleAramaYap = aDizindeMailIleAramaYap;
        this.mDizindeRdnTekillestir = aDizindeRdnTekillestir;
        this.mVarsayilan = aVarsayilan;
        this.mDizinKullaniciAdi = aDizinKullaniciAdi;
        this.mDizinKullaniciParola = aDizinKullaniciParola;

    }

    public long getDizinNo ()  //NOPMD
     {
          return mDizinNo;
     }

    public URI getURI() {
        return mURI;
    }

    @Deprecated
    public String getIP()  //NOPMD
     {
          return mDizinIP;
     }

    @Deprecated
    public int getPort()  //NOPMD
     {
          return mDizinPort;
     }

     public int getDizinMod ()  //NOPMD
     {
          return mDizinMod;
     }

     public String getUserName()  //NOPMD
     {
          return mDizinKullaniciAdi;
     }

     public String getUserPassword()  //NOPMD
     {
          return mDizinKullaniciParola;
     }

     public String getType()  //NOPMD
     {
          return mDizinTipi;
     }

     public String getDizinCalismaNoktasi ()  //NOPMD
     {
          return mDizinCalismaNoktasi;
     }

     public boolean isDizindeMailIleAramaYap()  //NOPMD
     {
          return mDizindeMailIleAramaYap;
     }
     
     public boolean isDizindeRdnTekillestir ()  //NOPMD
     {
          return mDizindeRdnTekillestir;
     }
     
     public boolean isVarsayilan()  //NOPMD
     {
          return mVarsayilan;
     }

}