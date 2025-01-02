package tr.gov.tubitak.uekae.esya.api.asic.core.impl;

import tr.gov.tubitak.uekae.esya.api.asic.core.SignableASiCDocument;
import tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest.ASiCManifest;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignatureContainerEx;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignaturePackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author ayetgin
 */
public class ContainerWithManifest implements SignatureContainerEx
{
    private SignatureContainerEx impl;
    private ASiCManifest manifest;
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
            throw new ESYARuntimeException(x);
        }
    }

    public void addExternalSignature(Signature signature) throws SignatureException
    {
        impl.addExternalSignature(signature);
    }

    public void detachSignature(Signature signature) throws SignatureException
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

    public SignaturePackage getPackage() {
        return signaturePackage;
    }

    public void setSignaturePackage(SignaturePackage signaturePackage) {
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

    public boolean isSignatureContainer(InputStream stream) throws SignatureException
    {
        return impl.isSignatureContainer(stream);
    }

    public void read(InputStream stream) throws SignatureException
    {
        impl.read(stream);
    }

    public void write(OutputStream stream) throws SignatureException
    {
        impl.write(stream);
    }

    public Object getUnderlyingObject()
    {
        return impl.getUnderlyingObject();
    }

    @Override
    public void close() throws IOException {

    }
}
