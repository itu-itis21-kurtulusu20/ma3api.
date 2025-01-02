using System;
using System.Collections.Generic;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.util;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer
{
    public abstract class Islemler<T> : ModelNesneYardimci<T> where T : class
    {
        #region IModelObject Members

        public abstract String tabloAdiAl();

        public abstract String[] sutunAdlariAl();

        public abstract void sorguyuDoldur(DbCommand aCommand);

        public abstract /*Object*/T nesneyiDoldur(DbDataReader aDataReader);

        public abstract long? getNo();
        public abstract void setNo(long? aNo);
        public abstract String getName();
        #endregion


        public String silmeSorgusu()
        {
            String[] alanlar = sutunAdlariAl();
            String idAlani = alanlar[alanlar.Length - 1];
            return "DELETE FROM " + tabloAdiAl() + " WHERE " + idAlani
                   + " = ? ";
        }

        public String guncellemeSorgusu()
        {
            String[] alanlar = sutunAdlariAl();
            String idAlani = alanlar[alanlar.Length - 1];

            String sorgu = "UPDATE " + tabloAdiAl() + " SET ";
            for (int i = 0; i < alanlar.Length - 1; i++)
            {
                sorgu += alanlar[i] + " = ? ";
                if (i < (alanlar.Length - 2)) sorgu += ", ";
            }
            sorgu += "WHERE " + idAlani + " = ? ";
            return sorgu;
        }
        public String eklemeSorgusu()
        {
            String[] alanlar = sutunAdlariAl();
            String sorgu = "INSERT INTO " + tabloAdiAl();
            String alanAdlari = "";
            String valuesKismi = "";

            for (int i = 0; i < alanlar.Length; i++)
            {
                alanAdlari += alanlar[i];
                valuesKismi += "?";
                if (i < (alanlar.Length - 1))
                {
                    alanAdlari += ", ";
                    valuesKismi += ", ";
                }
            }

            sorgu += " ( " + alanAdlari + " ) VALUES ( " + valuesKismi + " )";

            return sorgu;
        }

        public String yuklemeSorgusu()
        {
            //String[] alanlar = sutunAdlariAl();
            //String idAlani = alanlar[alanlar.Length - 1];

            //String sorgu = "SELECT ";

            //for (int i = 0; i < alanlar.Length; i++)
            //{
            //    sorgu += alanlar[i];
            //    if (i < (alanlar.Length - 1)) sorgu += ", ";
            //}

            //sorgu += " FROM " + tabloAdiAl() + " WHERE " + idAlani + " = ? ";

            //return sorgu;
            String[] alanlar = sutunAdlariAl();
            String idAlani = alanlar[alanlar.Length - 1];
            return yuklemeSorgusu(idAlani);
        }

        public String yuklemeSorgusu(String aAlanAdi)
        {
            String[] alanlar = sutunAdlariAl();
            //String idAlani = alanlar[alanlar.Length - 1];

            String sorgu = "SELECT ";

            for (int i = 0; i < alanlar.Length; i++)
            {
                sorgu += alanlar[i];
                if (i < (alanlar.Length - 1)) sorgu += ", ";
            }

            sorgu += " FROM " + tabloAdiAl();
            if (aAlanAdi != null)    //alan adi verilmemisse, WHERE clause eklenmesin
                sorgu += " WHERE " + aAlanAdi + " = ? ";

            return sorgu;
        }

        public /*Object*/T tekilSonuc(DbCommand aCommand, String aWhereClause, Object[] aParams)
        {
            ItemSource<T> liste = sorgula(aCommand, aWhereClause, aParams);
            try
            {
                T item = liste.nextItem();
                if (item == null)
                {
                    throw new ObjectNotFoundException(typeof (T).Name + " nesnesi veritabaninda bulunamadi");
                }
                return item;
            }
            finally
            {
                if (liste != null)
                    liste.close();
            }
            
        }

        public ItemSource</*Object*/T> sorgula(DbCommand aCommand, String aWhereClause, Object[] aParams)
        {
            ItemSource<T> result;
            String[] alanlar = sutunAdlariAl();
            String sorgu = "SELECT ";
            for (int i = 0; i < alanlar.Length; i++)
            {
                sorgu += alanlar[i];
                if (i < (alanlar.Length - 1)) sorgu += ", ";
            }
            sorgu += " FROM " + tabloAdiAl();
            if (aWhereClause != null)
            {
                sorgu += " WHERE " + aWhereClause;
                _parametreleriDoldur(aCommand, aParams);
            }
            aCommand.CommandText = sorgu;
            DbDataReader reader = aCommand.ExecuteReader();
            result = new RsItemSource<T>(reader, this);

            /*using (DbDataReader reader = aCommand.ExecuteReader())
            {
                while (reader.Read())
                {
                    T obj = nesneyiDoldur(reader);
                    sonuclar.Add(obj);
                }
            }*/
            return result;
        }


        public /*Object*/T yukle(DbCommand aCommand, long? aId)
        {
            String sorgu = yuklemeSorgusu();
            aCommand.CommandText = sorgu;
            DbParameter dizinNoParam = aCommand.CreateParameter();
            dizinNoParam.Value = aId;
            aCommand.Parameters.Add(dizinNoParam);
            T t;
            try
            {
                using (DbDataReader reader = aCommand.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        t = nesneyiDoldur(reader);
                        return t;
                    }
                    throw new ObjectNotFoundException(aId + " nolu " + typeof(T).Name + " nesnesi veritabaninda bulunamadi.");

                }
            }
            catch (DbException tEx)
            {
                throw new DatabaseException(typeof(T).ToString() + " yuklenmeye calisilirken hata olustu.", tEx);
            }
        }

        public void yaz(DbConnection aConnection)
        {
            String sorgu = (getNo() == null) ? eklemeSorgusu() : guncellemeSorgusu();
            try
            {
                using (DbCommand command = aConnection.CreateCommand())
                {
                    command.CommandText = sorgu;

                    sorguyuDoldur(command);
                    int res = command.ExecuteNonQuery();
                    using (DbCommand s = aConnection.CreateCommand())
                    {
                        s.CommandText = "select last_insert_rowid()";
                        Object rowId = s.ExecuteScalar();
                        //aDizin.setmDizinNo((long)rowId);
                        setNo((long?)rowId);
                    }
                }
            }
            catch (Exception tEx)
            {
                throw new DatabaseException(/*aDizin.getmDizinAdi()*/typeof(T).Name + " yazilmaya calisilirken hata olustu.", tEx);
            }
        }

        public int updateSorguCalistir(DbCommand aCommand, String aSetClause, String aWhereClause, Object[] aParams)
        {
            String sorgu = "UPDATE " + tabloAdiAl() + " SET " + aSetClause + " WHERE " + aWhereClause;
            _parametreleriDoldur(aCommand, aParams);
            aCommand.CommandText = sorgu;
            return aCommand.ExecuteNonQuery();
        }

        private static void _parametreleriDoldur(DbCommand aCommand, Object[] aParams)
        {
            if (aParams != null)
                foreach (Object param in aParams)
                {
                    DbParameter sorguParam = aCommand.CreateParameter();
                    sorguParam.Value = param;
                    aCommand.Parameters.Add(sorguParam);
                }
        }

        public int sil(DbCommand aCommand, long? aId)
        {
            String sorgu = silmeSorgusu();
            aCommand.CommandText = sorgu;
            DbParameter dizinNoParam = aCommand.CreateParameter();
            dizinNoParam.Value = aId;
            aCommand.Parameters.Add(dizinNoParam);
            return aCommand.ExecuteNonQuery();
        }

        public void insertSorguCalistir(DbCommand aCommand, String aFieldsClause, String aValuesClause, Object[] aParams)
        {
            String sorgu = "INSERT INTO " + tabloAdiAl() + aFieldsClause + " VALUES " + aValuesClause;
            _parametreleriDoldur(aCommand, aParams);
            aCommand.CommandText = sorgu;
            aCommand.ExecuteNonQuery();
        }

        public int deleteSorguCalistir(DbCommand aCommand, String aWhereClause, Object[] aParams)
        {
            String sorgu = "DELETE FROM " + tabloAdiAl() + " WHERE " + aWhereClause;
            _parametreleriDoldur(aCommand, aParams);
            aCommand.CommandText = sorgu;
            return aCommand.ExecuteNonQuery();
        }

        public bool selectSorguCalistir(DbCommand aCommand, String aFieldsClause, String aWhereClause, Object[] aParams)
        {
            String sorgu = "SELECT " + aFieldsClause + " FROM " + tabloAdiAl() + " WHERE " + aWhereClause;
            _parametreleriDoldur(aCommand, aParams);
            aCommand.CommandText = sorgu;
            return aCommand.ExecuteReader().HasRows;
        }
    }
}
