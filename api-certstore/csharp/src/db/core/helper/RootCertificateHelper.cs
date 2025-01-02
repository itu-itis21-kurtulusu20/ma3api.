using System;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class RootCertificateHelper : Islemler<DepoKokSertifika>
    {
        public static readonly String COLUMN_KOKSERTIFIKA_DATE = "KOKEklenmeTarihi";
        public static readonly String COLUMN_KOKSERTIFIKA_VALUE = "KOKSertifikaValue";
        //public static readonly String COLUMN_KOKSERTIFIKA_HASH = "KOKSertifikaHash";
        public static readonly String COLUMN_KOKSERTIFIKA_SERIAL = "KOKSerialNumber";
        public static readonly String COLUMN_KOKSERTIFIKA_ISSUER = "KOKIssuerName";
        public static readonly String COLUMN_KOKSERTIFIKA_START_DATE = "KOKStartDate";
        public static readonly String COLUMN_KOKSERTIFIKA_END_DATE = "KOKEndDate";
        public static readonly String COLUMN_KOKSERTIFIKA_SUBJECT = "KOKSubjectName";
        public static readonly String COLUMN_KOKSERTIFIKA_KEYUSAGE = "KOKKeyUsageStr";
        public static readonly String COLUMN_KOKSERTIFIKA_SUBJECT_KEY_ID = "KOKSubjectKeyIdentifier";
        public static readonly String COLUMN_KOKSERTIFIKA_TIP = "KOKSertifikaTipi";
        public static readonly String COLUMN_KOKSERTIFIKA_GUVENLIK_SEVIYESI = "KOKGuvenSeviyesi";
        public static readonly String COLUMN_KOKSERTIFIKA_SATIR_IMZA = "KOKSatirImzasi";
        public static readonly String COLUMN_KOKSERTIFIKA_NO = "KOKSertifikaNo";

        public static readonly String KOKSERTIFIKA_TABLO_ADI = "KOKSERTIFIKA";

        public static readonly String[] COLUMNS = 
	    {
		    COLUMN_KOKSERTIFIKA_DATE,
		    COLUMN_KOKSERTIFIKA_VALUE,
		    //COLUMN_KOKSERTIFIKA_HASH,
		    COLUMN_KOKSERTIFIKA_SERIAL,
		    COLUMN_KOKSERTIFIKA_ISSUER,
		    COLUMN_KOKSERTIFIKA_START_DATE,
		    COLUMN_KOKSERTIFIKA_END_DATE,
		    COLUMN_KOKSERTIFIKA_SUBJECT,
		    COLUMN_KOKSERTIFIKA_KEYUSAGE,
		    COLUMN_KOKSERTIFIKA_SUBJECT_KEY_ID,
		    COLUMN_KOKSERTIFIKA_TIP,
		    COLUMN_KOKSERTIFIKA_GUVENLIK_SEVIYESI,
		    COLUMN_KOKSERTIFIKA_SATIR_IMZA,
		    COLUMN_KOKSERTIFIKA_NO
		
	    };
        public override String[] sutunAdlariAl()
        {
            return COLUMNS;
        }

        public override String tabloAdiAl()
        {
            return KOKSERTIFIKA_TABLO_ADI;
        }

        private readonly DepoKokSertifika _mDepoKokSertifika;
        public RootCertificateHelper(DepoKokSertifika aDepoKokSertifika)
        {
            _mDepoKokSertifika = aDepoKokSertifika;
        }
        public RootCertificateHelper() { }
        public override void sorguyuDoldur(DbCommand aCommand)
        {            
            DbParameter kokEklenmeTarihiParam = aCommand.CreateParameter();
            DateTime? kokEklenmeTarihi = _mDepoKokSertifika.getKokEklenmeTarihi();
            if (kokEklenmeTarihi != null)
                kokEklenmeTarihiParam.Value = kokEklenmeTarihi;
            else
                kokEklenmeTarihiParam.Value = DateTime.UtcNow;
            aCommand.Parameters.Add(kokEklenmeTarihiParam);

            DbParameter valueParam = aCommand.CreateParameter();
            valueParam.Value = _mDepoKokSertifika.getValue();
            aCommand.Parameters.Add(valueParam);

            DbParameter serialNumberParam = aCommand.CreateParameter();
            serialNumberParam.Value = _mDepoKokSertifika.getSerialNumber();
            aCommand.Parameters.Add(serialNumberParam);

            DbParameter issuerNameParam = aCommand.CreateParameter();
            issuerNameParam.Value = _mDepoKokSertifika.getIssuerName();
            aCommand.Parameters.Add(issuerNameParam);

            DbParameter startDateParam = aCommand.CreateParameter();
            startDateParam.Value = _mDepoKokSertifika.getStartDate();
            aCommand.Parameters.Add(startDateParam);

            DbParameter endDateParam = aCommand.CreateParameter();
            endDateParam.Value = _mDepoKokSertifika.getEndDate();
            aCommand.Parameters.Add(endDateParam);

            DbParameter subjectNameParam = aCommand.CreateParameter();
            subjectNameParam.Value = _mDepoKokSertifika.getSubjectName();
            aCommand.Parameters.Add(subjectNameParam);

            DbParameter keyUsageParam = aCommand.CreateParameter();
            keyUsageParam.Value = _mDepoKokSertifika.getKeyUsageStr();
            aCommand.Parameters.Add(keyUsageParam);

            DbParameter subjectKeyIdentifierParam = aCommand.CreateParameter();
            subjectKeyIdentifierParam.Value = _mDepoKokSertifika.getSubjectKeyIdentifier();
            aCommand.Parameters.Add(subjectKeyIdentifierParam);

            DbParameter kokTipiParam = aCommand.CreateParameter();
            kokTipiParam.Value = _mDepoKokSertifika.getKokTipi().getIntValue();
            aCommand.Parameters.Add(kokTipiParam);

            DbParameter kokGuvenSeviyesiParam = aCommand.CreateParameter();
            kokGuvenSeviyesiParam.Value = _mDepoKokSertifika.getKokGuvenSeviyesi().getIntValue();
            aCommand.Parameters.Add(kokGuvenSeviyesiParam);

            DbParameter satirImzasiParam = aCommand.CreateParameter();
            satirImzasiParam.Value = _mDepoKokSertifika.getSatirImzasi();
            aCommand.Parameters.Add(satirImzasiParam);


            DbParameter kokSertifikaNoParam = aCommand.CreateParameter();
            kokSertifikaNoParam.Value = _mDepoKokSertifika.getKokSertifikaNo();
            aCommand.Parameters.Add(kokSertifikaNoParam);           
        }
        public override /*Object*/DepoKokSertifika nesneyiDoldur(DbDataReader aDataReader)
        {
            DepoKokSertifika sertifika = new DepoKokSertifika();
            //Object serial = aDataReader[COLUMN_KOKSERTIFIKA_SERIAL];
            
            //Object eklenme = aDataReader[COLUMN_KOKSERTIFIKA_DATE];
            //Object obj = aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_DATE));
            sertifika.setKokEklenmeTarihi(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_DATE)) as DateTime?);
            sertifika.setEndDate(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_END_DATE)) as DateTime?);
            sertifika.setStartDate(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_START_DATE)) as DateTime?);
            sertifika.setSerialNumber(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_SERIAL)) as byte[]);
            sertifika.setValue(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_VALUE)) as byte[]);
            //sertifika.setmHash(aRS.getBytes(COLUMN_KOKSERTIFIKA_HASH));
            sertifika.setIssuerName(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_ISSUER)) as byte[]);
            sertifika.setSubjectName(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_SUBJECT)) as byte[]);
            sertifika.setKeyUsageStr(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_KEYUSAGE)) as string);
            sertifika.setKokTipi(CertificateType.getNesne((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_TIP))));
            sertifika.setKokGuvenSeviyesi(SecurityLevel.getNesne((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_GUVENLIK_SEVIYESI))));
            sertifika.setSatirImzasi(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_SATIR_IMZA)) as byte[]);
            sertifika.setSubjectKeyIdentifier(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_SUBJECT_KEY_ID)) as byte[]);
            sertifika.setKokSertifikaNo((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_KOKSERTIFIKA_NO)));
            return sertifika;
        }

        public override long? getNo()
        {
            //throw new NotImplementedException();
            return _mDepoKokSertifika.getKokSertifikaNo();
        }
        public override void setNo(long? aNo)
        {
            //throw new NotImplementedException();
            _mDepoKokSertifika.setKokSertifikaNo(aNo);
        }
        public override String getName()
        {
            throw new NotImplementedException();
        }
    }
}

