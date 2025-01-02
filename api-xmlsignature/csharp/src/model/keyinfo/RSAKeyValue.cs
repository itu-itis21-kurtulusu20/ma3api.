using System;
using System.Security.Cryptography;
using Org.BouncyCastle.Math;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

    using Element = System.Xml.XmlElement;
    using Logger = log4net.ILog;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


	/// <summary>
	/// <dt>Identifier</dt>
	/// 
	/// <dd><code>Type="<a name="RSAKeyValue" id="RSAKeyValue"
	///   href="http://www.w3.org/2000/09/xmldsig#RSAKeyValue"
	///   >http://www.w3.org/2000/09/xmldsig#RSAKeyValue</a>"
	///   </code> (this can be used within a <code>RetrievalMethod</code> or
	///   <code>Reference</code> element to identify the referent's type)</dd>
	/// </dl>
	/// 
	/// <p>RSA key values have two fields: Modulus and Exponent.</p>
	///  <pre>
	/// &lt;RSAKeyValue&gt;
	///    &lt;Modulus&gt;xA7SEU+e0yQH5rm9kbCDN9o3aPIo7HbP7tX6WOocLZAtNfyxSZDU16ksL6W
	///        jubafOqNEpcwR3RdFsT7bCqnXPBe5ELh5u4VEy19MzxkXRgrMvavzyBpVRgBUwUlV
	///        5foK5hhmbktQhyNdy/6LpQRhDUDsTvK+g9Ucj47es9AQJ3U=
	///   &lt;/Modulus&gt;
	///   &lt;Exponent&gt;AQAB&lt;/Exponent&gt;
	/// &lt;/RSAKeyValue&gt;
	/// </pre>
	/// 
	/// <p>Arbitrary-length integers (e.g. "bignums" such as RSA moduli) are
	/// represented in XML as octet strings as defined by the
	/// <code>ds:CryptoBinary</code> type.</p>
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// 
	/// <pre>
	/// &lt;complexType name="RSAKeyValueType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;sequence>
	///         &lt;element name="Modulus" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
	///         &lt;element name="Exponent" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
	///       &lt;/sequence>
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// 
	/// 
	/// @author ahmety
	/// date: Jun 10, 2009
	/// </summary>
	public class RSAKeyValue : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, KeyValueElement
	{

		private static Logger logger = log4net.LogManager.GetLogger(typeof(RSAKeyValue));

		private readonly BigInteger mModulus;
		private readonly BigInteger mExponent;
        /*
		private RSAPublicKey mPublicKey;
         */
	    private readonly RSA mRSA;
	    private RSAParameters mRsaParams;

        public RSAKeyValue(Context aBaglam, RSA aRsa)
            : base(aBaglam)
        {
            mRSA = aRsa;
            mRsaParams = mRSA.ExportParameters(false);
            mModulus = new BigInteger(mRsaParams.Modulus);
            mExponent = new BigInteger(mRsaParams.Exponent);

			if (logger.IsDebugEnabled)
			{
				logger.Debug("Constructing rsa key value from public key");
				logger.Debug("modulus: " + mModulus);
				logger.Debug("exponent: " + mExponent);
			}

			addLineBreak();
			addBigIntegerElement(mModulus, Constants.NS_XMLDSIG, Constants.TAG_MODULUS);
			addLineBreak();
			addBigIntegerElement(mExponent, Constants.NS_XMLDSIG, Constants.TAG_EXPONENT);
			addLineBreak();
		}

		/// <summary>
		///  Construct RSAKeyValue from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
		public RSAKeyValue(Element aElement, Context aContext) : base(aElement, aContext)
		{

			mModulus = getBigIntegerFromElement(Constants.NS_XMLDSIG, Constants.TAG_MODULUS);
			mExponent = getBigIntegerFromElement(Constants.NS_XMLDSIG, Constants.TAG_EXPONENT);

			if (logger.IsDebugEnabled)
			{
				logger.Debug("Constructing rsa key value from xml element");
				logger.Debug("modulus: " + mModulus);
				logger.Debug("exponent: " + mExponent);
			}

		}

		public virtual AsymmetricAlgorithm PublicKey
		{
			get
			{
				if (mRSA == null)
				{
					// generate public key from its components
					logger.Debug("Generating rsa public key.");
    
					try
					{
                        RSACryptoServiceProvider provider = new RSACryptoServiceProvider();
					    mRsaParams = provider.ExportParameters(false);
                        mRSA.ImportParameters(mRsaParams);
					}
					catch (Exception x)
					{
						throw new XMLSignatureException(x, "core.cantGeneratePublicKey", "RSA");
					}
				}
				return mRSA;
			}
		}

		public virtual BigInteger Modulus
		{
			get
			{
				return mModulus;
			}
		}

		public virtual BigInteger Exponent
		{
			get
			{
				return mExponent;
			}
		}


		// baseElement metodlari
		public override string LocalName
		{
			get
			{
				return Constants.TAG_RSAKEYVALUE;
			}
		}
	}

}