using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Reflection;
using System.Text;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.util;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.ops
{
    //todo Annotation!
    //@ApiClass
    public class CertStoreOCSPOps
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly CertStore mCertStore;

        public CertStoreOCSPOps(CertStore aDepo)
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.ORTAK);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
            mCertStore = aDepo;
        }

        public DepoOCSP readStoreOCSPResponse(long aOCSPNo)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            try
            {
                DepoOCSP ocspCevabi = ven.ocspCevabiOku(aOCSPNo);
                return ocspCevabi;
            }
            catch (ObjectNotFoundException aEx)
            {
                throw new CertStoreException(aOCSPNo + " nolu ocsp cevabi veritabaninda bulunamadi.", aEx);
            }
            catch (Exception aEx)
            {
                throw new CertStoreException(aOCSPNo + " nolu ocsp cevabi okunurken VT hatasi olustu.", aEx);
            }
        }


        public void writeOCSPResponseAndCertificate(EOCSPResponse aResponse, ECertificate aCertificate)
        {
            EBasicOCSPResponse basicResponse = aResponse.getBasicOCSPResponse();
            ESingleResponse singleResponse = CertStoreUtil.getOCSPResponseForCertificate(basicResponse, aCertificate);
            if (singleResponse == null)
            {
                throw new CertStoreException("OCSPCevabi sertifikayi icermiyor.");
            }

            DepoSertifika dSertifika = CertStoreUtil.eSYASertifikaTODepoSertifika(aCertificate);

            DepoOCSPToWrite dOCSP = CertStoreUtil.asnOCSPResponseTODepoOCSP(aResponse);

            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    List<DepoOzet> basicOcspOzetler = CertStoreUtil.convertToDepoOzet(dOCSP.getBasicOCSPResponse(), OzneTipi.OCSP_BASIC_RESPONSE);
                    List<DepoOzet> ocspOzetler = CertStoreUtil.convertToDepoOzet(dOCSP.getOCSPResponse(), OzneTipi.OCSP_RESPONSE);
                    List<DepoOzet> tumOcspOzetler = new List<DepoOzet>(basicOcspOzetler);
                    tumOcspOzetler.AddRange(ocspOzetler);
                    
                    List<DepoOzet> sertifikaOzetler = CertStoreUtil.convertToDepoOzet(dSertifika.getValue(), OzneTipi.SERTIFIKA);
                    
                    ven.ocspCevabiVeSertifikaYaz(dOCSP, tumOcspOzetler, dSertifika, sertifikaOzetler, singleResponse);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException("Ocsp cevabi ve sertifika veritabanina yazilirken hata olustu.", aEx);
                }
            }
        }

        public List<DepoOzet> listOCSPResponseHashes(long aOCSPNo)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            try
            {
                ItemSource<DepoOzet> depoOzetItemSource = ven.ocspOzetleriniListele(aOCSPNo);
                List<DepoOzet> ozetler = ((RsItemSource<DepoOzet>) depoOzetItemSource).toList();
                return ozetler;
            }
            catch (Exception aEx)
            {
                throw new CertStoreException(aOCSPNo + " nolu ocsp cevabinin ozetleri okunurken VT hatasi olustu.", aEx);
            }
        }

        public ItemSource<DepoOCSP> listStoreOCSPResponses(OCSPSearchTemplate aSablon)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn()); 
            try
            {
                Pair<String, List<Object>> ikili = _createQuery(aSablon);
                String sorgu = ikili.first();
                List<Object> params_ = ikili.second();
                ItemSource<DepoOCSP> ocspItemSource = ven.ocspCevabiListele(sorgu, params_.ToArray());
                return ocspItemSource;
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Sablona gore OCSP cevabi arama sirasinda VT hatasi olustu.", aEx);
            }
        }

        public EBasicOCSPResponse listOCSPResponses(OCSPSearchTemplate aSablon)
        {
            ItemSource<DepoOCSP> depoOCSPItemSource = listStoreOCSPResponses(aSablon);
            if (depoOCSPItemSource == null)
                return null;
            DepoOCSP depoOCSP = depoOCSPItemSource.nextItem();
            depoOCSPItemSource.close();
            if (depoOCSP == null)
                return null;
            try
            {
                return new EBasicOCSPResponse(depoOCSP.getBasicOCSPResponse());
            }
            catch (Exception aEx)
            {
                throw new CertStoreException(
                    depoOCSP.getOCSPNo() + " nolu ocsp asn basicocspresponse tipine cevrilirken hata olustu.", aEx);
            }
        }

        public int deleteOCSPResponse(long aOCSPNo)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(mCertStore.getConn());
            using (DbTransaction transaction = mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    if (aOCSPNo <= 0)
                    {
                        throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
                    }
                    int sonuc = ven.ocspCevabiSil(aOCSPNo);
                    transaction.Commit();
                    return sonuc;
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(aOCSPNo + " nolu ocsp cevabi silinirken VT hatasi olustu. " +
                                            "Yapilan VT islemleri geri alindi.", aEx);
                }
            }
        }

        private Pair<String, List<Object>> _createQuery(OCSPSearchTemplate aSablon)
        {
            StringBuilder sb = new StringBuilder("");
            List<Object> params_ = new List<Object>();
            sb.Append("1=1");

            byte[] hash = aSablon.getHash();
            OzetTipi hashtype = aSablon.getHashType();
            
            if (hash != null)
            {
                sb.Append(" AND " + OCSPHelper.COLUMN_OCSP_NO + " IN (SELECT " + HashHelper.COLUMN_OBJECT_NO + " FROM " +
                        HashHelper.OZET_TABLO_ADI + " WHERE ( (" + HashHelper.COLUMN_OBJECT_TYPE + " = ? OR " +
                        HashHelper.COLUMN_OBJECT_TYPE + " = ? ) AND " + HashHelper.COLUMN_HASH_VALUE + " = ? ");
                params_.Add(OzneTipi.OCSP_RESPONSE.getIntValue());
                params_.Add(OzneTipi.OCSP_BASIC_RESPONSE.getIntValue());
                params_.Add(hash);
                
                if (hashtype != null)
                {
                    sb.Append(" AND " + HashHelper.COLUMN_HASH_TYPE + " = ? ))");
                    params_.Add(hashtype.getIntValue());
                }
                else
                {
                    sb.Append(" ))");
                }

            }
            else
            {
                if (hashtype != null)
                {
                    sb.Append(" AND " + OCSPHelper.COLUMN_OCSP_NO + " IN (SELECT " + HashHelper.COLUMN_OBJECT_NO + " FROM " +
                            HashHelper.OZET_TABLO_ADI + " WHERE ( (" + HashHelper.COLUMN_OBJECT_TYPE + " = ? OR " +
                            HashHelper.COLUMN_OBJECT_TYPE + " = ? ) AND " + HashHelper.COLUMN_HASH_TYPE + " = ? ))");
                    params_.Add(OzneTipi.OCSP_RESPONSE.getIntValue());
                    params_.Add(OzneTipi.OCSP_BASIC_RESPONSE.getIntValue());
                    params_.Add(hashtype.getIntValue());
                }
            }


            byte[] respID = aSablon.getOCSPResponderID();
            if (respID != null)
            {
                sb.Append(" AND " + OCSPHelper.COLUMN_OCSP_RESP_ID + " = ? ");
                params_.Add(respID);
            }

            byte[] value = aSablon.getOCSPValue();
            if (value != null)
            {
                sb.Append(" AND " + OCSPHelper.COLUMN_OCSP_VALUE + " = ? ");
                params_.Add(value);
            }
            
            DateTime? producedAtAfter = aSablon.getProducedAtAfter();
            if (producedAtAfter != null)
            {
                sb.Append(" AND " + OCSPHelper.COLUMN_OCSP_NO + " IN (SELECT " + CertificateOCSPsHelper.COLUMN_OCSP_NO + " FROM " +
                    CertificateOCSPsHelper.SERTIFIKA_OCSPS_TABLO_ADI + " WHERE " + CertificateOCSPsHelper.COLUMN_THIS_UPDATE + " >= ? ORDER BY " +
                    CertificateOCSPsHelper.COLUMN_THIS_UPDATE + " )");
                params_.Add(producedAtAfter);
            }

            DateTime? producedAt = aSablon.getProducedAt();
            if (producedAt != null)
            {
                sb.Append(" AND " + OCSPHelper.COLUMN_OCSP_NO + " = (SELECT " + CertificateOCSPsHelper.COLUMN_OCSP_NO + " FROM " +
                    CertificateOCSPsHelper.SERTIFIKA_OCSPS_TABLO_ADI + " WHERE " + CertificateOCSPsHelper.COLUMN_THIS_UPDATE + " = ? ORDER BY " +
                    CertificateOCSPsHelper.COLUMN_THIS_UPDATE + " )");
                params_.Add(producedAt);
            }

            DateTime? producedAtBefore = aSablon.getProducedAtBefore();
            if (producedAtBefore != null)
            {
                sb.Append(" AND " + OCSPHelper.COLUMN_OCSP_NO + " IN (SELECT " + CertificateOCSPsHelper.COLUMN_OCSP_NO + " FROM " +
                    CertificateOCSPsHelper.SERTIFIKA_OCSPS_TABLO_ADI + " WHERE " + CertificateOCSPsHelper.COLUMN_THIS_UPDATE + " <= ? ORDER BY " +
                    CertificateOCSPsHelper.COLUMN_THIS_UPDATE + " )");
                params_.Add(producedAtBefore);
            }
            byte[] serial = aSablon.getCertSerialNumber();
            if (serial != null)
            {
                sb.Append(" AND " + OCSPHelper.COLUMN_OCSP_NO + " IN (SELECT " + CertificateOCSPsHelper.COLUMN_OCSP_NO + " FROM " +
                    CertificateOCSPsHelper.SERTIFIKA_OCSPS_TABLO_ADI + " WHERE " + CertificateHelper.COLUMN_SERTIFIKA_NO + " IN (SELECT " +
                    CertificateHelper.COLUMN_SERTIFIKA_NO + " FROM " + CertificateHelper.SERTIFIKA_TABLO_ADI + " WHERE ( " +
                    CertificateHelper.COLUMN_SERTIFIKA_SERIAL + " = ? )))");
                params_.Add(serial);

            }

            return new Pair<String, List<Object>>(sb.ToString(), params_);
        }

    }
}
