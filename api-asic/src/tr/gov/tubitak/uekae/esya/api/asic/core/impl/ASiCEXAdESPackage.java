package tr.gov.tubitak.uekae.esya.api.asic.core.impl;

import tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo.ASiCEPackageInfo;
import tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo.PackageInfo;
import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.asic.model.signatures.ASiCSignatures;
import tr.gov.tubitak.uekae.esya.api.asic.util.ASiCMimetype;
import tr.gov.tubitak.uekae.esya.api.asic.util.ASiCUtil;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFormat;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;

/**
 * @author ayetgin
 */
public class ASiCEXAdESPackage extends AbstractSignaturePackage
{
    private static ASiCEPackageInfo packageInfo = new ASiCEPackageInfo();

    public ASiCEXAdESPackage(Context context)
    {
        super(context);
    }

    public SignatureFormat getSignatureFormat()
    {
        return SignatureFormat.XAdES;
    }

    public PackageType getPackageType()
    {
        return PackageType.ASiC_E;
    }

    PackageInfo getPackageInfo()
    {
        return packageInfo;
    }

    PackageContents createInitialPackage()
    {
        PackageContents pc = new PackageContents();
        pc.setMimetype(ASiCMimetype.ASiC_E);
        return pc;
    }

    @Override
    protected SignatureContainer createContainerImpl()
    {
        return new ASiCSignatures(context, contents);
    }

    String generateSignatureContainerName()
    {
        return "signature-"+ ASiCUtil.id()+".xml";
    }
}
