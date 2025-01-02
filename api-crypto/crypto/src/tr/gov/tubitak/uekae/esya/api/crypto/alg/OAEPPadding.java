package tr.gov.tubitak.uekae.esya.api.crypto.alg;


import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1OpenType;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithOctetString;
import tr.gov.tubitak.uekae.esya.asn.algorithms.RSAES_OAEP_params;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.util.UtilOpenType;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import javax.crypto.spec.PSource;
import java.io.IOException;

/**
 * @author ayetgin
 */

public class OAEPPadding extends Padding
{

    @Deprecated
    public static final OAEPPadding OAEP_SHA1_MGF1   = new OAEPPadding(DigestAlg.SHA1,   MGF.MGF1);
    public static final OAEPPadding OAEP_SHA256_MGF1 = new OAEPPadding(DigestAlg.SHA256, MGF.MGF1);
    public static final OAEPPadding OAEP_SHA512_MGF1 = new OAEPPadding(DigestAlg.SHA512, MGF.MGF1);

    private DigestAlg mDigestAlg;
    private MGF mMaskGenerationFunction;
    private PSource mPsourceAlg;

    public OAEPPadding(DigestAlg aDigestAlg, MGF aMaskGenerationFunction,PSource aPsourceAlg)
    {
        mDigestAlg = aDigestAlg;
        mMaskGenerationFunction = aMaskGenerationFunction;
        mPsourceAlg = aPsourceAlg;
    }


    public OAEPPadding(DigestAlg aDigestAlg, MGF aMaskGenerationFunction)
    {
        mDigestAlg = aDigestAlg;
        mMaskGenerationFunction = aMaskGenerationFunction;
    }

    public DigestAlg getDigestAlg()
    {
        return mDigestAlg;
    }

    public MGF getMaskGenerationFunction()
    {
        return mMaskGenerationFunction;
    }

    public PSource getPsourceAlg()
    {
        return mPsourceAlg;
    }

    public byte [] toRSAES_OAEP_params()
    {
        AlgorithmIdentifier hashAlg = new AlgorithmIdentifier(this.getDigestAlg().getOID(), UtilOpenType.Asn1NULL );
        AlgorithmIdentifier mgfAlg = new AlgorithmIdentifier(this.getMaskGenerationFunction().getOID(), new Asn1OpenType(this.getDigestAlg().toAlgorithmIdentifier().getEncoded()));
        AlgorithmIdentifier pSourceAlg = new AlgorithmIdentifier(_algorithmsValues.id_pSpecified, ParamsWithOctetString.Asn1OctetStringNULL );

        RSAES_OAEP_params oaepParams = new RSAES_OAEP_params(hashAlg, mgfAlg, pSourceAlg);

        Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
        oaepParams.encode(buff);

        return buff.getMsgCopy();
    }

    public static OAEPPadding fromRSAES_OAEP_params(byte [] encoded) throws IOException {
        Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(encoded);
        RSAES_OAEP_params params = new RSAES_OAEP_params();
        params.decode(buff);


        DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(params.hashAlgorithm));

        MGF mgfAlg = null;
        if(params.maskGenAlgorithm == null)
            mgfAlg = MGF.MGF1;
        else
            mgfAlg = MGF.fromAlgorithmIdentifier(new EAlgorithmIdentifier(params.maskGenAlgorithm));

        return new OAEPPadding(digestAlg, mgfAlg);


    }


}
