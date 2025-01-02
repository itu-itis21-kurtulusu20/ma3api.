package tr.gov.tubitak.uekae.esya.api.infra.certstore.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.ModelNesneYardimci;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 8/15/11
 * Time: 3:51 PM
 */
public class RsItemSource<T> implements ItemSource<T> {

    protected static Logger logger = LoggerFactory.getLogger(RsItemSource.class);

    private ResultSet _resultSet;
    ModelNesneYardimci _nesneYardimci;

    public RsItemSource(/*Connection aCon, */ModelNesneYardimci aNesneYardimci, ResultSet aRs) {
        _nesneYardimci = aNesneYardimci;
        _resultSet = aRs;
    }

    public boolean open() {
        reset();
        return true;
    }

    public boolean close() {
        try {
            _resultSet.getStatement().close();
            _resultSet.close();
            return true;
        } catch (SQLException e) {
            //To change body of catch statement use File | Settings | File Templates.
            logger.error("Error in RsItemSource", e);
        }
        return false;
    }

    public boolean reset() {

        try {
            _resultSet.beforeFirst();
            return true;
        } catch (SQLException e) {
            //To change body of catch statement use File | Settings | File Templates.
            logger.error("Error in RsItemSource", e);
        }
        return false;
    }

    public T nextItem() throws ESYAException {
        try {
            if (_resultSet.next())
                return (T) _nesneYardimci.nesneyiDoldur(_resultSet);
        } catch (SQLException e) {
            //To change body of catch statement use File | Settings | File Templates.
            logger.error("Error in RsItemSource", e);
        }
        return null;
    }

    public boolean atEnd() {
        try {
            return _resultSet.isLast();
        } catch (SQLException e) {
            //To change body of catch statement use File | Settings | File Templates.
            logger.error("Error in RsItemSource", e);
        }
        return false;
    }


    public List<T> toList() {
        List<T> tList = new ArrayList<T>();
        try {
            T t = nextItem();
            while (t != null) {
                tList.add(t);
                t = nextItem();
            }
            return tList;
        } catch (Exception e) {
            logger.warn("Warning in RsItemSource", e);
            return null;
        }
    }
}
