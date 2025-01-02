using System;
using System.Collections.Generic;
using System.Data.Common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.layer;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.util
{
    public class RsItemSource<T> : ItemSource<T> where T : class
    {
        private readonly DbDataReader _resultSet;
       
        //ModelNesneYardimci<T> _nesneYardimci;
        private readonly Islemler<T> _islemler; 

        public RsItemSource(DbDataReader aRs, Islemler<T> aIslemler )
        {
            //_nesneYardimci = aNesneYardimci;
            _resultSet = aRs;
            _islemler = aIslemler;
        }

        public bool open()
        {
            //reset();
            throw new NotSupportedException();
        }

        public bool close()
        {
            _resultSet.Dispose();
            return true;
        }

        public bool reset()
        {
            throw new NotSupportedException();
        }

        public T nextItem()
        {
            return _resultSet.Read() ? _islemler.nesneyiDoldur(_resultSet) : null;
        }

        public bool atEnd()
        {
            throw new NotSupportedException();
        }

        public List<T> toList()
        {
            List<T> tList = new List<T>();
            try
            {
                T t = nextItem();
                while (t != null)
                {
                    tList.Add(t);
                    t = nextItem();
                }
                return tList;
            }
            catch (Exception ex)
            {
                return null;
            }
        }
    }
}
