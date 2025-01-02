package tr.gov.tubitak.uekae.esya.cmpapi.base20.protection;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IConfigType;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 3, 2010
 * Time: 4:28:49 PM
 * To change this template use File | Settings | File Templates.
 */

public interface IProtectionController<T extends IConfigType> {

    boolean verifyProtection(EPKIMessage message) ;

}
