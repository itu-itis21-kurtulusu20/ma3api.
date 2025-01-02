using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver
{

	using FileDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
	using Document = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


	/// <summary>
	/// @author ahmety
	/// date: Jul 1, 2009
	/// </summary>
	public class AnonymousResolver : IResolver
	{
		internal FileDocument mFileDocument;

		public AnonymousResolver(FileInfo aFile, string aMIMEType)
		{
			mFileDocument = new FileDocument(aFile, aMIMEType);
		}

		public virtual bool isResolvable(string aURI, Context aBaglam)
		{
			return aURI == null;
		}

		public virtual Document resolve(string uri, Context aBaglam)
		{
			return mFileDocument;
		}
	}

}