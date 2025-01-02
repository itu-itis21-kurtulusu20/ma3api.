package tr.gov.tubitak.uekae.esya.api.asic.core.impl;

import tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo.ASiCSPackageInfo;
import tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo.PackageInfo;
import tr.gov.tubitak.uekae.esya.api.asic.model.DeferredSignable;
import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.asic.model.SignatureContainerEntryImpl;
import tr.gov.tubitak.uekae.esya.api.asic.util.ASiCMimetype;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFormat;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;

/**
 * @author ayetgin
 */
public class ASiCSCAdESPackage extends AbstractSignaturePackage
{
    static PackageInfo packageInfo = new ASiCSPackageInfo();

    public ASiCSCAdESPackage(Context context)
    {
        super(context);
    }

    @Override
    public void setContents(PackageContents aContents)
    {
        super.setContents(aContents);
        if (contents.getDatas().size()!=1)
            throw new SignatureRuntimeException("Only one data is allowed for signing for ASiC-S. Found : "+contents.getDatas().size());
        if (contents.getContainers().size()!=1)
            throw new SignatureRuntimeException("Only one signature is allowed for ASiC-S. Found : "+contents.getContainers().size());

        SignatureContainerEntryImpl ce = contents.getContainers().get(0);
        DeferredSignable ds = (DeferredSignable)ce.getContainer().getContext().getData();
        ds.setActualSignable(contents.getDatas().get(0));
    }

    PackageInfo getPackageInfo()
    {
        return packageInfo;
    }

    PackageContents createInitialPackage()
    {
        PackageContents pc = new PackageContents();
        pc.setMimetype(ASiCMimetype.ASiC_S);
        return pc;
    }

    String generateSignatureContainerName()
    {
        return "signature.p7s";
    }

    public PackageType getPackageType()
    {
        return PackageType.ASiC_S;
    }

    public SignatureFormat getSignatureFormat()
    {
        return SignatureFormat.CAdES;
    }
}
