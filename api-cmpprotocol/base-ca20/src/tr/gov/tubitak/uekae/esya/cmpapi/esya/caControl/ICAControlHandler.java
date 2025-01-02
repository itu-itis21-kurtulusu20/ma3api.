package tr.gov.tubitak.uekae.esya.cmpapi.esya.caControl;

import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 28.06.2013
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public interface ICAControlHandler {
    String decryptDBPassword(String dbUserName,byte[] encryptedDBPass,byte[] registrarCertSerial);
    Pair<Long,String> getHSMPassword(String titleStr, List<Pair<Long, String>> slotInfoPairList);
}
