
using System;
using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper
{
    using DigestAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
    public class DigestAlgorithm
    {
        public static readonly DigestAlgorithm MD_5 = new DigestAlgorithm("http://www.w3.org/2001/04/xmldsig-more#md5", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.MD5);
        public static readonly DigestAlgorithm RIPEMD_160 = new DigestAlgorithm("http://www.w3.org/2001/04/xmlenc#ripemd160", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.RIPEMD160);
        public static readonly DigestAlgorithm SHA_1 = new DigestAlgorithm("http://www.w3.org/2000/09/xmldsig#sha1", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.SHA1);
        public static readonly DigestAlgorithm SHA_224 = new DigestAlgorithm("http://www.w3.org/2001/04/xmldsig-more#sha224", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.SHA224);
        public static readonly DigestAlgorithm SHA_256 = new DigestAlgorithm("http://www.w3.org/2001/04/xmlenc#sha256", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.SHA256);
        public static readonly DigestAlgorithm SHA_384 = new DigestAlgorithm("http://www.w3.org/2001/04/xmldsig-more#sha384", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.SHA384);
        public static readonly DigestAlgorithm SHA_512 = new DigestAlgorithm("http://www.w3.org/2001/04/xmlenc#sha512", tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg.SHA512);

        public static IEnumerable<DigestAlgorithm> Values
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

        DigestAlgorithm(String aUrl, tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg aAlgorithm)
	    {
	        this.mUrl = aUrl;
			this.mAlgorithm = aAlgorithm;
	    }

		public static DigestAlg resolve(String aUrl)
		{
			if (aUrl!=null)
			{
				foreach (DigestAlgorithm alg in Values)
				{
					if (alg.Url.Equals(aUrl))
                        return alg.Algorithm;
				}
			}
		    return null;
		}
    }
}
