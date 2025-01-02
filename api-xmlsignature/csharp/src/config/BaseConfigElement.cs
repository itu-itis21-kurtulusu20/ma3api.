using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{

    using Element = XmlElement;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

	/// <summary>
	/// @author ahmety
	/// date: Dec 4, 2009
	/// </summary>
	public class BaseConfigElement : tr.gov.tubitak.uekae.esya.api.signature.config.BaseConfigElement
	{
        public BaseConfigElement()
        {
            
        }

        public BaseConfigElement(Element aElement) : base(aElement)
        {
            
        }

        /*
        protected internal Element mElement;

		public BaseConfigElement()
		{
		}

		public BaseConfigElement(Element aElement)
		{
			mElement = aElement;
		}

		protected internal virtual string getChildText(string aNamespace, string aTagname)
		{
			Element e = selectChildElement(aNamespace, aTagname);
			if (e != null)
			{
				return XmlUtil.getText(e);
			}
			return null;
		}

		protected internal virtual Element selectChildElement(string aNamespace, string aTagname)
		{
			return XmlUtil.selectFirstElement(mElement.FirstChild, aNamespace, aTagname);
		}

		protected internal virtual string getParamString(string aParamName)
		{
			Element paramElm = XmlUtil.selectFirstElement(mElement.FirstChild, Constants.NS_MA3, aParamName);
			return paramElm == null ? null : paramElm.GetAttribute("value");
		}

		protected internal virtual bool getParamBoolean(string paramName)
		{
			string str = getParamString(paramName);
			if (str != null && (str.ToUpper() == "true".ToUpper() || str.ToUpper() == "yes".ToUpper()))
			{
				return true;
			}
			return false;
		}

		protected internal virtual int? getChildInteger(string paramName)
		{
			string str = getChildText(Constants.NS_MA3, paramName);
			if (str != null && str.Length > 0)
			{
				return Convert.ToInt32(str.Trim());
			}
			return null;
		}

		protected internal virtual bool? getChildBoolean(string paramName)
		{
			string str = getChildText(Constants.NS_MA3, paramName);
			if (str != null && str.Length > 0)
			{
				return Convert.ToBoolean(str.Trim());
			}
			return null;
		}

		public virtual Element Element
		{
			get
			{
				return mElement;
			}
			set
			{
				mElement = value;
			}
		}
        */

	}

}