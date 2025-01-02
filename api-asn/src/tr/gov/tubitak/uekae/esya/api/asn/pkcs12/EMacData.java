package tr.gov.tubitak.uekae.esya.api.asn.pkcs12;

import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EDigestInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.DigestInfo;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.MacData;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:08 AM
 */
public class EMacData extends BaseASNWrapper<MacData>{
    public EMacData(byte[] aBytes) throws ESYAException {
        super(aBytes, new MacData());
    }

    public EMacData( EDigestInfo mac, byte[] macSalt, long iterations ) {
        super( new MacData(mac.getObject(), macSalt, iterations ) );
    }
}
