using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo
{
    /**
     * @author yavuz.kahveci
     */
    public class PackageInfoFactory
    {
        static IDictionary<PackageType, PackageInfo> infos = new Dictionary<PackageType, PackageInfo>()
                                                                 {{PackageType.ASiC_E,   new ASiCEPackageInfo()},
                                                                  {PackageType.OCF,      new OCFPackageInfo()},
                                                                  {PackageType.ODF,      new ODFPackageInfo()},
                                                                  {PackageType.UCF,      new UCFPackageInfo()},
                                                                 };

        public PackageInfo getPackageInfo(PackageType type){
            if (!infos.ContainsKey(type))
                throw new SystemException("Unknown package type "+type);
            return infos[type];
        }
    }

}
