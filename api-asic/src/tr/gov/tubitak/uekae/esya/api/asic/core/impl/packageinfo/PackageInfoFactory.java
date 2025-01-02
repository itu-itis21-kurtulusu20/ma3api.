package tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */
public class PackageInfoFactory
{
    static Map<PackageType, PackageInfo> infos = new HashMap<PackageType, PackageInfo>();

    static {
        infos.put(PackageType.ASiC_E,   new ASiCEPackageInfo());
        infos.put(PackageType.OCF,      new OCFPackageInfo());
        infos.put(PackageType.ODF,      new ODFPackageInfo());
        infos.put(PackageType.UCF,      new UCFPackageInfo());
    }

    public PackageInfo getPackageInfo(PackageType type){
        if (!infos.containsKey(type))
            throw new ESYARuntimeException("Unknown package type "+type);
        return infos.get(type);
    }
}
