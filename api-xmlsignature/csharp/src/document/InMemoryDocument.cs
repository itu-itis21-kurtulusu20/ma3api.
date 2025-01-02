using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.document
{

	using DataType = tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


	/// <summary>
	/// Document for in memory byte[]
	/// 
	/// @author ahmety
	/// date: May 14, 2009
	/// </summary>
	public class InMemoryDocument : Document
	{
		private readonly byte[] mBytes;
		private Stream mStream;

		public InMemoryDocument(byte[] aBytes, string aURI, string aMIMEType, string aEncoding) : base(aURI, aMIMEType, aEncoding)
		{
			mBytes = aBytes;
		}

		public override DataType Type
		{
			get
			{
				return DataType.OCTETSTREAM;
			}
		}

		public override byte[] Bytes
		{
			get
			{
				return mBytes;
			}
		}

		public override Stream Stream
		{
			get
			{
				if (mStream == null)
				{
					mStream = new MemoryStream(mBytes);
				}
				return mStream;
			}
		}

	}

}