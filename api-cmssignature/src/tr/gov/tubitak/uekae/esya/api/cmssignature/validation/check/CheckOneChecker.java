package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import java.util.List;

import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;


/**
 * Birden fazla checker verildigi zaman yapida hangisi varsa,onu kontrol eder.
 * SigningCertificate ve SigningCertificateV2 checker larindan hangisi ile ilgili ozellik varsa,o
 * checker i cagirir
 * @author aslihanu
 *
 */
public class CheckOneChecker extends BaseChecker
{
	private List<Checker> mCheckerList = null;
	
	public CheckOneChecker(List<Checker> aCheckerList)
	{
		mCheckerList = aCheckerList;
	}
	
	
	@Override
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		
		for(Checker checker:mCheckerList)
		{
			boolean sonuc = checker.check(aSigner, aCheckerResult);
			CheckerResult_Status status = aCheckerResult.getResultStatus();
			
			if(status == CheckerResult_Status.NOTFOUND)
				continue;
			else
				return sonuc;
		}
		
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.CHECK_ONE_CHECKER), CheckOneChecker.class);
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_CHECKER_SUCCESSFULL)));
		return false;
	}

	
	
	
}
