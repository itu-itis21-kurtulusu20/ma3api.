using System;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class SilinecekKokSertifikaHelper : Islemler<DepoSilinecekKokSertifika>
    {
        public static readonly String COLUMN_SILINECEKKOKSERTIFIKA_DATE = "KOKEklenmeTarihi";
        public static readonly String COLUMN_SILINECEKKOKSERTIFIKA_VALUE = "KOKSertifikaValue";
        public static readonly String COLUMN_SILINECEKKOKSERTIFIKA_SERIAL = "KOKSerialNumber";
        public static readonly String COLUMN_SILINECEKKOKSERTIFIKA_ISSUER = "KOKIssuerName";
        public static readonly String COLUMN_SILINECEKKOKSERTIFIKA_SUBJECT = "KOKSubjectName";
        public static readonly String COLUMN_SILINECEKKOKSERTIFIKA_SATIR_IMZA = "KOKSatirImzasi";
        public static readonly String COLUMN_SILINECEKKOKSERTIFIKA_NO = "KOKSertifikaNo";

        public static readonly String SILINECEKKOKSERTIFIKA_TABLO_ADI = "SILINECEKKOKSERTIFIKA";

        public static readonly String[] COLUMNS = 
	    {
		    COLUMN_SILINECEKKOKSERTIFIKA_DATE,
		    COLUMN_SILINECEKKOKSERTIFIKA_VALUE,
		    COLUMN_SILINECEKKOKSERTIFIKA_SERIAL,
		    COLUMN_SILINECEKKOKSERTIFIKA_ISSUER,
		    COLUMN_SILINECEKKOKSERTIFIKA_SUBJECT,
		    COLUMN_SILINECEKKOKSERTIFIKA_SATIR_IMZA,
		    COLUMN_SILINECEKKOKSERTIFIKA_NO
	    };

        public override String tabloAdiAl()
        {
            return SILINECEKKOKSERTIFIKA_TABLO_ADI;
        }

        public override String[] sutunAdlariAl()
        {
            return COLUMNS;
        }

        private readonly DepoSilinecekKokSertifika _mDepoSilinecekKokSertfika;
        public SilinecekKokSertifikaHelper(DepoSilinecekKokSertifika aDepoSilinecekKokSertifika)
        {
            _mDepoSilinecekKokSertfika = aDepoSilinecekKokSertifika;
        }
        public SilinecekKokSertifikaHelper() { }

        public override void sorguyuDoldur(DbCommand aCommand)
        {
            
            DbParameter eklenmeDateParam = aCommand.CreateParameter();
            DateTime? eklenmeDate = _mDepoSilinecekKokSertfika.getKokEklenmeTarihi();
            if (eklenmeDate != null)
                eklenmeDateParam.Value = eklenmeDate;
            else
                eklenmeDateParam.Value = DateTime.UtcNow;
            aCommand.Parameters.Add(eklenmeDateParam);

            DbParameter valueParam = aCommand.CreateParameter();
            valueParam.Value = _mDepoSilinecekKokSertfika.getValue();
            aCommand.Parameters.Add(valueParam);

            DbParameter serialNumberParam = aCommand.CreateParameter();
            serialNumberParam.Value = _mDepoSilinecekKokSertfika.getSerialNumber();
            aCommand.Parameters.Add(serialNumberParam);

            DbParameter issuerNameParam = aCommand.CreateParameter();
            issuerNameParam.Value = _mDepoSilinecekKokSertfika.getIssuerName();
            aCommand.Parameters.Add(issuerNameParam);

            DbParameter subjectNameParam = aCommand.CreateParameter();
            subjectNameParam.Value = _mDepoSilinecekKokSertfika.getSubjectName();
            aCommand.Parameters.Add(subjectNameParam);

            DbParameter satirImzasiParam = aCommand.CreateParameter();
            satirImzasiParam.Value = _mDepoSilinecekKokSertfika.getSatirImzasi();
            aCommand.Parameters.Add(satirImzasiParam);

            DbParameter kokSertifikaNo = aCommand.CreateParameter();
            kokSertifikaNo.Value = _mDepoSilinecekKokSertfika.getKokSertifikaNo();
            aCommand.Parameters.Add(kokSertifikaNo);

        }
        public override /*Object*/DepoSilinecekKokSertifika nesneyiDoldur(DbDataReader aDataReader)
        {
            throw new NotImplementedException();
        }

        public override long? getNo()
        {
            return _mDepoSilinecekKokSertfika.getKokSertifikaNo();            
        }
        public override void setNo(long? aNo)
        {
            _mDepoSilinecekKokSertfika.setKokSertifikaNo(aNo);
           
        }
        public override String getName()
        {
            throw new NotImplementedException();
        }
    }
}
