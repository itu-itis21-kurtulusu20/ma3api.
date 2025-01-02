package tr.gov.tubitak.uekae.esya.api.asn.pkcs12;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.PKCS12Attribute;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:10 AM
 */
public class EPKCS12Attribute extends BaseASNWrapper<PKCS12Attribute>{
    public EPKCS12Attribute(byte[] aBytes) throws ESYAException {
        super(aBytes, new PKCS12Attribute());
    }
}
