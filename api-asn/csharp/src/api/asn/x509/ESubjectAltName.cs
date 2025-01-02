using System;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ESubjectAltName : EGeneralNames
    {
        public ESubjectAltName(GeneralNames aName)
            : base(aName)
        {
        }

        public ESubjectAltName(SubjectAltName aSubjectAltName)
            : base(aSubjectAltName)
        {
        }

        public ESubjectAltName(EGeneralNames aName)
            : this(aName.getObject())
        { }

        public override String ToString()
        {
            String result = "";
            for (int i = 0; i < mObject.elements.Length; i++)
            {
                GeneralName element = mObject.elements[i];
                if (element.GetElement() is Name)
                {
                    result = result + UtilName.name2String(((Name)element.GetElement())) + "\n";
                }
                else
                {
                    result = result + element.GetElement().ToString() + "\n";
                }
            }
            return result;
        }
    }
}
