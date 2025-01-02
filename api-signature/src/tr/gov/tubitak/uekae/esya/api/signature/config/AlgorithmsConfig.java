package tr.gov.tubitak.uekae.esya.api.signature.config;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.text.MessageFormat;

import static tr.gov.tubitak.uekae.esya.api.signature.config.ConfigConstants.*;

/**
 * Algorithms that will be used for signature generation
 * @author ayetgin
 */
public class AlgorithmsConfig extends BaseConfigElement
{
    private final String XML_WRONG_ELEMENT = "Cannot create a {0} from a {1} element. Read value: {2}";

    private DigestAlg digestAlg = DigestAlg.SHA256;
    private SignatureAlg signatureAlg = SignatureAlg.RSA_SHA256;    
    private DigestAlg digestAlgForOCSP; 
    
    public AlgorithmsConfig()
    {
    }

    /**
     * Constructor for read config from XML element
     * @throws ConfigurationException
     */
    public AlgorithmsConfig(Element element)
            throws ConfigurationException
    {
        super(element);
        String digestAlgStr = getChildText(NS_MA3, TAG_DIGESTALG);
        try {
            digestAlg = DigestAlg.fromName(digestAlgStr);
        } catch (Exception x){
            throw new ConfigurationException(MessageFormat.format(XML_WRONG_ELEMENT, "DigestAlg", ConfigConstants.TAG_DIGESTALG, digestAlgStr), x);
        }
        String signatureAlgStr = getChildText(NS_MA3, TAG_SIGNATUREALG);
        try {
            signatureAlg = SignatureAlg.fromName(signatureAlgStr);
        } catch (Exception x){
            throw new ConfigurationException(MessageFormat.format(XML_WRONG_ELEMENT, "signatureAlg", ConfigConstants.TAG_SIGNATUREALG, signatureAlgStr), x);
        }
        
        digestAlgStr = getChildText(NS_MA3, TAG_OCSP_DIGEST_ALG);
        try {
        	digestAlgForOCSP = DigestAlg.fromName(digestAlgStr);
        } catch (Exception x){
            throw new ConfigurationException(MessageFormat.format(XML_WRONG_ELEMENT, "DigestAlg", ConfigConstants.TAG_OCSP_DIGEST_ALG, digestAlgStr), x);
        }
    }

    /**
     * @return digest algorithm that will be used to calculate digest value for
     * data to signed
     */
    public DigestAlg getDigestAlg()
    {
        return digestAlg;
    }

    /**
     * @return algorithm that will be used for signature operation
     */
    public SignatureAlg getSignatureAlg()
    {
        return signatureAlg;
    }

    /**
     * @param digestAlg algorithm that will be used to calculate digest value
     *                   for data to signed
     */
    public void setDigestAlg(DigestAlg digestAlg)
    {
        this.digestAlg = digestAlg;
    }

    /**
     * @param signatureAlg algorithm that will be used for signature operation
     */
    public void setSignatureAlg(SignatureAlg signatureAlg)
    {
        this.signatureAlg = signatureAlg;
    }
    
    /**
     * @return digest algorithm that will be used for OCSP request
     */
    public DigestAlg getDigestAlgForOCSP() {
		return digestAlgForOCSP;
	}
    /**
     * @param digestAlgForOCSP algorithm that will be used for OCSP request
     */
	public void setDigestAlgForOCSP(DigestAlg digestAlgForOCSP) {
		this.digestAlgForOCSP = digestAlgForOCSP;
	}
}
