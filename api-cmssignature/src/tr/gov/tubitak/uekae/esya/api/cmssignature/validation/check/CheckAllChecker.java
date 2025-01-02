package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import java.util.List;
import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;

/**
 * Birden fazla checker verildigi zaman, hepsini ayri ayri kontrol eder.
 * Arsiv imzalarinda cadesc ve referans zaman damgalarinin ikiside bulunabilir,bulunmayabilir
 * ya da sadece biri bulunabilir. 
 * @author aslihanu
 *
 */

public class CheckAllChecker extends BaseChecker
{
	List<Checker> mCheckerList = null;
	boolean mIsOneCheckerMust = false;
	
	public CheckAllChecker(List<Checker> aCheckerList,boolean aIsOneCheckerMust)
	{
		mCheckerList = aCheckerList;
		mIsOneCheckerMust = aIsOneCheckerMust;
	}

	@Override
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.CHECK_ALL_CHECKER), CheckAllChecker.class);
		
		boolean oneFound = false;
		boolean totalResult = true;
		for(Checker checker:mCheckerList)
		{
			CheckerResult cr = new CheckerResult();
			boolean sonuc = checker.check(aSigner, cr);
			if(cr.getResultStatus() != CheckerResult_Status.NOTFOUND)
			{
				oneFound = true;
				aCheckerResult.addCheckerResult(cr);
				totalResult = totalResult && sonuc;
			}
		}
		
		if(mIsOneCheckerMust && !oneFound )
		{
			aCheckerResult.setResultStatus(CheckerResult_Status.UNSUCCESS);
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_MUST_ATTRIBUTE_IN_SIGNED_DATA)));
			return false;
		
		}
		else if(!mIsOneCheckerMust && !oneFound )
		{
			aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_OPTIONAL_ATTRIBUTE_IN_SIGNED_DATA)));
			return true;
		}
		else
		{
			if(totalResult)
			{
				aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ALL_CHECKERS_SUCCESSFULL)));
			}
			else
			{
				aCheckerResult.setResultStatus(CheckerResult_Status.UNSUCCESS);
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ALL_CHECKERS_UNSUCCESSFULL)));
			}
			
			return totalResult;
		}
		
	}
	
	
	public void setParameters(Map<String, Object> aParameters)
	{
			for(Checker checker:mCheckerList)
			{
				checker.setParameters(aParameters);
			}
	}
	
	
	
	

}
