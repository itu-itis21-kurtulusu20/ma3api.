using System;
using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.formats
{

	using Logger = log4net.ILog;
	using ESignedData = tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
	using EOCSPResponse = tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
	using ECRL = tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using CertificateStatus = tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
	using CertificateValidation = tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
	using ValidationSystem = tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
	using CertificateStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
	using CRLStatus = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatus;
	using CRLStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
	using OCSPResponseStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
	using OCSPResponseStatus = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo.OCSPResponseStatus;
	using ValidationPolicy = tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
	using AllEParameters = tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
	using SignedDataValidation = tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
	using SignedDataValidationResult = tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
	using SignedData_Status = tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
	using LE = tr.gov.tubitak.uekae.esya.api.common.license.LE;
    using LV = tr.gov.tubitak.uekae.esya.api.common.license.LV;
	using CryptoException = tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
	using KeyUtil = tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
	using TSSettings = tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using TimestampConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
	using ValidationConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ValidationConfig;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using ValidationDataCollector = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.ValidationDataCollector;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
	using EncapsulatedTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
	using XAdESTimeStamp = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
	using Resolver = tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
	using XmlSignUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlSignUtil;
	using Validator = tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.Validator;

    /// <summary>
    /// A Basic Electronic Signature (XAdES-BES) will build on a XMLDSIG by
    /// incorporating qualifying properties defined in the present document. They
    /// will be added to XMLDSIG within one <code>ds:Object</code> acting as the bag
    /// for the whole set of qualifying properties.
    /// <p/>
    /// <p>Some properties defined for building up this form will be covered by the
    /// signer's signature (signed qualifying information grouped within one new
    /// element, <code>SignedProperties</code>, Other properties will be not covered
    /// by the signer's signature (unsigned qualifying information grouped within
    /// one new element, <code>UnsignedProperties</code>.
    /// <p/>
    /// <p>In a XAdES-BES the signature value SHALL be computed in the usual way of
    /// XMLDSIG over the data object(s) to be signed and on the whole set of signed
    /// properties when present (<code>SignedProperties</code> element).
    /// <p/>
    /// <p>For this form it is mandatory to protect the signing certificate with the
    /// signature, in one of the two following ways:
    /// <ul>
    /// <li>either incorporating the SigningCertificate signed property; or
    /// <li>not incorporating the SigningCertificate but incorporating the signing
    /// certificate within the <code>ds:KeyInfo</code> element and signing at least
    /// the signing certificate.
    /// </ul>
    /// <p/>
    /// <p>A XAdES-BES signature MUST, in consequence, contain at least one of the
    /// following elements with the specified contents:
    /// <p/>
    /// <ul>
    /// <li>The SigningCertificate signed property. This property MUST contain the
    /// reference and the digest value of the signing certificate. It MAY contain
    /// references and digests values of other certificates (that MAY form a chain
    /// up to the point of trust). In the case of ambiguities identifying the actual
    /// signer's certificate the applications SHOULD include the
    /// <code>SigningCertificate</code> property.
    /// <p/>
    /// <li>The <code>ds:KeyInfo</code> element. If <code>SigningCertificate</code>
    /// is present in the signature, no restrictions apply to this element. If
    /// <code>SigningCertificate</code> element is not present in the signature,
    /// then the following restrictions apply:
    /// <p/>
    /// <ul>
    /// <li>the <code>ds:KeyInfo</code> element MUST include a
    /// <code>ds:X509Data</code> containing the signing certificate;
    /// <p/>
    /// <li>the <code>ds:KeyInfo</code> element also MAY contain other certificates
    /// forming a chain that MAY reach the point of trust;
    /// <p/>
    /// <li>the <code>ds:SignedInfo</code> element MUST contain a
    /// <code>ds:Reference</code> element referencing <code>ds:KeyInfo</code>. That
    /// <code>ds:Reference</code> element SHALL be built in such a way that at least
    /// the signing certificate is actually signed.
    /// </ul>
    /// </ul>
    /// <p/>
    /// <p>NOTE 1: Signing the whole <code>ds:KeyInfo</code>, readers are warned that
    /// this locks the element: any addition of a certificate or validation data
    /// would make signature validation fail. Applications may, alternatively, use
    /// XPath transforms for signing at least the signing certificate, leaving the
    /// <code>ds:KeyInfo</code> element open for addition of new data after signing.
    /// <p/>
    /// <p>By incorporating one of these elements, XAdES-BES prevents the simple
    /// substitution of the signer's certificate.
    /// <p/>
    /// <p>A XAdES-BES signature MAY also contain the following properties:
    /// <ul>
    /// <li>the SigningTime signed property;
    /// <li>the DataObjectFormat signed property;
    /// <li>the CommitmentTypeIndication signed property;
    /// <li>the SignerRole signed property;
    /// <li>the SignatureProductionPlace signed property;
    /// <li>one or more <code>IndividualDataObjectsTimeStamp</code> or
    /// <code>AllDataObjectTimeStamp</code> signed properties;
    /// <li>one or more CounterSignature unsigned properties.
    /// </ul>
    /// <p/>
    /// <p>NOTE 2: The XAdES-BES is the minimum format for an electronic signature
    /// to be generated by the signer. On its own, it does not provide enough
    /// information for it to be verified in the longer term. For example,
    /// revocation information issued by the relevant certificate status information
    /// issuer needs to be available for long term validation.
    /// <p/>
    /// <p>The XAdES-BES satisfies the legal requirements for electronic signatures
    /// as defined in the European Directive on electronic signatures. It provides
    /// basic authentication and integrity protection.
    /// <p/>
    /// <p>The semantics of the signed data of a XAdES-BES or its context may
    /// implicitly indicate a signature policy to the verifier.
    /// 
    /// @author ahmety
    ///         date: Apr 20, 2009
    /// </summary>
	public class BES : XMLDSig, SignatureFormat
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
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(BES));
        readonly private CertRevInfoExtractor extractor = new CertRevInfoExtractor();


		public BES(Context aContext, XMLSignature aSignature) : base(aContext, aSignature)
		{
		}

		public override void addKeyInfo(ECertificate aCertificate)
		{
			base.addKeyInfo(aCertificate);
			// object

			SigningCertificate certList = new SigningCertificate(mContext);

			DigestMethod dm = null;
			try
            {
                dm = mContext.Config.AlgorithmsConfig.DigestMethod;
				certList.addCertID(new CertID(mContext, aCertificate, dm));

			}
			catch (Exception ux)
			{
				throw new XMLSignatureRuntimeException(ux, "Cant add CertID");
			}

			mSignature.createOrGetQualifyingProperties().SignedProperties.SignedSignatureProperties.SigningCertificate = certList;
		}

        /*
        public override void addKeyInfo(AsymmetricAlgorithm pk)
		{
			throw new XMLSignatureException("core.etsiRequiresCertificate");
		}*/

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

		public override SignatureValidationResult validateCore()
		{
			ECertificate certificate = extractCertificate();

			if (certificate == null)
			{
				logger.Warn("Cant find certificate for xml signature. Halting verification");
				return new SignatureValidationResult(ValidationResultType.INCOMPLETE,
                                                     I18n.translate("errors.cantFindCertificate"),
                                                     null);
			}
			return validateCore(certificate);
		}

		public virtual ECertificate extractCertificate()
		{
			ECertificate certificate = null;

			QualifyingProperties qp = mSignature.QualifyingProperties;
			if (qp != null)
			{
				SignedProperties sp = qp.SignedProperties;
				if (sp != null)
				{
					SignedSignatureProperties ssp = sp.SignedSignatureProperties;

					/*
					todo burasi dogru degil. signing certificate validasyon icin
					gerekli ama certifikalarin tanimlandigi yer degil...
					*/
					if (ssp != null)
					{
						SigningCertificate sc = ssp.SigningCertificate;
						if (sc != null)
						{
							CertID certId = resolveSigningCertificate(sc.CertIDListCopy);
							string uri = certId.URI;
							if (uri != null && uri.Length > 0)
							{
								logger.Info("Signing certificate attribute points '" + uri + "' for certificate location. KeyInfo will be neglected.");
								try
								{
									Document cert = Resolver.resolve(uri, mContext);
									certificate = new ECertificate(cert.Bytes);
								}
								catch (Exception x)
								{
									logger.Warn(I18n.translate("core.cantResolve.signingCertificateAttribute"));
									//throw new XMLSignatureException("core.cantResolve.signingCertificateAttribute");
								}
							}
						}
					}
				}
			}

			if (certificate == null)
			{
				certificate = mSignature.KeyInfo.resolveCertificate();
            }
            return certificate;
        }

        public override XMLSignature sign(BaseSigner aSigner)
        {
            bool isMobileSigner = aSigner is MobileSigner;
            bool validateCertificate = mContext.isValidateCertificateBeforeSign();

			if (!isMobileSigner) {

                ECertificate certificate = extractCertificate();
                try
                {
                    SignatureAlg signatureAlg = SignatureAlg.fromName(aSigner.getSignatureAlgorithmStr());
                    ECUtil.checkKeyAndSigningAlgorithmConsistency(certificate, signatureAlg);
                    ECUtil.checkDigestAlgForECCAlgorithm(certificate, signatureAlg);
                }
                catch (ESYAException e)
                {
                    throw new XMLSignatureException(e, "Signature Algorithm and Certificate Signing Algorithm are incompatible");
                }

                if (validateCertificate)
                {
                    this.validateCertificate(certificate);
                }
            }

            XMLSignature xmlSignature = base.sign(aSigner);

            if (isMobileSigner)
            {
                ECertificate certificate = extractCertificate();
                try
                {
                    ECUtil.checkDigestAlgForECCAlgorithm(certificate, SignatureAlg.fromName(aSigner.getSignatureAlgorithmStr()));
                }
                catch (ESYAException e)
                {
                    throw new XMLSignatureException(e, "Target digest algorithm length does not satisfy certificate signature algorithm length");
                }

                if (validateCertificate)
                {
                    this.validateCertificate(certificate);
                }
            }

            return xmlSignature;
        }

        public void validateCertificate(ECertificate certificate)
        {
            if (certificate == null)
                    throw new XMLSignatureRuntimeException("Cant find certificate to validate before sign. Either turn this feature off, or call #addKeyInfo(Certificate) before signing.");

            CertificateStatusInfo csi = _validateCertificate(certificate, DateTime.UtcNow, true);
            if (csi.getCertificateStatus() != CertificateStatus.VALID)
            {
                logger.Info("Cant validate certificate: " + csi.printDetailedValidationReport());
                throw new XMLSignatureException(I18n.translate("validation.certificate.cantValidateSigner", csi.getDetailedMessage()));
            }

            try
            {
                // Check Turkish Electronic Signature Profile attribute consistency before sign.
                // Signature type and revocation data consistency will be checked only at validation
                // using the class TurkishESigProfileValidator.
                if (mSignature.createOrGetQualifyingProperties().SignedSignatureProperties.SignaturePolicyIdentifier != null)
                {
                    List<Type> validators = new List<Type>(0);
                    Type clazz = Type.GetType("tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.TurkishESigProfileAttributeValidator");
                    validators.Add(clazz);
                    foreach (Type validator in validators)
                    {
                        Validator v = (Validator)Activator.CreateInstance(validator);
                        ValidationResult vr = v.validate(mSignature, certificate);
                        if (vr.getResultType() != (tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType.VALID))
                        {
                            throw new XMLSignatureException(I18n.translate("validation.policy.cantValidatePolicy"));
                        }
                    }
                }
            }
            catch (Exception e)
            {
                throw new XMLSignatureException(e, "validation.policy.cantValidatePolicy");
            }
        }

		public override tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureValidationResult validateCore(IPublicKey aKey)
		{
			throw new XMLSignatureException("core.etsiRequiresCertificate");
		}

		public override SignatureValidationResult validateCore(ECertificate aCertificate)
		{
			checkLAtValidation(aCertificate);

			IPublicKey key;
			try
			{
				key = KeyUtil.decodePublicKey(aCertificate.getSubjectPublicKeyInfo());
			}
			catch (CryptoException c)
			{
				throw new XMLSignatureException(c, "errors.cantDecode", "PublicKey");
			}
			SignatureValidationResult result = base.validateCore(key);
			if (!result.getType().Equals(ValidationResultType.VALID))
			{
				return result;
			}

			// construct validationSystem
			if (mContext.ValidateCertificates)
			{
                // burasi C icin JAVA'dan alindi
                bool resolveExternalReferencesOfSignature = true;
                SignatureType st = mSignature.SignatureType;
                if (st == SignatureType.XAdES_X_L || st == SignatureType.XAdES_A)
                {
                    resolveExternalReferencesOfSignature = !mContext.Config.ValidationConfig.ForceStrictReferences;
                }

                bool useExternalResourcesAtCertValidation = true;
                if (st == SignatureType.XAdES_C || st == SignatureType.XAdES_X || st == SignatureType.XAdES_X_L || st == SignatureType.XAdES_A)
                {
                    useExternalResourcesAtCertValidation = !mContext.Config.ValidationConfig.ForceStrictReferences;
                }
				//
                
                ValidationDataCollector collector = new ValidationDataCollector();

				ValidationResult r1 = collector.collect(mSignature, resolveExternalReferencesOfSignature);
				if ((r1 != null) && (r1.getType() != ValidationResultType.VALID))
				{
					result.setStatus(r1.getType(), r1.getMessage());
					return result;
				}

				ValidationResult vr = validateCertificate(aCertificate, useExternalResourcesAtCertValidation, result);
				if (vr.getType() != ValidationResultType.VALID)
				{
					result.setStatus(vr.getType(), vr.getMessage());
					return result;
				}
			}

			IList<Validator> defaultValidators = mContext.Config.ValidationConfig.getProfile(mSignature.SignatureType).createValidators();
            List<Validator> validators = new List<Validator>(mContext.Validators);

			validators.AddRange(defaultValidators);

			foreach (Validator validator in validators)
			{
				ValidationResult item = validator.validate(mSignature, aCertificate);
				if (item != null)
				{
					//item.setVerifierClass(validator.getName());
					item.setVerifierClass(validator.GetType());
					result.addItem(item);
					if ((item.getType() == ValidationResultType.INVALID) || (item.getType() == ValidationResultType.INCOMPLETE))
					{
						result.setStatus(item.getType(), item.getMessage());
						return result;
					}
				}
			}

			// verify counter signatures
			foreach (XMLSignature counterSignature in mSignature.AllCounterSignatures)
			{
				ValidationResult vr = counterSignature.verify();
				result.addItem(vr);
				if ((vr.getType() == ValidationResultType.INVALID) || (vr.getType() == ValidationResultType.INCOMPLETE))
				{
					result.setStatus(vr.getType(), vr.getMessage());
					return result;
				}
			}

			logger.Info("BES core verification is OK.");
			return result;
		}

		private void checkLAtValidation(ECertificate aCertificate)
		{            
			try
			{
				bool isTest = LV.getInstance().isTestLicense(LV.Products.XMLIMZA);
				if (isTest && !aCertificate.getSubject().getCommonNameAttribute().ToLower().Contains("test"))
				{				
					throw new Exception("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");					
				}
			}
			catch (LE ex)
			{
				logger.Fatal("Lisans kontrolu basarisiz.");
				throw new Exception("Lisans kontrolu basarisiz.", ex);
			}         
		}

		protected internal virtual ValidationResult validateCertificate(ECertificate aCertificate, bool useExternalResources, SignatureValidationResult svr)
		{
			try
			{
                /*bool useExternalResources = true;
                SignatureType st = mSignature.SignatureType;
                if (st == SignatureType.XAdES_X_L || st == SignatureType.XAdES_A)
                {
                    useExternalResources = !mContext.Config.ValidationConfig.ForceStrictReferences;
                }*/
                CertificateStatusInfo csi = _validateCertificate(aCertificate, mContext.GetValidationTime(mSignature), useExternalResources);
				mContext.setValidationResult(mSignature, csi);
				ValidationResult vr;

				if (csi.getCertificateStatus() == CertificateStatus.VALID)
				{
					vr = new ValidationResult(ValidationResultType.VALID,
                                              I18n.translate("validation.check.certificate"),
                                              I18n.translate("validation.certificate.validatedSigner"),
                                              XmlSignUtil.verificationInfo(csi),
                                              typeof(XMLSignature));
				}
				else
				{
					vr = new ValidationResult(ValidationResultType.INVALID,
                                              I18n.translate("validation.check.certificate"),
                                              I18n.translate("validation.certificate.cantValidateSigner",
                                              aCertificate.getSubject().stringValue()) + "\n" + csi.getDetailedMessage(),
                                              XmlSignUtil.verificationInfo(csi),
                                              typeof(XMLSignature));
				}
				svr.setCertificateStatusInfo(csi);
				svr.addItem(vr);
				return vr;
			}
			catch (Exception x)
			{
				logger.Warn(x);
                return new ValidationResult(ValidationResultType.INCOMPLETE,
                                            I18n.translate("validation.check.certificate"), //todo 18n below line!
                                            "Cant validate certificate " + aCertificate.getSubject().stringValue() + "," + aCertificate.getSerialNumber() + " " + x.Message,
                                            null, typeof(XMLSignature));
			}
		}

        protected internal virtual CertificateStatusInfo _validateCertificate(ECertificate aCertificate, DateTime ? aValidationTime, bool useExternalResources)
        {
            CertValidationData vdata = mContext.getValidationData(mSignature);
			ValidationConfig config = mContext.Config.ValidationConfig;
            ValidationSystem vs = mContext.getCertValidationSystem(aCertificate, useExternalResources, mSignature);

		    vs.setUserInitialCertificateSet(vdata.Certificates);
		    vs.setUserInitialCRLSet(vdata.Crls);
			vs.setUserInitialOCSPResponseSet(vdata.OcspResponses);

            DigestAlg digestAlgForOCSP = mContext.Config.AlgorithmsConfig.getDigestAlgForOCSP();
            if (digestAlgForOCSP != null)
            {
                List<RevocationChecker> rvCheckers = vs.getCheckSystem().getRevocationCheckers();
                foreach (RevocationChecker rvChecker in rvCheckers)
                {
                    if (rvChecker is RevocationFromOCSPChecker)
                    {
                        foreach (Finder ocspChecker in rvChecker.getFinders<Finder>())
                        {
                            if (ocspChecker is OCSPResponseFinderFromAIA)
                            {
                                ((OCSPResponseFinderFromAIA)ocspChecker).setDigestAlgForOcspFinder(digestAlgForOCSP);
                            }
                        }
                    }
                }
            }

            DateTime now = DateTime.Now;
            DateTime graceTime = (DateTime) aValidationTime;
			graceTime = graceTime.AddSeconds(config.GracePeriodInSeconds);

			if (graceTime<now)
			{

			    DateTime lastRevocationTime = (DateTime) aValidationTime;
                lastRevocationTime = lastRevocationTime.AddSeconds(config.LastRevocationPeriodInSeconds);

				if (lastRevocationTime>aCertificate.getNotAfter())
				{
					lastRevocationTime = max(aCertificate.getNotAfter().Value, graceTime);
				}

				vs.setBaseValidationTime(aValidationTime);
				vs.setLastRevocationTime (lastRevocationTime);
			} 
            return CertificateValidation.validateCertificate(vs, aCertificate, config.UseValidationDataPublishedAfterCreation);
        }

		private DateTime max(DateTime time1, DateTime time2)
		{
			return time1>time2 ? time1 : time2;
		}


		/* todo resolve signing certificate from certificate chain
	whatever exist in key info also included to this set ... */

		private CertID resolveSigningCertificate(IList<CertID> aList)
		{
			return aList[0];
		}

		public override XMLSignature createCounterSignature()
		{
			XMLSignature signature = new XMLSignature(mContext, false);
			SignatureMethod sm = mSignature.SignatureMethod;
			signature.SignatureMethod = sm;

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
			string sviURI = "#" + mSignature.SignatureValueId;
			signature.addDocument(sviURI, null, null, null, Constants.REFERENCE_TYPE_COUNTER_SIGNATURE, false);

			QualifyingProperties qp = mSignature.createOrGetQualifyingProperties();
			qp.createOrGetUnsignedProperties().UnsignedSignatureProperties.addCounterSignature(signature);
			return signature;
		}

		public override SignatureFormat evolveToT()
		{
			TimestampConfig tsConfig = mContext.Config.TimestampConfig;
			TSSettings settings = tsConfig.Settings;

            // burayi c14n icin kapattik
			//SignatureTimeStamp sts = new SignatureTimeStamp(mContext, mSignature, tsConfig.DigestMethod, settings);

            SignatureTimeStamp sts = new SignatureTimeStamp(mContext, mSignature, tsConfig.getC14nMethod(), tsConfig.DigestMethod, settings);
			
            mSignature.createOrGetQualifyingProperties().createOrGetUnsignedProperties().UnsignedSignatureProperties.addSignatureTimeStamp(sts);

			return new T(mContext, mSignature);
		}

		public override SignatureFormat evolveToC()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_BES, SignatureType.XAdES_C, SignatureType.XAdES_T);
		}

		public override SignatureFormat evolveToX1()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_BES, SignatureType.XAdES_X, SignatureType.XAdES_T);
		}

		public override SignatureFormat evolveToX2()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_BES, SignatureType.XAdES_X, SignatureType.XAdES_T);
		}

		public override SignatureFormat evolveToXL()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_BES, SignatureType.XAdES_X_L, SignatureType.XAdES_T);
		}

		public override SignatureFormat evolveToA()
		{
			throw new XMLSignatureException("error.formatCantEvolveMultiple", SignatureType.XAdES_BES, SignatureType.XAdES_A, SignatureType.XAdES_T);
		}

		public override SignatureFormat addArchiveTimeStamp()
		{
			throw new XMLSignatureException("error.formatCantAddArchiveTS");
		}

		// internal
		protected internal virtual void addReferences(CertificateStatusInfo csi)
		{
           CompleteCertificateRefs certRefs = new CompleteCertificateRefs(mContext);
            CompleteRevocationRefs revRefs = new CompleteRevocationRefs(mContext);
            DigestMethod digestMethod = mContext.Config.AlgorithmsConfig.DigestMethod;
		    bool writeData = mContext.Config.Parameters.WriteReferencedValidationDataToFileOnUpgrade;
            logger.Info("Force reference write is set to : " + writeData);

            UniqueCertRevInfo ucri = extractor.trace(csi);

            int counter = 0;
            foreach (ECertificate cert in ucri.getCertificates()){
            if (counter != 0)
                certRefs.addCertificateReference(new CertID(mContext, cert, digestMethod));

            counter++;
        }

        foreach (ECRL crl in ucri.getCrls()){
            String uri = null;
            if (writeData)
            {
                uri = crl.getIssuer().stringValue() + "_" + crl.getThisUpdate().Value + ".crl";
                uri = writeToFile(crl.getEncoded(), uri);
            }
            revRefs.addCRLReference(new CRLReference(mContext, crl, digestMethod, uri));
        }

        foreach (EOCSPResponse ocsp in ucri.getOcspResponses()){
            String uri = null;
            if (writeData) {
                uri = ocsp.getBasicOCSPResponse().getTbsResponseData().getResponderIdByName().stringValue() + "_" + ocsp.getBasicOCSPResponse().getProducedAt().Value + ".ocsp";
                uri = writeToFile(ocsp.getEncoded(), uri);
            }
            revRefs.addOCSPReference(new OCSPReference(mContext, ocsp, digestMethod, uri));
        }

        QualifyingProperties qp = mSignature.createOrGetQualifyingProperties();
        UnsignedProperties up = qp.createOrGetUnsignedProperties();
		    UnsignedSignatureProperties usp = up.UnsignedSignatureProperties;
        usp.CompleteCertificateRefs=certRefs;
        usp.CompleteRevocationRefs=revRefs;
		}

		protected internal virtual void addValidationData()
		{
			CertificateStatusInfo csi = mContext.getValidationResult(mSignature);
			if (csi == null)
			{
				ECertificate certificate = extractCertificate();
				if (certificate == null)
				{
					throw new XMLSignatureException("validation.certificate.cantFound");
				}

				ValidationResult r1 = (new ValidationDataCollector()).collect(mSignature, true);

				csi = _validateCertificate(certificate, mContext.GetValidationTime(mSignature),true);
				mContext.setValidationResult(mSignature, csi);
			}

			if (csi.getCertificateStatus() == CertificateStatus.VALID)
			{
				addValidationData(csi);
			}
			else
			{
				logger.Warn(XmlSignUtil.verificationInfo(csi));
				throw new XMLSignatureException("validation.certificate.cantValidateSigner");
			}
		}

		protected internal virtual void addValidationData(CertificateStatusInfo csi)
		{
            Pair<CertificateValues, RevocationValues> vd = extractor.extractValidationDataCSI(mContext,csi);


			QualifyingProperties qp = mSignature.createOrGetQualifyingProperties();
			UnsignedProperties up = qp.createOrGetUnsignedProperties();
			UnsignedSignatureProperties usp = up.UnsignedSignatureProperties;
			usp.CertificateValues = vd.getmObj1();
			usp.RevocationValues = vd.getmObj2();
		}
        /*
		protected internal virtual Pair<CertificateValues, RevocationValues> extractValidationData(CertificateStatusInfo csi)
		{
		 CertificateValues certValues = new CertificateValues(mContext);
        RevocationValues revValues = new RevocationValues(mContext);
        //DigestMethod digestMethod = mContext.getConfig().getAlgorithmsConfig().getDigestMethod();

        UniqueCertRevInfo ucri = extractor.trace(csi);

        int counter = 0;
        foreach (ECertificate cert in ucri.getCertificates()){
            if (counter != 0)
                certValues.addCertificate(cert);

            counter++;
        }

        foreach (ECRL crl in ucri.getCrls()){
            revValues.addCRL(crl);
        }

        foreach(EOCSPResponse ocsp in ucri.getOcspResponses()){
            revValues.addOCSPResponse(ocsp);
        }

        return new Pair<CertificateValues, RevocationValues>(certValues, revValues);
		}
        */
		// wors for first timestamp!
		public virtual void addTimestampValidationData(XAdESTimeStamp aXAdESTimeStamp, DateTime aValidationTime)
		{
			ValidationConfig validationConfig = mContext.Config.ValidationConfig;
			EncapsulatedTimeStamp ets = aXAdESTimeStamp.getEncapsulatedTimeStamp(0);

            TimeStampValidationData timeStampValidationData = mContext.getValidationData(mSignature).getTSValidationDataForTS(aXAdESTimeStamp);

            if (timeStampValidationData == null)
            {
                //if not already verified

                // verify timestamp
                byte[] input = ets.ContentInfo.getEncoded();
                SignedDataValidation sd = new SignedDataValidation();
                Dictionary<string, object> @params = new Dictionary<string, object>();

                SignedDataValidationResult sdvr = null;

                try
                {
                    //ValidationPolicy policy = validationConfig.CertificateValidationPolicy;

                    //@params[AllEParameters.P_CERT_VALIDATION_POLICY] = policy;

                    @params[AllEParameters.P_CERT_VALIDATION_POLICIES] = validationConfig.getCertValidationPolicies();

                    @params[AllEParameters.P_SIGNING_TIME] = aValidationTime; // ets.getTime ?
                    @params[AllEParameters.P_GRACE_PERIOD] = (long) validationConfig.GracePeriodInSeconds;
                    @params[AllEParameters.P_IGNORE_GRACE] = true;
                    @params[AllEParameters.P_REVOCINFO_PERIOD] = (long) validationConfig.LastRevocationPeriodInSeconds;

                    //CertValidationData validationData = mContext.ValidationData;

                    //@params.putAll(extractor.collectAllInitialValidationDataFromContextAsParams(mContext));

                    IDictionary<String, Object> collectedParams = extractor.collectAllInitialValidationDataFromContextAsParams(mContext);
                    foreach (String key in collectedParams.Keys)
                    {
                        @params[key] = collectedParams[key];
                    }

                    /*@params[AllEParameters.P_INITIAL_CERTIFICATES] = validationData.Certificates;
                    @params[AllEParameters.P_INITIAL_CRLS] = validationData.Crls;
                    @params[AllEParameters.P_INITIAL_OCSP_RESPONSES] = validationData.OcspResponses;*/


                    logger.Debug("Validate timestamp " + aXAdESTimeStamp.LocalName);
                    sdvr = sd.verify(input, @params);
                    logger.Debug(sdvr.ToString());
                    if (sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
                    {
                        bool secondPass = false;
                        if (sdvr == null || (sdvr.getSDStatus() != SignedData_Status.ALL_VALID))
                        {
                            logger.Debug("SignedData is not verified, try validation in timestamp time");

                            @params[AllEParameters.P_SIGNING_TIME] = ets.Time;
                            sdvr = sd.verify(input, @params);
                            secondPass = true;
                        }
                        if (sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
                        {
                            throw new XMLSignatureException("validation.timestamp.certificateNotValidated");
                        }
                    }
                }
                catch (Exception x)
                {
                    Console.WriteLine(x.ToString());
                    Console.Write(x.StackTrace);
                    throw new XMLSignatureException("validation.timestamp.certificateNotValidated", x);
                }

                //validate ts signer cerficate

                ESignedData tsSignedData = ets.SignedData;
                IList<ECertificate> tsCertificates = tsSignedData.getCertificates();

                if (tsCertificates == null || tsCertificates.Count == 0)
                {
                    throw new XMLSignatureException("validation.timestamp.certificateNotFound",
                                                    aXAdESTimeStamp.LocalName);
                }

                //ECertificate signer = tsCertificates.get(0);

                CertificateStatusInfo csi = sdvr.getSDValidationResults()[0].getCertStatusInfo();

                //CertificateStatusInfo csi = extractCertValidationResult(sdvr);
                //_validateCertificate(signer, aValidationTime);

                if (csi.getCertificateStatus() != CertificateStatus.VALID)
                {
                    logger.Warn("Cant validate Timestamp signer certificate. ");
                    logger.Info(XmlSignUtil.verificationInfo(csi));
                    throw new XMLSignatureException("validation.timestamp.certificateNotFound");
                }

                Pair<CertificateValues, RevocationValues> vd = extractor.extractValidationDataCSI(mContext, csi);
                vd = extractor.removeDuplicateReferences(mContext, mSignature, vd.getmObj1(), vd.getmObj2(), tsSignedData);

                timeStampValidationData = new TimeStampValidationData(mContext, vd.getmObj1(), vd.getmObj2());
            } // verify ts certificaete

		    //
            // burda Erbay'dan gelen bir duzenleme yaptim
            // yeni ts validation data'ya koyacağımız cert, ocsp veya crl daha oncekilerde varsa
            // remove duplicate ile kaldiriyorduk, ben de eger hic bisey kalmadiysa bos validation
            // data tag'i koymasin diye ayarladim
            //
            if (timeStampValidationData.CertificateValues.AllCertificates.Count != 0 ||
                timeStampValidationData.RevocationValues.AllOCSPResponses.Count != 0 ||
                timeStampValidationData.RevocationValues.AllCRLs.Count != 0)
            {
                mContext.getValidationData(mSignature).addTSValidationData(aXAdESTimeStamp, timeStampValidationData);

                UnsignedSignatureProperties usp = mSignature.QualifyingProperties.createOrGetUnsignedProperties().UnsignedSignatureProperties;
                usp.addTimeStampValidationData(timeStampValidationData, aXAdESTimeStamp);
            }
		}
		
        
        /* remove data existing in TimeStamp, and existing in related signature parts!
		private Pair<CertificateValues, RevocationValues> removeDuplicateReferences(CertificateValues certValues, RevocationValues revValues, ESignedData data)
		{
			CertificateValues resultingCerts = new CertificateValues(mContext);
			RevocationValues resultingRevs = new RevocationValues(mContext);

			IList<ECertificate> certsInTS = data.getCertificates();
			IList<ECertificate> certsExisting = mContext.ValidationData.Certificates;
			ICollection<TimeStampValidationData> tsValDatas = mContext.ValidationData.TSValidationData.Values;

			for (int i = 0; i < certValues.CertificateCount;i++)
			{

				ECertificate cert = certValues.getCertificate(i);

				if ((certsInTS != null && certsInTS.Contains(cert)) || certsExisting.Contains(cert))
				{
					continue;
				}

				bool exists = false;

				foreach (TimeStampValidationData timeStampValidationData in tsValDatas)
				{
					if (timeStampValidationData.CertificateValues.AllCertificates.Contains(cert))
					{
						exists = true;
						break;
					}

				}
				if (!exists)
				{
					resultingCerts.addCertificate(cert);
				}
			}

		    IList<ECRL> crlsInTS = data.getCRLs();
			IList<ECRL> crlsExisting = mContext.ValidationData.Crls;

			for (int i = 0; i < revValues.CRLValueCount;i++)
			{

				ECRL crl = revValues.getCRL(i);

				if ((crlsInTS != null && crlsInTS.Contains(crl)) || crlsExisting.Contains(crl))
				{
					continue;
				}

				bool exists = false;

				foreach (TimeStampValidationData timeStampValidationData in tsValDatas)
				{
					if (timeStampValidationData.RevocationValues.AllCRLs.Contains(crl))
					{
						exists = true;
						break;
					}

				}
				if (!exists)
				{
					resultingRevs.addCRL(crl);
				}
			}
			for (int i = 0; i < revValues.OCSPValueCount; i++)
			{
				EncapsulatedOCSPValue encapsulatedOCSPValue = revValues.getOCSPValue(i);
				resultingRevs.addOCSPValue(encapsulatedOCSPValue);
			}


			return new Pair<CertificateValues, RevocationValues>(resultingCerts, resultingRevs);
		}*/

		private string writeToFile(byte[] aData, string aURI)
		{
			string uri = null;
			try
			{
				string baseURI = mContext.OutputDir;
				uri = aURI.Replace("\\W", "_");
				uri = uri + ".der";

                BinaryWriter writer = new BinaryWriter(File.OpenWrite(baseURI + "/" + uri)); 
				writer.Write(aData);
			}
			catch (Exception x)
			{
				Console.WriteLine(x.ToString());
				Console.Write(x.StackTrace);
				// shouldn't happen no i18n
				throw new XMLSignatureException("Cannot write reference data to file " + uri);
			}
			return uri;
		}

	}

}