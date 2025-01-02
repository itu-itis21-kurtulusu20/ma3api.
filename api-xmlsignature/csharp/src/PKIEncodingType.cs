using System;
using System.Collections.Generic;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{
	//using com.objsys.asn1j.runtime;
	//using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.I18n;


	/// <summary>
	/// <p>Denotes URI identifying the encoding used in the original PKI data.
	/// 
	/// <p>If the Encoding attribute is not present, then it is assumed that the PKI
	/// data is ASN.1 data encoded in DER.
	/// 
	/// <p>So far, the following URIs have been identified:
	/// <ul>
	/// <li>http://uri.etsi.org/01903/v1.2.2#DER for denoting that the original PKI
	/// data were ASN.1 data encoded in DER.
	/// <li>http://uri.etsi.org/01903/v1.2.2#BER for denoting that the original PKI
	/// data were ASN.1 data encoded in BER.
	/// <li>http://uri.etsi.org/01903/v1.2.2#CER for denoting that the original PKI
	/// data were ASN.1 data encoded in CER.
	/// <li>http://uri.etsi.org/01903/v1.2.2#PER for denoting that the original PKI
	/// data were ASN.1 data encoded in PER.
	/// <li>http://uri.etsi.org/01903/v1.2.2#XER for denoting that the original PKI
	/// data were ASN.1 data encoded in XER.
	/// </ul>
	/// </summary>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData
	/// @author ahmety
	/// date: Sep 14, 2009 </seealso>
	public class PKIEncodingType
	{
		public static readonly PKIEncodingType DER=new PKIEncodingType("http://uri.etsi.org/01903/v1.2.2#DER", typeof(Asn1DerEncodeBuffer), typeof(Asn1DerDecodeBuffer));
		public static readonly PKIEncodingType BER=new PKIEncodingType("http://uri.etsi.org/01903/v1.2.2#BER", typeof(Asn1BerEncodeBuffer),typeof(Asn1BerDecodeBuffer));
		public static readonly PKIEncodingType NOT_SUPPORTED_CER=new PKIEncodingType("http://uri.etsi.org/01903/v1.2.2#CER", null, null);
		public static readonly PKIEncodingType NOT_SUPPORTED_PER=new PKIEncodingType("http://uri.etsi.org/01903/v1.2.2#PER", null, null);
		public static readonly PKIEncodingType XER=new PKIEncodingType("http://uri.etsi.org/01903/v1.2.2#XER",typeof(Asn1XerEncodeBuffer), null); //Asn1XerDecodeBuffer.class

        public static IEnumerable<PKIEncodingType> Values
        {
            get
            {
                yield return DER;
                yield return BER;
                yield return NOT_SUPPORTED_CER;
                yield return NOT_SUPPORTED_PER;
                yield return XER;
            }
        }

	    public string URI
	    {
	        get { return mURI; }
	        set { mURI = value; }
	    }

	    private String mURI;
		private readonly Type mEncodeBufferClass;
		private readonly Type mDecodeBufferClass;

		PKIEncodingType(String aURI, Type aEncodeBufferClass, Type aDecodeBufferClass)
		{
			URI = aURI;
			mEncodeBufferClass = aEncodeBufferClass;
			mDecodeBufferClass = aDecodeBufferClass;
		}


		public static PKIEncodingType resolve(String aURI)
		{
			if (aURI==null || aURI.Equals("") || aURI.Trim().Length<1)
			{
				return DER;
			}
			foreach (PKIEncodingType encoding in Values)
			{
                if (encoding.URI.Equals(aURI))
					return encoding;
			}
	
			throw new XMLSignatureException("unknown.encoding ", aURI);
		}
        
	    public Asn1EncodeBuffer createEncodeBuffer()
		{
			if (mEncodeBufferClass != null)
			{
				try
				{
                    return (Asn1EncodeBuffer)Activator.CreateInstance(mEncodeBufferClass);
				}
                catch (Exception x)
                {
					throw new XMLSignatureException(x, "core.cantCreateBuffer", I18n.translate("encode"), URI);
				}
			}
			else
			{
                throw new XMLSignatureException("core.cantCreateBuffer", I18n.translate("encode"), URI);
			}
		}

		public Asn1DecodeBuffer createDecodeBuffer(byte[] aBytes)
		{
			if (mDecodeBufferClass != null)
			{
				try
				{
                    Type[] types = new Type[1];
                    types[0] = typeof(byte[]);
				    ConstructorInfo constructorInfo = mDecodeBufferClass.GetConstructor(types);
                    object[] paramValues = new object[1];
				    paramValues[0] = aBytes;
				    return (Asn1DecodeBuffer) constructorInfo.Invoke(paramValues);
				    //return (Asn1DecodeBuffer)Activator.CreateInstance(mDecodeBufferClass);
				}
				catch (Exception x)
				{
					throw new XMLSignatureException(x, "core.cantCreateBuffer", I18n.translate("decode"), URI);
				}
			}
			else
			{
				throw new XMLSignatureException("core.cantCreateBuffer", I18n.translate("decode"), URI);
			}
		}
	}

}