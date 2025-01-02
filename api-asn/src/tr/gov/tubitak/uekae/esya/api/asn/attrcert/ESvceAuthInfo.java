package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.SvceAuthInfo;

/**
 * User: zeldal.ozdemir
 * Date: 3/23/11
 * Time: 10:22 AM
 */
public class ESvceAuthInfo extends BaseASNWrapper<SvceAuthInfo> {
    public ESvceAuthInfo(SvceAuthInfo aObject) {
        super(aObject);
    }

    public ESvceAuthInfo(byte[] aBytes) throws ESYAException {
        super(aBytes, new SvceAuthInfo());
    }

    public ESvceAuthInfo(EGeneralName service, EGeneralName ident, byte[] authInfo) {
        super(new SvceAuthInfo());
        assert service.getObject() != null;
        assert ident.getObject() != null;
        mObject.service = service.getObject();
        mObject.ident = ident.getObject();
        if (authInfo != null)
            mObject.authInfo = new Asn1OctetString(authInfo);
    }
}
