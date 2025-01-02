using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Security.Cryptography;
using System.Text;
using Org.BouncyCastle.Crypto.Engines;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Utilities;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

using Org.BouncyCastle.Math;
using tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8;


namespace tr.gov.tubitak.uekae.esya.api.crypto.sig.rsa
{
    public class RSA_SSCD
    {
        protected BaseSigner mSigner;
        protected int blockSize;
        protected BigInteger mModulus;
        protected DigestAlg mDigestAlg;
        protected ERSAPublicKey mPublicKey;
        

        protected static readonly ILog LOGGER = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        byte[] mDataEmbedded;

        public static RSA_SSCD getInstance(DigestAlg aDigestAlg, IRandomGenerator aRand)
        {
            return aRand == null ? new RSA_SSCD(aDigestAlg, new BouncyRandomGenerator()) : new RSA_SSCD(aDigestAlg, aRand);
        }

        public static  RSA_SSCD getInstance(DigestAlg aDigestAlg)
	    {
            return new RSA_SSCD(aDigestAlg, new BouncyRandomGenerator());
	    }

        private RSA_SSCD(DigestAlg aDigestAlg, IRandomGenerator irnd)
        {
            mDigestAlg = aDigestAlg;
        }
        public void setupForVerification(IPublicKey key)
        {                  
           ESubjectPublicKeyInfo subjectPublicKeyInfo = new ESubjectPublicKeyInfo(key.getEncoded());           
           mPublicKey = new ERSAPublicKey(subjectPublicKeyInfo.getSubjectPublicKey());           
        }

        public void setupForSigning(IPrivateKey key)
        {
            RsaKeyParameters param = (RsaKeyParameters)BouncyProviderUtil.resolvePrivateKey(key);

            mModulus = param.Modulus;
          
            blockSize = (mModulus.BitLength + 7) / 8;
		
		    Signer signer = Crypto.getSigner(SignatureAlg.RSA_RAW);
          
		    signer.init(key);
		    mSigner = signer;
	    }

        public void setupForSigning(BaseSigner signer, BigInteger modulus)
        {
            mModulus = modulus;
            blockSize = ((modulus.BitLength) + 7) / 8;
            mSigner = signer;
        }

      private byte[] I2OSP(BigInteger s, int k) 
      {
            byte[] result = s.ToByteArray();
          if (result.Length < k) {
             byte[] newResult = new byte[k];
             Array.Copy(result, 0, newResult, k-result.Length, result.Length);
             result = newResult;
          } else if (result.Length > k) { // leftmost extra bytes should all be 0
             int limit = result.Length - k;
             for (int i = 0; i < limit; i++) {
                if (result[i] != 0x00) {
                   throw new ArgumentException("integer too large");
                }
             }
             byte[] newResult = new byte[k];
             Array.Copy(result, limit, newResult, 0, k);
             result = newResult;
          }
          return result;
   }
        /**
	 * Ozet=Hash(PRND||dataToBeSigned)
	 * ImzaVerisi=(â€™6Aâ€™||PRND||dataToBeEmbedded||Ozet||â€™BCâ€™) 
	 * Imza=Sign(Private,ImzaVerisi)
	 * ImzaMin=Min(Imza,N.GEM-Imza)
	 * @param dataToBeSigned
	 * @param dataToBeRecovered
	 * @return
	 * @throws IllegalStateException
	 * @throws ESYAException 
	 */
	public byte [] generateSignature(byte [] dataToBeSigned, byte [] dataToBeRecovered)
	{
		
		byte [] toBeSigned = new byte[blockSize];
		toBeSigned[0] = 0x6A;
		
		byte [] PRND = new byte[blockSize - 2 - mDigestAlg.getDigestLength() - dataToBeRecovered.Length];
		try
		{

		    RandomUtil.generateRandom(PRND, 0, PRND.Length);
			
            Array.Copy(PRND, 0, toBeSigned, 1, PRND.Length);
            

            Array.Copy(dataToBeRecovered, 0, toBeSigned, toBeSigned.Length - 1 - mDigestAlg.getDigestLength()
                    - dataToBeRecovered.Length, dataToBeRecovered.Length);
			
			byte [] toBeDigest = ByteUtil.concatAll(PRND, dataToBeSigned);
			byte [] digest = DigestUtil.digest(mDigestAlg, toBeDigest);

            Array.Copy(digest, 0, toBeSigned, toBeSigned.Length - 1 - digest.Length, digest.Length);

            toBeSigned[toBeSigned.Length - 1] = (byte)0xBC;
			
			byte [] sBytes = mSigner.sign(toBeSigned);
			BigInteger s = new BigInteger(1, sBytes);
			
			
			if(mModulus.Divide(BigInteger.ValueOf(2)).CompareTo(s) == -1)
			{
				s = mModulus.Subtract(s);
			}
			
            
			return I2OSP(s, blockSize);
			
		} 
		
		catch (CryptoException e) 
		{
            throw new ESYARuntimeException("Digest problem", e);
		}
	}


        /**
	 * Ozet=Hash(PRND1||dataEmbedded||aDataThatisIncludedByMe)
	 * ImzaVerisi=(â€™6Aâ€™||PRND1||dataEmbedded||Ozet||â€™BCâ€™) 
	 * Imza=Sign(PrK.TCKK.GM,ImzaVerisi)
	 * @param signature
	 * @param aDataThatisIncludedByMe
	 * @return
	 * @throws IllegalStateException
	 */
	public Boolean verifySignature(byte [] signature,  byte [] aDataThatisIncludedByMe, int aLenOfDataToBeRecovered)			
	{
		BigInteger s = new BigInteger(1, signature);
        
        RsaBlindedEngine rsaEngine = new RsaBlindedEngine();
         RsaKeyParameters publicKey = new RsaKeyParameters(false, new BigInteger(mPublicKey.getModulus().mValue.GetData()), new BigInteger(mPublicKey.getPublicExponent().mValue.GetData()));

        
        byte[] mBytes;
		try 
		{
            rsaEngine.Init(false, publicKey);
            mBytes = rsaEngine.ProcessBlock(signature, 0, signature.Length);
		} 
		catch (ArgumentException x) 
		{
			return false;
		}
				
		byte CC = (byte) 0xCC;
		byte BC = (byte) 0xBC;
		
		Boolean implicitt = mBytes[mBytes.Length-1] == BC ? true : false;
		Boolean sha1 = (mBytes[mBytes.Length-1] == CC && mBytes[mBytes.Length-2] == 0x33) ? true : false;
		Boolean sha256 = (mBytes[mBytes.Length-1] == CC && mBytes[mBytes.Length-2] == 0x34) ? true : false;
		
		
		if(!(implicitt || sha1 || sha256))
		{
		    BigInteger m = new BigInteger(mBytes);
                
            m = new BigInteger(mPublicKey.getModulus().mValue.GetData()).Subtract(m);

			mBytes = m.ToByteArray();
			implicitt = mBytes[mBytes.Length-1] == BC ? true : false;
			sha1 = (mBytes[mBytes.Length-1] == CC && mBytes[mBytes.Length-2] == 0x33) ? true : false;
			sha256 = (mBytes[mBytes.Length-1] == CC && mBytes[mBytes.Length-2] == 0x34) ? true : false;
		}


        if (!(implicitt || sha1 || sha256))
		{
			return false;
		}
		
		int endLength = 1;
		if(sha1 || sha256)
			endLength = 2;
		
		int digestLen = mDigestAlg.getDigestLength();
		
		byte [] digestValue = new byte[digestLen];
		Array.Copy(mBytes, mBytes.Length - endLength - digestLen, 
				digestValue, 0, digestLen);

        byte[] otherDeviceData = new byte[mBytes.Length - endLength - digestLen - 1];
        Array.Copy(mBytes, 1, otherDeviceData, 0, otherDeviceData.Length);
		
		
		byte [] toBeDigestData = ByteUtil.concatAll(otherDeviceData, aDataThatisIncludedByMe);
		byte [] calculatedDigest = null;
		try 
		{
			calculatedDigest = DigestUtil.digest(mDigestAlg, toBeDigestData);
		} 
		catch (CryptoException e) 
		{
            LOGGER.Error("Can not calculate digest", e);
			return false;
		}
		
		if(Arrays.AreEqual(calculatedDigest, digestValue) == true)
		{
			mDataEmbedded = new byte[aLenOfDataToBeRecovered];
            Array.Copy(otherDeviceData, otherDeviceData.Length - aLenOfDataToBeRecovered,
					mDataEmbedded, 0, aLenOfDataToBeRecovered);
			return true;
		}
		else
			return false;
	}
		
	public byte [] getDataRecovered()
	{
		return mDataEmbedded;
	}
		

    }
}
