package tr.gov.tubitak.uekae.esya.api.infra.certstore.db;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;

/**
 * @author ayetgin
 */
public class NotFoundException extends CertStoreException
{
    public NotFoundException(String aMessage)
    {
        super(aMessage);
    }
}
