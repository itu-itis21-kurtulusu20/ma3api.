package tr.gov.tubitak.uekae.esya.api.cmssignature.validation;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.*;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertificateRevocationInfoCollector 
{
	protected static Logger logger = LoggerFactory.getLogger(CertificateRevocationInfoCollector.class);
	   List<ECertificate> mAllCerts = new ArrayList<ECertificate>();
	   List<ECRL> mAllCrls = new ArrayList<ECRL>();
	   List<EBasicOCSPResponse> mAllOcsps = new ArrayList<EBasicOCSPResponse>();
	
	   /**
		 * Default constructor
		 */
	   public CertificateRevocationInfoCollector()
	   {}
	   
	   /**
		 * Extract all information from signed data
		 */
	   public void _extractAll(ESignedData aSD,Map<String,Object> aParams)
	   throws CMSSignatureException
	   {
		   _extractFromSignedData(aSD);
		   
		   int count = aSD.getSignerInfoCount();
		   for(int i=0;i<count;i++)
		   {
			   ESignerInfo si = aSD.getSignerInfo(i);
			   _extractFromSignerInfo(si,aParams);
			}
		   
		   
		}

	/**
	 * Extract information from signed data and signerInfo
	 */
	public void _extractFromSigner(ESignedData aSD, ESignerInfo aSI,Map<String,Object> aParams)
			throws CMSSignatureException {
		_extractFromSignedData(aSD);
		_extractFromSignerInfo(aSI,aParams);
	}
	   
	   private void _extractFromSignedData(ESignedData aSD)
	   {
		   //get certificates from signeddata
		   List<ECertificate> certs = aSD.getCertificates();
		   if(certs!=null)
		   {
			   for(ECertificate cert:certs)
				   _addIfDifferent(cert);
		   }
		   
		   //get crls from signeddata
		   List<ECRL> crls = aSD.getCRLs();
		   if(crls!=null)
		   {
			   for(ECRL crl:crls)
				   _addIfDifferent(crl);
		   }
		   		  
		   List<EOCSPResponse> ocsps = aSD.getOSCPResponses();
		   _extractFromOCSP(ocsps);	   
		   if(ocsps!=null)
		   {
			   for(EOCSPResponse ocsp : ocsps)
				   _addIfDifferent(ocsp);
		   }
	   }
	   
	   
	   public void _extractFromSignerInfo(ESignerInfo aSI,Map<String,Object> aParams)
	   throws CMSSignatureException
	   {
		    _extractFromValuesAttributes(aSI);
		    _extractFromCounterSignature(aSI,aParams);
		    //_extractFromReferences(aSI,aParams);
		    _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_archiveTimestamp);
		    _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_archiveTimestampV2);
		    _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_archiveTimestampV3);
		   
		    _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_certCRLTimestamp);
		    _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_escTimeStamp);
		    _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_signatureTimeStampToken);
           _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_contentTimestamp);
	   }

    /*
	private void _extractFromReferences(ESignerInfo aSI, Map<String, Object> aParams) 
			throws CMSSignatureException {
		// bu fonksiyon çağrılamyabilir mi?Certificate checker'da referanslara karşılık gelenler bulunuyor zaten.
		
		// if type is ESC, ESX1 or ESX2; gather the values of references from certstore
		// DONE counter signaturelarda referans varsa,onlarin degerlerinide bul
		 //TODO herzaman aIsCounterSigner=true oluyor şimdi, ama çok onemli de değil
		ESignatureType type = SignatureParser.parse(aSI, true);
		if (type == ESignatureType.TYPE_ESC
				|| type == ESignatureType.TYPE_ESX_Type1
				|| type == ESignatureType.TYPE_ESX_Type2) {
			try {
				if (aParams != null) {
					Object policyO = aParams.get(AllEParameters.P_CERT_VALIDATION_POLICIES);
					ValidationPolicy policy = null;
					if (policyO != null) {
						CertValidationPolicies policies = (CertValidationPolicies) policyO;
						// ValidationPolicy policy = (ValidationPolicy) policyO;
						// TODO use most appropriate instead of default
						policy = policies.getPolicyFor(CertValidationPolicies.CertificateType.DEFAULT);

					} else {
						policyO = aParams.get(AllEParameters.P_CERT_VALIDATION_POLICY);
						if (policyO == null) {
							throw new RuntimeException();
						}
						policy = (ValidationPolicy) policyO;
					}

					ValidationSystem vs = CertificateValidation.createValidationSystem(policy);

					vs.getFindSystem().findTrustedCertificates();

					// DONE ValueFinderFromElsewhere -> ValueFinderFromElsewhere
					// TODO EParametersda ValidationInfoFinder al...
					// DONE ValueFinderFromElsewhere'a bilinen initial value ve
					// trusted cert'ları ekle...

					ValueFinderFromElsewhere vf = new ValueFinderFromElsewhere(vs.getFindSystem().getTrustedCertificates());
					ECompleteCertificateReferences certRefs = new ECompleteCertificateReferences(aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs).get(0).getValue(0));
					ECompleteRevocationReferences revRefs = new ECompleteRevocationReferences(aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs).get(0).getValue(0));
					vf.findRevocationValues(revRefs);
					vf.findCertValues(certRefs);

					_addDifferentCerts(vf.getCertificates());
					_addDifferentCRLs(vf.getCRLs());
					_addDifferentOCSPs(vf.getOCSPs());
					_extractFromBasicOCSP(vf.getOCSPs());
				}
			} catch (Exception aEx) {
				
				/*  Exception neden atalım ki?? tüm dataları hem bulmadık daha, hem vermedik
				if (Boolean.TRUE.equals(aParams.get(AllEParameters.P_FORCE_STRICT_REFERENCE_USE)))
					throw new CMSSignatureException(aEx.getMessage(), aEx);
					/
			}
		}

	} */

	private void _extractFromValuesAttributes(ESignerInfo aSI)
	   throws CMSSignatureException
	   {
		   //SignerInfo icinde certvalues attribute u varsa,burdaki sertifikalari al. CertificateValues attribute u bir tane olabilir ve
		   //bir degeri olabilir
		   List<EAttribute> cvAttrs = aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certValues);
		   if(!cvAttrs.isEmpty())
		   {
			   try
			   {
				   ECertificateValues values = new ECertificateValues(cvAttrs.get(0).getValue(0));
				   List<ECertificate> certs = values.getCertificates();
				   if(certs!=null)
				   {
					   _addDifferentCerts(certs);
				   }
			   }
			   catch(Exception aEx)
			   {
				   throw new CMSSignatureException("Error while extracting certificates from signerinfo",aEx);
			   }
		   }
		   
		   
		   List<EAttribute> rvAttrs = aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationValues);
		   if(!rvAttrs.isEmpty())
		   {
			   try
			   {
				   ERevocationValues values = new ERevocationValues(rvAttrs.get(0).getValue(0));
				   List<ECRL> crls = values.getCRLs();
				   if(crls!=null)
				   {
					   _addDifferentCRLs(crls);
				   }
				   
				   List<EBasicOCSPResponse> ocsps = values.getBasicOCSPResponses();
				   if(ocsps!=null)
				   {
					   _addDifferentOCSPs(ocsps);
					   _extractFromBasicOCSP(ocsps);
				   }
			   }
			   catch(Exception aEx)
			   {
				   throw new CMSSignatureException("Error while extracting revocation values from signerinfo",aEx);
			   }
		   }
	   }
	   
	   private void _extractFromTimeStamp(ESignerInfo aSI,Asn1ObjectIdentifier aTSOID)
	   throws CMSSignatureException
	   {
		   //SignerInfo icinde verilen OID li timestamp attribute u varsa,bu attribute lari al.
		   List<EAttribute> attrs = aSI.getUnsignedAttribute(aTSOID);
           if(attrs.isEmpty()){
               attrs = aSI.getSignedAttribute(aTSOID);
           }

		   if(!attrs.isEmpty())
		   {
			   for(EAttribute attr:attrs)
			   {
				   ESignedData sd = null;
				   try
				   {
					   EContentInfo ci = new EContentInfo(attr.getValue(0));
					   sd = new ESignedData(ci.getContent());
					  
				   }
				   catch(Exception aEx)
				   {
					   throw new CMSSignatureException("Error while decoding timestamp attribute",aEx);
				   }
					   
				   _extractFromSignedData(sd);
				   _extractFromValuesAttributes(sd.getSignerInfo(0));
			   }
		   }
	   }
	   
	   
	   private void _extractFromCounterSignature(ESignerInfo aSI,Map<String,Object> aParams)
	   throws CMSSignatureException
	   {
		   List<EAttribute> attrs = aSI.getUnsignedAttribute(AttributeOIDs.id_countersignature);
		   if(!attrs.isEmpty())
		   {
			   ESignerInfo si = null;
			   for(EAttribute attr:attrs)
			   {
				   try
				   {
					   si = new ESignerInfo(attr.getValue(0));
					   _extractFromSignerInfo(si,aParams);
				   }
				   catch(Exception aEx)
				   {
					   throw new CMSSignatureException("Error while decoding countersignature attribute",aEx);
				   }
			   }
		   }
	   }

	private void _extractFromBasicOCSP(List<EBasicOCSPResponse> responses) {
		for (EBasicOCSPResponse response : responses) {
			List<ECertificate> certs = new ArrayList<ECertificate>();
			for (int i = 0; i < response.getCertificateCount(); i++) {
				certs.add(response.getCertificate(i));
			}
			_addDifferentCerts(certs);
		}
	}

	private void _extractFromOCSP(List<EOCSPResponse> responses) {
		if (responses != null) {
			for (EOCSPResponse response : responses) {
				List<ECertificate> certs = new ArrayList<ECertificate>();
				for (int i = 0; i < response.getBasicOCSPResponse().getCertificateCount(); i++) {
					certs.add(response.getBasicOCSPResponse().getCertificate(i));
				}
				_addDifferentCerts(certs);
			}
		}
	}
	
	protected void _addIfDifferent(ECertificate aCert) {
		if (!mAllCerts.contains(aCert))
			mAllCerts.add(aCert);
	}

	protected void _addIfDifferent(ECRL aCRL) {
		if (!mAllCrls.contains(aCRL))
			mAllCrls.add(aCRL);
	}

	protected void _addIfDifferent(EOCSPResponse aOCSPResponse) {
		if (!mAllOcsps.contains(aOCSPResponse.getBasicOCSPResponse()))
			mAllOcsps.add(aOCSPResponse.getBasicOCSPResponse());
	}
	   /**
		 * Returns all certificates
		 * @return
		 */
	   public List<ECertificate> getAllCertificates()
	   {
		   return mAllCerts;
	   }
	   /**
		 * Returns all CRLs
		 * @return
		 */
	   public List<ECRL> getAllCRLs()
	   {
		   return mAllCrls;
	   }
	   /**
		 * Returns OCSPs
		 * @return
		 */
	   public List<EBasicOCSPResponse> getAllBasicOCSPResponses()
	   {
		   return mAllOcsps;
	   }
	   
	   protected void _addDifferentCerts(List<ECertificate> aCerts)
	   {
		   for(ECertificate cert:aCerts)
		   {
			   if(!mAllCerts.contains(cert))
				   mAllCerts.add(cert);
		   }
	   }
	   
	   protected void _addDifferentCRLs(List<ECRL> aCRLs)
	   {
		   for(ECRL crl:aCRLs)
		   {
			   if(!mAllCrls.contains(crl))
				   mAllCrls.add(crl);
		   }
	   }
	   
	   protected void _addDifferentOCSPs(List<EBasicOCSPResponse> aOCSPs)
	   {
		   for(EBasicOCSPResponse ocsp:aOCSPs)
		   {
			   if(!mAllOcsps.contains(ocsp))
				   mAllOcsps.add(ocsp);
		   }
	   }
	   
	   public static void main(String[] args) 
	   {
		   try
		   {
		   EContentInfo ci = new EContentInfo(AsnIO.dosyadanOKU("C://Signature-C-XL-4.p7s"));
		   ESignedData sd = new ESignedData(ci.getContent());
		   
		   CertificateRevocationInfoCollector cc =  new CertificateRevocationInfoCollector();
		   cc._extractAll(sd,null);
		   List<ECertificate> certs = cc.getAllCertificates();
		   List<ECRL> crls = cc.getAllCRLs();
		   List<EBasicOCSPResponse> ocsps = cc.getAllBasicOCSPResponses();
		   
		   for(ECertificate cer:certs)
		   {
			   System.out.println("cer:"+cer.getSerialNumberHex());
		   }
		   
		   for(ECRL crl:crls)
		   {
			   System.out.println("crl:"+UtilName.name2String(crl.getIssuer().getObject()));
		   }
		   
		   for(EBasicOCSPResponse ocsp:ocsps)
		   {
			   System.out.println("ocsp:"+ocsp.getTbsResponseData().getProducetAt());
		   }
		   
		   }
		   catch(Exception aEx)
		   {
			   logger.error("Error in CertificateRevocationInfoCollector", aEx);
		   }
	   }
}
