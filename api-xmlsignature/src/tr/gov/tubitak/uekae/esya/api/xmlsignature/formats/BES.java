package tr.gov.tubitak.uekae.esya.api.xmlsignature.formats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Finder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp.OCSPResponseFinderFromAIA;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.ECAlgorithmUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.MobileSigner;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ValidationConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.ValidationDataCollector;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.CertRevInfoExtractor;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.UniqueCertRevInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlSignUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.Validator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

/**
 * A Basic Electronic Signature (XAdES-BES) will build on a XMLDSIG by
 * incorporating qualifying properties defined in the present document. They
 * will be added to XMLDSIG within one <code>ds:Object</code> acting as the bag
 * for the whole set of qualifying properties.
 * <p>
 * <p>Some properties defined for building up this form will be covered by the
 * signer's signature (signed qualifying information grouped within one new
 * element, <code>SignedProperties</code>, Other properties will be not covered
 * by the signer's signature (unsigned qualifying information grouped within
 * one new element, <code>UnsignedProperties</code>.
 * <p>
 * <p>In a XAdES-BES the signature value SHALL be computed in the usual way of
 * XMLDSIG over the data object(s) to be signed and on the whole set of signed
 * properties when present (<code>SignedProperties</code> element).
 * <p>
 * <p>For this form it is mandatory to protect the signing certificate with the
 * signature, in one of the two following ways:
 * <ul>
 * <li>either incorporating the SigningCertificate signed property; or
 * <li>not incorporating the SigningCertificate but incorporating the signing
 * certificate within the <code>ds:KeyInfo</code> element and signing at least
 * the signing certificate.
 * </ul>
 * <p>
 * <p>A XAdES-BES signature MUST, in consequence, contain at least one of the
 * following elements with the specified contents:
 * <p>
 * <ul>
 * <li>The SigningCertificate signed property. This property MUST contain the
 * reference and the digest value of the signing certificate. It MAY contain
 * references and digests values of other certificates (that MAY form a chain
 * up to the point of trust). In the case of ambiguities identifying the actual
 * signer's certificate the applications SHOULD include the
 * <code>SigningCertificate</code> property.
 * <p>
 * <li>The <code>ds:KeyInfo</code> element. If <code>SigningCertificate</code>
 * is present in the signature, no restrictions apply to this element. If
 * <code>SigningCertificate</code> element is not present in the signature,
 * then the following restrictions apply:
 * <p>
 * <ul>
 * <li>the <code>ds:KeyInfo</code> element MUST include a
 * <code>ds:X509Data</code> containing the signing certificate;
 * <p>
 * <li>the <code>ds:KeyInfo</code> element also MAY contain other certificates
 * forming a chain that MAY reach the point of trust;
 * <p>
 * <li>the <code>ds:SignedInfo</code> element MUST contain a
 * <code>ds:Reference</code> element referencing <code>ds:KeyInfo</code>. That
 * <code>ds:Reference</code> element SHALL be built in such a way that at least
 * the signing certificate is actually signed.
 * </ul>
 * </ul>
 * <p>
 * <p>NOTE 1: Signing the whole <code>ds:KeyInfo</code>, readers are warned that
 * this locks the element: any addition of a certificate or validation data
 * would make signature validation fail. Applications may, alternatively, use
 * XPath transforms for signing at least the signing certificate, leaving the
 * <code>ds:KeyInfo</code> element open for addition of new data after signing.
 * <p>
 * <p>By incorporating one of these elements, XAdES-BES prevents the simple
 * substitution of the signer's certificate.
 * <p>
 * <p>A XAdES-BES signature MAY also contain the following properties:
 * <ul>
 * <li>the SigningTime signed property;
 * <li>the DataObjectFormat signed property;
 * <li>the CommitmentTypeIndication signed property;
 * <li>the SignerRole signed property;
 * <li>the SignatureProductionPlace signed property;
 * <li>one or more <code>IndividualDataObjectsTimeStamp</code> or
 * <code>AllDataObjectTimeStamp</code> signed properties;
 * <li>one or more CounterSignature unsigned properties.
 * </ul>
 * <p>
 * <p>NOTE 2: The XAdES-BES is the minimum format for an electronic signature
 * to be generated by the signer. On its own, it does not provide enough
 * information for it to be verified in the longer term. For example,
 * revocation information issued by the relevant certificate status information
 * issuer needs to be available for long term validation.
 * <p>
 * <p>The XAdES-BES satisfies the legal requirements for electronic signatures
 * as defined in the European Directive on electronic signatures. It provides
 * basic authentication and integrity protection.
 * <p>
 * <p>The semantics of the signed data of a XAdES-BES or its context may
 * implicitly indicate a signature policy to the verifier.
 *
 * @author ahmety
 *         date: Apr 20, 2009
 */
public class BES extends XMLDSig implements SignatureFormat
{
    /*
     *  Bir Imzanin BES olabilmesi için
     *      1. a. SigningCertificate signed propertisi olacak
     *         b. ya da ds:KeyInfo elementi içinde sertifika olacak ve imzalanacak
     *
     * optional :
     *      SigninTime
     *      DataObjectFormat
     *      CommitmentType
     *      SignerRole
     *      SignatureProductionPlace signed popertileri
     *      1 yada daha fazla IndividualDataObjectsTimeStamp
     *                   veya AllDataObjectTimeStamp signed
     *      1 yada daha fazla CounterSignature unsigned propertileri
    */
    private static final Logger logger = LoggerFactory.getLogger(BES.class);
    private CertRevInfoExtractor extractor = new CertRevInfoExtractor();


    public BES(Context aContext, XMLSignature aSignature)
    {
        super(aContext, aSignature);
    }

    @Override
    public void addKeyInfo(ECertificate aCertificate)
    {
        super.addKeyInfo(aCertificate);
        // object

        SigningCertificate certList = new SigningCertificate(mContext);

        DigestMethod dm = null;
        try {
            dm = mContext.getConfig().getAlgorithmsConfig().getDigestMethod();
            certList.addCertID(new CertID(mContext, aCertificate, dm));

        } catch (Exception ux){
            throw new XMLSignatureRuntimeException(ux, "Cant add CertID");
        }

        mSignature.createOrGetQualifyingProperties()
                .getSignedProperties()
                .getSignedSignatureProperties()
                .setSigningCertificate(certList);
    }

    @Override
    public void addKeyInfo(PublicKey pk) throws XMLSignatureException
    {
        throw new XMLSignatureException("core.etsiRequiresCertificate");
    }

    /*
    @Override
    public XMLSignature sign(ECertificate aCertificate,
                             CardType aCardType, char[] aPass)
            throws XMLSignatureException
    {
        // digest references first, otherwise signature value changes!
        digestReferences(mSignature.getSignedInfo());

        // signature value
        fillSignatureValue(aCertificate, aCardType, aPass);

        return mSignature;
    }*/

    /*
    The normative part of the present document states that the CertificateValues
    element "contains the full set of certificates that have been used to
    validate the electronic signature, including the signer's certificate"
    except those ones that are already present within the ds:KeyInfo element. In
    consequence, if the XAdES signature contains the CertificateValues property,
    then the verifier should use this property and the ds:KeyInfo for getting
    all the certificates required for performing the validation. The verifier
    should also check that the contents of these two elements actually form a
    valid certification path. If not, the verifier should assume that the
    validation process has failed.

    If CertificateValues is not present but CompleteCertificateRefs is present,
    the verifier should get the certificates referenced there and check if they
    actually form a valid certification path. If not, the verifier should assume
    that the validation process has failed.

    If neither CertificateValues nor CompleteCertificateRefs are present, the
    specific means by which the verifier can get the certification path are out
    of scope of the present document.
    */

    @Override
    public SignatureValidationResult validateCore() throws XMLSignatureException
    {
        ECertificate certificate = extractCertificate();

        if (certificate == null) {
            logger.warn("Cant find certificate for xml signature. Halting verification");
            return new SignatureValidationResult(
                    ValidationResultType.INCOMPLETE,
                    I18n.translate("errors.cantFindCertificate"), null);
        }
        return validateCore(certificate);
    }

    public ECertificate extractCertificate() throws XMLSignatureException
    {
        ECertificate certificate = null;

        QualifyingProperties qp = mSignature.getQualifyingProperties();
        if (qp != null) {
            SignedProperties sp = qp.getSignedProperties();
            if (sp != null) {
                SignedSignatureProperties ssp = sp.getSignedSignatureProperties();

                /*
                todo burasi dogru degil. signing certificate validasyon icin
                gerekli ama certifikalarin tanimlandigi yer degil...
                */
                if (ssp != null) {
                    SigningCertificate sc = ssp.getSigningCertificate();
                    if (sc != null) {
                        CertID certId = resolveSigningCertificate(sc.getCertIDListCopy());
                        String uri = certId.getURI();
                        if (uri != null && uri.length() > 0) {
                            logger.info("Signing certificate attribute points '" + uri +
                                                  "' for certificate location. KeyInfo will be neglected.");
                            try {
                                Document cert = Resolver.resolve(uri, mContext);
                                certificate = new ECertificate(cert.getBytes());
                            }
                            catch (Exception e) {
                                logger.warn(I18n.translate("core.cantResolve.signingCertificateAttribute"), e);
                                //throw new XMLSignatureException("core.cantResolve.signingCertificateAttribute");
                            }
                        }
                    }
                }
            }
        }

        if (certificate == null)
            certificate = mSignature.getKeyInfo().resolveCertificate();

        return certificate;
    }

    @Override
    public XMLSignature sign(BaseSigner aSigner) throws XMLSignatureException {

        boolean isMobileSigner = aSigner instanceof MobileSigner;
        boolean validateCertificate = mContext.isValidateCertificateBeforeSign();

        if(!isMobileSigner){
            ECertificate certificate = extractCertificate();

            try {
                SignatureAlg signatureAlg = SignatureAlg.fromName(aSigner.getSignatureAlgorithmStr());
                ECAlgorithmUtil.checkKeyAndSigningAlgorithmConsistency(certificate, signatureAlg);
                ECAlgorithmUtil.checkDigestAlgForECCAlgorithm(certificate, signatureAlg);
            } catch (ESYAException e) {
                throw new XMLSignatureException(e, "Signature Algorithm and Certificate Signing Algorithm are incompatible");
            }

            if (validateCertificate) {
                validateCertificate(certificate);
            }
        }

        XMLSignature result = super.sign(aSigner);

        if(isMobileSigner){
            ECertificate certificate = extractCertificate();

            try {
                SignatureAlg signatureAlg = SignatureAlg.fromName(aSigner.getSignatureAlgorithmStr());
                ECAlgorithmUtil.checkDigestAlgForECCAlgorithm(certificate, signatureAlg);
            } catch (ESYAException e) {
                throw new XMLSignatureException(e, "Target digest algorithm length does not satisfy certificate signature algorithm length");
            }

            if (validateCertificate) {
                validateCertificate(certificate);
            }
        }

        return result;
    }

    public void validateCertificate(ECertificate certificate) throws XMLSignatureException {
        if (certificate == null)
            throw new XMLSignatureRuntimeException("Cant find certificate to validate before sign. Either turn this feature off, or call #addKeyInfo(Certificate) before signing.");

        CertificateStatusInfo csi = _validateCertificate(certificate, Calendar.getInstance(), true);
        if (csi.getCertificateStatus() != CertificateStatus.VALID) {
            logger.info("Cant validate certificate: " + csi.printDetailedValidationReport());
            throw new XMLSignatureException(I18n.translate("validation.certificate.cantValidateSigner", csi.getDetailedMessage()));
        }

        // Check Turkish Electronic Signature Profile attribute consistency before sign.
        // Signature type and revocation data consistency will be checked only at validation
        // using the class TurkishESigProfileValidator.
        try {

            if (mSignature.createOrGetQualifyingProperties().getSignedSignatureProperties().getSignaturePolicyIdentifier() != null) {
                List<Class<? extends Validator>> validators = new ArrayList<Class<? extends Validator>>(0);
                Class clazz = Class.forName("tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.TurkishESigProfileAttributeValidator");
                validators.add(clazz);
                for (Class<? extends Validator> validator : validators) {
                    Validator v = validator.newInstance();
                    ValidationResult vr = v.validate(mSignature, certificate);
                    if (vr.getResultType() != (tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType.VALID)) {
                        throw new XMLSignatureException(I18n.translate("validation.policy.cantValidatePolicy"));
                    }
                }
            }
        } catch (Exception e) {
            throw new XMLSignatureException(e, "validation.policy.cantValidatePolicy");
        }
    }

    @Override
    public tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureValidationResult validateCore(Key aKey) throws XMLSignatureException
    {
        throw new XMLSignatureException("core.etsiRequiresCertificate");
    }

    @Override
    public SignatureValidationResult validateCore(ECertificate aCertificate) throws XMLSignatureException
    {
        checkLAtValidation(aCertificate);

        PublicKey key;
        try {
            key = KeyUtil.decodePublicKey(aCertificate.getSubjectPublicKeyInfo());
        }
        catch (CryptoException c) {
            throw new XMLSignatureException(c, "errors.cantDecode", "PublicKey");
        }
        SignatureValidationResult result = super.validateCore(key);
        if (!result.getType().equals(ValidationResultType.VALID))
            return result;

        // construct validationSystem
        if (mContext.isValidateCertificates()) {

            boolean resolveExternalReferencesOfSignature = true;
            SignatureType st = mSignature.getSignatureType();
            if (st==SignatureType.XAdES_X_L || st==SignatureType.XAdES_A){
                resolveExternalReferencesOfSignature = !mContext.getConfig().getValidationConfig().isForceStrictReferences();
            }

            boolean useExternalResourcesAtCertValidation = true;
            if (st==SignatureType.XAdES_C || st==SignatureType.XAdES_X || st==SignatureType.XAdES_X_L || st==SignatureType.XAdES_A){
                useExternalResourcesAtCertValidation = !mContext.getConfig().getValidationConfig().isForceStrictReferences();
            }

            ValidationDataCollector collector = new ValidationDataCollector();

            ValidationResult r1 = collector.collect(mSignature, resolveExternalReferencesOfSignature);
            if ((r1 != null) && (r1.getType() != ValidationResultType.VALID)) {
                result.setStatus(r1.getType(), r1.getMessage());
                return result;
            }

            ValidationResult vr = validateCertificate(aCertificate, useExternalResourcesAtCertValidation, result);
            if (vr.getType() != ValidationResultType.VALID) {
                result.setStatus(vr.getType(), vr.getMessage());
                return result;
            }
        }

        List<Validator> defaultValidators = mContext.getConfig().getValidationConfig().getProfile(mSignature.getSignatureType()).createValidators();
        List<Validator> validators = new ArrayList<Validator>(mContext.getValidators());

        validators.addAll(defaultValidators);

        for (Validator validator : validators) {
            ValidationResult item = validator.validate(mSignature, aCertificate);
            if (item != null) {
                //item.setVerifierClass(validator.getName());
                item.setVerifierClass(validator.getClass());
                result.addItem(item);
                if ((item.getType() == ValidationResultType.INVALID) || (item.getType() == ValidationResultType.INCOMPLETE)) {
                    result.setStatus(item.getType(), item.getMessage());
                    return result;
                }
            }
        }

        // verify counter signatures
        for (XMLSignature counterSignature : mSignature.getAllCounterSignatures()) {
            ValidationResult vr = counterSignature.verify();
            result.addItem(vr);
            if ((vr.getType() == ValidationResultType.INVALID) || (vr.getType() == ValidationResultType.INCOMPLETE)) {
                result.setStatus(vr.getType(), vr.getMessage());
                return result;
            }
        }

        logger.info("BES core verification is OK.");
        return result;
    }

    private void checkLAtValidation(ECertificate aCertificate)
    {
        try {
            boolean isTest = LV.getInstance().isTL(LV.Urunler.XMLIMZA);
            if (isTest)
                if (!aCertificate.getSubject().getCommonNameAttribute().toLowerCase().contains("test")) {
                    throw new ESYARuntimeException("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
                }
        }
        catch (LE e) {
            logger.error("Lisans kontrolu basarisiz.", e);
            throw new ESYARuntimeException("Lisans kontrolu basarisiz.", e);
        }
    }

    private ValidationResult validateCertificate(ECertificate aCertificate, boolean useExternalResources, SignatureValidationResult svr)
            throws XMLSignatureException
    {
        try {

            CertificateStatusInfo csi = _validateCertificate(aCertificate, mContext.getValidationTime(mSignature), useExternalResources);
            mContext.setValidationResult(mSignature, csi);
            ValidationResult vr;

            if (csi.getCertificateStatus() == CertificateStatus.VALID) {
                vr = new ValidationResult(ValidationResultType.VALID,
                                          I18n.translate("validation.check.certificate"),
                                          I18n.translate("validation.certificate.validatedSigner"),
                                          XmlSignUtil.verificationInfo(csi), XMLSignature.class);
            }
            else {
                vr = new ValidationResult(ValidationResultType.INVALID,
                                          I18n.translate("validation.check.certificate"),
                                          I18n.translate("validation.certificate.cantValidateSigner", aCertificate.getSubject().stringValue()) + "\n"
                                            + csi.getDetailedMessage(),
                                          XmlSignUtil.verificationInfo(csi), XMLSignature.class);
            }
            svr.setCertificateStatusInfo(csi);
            svr.addItem(vr);
            return vr;
        }
        catch (Exception x) {
            logger.warn(x.getMessage(), x);
            return new ValidationResult(ValidationResultType.INCOMPLETE,
                                        I18n.translate("validation.check.certificate"), // todo 18n below line!
                                        "Cant validate certificate " + aCertificate.getSubject().stringValue() + "," + aCertificate.getSerialNumber() + " " + x.getMessage(),
                                        null, XMLSignature.class);
        }
    }


    protected CertificateStatusInfo _validateCertificate(ECertificate aCertificate, Calendar aValidationTime, boolean useExternalResources)
            throws XMLSignatureException
    {
        CertValidationData vdata = mContext.getValidationData(mSignature);
        ValidationConfig config = mContext.getConfig().getValidationConfig();

        ValidationSystem vs = mContext.getCertValidationSystem(aCertificate,useExternalResources, mSignature);

        vs.setUserInitialCertificateSet(vdata.getCertificates());
        vs.setUserInitialCRLSet(vdata.getCrls());
        vs.setUserInitialOCSPResponseSet(vdata.getOcspResponses());

        DigestAlg digestAlgO = mContext.getConfig().getAlgorithmsConfig().getDigestAlgForOCSP();
		if (digestAlgO != null) {
			List<RevocationChecker> rvCheckers = vs.getCheckSystem().getRevocationCheckers();
			for (RevocationChecker rvChecker : rvCheckers) {
				if (rvChecker instanceof RevocationFromOCSPChecker) {
					for (Finder ocspChecker : rvChecker.getFinders()) {
						if (ocspChecker instanceof OCSPResponseFinderFromAIA) {
							((OCSPResponseFinderFromAIA) ocspChecker).setDigestAlgForOcspFinder(digestAlgO);
						}
					}
				}
			}
		}

        Calendar now = Calendar.getInstance();
        Calendar graceTime = (Calendar) aValidationTime.clone();
        graceTime.add(Calendar.SECOND, config.getGracePeriodInSeconds());

        if (graceTime.before(now)) {

            Calendar lastRevocationTime = (Calendar) aValidationTime.clone();
            lastRevocationTime.add(Calendar.SECOND, config.getLastRevocationPeriodInSeconds());

            if (lastRevocationTime.after(aCertificate.getNotAfter())) {
                lastRevocationTime = max(aCertificate.getNotAfter(), graceTime);
            }

            vs.setBaseValidationTime(aValidationTime);
            vs.setLastRevocationTime(lastRevocationTime);
        }


        try {
            return CertificateValidation.validateCertificate(vs, aCertificate, config.isUseValidationDataPublishedAfterCreation());
        }
        catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    private Calendar max(Calendar time1, Calendar time2)
    {
        return time1.after(time2) ? time1 : time2;
    }


    /* todo resolve signing certificate from certificate chain
whatever exist in key info also included to this set ... */

    private CertID resolveSigningCertificate(List<CertID> aList)
    {
        return aList.get(0);
    }

    public XMLSignature createCounterSignature() throws XMLSignatureException
    {
        XMLSignature signature = new XMLSignature(mContext, false);
        SignatureMethod sm = mSignature.getSignatureMethod();
        signature.setSignatureMethod(sm);

        /*
        countersignature property is a XMLDSIG or XAdES signature whose
        ds:SignedInfo MUST contain one ds:Reference element referencing the
        ds:SignatureValue element of the embedding and countersigned XAdES
        signature. The content of the ds:DigestValue in the ds:Reference
        element of the countersignature MUST be the base-64 encoded digest of
        the complete (and canonicalized) ds:SignatureValue element (i.e.
        including the starting and closing tags) of the embedding and
        countersigned XAdES signature.
        */
        String sviURI = "#" + mSignature.getSignatureValueId();
        signature.addDocument(sviURI, null, null, null, Constants.REFERENCE_TYPE_COUNTER_SIGNATURE, false);

        QualifyingProperties qp = mSignature.createOrGetQualifyingProperties();
        qp.createOrGetUnsignedProperties().getUnsignedSignatureProperties().addCounterSignature(signature);
        return signature;
    }


    public SignatureFormat evolveToT() throws XMLSignatureException
    {
        TimestampConfig tsConfig = mContext.getConfig().getTimestampConfig();
        TSSettings settings = tsConfig.getSettings();

        // burayi kapattim, c14n'lisini kulliyorum
        //SignatureTimeStamp sts = new SignatureTimeStamp(mContext, mSignature, tsConfig.getDigestMethod(), settings);

        SignatureTimeStamp sts = new SignatureTimeStamp(mContext, mSignature, tsConfig.getC14nMethod(), tsConfig.getDigestMethod(), settings);

        mSignature.createOrGetQualifyingProperties().createOrGetUnsignedProperties().getUnsignedSignatureProperties().addSignatureTimeStamp(sts);

        return new T(mContext, mSignature);
    }

    public SignatureFormat evolveToC() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_BES, SignatureType.XAdES_C, SignatureType.XAdES_T);
    }

    public SignatureFormat evolveToX1() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_BES, SignatureType.XAdES_X, SignatureType.XAdES_T);
    }

    public SignatureFormat evolveToX2() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_BES, SignatureType.XAdES_X, SignatureType.XAdES_T);
    }

    public SignatureFormat evolveToXL() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_BES, SignatureType.XAdES_X_L, SignatureType.XAdES_T);
    }

    public SignatureFormat evolveToA() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_BES, SignatureType.XAdES_A, SignatureType.XAdES_T);
    }

    public SignatureFormat addArchiveTimeStamp() throws XMLSignatureException
    {
        throw new XMLSignatureException("error.formatCantAddArchiveTS");
    }

    // internal
    protected void addReferences(CertificateStatusInfo csi) throws XMLSignatureException
    {
        CompleteCertificateRefs certRefs = new CompleteCertificateRefs(mContext);
        CompleteRevocationRefs revRefs = new CompleteRevocationRefs(mContext);
        DigestMethod digestMethod = mContext.getConfig().getAlgorithmsConfig().getDigestMethod();
        boolean writeData = mContext.getConfig().getParameters().isWriteReferencedValidationDataToFileOnUpgrade();
        logger.info("Force reference write is set to : " + writeData);

        UniqueCertRevInfo ucri = extractor.trace(csi);

        int counter = 0;
        for (ECertificate cert: ucri.getCertificates()){
            if (counter != 0)
                certRefs.addCertificateReference(new CertID(mContext, cert, digestMethod));

            counter++;
        }

        for (ECRL crl : ucri.getCrls()){
            String uri = null;
            if (writeData)
            {
                uri = crl.getIssuer().stringValue() + "_" + crl.getThisUpdate().getTime() + ".crl";
                uri = writeToFile(crl.getEncoded(), uri);
            }
            revRefs.addCRLReference(new CRLReference(mContext, crl, digestMethod, uri));
        }

        for (EOCSPResponse ocsp : ucri.getOcspResponses()){
            String uri = null;
            if (writeData) {
                uri = ocsp.getBasicOCSPResponse().getTbsResponseData().getResponderIdByName().stringValue() + "_" + ocsp.getBasicOCSPResponse().getProducedAt().getTime() + ".ocsp";
                uri = writeToFile(ocsp.getEncoded(), uri);
            }
            revRefs.addOCSPReference(new OCSPReference(mContext, ocsp, digestMethod, uri));
        }

        QualifyingProperties qp = mSignature.createOrGetQualifyingProperties();
        UnsignedProperties up = qp.createOrGetUnsignedProperties();
        UnsignedSignatureProperties usp = up.getUnsignedSignatureProperties();
        usp.setCompleteCertificateRefs(certRefs);
        usp.setCompleteRevocationRefs(revRefs);
    }

    protected void addValidationData() throws XMLSignatureException
    {
	    CertificateStatusInfo csi = mContext.getValidationResult(mSignature);
	    if (csi==null){
	        ECertificate certificate = extractCertificate();
	        if (certificate==null)
	            throw new XMLSignatureException("validation.certificate.cantFound");

	        ValidationResult r1 = new ValidationDataCollector().collect(mSignature, true);

	        csi = _validateCertificate(certificate, mContext.getValidationTime(mSignature), true);
	        mContext.setValidationResult(mSignature, csi);
	    }

	    if (csi.getCertificateStatus()== CertificateStatus.VALID){
	        addValidationData(csi);
	    } else {
	        logger.warn(XmlSignUtil.verificationInfo(csi));
	        throw new XMLSignatureException("validation.certificate.cantValidateSigner");
	    }
	}

    protected void addValidationData(CertificateStatusInfo csi) throws XMLSignatureException
    {
        Pair<CertificateValues, RevocationValues> vd = extractor.extractValidationDataFromCSI(mContext, csi);

        QualifyingProperties qp = mSignature.createOrGetQualifyingProperties();
        UnsignedProperties up = qp.createOrGetUnsignedProperties();
        UnsignedSignatureProperties usp = up.getUnsignedSignatureProperties();
        usp.setCertificateValues(vd.getObject1());
        usp.setRevocationValues(vd.getObject2());
    }

    /*
    protected Pair<CertificateValues, RevocationValues> extractValidationData(CertificateStatusInfo csi)
        throws XMLSignatureException
    {
        CertificateValues certValues = new CertificateValues(mContext);
        RevocationValues revValues = new RevocationValues(mContext);
        //DigestMethod digestMethod = mContext.getConfig().getAlgorithmsConfig().getDigestMethod();

        UniqueCertRevInfo ucri = extractor.trace(csi);

        int counter = 0;
        for (ECertificate cert: ucri.getCertificates()){
            if (counter != 0)
                certValues.addCertificate(cert);

            counter++;
        }

        for (ECRL crl : ucri.getCrls()){
            revValues.addCRL(crl);
        }

        for (EOCSPResponse ocsp : ucri.getOcspResponses()){
            revValues.addOCSPResponse(ocsp);
        }

        return new Pair<CertificateValues, RevocationValues>(certValues, revValues);
    }  */

    // works for first timestamp!
    public void addTimestampValidationData(XAdESTimeStamp aXAdESTimeStamp, Calendar aValidationTime) throws XMLSignatureException
    {
        ValidationConfig validationConfig = mContext.getConfig().getValidationConfig();
        EncapsulatedTimeStamp ets = aXAdESTimeStamp.getEncapsulatedTimeStamp(0);

        TimeStampValidationData timeStampValidationData = mContext.getValidationData(mSignature).getTSValidationDataForTS(aXAdESTimeStamp);

        if (timeStampValidationData==null) { //if not already verified

            // verify timestamp
            byte[] input = ets.getContentInfo().getEncoded();
            SignedDataValidation sd = new SignedDataValidation();
            Hashtable<String, Object> params = new Hashtable<String, Object>();

            SignedDataValidationResult sdvr = null;

            try {
                //ValidationPolicy policy = validationConfig.getCertificateValidationPolicy();
                //ValidationPolicy policy = validationConfig.getCertValidationPolicies().getPolicyFor(CertValidationPolicies.CertificateType.TimeStampingCertificate);

                params.put(AllEParameters.P_CERT_VALIDATION_POLICIES, validationConfig.getCertValidationPolicies());

                params.put(AllEParameters.P_SIGNING_TIME, aValidationTime); // ets.getTime ?
                params.put(AllEParameters.P_GRACE_PERIOD, (long) validationConfig.getGracePeriodInSeconds());
                params.put(AllEParameters.P_IGNORE_GRACE, true);
                params.put(AllEParameters.P_REVOCINFO_PERIOD, (long) validationConfig.getLastRevocationPeriodInSeconds());

                //CertValidationData validationData = mContext.getValidationData(mSignature);

                params.putAll(extractor.collectAllInitialValidationDataFromContextAsParams(mContext));

                /*params.put(AllEParameters.P_INITIAL_CERTIFICATES, validationData.getCertificates());
                params.put(AllEParameters.P_INITIAL_CRLS, validationData.getCrls());
                params.put(AllEParameters.P_INITIAL_OCSP_RESPONSES, validationData.getOcspResponses());*/


                logger.debug("Validate timestamp " + aXAdESTimeStamp.getLocalName());
                sdvr = sd.verify(input, params);
                logger.debug(sdvr.toString());
                if (sdvr.getSDStatus() != SignedData_Status.ALL_VALID) {

                    boolean secondPass = false;
                    if (sdvr == null || (sdvr.getSDStatus() != SignedData_Status.ALL_VALID)) {
                        logger.debug("SignedData is not verified, try validation in timestamp time");

                        params.put(AllEParameters.P_SIGNING_TIME, ets.getTime());
                        sdvr = sd.verify(input, params);
                        secondPass = true;
                    }
                    if (sdvr.getSDStatus() != SignedData_Status.ALL_VALID) {
                        throw new XMLSignatureException("validation.timestamp.certificateNotValidated");
                    }
                }
            } catch (Exception e) {
                logger.error("Error in BES", e);
                throw new XMLSignatureException("validation.timestamp.certificateNotValidated", e);
            }

            //validate ts signer cerficate

            ESignedData tsSignedData = ets.getSignedData();
            List<ECertificate> tsCertificates = tsSignedData.getCertificates();
            if (tsCertificates == null || tsCertificates.size() == 0)
                throw new XMLSignatureException("validation.timestamp.certificateNotFound", aXAdESTimeStamp.getLocalName());
            //ECertificate signer = tsCertificates.get(0);

            CertificateStatusInfo csi = sdvr.getSDValidationResults().get(0).getCertStatusInfo();
            //_validateCertificate(signer, aValidationTime);
            if (csi.getCertificateStatus() != CertificateStatus.VALID) {
                logger.warn("Cant validate Timestamp signer certificate. ");
                logger.info(XmlSignUtil.verificationInfo(csi));
                throw new XMLSignatureException("validation.timestamp.certificateNotFound");
            }
            Pair<CertificateValues, RevocationValues> vd = extractor.extractValidationDataFromCSI(mContext, csi);
            vd = extractor.removeDuplicateReferences(mContext, mSignature, vd.getObject1(), vd.getObject2(), tsSignedData);

            timeStampValidationData = new TimeStampValidationData(mContext, vd.getObject1(), vd.getObject2());

        }   // verify ts certificaete


        //
        // burda Erbay'dan gelen bir duzenleme yaptim
        // yeni ts validation data'ya koyacağımız cert, ocsp veya crl daha oncekilerde varsa
        // remove duplicate ile kaldiriyorduk, ben de eger hic bisey kalmadiysa bos validation
        // data tag'i koymasin diye ayarladim
        //
        if( timeStampValidationData.getCertificateValues().getAllCertificates().size() != 0 ||
            timeStampValidationData.getRevocationValues().getAllOCSPResponses().size() != 0 ||
            timeStampValidationData.getRevocationValues().getAllCRLs().size() != 0) {

            mContext.getValidationData(mSignature).addTSValidationData(aXAdESTimeStamp, timeStampValidationData);

            UnsignedSignatureProperties usp = mSignature.getQualifyingProperties().createOrGetUnsignedProperties().getUnsignedSignatureProperties();
            usp.addTimeStampValidationData(timeStampValidationData, aXAdESTimeStamp);

        }
    }

    /* remove data existing in TimeStamp, and existing in related signature parts!
    private Pair<CertificateValues, RevocationValues> removeDuplicateReferences(CertificateValues certValues, RevocationValues revValues, ESignedData data)
            throws XMLSignatureException
    {
        CertificateValues resultingCerts = new CertificateValues(mContext);
        RevocationValues resultingRevs = new RevocationValues(mContext);

        List<ECertificate> certsInTS = data.getCertificates();
        List<ECertificate> certsExisting = mContext.getValidationData().getCertificates();
        Collection<TimeStampValidationData> tsValDatas = mContext.getValidationData().getTSValidationData().values();

        for (int i=0; i<certValues.getCertificateCount();i++){

            ECertificate cert = certValues.getCertificate(i);

            if ((certsInTS!=null && certsInTS.contains(cert)) || certsExisting.contains(cert))
                continue;

            boolean exists = false;

            for (TimeStampValidationData timeStampValidationData : tsValDatas) {
                if (timeStampValidationData.getCertificateValues().getAllCertificates().contains(cert)) {
                    exists = true;
                    break;
                }

            }
            if (!exists)
                resultingCerts.addCertificate(cert);
        }

        List<ECRL> crlsInTS = data.getCRLs();
        List<ECRL> crlsExisting = mContext.getValidationData().getCrls();

        for (int i=0; i<revValues.getCRLValueCount();i++){

            ECRL crl = revValues.getCRL(i);

            if ((crlsInTS!=null && crlsInTS.contains(crl)) || crlsExisting.contains(crl))
                continue;

            boolean exists = false;

            for (TimeStampValidationData timeStampValidationData : tsValDatas) {
                if (timeStampValidationData.getRevocationValues().getAllCRLs().contains(crl)) {
                    exists = true;
                    break;
                }

            }
            if (!exists)
                resultingRevs.addCRL(crl);
        }
        for (int i=0; i< revValues.getOCSPValueCount(); i++){
            EncapsulatedOCSPValue encapsulatedOCSPValue = revValues.getOCSPValue(i);
            resultingRevs.addOCSPValue(encapsulatedOCSPValue);
        }


        return new Pair<CertificateValues, RevocationValues>(resultingCerts, resultingRevs);
    }  */

    private String writeToFile(byte[] aData, String aURI)
            throws XMLSignatureException {
        String uri = null;
        FileOutputStream writer = null;
        try {
            String baseURI = mContext.getOutputDir();
            uri = aURI.replaceAll("\\W", "_");
            uri = uri + ".der";
            writer = new FileOutputStream(baseURI + File.separator + uri);
            writer.write(aData);
        }
        catch (Exception e) {
            logger.error("Error in BES", e);
            // shouldn't happen no i18n
            throw new XMLSignatureException("Cannot write reference data to file " + uri);
        }
        finally {
            if(writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("Error in closing the file..",e);
                }
        }
        return uri;
    }

}
