using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms
{

	using Crypto = tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
	using Signer = tr.gov.tubitak.uekae.esya.api.crypto.Signer;
    using Verifier = tr.gov.tubitak.uekae.esya.api.crypto.IVerifier;
	using SignatureAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
	using CryptoException = tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;



	/// <summary>
	/// @author ahmety
	/// date: Aug 27, 2009
	/// </summary>
	public class BaseXmlSignatureAlgorithm : XmlSignatureAlgorithm
	{
		protected internal Signer mSigner;
		protected internal Verifier mVerifier;
		protected internal SignatureAlg mSignatureAlg;
		protected internal bool mSign = false;

		public BaseXmlSignatureAlgorithm(SignatureAlg aSignatureAlg)
		{
			mSignatureAlg = aSignatureAlg;
		}

		public virtual string AlgorithmName
		{
			get
			{
				return mSignatureAlg.getName();
			}
		}

		public virtual void initSign(IPrivateKey aKey, IAlgorithmParams aParameters)
		{
			if (!(aKey is PrivateKey))
			{
				throw new XMLSignatureException("core.invalid.privatekey", "ECDSA", aKey.ToString());
			}
			try
			{
				mSigner = Crypto.getSigner(mSignatureAlg);
                mSigner.init(aKey,aParameters);
				mSign = true;
			}
			catch (CryptoException x)
			{
				throw new XMLSignatureException(x, "errors.cantInit", "Signer");
			}
		}

        public virtual void initVerify(IPublicKey aKey, IAlgorithmParams aParameters)
		{
			if (!(aKey is PublicKey))
			{
				throw new XMLSignatureException("core.invalid.publickey", "ECDSA", aKey.ToString());
			}
			try
			{
			    Crypto.getVerifier(mSignatureAlg);
				mVerifier = Crypto.getVerifier(mSignatureAlg);
				mVerifier.init((PublicKey)aKey, aParameters);
				mSign = false;
			}
			catch (CryptoException x)
			{
				throw new XMLSignatureException(x, "errors.cantInit", I18n.translate("signer"));
			}

		}

		public virtual void update(byte[] aData)
		{
			try
			{
				if (mSign)
				{
					mSigner.update(aData, 0, aData.Length);
				}
				else
				{
					mVerifier.update(aData, 0, aData.Length);
				}
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "errors.cantUpdate", "Signature");
			}

		}

		public virtual byte[] sign()
		{
			try
			{
				byte[] asn1Bytes = mSigner.sign(null);
				return asn1Bytes;
			}
			catch (CryptoException x)
			{
				throw new XMLSignatureException(x, "errors.Sign error");
			}
		}

		public virtual bool verify(byte[] aSignatureValue)
		{
			try
			{
				return mVerifier.verifySignature(aSignatureValue);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "errors.verify");
			}
		}


	}
}