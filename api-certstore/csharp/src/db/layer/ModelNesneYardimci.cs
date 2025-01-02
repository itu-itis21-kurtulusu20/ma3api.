using System;
using System.Data.Common;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer
{
    public interface ModelNesneYardimci<T>
    {
        String tabloAdiAl();

        String[] sutunAdlariAl();

        

        void sorguyuDoldur(DbCommand aCommand);

        /*Object*/T nesneyiDoldur(DbDataReader aDataReader);

        long? getNo();
        void setNo(long? aNo);
        String getName();
    }
}
