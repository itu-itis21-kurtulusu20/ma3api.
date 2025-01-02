using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.util;
using tr.gov.tubitak.uekae.esya.api.infra.db.certstore.core.helper;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core
{
    public class JDepoVEN : DepoVEN
    {
        private readonly DbConnection _mConnection = null;// DbProviderFactories.GetFactory("System.Data.SQLite").CreateConnection();
        

        public JDepoVEN(DbConnection aConnection)
        {
            _mConnection = aConnection;
        }

        #region DepoVEN Members

        public void sorguCalistir(string aSorgu)
        {
            try
            {
                using (DbCommand command = _mConnection.CreateCommand())
                {
                    command.CommandText = aSorgu;
                    command.CommandType = CommandType.Text;
                    command.ExecuteNonQuery();
                }
            }
            catch (Exception tEx)
            {
                throw new DatabaseException("Sorgu calistirmada hata olustu", tEx);
            }

        }

        public DepoDizin dizinOku(long? aDizinNo)
        {
            DirectoryHelper directoryTable = new DirectoryHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                DepoDizin depoDizin = (DepoDizin)directoryTable.yukle(command, aDizinNo);
                return depoDizin;
            }

        }

        public void dizinYaz(DepoDizin aDizin)
        {
            DirectoryHelper directoryTable = new DirectoryHelper(aDizin);
            directoryTable.yaz(_mConnection);
        }

        public DepoDizin dizinBul(string aDizinAdi)
        {
            String whereClause = DirectoryHelper.COLUMN_DIZIN_AD + " = ?";
            DirectoryHelper directoryTable = new DirectoryHelper();
            try
            {
                using (DbCommand command = _mConnection.CreateCommand())
                {
                    DepoDizin dizin = directoryTable.tekilSonuc(command, whereClause, new Object[] { aDizinAdi });
                    return dizin;
                }
            }
            catch (Exception tEx)
            {
                throw new DatabaseException(typeof(DepoDizin).ToString() + " yuklenmeye calisilirken hata olustu.", tEx);
            }
        }

        public void dizinAdiDegistir(long aDizinNo, string aYeniIsim)
        {
            String setClause = DirectoryHelper.COLUMN_DIZIN_AD + " = ? ";
            String whereClause = DirectoryHelper.COLUMN_DIZIN_NO + " = ? ";

            DirectoryHelper directoryTable = new DirectoryHelper();
            try
            {
                using (DbCommand command = _mConnection.CreateCommand())
                {
                    int affectedRow = directoryTable.updateSorguCalistir(command, setClause, whereClause, new Object[] { aYeniIsim, aDizinNo });
                    if (affectedRow == 0)
                    {
                        throw new DatabaseException(aDizinNo + " nolu dizin veritabaninda olmayabilir");
                    }
                }
            }
            catch (Exception tEx)
            {
                throw new DatabaseException("Dizin adi degistirme sirasinda hata olustu", tEx);
            }
        }

        public ItemSource<DepoDizin> dizinListele()
        {
            DirectoryHelper directoryTable = new DirectoryHelper();
            try
            {
                using (DbCommand command = _mConnection.CreateCommand())
                {
                    return directoryTable.sorgula(command, null, null);
                }
            }
            catch (Exception tEx)
            {
                throw new DatabaseException(typeof(DepoDizin).ToString() + " yuklenmeye calisilirken hata olustu.", tEx);
            }
        }

        //todo test edilecek!!
        public void dizinSil(long aDizinNo)
        {
            String dizinSertifikalariClause = CertificateHelper.COLUMN_SERTIFIKA_NO + " IN (SELECT " + DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " FROM " +
            DirectoryCertificateHelper.DIZINSERTIFIKA_TABLO_ADI + " WHERE " + DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " = ? )";
            CertificateHelper certificateTable = new CertificateHelper();
            //List<DepoSertifika> depoSertifikalar;
            ItemSource<DepoSertifika> sertifikaItemSource;
            using (DbCommand commandCert = _mConnection.CreateCommand())
            {
                sertifikaItemSource = certificateTable.sorgula(commandCert, dizinSertifikalariClause, new Object[] { aDizinNo });
            }

            String dizinSILleriClause = CRLHelper.COLUMN_SIL_NO + " IN (SELECT " + DirectoryCRLHelper.COLUMN_DIZINSIL_SIL_NO + " FROM " + DirectoryCRLHelper.DIZINSIL_TABLO_ADI + " WHERE " + DirectoryCRLHelper.COLUMN_DIZINSIL_DIZIN_NO + " = ? )";
            CRLHelper crlTable = new CRLHelper();
            ItemSource<DepoSIL> sILItemSource;
            using (DbCommand commandForCRL = _mConnection.CreateCommand())
            {
                sILItemSource = crlTable.sorgula(commandForCRL, dizinSILleriClause, new Object[] { aDizinNo });
            }

            DepoSertifika depoSertifika = sertifikaItemSource.nextItem();
            while (depoSertifika != null)
            {
                dizindenSertifikaSil(depoSertifika.getSertifikaNo(), aDizinNo);
                depoSertifika = sertifikaItemSource.nextItem();
            }
            sertifikaItemSource.close();

            DepoSIL depoSIL = sILItemSource.nextItem();
            while (depoSIL != null)
            {
                dizindenSILSil(depoSIL.getSILNo(), aDizinNo);
                depoSIL = sILItemSource.nextItem();
            }
            sILItemSource.close();

            DirectoryHelper directoryTable = new DirectoryHelper();
            using (DbCommand commandForDirectory = _mConnection.CreateCommand())
            {
                directoryTable.sil(commandForDirectory, aDizinNo);
            }
        }
        //todo test edilecek!!
        public DepoSIL sILOku(long aSILNo)
        {
            CRLHelper directoryTable = new CRLHelper();
            try
            {
                using (DbCommand command = _mConnection.CreateCommand())
                {
                    DepoSIL depoSIL = (DepoSIL)directoryTable.yukle(command, aSILNo);
                    return depoSIL;
                }
            }
            catch (Exception tEx)
            {
                throw new DatabaseException(typeof(DepoSIL).ToString() + " yuklenmeye calisilirken hata olustu.", tEx);
            }
        }
        //todo test edilecek!!
        public void sILYaz(DepoSIL aSIL, List<DepoOzet> aOzetler, long? aDizinNo)
        {
            long? dizinNo = aDizinNo;
            long defaultDizinNo = DirectoryHelper.getDefaultDizinNo();
            if (aDizinNo == null)
            {
                dizinNo = defaultDizinNo;
            }
            else
            {
                try
                {
                    dizinOku(aDizinNo);
                }
                catch (ObjectNotFoundException aEx)
                {
                    dizinNo = defaultDizinNo;
                }
            }

            if (dizinNo == defaultDizinNo)
            {
                try
                {
                    dizinOku(defaultDizinNo);
                }
                catch (ObjectNotFoundException aEx)
                {
                    throw new DatabaseException("Default dizin (1 nolu DEPO isimli) veritabaninda bulunamadi");
                }
            }

            long? vtSILNo = null;
            try
            {
                vtSILNo = sILNoHasheGoreBul(aOzetler[0].getHashValue());
            }
            catch (ObjectNotFoundException aEx)
            {
                CRLHelper crllTable = new CRLHelper(aSIL);

                crllTable.yaz(_mConnection);
                vtSILNo = aSIL.getSILNo();
                ozneOzetIliskiYaz(aOzetler, vtSILNo);

            }

            dizinSILIliskiYaz(dizinNo.Value, vtSILNo.Value);
        }
        //todo test edilecek!!
        public ItemSource<DepoSIL> sILListele(string aSorgu, object[] aParams)
        {
            CRLHelper crlTable = new CRLHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return crlTable.sorgula(command, aSorgu, aParams);
            }
        }
        //todo test edilecek!!
        public ItemSource<DepoSIL> sILListele()
        {
            return sILListele(null, null);
        }
        //todo test edilecek!!
        public ItemSource<DepoDizin> sILDizinleriniListele(long aSILNo)
        {
            String whereClause = DirectoryHelper.COLUMN_DIZIN_NO + " IN (SELECT " + DirectoryCRLHelper.COLUMN_DIZINSIL_DIZIN_NO + " FROM " +
         DirectoryCRLHelper.DIZINSIL_TABLO_ADI + " WHERE " + DirectoryCRLHelper.COLUMN_DIZINSIL_SIL_NO + " = ? )";
            DirectoryHelper directoryTable = new DirectoryHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return directoryTable.sorgula(command, whereClause, new Object[] { aSILNo });
            }
        }
        //todo test edilecek!!
        public int dizindenSILSil(long? aSILNo, long? aDizinNo)
        {
            String deleteDizinSIL = DirectoryCRLHelper.COLUMN_DIZINSIL_SIL_NO + " = ? AND " + DirectoryCRLHelper.COLUMN_DIZINSIL_DIZIN_NO + " = ?";
            DirectoryCRLHelper directoryCrlTable = new DirectoryCRLHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                int sonuc = directoryCrlTable.deleteSorguCalistir(command, deleteDizinSIL, new Object[] { aSILNo, aDizinNo });
                if (sonuc == 0)
                {
                    throw new DatabaseException("Verilen silno ve dizinno ya ait kayit bulunamamistir");
                }
            }
            String fieldsClause = DirectoryCRLHelper.COLUMN_DIZINSIL_SIL_NO;
            String whereClause = DirectoryCRLHelper.COLUMN_DIZINSIL_SIL_NO + " =? ";
            bool sILVar;
            using (DbCommand command = _mConnection.CreateCommand())
            {
                sILVar = directoryCrlTable.selectSorguCalistir(command, fieldsClause, whereClause, new Object[] { aSILNo });
            }
            int etkilenen = 0;
            if (!sILVar)
            {
                CRLHelper crlTable = new CRLHelper();
                using (DbCommand command = _mConnection.CreateCommand())
                {
                    etkilenen = crlTable.sil(command, aSILNo);
                }
                ozetleriSil(OzneTipi.SIL.getIntValue(), aSILNo);
            }

            return etkilenen;
        }
        //todo test edilecek!!
        public void sILTasi(long aSILNo, long aEskiDizinNo, long aYeniDizinNo)
        {
            try
            {
                dizinOku(aYeniDizinNo);
            }
            catch (ObjectNotFoundException aEx)
            {
                throw new DatabaseException(aYeniDizinNo + " nolu dizin veritabaninda bulunamamistir.", aEx);
            }

            String setClause = DirectoryCRLHelper.COLUMN_DIZINSIL_DIZIN_NO + " = ?";
            String whereClause = DirectoryCRLHelper.COLUMN_DIZINSIL_DIZIN_NO + " = ? AND " + DirectoryCRLHelper.COLUMN_DIZINSIL_SIL_NO + " = ?";
            DirectoryCRLHelper directoryCrlTable = new DirectoryCRLHelper();
            int sonuc;
            using (DbCommand command = _mConnection.CreateCommand())
            {
                sonuc = directoryCrlTable.updateSorguCalistir(command, setClause, whereClause, new Object[] { aYeniDizinNo, aEskiDizinNo, aSILNo });
            }
            if (sonuc == 0)
            {
                throw new DatabaseException("Verilen parametreler icin update islemi yapilamamistir.Parametreler yanlis olabilir");
            }
        }
        //todo test edilecek!!
        public int sILSil(long? aSILNo)
        {
            CRLHelper crlTable = new CRLHelper();

            int etkilenen;
            using (DbCommand command = _mConnection.CreateCommand())
            {
                etkilenen = crlTable.sil(command, aSILNo);
            }
            String whereClause = DirectoryCRLHelper.COLUMN_DIZINSIL_SIL_NO + " = ?";
            DirectoryCRLHelper directoryCrlTable = new DirectoryCRLHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                directoryCrlTable.deleteSorguCalistir(command, whereClause, new Object[] { aSILNo });
            }
            ozetleriSil(OzneTipi.SIL.getIntValue(), aSILNo);
            return etkilenen;
        }
        //todo test edilecek!!
        public int kokSertifikaSil(long? aKokNo)
        {
            RootCertificateHelper rootCertificateTable = new RootCertificateHelper();
            int sonuc;
            using (DbCommand command = _mConnection.CreateCommand())
            {
                sonuc = rootCertificateTable.sil(command, aKokNo);
            }
            ozetleriSil(OzneTipi.KOK_SERTIFIKA.getIntValue(), aKokNo);
            return sonuc;
        }
        //todo test edilecek!!
        public void kokSertifikaYaz(DepoKokSertifika aKok, List<DepoOzet> aOzetler)
        {
            try
            {
                DepoKokSertifika kok = kokSertifikaHasheGoreBul(aOzetler[0].getHashValue());
                int vtkokseviye = kok.getKokGuvenSeviyesi().getIntValue();
                int gelenkokseviye = aKok.getKokGuvenSeviyesi().getIntValue();

                if (vtkokseviye > gelenkokseviye)
                {
                    RootCertificateHelper rootCertificateTable = new RootCertificateHelper(kok);
                    rootCertificateTable.yaz(_mConnection);
                }
                else if (vtkokseviye == gelenkokseviye)
                {
                    throw new DatabaseException("Depoda ayni hash ve guvenlik seviyesine sahip kok sertifika var.");
                }
            }
            catch (ObjectNotFoundException aEx)
            {
                RootCertificateHelper rootCertificateTable = new RootCertificateHelper(aKok);
                rootCertificateTable.yaz(_mConnection);
                ozneOzetIliskiYaz(aOzetler, aKok.getKokSertifikaNo());
            }
        }
        //todo test edilecek!!
        public ItemSource<DepoKokSertifika> kokSertifikaListele()
        {
            //throw new NotImplementedException();
            return kokSertifikaListele(null, null);
        }
        //todo test edilecek!!
        public ItemSource<DepoKokSertifika> kokSertifikaListele(string aSorgu, object[] aParams)
        {
            RootCertificateHelper rootCertificateTable = new RootCertificateHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return rootCertificateTable.sorgula(command, aSorgu, aParams);
            }
        }
        //todo test edilecek!!
        public DepoKokSertifika kokSertifikaHasheGoreBul(byte[] aHash)
        {
            String whereClause = RootCertificateHelper.COLUMN_KOKSERTIFIKA_NO + " IN (SELECT " + HashHelper.COLUMN_OBJECT_NO + " FROM " +
        HashHelper.OZET_TABLO_ADI + " WHERE " + HashHelper.COLUMN_OBJECT_TYPE + " = ? AND " + HashHelper.COLUMN_HASH_VALUE + " = ?  )";
            RootCertificateHelper rootCertificateTable = new RootCertificateHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return rootCertificateTable.tekilSonuc(command, whereClause, new Object[] { OzneTipi.KOK_SERTIFIKA, aHash });
            }
        }
        //todo test edilecek!!
        public DepoKokSertifika kokSertifikaValueYaGoreBul(byte[] aValue)
        {
            String whereClause = RootCertificateHelper.COLUMN_KOKSERTIFIKA_VALUE + " = ? ";
            RootCertificateHelper rootCertificateTable = new RootCertificateHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return rootCertificateTable.tekilSonuc(command, whereClause, new Object[] { aValue });
            }
        }
        //todo test edilecek!!
        public void silinecekKokSertifikaYaz(DepoSilinecekKokSertifika aKok)
        {
            SilinecekKokSertifikaHelper silinecekKokSertifikaTable = new SilinecekKokSertifikaHelper(aKok);
            silinecekKokSertifikaTable.yaz(_mConnection);
        }

        public ItemSource<DepoSilinecekKokSertifika> silinecekKokSertifikaListele()
        {
            SilinecekKokSertifikaHelper silinecekKokSertifikaTable = new SilinecekKokSertifikaHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return silinecekKokSertifikaTable.sorgula(command, null, null);
            }
        }

        public Dictionary<String, Object> sistemParametreleriniOku()
        {
            Dictionary<String, Object> parammap = new Dictionary<String, Object>();
            SistemParametresiHelper sistemParametresiHelper = new SistemParametresiHelper();
            ItemSource<DepoSistemParametresi> depoSistemParametresiItemSource;
            using (DbCommand command = _mConnection.CreateCommand())
            {
                depoSistemParametresiItemSource = sistemParametresiHelper.sorgula(command, null, null);

                DepoSistemParametresi sp = depoSistemParametresiItemSource.nextItem();
                while (sp != null)
                {
                    parammap[sp.getParamAdi()] = sp.getParamDegeri();
                    sp = depoSistemParametresiItemSource.nextItem();
                }
            }
            return parammap;
        }

        public DepoSistemParametresi sistemParametresiOku(string aParamAdi)
        {
            String whereClause = SistemParametresiHelper.COLUMN_SP_PARAM_ADI + " = ?";
            SistemParametresiHelper sistemParametresiHelper = new SistemParametresiHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return (DepoSistemParametresi)sistemParametresiHelper.tekilSonuc(command, whereClause, new Object[] { aParamAdi });
            }
        }

        public void sistemParametresiUpdate(string aParamAdi, object aParamDeger)
        {
            String setClause = SistemParametresiHelper.COLUMN_SP_PARAM_DEGERI + " = ?";
            String whereClause = SistemParametresiHelper.COLUMN_SP_PARAM_ADI + " = ?";
            SistemParametresiHelper sistemParametresiHelper = new SistemParametresiHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                sistemParametresiHelper.updateSorguCalistir(command, setClause, whereClause, new Object[] { aParamDeger, aParamAdi });
            }
        }

        public DepoSertifika sertifikaOku(long? aSertifikaNo)
        {
            CertificateHelper certificateHelper = new CertificateHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return (DepoSertifika)certificateHelper.yukle(command, aSertifikaNo);
            }
        }

        public ItemSource<DepoSertifika> sertifikaListele()
        {
            return sertifikaListele(null, null);
        }

     


        public ItemSource<DepoSertifika> sertifikaListele(string aSorgu, object[] aParams)
        {
            CertificateHelper certificateHelper = new CertificateHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return certificateHelper.sorgula(command, aSorgu, aParams);
            }
        }

        public ItemSource<DepoNitelikSertifikasi> nitelikSertifikasiListele(string aSorgu, object[] aParams)
        {
            NitelikSertifikasiYardimci nsHelper = new NitelikSertifikasiYardimci();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return nsHelper.sorgula(command, aSorgu, aParams);
            }
        }

        public ItemSource<DepoDizin> sertifikaDizinleriniListele(long aSertifikaNo)
        {
            String whereClause = DirectoryHelper.COLUMN_DIZIN_NO + " IN (SELECT " + DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " FROM " +
        DirectoryCertificateHelper.DIZINSERTIFIKA_TABLO_ADI + " WHERE " + DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " = ? )";
            DirectoryHelper directoryHelper = new DirectoryHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return directoryHelper.sorgula(command, whereClause, new Object[] { aSertifikaNo });
            }
        }
        //todo test edilecek!
        public int sertifikaSil(long? aSertifikaNo)
        {
            CertificateHelper certificateHelper = new CertificateHelper();
            int etkilenen;
            using (DbCommand command = _mConnection.CreateCommand())
            {
                etkilenen = certificateHelper.sil(command, aSertifikaNo);
            }

            String whereClause = DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " = ?";
            DirectoryCertificateHelper directoryCertificateHelper = new DirectoryCertificateHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                directoryCertificateHelper.deleteSorguCalistir(command, whereClause, new Object[] { aSertifikaNo });
            }

            ozetleriSil(OzneTipi.SERTIFIKA.getIntValue(), aSertifikaNo);

            attributeCertSil(aSertifikaNo);

            CertificateOCSPsHelper certificateOcspsHelper = new CertificateOCSPsHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                DepoSertifikaOcsps depoSertifikaOcsps = null;
                try
                {
                    depoSertifikaOcsps = certificateOcspsHelper.tekilSonuc(command, CertificateOCSPsHelper.COLUMN_SERTIFIKA_NO + " = ?", new Object[] { aSertifikaNo });
                }
                catch (ObjectNotFoundException ex)
                {
                    return etkilenen;
                }
                ocspCevabiSil(depoSertifikaOcsps.getOcspNo().Value);
            }

            return etkilenen;
        }

        public int nitelikSertifikasiSil(long? aNitelikSertifikaNo)
        {
            NitelikSertifikasiYardimci nsHelper = new NitelikSertifikasiYardimci();
            String whereClause = NitelikSertifikasiYardimci.COLUMN_SERTIFIKA_ID + " = ? ";

            using (DbCommand command = _mConnection.CreateCommand())
            {
                return nsHelper.deleteSorguCalistir(command, whereClause, new Object[] { aNitelikSertifikaNo });
            }
            
        }

        //todo test edilecek!!
        public int dizindenSertifikaSil(long? aSertifikaNo, long? aDizinNo)
        {
            String deleteDizinSertifika = DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " = ? AND " + DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " = ?";
            DirectoryCertificateHelper directoryCertificateHelper = new DirectoryCertificateHelper();
            int sonuc = 0;
            using (DbCommand command = _mConnection.CreateCommand())
            {
                sonuc = directoryCertificateHelper.deleteSorguCalistir(command, deleteDizinSertifika, new Object[] { aSertifikaNo, aDizinNo });
            }
            if (sonuc == 0)
            {
                throw new DatabaseException("Verilen sertifikano ve dizinno ya ait kayit bulunamamistir");
            }
            String fieldsClause = DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO;
            String whereClause = DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " =? ";
            bool sertifikaVar = false;
            using (DbCommand command = _mConnection.CreateCommand())
            {
                sertifikaVar = directoryCertificateHelper.selectSorguCalistir(command, fieldsClause, whereClause, new Object[] { aSertifikaNo });
            }
            int etkilenen = 0;
            if (!sertifikaVar)
            {
                CertificateHelper certificateHelper = new CertificateHelper();
                using (DbCommand command = _mConnection.CreateCommand())
                {
                    etkilenen = certificateHelper.sil(command, aSertifikaNo);
                    ozetleriSil(OzneTipi.SERTIFIKA.getIntValue(), aSertifikaNo);
                    attributeCertSil(aSertifikaNo);
                }
            }

            return etkilenen;
        }

        public void ozetleriSil(int aObjectType, long? aObjectNo)
        {
            HashHelper hashHelper = new HashHelper();
            String whereClause = HashHelper.COLUMN_OBJECT_TYPE + " = ? AND " + HashHelper.COLUMN_OBJECT_NO + " = ? ";
            using (DbCommand command = _mConnection.CreateCommand())
            {
                hashHelper.deleteSorguCalistir(command, whereClause, new Object[] { aObjectType, aObjectNo });
            }
        }

        public void attributeCertSil(long? aCertObjNo)
        {
            nitelikSertifikasiSil(aCertObjNo);
        }

        //todo test edilecek!!
        public void sertifikaTasi(long aSertifikaNo, long aEskiDizinNo, long aYeniDizinNo)
        {
            try
            {
                dizinOku(aYeniDizinNo);
            }
            catch (ObjectNotFoundException aEx)
            {
                throw new DatabaseException("Sertifikanin tasinmak istendigi dizin olan," + aYeniDizinNo + " nolu dizin veritabaninda bulunamamistir.", aEx);
            }

            String setClause = DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " = ?";
            String whereClause = DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_DIZIN_NO + " = ? AND " + DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + " = ?";
            DirectoryCertificateHelper directoryCertificateHelper = new DirectoryCertificateHelper();
            int sonuc = 0;
            using (DbCommand command = _mConnection.CreateCommand())
            {
                sonuc = directoryCertificateHelper.updateSorguCalistir(command, setClause, whereClause, new Object[] { aYeniDizinNo, aEskiDizinNo, aSertifikaNo });
            }
            if (sonuc == 0)
            {
                throw new DatabaseException("Verilen parametreler icin update islemi yapilamamistir.Parametreler yanlis olabilir");
            }
        }

        public ItemSource<DepoSertifika> ozelAnahtarliSertifikalariListele()
        {
            String whereClause = CertificateHelper.COLUMN_SERTIFIKA_PRIVATE_KEY + " IS NOT NULL ";
            CertificateHelper certificateHelper = new CertificateHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return certificateHelper.sorgula(command, whereClause, null);
            }
        }

        public void sertifikaYaz(DepoSertifika aSertifika, bool aYeniNesne)
        {
            CertificateHelper certificateHelper = new CertificateHelper(aSertifika);
            certificateHelper.yaz(_mConnection);
        }

        public void sertifikaYaz(DepoSertifika aSertifika, List<DepoOzet> aOzetler, long? aDizinNo)
        {
            long? dizinNo = aDizinNo;

            long defaultDizin = DirectoryHelper.getDefaultDizinNo();

            if (aDizinNo == null)
            {
                dizinNo = defaultDizin;
            }
            else
            {
                try
                {
                    dizinOku(aDizinNo);
                }
                catch (ObjectNotFoundException aEx)
                {
                    dizinNo = defaultDizin;
                }
            }

            if (dizinNo == defaultDizin)
            {
                try
                {
                    dizinOku(defaultDizin);
                }
                catch (ObjectNotFoundException aEx)
                {
                    throw new DatabaseException("Default dizin (1 nolu DEPO isimli) veritabaninda bulunamadi");
                }
            }

            long? vtsertifikaNo = null;
            try
            {
                vtsertifikaNo = sertifikaNoHasheGoreBul(aOzetler[0].getHashValue());
            }
            catch (ObjectNotFoundException aEx)
            {
                sertifikaYaz(aSertifika, true);
                vtsertifikaNo = aSertifika.getSertifikaNo();
                ozneOzetIliskiYaz(aOzetler, vtsertifikaNo);
            }

            dizinSertifikaIliskiYaz(dizinNo, vtsertifikaNo);
        }       
      
       
        /********* OCSP *********/

        public DepoOCSP ocspCevabiOku(long aOCSPNo)
        {
            OCSPHelper ocspHelper = new OCSPHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return ocspHelper.yukle(command, aOCSPNo);
            }
        }

        public int ocspCevabiSil(long aOCSPNo)
        {
            OCSPHelper ocspHelper = new OCSPHelper();
            int sonuc = 0;
            using (DbCommand command = _mConnection.CreateCommand())
            {
                sonuc = ocspHelper.sil(command, aOCSPNo);
            }
            ocspSertifikaIliskiSil(aOCSPNo);
            ozetleriSil(OzneTipi.OCSP_RESPONSE.getIntValue(), aOCSPNo);
            ozetleriSil(OzneTipi.OCSP_BASIC_RESPONSE.getIntValue(), aOCSPNo);
            return sonuc;
        }

        public void ocspCevabiYaz(DepoOCSP aOCSPCevabi, List<DepoOzet> aOzetler)
        {
            try
            {
                ocspCevabiNoHasheGoreBul(aOzetler[0].getHashValue());
                throw new DatabaseException("OCSP Cevabi veritabaninda var");
            }
            catch (ObjectNotFoundException aEx)
            {
                OCSPHelper ocspHelper = new OCSPHelper(aOCSPCevabi);
                ocspHelper.yaz(_mConnection);
                ozneOzetIliskiYaz(aOzetler, aOCSPCevabi.getOCSPNo());
            }
        }

        public void ocspCevabiVeSertifikaYaz(DepoOCSP aOCSP, List<DepoOzet> aOCSPOzetler, DepoSertifika aSertifika, List<DepoOzet> aSertifikaOzetler, ESingleResponse aSingleResponse)
        {
            DepoSertifika vtsertifika = null;
            CertificateHelper certificateHelper = new CertificateHelper();
            try
            {
                using (DbCommand command = _mConnection.CreateCommand())
                {
                    vtsertifika = certificateHelper.tekilSonuc(command, CertificateHelper.COLUMN_SERTIFIKA_VALUE + " = ?", new Object[] { aSertifika.getValue() });
                }
            }
            catch (ObjectNotFoundException aEx)
            {
                sertifikaYaz(aSertifika, aSertifikaOzetler, null);
                //ozneOzetIliskiYaz(aSertifikaOzetler, aSertifika.getmSertifikaNo());  //sertifikaYaz metodu icinde yapiliyor zaten
                vtsertifika = aSertifika;
            }

            ocspCevabiYaz(aOCSP, aOCSPOzetler);
            //ocspSertifikaIliskiYaz(aOCSP.getmOCSPNo(),vtsertifika.getmSertifikaNo());
            DepoSertifikaOcsps dSertifikaOcsps = CertStoreUtil.toDepoSertifikaOcsps(aSingleResponse, aOCSP, vtsertifika);
            ocspSertifikaIliskiYaz(dSertifikaOcsps);
        }

        public ItemSource<DepoOCSP> ocspCevabiListele(string aSorgu, object[] aParams)
        {
            OCSPHelper ocspHelper = new OCSPHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return ocspHelper.sorgula(command, aSorgu, aParams);
            }
        }

        public ItemSource<DepoOzet> ocspOzetleriniListele(long aOCSPNo)
        {
            String whereClause = "( " + HashHelper.COLUMN_OBJECT_TYPE + " = ? AND " + HashHelper.COLUMN_OBJECT_NO + " = ? )";
            HashHelper hashHelper = new HashHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                return hashHelper.sorgula(command, whereClause, new Object[] { OzneTipi.OCSP_RESPONSE.getIntValue(), OzneTipi.OCSP_BASIC_RESPONSE.getIntValue(), aOCSPNo });
            }
        }

        #endregion

        public long? ocspCevabiNoHasheGoreBul(byte[] aHash)
        {
            //C imza icin JAVA tarafinda yapilan degisikliklere gore duzenlendi
            //return hasheGoreBul(aHash, OzneTipi.OCSP);
            String whereClause = HashHelper.COLUMN_HASH_VALUE + " = ? ";// "AND " + HashHelper.COLUMN_OBJECT_TYPE + " = ?";
            HashHelper hashTable = new HashHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                DepoOzet ozet = hashTable.tekilSonuc(command, whereClause, new Object[] { aHash/*, aOzne.getIntValue()*/ });
                return ozet.getObjectNo();
            }
        }
        public long? sertifikaNoHasheGoreBul(byte[] aHash)
        {
            return hasheGoreBul(aHash, OzneTipi.SERTIFIKA);
        }

        public long? sILNoHasheGoreBul(byte[] aHash)
        {
            return hasheGoreBul(aHash, OzneTipi.SIL);
        }

        private long? hasheGoreBul(byte[] aHash, OzneTipi aOzne)
        {
            String whereClause = HashHelper.COLUMN_HASH_VALUE + " = ? AND " + HashHelper.COLUMN_OBJECT_TYPE + " = ?";
            HashHelper hashTable = new HashHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                DepoOzet ozet = hashTable.tekilSonuc(command, whereClause, new Object[] { aHash, aOzne.getIntValue() });
                return ozet.getObjectNo();
            }
        }

        private void ozneOzetIliskiYaz(List<DepoOzet> aOzetler, long? aOzneNo)
        {
            foreach (DepoOzet ozet in aOzetler)
            {
                ozet.setObjectNo(aOzneNo);
                HashHelper hashTable = new HashHelper(ozet);
                hashTable.yaz(_mConnection);
            }
        }

        private void dizinSILIliskiYaz(long aDizinNo, long aSILNo)
        {
            Boolean exist = false;
            List<DepoDizin> dizinler = ((RsItemSource<DepoDizin>)sILDizinleriniListele(aSILNo)).toList();
            foreach (DepoDizin depoDizin in dizinler)
            {
                if(depoDizin.getDizinNo() == aDizinNo)
    		    {
    			    exist = true;
    			    break;
    		    }
            }

            if (exist == false)
            {
                String fieldsClause = "(" + DirectoryCRLHelper.COLUMN_DIZINSIL_DIZIN_NO + "," +
                                      DirectoryCRLHelper.COLUMN_DIZINSIL_SIL_NO + ")";
                String valuesClause = "(?,?)";

                DirectoryCRLHelper directoryTable = new DirectoryCRLHelper();
                using (DbCommand command = _mConnection.CreateCommand())
                {
                    directoryTable.insertSorguCalistir(command, fieldsClause, valuesClause,
                                                       new Object[] {aDizinNo, aSILNo});
                }
            }
        }

        private void dizinSertifikaIliskiYaz(long? aDizinNo, long? aSertifikaNo)
        {
            String fieldsClause = "(" + DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_DIZIN_NO + "," + DirectoryCertificateHelper.COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO + ")";
            String valuesClause = "(?,?)";
            DirectoryCertificateHelper directoryCertificateHelper = new DirectoryCertificateHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                directoryCertificateHelper.insertSorguCalistir(command, fieldsClause, valuesClause, new Object[] { aDizinNo, aSertifikaNo });
            }
        }
        public void ocspSertifikaIliskiSil(long aOCSPNo)
        {
            String sorgu = CertificateOCSPsHelper.COLUMN_OCSP_NO + " = ? ";
            CertificateOCSPsHelper certificateOCSPsHelper = new CertificateOCSPsHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                certificateOCSPsHelper.deleteSorguCalistir(command, sorgu, new Object[] { aOCSPNo });
            }
        }

        public void attributeAndPKICertYaz(DepoSertifika aSertifika, List<DepoOzet> aOzetler, List<DepoNitelikSertifikasi> aNitelikSertifikalari, long? aDizinNo)
        {

            DepoSertifika vtsertifika = null;
            CertificateHelper certificateHelper = new CertificateHelper();
            try
            {
                using (DbCommand command = _mConnection.CreateCommand())
                {
                    vtsertifika = certificateHelper.tekilSonuc(command, CertificateHelper.COLUMN_SERTIFIKA_VALUE + " = ?", new Object[] { aSertifika.getValue() });
                }
            }
            catch (ObjectNotFoundException aEx)
            {
                sertifikaYaz(aSertifika, aOzetler, null);
                //ozneOzetIliskiYaz(aSertifikaOzetler, aSertifika.getmSertifikaNo());  //sertifikaYaz metodu icinde yapiliyor zaten
                vtsertifika = aSertifika;
            }
                    
            foreach(DepoNitelikSertifikasi ns in aNitelikSertifikalari)
            {
        	    ns.setSertifikaNo(vtsertifika.getSertifikaNo());
        	    attributeCertYaz(ns);
            }

        }
        public void attributeCertYaz(DepoNitelikSertifikasi aNitelikSertifikasi)
        {
            NitelikSertifikasiYardimci ns = new NitelikSertifikasiYardimci(aNitelikSertifikasi);
            ns.yaz(_mConnection);    	     
        }


        //private void ocspSertifikaIliskiYaz(long aOCSPNo,long aSertifikaNo)
        private void ocspSertifikaIliskiYaz(DepoSertifikaOcsps dSertifikaOcsps)
        {
            String fieldsClause = "(" + CertificateOCSPsHelper.COLUMN_OCSP_NO + "," + CertificateOCSPsHelper.COLUMN_SERTIFIKA_NO + "," +
                CertificateOCSPsHelper.COLUMN_THIS_UPDATE + "," + CertificateOCSPsHelper.COLUMN_STATUS + "," +
                CertificateOCSPsHelper.COLUMN_REVOCATION_TIME + "," + CertificateOCSPsHelper.COLUMN_REVOCATION_REASON + ")";
            String valuesClause = "(?,?,?,?,?,?)";
            CertificateOCSPsHelper certificateOcspHelper = new CertificateOCSPsHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                certificateOcspHelper.insertSorguCalistir(command, fieldsClause, valuesClause, new Object[] {dSertifikaOcsps.getOcspNo(), dSertifikaOcsps.getSertifikaNo(),
                    dSertifikaOcsps.getThisUpdate(), dSertifikaOcsps.getStatus(), dSertifikaOcsps.getRevocationTime(), dSertifikaOcsps.getRevocationReason() });
            }
        }
        /*
        private void ocspSertifikaIliskiYaz(long? aOCSPNo, long? aSertifikaNo)
        {
            String fieldsClause = "(" + CertificateOCSPsHelper.COLUMN_OCSP_NO + "," + CertificateOCSPsHelper.COLUMN_SERTIFIKA_NO + ")";
            String valuesClause = "(?,?)";
            CertificateOCSPsHelper certificateOcspHelper = new CertificateOCSPsHelper();
            using (DbCommand command = _mConnection.CreateCommand())
            {
                certificateOcspHelper.insertSorguCalistir(command, fieldsClause, valuesClause, new Object[] { aOCSPNo, aSertifikaNo });
            }
        }*/
    }
}
