using System.Security.Cryptography;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.formats
{
    using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using BaseSigner = tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
	using SignatureValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureValidationResult;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


	/// <summary>
	/// @author ahmety
	/// date: May 8, 2009
	/// </summary>
	public interface SignatureFormat
	{
		XMLSignature createCounterSignature();
		void addKeyInfo(ECertificate aCertificate);
        void addKeyInfo(AsymmetricAlgorithm pk);
		XMLSignature sign(BaseSigner aSigner);		
		XMLSignature sign(IPrivateKey aKey);
		SignatureValidationResult validateCore();
        SignatureValidationResult validateCore(IPublicKey aKey);
		SignatureValidationResult validateCore(ECertificate aCertificate);
        SignatureValidationResult validateSignatureValue(IPublicKey aKey);
        SignatureFormat evolveToT();
		SignatureFormat evolveToC();
		SignatureFormat evolveToX1();
		SignatureFormat evolveToX2();
		SignatureFormat evolveToXL();
		SignatureFormat evolveToA();
		SignatureFormat addArchiveTimeStamp();

	}

}