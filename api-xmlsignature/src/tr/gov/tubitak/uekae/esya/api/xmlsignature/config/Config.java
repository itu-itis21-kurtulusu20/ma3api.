package tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.signature.config.ConfigurationException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml.NamespacePrefixMap;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.CONFIG_FILE_NAME;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.TAG_ALGORITHMS;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.TAG_HTTP;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.TAG_PARAMETERS;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.TAG_RESOLVERS;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.TAG_TIMESTAMPSERVER;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.TAG_VALIDATION;

/**
 * @author ahmety
 * date: Dec 4, 2009
 */
public class Config extends BaseConfigElement
{
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private HttpConfig mHttpConfig;
    private ResolversConfig mResolversConfig;
    private ValidationConfig mValidationConfig;
    private TimestampConfig mTimestampConfig;
    private AlgorithmsConfig mAlgorithmsConfig;
    private Parameters mParameters;

    private NamespacePrefixMap nsPrefixMap = new NamespacePrefixMap();

    private String mConfigFilePath;


    public Config()
    {
        loadConfig((String)null);
    }

    public Config(String aPath)
    {
        mConfigFilePath = aPath;
        loadConfig(aPath);
    }

    public Config(InputStream configStream)
    {
        loadConfig(configStream);
    }

    protected Config(Document configDocument)
    {
        loadConfig(configDocument);
    }

    // no init constructor for not reading config file
    private Config(Object dummy){}

    private void init(Document aDocument) throws ConfigurationException
    {
        setElement(aDocument.getDocumentElement());

        Element resolverConfElement = selectChildElement(Constants.NS_MA3, TAG_RESOLVERS);
        mResolversConfig = new ResolversConfig(resolverConfElement);

        Element timestampConfElement = selectChildElement(Constants.NS_MA3, TAG_TIMESTAMPSERVER);
        mTimestampConfig = new TimestampConfig(timestampConfElement);

        Element algoConfElement = selectChildElement(Constants.NS_MA3, TAG_ALGORITHMS);
        mAlgorithmsConfig = new AlgorithmsConfig(algoConfElement);

        Element httpConfElement = selectChildElement(Constants.NS_MA3, TAG_HTTP);
        mHttpConfig = new HttpConfig(httpConfElement);

        Element paramsConfElement = selectChildElement(Constants.NS_MA3, TAG_PARAMETERS);
        mParameters = new Parameters(paramsConfElement);

        Element validationConfElement = selectChildElement(Constants.NS_MA3, TAG_VALIDATION);

        if(!StringUtil.isNullorEmpty(mConfigFilePath))
        {
            String configFolderPath = new File(mConfigFilePath).getParent();
            mValidationConfig = new ValidationConfig(validationConfElement, configFolderPath);
        }
        else
        {
            mValidationConfig = new ValidationConfig(validationConfElement);
        }
    }

    public HttpConfig getHttpConfig()
    {
        return mHttpConfig;
    }

    public ResolversConfig getResolversConfig()
    {
        return mResolversConfig;
    }

    public ValidationConfig getValidationConfig()
    {
        return mValidationConfig;
    }

    public TimestampConfig getTimestampConfig()
    {
        return mTimestampConfig;
    }

    public void setTimestampConfig(TimestampConfig aTimestampConfig)
    {
        mTimestampConfig = aTimestampConfig;
    }

    public AlgorithmsConfig getAlgorithmsConfig()
    {
        return mAlgorithmsConfig;
    }

    public Parameters getParameters()
    {
        return mParameters;
    }

    public NamespacePrefixMap getNsPrefixMap() {
		return nsPrefixMap;
	}

	public void setNsPrefixMap(NamespacePrefixMap nsPrefixMap) {
		this.nsPrefixMap = nsPrefixMap;
	}

    public void setHttpConfig(HttpConfig aHttpConfig)
    {
        mHttpConfig = aHttpConfig;
    }

    public void setResolversConfig(ResolversConfig aResolversConfig)
    {
        mResolversConfig = aResolversConfig;
    }

    public void setValidationConfig(ValidationConfig aValidationConfig)
    {
        mValidationConfig = aValidationConfig;
    }

    public void setAlgorithmsConfig(AlgorithmsConfig aAlgorithmsConfig)
    {
        mAlgorithmsConfig = aAlgorithmsConfig;
    }

    public void setParameters(Parameters aParameters)
    {
        mParameters = aParameters;
    }

    private void loadConfig(InputStream configStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(configStream);
            init(doc);
        }
        catch (Exception x) {
            logger.error("Cannot load config...", x);
            throw new XMLSignatureRuntimeException(x, I18n.translate("config.cantLoad", I18n.translate("config")));
        }
    }

    private void loadConfig(Document document) {
        try {
            init(document);
        }
        catch (Exception x) {
            logger.error("Cant load config...", x);
            throw new XMLSignatureRuntimeException(x, I18n.translate("config.cantLoad", I18n.translate("config")));
        }
    }

    private void loadConfig(String aConfigFilePath) throws ESYARuntimeException
    {
        logger.info("Load config from : "+aConfigFilePath);

        if (aConfigFilePath==null) {
            aConfigFilePath= CONFIG_FILE_NAME;
            logger.info("Config was null, new path: " + aConfigFilePath);
        }

        try {
            File f = new File(aConfigFilePath);
            logger.info("Try working directory: " + f.getAbsolutePath());
            if (!(f.exists() && f.isFile())) {
                aConfigFilePath = ClassLoader.getSystemResource(aConfigFilePath).getPath();
                aConfigFilePath = URLDecoder.decode(aConfigFilePath, "UTF-8");
            }
        }
        catch (Exception e) {
            logger.debug("Debug in Config", e);
            try {
                URL url = Config.class.getClassLoader().getResource(aConfigFilePath);
                if(url != null) {
                    aConfigFilePath = url.getPath();
                    logger.info("Try Config.class directory: " + url.getPath());
                    aConfigFilePath = URLDecoder.decode(aConfigFilePath, "UTF-8");
                }
            }
            catch (Exception x) {
                logger.error("Cant find config...", x);
                throw new XMLSignatureRuntimeException(x, I18n.translate("config.cantFind", I18n.translate("config")));
            }

        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(aConfigFilePath);
            init(doc);
        }
        catch (Exception x) {
            logger.error("Cant load config...", x);
            throw new XMLSignatureRuntimeException(x, I18n.translate("config.cantLoad", I18n.translate("config")));
        }
    }

    public static Config noInit(){
        return new Config(new Object());
    }

    public static void main(String[] args) throws Exception
    {
        Config c = new Config();

        // bu su an working directory icin calisiyor
        //Config c = new Config(new FileInputStream(new File("xmlsignature-config.xml"/*"T:\\IdeaProjects\\xmlsignature-config.xml"*/)));

        /*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse("T:\\IdeaProjects\\xmlsignature-config.xml");
        Config c = new Config(doc);*/

        String port = c.getHttpConfig().getProxyPort();
        System.out.println("port: "+port);

        List validators1 = c.getValidationConfig().getProfile(SignatureType.XAdES_C).getValidators();
        List validators2 = c.getValidationConfig().getProfile(SignatureType.XAdES_BES).getValidators();
        List validators3 = c.getValidationConfig().getProfile(SignatureType.XMLDSig).getValidators();
        List validators4 = c.getValidationConfig().getProfile(SignatureType.XAdES_A).getValidators();
        System.out.println("v1: "+validators1.size());
        System.out.println("v2: "+validators2.size());
        System.out.println("v3: "+validators3.size());
        System.out.println("v4: "+validators4.size());

        System.out.println("write: "+c.getParameters().isWriteReferencedValidationDataToFileOnUpgrade());
    }
}
