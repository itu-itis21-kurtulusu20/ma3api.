package tr.gov.tubitak.uekae.esya.api.pades.pdfbox;

import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETimeStampResponse;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.signature.Context;

import java.io.IOException;
import java.io.InputStream;

public class TSSignatureInterface implements SignatureInterface
{

    Context context;

    public TSSignatureInterface(Context context)
    {
        this.context = context;
    }

    @Override
    public byte[] sign(InputStream inputStream) throws IOException
    {
        DigestAlg digestAlg = context.getConfig().getTimestampConfig().getDigestAlg();

        byte[] digest;
        try
        {
            digest = DigestUtil.digestStream(digestAlg, inputStream);
        }
        catch (CryptoException e)
        {
            throw new IOException("Can not get digest for Time Stamp!", e);
        }

        TSClient tsClient = new TSClient();
        try
        {
            TSSettings tsSettings = context.getConfig().getTimestampConfig().getSettings();
            ETimeStampResponse timestamp = tsClient.timestamp(digest, tsSettings);

            byte [] encoded = timestamp.getContentInfo().getEncoded();
            return encoded;
        }
        catch (ESYAException e)
        {
            throw new IOException("Can not get Time Stamp!", e);
        }
    }
}
