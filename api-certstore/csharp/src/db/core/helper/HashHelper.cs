using System;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class HashHelper : Islemler<DepoOzet>
    {
        public static readonly String COLUMN_HASH_NO = "HashNo";
        public static readonly String COLUMN_OBJECT_TYPE = "ObjectType";
        public static readonly String COLUMN_OBJECT_NO = "ObjectNo";
        public static readonly String COLUMN_HASH_TYPE = "HashType";
        public static readonly String COLUMN_HASH_VALUE = "HashValue";

        public static readonly String OZET_TABLO_ADI = "HASH";

        public static readonly String[] COLUMNS = 
	    {
		    COLUMN_OBJECT_TYPE,
		    COLUMN_OBJECT_NO,
		    COLUMN_HASH_TYPE,
		    COLUMN_HASH_VALUE,
		    COLUMN_HASH_NO
	    };
        public override String[] sutunAdlariAl()
        {
            return COLUMNS;
        }

        public override String tabloAdiAl()
        {
            return OZET_TABLO_ADI;
        }

        private readonly DepoOzet _mDepoOzet;
        public HashHelper(DepoOzet aDepoOzet)
        {
            _mDepoOzet = aDepoOzet;
        }
        public HashHelper() { }
        public override void sorguyuDoldur(DbCommand aCommand)
        {            
            DbParameter objectTypeParam = aCommand.CreateParameter();
            objectTypeParam.Value = _mDepoOzet.getObjectType();
            aCommand.Parameters.Add(objectTypeParam);

            DbParameter objectNoParam = aCommand.CreateParameter();
            objectNoParam.Value = _mDepoOzet.getObjectNo();
            aCommand.Parameters.Add(objectNoParam);

            DbParameter hashTypeParam = aCommand.CreateParameter();
            hashTypeParam.Value = _mDepoOzet.getHashType();
            aCommand.Parameters.Add(hashTypeParam);

            DbParameter hashValueParam = aCommand.CreateParameter();
            hashValueParam.Value = _mDepoOzet.getHashValue();
            aCommand.Parameters.Add(hashValueParam);

            DbParameter hashNoParam = aCommand.CreateParameter();
            hashNoParam.Value = _mDepoOzet.getHashNo();
            aCommand.Parameters.Add(hashNoParam);            
        }
        public override /*Object*/DepoOzet nesneyiDoldur(DbDataReader aDataReader)
        {
            DepoOzet ozet = new DepoOzet();
            ozet.setObjectType(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_OBJECT_TYPE)) as long?);
            ozet.setObjectNo(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_OBJECT_NO)) as long?);
            ozet.setHashType((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_HASH_TYPE)));
            ozet.setHashValue(aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_HASH_VALUE)) as byte[]);
            ozet.setHashNo((long)aDataReader.GetValue(aDataReader.GetOrdinal(COLUMN_HASH_NO)));
            return ozet;
        }

        public override long? getNo()
        {
            //throw new NotImplementedException();
            return _mDepoOzet.getHashNo();
        }
        public override void setNo(long? aNo)
        {
            //throw new NotImplementedException();
            _mDepoOzet.setHashNo(aNo);
        }
        public override String getName()
        {
            throw new NotImplementedException();
        }
    }
}
