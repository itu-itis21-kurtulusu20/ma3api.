package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.Extension;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralNames;
import tr.gov.tubitak.uekae.esya.asn.x509._ImplicitValues;

/**
 * @author ahmety
 *         date: Jan 29, 2010
 */
public class EGeneralNames extends BaseASNWrapper<GeneralNames>
{


    public EGeneralNames()
    {
        super(new GeneralNames());
    }

    public EGeneralNames(GeneralNames aObject)
    {
        super(aObject);
    }

    public EGeneralNames(EGeneralName[] generalNames) {
        super(new GeneralNames());
        mObject.elements = BaseASNWrapper.unwrapArray(generalNames);
    }

    public int getElementCount(){
        return mObject.elements.length;
    }

    public EGeneralName getElement(int aIndex){
        return new EGeneralName(mObject.elements[aIndex]);
    }

    public void addElement(GeneralName aGeneralName){
        mObject.elements = extendArray(mObject.elements, aGeneralName);
    }

    public void addElement(EGeneralName generalName){
        addElement(generalName.getObject());
    }

    public static boolean hasMatch(EGeneralNames iList, EName iName)
    {
        for (int i = 0; i < iList.getElementCount(); i++) {
            EGeneralName gn = iList.getElement(i);
            if (gn.getType() == GeneralName._DIRECTORYNAME && gn.getDirectoryName().equals(iName))
                return true;
        }
        return false;
    }

    public EExtension toSubjectAltNameExtension(boolean critic){
        return new EExtension(new Extension(_ImplicitValues.id_ce_subjectAltName, critic, getEncoded()));
    }

    public String toStringWithNewLines(){
        String stringValue = "";
        for (GeneralName element : mObject.elements)
            stringValue += UtilName.generalName2String(element)+" \n";
        return stringValue;
    }



}
