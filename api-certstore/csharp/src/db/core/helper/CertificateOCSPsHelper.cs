using System;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class CertificateOCSPsHelper : Islemler<DepoSertifikaOcsps>
    {
        public static readonly String COLUMN_OCSP_NO = "OCSPNo";
        public static readonly String COLUMN_SERTIFIKA_NO = "SertifikaNo";
        public static readonly String COLUMN_THIS_UPDATE = "ThisUpdate";
        public static readonly String COLUMN_STATUS = "Status";
        public static readonly String COLUMN_REVOCATION_TIME = "RevocationTime";
        public static readonly String COLUMN_REVOCATION_REASON = "RevocationReason";

        public static readonly String SERTIFIKA_OCSPS_TABLO_ADI = "SERTIFIKAOCSPS";

        public static readonly String[] COLUMNS = {
		    COLUMN_OCSP_NO,
		    COLUMN_SERTIFIKA_NO,
            COLUMN_THIS_UPDATE,
            COLUMN_STATUS,
            COLUMN_REVOCATION_TIME,
            COLUMN_REVOCATION_REASON
	    };

        public override String[] sutunAdlariAl()
        {
            return COLUMNS;
        }

        public override String tabloAdiAl()
        {
            return SERTIFIKA_OCSPS_TABLO_ADI;
        }
        public CertificateOCSPsHelper() { }


        public override void sorguyuDoldur(DbCommand aCommand)
        {
            throw new NotImplementedException("Not Implemented Yet");
        }

        public override DepoSertifikaOcsps nesneyiDoldur(DbDataReader aDataReader)
        {
            DepoSertifikaOcsps sertifikaOcsps = new DepoSertifikaOcsps();
            sertifikaOcsps.setOcspNo(aDataReader.GetInt64(aDataReader.GetOrdinal(COLUMN_OCSP_NO)));
            sertifikaOcsps.setSertifikaNo(aDataReader.GetInt64(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_NO)));
            sertifikaOcsps.setThisUpdate(aDataReader.GetDateTime(aDataReader.GetOrdinal(COLUMN_THIS_UPDATE)));
            sertifikaOcsps.setStatus(aDataReader.GetInt64(aDataReader.GetOrdinal(COLUMN_STATUS)));
            sertifikaOcsps.setRevocationTime(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_REVOCATION_TIME)) as DateTime?);
            sertifikaOcsps.setRevocationReason(aDataReader.GetInt64(aDataReader.GetOrdinal(COLUMN_REVOCATION_REASON)));
            return sertifikaOcsps;
        }

        public override long? getNo()
        {
            throw new NotImplementedException();
        }
        public override void setNo(long? aNo)
        {
            throw new NotImplementedException();
        }
        public override string getName()
        {
            throw new NotImplementedException();
        }
    }
}
