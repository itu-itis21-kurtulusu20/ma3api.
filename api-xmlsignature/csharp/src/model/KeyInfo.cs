using System;
using System.Collections.Generic;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.core;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model
{

	using Logger = log4net.ILog;
	using Element = XmlElement;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using CryptoException = tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
	using KeyUtil = tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	//using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.I18n;
	using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;
	using X509Certificate = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509Certificate;
	using X509DataElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509.X509DataElement;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    /// <summary>
    /// <p>KeyInfo is an optional element that enables the recipient(s) to obtain the
    /// key needed to validate the signature.  KeyInfo may contain keys, names,
    /// certificates and other public key management information, such as in-band key
    /// distribution or key agreement data. This specification defines a few simple
    /// types but applications may extend those types or all together replace them
    /// with their own key identification and exchange semantics using the XML
    /// namespace facility. [XML-ns] However, questions of trust of such key
    /// information (e.g., its authenticity or  strength) are out of scope of this
    /// specification and left to the application.
    /// <p/>
    /// <p>If KeyInfo is omitted, the recipient is expected to be able to identify
    /// the key based on application context. Multiple declarations within KeyInfo
    /// refer to the same key. While applications may define and use any mechanism
    /// they choose through inclusion of elements from a different namespace,
    /// compliant versions MUST implement KeyValue (section 4.4.2) and SHOULD
    /// implement RetrievalMethod (section 4.4.3).
    /// <p/>
    /// <p>The schema/DTD specifications of many of KeyInfo's children (e.g., PGPData,
    /// SPKIData, X509Data) permit their content to be extended/complemented with
    /// elements from another namespace. This may be done only if it is safe to
    /// ignore these extension elements while claiming support for the types defined
    /// in this specification. Otherwise, external elements, including alternative
    /// structures to those defined by this specification, MUST be a child of
    /// KeyInfo. For example, should a complete XML-PGP standard be defined, its root
    /// element MUST be a child of KeyInfo. (Of course, new structures from external
    /// namespaces can incorporate elements from the &dsig; namespace via features of
    /// the type definition language. For instance, they can create a DTD that mixes
    /// their own and dsig qualified elements, or a schema that permits, includes,
    /// imports, or derives new types based on &dsig; elements.)
    /// <p/>
    /// <p>The following schema fragment specifies the expected content contained
    /// within this class.
    /// <p/>
    /// <pre>
    /// &lt;complexType name="KeyInfoType">
    ///   &lt;complexContent>
    ///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
    ///       &lt;choice maxOccurs="unbounded">
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}KeyName"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}KeyValue"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}RetrievalMethod"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}X509Data"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}PGPData"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}SPKIData"/>
    ///         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}MgmtData"/>
    ///         &lt;any processContents='lax' namespace='##other'/>
    ///       &lt;/choice>
    ///       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
    ///     &lt;/restriction>
    ///   &lt;/complexContent>
    /// &lt;/complexType>
    /// </pre>
    /// 
    /// @author ahmety
    ///         date: Jun 10, 2009
    /// </summary>
    public class KeyInfo : BaseElement
	{
		private static Logger logger = log4net.LogManager.GetLogger(typeof(KeyInfo));

		// KeyName, KeyValue, RetrievalMethod, X509Data, PGPData, SPKIData, MgmtData
		private readonly IList<KeyInfoElement> mIcerik = new List<KeyInfoElement>(1);

		private bool mInited = false;

		public KeyInfo(Context aBaglam) : base(aBaglam)
		{

			addLineBreak();
		}

		/// <summary>
		/// Construct KeyInfo from existing
		/// </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///                               resolved appropriately </exception>
		public KeyInfo(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		// delay construction of conntent because some children may reference a not
		// yet constructed conted via RetriecalMethod!

		private void init()
		{
			mInited = true;
			IList<Element> children = XmlCommonUtil.selectChildElements(mElement);
			foreach (Element element in children)
			{
				KeyInfoElement kie = resolve(element, mContext);
				if (kie != null)
				{
					if (kie is RetrievalMethod)
					{
						RetrievalMethod rm = (RetrievalMethod) kie;
						kie = rm.resolve();
					}
					mIcerik.Add(kie);
					if (logger.IsDebugEnabled)
					{
						logger.Debug("Key info element: " + kie);
					}
				}
			}
		}

		public static KeyInfoElement resolve(Element aElement, Context aBaglam)
		{
			string name = aElement.LocalName;

            if (name.Equals(Constants.TAG_KEYNAME))
			{
				return new KeyName(aElement, aBaglam);
			}
            else if (name.Equals(Constants.TAG_KEYVALUE))
			{
				return new KeyValue(aElement, aBaglam);
			}
            else if (name.Equals(Constants.TAG_RETRIEVALMETHOD))
			{
				return new RetrievalMethod(aElement, aBaglam);
			}
            else if (name.Equals(Constants.TAG_X509DATA))
			{
				return new X509Data(aElement, aBaglam);
			}
            else if (name.Equals(Constants.TAG_PGPDATA))
			{
				return new PGPData(aElement, aBaglam);
			}
            else if (name.Equals(Constants.TAG_SPKIDATA))
			{
				return new SPKIData(aElement, aBaglam);
			}
            else if (name.Equals(Constants.TAG_MGMTDATA))
			{
				return new MgmtData(aElement, aBaglam);
			}
			else
			{
				//throw new XMLSignatureException("Unknown KeyInfoElement:" + name);
				// todo unknown permitted element
			}
			return null;
		}

		public virtual int ElementCount
		{
			get
			{
				if (!mInited)
				{
					init();
				}
				return mIcerik.Count;
			}
		}

		public virtual KeyInfoElement get(int aIndex)
		{
			if (!mInited)
			{
				init();
			}
			return mIcerik[aIndex];
		}

		public virtual void add(KeyInfoElement aElement)
		{
			mIcerik.Add(aElement);
			mElement.AppendChild(((BaseElement) aElement).Element);
			addLineBreak();
		}

		public virtual ECertificate resolveCertificate()
		{
			for (int i = 0; i < ElementCount; i++)
			{
				KeyInfoElement o = get(i);

				try
				{
					if (o is X509Data)
					{
						X509Data data = (X509Data) o;
						for (int j = 0; j < data.ElementCount; j++)
						{
							X509DataElement xde = data.get(j);
							if (xde is X509Certificate)
							{
								X509Certificate cert = (X509Certificate) xde;
								return new ECertificate(cert.CertificateBytes);
							}
						}
					}
					else if (o is RetrievalMethod)
					{
						// retrieval metod raxX509 a map eder
						return new ECertificate(((RetrievalMethod) o).RawX509);
					}
				}
				catch (Exception exc)
				{
					throw new XMLSignatureException(exc,"core.invalidKeyInfo");
				}

			}
			return null;
		}

        public virtual IPublicKey resolvePublicKey()
		{
            //E�er sertifika var ise public key ondan al�n�yor.
			ECertificate certificate = resolveCertificate();
			if (certificate != null)
			{
				try
				{
				    return KeyUtil.decodePublicKey(certificate.getSubjectPublicKeyInfo());
				}
				catch (CryptoException c)
				{
					throw new XMLSignatureException(c, "errors.cantDecode", certificate.getSubjectPublicKeyInfo(), I18n.translate("publicKey"));
				}
			}

            //TODO Sertifika yok ise parametrelerden public key olu�turulabilmelidir.
            //E�er sertifika yok ise public key 
            //Key infodan al�n�yor.
            /*
			for (int i = 0; i < ElementCount; i++)
			{
				KeyInfoElement o = get(i);

				if (o is KeyValue)
				{
                    
					KeyValue kv = (KeyValue) o;
				    AsymmetricAlgorithm asymmetricAlgorithm = kv.Value.PublicKey;
				    KeyUtil.decodePublicKey(asymmetricAlgorithm,kv.)
					return 
				}
				// todo more key info types
			}*/
			return null;
		}

		// baseElement metodlari

		public override string LocalName
		{
			get
			{
				return Constants.TAG_KEYINFO;
			}
		}

	}

}