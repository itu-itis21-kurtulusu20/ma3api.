package tr.gov.tubitak.uekae.esya.api.pades.pdfbox;

import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.IDTBSRetrieverSigner;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignableStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class PdfBoxSignatureInterface implements SignatureInterface
{
    private Signature cadesSignature;
    private BaseSigner signer;
    private Context context;


    public PdfBoxSignatureInterface(Signature cadesSignature, BaseSigner signer, Context context)
    {
        this.cadesSignature = cadesSignature;
        this.signer = signer;
        this.context = context;
    }

    @Override
    public byte[] sign(InputStream inputStream) throws IOException
    {
        try
        {
            cadesSignature.addContent(new SignableStream(inputStream, null, null), false);
            cadesSignature.sign(signer);

            // sign ES_T if requested
            if (context instanceof PAdESContext && ((PAdESContext) context).isSignWithTimestamp() && !(signer instanceof IDTBSRetrieverSigner)){
                cadesSignature.upgrade(SignatureType.ES_T);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            cadesSignature.getContainer().write(baos);
            return baos.toByteArray();
        }
        catch (Exception x)
        {
            throw new IOException(x);
        }
    }

}
