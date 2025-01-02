package tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ResolversConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ahmety
 * date: May 15, 2009
 */
public class Resolver
{

    private static List<IResolver> msResolvers = Collections.synchronizedList(new ArrayList<IResolver>());
    private static Boolean msInited  = Boolean.FALSE;

    private static Logger logger = LoggerFactory.getLogger(Resolver.class);

    public static Document resolve(Reference aReference, Context aContext)
        throws XMLSignatureException
    {
        return resolve(aReference.getURI(), aContext);
    }

    private static synchronized void init(Config aConfig){
        ResolversConfig rc = aConfig.getResolversConfig();
        List<Class<? extends IResolver>> classes = rc.getResolvers();
        for (Class<? extends IResolver> clazz : classes) {
            try {
                IResolver resolver = clazz.newInstance();
                msResolvers.add(resolver);
            }
            catch (Exception e) {
                logger.error("Cant init resolver: " + clazz, e);
            }
        }
        msInited = true;
    }

    public static Document resolve(String aUri, Context aContext)
            throws XMLSignatureException
    {
        logger.debug("Resolver.resolve: '"+aUri+"'");

        synchronized (msInited){
            if (!msInited){
                init(aContext.getConfig());
            }
        }

        synchronized (msResolvers)
        {
            if (aContext.getResolvers()!=null)
                msResolvers.addAll(aContext.getResolvers());

            try {
                for (int i = 0; i < msResolvers.size(); i++)
                {
                    IResolver resolver = msResolvers.get(i);
                    if (resolver.isResolvable(aUri, aContext))
                    {
                        logger.debug("Resolver.resolver: '"+resolver+"'");

                        try {
                            return resolver.resolve(aUri, aContext);
                        }
                        catch (Exception e){
                            logger.error("Resolver "+resolver.getClass()+" return error while resolving: '"+aUri+"' : "+e.getMessage(), e);
                        }
                    }
                }
            }
            finally {
                if (aContext.getResolvers()!=null)
                    msResolvers.removeAll(aContext.getResolvers());
            }
        }
        logger.info("Cant resolve reference!");
        throw new XMLSignatureException("resolver.cantFindResolverForUri", aUri);
    }

}
