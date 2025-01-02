/**
 * Proxy class for signing multiple files through manifest file
 *
 * @author yavuz.kahveci
 */
using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;
using tr.gov.tubitak.uekae.esya.api.signature.impl;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl
{
    public class SignatureWithManifest : Signature
    {
        private readonly ASiCManifest manifest;
        private readonly Signature impl;
        private readonly Context context;
        private readonly SignatureContainerEx container;

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

        public void addContent(Signable aData, bool includeContent)
        {
            manifest.addContent(aData, context.getConfig().getAlgorithmsConfig().getDigestAlg());
        }

        // delegate job to impl
        public void setSigningTime(DateTime? aTime)
        {
            impl.setSigningTime(aTime);
        }

        public DateTime? getSigningTime()
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

        /*public DateTime? getSignatureTimestampTime()
        {
            return impl.getSignatureTimestampTime();
        }*/

        public List<TimestampInfo> getTimestampInfo(TimestampType type)
        {
            return impl.getTimestampInfo(type);
        }

        public List<TimestampInfo> getAllTimestampInfos()
        {
            return impl.getAllTimestampInfos();
        }

        public CertValidationReferences getCertValidationReferences()
        {
            return impl.getCertValidationReferences();
        }

        public CertValidationValues getCertValidationValues()
        {
            return impl.getCertValidationValues();
        }

        public Signature createCounterSignature(ECertificate certificate)
        {
            return impl.createCounterSignature(certificate);
        }

        public List<Signature> getCounterSignatures()
        {
            return impl.getCounterSignatures();
        }

        public void detachFromParent()
        {
            impl.detachFromParent();
        }

        public List<Signable> getContents()
        {
            return impl.getContents();
        }

        public IAlgorithm getSignatureAlg()
        {
            return impl.getSignatureAlg();
        }

        public void sign(BaseSigner cryptoSigner)
        {
            impl.sign(cryptoSigner);
        }

        public void upgrade(SignatureType type)
        {
            impl.upgrade(type);
        }

        public SignatureValidationResult verify()
        {
            return impl.verify();
        }

        public void addArchiveTimestamp()
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

        public SignatureType? getSignatureType()
        {
            return impl.getSignatureType();
        }

        public ECertificate getSignerCertificate()
        {
            return impl.getSignerCertificate();
        }
    }
}
