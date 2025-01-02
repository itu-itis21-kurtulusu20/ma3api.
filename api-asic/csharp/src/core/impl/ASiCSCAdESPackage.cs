using tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo;
using tr.gov.tubitak.uekae.esya.api.asic.model;
using tr.gov.tubitak.uekae.esya.api.asic.util;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl
{
    /**
     * @author yavuz.kahveci
     */
    public class ASiCSCAdESPackage : AbstractSignaturePackage
    {
        static PackageInfo packageInfo = new ASiCSPackageInfo();

        public ASiCSCAdESPackage(Context context) : base(context)
        {
        }

        public override void setContents(PackageContents aContents)
        {
            base.Contents = aContents;
            if (Contents.getDatas().Count!=1)
                throw new SignatureRuntimeException("Only one data is allowed for signing for ASiC-S. Found : "+Contents.getDatas().Count);
            if (Contents.getContainers().Count!=1)
                throw new SignatureRuntimeException("Only one signature is allowed for ASiC-S. Found : "+Contents.getContainers().Count);

            SignatureContainerEntryImpl ce = Contents.getContainers()[0];
            DeferredSignable ds = (DeferredSignable)ce.getContainer().getContext().getData();
            ds.setActualSignable(Contents.getDatas()[0]);
        }

        public override PackageInfo getPackageInfo()
        {
            return packageInfo;
        }

        public override PackageContents createInitialPackage()
        {
            PackageContents pc = new PackageContents();
            pc.setMimetype(ASiCMimetype.ASiC_S);
            return pc;
        }

        public override string generateSignatureContainerName()
        {
            return "signature.p7s";
        }

        public override PackageType getPackageType()
        {
            return PackageType.ASiC_S;
        }

        public override SignatureFormat getSignatureFormat()
        {
            return SignatureFormat.CAdES;
        }
    }
}