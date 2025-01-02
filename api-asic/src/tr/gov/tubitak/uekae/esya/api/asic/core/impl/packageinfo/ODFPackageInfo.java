package tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo;

import tr.gov.tubitak.uekae.esya.api.asic.util.ASiCMimetype;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;

/**
 * @author ayetgin
 */
public class ODFPackageInfo implements PackageInfo
{
    public PackageType getType()
    {
        return PackageType.ODF;
    }

    public RequirementLevel getMimetypeRequirement()
    {
        return RequirementLevel.OPTIONAL;
    }

    public String getMimetype()
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

    public boolean allowsCAdES()
    {
        return true;
    }

    public boolean allowsXAdES()
    {
        return true;
    }

    // Any file in META-INF containing "signatures"
    public boolean allowsMultipleSignatureContainers()
    {
        return true;
    }
}
