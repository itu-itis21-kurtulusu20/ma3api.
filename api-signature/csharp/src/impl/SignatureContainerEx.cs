using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.signature.impl
{
    /**
     * interface for signature container implementers
     * contains detachSignature method callback
     * for signature implementers convenience and setSignaturePackage for
     * package implementers
     *
     * @author ayetgin
     */
    public interface SignatureContainerEx : SignatureContainer
   {
       void detachSignature(Signature signature);

       void setSignaturePackage(SignaturePackage signaturePackage);
   }
}
