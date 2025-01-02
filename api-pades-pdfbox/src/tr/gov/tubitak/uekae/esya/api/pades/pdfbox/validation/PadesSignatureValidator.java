package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSignatureImpl;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.DocSecurityStore;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.util.PAdESUtil;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.check.PadesProfileRevocationValueMatcherChecker;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.check.PadesTurkishProfileAttributesChecker;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.check.SignatureTimeChecker;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.check.TSDigestChecker;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author ayetgin
 */
public class PadesSignatureValidator {

	private Logger logger = LoggerFactory.getLogger(PadesSignatureValidator.class);

	private List<PadesChecker> checkers = new ArrayList<PadesChecker>();

	public PadesSignatureValidator(){
		//checkers = new ArrayList<PadesChecker>();
		addPadesCheckers();
	}

	public SignatureValidationResult verify(PAdESSignature signature)
			throws SignatureException {

		if(signature.getCounterSignatures().size() > 0){
			throw new SignatureException("Counter signatures not supported in PAdES!");
		}

		// todo if last check at signing time if validation fails...
		// if more signatures(timestamp) exist and type is more complex than
		// ES_T (then DSS should exist)

		boolean withoutFinders = !PAdESUtil.isPre(signature.getSignatureType(), SignatureType.ES_XL);
        if(withoutFinders){
            if(signature.isLastSignature() && signature.getSignatureType() == SignatureType.ES_A){
                withoutFinders = false;
            }
        }

		logger.info("Validate without finders: {}", withoutFinders);
		
		Signature underlyingSignature = (Signature) signature.getUnderlyingObject();
		Context context = underlyingSignature.getContainer().getContext();
		context.getConfig().getCertificateValidationConfig().setValidateWithResourcesWithinSignature(withoutFinders);

		ValidationInfoResolver vir = context.getValidationInfoResolver();
		if (vir == null) {
			vir = new ValidationInfoResolver();
			context.setValidationInfoResolver(vir);
		}

		
		Calendar signingTime = null;
		if(signature.isTimestamp() && !signature.isLastSignature()){
			SignatureContainer container = signature.getContainer();
			int index = container.getSignatures().indexOf(signature);
			signingTime = ((PAdESSignature)container.getSignatures().get(index+1)).getTimeStampTime();
		}		
		else{
			signingTime = signature.getTimeStampTime();
			if (signingTime == null) {
				signingTime = signature.getSigningTimeAttrTime();
				context.setValidationTimeSigningTime(true);
			}
		}
		if (signingTime != null)
			context.setValidationTime(signingTime);		

		DocSecurityStore dss = ((PAdESContainer)signature.getContainer()).getDocumentSecurityStore();
		if (dss != null) {
			vir.addCertificates(dss.getCertificates());
			vir.addCRLs(dss.getCRLs());
			vir.addOCSPResponses(dss.getOCSPResponses());
		}

		context.setPAdES(true);

		SignatureValidationResult svr = underlyingSignature.verify();

		
		if (svr.getResultType() != ValidationResultType.VALID && signature.isTimestamp() && !signature.isLastSignature()) {
			context.setValidationTime(signature.getTimeStampTime());
			svr = underlyingSignature.verify();
		}

		Signer signer =((CMSSignatureImpl) underlyingSignature).getSigner();
		if(!signature.isTimestamp() && (signer.isTurkishProfile() || context.getConfig().getCertificateValidationConfig().getValidationProfile() != null)){
			if (svr.getCertificateStatusInfo().getCertificateStatus() == CertificateStatus.VALID)
				checkers.add(new PadesProfileRevocationValueMatcherChecker(svr.getCertificateStatusInfo()));
		}

		PadesSignatureValidationResult result = new PadesSignatureValidationResult(svr);

		for (PadesChecker checker : checkers) {
			ValidationResultDetail vrd = checker.check(signature);
			result.addDetail(vrd);
		}

		return result;
	}

	public void addPadesCheckers() {
		checkers.add(new SignatureTimeChecker());
		checkers.add(new TSDigestChecker());
		checkers.add(new PadesTurkishProfileAttributesChecker());
	}
}
