package tr.gov.tubitak.uekae.esya.api.xmlsignature.formats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.MobileSigner;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms.XmlSignatureAlgorithm;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Manifest;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.KeyValue;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509Certificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SigningCertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import java.security.Key;
import java.security.PublicKey;


/**
 * @author ahmety
 * date: May 18, 2009
 */
public abstract class BaseSignatureFormat implements SignatureFormat
{

    private static Logger logger =LoggerFactory.getLogger(BaseSignatureFormat.class);


    protected XMLSignature mSignature;

    protected Context mContext;


    protected BaseSignatureFormat(Context aContext, XMLSignature aSignature)
    {
        mContext = aContext;
        mSignature = aSignature;
    }

    protected DigestAlg getDigestAlgorithmUrl()
    {
        return mSignature.getSignedInfo().getSignatureMethod().getDigestAlg();
    }

    protected C14nMethod getC14nMethod()
    {
        return mSignature.getSignedInfo().getCanonicalizationMethod();
    }

    protected SignatureMethod getSignatureMethod()
    {
        return mSignature.getSignedInfo().getSignatureMethod();
    }

    protected void digestReferences(Manifest aReferences)
            throws XMLSignatureException
    {
        logger.info("Digesting references : "+aReferences);
        for (int i =0; i<aReferences.getReferenceCount(); i++) {
            Reference ref = aReferences.getReference(i);
            ref.generateDigestValue();

            Manifest manifest = ref.getReferencedManifest();
            if (manifest!=null){
                logger.info("Digesting found manifest: " + manifest);
                digestReferences(manifest);
            }
        }
    }

	public SignatureValidationResult validateCore() throws XMLSignatureException
    {
        PublicKey pk = mSignature.getKeyInfo().resolvePublicKey();
        if (pk==null)
        {
            throw new XMLSignatureException("core.cantResolve.verificationKey");
        }
        return validateCore(pk);
    }

    @Override
    public SignatureValidationResult validateSignatureValue(Key aKey) throws XMLSignatureException
    {
        logger.debug("Validate Signature Value.");
        SignatureValidationResult vr = new SignatureValidationResult();
        vr.setVerifierClass(XMLSignature.class);

        // signatureValue dogrulama
        SignedInfo si = mSignature.getSignedInfo();

        XmlSignatureAlgorithm verifier;
        try {
            verifier = getSignatureMethod().getSignatureImpl();
            verifier.initVerify(aKey, si.getSignatureAlgorithmParameters());
        } catch (Exception e) {
            logger.warn("Warning in BaseSignatureFormat", e);
            vr.setStatus(ValidationResultType.INVALID, e.getMessage());
            return vr;
        }

        byte[] canoned = si.getCanonicalizedBytes();
        logger.debug("XAdES Signature Verifier Canonicalized Bytes: "+ StringUtil.toHexString(canoned));
        verifier.update(canoned);

        if (logger.isDebugEnabled())
            debugSign(si, canoned, aKey);

        boolean imzaOk = verifier.verify(mSignature.getSignatureValue());
        if (imzaOk) {
            vr.addItem(new ValidationResult(ValidationResultType.VALID,
                    I18n.translate("validation.check.signatureValue"),
                    I18n.translate("core.verified.signatureValue"),
                    null, XMLSignature.class));
            logger.info("Signature value validated.");
            vr.setStatus(ValidationResultType.VALID, I18n.translate("core.verified.signatureValue"));
        } else {
            String failMessage = I18n.translate("core.cantVerify.signatureValue");
            logger.info(failMessage);
            vr.setStatus(ValidationResultType.INVALID, failMessage);
            return vr;
        }

        return vr;
    }

    public SignatureValidationResult validateCore(Key aKey) throws XMLSignatureException
    {
        SignatureValidationResult vr = validateSignatureValue(aKey);

        // referanslari dogrula. Bu kisim uzun surebilecegi icin sonra
        // calistirilmali.
        ValidationResult referencesItem = validateReferences(mSignature.getSignedInfo());
        vr.addItem(referencesItem);
        if (referencesItem.getType()!= ValidationResultType.VALID){
            vr.setStatus(referencesItem.getType(), I18n.translate("core.cantVerify"));
        } else {
            vr.setStatus(ValidationResultType.VALID, I18n.translate("core.verified"));
        }
        return vr;
    }

    public SignatureValidationResult validateCore(ECertificate aCertificate)
            throws XMLSignatureException
    {
        try {
            return validateCore(KeyUtil.decodePublicKey(aCertificate.getSubjectPublicKeyInfo()));
        } catch (tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException c){
            throw new XMLSignatureException(c, "errors.cantDecode", aCertificate.getSubjectPublicKeyInfo(), "PublicKey");
        }
    }

    protected ValidationResult validateReferences(Manifest aReferences)
            throws XMLSignatureException
    {
        logger.info("Verifying references : "+aReferences);
        // referanslari dogrulama
        for (int i =0; i<aReferences.getReferenceCount(); i++)
        {
            Reference ref = aReferences.getReference(i);
            boolean validate = false;
            Exception x = null;
            try {
            	validate = ref.validateDigestValue();
            }
            catch (Exception e) {
				x = e;
			}
            if (!validate) {
                ValidationResult fail= new ValidationResult(ValidationResultType.INVALID,
                                                            I18n.translate("validation.check.reference"),
                                            				I18n.translate("core.cantVerify.reference", ref.getURI()),
                                                            null, XMLSignature.class);
                if (x!=null){
                	fail.setMessage(fail.getMessage()+ "\nReason: "+x.getMessage());
                }
                return fail;
            }
            if (mContext.isValidateManifests())
            {
                Manifest manifest = ref.getReferencedManifest();
                if (manifest!=null)
                {
                    logger.info("Manifest found:" + manifest);
                    ValidationResult item = validateReferences(manifest);
                    if (item.getType() != ValidationResultType.VALID){
                        return item;
                    }
                }
            }
        }
        logger.info("References validated.");

        return new ValidationResult(ValidationResultType.VALID,
                                    I18n.translate("validation.check.reference"),
                                    I18n.translate("core.referencesAreValid"),
                                    null, getClass());
    }


    public XMLSignature sign(BaseSigner aSigner) throws XMLSignatureException
    {
        if(aSigner instanceof MobileSigner){
            MobileSigner mobileSigner = (MobileSigner) aSigner;
            addSigningCertificateAttr(mobileSigner.getSigningCertAttrv2().getFirstHash(), mobileSigner.getSignerIdentifier());
        }

        // digest references first, otherwise signature value changes!
        digestReferences(mSignature.getSignedInfo());
        SignatureMethod method = null;

        // signature value
        try {
            SignedInfo si = mSignature.getSignedInfo();
            String algName = si.getSignatureMethod().getSignatureImpl().getAlgorithmName();
            String signersAlg = aSigner.getSignatureAlgorithmStr();

            method = SignatureMethod.fromAlgorithmAndParams(signersAlg,aSigner.getAlgorithmParameterSpec());

            if (method==null)
                throw new XMLSignatureException("unknown.algorithm", signersAlg);

            // if the algorithm coming from signer does not match with the algorithm in signed info
            // warn the user and assign the algorithm of the signer to algorithm in signed info
            if (!algName.equals(signersAlg)){
                logger.warn("Signature method "+algName+" does not match with signers. So switching to signers alg " + signersAlg);
                si.setSignatureMethod(method);
                //String errMessage = "Signature method "+algName+" in signature does not match with signers alg : "+signersAlg;
                //logger.error(errMessage);
                //throw new XMLSignatureException(errMessage);
            }

            byte[] canoned = mSignature.getSignedInfo().getCanonicalizedBytes();

            debugSign(si, canoned, aSigner.getClass());

            byte[] sonuc =  aSigner.sign(canoned);

            if (aSigner instanceof MobileSigner) {
                addKeyInfo(((MobileSigner) aSigner).getSigningCert());
            }

            mSignature.setSignatureValue(sonuc);
        }
        catch (Exception x)
        {
            throw new XMLSignatureException(x, "core.cantCalculateSignatureValue");
        }

        return mSignature;
    }

    protected void fillSignatureValue(Key aKey)
            throws XMLSignatureException
    {
        try {
            SignedInfo si = mSignature.getSignedInfo();
            SignatureMethod sm = si.getSignatureMethod();

            XmlSignatureAlgorithm signer = sm.getSignatureImpl();
            signer.initSign(aKey, si.getSignatureAlgorithmParameters());
            byte[] canoned = mSignature.getSignedInfo().getCanonicalizedBytes();

            // no need to pass arguments, if debug disabled
            if (logger.isDebugEnabled())
                debugSign(si, canoned, aKey.getClass());

            signer.update(canoned);
            byte[] sonuc =  signer.sign();

            mSignature.setSignatureValue(sonuc);
        }
        catch (Exception x)
        {
            throw new XMLSignatureException(x, "core.cantCalculateSignatureValue");
        }
    }

    protected void debugSign(SignedInfo si, byte[] canoned, Object aKey)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("c14n : "+si.getCanonicalizationMethod().getURL());
            logger.debug("signMethod : "+si.getSignatureMethod().getUrl());
            /* logger.debug("imzalanan(canoned) : " + StringUtil.substring(canoned, 256)); */
            logger.debug("data(canoned) : "+ new String(canoned));
            logger.debug("key : "+aKey);
        }

    }


    public void addKeyInfo(ECertificate aCertificate)
    {
       X509Data x509data = new X509Data(mContext);

       X509Certificate cert = new X509Certificate(mContext, aCertificate);

       x509data.add(cert);
       mSignature.createOrGetKeyInfo().add(x509data);
    }

    public void addKeyInfo(PublicKey pk) throws XMLSignatureException
    {
        KeyValue keyValue = new KeyValue(mContext, pk);
        mSignature.createOrGetKeyInfo().add(keyValue);
    }

    protected void addSigningCertificateAttr(byte[] certHash, ESignerIdentifier eSignerIdentifier) {
        SigningCertificate certList = new SigningCertificate(mContext);

        DigestMethod dm = null;
        try {
            dm = mContext.getConfig().getAlgorithmsConfig().getDigestMethod();
            certList.addCertID(new CertID(mContext, certHash, dm, eSignerIdentifier));

        } catch (Exception ux) {
            throw new XMLSignatureRuntimeException(ux, "Cant add CertID");
        }

        mSignature.createOrGetQualifyingProperties()
            .getSignedProperties()
            .getSignedSignatureProperties()
            .setSigningCertificate(certList);
    }

}
