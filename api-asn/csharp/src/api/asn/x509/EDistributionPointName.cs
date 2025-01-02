using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EDistributionPointName : BaseASNWrapper<DistributionPointName>
    {
        public EDistributionPointName(DistributionPointName aObject)
            : base(aObject)
        {
        }

        public EDistributionPointName(EGeneralNames generalNames)
            : base(new DistributionPointName())
        {
            //super(new DistributionPointName());
            mObject.Set_fullName(generalNames.getObject());
        }

        public EDistributionPointName(ERelativeDistinguishedName distinguishedName)
            : base(new DistributionPointName())
        {
            //super(new DistributionPointName());
            mObject.Set_nameRelativeToCRLIssuer(distinguishedName.getObject());
        }

        public int getType()
        {
            return mObject.ChoiceID;
        }

        public EGeneralNames getFullName()
        {
            if (getType() == DistributionPointName._FULLNAME)
                return new EGeneralNames((GeneralNames)mObject.GetElement());
            return null;
        }

        public RelativeDistinguishedName getNameRelativeToCRLIssuer()
        {
            if (getType() == DistributionPointName._NAMERELATIVETOCRLISSUER)
                return (RelativeDistinguishedName)mObject.GetElement();
            return null;

        }
    }
}
