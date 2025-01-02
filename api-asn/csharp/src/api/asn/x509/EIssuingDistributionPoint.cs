using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EIssuingDistributionPoint : BaseASNWrapper<IssuingDistributionPoint>
    {
        public EIssuingDistributionPoint(IssuingDistributionPoint aObject)
            : base(aObject)
        {
        }

        public EIssuingDistributionPoint(byte[] aBytes)
            : base(aBytes, new IssuingDistributionPoint())
        {
        }

        public EIssuingDistributionPoint(EDistributionPointName distributionPoint,
                                         bool onlyContainsUserCerts,
                                         bool onlyContainsCACerts,
                                         EReasonFlags onlySomeReasons,
                                         bool indirectCRL,
                                         bool onlyContainsAttributeCerts) :
                                             base(new IssuingDistributionPoint(
                                                      distributionPoint.getObject(),
                                                      onlyContainsUserCerts,
                                                      onlyContainsCACerts,
                                                      onlySomeReasons.getObject(),
                                                      indirectCRL,
                                                      onlyContainsAttributeCerts))
        {
        }

        public EIssuingDistributionPoint()
            : base(new IssuingDistributionPoint())
        {
        }

        public EDistributionPointName getDistributionPoint()
        {
            if (mObject.distributionPoint == null)
                return null;
            return new EDistributionPointName(mObject.distributionPoint);
        }

        public void setDistributionPoint(EDistributionPointName aEDistributionPointName)
        {
            mObject.distributionPoint = aEDistributionPointName.getObject();
        }

        public bool isOnlyContainsCACerts()
        {
            return mObject.onlyContainsCACerts.mValue;
        }

        public void setOnlyContainsCACerts(bool aFlag)
        {
            mObject.onlyContainsCACerts = new Asn1Boolean(aFlag);
        }

        public bool isOnlyContainsUserCerts()
        {
            return mObject.onlyContainsUserCerts.mValue;
        }

        public void setOnlyContainsUserCerts(bool aFlag)
        {
            mObject.onlyContainsUserCerts = new Asn1Boolean(aFlag);
        }

        public bool isOnlyContainsAttributeCerts()
        {
            return (mObject.onlyContainsAttributeCerts != null) && mObject.onlyContainsAttributeCerts.mValue;
        }

        public void setOnlyContainsAttributeCerts(bool aFlag)
        {
            mObject.onlyContainsAttributeCerts = new Asn1Boolean(aFlag);
        }

        public bool isIndirectCRL()
        {
            return mObject.indirectCRL.mValue;
        }

        public void setIndirectCRL(bool aFlag)
        {
            mObject.indirectCRL = new Asn1Boolean(aFlag);
        }

        public ReasonFlags getOnlySomeReasons()
        {
            return mObject.onlySomeReasons;
        }

        public void setOnlySomeReasons(EReasonFlags aEReasonFlags)
        {
            mObject.onlySomeReasons = aEReasonFlags.getObject();
        }
    }
}