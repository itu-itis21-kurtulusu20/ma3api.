using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cvc;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.cvc
{
    public interface ICVCChainInfoProvider
    {
        List<ENonSelfDescCVCwithHeader> getSubCvcAuthorities();

        IPublicKey getRootCVCAuthPublic();

        ENonSelfDescCVCwithHeader getRootCVCAuthority();
    }
}
