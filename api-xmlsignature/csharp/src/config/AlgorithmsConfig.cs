using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{

	using Element = XmlElement;
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;


    // Obsolete yapılmış ama kullanılmaya devam ediliyor. Obsolete'liği kaldırıldı.
    // [Obsolete]

	public class AlgorithmsConfig : tr.gov.tubitak.uekae.esya.api.signature.config.AlgorithmsConfig
	{
        public AlgorithmsConfig(Element aElement) : base(aElement)
        {
            
        }

        public AlgorithmsConfig(tr.gov.tubitak.uekae.esya.api.signature.config.AlgorithmsConfig c) : base()
        {
            setDigestAlg(c.getDigestAlg());
            setSignatureAlg(c.getSignatureAlg());
            setDigestAlgForOCSP(c.getDigestAlgForOCSP());
        }

        public virtual DigestMethod DigestMethod
		{
			get
			{
				return DigestMethod.resolveFromName(getDigestAlg());
			}
		}
	}

}