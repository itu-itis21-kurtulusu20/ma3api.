package tr.gov.tubitak.uekae.esya.api.asic.core.impl;

import tr.gov.tubitak.uekae.esya.api.asic.core.SignableASiCDocument;
import tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest.ASiCManifest;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignatureContainerEx;

import java.util.Calendar;
import java.util.List;

/**
 * Proxy class for signing multiple files through manifest file
 *
 * @author ayetgin
 */
public class SignatureWithManifest implements Signature
{
    private ASiCManifest manifest;
    private Signature impl;
    private Context context;
    private SignatureContainerEx container;

    public SignatureWithManifest(Context context, SignatureContainerEx container, ASiCManifest manifest, Signature impl)
    {
        this.context = context;
        this.container = container;
        this.manifest = manifest;
        this.impl = impl;
        try {
            this.impl.addContent(new SignableASiCDocument(this.manifest), false);
        } catch (Exception x){
            // shouldnot happen
            throw new SignatureRuntimeException(x);
        }
    }

    public void addContent(Signable aData, boolean includeContent) throws SignatureException
    {
        manifest.addContent(aData, context.getConfig().getAlgorithmsConfig().getDigestAlg());
    }

    // delegate job to impl
    public void setSigningTime(Calendar aTime)
    {
        impl.setSigningTime(aTime);
    }

    public Calendar getSigningTime()
    {
        return impl.getSigningTime();
    }

    public void setSignaturePolicy(SignaturePolicyIdentifier policyId)
    {
        impl.setSignaturePolicy(policyId);
    }

    public SignaturePolicyIdentifier getSignaturePolicy()
    {
        return impl.getSignaturePolicy();
    }

    /*
    public Calendar getSignatureTimestampTime()
    {
        return impl.getSignatureTimestampTime();
    } */

    public List<TimestampInfo> getTimestampInfo(TimestampType type) {
        return impl.getTimestampInfo(type);
    }

    public List<TimestampInfo> getAllTimestampInfos() {
        return impl.getAllTimestampInfos();
    }

    public CertValidationReferences getCertValidationReferences() {
        return impl.getCertValidationReferences();
    }

    public CertValidationValues getCertValidationValues() {
        return impl.getCertValidationValues();
    }

    public Signature createCounterSignature(ECertificate certificate) throws SignatureException
    {
        return impl.createCounterSignature(certificate);
    }

    public List<Signature> getCounterSignatures()
    {
        return impl.getCounterSignatures();
    }

    public void detachFromParent() throws SignatureException
    {
        impl.detachFromParent();
    }

    public List<Signable> getContents() throws SignatureException
    {
        return impl.getContents();
    }

    public Algorithm getSignatureAlg() {
        return impl.getSignatureAlg();
    }

    public void sign(BaseSigner cryptoSigner) throws SignatureException
    {
        impl.sign(cryptoSigner);
    }

    public void upgrade(SignatureType type) throws SignatureException
    {
        impl.upgrade(type);
    }

    public SignatureValidationResult verify() throws SignatureException
    {
        return impl.verify();
    }

    public void addArchiveTimestamp() throws SignatureException
    {
        impl.addArchiveTimestamp();
    }

    public SignatureFormat getSignatureFormat()
    {
        return impl.getSignatureFormat();
    }

    public SignatureContainer getContainer() {
        return container;
    }

    public Object getUnderlyingObject()
    {
        return impl.getUnderlyingObject();
    }

    public SignatureType getSignatureType()
    {
        return impl.getSignatureType();
    }

    public ECertificate getSignerCertificate()
    {
        return impl.getSignerCertificate();
    }

}
