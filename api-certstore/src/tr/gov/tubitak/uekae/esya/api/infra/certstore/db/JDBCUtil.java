package tr.gov.tubitak.uekae.esya.api.infra.certstore.db;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;

import java.sql.Connection;

/**
 * @author ayetgin
 */
public class JDBCUtil
{
    public static void close(Connection aConnection) throws CertStoreException
    {
        if (aConnection!=null){
            try {
                aConnection.close();
            }
            catch (Exception x) {
                throw new CertStoreException("Kaynaklar kapatilmaya calisilirken hata olustu!", x);
            }
        }
    }

    public static void rollback(Connection aConnection) throws CertStoreException
    {
        try {
            aConnection.rollback();
        }
        catch (Exception x) {
            throw new CertStoreException("Kaynaklar kapatilmaya calisilirken hata olustu!", x);
        }
    }

    public static void commit(Connection aConnection) throws CertStoreException
    {
        try {
            aConnection.commit();
        }
        catch (Exception x) {
            throw new CertStoreException("Oturum commit edilmeye calisilirken hata olustu!", x);
        }
    }

}
