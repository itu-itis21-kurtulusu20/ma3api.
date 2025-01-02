using System;
using System.Security.Cryptography;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.formats
{

	using Logger = log4net.ILog;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using BaseSigner = tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
	using DigestAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
	using KeyUtil = tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using XmlSignatureAlgorithm = tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms.XmlSignatureAlgorithm;
	using Manifest = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Manifest;
	using Reference = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
	using SignedInfo = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
	using KeyValue = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.KeyValue;
	using X509Data = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data;
	using X509Certificate = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509Certificate;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;



	/// <summary>
	/// @author ahmety
	/// date: May 18, 2009
	/// </summary>
	public abstract class BaseSignatureFormat : SignatureFormat
	{
			public abstract SignatureFormat addArchiveTimeStamp();
			public abstract SignatureFormat evolveToA();
			public abstract SignatureFormat evolveToXL();
			public abstract SignatureFormat evolveToX2();
			public abstract SignatureFormat evolveToX1();
			public abstract SignatureFormat evolveToC();
			public abstract SignatureFormat evolveToT();
            public abstract tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature sign(IPrivateKey aKey);
			public abstract tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature createCounterSignature();

		private static Logger logger = log4net.LogManager.GetLogger(typeof(BaseSignatureFormat));


		protected internal XMLSignature mSignature;

		protected internal Context mContext;


		protected internal BaseSignatureFormat(Context aContext, XMLSignature aSignature)
		{
			mContext = aContext;
			mSignature = aSignature;
		}

		protected internal virtual DigestAlg DigestAlgorithmUrl
		{
			get
			{
                return mSignature.SignedInfo.SignatureMethod.MDigestAlg;
			}
		}

		protected internal virtual C14nMethod C14nMethod
		{
			get
			{
				return mSignature.SignedInfo.CanonicalizationMethod;
			}
		}

		protected internal virtual SignatureMethod SignatureMethod
		{
			get
			{
				return mSignature.SignedInfo.SignatureMethod;
			}
		}

		protected virtual void digestReferences(Manifest aReferences)
		{
			logger.Info("Digesting references : " + aReferences);
			for (int i = 0; i < aReferences.ReferenceCount; i++)
			{
                Reference @ref = aReferences.getReference(i);

				@ref.generateDigestValue();

				Manifest manifest = @ref.ReferencedManifest;
				if (manifest != null)
				{
					logger.Info("Digesting found manifest: " + manifest);
					digestReferences(manifest);
				}
			}
		}

		public virtual SignatureValidationResult validateCore()
		{
		    IPublicKey publicKey = mSignature.KeyInfo.resolvePublicKey();
            if (publicKey == null)
			{
				throw new XMLSignatureException("core.cantResolve.verificationKey");
			}
            return validateCore(publicKey);
		}

        public virtual SignatureValidationResult validateCore(IPublicKey aKey)
		{
            SignatureValidationResult vr = validateSignatureValue(aKey);

            // referanslari dogrula. Bu kisim uzun surebilecegi icin sonra
            // calistirilmali.
			ValidationResult referencesItem = validateReferences(mSignature.SignedInfo);
			vr.addItem(referencesItem);
			if (referencesItem.getType() != ValidationResultType.VALID)
			{
				vr.setStatus(referencesItem.getType(), I18n.translate("core.cantVerify"));
			}
			else
			{
				vr.setStatus(ValidationResultType.VALID, I18n.translate("core.verified"));
			}
			return vr;
		}

		public virtual SignatureValidationResult validateCore(ECertificate aCertificate)
		{
			try
			{
			    ESubjectPublicKeyInfo eSubjectPublicKeyInfo = aCertificate.getSubjectPublicKeyInfo();
                return validateCore(KeyUtil.decodePublicKey(eSubjectPublicKeyInfo));
			}
			catch (tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException c)
			{
                throw new XMLSignatureException(c, "errors.cantDecode", aCertificate.getSubjectPublicKeyInfo(), "PublicKey");
			}
		}

		protected internal virtual ValidationResult validateReferences(Manifest aReferences)
		{
			logger.Info("Verifying references : " + aReferences);
			// referanslari dogrulama
			for (int i = 0; i < aReferences.ReferenceCount; i++)
			{
				Reference @ref = aReferences.getReference(i);
				bool validate = false;
				Exception x = null;
				try
				{
					validate = @ref.validateDigestValue();
				}
				catch (Exception e)
				{
					x = e;
				}
				if (!validate)
				{
					ValidationResult fail = new ValidationResult(ValidationResultType.INVALID,
                                                                 I18n.translate("validation.check.reference"),
                                                                 I18n.translate("core.cantVerify.reference", @ref.URI),
                                                                 null, typeof(XMLSignature));
					if (x != null)
					{
						fail.setMessage(fail.getMessage() + "\nReason: " + x.Message);
					}
					return fail;
				}
				if (mContext.ValidateManifests)
				{
					Manifest manifest = @ref.ReferencedManifest;
					if (manifest != null)
					{
						logger.Info("Manifest found:" + manifest);
						ValidationResult item = validateReferences(manifest);
						if (item.getType() != ValidationResultType.VALID)
						{
							return item;
						}
					}
				}
			}
			logger.Info("References validated.");

			return new ValidationResult(ValidationResultType.VALID,
                                        I18n.translate("validation.check.reference"),
                                        I18n.translate("core.referencesAreValid"),
                                        null, GetType());
		}

		public virtual XMLSignature sign(BaseSigner aSigner)
		{
            if (aSigner is MobileSigner){
                MobileSigner mobileSigner = (MobileSigner)aSigner;
                addSigningCertificateAttr(mobileSigner.getSigningCertAttrv2().getFirstHash(), mobileSigner.getSignerIdentifier());
            }

			// digest references first, otherwise signature value changes!
			digestReferences(mSignature.SignedInfo);
		    SignatureMethod method = null;

			// signature value
			try
			{
				SignedInfo si = mSignature.SignedInfo;
				string algName = si.SignatureMethod.getSignatureImpl().AlgorithmName;

				string signersAlg = aSigner.getSignatureAlgorithmStr();

			    method = SignatureMethod.fromAlgorithmAndParams(signersAlg, aSigner.getAlgorithmParameterSpec());
                if (method == null)
				{
					throw new XMLSignatureException("unknown.algorithm", signersAlg);
				}

                // if the algorithm coming from signer does not match with the algorithm in signed info
                // warn the user and assign the algorithm of the signer to algorithm in signed info
			    if (!algName.Equals(signersAlg))
			    {
			        logger.Warn("Signature method " + algName + " does not match with signers. So switching to signers alg " + signersAlg);
			        si.SignatureMethod=method;
                    /*String errMessage = "Signature method " + algName + " in signature does not match with signers alg : " +
				                        signersAlg;
					logger.Warn(errMessage);
                    throw new XMLSignatureException(errMessage);*/
				}

				byte[] canoned = mSignature.SignedInfo.CanonicalizedBytes;

                ///////////////
               /* FileStream fileStream = new FileStream("SignInfoCanonied_cSharp",FileMode.Create);
                fileStream.Write(canoned,0,canoned.Length);
                fileStream.Close();*/
                /// ///////////

				debugSign(si, canoned, aSigner.GetType());

				byte[] sonuc = aSigner.sign(canoned);

                if (aSigner is MobileSigner) {
                    addKeyInfo(((MobileSigner)aSigner).getSigningCert());
                }

				mSignature.SignatureValue = sonuc;
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "core.cantCalculateSignatureValue");
			}


			return mSignature;
		}

        protected internal virtual void fillSignatureValue(IPrivateKey aKey)
		{
			try
			{
				SignedInfo si = mSignature.SignedInfo;
				SignatureMethod sm = si.SignatureMethod;

				XmlSignatureAlgorithm signer = sm.getSignatureImpl();
				signer.initSign(aKey, si.SignatureAlgorithmParameters);
				byte[] canoned = mSignature.SignedInfo.CanonicalizedBytes;

				// no need to pass arguments, if debug disabled
				if (logger.IsDebugEnabled)
				{
					debugSign(si, canoned, aKey.GetType());
				}

				signer.update(canoned);
				byte[] sonuc = signer.sign();

				mSignature.SignatureValue = sonuc;
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "core.cantCalculateSignatureValue");
			}
		}

		protected internal virtual void debugSign(SignedInfo si, byte[] canoned, object aKey)
		{
			if (logger.IsDebugEnabled)
			{
				logger.Debug("c14n : " + si.CanonicalizationMethod.URL);
				logger.Debug("signMethod : " + si.SignatureMethod.Url);
				/* logger.debug("imzalanan(canoned) : " + StringUtil.substring(canoned, 256)); */
                string canonStr = Encoding.UTF8.GetString(canoned);
                logger.Debug("data(canoned) : " + canonStr);
				logger.Debug("key : " + aKey);
			}
		}


		public virtual void addKeyInfo(ECertificate aCertificate)
		{
		   X509Data x509data = new X509Data(mContext);

		   X509Certificate cert = new X509Certificate(mContext, aCertificate);

		   x509data.add(cert);
            
		   mSignature.createOrGetKeyInfo().add(x509data);
		}

		public virtual void addKeyInfo(AsymmetricAlgorithm keyInfo)
		{
			KeyValue keyValue = new KeyValue(mContext, keyInfo);
			mSignature.createOrGetKeyInfo().add(keyValue);
		}

        public SignatureValidationResult validateSignatureValue(IPublicKey aKey)
        {
			logger.Debug("Validate Signature value.");
            SignatureValidationResult vr = new SignatureValidationResult();
            vr.setVerifierClass(typeof(XMLSignature));

            // signatureValue dogrulama
            SignedInfo si = mSignature.SignedInfo;

            XmlSignatureAlgorithm verifier;
            try
            {
                verifier = SignatureMethod.getSignatureImpl();
                verifier.initVerify(aKey, si.SignatureAlgorithmParameters);
            }
            catch (Exception x)
            {
                vr.setStatus(ValidationResultType.INVALID, x.Message);
                return vr;
            }

            byte[] canoned = si.CanonicalizedBytes;

            //////////////////////////////////////
            //FileUtil.ByteArrayToFile("SignedInfo_Canon", canoned);
            //////////////////////////////////////

            verifier.update(canoned);

            if (logger.IsDebugEnabled)
            {
                debugSign(si, canoned, aKey);
            }

            bool imzaOk = verifier.verify(mSignature.SignatureValue);
            if (imzaOk)
            {
                vr.addItem(new ValidationResult(ValidationResultType.VALID,
                    I18n.translate("validation.check.signatureValue"),
                    I18n.translate("core.verified.signatureValue"),
                    null, typeof(XMLSignature)));
                logger.Info("Signature value validated.");
                vr.setStatus(ValidationResultType.VALID, I18n.translate("core.verified.signatureValue"));
            } else {
                String failMessage = I18n.translate("core.cantVerify.signatureValue");
                logger.Info(failMessage);
                vr.setStatus(ValidationResultType.INVALID, failMessage);
                return vr;
            }

            return vr;
		}

        protected void addSigningCertificateAttr(byte[] certHash, ESignerIdentifier eSignerIdentifier)
        {

            SigningCertificate certList = new SigningCertificate(mContext);

            DigestMethod dm = null;
            try
            {
                dm = mContext.Config.AlgorithmsConfig.DigestMethod;
                certList.addCertID(new CertID(mContext, certHash, dm, eSignerIdentifier));
            }
            catch (Exception ux)
            {
                throw new XMLSignatureRuntimeException(ux, "Cant add CertID");
            }

            mSignature.createOrGetQualifyingProperties().SignedProperties.SignedSignatureProperties.SigningCertificate = certList;
        }
	}
}