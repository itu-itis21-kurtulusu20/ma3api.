package tr.gov.tubitak.uekae.esya.api.asic.model;

import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignatureContainerEntry;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Signature container and its location in the signature package
 * @author ayetgin
 */
public class SignatureContainerEntryImpl implements SignatureContainerEntry, ASiCDocument
{
    String entryName;
    SignatureContainer container;

    public SignatureContainerEntryImpl()
    {
    }

    public SignatureContainerEntryImpl(String aEntryName, SignatureContainer aContainer)
    {
        entryName = aEntryName;
        container = aContainer;
    }

    public void read(InputStream inputStream) throws SignatureException
    {
        container.read(inputStream);
    }

    public void write(OutputStream outputStream) throws SignatureException
    {
        container.write(outputStream);
    }

    public String getASiCDocumentName()
    {
        return entryName;
    }

    public void setASiCDocumentName(String name)
    {
        entryName = name;
    }

    public SignatureContainer getContainer()
    {
        return container;
    }

    public void setContainer(SignatureContainer aContainer)
    {
        container = aContainer;
    }
}
