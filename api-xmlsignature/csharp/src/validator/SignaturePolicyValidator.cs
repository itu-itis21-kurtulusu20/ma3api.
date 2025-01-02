using System;
using System.Linq;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature.profile;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using Logger = log4net.ILog;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using Transforms = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
	using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
	using SignedProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedProperties;
	using SignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedSignatureProperties;
	using SignaturePolicyId = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyId;
	using SignaturePolicyIdentifier = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyIdentifier;
	using SignaturePolicyQualifier = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyQualifier;
	using Resolver = tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
	using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;


	/// <summary>
	/// Verifies SignaturePoliciyIdentifier when it is not implicit. Note that
	/// application of checks mandated by policy is not under responsibility of this
	/// class. Here we check correctness of the policy.
	/// 
	/// @author ahmety
	/// date: Oct 19, 2009
	/// </summary>
	public class SignaturePolicyValidator : Validator
	{
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(SignaturePolicyValidator));

		/// <param name="aSignature">   to be validated </param>
		/// <param name="aCertificate"> used for signature </param>
		/// <returns> null if this validator is not related to signature </returns>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          if unexpected errors occur on IO, or
		///          crypto operations etc. </exception>
		public virtual ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
		{
			// check if this property is present and it is not implied
			QualifyingProperties qp = aSignature.QualifyingProperties;
			if (qp == null)
			{
				return null;
			}
			SignedProperties sp = qp.SignedProperties;
			if (sp == null)
			{
				return null;
			}
			SignedSignatureProperties ssp = sp.SignedSignatureProperties;
			if (ssp == null)
			{
				return null;
			}
			SignaturePolicyIdentifier spi = ssp.SignaturePolicyIdentifier;
			if (spi == null)
			{
				return null;
			}

			SignaturePolicyId policyId = spi.SignaturePolicyId;
			if (policyId == null && spi.SignaturePolicyImplied)
			{
				return null;
			}

			if (policyId == null)
			{
				return new ValidationResult(ValidationResultType.INCOMPLETE,
                                            I18n.translate("validation.check.signaturePolicy"),
                                            I18n.translate("validation.policy.cantFindId"),
                                            null, GetType());
			}

			/*
			1) Retrieve the electronic document containing the details of the policy,
			and identified by the contents of SigPolicyId element. Apply the
			transformations indicated in the ds:Transforms element of SignaturePolicyId,
			compute the digest of the resulting document using the algorithm indicated
			in ds:DigestMethod and check its value with the digest value present in
			in SigPolicyHash.
			*/

			DigestMethod digestMethod = policyId.DigestMethod;
			byte[] digestValue = policyId.DigestValue;

			Transforms transforms = policyId.Transforms;

            if (!checkIfKnownPolicy(policyId))
            {


                String policyUrl = policyId.PolicyId.Identifier.Value;
                if (logger.IsDebugEnabled)
                    logger.Debug("Policy URI: " + policyUrl);

                // Retrieve the electronic document containing the details of the policy
                Document doc;
                try
                {
                    doc = Resolver.resolve(policyUrl, aSignature.Context);
                }
                catch (Exception x)
                {
                    return new ValidationResult(ValidationResultType.INCOMPLETE,
                                                I18n.translate("validation.check.signaturePolicy"),
                                                I18n.translate("validation.policy.cantFind"),
                                                null, GetType());
                }

                // apply transforms, check hash
                bool digestOk = validateDigest(doc, transforms, digestMethod, digestValue);
                if (!digestOk)
                    return new ValidationResult(ValidationResultType.INVALID,
                                                I18n.translate("validation.check.signaturePolicy"),
                                                I18n.translate("validation.policy.invalidDigest"),
                                                null, GetType());

            }


			/*
			2) Should the SignaturePolicyIdentifier element have qualifiers, the
			verifier should manage them according to the rules that are stated by
			the policy applying within the specific scenario. Here we only check
			policy referenced by SPURI is same with the policy used for signature
			creation
			 */
			IList<SignaturePolicyQualifier> qualifiers = policyId.PolicyQualifiers;

			bool checkPolicyURI = aSignature.Context.Config.ValidationConfig.CheckPolicyURI;

			foreach (SignaturePolicyQualifier spq in qualifiers)
			{
				// Retrieve the electronic document containing the details of the policy
				if (spq.URI != null && checkPolicyURI)
				{
					Document docReferencedInSPUri;
					try
					{
						docReferencedInSPUri = Resolver.resolve(spq.URI, aSignature.Context);
					}
					catch (Exception x)
					{
						return new ValidationResult(ValidationResultType.WARNING,
                                                    I18n.translate("validation.check.signaturePolicy"),
                                                    I18n.translate("validation.policy.cantFindByUri"),
                                                    null, GetType());
					}
					bool digestQualOk = validateDigest(docReferencedInSPUri, transforms, digestMethod, digestValue);
					if (!digestQualOk)
					{
						return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.signaturePolicy"),
                                                    I18n.translate("validation.policy.invalidDigestByUri"),
                                                    null, GetType());
					}
				}

			}

			/* todo we do not apply signature policy internals!
			3) If the checks described before end successfully, the verifier should
			proceed to perform the checks mandated by the specific signature policy.
			The way used by the signature policy for presenting them and their
			description are out of the scope. TR 102 038 [i.1] specifies a "XML format
			for signature policies" that may be automatically processed.
			 */

			return new ValidationResult(ValidationResultType.VALID,
                                        I18n.translate("validation.check.signaturePolicy"),
                                        I18n.translate("validation.policy.valid"),
                                        null, GetType());
		}

        private bool checkIfKnownPolicy(SignaturePolicyId aPolicyId)
        {
            try
            {
                // read oid from policy
                Identifier identifier = aPolicyId.PolicyId.Identifier;
                OID oid;
                if (identifier.Qualifier == Identifier.QUALIFIER_OIDAsURI)
                    oid = OID.parse(identifier.Value);
                else // oid as urn
                    oid = OID.fromURN(identifier.Value);

                // check if known policy
                SignatureProfile sp = TurkishESigProfiles.resolve(oid);
                if (sp == null)
                {
                    return false;
                }

                // compare digest
                DigestAlg digestInSignature = aPolicyId.DigestMethod.Algorithm;
                bool digestOk = aPolicyId.DigestValue.SequenceEqual(sp.getProfileDocInfo().getDigestOfProfile(digestInSignature));
                if (!digestOk)
                {
                    logger.Error("Policy digest do not match!");
                    return false;
                }
                return true;
            }
            catch (Exception x)
            {
                logger.Debug("Policy OID parse edilemedi. ", x);
            }
            return false;
        }

		public virtual bool validateDigest(Document doc, Transforms transforms, DigestMethod digestMethod, byte[] digestValue)
		{
			/*
			compute the digest of the resulting document using the algorithm
			indicated in ds:DigestMethod
			 */
			if (transforms != null)
			{
				doc = transforms.apply(doc);
			}
			byte[] calculated = KriptoUtil.digest(doc.Bytes, digestMethod);

			if (logger.IsDebugEnabled)
			{
                logger.Debug("digest.original   : " +Convert.ToBase64String(digestValue));
				logger.Debug("digest.calculated : " + Convert.ToBase64String(calculated));
			}
			/*
			check its value with the digest value present in SigPolicyHash.
			 */
			return ArrayUtil.Equals(calculated, digestValue);
		}

		public virtual string Name
		{
			get
			{
				return this.GetType().Name;
			}
		}

	}

}