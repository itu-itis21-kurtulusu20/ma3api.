package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * @author ayetgin
 */
public interface ExtensionType
{
    public EExtension toExtension(boolean aCritic) throws ESYAException;
}
