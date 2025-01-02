using System;
using System.IO;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver
{

	using Logger = log4net.ILog;
	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
	using DOMDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;


	/// <summary>
	/// <p>'#xpointer(/)' MUST be interpreted to identify the root node [XPath] of
	/// the document that contains the URI attribute.
	/// 
	/// <p>'#xpointer(id('ID'))' MUST be interpreted to identify the element node
	/// identified by '#element(ID)' [XPointer-Element] when evaluated with respect
	/// to the document that contains the URI attribute.
	/// 
	/// <p>To retain comments while selecting an element by an identifier ID, use
	/// the following scheme-based XPointer: URI='#xpointer(id('ID'))'. To retain
	/// comments while selecting the entire document, use the following scheme-based
	/// XPointer: URI='#xpointer(/)'
	/// 
	/// @author ahmety
	/// date: May 18, 2009
	/// </summary>
	public class XPointerResolver : IResolver
	{

		private static Logger msLogcu = log4net.LogManager.GetLogger(typeof(XPointerResolver));

		public virtual bool isResolvable(string aURI, Context aBaglam)
		{
			if (aURI == null || aURI.Equals(""))
			{
				return false;
			}
			if (isXPointerSlash(aURI) || isXPointerId(aURI))
			{
				return true;
			}
			return false;
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document resolve(String aUri, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws java.io.IOException
		public virtual Document resolve(string aUri, Context aContext)
		{
			string uriStr = aUri;
			if (isXPointerSlash(uriStr))
			{
				return new DOMDocument(aContext.Document, aContext.BaseURIStr, true);
			}
			else if (isXPointerId(uriStr))
			{
				string id = getXPointerId(uriStr);
				Element element = aContext.IdRegistry.get(id);


				if (element == null)
				{
					throw new IOException(I18n.translate("resolver.cantResolveUri",aUri));
				}

				string baseURI = aContext.BaseURIStr;
				string uri;
				if (baseURI != null && baseURI.Length > 0)
				{
					uri = baseURI + aUri;
				}
				else
				{
					uri = aUri;
				}

				return new DOMDocument(element, uri, true);
			}
			throw new IOException(I18n.translate("resolver.xpointerSupportLimited", aUri));
		}

		/// <param name="uri"> check if isXPointerSlash </param>
		/// <returns> true if begins with xpointer </returns>
		private static bool isXPointerSlash(string uri)
		{

			if (uri.Equals("#xpointer(/)"))
			{
				return true;
			}

			return false;
		}


		private const string XP = "#xpointer(id(";
		private static readonly int XP_LENGTH = XP.Length;

		/// <param name="uri"> check value </param>
		/// <returns> it it has an xpointer id </returns>
		private static bool isXPointerId(string uri)
		{


			if (uri.StartsWith(XP) && uri.EndsWith("))"))
			{
				string idPlusDelim = uri.Substring(XP_LENGTH, uri.Length - 2 - XP_LENGTH);

				int idLen = idPlusDelim.Length - 1;
				if (((idPlusDelim[0] == '"') && (idPlusDelim[idLen] == '"')) || ((idPlusDelim[0] == '\'') && (idPlusDelim[idLen] == '\'')))
				{
					if (msLogcu.IsDebugEnabled)
					{
						msLogcu.Debug("Id=" + idPlusDelim.Substring(1, idLen - 1));
					}

					return true;
				}
			}

			return false;
		}

		/// <param name="uri"> to be checked </param>
		/// <returns> xpointerId to search. </returns>
		private static string getXPointerId(string uri)
		{
			if (uri.StartsWith(XP) && uri.EndsWith("))"))
			{
				string idPlusDelim = uri.Substring(XP_LENGTH, uri.Length - 2 - XP_LENGTH);
				int idLen = idPlusDelim.Length - 1;
				if (((idPlusDelim[0] == '"') && (idPlusDelim[idLen] == '"')) || ((idPlusDelim[0] == '\'') && (idPlusDelim[idLen] == '\'')))
				{
					return idPlusDelim.Substring(1, idLen - 1);
				}
			}

			return null;
		}

		//static void Main(string[] args)
		//{
		//	Console.WriteLine(isXPointerId("#xpointer(id('object-3'))"));
		//}

	}

}