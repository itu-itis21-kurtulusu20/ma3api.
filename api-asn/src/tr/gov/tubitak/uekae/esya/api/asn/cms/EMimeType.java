package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.MimeType;

/**
 * Created by sura.emanet on 5.02.2020.
 */
public class EMimeType extends BaseASNWrapper<MimeType> {

    public EMimeType(byte[] aBytes)	throws ESYAException
    {
        super(aBytes, new MimeType());
    }

    /**
     *
     * @param aMimeType defines mime type
     */
    public EMimeType(String aMimeType)
    {
        super(new MimeType(aMimeType));
    }
}
