package tr.gov.tubitak.uekae.esya.api.cvc;

import tr.gov.tubitak.uekae.esya.api.asn.cvc.ENonSelfDescCVCwithHeader;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;

import java.util.List;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 11/17/11 - 2:30 PM <p>
 <b>Description</b>: <br>
 */
public class CVCChainVerificationResult {
    private CVCFields verifiedCVC;
    private List<Pair<ENonSelfDescCVCwithHeader, CVCFields>> verifiedCVCAuthorities;
    private int pathLength;

    public CVCChainVerificationResult(CVCFields verifiedCVC, List<Pair<ENonSelfDescCVCwithHeader, CVCFields>> verifiedCVCAuthorities) {
        this.verifiedCVC = verifiedCVC;
        this.verifiedCVCAuthorities = verifiedCVCAuthorities;
        this.pathLength = verifiedCVCAuthorities.size();
    }

    /**
     Verified CVC Certificate Fields...
     @return
     */
    public CVCFields getVerifiedCVC() {
        return verifiedCVC;
    }

    public List<Pair<ENonSelfDescCVCwithHeader, CVCFields>> getVerifiedCVCAuthorities() {
        return verifiedCVCAuthorities;
    }

    /**
     Path Length of Verified CVC Authorities in CVC Chain including RootCVC...
     @return
     */
    public int getPathLength() {
        return pathLength;
    }

    /**
     * First element contains RootCVC and its fields. last  pathLength element is CVC Authority which issued Verified CVC
     * @param pathDepth
     * @return
     */

    public Pair<ENonSelfDescCVCwithHeader, CVCFields> getVerifiedCVCAuthorityWithDepth(int pathDepth) {
        if (pathDepth < 1 || verifiedCVCAuthorities.size() < pathDepth)
            return null;
        else
            return verifiedCVCAuthorities.get(pathDepth-1);
    }
}
