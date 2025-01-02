using System;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
namespace tr.gov.tubitak.uekae.esya.api.infra.db.certstore.core.helper
{   //todo template arguman olarak Object verilecek!
    public class DirectoryCRLHelper : Islemler<Object>
    {
        public static readonly String COLUMN_DIZINSIL_DIZIN_NO = "DizinNo";
        public static readonly String COLUMN_DIZINSIL_SIL_NO = "SILNo";

        public static readonly String DIZINSIL_TABLO_ADI = "DIZINSIL";

        private static readonly String[] COLUMNS = new String[] 
	    {
		    COLUMN_DIZINSIL_DIZIN_NO,
		    COLUMN_DIZINSIL_SIL_NO
	    };


        public override String tabloAdiAl()
        {
            return DIZINSIL_TABLO_ADI;
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
