package tr.gov.tubitak.uekae.esya.api.asn.pkcs12;

import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.PKCS12PbeParams;

/**
 * User: zeldal.ozdemir
 * Date: 1/27/11
 * Time: 9:35 AM
 */
public class EPKCS12PbeParams extends BaseASNWrapper<PKCS12PbeParams>{
    public EPKCS12PbeParams( byte[] salt, int iterationCount) {
        super(new PKCS12PbeParams( new Asn1OctetString(salt), new Asn1Integer(iterationCount)) );
    }

    public EPKCS12PbeParams(byte[] aBytes) throws ESYAException {
        super(aBytes, new PKCS12PbeParams());
    }

    public byte[] getSalt(){
        return mObject.salt.value;
    }

    public long getIterationCount(){
        return mObject.iterations.value;
    }
}
