using System.Collections;
using NUnit.Framework;



namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    public class AllNISTTests
    {
        [Suite]
        public static IEnumerable Suite
        {
            get
            {
                ArrayList suite = new ArrayList();
                suite.Add(typeof(BasicConstraintsTest));
                suite.Add(typeof(BasicRevocationTest));
                suite.Add(typeof(CertificatePoliciesTest));
                suite.Add(typeof(DeltaCRLTest));
                suite.Add(typeof(DistributionPointsTest));
                suite.Add(typeof(InhibitAnyPolicyTest));
                suite.Add(typeof(InhibitPolicyTest));
                suite.Add(typeof(KeyUsageTest));
                suite.Add(typeof(NameChainingTest));
                suite.Add(typeof(NameConstraintsTest));
                suite.Add(typeof(PolicyMappingsTest));
                suite.Add(typeof(PrivateExtensionsTest));
                suite.Add(typeof(RequireExplicitPolicyTest));        
                suite.Add(typeof(SelfIssuedTest)); 
                suite.Add(typeof(SignatureVerificationTest)); 
                suite.Add(typeof(ValidityPeriodsTest));                 

                return suite;
            }

        }

    }
}
