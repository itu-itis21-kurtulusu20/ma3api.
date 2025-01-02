package tr.gov.tubitak.uekae.esya.api.infra.certstore.db;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ESingleResponse;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.*;

import java.util.HashMap;
import java.util.List;

public interface DepoVEN
{
    /**
     * ORTAK
     */
	
	void sorguCalistir(String aSorgu) throws CertStoreException;
	
	
	/**
     * DIZIN API leri
     */
    DepoDizin dizinOku(Long aDizinNo) throws CertStoreException, NotFoundException;
    void dizinYaz(DepoDizin aDizin) throws CertStoreException;
    DepoDizin dizinBul(String aDizinAdi) throws CertStoreException;
    void dizinAdiDegistir(Long aDizinNo,String aYeniIsim) throws CertStoreException;
    ItemSource<DepoDizin> dizinListele() throws CertStoreException;
    void dizinSil(Long aDizinNo) throws CertStoreException;

    /**
     * SIL API leri
     */
    DepoSIL sILOku(long aSILNo) throws NotFoundException, CertStoreException;
    void sILYaz(DepoSIL aSIL,List<DepoOzet> aOzetler,Long aDizinNo) throws CertStoreException;
    ItemSource<DepoSIL> sILListele(String aSorgu,Object[] aParams) throws CertStoreException;
    ItemSource<DepoSIL> sILListele() throws CertStoreException;
    ItemSource<DepoDizin> sILDizinleriniListele(long aSILNo) throws CertStoreException;
    int dizindenSILSil(long aSILNo,long aDizinNo) throws CertStoreException;
    void sILTasi(long aSILNo,long aEskiDizinNo,long aYeniDizinNo) throws CertStoreException;
    int sILSil(long aSILNo) throws CertStoreException;
    
    /**
     * KOKSERTIFIKA API leri
     */
    int kokSertifikaSil(long aKokNo) throws CertStoreException;
    void kokSertifikaYaz(DepoKokSertifika aKok,List<DepoOzet> aOzetler) throws CertStoreException;
    ItemSource<DepoKokSertifika> kokSertifikaListele() throws CertStoreException;
    ItemSource<DepoKokSertifika> kokSertifikaListele(String aSorgu,Object[] aParams) throws CertStoreException;
    DepoKokSertifika kokSertifikaHasheGoreBul(byte[] aHash) throws NotFoundException,CertStoreException;
    DepoKokSertifika kokSertifikaValueYaGoreBul(byte[] aValue) throws NotFoundException,CertStoreException;

    /**
     * SILINECEKKOKSERTIFIKA API leri
     */
    void silinecekKokSertifikaYaz(DepoSilinecekKokSertifika aKok) throws CertStoreException;
    ItemSource<DepoSilinecekKokSertifika> silinecekKokSertifikaListele() throws CertStoreException;
    
    /**
     * SISTEMPARAMETRELERI API leri
     */
    HashMap<String,Object> sistemParametreleriniOku() throws CertStoreException;
    DepoSistemParametresi sistemParametresiOku(String aParamAdi) throws NotFoundException,CertStoreException;
    void sistemParametresiUpdate(String aParamAdi,Object aParamDeger) throws CertStoreException;
    
    /**
     * SERTIFIKA API leri
     */
    DepoSertifika sertifikaOku(long aSertifikaNo) throws NotFoundException,CertStoreException;
    ItemSource<DepoSertifika> sertifikaListele() throws CertStoreException;
    ItemSource<DepoSertifika> sertifikaListele(String aSorgu,Object[] aParams) throws CertStoreException;
    ItemSource<DepoDizin> sertifikaDizinleriniListele(long aSertifikaNo) throws CertStoreException;
    int sertifikaSil(long aSertifikaNo) throws CertStoreException;
    int dizindenSertifikaSil(long aSertifikaNo,long aDizinNo) throws CertStoreException;
    void sertifikaTasi(long aSertifikaNo,long aEskiDizinNo,long aYeniDizinNo)throws CertStoreException;
    ItemSource<DepoSertifika> ozelAnahtarliSertifikalariListele() throws CertStoreException;
    void sertifikaYaz(DepoSertifika aSertifika,boolean aYeniNesne) throws CertStoreException;
    void sertifikaYaz(DepoSertifika aSertifika,List<DepoOzet> aOzetler,Long aDizinNo) throws CertStoreException;
    /**
     * NITELIK SERTIFIKASI API leri
     */
    ItemSource<DepoNitelikSertifikasi> nitelikSertifikasiListele(String aSorgu,Object[] aParams) throws CertStoreException;
    int nitelikSertifikasiSil(long aNitelikSertifikaNo) throws CertStoreException;
    void attributeAndPKICertYaz(DepoSertifika aSertifika, List<DepoOzet> aOzetler,List<DepoNitelikSertifikasi> aNitelikSertifikalari, Long aDizinNo)
	throws CertStoreException;
    /**
     * OCSP API leri
     */
    DepoOCSP ocspCevabiOku(long aOCSPNo) throws NotFoundException,CertStoreException;
    int ocspCevabiSil(long aOCSPNo) throws CertStoreException;
    void ocspCevabiYaz(DepoOCSP aOCSPCevabi,List<DepoOzet> aOzetler) throws CertStoreException;
    void ocspCevabiVeSertifikaYaz(DepoOCSP aOCSP, List<DepoOzet> aOCSPOzetler, DepoSertifika aSertifika, List<DepoOzet> aSertifikaOzetler, ESingleResponse dSertifikaOcsps) throws CertStoreException;
    ItemSource<DepoOCSP> ocspCevabiListele(String aSorgu,Object[] aParams) throws CertStoreException;
    ItemSource<DepoOzet> ocspOzetleriniListele(Long aOCSPNo) throws CertStoreException;
    
}
