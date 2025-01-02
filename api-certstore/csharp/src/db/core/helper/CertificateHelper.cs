using System;
using System.Data.Common;
using System.Globalization;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class CertificateHelper : Islemler<DepoSertifika>
    {
        public static readonly String COLUMN_SERTIFIKA_DATE = "EklenmeTarihi";
        public static readonly String COLUMN_SERTIFIKA_VALUE = "SertifikaValue";
        //public static final String COLUMN_SERTIFIKA_HASH_NO = "HashNo";
        public static readonly String COLUMN_SERTIFIKA_SERIAL = "SerialNumber";
        public static readonly String COLUMN_SERTIFIKA_ISSUER = "IssuerName";
        public static readonly String COLUMN_SERTIFIKA_START_DATE = "StartDate";
        public static readonly String COLUMN_SERTIFIKA_END_DATE = "EndDate";
        public static readonly String COLUMN_SERTIFIKA_SUBJECT = "SubjectName";
        public static readonly String COLUMN_SERTIFIKA_SUBJECT_STR = "SubjectNameStr";
        public static readonly String COLUMN_SERTIFIKA_KEYUSAGE = "KeyUsageStr";
        public static readonly String COLUMN_SERTIFIKA_SUBJECT_KEY_ID = "SubjectKeyIdentifier";
        public static readonly String COLUMN_SERTIFIKA_EPOSTA = "EPosta";
        public static readonly String COLUMN_SERTIFIKA_PRIVATE_KEY = "PrivateKeyValue";
        public static readonly String COLUMN_SERTIFIKA_PKCS11_LIB = "PKCS11Lib";
        public static readonly String COLUMN_SERTIFIKA_PKCS11_ID = "PKCS11Id";
        public static readonly String COLUMN_SERTIFIKA_NO = "SertifikaNo";
        public static readonly String COLUMN_SERTIFIKA_CARD_SERIAL_NUMBER = "CardSerialNumber";
        public static readonly String COLUMN_SERTIFIKA_X400ADDRESS = "X400Address";

        public static readonly CultureInfo EN_LOCALE = new CultureInfo("en");

        public static readonly String SERTIFIKA_TABLO_ADI = "SERTIFIKA";

        private static readonly String[] COLUMNS = new String[] 
	    {
		    COLUMN_SERTIFIKA_DATE,
		    COLUMN_SERTIFIKA_VALUE,
		    //COLUMN_SERTIFIKA_HASH_NO,
		    COLUMN_SERTIFIKA_SERIAL,
		    COLUMN_SERTIFIKA_ISSUER,
		    COLUMN_SERTIFIKA_START_DATE,
		    COLUMN_SERTIFIKA_END_DATE,
		    COLUMN_SERTIFIKA_SUBJECT,
            COLUMN_SERTIFIKA_SUBJECT_STR,
		    COLUMN_SERTIFIKA_KEYUSAGE,
		    COLUMN_SERTIFIKA_SUBJECT_KEY_ID,
		    COLUMN_SERTIFIKA_EPOSTA,
		    COLUMN_SERTIFIKA_PRIVATE_KEY,
		    COLUMN_SERTIFIKA_PKCS11_LIB,
		    COLUMN_SERTIFIKA_PKCS11_ID,		    
            COLUMN_SERTIFIKA_CARD_SERIAL_NUMBER,
            COLUMN_SERTIFIKA_X400ADDRESS,
            COLUMN_SERTIFIKA_NO
	    };


        public override String tabloAdiAl()
        {
            return SERTIFIKA_TABLO_ADI;
        }


        public override String[] sutunAdlariAl()
        {
            return COLUMNS;
        }

        private readonly DepoSertifika _mDepoSertifika;
        public CertificateHelper(DepoSertifika aDepoSertifika)
        {
            _mDepoSertifika = aDepoSertifika;
        }

        public CertificateHelper() { }
        public override void sorguyuDoldur(DbCommand aCommand)
        {
            DbParameter eklenmeTarihiParam = aCommand.CreateParameter();
            DateTime? eklenmeTarihi = _mDepoSertifika.getEklenmeTarihi();
            if (eklenmeTarihi != null)
                eklenmeTarihiParam.Value = eklenmeTarihi;
            else
                eklenmeTarihiParam.Value = DateTime.UtcNow;
            aCommand.Parameters.Add(eklenmeTarihiParam);

            DbParameter valueParam = aCommand.CreateParameter();
            valueParam.Value = _mDepoSertifika.getValue();
            aCommand.Parameters.Add(valueParam);

            DbParameter serialNumber = aCommand.CreateParameter();
            serialNumber.Value = _mDepoSertifika.getSerialNumber();
            aCommand.Parameters.Add(serialNumber);

            DbParameter normalizedIssuerNameParam = aCommand.CreateParameter();
            normalizedIssuerNameParam.Value = _mDepoSertifika.getNormalizedIssuerName();
            aCommand.Parameters.Add(normalizedIssuerNameParam);

            DbParameter startDateParam = aCommand.CreateParameter();
            startDateParam.Value = _mDepoSertifika.getStartDate();
            aCommand.Parameters.Add(startDateParam);

            DbParameter endDateParam = aCommand.CreateParameter();
            endDateParam.Value = _mDepoSertifika.getEndDate();
            aCommand.Parameters.Add(endDateParam);

            DbParameter normalizedSubjectNameParam = aCommand.CreateParameter();
            normalizedSubjectNameParam.Value = _mDepoSertifika.getNormalizedSubjectName();
            aCommand.Parameters.Add(normalizedSubjectNameParam);

            DbParameter subjectNameStrParam = aCommand.CreateParameter();
            subjectNameStrParam.Value = _mDepoSertifika.getSubjectNameStr();
            aCommand.Parameters.Add(subjectNameStrParam);

            DbParameter keyUsageStrParam = aCommand.CreateParameter();
            keyUsageStrParam.Value = _mDepoSertifika.getKeyUsageStr();
            aCommand.Parameters.Add(keyUsageStrParam);

            DbParameter subjectKeyIDParam = aCommand.CreateParameter();
            subjectKeyIDParam.Value = _mDepoSertifika.getSubjectKeyID();
            aCommand.Parameters.Add(subjectKeyIDParam);

            DbParameter epostaParam = aCommand.CreateParameter();
            epostaParam.Value = (_mDepoSertifika.getEPosta() == null) ? null : _mDepoSertifika.getEPosta().ToLower(EN_LOCALE);
            aCommand.Parameters.Add(epostaParam);

            DbParameter privateKeyParam = aCommand.CreateParameter();
            privateKeyParam.Value = _mDepoSertifika.getPrivateKey();
            aCommand.Parameters.Add(privateKeyParam);

            DbParameter pkcs11LibParam = aCommand.CreateParameter();
            pkcs11LibParam.Value = _mDepoSertifika.getPKCS11Lib();
            aCommand.Parameters.Add(pkcs11LibParam);

            DbParameter pkcs11IDParam = aCommand.CreateParameter();
            pkcs11IDParam.Value = _mDepoSertifika.getPKCS11ID();
            aCommand.Parameters.Add(pkcs11IDParam);

            DbParameter cardSerialNumberParam = aCommand.CreateParameter();
            cardSerialNumberParam.Value = _mDepoSertifika.getCardSerialNumber();
            aCommand.Parameters.Add(cardSerialNumberParam);

            DbParameter x400Address = aCommand.CreateParameter();
            x400Address.Value = _mDepoSertifika.getX400Address();
            aCommand.Parameters.Add(x400Address);

            DbParameter sertifikaNoParam = aCommand.CreateParameter();
            sertifikaNoParam.Value = _mDepoSertifika.getSertifikaNo();
            aCommand.Parameters.Add(sertifikaNoParam);        
        }
        public override /*Object*/DepoSertifika nesneyiDoldur(DbDataReader aDataReader)
        {
            DepoSertifika sertifika = new DepoSertifika();
            sertifika.setEklenmeTarihi((DateTime?)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_DATE)));
            sertifika.setValue(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_VALUE)) as byte[]);
            //sertifika.setmHashNo(aRS.getLong(COLUMN_SERTIFIKA_HASH_NO));
            sertifika.setSerialNumber(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_SERIAL)) as byte[]);
            sertifika.setIssuerName(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_ISSUER)) as byte[]);
            //TODO date index ler degisecek
            sertifika.setStartDate(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_START_DATE)) as DateTime?);
            sertifika.setEndDate(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_END_DATE)) as DateTime?);
            sertifika.setSubjectName(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_SUBJECT)) as byte[]);
            sertifika.setSubjectNameStr(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_SUBJECT_STR)) as string);
            sertifika.setKeyUsageStr(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_KEYUSAGE)) as string);

            String eposta = aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_EPOSTA)) as string;
            if (eposta != null)
                eposta = eposta.ToLower(EN_LOCALE);
            sertifika.setEPosta(eposta);
            sertifika.setPrivateKey(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_PRIVATE_KEY)) as byte[]);
            sertifika.setPKCS11Lib(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_PKCS11_LIB)) as string);
            sertifika.setPKCS11ID(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_PKCS11_ID)) as byte[]);
            sertifika.setCardSerialNumber(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_CARD_SERIAL_NUMBER)) as string);
            sertifika.setX400Address(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_X400ADDRESS)) as string);         
            sertifika.setSertifikaNo((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_NO)));
            sertifika.setSubjectKeyID(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_SUBJECT_KEY_ID)) as byte[]);
            return sertifika;

        }


        public override long? getNo()
        {
            //throw new NotImplementedException();
            return _mDepoSertifika.getSertifikaNo();
        }
        public override void setNo(long? aNo)
        {
            //throw new NotImplementedException();
            _mDepoSertifika.setSertifikaNo(aNo);
        }
        public override string getName()
        {
            throw new NotImplementedException();
        }
    }
}
