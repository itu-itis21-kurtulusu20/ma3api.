using tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo;
using tr.gov.tubitak.uekae.esya.api.asic.model;
using tr.gov.tubitak.uekae.esya.api.asic.model.signatures;
using tr.gov.tubitak.uekae.esya.api.asic.util;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl
{
    /**
     * @author yavuz.kahveci
     */
    public class ASiCEXAdESPackage : AbstractSignaturePackage
    {
        private static ASiCEPackageInfo packageInfo = new ASiCEPackageInfo();

        public ASiCEXAdESPackage(Context context)
            : base(context)
        {
        }

        public override SignatureFormat getSignatureFormat()
        {
            return SignatureFormat.XAdES;
        }

        public override PackageType getPackageType()
        {
            return PackageType.ASiC_E;
        }

        public override PackageInfo getPackageInfo()
        {
            return packageInfo;
        }

        public override PackageContents createInitialPackage()
        {
            PackageContents pc = new PackageContents();
            pc.setMimetype(ASiCMimetype.ASiC_E);
            return pc;
        }

        protected override SignatureContainer createContainerImpl()
        {
            return new ASiCSignatures(Context, Contents);
        }

        public override string generateSignatureContainerName()
        {
            return "signature-" + ASiCUtil.id() + ".xml";
        }
    }
}