using System;
using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

	using DigestAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


	/// <summary>
	/// @author ahmety
	/// date: Apr 22, 2009
	/// </summary>
	public class DigestMethod
	{
		/*
		"http://www.w3.org/2001/04/xmldsig-more#md5"
		"http://www.w3.org/2001/04/xmlenc#ripemd160"
		"http://www.w3.org/2000/09/xmldsig#sha1"
		"http://www.w3.org/2001/04/xmlenc#sha256"
		"http://www.w3.org/2001/04/xmldsig-more#sha384"
		"http://www.w3.org/2001/04/xmlenc#sha512"
		*/

		public static readonly DigestMethod MD_5=new DigestMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "md5", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.MD5);
		public static readonly DigestMethod RIPEMD_160=new DigestMethod("http://www.w3.org/2001/04/xmlenc#ripemd160", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.RIPEMD160);
		public static readonly DigestMethod SHA_1=new DigestMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG + "sha1", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.SHA1);
		public static readonly DigestMethod SHA_224=new DigestMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "sha224", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.SHA224);
		public static readonly DigestMethod SHA_256=new DigestMethod("http://www.w3.org/2001/04/xmlenc#sha256", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.SHA256);
		public static readonly DigestMethod SHA_384=new DigestMethod(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG_MORE + "sha384", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.SHA384);
		public static readonly DigestMethod SHA_512=new DigestMethod("http://www.w3.org/2001/04/xmlenc#sha512", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.SHA512);

         public static IEnumerable<DigestMethod> Values
        {
            get
            {
                yield return MD_5;
                yield return RIPEMD_160;
                yield return SHA_1;
                yield return SHA_224;
                yield return SHA_256;
                yield return SHA_384;                
                yield return SHA_512;                
            }
        }

	    public DigestAlg Algorithm
	    {
	        get { return mAlgorithm; }
	    }

	    public string Url
	    {
	        get { return mUrl; }
	    }


	    private readonly string mUrl;
		private readonly tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg mAlgorithm;

		DigestMethod(String aUrl, tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg aAlgorithm)
	    {
	        this.mUrl = aUrl;
			this.mAlgorithm = aAlgorithm;
	    }

		public static DigestMethod resolve(String aUrl)
		{
			if (aUrl!=null)
			{
				foreach (DigestMethod alg in Values)
				{
					if (alg.Url.Equals(aUrl))
						return alg;
				}
			}
			throw new UnknownAlgorithmException(null, "unknown.algorithm",aUrl);
		}


		public static DigestMethod resolveFromName(tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg aAlg)
		{
			if (aAlg!=null)
			{
				foreach (DigestMethod alg in Values)
				{
					if (alg.Algorithm.Equals(aAlg))
						return alg;
				}
			}
			throw new UnknownAlgorithmException(null, "unknown.algorithm",aAlg.getName());
		}
	}

}