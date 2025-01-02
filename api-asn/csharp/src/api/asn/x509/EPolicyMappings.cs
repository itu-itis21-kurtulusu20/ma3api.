using System;
using System.Collections.Generic;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EPolicyMappings : BaseASNWrapper<PolicyMappings>, ExtensionType
    {
        public EPolicyMappings(PolicyMappings aObject)
            : base(aObject)
        {
        }
        public EPolicyMappings(int[][] map1, int[][] map2)
            : base(new PolicyMappings())
        {
            if (map1.Length != map2.Length)
                throw new ESYAException("Invalid policy mapping arguments");

            int s = map1.Length;
            PolicyMappings_element[] elements = new PolicyMappings_element[s];

            for (int i = 0; i < s; i++)
            {
                elements[i] = new PolicyMappings_element(map1[i], map2[i]);
            }

            mObject.elements = elements;
        }

        public int getPolicyMappingElementCount()
        {
            return mObject.elements.Length;
        }

        public EPolicyMappingElement getPolicyMappingElement(int aIndex)
        {
            return new EPolicyMappingElement(mObject.elements[aIndex]);
        }

        public List<Asn1ObjectIdentifier> getSubjectEquivalents(Asn1ObjectIdentifier aIssuerDomainPolicy)
        {
            List<Asn1ObjectIdentifier> pList = new List<Asn1ObjectIdentifier>();
            foreach (PolicyMappings_element pme in mObject.elements)
            {
                if (pme.issuerDomainPolicy.Equals(aIssuerDomainPolicy))
                    pList.Add(pme.subjectDomainPolicy);
            }
            return pList;
        }

        public EExtension toExtension(bool aCritic)
        {
            return new EExtension(EExtensions.oid_ce_policyMappings, aCritic, getBytes());
        }

        public override String ToString()
        {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < mObject.elements.Length; i++)
            {
                PolicyMappings_element element = mObject.elements[i];
                result.Append(" [" + (i + 1) + "]\n");
                result.Append(Resource.message(Resource.PM_IDP) + "=" + element.issuerDomainPolicy.ToString() + "\n");
                result.Append(Resource.message(Resource.PM_SDP) + "=" + element.subjectDomainPolicy.ToString() + "\n");
            }
            return result.ToString();
        }
    }
}
