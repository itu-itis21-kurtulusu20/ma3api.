using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms
{

	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


	/// <summary>
	/// Signature algorithm for xml signatures.
	/// 
	/// @implementors require default constructors!
	/// 
	/// @author ahmety
	///         date: Aug 26, 2009
	/// </summary>
	public interface XmlSignatureAlgorithm
	{
		string AlgorithmName {get;}

        void initSign(IPrivateKey aKey, IAlgorithmParams aParameters);

		void initVerify(IPublicKey aKey, IAlgorithmParams aParameters);

		void update(byte[] aData);

		byte[] sign();

		bool verify(byte[] aSignatureValue);

	}

}