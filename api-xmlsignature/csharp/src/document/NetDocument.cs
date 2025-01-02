using System;
using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.document
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using DataType = tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
	using UnsupportedOperationException = tr.gov.tubitak.uekae.esya.api.xmlsignature.UnsupportedOperationException;
	using HttpResolver = tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.HttpResolver;


	/// <summary>
	/// Document for data over network.
	/// 
	/// @author ahmety
	/// date: May 14, 2009
	/// </summary>
	public class NetDocument : Document
	{
		private static Context EMPTY_CONTEXT = new Context();
		private static HttpResolver msResolver = new HttpResolver();

		private readonly StreamingDocument mInternalDocument;


		public NetDocument(string aURL, string aMimeType, string aEncoding) : base(aURL, aMimeType, aEncoding)
		{
			try
			{
				mInternalDocument = (StreamingDocument) msResolver.resolve(aURL, EMPTY_CONTEXT);
			}
			catch (IOException x)
			{
				// shouldnt happen
				Console.WriteLine(x.ToString());
				Console.Write(x.StackTrace);
			}
		}

		public override DataType Type
		{
			get
			{
				return DataType.OCTETSTREAM;
			}
		}

    	public override Stream Stream
		{
			get
			{
				return mInternalDocument.Stream;
			}
		}

	}

}