package tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo;

import tr.gov.tubitak.uekae.esya.api.asic.util.ASiCMimetype;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;

/**
 * @author ayetgin
 */
public class OCFPackageInfo implements PackageInfo
{
    public PackageType getType()
    {
        return PackageType.OCF;
    }

    public String getMimetype()
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
    public boolean allowsCAdES()
    {
        return false;
    }

    public boolean allowsXAdES()
    {
        return true;
    }

    //Optional; a single file is allowed with name "signatures.xml"
    public boolean allowsMultipleSignatureContainers()
    {
        return false;
    }
}
