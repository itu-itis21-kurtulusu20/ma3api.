package tr.gov.tubitak.uekae.esya.api.infra.certstore.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.JDepoVEN;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.SQLiteBaglantiSaglayici;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * @author ahmety
 */
public class DepoVTKatmani
{
    static Map<String, SQLiteBaglantiSaglayici> msVtKatmanlari = new HashMap<String, SQLiteBaglantiSaglayici>();

    static Logger logger = LoggerFactory.getLogger(DepoVTKatmani.class);

    public static Connection yeniOturum(String aVtAdi) throws CertStoreException{
        if (!msVtKatmanlari.containsKey(aVtAdi.toLowerCase())){
            SQLiteBaglantiSaglayici katman = new SQLiteBaglantiSaglayici();
            katman.init(aVtAdi);
            msVtKatmanlari.put(aVtAdi.toLowerCase(Locale.US), katman);
        }
        try {
            return msVtKatmanlari.get(aVtAdi.toLowerCase(Locale.US)).newConnection();
        } catch (Exception t){
            throw new CertStoreException("Cant create connection", t);
        }
    }

    public static DepoVEN yeniDepoVEN(Connection aOturum) throws CertStoreException {
        return new JDepoVEN(aOturum);
    }


}
