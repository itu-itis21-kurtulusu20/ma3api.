package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.Version;

/**
 * User: zeldal.ozdemir
 * Date: 1/28/11
 * Time: 10:20 AM
 */
public class EVersion extends BaseASNWrapper<Version>{
   public final static EVersion v1 = new EVersion(Version.v1);
   public final static EVersion v2 = new EVersion(Version.v2);
   public final static EVersion v3 = new EVersion(Version.v3);

    public EVersion(byte[] aBytes) throws ESYAException {
        super(aBytes, new Version());
    }

    public EVersion(long version) {
        super(new Version(version));
    }

    public long getVersion(){
        return mObject.value;
    }
}
