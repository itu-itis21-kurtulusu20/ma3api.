using System;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EGeneralNames : BaseASNWrapper<GeneralNames>
    {
        public EGeneralNames()
            : base(new GeneralNames())
        {
        }

        public EGeneralNames(GeneralNames aObject)
            : base(aObject)
        {
        }

        public EGeneralNames(EGeneralName[] generalNames)
            : base(new GeneralNames())
        {
            mObject.elements = unwrapArray<GeneralName, EGeneralName>(generalNames);
        }

        public int getElementCount()
        {
            return mObject.elements.Length;
        }

        public EGeneralName getElement(int aIndex)
        {
            return new EGeneralName(mObject.elements[aIndex]);
        }

        public void addElement(GeneralName aGeneralName)
        {
            mObject.elements = extendArray(mObject.elements, aGeneralName);
        }

        public void addElement(EGeneralName generalName)
        {
            addElement(generalName.getObject());
        }

        public static bool hasMatch(EGeneralNames iList, EName iName)
        {
            for (int i = 0; i < iList.getElementCount(); i++)
            {
                EGeneralName gn = iList.getElement(i);
                if (gn.getType() == GeneralName._DIRECTORYNAME && gn.getDirectoryName().Equals(iName))
                    return true;
            }
            return false;
        }

        public EExtension toSubjectAltNameExtension(bool critic)
        {
            return new EExtension(new Extension(_ImplicitValues.id_ce_subjectAltName, critic, getBytes()));
        }

        public String toStringWithNewLines()
        {
            String stringValue = "";
            foreach (GeneralName element in mObject.elements)
                stringValue += UtilName.generalName2String(element) + " \n";
            return stringValue;
        }
    }
}