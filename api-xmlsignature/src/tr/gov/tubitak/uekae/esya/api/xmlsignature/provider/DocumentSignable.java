package tr.gov.tubitak.uekae.esya.api.xmlsignature.provider;

import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;

import java.io.InputStream;

/**
 * @author ayetgin
 */
public class DocumentSignable extends Document
{
    Signable signable;

    public DocumentSignable(Signable signable)
    {
        super(signable.getURI(), signable.getMimeType(), null);
        this.signable = signable;
    }

    @Override
    public InputStream getStream() throws XMLSignatureException
    {
        try {
            return signable.getContent();
        } catch (Exception x){
            throw new XMLSignatureException(x, x.getMessage());
        }
    }

    @Override
    public DataType getType()
    {
        return DataType.OCTETSTREAM;
    }
}
