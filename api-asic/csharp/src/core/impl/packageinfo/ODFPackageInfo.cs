using tr.gov.tubitak.uekae.esya.api.asic.util;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo
{
    /**
     * @author yavuz.kahveci
     */
    public class ODFPackageInfo : PackageInfo
    {
        public PackageType getType()
        {
            return PackageType.ODF;
        }

        public RequirementLevel getMimetypeRequirement()
        {
            return RequirementLevel.OPTIONAL;
        }

        public string getMimetype()
        {
            return ASiCMimetype.ODF_TEXT; // todo
        }

        public RequirementLevel getManifestRequirement()
        {
            return RequirementLevel.MANDATORY;
        }

        public RequirementLevel getMetadataRequirement()
        {
            return RequirementLevel.NOT_PRESENT;
        }

        public RequirementLevel getContainerRequirement()
        {
            return RequirementLevel.NOT_PRESENT;
        }

        public bool allowsCAdES()
        {
            return true;
        }

        public bool allowsXAdES()
        {
            return true;
        }

        // Any file in META-INF containing "signatures"
        public bool allowsMultipleSignatureContainers()
        {
            return true;
        }
    }
}
