using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms
{

	using Logger = log4net.ILog;
	using Crypto = tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
	using MAC = tr.gov.tubitak.uekae.esya.api.crypto.IMAC;
	using MACAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
    using ParamsWithLength = tr.gov.tubitak.uekae.esya.api.crypto.parameters.ParamsWithLength;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

	//using SecretKey = javax.crypto.SecretKey;

	/// <summary>
	/// <p>MAC algorithms take two implicit parameters, their keying material
	/// determined from <code>KeyInfo</code> and the octet stream output by <code>
	/// CanonicalizationMethod</code>. MACs and signature algorithms are
	/// syntactically identical but a MAC implies a shared secret key.
	/// 
	/// <p>The <a href="http://www.ietf.org/rfc/rfc2104.txt">HMAC</a> algorithm
	/// (RFC2104 ) takes the output (truncation) length in bits as a parameter; this
	/// specification REQUIRES that the truncation length be a multiple of 8 (i.e.
	/// fall on a byte boundary) because Base64 encoding operates on full bytes.
	/// If the truncation parameter is not specified then all the bits of the hash
	/// are output. Any signature with a truncation length that is less than half
	/// the output length of the underlying hash algorithm MUST be deemed invalid. An
	/// example of an HMAC <code>SignatureMethod</code> element:</p>
	/// 
	/// <pre>   &lt;SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#hmac-sha1"&gt;
	///      &lt;HMACOutputLength&gt;128&lt;/HMACOutputLength&gt;
	///   &lt;/SignatureMethod&gt;
	/// </pre>
	/// 
	/// <p>The output of the HMAC algorithm is ultimately the output (possibly
	/// truncated) of the chosen digest algorithm. This value shall be base64
	/// encoded in the same straightforward fashion as the output of the digest
	/// algorithms.
	/// 
	/// <pre xml:space="preserve">   Schema Definition:
	/// 
	///   &lt;simpleType name="HMACOutputLengthType"&gt;
	///     &lt;restriction base="integer"/&gt;
	///   &lt;/simpleType&gt;
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jul 30, 2009
	/// </summary>
	public class HMACIntegrity : XmlSignatureAlgorithm
	{
		private static Logger logger = log4net.LogManager.GetLogger(typeof(HMACIntegrity));

		public const string PARAM_HMAC_OUTPUT_LEGTH = "hmac.output.lenght";

		private readonly MACAlg mMACAlg;
		private MAC mMAC;

		public HMACIntegrity(MACAlg aMACAlg)
		{
			mMACAlg = aMACAlg;
		}

		public virtual string AlgorithmName
		{
			get
			{
				return mMACAlg.getName();
			}
		}

        public virtual void initSign(IPrivateKey aKey, IAlgorithmParams aParameters)
		{
			_init(aKey, aParameters);
		}

        public virtual void initVerify(IPublicKey aKey, IAlgorithmParams aParameters)
		{
			_init(aKey, aParameters);

		}

		public virtual void update(byte[] aData)
		{
			try
			{
				mMAC.process(aData);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "errors.cantUpdate", "MAC");
			}
		}

		public virtual byte[] sign()
		{
			try
			{
				return mMAC.doFinal(null);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "Cant digest MAC");
			}
		}

		public virtual bool verify(byte[] aSignatureValue)
		{
			try
			{
				byte[] digest = mMAC.doFinal(null);
				if (logger.IsDebugEnabled)
				{
					logger.Debug("digest: " + ""+digest);
					logger.Debug("signatureValue: " + aSignatureValue+"");
				}
				return ArrayUtil.Equals(digest, aSignatureValue);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "Cant verify MAC");
			}

		}

        private void _init(IKey aKey, IAlgorithmParams aParameters)
		{
			if (logger.IsDebugEnabled)
			{
				logger.Debug("hmac init: " + aKey + ", " + aParameters);
			}

			if (!(aKey is ISecretKey))
			{
				throw new XMLSignatureException("core.invalid.secretkey", aKey.ToString());
			}

			try
			{
				mMAC = Crypto.getMAC(mMACAlg);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "Cant resolve Mac " + mMACAlg);
			}


			try
			{
				mMAC.init(aKey, aParameters);
			}
			catch (Exception e)
			{
				throw new XMLSignatureException(e, "errors.cantInit", "HMAC " + I18n.translate("signer"));
			}
		}

		public class HMACwithMD5 : HMACIntegrity
		{
			public HMACwithMD5() : base(MACAlg.HMAC_MD5)
			{
			}
		}
		public class HMACwithRIPEMD : HMACIntegrity
		{
			public HMACwithRIPEMD() : base(MACAlg.HMAC_RIPEMD)
			{
			}
		}
		public class HMACwithSHA1 : HMACIntegrity
		{
			public HMACwithSHA1() : base(MACAlg.HMAC_SHA1)
			{
			}
		}
		public class HMACwithSHA256 : HMACIntegrity
		{
			public HMACwithSHA256() : base(MACAlg.HMAC_SHA256)
			{
			}
		}
		public class HMACwithSHA384 : HMACIntegrity
		{
			public HMACwithSHA384() : base(MACAlg.HMAC_SHA384)
			{
			}
		}
		public class HMACwithSHA512 : HMACIntegrity
		{
			public HMACwithSHA512() : base(MACAlg.HMAC_SHA512)
			{
			}
		}
	}

}