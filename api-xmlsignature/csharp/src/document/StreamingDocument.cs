using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.document
{

	using DataType = tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;


	/// <summary>
	/// Document for input streams.
	/// 
	/// @author ahmety
	/// date: May 14, 2009
	/// </summary>
	public class StreamingDocument : Document
	{
        internal Stream mStream;
		internal byte[] mBytes;

        public StreamingDocument(Stream aStream, string aURI, string aMIMEType, string aEncoding)
            : base(aURI, aMIMEType, aEncoding)
		{
			mStream = aStream;
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
				return mStream;
			}
		}

	}

}