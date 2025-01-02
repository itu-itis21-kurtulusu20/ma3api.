using System;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.signature.config
{

    //using Element = XmlElement;
	//using ConfigConstants = //tr.gov.tubitak.uekae.esya.api.signature.config;
	//using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

	/// <summary>
	/// @author ahmety
	/// date: Dec 4, 2009
	/// </summary>
	public class BaseConfigElement
	{
	    public static readonly int MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS = 86340;
	    public static readonly int MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS = 0;

        protected internal XmlElement mElement;

		public BaseConfigElement()
		{
		}

		public BaseConfigElement(XmlElement aElement)
		{
			mElement = aElement;
		}

		protected internal virtual string getChildText(string aNamespace, string aTagname)
		{
			XmlElement e = selectChildElement(aNamespace, aTagname);
			if (e != null)
			{
				return XmlUtil.getText(e);
			}
			return null;
		}

		protected internal virtual XmlElement selectChildElement(string aNamespace, string aTagname)
		{
			return XmlUtil.selectFirstElement(mElement.FirstChild, aNamespace, aTagname);
		}

		protected internal virtual string getParamString(string aParamName)
		{
			XmlElement paramElm = XmlUtil.selectFirstElement(mElement.FirstChild, ConfigConstants.NS_MA3, aParamName);
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
			string str = getChildText(ConfigConstants.NS_MA3, paramName);
			if (str != null && str.Length > 0)
			{
				return Convert.ToInt32(str.Trim());
			}
			return null;
		}

		protected internal virtual bool? getChildBoolean(string paramName)
		{
			string str = getChildText(ConfigConstants.NS_MA3, paramName);
			if (str != null && str.Length > 0)
			{
				return Convert.ToBoolean(str.Trim());
			}
			return null;
		}

		public virtual XmlElement Element
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

	}

}