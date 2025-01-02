using System.IO;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
	using DOMDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;


	/// <summary>
	/// Resolve resource over node hierarchy by Id attribute.
	/// 
	/// @author ahmety
	/// date: Jun 17, 2009
	/// </summary>
	public class DOMResolver : IResolver
	{

		public virtual bool isResolvable(string uri, Context aBaglam)
		{
			if (uri == null)
			{
				return false;
			}

			// if (empty uri) or (fragment) and (not xpointer)
			if (uri.Equals("") || ((uri[0] == '#') && !((uri[1] == 'x') && uri.StartsWith("#xpointer("))))
			{
				return true;
			}
			return false;

		}

		public virtual Document resolve(string aURI, Context aBaglam)
		{
			if (aURI.Equals(""))
			{
				return new DOMDocument(aBaglam.Document, aBaglam.BaseURIStr);
			}
			else
			{
				string seek = aURI.Substring(1);
				Element found = aBaglam.Document.GetElementById(seek);
				if (found == null)
				{
					found = XmlCommonUtil.findByIdAttr(aBaglam.Document.DocumentElement, seek);
				}
				if (found == null)
				{
					throw new IOException(I18n.translate("resolver.cantResolveUri", aURI));
				}

				return new DOMDocument(found, aBaglam.BaseURIStr);
			}
		}

	}

}