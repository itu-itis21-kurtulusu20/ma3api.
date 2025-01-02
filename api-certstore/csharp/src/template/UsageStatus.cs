
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.template
{
    public class UsageStatus
    {
        public static readonly UsageStatus YOK = new UsageStatus(_enum.YOK);
        public static readonly UsageStatus VAR = new UsageStatus(_enum.VAR);
        public static readonly UsageStatus FARKETMEZ = new UsageStatus(_enum.FARKETMEZ);

        enum _enum
        {
            YOK = 0,
            VAR = 1,
            FARKETMEZ= 2
        }

        private readonly _enum mValue;
        
        private UsageStatus(_enum aValue)
        {
            mValue = aValue;
        }

        public int getValue()
        {
            return (int) mValue;
        }
    }
}
