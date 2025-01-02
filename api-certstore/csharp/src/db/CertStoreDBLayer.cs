using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core;
using System.Data.Common;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db
{
    class CertStoreDBLayer
    {        
        private static String DEFAULT_PROVIDER_NAME = "System.Data.SQLite";
        private static String DEFAULT_CONNECTION_PARAMS = "DateTimeFormat=unixepochmillis";

        private static DbProviderFactory _mDbProviderFactory = DbProviderFactories.GetFactory(DEFAULT_PROVIDER_NAME);

        static CertStoreDBLayer()
        {

        }

        public static void setProviderProperties(String aProvName, String aConnectionParams)
        {
            DEFAULT_PROVIDER_NAME = aProvName;
            DEFAULT_CONNECTION_PARAMS = aConnectionParams;

            _mDbProviderFactory = DbProviderFactories.GetFactory(DEFAULT_PROVIDER_NAME);
        }

        public static void setProviderProperties(String aProvName)
        {
            setProviderProperties(aProvName, DEFAULT_CONNECTION_PARAMS);
        }

        public static DbConnection newConnection(String aDbName)
        {
            try
            {
                DbConnection connection = _mDbProviderFactory.CreateConnection();
                if ((DEFAULT_CONNECTION_PARAMS == null) || (DEFAULT_CONNECTION_PARAMS == ""))
                {
                    connection.ConnectionString = "Data Source=" + aDbName;
                }
                else
                {
                    connection.ConnectionString = "Data Source=" + aDbName + ";" + DEFAULT_CONNECTION_PARAMS;
                }
                connection.Open();
                return connection;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
                //throw;
            }
            return null;
        }
        public static DepoVEN newDepoVEN(DbConnection aConnection)
        {
            return new JDepoVEN(aConnection);
        }

    }
}
