package tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo;

import tr.gov.tubitak.uekae.esya.api.asic.util.ASiCMimetype;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;

/**
 * @author ayetgin
 */
public class ASiCEPackageInfo implements PackageInfo
{
    public PackageType getType()
    {
        return PackageType.ASiC_E;
    }

    public String getMimetype()
    {
        return ASiCMimetype.ASiC_E;
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

    public boolean allowsCAdES()
    {
        return true;
    }

    public boolean allowsXAdES()
    {
        return true;
    }

    public boolean allowsMultipleSignatureContainers()
    {
        return true;
    }
}
