using System;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESignerLocation : BaseASNWrapper<SignerLocation>
    {
        public ESignerLocation(byte[] aBytes)
            : base(aBytes, new SignerLocation())
        {
        }

        public String getCountry()
        {
            return mObject.countryName.GetElement().ToString();
        }

        public String getLocalityName()
        {
            return mObject.localityName.GetElement().ToString();
        }

        public String[] getPostalAddress()
        {
            String[] postalAdress = null;
            if (mObject.postalAdddress != null)
            {
                DirectoryString[] directoryStrings = mObject.postalAdddress.elements;
                postalAdress = new String[directoryStrings.Length];
                for (int i = 0; i < directoryStrings.Length; i++)
                    postalAdress[i] = directoryStrings[i].GetElement().ToString();
                return postalAdress;
            }

            return null;
        }
    }
}