using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8;
namespace tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8
{
    public class ERSAPublicKey : BaseASNWrapper<RSAPublicKey>
    {
        public ERSAPublicKey(RSAPublicKey aObject)
            : base(aObject)
        {
        }

        public ERSAPublicKey(Asn1BigInteger aModulus, Asn1BigInteger aPublicExponent) : base(new RSAPublicKey(aModulus, aPublicExponent)) { }

        public ERSAPublicKey(byte[] aObject)
            : base(aObject, new RSAPublicKey())
        {

        }
        /*
        public byte[] getEncodedBytes()
        {
            byte[] encoded = null;
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            try
            {
                mObject.Encode(encBuf);
                encoded = encBuf.MsgCopy;
                encBuf.Reset();
            }
            catch (Asn1Exception aEx)
            {
                //logger.error("Sertifika değeri alınırken hata oluştu.", aEx);
            }
            return encoded;
        }*/
        public Asn1BigInteger getModulus()
        {
            return mObject.modulus;
        }

        public Asn1BigInteger getPublicExponent()
        {
            return mObject.publicExponent;
        }

    }
}
