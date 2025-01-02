using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EPolicyMappingElement : BaseASNWrapper<PolicyMappings_element>
    {
        public EPolicyMappingElement(PolicyMappings_element aObject)
            : base(aObject)
        {
        }

        public Asn1ObjectIdentifier getIssuerDomainPolicy()
        {
            return mObject.issuerDomainPolicy;
        }

        public Asn1ObjectIdentifier getSubjectDomainPolicy()
        {
            return mObject.subjectDomainPolicy;
        }
    }
}
