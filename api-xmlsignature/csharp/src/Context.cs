using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{
    using Document = XmlDocument;
	using CertificateValidation = tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
	using ValidationSystem = tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
	using CertificateStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
	using ValidationPolicy = tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
	using SignedDataValidationResult = tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
	using Config = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config;
	using ValidationConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ValidationConfig;
	using IdRegistry = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.IdRegistry;
	using IResolver = tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IResolver;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
	using IdGenerator = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator;
	//using URI = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.URI;
	using Validator = tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.Validator;

	//using DocumentBuilder = javax.xml.parsers.DocumentBuilder;
	//using DocumentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory;

    /// <summary>
    /// Contains context information for construction and validation of XML
    /// Signatures.
    /// 
    /// <p>Note that <code>Context</code> instances can contain information and state
    /// specific to the XML signature structure it is used with. 
    /// 
    /// @author ahmety
    /// date: May 14, 2009
    /// </summary>
    public class Context
    {
        private Document mDocument;

        private Uri mBaseURI;
        private string mOutputDir;

        private Config mConfig;

        private IdRegistry mIdRegistry = new IdRegistry();
        private IdGenerator mIdGenerator = new UniqueIdGenerator();

        private readonly List<IResolver> mResolvers = new List<IResolver>(0);

        private readonly IList<Validator> mValidators = new List<Validator>();

        //private ValidationSystem mCertValidationSystem;

        private readonly Dictionary<XMLSignature, CertValidationData> mValidationDatas = new Dictionary<XMLSignature, CertValidationData>();
        private readonly Dictionary<XMLSignature, CertificateStatusInfo> mValidationResults = new Dictionary<XMLSignature, CertificateStatusInfo>();
        private readonly Dictionary<XMLSignature, List<SignedDataValidationResult>> mTimestampValidationResults = new Dictionary<XMLSignature, List<SignedDataValidationResult>>();

        private bool mValidateCertificates = true;
        private bool mValidateCertificateBeforeSign = false;
        private bool mValidateTimeStamps = true;

        private bool mValidateManifests = true;

        private DateTime ? mUserValidationTime;

        public Context()
        {
        }
       
        public Context(string aBaseURI)
        {
            try
            {                
                mBaseURI = new Uri(aBaseURI);
            }
            catch (Exception x)
            {
                throw new XMLSignatureException(x, "resolver.cantResolveUri", aBaseURI);
            }
        }

        //*
		public Context(FileInfo aBaseDir)
		{
			try
			{
				mBaseURI = new Uri(aBaseDir.FullName);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "resolver.cantResolveUri", aBaseDir);
			}
		}//*/

        public Context(Uri aBaseURI)
        {
            try
            {
                mBaseURI = new Uri(aBaseURI.ToString());
            }
            catch (Exception x)
            {
                throw new XMLSignatureRuntimeException(x, "resolver.cantResolveUri", aBaseURI);
            }
        }

        public virtual Document Document
        {
            get
            {
                if (mDocument == null)
                {
                    try
                    {
                        //TODO Burdaki NamespaceAware ne anlama geliyor.
                        //Gerekli mi?
                        mDocument = new XmlDocument();
                        /*
					    mDocument.na
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						factory.NamespaceAware = true;
						DocumentBuilder db = factory.newDocumentBuilder();
						mDocument = db.newDocument();
                         * */
                    }
                    catch (Exception x)
                    {
                        // should not happen so no bundle message
                        throw new ESYAException("Problem in creating document", x);
                    }
                }
                return mDocument;
            }
            set { mDocument = value; }
        }


        public virtual Config Config
        {
            set
            {
                mConfig = value;
                setValidateCertificateBeforeSign(mConfig.ValidationConfig.isValidateCertificateBeforeSigning());
                ValidateTimeStamps = mConfig.ValidationConfig.isValidateTimestampWhileSigning();
            }
            get
            {
                if (mConfig == null)
                {
                    mConfig = new Config();
                    setValidateCertificateBeforeSign(mConfig.ValidationConfig.isValidateCertificateBeforeSigning());
                    ValidateTimeStamps = mConfig.ValidationConfig.isValidateTimestampWhileSigning();
                }
                return mConfig;
            }
        }


        public virtual IdRegistry IdRegistry
        {
            get { return mIdRegistry; }
            set { mIdRegistry = value; }
        }


        public virtual IdGenerator IdGenerator
        {
            get { return mIdGenerator; }
            set { mIdGenerator = value; }
        }


        public virtual Uri BaseURI
        {
            get { return mBaseURI; }
            set { mBaseURI = value; }
        }

        public virtual string BaseURIStr
        {
            get { return mBaseURI != null ? mBaseURI.ToString() : null; }
        }


        public virtual string OutputDir
        {
            get { return mOutputDir; }
            set { mOutputDir = value; }
        }


        public virtual List<IResolver> Resolvers
        {
            get { return mResolvers; }
        }

        public virtual void addExternalResolver(IResolver aResolver)
        {
            mResolvers.Add(aResolver);
        }

        public virtual IList<Validator> Validators
        {
            get { return mValidators; }
        }

        public virtual void addValidator(Validator aValidator)
        {
            mValidators.Add(aValidator);
        }

        public virtual CertValidationData getValidationData(XMLSignature signature)
        {
            if (!mValidationDatas.ContainsKey(signature))
            {
                mValidationDatas[signature] = new CertValidationData();
            }

            CertValidationData certValidationData;
            mValidationDatas.TryGetValue(signature, out certValidationData);
            return certValidationData;
        }

        public List<CertValidationData> getAllValidationData()
        {
            return new List<CertValidationData>(mValidationDatas.Values);
        }

        public virtual CertificateStatusInfo getValidationResult(XMLSignature signature)
        {
            CertificateStatusInfo certificateStatusInfo;
            mValidationResults.TryGetValue(signature, out certificateStatusInfo);
            return certificateStatusInfo;
        }

        public void setValidationResult(XMLSignature signature, CertificateStatusInfo aValidationResult)
        {
            mValidationResults[signature] = aValidationResult;
        }

        public virtual IList<SignedDataValidationResult> getTimestampValidationResults (XMLSignature signature)
        {
            List<SignedDataValidationResult> signedDataValidationResults;
            mTimestampValidationResults.TryGetValue(signature, out signedDataValidationResults);
            return signedDataValidationResults;
        }

        public virtual void addTimestampValidationResult(XMLSignature signature, SignedDataValidationResult aValidationResult)
        {
            if (!mTimestampValidationResults.ContainsKey(signature))
            {
                mTimestampValidationResults[signature] = new List<SignedDataValidationResult>();
            }

            List<SignedDataValidationResult> signedDataValidationResults;
            mTimestampValidationResults.TryGetValue(signature, out signedDataValidationResults);
            signedDataValidationResults.Add(aValidationResult);

            mTimestampValidationResults[signature] = signedDataValidationResults;
        }

        public virtual bool ValidateCertificates
        {
            get { return mValidateCertificates; }
            set { mValidateCertificates = value; }
        }

        public virtual bool ValidateTimeStamps
        {
            get { return mValidateTimeStamps; }
            set { mValidateTimeStamps = value; }
        }

        public bool isValidateCertificateBeforeSign()
        {
            return mValidateCertificateBeforeSign;
        }

        public void setValidateCertificateBeforeSign(bool validateCertificateBeforeSign)
        {
            mValidateCertificateBeforeSign = validateCertificateBeforeSign;
        }


        public ValidationSystem getCertValidationSystem(ECertificate certificate, bool useExternalResources, XMLSignature signature)
        {
            // todo cache validation system?
            
            ValidationSystem validationSystem;
            ValidationPolicy policy;

            try
            {
                ValidationConfig config = Config.ValidationConfig;
                //policy = config.CertificateValidationPolicy;
                CertValidationPolicies policies = config.getCertValidationPolicies();
                policy = policies.getPolicyFor(certificate);
                validationSystem = CertificateValidation.createValidationSystem(policy);
            }
            catch (Exception x) {
                throw new XMLSignatureRuntimeException(x, "config.cantFind",  I18n.translate("configFile"));
            }

            ValidationSystem result = validationSystem;

            if (!useExternalResources)
            {
                result = (ValidationSystem) validationSystem.Clone();

                //mCertValidationSystem.getFindSystem().getCertificateFinders().Clear();
                //mCertValidationSystem.getFindSystem().getCRLFinders().Clear();
                //mCertValidationSystem.getFindSystem().getOCSPResponseFinders().Clear();

                List<RevocationChecker> revocationCheckers = result.getCheckSystem().getRevocationCheckers();

                foreach (RevocationChecker revocationChecker in revocationCheckers)
                {
                    revocationChecker.setParentSystem(result);
                    revocationChecker.getFinders<Finder>().Clear();
                }

                FindSystem emptyCloneFS = new FindSystem();
                FindSystem originalFS =validationSystem.getFindSystem();
                emptyCloneFS.setTrustedCertificateFinders(originalFS.getTrustedCertificateFinders());

                result.setFindSystem(emptyCloneFS);

            }

            result.setBaseValidationTime(GetValidationTime(signature));
            return result;
        }

        /*public void setCertValidationSystem(ValidationSystem aCertValidationSystem)
        {
            mCertValidationSystem = aCertValidationSystem;
        }*/
               
       
        /// <returns> should manifest be validated automatically </returns>
        public virtual bool ValidateManifests
        {
            get { return mValidateManifests; }
            set { mValidateManifests = value; }
        }



        public virtual DateTime ? UserValidationTime
        {
            get { return mUserValidationTime; }
            set { mUserValidationTime = value; }
        }
 
        public virtual DateTime ? GetValidationTime(XMLSignature xmlSignature)
        {
            DateTime? mSignatureTimestampTime = xmlSignature.getTimestampTime();
            if (mSignatureTimestampTime != null)
            {
                return mSignatureTimestampTime;
            }

            if (mConfig.ValidationConfig.TrustSigningTime)
            {
                DateTime? mSigningTime = xmlSignature.getSigningTimePropertyTime(); 
                if(mSigningTime != null)                  
                    return mSigningTime;                  
            }

            if (mUserValidationTime != null)
            {
                return mUserValidationTime;
            }

            return DateTime.Now;
        }
    }
}