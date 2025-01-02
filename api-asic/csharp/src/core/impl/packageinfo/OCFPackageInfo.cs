using tr.gov.tubitak.uekae.esya.api.asic.util;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo
{
    /**
     * @author yavuz.kahveci
     */
    public class OCFPackageInfo : PackageInfo
    {
        public PackageType getType()
        {
            return PackageType.OCF;
        }

        public string getMimetype()
        {
            return ASiCMimetype.OCF;
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
            return RequirementLevel.MANDATORY;
        }

        // a single file is allowed with name "signatures.xml"
        public bool allowsCAdES()
        {
            return false;
        }

        public bool allowsXAdES()
        {
            return true;
        }

        //Optional; a single file is allowed with name "signatures.xml"
        public bool allowsMultipleSignatureContainers()
        {
            return false;
        }
    }

}
