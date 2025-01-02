package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.EncryptedContentInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.EncryptedData;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:05 AM
 */
public class EEncryptedData extends BaseASNWrapper<EncryptedData>{
    public EEncryptedData(byte[] aBytes) throws ESYAException {
        super(aBytes, new EncryptedData());
    }

    public EEncryptedData(ECmsVersion cmsVersion, EEncryptedContentInfo contentInfo) throws ESYAException {
        super(new EncryptedData(cmsVersion.getObject(), contentInfo.getObject() ));
    }
}
