using System;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EBasicConstraints : BaseASNWrapper<BasicConstraints>, ExtensionType
    {
        public EBasicConstraints(BasicConstraints aObject)
            : base(aObject)
        {
        }
        public EBasicConstraints(bool aIsCA, int aLen)
            : base(new BasicConstraints())
        {
            //super(new BasicConstraints());
            if (aLen < 0)
            {
                mObject.cA = new Asn1Boolean(aIsCA);
            }
            else
            {
                mObject.cA = new Asn1Boolean(aIsCA);
                mObject.pathLenConstraint = new Asn1Integer(aLen);
            }
        }

        public bool isCA()
        {
            return mObject.cA.mValue;
        }

        public long? getPathLenConstraint()
        {
            //return (mObject.pathLenConstraint == null) ? null : mObject.pathLenConstraint.mValue;
            if (mObject.pathLenConstraint == null)
                return null;
            else
                return mObject.pathLenConstraint.mValue;
        }
        public EExtension toExtension(bool aCritic)
        {
            return new EExtension(EExtensions.oid_ce_basicConstraints, aCritic, getBytes());
        }
        public override String ToString()
        {
            StringBuilder result = new StringBuilder();
            if (mObject.cA.mValue)
            {
                result.Append(Resource.message(Resource.BC_TYPE) + "= " + Resource.message(Resource.BC_CA) + "\n");
            }
            else
            {
                result.Append(Resource.message(Resource.BC_TYPE) + "= " + Resource.message(Resource.BC_CA_DEGIL) + "\n");
            }
            if (mObject.pathLenConstraint == null)
            {
                result.Append(Resource.message(Resource.BC_PATH) + "=" + "NONE" + "\n");
            }
            else
            {
                result.Append(Resource.message(Resource.BC_PATH) + "=" + mObject.pathLenConstraint.mValue + "\n");
            }
            return result.ToString();
        }
    }
}
