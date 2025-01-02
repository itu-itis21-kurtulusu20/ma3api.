using System.Security.Cryptography;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


	/// <summary>
	/// @author ahmety
	/// date: Jun 10, 2009
	/// </summary>
	public interface KeyValueElement
	{
        AsymmetricAlgorithm PublicKey { get; }
	}

}