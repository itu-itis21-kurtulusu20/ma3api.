using System;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class OCSPHelper : Islemler<DepoOCSP>
    {
        public static readonly String COLUMN_OCSP_NO = "OCSPNo";
        public static readonly String COLUMN_OCSP_RESP_ID = "OCSPResponderID";
        public static readonly String COLUMN_OCSP_PRODUCED_AT = "OCSPProducedAt";
        public static readonly String COLUMN_OCSP_VALUE = "OCSPValue";

        public static readonly String OCSP_TABLO_ADI = "OCSP";

        private static readonly String[] COLUMNS = new String[] 
	    {
		    COLUMN_OCSP_RESP_ID,
		    COLUMN_OCSP_PRODUCED_AT,
		    COLUMN_OCSP_VALUE,
		    COLUMN_OCSP_NO
	    };
        public override String[] sutunAdlariAl()
        {
            return COLUMNS;
        }

        public override String tabloAdiAl()
        {
            return OCSP_TABLO_ADI;
        }

        private readonly DepoOCSP _mDepoOcsp;
        public OCSPHelper(DepoOCSP aDepoOcsp)
        {
            _mDepoOcsp = aDepoOcsp;
        }
        public OCSPHelper() { }
        public override void sorguyuDoldur(DbCommand aCommand)
        {
            DbParameter ocspResponderIDParam = aCommand.CreateParameter();
            ocspResponderIDParam.Value = _mDepoOcsp.getOCSPResponderID();
            aCommand.Parameters.Add(ocspResponderIDParam);

            DbParameter ocspProducedAtParam = aCommand.CreateParameter();
            ocspProducedAtParam.Value = _mDepoOcsp.getOCSPProducedAt();
            aCommand.Parameters.Add(ocspProducedAtParam);

            DbParameter ocspValueParam = aCommand.CreateParameter();
            ocspValueParam.Value = _mDepoOcsp.getBasicOCSPResponse();
            aCommand.Parameters.Add(ocspValueParam);

            DbParameter ocspNoParam = aCommand.CreateParameter();
            ocspNoParam.Value = _mDepoOcsp.getOCSPNo();
            aCommand.Parameters.Add(ocspNoParam);

        }
        public override /*Object*/DepoOCSP nesneyiDoldur(DbDataReader aDataReader)
        {
            DepoOCSP ocsp = new DepoOCSP();
            ocsp.setOCSPResponderID(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_OCSP_RESP_ID)) as byte[]);
            ocsp.setOCSPProducedAt(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_OCSP_PRODUCED_AT)) as DateTime?);
            ocsp.setBasicOCSPResponse(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_OCSP_VALUE)) as byte[]);
            ocsp.setOCSPNo((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_OCSP_NO)));
            return ocsp;
        }

        public override long? getNo()
        {
            //throw new NotImplementedException();
            return _mDepoOcsp.getOCSPNo();
        }
        public override void setNo(long? aNo)
        {
            //throw new NotImplementedException();
            _mDepoOcsp.setOCSPNo(aNo);
        }
        public override String getName()
        {
            throw new NotImplementedException();
        }
    }
}
