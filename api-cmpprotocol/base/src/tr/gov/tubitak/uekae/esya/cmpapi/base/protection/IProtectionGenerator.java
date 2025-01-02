package tr.gov.tubitak.uekae.esya.cmpapi.base.protection;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IConfigType;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 5, 2010
 * Time: 10:40:18 AM
 * To change this template use File | Settings | File Templates.
 */

public interface IProtectionGenerator<T extends IConfigType> {

    EAlgorithmIdentifier getProtectionAlg();

    void addProtection(EPKIMessage pkiMessage);

}
