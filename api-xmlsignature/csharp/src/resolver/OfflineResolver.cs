using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using StreamingDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.StreamingDocument;


	using Logger = log4net.ILog;

	/// <summary>
	/// @author ahmety
	/// date: Jul 2, 2009
	/// </summary>
	public class OfflineResolver : IResolver
	{
		private static Logger logger = log4net.LogManager.GetLogger(typeof(OfflineResolver));

		private readonly IDictionary<string, Entry> mUrlToEntry = new Dictionary<string, Entry>(1);

		public OfflineResolver()
		{
		}

		public OfflineResolver(string aURL, string aFileUrl, string aMIMEType)
		{
			register(aURL, aFileUrl, aMIMEType);
		}

		public virtual bool isResolvable(string aURI, Context aBaglam)
		{
			bool resolvable = mUrlToEntry.ContainsKey(aURI);
			if (logger.IsDebugEnabled)
			{
				logger.Debug("I " + (resolvable? "can" : "cant") + " resolve: " + aURI);
			}
			return resolvable;
		}

		public virtual void register(string aURL, string aFileUrl, string aMIMEType)
		{
			mUrlToEntry[aURL] = new Entry(this, aURL, aFileUrl, aMIMEType);
		}

		public virtual Document resolve(string aURI, Context aBaglam)
		{
			Entry e = mUrlToEntry[aURI];

			if (e == null)
			{
				throw new IOException("Unknown offline entry: " + aURI);
			}

            FileStream fis = File.OpenRead(e.mFileUrl);
			string mime = e.mMIMEType;
			if (e.mMIMEType == null)
			{
                mime = EMimeUtil.GetMimeType(aURI);
				if (mime == null)
				{
                    mime = EMimeUtil.GetMimeFromFile(fis,e.mFileUrl);
				}
			}
			return new StreamingDocument(fis, e.mURL, mime, null);
		}

		internal class Entry
		{
			private readonly OfflineResolver outerInstance;

			internal string mURL;
			internal string mMIMEType;
			internal string mFileUrl;

			internal Entry(OfflineResolver outerInstance, string aURL, string aFileUrl, string aMIMEType)
			{
					this.outerInstance = outerInstance;
				mURL = aURL;
				mFileUrl = aFileUrl;
				mMIMEType = aMIMEType;
			}
		}
	}

}