package tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.signature.config.ConfigurationException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;

// Deprecated yapılmış ama kullanılmaya devam edilmiş. Deprecated olması kaldırıldı.
// author ayetgin
// deprecated
// see tr.gov.tubitak.uekae.esya.api.signature.config.AlgorithmsConfig

@Deprecated
public class AlgorithmsConfig extends tr.gov.tubitak.uekae.esya.api.signature.config.AlgorithmsConfig
{
    public AlgorithmsConfig(Element aElement) throws ConfigurationException
    {
        super(aElement);
    }

    public AlgorithmsConfig(tr.gov.tubitak.uekae.esya.api.signature.config.AlgorithmsConfig c)
    {
        super();
        setDigestAlg(c.getDigestAlg());
        setSignatureAlg(c.getSignatureAlg());
        setDigestAlgForOCSP(c.getDigestAlgForOCSP());
    }

    public DigestMethod getDigestMethod()
    {
        return DigestMethod.resolveFromName(getDigestAlg());
    }
}
