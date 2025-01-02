package tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.xpath;

import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathFunction;
import javax.xml.namespace.QName;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>XPath functions are resolved by name and arity.
 * The resolver is not needed for XPath built-in functions and the resolver
 * <strong><em>cannot</em></strong> be used to override those functions.</p>
 *
 * @author ahmety
 * date: Aug 6, 2009
 */
public class XPathFunctionResolverImpl implements XPathFunctionResolver
{
    private static final Logger logger = LoggerFactory.getLogger(XPathFunctionResolverImpl.class);

    XPathFunction mFuncHere;

    public XPathFunctionResolverImpl(XPathFunction aFuncHere)
    {
        mFuncHere = aFuncHere;
    }

    public XPathFunction resolveFunction(QName aQName, int i)
    {
        if (aQName.getNamespaceURI().equals(Constants.NS_MA3)
                        && aQName.getLocalPart().equals("here"))
        {
            return mFuncHere;
        }
        else {
            logger.warn("Unknown function requested: "+aQName);
        }
        return null;
    }
}
