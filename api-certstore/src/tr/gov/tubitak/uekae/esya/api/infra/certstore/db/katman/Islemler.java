package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman;


import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.NotFoundException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.util.RsItemSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ahmety
 */
public abstract class Islemler
{
    protected static Logger logger = LoggerFactory.getLogger(Islemler.class);

    private Map<Class, ModelNesneYardimci> mYardimcilar = new HashMap<Class, ModelNesneYardimci>();

    protected void register(Class aClass, ModelNesneYardimci aYardimci)
    {
        mYardimcilar.put(aClass, aYardimci);
    }

    private ModelNesneYardimci yardimciyiAl(Class aClass)
    {
        return mYardimcilar.get(aClass);
    }

    @SuppressWarnings("unchecked")
    public <T> T yukle(Connection conn, Class<T> aClass, Long aId)
            throws NotFoundException, CertStoreException
    {
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String sorgu = yuklemeSorgusu(aClass);

            ps = conn.prepareStatement(sorgu);
            ps.setLong(1, aId);
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotFoundException(aId + " nolu " + aClass.getName() + " nesnesi veritabaninda bulunamadi.");
            }
            return (T)yardimciyiAl(aClass).nesneyiDoldur(rs);
        }
        catch (SQLException tEx) {
            throw new CertStoreException(aClass.getName() + "(" + aId + ") yuklenmeye calisilirken hata olustu.", tEx);
        }
        finally {
            kaynaklariKapat(rs, ps);
        }
    }

    public int sil(Connection conn, Class aClass, Long aId)
            throws CertStoreException
    {
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String sorgu = silmeSorgusu(aClass);

            ps = conn.prepareStatement(sorgu);
            ps.setLong(1, aId);
            return ps.executeUpdate();
        }
        catch (Exception tEx) {
            throw new CertStoreException(aClass.getName() + "(" + aId + ") silinmeye calisilirken hata olustu.", tEx);
        }
        finally {
            kaynaklariKapat(rs, ps);
        }
    }

    protected abstract void setVtId(Object aModelNesnesi, Long aId);
    protected abstract Long getVtId(Object aModelNesnesi);

    // bu metod SQLite için calisir. Diger VT ler icin gerekiyorsa
    // gelistirlmeli. Ornegin idler auto increment kabul ediliyor ve buna
    // fetch var...
    public void yaz(Connection conn, Object aNesne)
            throws CertStoreException
    {
        boolean aYeniNesne = (getVtId(aNesne)==null);
        ResultSet rs = null;
        PreparedStatement ps = null;
        Class clazz = aNesne.getClass();
        try {

            String sorgu = aYeniNesne ? eklemeSorgusu(clazz) : guncellemeSorgusu(clazz);
            ps = conn.prepareStatement(sorgu);
            ModelNesneYardimci yardimci = yardimciyiAl(clazz);
            yardimci.sorguyuDoldur(aNesne, ps);
            ps.executeUpdate();

            PreparedStatement s = null;
            ResultSet rsId = null;
            try {
                s = conn.prepareStatement("select last_insert_rowid()");
                rsId = s.executeQuery();
                rsId.next();
            long id = rsId.getLong(1);
                setVtId(aNesne, id);
            } finally {
                kaynaklariKapat(rsId, s);
            }
            return;
        }
        catch (Exception t) {
            throw new CertStoreException(clazz.getName() + " yazilmaya calisilirken hata olustu.", t);
        }
        finally {
            kaynaklariKapat(rs, ps);
        }
    }

    public <T> T tekilSonuc(Connection aOturum, Class aClass, String aWhereClause, Object[] aParams)
            throws CertStoreException {
        ItemSource<T> liste = sorgula(aOturum, aClass, aWhereClause, aParams);
        try{
            T item = liste.nextItem();
            if(item == null)
            {
               throw new NotFoundException(aClass.getName() + " nesnesi veritabaninda bulunamadi");
            }
            return item;
        }
        catch (ESYAException e)
        {
             throw new CertStoreException(e);
        }
        finally {
             if(liste != null)
                liste.close();
        }
    }

    public <T> ItemSource<T> sorgula(Connection conn, Class aClass, String aWhereClause, Object[] aParams)
            throws CertStoreException
    {
        ItemSource<T> sonuclar;
        ModelNesneYardimci yardimci = yardimciyiAl(aClass);

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {

            String[] alanlar = yardimci.sutunAdlariAl();
            String sorgu = "SELECT ";
            for (int i = 0; i < alanlar.length; i++) {
                sorgu += alanlar[i];
                if (i < (alanlar.length - 1)) sorgu += ", ";
            }
            sorgu += " FROM " + yardimci.tabloAdiAl() + " WHERE " + aWhereClause;


            ps = conn.prepareStatement(sorgu);
            _parametreleriDoldur(ps, aParams);
            rs = ps.executeQuery();
            sonuclar = new RsItemSource<T>(yardimci, rs);
            /*while (rs.next()) {
                Object nesne = yardimci.nesneyiDoldur(rs);
                sonuclar.add(nesne);
            } */
        }
        catch (Exception tEx) {
            throw new CertStoreException("Sorgulama sirasinda hata olustu", tEx);
        }
        /*finally {
            kaynaklariKapat(ps);
        }*/

        return sonuclar;
    }

    private void kaynaklariKapat(ResultSet aRS, PreparedStatement aPS) throws CertStoreException
    {
        try {
            if (aPS != null) aPS.close();
            if (aRS != null) aRS.close();
        }
        catch (Exception x) {
            throw new CertStoreException("Kaynaklar kapatilmaya calisilirken hata olustu!", x);
        }
    }

    private void kaynaklariKapat(PreparedStatement aPS) throws CertStoreException
    {
        try {
            if (aPS != null) aPS.close();
        }
        catch (Exception x) {
            throw new CertStoreException("Kaynaklar kapatilmaya calisilirken hata olustu!", x);
        }
    }
    // sorgular

    private String silmeSorgusu(Class aModelClass)
    {
        String[] alanlar = yardimciyiAl(aModelClass).sutunAdlariAl();
        String idAlani = alanlar[alanlar.length - 1];
        return "DELETE FROM " + yardimciyiAl(aModelClass).tabloAdiAl() + " WHERE " + idAlani
               + " = ? ";
    }

    private String guncellemeSorgusu(Class aModelClass)
    {
        String[] alanlar = yardimciyiAl(aModelClass).sutunAdlariAl();
        String idAlani = alanlar[alanlar.length - 1];

        String sorgu = "UPDATE " + yardimciyiAl(aModelClass).tabloAdiAl() + " SET ";
        for (int i = 0; i < alanlar.length - 1; i++) {
            sorgu += alanlar[i] + " = ? ";
            if (i < (alanlar.length - 2)) sorgu += ", ";
        }
        sorgu += "WHERE " + idAlani + " = ? ";
        return sorgu;
    }

    private String eklemeSorgusu(Class aModelClass)
    {
        String[] alanlar = yardimciyiAl(aModelClass).sutunAdlariAl();
        String sorgu = "INSERT INTO " + yardimciyiAl(aModelClass).tabloAdiAl();
        String alanAdlari = "";
        String valuesKismi = "";

        for (int i = 0; i < alanlar.length; i++) {
            alanAdlari += alanlar[i];
            valuesKismi += "?";
            if (i < (alanlar.length - 1)) {
                alanAdlari += ", ";
                valuesKismi += ", ";
            }
        }

        sorgu += " ( " + alanAdlari + " ) VALUES ( " + valuesKismi + " )";

        return sorgu;
    }

    private String yuklemeSorgusu(Class aModelClass)
    {
        String[] alanlar = yardimciyiAl(aModelClass).sutunAdlariAl();
        String idAlani = alanlar[alanlar.length - 1];

        String sorgu = "SELECT ";

        for (int i = 0; i < alanlar.length; i++) {
            sorgu += alanlar[i];
            if (i < (alanlar.length - 1)) sorgu += ", ";
        }

        sorgu += " FROM " + yardimciyiAl(aModelClass).tabloAdiAl() + " WHERE " + idAlani + " = ? ";

        return sorgu;
    }


    public int updateSorguCalistir(Connection conn, Class aClass, String aSetClause, String aWhereClause, Object[] aParams)
            throws CertStoreException
    {
        PreparedStatement ps = null;

        try {
            String sorgu = "UPDATE " + yardimciyiAl(aClass).tabloAdiAl() + " SET " + aSetClause + " WHERE " + aWhereClause;
            ps = conn.prepareStatement(sorgu);
            _parametreleriDoldur(ps, aParams);
            return ps.executeUpdate();
        }
        catch (Exception tEx) {
            throw new CertStoreException("Update sorgusu sirasinda hata olustu", tEx);
        }
        finally {
            kaynaklariKapat(null, ps);
        }
    }


    public void insertSorguCalistir(Connection conn, Class aClass, String aFieldsClause, String aValuesClause, Object[] aParams)
            throws CertStoreException
    {
        PreparedStatement ps = null;

        try {
            String sorgu = "INSERT INTO " + yardimciyiAl(aClass).tabloAdiAl() + aFieldsClause + " VALUES " + aValuesClause;
            ps = conn.prepareStatement(sorgu);
            _parametreleriDoldur(ps, aParams);
            ps.executeUpdate();
        }
        catch (Exception tEx) {
            throw new CertStoreException("Insert sorgusu sirasinda hata olustu", tEx);
        }
        finally {
            kaynaklariKapat(null, ps);
        }
    }

    public int deleteSorguCalistir(Connection conn, Class aClass, String aWhereClause, Object[] aParams)
            throws CertStoreException
    {
        PreparedStatement ps = null;

        try {
            String sorgu = "DELETE FROM " + yardimciyiAl(aClass).tabloAdiAl() + " WHERE " + aWhereClause;
            ps = conn.prepareStatement(sorgu);
            _parametreleriDoldur(ps, aParams);
            return ps.executeUpdate();
        }
        catch (Exception tEx) {
            throw new CertStoreException("Delete sorgusu sirasinda hata olustu", tEx);
        }
        finally {
            kaynaklariKapat(null, ps);
        }
    }

    public boolean selectSorguCalistir(Connection conn, Class aClass, String aFieldsClause, String aWhereClause, Object[] aParams)
            throws CertStoreException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sorgu = "SELECT " + aFieldsClause + " FROM " + yardimciyiAl(aClass).tabloAdiAl() + " WHERE " + aWhereClause;
            ps = conn.prepareStatement(sorgu);
            _parametreleriDoldur(ps, aParams);
            rs = ps.executeQuery();
            return rs.next();

        }
        catch (Exception tEx) {
            throw new CertStoreException("Select sorgusu sirasinda hata olustu", tEx);
        }
        finally {
            kaynaklariKapat(null, ps);
        }
    }

    public void sorguCalistir(Connection conn, String aSorgu)
            throws CertStoreException
    {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(aSorgu);
            ps.execute();
        }
        catch (Exception tEx) {
            logger.error("Error in Islemler", tEx);
            throw new CertStoreException("Sorgu calistirmada hata olustu", tEx);
        }
        finally {
            kaynaklariKapat(null, ps);
        }
    }

    private void _parametreleriDoldur(PreparedStatement aPS, Object[] aParams)
            throws ESYAException
    {
        try {
            if (aParams != null && aParams.length > 0) {
                for (int i = 0; i < aParams.length; i++) {
                    Object param = aParams[i];
                    if (param instanceof byte[])
                        aPS.setBytes(i + 1, (byte[]) param);
                    else
                        aPS.setObject(i + 1, param);
                }
            }
        }catch (Exception e){
            throw new ESYAException(e);
        }
    }


}
