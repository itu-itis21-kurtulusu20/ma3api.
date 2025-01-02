using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ECertificateSet : BaseASNWrapper<CertificateSet>
    {
        public ECertificateSet(CertificateSet aObject):base(aObject)
        {          
        }

        public int getCertificateChoicesCount()
        {
            if (mObject.elements == null)
                return 0;
            return mObject.elements.Length;
        }


        public ECertificateChoices getCertificateChoices(int aIndex)
        {

            if (mObject == null || mObject.elements == null || mObject.elements[aIndex] == null)
                return null;

            if (aIndex < 0 || mObject.elements.Length <= aIndex) throw new ArgumentOutOfRangeException("Certificate index choice is improper..");

            return new ECertificateChoices(mObject.elements[aIndex]);
        }

        public void addCertificateChoices(ECertificateChoices aChoices)
        {
            mObject.elements = extendArray(mObject.elements, aChoices.getObject());
        }

        public ECertificate[] getCertificates()
        {
            if (mObject == null || mObject.elements == null)
                return null;

            ECertificate[] certs = null;
            foreach (CertificateChoices cc in mObject.elements)
            {
                if (cc.ChoiceID == CertificateChoices._CERTIFICATE)
                    certs = extendArray(certs, new ECertificate((Certificate)cc.GetElement()));
            }

            return certs;
        }
    }
}
