using System;
using System.Security.Cryptography;
using System.Security.Cryptography.Xml;
using System.Xml;
using Org.BouncyCastle.Math;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo
{

    using Element = XmlElement;
	using Logger = log4net.ILog;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;



	/// <summary>
	/// <dl>
	///   <dt>Identifier</dt> <dd>
	///      <code>Type="<a name="DSAKeyValue" id="DSAKeyValue"
	///          href="http://www.w3.org/2000/09/xmldsig#DSAKeyValue"
	///          >http://www.w3.org/2000/09/xmldsig#DSAKeyValue</a>"
	///      </code> (this can be used within a <code>RetrievalMethod</code> or
	///  <code>Reference</code> element to identify the referent's type)</dd>
	/// </dl>
	/// 
	/// <p>DSA keys and the DSA signature algorithm are specified in [DSS]. DSA
	/// public key values can have the following fields:</p>
	/// 
	/// <dl>
	///    <dt><code>P</code></dt>
	///    <dd>a prime modulus meeting the [DSS] requirements</dd>
	///    <dt><code>Q</code></dt>
	///    <dd>an integer in the range 2**159 &lt; Q &lt; 2**160 which is a prime
	///    divisor of P-1</dd>
	///    <dt><code>G</code></dt>
	///    <dd>an integer with certain properties with respect to P and Q</dd>
	///    <dt><code>Y</code></dt>
	///    <dd>G**X mod P (where X is part of the private key and not made public)</dd>
	///    <dt><code>J</code></dt>
	///    <dd>(P - 1) / Q</dd>
	///    <dt><code>seed</code></dt>
	///    <dd>a DSA prime generation seed</dd>
	///    <dt><code>pgenCounter</code></dt>
	///    <dd>a DSA prime generation counter</dd>
	///  </dl>
	/// 
	/// <p>Parameter J is available for inclusion solely for efficiency as it is
	/// calculatable from P and Q. Parameters seed and pgenCounter are used in the
	/// DSA prime number generation algorithm  specified in [DSS]. As such, they are
	/// optional but must either both be present or both be absent. This prime
	/// generation algorithm is designed to provide assurance that a weak prime is
	/// not being used and it yields a P and Q value. Parameters P, Q, and G can be
	/// public and common to a group of users. They might be known from application
	/// context. As such, they are optional but P and Q must either both appear or
	/// both be absent. If all of <code>P</code>, <code>Q</code>, <code>seed</code>,
	/// and <code>pgenCounter</code> are present, implementations are not required to
	/// check if they are consistent and are free to use either <code>P</code> and
	/// <code>Q</code> or <code>seed</code> and <code>pgenCounter</code>. All
	/// parameters are encoded as base64 [<a href="#ref-MIME" shape="rect">MIME</a>]
	/// values.</p>
	/// 
	/// <p>Arbitrary-length integers (e.g. "bignums" such as RSA moduli) are
	/// represented in XML as octet strings as defined by the
	/// <a href="#sec-CryptoBinary"><code>ds:CryptoBinary</code> type</a>.</p>
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// <pre>
	/// &lt;complexType name="DSAKeyValueType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;sequence>
	///         &lt;sequence minOccurs="0">
	///           &lt;element name="P" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
	///           &lt;element name="Q" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
	///         &lt;/sequence>
	///         &lt;element name="G" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary" minOccurs="0"/>
	///         &lt;element name="Y" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
	///         &lt;element name="J" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary" minOccurs="0"/>
	///         &lt;sequence minOccurs="0">
	///           &lt;element name="Seed" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
	///           &lt;element name="PgenCounter" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
	///         &lt;/sequence>
	///       &lt;/sequence>
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jun 10, 2009
	/// </summary>
	public class DSAKeyValue : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement, KeyValueElement
	{
		private static Logger logger = log4net.LogManager.GetLogger(typeof(DSAKeyValue));

		protected internal BigInteger mP;
		protected internal BigInteger mQ;
		protected internal BigInteger mG;
		protected internal BigInteger mY;

	    protected internal DSAParameters mDsaParams;
		
	    protected internal DSA mDSA;
        public DSAKeyValue(Context aBaglam, DSA aDsa)
            : base(aBaglam)
        {
            mDSA = aDsa;
            DSAParameters parameters = mDSA.ExportParameters(false);
            mG = new BigInteger(parameters.G);
            mP = new BigInteger(parameters.P);
            mQ = new BigInteger(parameters.Q);
            mY = new BigInteger(parameters.Y);

            mDsaParams = parameters;

			if (logger.IsDebugEnabled)
			{
				logger.Debug("Constructing dsa key value from public key");
				logger.Debug("p: " + mP);
				logger.Debug("q: " + mQ);
				logger.Debug("g: " + mG);
				logger.Debug("y: " + mY);
			}
			addLineBreak();
			addBigIntegerElement(mG, Constants.NS_XMLDSIG, Constants.TAG_G);
			addLineBreak();
			addBigIntegerElement(mP, Constants.NS_XMLDSIG, Constants.TAG_P);
			addLineBreak();
			addBigIntegerElement(mQ, Constants.NS_XMLDSIG, Constants.TAG_Q);
			addLineBreak();
			addBigIntegerElement(mY, Constants.NS_XMLDSIG, Constants.TAG_Y);
			addLineBreak();
		}

		/// <summary>
		///  Construct DSAKeyValue from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public DSAKeyValue(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public DSAKeyValue(Element aElement, Context aContext) : base(aElement, aContext)
		{

			mG = getBigIntegerFromElement(Constants.NS_XMLDSIG, Constants.TAG_G);
			mP = getBigIntegerFromElement(Constants.NS_XMLDSIG, Constants.TAG_P);
			mQ = getBigIntegerFromElement(Constants.NS_XMLDSIG, Constants.TAG_Q);
			mY = getBigIntegerFromElement(Constants.NS_XMLDSIG, Constants.TAG_Y);
			if (logger.IsDebugEnabled)
			{
				logger.Debug("Constructing dsa key value from xml element");
				logger.Debug("p: " + mP);
				logger.Debug("q: " + mQ);
				logger.Debug("g: " + mG);
				logger.Debug("y: " + mY);
			}
		}


        public virtual AsymmetricAlgorithm PublicKey
		{
			get
			{
				if (mDSA == null)
				{
					// generate public key from its components
					logger.Debug("Generating dsa public key.");
					try
					{
                        DSACryptoServiceProvider dsa = new DSACryptoServiceProvider();
					    mDsaParams = dsa.ExportParameters(false);
                        mDSA.ImportParameters(mDsaParams);
					}
					catch (Exception x)
					{
						logger.Error("Error occurred in dsa public key gen.", x);
						throw new XMLSignatureException(x, "core.cantGeneratePublicKey", "DSA");
					}
				}
				return mDSA;
			}
		}

		public virtual BigInteger P
		{
			get
			{
                return new BigInteger(mDsaParams.P);
			}
		}

		public virtual BigInteger Q
		{
			get
			{
                return new BigInteger(mDsaParams.Q);
			}
		}

		public virtual BigInteger G
		{
			get
			{
                return new BigInteger(mDsaParams.G);
			}
		}

		public virtual BigInteger Y
		{
			get
			{
                return new BigInteger(mDsaParams.Y);
			}
		}


		// baseElement metodlari
		public override string LocalName
		{
			get
			{
				return Constants.TAG_DSAKEYVALUE;
			}
		}
	}

}