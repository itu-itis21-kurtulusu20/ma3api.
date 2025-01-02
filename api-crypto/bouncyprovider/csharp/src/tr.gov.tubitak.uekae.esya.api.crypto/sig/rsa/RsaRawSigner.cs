using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Math;
using Org.BouncyCastle.Utilities;

namespace tr.gov.tubitak.uekae.esya.api.crypto.sig.rsa
{
    public class RsaRawSigner : ISigner
    {
        private readonly IAsymmetricBlockCipher cipher;
        private readonly IDigest digest;

        private int modulusByteLen;

        public RsaRawSigner(
            IAsymmetricBlockCipher cipher, 
            IDigest digest)            
        {
            this.cipher = cipher;
            this.digest = digest;
        }

        public string AlgorithmName
        {
            get { return "RawRsa"; }
        }

        public void Init(bool forSigning, ICipherParameters parameters)
        {                                  
            RsaKeyParameters kParam;
            
            if (parameters is RsaBlindingParameters)
            {
                kParam = ((RsaBlindingParameters)parameters).PublicKey;
            }
            else
            {
                kParam = (RsaKeyParameters)parameters;
            }

            modulusByteLen =  ( kParam.Modulus.BitLength + 7 ) / 8;
            
            Reset();
            cipher.Init(forSigning, parameters);
        }

        public void Update(byte input)
        {
            digest.Update(input);
        }

        public void BlockUpdate(byte[] input, int inOff, int length)
        {
            digest.BlockUpdate(input, inOff, length);
        }

        public byte[] GenerateSignature()
        {
            if (digest.GetDigestSize() > modulusByteLen)
                throw new DataLengthException("Data is larger than modulus");

            byte[] content = new byte[digest.GetDigestSize()];
            digest.DoFinal(content, 0);

            byte[] b = cipher.ProcessBlock(content, 0, content.Length);
            return b;
        }

        public bool VerifySignature(byte[] signature)
        {
            byte[] expected = new byte[digest.GetDigestSize()];
            digest.DoFinal(expected, 0);

            byte[] sig;
          

            try
            {
                sig = cipher.ProcessBlock(signature, 0, signature.Length);
              
                if (Arrays.AreEqual(sig,expected))
                {
                    return true;
                }
            }
            catch (Exception)
            {
                return false;
            }

            return false;


        }

        public void Reset()
        {
            digest.Reset();
        }
    }
}
