using System.Security.Cryptography;
using Org.BouncyCastle.Bcpg;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Pkcs;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyKeyFactory : IKeyFactory
    {
        #region IKeyFactory Members
        //todo aBytes subjectPublicKey'in encoded hali ise aAsymmetricAlg'ye gerek var mi???
        public IPublicKey decodePublicKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes)
        {
            //throw new NotImplementedException();
            return new PublicKey(aBytes);
        }

        public IPrivateKey decodePrivateKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes)
        {
            //throw new NotImplementedException();
            return new PrivateKey(aBytes);
        }

        public ISecretKey generateSecretKey(IKeySpec aKeySpec)
        {
            if (aKeySpec is PBEKeySpec)
                return new BouncyPBEKeyGenerator((PBEKeySpec)aKeySpec).generateKey();
            throw new UnknownElement("Unknown key spec " + aKeySpec);
        }

        public byte[] generateKey(CipherAlg aAlg, int aLength)
        {
            //throw new NotImplementedException();
            return RandomUtil.generateRandom(aLength / 8);   
        }

        public PrivateKey convertCSharpPrivateKey(RSACryptoServiceProvider aKey)
        {
            AsymmetricKeyParameter param = DotNetUtilities.GetRsaKeyPair(aKey).Private;

            return new PrivateKey(PrivateKeyInfoFactory.CreatePrivateKeyInfo(param).GetDerEncoded());
        }

        public int getKeyLength(IPublicKey aKey)
        {
            AsymmetricKeyParameter keyParameter =  BouncyProviderUtil.resolvePublicKey(aKey);

            if (keyParameter is RsaKeyParameters)
                return ((RsaKeyParameters) keyParameter).Modulus.BitLength;

            if (keyParameter is DsaKeyParameters)
                return ((DsaKeyParameters) keyParameter).Parameters.G.BitLength;

            if (keyParameter is ECPublicKeyParameters)
                return ((ECPublicKeyParameters) keyParameter).Parameters.Curve.FieldSize;//.BitLength; // getCurve().getField().getFieldSize();

            throw new ArgErrorException("");
        }
        #endregion
    }
}

