using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.infra.directory
{
    public class StaticDirectoryInfo : DirectoryInfo
    {
        private readonly long mDizinNo = 0;
        private readonly String mDizinIP, mDizinTipi, mDizinCalismaNoktasi, mDizinKullaniciAdi, mDizinKullaniciParola;
        private readonly int mDizinPort, mDizinMod;
        private readonly bool mDizindeMailIleAramaYap = false,
        mDizindeRdnTekillestir = false, mVarsayilan = false;

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
                  long aDizinNo,
                  String aDizinIP, int aDizinPort, int aDizinMod,
                  String aDizinTipi, String aDizinCalismaNoktasi,
                  bool aDizindeMailIleAramaYap,
                  bool aDizindeRdnTekillestir,
                  bool aVarsayilan,
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

        public long getDizinNo()  //NOPMD
        {
            return mDizinNo;
        }

        public String getIP()  //NOPMD
        {
            return mDizinIP;
        }

        public int getPort()  //NOPMD
        {
            return mDizinPort;
        }

        public int getDizinMod()  //NOPMD
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

        public String getDizinCalismaNoktasi()  //NOPMD
        {
            return mDizinCalismaNoktasi;
        }

        public bool isDizindeMailIleAramaYap()  //NOPMD
        {
            return mDizindeMailIleAramaYap;
        }

        public bool isDizindeRdnTekillestir()  //NOPMD
        {
            return mDizindeRdnTekillestir;
        }

        public bool isVarsayilan()  //NOPMD
        {
            return mVarsayilan;
        }

    }
}
