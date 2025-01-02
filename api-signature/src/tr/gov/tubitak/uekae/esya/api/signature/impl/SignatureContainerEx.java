package tr.gov.tubitak.uekae.esya.api.signature.impl;

import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignaturePackage;

/**
 * interface for signature container implementers
 * contains detachSignature method callback
 * for signature implementers convenience and setSignaturePackage for
 * package implementers
 *
 * @author ayetgin
 */
public interface SignatureContainerEx extends SignatureContainer
{
    public void detachSignature(Signature signature) throws SignatureException;

    public void setSignaturePackage(SignaturePackage signaturePackage);
}
