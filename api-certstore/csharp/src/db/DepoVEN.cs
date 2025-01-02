using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db
{
    public interface DepoVEN
    {
        /**
         * ORTAK
         */
        void sorguCalistir(String aSorgu);
        
        /**
         * DIZIN API leri
         */
        DepoDizin dizinOku(long? aDizinNo);
        void dizinYaz(DepoDizin aDizin);
        DepoDizin dizinBul(String aDizinAdi);
        void dizinAdiDegistir(long aDizinNo, String aYeniIsim);
        ItemSource<DepoDizin> dizinListele();
        void dizinSil(long aDizinNo);

        /**
         * SIL API leri
         */
        DepoSIL sILOku(long aSILNo);
        void sILYaz(DepoSIL aSIL, List<DepoOzet> aOzetler, long? aDizinNo);
        ItemSource<DepoSIL> sILListele(String aSorgu, Object[] aParams);
        ItemSource<DepoSIL> sILListele();
        ItemSource<DepoDizin> sILDizinleriniListele(long aSILNo);
        int dizindenSILSil(long? aSILNo, long? aDizinNo);
        void sILTasi(long aSILNo, long aEskiDizinNo, long aYeniDizinNo);
        int sILSil(long? aSILNo);

        /**
         * KOKSERTIFIKA API leri
         */
        int kokSertifikaSil(long? aKokNo);
        void kokSertifikaYaz(DepoKokSertifika aKok, List<DepoOzet> aOzetler);
        ItemSource<DepoKokSertifika> kokSertifikaListele();
        ItemSource<DepoKokSertifika> kokSertifikaListele(String aSorgu, Object[] aParams);
        DepoKokSertifika kokSertifikaHasheGoreBul(byte[] aHash);
        DepoKokSertifika kokSertifikaValueYaGoreBul(byte[] aValue);
        /**
         * SILINECEKKOKSERTIFIKA API leri
         */
        void silinecekKokSertifikaYaz(DepoSilinecekKokSertifika aKok);
        ItemSource<DepoSilinecekKokSertifika> silinecekKokSertifikaListele();

        /**
         * SISTEMPARAMETRELERI API leri
         */
        Dictionary<String, Object> sistemParametreleriniOku();
        DepoSistemParametresi sistemParametresiOku(String aParamAdi);
        void sistemParametresiUpdate(String aParamAdi, Object aParamDeger);

        /**
         * SERTIFIKA API leri
         */
        DepoSertifika sertifikaOku(long? aSertifikaNo);
        ItemSource<DepoSertifika> sertifikaListele();
        ItemSource<DepoSertifika> sertifikaListele(String aSorgu, Object[] aParams);
        ItemSource<DepoDizin> sertifikaDizinleriniListele(long aSertifikaNo);
        int sertifikaSil(long? aSertifikaNo);
        int dizindenSertifikaSil(long? aSertifikaNo, long? aDizinNo);
        void sertifikaTasi(long aSertifikaNo, long aEskiDizinNo, long aYeniDizinNo);
        ItemSource<DepoSertifika> ozelAnahtarliSertifikalariListele();
        void sertifikaYaz(DepoSertifika aSertifika, bool aYeniNesne);
        void sertifikaYaz(DepoSertifika aSertifika, List<DepoOzet> aOzetler, long? aDizinNo);

        /**
         * NITELIK SERTIFIKASI API leri
        */
        ItemSource<DepoNitelikSertifikasi> nitelikSertifikasiListele(String aSorgu, Object[] aParams);
        int nitelikSertifikasiSil(long? aNitelikSertifikaNo);

        void attributeAndPKICertYaz(DepoSertifika aSertifika, List<DepoOzet> aOzetler,
                                    List<DepoNitelikSertifikasi> aNitelikSertifikalari, long? aDizinNo);	    

        /**
         * OCSP API leri
         */
        DepoOCSP ocspCevabiOku(long aOCSPNo);
        int ocspCevabiSil(long aOCSPNo);
        void ocspCevabiYaz(DepoOCSP aOCSPCevabi, List<DepoOzet> aOzetler);

        void ocspCevabiVeSertifikaYaz(DepoOCSP aOCSP, List<DepoOzet> aOCSPOzetler, DepoSertifika aSertifika,
                                      List<DepoOzet> aSertifikaOzetler, ESingleResponse dSertifikaOcsps);
        ItemSource<DepoOCSP> ocspCevabiListele(String aSorgu, Object[] aParams);
        ItemSource<DepoOzet> ocspOzetleriniListele(long aOCSPNo);
    }
}
