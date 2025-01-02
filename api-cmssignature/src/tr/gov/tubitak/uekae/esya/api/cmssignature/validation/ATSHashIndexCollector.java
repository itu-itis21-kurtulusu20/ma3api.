package tr.gov.tubitak.uekae.esya.api.cmssignature.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EATSHashIndex;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.asn.cms.CertificateChoices;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherRevocationInfoFormat;
import tr.gov.tubitak.uekae.esya.asn.cms.RevocationInfoChoice;
import tr.gov.tubitak.uekae.esya.asn.cms.SignedData;
import tr.gov.tubitak.uekae.esya.asn.cms._cmsValues;
import tr.gov.tubitak.uekae.esya.asn.ocsp._ocspValues;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;

public class ATSHashIndexCollector {

	protected static Logger logger = LoggerFactory.getLogger(ATSHashIndexCollector.class);
	List<ECertificate> mAllCerts = new ArrayList<ECertificate>();
	List<ECRL> mAllCrls = new ArrayList<ECRL>();
	List<EBasicOCSPResponse> mAllOcsps = new ArrayList<EBasicOCSPResponse>();

	public void checkATSHashIndex(Signer aSigner) {

		try {
			List<EAttribute> unsignedAttribute = aSigner.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
			// unsignedAttribute.size()-1 for wider validation
			EContentInfo ci = new EContentInfo(unsignedAttribute.get(unsignedAttribute.size() - 1).getValue(0));
			ESignedData sd = new ESignedData(ci.getContent());
			EAttribute atsHashIndexAttr = sd.getSignerInfo(0).getUnsignedAttribute(AttributeOIDs.id_aa_ATSHashIndex).get(0);
			EATSHashIndex atsHashIndex = new EATSHashIndex(atsHashIndexAttr.getValue(0));

			mAllCerts = _checkCerts(aSigner, atsHashIndex);
			mAllCrls = _checkCrls(aSigner, atsHashIndex);
			mAllOcsps = _checkOcsps(aSigner, atsHashIndex);
			// _changeParameters(mAllCerts, crls);
		} catch (Exception e) {
			logger.error("Error in ATSHashIndexCollector", e);
			exceptionHandler(aSigner);
		}
	}
	
	private void exceptionHandler(Signer aSigner) {
		mAllCerts = aSigner.getBaseSignedData().getSignedData().getCertificates();
		mAllCrls = aSigner.getBaseSignedData().getSignedData().getCRLs();
		List<EBasicOCSPResponse> basicOcspsAll = new ArrayList<EBasicOCSPResponse>();
		for (EOCSPResponse eocspResponse : aSigner.getBaseSignedData().getSignedData().getOSCPResponses()) {
			basicOcspsAll.add(eocspResponse.getBasicOCSPResponse());
		}
		mAllOcsps = basicOcspsAll;
	}
	
	private List<ECertificate> _checkCerts(Signer aSigner, EATSHashIndex atsHashIndex) throws ESYAException {
		List<ECertificate> removeCerts = new ArrayList<ECertificate>();
		List<Asn1OctetString> certsHash = Arrays.asList(atsHashIndex .getCertificatesHashIndex());
		DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atsHashIndex.gethashIndAlgorithm());

		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		Asn1OctetString hash = null;
		SignedData signedData = aSigner.getBaseSignedData().getSignedData().getObject();

		if (signedData.certificates != null && signedData.certificates.elements != null) {
			for (CertificateChoices cert : signedData.certificates.elements) {
				if (cert != null) {
					cert.encode(encBuf);
					hash = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.getMsgCopy()));
					encBuf.reset();
					if (!certsHash.contains(hash)) {
						removeCerts.add(new ECertificate((Certificate) cert.getElement()));
					}
				}
			}
		}
		return removeCerts;
	}

	private List<ECRL> _checkCrls(Signer aSigner, EATSHashIndex atsHashIndex) throws ESYAException {
		List<ECRL> removeCrls = new ArrayList<ECRL>();
		List<Asn1OctetString> crlsHash = Arrays.asList(atsHashIndex.getCrlsHashIndex());
		DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atsHashIndex.gethashIndAlgorithm());

		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		Asn1OctetString hash = null;
		SignedData signedData = aSigner.getBaseSignedData().getSignedData().getObject();

		if (signedData.crls != null && signedData.crls.elements != null) {
			for (RevocationInfoChoice crl : signedData.crls.elements) {
				if (crl != null && crl.getChoiceID() == RevocationInfoChoice._CRL) {
					crl.encode(encBuf);
					hash = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.getMsgCopy()));
					encBuf.reset();
					if (!crlsHash.contains(hash)) {
						removeCrls.add(new ECRL((CertificateList) crl.getElement()));
					}
				}
			}
		}
		return removeCrls;
	}

	private List<EBasicOCSPResponse> _checkOcsps(Signer aSigner,EATSHashIndex atsHashIndex) throws ESYAException {
		List<EOCSPResponse> removeOcsps = new ArrayList<EOCSPResponse>();
		List<Asn1OctetString> crlsHash = Arrays.asList(atsHashIndex.getCrlsHashIndex());
		DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atsHashIndex.gethashIndAlgorithm());

		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		Asn1OctetString hash = null;
		SignedData signedData = aSigner.getBaseSignedData().getSignedData().getObject();

		List<EBasicOCSPResponse> basicOcsps = new ArrayList<EBasicOCSPResponse>();
		if (signedData.crls != null && signedData.crls.elements != null) {
			for (RevocationInfoChoice crl : signedData.crls.elements) {
				if (crl != null&& crl.getChoiceID() == RevocationInfoChoice._OTHER) {
					crl.encode(encBuf);
					hash = new Asn1OctetString(DigestUtil.digest(digestAlg,encBuf.getMsgCopy()));
					encBuf.reset();
					if (!crlsHash.contains(hash)) {
						OtherRevocationInfoFormat format = (OtherRevocationInfoFormat) crl.getElement();
						if (format.otherRevInfoFormat.equals(new Asn1ObjectIdentifier(_cmsValues.id_ri_ocsp_response))) {
							removeOcsps.add(new EOCSPResponse(format.otherRevInfo.value));
						} else if (format.otherRevInfoFormat.equals(new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_basic))) {
							basicOcsps.add(new EBasicOCSPResponse(format.otherRevInfo.value));
						}
					}
				}
			}
		}

		if (removeOcsps != null && removeOcsps.size() != 0) {
			for (EOCSPResponse eocspResponse : removeOcsps) {
				basicOcsps.add(eocspResponse.getBasicOCSPResponse());
			}
		}
		return basicOcsps;
	}
}