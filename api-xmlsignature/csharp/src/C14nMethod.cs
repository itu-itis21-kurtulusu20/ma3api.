namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{
    using System;
    using System.Collections.Generic;

	/// <summary>
	/// The <code>C14nMethod</code> (CanonicalizationMethod) is the algorithm that is
	/// used to canonicalize the <code>SignedInfo</code> element before it is
	/// digested as  part of the signature operation.
	/// 
	/// @author ahmety
	/// date: Apr 20, 2009
	/// </summary>
	public class C14nMethod
	{

		/// <summary>
		/// The <a href="http://www.w3.org/TR/2001/REC-xml-c14n-20010315">Canonical
		/// XML (without comments)</a> canonicalization method algorithm URI.
		/// </summary>
		public static readonly C14nMethod INCLUSIVE=new C14nMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");

		/// <summary>
		/// The <a href="http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments">
		/// Canonical XML with comments</a> canonicalization method algorithm URI.
		/// </summary>
        public static readonly C14nMethod INCLUSIVE_WITH_COMMENTS=new C14nMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments");

		/// <summary>
		/// The <a href="http://www.w3.org/2001/10/xml-exc-c14n#">Exclusive
		/// Canonical XML (without comments)</a> canonicalization method algorithm
		/// URI.
		/// </summary>
		public static readonly C14nMethod EXCLUSIVE=new C14nMethod("http://www.w3.org/2001/10/xml-exc-c14n#");
		/// <summary>
		/// The <a href="http://www.w3.org/2001/10/xml-exc-c14n#WithComments">
		/// Exclusive Canonical XML with comments</a> canonicalization method
		/// algorithm URI.
		/// </summary>
		public static readonly C14nMethod EXCLUSIVE_WITH_COMMENTS=new C14nMethod("http://www.w3.org/2001/10/xml-exc-c14n#WithComments");

		public static readonly C14nMethod V1_1=new C14nMethod("http://www.w3.org/2006/12/xml-c14n11");
		public static readonly C14nMethod V1_1_WITH_COMMENTS=new C14nMethod("http://www.w3.org/2006/12/xml-c14n11#WithComments");

        public static IEnumerable<C14nMethod> Values
        {
            get
            {
                yield return INCLUSIVE;
                yield return INCLUSIVE_WITH_COMMENTS;
                yield return EXCLUSIVE;
                yield return EXCLUSIVE_WITH_COMMENTS;
                yield return V1_1;
                yield return V1_1_WITH_COMMENTS;                
            }
        }

	    public string URL
	    {
	        get { return mURL; }
	    }

	    private readonly string mURL;

		C14nMethod(String aURL)
    	{
			this.mURL = aURL;
		}

	    public static C14nMethod resolve(String aURL)
		{
	    	if (aURL!=null)
			{
				foreach(C14nMethod alg in Values)
				{
					if (alg.URL.Equals(aURL))
						return alg;
				}
			}
			throw new UnknownAlgorithmException(null, "unknown.algorithm",aURL);
		}

		public static bool isSupported(String aURL)
		{
			if (aURL!=null)
			{
				foreach (C14nMethod alg in Values)
				{
					if (alg.URL.Equals(aURL))
						return true;
				}
			}
			return false;
        }
	}	
}