using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo
{
    /**
     * @author yavuz.kahveci
     */
    public interface PackageInfo
    {
        //
        PackageType getType();

        RequirementLevel getMimetypeRequirement();
        string getMimetype();

        // manifest xml
        RequirementLevel getManifestRequirement();

        // metadata xml
        RequirementLevel getMetadataRequirement();

        // container xml
        RequirementLevel getContainerRequirement();

        //
        bool allowsCAdES();

        //
        bool allowsXAdES();

        //
        bool allowsMultipleSignatureContainers();

    }

}
