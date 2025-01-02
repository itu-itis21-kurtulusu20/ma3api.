package tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo;

import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;

/**
 * @author ayetgin
 */
public interface PackageInfo
{
    //
    PackageType getType();

    RequirementLevel getMimetypeRequirement();
    String getMimetype();

    // manifest xml
    RequirementLevel getManifestRequirement();

    // metadata xml
    RequirementLevel getMetadataRequirement();

    // container xml
    RequirementLevel getContainerRequirement();

    //
    boolean allowsCAdES();

    //
    boolean allowsXAdES();

    //
    boolean allowsMultipleSignatureContainers();

}
