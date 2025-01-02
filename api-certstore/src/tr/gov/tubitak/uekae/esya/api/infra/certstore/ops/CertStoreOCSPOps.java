package tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.ESingleResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVEN;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVTKatmani;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.JDBCUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.NotFoundException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.*;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOCSP;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOCSPToWrite;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOzet;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.OCSPSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.util.RsItemSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CertStoreOCSPOps {

    private static Logger logger = LoggerFactory.getLogger(CertStoreOCSPOps.class);

    private final CertStore mCertStore;

    public CertStoreOCSPOps(final CertStore aDepo) {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE ex) {
            logger.error("Lisans kontrolu basarisiz.");
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
        }
        mCertStore = aDepo;
    }

    public DepoOCSP readStoreOCSPResponse(long aOCSPNo)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            DepoOCSP ocspCevabi = ven.ocspCevabiOku(aOCSPNo);
            return ocspCevabi;
        } catch (NotFoundException aEx) {
            throw new CertStoreException(aOCSPNo + " nolu ocsp cevabi veritabaninda bulunamadi.", aEx);
        } catch (CertStoreException aEx) {
            throw new CertStoreException(aOCSPNo + " nolu ocsp cevabi okunurken VT hatasi olustu.", aEx);
        } finally {
            try {
                if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException aEx) {
                throw new CertStoreException(aOCSPNo + " nolu ocsp cevabi okunurken VT hatasi olustu.", aEx);
            }
        }
    }


    public void writeOCSPResponseAndCertificate(EOCSPResponse aResponse, ECertificate aCertificate)
            throws CertStoreException
    {
        EBasicOCSPResponse basicResponse = aResponse.getBasicOCSPResponse();

        ESingleResponse signleResponse = CertStoreUtil.getOCSPResponseForCertificate(basicResponse, aCertificate);
        if (signleResponse == null) {
            throw new CertStoreException("OCSPCevabi sertifikayi icermiyor.");
        }

        DepoSertifika dSertifika = CertStoreUtil.eSYASertifikaTODepoSertifika(aCertificate);

        DepoOCSPToWrite dOCSP = CertStoreUtil.asnOCSPResponseTODepoOCSP(aResponse);

        try {
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());

            List<DepoOzet> basicOcspOzetler = CertStoreUtil.convertToDepoOzet(dOCSP.getBasicOCSPResponse(), OzneTipi.OCSP_BASIC_RESPONSE);
            List<DepoOzet> ocspOzetler = CertStoreUtil.convertToDepoOzet(dOCSP.getOCSPResponse(), OzneTipi.OCSP_RESPONSE);
            List<DepoOzet> tumOcspOzetler = new ArrayList<DepoOzet>(basicOcspOzetler);
            tumOcspOzetler.addAll(ocspOzetler);

            List<DepoOzet> sertifikaOzetler = CertStoreUtil.convertToDepoOzet(dSertifika.getValue(), OzneTipi.SERTIFIKA);

            ven.ocspCevabiVeSertifikaYaz(dOCSP, tumOcspOzetler, dSertifika, sertifikaOzetler, signleResponse);
            JDBCUtil.commit(mCertStore.getConn());
        } catch (CertStoreException aEx) {
            if (mCertStore.getConn() != null)
                JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException("Ocsp cevabi ve sertifika veritabanina yazilirken hata olustu.", aEx);
        }

    }

    public List<DepoOzet> listOCSPResponseHashes(long aOCSPNo)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            ItemSource<DepoOzet> depoOzetItemSource = ven.ocspOzetleriniListele(aOCSPNo);
            List<DepoOzet> ozetler = ((RsItemSource)depoOzetItemSource).toList();
            return ozetler;
        } catch (Exception aEx) {
            throw new CertStoreException(aOCSPNo + " nolu ocsp cevabinin ozetleri okunurken VT hatasi olustu.", aEx);
        } finally {
            try {
                if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException aEx) {
                throw new CertStoreException(aOCSPNo + " nolu ocsp cevabinin ozetleri okunurken VT hatasi olustu.", aEx);
            }
        }
    }

    public ItemSource<DepoOCSP> listStoreOCSPResponses(OCSPSearchTemplate aSablon)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            Pair<String, List<Object>> ikili = _createQuery(aSablon);
            String sorgu = ikili.first();
            List<Object> params = ikili.second();
            ItemSource<DepoOCSP> ocspItemSource = ven.ocspCevabiListele(sorgu, params.toArray());
            return ocspItemSource;
        } catch (CertStoreException aEx) {
            throw new CertStoreException("Sablona gore OCSP cevabi arama sirasinda VT hatasi olustu.", aEx);
        } finally {
            try {
                if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
                //JDBCUtil.close(oturum);
            } catch (CertStoreException aEx) {
                throw new CertStoreException("Sablona gore OCSP cevabi arama sirasinda VT hatasi olustu.", aEx);
            }
        }
    }

    public EBasicOCSPResponse listOCSPResponses(OCSPSearchTemplate aSablon)
            throws CertStoreException {
        ItemSource<DepoOCSP> depoOCSPItemSource = listStoreOCSPResponses(aSablon);
        if(depoOCSPItemSource == null)
            return null;
        DepoOCSP depoOCSP = null;
        try {
            depoOCSP = depoOCSPItemSource.nextItem();
            depoOCSPItemSource.close();
            if(depoOCSP == null)
                return null;
            return new EBasicOCSPResponse(depoOCSP.getBasicOCSPResponse());
        } catch (Exception aEx) {
            throw new CertStoreException(depoOCSP.getOCSPNo() + " nolu ocsp asn basicOcspResponse tipine cevrilirken hata olustu.", aEx);
        }

    }


    public int deleteOCSPResponse(long aOCSPNo)
            throws CertStoreException {
        //Connection oturum = null;
        try {
            if (aOCSPNo <= 0) {
                throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
            }
            //oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
            DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
            int sonuc = ven.ocspCevabiSil(aOCSPNo);
            JDBCUtil.commit(mCertStore.getConn());
            return sonuc;
        } catch (CertStoreException aEx) {
            if (mCertStore.getConn() != null) JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException(aOCSPNo + " nolu ocsp cevabi silinirken VT hatasi olustu." +
                    "Yapilan VT islemleri geri alindi.", aEx);
        } /*finally {
            JDBCUtil.close(oturum);
        }*/
    }

    private Pair<String, List<Object>> _createQuery(OCSPSearchTemplate aSablon) {
        StringBuffer sb = new StringBuffer("");
        List<Object> params = new ArrayList<Object>();
        sb.append("1=1");

        byte[] hash = aSablon.getHash();
        OzetTipi hashtype = aSablon.getHashType();
        if (hash != null) {
            sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_NO + " IN (SELECT " + OzetYardimci.COLUMN_OBJECT_NO + " FROM " +
                    OzetYardimci.OZET_TABLO_ADI + " WHERE ( (" + OzetYardimci.COLUMN_OBJECT_TYPE + " = ? OR " +
                    OzetYardimci.COLUMN_OBJECT_TYPE + " = ? ) AND " + OzetYardimci.COLUMN_HASH_VALUE + " = ? ");
            params.add(OzneTipi.OCSP_RESPONSE.getIntValue());
            params.add(OzneTipi.OCSP_BASIC_RESPONSE.getIntValue());
            params.add(hash);

            if (hashtype != null) {
                sb.append(" AND " + OzetYardimci.COLUMN_HASH_TYPE + " = ? ))");
                params.add(hashtype.getIntValue());
            } else {
                sb.append(" ))");
            }
        } else {
            if (hashtype != null) {
                sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_NO + " IN (SELECT " + OzetYardimci.COLUMN_OBJECT_NO + " FROM " +
                        OzetYardimci.OZET_TABLO_ADI + " WHERE ( (" + OzetYardimci.COLUMN_OBJECT_TYPE + " = ? OR " +
                        OzetYardimci.COLUMN_OBJECT_TYPE + " = ? ) AND " + OzetYardimci.COLUMN_HASH_TYPE + " = ? ))");
                params.add(OzneTipi.OCSP_RESPONSE.getIntValue());
                params.add(OzneTipi.OCSP_BASIC_RESPONSE.getIntValue());
                params.add(hashtype.getIntValue());
            }
        }

        byte[] respID = aSablon.getOCSPResponderID();
        if (respID != null) {
            sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_RESP_ID + " = ? ");
            params.add(respID);
        }

        byte[] value = aSablon.getOCSPValue();
        if (value != null) {
            sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_VALUE + " = ? ");
            params.add(value);
        }

        Date producedAtAfter = aSablon.getProducedAtAfter();
        if (producedAtAfter != null) {
            //sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_PRODUCED_AT + " >= ? ");
            sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_NO + " IN (SELECT " + SertifikaOCSPsYardimci.COLUMN_OCSP_NO + " FROM " +
                    SertifikaOCSPsYardimci.SERTIFIKA_OCSPS_TABLO_ADI + " WHERE " + SertifikaOCSPsYardimci.COLUMN_THIS_UPDATE + " >= ? ORDER BY " +
                    SertifikaOCSPsYardimci.COLUMN_THIS_UPDATE + " )");
            params.add(producedAtAfter);
        }

        Date producedAt = aSablon.getProducedAt();
        if (producedAt != null) {
            //sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_PRODUCED_AT + " >= ? ");
            sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_NO + " = (SELECT " + SertifikaOCSPsYardimci.COLUMN_OCSP_NO + " FROM " +
                    SertifikaOCSPsYardimci.SERTIFIKA_OCSPS_TABLO_ADI + " WHERE " + SertifikaOCSPsYardimci.COLUMN_THIS_UPDATE + " = ? ORDER BY " +
                    SertifikaOCSPsYardimci.COLUMN_THIS_UPDATE + " )");
            params.add(producedAt);
        }


        Date producedAtBefore = aSablon.getProducedAtBefore();
        if (producedAtBefore != null) {
            //sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_PRODUCED_AT + " <= ? ");
            sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_NO + " IN (SELECT " + SertifikaOCSPsYardimci.COLUMN_OCSP_NO + " FROM " +
                    SertifikaOCSPsYardimci.SERTIFIKA_OCSPS_TABLO_ADI + " WHERE " + SertifikaOCSPsYardimci.COLUMN_THIS_UPDATE + " <= ? ORDER BY " +
                    SertifikaOCSPsYardimci.COLUMN_THIS_UPDATE + " )");
            params.add(producedAtBefore);
        }

        byte[] serial = aSablon.getCertSerialNumber();
        if (serial != null) {
            /*sb.append(" AND "+ OCSPYardimci.COLUMN_OCSP_NO + " IN (SELECT "+ SertifikaOCSPsYardimci.COLUMN_OCSP_NO+" FROM "+
					SertifikaOCSPsYardimci.SERTIFIKA_OCSPS_TABLO_ADI+" WHERE ( "+SertifikaOCSPsYardimci.COLUMN_SERTIFIKA_NO+ " = ? )) " );*/
            sb.append(" AND " + OCSPYardimci.COLUMN_OCSP_NO + " IN (SELECT " + SertifikaOCSPsYardimci.COLUMN_OCSP_NO + " FROM " +
                    SertifikaOCSPsYardimci.SERTIFIKA_OCSPS_TABLO_ADI + " WHERE " + SertifikaYardimci.COLUMN_SERTIFIKA_NO + " IN (SELECT " +
                    SertifikaYardimci.COLUMN_SERTIFIKA_NO + " FROM " + SertifikaYardimci.SERTIFIKA_TABLO_ADI + " WHERE ( " +
                    SertifikaYardimci.COLUMN_SERTIFIKA_SERIAL + " = ? )))");
            params.add(serial);

        }
        return new Pair<String, List<Object>>(sb.toString(), params);
    }

}
