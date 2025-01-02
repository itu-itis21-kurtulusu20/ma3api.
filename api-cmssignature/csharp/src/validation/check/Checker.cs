using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    public interface Checker
    {
        Dictionary<String, Object> getParameters();
        void setParameters(Dictionary<String, Object> aParams);
        bool check(Signer aSigner, CheckerResult aCheckerResult);
	
    }
}
