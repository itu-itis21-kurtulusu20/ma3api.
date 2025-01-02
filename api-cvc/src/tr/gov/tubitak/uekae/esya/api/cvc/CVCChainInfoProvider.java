package tr.gov.tubitak.uekae.esya.api.cvc;

import tr.gov.tubitak.uekae.esya.api.asn.cvc.ENonSelfDescCVCwithHeader;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 11/17/11 - 2:25 PM <p>
 <b>Description</b>: <br>
 */
public class CVCChainInfoProvider implements ICVCChainInfoProvider {
    private List<ENonSelfDescCVCwithHeader> subCvcAuthorities = new ArrayList<ENonSelfDescCVCwithHeader>();
    private PublicKey cvcRootPublicKey;
    private ENonSelfDescCVCwithHeader rootCVCAuthority;

    public CVCChainInfoProvider() {
    }

    public List<ENonSelfDescCVCwithHeader> getSubCvcAuthorities() {
        return subCvcAuthorities;
    }

    public PublicKey getRootCVCAuthPublic() {
        return cvcRootPublicKey;
    }

    public void setRootCVCAuthority(ENonSelfDescCVCwithHeader rootCVCAuthority){
        this.rootCVCAuthority = rootCVCAuthority;
    }

    public void addSubCVCAuthority(ENonSelfDescCVCwithHeader subCvcAuthority){
        subCvcAuthorities.add(subCvcAuthority);
    }

    public void setRootCVCPublicKey(PublicKey cvcRootPublicKey) {
        this.cvcRootPublicKey = cvcRootPublicKey;
    }

    public ENonSelfDescCVCwithHeader getRootCVCAuthority() {
        return rootCVCAuthority;
    }
}
