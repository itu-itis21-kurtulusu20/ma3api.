package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.cms.CMSVersion;

/**
 * User: zeldal.ozdemir
 * Date: 1/25/11
 * Time: 3:45 PM
 */
public class ECmsVersion extends BaseASNWrapper<CMSVersion>{

   public final static ECmsVersion v0 = new ECmsVersion(CMSVersion.v0);
   public final static ECmsVersion v1 = new ECmsVersion(CMSVersion.v1);
   public final static ECmsVersion v2 = new ECmsVersion(CMSVersion.v2);
   public final static ECmsVersion v3 = new ECmsVersion(CMSVersion.v3);
   public final static ECmsVersion v4 = new ECmsVersion(CMSVersion.v4);
   public final static ECmsVersion v5 = new ECmsVersion(CMSVersion.v5);

    public ECmsVersion(long value) {
        super(new CMSVersion(value));
    }
}
