using System;
using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl
{
    /**
     * @author yavuz.kahveci
     */
    public class ContainerWithManifest : SignatureContainerEx
    {
        private readonly SignatureContainerEx impl;
        private readonly ASiCManifest manifest;
        private SignaturePackage signaturePackage;

        public ContainerWithManifest(SignatureContainerEx aImpl, ASiCManifest aManifest, SignaturePackage owner)
        {
            impl = aImpl;
            manifest = aManifest;
            signaturePackage = owner;
        }

        public Signature createSignature(ECertificate certificate)
        {
            try {
                Signature sig = impl.createSignature(certificate);
                return new SignatureWithManifest(getContext(), this, manifest, sig);
            }catch (Exception x){
                // should not happen
                throw new SignatureRuntimeException(x);
            }
        }

        public void addExternalSignature(Signature signature)
        {
            impl.addExternalSignature(signature);
        }

        public void detachSignature(Signature signature)
        {
            impl.detachSignature(signature);
        }

        public List<Signature> getSignatures()
        {
            return impl.getSignatures();
        }

        public SignatureFormat getSignatureFormat()
        {
            return impl.getSignatureFormat();
        }

        public SignaturePackage getPackage()
        {
            return signaturePackage;
        }

        public void setSignaturePackage(SignaturePackage signaturePackage)
        {
            this.signaturePackage = signaturePackage;
        }

        public ContainerValidationResult verifyAll()
        {
            return impl.verifyAll();
        }

        public void setContext(Context context)
        {
            impl.setContext(context);
        }

        public Context getContext()
        {
            return impl.getContext();
        }

        public bool isSignatureContainer(Stream stream)
        {
            return impl.isSignatureContainer(stream);
        }

        public void read(Stream stream)
        {
            impl.read(stream);
        }

        public void write(Stream stream)
        {
            impl.write(stream);
        }

        public Object getUnderlyingObject()
        {
            return impl.getUnderlyingObject();
        }
    }
}