using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class UtilCMS
    {
        public static IssuerAndSerialNumber issuerAndSerialNumberOlustur(Certificate aSertifika)
        {
            IssuerAndSerialNumber ias = new IssuerAndSerialNumber();
            ias.issuer = aSertifika.tbsCertificate.issuer;
            ias.serialNumber = aSertifika.tbsCertificate.serialNumber;
            return ias;
        }
    }
}
