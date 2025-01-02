package tr.gov.tubitak.uekae.esya.api.cvc;

import tr.gov.tubitak.uekae.esya.api.asn.cvc.ENonSelfDescCVCwithHeader;

import java.security.PublicKey;
import java.util.List;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 11/17/11 - 2:18 PM <p>
 <b>Description</b>: <br>
 */
public interface ICVCChainInfoProvider {

    public List<ENonSelfDescCVCwithHeader> getSubCvcAuthorities();

    public PublicKey getRootCVCAuthPublic();

    ENonSelfDescCVCwithHeader getRootCVCAuthority();
}
