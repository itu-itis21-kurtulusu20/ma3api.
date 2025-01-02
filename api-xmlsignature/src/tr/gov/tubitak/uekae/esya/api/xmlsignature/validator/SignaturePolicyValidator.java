package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.OID;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.profile.SignatureProfile;
import tr.gov.tubitak.uekae.esya.api.signature.profile.TurkishESigProfiles;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.Identifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyId;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyIdentifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyQualifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Verifies SignaturePoliciyIdentifier when it is not implicit. Note that
 * application of checks mandated by policy is not under responsibility of this
 * class. Here we check correctness of the policy.
 *
 * @author ahmety
 * date: Oct 19, 2009
 */
public class SignaturePolicyValidator implements Validator
{
    private static final Logger logger = LoggerFactory.getLogger(SignaturePolicyValidator.class);

    /**
     * @param aSignature   to be validated
     * @param aCertificate used for signature
     * @return null if this validator is not related to signature
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          if unexpected errors occur on IO, or
     *          crypto operations etc.
     */
    public ValidationResult validate(XMLSignature aSignature,
                                         ECertificate aCertificate)
            throws XMLSignatureException
    {
        // check if this property is present and it is not implied
        QualifyingProperties qp = aSignature.getQualifyingProperties();
        if (qp==null)
            return null;
        SignedProperties sp = qp.getSignedProperties();
        if (sp==null)
            return null;
        SignedSignatureProperties ssp = sp.getSignedSignatureProperties();
        if (ssp==null)
            return null;
        SignaturePolicyIdentifier spi = ssp.getSignaturePolicyIdentifier();
        if (spi==null)
            return null;

        SignaturePolicyId policyId = spi.getSignaturePolicyId();
        if (policyId==null && spi.isSignaturePolicyImplied()){
            return null;
        }

        if (policyId==null)
            return new ValidationResult(ValidationResultType.INCOMPLETE,
                                        I18n.translate("validation.check.signaturePolicy"),
                                        I18n.translate("validation.policy.cantFindId"),
                                        null, getClass());

        /*
        1) Retrieve the electronic document containing the details of the policy,
        and identified by the contents of SigPolicyId element. Apply the
        transformations indicated in the ds:Transforms element of SignaturePolicyId,
        compute the digest of the resulting document using the algorithm indicated
        in ds:DigestMethod and check its value with the digest value present in
        in SigPolicyHash.
        */

        DigestMethod digestMethod = policyId.getDigestMethod();
        byte[] digestValue = policyId.getDigestValue();
        Transforms transforms = policyId.getTransforms();

        if (!checkIfKnownPolicy(policyId)){


            String policyUrl = policyId.getPolicyId().getIdentifier().getValue();
            if (logger.isDebugEnabled())
                logger.debug("Policy URI: "+policyUrl);

            // Retrieve the electronic document containing the details of the policy
            Document doc;
            try {
                doc = Resolver.resolve(policyUrl, aSignature.getContext());
            } catch (Exception e){
                logger.warn("Warning in SignaturePolicyValidator", e);
                return new ValidationResult(ValidationResultType.INCOMPLETE,
                                            I18n.translate("validation.check.signaturePolicy"),
                                            I18n.translate("validation.policy.cantFind"),
                                            null, getClass());
            }

            // apply transforms, check hash
            boolean digestOk = validateDigest(doc, transforms, digestMethod, digestValue);
            if (!digestOk)
                return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.signaturePolicy"),
                                            I18n.translate("validation.policy.invalidDigest"),
                                            null, getClass());

        }
        /*
        2) Should the SignaturePolicyIdentifier element have qualifiers, the
        verifier should manage them according to the rules that are stated by
        the policy applying within the specific scenario. Here we only check
        policy referenced by SPURI is same with the policy used for signature
        creation
         */
        List<SignaturePolicyQualifier> qualifiers = policyId.getPolicyQualifiers();

        boolean checkPolicyURI = aSignature.getContext().getConfig().getValidationConfig().isCheckPolicyURI();

        for (SignaturePolicyQualifier spq : qualifiers)
        {
            // Retrieve the electronic document containing the details of the policy
            if (spq.getURI()!=null && checkPolicyURI){
                Document docReferencedInSPUri;
                try {
                    docReferencedInSPUri = Resolver.resolve(spq.getURI(), aSignature.getContext());
                }
                catch (Exception e){
                    logger.warn("Warning in SignaturePolicyValidator", e);
                    return new ValidationResult(ValidationResultType.WARNING,
                                                I18n.translate("validation.check.signaturePolicy"),
                                                I18n.translate("validation.policy.cantFindByUri"),
                                                null, getClass());
                }
                boolean digestQualOk = validateDigest(docReferencedInSPUri, transforms, digestMethod, digestValue);
                if (!digestQualOk)
                    return new ValidationResult(ValidationResultType.INVALID,
                                                I18n.translate("validation.check.signaturePolicy"),
                                                I18n.translate("validation.policy.invalidDigestByUri"),
                                                null, getClass());
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
                                    null, getClass());
    }

    private boolean checkIfKnownPolicy(SignaturePolicyId aPolicyId){
        try {
            // read oid from policy
            Identifier identifier = aPolicyId.getPolicyId().getIdentifier();
            OID oid;
            if (identifier.getQualifier()==Identifier.QUALIFIER_OIDAsURI)
                oid = OID.parse(identifier.getValue());
            else // oid as urn
                oid = OID.fromURN(identifier.getValue());

            // check if known policy
            SignatureProfile sp = TurkishESigProfiles.resolve(oid);
            if (sp==null){
                return false;
            }

            // compare digest
            DigestAlg digestInSignature = aPolicyId.getDigestMethod().getAlgorithm();
            boolean digestOk = Arrays.equals(aPolicyId.getDigestValue(),
                          sp.getProfileDocInfo().getDigestOfProfile(digestInSignature));
            if (!digestOk){
                logger.error("Policy digest do not match!");
                return false;
            }
            return true;
        } catch (Exception e){
            logger.debug("Policy OID parse edilemedi. ", e);
        }
        return false;
    }

    public boolean validateDigest(Document doc,
                                  Transforms transforms, 
                                  DigestMethod digestMethod,
                                  byte[] digestValue)
            throws XMLSignatureException
    {
        /*
        compute the digest of the resulting document using the algorithm
        indicated in ds:DigestMethod
         */
        if (transforms!=null){
            doc = transforms.apply(doc);
        }
        byte[] calculated = KriptoUtil.digest(doc.getBytes(), digestMethod);

        if (logger.isDebugEnabled()){
            logger.debug("digest.original   : "+ Base64.encode(digestValue));
            logger.debug("digest.calculated : "+ Base64.encode(calculated));
        }
        /*
        check its value with the digest value present in SigPolicyHash.
         */
        return Arrays.equals(calculated, digestValue);
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }

}
