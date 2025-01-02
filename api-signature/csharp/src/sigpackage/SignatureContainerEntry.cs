using System;

namespace tr.gov.tubitak.uekae.esya.api.signature.sigpackage
{
    /**
     * Signature container and its location in the signature package
     * @author suleyman.uslu
     */
    public interface SignatureContainerEntry
    {
        String getASiCDocumentName();

        SignatureContainer getContainer();
    }
}
