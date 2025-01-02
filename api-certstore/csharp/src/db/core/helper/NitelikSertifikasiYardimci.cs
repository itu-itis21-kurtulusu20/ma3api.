using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class NitelikSertifikasiYardimci : Islemler<DepoNitelikSertifikasi>
    {
        public static readonly String COLUMN_NITELIKSERTIFIKASI_ID = "NitelikSertifikasiNo";
	    public static readonly String COLUMN_SERTIFIKA_ID = "SertifikaNo";
	    public static readonly String COLUMN_NITELIKSERTIFIKASI_VALUE = "NitelikSertifikasiValue";
	    public static readonly String COLUMN_HOLDER_DNNAME = "HolderDNName";
	
	
	    public static readonly String NITELIKSERTIFIKASI_TABLO_ADI = "NITELIKSERTIFIKASI";
	
	    private static readonly String[] COLUMNS = new String[] 
	    {
		    COLUMN_SERTIFIKA_ID,
		    COLUMN_NITELIKSERTIFIKASI_VALUE,
		    COLUMN_HOLDER_DNNAME,
		    COLUMN_NITELIKSERTIFIKASI_ID
	    };
	
	    public override String tabloAdiAl()
	    {
		    return NITELIKSERTIFIKASI_TABLO_ADI;
	    }

	    public override String[] sutunAdlariAl()
	    {
		    return COLUMNS;
	    }

        private readonly DepoNitelikSertifikasi _mDepoNitelikSertifikasi;
        
        public NitelikSertifikasiYardimci(DepoNitelikSertifikasi aDepoNitelikSertifikasi)
        {
            _mDepoNitelikSertifikasi = aDepoNitelikSertifikasi;
        }
        public NitelikSertifikasiYardimci() { }
        public override void sorguyuDoldur(DbCommand aCommand)
	    {
		    DbParameter sertifikaNo = aCommand.CreateParameter();
            sertifikaNo.Value = _mDepoNitelikSertifikasi.getSertifikaNo();
            aCommand.Parameters.Add(sertifikaNo);

            DbParameter valueParam = aCommand.CreateParameter();
            valueParam.Value = _mDepoNitelikSertifikasi.getValue();
            aCommand.Parameters.Add(valueParam);

            DbParameter holderDNName = aCommand.CreateParameter();
            holderDNName.Value = _mDepoNitelikSertifikasi.getHolderDNName();
            aCommand.Parameters.Add(holderDNName);

            DbParameter nitelikSertifikaNo = aCommand.CreateParameter();
            nitelikSertifikaNo.Value = _mDepoNitelikSertifikasi.getNitelikSertifikaNo();
            aCommand.Parameters.Add(nitelikSertifikaNo);

	    }

        public override DepoNitelikSertifikasi nesneyiDoldur(DbDataReader aDataReader) 	    
	    {
		    DepoNitelikSertifikasi ns = new DepoNitelikSertifikasi();
                        		    
            ns.setSertifikaNo((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SERTIFIKA_ID)));
            ns.setValue(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_NITELIKSERTIFIKASI_VALUE)) as byte[]);
            ns.setHolderDNName(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_HOLDER_DNNAME)) as string);		    
            ns.setNitelikSertifikaNo((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_NITELIKSERTIFIKASI_ID)));
		    
            return ns;
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
