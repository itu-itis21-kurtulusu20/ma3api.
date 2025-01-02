using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Reflection;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.util;
using tr.gov.tubitak.uekae.esya.asn.depo;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.ops
{
    public class CertStoreRootCertificateOps
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly CertStore _mCertStore;

        public CertStoreRootCertificateOps(CertStore aDepo)
        {
            CertStoreUtil.CheckLicense();

            _mCertStore = aDepo;
        }

        public void deleteRootCertificate(long? aKokNo)
        {
            if (aKokNo <= 0)
            {
                throw new CertStoreException("Nesne ID leri 0 dan büyük olmak zorundadır");
            }
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mCertStore.getConn());
            using (DbTransaction transaction = _mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    ven.kokSertifikaSil(aKokNo);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException(aKokNo + " nolu kök sertifika silinirken VT hatası oluştu." +
                            "Yapılan VT işlemleri geri alındı.", aEx);
                }
            }
        }

        public byte[] createRootCertificatesTobeSigned(List<Certificate> aEklenecekList, List<CertificateType> aTipler, List<SecurityLevel> aSeviyeler, List<Certificate> aSilinecekList)
        {
            int eklenecekSize = 0;
            int tipSize = 0;
            int seviyeSize = 0;
            int silinecekSize = 0;

            if (aEklenecekList != null)
            {
                eklenecekSize = aEklenecekList.Count;
                tipSize = aTipler.Count;
                seviyeSize = aSeviyeler.Count;
            }

            if (aSilinecekList != null)
            {
                silinecekSize = aSilinecekList.Count;
            }

            if (!((eklenecekSize == tipSize) && (tipSize == seviyeSize)))
            {
                throw new CertStoreException("Eklenecek sertifikalar için liste boyutları aynı olmak zorundadır");
            }

            DepoASNKokSertifikalar sertifikalar = new DepoASNKokSertifikalar();
            sertifikalar.elements = new DepoASNKokSertifika[eklenecekSize + silinecekSize];
            int i = 0;
            for (; i < eklenecekSize; i++)
            {
                DepoASNKokSertifika sertifika = new DepoASNKokSertifika();
                DepoASNEklenecekKokSertifika eklenecek = CertStoreUtil.asnCertTOAsnEklenecek(aEklenecekList[i], aTipler[i], aSeviyeler[i]);
                sertifika.Set_eklenecekSertifika(eklenecek);
                sertifikalar.elements[i] = sertifika;
            }

            for (int j = 0; j < silinecekSize; i++, j++)
            {
                DepoASNKokSertifika sertifika = new DepoASNKokSertifika();
                DepoASNSilinecekKokSertifika silinecek = CertStoreUtil.asnCertTOAsnSilinecek(aSilinecekList[j]);
                sertifika.Set_silinecekSertifika(silinecek);
                sertifikalar.elements[i] = sertifika;
            }

            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
            sertifikalar.Encode(enc);
            return enc.MsgCopy;
        }


        public void addPersonalRootCertificate(Certificate aCert, CertificateType aTip)
        {
            DepoKokSertifika kok = CertStoreUtil.asnCertTODepoEklenecek(aCert);
            kok.setKokTipi(aTip);
            kok.setKokGuvenSeviyesi(SecurityLevel.PERSONAL);
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mCertStore.getConn());
            using (DbTransaction transaction = _mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    List<DepoOzet> ozetler = CertStoreUtil.convertToDepoOzet(kok.getValue(), OzneTipi.KOK_SERTIFIKA);
                    ven.kokSertifikaYaz(kok, ozetler);
                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    throw new CertStoreException("Kök Sertifika yazılırken VT hatası oluştu." +
                            "Yapılan VT işlemleri geri alındı.", aEx);
                }
            }
        }

        public bool verifySignature(DepoKokSertifika aKok)
        {
            bool sonuc = CertStoreUtil.verifyDepoKokSertifika(aKok);
            if (sonuc == false)
                deleteRootCertificate(aKok.getKokSertifikaNo());
            return sonuc;
        }

        public List<DepoKokSertifika> listStoreRootCertificates()
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mCertStore.getConn()); 
            try
            {
                List<DepoKokSertifika> list = ((RsItemSource<DepoKokSertifika>)ven.kokSertifikaListele()).toList();
                List<DepoKokSertifika> dogrulananlar = new List<DepoKokSertifika>();
                foreach (DepoKokSertifika kok in list)
                {
                    try
                    {
                        bool sonuc = CertStoreUtil.verifyDepoKokSertifika(kok);
                        if (sonuc == true)
                        {
                            dogrulananlar.Add(kok);
                        }
                        else
                        {
                            //todo: Metin Şimşek - bu işlemi neden burada yapıyoruz???
                            deleteRootCertificate(kok.getKokSertifikaNo());
                        }
                    }
                    catch (Exception aEx)
                    {
                        logger.Error("Kök Sertifika doğrulamada hata", aEx);
                    }
                }

                return dogrulananlar;
            }
            catch (DatabaseException aEx)
            {
                string hataMesaji = "Tüm Kök Sertifikalar listelenirken VT hatası oluştu.";
                logger.Error(hataMesaji, aEx);
                throw new CertStoreException(hataMesaji, aEx);
            }
        }



        public List<DepoKokSertifika> listStoreRootCertificates(CertificateSearchTemplate aSAS, CertificateType[] aTipler, SecurityLevel[] aSeviyeler)
        {

            DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mCertStore.getConn());

            try
            {
                Pair<String, List<Object>> ikili = _createQuery(aSAS, aTipler, aSeviyeler);
                String sorgu = ikili.first();
                List<Object> params_ = ikili.second();
                List<DepoKokSertifika> list = ((RsItemSource<DepoKokSertifika>)ven.kokSertifikaListele(sorgu, params_.ToArray())).toList();
                List<DepoKokSertifika> dogrulananlar = new List<DepoKokSertifika>();
                foreach (DepoKokSertifika kok in list)
                {
                    try
                    {
                        bool sonuc = CertStoreUtil.verifyDepoKokSertifika(kok);
                        if (sonuc == true)
                        {
                            dogrulananlar.Add(kok);
                        }
                        else
                        {
                            using (DbTransaction transaction = _mCertStore.getConn().BeginTransaction())
                            {
                                try
                                {
                                    ven.kokSertifikaSil(kok.getKokSertifikaNo());
                                    transaction.Commit();
                                }
                                catch (Exception aEx)
                                {
                                    transaction.Rollback();
                                    throw new CertStoreException("Kök Sertifika yazılırken VT hatası oluştu." +
                                            "Yapılan VT işlemleri geri alındı.", aEx);
                                }
                            }
                        }
                    }
                    catch (Exception aEx)
                    {
                        logger.Error("Kök sertifika doğrulamada hata", aEx);
                    }
                }

                return dogrulananlar;
            }
            catch (DatabaseException aEx)
            {
                throw new CertStoreException("Şablona göre Kök Sertifika listelenirken VT hatası oluştu.", aEx);
            }
        }



        public void addSignedRootCertificates(byte[] aImzaliKokSertifikalar)
        {
            DepoVEN ven = CertStoreDBLayer.newDepoVEN(_mCertStore.getConn());
            using (DbTransaction transaction = _mCertStore.getConn().BeginTransaction())
            {
                try
                {
                    Asn1DerDecodeBuffer dec = new Asn1DerDecodeBuffer(aImzaliKokSertifikalar);
                    DepoASNImzalar imzalar = new DepoASNImzalar();
                    imzalar.Decode(dec);

                    DepoASNImza[] imzaList = imzalar.elements;

                    foreach (DepoASNImza imza in imzaList)
                    {
                        byte[] asnkokb = null;
                        DepoASNKokSertifika asnkok = imza.imzalanan;

                        int tip = asnkok.ChoiceID;
                        try
                        {

                            DepoASNRawImza asnimza = imza.imza;

                            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
                            asnimza.Encode(enc);
                            byte[] depoimza = enc.MsgCopy;

                            Asn1DerEncodeBuffer enc2 = new Asn1DerEncodeBuffer();
                            asnkok.Encode(enc2);
                            asnkokb = enc2.MsgCopy;

                            bool dogru = false;
                            try
                            {
                                dogru = CertStoreUtil.verifyEncodedRootCertificate(asnkokb, asnimza);
                            }
                            catch (Exception aEx)
                            {
                                logger.Error("İmzalı dosyadaki kök sertifikalardan biri doğrulanamadı.", aEx);
                                continue;
                            }
                            if (!dogru)
                            {
                                try
                                {
                                    byte[] certValue = null;
                                    if (tip == DepoASNKokSertifika._EKLENECEKSERTIFIKA)
                                    {
                                        DepoASNEklenecekKokSertifika eklenecek = (DepoASNEklenecekKokSertifika)asnkok.GetElement();
                                        certValue = eklenecek.kokSertifikaValue.mValue;
                                    }
                                    else if (tip == DepoASNKokSertifika._SILINECEKSERTIFIKA)
                                    {
                                        DepoASNSilinecekKokSertifika silinecek = (DepoASNSilinecekKokSertifika)asnkok.GetElement();
                                        certValue = silinecek.kokSertifikaValue.mValue;
                                    }

                                    ECertificate cert = new ECertificate(certValue);

                                    String serino = cert.getSerialNumberHex();
                                    String subject = cert.getSubject().stringValue();
                                    logger.Error("Imzali Dosyadan " + serino + " seri nolu " + subject + " subjectli kok sertifika dogrulanamadi.");
                                    continue;
                                }
                                catch (Exception aEx)
                                {
                                    logger.Error("İmzalı dosyadaki kök sertifikalardan biri doğrulanamadı. Sertifika bilgileri alınamıyor", aEx);
                                    continue;
                                }
                            }

                            if (tip == DepoASNKokSertifika._EKLENECEKSERTIFIKA)
                            {
                                DepoASNEklenecekKokSertifika eklenecek = (DepoASNEklenecekKokSertifika)asnkok.GetElement();
                                DepoKokSertifika depokok = CertStoreUtil.asnEklenecekTODepoKok(eklenecek);
                                depokok.setSatirImzasi(depoimza);
                                List<DepoOzet> ozetler = CertStoreUtil.convertToDepoOzet(depokok.getValue(), OzneTipi.KOK_SERTIFIKA);
                                ven.kokSertifikaYaz(depokok, ozetler);
                            }
                            if (tip == DepoASNKokSertifika._SILINECEKSERTIFIKA)
                            {
                                DepoASNSilinecekKokSertifika silinecek = (DepoASNSilinecekKokSertifika)asnkok.GetElement();
                                DepoSilinecekKokSertifika depokok = CertStoreUtil.asnSilinecekToDepoSilinecek(silinecek);
                                depokok.setSatirImzasi(depoimza);
                                ven.silinecekKokSertifikaYaz(depokok);
                            }
                        }
                        catch (Exception aEx)
                        {
                            try
                            {
                                byte[] certValue = null;
                                if (tip == DepoASNKokSertifika._EKLENECEKSERTIFIKA)
                                {
                                    DepoASNEklenecekKokSertifika eklenecek = (DepoASNEklenecekKokSertifika)asnkok.GetElement();
                                    certValue = eklenecek.kokSertifikaValue.mValue;
                                }
                                else if (tip == DepoASNKokSertifika._SILINECEKSERTIFIKA)
                                {
                                    DepoASNSilinecekKokSertifika silinecek = (DepoASNSilinecekKokSertifika)asnkok.GetElement();
                                    certValue = silinecek.kokSertifikaValue.mValue;
                                }

                                ECertificate cert = new ECertificate(certValue);

                                String serino = cert.getSerialNumberHex();
                                String subject = cert.getSubject().stringValue();

                                logger.Error("İmzalı Dosyadan " + serino + " seri nolu " + subject + " subjectli kök sertifika eklenemedi.", aEx);
                            }
                            catch (Exception bEx)
                            {
                                logger.Error("İmzalı dosyadaki kök sertifikalardan biri eklenemedi. Sertifika bilgileri alınamıyor", bEx);
                            }
                        }
                    }

                    transaction.Commit();
                }
                catch (Exception aEx)
                {
                    transaction.Rollback();
                    var message = "İmzalı kök sertifikalar veritabanına aktarılırken hata oluştu. Yapılan VT işlemleri geri alınacak.";
                    logger.Error(message, aEx);
                    throw new CertStoreException(message, aEx);
                }
            }
        }


        private Pair<String, List<Object>> _createQuery(CertificateSearchTemplate aSAS, CertificateType[] aTipler, SecurityLevel[] aSeviyeler)
        {
            StringBuilder sb = new StringBuilder("");
            List<Object> params_ = new List<Object>();

            sb.Append("1=1");

            if (aSAS != null)
            {
                byte[] value = aSAS.getValue();
                if (value != null)
                {
                    sb.Append(" AND " + RootCertificateHelper.COLUMN_KOKSERTIFIKA_VALUE + " = ? ");
                    params_.Add(value);
                }

                byte[] hash = aSAS.getHash();
                OzetTipi hashtype = aSAS.getHashType();
                if (hash != null)
                {
                    sb.Append(" AND " + RootCertificateHelper.COLUMN_KOKSERTIFIKA_NO + " IN (SELECT " + HashHelper.COLUMN_OBJECT_NO + " FROM " +
                            HashHelper.OZET_TABLO_ADI + " WHERE ( " + HashHelper.COLUMN_OBJECT_TYPE + " = ? AND " + HashHelper.COLUMN_HASH_VALUE + " = ? ");
                    params_.Add(OzneTipi.KOK_SERTIFIKA.getIntValue());
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
                        sb.Append(" AND " + RootCertificateHelper.COLUMN_KOKSERTIFIKA_NO + " IN (SELECT " + HashHelper.COLUMN_OBJECT_NO + " FROM " +
                                HashHelper.OZET_TABLO_ADI + " WHERE ( " + HashHelper.COLUMN_OBJECT_TYPE + " = ? AND " + HashHelper.COLUMN_HASH_TYPE + " = ? ))");
                        params_.Add(OzneTipi.KOK_SERTIFIKA.getIntValue());
                        params_.Add(hashtype.getIntValue());
                    }
                }


                byte[] issuer = aSAS.getIssuer();
                if (issuer != null)
                {
                    sb.Append(" AND " + RootCertificateHelper.COLUMN_KOKSERTIFIKA_ISSUER + " = ? ");
                    params_.Add(issuer);
                }

                byte[] subject = aSAS.getSubject();
                if (subject != null)
                {
                    sb.Append(" AND " + RootCertificateHelper.COLUMN_KOKSERTIFIKA_SUBJECT + " = ?");
                    params_.Add(subject);
                }


                byte[] serial = aSAS.getSerialNumber();
                if (serial != null)
                {
                    sb.Append(" AND " + RootCertificateHelper.COLUMN_KOKSERTIFIKA_SERIAL + " = ? ");
                    params_.Add(serial);
                }

                byte[] subjectkeyID = aSAS.getSubjectKeyID();
                if (subjectkeyID != null)
                {
                    sb.Append(" AND " + RootCertificateHelper.COLUMN_KOKSERTIFIKA_SUBJECT_KEY_ID + " = ?");
                    params_.Add(subjectkeyID);
                }


                KeyUsageSearchTemplate anahtarkullanim = aSAS.getAnahtarKullanim();
                if (anahtarkullanim != null)
                {
                    sb.Append(" AND " + RootCertificateHelper.COLUMN_KOKSERTIFIKA_KEYUSAGE + " LIKE ?");
                    params_.Add(anahtarkullanim.sorguOlustur());
                }
            }

            if (aTipler != null && aTipler.Length != 0)
            {
                sb.Append(" AND ");
                sb.Append(RootCertificateHelper.COLUMN_KOKSERTIFIKA_TIP + " IN (");
                for (int i = 0; i < aTipler.Length - 1; i++)
                {
                    sb.Append(aTipler[i].getIntValue() + " , ");
                }
                sb.Append(aTipler[aTipler.Length - 1].getIntValue() + " )");
            }

            if (aSeviyeler != null && aSeviyeler.Length != 0)
            {
                sb.Append(" AND ");
                sb.Append(RootCertificateHelper.COLUMN_KOKSERTIFIKA_GUVENLIK_SEVIYESI + " IN (");
                for (int i = 0; i < aSeviyeler.Length - 1; i++)
                {
                    sb.Append(aSeviyeler[i].getIntValue() + " , ");
                }
                sb.Append(aSeviyeler[aSeviyeler.Length - 1].getIntValue() + " )");
            }

            return new Pair<String, List<Object>>(sb.ToString(), params_);

        }
    }
}
