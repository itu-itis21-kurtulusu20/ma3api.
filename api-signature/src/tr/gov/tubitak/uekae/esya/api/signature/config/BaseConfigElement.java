package tr.gov.tubitak.uekae.esya.api.signature.config;

/**
 * @author ayetgin
 */

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

/**
 * @author ahmety
 * date: Dec 4, 2009
 */
public class BaseConfigElement
{

    public static final int MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS  = 86340;
    public static final int MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS = 0;

    protected Element mElement;

    public BaseConfigElement()
    {
    }

    public BaseConfigElement(Element aElement)
    {
        mElement = aElement;
    }

    protected String getChildText(String aNamespace, String aTagname)
    {
        Element e = selectChildElement(aNamespace, aTagname);
        if (e != null) {
            return XmlUtil.getText(e);
        }
        return null;
    }

    protected Element selectChildElement(String aNamespace, String aTagname)
    {
        return XmlUtil.selectFirstElement(mElement.getFirstChild(), aNamespace, aTagname);
    }

    protected String getParamString(String aParamName){
        Element paramElm = XmlUtil.selectFirstElement(mElement.getFirstChild(), ConfigConstants.NS_MA3, aParamName);
        return paramElm==null ? null : paramElm.getAttribute("value");
    }

    protected boolean getParamBoolean(String paramName){
        String str = getParamString(paramName);
        if (str!=null && (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes")))
            return true;
        return false;
    }

    protected Integer getChildInteger(String paramName){
        String str = getChildText(ConfigConstants.NS_MA3, paramName);
        if (str!=null && str.length()>0)
            return Integer.valueOf(str.trim());
        return null;
    }

    protected Boolean getChildBoolean(String paramName){
        String str = getChildText(ConfigConstants.NS_MA3, paramName);
        if (str!=null && str.length()>0)
            return Boolean.valueOf(str.trim());
        return null;
    }

    public Element getElement()
    {
        return mElement;
    }

    public void setElement(Element aElement)
    {
        mElement = aElement;
    }
}