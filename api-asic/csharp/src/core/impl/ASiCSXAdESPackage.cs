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
    public class ASiCSXAdESPackage : AbstractSignaturePackage
    {
        static PackageInfo packageInfo = new ASiCSPackageInfo();

        public ASiCSXAdESPackage(Context context) : base(context)
        {
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

        public override void setContents(PackageContents aContents)
        {
            base.setContents(aContents);
            if (Contents.getDatas().Count != 1)
                throw new SignatureRuntimeException("Only one data is allowed for signing for ASiC-S. Found : "+Contents.getDatas().Count);
            if (Contents.getContainers().Count != 1)
                throw new SignatureRuntimeException("Only one signature is allowed for ASiC-S. Found : "+Contents.getContainers().Count);
        }

        protected override SignatureContainer createContainerImpl()
        {
            return new ASiCSignatures(Context, Contents);
        }


        public override string generateSignatureContainerName()
        {
            return "signatures.xml";
        }

        public override PackageType getPackageType()
        {
            return PackageType.ASiC_S;
        }

        public override SignatureFormat getSignatureFormat()
        {
            return SignatureFormat.XAdES;
        }
    }
}