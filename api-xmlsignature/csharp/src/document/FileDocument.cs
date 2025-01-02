using System;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.document
{

	using DataType = tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


	/// <summary>
	/// Document for file.
	/// 
	/// @author ahmety
	/// date: May 14, 2009
	/// </summary>
	public class FileDocument : Document
	{
	    private FileStream fileStream=null;
        private readonly FileInfo mFile;

        public FileDocument(string filePath, string aMIMEType, string aEncoding)
            : this(new FileInfo(filePath), aMIMEType,aEncoding)
        {
        }

        public FileDocument(string filePath)
            : this(new FileInfo(filePath))
        {
        }

        public FileDocument(string filePath, string aMIMEType)
            : this(new FileInfo(filePath), aMIMEType)
        {
        }

        public FileDocument(FileInfo aFile)
            : this(aFile, EMimeUtil.GetMimeType(aFile.Name))
		{
		}

		public FileDocument(FileInfo aFile, string aMIMEType) : this(aFile, aMIMEType, null)
		{
		}

		public FileDocument(FileInfo aFile, string aMIMEType, string aEncoding) : base(aFile.FullName, aMIMEType, aEncoding)
		{

            if (!aFile.Exists)
			{
				throw new XMLSignatureException("errors.cantFind", aFile.FullName);
			}
			mFile = aFile;
		}

		public override string URI
		{
			get
			{
				return mFile.FullName;
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
				try
				{
                    fileStream = File.Open(mFile.FullName, FileMode.Open, FileAccess.Read);
                    return fileStream;				    
				}
				catch (Exception x)
				{
					throw new ESYAException("I/O error on FileDocument.getStream", x);
				}
			}
		}

	}

}