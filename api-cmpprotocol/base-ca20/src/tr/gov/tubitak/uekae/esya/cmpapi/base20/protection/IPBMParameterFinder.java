package tr.gov.tubitak.uekae.esya.cmpapi.base20.protection;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPBMParameter;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIMessage;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 3, 2010
 * Time: 11:19:42 AM
 * To change this template use File | Settings | File Templates.
 */

public interface IPBMParameterFinder {
    EPBMParameter getPbmParameter(PKIMessage message);

    EPBMParameter getPBMParameter();

    String getHMacCode();
}
