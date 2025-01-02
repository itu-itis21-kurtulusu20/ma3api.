package tr.gov.tubitak.uekae.esya.api.xmlsignature.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolverFromCertStore;
import tr.gov.tubitak.uekae.esya.api.signature.certval.match.CRLCriteriaMatcher;
import tr.gov.tubitak.uekae.esya.api.signature.certval.match.CertificateCriteriaMatcher;
import tr.gov.tubitak.uekae.esya.api.signature.certval.match.OCSPResponseCriteriaMatcher;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.CertValidationData;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.KeyInfoElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.RetrievalMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509CRL;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509Certificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509DataElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509IssuerSerial;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509SKI;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509SubjectName;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CRLReference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CertificateValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteCertificateRefs;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteRevocationRefs;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.OCSPReference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RevocationValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public class ValidationDataCollector {
    private static final Logger logger = LoggerFactory.getLogger(ValidationDataCollector.class);

    // todo make false every time before commit
    private boolean debugWriteRevocationData = false;
    private String baseDir;
    private ValidationInfoResolver referenceResolver;

    public ValidationResult collect(XMLSignature aSignature, boolean resolveReferencesFromExternalSources) {

        if (resolveReferencesFromExternalSources)
            referenceResolver = new ValidationInfoResolverFromCertStore();
        else
            referenceResolver = new ValidationInfoResolver();

        baseDir = aSignature.getContext().getBaseURIStr();

        ValidationResult r1 = fillValidationDataFromKeyInfo(aSignature);
        if ((r1 != null) && (r1.getType() != ValidationResultType.VALID)) {
            return r1;
        }
        ValidationResult r2 = fillValidationDataFromSignatureProperties(aSignature);
        if ((r2 != null) && (r2.getType() != ValidationResultType.VALID)) {
            return r2;
        }

        // collect validation data from counter signatures
        for (XMLSignature counterSignature : aSignature.getAllCounterSignatures()) {
            ValidationResult r3 = collect(counterSignature, resolveReferencesFromExternalSources);
            if ((r3 != null) && (r3.getType() != ValidationResultType.VALID)) {
                return r3;
            }
        }

        return null;

    }

    public ValidationResult fillValidationDataFromKeyInfo(XMLSignature aSignature) {
        CertValidationData vdata = aSignature.getContext().getValidationData(aSignature);
        try {
            KeyInfo ki = aSignature.getKeyInfo();
            for (int i = 0; i < ki.getElementCount(); i++) {
                KeyInfoElement o = ki.get(i);

                if (o instanceof X509Data) {
                    X509Data data = (X509Data) o;
                    boolean criteriaSet = false;
                    ECertificate certificate = null;
                    CertificateSearchCriteria criteria = new CertificateSearchCriteria();
                    for (int j = 0; j < data.getElementCount(); j++) {
                        X509DataElement xde = data.get(j);
                        if (xde instanceof X509Certificate) {
                            X509Certificate cert = (X509Certificate) xde;
                            certificate = new ECertificate(cert.getCertificateBytes());
                            vdata.addCertificate(certificate);
                            writeResourceForDebug(certificate, "keyInfo" + i);
                        } else if (xde instanceof X509CRL) {
                            X509CRL xcrl = (X509CRL) xde;
                            vdata.addCRL(new ECRL(xcrl.getCRLBytes()));
                        } else if (xde instanceof X509IssuerSerial) {
                            X509IssuerSerial xis = (X509IssuerSerial) xde;
                            criteria.setIssuer(xis.getIssuerName());
                            criteria.setSerial(xis.getSerialNumber());
                            criteriaSet = true;
                        } else if (xde instanceof X509SKI) {
                            X509SKI xski = (X509SKI) xde;
                            criteria.setSubjectKeyIdentifier(xski.getSKIBytes());
                            criteriaSet = true;
                        } else if (xde instanceof X509SubjectName) {
                            X509SubjectName xsn = (X509SubjectName) xde;
                            criteria.setSubject(xsn.getSubjectName());
                            criteriaSet = true;
                        }
                    }

                    /*
                   Any X509IssuerSerial, X509SKI, and X509SubjectName elements that appear MUST
                   refer to the certificate or certificates containing the validation key. All
                   such elements that refer to a particular individual certificate MUST be grouped
                   inside a single X509Data element and if the certificate to which they refer
                   appears, it MUST also be in that X509Data element.
                    */
                    if (criteriaSet) {
                        if (certificate == null) {
                            List<ECertificate> certs = referenceResolver.resolve(criteria);
                            logger.error("Cant find certificate referenced in KeyInfo");
                            vdata.addCertificate(certs.get(0));
                            writeResourceForDebug(certs.get(0), "keyInfo_found_" + i);
                        } else {
                            // todo !
                            /*
                            CertificateCriteriaMatcher matcher = new CertificateCriteriaMatcher();
                            if (!matcher.match(criteria, certificate)){
                                logger.error("Certificate referenced in KeyInfo does not match "+criteria);
                                throw new XMLSignatureException("Certificate referenced in KeyInfo does not metch "+criteria);
                            } */
                        }
                    }

                } else if (o instanceof RetrievalMethod) {
                    // retrieval metod raxX509 a map eder
                    ECertificate cert = new ECertificate(((RetrievalMethod) o).getRawX509());
                    vdata.addCertificate(cert);
                    writeResourceForDebug(cert, "keyInfo_retrieved_" + i);
                }

            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return new ValidationResult(ValidationResultType.INCOMPLETE,
                                        I18n.translate("validation.check.keyInfo"),
                                        I18n.translate("core.invalidKeyInfo"),
                                        e.getMessage(), getClass());
        }
        return null;
    }

    public ValidationResult fillValidationDataFromSignatureProperties(XMLSignature aSignature) {

        CertValidationData vdata = aSignature.getContext().getValidationData(aSignature);

        QualifyingProperties qp = aSignature.getQualifyingProperties();
        if (qp != null) {
            UnsignedSignatureProperties usp = qp.getUnsignedSignatureProperties();
            if (usp != null) {

                // timestamp validation datas
                aSignature.getContext().getValidationData(aSignature).getTSValidationData().putAll(usp.getAllTimeStampValidationData());

                // cert values
                CertificateValues certificateValues = usp.getCertificateValues();
                if (certificateValues != null) {
                    for (int j = 0; j < certificateValues.getCertificateCount(); j++) {
                        try {
                            writeResourceForDebug(certificateValues.getCertificate(j), "cert_value_" + j);
                            vdata.addCertificate(certificateValues.getCertificate(j));
                        } catch (Exception e) {
                            logger.warn(e.getMessage(), e);
                            return new ValidationResult(
                                    ValidationResultType.INCOMPLETE,
                                    I18n.translate("validation.check.keyInfo"),
                                    I18n.translate("validation.data.cantConstruct", j, "certificate", "CertificateValues"),
                                    null, ValidationDataCollector.class);
                        }
                    }
                }

                // cert refs
                CompleteCertificateRefs certRefs = usp.getCompleteCertificateRefs();
                if (certRefs != null) {

                    CertificateCriteriaMatcher matcher = new CertificateCriteriaMatcher();

                    for (int i = 0; i < certRefs.getCertificateReferenceCount(); i++) {
                        CertID certID = certRefs.getCertificateReference(i);

                        boolean found = false;
                        CertificateSearchCriteria csc = certID.toSearchCriteria();
                        for (int k = 0; k < vdata.getCertificates().size(); k++) {
                            if (matcher.match(csc, vdata.getCertificates().get(k))) {
                                found = true;
                                break;
                            }
                        }
                        if (found)
                            continue;

                        String uri = certID.getURI();
                        if (uri != null && uri.length() > 0) {
                            try {
                                Document doc = Resolver.resolve(uri, aSignature.getContext());
                                ECertificate cert = new ECertificate(doc.getBytes());

                                if (!matcher.match(certID.toSearchCriteria(), cert)) {
                                    writeResourceForDebug(cert, "ref_mismatch_uri_" + uri);
                                    return new ValidationResult(ValidationResultType.INVALID,
                                                                I18n.translate("validation.check.keyInfo"),
                                            I18n.translate("validation.data.resolutionMismatch", "certificate", uri, "CertID", certID),
                                            null, ValidationDataCollector.class);
                                } else {
                                    writeResourceForDebug(cert, "ref_uri_" + uri);
                                }
                                vdata.addCertificate(cert);
                            } catch (Exception e) {
                                // todo Encapsulated Cert cozumu problemli...
                                logger.warn("Cant resolve certificate / issuer: " + certID.getX509IssuerName() + "; serial=" + certID.getX509SerialNumber() + " from URI: '" + uri + "'");
                                logger.warn(e.getMessage(), e);
                                //return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve certificate / issuer: " + certID.getX509IssuerName() + "; serial=" + certID.getX509SerialNumber() + " from URI: '" + uri + "'");
                            }
                            continue;
                        }
                        try {

                            List<ECertificate> foundCerts = referenceResolver.resolve(certID.toSearchCriteria());
                            if (foundCerts != null && foundCerts.size() == 1) {
                                writeResourceForDebug(foundCerts.get(0), "ref_found_" + i);
                                vdata.addCertificate(foundCerts.get(0));
                                continue;
                            }
                        } catch (Exception x) {
                            logger.warn("Error occurred searching cert ", x);
                        }
                        // todo timestamp sertifikalarına bakmadan burada hata dönülemez...
                        logger.warn("Cant resolve certificate / issuer: " + certID.getX509IssuerName() + "; serial=" + certID.getX509SerialNumber());
                        //return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve certificate / issuer: " + certID.getX509IssuerName() + "; serial=" + certID.getX509SerialNumber());
                    }
                }


                RevocationValues revocationValues = usp.getRevocationValues();
                if (revocationValues != null) {
                    for (int r = 0; r < revocationValues.getCRLValueCount(); r++) {
                        try {
                            writeResourceForDebug(revocationValues.getCRL(r), "rev_value_" + r);
                            vdata.addCRL(revocationValues.getCRL(r));
                        } catch (Exception e) {
                            logger.warn(e.getMessage(), e);
                            return new ValidationResult(ValidationResultType.INCOMPLETE,
                                                        I18n.translate("validation.check.keyInfo"),
                                    I18n.translate("validation.data.cantConstruct", r, "CRL", "RevocationValues"),
                                    null, ValidationDataCollector.class);
                        }
                    }
                    for (int s = 0; s < revocationValues.getOCSPValueCount(); s++) {
                        try {
                            writeResourceForDebug(revocationValues.getOCSPResponse(s), "rev_value_" + s);
                            vdata.addOCSPResponse(revocationValues.getOCSPResponse(s));
                        } catch (Exception e) {
                            logger.warn(e.getMessage(), e);
                            return new ValidationResult(ValidationResultType.INCOMPLETE,
                                                        I18n.translate("validation.check.keyInfo"),
                                    I18n.translate("validation.data.cantConstruct", s, "OCSP", "RevocationValues"),
                                    null, ValidationDataCollector.class);
                        }
                    }
                }

                // revocation refs
                CompleteRevocationRefs revocationRefs = usp.getCompleteRevocationRefs();

                if (revocationRefs != null) {
                    OCSPResponseCriteriaMatcher ocspMatcher = new OCSPResponseCriteriaMatcher();
                    CRLCriteriaMatcher crlMatcher = new CRLCriteriaMatcher();
                    // ocsp refs
                    for (int k = 0; k < revocationRefs.getOCSPReferenceCount(); k++) {
                        OCSPReference ocspRef = revocationRefs.getOCSPReference(k);
                        String uri = ocspRef.getOCSPIdentifier().getURI();
                        if (uri != null && uri.length() > 0) {
                            try {
                                Document doc = Resolver.resolve(uri, aSignature.getContext());
                                EOCSPResponse ocsp = new EOCSPResponse(doc.getBytes());
                                if (!ocspMatcher.match(ocspRef.toSearchCriteria(), ocsp)) {
                                    writeResourceForDebug(ocsp, "ref_mismatch_uri_" + uri);
                                    return new ValidationResult(ValidationResultType.INVALID,
                                                                I18n.translate("validation.check.keyInfo"),
                                            I18n.translate("validation.data.resolutionMismatch", "OCSP", uri, "OCSPRef", ocspRef),
                                            null, ValidationDataCollector.class);
                                } else {
                                    writeResourceForDebug(ocsp, "ref_uri_" + uri);
                                }

                                vdata.addOCSPResponse(ocsp);
                            } catch (Exception e) {
                                // todo
                                logger.warn("Cant resolve ocsp response " + ocspRef + " from URI: '" + uri + "'");
                                logger.warn(e.getMessage(), e);
                                //return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve ocsp response " + ocspRef + " from URI: '" + uri + "'");
                            }
                            continue;
                        }
                        try {
                            // todo bulunan resp reference ile uyumlu mu, resp bulma
                            List<EOCSPResponse> found = referenceResolver.resolve(ocspRef.toSearchCriteria());
                            if (found != null && found.size() == 1) {
                                writeResourceForDebug(found.get(0), "ref_found_" + k);
                                vdata.addOCSPResponse(found.get(0));
                                continue;
                            }
                        } catch (Exception x) {
                            logger.warn("Error occurred searching ocsp response ", x);
                        }
                        // todo 
                        logger.warn("Cant resolve ocsp response " + ocspRef);
                        //return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve ocsp response " + ocspRef);
                    }

                    // crl refs
                    for (int m = 0; m < revocationRefs.getCRLReferenceCount(); m++) {
                        CRLReference crlReference = revocationRefs.getCRLReference(m);
                        String uri = crlReference.getCRLIdentifier().getURI();
                        if (uri != null && uri.length() > 0) {
                            try {
                                Document doc = Resolver.resolve(uri, aSignature.getContext());
                                ECRL crl = new ECRL(doc.getBytes());
                                vdata.addCRL(crl);
                                if (!crlMatcher.match(crlReference.toSearchCriteria(), crl)) {
                                    logger.error("Resolved CRL (uri='" + uri + "') mismatch CRLRef: " + crlReference);
                                    writeResourceForDebug(crl, "ref_mismatch_uri_" + uri);
                                    return new ValidationResult(ValidationResultType.INVALID,
                                                                I18n.translate("validation.check.keyInfo"),
                                            I18n.translate("validation.data.resolutionMismatch", "CRL", uri, "CRLRef", crlReference),
                                            null, ValidationDataCollector.class);
                                } else {
                                    writeResourceForDebug(crl, "ref_uri_" + uri);
                                }
                            } catch (Exception e) {
                                logger.debug("Cant resolve CRL / " + crlReference + " from URI: '" + uri + "'");
                                logger.debug(e.getMessage(), e);
                                //return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve CRL / " + crlReference + " from URI: '" + uri + "'");
                            }
                            continue;
                        }
                        try {
                            // todo bulunan crl reference ile uyumlu mu, crl bulma
                            List<ECRL> found = referenceResolver.resolve(crlReference.toSearchCriteria());
                            if (found != null && found.size() == 1) {
                                writeResourceForDebug(found.get(0), "ref_found_" + m);
                                vdata.addCRL(found.get(0));
                                continue;
                            }
                        } catch (Exception x) {
                            logger.warn("Error occurred searching crl ", x);
                        }
                        // todo crlin elde olmaması mantıklı olabilir?
                        logger.warn("Cant resolve crl / " + crlReference);
                        //return new ValidationResult(ValidationResultType.INCOMPLETE, "Cant resolve crl / " + crlReference);
                    }
                }


            }
        }
        return null;
    }

    public List<ECertificate> findCertificatesFromReferences(XMLSignature aSignature) {

        List<ECertificate> certificateList = new ArrayList<ECertificate>();

        CompleteCertificateRefs completeCertificateRefs = aSignature.getQualifyingProperties().getUnsignedSignatureProperties().getCompleteCertificateRefs();
        if (completeCertificateRefs != null) {
            for (int i = 0; i < completeCertificateRefs.getCertificateReferenceCount(); i++) {
                certificateList.addAll(referenceResolver.resolve(completeCertificateRefs.getCertificateReference(i).toSearchCriteria()));
            }
        }
        return certificateList;
    }

    public List<ECRL> findCRLsFromReferences(XMLSignature aSignature) {

        List<ECRL> crlList = new ArrayList<ECRL>();

        CompleteRevocationRefs completeRevocationRefs = aSignature.getQualifyingProperties().getUnsignedSignatureProperties().getCompleteRevocationRefs();

        if (completeRevocationRefs != null) {
            for (int i = 0; i < completeRevocationRefs.getCRLReferenceCount(); i++) {
                crlList.addAll(referenceResolver.resolve(completeRevocationRefs.getCRLReference(i).toSearchCriteria()));
            }
        }

        return crlList;
    }

    public List<EOCSPResponse> findOCSResponsesFromReferences(XMLSignature aSignature) {

        List<EOCSPResponse> ocspResponseList = new ArrayList<EOCSPResponse>();

        CompleteRevocationRefs completeRevocationRefs = aSignature.getQualifyingProperties().getUnsignedSignatureProperties().getCompleteRevocationRefs();

        if (completeRevocationRefs != null) {
            for (int i = 0; i < completeRevocationRefs.getOCSPReferenceCount(); i++) {
                ocspResponseList.addAll(referenceResolver.resolve(completeRevocationRefs.getOCSPReference(i).toSearchCriteria()));
            }
        }
        return ocspResponseList;
    }

    private void writeResourceForDebug(BaseASNWrapper resource, String aFileName) {
        if (debugWriteRevocationData) {
            if (aFileName == null) {
                aFileName = System.currentTimeMillis() + "";
            }
            try {
                AsnIO.dosyayaz(resource.getEncoded(), baseDir + "\\debug_" + aFileName + "." + resource.getClass().getSimpleName());
            } catch (Exception e) {
                logger.error("Error in ValidationDataCollector", e);
            }
        }
    }
}
