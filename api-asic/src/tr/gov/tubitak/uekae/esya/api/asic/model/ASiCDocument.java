package tr.gov.tubitak.uekae.esya.api.asic.model;

import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * This particular xml document suppose to
 *    output same exact bytes that comes in if document did not change
 *    In signatures' world, this is how things goes...
 * @author ayetgin
 */
public interface ASiCDocument
{
    void read(InputStream inputStream) throws SignatureException;

    /**
     * Output document content to stream. If document has no changes
     * this method supposed to output same bytes that were read.
     *
     * @param outputStream
     * @throws SignatureException
     */
    void write(OutputStream outputStream) throws SignatureException;

    String getASiCDocumentName();
    void setASiCDocumentName(String name);

}
