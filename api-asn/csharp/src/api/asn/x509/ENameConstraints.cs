using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ENameConstraints : BaseASNWrapper<NameConstraints>
    {
        public ENameConstraints(NameConstraints aObject)
            : base(aObject)
        {
        }
        public ENameConstraints(byte[] nameConstraintsBytes)
            : base(nameConstraintsBytes, new NameConstraints())
        {
        }

        public ENameConstraints(EGeneralSubtrees aPermitted, EGeneralSubtrees aExcluded)
            : base(new NameConstraints(aPermitted.getObject(), aExcluded.getObject()))
        {
        }

        public List<EGeneralSubtree> getPermittedSubtrees()
        {
            int length = (mObject.permittedSubtrees != null) ? mObject.permittedSubtrees.elements.Length : 0;
            List<EGeneralSubtree> permittedList = new List<EGeneralSubtree>(length);
            if (length > 0)
            {
                foreach (GeneralSubtree permitted in mObject.permittedSubtrees.elements)
                {
                    permittedList.Add(new EGeneralSubtree(permitted));
                }
            }
            return permittedList;
        }

        public List<EGeneralSubtree> getExcludedSubtrees()
        {
            int length = (mObject.excludedSubtrees != null) ? mObject.excludedSubtrees.elements.Length : 0;
            List<EGeneralSubtree> excludedList = new List<EGeneralSubtree>(length);
            if (length > 0)
            {
                foreach (GeneralSubtree excluded in mObject.excludedSubtrees.elements)
                {
                    excludedList.Add(new EGeneralSubtree(excluded));
                }
            }
            return excludedList;
        }
        public String stringValue()
        {
            BigInteger bigInt = new BigInteger();
            bigInt.SetData(getBytes());
            return bigInt.ToString(16);
            //return new BigInteger(getBytes()).toString(16);
        }

        public EExtension toExtension(bool critic)
        {
            return new EExtension(EExtensions.oid_ce_nameConstraints, critic, getBytes());
        }

    }
}
