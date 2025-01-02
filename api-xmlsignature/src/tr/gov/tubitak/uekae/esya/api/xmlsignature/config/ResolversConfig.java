package tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.ATTR_CLASS;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.TAG_RESOLVER;

/**
 * @author ayetgin
 */
public class ResolversConfig extends BaseConfigElement {
    protected static Logger logger = LoggerFactory.getLogger(ResolversConfig.class);

    private List<Class<? extends IResolver>> mResolvers = new ArrayList<Class<? extends IResolver>>();

    public ResolversConfig(List<Class<? extends IResolver>> aResolvers)
    {
        mResolvers = aResolvers;
    }

    public ResolversConfig(Element aElement)
    {
        super(aElement);

        Element[] resolverElms = XmlUtil.selectNodes(aElement.getFirstChild(), Constants.NS_MA3, TAG_RESOLVER);
        for (Element resolverElm : resolverElms) {
            try {
                String className = resolverElm.getAttributeNS(null, ATTR_CLASS);
                Class<? extends IResolver> resolverClazz = (Class<? extends IResolver>) Class.forName(className);
                mResolvers.add(resolverClazz);
            }
            catch (Exception e) {
                logger.error("Error in ResolversConfig", e);
            }

        }
    }

    public List<Class<? extends IResolver>> getResolvers()
    {
        return mResolvers;
    }
}
