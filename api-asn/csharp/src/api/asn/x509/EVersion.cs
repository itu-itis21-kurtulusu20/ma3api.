using Version = tr.gov.tubitak.uekae.esya.asn.x509.Version;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EVersion : BaseASNWrapper<Version>
    {
        public static readonly EVersion v1 = new EVersion(Version.v1);
        public static readonly EVersion v2 = new EVersion(Version.v2);
        public static readonly EVersion v3 = new EVersion(Version.v3);

        public EVersion(byte[] aBytes)
            : base(aBytes, new Version())
        {
        }

        public EVersion(long version)
            : base(new Version(version))
        {
        }
        public long getVersion()
        {
            return mObject.mValue;
        }
    }
}
