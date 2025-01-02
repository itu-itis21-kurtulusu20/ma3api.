using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Com.Objsys.Asn1.Runtime;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EPolicyConstraints : BaseASNWrapper<PolicyConstraints>
    {
        public EPolicyConstraints(PolicyConstraints aObject)
            : base(aObject)
        {
        }

        public EPolicyConstraints(byte[] aNameConstraintsBytes)
            : base(aNameConstraintsBytes, new PolicyConstraints())
        {
        }
        public EPolicyConstraints(Asn1Integer requireExplicitPolicy, Asn1Integer inhibitPolicyMapping)
            : base(new PolicyConstraints(requireExplicitPolicy, inhibitPolicyMapping))
        {
        }

        public String stringValue()
        {
            //return new BigInteger(getBytes()).ToString(16);
            return BitConverter.ToString(getBytes()).Replace("-", String.Empty);
        }
        public EExtension toExtension(bool critic)
        {
            return new EExtension(EExtensions.oid_ce_policyConstraints, critic, getBytes());
        }

        public Asn1Integer getRequireExplicitPolicy()
        {
            return mObject.requireExplicitPolicy;
        }

        public Asn1Integer getInhibitPolicyMapping()
        {
            return mObject.inhibitPolicyMapping;
        }

    }
}
