package tr.gov.tubitak.uekae.esya.api.signature.sigpackage;


import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;

/**
 * Signature container and its location in the signature package
 * @author ayetgin
 */
public interface SignatureContainerEntry {

    String getASiCDocumentName();

    SignatureContainer getContainer();

}
