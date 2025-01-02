using tr.gov.tubitak.uekae.esya.api.asic.util;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo
{
    /**
     * @author yavuz.kahveci
     */
    public class ASiCSPackageInfo : PackageInfo
    {
        public PackageType getType()
        {
            return PackageType.ASiC_S;
        }

        public string getMimetype()
        {
            return ASiCMimetype.ASiC_S;
        }

        public RequirementLevel getMimetypeRequirement()
        {
            return RequirementLevel.OPTIONAL;
        }

        public RequirementLevel getManifestRequirement()
        {
            return RequirementLevel.OPTIONAL;
        }

        public RequirementLevel getMetadataRequirement()
        {
            return RequirementLevel.OPTIONAL;
        }

        public RequirementLevel getContainerRequirement()
        {
            return RequirementLevel.OPTIONAL;
        }

        public bool allowsCAdES()
        {
            return true;
        }

        public bool allowsXAdES()
        {
            return true;
        }

        public bool allowsMultipleSignatureContainers()
        {
            return false;
        }
    }
}
