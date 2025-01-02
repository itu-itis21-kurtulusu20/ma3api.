package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.asn.x509.CertificateIssuer;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;

/**
 * @author ahmety
 * date: Jan 28, 2010
 */
public class ECertificateIssuer extends EGeneralNames
{

    public ECertificateIssuer()
    {
        super();
    }

    public ECertificateIssuer(CertificateIssuer aIssuer)
    {
        super(aIssuer);
    }

    public boolean hasIssuer(EName aIssuer)
    {
        for (int i=0; i < getElementCount(); i++)
        {
            EGeneralName gn = getElement(i);
            if (gn.getType() == GeneralName._DIRECTORYNAME)
            {
                if (gn.getDirectoryName().equals(aIssuer))
                    return true;
            }
        }
        return false;
    }


}
