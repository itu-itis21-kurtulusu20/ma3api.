package tr.gov.tubitak.uekae.esya.api.signature.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertValidationPolicies;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Map;

import static tr.gov.tubitak.uekae.esya.api.signature.config.ConfigConstants.*;

/**
 * ESYA Signature API configuration
 * @author ayetgin
 */
public class Config extends BaseConfigElement
{
    private static Logger logger = LoggerFactory.getLogger(Config.class);

    private CertValidationPolicies certificateValidationPolicies;

    private HttpConfig httpConfig;

    private TimestampConfig timestampConfig;
    private AlgorithmsConfig algorithmsConfig;

    private CertificateValidationConfig certificateValidationConfig;

    private Parameters parameters;

    private String configFilePath;

    public Config() {
        configFilePath = CONFIG_FILE_NAME;

        try {
            File f = new File(configFilePath);
            if (!(f.exists() && f.isFile())) {
                configFilePath = ClassLoader.getSystemResource(configFilePath).getPath();
                configFilePath = URLDecoder.decode(configFilePath, "UTF-8");
            }
        }
        catch (Exception ex) {
            try {
                configFilePath = Config.class.getClassLoader().getResource(configFilePath).getPath();
                configFilePath = URLDecoder.decode(configFilePath, "UTF-8");
            }
            catch (Exception ignored) {
                return;
            }

        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(configFilePath);
            init(doc);
        }
        catch (Exception x) {
            logger.error("Cant load config...", x);
            throw new SignatureRuntimeException("config.cantLoad", x);
        }
    }

    public Config(String configPath) {

        try {
            configFilePath = configPath;
            loadConfig(new FileInputStream(configPath));
        } catch (Exception e) {
            logger.error("Cannot open config file: " + configPath, e);
        }
    }

    public Config(InputStream configStream) throws ConfigurationException {
        loadConfig(configStream);
    }

    protected Config(Element aElement) throws ConfigurationException
    {
        super(aElement);
    }


    public void loadConfig(InputStream configStream)
    {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(configStream);
            init(doc);
        }
        catch (Exception x) {
            logger.error("Cannot load config...", x);
            throw new SignatureRuntimeException("config.cantLoad", x);
        }
    }

    private void init(Document aDocument) throws ConfigurationException
    {
        setElement(aDocument.getDocumentElement());

        //Element resolverConfElement = selectChildElement(NS_MA3, TAG_RESOLVERS);
        // todo resolversConfig = new ResolversConfig(resolverConfElement);

        Element timestampConfElement = selectChildElement(NS_MA3, TAG_TIMESTAMPSERVER);
        timestampConfig = new TimestampConfig(timestampConfElement);

        Element algoConfElement = selectChildElement(NS_MA3, TAG_ALGORITHMS);
        algorithmsConfig = new AlgorithmsConfig(algoConfElement);

        Element httpConfElement = selectChildElement(NS_MA3, TAG_HTTP);
        httpConfig = new HttpConfig(httpConfElement);

        Element paramsConfElement = selectChildElement(NS_MA3, TAG_PARAMS);
        parameters = new Parameters(paramsConfElement);

        Element certValConfElement = selectChildElement(NS_MA3, TAG_CERTVALIDATION);
        if(!StringUtil.isNullorEmpty(configFilePath))
        {
            String configFolderPath = new File(configFilePath).getParent();
            certificateValidationConfig = new CertificateValidationConfig(certValConfElement, configFolderPath);
        }
        else
        {
            certificateValidationConfig = new CertificateValidationConfig(certValConfElement);
        }


    }

    private void loadConfig(){
        try {
            Map<String, String> policyFiles = certificateValidationConfig.getCertificateValidationPolicyFile();
            certificateValidationPolicies = new CertValidationPolicies();
            for (String certType : policyFiles.keySet()){
                ValidationPolicy policy = PolicyReader.readValidationPolicyFromURL(policyFiles.get(certType));
                certificateValidationPolicies.register(certType, policy);
            }
        } catch (Exception x){
            throw new SignatureRuntimeException("Cant read certificate validation policy", x);
        }
    }


    public CertValidationPolicies getCertificateValidationPolicies()
    {
        if (certificateValidationPolicies==null){
            loadConfig();
        }
        return certificateValidationPolicies;
    }

    public void setCertificateValidationPolicies(CertValidationPolicies aCertificateValidationPolicy)
    {
        certificateValidationPolicies = aCertificateValidationPolicy;
    }

    public HttpConfig getHttpConfig()
    {
        return httpConfig;
    }

    public void setHttpConfig(HttpConfig aHttpConfig)
    {
        httpConfig = aHttpConfig;
    }

    public CertificateValidationConfig getCertificateValidationConfig()
    {
        return certificateValidationConfig;
    }

    public void setCertificateValidationConfig(CertificateValidationConfig aCertificateValidationConfig)
    {
        certificateValidationConfig = aCertificateValidationConfig;
    }

    public Parameters getParameters()
    {
        return parameters;
    }

    public void setParameters(Parameters aParameters)
    {
        parameters = aParameters;
    }

    public TimestampConfig getTimestampConfig()
    {
        return timestampConfig;
    }

    public void setTimestampConfig(TimestampConfig aTimestampConfig)
    {
        timestampConfig = aTimestampConfig;
    }

    public AlgorithmsConfig getAlgorithmsConfig()
    {
        return algorithmsConfig;
    }

    public void setAlgorithmsConfig(AlgorithmsConfig aAlgorithmsConfig)
    {
        algorithmsConfig = aAlgorithmsConfig;
    }
}
