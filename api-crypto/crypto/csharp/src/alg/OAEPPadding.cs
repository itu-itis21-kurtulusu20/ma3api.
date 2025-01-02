/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass

using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class OAEPPadding : Padding
    {
        public static readonly OAEPPadding OAEP_SHA1_MGF1 = new OAEPPadding(DigestAlg.SHA1, MGF.MGF1);
        public static readonly OAEPPadding OAEP_SHA256_MGF1 = new OAEPPadding(DigestAlg.SHA256, MGF.MGF1);
        public static readonly OAEPPadding OAEP_SHA512_MGF1 = new OAEPPadding(DigestAlg.SHA512, MGF.MGF1);

        private readonly DigestAlg mDigestAlg;
        private readonly MGF mMaskGenerationFunction;
        //private Padding mPadding;

        private OAEPPadding(DigestAlg aDigestAlg, MGF aMaskGenerationFunction)
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

        public byte [] toRSAES_OAEP_params()
        {
           AlgorithmIdentifier hashAlg = new AlgorithmIdentifier(this.getDigestAlg().getOID(), UtilOpenType.Asn1NULL );
           AlgorithmIdentifier mgfAlg = new AlgorithmIdentifier(this.getMaskGenerationFunction().getOID(), new Asn1OpenType(this.getDigestAlg().toAlgorithmIdentifier().getEncoded()));
           AlgorithmIdentifier pSourceAlg = new AlgorithmIdentifier(_algorithmsValues.id_pSpecified, ParamsWithOctetString.Asn1OctetStringNULL );

           RSAES_OAEP_params oaepParams = new RSAES_OAEP_params(hashAlg, mgfAlg, pSourceAlg);

           Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
           oaepParams.Encode(buff);
 
           return buff.MsgCopy;
        }

        public static OAEPPadding fromRSAES_OAEP_params(byte  [] encoded) 
        {        
           Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(encoded);
           RSAES_OAEP_params parameters = new RSAES_OAEP_params();
           parameters.Decode(buff);

           DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(parameters.hashAlgorithm));
            MGF mgfAlg = null;
            if (parameters.maskGenAlgorithm == null)
              mgfAlg = MGF.MGF1;
            else
              mgfAlg = MGF.fromAlgorithmIdentifier(new EAlgorithmIdentifier(parameters.maskGenAlgorithm));

            return new OAEPPadding(digestAlg, mgfAlg);
       }
   }
}
