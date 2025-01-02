using System;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class DirectoryHelper : Islemler<DepoDizin>
    {
        public static readonly String COLUMN_DIZIN_AD = "DIZINADI";
        public static readonly String COLUMN_DIZIN_EKLENME_TARIHI = "EKLENMETARIHI";
        public static readonly String COLUMN_DIZIN_NO = "DIZINNO";

        public static readonly String DIZIN_TABLO_ADI = "DIZIN";

        private static readonly long DEFAULT_DIZIN_NO = 1;


        private static readonly String[] COLUMNS = new String[] 
        {
            COLUMN_DIZIN_EKLENME_TARIHI,
            COLUMN_DIZIN_AD,
            COLUMN_DIZIN_NO
        };

        public override String tabloAdiAl()
        {
            return DIZIN_TABLO_ADI;
        }

        public override String[] sutunAdlariAl()
        {
            return COLUMNS;
        }

        public static long getDefaultDizinNo()
        {
            return DEFAULT_DIZIN_NO;
        }

        private readonly DepoDizin _mDepoDizin;
        public DirectoryHelper(DepoDizin aDepoDizin)
        {
            _mDepoDizin = aDepoDizin;
        }
        public DirectoryHelper()
        {
        }

        public override void sorguyuDoldur(DbCommand aDbCommand)
        {

            DbParameter eklenmeParam = aDbCommand.CreateParameter();
            DateTime? eklenmeDate = _mDepoDizin.getEklenmeTarihi();
            if (eklenmeDate != null){                
                eklenmeParam.Value = eklenmeDate;                
            }
            else{
                eklenmeParam.Value = DateTime.UtcNow;                
            }
            aDbCommand.Parameters.Add(eklenmeParam);

            DbParameter dizinAdiParam = aDbCommand.CreateParameter();
            dizinAdiParam.Value = _mDepoDizin.getDizinAdi();
            aDbCommand.Parameters.Add(dizinAdiParam);
            DbParameter dizinNoParam = aDbCommand.CreateParameter();
            dizinNoParam.Value = _mDepoDizin.getDizinNo();
            aDbCommand.Parameters.Add(dizinNoParam);            
        }

        public override /*Object*/DepoDizin nesneyiDoldur(DbDataReader aDataReader)
        {
            DepoDizin dizin = new DepoDizin();
            String dizinAdi = aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_DIZIN_AD)) as String;
            dizin.setDizinAdi(dizinAdi);
            DateTime? eklenmeTarihi = aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_DIZIN_EKLENME_TARIHI)) as DateTime?;
            dizin.setEklenmeTarihi(eklenmeTarihi);
            long dizinNo = (long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_DIZIN_NO));
            dizin.setDizinNo(dizinNo);
            return dizin;           
        }

        public override long? getNo()
        {
            if (_mDepoDizin == null)
                throw new NullReferenceException("DepoDizin initialize edilmedi");
            return _mDepoDizin.getDizinNo();
        }
        public override void setNo(long? aNo)
        {
            if(_mDepoDizin == null)
                throw new NullReferenceException("DepoDizin initialize edilmedi");
            _mDepoDizin.setDizinNo(aNo);
        }
        public override String getName()
        {
            if(_mDepoDizin == null)
                throw new NullReferenceException("DepoDizin initialize edilmedi");
            return _mDepoDizin.getDizinAdi();
        }
    }
}
