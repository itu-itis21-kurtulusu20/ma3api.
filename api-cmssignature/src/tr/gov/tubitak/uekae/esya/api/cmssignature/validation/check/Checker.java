package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;

public interface Checker
{
	public Map<String, Object> getParameters();
	public void setParameters(Map<String, Object> aParams);
	public boolean check(Signer aSignerInfo, CheckerResult aCheckerResult);
}
