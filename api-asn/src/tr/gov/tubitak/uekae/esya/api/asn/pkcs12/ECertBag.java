package tr.gov.tubitak.uekae.esya.api.asn.pkcs12;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OpenType;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.CertBag;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:07 AM
 */
public class ECertBag extends BaseASNWrapper<CertBag>{
    public ECertBag(byte[] aBytes) throws ESYAException {
        super(aBytes, new CertBag());
    }

    public ECertBag(Asn1ObjectIdentifier certId, Asn1OpenType certValue) {
        super(new CertBag(certId, certValue));
    }
}
