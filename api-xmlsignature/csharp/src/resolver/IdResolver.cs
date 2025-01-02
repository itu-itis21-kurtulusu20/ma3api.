using System;
using System.IO;
using System.Security.Cryptography.Xml;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
	using DOMDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;


	using Logger = log4net.ILog;
	using Element = XmlElement;

	/// <summary>
	/// @author ahmety
	/// date: May 14, 2009
	/// </summary>
	public class IdResolver : IResolver
	{
		private static Logger logger = log4net.LogManager.GetLogger(typeof(IdResolver));

		public virtual bool isResolvable(string aURI, Context aContext)
		{
			if (aURI == null || aURI.Length == 0)
			{
				return false;
			}

			return (aURI.StartsWith("#")) && (!aURI.StartsWith("#xpointer(") && aContext.IdRegistry.get(aURI.Substring(1)) != null);
		}

		public virtual Document resolve(string aURI, Context aContext)
		{
			try
			{
				Element element = aContext.IdRegistry.get(aURI.Substring(1));

                /*int debugInt = 1;
			    if (debugInt == 0)
			    {
                    context_debug = aContext;
			    }*/

				if (element == null)
				{
					throw new IOException(I18n.translate("resolver.cantResolveUri", aURI));
				}

				return new DOMDocument(element, aURI);
			}
			catch (Exception x)
			{
				logger.Error(I18n.translate("resolver.cantResolveUri", aURI), x);
				throw new IOException(I18n.translate("resolver.cantResolveUri", aURI));
			}
		}

	    /*
	    public static Context context_debug;
	    public static int Debug()
	    {
	        string URI = "#Signed-Properties-Id-9cb197ed-ddbb-4a7c-8add-ac325e762437";

            Element element = context_debug.IdRegistry.get(URI.Substring(1));

            DOMDocument document = new DOMDocument(element, URI);

            XmlDsigC14NTransform transform = new XmlDsigC14NTransform();
            transform.LoadInput(document.NodeList);

            MemoryStream outputStream = (MemoryStream)transform.GetOutput(typeof(Stream));

            byte[] retData = outputStream.ToArray();

	        return retData.Length;
	    }*/

	}

}