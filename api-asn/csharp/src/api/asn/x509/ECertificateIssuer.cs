using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ECertificateIssuer : EGeneralNames
    {
        public ECertificateIssuer()
        {
        }

        public ECertificateIssuer(CertificateIssuer aIssuer)
            : base(aIssuer)
        {

        }

        public bool hasIssuer(EName aIssuer)
        {
            //foreach (GeneralName x509gn in mObject.elements)
            for (int i = 0; i < getElementCount(); i++)
            {
                //EGeneralName gn = new EGeneralName(x509gn);
                EGeneralName gn = getElement(i);
                if (gn.getType() == GeneralName._DIRECTORYNAME)
                {
                    if (gn.getDirectoryName().Equals(aIssuer))
                        return true;
                }
            }
            return false;
        }
    }
}
