using System;
using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{
	using Element = XmlElement;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    /// <summary>
    /// @author ayetgin
    /// </summary>
    public class ResolversConfig : BaseConfigElement
	{
		private readonly IList<Type> mResolvers = new List<Type>();

        public ResolversConfig(IList<Type> aResolvers)
        {
            mResolvers = aResolvers;
        }

		public ResolversConfig(Element aElement) : base(aElement)
		{
			Element[] resolverElms = XmlCommonUtil.selectNodes(aElement.FirstChild, Constants.NS_MA3, ConfigConstants.TAG_RESOLVER);
			foreach (Element resolverElm in resolverElms)
			{
				try
				{
                    string className = resolverElm.GetAttribute(ConfigConstants.ATTR_CLASS);
					Type resolverClazz = (Type) Type.GetType(className);
					mResolvers.Add(resolverClazz);
				}
				catch (Exception t)
				{
					Console.WriteLine(t.ToString());
					Console.Write(t.StackTrace);
				}

			}
		}

		public virtual IList<Type> Resolvers
		{
			get
			{
				return mResolvers;
			}
		}
	}

}