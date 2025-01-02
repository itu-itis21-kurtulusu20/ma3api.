using System;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class CRLHelper : Islemler<DepoSIL>
    {
        public static readonly string COLUMN_SIL_DATE = "EklenmeTarihi";
        public static readonly string COLUMN_SIL_VALUE = "SILValue";
        //public static readonls String COLUMN_SIL_HASH = "SILHash";
        public static readonly string COLUMN_SIL_ISSUER_NAME = "IssuerName";
        public static readonly string COLUMN_SIL_THIS_UPDATE = "ThisUpdate";
        public static readonly string COLUMN_SIL_NEXT_UPDATE = "NextUpdate";
        public static readonly string COLUMN_SIL_NUMBER = "SILNumber";
        public static readonly string COLUMN_SIL_BASE_SIL_NUMBER = "BaseSILNumber";
        public static readonly string COLUMN_SIL_TIPI = "SILTipi";
        public static readonly string COLUMN_SIL_NO = "SILNo";
                               
        public static readonly string SIL_TABLO_ADI = "SIL";

        private static readonly string[] COLUMNS = 
	    {
		    COLUMN_SIL_DATE,
		    COLUMN_SIL_VALUE,
		    //COLUMN_SIL_HASH,
		    COLUMN_SIL_ISSUER_NAME,
		    COLUMN_SIL_THIS_UPDATE,
		    COLUMN_SIL_NEXT_UPDATE,
		    COLUMN_SIL_NUMBER,
		    COLUMN_SIL_BASE_SIL_NUMBER,
		    COLUMN_SIL_TIPI,
		    COLUMN_SIL_NO
	    };
        public override String[] sutunAdlariAl()
        {
            return COLUMNS;
        }

        public override String tabloAdiAl()
        {
            return SIL_TABLO_ADI;
        }

        private readonly DepoSIL _mDepoSIL;
        public CRLHelper(DepoSIL aDepoSIL)
        {
            _mDepoSIL = aDepoSIL;
        }
        public CRLHelper() { }
        public override void sorguyuDoldur(DbCommand aCommand)
        {
            
            DbParameter eklenmeTarihiParam = aCommand.CreateParameter();
            DateTime? eklenmeTarihi = _mDepoSIL.getEklenmeTarihi();
            if (eklenmeTarihi != null)
                eklenmeTarihiParam.Value = eklenmeTarihi;
            else
                eklenmeTarihiParam.Value = DateTime.UtcNow;            
            aCommand.Parameters.Add(eklenmeTarihiParam);

            DbParameter valueParam = aCommand.CreateParameter();
            valueParam.Value = _mDepoSIL.getValue();
            aCommand.Parameters.Add(valueParam);

            DbParameter issuerNameParam = aCommand.CreateParameter();
            issuerNameParam.Value = _mDepoSIL.getIssuerName();
            aCommand.Parameters.Add(issuerNameParam);

            DbParameter thisUpdateParam = aCommand.CreateParameter();
            thisUpdateParam.Value = _mDepoSIL.getThisUpdate();
            aCommand.Parameters.Add(thisUpdateParam);

            DbParameter nextUpdateParam = aCommand.CreateParameter();
            nextUpdateParam.Value = _mDepoSIL.getNextUpdate();
            aCommand.Parameters.Add(nextUpdateParam);

            DbParameter silNumber = aCommand.CreateParameter();
            silNumber.Value = _mDepoSIL.getSILNumber();
            aCommand.Parameters.Add(silNumber);

            DbParameter baseSILNumber = aCommand.CreateParameter();
            baseSILNumber.Value = _mDepoSIL.getBaseSILNumber();
            aCommand.Parameters.Add(baseSILNumber);

            DbParameter silTipi = aCommand.CreateParameter();
            silTipi.Value = _mDepoSIL.getSILTipi().getIntValue();
            aCommand.Parameters.Add(silTipi);

            DbParameter silNo = aCommand.CreateParameter();
            silNo.Value = _mDepoSIL.getSILNo();
            aCommand.Parameters.Add(silNo);           
            
        }
        public override /*Object*/DepoSIL nesneyiDoldur(DbDataReader aDataReader)
        {
            DepoSIL sil = new DepoSIL();
            sil.setEklenmeTarihi(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SIL_DATE)) as DateTime?);
            sil.setValue(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SIL_VALUE)) as byte[]);
            //sil.setmHash(aRS.getBytes(COLUMN_SIL_HASH));
            sil.setIssuerName(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SIL_ISSUER_NAME)) as byte[]);
            sil.setThisUpdate(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SIL_THIS_UPDATE)) as DateTime?);
            sil.setNextUpdate(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SIL_NEXT_UPDATE)) as DateTime?);
            sil.setSILNumber(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SIL_NUMBER)) as byte[]);
            sil.setBaseSILNumber(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SIL_BASE_SIL_NUMBER)) as byte[]);
            sil.setSILTipi(CRLType.getNesne((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SIL_TIPI))));
            sil.setSILNo((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SIL_NO)));
            return sil;
        }
        public override long? getNo()
        {
            //throw new NotImplementedException();
            return _mDepoSIL.getSILNo();
        }
        public override void setNo(long? aNo)
        {
            //throw new NotImplementedException();
            _mDepoSIL.setSILNo(aNo);
        }
        public override String getName()
        {
            throw new NotImplementedException();
        }
    }
}
