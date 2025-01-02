using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cvc;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.cvc
{
    public class CVCChainInfoProvider : ICVCChainInfoProvider
    {
        private List<ENonSelfDescCVCwithHeader> subCvcAuthorities = new List<ENonSelfDescCVCwithHeader>();
        private IPublicKey cvcRootPublicKey;
        private ENonSelfDescCVCwithHeader rootCVCAuthority;

        public CVCChainInfoProvider()
        {
        }

        public List<ENonSelfDescCVCwithHeader> getSubCvcAuthorities()
        {
            return subCvcAuthorities;
        }

        public IPublicKey getRootCVCAuthPublic()
        {
            return cvcRootPublicKey;
        }

        public void setRootCVCAuthority(ENonSelfDescCVCwithHeader rootCVCAuthority)
        {
            this.rootCVCAuthority = rootCVCAuthority;
        }

        public void addSubCVCAuthority(ENonSelfDescCVCwithHeader subCvcAuthority)
        {
            subCvcAuthorities.Add(subCvcAuthority);
        }

        public void setRootCVCPublicKey(IPublicKey cvcRootPublicKey)
        {
            this.cvcRootPublicKey = cvcRootPublicKey;
        }

        public ENonSelfDescCVCwithHeader getRootCVCAuthority()
        {
            return rootCVCAuthority;
        }
    }
}
