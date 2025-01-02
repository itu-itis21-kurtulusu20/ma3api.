using System;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class SistemParametresiHelper : Islemler<DepoSistemParametresi>
    {
        public static readonly String COLUMN_SP_PARAM_ADI = "ParametreAdi";
        public static readonly String COLUMN_SP_PARAM_DEGERI = "ParametreDegeri";

        public static readonly String SP_TABLO_ADI = "SistemParametreleri";

        private static readonly String[] COLUMNS = 
	    {
		    COLUMN_SP_PARAM_ADI,
		    COLUMN_SP_PARAM_DEGERI
	    };

        private readonly DepoSistemParametresi _mDepoSistemParametresi;
        public SistemParametresiHelper(DepoSistemParametresi aDepoSistemParametresi)
        {
            _mDepoSistemParametresi = aDepoSistemParametresi;
        }
        public SistemParametresiHelper() { }

        public override String[] sutunAdlariAl()
        {
            return COLUMNS;
        }

        public override String tabloAdiAl()
        {
            return SP_TABLO_ADI;
        }

        public override void sorguyuDoldur(DbCommand aCommand)
        {            
            DbParameter paramAdiParam = aCommand.CreateParameter();
            paramAdiParam.Value = _mDepoSistemParametresi.getParamAdi();
            aCommand.Parameters.Add(paramAdiParam);

            DbParameter paramDegeriParam = aCommand.CreateParameter();
            paramDegeriParam.Value = _mDepoSistemParametresi.getParamDegeri();
            aCommand.Parameters.Add(paramDegeriParam);
        }

        public override /*Object*/DepoSistemParametresi nesneyiDoldur(DbDataReader aDataReader)
        {
            DepoSistemParametresi sp = new DepoSistemParametresi();
            sp.setParamAdi(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SP_PARAM_ADI)) as String);
            sp.setParamDegeri(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_SP_PARAM_DEGERI)));
            return sp;
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
