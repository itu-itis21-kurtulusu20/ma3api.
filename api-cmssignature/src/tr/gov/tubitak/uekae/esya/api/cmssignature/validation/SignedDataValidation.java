package tr.gov.tubitak.uekae.esya.api.cmssignature.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.Checker;
import tr.gov.tubitak.uekae.esya.api.common.tools.Chronometer;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class validates all signatures of a document. 
 * @author orcun.ertugrul
 *
 */


public class SignedDataValidation
{
    private static Logger logger = LoggerFactory.getLogger(SignedDataValidation.class);

	private List<Checker> mUserDefinedCheckers = new ArrayList<>();

	protected List<ECertificate> mCerts = new ArrayList<ECertificate>();
	protected List<ECRL> mCRLs = new ArrayList<ECRL>();
	protected List<EBasicOCSPResponse> mOCSPs = new ArrayList<EBasicOCSPResponse>();

	
	/**
	 * Validates all signatures of a document
	 * @param aContentInfo document data
	 * @param aParams parameters of validation. At least certificate validation policy file must be given. 
	 * @return returns the result of all signatures. You can get status of signatures 
	 * from function getSDStatus().
	 * @throws CMSSignatureException
	 */
	public SignedDataValidationResult verify(byte[] aContentInfo,Map<String,Object> aParams)
	throws CMSSignatureException
	{
        Chronometer c = new Chronometer("Verify");
        c.start();

		BaseSignedData bs = new BaseSignedData(aContentInfo);

		if(aParams.containsKey(EParameters.P_EXTERNAL_CONTENT)){
			if (bs.getSignedData().getEncapsulatedContentInfo().getContent() != null) {
				logger.error("Internal and external content must not be used together");
				throw new CMSSignatureException("Internal and external content must not be used together");
			}
		}
		
		_fillCertRevocationLists(bs.getSignedData(), aParams);
		
		List<Signer> sis  = bs.getSignerList();
		
		SignedDataValidationResult sdvr = new SignedDataValidationResult();
		boolean verified = true;
		
		for(Signer si:sis)
		{	
			SignatureValidator sv = new SignatureValidator(aContentInfo);
			sv.setCertificates(mCerts);
			sv.setCRLs(mCRLs);
			sv.setOCSPs(mOCSPs);
			sv.setUserDefinedCheckers(mUserDefinedCheckers);
			SignatureValidationResult svr = new SignatureValidationResult();
			sv.verify(svr,si,false,aParams);
			sdvr.addSignatureValidationResult(svr);
			
			verified = verified && _findCSStatus(svr);
		}
		
		if(!verified)
		{
			sdvr.setSDStatus(SignedData_Status.NOT_ALL_VALID);
			sdvr.setDescription("Imzali Veri icerisinde dogrulanamayan imzalar var.");		
		}
		else
		{
			sdvr.setSDStatus(SignedData_Status.ALL_VALID);
			sdvr.setDescription("Imzali Veri icerisindeki tum imzalar dogrulandi.");	
		}

        logger.info(c.stopSingleRun());
		return sdvr;
	}

	public SignatureValidationResult verifyByGivenSigner(byte[] aContentInfo, Signer signer, Map<String,Object> aParams) throws CMSSignatureException
	{
		Chronometer c = new Chronometer("Verify");
		c.start();

		BaseSignedData bs = new BaseSignedData(aContentInfo);

		if(aParams.containsKey(EParameters.P_EXTERNAL_CONTENT)){
			if (bs.getSignedData().getEncapsulatedContentInfo().getContent() != null) {
				logger.error("Internal and external content must not be used together");
				throw new CMSSignatureException("Internal and external content must not be used together");
			}
		}

		_fillCertRevocationLists(bs.getSignedData(), signer, aParams);

		boolean verified = true;

		SignatureValidator sv = new SignatureValidator(aContentInfo);
		sv.setCertificates(mCerts);
		sv.setCRLs(mCRLs);
		sv.setOCSPs(mOCSPs);
		sv.setUserDefinedCheckers(mUserDefinedCheckers);
		SignatureValidationResult svr = new SignatureValidationResult();
		sv.verify(svr,signer,false,aParams);

		verified = verified && _findCSStatus(svr);

		if(!verified)
		{
			svr.setSignatureStatus(Types.Signature_Status.INVALID);
			svr.setDescription("Imzali Veri icerisinde dogrulanamayan imzalar var.");
		}
		else
		{
			svr.setSignatureStatus(Types.Signature_Status.VALID);
			svr.setDescription("Imzali Veri icerisindeki tum imzalar dogrulandi.");
		}

		logger.info(c.stopSingleRun());
		return svr;
	}
	
	//Certificates,crls and basicocspresponses are gathered from
	//1. SignedData
	//2. Given user parameters
	//3. From references(certstore) if type is one of ESC,ESX1,ESX2
	//4.From all signer
	protected void _fillCertRevocationLists(ESignedData aSD,Map<String,Object> aParams)
	throws CMSSignatureException
	{
		CertificateRevocationInfoCollector collector = new CertificateRevocationInfoCollector();
		collector._extractAll(aSD,aParams);

		//gathering values from signeddata
		mCerts=collector.getAllCertificates();
		mCRLs=collector.getAllCRLs();
		mOCSPs=collector.getAllBasicOCSPResponses();
		
		_addUserParamaters(aParams);
	}
	
	//Certificates,crls and basicocspresponses are gathered from
	//1. SignedData
	//2. Given user parameters
	//3. From references(certstore) if type is one of ESC,ESX1,ESX2
	//4.From given signer
	protected void _fillCertRevocationLists(ESignedData aSD,Signer aSigner,Map<String,Object> aParams)
	throws CMSSignatureException
	{
		CertificateRevocationInfoCollector collector = new CertificateRevocationInfoCollector();
		collector._extractFromSigner(aSD, aSigner.getSignerInfo(), aParams);
	
		//gathering values from signeddata
		mCerts=collector.getAllCertificates();
		mCRLs=collector.getAllCRLs();
		mOCSPs=collector.getAllBasicOCSPResponses();
		
		_addUserParamaters(aParams);
	}
	
	private void _addUserParamaters(Map<String,Object> aParams) throws CMSSignatureException{
		//gathering values from user parameters
		if(aParams !=null)
		{
			Object certListO = aParams.get(AllEParameters.P_INITIAL_CERTIFICATES);
			if(certListO!=null)
			{
				try
				{
					List<ECertificate> certs = (List<ECertificate>) certListO;
					new Adder<ECertificate>().addDifferent(mCerts, certs);
				}
				catch(ClassCastException aEx)
				{
					throw new CMSSignatureException("P_INITIAL_CERTIFICATES parameter is not of type List<ECertificate>",aEx);
				}
			}
			
			Object crlListO = aParams.get(AllEParameters.P_INITIAL_CRLS);
			if(crlListO!=null)
			{
				try
				{
					List<ECRL> crls = (List<ECRL>) crlListO;
					new Adder<ECRL>().addDifferent(mCRLs, crls);
				}
				catch(ClassCastException aEx)
				{
					throw new CMSSignatureException("P_INITIAL_CRLS parameter is not of type List<ECRL>",aEx);
				}
			}
			
			
			Object ocspListO = aParams.get(AllEParameters.P_INITIAL_OCSP_RESPONSES);
			if(ocspListO!=null)
			{
				try
				{
					List<EOCSPResponse> ocsps = (List<EOCSPResponse>) ocspListO;
					List<EBasicOCSPResponse> basicOcsps = new ArrayList<EBasicOCSPResponse>();
					for(EOCSPResponse resp:ocsps)
					{
						basicOcsps.add(resp.getBasicOCSPResponse());
					}
					
					new Adder<EBasicOCSPResponse>().addDifferent(mOCSPs, basicOcsps);
				}
				catch(ClassCastException aEx)
				{
					throw new CMSSignatureException("P_INITIAL_BASIC_OCSP_RESPONSES parameter is not of type List<EBasicOCSPResponse>",aEx);
				}
			}
		}
	}
	
	private boolean _findCSStatus(SignatureValidationResult aSVR)
	{
		if(aSVR.getSignatureStatus() != Types.Signature_Status.VALID)
			return false;
		
		
		List<SignatureValidationResult> csResults = aSVR.getCounterSigValidationResults();
		if(csResults==null || csResults.isEmpty())
			return true;
		
		for(SignatureValidationResult svr:csResults)
		{
			if(!_findCSStatus(svr))
			{
				return false;
			}
		}

		return true;
	}

	public void setUserDefinedCheckers(List<Checker> aCheckers) {
		mUserDefinedCheckers = aCheckers;
	}
}

 	class Adder<T> {
	  void addDifferent(List<T> aAll,List<T> aNews){
		  for(T t:aNews)
			{
				if(!aAll.contains(t))
					aAll.add(t);
			}
	  }
	}
