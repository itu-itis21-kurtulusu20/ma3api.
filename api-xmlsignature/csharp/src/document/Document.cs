using System;
using System.IO;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.document
{


	using NodeList = XmlNodeList;
	using C14nMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
	using DataType = tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
	using UnsupportedOperationException = tr.gov.tubitak.uekae.esya.api.xmlsignature.UnsupportedOperationException;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;


	/// <summary>
	/// Base class for different type of resolved data.
	/// 
	/// @author ahmety
	/// date: May 6, 2009
	/// </summary>
	public abstract class Document
	{
		protected internal string mURI;
		protected internal string mMIMEType;
		protected internal string mEncoding;

		// used for getBytes 
		protected internal byte[] mBytesCached;


		protected internal Document(string aURI, string aMIMEType, string aEncoding)
		{
			mURI = aURI;
			mMIMEType = aMIMEType;
			mEncoding = aEncoding;
		}

		public virtual string URI
		{
			get
			{
				return mURI;
			}
		}

		public virtual string MIMEType
		{
			get
			{
				return mMIMEType;
			}
		}

		public virtual string Encoding
		{
			get
			{
				return mEncoding;
			}
		}

		public abstract DataType Type {get;}

		/// <summary>
		/// Get document content in most suitable form
		/// </summary>
		/// <returns> document content as InputStream or <code><seealso cref="NodeList"/></code>
		///      according to <code><seealso cref="#getType"/></code> </returns>
		/// <exception cref="XMLSignatureException"> if IOException occurs </exception>
		public virtual object Data
		{
			get
			{
				try
				{
					switch (Type)
					{
						case DataType.OCTETSTREAM:
							return Stream;
                        case DataType.NODESET:
							return NodeList;
					}
				}
				catch (UnsupportedOperationException exc)
				{
					// we shouldnt be here!
					throw new ESYAException(this.GetType() + "'es " + Type + " data returning method should have been supported.",exc);
				}
				// shouldnt be here, no i18n
				throw new ESYAException("Unknown data type.");
			}
		}

		/// <summary>
		/// Get document content as bytes, which requires datatype conversion from
		///      InputStream or NodeList.
		/// </summary>
		/// <returns> document content as byte[] </returns>
		/// <exception cref="XMLSignatureException"> if any errors occur while datatype
		///      conversion </exception>
		public virtual byte[] Bytes
		{
			get
			{
			    if (mBytesCached != null) return mBytesCached;
			    switch (Type)
			    {
			        case DataType.NODESET:
			            mBytesCached = XmlUtil.outputDOM(NodeList, C14nMethod.INCLUSIVE);
			            break;
			        case DataType.OCTETSTREAM:
			            Stream stream = Stream;
			            mBytesCached = toByteArray(stream);
			            stream.Close();
			            break;
			    }
			    // dummy return, we shouldnt be here!
				return mBytesCached;
			}
		}

		/// <summary>
		/// data return method for OCTETSTREAM data type documents
		/// </summary>
		/// <returns> data as Stream </returns>
		/// <exception cref="UnsupportedOperationException"> if implementing class is a NodeSet
		///      type document </exception>
		public virtual Stream Stream
		{
			get
			{
				throw new UnsupportedOperationException("unsupported.operation", this.GetType().Name + ".getStream()");
			}
		}

		/// <summary>
		/// data return method for NODESET data type documents
		/// </summary>
		/// <returns> data as NodeList </returns>
		/// <exception cref="UnsupportedOperationException"> if implementing class is a
		///      OctetStream type document </exception>
		public virtual NodeList NodeList
		{
			get
			{
				throw new UnsupportedOperationException("unsupported.operation", this.GetType().Name + "NodeList");
			}
		}


        public virtual object C14NObject
        {
            get
            {
                throw new UnsupportedOperationException("unsupported.operation", this.GetType().Name + "C14NObject");
            }
        }



        /// <summary>
        /// Convert stream to byte[]
        /// </summary>
        /// <param name="aStream"> to be converted </param>
        /// <returns> Stream in byte[] format </returns>
        public virtual byte[] toByteArray(Stream aStream)
		{
			try
			{
                return StreamUtil.ReadToEnd(aStream);
			}
			catch (IOException x)
			{
				Console.WriteLine("Stream->byte donusumunde hata");
				Console.WriteLine(x.ToString());
				Console.Write(x.StackTrace);
			    return null;
			}
		}

	}

}