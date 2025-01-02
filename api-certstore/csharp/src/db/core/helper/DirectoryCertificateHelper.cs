using System;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    //todo template arguman olarak Object verilecek!
    public class DirectoryCertificateHelper : Islemler<Object>
    {
        public static readonly String COLUMN_DIZINSERTIFIKA_DIZIN_NO = "DizinNo";
        public static readonly String COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO = "SertifikaNo";

        public static readonly String DIZINSERTIFIKA_TABLO_ADI = "DIZINSERTIFIKA";

        private static readonly String[] COLUMNS = new String[] 
	    {
		    COLUMN_DIZINSERTIFIKA_DIZIN_NO,
		    COLUMN_DIZINSERTIFIKA_SERTIFIKA_NO
	    };

        public override String tabloAdiAl()
        {
            return DIZINSERTIFIKA_TABLO_ADI;
        }

        public override String[] sutunAdlariAl()
        {
            return COLUMNS;
        }

        public override void sorguyuDoldur(DbCommand aCommand)
        {
            throw new NotImplementedException();
        }
        public override Object nesneyiDoldur(DbDataReader aDataReader)
        {
            throw new NotImplementedException();
        }

        public override long? getNo()
        {
            throw new NotImplementedException();
        }
        public override void setNo(long? aNo)
        {
            throw new NotImplementedException();
        }
        public override String getName()
        {
            throw new NotImplementedException();
        }
    }
}
