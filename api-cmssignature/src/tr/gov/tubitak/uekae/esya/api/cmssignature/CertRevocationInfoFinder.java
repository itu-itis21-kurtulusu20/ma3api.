package tr.gov.tubitak.uekae.esya.api.cmssignature;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.CertificateValidationException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CertificateChecker;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CertificateCheckerResultObject;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;

import java.util.*;

public class CertRevocationInfoFinder 
{
	private List<ECertificate> mCertificates = new ArrayList<ECertificate>();
	List<CertRevocationInfo> mCertRevList = new ArrayList<CertRevocationInfo>();
	List<ECertificate> mTrustedCerts = null;

	public CertRevocationInfoFinder(boolean gelismis)
	{
		if(gelismis){
		try
    	{
    		LV.getInstance().checkLD(Urunler.CMSIMZAGELISMIS);
    	}
    	catch(LE ex)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
    	}
		}
	}
	
	public CertificateStatusInfo validateCertificate(ECertificate aCer,Map<String,Object> aParams,Calendar aDate)
	throws CMSSignatureException
	{

		CheckerResult result = new CheckerResult();
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.putAll(aParams);
		
		CertificateChecker certChecker = new CertificateChecker();
		certChecker.setParameters(params);
		if(certChecker.checkCertificateAtTime(aCer, result, aDate, true, true, false) == false)
		{
			throw new CertificateValidationException(aCer, result);
		}
		
		CertificateStatusInfo csi = ((CertificateCheckerResultObject) result.getResultObject()).getCertStatusInfo();
		mTrustedCerts = csi.getTrustedCertificates();
		
		return csi;
	}
	
	public CertRevocationInfoFinder(CertificateStatusInfo aCSI)
	{
        mTrustedCerts = aCSI.getTrustedCertificates();
		_fillLists(aCSI);
	}
	
	
	private void _fillLists(CertificateStatusInfo aStatus)
	{
		ECertificate cer = aStatus.getCertificate();
		List<CertificateStatusInfo> iptalSMDurumlari = new ArrayList<CertificateStatusInfo>();
		
		CertificateStatusInfo cerStatus = aStatus;
		
		while(!mTrustedCerts.contains(cer) && !mCertificates.contains(cer))
		{
			mCertificates.add(cer);
			
			List<CRLStatusInfo> crlStatusList = cerStatus.getCRLInfoList();
			List<CRLStatusInfo> deltaCrlStatusList = cerStatus.getDeltaCRLInfoList();
			List<OCSPResponseStatusInfo> ocspStatusList = cerStatus.getOCSPResponseInfoList();
			
			List<ECRL> crlList = new ArrayList<ECRL>();
			for(CRLStatusInfo crlStatus : crlStatusList)
			{
				if(crlStatus.getCRLStatus() == CRLStatus.VALID)
                {
                    crlList.add(crlStatus.getCRL());
                    iptalSMDurumlari.add(crlStatus.getSigningCertficateInfo());
                }
			}
			
			for(CRLStatusInfo deltaCrlStatus : deltaCrlStatusList)
			{
				if(deltaCrlStatus.getCRLStatus() == CRLStatus.VALID)
                {
                    crlList.add(deltaCrlStatus.getCRL());
                    iptalSMDurumlari.add(deltaCrlStatus.getSigningCertficateInfo());
                }
			}
			
			List<EBasicOCSPResponse> basicOCSPResponseList = new ArrayList<EBasicOCSPResponse>();
            for(OCSPResponseStatusInfo ocspStatus : ocspStatusList)
            {
                if (ocspStatus.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID)
                {
                    basicOCSPResponseList.add(ocspStatus.getOCSPResponse().getBasicOCSPResponse());
                    iptalSMDurumlari.add(ocspStatus.getSigningCertficateInfo());
                }
            }
			
			//TODO sil de ocsp de null gelirse kontrol et
            CertRevocationInfo info = new CertRevocationInfo(cerStatus.getCertificate(), 
                    crlList.toArray(new ECRL[0]), basicOCSPResponseList.toArray(new EBasicOCSPResponse[0]));
			mCertRevList.add(info);
			
			
			cerStatus = cerStatus.getSigningCertficateInfo();
			cer = cerStatus.getCertificate();
		}
		
		if(mTrustedCerts.contains(cer) && !mCertificates.contains(cer))
		{
			mCertificates.add(cer);
			CertRevocationInfo info = new CertRevocationInfo(cer,null,null);
			mCertRevList.add(info);
		}
		
		for(CertificateStatusInfo csi:iptalSMDurumlari)
		{
			_fillLists(csi);
		}
	}
	
	public List<CertRevocationInfo> getCertRevRefs(CertificateStatusInfo aCSI)
	{
		_fillLists(aCSI);
		return mCertRevList;
	}
	
	public List<ECertificate> getCertificates()
	{
		return mCertificates;
	}

    public List<EBasicOCSPResponse> getOCSPs()
    {
        List<EBasicOCSPResponse> all = new ArrayList<EBasicOCSPResponse>();
        for(CertRevocationInfo cri : mCertRevList)
        {
            EBasicOCSPResponse[] ocsps = cri.getOCSPResponses();
            if(ocsps != null)
            {
                for(EBasicOCSPResponse ocsp : ocsps)
                {
                    if(!all.contains(ocsp))
                        all.add(ocsp);
                }
            }
        }

        return all;
    }

	public List<ECRL> getCRLs()
	{
		List<ECRL> all = new ArrayList<ECRL>();
		for(CertRevocationInfo cri:mCertRevList)
		{
			ECRL[] crls = cri.getCRLs();
			if(crls != null)
			{
				for(ECRL crl:crls)
				{
					if(!all.contains(crl))
						all.add(crl);
				}
			}
		}
		
		return all;
	}
	
	
	public class CertRevocationInfo
	{
		ECertificate mCertificate;
		ECRL[] mCRLs;
		EBasicOCSPResponse [] mOCSPResponse;
		
		public CertRevocationInfo(ECertificate aCert,ECRL[] aCRLs,EBasicOCSPResponse [] aOCSPResp)
		{
			mCertificate = aCert;
			mCRLs = aCRLs;
			mOCSPResponse = aOCSPResp;
		}
		
		public ECertificate getCertificate()
		{
			return mCertificate;
		}
		
		public ECRL[] getCRLs()
		{
			return mCRLs;
		}
		
		public EBasicOCSPResponse [] getOCSPResponses()
		{
			return mOCSPResponse;
		}
	}

}
