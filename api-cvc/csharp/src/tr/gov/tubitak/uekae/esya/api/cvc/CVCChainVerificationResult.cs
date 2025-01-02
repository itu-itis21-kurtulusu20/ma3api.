using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cvc;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;

namespace tr.gov.tubitak.uekae.esya.api.cvc
{
    public class CVCChainVerificationResult
    {
        private CVCFields verifiedCVC;
        private List<Pair<ENonSelfDescCVCwithHeader, CVCFields>> verifiedCVCAuthorities;
        private int pathLength;

        public CVCChainVerificationResult(CVCFields verifiedCVC, List<Pair<ENonSelfDescCVCwithHeader, CVCFields>> verifiedCVCAuthorities)
        {
            this.verifiedCVC = verifiedCVC;
            this.verifiedCVCAuthorities = verifiedCVCAuthorities;
            this.pathLength = verifiedCVCAuthorities.Count;
        }

        /**
         Verified CVC Certificate Fields...
         @return
         */
        public CVCFields getVerifiedCVC()
        {
            return verifiedCVC;
        }

        public List<Pair<ENonSelfDescCVCwithHeader, CVCFields>> getVerifiedCVCAuthorities()
        {
            return verifiedCVCAuthorities;
        }

        /**
         Path Length of Verified CVC Authorities in CVC Chain including RootCVC...
         @return
         */
        public int getPathLength()
        {
            return pathLength;
        }

        /**
         * First element contains RootCVC and its fields. last  pathLength element is CVC Authority which issued Verified CVC
         * @param pathDepth
         * @return
         */

        public Pair<ENonSelfDescCVCwithHeader, CVCFields> getVerifiedCVCAuthorityWithDepth(int pathDepth)
        {
            if (pathDepth < 1 || verifiedCVCAuthorities.Count < pathDepth)
                return null;
            else
                return verifiedCVCAuthorities[pathDepth - 1];
        }
    }
}
