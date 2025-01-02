package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralNames;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectAltName;

/**
 * @author ahmety
 * date: Jan 29, 2010
 */
public class ESubjectAltName extends EGeneralNames
{

    public ESubjectAltName(GeneralNames aName)
    {
        super(aName);
    }

    public ESubjectAltName(SubjectAltName aSubjectAltName)
    {
        super(aSubjectAltName);
    }

    @Override
    public String toString()
    {
        String result = "";
        for (int i = 0; i < mObject.elements.length; i++)
        {
            GeneralName element = mObject.elements[i];
             if (element.getElement() instanceof Name)
             {
                  result = result + UtilName.name2String( ( (Name) element.getElement())) + "\n";
             } else
             {
                  result = result + element.getElement().toString() + "\n";
             }
        }
        return result;
    }
}
